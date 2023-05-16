package gal.usc.grei.cn.tienda.model;

import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import java.util.Objects;

@Document(collection = "usuarios")
public class Usuario {
    @Id
    @NotNull
    @Schema(required = true, example = "pedro.duran@rai.usc.es")
    @Email
    private String email;
    @NotNull
    @Schema(required = true, example = "Pedro")
    private String name;

    @Schema(example = "Spain")
    private String country;

    public Usuario(String email, String name, String country) {
        this.email = email;
        this.name = name;
        this.country = country;
    }

    public String getEmail() {
        return email;
    }

    public Usuario setEmail(String email) {
        this.email = email;
        return this;
    }

    public String getName() {
        return name;
    }

    public Usuario setName(String name) {
        this.name = name;
        return this;
    }

    public String getCountry() {
        return country;
    }

    public Usuario setCountry(String country) {
        this.country = country;
        return this;
    }

    @Override
    public String toString() {
        return "Usuario{" +
                "email='" + email + '\'' +
                ", name='" + name + '\'' +
                ", country='" + country + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Usuario usuario = (Usuario) o;
        return Objects.equals(email, usuario.email) && Objects.equals(name, usuario.name) && Objects.equals(country, usuario.country);
    }

    @Override
    public int hashCode() {
        return Objects.hash(email, name, country);
    }
}
