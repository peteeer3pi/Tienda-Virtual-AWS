package gal.usc.grei.cn.tienda.service;

import gal.usc.grei.cn.tienda.model.*;
import gal.usc.grei.cn.tienda.repository.CompraRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Service
public class CompraService {
    private final CarritoService carritos;
    private final CompraRepository compras;
    private final UsuarioService usuarios;

    @Autowired
    public CompraService(CarritoService carritos, CompraRepository compras, UsuarioService usuarios) {
        this.carritos = carritos;
        this.compras = compras;
        this.usuarios = usuarios;
    }

    public Optional<Page<Compra>> get(int page, int size) {
        Pageable request = PageRequest.of(page, size);
        Page<Compra> result = compras.findAll(request);
        if (result.isEmpty()) {
            return Optional.empty();
        }
        else{
            return Optional.of(result);
        }
    }
    public Optional<Compra> get(String id){
        return compras.findById(id);
    }


    public Compra realizarCompra(Compra compra){
        Carrito c = carritos.get(compra.getCarrito().getId());
        Optional<Usuario> usuarioOptional = usuarios.get(compra.getUsuario().getEmail());
        if(!usuarioOptional.isPresent())
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No se ha encontrado el usuario\n");
        compra.setCarrito(c);
        compra.setUsuario(usuarioOptional.get());
        carritos.delete(c);
        //Insetamos la compra en la BD
        return compras.insert(compra);

    }

    public boolean deleteCompra(String id){
        if(compras.findById(id).isPresent()){
            compras.deleteById(id);
            return true;
        }else {
            return false;
        }
    }

}