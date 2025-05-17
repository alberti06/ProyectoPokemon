package controller;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;

import dao.ConexionBD;
import dao.EntrenadorDAO;
import dao.MochilaDAO;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import model.Entrenador;

public class TiendaController {

	private MenuController menuController;
	private Stage stage;
	private Entrenador entrenador;
	private LoginController loginController;

	@FXML
	private Button btnComprar;

	@FXML
	private ImageView btnSalir;

	@FXML
	private ImageView fondoTienda;

	@FXML
	private Label lblAnillo;

	@FXML
	private Label lblBastón;

	@FXML
	private Label lblChaleco;

	@FXML
	private Label lblEter;

	@FXML
	private Label lblPesa;

	@FXML
	private Label lblPila;

	@FXML
	private Label lblPluma;

	@FXML
	private Label lblPokeball;

	@FXML
	private Label lblPrecio;

	@FXML
	private Label lblPesetas;

	@FXML
	private Label lblDinero;

	@FXML
	private Spinner<Integer> spinnerPila;

	@FXML
	private Spinner<Integer> spinnerEter;

	@FXML
	private Spinner<Integer> spinnerPokeball;

	@FXML
	private Spinner<Integer> spinnerBaston;

	@FXML
	private Spinner<Integer> spinnerChaleco;

	@FXML
	private Spinner<Integer> spinnerPluma;

	@FXML
	private Spinner<Integer> spinnerPesa;

	@FXML
	private Spinner<Integer> spinnerAnillo;

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

	public void init(Entrenador entrenador, Stage stage, MenuController menuController,
			LoginController loginController) {
		this.menuController = menuController;
		this.stage = stage;
		this.entrenador = entrenador;
		this.loginController = loginController;

		configurarSpinners();
		agregarListenersSpinners();
		actualizarTotal();
		actualizarLabelDinero();
	}

	private void configurarSpinners() {
		spinnerPila.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 99, 0));
		spinnerEter.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 99, 0));
		spinnerPokeball.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 99, 0));
		spinnerBaston.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 99, 0));
		spinnerChaleco.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 99, 0));
		spinnerPluma.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 99, 0));
		spinnerPesa.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 99, 0));
		spinnerAnillo.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 99, 0));
	}

	private void actualizarTotal() {
		int totalObjetos = spinnerPila.getValue() + spinnerEter.getValue() + spinnerPokeball.getValue()
				+ spinnerBaston.getValue() + spinnerChaleco.getValue() + spinnerPluma.getValue()
				+ spinnerPesa.getValue() + spinnerAnillo.getValue();

		int total = totalObjetos * 1000;
		lblPrecio.setText(total + " Pesetas");
	}

	private void agregarListenersSpinners() {
		spinnerPila.valueProperty().addListener((obs, oldVal, newVal) -> actualizarTotal());
		spinnerEter.valueProperty().addListener((obs, oldVal, newVal) -> actualizarTotal());
		spinnerPokeball.valueProperty().addListener((obs, oldVal, newVal) -> actualizarTotal());
		spinnerBaston.valueProperty().addListener((obs, oldVal, newVal) -> actualizarTotal());
		spinnerChaleco.valueProperty().addListener((obs, oldVal, newVal) -> actualizarTotal());
		spinnerPluma.valueProperty().addListener((obs, oldVal, newVal) -> actualizarTotal());
		spinnerPesa.valueProperty().addListener((obs, oldVal, newVal) -> actualizarTotal());
		spinnerAnillo.valueProperty().addListener((obs, oldVal, newVal) -> actualizarTotal());
	}

	private void actualizarLabelDinero() {
		lblDinero.setText(entrenador.getPokedolares() + " ₧");
	}

	@FXML
	private void comprar() {
		int totalObjetos = spinnerPila.getValue() + spinnerEter.getValue() + spinnerPokeball.getValue()
				+ spinnerBaston.getValue() + spinnerChaleco.getValue() + spinnerPluma.getValue()
				+ spinnerPesa.getValue() + spinnerAnillo.getValue();

		int costeTotal = totalObjetos * 1000;

		if (costeTotal == 0) {
			mostrarMensaje("No has seleccionado ningún objeto.");
			return;
		}

		if (entrenador.getPokedolares() < costeTotal) {
			mostrarMensaje("No tienes suficiente dinero.");
			return;
		}

		// Actualizar dinero y reflejarlo
		entrenador.setPokedolares(entrenador.getPokedolares() - costeTotal);
		actualizarDineroBD(entrenador.getIdentrenador(), entrenador.getPokedolares());

		// Mapa de nombres para imprimir
		Map<Spinner<Integer>, String> nombres = Map.of(spinnerPila, "Pilas", spinnerEter, "Éter", spinnerPokeball,
				"Pokéball", spinnerBaston, "Bastón", spinnerChaleco, "Chaleco", spinnerPluma, "Pluma", spinnerPesa,
				"Pesa", spinnerAnillo, "Anillo");

		// Añadir a la mochila e imprimir en consola
		for (Map.Entry<Spinner<Integer>, String> entry : nombres.entrySet()) {
			int cantidad = entry.getKey().getValue();
			if (cantidad > 0) {
				String nombreObjeto = entry.getValue();
				int idObjeto = obtenerIDPorNombre(nombreObjeto);
				actualizarMochila(idObjeto, cantidad);
				System.out.println("Entrenador: " + entrenador.getUsuario() + " ha comprado " + cantidad
						+ " unidades de " + nombreObjeto);
			}
		}

		mostrarMensaje("¡Compra realizada con éxito!");
		configurarSpinners();
		actualizarTotal();
		actualizarLabelDinero();
	}

	private int obtenerIDPorNombre(String nombre) {
		return switch (nombre) {
		case "Pesa" -> 1;
		case "Pluma" -> 2;
		case "Chaleco" -> 3;
		case "Bastón" -> 4;
		case "Pilas" -> 5;
		case "Éter" -> 6;
		case "Anillo" -> 7;
		case "Pokéball" -> 8;
		default -> -1;
		};
	}

	private void mostrarMensaje(String mensaje) {
		Alert alert = new Alert(Alert.AlertType.INFORMATION);
		alert.setHeaderText(null);
		alert.setContentText(mensaje);
		alert.showAndWait();
	}

	private void actualizarDineroBD(int id, int nuevoDinero) {
		try (Connection con = ConexionBD.conectar()) {
			boolean actualizado = EntrenadorDAO.actualizarDinero(con, id, nuevoDinero);
			if (!actualizado) {
				mostrarMensaje("Error al actualizar el dinero.");
			}
		} catch (SQLException e) {
			e.printStackTrace();
			mostrarMensaje("Error de conexión al actualizar el dinero.");
		}
	}

	private void actualizarMochila(int idObjeto, int cantidad) {
		if (cantidad <= 0)
			return;
		try (Connection con = ConexionBD.conectar()) {
			MochilaDAO.agregarObjeto(con, entrenador.getIdentrenador(), idObjeto, cantidad);
		} catch (SQLException e) {
			e.printStackTrace();
			mostrarMensaje("Error al actualizar la mochila.");
		}
	}
}
