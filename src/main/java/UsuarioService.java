import java.util.Collection;

public interface UsuarioService {

    public void addUsuario(Usuario usuario) throws UsuarioException;
    public Collection<Usuario> getUsuarios();
    public Usuario getUsuario(Integer id);
    public Usuario editUsuario (Usuario usuario) throws UsuarioException;
    public void deleteUsuario(Integer id) throws UsuarioException;
    public boolean UsuarioExists(Integer id);
}
