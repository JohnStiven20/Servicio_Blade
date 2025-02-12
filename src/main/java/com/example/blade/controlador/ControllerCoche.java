package com.example.blade.controlador;

import java.sql.SQLException;

import com.example.blade.controlador.dao.CocheDao;
import com.example.blade.controlador.dao.impl.JpaCocheDao;
import com.example.blade.modelo.Coche;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.hellokaton.blade.annotation.Path;
import com.hellokaton.blade.annotation.request.Body;
import com.hellokaton.blade.annotation.request.PathParam;
import com.hellokaton.blade.annotation.route.DELETE;
import com.hellokaton.blade.annotation.route.GET;
import com.hellokaton.blade.annotation.route.POST;
import com.hellokaton.blade.annotation.route.PUT;
import com.hellokaton.blade.mvc.http.Response;

/**
 * Controlador para la gestión de objetos {@link Coche} mediante endpoints REST
 *
 * <p>
 * Esta clase utiliza las anotaciones del framework Blade para definir las rutas
 * HTTP que permiten realizar operaciones de creación, consulta, actualización y
 * eliminación de instancias de {@link Coche}. Se apoya en la capa de
 * persistencia a través de la interfaz {@link CocheDao} y su implementación
 * {@link JpaCocheDao}
 * </p>
 *
 * @author John
 */
@Path
public class ControllerCoche {

    // Instancia del DAO para acceder a las operaciones CRUD sobre la entidad Coche
    private final CocheDao cocheDao = JpaCocheDao.instancia();

    /**
     * Endpoint para agregar un nuevo coche a la base de datos
     * <p>
     * Ruta: <code>POST /api/coche</code>
     * </p>
     * <p>
     * Este método recibe el cuerpo de la solicitud en formato JSON que
     * representa un objeto {@link Coche}. Utiliza la librería Gson para
     * convertir el JSON a un objeto Coche, y luego llama al método
     * <code>addCoche</code> del DAO para persistirlo Si la operación se realiza
     * correctamente, se envía una respuesta HTTP 200 con un mensaje de
     * confirmación; de lo contrario, se captura la excepción y se responde con
     * un error HTTP 500 y el mensaje correspondiente
     * </p>
     *
     * @param body Cadena JSON que contiene la información del coche a agregar
     * @param response Objeto {@link Response} para construir y enviar la
     * respuesta HTTP
     */
    @POST("/api/coche")
    public void addCoche(@Body String body, Response response) {
        try {
            Gson gson = new GsonBuilder()
                    .create();

            System.out.println(body);
            Coche coche = gson.fromJson(body, Coche.class);
            cocheDao.addCoche(coche);
            response.status(200).body("Agregado Correctamente");
        } catch (SQLException e) {
            response.status(500).body(e.getMessage());
        }
    }

    /**
     * Endpoint para obtener todos los coches registrados en la base de datos
     * <p>
     * Ruta: <code>GET /api/coches</code>
     * </p>
     * <p>
     * Este método procesa una solicitud GET y utiliza el método
     * <code>getAllCoches</code> del DAO para recuperar una lista de todos los
     * objetos {@link Coche}. La respuesta se devuelve en formato JSON. Si
     * ocurre algún error durante la consulta, se envía un estado HTTP 500 junto
     * con el mensaje de error
     * </p>
     *
     * @param response Objeto {@link Response} para construir y enviar la
     * respuesta HTTP
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
     * Endpoint para actualizar la información de un coche existente
     * <p>
     * Ruta: <code>PUT /api/coche</code>
     * </p>
     * <p>
     * Este método maneja una solicitud PUT cuyo cuerpo contiene en formato JSON
     * la representación actualizada de un objeto {@link Coche}. Se convierte el
     * JSON a un objeto Coche y se invoca el método <code>updateCoche</code> del
     * DAO para actualizarlo en la base de datos. Una vez actualizado, se envía
     * una respuesta HTTP 200; si ocurre algún error, se envía un estado HTTP
     * 500 con el mensaje de error
     * </p>
     *
     * @param body Cadena JSON con los datos actualizados del coche
     * @param response Objeto {@link Response} para construir y enviar la
     * respuesta HTTP
     */
    @PUT("/api/coche")
    public void actualizarCoche(@Body String body, Response response) {
        try {
            Coche coche = new Gson().fromJson(body, Coche.class);
            cocheDao.updateCoche(coche);
            response.status(200).body("Coche actualizado correctamente.");
        } catch (Exception e) {
            response.status(500).body(e.getMessage());
        }
    }

    /**
     * Endpoint para eliminar un coche de la base de datos
     * <p>
     * Ruta: <code>DELETE /api/coche</code>
     * </p>
     * <p>
     * Este método procesa una solicitud DELETE cuyo cuerpo contiene un objeto
     * {@link Coche} en formato JSON, identificando el coche a eliminar. Se
     * convierte el JSON a un objeto Coche y se llama al método
     * <code>delete</code> del DAO para realizar la eliminación Si la operación
     * es exitosa, se envía una respuesta HTTP 200; de lo contrario, se envía un
     * error HTTP 500 con el mensaje del fallo
     * </p>
     *
     * @param body Cadena JSON que representa el coche a eliminar
     * @param response Objeto {@link Response} para construir y enviar la
     * respuesta HTTP
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

    /**
     * Endpoint para buscar un coche por su matrícula
     * <p>
     * Ruta: <code>GET /api/coche/:matricula</code>
     * </p>
     * <p>
     * Este método extrae el parámetro de ruta <code>matricula</code> de la URL
     * y utiliza el método <code>findByMatricula</code> del DAO para buscar el
     * coche en la base de datos Si se encuentra el coche, se devuelve en
     * formato JSON; en caso contrario, se responde con un error HTTP 500 junto
     * con el mensaje de error
     * </p>
     *
     * @param response Objeto {@link Response} para construir y enviar la
     * respuesta HTTP
     * @param matricula Parámetro de la ruta que especifica la matrícula del
     * coche a buscar
     */
    @GET("/api/coche/:matricula")
    public void findByMatricula(Response response, @PathParam String matricula) {
        try {
            response.json(cocheDao.findByMatricula(matricula));
        } catch (Exception e) {
            response.status(500).body(e.getMessage());
        }
    }
}
