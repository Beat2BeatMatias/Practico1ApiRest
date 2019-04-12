import com.google.gson.Gson;

import static spark.Spark.*;
import static spark.Spark.delete;

public class ApiRestEjercicio1 {

    public static void main(String[] args) {

        final UsuarioService usuarioService = precarga();

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
    }
    private static UsuarioServiceMapImpl precarga(){
        UsuarioServiceMapImpl usuarioService=new UsuarioServiceMapImpl();
        usuarioService.addUsuario(new Usuario(1,"Matías","Farías"));
        usuarioService.addUsuario(new Usuario(2,"Facundo","Pereira"));
        usuarioService.addUsuario(new Usuario(3,"Santiago","Ardiles"));

        return usuarioService;
    }
}
