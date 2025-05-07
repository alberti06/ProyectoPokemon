package model;

public class Mochila {
    private int idEntrenador;
    private int idObjeto;
    private int cantidad;

    public Mochila(int idEntrenador, int idObjeto, int cantidad) {
        this.idEntrenador = idEntrenador;
        this.idObjeto = idObjeto;
        this.cantidad = cantidad;
    }

    public int getIdEntrenador() { return idEntrenador; }
    public int getIdObjeto() { return idObjeto; }
    public int getCantidad() { return cantidad; }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }
}