public class Arista implements Comparable<Arista> {
    private final int desde;
    private final int hasta;
    private int costo;

    public Arista(int desde, int hasta, int costo) {
        this.desde = desde;
        this.hasta = hasta;
        this.costo = costo;
    }

    public int getDesde() {
        return desde;
    }

    public int getHasta() {
        return hasta;
    }

    public int getCosto() {
        return costo;
    }

    public void setCosto(int costo) {
        this.costo = costo;
    }

    @Override
    public int compareTo(Arista o) {
        return Integer.compare(this.costo, o.costo);
    }
}
