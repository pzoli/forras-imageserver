function get(name){
    if(name=(new RegExp('[?&]'+encodeURIComponent(name)+'=([^&]*)')).exec(location.search))
        return decodeURIComponent(name[1]);
}

const wsUri = 'ws://localhost:8082';
var
    websocket,
    image, data,
    docinfo, cropper, canvas, ctx;

function init() {
    websocket = new WebSocket(wsUri);
    docinfo = get('docinfo');
    console.log(docinfo);

    image = new Image();
    canvas = document.querySelector('#canvas')
    ctx = canvas.getContext("2d");

    image.addEventListener("load", (e) => {
        canvas.width = image.width;
        canvas.height = image.height;
        ctx.drawImage(image, 0, 0);
        cropper = new Cropper(canvas, {
            modal: false,
            ready()
            {
                this.cropper.moveTo(0, 0);
                const canvasData = this.cropper.getCanvasData()
                this.cropper.setCropBoxData({left:0,top:0,width:canvasData.width,height:canvasData.height})
            }
        });
    });

    $('#scanbutton').on('click', scanButtonOnClick);
    $('#sendbutton').on('click', sendButtonOnClick);
    $('#cropbutton').on('click', cropButtonOnClick);
    $('#restorebutton').on('click', restoreButtonClick);
    $('#scandialogbutton').on('click', scandialogbuttonOnClick);
    $('#zoomOutButton').on('click', zoomOutButtonOnClick)
    $('#zoomInButton').on('click', zoomInButtonOnClick)
    resolutionSelect = document.querySelector('#resolution');
    colorSelect = document.querySelector('#color_mode');

    websocket.onopen = function (e) {
        writeToScreen('CONNECTED');
        doSend(JSON.stringify({action:'deviceinfo'}));
    };

    websocket.onclose = function (e) {
        writeToScreen('DISCONNECTED');
    };

    websocket.onmessage = function (e) {
        if (e.data instanceof Blob) {
            var reader = new FileReader();
            reader.addEventListener('loadend', () => {
                if (cropper) {
                    cropper.destroy();
                }
                data = reader.result;
                image.src = data;
                $('#spinner').hide();
                $('#scanbutton').prop( "disabled", false );
                $('#dialog').modal('toggle');
            });
            reader.readAsDataURL(e.data);
        } else {
            try{
                const response = JSON.parse(e.data)
                if (response.return == 'deviceinfo') {
                    $("#asset").empty();
                    const assetSelect = document.getElementById('asset');
                    for(var device in response.devices) {
                        assetSelect.add(new Option(response.devices[device].assetName, response.devices[device].assetId));
                    }
                } else if (response.return = "error") {
                    alert(response.message);
                }
            } catch (e) {
                alert(e.data)
            }
            $('#spinner').hide();
            $('#scanbutton').prop( "disabled", false );
        }
    };

    websocket.onerror = function (e) {
        writeToScreen('<span class=error>ERROR:</span> ' + e.data);
    };
}
function doSend(message) {
    //writeToScreen('SENT: ' + message);
    websocket.send(message);
}

function writeToScreen(message) {
    $('<p>' + message + '</p>').prependTo($('#output'));
}

function scanButtonOnClick() {
    const assetId = $('#asset :selected').val();
    const resolution = $('#resolution').val();
    const color_mode = $('#color_mode').val();
    const action = {action:'scan',assetId:Number.parseInt(assetId),color_mode:Number.parseInt(color_mode),resolution:Number.parseInt(resolution)}
    doSend(JSON.stringify(action));
    $('#scanbutton').prop( "disabled", true );
    $('#spinner').show();
}

function scandialogbuttonOnClick() {
    //$('#dialog').modal();
}

function cropButtonOnClick() {
    if (cropper) {
        var croppedCanvas = cropper.getCroppedCanvas();
        croppedCanvas.setAttribute('id', 'canvas');
        canvas = croppedCanvas;
        ctx = canvas.getContext('2d');
        $("#canvaswrapper").html(croppedCanvas);
        cropper = new Cropper(canvas,{
            modal: false,
            ready()
            {
                this.cropper.moveTo(0, 0);
                const canvasData = this.cropper.getCanvasData()
                this.cropper.setCropBoxData({left:0,top:0,width:canvasData.width,height:canvasData.height})
            }
        });
    }
}

function restoreButtonClick() {
    if (cropper) {
        cropper.destroy();
        image.src = data;
        canvas.width = image.width;
        canvas.height = image.height;
    }
}

function sendButtonOnClick() {
    if (data) {
        canvas.toBlob((blob) => {
            sendImageAsPostRequest(
                blob,
                'image/png',
                '/api/file/uploadimage',
            );
        });
    } else {
        alert('Nincs beolvasott elmenthető kép!')
    }
}

async function sendImageAsPostRequest(imageBlob, mimeType, url) {
    const formData = new FormData();
    formData.append('file',imageBlob,'scanned.png');
    formData.append('docid', JSON.parse(docinfo).id);
    formData.append('openPaint', $('#openPaintCheckBox').is(":checked"));
    try {
        const response = await fetch(url, {
            method: 'POST',
            body: formData,
        });

        if (response.ok) {
            console.log('Image uploaded successfully');
            if (confirm("Kép elküldve. Beolvas új képet?")) {
                cropper.destroy()
                ctx.clearRect(0, 0, canvas.width, canvas.height);
                data = null;
            }
        } else {
            console.error('Error uploading image:', response.status);
            alert("Hiba történt a kép feltöltése közben:" + response.status);
        }
    } catch (e) {
        console.log(e);
    }
}

function zoomOutButtonOnClick() {
    cropper.zoom(-0.1);
}

function zoomInButtonOnClick() {
    cropper.zoom(0.1);
}