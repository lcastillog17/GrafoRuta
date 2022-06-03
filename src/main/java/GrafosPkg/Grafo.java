/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GrafosPkg;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Stack;
//import java.util.Vector;

/**
 *
 * @author Ruldin Ayala
 */
public class Grafo {
    String[]  nodos;  // Letras de identificación de nodo
    int[][] grafo;  // Matriz de distancias entre nodos
    String  rutaMasCorta;                           // distancia más corta
    int     longitudMasCorta = Integer.MAX_VALUE;   // ruta más corta
    List<Nodo>  listos=null;                        // nodos revisados Dijkstra

    // construye el grafo con la serie de identificadores de nodo en una cadena
    public Grafo(String serieNodos) {
        nodos = serieNodos.split("-"); //el toCharArray devuelve un vector con las letras del string que le enviemos
        grafo = new int[nodos.length][nodos.length];
    }

    // asigna el tamaño de la arista entre dos nodos
    //esto es para meter las distancias a la tabla
    public void agregarRuta(String origen, String destino, int distancia) {
        int n1 = posicionNodo(origen);
        int n2 = posicionNodo(destino);
        grafo[n1][n2]=distancia;
        grafo[n2][n1]=distancia;
    }

    // retorna la posición en el arreglo de un nodo específico
    private int posicionNodo(String nodo) {
        for(int i=0; i<nodos.length; i++) {
            if(nodos[i].equals(nodo)) return i;
        }
        return -1;
    }
    
    
    
    // encuentra la ruta más corta desde un nodo origen a un nodo destino
    public String encontrarRutaMinimaDijkstra(String inicio, String fin) {
        // calcula la ruta más corta del inicio a los demás
        encontrarRutaMinimaDijkstra(inicio);
        // recupera el nodo final de la lista de terminados
        Nodo tmp = new Nodo(fin);//se guarda el nodo final en tmp
        if(!listos.contains(tmp)) {//si listos no contiene a tmp //el contains es para ver si en la lista listos existe tmp
            System.out.println("Error, nodo no alcanzable");
            return "Bye";
        }

        tmp = listos.get(listos.indexOf(tmp));//devuelve el indice de tmp de la lista listos OSEA EL INDICE DE FIN
        int distancia = tmp.distancia;  //guarda la distancia del nodo final
        // crea una pila para almacenar la ruta desde el nodo final al origen
        Stack<Nodo> pila = new Stack<Nodo>();//se usa un stack para dejar hasta abajo al fin e ir subiendo hasta llegar al origen
        while(tmp != null) {
            pila.add(tmp);//agrego a la pila tmp que sería el fin
            tmp = tmp.procedencia;//agrego en tmp la  procedencia osea de donde viene que seria el nodo anterior al ultimo
        }
        String ruta = "";
        // recorre la pila para armar la ruta en el orden correcto
         while(!pila.isEmpty()) ruta+=(pila.pop().nombre + (pila.size()>0 ?" - ":"."));
        return distancia + " Km" + ": " + ruta;
    }

    // encuentra la ruta más corta desde el nodo inicial a todos los demás
    public void encontrarRutaMinimaDijkstra(String inicio) {
        Queue<Nodo>   cola = new PriorityQueue<Nodo>(); // cola de prioridad (cola es primero que entra, primero que sale
        Nodo            ni = new Nodo(inicio);          // nodo inicial sse crea nada mas con el nombre
        listos = new LinkedList<Nodo>();// lista de nodos ya revisados
        cola.add(ni);                   // Agregar nodo inicial a la cola de prioridad
        while(!cola.isEmpty()) {        // mientras que la cola no esta vacia
             Nodo tmp = cola.poll();  // Elimina la cabeza de la cola /saca el primer elemento
            listos.add(tmp);            // lo manda a la lista de terminados
            int p = posicionNodo(tmp.nombre);   //busca la posicion del nodo donde está el nombre de la cabeza 
            for(int j=0; j<grafo[p].length; j++) { //revisa las filas del nodo tmp // revisa los nodos hijos del nodo tmp
                if(grafo[p][j]==0) continue;        // si no hay conexión entre loso nodos no lo evalua
                if(estaTerminado(j)) continue;      // si ya fue agregado a la lista de terminados
                Nodo nod = new Nodo(nodos[j],tmp.distancia+grafo[p][j],tmp); //crea un nodo mandandole hoy los datos de nombre, dist y procedencia
           // si no está en la cola de prioridad, lo agrega
                if(!cola.contains(nod)) {
                    cola.add(nod);
                    continue;
                }
                // si ya está en la cola de prioridad actualiza la distancia menor
                for(Nodo x: cola) {
                    // si la distancia en la cola es mayor que la distancia calculada
                    if(x.nombre.equals(nod.nombre) && x.distancia > nod.distancia) {
                        cola.remove(x); // remueve el nodo de la cola
                        cola.add(nod);  // agrega el nodo con la nueva distancia
                        //por eso se usa aca la cola de prioridad, para sacar un dato por algun
                        //motivo por alguna conidicon no importando si le toca salir o no
                        break;          // no sigue revisando
                    }
                }
            }
        }
    }

    // verifica si un nodo ya está en lista de terminados
    public boolean estaTerminado(int j) {
        Nodo tmp = new Nodo(nodos[j]);
        return listos.contains(tmp);
    }

    // encontrar la ruta mínima por fuerza bruta
    public void encontrarRutaMinimaFuerzaBruta(String inicio, String fin) {
        int p1 = posicionNodo(inicio);
        int p2 = posicionNodo(fin);
        // cola para almacenar cada ruta que está siendo evaluada
        Stack<Integer> resultado = new Stack<Integer>();
        resultado.push(p1);
        recorrerRutas(p1, p2, resultado);
    }

    // recorre recursivamente las rutas entre un nodo inicial y un nodo final
    // almacenando en una cola cada nodo visitado
    private void recorrerRutas(int nodoI, int nodoF, Stack<Integer> resultado) {
        // si el nodo inicial es igual al final se evalúa la ruta en revisión
        if(nodoI==nodoF) {
            int respuesta = evaluar(resultado);
            if(respuesta < longitudMasCorta) {
                longitudMasCorta = respuesta;
                rutaMasCorta     = "";
                for(int x: resultado) rutaMasCorta+=(nodos[x]+" ");
            }
            return;
        }
        // Si el nodoInicial no es igual al final se crea una lista con todos los nodos
        // adyacentes al nodo inicial que no estén en la ruta en evaluación
        List<Integer> lista = new ArrayList<Integer>();
        for(int i=0; i<grafo.length;i++) {
            if(grafo[nodoI][i]!=0 && !resultado.contains(i))lista.add(i);//es para agregar las rutas al grafo
        }
        // se recorren todas las rutas formadas con los nodos adyacentes al inicial
        for(int nodo: lista) {
            resultado.push(nodo);
            recorrerRutas(nodo, nodoF, resultado);
            resultado.pop();
        }
    }
    
    

    // evaluar la longitud de una ruta
    //este metodo hace la suma de las distancias, hace como la ruta y devuelve los kilometros
    public int evaluar(Stack<Integer> resultado) {
        int  resp = 0;
        int[]   r = new int[resultado.size()];
        int     i = 0;
        for(int x: resultado) r[i++]=x;
        for(i=1; i<r.length; i++) resp+=grafo[r[i]][r[i-1]];//resp=resp+grafo....
        return resp;
    }

}
