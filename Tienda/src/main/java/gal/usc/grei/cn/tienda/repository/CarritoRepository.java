package gal.usc.grei.cn.tienda.repository;

import gal.usc.grei.cn.tienda.model.Carrito;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CarritoRepository extends MongoRepository<Carrito, String> {
}
