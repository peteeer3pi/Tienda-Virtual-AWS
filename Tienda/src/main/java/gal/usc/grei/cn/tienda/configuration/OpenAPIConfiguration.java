package gal.usc.grei.cn.tienda.configuration;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.servers.Server;

@OpenAPIDefinition(
        info = @Info(
                title = "TMDB clone REST API",
                description = "API de la Práctica 8: Tienda Online de Computación en la Nube",
                version = "1.0.0",
                contact = @Contact(
                        name = "Pedro Durán Altés, Agustín Gallego Gómez y Martín Rodríguez Camino",
                        email = "pedro.duran@rai.usc.es, agustin.gallego@rai.usc.es, martin.rodriguez.camino@rai.usc.es"
                ),

                license = @License(
                        name = "MIT Licence",
                        url = "https://opensource.org/licenses/MIT")),
        servers = {
                @Server(url = "/", description = "General use server"),
                @Server(url = "testing.tmdbclone.tv", description = "Testing server")
        }
)
public class OpenAPIConfiguration {
}


