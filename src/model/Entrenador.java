package model;

public class Entrenador {
	
	private String usuario;
	private String pass;
	private int pokedolares;
	
	public Entrenador(String usuario, String pass, int pokedolares) {
		super();
		this.usuario = usuario;
		this.pass = pass;
		this.pokedolares = pokedolares;
	}
	
	public Entrenador() {
		super();
		this.usuario = "";
		this.pass = "";
		this.pokedolares = 0;
	}
	
	public Entrenador(Entrenador c) {
		super();
		this.usuario = c.usuario;
		this.pass = c.pass;
		this.pokedolares = c.pokedolares;
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
	
	
	
	

}
