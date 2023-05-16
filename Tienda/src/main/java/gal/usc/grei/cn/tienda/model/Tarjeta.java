package gal.usc.grei.cn.tienda.model;

import io.swagger.v3.oas.annotations.media.Schema;

import javax.validation.constraints.Pattern;

public class Tarjeta {
    @Schema(required = true,example = "1234123412341234")
    @Pattern(regexp = "[0-9]{16}")
    private String numero;
    @Schema(required = true, example = "01/22")
    @Pattern(regexp = "[0-12]/[22-99]")
    private String vigencia;
    @Schema(required = true, example = "123")
    @Pattern(regexp = "[0-9]{3}")
    private String cvv;

    public Tarjeta(String numero, String vigencia, String cvv) {
        this.numero = numero;
        this.vigencia = vigencia;
        this.cvv = cvv;
    }

    public String getNumero() {
        return numero;
    }

    public Tarjeta setNumero(String numero) {
        this.numero = numero;
        return this;
    }

    public String getVigencia() {
        return vigencia;
    }

    public Tarjeta setVigencia(String vigencia) {
        this.vigencia = vigencia;
        return this;
    }

    public String getCvv() {
        return cvv;
    }

    public Tarjeta setCvv(String cvv) {
        this.cvv = cvv;
        return this;
    }
}
