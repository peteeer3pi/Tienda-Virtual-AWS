package gal.usc.grei.cn.tienda.controller;

import gal.usc.grei.cn.tienda.model.Carrito;
import gal.usc.grei.cn.tienda.service.CarritoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("carritos")
@Tag(name = "Carrito API", description = "Operaciones relacionadas con el carrito")
public class CarritoController{

    private final CarritoService carritos;

    @Autowired
    public CarritoController(CarritoService carritos){
        this.carritos = carritos;
    }

    @GetMapping(
            produces = MediaType.APPLICATION_JSON_VALUE,
            path = "{id}"
    )
    @Operation(
            operationId = "getAllProductosInCarrito",
            summary = "Se obtienen todos los productos del carrito"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Los detalles del carrito",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = Carrito.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Bad request",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "No se ha encontrado el carrito",
                    content = @Content
            )
    })
    ResponseEntity<Carrito> get(
            @PathVariable("id") String id
    ){
        Carrito c = carritos.get(id);
        return ResponseEntity.ok().body(c);
    }

    @PostMapping(
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @Operation(
            operationId = "createCarrito",
            summary = "Inicializa el carrito"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "Los detalles del carrito",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = Carrito.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Bad request",
                    content = @Content
            )
    })
    ResponseEntity<Carrito> post(@RequestBody @Valid Carrito c) {
        Carrito ca = carritos.post(c);
        return ResponseEntity.created(null).body(ca);
    }

    @PatchMapping(
            path = "{idCarrito}/productos/{idProducto}",
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = "application/json-patch+json"
    )
    @Operation(
            operationId = "modifyAmountDisks",
            summary = "Se modifica la cantidad de un disco en el carrito"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Los detalles del carrito",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = Carrito.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Bad request",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "No se ha encontrado el carrito",
                    content = @Content
            )
    })
    ResponseEntity<?> patchProducto(@io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Patch operation",
            content = @Content(
                    mediaType = "application/json-patch+json",
                    examples = @ExampleObject(
                            value = "[{\"op\":\"replace\", \"path\":\"/cantidad\", \"value\": \"10\"}]"
                    )
            )
    )@RequestBody List<Map<String,Object>> patch, @PathVariable("idCarrito") String id, @PathVariable("idProducto") String idProducto){
        String op;
        if(!patch.get(0).containsKey("op") || !patch.get(0).containsKey("path") || !patch.get(0).containsKey("value"))
            return ResponseEntity.badRequest().body("Formato de instrucción Json Patch invalido");
        op  = patch.get(0).get("op").toString();
        String path = patch.get(0).get("path").toString();
        //Comprobamos que el usuario haya introducido un campo que se pueda modificar
        if(!path.startsWith("/cantidad"))
            return ResponseEntity.badRequest().body("Path incorrecto");
        else if(!op.equals("replace"))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Operacion invalida");
        String value = patch.get(0).get("value").toString();
        return ResponseEntity.ok().body(carritos.patchProducto(id,idProducto,value));
    }

    @PatchMapping(
            path = "{idCarrito}",
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = "application/json-patch+json"
    )
    @Operation(
            operationId = "addOrRemoveProductoFromCarrito",
            summary = "Se añade o elimina un producto al carrito (add o remove)"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Los detalles del carrito",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = Carrito.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Bad request",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "No se ha encontrado el carrito",
                    content = @Content
            )
    })
    ResponseEntity<?> patch(@io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Patch operation",
            content = @Content(
                    mediaType = "application/json-patch+json",
                    examples = @ExampleObject(
                            value = "[{\"op\":\"add\", \"path\":\"/contenido\", \"value\": \"11223344\"}]"
                    )
            )
    )@RequestBody List<Map<String,Object>> patch, @PathVariable("idCarrito") String id){
        String op;
        if(!patch.get(0).containsKey("op") || !patch.get(0).containsKey("path") || !patch.get(0).containsKey("value"))
            return ResponseEntity.badRequest().body("Formato de instrucción Json Patch invalido");
        op  = patch.get(0).get("op").toString();
        String path = patch.get(0).get("path").toString();
        //Comprobamos que el usuario haya introducido un campo que se pueda modificar
        if(!path.startsWith("/contenido")  && !path.startsWith("/comprado"))
            return ResponseEntity.badRequest().body("Path incorrecto");
        else if(!op.equals("remove") && !op.equals("add") && !op.equals("replace"))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Operacion invalida");
        String value = patch.get(0).get("value").toString();
        if(path.startsWith("/comprado") && (!op.equals("replace") || !value.equals("true")))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Operacion invalida");
        if(op.equals("add"))
            return ResponseEntity.ok().body(carritos.patchAdd(id,value));
        else
            return ResponseEntity.ok().body(carritos.patchRemove(id,patch));
    }
}

