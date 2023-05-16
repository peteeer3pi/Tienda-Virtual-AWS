package gal.usc.grei.cn.tienda.repository;

import gal.usc.grei.cn.tienda.model.Compra;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CompraRepository extends MongoRepository<Compra, String> {
}
