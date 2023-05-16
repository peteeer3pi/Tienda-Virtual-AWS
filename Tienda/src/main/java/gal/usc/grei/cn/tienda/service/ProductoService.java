package gal.usc.grei.cn.tienda.service;

import com.github.fge.jsonpatch.JsonPatchException;
import gal.usc.grei.cn.tienda.model.Producto;
import gal.usc.grei.cn.tienda.repository.ProductoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class ProductoService {
    private final ProductoRepository productos;
    private final PatchUtils mapper;

    @Autowired
    public ProductoService(ProductoRepository productos, PatchUtils mapper) {
        this.productos = productos;
        this.mapper = mapper;
    }

    public Optional<Page<Producto>> get(int page, int size) {
        Pageable request = PageRequest.of(page, size);
        Page<Producto> result = productos.findAll(request);
        if (result.isEmpty()) {
            return Optional.empty();
        }
        else{
            return Optional.of(result);
        }
    }
    public Optional<Producto> get(String id){
        return productos.findById(id);
    }

    public Optional<Producto> addProducto(Producto producto){
        productos.insert(producto);
        return Optional.of(producto);
    }

    public boolean deleteProducto(String id){
        if(productos.findById(id).isPresent()){
            productos.deleteById(id);
            return true;
        }else {
            return false;
        }
    }

    public Optional<Producto> updateProducto(String id, List<Map<String, Object>> patch) throws JsonPatchException {
        Optional<Producto> principal = productos.findById(id);
        if(principal.isPresent()) {
            Producto updated = mapper.patch(principal.get(), patch);
            updated.setId(id); //No dejamos modificar el id
            productos.save(updated);
            return Optional.of(updated);
        }else{
            return Optional.empty();
        }
    }


}
