import java.util.*;

public class Grafo {
    private final ArrayList<ArrayList<Arista>> listaDeAdyacencia;
    private final int cantidadDeVertices;

    public static void main(String[] args) {
        Grafo grafo = new Grafo(5);
//        grafo.agregarAristaBidireccional(1, 2, 1);
//        grafo.agregarAristaBidireccional(2, 4, 2);
//        grafo.agregarAristaBidireccional(4, 3, 3);
//        grafo.agregarAristaBidireccional(3, 1, 4);
//        grafo.agregarAristaBidireccional(1, 5, 5);
//        grafo.agregarAristaBidireccional(5, 2, 6);
//        grafo.agregarAristaBidireccional(5, 4, 7);
//        grafo.agregarAristaBidireccional(5, 3, 8);
        grafo.agregarArista(1, 2, 4);
        grafo.agregarArista(1, 3, 2);
        grafo.agregarArista(2, 3, 3);
        grafo.agregarArista(3, 2, 1);
        grafo.agregarArista(2, 4, 2);
        grafo.agregarArista(2, 5, 3);
        grafo.agregarArista(3, 4, 4);
        grafo.agregarArista(3, 5, 5);
        grafo.agregarArista(4, 5, 1);
        System.out.println(Arrays.toString(grafo.dijkstra(0)));
//        Grafo resultado = grafo.kruskal();
//        System.out.println(resultado.toString());
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        int[][] res = new int[cantidadDeVertices][cantidadDeVertices];
        for (int i = 0; i < cantidadDeVertices; i++) {
            for (Arista arista : listaDeAdyacencia.get(i)) {
                res[i][arista.getHasta()] = arista.getCosto();
            }
        }
        for (int[] fila : res) {
            for (int i = 0; i < fila.length; i++) {
                sb.append(fila[i]).append(i == fila.length - 1 ? "" : ", ");
            }
            sb.append('\n');
        }
        return sb.toString();

    }

    private void agregarAristaBidireccional(int desde, int hasta, int costo) {
        agregarArista(desde, hasta, costo);
        agregarArista(hasta, desde, costo);
    }

    public int getPuentes() {
        int puentes = 0;
        for (ArrayList<Arista> aristas : listaDeAdyacencia) {
            for (Arista arista : aristas) {
                if (arista.getCosto() == 5) {
                    puentes++;
                }
            }
        }
        return puentes;
    }

    public Grafo(int cantidadDeVertices) {
        this.cantidadDeVertices = cantidadDeVertices;
        listaDeAdyacencia = new ArrayList<ArrayList<Arista>>();
        for (int i = 0; i < cantidadDeVertices; i++) {
            listaDeAdyacencia.add(new ArrayList<Arista>());
        }
    }

    public void agregarArista(int desde, int hasta, int costo) {
        listaDeAdyacencia.get(desde - 1).add(new Arista(desde - 1, hasta - 1, costo));
    }

    public ArrayList<Arista> getAristas(int vertice) {
        return listaDeAdyacencia.get(vertice);
    }

    public int getCantidadDeVertices() {
        return cantidadDeVertices;
    }

    public int getCantidadDeAristas() {
        int cantidadDeAristas = 0;
        for (ArrayList<Arista> aristas : listaDeAdyacencia) {
            cantidadDeAristas += aristas.size();
        }
        return cantidadDeAristas;
    }

    public int[] dijkstra(int nodoInicial) {
        int[] distancias = new int[cantidadDeVertices];
        boolean[] visitados = new boolean[cantidadDeVertices];
        Arrays.fill(distancias, Integer.MAX_VALUE);

        PriorityQueue<Arista> cola = new PriorityQueue<Arista>();

        cola.add(new Arista(0, nodoInicial, 0));
        distancias[nodoInicial] = 0;
        while (!cola.isEmpty()) {               // O(E)
            Arista arista = cola.poll();        //O(log(E))
            if (visitados[arista.getHasta()]) continue;

            int nodoActual = arista.getHasta();
            visitados[nodoActual] = true;
            distancias[nodoActual] = arista.getCosto();

            for (Arista aristaAdyacente : listaDeAdyacencia.get(nodoActual)) { // O(V)
                if (!visitados[aristaAdyacente.getHasta()] && distancias[aristaAdyacente.getHasta()] > distancias[nodoActual] + aristaAdyacente.getCosto()) {
                    distancias[aristaAdyacente.getHasta()] = distancias[nodoActual] + aristaAdyacente.getCosto();
                    cola.add(new Arista(0, aristaAdyacente.getHasta(), distancias[aristaAdyacente.getHasta()])); // O(log(E))
                }
            }
        }

        System.out.println(Arrays.toString(distancias));
        return distancias;
    }

    public Grafo prim() {
        Grafo arbol = new Grafo(cantidadDeVertices);
        boolean[] visitados = new boolean[cantidadDeVertices];
        PriorityQueue<Arista> cola = new PriorityQueue<Arista>();
        cola.add(new Arista(0, 0, 0));
        while (!cola.isEmpty()) {
            Arista arista = cola.poll();
            if (visitados[arista.getHasta()]) continue;
            visitados[arista.getHasta()] = true;
            arbol.agregarArista(arista.getDesde(), arista.getHasta(), arista.getCosto());
            for (Arista ady : listaDeAdyacencia.get(arista.getHasta())) {
                if (!visitados[ady.getHasta()]) {
                    cola.add(ady);
                }
            }
        }

        return arbol;
    }

    private static class Nodo implements Comparable<Nodo> {
        private final int id;
        private final int grado;

        public Nodo(int id, int grado) {
            this.id = id;
            this.grado = grado;
        }

        public int compareTo(Nodo o) {
            return grado - o.grado;
        }
    }

    public int[] coloreo() {
        PriorityQueue<Nodo> nodos = new PriorityQueue<>();
        for (int i = 0; i < cantidadDeVertices; i++) {
            nodos.add(new Nodo(i, listaDeAdyacencia.get(i).size()));
        }
        int[] colores = new int[cantidadDeVertices];
        Arrays.fill(colores, -1);

        while (!nodos.isEmpty()) {                                      //O(V)
            int color = 0;
            Nodo nodo = nodos.poll();                                   //O(log(V))
            while (!puedeColorear(nodo.id, color, colores)) {           //O(V) * O(V)
                color++;
            }
            colores[nodo.id] = color;
        }
        return colores;
    }

    private boolean puedeColorear(int id, int color, int[] colores) {       //O(V)
        for (Arista arista : listaDeAdyacencia.get(id)) {
            if (colores[arista.getHasta()] == color) {
                return false;
            }
        }
        return true;
    }

    private void floyd() {
        int[][] distancias = new int[cantidadDeVertices][cantidadDeVertices];
        for (int i = 0; i < cantidadDeVertices; i++) {
            for (int j = 0; j < cantidadDeVertices; j++) {
                distancias[i][j] = Integer.MAX_VALUE;
            }
        }
        for (ArrayList<Arista> aristas : listaDeAdyacencia) {
            for (Arista arista : aristas) {
                distancias[arista.getDesde()][arista.getHasta()] = arista.getCosto();
            }
        }
        for (int k = 0; k < cantidadDeVertices; k++) {
            for (int i = 0; i < cantidadDeVertices; i++) {
                for (int j = 0; j < cantidadDeVertices; j++) {
                    if (distancias[i][k] != Integer.MAX_VALUE && distancias[k][j] != Integer.MAX_VALUE) {
                        distancias[i][j] = Math.min(distancias[i][j], distancias[i][k] + distancias[k][j]);
                    }
                }
            }
        }
        PriorityQueue<Integer> cola = new PriorityQueue<>();
        for (int i = 0; i < cantidadDeVertices; i++) {
            cola.add(distancias[i][i]);
        }
        System.out.println(cola.poll());
    }


    public Grafo kruskal() {
        Grafo arbol = new Grafo(cantidadDeVertices);
        PriorityQueue<Arista> aristas = new PriorityQueue<>();
        for (ArrayList<Arista> aristasDeUnNodo : listaDeAdyacencia) {
            aristas.addAll(aristasDeUnNodo);
        }
        UnionFind unionFind = new UnionFind(cantidadDeVertices);

        while (arbol.getCantidadDeAristas() < cantidadDeVertices * 2 - 1) {
            if (aristas.isEmpty()) break;
            Arista arista = aristas.poll();
            if (unionFind.find(arista.getDesde()) != unionFind.find(arista.getHasta())) {
                unionFind.union(arista.getDesde(), arista.getHasta());
                arbol.agregarAristaBidireccional(arista.getDesde() + 1, arista.getHasta() + 1, arista.getCosto());
            }
        }

        return arbol;
    }
}
