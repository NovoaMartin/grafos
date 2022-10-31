public class UnionFind {
    int[] elementos;
    int[] tamanios;
    int cantidadDeConjuntos;

    public UnionFind(int n) {
        elementos = new int[n];
        tamanios = new int[n];
        for (int i = 0; i < n; i++) {
            elementos[i] = i;
            tamanios[i] = 1;
        }
        cantidadDeConjuntos = n;
    }

    public int find(int x) {
        int a = x;
        while (elementos[a] != a) {
            a = elementos[a];
        }
        return a;
    }

    public void union(int x, int y) {
        int a = find(x);
        int b = find(y);

        if (a == b) return;

        if (tamanios[b] < tamanios[a]) {
            int aux = a;
            a = b;
            b = aux;
        }

        elementos[a] = b;
        tamanios[b] += tamanios[a];
        tamanios[a] = 0;
        cantidadDeConjuntos--;
    }
}
