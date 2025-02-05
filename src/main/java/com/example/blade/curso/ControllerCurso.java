package com.example.blade.curso;

import java.util.List;
import java.util.Optional;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.hellokaton.blade.annotation.Path;
import com.hellokaton.blade.annotation.request.Body;
import com.hellokaton.blade.annotation.request.Form;
import com.hellokaton.blade.annotation.request.PathParam;
import com.hellokaton.blade.annotation.route.GET;
import com.hellokaton.blade.annotation.route.POST;
import com.hellokaton.blade.mvc.http.Request;
import com.hellokaton.blade.mvc.http.Response;
import com.hellokaton.blade.mvc.ui.ResponseType;

@Path
public class ControllerCurso {

    CursoDao cursoDao = CursoDao.instancia();
    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(ControllerCurso.class);

    @GET(value = "/api/curso/:codigo", responseType = ResponseType.JSON)
    public void apiCodigo(Response response, @PathParam int codigo) {
        Optional<Curso> curso = cursoDao.getCursoByCodigo(codigo);
        if (curso.isPresent())
            response.json(curso.get());
        else
            response.status(400);
    }

    /**
     * Añadimso un curso por api
     * 
     * @param body
     * @param response
     */
    @POST("/api/curso")
    public void apiAddCurso(@Body String body, Response response) {
        try {
            System.out.println("📥 JSON recibido: " + body);

            if (body == null || body.trim().isEmpty()) {
                response.status(400).body("{\"error\": \"JSON vacío o inválido\"}");
                return;
            }

            Curso curso = new Gson().fromJson(body, Curso.class);

            if (cursoDao.addCurso(curso)) {
                System.out.println("✅ Curso agregado a la BD");
                response.status(200).body("{\"mensaje\": \"Curso agregado correctamente\"}");
            } else {
                response.status(400).body("{\"error\": \"No se pudo agregar el curso\"}");
            }
        } catch (JsonSyntaxException jse) {
            response.status(400).body("{\"error\": \"JSON inválido: " + jse.getMessage() + "\"}");
        } catch (Exception e) {
            response.status(500).body("{\"error\": \"" + e.getMessage() + "\"}");
        }
    }

    @GET("/api/cursos")
    public void apiListaCursos(Response response, @PathParam int codigo) {
        response.json(cursoDao.getAllCursos());
    }

    @GET("/cursos/:codigo")
    public void vistaCodigo(Response response, Request request, @PathParam int codigo) {
        Optional<Curso> curso = cursoDao.getCursoByCodigo(codigo);
        if (curso.isPresent()) {
            List<Curso> cursos = cursoDao.getAllCursos();
            request.attribute("cursos", cursos);
            request.attribute("curso", curso.get());
            response.render("curso.html");
        } else
            response.render("/error/error.html");
    }

    @GET("/cursos")
    public void vistaCursos(Request request, Response response) {
        List<Curso> cursos = cursoDao.getAllCursos();
        request.attribute("cursos", cursos);
        response.render("cursos.html");
    }

    @POST("/cursos")
    public void actualizaCurso(@Form int codigo, @Form String nombre, Response response) {

        log.info("Dentro de actualizarcurso: " + codigo + ":" + nombre);
        
        Optional<Curso> curso = cursoDao.getCursoByCodigo(codigo);
        if (curso.isPresent()) {
            // actualizamos curso
            cursoDao.updateCurso(codigo, nombre);
        } else {
            // Damos de alta el curso
            cursoDao.addCurso(codigo, nombre);
        }
        response.redirect("/cursos");
    }

}
