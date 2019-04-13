import java.util.Collection;
import java.util.HashMap;

public class IncidenteServiceMapImpl implements IncidenteService {

    HashMap<Integer, Incidente> incidenteMap;


    public IncidenteServiceMapImpl(){
        incidenteMap=new HashMap<Integer, Incidente>();
    }

    @Override
    public void addIncidente(Incidente incidente) {
        incidenteMap.put(incidente.getId(),incidente);
    }

    @Override
    public Incidente editIncidente(Incidente incidente) throws IncidenteException {
        Incidente incidenteEditado = incidenteMap.get(incidente.getId());

        if(incidente.getClasificacion().equals(incidenteEditado.getClasificacion()) &&
                incidente.getFechaCreacion().equals(incidenteEditado.getFechaCreacion()) &&
                incidente.getReponsable().getId() == (incidenteEditado.getReponsable().getId()) &&
                incidente.getReportador().getId() == (incidenteEditado.getReportador().getId())){
            incidenteEditado.setDescripcion(incidente.getDescripcion());
            incidenteEditado.setEstado(incidente.getEstado());
            return incidenteEditado;
        }else{
            throw new IncidenteException("Solo se puede modificar la 'Descripción' y el 'Estado' del incidente");
        }
    }

    @Override
    public Collection<Incidente> getIncidentes() {
        return incidenteMap.values();
    }
}
