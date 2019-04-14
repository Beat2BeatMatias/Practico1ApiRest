import java.util.Collection;

public interface IncidenteService {

    public void addIncidente(Incidente incidente) throws IncidenteException;
    public Incidente editIncidente(Incidente incidente) throws IncidenteException;
    public Collection<Incidente> getIncidentes();
}
