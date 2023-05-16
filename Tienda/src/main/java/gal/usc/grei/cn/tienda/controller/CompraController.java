package gal.usc.grei.cn.tienda.controller;

import gal.usc.grei.cn.tienda.model.Compra;
import gal.usc.grei.cn.tienda.service.CompraService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("compras")
@Tag(name = "Compra API", description = "Operaciones relacionadas con la compra")
public class CompraController {
    private final CompraService compras;

    @Autowired
    public CompraController(CompraService compras){
        this.compras = compras;
    }

    @GetMapping(
            path = "",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @Operation(
            operationId = "getAllCompras",
            summary = "Obtener todas las compras"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Todos los detalles de las compras",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ResponseEntity.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Compras no encontradas",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Bad Request",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Internal Server Error",
                    content = @Content
            ),
    })
    ResponseEntity<?> get(
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "20") int size
    ){
        try {
            Optional<Page<Compra>> result = compras.get(page, size);
            if(result.isPresent()) {
                //Page<Compra> data = result.get();
                return ResponseEntity.ok()
                        .body(result);
            }
        }catch(Exception e){return new ResponseEntity<>("INTERNAL SERVER ERROR", HttpStatus.INTERNAL_SERVER_ERROR);}
        return ResponseEntity.notFound().build();
    }

    @GetMapping(
            path = "/{id}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @Operation(
            operationId = "getOneCompra",
            summary = "Obtener una compra en concreto",
            description = "Obtener todos los detalles de la compra seleccionado. "
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Los detalles de la compra",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = Compra.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Compra no encontrada",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Bad Request",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Internal Server Error",
                    content = @Content
            ),
    })
    ResponseEntity<?> get(@PathVariable("id") String id) {
        try {
            Optional<Compra> compra = compras.get(id);
            if (compra.isPresent()) {
                return ResponseEntity.ok()
                        .body(compra.get());
            } else {
                return ResponseEntity.notFound().build();
            }
        }catch(Exception e){return new ResponseEntity<>("INTERNAL SERVER ERROR", HttpStatus.INTERNAL_SERVER_ERROR);}

    }


    @PostMapping(
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @Operation(
            operationId = "createCompra",
            summary = "Realiza la compra"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "Los detalles de la compra",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = Compra.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Bad request",
                    content = @Content
            )
    })
    ResponseEntity<Compra> post(@RequestBody @Valid Compra compra) {
        Compra c = compras.realizarCompra(compra);
        return ResponseEntity.created(null).body(c);
    }

    @DeleteMapping(
            path = "/{id}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @Operation(
            operationId = "deleteOneCompra",
            summary = "Borrar una única compra",
            description = "Borrar una compra utilizando su id"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "204",
                    description = "Compra eliminada satisfactoriamente"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Compra no encontrada",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Bad Request",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Internal Server Error",
                    content = @Content
            ),
    })
    ResponseEntity<?> deleteCompra(@PathVariable("id") String id) {
        try {
            if(id.isEmpty()){
                return ResponseEntity.badRequest().body("BAD REQUEST: DEBES DE INDICAR EL ID DE LA COMPRA");
            }
            if(!compras.deleteCompra(id)){
                return ResponseEntity.notFound().build();
            }
        }catch(Exception e){return new ResponseEntity<>("INTERNAL SERVER ERROR", HttpStatus.INTERNAL_SERVER_ERROR);}
        return ResponseEntity.noContent().build();
    }

}
