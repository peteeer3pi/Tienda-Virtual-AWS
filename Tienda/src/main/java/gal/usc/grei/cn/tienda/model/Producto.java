package gal.usc.grei.cn.tienda.model;

import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import java.math.BigDecimal;
import java.util.Objects;


@Document(collection = "productos")

@Schema(
        name = "Product",
        description = "A complete product representation"
)
public class Producto {
    @Id
    @Null
    @Schema(hidden = true)
    private String id;
    @NotNull
    @Schema(required = true, example = "Marvin Gaye")
    private String author;
    @NotNull
    @Schema(required = true, example = "What's Going On")
    private String album;

    @NotNull
    @Schema(required = true, example = "EEUU")
    private String country;
    @NotNull
    @Schema(required = true, defaultValue = "0.00" ,minimum = "0.00",example = "25.00")
    private BigDecimal price;
    @Null
    @Schema(hidden = true)
    private Integer cantidad;

    public Producto() {

    }

    public String getId() {
        return id;
    }

    public Producto setId(String id) {
        this.id = id;
        return this;
    }

    public String getAuthor() {
        return author;
    }

    public Producto setAuthor(String author) {
        this.author = author;
        return this;
    }

    public String getAlbum() {
        return album;
    }

    public Producto setAlbum(String album) {
        this.album = album;
        return this;
    }

    public Integer getCantidad() {
        return cantidad;
    }

    public Producto setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
        return this;
    }

    public String getCountry() {
        return country;
    }

    public Producto setCountry(String country) {
        this.country = country;
        return this;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public Producto setPrice(BigDecimal price) {
        this.price = price;
        return this;
    }


    @Override
    public String toString() {
        return "Producto{" +
                "id='" + id + '\'' +
                ", author='" + author + '\'' +
                ", album='" + album + '\'' +
                ", country='" + country + '\'' +
                ", price=" + price +
                ", cantidad=" + cantidad +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Producto producto = (Producto) o;
        return id.equals(producto.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
