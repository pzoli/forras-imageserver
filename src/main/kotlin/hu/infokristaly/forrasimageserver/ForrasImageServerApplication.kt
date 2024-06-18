package hu.infokristaly.forrasimageserver

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.boot.runApplication

@SpringBootApplication
@EntityScan(basePackages = ["hu.infokristaly.forrasimageserver.entity"])
class ForrasImageServerApplication

fun main(args: Array<String>) {
	runApplication<ForrasImageServerApplication>(*args)
}
