package gal.usc.grei.cn.tienda.repository;

import gal.usc.grei.cn.tienda.model.Usuario;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UsuarioRepository extends MongoRepository<Usuario, String> {
}
