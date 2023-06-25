package io.github.kongpf8848.azure

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@SpringBootApplication
class AzureOpenaiProxyApplication{

	@GetMapping("/")
	fun home(): String{
		return "server status is ok"
	}
}

fun main(args: Array<String>) {
	runApplication<AzureOpenaiProxyApplication>(*args)
}
