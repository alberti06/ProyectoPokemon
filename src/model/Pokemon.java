package model;

public class Pokemon {
    private int id;
    private int idEntrenador;
    private int numPokedex;
    private String nombre;
    private String tipo1;
    private String tipo2;
    private int vitalidad;
    private int ataque;
    private int defensa;
    private int atEspecial;
    private int defEspecial;
    private int velocidad;
    private int nivel;
    private int fertilidad;
    private char sexo;
    private String estado;
    private int equipo;
    private String imgFrontal;
    private String imgTrasera;
    private String sonido;
    private Integer nivelEvolucion; // Puede ser NULL

    // Constructor completo
    public Pokemon(int id, int idEntrenador, int numPokedex, String nombre,
                   String tipo1, String tipo2, int vitalidad, int ataque, int defensa,
                   int atEspecial, int defEspecial, int velocidad, int nivel,
                   int fertilidad, char sexo, String estado, int equipo,
                   String imgFrontal, String imgTrasera, String sonido, Integer nivelEvolucion) {
        this.id = id;
        this.idEntrenador = idEntrenador;
        this.numPokedex = numPokedex;
        this.nombre = nombre;
        this.tipo1 = tipo1;
        this.tipo2 = tipo2;
        this.vitalidad = vitalidad;
        this.ataque = ataque;
        this.defensa = defensa;
        this.atEspecial = atEspecial;
        this.defEspecial = defEspecial;
        this.velocidad = velocidad;
        this.nivel = nivel;
        this.fertilidad = fertilidad;
        this.sexo = sexo;
        this.estado = estado;
        this.equipo = equipo;
        this.imgFrontal = imgFrontal;
        this.imgTrasera = imgTrasera;
        this.sonido = sonido;
        this.nivelEvolucion = nivelEvolucion;
    }

    // Constructor simplificado para combate
    public Pokemon(int id, String nombre, int nivel, int vitalidad, int ataque) {
        this.id = id;
        this.nombre = nombre;
        this.nivel = nivel;
        this.vitalidad = vitalidad;
        this.ataque = ataque;
    }

    // Getters y Setters
    public int getId() { return id; }
    public int getIdEntrenador() { return idEntrenador; }
    public int getNumPokedex() { return numPokedex; }
    public String getNombre() { return nombre; }
    public String getTipo1() { return tipo1; }
    public String getTipo2() { return tipo2; }
    public int getVitalidad() { return vitalidad; }
    public int getAtaque() { return ataque; }
    public int getDefensa() { return defensa; }
    public int getAtEspecial() { return atEspecial; }
    public int getDefEspecial() { return defEspecial; }
    public int getVelocidad() { return velocidad; }
    public int getNivel() { return nivel; }
    public int getFertilidad() { return fertilidad; }
    public char getSexo() { return sexo; }
    public String getEstado() { return estado; }
    public int getEquipo() { return equipo; }
    public String getImgFrontal() { return imgFrontal; }
    public String getImgTrasera() { return imgTrasera; }
    public String getSonido() { return sonido; }
    public Integer getNivelEvolucion() { return nivelEvolucion; }

    public void setVitalidad(int vitalidad) { this.vitalidad = vitalidad; }
    public void setEstado(String estado) { this.estado = estado; }
    public void setNivel(int nivel) { this.nivel = nivel; }

    // Métodos de utilidad
    public void reducirVida(int cantidad) {
        this.vitalidad = Math.max(0, this.vitalidad - cantidad);
    }

    public boolean estaDebilitado() {
        return this.vitalidad <= 0;
    }

    public boolean puedeEvolucionar() {
        return nivelEvolucion != null && nivel >= nivelEvolucion;
    }

    @Override
    public String toString() {
        return nombre + " (Nivel " + nivel + ") - Vida: " + vitalidad;
    }
}

