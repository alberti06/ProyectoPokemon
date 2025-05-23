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
    private String sexo;
    private String estado;
    private int equipo;
    private String imgFrontal;
    private String imgTrasera;
    private String sonido;
    private Integer nivelEvolucion;
    private int vidaActual;

    public int getVidaActual() {
        return vidaActual;
    }

    public void setVidaActual(int vidaActual) {
        this.vidaActual = Math.max(0, vidaActual);
    }

    public void reducirVida(int cantidad) {
        setVidaActual(vidaActual - cantidad);
    }


	// Constructor completo
    public Pokemon(int id, int idEntrenador, int numPokedex, String nombre,
                   String tipo1, String tipo2, int vitalidad, int ataque, int defensa,
                   int atEspecial, int defEspecial, int velocidad, int nivel,
                   int fertilidad, String sexo, String estado, int equipo,
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

    // Constructor compatible con inserción básica
    public Pokemon(int id, int idEntrenador, int numPokedex, String nombre,
                   int vitalidad, int ataque, int defensa, int atEspecial, int defEspecial,
                   int velocidad, int nivel, int fertilidad, String sexo, String estado, int equipo) {
        this.id = id;
        this.idEntrenador = idEntrenador;
        this.numPokedex = numPokedex;
        this.nombre = nombre;
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
        this.tipo1 = null;
        this.tipo2 = null;
        this.imgFrontal = null;
        this.imgTrasera = null;
        this.sonido = null;
        this.nivelEvolucion = null;
    }

    // Constructor simplificado para combate o Pokémon salvajes
    public Pokemon(int numPokedex, String nombre, int nivel, int vitalidad, int ataque, String sexo) {
        this.numPokedex = numPokedex;
        this.nombre = nombre;
        this.nivel = nivel;
        this.vitalidad = vitalidad;
        this.ataque = ataque;
        this.sexo = sexo;
    }
    
    public Pokemon(int id, int idEntrenador, int numPokedex, String nombre, String tipo1, String tipo2,
            int vitalidad, int vidaActual, int ataque, int defensa, int atEspecial, int defEspecial,
            int velocidad, int nivel, int fertilidad, String sexo, String estado, int equipo,
            String imgFrontal, String imgTrasera, String sonido, Integer nivelEvolucion) {
 this.id = id;
 this.idEntrenador = idEntrenador;
 this.numPokedex = numPokedex;
 this.nombre = nombre;
 this.tipo1 = tipo1;
 this.tipo2 = tipo2;
 this.vitalidad = vitalidad;
 this.vidaActual = vidaActual;
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

    public Pokemon(int numPokedex, String nombre, int nivel, int vitalidad, int ataque) {
        this.numPokedex = numPokedex;
        this.nombre = nombre;
        this.nivel = nivel;
        this.vitalidad = vitalidad;
        this.ataque = ataque;
        this.sexo = "M"; 
    }

    // Getters
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
    public String getSexo() { return sexo; }
    public String getEstado() { return estado; }
    public int getEquipo() { return equipo; }
    public String getImgFrontal() { return imgFrontal; }
    public String getImgTrasera() { return imgTrasera; }
    public String getSonido() { return sonido; }
    public Integer getNivelEvolucion() { return nivelEvolucion; }

    // Setters
    public void setId(int id) { this.id = id; }
    public void setIdEntrenador(int idEntrenador) { this.idEntrenador = idEntrenador; }
    public void setNumPokedex(int numPokedex) { this.numPokedex = numPokedex; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public void setTipo1(String tipo1) { this.tipo1 = tipo1; }
    public void setTipo2(String tipo2) { this.tipo2 = tipo2; }
    public void setVitalidad(int vitalidad) { this.vitalidad = vitalidad; }
    public void setAtaque(int ataque) { this.ataque = ataque; }
    public void setDefensa(int defensa) { this.defensa = defensa; }
    public void setAtEspecial(int atEspecial) { this.atEspecial = atEspecial; }
    public void setDefEspecial(int defEspecial) { this.defEspecial = defEspecial; }
    public void setVelocidad(int velocidad) { this.velocidad = velocidad; }
    public void setNivel(int nivel) { this.nivel = nivel; }
    public void setFertilidad(int fertilidad) { this.fertilidad = fertilidad; }
    public void setSexo(String sexo) { this.sexo = sexo; }
    public void setEstado(String estado) { this.estado = estado; }
    public void setEquipo(int equipo) { this.equipo = equipo; }
    public void setImgFrontal(String imgFrontal) { this.imgFrontal = imgFrontal; }
    public void setImgTrasera(String imgTrasera) { this.imgTrasera = imgTrasera; }
    public void setSonido(String sonido) { this.sonido = sonido; }
    public void setNivelEvolucion(Integer nivelEvolucion) { this.nivelEvolucion = nivelEvolucion; }


    public boolean estaDebilitado() {
        return this.vidaActual <= 0;
    }


    public boolean puedeEvolucionar() {
        return nivelEvolucion != null && nivel >= nivelEvolucion;
    }

    public String resumenCombate() {
        return nombre + " (Nv. " + nivel + ", Vida: " + vitalidad + ")";
    }
    
    @Override
    public String toString() {
        return nombre + " (Nv. " + nivel + ", Fertilidad: " + fertilidad + ")";
    }
}
