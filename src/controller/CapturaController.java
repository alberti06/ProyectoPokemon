package controller;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Random;

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
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import model.Entrenador;
import model.Pokemon;

public class CapturaController {

	private MenuController menuController;
	private Stage stage;
	private Entrenador entrenador;
	private LoginController loginController;
	private Pokemon pokemonSalvaje;

	@FXML
	private VBox Vbox;
	@FXML
	private ImageView imgHuevoMasc;
	@FXML
	private ImageView imgFondo;
	@FXML
	private Label lblText1;
	@FXML
	private Label lblText2;

	public void init(Entrenador entrenador, Stage stage, MenuController menuController,
			LoginController loginController) {
		this.menuController = menuController;
		this.stage = stage;
		this.entrenador = entrenador;
		this.loginController = loginController;
		generarNuevoPokemonSalvaje();
	}

	@FXML
	void salirMenupoke(MouseEvent event) {
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

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@FXML
	void capturarPokemon(MouseEvent event) {
		try (Connection con = ConexionBD.conectar()) {
			PreparedStatement comprobar = con.prepareStatement("""
					    SELECT CANTIDAD FROM MOCHILA
					    WHERE ID_ENTRENADOR = ? AND ID_OBJETO = ?
					""");
			comprobar.setInt(1, entrenador.getIdentrenador());
			comprobar.setInt(2, 1); // Suponiendo que 1 = Pokéball

			ResultSet rs = comprobar.executeQuery();

			if (rs.next() && rs.getInt("CANTIDAD") > 0) {
				// Reducir Pokéballs
				PreparedStatement reducir = con.prepareStatement("""
						    UPDATE MOCHILA SET CANTIDAD = CANTIDAD - 1
						    WHERE ID_ENTRENADOR = ? AND ID_OBJETO = ?
						""");
				reducir.setInt(1, entrenador.getIdentrenador());
				reducir.setInt(2, 1);
				reducir.executeUpdate();

				// Probabilidad 2/3 de captura
				Random rand = new Random();
				boolean capturado = rand.nextInt(3) < 2;

				if (capturado) {
					PokemonDAO.insertarPokemon(entrenador.getIdentrenador(), pokemonSalvaje);
					lblText1.setText("¡Has capturado a " + pokemonSalvaje.getNombre() + "!");
				} else {
					lblText1.setText("¡" + pokemonSalvaje.getNombre() + " se ha escapado!");
				}

				generarNuevoPokemonSalvaje();

			} else {
				lblText1.setText("¡No te quedan Pokéballs!");
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private void generarNuevoPokemonSalvaje() {
		try (Connection con = ConexionBD.conectar()) {
			Random rand = new Random();
			int nivelJugador = entrenador.getPrimerPokemon().getNivel();
			int nivelSalvaje = nivelJugador + rand.nextInt(11) - 5;
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
				int numPokedex = rs.getInt("NUM_POKEDEX");
				double escalado = obtenerEscalado(nivelJugador, nivelSalvaje);
				int vida = (int) (50 * escalado);
				int atk = (int) (25 * escalado);

				pokemonSalvaje = new Pokemon(numPokedex, nombre, nivelSalvaje, vida, atk);

				// Actualizar imagen
				String rutaImagen = "/img/Pokemon/Front/" + String.format("%03d", numPokedex) + "_front.png";
				Image img = new Image(getClass().getResourceAsStream(rutaImagen));
				imgHuevoMasc.setImage(img);

				lblText2.setText("¡Un " + nombre + " salvaje apareció!");
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
}
