import java.util.Collection;
import java.util.HashMap;

public class ProyectoServiceMapImpl implements ProyectoService {

    private HashMap<Integer, Proyecto> proyectoMap;

    public ProyectoServiceMapImpl() {
        this.proyectoMap = new HashMap<Integer, Proyecto>();
    }

    @Override
    public void addProyecto(Proyecto proyecto) throws ProyectoException {
        if (proyectoMap.get(proyecto.getId()) != null){
            if (proyecto.getId() != proyectoMap.get(proyecto.getId()).getId())
                proyectoMap.put(proyecto.getId(), proyecto);
            else
                throw new ProyectoException();
        }else{
            proyectoMap.put(proyecto.getId(), proyecto);
        }
    }

    @Override
    public Collection<Proyecto> getProyectos() {
        return proyectoMap.values();
    }

    @Override
    public Proyecto getProyecto(Integer id) throws ProyectoException {
      if(proyectoMap.get(id) != null)
          return proyectoMap.get(id);
      else
          throw new ProyectoException();
    }

    @Override
    public Proyecto editProyecto(Proyecto proyecto) throws ProyectoException {
        Proyecto proyectoEditado=proyectoMap.get(proyecto.getId());
        if(proyectoEditado != null){
            if (proyecto.getPropietario() != null) {
                proyectoEditado.setPropietario(proyecto.getPropietario());
            }
            if (proyecto.getTitulo() != null) {
                proyectoEditado.setTitulo(proyecto.getTitulo());
            }
            return proyectoEditado;
        }else {
            throw new ProyectoException("¡No existe el usuario a modificar!");
        }
    }

    @Override
    public void deleteProyecto(Integer id) throws ProyectoException {

        if (proyectoMap.containsKey(id)){
            proyectoMap.remove(id);
        }else{
            throw new ProyectoException("No existe el proyecto");
        }
    }
}
