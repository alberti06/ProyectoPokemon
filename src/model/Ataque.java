package model;

public class Ataque {
	private int id;
	private String nombre;
	private int nivelAprendizaje;
	private int ppMax;
	private int ppActual;
	private String tipo; // ATAQUE, ESTADO, MEJORA
	private int potencia; // Puede ser NULL si es de tipo ESTADO
	private String tipoMovimiento; // FUEGO, PLANTA, etc.
	private String estado; // Envenenado, Dormido... o NULL
	private Integer turnos; // Duración del estado si aplica
	private String mejora; // Atributo que mejora/reduce
	private Integer modificador; // Cuánto mejora/reduce
	
	public Ataque(int id, String nombre, int nivelAprendizaje, int ppMax, Integer ppActual, String tipo, Integer potencia,
            String tipoMovimiento, String estado, Integer turnos, String mejora, Integer modificador) {
	this.id = id;
	this.nombre = nombre;
	this.nivelAprendizaje = nivelAprendizaje;
	this.ppMax = ppMax;
	this.ppActual = (ppActual == null || ppActual == 0) ? ppMax : ppActual; 
	this.tipo = tipo;
	this.potencia = potencia != null ? potencia : 0;
	this.tipoMovimiento = tipoMovimiento;
	this.estado = estado;
	this.turnos = turnos;
	this.mejora = mejora;
	this.modificador = modificador;
}

	// Constructor más simple (útil para botones de combate)
	public Ataque(String nombre, String tipoMovimiento, int potencia, int pp) {
		this.nombre = nombre;
		this.tipoMovimiento = tipoMovimiento;
		this.potencia = potencia;
		this.ppMax = pp;
		this.ppActual = pp;
		this.tipo = "ATAQUE";
	}

	// Getters
	public int getId() {
		return id;
	}

	public String getNombre() {
		return nombre;
	}

	public int getNivelAprendizaje() {
		return nivelAprendizaje;
	}

	public int getPpMax() {
		return ppMax;
	}

	public int getPpActual() {
		return ppActual;
	}

	public String getTipo() {
		return tipo;
	}

	public int getPotencia() {
		return potencia;
	}

	public String getTipoMovimiento() {
		return tipoMovimiento;
	}

	public String getEstado() {
		return estado;
	}

	public Integer getTurnos() {
		return turnos;
	}

	public String getMejora() {
		return mejora;
	}

	public Integer getModificador() {
		return modificador;
	}

	// Lógica de combate
	public void reducirPP() {
		if (ppActual > 0) {
			ppActual--;
		}
	}

	public boolean tienePP() {
		return ppActual > 0;
	}

	public void restaurarPP() {
		this.ppActual = this.ppMax;
	}

	public boolean esOfensivo() {
		return "ATAQUE".equalsIgnoreCase(tipo);
	}

	public boolean esEstado() {
		return "ESTADO".equalsIgnoreCase(tipo);
	}

	public boolean esMejora() {
		return "MEJORA".equalsIgnoreCase(tipo);
	}

	@Override
	public String toString() {
		return nombre + " (" + tipoMovimiento + ", " + ppActual + "/" + ppMax + ")";
	}

}
