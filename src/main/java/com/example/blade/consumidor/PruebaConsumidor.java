package com.example.blade.consumidor;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.List;

import com.example.blade.curso.Curso;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;

import okhttp3.Call;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class PruebaConsumidor {
    private static final OkHttpClient cliente = new OkHttpClient();

    public static void main(String[] args) {
        // conexionSimple();
        // pruebaClienteHTML();
        // pruebaClienteJsonGson();
        // pruebaClienteJsonGsonCursos();
        pruebaPOST();
    }

    public static void pruebaPOST() {
        Curso curso = Curso.builder().codigo(11).nombre("Curso Hola").build();

        Gson gson = new Gson();
        String json = gson.toJson(curso);

        System.out.println("📡 JSON enviado: " + json);

        RequestBody body = RequestBody.create(json, MediaType.parse("application/json"));

        Request request = new Request.Builder()
                .url("http://localhost:9000/api/curso")
                .post(body)
                .addHeader("Content-Type", "application/json")
                .build();

        Call call = cliente.newCall(request);
        try (Response response = call.execute()) {
            
            System.out.println("🔄 Código de respuesta: " + response.code());

            // Verificar si el servidor envía una respuesta
            ResponseBody responseBody = response.body();
            if (responseBody != null) {
                String responseString = responseBody.string();
                System.out.println("📜 Respuesta del servidor: " + responseString);
            } else {
                System.out.println("⚠️ Respuesta del servidor es NULL");
            }
        } catch (IOException e) {
            System.out.println("🚨 Error al conectar con el servidor:");
        }
    }

    /**
     * Prueba usando HttpURLConnection
     */
    public static void conexionSimple() {
        URL url;
        try {
            url = new URI("http://localhost:9001/cursos/1").toURL();
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestProperty("accept", "application/json");
            InputStream responseStream = connection.getInputStream();
            BufferedReader bis = new BufferedReader(new InputStreamReader(responseStream));
            bis.lines().forEach(System.out::println);
        } catch (MalformedURLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (URISyntaxException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /**
     * Prueba usando libreria OkHttp
     * https://www.baeldung.com/guide-to-okhttp
     */
    public static void pruebaClienteHTML() {
        String url = "http://localhost:9001/cursos/1";
        Request request = new Request.Builder().url(url).build();

        try (Response response = cliente.newCall(request).execute()) {
            if (response.isSuccessful())
                System.out.println(response.body().string());
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /**
     * Obtenemos Json
     */
    public static void pruebaClienteJson() {
        String url = "http://localhost:9001/api/cursos/1";
        OkHttpClient cliente = new OkHttpClient();
        Request request = new Request.Builder().url(url).build();
        Moshi moshi = new Moshi.Builder().build();
        JsonAdapter<Curso> cursoJsonAdapter = moshi.adapter(Curso.class);

        try (Response response = cliente.newCall(request).execute()) {
            if (response.isSuccessful()) {
                Curso curso = cursoJsonAdapter.fromJson(response.body().source());
                System.out.println("El curso:" + curso);
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public static void obtenerCursoApi() {
        String url = "http://localhost:9000/api/curso/1";
        OkHttpClient cliente = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .build();

        try (Response response = cliente.newCall(request).execute()) {
            if (response.isSuccessful()) {
                Gson gson = new Gson();
                Curso curso = gson.fromJson(response.body().string(), new TypeToken<Curso>() {
                }.getType());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Obtenemos una lista de objetos
    public static void pruebaClienteJsonGsonCursos() {
        String url = "http://localhost:9001/api/cursos";
        OkHttpClient cliente = new OkHttpClient();
        Request request = new Request.Builder().url(url).build();

        try (Response response = cliente.newCall(request).execute()) {
            if (response.isSuccessful()) {
                String json = response.body().string();
                Gson gson = new Gson();
                TypeToken<List<Curso>> typeToken = new TypeToken<List<Curso>>() {
                };
                List<Curso> cursos = gson.fromJson(json, typeToken.getType());

                System.out.println("Los cursos:" + cursos);
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

}
