package gal.usc.grei.cn.tienda.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.*;
import java.util.*;
import java.math.BigDecimal;


@Document(collection = "compras")
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(
        name = "Compra",
        description = "Representación de una compra"
)
public class Compra {
    @Id
    @Null(message = "El id debe ser nulo")
    @Schema(hidden = true)
    private String id;
    @NotNull
    private Carrito carrito;
    @NotNull
    private Usuario usuario;
    @NotNull
    private Tarjeta tarjeta;


    public Compra(String id, Carrito carrito, Tarjeta tarjeta, Usuario usuario) {
        this.id = id;
        this.carrito = carrito;
        this.tarjeta = tarjeta;
        this.usuario = usuario;
    }

    public String getId() {
        return id;
    }

    public Compra setId(String id) {
        this.id = id;
        return this;
    }


    public Carrito getCarrito() {
        return carrito;
    }

    public void setCarrito(Carrito carrito) {
        this.carrito = carrito;
    }




    public Tarjeta getTarjeta() {
        return tarjeta;
    }

    public void setTarjeta(Tarjeta tarjeta) {
        this.tarjeta = tarjeta;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    @Override
    public String toString() {
        return "Compra{" +
                "id='" + id + '\'' +
                ", carrito=" + carrito +
                ", usuario=" + usuario +
                ", tarjeta=" + tarjeta +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Compra compra = (Compra) o;
        return Objects.equals(id, compra.id) && Objects.equals(carrito, compra.carrito) && Objects.equals(usuario, compra.usuario) && Objects.equals(tarjeta, compra.tarjeta);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, carrito, usuario, tarjeta);
    }
}