package model;

import java.util.ArrayList;
import java.util.List;

public class Entrenador {

    private int identrenador;
    private String usuario;
    private String pass;
    private int pokedolares;
    private List<Pokemon> pokemons; // Lista con todos sus Pokémon

    // Constructor principal con todos los datos
    public Entrenador(String usuario, String pass, int pokedolares, int identrenador) {
        this.usuario = usuario;
        this.pass = pass;
        this.pokedolares = pokedolares;
        this.identrenador = identrenador;
        this.pokemons = new ArrayList<>(); // Inicializa lista
    }

    // Constructor alternativo
    public Entrenador(String usuario, String pass, int pokedolares) {
        this.usuario = usuario;
        this.pass = pass;
        this.pokedolares = pokedolares;
        this.identrenador = 0;
        this.pokemons = new ArrayList<>();
    }

    // Constructor vacío
    public Entrenador() {
        this.usuario = "";
        this.pass = "";
        this.pokedolares = 0;
        this.identrenador = 0;
        this.pokemons = new ArrayList<>();
    }

    // Constructor copia
    public Entrenador(Entrenador c) {
        this.usuario = c.usuario;
        this.pass = c.pass;
        this.pokedolares = c.pokedolares;
        this.identrenador = c.identrenador;
        this.pokemons = new ArrayList<>(c.pokemons); // Copia segura
    }

    // Getters y Setters
    public int getIdentrenador() {
        return identrenador;
    }

    public void setIdentrenador(int identrenador) {
        this.identrenador = identrenador;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public int getPokedolares() {
        return pokedolares;
    }

    public void setPokedolares(int pokedolares) {
        this.pokedolares = pokedolares;
    }

    public List<Pokemon> getPokemons() {
        return pokemons;
    }

    public void setPokemons(List<Pokemon> pokemons) {
        this.pokemons = pokemons != null ? pokemons : new ArrayList<>();
    }

    public Pokemon getPrimerPokemon() {
        if (pokemons == null || pokemons.isEmpty()) {
            throw new IllegalStateException("El entrenador no tiene ningún Pokémon.");
        }

        for (Pokemon p : pokemons) {
            if (p.getEquipo() == 1) {
                return p;
            }
        }
        return pokemons.get(0); // Devuelve el primero aunque no esté en equipo 1
    }
    public Pokemon getPokemonDeEquipo(int posicion) {
        if (pokemons == null || pokemons.isEmpty()) {
            throw new IllegalStateException("El entrenador no tiene ningún Pokémon.");
        }

        for (Pokemon p : pokemons) {
            if (p.getEquipo() == posicion) {
                return p;
            }
        }

        throw new IllegalArgumentException("No hay Pokémon en la posición de equipo: " + posicion);
    }

    @Override
    public String toString() {
        return "Entrenador [identrenador=" + identrenador + ", usuario=" + usuario +
                ", pass=" + pass + ", pokedolares=" + pokedolares + "]";
    }
}