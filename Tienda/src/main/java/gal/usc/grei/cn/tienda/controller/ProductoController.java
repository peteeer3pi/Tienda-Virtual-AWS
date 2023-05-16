package gal.usc.grei.cn.tienda.controller;

import gal.usc.grei.cn.tienda.model.Producto;
import gal.usc.grei.cn.tienda.service.ProductoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@Tag(name = "Product API", description = "Operaciones relacionadas con el producto")
@RequestMapping("/productos")
public class ProductoController {
    private final ProductoService productos;

    @Autowired
    public ProductoController(ProductoService productos) {
        this.productos = productos;
    }

    @GetMapping(
            path = "",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @Operation(
            operationId = "getAllProducts",
            summary = "Obtener todos los productos"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Todos los detalles de los productos",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = Producto.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Productos no encontrados",
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
            Optional<Page<Producto>> result = productos.get(page, size);
            if(result.isPresent()) {
                //Page<Producto> data = result.get();
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
            operationId = "getOneProduct",
            summary = "Obtener un producto en concreto",
            description = "Obtener todos los detalles del producto seleccionado. "
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Los detalles del producto",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = Producto.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Producto no encontrado",
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
            Optional<Producto> producto = productos.get(id);
            if (producto.isPresent()) {
                return ResponseEntity.ok()
                        .body(producto.get());
            } else {
                return ResponseEntity.notFound().build();
            }
        }catch(Exception e){return new ResponseEntity<>("INTERNAL SERVER ERROR", HttpStatus.INTERNAL_SERVER_ERROR);}

    }

    @PostMapping(
            path = "",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @Operation(
            operationId = "create a product",
            summary = "Crear un producto con su informacion",
            description = "Dar los detalles para la creación de un nuevo producto."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "Producto creado satisfactoriamente",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = Producto.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Bad Request",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "Conflict",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Internal Server Error",
                    content = @Content
            ),
    })
    ResponseEntity<?> addProducto(@RequestBody @Valid Producto producto) {
        try {
            Optional<Producto> result = productos.addProducto(producto);
            if(result.isEmpty()){
                return new ResponseEntity<>("CONFLICT: EL ID DEL PRODUCTO YA ESTA EN USO", HttpStatus.CONFLICT);
            }
        }catch(Exception e){return new ResponseEntity<>("INTERNAL SERVER ERROR", HttpStatus.INTERNAL_SERVER_ERROR);}
        return ResponseEntity.created(URI.create("/productos/" + producto.getId()))
                .body(producto);
    }

    @DeleteMapping(
            path = "/{id}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @Operation(
            operationId = "deleteOneProduct",
            summary = "Borrar un único producto",
            description = "Borrar un producto utilizando su id"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "204",
                    description = "Producto eliminado satisfactoriamente"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Producto no encontrado",
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
    ResponseEntity<?> deleteProducto(@PathVariable("id") String id) {
        try {
            if(id.isEmpty()){
                return ResponseEntity.badRequest().body("BAD REQUEST: DEBES DE INDICAR EL ID DEL PRODUCTO");
            }
            if(!productos.deleteProducto(id)){
                return ResponseEntity.notFound().build();
            }
        }catch(Exception e){return new ResponseEntity<>("INTERNAL SERVER ERROR", HttpStatus.INTERNAL_SERVER_ERROR);}
        return ResponseEntity.noContent().build();
    }

    @PatchMapping(
            path = "/{id}",
            produces = "application/json-patch+json"

    )
    @Operation(
            operationId = "updateProduct",
            summary = "Actualizar un producto",
            description = "Actualizar la información de un producto." +
                    "No se puede modificar el id"
    )
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Update product",
            content = @Content(
                    mediaType = "application/json",
                    examples = @ExampleObject(
                            value = "[{\"op\": \"replace\", \"path\": \"/autor\", \"value\": \"Pedro\"}]"
                    )
            )
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Producto actualizado satisfactoriamente",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = Producto.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Producto no encontrado",
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
    ResponseEntity<?> update(@PathVariable("id") String id, @RequestBody List<Map<String, Object>> patch) {
        Optional<Producto> result;
        try{
            if(id.isEmpty()){
                return ResponseEntity.badRequest().body("BAD REQUEST: DEBES DE INDICAR EL ID DEL PRODUCTO");
            }
            result = productos.updateProducto(id, patch);
            if(result.isEmpty()){
                return ResponseEntity.notFound().build();
            }
        }catch(Exception e){return new ResponseEntity<>("INTERNAL SERVER ERROR", HttpStatus.INTERNAL_SERVER_ERROR);}
        return ResponseEntity.ok()
                .body(result.get());
    }

}

