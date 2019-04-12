import java.util.Collection;
import java.util.HashMap;

public class UsuarioServiceMapImpl implements UsuarioService {
    private HashMap<Integer, Usuario> usuarioMap;

    public UsuarioServiceMapImpl(){
        usuarioMap=new HashMap<Integer, Usuario>();
    }

    public void addUsuario(Usuario usuario) {
        usuarioMap.put(usuario.getId(),usuario);
    }

    public Collection<Usuario> getUsuarios() {
        return usuarioMap.values();
    }

    public Usuario getUsuario(Integer id) {
        return usuarioMap.get(id);
    }

    public Usuario editUsuario(Usuario usuario) throws UsuarioException {
        Usuario usuarioEditado=usuarioMap.get(usuario.getId());
        if(usuarioEditado != null){
            if (usuario.getNombre() != null) {
                usuarioEditado.setNombre(usuario.getNombre());
            }
            if (usuario.getApellido() != null) {
                usuarioEditado.setApellido(usuario.getApellido());
            }
            return usuarioEditado;
        }else {
            throw new UsuarioException("¡No existe el usuario a modificar!");
        }
    }

    public void deleteUsuario(Integer id) {
        usuarioMap.remove(id);
    }

    public boolean UsuarioExists(Integer id) {
        return false;
    }
}
