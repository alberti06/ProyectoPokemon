package controller;

import java.io.File;
import java.io.IOException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import model.Ataque;
import model.Entrenador;
import model.Pokemon;

import java.io.*;
import java.sql.*;
import java.util.*;

import dao.ConexionBD;
import dao.PokemonDAO;

//añadimos los elementos de las vistas
public class EntrenamientoController {
	private List<String> nombresObjetos = new ArrayList<>();
	private List<Integer> idsObjetos = new ArrayList<>();
	private MenuController menuController;
	private Stage stage;
	private Entrenador entrenador;
	private LoginController loginController;
	private Pokemon pokemonSalvaje;
	private Pokemon pokemonAliado;
	private List<Ataque> ataquesJugador = new ArrayList<>();
	private Stage stageBolsa;
	private Stage stageEquipo;

	@FXML
	private AnchorPane anchorpane;
	@FXML
	private ImageView btnSalir;
	@FXML
	private ChoiceBox<Pokemon> chcEquipo;
	@FXML
	private ChoiceBox<String> chcObjetos;
	@FXML
	private ImageView imgPokemonEntrenador;
	@FXML
	private ImageView imgPokemonSalvaje;
	@FXML
	private ImageView imgSuelo1;
	@FXML
	private ImageView imgSuelo2;
	@FXML
	private Button btnusarObjetoCombate;
	@FXML
	private Button botonAtaque1;
	@FXML
	private Button botonAtaque2;
	@FXML
	private Button botonAtaque3;
	@FXML
	private Button botonAtaque4;
	@FXML
	private Label lblTurnos;
	@FXML
	private ProgressBar barraVidaEntrenador;
	@FXML
	private ProgressBar barraVidaSalvaje;

	private final String RUTA_LOG = "./combate_log.txt";

	public void init(Entrenador entrenador, Stage stage, MenuController menuController,
			LoginController loginController) {
		this.entrenador = entrenador;
		this.stage = stage;
		this.menuController = menuController;
		this.loginController = loginController;
		this.entrenador.setPokemons(PokemonDAO.obtenerEquipo(entrenador.getIdentrenador()));
		this.pokemonAliado = entrenador.getPrimerPokemon();
		if (this.pokemonAliado == null) {
			System.err.println(" El entrenador no tiene ningún Pokémon en el equipo.");
			return;
		}

		cargarObjetosDisponibles();
		inicializarChoiceBoxEquipo();
		generarPokemonSalvaje();
		cargarAtaquesDesdeBD();
		actualizarBotones();
		actualizarImagenPokemonSalvaje(pokemonSalvaje.getNumPokedex());
		actualizarImagenPokemonEntrenador(entrenador.getPrimerPokemon().getNumPokedex());
		actualizarBarrasVida();

		log("¡Ha comenzado un combate entre " + entrenador.getPrimerPokemon().getNombre() + " y "
				+ pokemonSalvaje.getNombre() + "!");
	}

//Metodos para mostrar el equipo 
	public void mostrarEquipoConVida() {
		List<Pokemon> equipo = PokemonDAO.obtenerEquipo(entrenador.getIdentrenador());

		if (equipo == null || equipo.isEmpty()) {
			System.out.println("No hay Pokémon en el equipo.");
			return;
		}

		System.out.println("Pokémon del equipo:");
		for (Pokemon p : equipo) {
			String info = p.getNombre() + " (Nv. " + p.getNivel() + ", PS: " + p.getVidaActual() + "/"
					+ p.getVitalidad() + ")";
			System.out.println(info);
		}
	}

	private String formatoBoton(Ataque atk) {
		return atk.getNombre() + " (" + atk.getPpActual() + "/" + atk.getPpMax() + ")";
	}

//con este metodo podemos cargar desde la conexion de la base de datos 
	private void cargarAtaquesDesdeBD() {
		ataquesJugador.clear();
		try (Connection con = ConexionBD.conectar()) {
			PreparedStatement ps = con.prepareStatement("""
						SELECT m.ID_MOVIMIENTO, m.NOM_MOVIMIENTO, m.NIVEL_APRENDIZAJE, m.PP_MAX,
						       mp.PP_ACTUALES, m.TIPO, m.POTENCIA, m.TIPO_MOV, m.ESTADO, m.TURNOS, m.MEJORA, m.NUM
						FROM MOVIMIENTO_POKEMON mp
						JOIN MOVIMIENTOS m ON mp.ID_MOVIMIENTO = m.ID_MOVIMIENTO
						WHERE mp.ID_POKEMON = ? LIMIT 4
					""");

			ps.setInt(1, pokemonAliado.getId());
			ResultSet rs = ps.executeQuery();

			while (rs.next()) {
				Ataque atk = new Ataque(rs.getInt("ID_MOVIMIENTO"), rs.getString("NOM_MOVIMIENTO"),
						rs.getInt("NIVEL_APRENDIZAJE"), rs.getInt("PP_MAX"), rs.getInt("PP_ACTUALES"),
						rs.getString("TIPO"), rs.getObject("POTENCIA") != null ? rs.getInt("POTENCIA") : null,
						rs.getString("TIPO_MOV"), rs.getString("ESTADO"),
						rs.getObject("TURNOS") != null ? rs.getInt("TURNOS") : null, rs.getString("MEJORA"),
						rs.getObject("NUM") != null ? rs.getInt("NUM") : null);
				ataquesJugador.add(atk);
			}

			if (ataquesJugador.isEmpty()) {
				ataquesJugador.add(new Ataque(1, "Placaje", 1, 30, 30, "ATAQUE", 40, "Normal", null, null, null, null));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private void log(String mensaje) {
		String timestamp = java.time.LocalDateTime.now()
				.format(java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
		String msg = "[" + timestamp + "] " + mensaje;
		try (BufferedWriter writer = new BufferedWriter(new FileWriter(RUTA_LOG, true))) {
			writer.write(msg);
			writer.newLine();
		} catch (IOException e) {
			e.printStackTrace();
		}
		lblTurnos.setText(mensaje);
	}

	@FXML
	void usarAtaque1() {
		ejecutarAtaque(0);
	}

	@FXML
	void usarAtaque2() {
		ejecutarAtaque(1);
	}

	@FXML
	void usarAtaque3() {
		ejecutarAtaque(2);
	}

	@FXML
	void usarAtaque4() {
		ejecutarAtaque(3);
	}

	private String realizarAtaqueRival() {
		double mult = calcularMultiplicadorTipo("NORMAL", pokemonAliado.getTipo1(), pokemonAliado.getTipo2());
		int daño = (int) (pokemonSalvaje.getAtaque() * mult);
		pokemonAliado.reducirVida(daño);

		String mensaje = pokemonSalvaje.getNombre() + " atacó e hizo " + daño + " de daño";
		if (mult > 1.5)
			mensaje += " ¡Es muy efectivo!";
		else if (mult < 1.0)
			mensaje += " No fue muy efectivo...";
		else if (mult == 0.0)
			mensaje += " ¡No te ha afectado!";

		return mensaje + "\n";
	}

	private int turnoEnemigo() {
		int daño = pokemonSalvaje.getAtaque();
		pokemonAliado.reducirVida(daño);
		actualizarBarrasVida();
		guardarEstadoPokemonAliado();
		return daño;
	}

	private void generarPokemonSalvaje() {
		try (Connection con = ConexionBD.conectar()) {
			Random rand = new Random();
			int nivelJugador = pokemonAliado.getNivel();
			int nivelSalvaje = Math.max(1, nivelJugador + rand.nextInt(11) - 5);

			PreparedStatement ps = con.prepareStatement("""
					    SELECT NUM_POKEDEX, NOM_POKEMON, IMG_FRONTAL, IMG_TRASERA, SONIDO, TIPO1, TIPO2, NIVEL_EVOLUCION
					    FROM POKEDEX
					    ORDER BY RAND()
					    LIMIT 1
					""");

			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				String nombre = rs.getString("NOM_POKEMON");
				double escalado = obtenerEscalado(nivelJugador, nivelSalvaje);
				int vitalidad = (int) (50 * escalado);
				int ataque = (int) (25 * escalado);

				pokemonSalvaje = new Pokemon(rs.getInt("NUM_POKEDEX"), nombre, nivelSalvaje, vitalidad, ataque);
				pokemonSalvaje.setTipo1(rs.getString("TIPO1"));
				pokemonSalvaje.setTipo2(rs.getString("TIPO2"));
				pokemonSalvaje.setVidaActual(vitalidad);

				actualizarImagenPokemonSalvaje(pokemonSalvaje.getNumPokedex());

				System.out.println("Pokémon salvaje generado: " + nombre + " Nv." + nivelSalvaje);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private double obtenerEscalado(int nivelJugador, int nivelSalvaje) {
		int diff = nivelSalvaje - nivelJugador;
		if (diff > 0)
			return Math.min(1.0 + diff * 0.1, 1.5);
		else if (diff < 0)
			return Math.max(1.0 + diff * 0.04, 0.8);
		else
			return 1.0;
	}

	private void actualizarBotones() {
		Button[] botones = { botonAtaque1, botonAtaque2, botonAtaque3, botonAtaque4 };
		for (int i = 0; i < botones.length; i++) {
			if (i < ataquesJugador.size()) {
				Ataque atk = ataquesJugador.get(i);
				botones[i].setText(atk.getNombre() + " (" + atk.getPpActual() + "/" + atk.getPpMax() + ")");
			} else {
				botones[i].setText("—");
			}
		}
	}

	private int calcularExperiencia(int nivelPropio, int nivelRival) {
		return (nivelPropio + nivelRival * 10) / 4;
	}

	private void actualizarImagenPokemonEntrenador(int id) {
		String ruta = "C:/ProyectoPokemon/resources/img/Pokemon/Back/" + String.format("%03d", id) + "_back.png";
		File f = new File(ruta);
		imgPokemonEntrenador.setImage(f.exists() ? new Image(f.toURI().toString()) : null);
	}

	private void actualizarImagenPokemonSalvaje(int id) {
		String ruta = "C:/ProyectoPokemon/resources/img/Pokemon/Front/" + String.format("%03d", id) + ".png";
		File f = new File(ruta);
		imgPokemonSalvaje.setImage(f.exists() ? new Image(f.toURI().toString()) : null);
	}

	private void actualizarBarrasVida() {
		int vidaMaxAliado = pokemonAliado.getVitalidad();
		int vidaActualAliado = pokemonAliado.getVidaActual();
		double porcentajeAliado = vidaMaxAliado > 0 ? (double) vidaActualAliado / vidaMaxAliado : 0;
		barraVidaEntrenador.setProgress(Math.max(0.0, Math.min(1.0, porcentajeAliado)));
		barraVidaEntrenador.setStyle(colorPorcentaje(porcentajeAliado));
		int vidaMaxEnemigo = pokemonSalvaje.getVitalidad();
		int vidaActualEnemigo = pokemonSalvaje.getVidaActual();
		double porcentajeEnemigo = vidaMaxEnemigo > 0 ? (double) vidaActualEnemigo / vidaMaxEnemigo : 0;
		barraVidaSalvaje.setProgress(Math.max(0.0, Math.min(1.0, porcentajeEnemigo)));
		barraVidaSalvaje.setStyle(colorPorcentaje(porcentajeEnemigo));
		guardarEstadoPokemonAliado();
	}

	private String colorPorcentaje(double p) {
		if (p >= 0.5)
			return "-fx-accent: #00cc00;";
		if (p >= 0.3)
			return "-fx-accent: #ffcc00;";
		return "-fx-accent: #cc0000;";
	}

	@FXML
	void salirMenupoke(MouseEvent event) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("../view/MenuPrincipal.fxml"));
			Parent root = loader.load();
			menuController = loader.getController();
			Scene sc = new Scene(root);
			Stage st = new Stage();
			st.setTitle("Proyecto Pokemon los 3 mosqueteros");
			st.setScene(sc);
			menuController.init(entrenador, st, loginController);
			st.show();
			this.stage.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@FXML
	void salirMenupokeForzado() {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("../view/MenuPrincipal.fxml"));
			Parent root = loader.load();
			menuController = loader.getController();
			Scene sc = new Scene(root);
			Stage st = new Stage();
			st.setTitle("Proyecto Pokemon los 3 mosqueteros");
			st.setScene(sc);
			menuController.init(entrenador, st, loginController);
			st.show();
			this.stage.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private boolean cambiarASiguientePokemonDisponible() {
		List<Pokemon> equipo = PokemonDAO.obtenerEquipo(entrenador.getIdentrenador());

		for (int i = 1; i < equipo.size(); i++) {
			Pokemon candidato = equipo.get(i);
			if (candidato.getVidaActual() > 0) {
				guardarEstadoPokemonAliado();
				Collections.swap(equipo, 0, i);
				entrenador.setPokemons(equipo);
				actualizarImagenPokemonEntrenador(candidato.getNumPokedex());
				cargarAtaquesDesdeBD();
				actualizarBotones();
				actualizarBarrasVida();
				log("¡" + candidato.getNombre() + " entra en combate!");
				return true;
			}
		}

		return false;
	}

	private void cambiarPokemonActivo(Pokemon nuevo) {
		guardarEstadoPokemonAliado();
		if (nuevo == null) {
			log(" No se ha seleccionado ningún Pokémon.");
			return;
		}

		if (nuevo.equals(pokemonAliado)) {
			log(" ¡Ese Pokémon ya está en combate!");
			return;
		}

		if (nuevo.getVidaActual() <= 0) {
			log(" No puedes enviar un Pokémon debilitado al combate.");
			return;
		}

		this.pokemonAliado = nuevo;
		cargarAtaquesDesdeBD();
		actualizarBotones();
		actualizarImagenPokemonEntrenador(nuevo.getNumPokedex());
		actualizarBarrasVida();
		log(nuevo.getNombre() + " ha sido enviado al combate!");
	}

	public void inicializarChoiceBoxEquipo() {
		List<Pokemon> equipo = PokemonDAO.obtenerEquipo(entrenador.getIdentrenador());

		chcEquipo.getItems().clear();
		chcEquipo.getItems().addAll(equipo);

		chcEquipo.setConverter(new javafx.util.StringConverter<>() {
			@Override
			public String toString(Pokemon p) {
				return p == null ? ""
						: p.getNombre() + " (Nv. " + p.getNivel() + " | PS: " + p.getVidaActual() + "/"
								+ p.getVitalidad() + ")";
			}

			@Override
			public Pokemon fromString(String s) {
				return null;
			}
		});

		chcEquipo.setOnAction(e -> {
			Pokemon seleccionado = chcEquipo.getValue();
			cambiarPokemonActivo(seleccionado);
		});
	}

	private void cargarObjetosDisponibles() {
		nombresObjetos.clear();
		idsObjetos.clear();

		if (chcObjetos == null) {
			System.err.println("ChoiceBox de objetos no está inicializado.");
			return;
		}

		chcObjetos.getItems().clear();

		try (Connection con = ConexionBD.conectar()) {
			String query = """
					    SELECT o.ID_OBJETO, o.NOM_OBJETO, m.NUMERO_OBJETOS
					    FROM MOCHILA m
					    JOIN OBJETO o ON m.ID_OBJETO = o.ID_OBJETO
					    WHERE m.ID_ENTRENADOR = ? AND m.NUMERO_OBJETOS > 0
					      AND LOWER(o.NOM_OBJETO) NOT LIKE 'pokeball'
					""";

			PreparedStatement ps = con.prepareStatement(query);
			ps.setInt(1, entrenador.getIdentrenador());

			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				int id = rs.getInt("ID_OBJETO");
				String nombre = rs.getString("NOM_OBJETO");
				int cantidad = rs.getInt("NUMERO_OBJETOS");

				idsObjetos.add(id);
				nombresObjetos.add(nombre);
				chcObjetos.getItems().add(nombre + ": " + cantidad);

				System.out.println(" Objeto cargado: " + nombre + ": " + cantidad);
			}

			if (chcObjetos.getItems().isEmpty()) {
				System.out.println(" No hay objetos disponibles.");
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private int obtenerIdObjetoPorNombre(String nombre) {
		return switch (nombre.toLowerCase()) {
		case "pesa" -> 1;
		case "pluma" -> 2;
		case "chaleco" -> 3;
		case "bastón" -> 4;
		case "pilas" -> 5;
		case "éter" -> 6;
		case "anillo único" -> 7;
		default -> -1;
		};
	}

	private void aplicarEfectoObjeto(String nombre) {
		switch (nombre.toLowerCase()) {
		case "pesa" -> {
			pokemonAliado.setAtaque((int) (pokemonAliado.getAtaque() * 1.2));
			pokemonAliado.setDefensa((int) (pokemonAliado.getDefensa() * 1.2));
			pokemonAliado.setVelocidad((int) (pokemonAliado.getVelocidad() * 0.8));
		}
		case "pluma" -> {
			pokemonAliado.setVelocidad((int) (pokemonAliado.getVelocidad() * 1.3));
			pokemonAliado.setDefensa((int) (pokemonAliado.getDefensa() * 0.8));
			pokemonAliado.setDefEspecial((int) (pokemonAliado.getDefEspecial() * 0.8));
		}
		case "chaleco" -> {
			pokemonAliado.setDefensa((int) (pokemonAliado.getDefensa() * 1.2));
			pokemonAliado.setDefEspecial((int) (pokemonAliado.getDefEspecial() * 1.2));
			pokemonAliado.setVelocidad((int) (pokemonAliado.getVelocidad() * 0.85));
			pokemonAliado.setAtaque((int) (pokemonAliado.getAtaque() * 0.85));
		}
		case "bastón" -> {
			pokemonAliado.setVitalidad((int) (pokemonAliado.getVitalidad() * 1.2));
			pokemonAliado.setVelocidad((int) (pokemonAliado.getVelocidad() * 0.85));
		}
		case "pilas" -> {
			pokemonAliado.setVitalidad((int) (pokemonAliado.getVitalidad() * 1.5));
			pokemonAliado.setDefEspecial((int) (pokemonAliado.getDefEspecial() * 0.7));
		}
		case "éter" -> {
			if (!ataquesJugador.isEmpty()) {
				ataquesJugador.get(0).restaurarPP(); // o elegir el ataque con un ChoiceBox también
				actualizarBotones();
			}
		}
		case "anillo único" -> {
			log(pokemonAliado.getNombre() + " es invencible durante 3 turnos y su ataque se multiplica por 10.");
		}
		}
	}

	private void restarObjetoDeBD(int idObjeto) {
		try (Connection con = ConexionBD.conectar()) {
			PreparedStatement ps = con.prepareStatement("""
					    UPDATE MOCHILA SET NUMERO_OBJETOS = NUMERO_OBJETOS - 1
					    WHERE ID_ENTRENADOR = ? AND ID_OBJETO = ?
					""");
			ps.setInt(1, entrenador.getIdentrenador());
			ps.setInt(2, idObjeto);
			ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@FXML
	void usarObjetoSeleccionado() {
		String seleccionado = chcObjetos.getValue();
		if (seleccionado == null || !seleccionado.contains(":")) {
			log("Selecciona un objeto válido.");
			return;
		}

		String nombreObjeto = seleccionado.split(":")[0].trim();
		int idObjeto = obtenerIdObjetoPorNombre(nombreObjeto);

		if (idObjeto == -1) {
			log("Objeto no reconocido.");
			return;
		}

		aplicarEfectoObjeto(nombreObjeto);
		restarObjetoDeBD(idObjeto);

		log("Has usado " + nombreObjeto + " sobre " + pokemonAliado.getNombre());
		actualizarBarrasVida();
		cargarObjetosDisponibles();
	}

	private void guardarEstadoPokemonAliado() {
		try (Connection con = ConexionBD.conectar()) {
			PreparedStatement ps = con.prepareStatement("""
					    UPDATE POKEMON SET
					        VITALIDAD = ?, ATAQUE = ?, DEFENSA = ?,
					        AT_ESPECIAL = ?, DEF_ESPECIAL = ?, VELOCIDAD = ?,
					        NIVEL = ?, ESTADO = ?, VIDA_ACTUAL = ?
					    WHERE ID_POKEMON = ?
					""");

			ps.setInt(1, pokemonAliado.getVitalidad());
			ps.setInt(2, pokemonAliado.getAtaque());
			ps.setInt(3, pokemonAliado.getDefensa());
			ps.setInt(4, pokemonAliado.getAtEspecial());
			ps.setInt(5, pokemonAliado.getDefEspecial());
			ps.setInt(6, pokemonAliado.getVelocidad());
			ps.setInt(7, pokemonAliado.getNivel());
			ps.setString(8, pokemonAliado.getEstado());
			ps.setInt(9, pokemonAliado.getVidaActual());
			ps.setInt(10, pokemonAliado.getId());

			ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private double calcularContraTipo(String tipoAtaque, String tipoDef) {
		if (tipoDef == null)
			return 1.0;

		// FUEGO
		if (tipoAtaque.equals("FUEGO")
				&& (tipoDef.equals("PLANTA") || tipoDef.equals("BICHO") || tipoDef.equals("HIELO")))
			return 2.0;
		if (tipoAtaque.equals("FUEGO") && (tipoDef.equals("AGUA") || tipoDef.equals("FUEGO") || tipoDef.equals("ROCA")
				|| tipoDef.equals("DRAGÓN")))
			return 0.5;

		// AGUA
		if (tipoAtaque.equals("AGUA")
				&& (tipoDef.equals("FUEGO") || tipoDef.equals("ROCA") || tipoDef.equals("TIERRA")))
			return 2.0;
		if (tipoAtaque.equals("AGUA")
				&& (tipoDef.equals("AGUA") || tipoDef.equals("PLANTA") || tipoDef.equals("DRAGÓN")))
			return 0.5;

		// PLANTA
		if (tipoAtaque.equals("PLANTA")
				&& (tipoDef.equals("AGUA") || tipoDef.equals("TIERRA") || tipoDef.equals("ROCA")))
			return 2.0;
		if (tipoAtaque.equals("PLANTA")
				&& (tipoDef.equals("PLANTA") || tipoDef.equals("FUEGO") || tipoDef.equals("VENENO")
						|| tipoDef.equals("BICHO") || tipoDef.equals("DRAGÓN") || tipoDef.equals("VOLADOR")))
			return 0.5;

		// VENENO
		if (tipoAtaque.equals("VENENO") && (tipoDef.equals("PLANTA") || tipoDef.equals("HADA")))
			return 2.0;
		if (tipoAtaque.equals("VENENO") && (tipoDef.equals("VENENO") || tipoDef.equals("TIERRA")
				|| tipoDef.equals("ROCA") || tipoDef.equals("FANTASMA")))
			return 0.5;

		// HADA
		if (tipoAtaque.equals("HADA")
				&& (tipoDef.equals("DRAGÓN") || tipoDef.equals("LUCHA") || tipoDef.equals("SINIESTRO")))
			return 2.0;
		if (tipoAtaque.equals("HADA")
				&& (tipoDef.equals("FUEGO") || tipoDef.equals("VENENO") || tipoDef.equals("ACERO")))
			return 0.5;

		// DRAGÓN
		if (tipoAtaque.equals("DRAGÓN") && tipoDef.equals("DRAGÓN"))
			return 2.0;
		if (tipoAtaque.equals("DRAGÓN") && tipoDef.equals("HADA"))
			return 0.0;

		// NORMAL
		if (tipoAtaque.equals("NORMAL") && tipoDef.equals("FANTASMA"))
			return 0.0;
		if (tipoAtaque.equals("NORMAL") && (tipoDef.equals("ROCA") || tipoDef.equals("ACERO")))
			return 0.5;

		// TIERRA
		if (tipoAtaque.equals("TIERRA") && (tipoDef.equals("FUEGO") || tipoDef.equals("ELÉCTRICO")
				|| tipoDef.equals("VENENO") || tipoDef.equals("ROCA") || tipoDef.equals("ACERO")))
			return 2.0;
		if (tipoAtaque.equals("TIERRA") && (tipoDef.equals("PLANTA") || tipoDef.equals("BICHO")))
			return 0.5;
		if (tipoAtaque.equals("TIERRA") && tipoDef.equals("VOLADOR"))
			return 0.0;

		// ELÉCTRICO
		if (tipoAtaque.equals("ELÉCTRICO") && (tipoDef.equals("AGUA") || tipoDef.equals("VOLADOR")))
			return 2.0;
		if (tipoAtaque.equals("ELÉCTRICO")
				&& (tipoDef.equals("PLANTA") || tipoDef.equals("DRAGÓN") || tipoDef.equals("ELÉCTRICO")))
			return 0.5;
		if (tipoAtaque.equals("ELÉCTRICO") && tipoDef.equals("TIERRA"))
			return 0.0;

		return 1.0;
	}

	private double calcularMultiplicadorTipo(String tipoAtaque, String tipoDef1, String tipoDef2) {
		tipoAtaque = tipoAtaque.toUpperCase();
		double mult1 = calcularContraTipo(tipoAtaque, tipoDef1 != null ? tipoDef1.toUpperCase() : null);
		double mult2 = calcularContraTipo(tipoAtaque, tipoDef2 != null ? tipoDef2.toUpperCase() : null);
		return mult1 * mult2;
	}

	private void ejecutarAtaque(int index) {
		if (index >= ataquesJugador.size())
			return;

		Ataque atk = ataquesJugador.get(index);
		if (!atk.tienePP()) {
			lblTurnos.setText("¡No quedan PP para ese ataque!");
			return;
		}

		atk.reducirPP();
		switch (index) {
		case 0 -> botonAtaque1.setText(formatoBoton(atk));
		case 1 -> botonAtaque2.setText(formatoBoton(atk));
		case 2 -> botonAtaque3.setText(formatoBoton(atk));
		case 3 -> botonAtaque4.setText(formatoBoton(atk));
		}

		double mult = calcularMultiplicadorTipo(atk.getTipoMovimiento(), pokemonSalvaje.getTipo1(),
				pokemonSalvaje.getTipo2());
		int dañoHecho = (int) (atk.getPotencia() * mult);
		pokemonSalvaje.reducirVida(dañoHecho);

		String mensaje = pokemonAliado.getNombre() + " usó " + atk.getNombre() + " e hizo " + dañoHecho + " de daño.";
		if (mult > 1.5)
			mensaje += " ¡Es muy efectivo!";
		else if (mult < 1.0)
			mensaje += " No fue muy efectivo...";
		else if (mult == 0.0)
			mensaje += " ¡No afecta al rival!";
		mensaje += "\n";

		if (pokemonAliado.estaDebilitado()) {
			mensaje += "¡" + pokemonAliado.getNombre() + " se ha debilitado!\n";
			if (!cambiarASiguientePokemonDisponible()) {
				mensaje += "¡No quedan Pokémon disponibles! Has perdido el combate.";
				log(mensaje.trim());
				salirMenupokeForzado();
				return;
			}
		}

		actualizarBarrasVida();

		if (pokemonSalvaje.estaDebilitado()) {
			mensaje += "¡" + pokemonSalvaje.getNombre() + " se ha debilitado!\n";

			int exp = calcularExperiencia(pokemonAliado.getNivel(), pokemonSalvaje.getNivel());
			mensaje += pokemonAliado.getNombre() + " gana " + exp + " puntos de experiencia.\n";

			try (Connection con = ConexionBD.conectar()) {
				int nuevoNivel = pokemonAliado.getNivel();
				int umbral = nuevoNivel * 10;
				int expActual = PokemonDAO.obtenerExp(pokemonAliado.getId());
				int nuevaExp = expActual + exp;

				if (nuevaExp >= umbral) {
					nuevoNivel++;
					mensaje += "¡" + pokemonAliado.getNombre() + " sube a nivel " + nuevoNivel + "!\n";
					pokemonAliado.setNivel(nuevoNivel);
					Random rand = new Random();
					pokemonAliado.setVitalidad(pokemonAliado.getVitalidad() + rand.nextInt(5) + 1);
					pokemonAliado.setAtaque(pokemonAliado.getAtaque() + rand.nextInt(5) + 1);
					pokemonAliado.setDefensa(pokemonAliado.getDefensa() + rand.nextInt(5) + 1);
					pokemonAliado.setAtEspecial(pokemonAliado.getAtEspecial() + rand.nextInt(5) + 1);
					pokemonAliado.setDefEspecial(pokemonAliado.getDefEspecial() + rand.nextInt(5) + 1);
					pokemonAliado.setVelocidad(pokemonAliado.getVelocidad() + rand.nextInt(5) + 1);

					// Aprender ataque nuevo si toca
					if (nuevoNivel % 3 == 0) {
						Ataque nuevoAtaque = obtenerAtaqueAprendible(nuevoNivel, pokemonAliado.getNumPokedex());
						if (nuevoAtaque != null) {
							List<Ataque> ataques = PokemonDAO.obtenerAtaques(pokemonAliado.getId());
							if (ataques.size() >= 4) {
								List<String> opciones = new ArrayList<>();
								for (Ataque a : ataques)
									opciones.add(a.getNombre());

								ChoiceDialog<String> dialogo = new ChoiceDialog<>(opciones.get(0), opciones);
								dialogo.setTitle("Aprendizaje de ataque");
								dialogo.setHeaderText(
										pokemonAliado.getNombre() + " quiere aprender " + nuevoAtaque.getNombre());
								dialogo.setContentText("¿Cuál quieres olvidar?");
								Optional<String> resultado = dialogo.showAndWait();

								resultado.ifPresent(nombreElegido -> {
									try (Connection c2 = ConexionBD.conectar()) {
										for (Ataque a : ataques) {
											if (a.getNombre().equals(nombreElegido)) {
												PreparedStatement psDel = c2.prepareStatement(
														"""
																	DELETE FROM MOVIMIENTO_POKEMON WHERE ID_POKEMON = ? AND ID_MOVIMIENTO = ?
																""");
												psDel.setInt(1, pokemonAliado.getId());
												psDel.setInt(2, a.getId());
												psDel.executeUpdate();
												break;
											}
										}
										PreparedStatement psIns = c2.prepareStatement(
												"""
															INSERT INTO MOVIMIENTO_POKEMON (ID_POKEMON, ID_MOVIMIENTO, PP_ACTUALES)
															VALUES (?, ?, ?)
														""");
										psIns.setInt(1, pokemonAliado.getId());
										psIns.setInt(2, nuevoAtaque.getId());
										psIns.setInt(3, nuevoAtaque.getPpMax());
										psIns.executeUpdate();
										log(pokemonAliado.getNombre() + " olvidó " + nombreElegido + " y aprendió "
												+ nuevoAtaque.getNombre() + "!");
									} catch (SQLException e) {
										e.printStackTrace();
									}
								});
							} else {
								PreparedStatement psIns = con.prepareStatement("""
											INSERT INTO MOVIMIENTO_POKEMON (ID_POKEMON, ID_MOVIMIENTO, PP_ACTUALES)
											VALUES (?, ?, ?)
										""");
								psIns.setInt(1, pokemonAliado.getId());
								psIns.setInt(2, nuevoAtaque.getId());
								psIns.setInt(3, nuevoAtaque.getPpMax());
								psIns.executeUpdate();
								log(pokemonAliado.getNombre() + " aprendió " + nuevoAtaque.getNombre() + "!");
							}
						}
					}
				}

				PreparedStatement ps = con.prepareStatement(
						"""
									UPDATE POKEMON SET NIVEL = ?, VIDA_ACTUAL = ?, VITALIDAD = ?, ATAQUE = ?, DEFENSA = ?, VELOCIDAD = ?, AT_ESPECIAL = ?, DEF_ESPECIAL = ?, EXP = ? WHERE ID_POKEMON = ?
								""");
				ps.setInt(1, pokemonAliado.getNivel());
				ps.setInt(2, pokemonAliado.getVidaActual());
				ps.setInt(3, pokemonAliado.getVitalidad());
				ps.setInt(4, pokemonAliado.getAtaque());
				ps.setInt(5, pokemonAliado.getDefensa());
				ps.setInt(6, pokemonAliado.getVelocidad());
				ps.setInt(7, pokemonAliado.getAtEspecial());
				ps.setInt(8, pokemonAliado.getDefEspecial());
				ps.setInt(9, nuevaExp);
				ps.setInt(10, pokemonAliado.getId());
				ps.executeUpdate();

			} catch (SQLException e) {
				mensaje += "Error al actualizar EXP o nivel.\n";
				e.printStackTrace();
			}

			generarPokemonSalvaje();
			actualizarImagenPokemonSalvaje(pokemonSalvaje.getNumPokedex());
			actualizarBarrasVida();
			mensaje += "¡Ha aparecido un nuevo " + pokemonSalvaje.getNombre() + " salvaje!\n";
		} else {
			mensaje += realizarAtaqueRival();
			if (pokemonAliado.estaDebilitado()) {
				mensaje += "¡" + pokemonAliado.getNombre() + " se ha debilitado!\n";
			}
		}

		actualizarBarrasVida();
		log(mensaje.trim());
	}

	private Ataque obtenerAtaqueAprendible(int nivel, int numPokedex) {
		try (Connection con = ConexionBD.conectar()) {
			PreparedStatement ps = con.prepareStatement("""
					    SELECT * FROM MOVIMIENTOS
					    WHERE NIVEL_APRENDIZAJE = ? AND NUM_POKEDEX = ?
					    LIMIT 1
					""");
			ps.setInt(1, nivel);
			ps.setInt(2, numPokedex);
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				return new Ataque(rs.getInt("ID_MOVIMIENTO"), rs.getString("NOM_MOVIMIENTO"),
						rs.getInt("NIVEL_APRENDIZAJE"), rs.getInt("PP_MAX"), rs.getInt("PP_MAX"), rs.getString("TIPO"),
						rs.getObject("POTENCIA") != null ? rs.getInt("POTENCIA") : null, rs.getString("TIPO_MOV"),
						rs.getString("ESTADO"), rs.getObject("TURNOS") != null ? rs.getInt("TURNOS") : null,
						rs.getString("MEJORA"), rs.getObject("NUM") != null ? rs.getInt("NUM") : null);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

}