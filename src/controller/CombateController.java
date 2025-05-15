package controller;

import java.io.File;
import java.io.IOException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
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

public class CombateController {

	private MenuController menuController;
	private Stage stage;
	private Entrenador entrenador;
	private LoginController loginController;
	private Pokemon pokemonSalvaje;
	private List<Ataque> ataquesJugador = new ArrayList<>();
	
	@FXML
	private AnchorPane anchorpane;

	@FXML
	private ImageView btnSalir;

	@FXML
	private ImageView imgPokemonEntrenador;

	@FXML
	private ImageView imgPokemonSalvaje;

	@FXML
	private ImageView imgSuelo1;

	@FXML
	private ImageView imgSuelo2;

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

	private final String RUTA_LOG = "./combate_log.txt";
	
	public void init(Entrenador entrenador, Stage stage) {
		this.entrenador = entrenador;
		this.stage = stage;

		cargarAtaquesDesdeBD();
		actualizarBotones();
		generarPokemonSalvaje();
		cargarSprites();
		log("¡Ha comenzado un combate entre " + entrenador.getPrimerPokemon().getNombre() + " y "
				+ pokemonSalvaje.getNombre() + "!");
	}

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
			ps.setInt(1, entrenador.getPrimerPokemon().getId());
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
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	private void generarPokemonSalvaje() {
		try (Connection con = ConexionBD.conectar()) {
			Random rand = new Random();
			int nivelEntrenador = entrenador.getPrimerPokemon().getNivel();
			int nivelSalvaje = nivelEntrenador + rand.nextInt(11) - 5;
			if (nivelSalvaje < 1)
				nivelSalvaje = 1;

			PreparedStatement ps = con.prepareStatement("""
					    SELECT p.NUM_POKEDEX, p.NOM_POKEMON, p.TIPO1, p.TIPO2, p.IMG_FRONTAL, p.IMG_TRASERA,
					           p.SONIDO, p.NIVEL_EVOLUCION
					    FROM POKEDEX p
					    ORDER BY RAND()
					    LIMIT 1
					""");
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				String nombre = rs.getString("NOM_POKEMON");
				double escalado = obtenerEscalado(nivelEntrenador, nivelSalvaje);
				int vida = (int) (50 * escalado);
				int atk = (int) (25 * escalado);

				pokemonSalvaje = new Pokemon(rs.getInt("NUM_POKEDEX"), nombre, nivelSalvaje, vida, atk);
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

	private void cargarSprites() {
		String pathJugador = "file:img/Pokemon/Front/"
				+ String.format("%03d", entrenador.getPrimerPokemon().getNumPokedex()) + ".png";
		String pathSalvaje = "file:img/Pokemon/Front/" + String.format("%03d", pokemonSalvaje.getId()) + ".png";

		imgPokemonEntrenador.setImage(new Image(pathJugador));
		imgPokemonSalvaje.setImage(new Image(pathSalvaje));
	}

	private void log(String mensaje) {
		try (BufferedWriter writer = new BufferedWriter(new FileWriter(RUTA_LOG, true))) {
			writer.write(mensaje);
			writer.newLine();
		} catch (IOException e) {
			e.printStackTrace();
		}
		lblTurnos.setText(mensaje);
	}

	// MÉTODOS DE ATAQUE
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

	private void ejecutarAtaque(int index) {
		if (index >= ataquesJugador.size())
			return;
		Ataque atk = ataquesJugador.get(index);
		if (!atk.tienePP()) {
			lblTurnos.setText("¡No quedan PP para ese ataque!");
			return;
		}

		Pokemon jugador = entrenador.getPrimerPokemon();

		atk.reducirPP();
		pokemonSalvaje.reducirVida(atk.getPotencia());
		log(jugador.getNombre() + " ha usado " + atk.getNombre());

		actualizarBotones();

		if (pokemonSalvaje.estaDebilitado()) {
			log("¡" + pokemonSalvaje.getNombre() + " se ha debilitado!");
		} else {
			turnoEnemigo();
		}
	}

	private void turnoEnemigo() {
		int daño = pokemonSalvaje.getAtaque();
		entrenador.getPrimerPokemon().reducirVida(daño);
		log(pokemonSalvaje.getNombre() + " ha atacado con fuerza");

		if (entrenador.getPrimerPokemon().estaDebilitado()) {
			log("¡" + entrenador.getPrimerPokemon().getNombre() + " se ha debilitado!");
		}
	}

	@FXML
	void salirMenupoke() {
		try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../view/MenuPrincipal.fxml"));
            Parent root = loader.load();
            menuController = loader.getController();

            Scene sc = new Scene(root);
            Stage st = new Stage();
            
        	st.getIcons().add(new Image(new File("./img/imagenesExtra/logo.jpg").toURI().toString()));
            st.setTitle("Proyecto Pokemon los 3 mosqueteros");
            st.setScene(sc);
            
            menuController.init(entrenador, st, loginController);
            st.show();
            this.stage.close();
            
        } catch (Exception e) {
            e.printStackTrace();
        }
	}	

	public void init(Entrenador entrenador, Stage stage, MenuController menuController,
			LoginController loginController) {

		this.menuController = menuController;
		this.stage = stage;
		this.entrenador = entrenador;
		this.loginController = loginController;
	}

	private void actualizarBotones() {
		if (ataquesJugador.size() >= 1)
			botonAtaque1.setText(formatoBoton(ataquesJugador.get(0)));
		if (ataquesJugador.size() >= 2)
			botonAtaque2.setText(formatoBoton(ataquesJugador.get(1)));
		if (ataquesJugador.size() >= 3)
			botonAtaque3.setText(formatoBoton(ataquesJugador.get(2)));
		if (ataquesJugador.size() >= 4)
			botonAtaque4.setText(formatoBoton(ataquesJugador.get(3)));
	}

	private String formatoBoton(Ataque atk) {
		return atk.getNombre() + " (" + atk.getPpActual() + "/" + atk.getPpMax() + ")";
	}

    // Cambia la imagen del Pokémon del jugador
    private void actualizarImagenPokemonEntrenador(int idPokemon) {
        String ruta = "/img/Pokemon/Back/" + String.format("%03d", idPokemon) + "_back.png";
        Image imagen = new Image(getClass().getResourceAsStream(ruta));
        imgPokemonEntrenador.setImage(imagen);
    }
    // Cambia la imagen del Pokémon salvaje
    private void actualizarImagenPokemonSalvaje(int idPokemon) {
        String ruta = "/img/Pokemon/Front/" + String.format("%03d", idPokemon) + "_front.png";
        Image imagen = new Image(getClass().getResourceAsStream(ruta));
        imgPokemonSalvaje.setImage(imagen);
    }

    // Cambia el Pokémon del jugador en combate
    public void intercambiarPrimerPokemon(int indiceOtro) {
        List<Pokemon> equipo = entrenador.getPokemons(); //  obtener la lista desde el objeto Entrenador
        if (indiceOtro < 0 || indiceOtro >= equipo.size()) return;

        Pokemon temp = equipo.get(0);
        equipo.set(0, equipo.get(indiceOtro));
        equipo.set(indiceOtro, temp);
    }



    // Abre una ventana para seleccionar otro Pokémon
    public void abrirSelectorDePokemon() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/Equipo.fxml"));
            Parent root = loader.load();
            EquipoController equipoController = loader.getController();

            Stage selectorStage = new Stage(); // Primero creamos el Stage
            selectorStage.setScene(new Scene(root));
            selectorStage.setTitle("Cambiar Pokémon");
            selectorStage.setResizable(false);

            // ✅ Luego pasamos el stage y el resto de los parámetros
            equipoController.init(entrenador, selectorStage, menuController, loginController);

            selectorStage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
