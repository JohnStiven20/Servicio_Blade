package com.example.blade.controlador;

import java.sql.SQLException;

import com.example.blade.controlador.dao.CocheDao;
import com.example.blade.controlador.dao.impl.JpaCocheDao;
import com.example.blade.modelo.Coche;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hellokaton.blade.annotation.Path;
import com.hellokaton.blade.annotation.request.Body;
import com.hellokaton.blade.annotation.route.DELETE;
import com.hellokaton.blade.annotation.route.GET;
import com.hellokaton.blade.annotation.route.POST;
import com.hellokaton.blade.annotation.route.PUT;
import com.hellokaton.blade.mvc.http.Response;

/**
 * Controlador para la gestión de objetos {@link Coche} mediante la definición
 * de endpoints REST
 * 
 * <p>
 * Esta clase utiliza las anotaciones del framework Blade para definir las rutas
 * HTTP que permiten realizar operaciones de creación, consulta, actualización y
 * eliminación de entidades {@link Coche}. Se apoya en la capa de persistencia
 * representada por {@link CocheDao} y su implementación {@link JpaCocheDao}.
 * </p>
 * 
 * @author John Stiven Solano Macas
 */
@Path
public class ControllerCoche {

    /**
     * Instancia de {@link CocheDao} utilizada para acceder a las operaciones
     * CRUD de los coches
     */
    private final CocheDao cocheDao = JpaCocheDao.instancia();

    /**
     * Endpoint para agregar un nuevo coche.
     * <p>
     * Ruta: <code>POST /api/coche</code>
     * <br>
     * Recibe el cuerpo de la solicitud en formato JSON, lo convierte en un
     * objeto {@link Coche} y lo persiste en la base de datos utilizando el DAO.
     * En caso de éxito, se envía una respuesta con estado 200 y un mensaje de
     * confirmación. Si ocurre algún error al persistir el coche, se envía una
     * respuesta con estado 500 y el mensaje de error correspondiente
     * </p>
     *
     * @param body     el cuerpo de la solicitud en formato JSON que representa el
     *                 coche a agregar
     * @param response el objeto {@link Response} utilizado para construir la
     *                 respuesta HTTP
     * 
     */
    @POST("/api/coche")
    public void addCoche(@Body String body, Response response) {
        try {

            Coche coche = new Gson().fromJson(body, new TypeToken<Coche>() {
            }.getType());
            cocheDao.addCoche(coche);
            response.status(200).body("Agregado Correctamente");

        } catch (SQLException e) {
            response.status(500).body(e.getMessage());
        }
    }

    /**
     * Endpoint para obtener todos los coches
     * 
     * <p>
     * Ruta: <code>GET /api/coches</code>
     * <br>
     * Este método consulta la base de datos a través del DAO para recuperar una
     * lista de todos los coches y devuelve el resultado en formato JSON. Si
     * ocurre algún error durante la consulta, se responde con un estado 500 y
     * el mensaje de error correspondiente.
     * </p>
     *
     * @param response el objeto {@link Response} utilizado para construir la
     *                 respuesta HTTP
     * 
     */
    @GET("/api/coches")
    public void getAllCoches(Response response) {
        try {
            response.json(cocheDao.getAllCoches());
        } catch (SQLException e) {
            response.status(500).body(e.getMessage());
        } catch (Exception e) {
            response.status(500).body(e.getMessage());
        }
    }

    /**
     * Actualiza un coche existente en la base de datos mediante una solicitud HTTP
     * PUT
     * 
     * <p>
     * Ruta: <code>PUT /api/coche</code>
     * </p>
     *
     * <p>
     * Este método recibe el cuerpo de la solicitud en formato JSON, lo convierte en
     * un
     * objeto {@link Coche} y llama al método de actualización del DAO. Si ocurre un
     * error durante la actualización, se responde con un estado HTTP 500
     * </p>
     *
     * @param body     El cuerpo de la solicitud en formato JSON que representa el
     *                 coche a actualizar.
     * @param response El objeto {@link Response} utilizado para construir y enviar
     *                 la respuesta HTTP.
     */
    @PUT("/api/coche")
    public void actualizarCoche(@Body String body, Response response) {
        try {
            Coche coche = new Gson().fromJson(body, Coche.class);
            cocheDao.updateCoche(coche);
            response.status(200).body("Coche actualizado correctamente.");
        } catch (Exception e) {
            response.status(500).body("Error al actualizar el coche: " + e.getMessage());
        }
    }

    /**
     * Elimina un coche de la base de datos mediante una solicitud HTTP DELETE.
     * <p>
     * Este método recibe una solicitud DELETE en la ruta <code>/api/coche</code>,
     * extrayendo del cuerpo de la solicitud (en formato JSON) la información de un
     * objeto {@link Coche}. Luego, utiliza el DAO para eliminar el coche de la base
     * de
     * datos. Si la eliminación es exitosa, responde con un código HTTP 200 y un
     * mensaje de confirmación. En caso de error, devuelve un código HTTP 500 con el
     * mensaje de error correspondiente
     * </p>
     *
     * @param body     El cuerpo de la solicitud en formato JSON, que representa el
     *                 coche a eliminar
     * @param response El objeto {@link Response} utilizado para construir y enviar
     *                 la respuesta HTTP
     */
    @DELETE("/api/coche")
    public void eliminarCoche(@Body String body, Response response) {
        try {
            Coche cocheRecibido = new Gson().fromJson(body, Coche.class);
            cocheDao.delete(cocheRecibido);
            response.status(200).body("Coche eliminado correctamente.");
        } catch (Exception e) {
            response.status(500).body("Error al eliminar el coche: " + e.getMessage());
        }
    }

}
