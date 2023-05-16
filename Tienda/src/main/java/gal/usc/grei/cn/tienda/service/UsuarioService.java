package gal.usc.grei.cn.tienda.service;

import com.github.fge.jsonpatch.JsonPatchException;
import gal.usc.grei.cn.tienda.model.Usuario;
import gal.usc.grei.cn.tienda.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class UsuarioService {
    private final UsuarioRepository usuarios;
    private final PatchUtils mapper;

    @Autowired
    public UsuarioService(UsuarioRepository usuarios, PatchUtils mapper) {
        this.usuarios = usuarios;
        this.mapper = mapper;
    }

    public Optional<Page<Usuario>> get(int page, int size) {
        Pageable request = PageRequest.of(page, size);
        Page<Usuario> result = usuarios.findAll(request);
        if (result.isEmpty()) {
            return Optional.empty();
        }
        else{
            return Optional.of(result);
        }
    }
    public Optional<Usuario> get(String id){
        return usuarios.findById(id);
    }

    public Optional<Usuario> addUsuario(Usuario usuario){
        if(usuarios.findById(usuario.getEmail()).isPresent()){
            return Optional.empty();
        }
        usuarios.insert(usuario);
        return Optional.of(usuario);
    }

    public boolean deleteUsuario(String id){
        if(usuarios.findById(id).isPresent()){
            usuarios.deleteById(id);
            return true;
        }else {
            return false;
        }
    }

    public Optional<Usuario> updateUsuario(String id, List<Map<String, Object>> patch) throws JsonPatchException {
        Optional<Usuario> principal = usuarios.findById(id);
        if(principal.isPresent()) {
            Usuario updated = mapper.patch(principal.get(), patch);
            updated.setEmail(id); //No dejamos modificar el id
            usuarios.save(updated);
            return Optional.of(updated);
        }else{
            return Optional.empty();
        }
    }
}
