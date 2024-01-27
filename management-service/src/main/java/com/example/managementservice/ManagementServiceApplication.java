package com.example.managementservice;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
@OpenAPIDefinition(
        info = @Info(
                title = "Asset Management Service Rest APIs ",
                description = "service quản lý các chức năng CRUD sản phẩm ",
                version = "v1.0",
                contact = @Contact(
                        email = "dinhtruong1234lhp@gmail.com"
                )
        ),
        externalDocs = @ExternalDocumentation(
                description = "no"
        )
)
@SpringBootApplication
@EnableDiscoveryClient
public class ManagementServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(ManagementServiceApplication.class, args);
    }

}
