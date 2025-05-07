package model;

public class Objeto {
    private int id;
    private String nombre;
    private int ataque;
    private int ataqueEspecial;
    private int defensa;
    private int defensaEspecial;
    private int velocidad;
    private int precio;

    public Objeto(int id, String nombre, int ataque, int ataqueEspecial, int defensa,
                  int defensaEspecial, int velocidad, int precio) {
        this.id = id;
        this.nombre = nombre;
        this.ataque = ataque;
        this.ataqueEspecial = ataqueEspecial;
        this.defensa = defensa;
        this.defensaEspecial = defensaEspecial;
        this.velocidad = velocidad;
        this.precio = precio;
    }

    // Getters y setters
    public int getId() { return id; }
    public String getNombre() { return nombre; }
    public int getAtaque() { return ataque; }
    public int getAtaqueEspecial() { return ataqueEspecial; }
    public int getDefensa() { return defensa; }
    public int getDefensaEspecial() { return defensaEspecial; }
    public int getVelocidad() { return velocidad; }
    public int getPrecio() { return precio; }

    @Override
    public String toString() {
        return nombre + " (ID: " + id + ")";
    }
}