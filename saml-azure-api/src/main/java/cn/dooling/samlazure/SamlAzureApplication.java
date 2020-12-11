package cn.dooling.samlazure;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.jdbc.metadata.DataSourcePoolMetadataProvider;

@SpringBootApplication
public class SamlAzureApplication {
    public static void main(String[] args) {
        SpringApplication.run(SamlAzureApplication.class, args);
    }
}
