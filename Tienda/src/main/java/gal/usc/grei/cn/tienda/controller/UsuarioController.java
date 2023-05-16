package gal.usc.grei.cn.tienda.controller;

import gal.usc.grei.cn.tienda.model.Usuario;
import gal.usc.grei.cn.tienda.service.UsuarioService;
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
@Tag(name = "User API", description = "Operaciones relacionadas con el usuario")
@RequestMapping("/usuarios")
public class UsuarioController {
    private final UsuarioService usuarios;

    @Autowired
    public UsuarioController(UsuarioService usuarios) {
        this.usuarios = usuarios;
    }

    @GetMapping(
            path = "",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @Operation(
            operationId = "getAllUsers",
            summary = "Obtener todos los usuarios"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Todos los detalles de los usuarios",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = Usuario.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Usuarios no encontrados",
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
            Optional<Page<Usuario>> result = usuarios.get(page, size);
            if(result.isPresent()) {
                //Page<Usuario> data = result.get();
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
            operationId = "getOneUser",
            summary = "Obtener un usuario en concreto",
            description = "Obtener todos los detalles del usuario seleccionado. "
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Los detalles del usuario",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = Usuario.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Usuario no encontrado",
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
            Optional<Usuario> usuario = usuarios.get(id);
            if (usuario.isPresent()) {
                return ResponseEntity.ok()
                        .body(usuario.get());
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
            operationId = "create a user",
            summary = "Crear un usuario con su informacion",
            description = "Dar los detalles para la creación de un nuevo usuario."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "Usuario creado satisfactoriamente",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = Usuario.class)
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
    ResponseEntity<?> addUsuario(@RequestBody @Valid Usuario usuario) {
        try {
            Optional<Usuario> result = usuarios.addUsuario(usuario);
            if(result.isEmpty()){
                return new ResponseEntity<>("CONFLICT: EL ID DEL USUARIO YA ESTA EN USO", HttpStatus.CONFLICT);
            }
        }catch(Exception e){return new ResponseEntity<>("INTERNAL SERVER ERROR", HttpStatus.INTERNAL_SERVER_ERROR);}
        return ResponseEntity.created(URI.create("/usuarios/" + usuario.getEmail()))
                .body(usuario);
    }

    @DeleteMapping(
            path = "/{id}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @Operation(
            operationId = "deleteOneUser",
            summary = "Borrar un único usuario",
            description = "Borrar un usuario utilizando su id"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "204",
                    description = "Usuario eliminado satisfactoriamente"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Usuario no encontrado",
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
    ResponseEntity<?> deleteUsuario(@PathVariable("id") String id) {
        try {
            if(id.isEmpty()){
                return ResponseEntity.badRequest().body("BAD REQUEST: DEBES DE INDICAR EL ID DEL USUARIO");
            }
            if(!usuarios.deleteUsuario(id)){
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
            operationId = "updateUser",
            summary = "Actualizar un usuario",
            description = "Actualizar la información de un usuario." +
                    "No se puede modificar el id"
    )
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Update user",
            content = @Content(
                    mediaType = "application/json",
                    examples = @ExampleObject(
                            value = "[{\"op\": \"replace\", \"path\": \"/name\", \"value\": \"Pedro\"}]"
                    )
            )
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Usuario actualizado satisfactoriamente",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = Usuario.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Usuario no encontrado",
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
        Optional<Usuario> result;
        try{
            if(id.isEmpty()){
                return ResponseEntity.badRequest().body("BAD REQUEST: DEBES DE INDICAR EL ID DEL USUARIO");
            }
            result = usuarios.updateUsuario(id, patch);
            if(result.isEmpty()){
                return ResponseEntity.notFound().build();
            }
        }catch(Exception e){return new ResponseEntity<>("INTERNAL SERVER ERROR", HttpStatus.INTERNAL_SERVER_ERROR);}
        return ResponseEntity.ok()
                .body(result.get());
    }

}


