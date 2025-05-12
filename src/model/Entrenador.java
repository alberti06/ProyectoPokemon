package model;

import java.util.List;

public class Entrenador {
	
	private int identrenador;
	private String usuario;
	private String pass;
	private int pokedolares;
	private List<Pokemon> pokemons; // Lista con todos sus Pokémon

	public List<Pokemon> getPokemons() {
	    return pokemons;
	}

	public void setPokemons(List<Pokemon> pokemons) {
	    this.pokemons = pokemons;
	}

	
	public Entrenador(String usuario, String pass, int pokedolares, int identrenador) {
	    this.usuario = usuario;
	    this.pass = pass;
	    this.pokedolares = pokedolares;
	    this.identrenador = identrenador;
	}
	
	public Entrenador(String usuario, String pass, int pokedolares) {
	    this.usuario = usuario;
	    this.pass = pass;
	    this.pokedolares = pokedolares;
	    this.identrenador = 0; // Por defecto, hasta que se establezca desde la BD
	}
	
	public Entrenador() {
		super();
		this.usuario = "";
		this.pass = "";
		this.pokedolares = 0;
		this.identrenador = 0;
	}
	
	public Entrenador(Entrenador c) {
		super();
		this.usuario = c.usuario;
		this.pass = c.pass;
		this.pokedolares = c.pokedolares;
		this.identrenador = c.identrenador;
	}

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
	public Pokemon getPrimerPokemon() {
	    for (Pokemon p : this.pokemons) { // Asume que tienes una lista llamada 'pokemons'
	        if (p.getEquipo() == 1) {
	            return p;
	        }
	    }
	    // Si no encuentra ninguno con equipo 1, devuelve el primero de la lista (opcional)
	    if (!pokemons.isEmpty()) {
	        return pokemons.get(0);
	    }
	    return null; // o lanza excepción si prefieres
	}

	@Override
	public String toString() {
		return "Entrenador [identrenador=" + identrenador + ", usuario=" + usuario + ", pass=" + pass + ", pokedolares="
				+ pokedolares + "]";
	}

	
	
	
	
	

}
