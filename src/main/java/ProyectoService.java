import java.util.Collection;

public interface ProyectoService {

    public void addProyecto(Proyecto proyecto) throws ProyectoException;
    public Collection<Proyecto> getProyectos();
    public Proyecto getProyecto(Integer id) throws ProyectoException;
    public Proyecto editProyecto(Proyecto proyecto) throws ProyectoException;
    public void deleteProyecto(Integer id) throws ProyectoException;
}
