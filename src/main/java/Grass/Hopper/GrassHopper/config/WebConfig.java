package Grass.Hopper.GrassHopper.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebMvc
public class WebConfig implements WebMvcConfigurer {

    @Override
public void addCorsMappings(CorsRegistry registry) {
    registry.addMapping("/**")               // Se aplica a todas las rutas
            .allowedOriginPatterns("*")      // Permite solicitudes desde cualquier origen
            .allowedMethods("*")              // Permite todos los métodos HTTP
            .maxAge(3600L)                        // Cache de opciones preflight en segundos (1 hora)
            .allowedHeaders("*")              // Permite todos los encabezados en las solicitudes
            .exposedHeaders("Authorization")  // Expone el encabezado 'Authorization' en las respuestas
            .allowCredentials(true);    // Permite el envío de credenciales (por ejemplo, cookies)
}


    @Override
    //Mapea rutas a recursos estáticos
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("swagger-ui.html")
                .addResourceLocations("classpath:/META-INF/resources/");

        registry.addResourceHandler("/webjars/**")
                .addResourceLocations("classpath:/META-INF/resources/webjars/");
    }
}
