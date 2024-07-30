# Forras-imageserver 

With this Spring Boot project you can serve [DocuStorePictureApp](https://github.com/pzoli/DocuStorePictureApp) RestAPI requests for storing document images and informations.

## Server required

- Java 17 JDK
- PostgreSQL server

Before run the program, you must copy src/main/resources/example-application.properties to src/main/resources/application.properties and update the application settings.
(Database access properties and file upload temporal path, ssl certificate properties and Basic auth username/password)

## generate ssl keystore

Use the following to generate a self-signed certificate
```
keytool -genkeypair -alias baeldung -keyalg RSA -keysize 2048 -storetype PKCS12 -keystore baeldung.p12 -validity 3650
```
## Swagger-UI

You can access Swagger-UI after start project
```
https://[host]:8088/swagger-ui/index.html
```