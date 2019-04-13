import com.google.gson.Gson;
import org.slf4j.Logger;
import spark.Route;
import sun.rmi.runtime.Log;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import static spark.Spark.*;
import static spark.Spark.delete;

public class ApiRestEjercicio1 {

    public static void main(String[] args) {

        final UsuarioService usuarioService     = precargaUsuario();
        final ProyectoService proyectoService   = precargaProyecto(usuarioService.getUsuario(1),
                                                  usuarioService.getUsuario(2));
        final IncidenteService incidenteService = precargaIncidente(usuarioService.getUsuario(3),
                                                  proyectoService.getProyecto(1), proyectoService.getProyecto(2));

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
            response.type("application/json");
            Usuario usuarioNuevo=new Gson().fromJson(request.body(),Usuario.class);
            usuarioService.addUsuario(usuarioNuevo);
            return new Gson().toJson(new StandarResponse(StatusResponse.SUCCESS));
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
                return new Gson().toJson(new StandarResponse(StatusResponse.ERROR));
            }
        });
        delete("/usuario/:id",(request, response) -> {
            try {
                usuarioService.deleteUsuario(Integer.parseInt(request.params(":id")));
                return new Gson().toJson(new StandarResponse(StatusResponse.SUCCESS));
            }catch(UsuarioException e){
                return new Gson().toJson(new StandarResponse(StatusResponse.ERROR));
            }
        });
        //Métodos para proyecto=========================================================================================
        get("/proyecto", (request, response) -> {
            response.type("application/json");
            return new Gson().toJson(new StandarResponse(StatusResponse.SUCCESS,new Gson().toJsonTree(proyectoService.getProyectos())));
        });
        get("/proyecto/:id", (request, response) -> {
            response.type("application/json");
            return new Gson().toJson(new StandarResponse(StatusResponse.SUCCESS,new Gson().toJsonTree(proyectoService.getProyecto(Integer.parseInt(request.params(":id"))))));
        });
        post("/proyecto", (request, response) -> {
            response.type("application/json");
            proyectoService.addProyecto(new Gson().fromJson(request.body(),Proyecto.class));
            return new Gson().toJson(new StandarResponse(StatusResponse.SUCCESS));
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
                return new Gson().toJson(new StandarResponse(StatusResponse.ERROR));
            }
        });
        delete("/proyecto/:id",(request, response) -> {
            try {
                proyectoService.deleteProyecto(Integer.parseInt(request.params(":id")));
                return new Gson().toJson(new StandarResponse(StatusResponse.SUCCESS));
            }catch(ProyectoException e){
                return new Gson().toJson(new StandarResponse(StatusResponse.ERROR));
            }
        });
        //==============================================================================================================
        get("/incidente",(request, response) -> {
            response.type("application/json");
            return new Gson().toJson(new StandarResponse(StatusResponse.SUCCESS, new Gson().toJsonTree(incidenteService.getIncidentes())));
        });
        post("/incidente",(request, response) -> {
            response.type("application/json");
            incidenteService.addIncidente(new Gson().fromJson(request.body(),Incidente.class));
            return new Gson().toJson(new StandarResponse(StatusResponse.SUCCESS));
        });
        put("/incidente/:id",(request, response) -> {
            try {
                response.type("application/json");
                Incidente incidente = new Gson().fromJson(request.body(), Incidente.class);
                Incidente incidenteEditado = incidenteService.editIncidente(incidente);
                incidenteService.addIncidente(new Gson().fromJson(request.body(), Incidente.class));
                return new Gson().toJson(new StandarResponse(StatusResponse.SUCCESS));
            }catch (IncidenteException e){
                return new Gson().toJson(new StandarResponse(StatusResponse.ERROR,e.getMessage()));
            }
        });

    }

    private static UsuarioServiceMapImpl precargaUsuario(){
        UsuarioServiceMapImpl usuarioService=new UsuarioServiceMapImpl();
        usuarioService.addUsuario(new Usuario(1,"Matías","Farías"));
        usuarioService.addUsuario(new Usuario(2,"Facundo","Pereira"));
        usuarioService.addUsuario(new Usuario(3,"Santiago","Ardiles"));
        return usuarioService;
    }
    private static ProyectoServiceMapImpl precargaProyecto(Usuario usuario1,Usuario usuario2){
        ProyectoServiceMapImpl proyectoServiceMap=new ProyectoServiceMapImpl();
        proyectoServiceMap.addProyecto(new Proyecto(1,"Diseño de un Hospital", usuario1));
        proyectoServiceMap.addProyecto(new Proyecto(2,"Mantenimiento del equipamiento biomédico", usuario2));
        return proyectoServiceMap;
    }
    private static IncidenteServiceMapImpl precargaIncidente(Usuario usuario3, Proyecto proyecto, Proyecto proyecto1) {
        IncidenteServiceMapImpl incidenteServiceMap=new IncidenteServiceMapImpl();
        incidenteServiceMap.addIncidente(new Incidente(1,Clasificacion.NORMAL,"Terreno irregular",
                                         usuario3,usuario3,Estado.ASIGNADO,
                                         new Date(2019-1900,1,1,10,30),null));
        incidenteServiceMap.addIncidente(new Incidente(2,Clasificacion.CRITICO,"Rerspirador averiado",
                                         usuario3,usuario3,Estado.RESUELTO,
                                         new Date(2019-1900,0,15,9,0),new Date(2019-1900,0,18,8,30)));
        return incidenteServiceMap;
    }
}
