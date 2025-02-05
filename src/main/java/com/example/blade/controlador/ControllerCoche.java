package com.example.blade.controlador;

import java.sql.SQLException;

import com.example.blade.controlador.dao.CocheDao;
import com.example.blade.controlador.dao.impl.JpaCocheDao;
import com.example.blade.modelo.Coche;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.hellokaton.blade.annotation.Path;
import com.hellokaton.blade.annotation.request.Body;
import com.hellokaton.blade.annotation.request.PathParam;
import com.hellokaton.blade.annotation.route.DELETE;
import com.hellokaton.blade.annotation.route.GET;
import com.hellokaton.blade.annotation.route.POST;
import com.hellokaton.blade.annotation.route.PUT;
import com.hellokaton.blade.mvc.http.Response;


@Path
public class ControllerCoche {

    private CocheDao cocheDao = JpaCocheDao.instancia();

    @POST("/api/coche")
    public void addCoche(@Body String body, Response response) {

        try {
            Coche coche = new Gson().fromJson(body, Coche.class);

            if (cocheDao.addCoche(coche)) {
                response.status(200).body("Agregado Correctamente");
            } else {
                response.status(400).body("Error: no se ha podido agregar el coche");
            }

        } catch (JsonSyntaxException jse) {
            response.status(400).body("Error: Json Invalido");
        } catch (Exception e) {
            response.status(500).body("Error: " + e.getMessage() + "}");
        }
    }

    @GET("/api/coches")
    public void getAllCoches(Response response, @PathParam int codigo) throws SQLException {
        response.json(cocheDao.getAllCoches());
    }

    @PUT("/api/cochesL")
    public void actualizarCoche(@Body String body, Response response) {
        try {
            Coche cocheRecibido = new Gson().fromJson(body, Coche.class);

            Coche cocheActualizado = cocheDao.updateCoche(cocheRecibido);
            response.body("Coche actualizado correctamente: " +
                    "ID: " + cocheActualizado.getId() + ", " +
                    "Matrícula: " + cocheActualizado.getMatricula() + ", " +
                    "Marca: " + cocheActualizado.getMarca() + ", " +
                    "Modelo: " + cocheActualizado.getModelo() + ", " +
                    "Fecha: " + cocheActualizado.getDate());

        } catch (JsonSyntaxException jse) {
            response.status(400).body("Error: JSON inválido");
        } catch (Exception e) {
            response.status(404).body("Error: " + e.getMessage());
        }
    }

    @DELETE("/api/coche/delete")
    public void eliminarCoche(@Body String body, Response response) {
        try {
            Coche cocheRecibido = new Gson().fromJson(body, Coche.class);

            boolean eliminado = cocheDao.delete(cocheRecibido);

            if (eliminado) {
                response.status(200).body("Coche eliminado correctamente con ID: " + cocheRecibido.getId());
            } else {
                response.status(404).body("Error: No se encontró el coche con ID: " + cocheRecibido.getId());
            }

        } catch (Exception e) {
            response.status(500).body("Error al eliminar el coche: " + e.getMessage());
        }
    }

}
