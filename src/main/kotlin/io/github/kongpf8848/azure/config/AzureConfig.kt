package io.github.kongpf8848.azure.config

import lombok.Getter
import lombok.Setter
import lombok.extern.slf4j.Slf4j
import lombok.ToString
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration
import org.springframework.stereotype.Component

@Configuration
@ConfigurationProperties("azure.openai")
@Component
@Slf4j
@Getter
@Setter
@ToString
class AzureConfig {
    var endpoint: String? = null
    var deployment_id: String? = null
    var api_version: String? = null
    var api_delay_ms: Long = 50L
}