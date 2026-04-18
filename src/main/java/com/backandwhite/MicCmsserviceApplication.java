package com.backandwhite;

import com.backandwhite.common.configuration.annotation.EnableCoreApplication;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.boot.SpringApplication;

@EnableCoreApplication
@OpenAPIDefinition(servers = {@Server(url = "https://cms-service.up.railway.app", description = "Production Server."),
        @Server(url = "https://localhost:6006", description = "Local Server.")})
public class MicCmsserviceApplication {

    public static void main(String[] args) {
        SpringApplication.run(MicCmsserviceApplication.class, args);
    }
}
