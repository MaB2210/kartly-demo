package kartly_demo.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {
    public OpenAPI karlyOpenApi(){
        return new OpenAPI()
                .info(new Info()
                        .title("Kartly API")
                        .description("REST API for the Karly e-commerce order management platform")
                        .version("v1.0"));
    }
}
