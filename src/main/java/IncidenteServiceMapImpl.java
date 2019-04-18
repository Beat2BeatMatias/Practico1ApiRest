import java.util.Calendar;
import java.util.Collection;
import java.util.HashMap;

public class IncidenteServiceMapImpl implements IncidenteService {

    HashMap<Integer, Incidente> incidenteMap;


    public IncidenteServiceMapImpl(){
        incidenteMap=new HashMap<Integer, Incidente>();
    }

    @Override
    public void addIncidente(Incidente incidente) throws IncidenteException {
        if (incidenteMap.get(incidente.getId()) != null){
            if (incidente.getId() != incidenteMap.get(incidente.getId()).getId())
                incidenteMap.put(incidente.getId(), incidente);
            else
                throw new IncidenteException();
        }else{
            incidenteMap.put(incidente.getId(), incidente);
        }
    }

    @Override
    public Incidente editIncidente(Incidente incidente) throws IncidenteException {
       if (incidenteMap.get(incidente.getId()) != null) {
           Incidente incidenteEditado = incidenteMap.get(incidente.getId());
           if (incidente.getClasificacion().equals(incidenteEditado.getClasificacion()) &&
                   incidente.getFechaCreacion().equals(incidenteEditado.getFechaCreacion()) &&
                   incidente.getReponsable().getId() == (incidenteEditado.getReponsable().getId()) &&
                   incidente.getReportador().getId() == (incidenteEditado.getReportador().getId()) &&
                   incidente.getIdProyecto() == (incidenteEditado.getIdProyecto())) {

               incidenteEditado.setDescripcion(incidente.getDescripcion());
               if (incidente.getEstado() == (Estado.RESUELTO) || incidente.getEstado() == (Estado.ASIGNADO)) {
                   if(incidente.getEstado() == Estado.RESUELTO && incidente.getFechaSolucion() != null){
                       incidenteEditado.setEstado(incidente.getEstado());
                       incidenteEditado.setFechaSolucion(incidente.getFechaSolucion());
                   }else {
                       throw new IncidenteException("Debe colocar una fecha de resolución");
                   }
                   incidenteEditado.setEstado(incidente.getEstado());
               }
               else
                   throw new IncidenteException("Solo puedes utilizar la palabra 'ASIGNADO' o 'RESUELTO'");
               return incidenteEditado;
           } else {
               throw new IncidenteException("Solo se puede modificar la 'Descripción' y el 'Estado' del incidente");
           }
       }else {
           throw new IncidenteException("No existe el incidente que quieres modificar");
       }
    }

    @Override
    public Collection<Incidente> getIncidentes() {
        return incidenteMap.values();
    }
}
