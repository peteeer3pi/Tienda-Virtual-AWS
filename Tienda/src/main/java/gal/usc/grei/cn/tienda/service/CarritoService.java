package gal.usc.grei.cn.tienda.service;

import com.github.fge.jsonpatch.JsonPatchException;
import gal.usc.grei.cn.tienda.model.Carrito;
import gal.usc.grei.cn.tienda.model.Producto;
import gal.usc.grei.cn.tienda.repository.CarritoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class CarritoService {
    private final CarritoRepository carritos;
    private final ProductoService productos;
    private final PatchUtils pu;

    @Autowired
    public CarritoService(CarritoRepository carritos, ProductoService productos, PatchUtils pu){
        this.carritos = carritos;
        this.productos = productos;
        this.pu = pu;
    }

    public Carrito get(String id){
        Optional<Carrito> c = this.carritos.findById(id);
        if(!c.isPresent())
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No se ha encontrado el carrito");
        return c.get();
    }

    public Carrito post(Carrito c){
        c.setContenido(new ArrayList<>());
        return carritos.insert(c);
    }

    public Carrito patchAdd(String id, String idProducto){
        Optional<Carrito> cOptional = carritos.findById(id);
        Optional<Producto> pOptional = productos.get(idProducto);
        if(!cOptional.isPresent())
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No se ha encontrado el carrito\n");
        if(!pOptional.isPresent())
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No se ha encontrado el producto\n");
        Carrito c = cOptional.get();
        Producto p = pOptional.get();
        //Realizamos el patch para llevar a cabo la operacion especificada por el usuario (Modificar un campo)
        if(c.getContenido().contains(p))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El disco ya está en el carrito\n");
        p.setCantidad(1);
        c.getContenido().add(p);
        c.setImporteTotal(c.getImporteTotal().add(p.getPrice()));
        //Modificamos los datos del carrito en la BD
        return carritos.save(c);
    }

    public Carrito patchRemove(String id, List<Map<String,Object>> patch){
        Optional<Carrito> cOptional = carritos.findById(id);
        if(!cOptional.isPresent())
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No se ha encontrado el carrito\n");
        Carrito c = cOptional.get();
        //Realizamos el patch para llevar a cabo la operacion especificada por el usuario (Modificar un campo)
        try{
            int indice = Integer.valueOf(patch.get(0).get("path").toString().split("/")[2]);
            Producto p = c.getContenido().get(indice);
            c.setImporteTotal(c.getImporteTotal().subtract(p.getPrice().multiply(new BigDecimal(c.getContenido().get(c.getContenido().indexOf(p)).getCantidad()))));
        }catch(Exception e){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El indice proporcionado no es válido\n");
        }
        try{
            //Realizamos el patch para llevar a cabo la operacion especificada por el usuario (Modificar un campo)
            c = pu.patch(c, patch);
            //Modificamos los datos del usuario
            return carritos.save(c);
        }catch(JsonPatchException e){
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Se ha producido un error inesperado\n");
        }
    }

    public Carrito patchProducto(String id, String idProducto, String value){
        Optional<Carrito> cOptional = carritos.findById(id);
        if(!cOptional.isPresent())
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No se ha encontrado el carrito\n");
        Carrito c = cOptional.get();
        Producto p = null;
        for(Producto prod : c.getContenido()){
            if(prod.getId().equals(idProducto)) {
                p = prod;
                break;
            }
        }
        Integer cantidad = Integer.valueOf(value);
        Integer cantidadAnterior = p.getCantidad();
        if(cantidad < 0)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "La cantidad debe ser positiva\n");
        p.setCantidad(cantidad);
        //c.setImporteTotal(c.getImporteTotal()-cantidadAnterior*p.getPrice()+cantidad*p.getPrice());
        c.setImporteTotal(c.getImporteTotal().subtract(p.getPrice().multiply(new BigDecimal(cantidadAnterior))).add(p.getPrice().multiply(new BigDecimal(cantidad))));
        //Modificamos los datos del carrito en la BD
        return carritos.save(c);
    }

    public void delete(Carrito c){
        carritos.deleteById(c.getId());
    }
}
