package com.example.blade.curso;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class CursoDao {
        
    private ArrayList<Curso> cursos= new ArrayList<>(Arrays.asList(
     new Curso(1, "Nada"), new Curso(2, "Caca")
    ));

    private static CursoDao cursoDao = null;

    private CursoDao(){};

    public static CursoDao instancia(){
        if (cursoDao==null){
            cursoDao=new CursoDao();
            return cursoDao;
        }
        return cursoDao;        
    }

    Optional<Curso> getCursoByCodigo(int cod){
        return cursos.stream()
          .filter(c -> c.getCodigo() == cod)
          .findAny();
    }

    Iterable<String> getAllNombresCursos() {
        return cursos.stream()
          .map(curso -> curso.getNombre())
          .collect(Collectors.toList());
    }

    List<Curso> getAllCursos() {
        return cursos;
        // return cursos.stream()
        //   .collect(Collectors.toList());
    }

    Optional<Curso> updateCurso(int cod, String newNombre){
        Optional<Curso> curso=cursos.stream().filter(c->c.getCodigo()==cod).findAny();
        if (curso.isPresent()){
            curso.get().setNombre(newNombre);
        }
        return curso;
    }

    void addCurso(int cod, String nombre){
        cursos.add(new Curso(cod,nombre));
    }    


    boolean addCurso(Curso newc){
        Optional<Curso> curso=cursos.stream().filter(c->c.getCodigo()==newc.getCodigo()).findAny();
        if (!curso.isPresent() && newc.getCodigo()!=0){
            cursos.add(newc);
            return true;
        }
        return false;
    }   
}
