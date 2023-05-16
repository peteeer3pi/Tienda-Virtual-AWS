package gal.usc.grei.cn.tienda.repository;

import gal.usc.grei.cn.tienda.model.Producto;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductoRepository extends MongoRepository<Producto, String> {

}
