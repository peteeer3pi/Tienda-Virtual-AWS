package gal.usc.grei.cn.tienda.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import javax.validation.*;
import javax.validation.constraints.AssertFalse;
import javax.validation.constraints.Max;
import javax.validation.constraints.Null;
import javax.validation.constraints.PositiveOrZero;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.*;
import java.math.BigDecimal;


@Document(collection = "carritos")
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(
        name = "Carrito",
        description = "Representación de un carrito"
)
public class Carrito {
    @Id
    @Null(message = "El id debe ser nulo")
    @Schema(hidden = true)
    private String id;
    @Null(message = "El carrito debe estar vacio")
    @Schema(hidden = true)
    private List<Producto> contenido;
    @Schema(required = true, defaultValue = "0.00" ,minimum = "0.00",example = "0.00")
    @PositiveOrZero(message = "El importe debe ser un numero positivo")
    @Max(value = 0, message = "El importe inicial debe ser cero")
    private BigDecimal importeTotal;

    public Carrito(String id, List<Producto> contenido, BigDecimal importeTotal) {
        this.id = id;
        this.contenido = contenido;
        this.importeTotal = importeTotal;
    }

    public String getId() {
        return id;
    }

    public Carrito setId(String id) {
        this.id = id;
        return this;
    }

    public List<Producto> getContenido() {
        return contenido;
    }

    public Carrito setContenido(List<Producto> contenido) {
        this.contenido = contenido;
        return this;
    }

    public BigDecimal getImporteTotal(){
        return this.importeTotal;
    }

    public Carrito setImporteTotal(BigDecimal importeTotal){
        this.importeTotal=importeTotal;
        return this;
    }

    @Override
    public String toString() {
        return "Carrito{" +
                "id='" + id + '\'' +
                ", contenido=" + contenido +
                ", importeTotal=" + importeTotal +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Carrito carrito = (Carrito) o;
        return Objects.equals(id, carrito.id) && Objects.equals(contenido, carrito.contenido) && Objects.equals(importeTotal, carrito.importeTotal);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, contenido, importeTotal);
    }
}