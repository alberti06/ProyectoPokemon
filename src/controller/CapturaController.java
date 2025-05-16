package controller;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Random;

import javax.swing.JOptionPane;

import dao.ConexionBD;
import dao.PokemonDAO;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import model.Entrenador;
import model.Pokemon;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

public class CapturaController {

	private MenuController menuController;
	private Stage stage;
	private Entrenador entrenador;
	private LoginController loginController;
	private Pokemon pokemonSalvaje;
	private MediaPlayer efectoSonido;

	@FXML private ImageView btnCambiarPoke;
	@FXML private ImageView btnSalir;
	@FXML private ImageView imgFondo;
	@FXML private ImageView imgHuevoMasc;
	@FXML private ImageView imgPokemon;
	@FXML private Label lblNunpokebolas;
	@FXML private Label lblText2;

	public void init(Entrenador entrenador, Stage stage, MenuController menuController, LoginController loginController) {
		this.menuController = menuController;
		this.stage = stage;
		this.entrenador = entrenador;
		this.loginController = loginController;
		generarNuevoPokemonSalvaje();
	}

	@FXML
	void salirMenupoke(MouseEvent event) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/MenuPrincipal.fxml"));
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
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@FXML
	void capturarPokemon(MouseEvent event) {
		final int ID_POKEBALL = 8;

		if (pokemonSalvaje == null) {
			JOptionPane.showMessageDialog(null,
				"No hay ningún Pokémon para capturar.",
				"Error", JOptionPane.ERROR_MESSAGE);
			return;
		}

		try (Connection con = ConexionBD.conectar()) {
			PreparedStatement comprobar = con.prepareStatement("""
				SELECT NUMERO_OBJETOS FROM MOCHILA
				WHERE ID_ENTRENADOR = ? AND ID_OBJETO = ?
			""");
			comprobar.setInt(1, entrenador.getIdentrenador());
			comprobar.setInt(2, ID_POKEBALL);
			ResultSet rs = comprobar.executeQuery();

			if (rs.next()) {
				int cantidad = rs.getInt("NUMERO_OBJETOS");
				System.out.println("Cantidad de Pokéballs: " + cantidad);

				if (cantidad > 0) {
					PreparedStatement reducir = con.prepareStatement("""
						UPDATE MOCHILA SET NUMERO_OBJETOS = NUMERO_OBJETOS - 1
						WHERE ID_ENTRENADOR = ? AND ID_OBJETO = ?
					""");
					reducir.setInt(1, entrenador.getIdentrenador());
					reducir.setInt(2, ID_POKEBALL);
					reducir.executeUpdate();

					Random rand = new Random();
					boolean capturado = rand.nextInt(3) < 2;

					if (capturado) {
						int slotEquipo = PokemonDAO.obtenerSiguienteHuecoEquipo(entrenador.getIdentrenador());

						if (slotEquipo == -1) {
							pokemonSalvaje.setEquipo(0);
							JOptionPane.showMessageDialog(null,
								"¡Has capturado a " + pokemonSalvaje.getNombre() + ", pero tu equipo está lleno!\nSe ha enviado a la caja.",
								"Equipo lleno", JOptionPane.WARNING_MESSAGE);
						} else {
							pokemonSalvaje.setEquipo(slotEquipo);
							JOptionPane.showMessageDialog(null,
								"¡Has capturado a " + pokemonSalvaje.getNombre() + "!",
								"¡Captura exitosa!", JOptionPane.INFORMATION_MESSAGE);
						}

						PokemonDAO.insertarPokemon(entrenador.getIdentrenador(), pokemonSalvaje);

						// Añadir movimiento Placaje (ID 31)
						try (Connection con2 = ConexionBD.conectar()) {
							int idNuevoPokemon = PokemonDAO.obtenerUltimoIdPokemonInsertado(con2);

							PreparedStatement ps = con2.prepareStatement("""
								INSERT INTO MOVIMIENTO_POKEMON (ID_POKEMON, ID_MOVIMIENTO, PP_RESTANTES)
								VALUES (?, 31, (SELECT PP FROM MOVIMIENTOS WHERE ID_MOVIMIENTO = 31))
							""");
							ps.setInt(1, idNuevoPokemon);
							ps.executeUpdate();

							System.out.println("✔ Placaje (ID 31) añadido al Pokémon capturado.");
						} catch (Exception e) {
							System.err.println("❌ Error al asignar Placaje: " + e.getMessage());
						}

						generarNuevoPokemonSalvaje();
					} else {
						JOptionPane.showMessageDialog(null,
							"¡" + pokemonSalvaje.getNombre() + " se ha escapado!\n¡Vamos chaval, inténtalo de nuevo!",
							"¡Se fue el Pokémon!", JOptionPane.WARNING_MESSAGE);
						imgPokemon.setImage(null);
						generarNuevoPokemonSalvaje();
					}
				} else {
					JOptionPane.showMessageDialog(null,
						"¡No te quedan Pokéballs!",
						"Sin Pokéballs", JOptionPane.WARNING_MESSAGE);
				}
			} else {
				PreparedStatement crearEntrada = con.prepareStatement("""
					INSERT INTO MOCHILA (ID_ENTRENADOR, ID_OBJETO, NUMERO_OBJETOS)
					VALUES (?, ?, 0)
				""");
				crearEntrada.setInt(1, entrenador.getIdentrenador());
				crearEntrada.setInt(2, ID_POKEBALL);
				crearEntrada.executeUpdate();

				JOptionPane.showMessageDialog(null,
					"¡No tienes Pokéballs! Se ha creado la entrada en la mochila.",
					"Entrada creada", JOptionPane.WARNING_MESSAGE);
			}
		} catch (SQLException e) {
			System.err.println("Error SQL: " + e.getMessage());
			JOptionPane.showMessageDialog(null,
				"Ocurrió un error al capturar el Pokémon:\n" + e.getMessage(),
				"Error de base de datos", JOptionPane.ERROR_MESSAGE);
		}

		actualizarLabelPokeballs();
	}

	@FXML
	void generarNuevoPokemonSalvaje(MouseEvent event) {
		generarNuevoPokemonSalvaje();
	}

	private void generarNuevoPokemonSalvaje() {
		try (Connection con = ConexionBD.conectar()) {
			Random rand = new Random();
			int nivelJugador = entrenador.getPrimerPokemon().getNivel();
			int nivelSalvaje = nivelJugador + rand.nextInt(11) - 5;
			if (nivelSalvaje < 1) nivelSalvaje = 1;

			PreparedStatement ps = con.prepareStatement("""
				SELECT p.NUM_POKEDEX, p.NOM_POKEMON FROM POKEDEX p
				ORDER BY RAND()
				LIMIT 1
			""");
			ResultSet rs = ps.executeQuery();

			if (rs.next()) {
				String nombre = rs.getString("NOM_POKEMON");
				int numPokedex = rs.getInt("NUM_POKEDEX");

				double escalado = obtenerEscalado(nivelJugador, nivelSalvaje);
				int vida = (int) (50 * escalado);
				int atk = (int) (25 * escalado);
				String sexo = rand.nextBoolean() ? "M" : "F";

				pokemonSalvaje = new Pokemon(numPokedex, nombre, nivelSalvaje, vida, atk, sexo);

				File sonidoFile = new File("C:/ProyectoPokemon/sonidos/Pokemon/" + String.format("%03d", numPokedex) + ".wav");
				if (sonidoFile.exists()) {
					Media media = new Media(sonidoFile.toURI().toString());
					efectoSonido = new MediaPlayer(media);
					efectoSonido.play();
				} else {
					System.out.println("No se encontró el archivo de sonido");
				}

				String ruta = "C:/ProyectoPokemon/resources/img/Pokemon/Front/" + String.format("%03d", numPokedex) + ".png";
				File file = new File(ruta);
				if (file.exists()) {
					imgPokemon.setImage(new Image(file.toURI().toString()));
				} else {
					File defaultFile = new File("C:/ProyectoPokemon/resources/img/Pokemon/Front/default.png");
					if (defaultFile.exists()) {
						imgPokemon.setImage(new Image(defaultFile.toURI().toString()));
					} else {
						System.err.println("❌ Imagen por defecto tampoco encontrada.");
					}
				}
				lblText2.setText("¡Un " + nombre + " salvaje apareció!");
				actualizarLabelPokeballs();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private void actualizarLabelPokeballs() {
		try (Connection con = ConexionBD.conectar()) {
			PreparedStatement ps = con.prepareStatement("""
				SELECT NUMERO_OBJETOS FROM MOCHILA
				WHERE ID_ENTRENADOR = ? AND ID_OBJETO = 8
			""");
			ps.setInt(1, entrenador.getIdentrenador());
			ResultSet rs = ps.executeQuery();

			if (rs.next()) {
				int cantidad = rs.getInt("NUMERO_OBJETOS");
				lblNunpokebolas.setText("Pokéballs: " + cantidad);

				if (cantidad == 0) {
					lblNunpokebolas.setStyle("-fx-text-fill: red;");
				} else {
					lblNunpokebolas.setStyle("-fx-text-fill: black;");
				}
			} else {
				lblNunpokebolas.setText("Pokéballs: 0");
				lblNunpokebolas.setStyle("-fx-text-fill: red;");
			}
		} catch (SQLException e) {
			lblNunpokebolas.setText("Pokéballs: ?");
			System.err.println("Error al obtener Pokéballs: " + e.getMessage());
		}
	}

	private double obtenerEscalado(int nivelJugador, int nivelSalvaje) {
		int diff = nivelSalvaje - nivelJugador;
		if (diff > 0) return Math.min(1.0 + diff * 0.1, 1.5);
		else if (diff < 0) return Math.max(1.0 + diff * 0.04, 0.8);
		else return 1.0;
	}
}
