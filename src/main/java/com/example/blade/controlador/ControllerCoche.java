package com.example.blade.controlador;

import java.sql.SQLException;

import com.example.blade.controlador.dao.CocheDao;
import com.example.blade.controlador.dao.impl.JpaCocheDao;
import com.example.blade.modelo.Coche;
import com.google.gson.Gson;
import com.hellokaton.blade.annotation.Path;
import com.hellokaton.blade.annotation.request.Body;
import com.hellokaton.blade.annotation.route.DELETE;
import com.hellokaton.blade.annotation.route.GET;
import com.hellokaton.blade.annotation.route.POST;
import com.hellokaton.blade.annotation.route.PUT;
import com.hellokaton.blade.mvc.http.Response;

/**
 * Controlador para la gestión de objetos {@link Coche} mediante la definición
 * de endpoints REST.
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
     * CRUD de los coches.
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
     * respuesta con estado 500 y el mensaje de error correspondiente.
     * </p>
     *
     * @param body el cuerpo de la solicitud en formato JSON que representa el coche a agregar         
     * @param response el objeto {@link Response} utilizado para construir la respuesta HTTP
     *                 
     */
    @POST("/api/coche")
    public void addCoche(@Body String body, Response response) {
        try {

            Coche coche = new Gson().fromJson(body, Coche.class);
            cocheDao.addCoche(coche);
            response.status(200).body("Agregado Correctamente");

        } catch (SQLException e) {
            response.status(500).body(e.getMessage());
        }
    }

    /**
     * Endpoint para obtener todos los coches.
     * <p>
     * Ruta: <code>GET /api/coches</code>
     * <br>
     * Este método consulta la base de datos a través del DAO para recuperar una
     * lista de todos los coches y devuelve el resultado en formato JSON. Si
     * ocurre algún error durante la consulta, se responde con un estado 500 y
     * el mensaje de error correspondiente.
     * </p>
     *
     * @param response el objeto {@link Response} utilizado para construir la  respuesta HTTP
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
     * Endpoint para actualizar un coche existente.
     * <p>
     * Ruta: <code>PUT /api/coche</code>
     * <br>
     * El método recibe el cuerpo de la solicitud en formato JSON, lo convierte
     * en un objeto {@link Coche} y llama al método de actualización del DAO. En
     * caso de que el JSON tenga una sintaxis incorrecta, se responde con un
     * estado 400; si ocurre cualquier otro error, se responde con un estado
     * 404
     * </p>
     *
     * @param body el cuerpo de la solicitud en formato JSON que representa el coche            
     * @param response el objeto {@link Response} utilizado para construir la respuesta HTTP
     *               
     */
    @PUT("/api/coche")
    public void actualizarCoche(@Body String body, Response response) {
        try {

            Coche cocheRecibido = new Gson().fromJson(body, Coche.class);
            cocheDao.updateCoche(cocheRecibido);
            response.status(200).body("Actualizado Correctamente");

        } catch (Exception e) {
            response.status(404).body(e.getMessage());
        }
    }

    /**
     * Elimina un coche de la base de datos.
     * <p>
     * Este método se encarga de recibir una solicitud HTTP DELETE en la ruta
     * <code>/api/coche</code>, extrayendo del cuerpo de la solicitud (en
     * formato JSON) la información de un objeto {@link Coche}. A continuación,
     * utiliza el DAO para eliminar el coche de la base de datos. Si la
     * eliminación es exitosa, se devuelve una respuesta HTTP con código 200 y
     * un mensaje de confirmación que incluye el ID del coche eliminado. En caso
     * de producirse cualquier error, se responde con un código 500 y el mensaje
     * de error correspondiente.
     * </p>
     *
     * @param body el cuerpo de la solicitud en formato JSON que representa el coche a eliminar.
     * @param response el objeto {@link Response} utilizado para construir y enviar la respuesta HTTP.
     *                 
     */
    @DELETE("/api/coche")
    public void eliminarCoche(@Body String body, Response response) {
        try {

            Coche cocheRecibido = new Gson().fromJson(body, Coche.class);
            cocheDao.delete(cocheRecibido);
            response.status(200).body("Coche eliminado correctamente con ID: " + cocheRecibido.getId());

        } catch (Exception e) {
            response.status(500).body(e.getMessage());
        }
    }
}
