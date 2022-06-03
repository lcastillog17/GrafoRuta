/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GrafosPkg;

import java.util.Objects;

/**
 *
 * @author Ruldin Ayala
 * Preguntas:
 * Para qué sirve la implementación Comparable?
 * y porqué es de tipo Nodo
 */
//la interface comparable es para comparar objetos
public class Nodo implements Comparable<Nodo> {
    String nombre;
    int  distancia   = Integer.MAX_VALUE; //le ponemos el valor mas alto para comparar y obtener el mas corto
    Nodo procedencia = null;
    
    public Nodo(String x, int d, Nodo p) { 
        nombre=x; distancia=d; procedencia=p; 
    }
    
    public Nodo(String x) { 
        this(x, 0, null); //crea un nodo solo con nombre sin conectarse a nadad
    }
    
    @Override
    public int compareTo(Nodo tmp) { 
        return this.distancia-tmp.distancia; 
    }

//    @Override
//    public int hashCode() {
//        int hash = 3;
//        hash = 13 * hash + Objects.hashCode(this.nombre);
////        hash = 13 * hash + this.distancia;
////        hash = 13 * hash + Objects.hashCode(this.procedencia);
//        return hash;
//    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Nodo other = (Nodo) obj;
//        if (this.distancia != other.distancia) {
//            return false;
//        }
        if (!Objects.equals(this.nombre, other.nombre)) {
            return false;
        }
//        if (!Objects.equals(this.procedencia, other.procedencia)) {
//            return false;
//        }
        return true;
    }
    

}
