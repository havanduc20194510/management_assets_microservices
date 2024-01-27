package com.example.assetloanservice;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.web.reactive.function.client.WebClient;

@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
@OpenAPIDefinition(
        info = @Info(
                title = "AssetsLoansn Service Rest APIs",
                description = "service thực hiện quản lý các tác vụ mượn trả sản phẩm",
                version = "v1.0",
                contact = @Contact(
                        email = "dinhtruong1234lhp@gmail.com"
                )
        ),
        externalDocs = @ExternalDocumentation(
                description = "no"
        )
)
public class AssetLoanServiceApplication {

    @Bean
    public WebClient webClient() {
        return WebClient.builder().build();
    }

    public static void main(String[] args) {
        SpringApplication.run(AssetLoanServiceApplication.class, args);
    }

}
