import com.google.gson.Gson;
import org.slf4j.Logger;
import spark.Route;
import sun.rmi.runtime.Log;

import java.io.UncheckedIOException;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.function.Predicate;
import java.util.stream.Stream;

import static spark.Spark.*;
import static spark.Spark.delete;

public class ApiRestEjercicio1 {

    public static void main(String[] args) {

        final UsuarioService usuarioService;
        final ProyectoService proyectoService;
        final IncidenteService incidenteService;



        usuarioService   = precargaUsuario();
        proyectoService  = precargaProyecto(usuarioService.getUsuario(1),
                                                  usuarioService.getUsuario(2));

        Proyecto proyecto1 = null;
        Proyecto proyecto2 = null;
        try {
            proyecto1 = proyectoService.getProyecto(1);
            proyecto2 = proyectoService.getProyecto(2);
        } catch (ProyectoException e) {
            e.printStackTrace();
        }


        incidenteService = precargaIncidente(usuarioService.getUsuario(1),usuarioService.getUsuario(3),
                                                      proyecto1, proyecto2);

        //Métodos para usuario==========================================================================================
        get("/usuario", (request, response) -> {
            response.type("application/json");
            return new Gson().toJson(new StandarResponse(StatusResponse.SUCCESS,new Gson().toJsonTree(usuarioService.getUsuarios())));
        });

        get("/usuario/:id",(request, response) -> {
            response.type("application/json");
            return new Gson().toJson(new StandarResponse(StatusResponse.SUCCESS,new Gson().toJsonTree(usuarioService.getUsuario(Integer.parseInt(request.params(":id"))))));
        });
        post("/usuario", (request, response) -> {
            try {
                response.type("application/json");
                Usuario usuarioNuevo = new Gson().fromJson(request.body(), Usuario.class);
                usuarioService.addUsuario(usuarioNuevo);
                return new Gson().toJson(new StandarResponse(StatusResponse.SUCCESS));
            }catch (UsuarioException e){
                return new Gson().toJson(new StandarResponse(StatusResponse.ERROR, "Usuario existente"));
            }
        });
        put("/usuario", (request, response) -> {
            try {
                response.type("application/json");
                Usuario usuario = new Gson().fromJson(request.body(), Usuario.class);
                Usuario usuarioEditado = usuarioService.editUsuario(usuario);
                if (usuarioEditado != null) {
                    return new Gson().toJson(new StandarResponse(StatusResponse.SUCCESS,
                            new Gson().toJsonTree(usuario)));
                } else {
                    return new Gson().toJson(new StandarResponse(StatusResponse.ERROR, "Error al editar el integrante"));
                }
            }catch (UsuarioException e){
                return new Gson().toJson(new StandarResponse(StatusResponse.ERROR, "El usuario a modificar no existe"));
            }
        });
        delete("/usuario/:id",(request, response) -> {
            response.type("application/json");
            try {
                usuarioService.deleteUsuario(Integer.parseInt(request.params(":id")));
                return new Gson().toJson(new StandarResponse(StatusResponse.SUCCESS));
            }catch(UsuarioException e){
                return new Gson().toJson(new StandarResponse(StatusResponse.ERROR, "El usuario a borrar no existe"));
            }
        });

        //Métodos para proyecto=========================================================================================
        get("/proyecto", (request, response) -> {
            response.type("application/json");
                return new Gson().toJson(new StandarResponse(StatusResponse.SUCCESS, new Gson().toJsonTree(proyectoService.getProyectos())));

        });
        get("/proyecto/:id", (request, response) -> {
            response.type("application/json");
            try {
                return new Gson().toJson(new StandarResponse(StatusResponse.SUCCESS, new Gson().toJsonTree(proyectoService.getProyecto(Integer.parseInt(request.params(":id"))))));
            }catch (ProyectoException e){
                return new Gson().toJson(new StandarResponse(StatusResponse.ERROR, "No existe el proyecto"));
            }
        });
        post("/proyecto", (request, response) -> {
            response.type("application/json");
            try {
                proyectoService.addProyecto(new Gson().fromJson(request.body(), Proyecto.class));
                return new Gson().toJson(new StandarResponse(StatusResponse.SUCCESS));
            }catch (ProyectoException e){
                return new Gson().toJson(new StandarResponse(StatusResponse.ERROR, "Ya existe el proyecto"));
            }
        });
        put("/proyecto", (request, response) -> {
           try{
               response.type("application/json");
               Proyecto proyecto = new Gson().fromJson(request.body(),Proyecto.class);
               Proyecto proyectoEditado = proyectoService.editProyecto(proyecto);
            if (proyectoEditado != null) {
                return new Gson().toJson(new StandarResponse(StatusResponse.SUCCESS,
                        new Gson().toJsonTree(proyecto)));
            } else {
                return new Gson().toJson(new StandarResponse(StatusResponse.ERROR, "Error al editar el proyecto"));
            }
            }catch (ProyectoException e){
                return new Gson().toJson(new StandarResponse(StatusResponse.ERROR, "El proyecto a editar no exite"));
            }
        });
        delete("/proyecto/:id",(request, response) -> {
            response.type("application/json");
            try {
                if (incidenteService.getIncidentes().stream().noneMatch(incidente -> incidente.getIdProyecto() == Integer.parseInt(request.params((":id"))))) {
                    proyectoService.deleteProyecto(Integer.parseInt(request.params(":id")));
                    return new Gson().toJson(new StandarResponse(StatusResponse.SUCCESS));
                }else{
                    return new Gson().toJson(new StandarResponse(StatusResponse.ERROR, "El proyecto no se puede borrar porque tiene incidentes reportados"));
                }
            }catch(ProyectoException e){
                return new Gson().toJson(new StandarResponse(StatusResponse.ERROR, "El proyecto a borrar no existe"));
            }
        });

        //Métodos para incidentes=======================================================================================
        get("/incidente",(request, response) -> {
            response.type("application/json");
            return new Gson().toJson(new StandarResponse(StatusResponse.SUCCESS, new Gson().toJsonTree(incidenteService.getIncidentes())));
        });
        post("/incidente",(request, response) -> {
            response.type("application/json");
            try {
                incidenteService.addIncidente(new Gson().fromJson(request.body(), Incidente.class));
                return new Gson().toJson(new StandarResponse(StatusResponse.SUCCESS));
            }catch (IncidenteException e){
                return new Gson().toJson(new StandarResponse(StatusResponse.ERROR, "Ya existe el incidente"));
            }
        });
        put("/incidente",(request, response) -> {
            try {
                response.type("application/json");
                Incidente incidente = new Gson().fromJson(request.body(), Incidente.class);
                Incidente incidenteEditado = incidenteService.editIncidente(incidente);
                return new Gson().toJson(new StandarResponse(StatusResponse.SUCCESS));
            }catch (IncidenteException e){
                return new Gson().toJson(new StandarResponse(StatusResponse.ERROR, e.getMessage()));
            }
        });

        //Métodos de la API=============================================================================================
        get("/usuario/:id/proyectos",(request, response) -> {
            response.type("application/json");
            Object[] proyectos = proyectoService.getProyectos().stream()
                    .filter(proyecto -> proyecto.getPropietario().getId() == Integer.parseInt(request.params(":id")))
                    .toArray();
            if(proyectos.length>0) {
                return new Gson().toJson(new StandarResponse(StatusResponse.SUCCESS, new Gson().toJsonTree(proyectos)));
            }else{
                return new Gson().toJson(new StandarResponse(StatusResponse.SUCCESS, "No tiene proyectos asociados"));
            }
        });
        get("/usuario/:id/incidentes",(request, response) -> {
            response.type("application/json");
            Object[] incidenteAsociado = incidenteService.getIncidentes().stream()
                    .filter(incidente -> (incidente.getReponsable().getId() == Integer.parseInt(request.params(":id"))
                            || incidente.getReportador().getId() == Integer.parseInt(request.params(":id"))))
                    .toArray();
            return new Gson().toJson(new StandarResponse(StatusResponse.SUCCESS, new Gson().toJsonTree(incidenteAsociado)));
        });
        get("/proyecto/:id/incidentes",(request, response) -> {
            response.type("application/json");
            Object[] incidenteAsociado = incidenteService.getIncidentes().stream()
                    .filter(incidente -> (incidente.getIdProyecto() == Integer.parseInt(request.params(":id"))))
                    .toArray();
            return new Gson().toJson(new StandarResponse(StatusResponse.SUCCESS, new Gson().toJsonTree(incidenteAsociado)));
        });
        get("/incidente/:tipo",(request, response) -> {
            String tipo=request.params(":tipo");
            response.type("application/json");
            if(tipo.equals("asignados")) {
                Object[] incidentePendientes = incidenteService.getIncidentes().stream()
                        .filter(incidente -> (incidente.getEstado() == Estado.ASIGNADO))
                        .toArray();
                return new Gson().toJson(new StandarResponse(StatusResponse.SUCCESS, new Gson().toJsonTree(incidentePendientes)));
            }else if(tipo.equals("resueltos")){
                Object[] incidenteCerrados = incidenteService.getIncidentes().stream()
                        .filter(incidente -> (incidente.getEstado() == Estado.RESUELTO))
                        .toArray();
                return new Gson().toJson(new StandarResponse(StatusResponse.SUCCESS, new Gson().toJsonTree(incidenteCerrados)));
            }else {
                return new Gson().toJson(new StandarResponse(StatusResponse.ERROR, "No se encontro la dirección"));
            }
        });

    }

    private static UsuarioServiceMapImpl precargaUsuario(){
        UsuarioServiceMapImpl usuarioService=new UsuarioServiceMapImpl();
        try {
            usuarioService.addUsuario(new Usuario(1, "Matías", "Farías"));
            usuarioService.addUsuario(new Usuario(2, "Facundo", "Pereira"));
            usuarioService.addUsuario(new Usuario(3, "Santiago", "Ardiles"));
            return usuarioService;
        }catch (UsuarioException e){
            return usuarioService;
        }
    }
    private static ProyectoServiceMapImpl precargaProyecto(Usuario usuario1,Usuario usuario2) {
        ProyectoServiceMapImpl proyectoServiceMap=new ProyectoServiceMapImpl();
        try {
            proyectoServiceMap.addProyecto(new Proyecto(1, "Diseño de un Hospital", usuario1));
            proyectoServiceMap.addProyecto(new Proyecto(2, "Mantenimiento del equipamiento biomédico", usuario2));
            return proyectoServiceMap;
        }catch (ProyectoException e){
            return proyectoServiceMap;
        }
    }
    private static IncidenteServiceMapImpl precargaIncidente(Usuario usuario1, Usuario usuario3, Proyecto proyecto1, Proyecto proyecto2) {
        IncidenteServiceMapImpl incidenteServiceMap=new IncidenteServiceMapImpl();
        try {
            incidenteServiceMap.addIncidente(new Incidente(1, Clasificacion.NORMAL, "Terreno irregular",
                    usuario3, usuario1, Estado.ASIGNADO,
                    new Date(2019 - 1900, 1, 1, 10, 30), null, proyecto1.getId()));
            incidenteServiceMap.addIncidente(new Incidente(2, Clasificacion.CRITICO, "Respirador averiado",
                    usuario1, usuario3, Estado.RESUELTO,
                    new Date(2019 - 1900, 0, 15, 9, 0),
                    new Date(2019 - 1900, 0, 18, 8, 30), proyecto2.getId()));
            return incidenteServiceMap;
        }catch (IncidenteException e){
            return incidenteServiceMap;
        }
    }
}
