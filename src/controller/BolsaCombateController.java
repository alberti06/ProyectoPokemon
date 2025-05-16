package controller;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import dao.ConexionBD;
import dao.EntrenadorDAO;
import dao.MochilaDAO;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import model.Entrenador;
import model.Pokemon;

public class BolsaCombateController {
	private CombateController combateController;
	private MenuController menuController;
	private Stage stage;
	private Entrenador entrenador;
	private LoginController loginController;

	private Map<Spinner<Integer>, Integer> mapaSpinnerIDObjeto = new HashMap<>();
	private Map<Spinner<Integer>, Label> mapaSpinnerLabelCantidad = new HashMap<>();

	@FXML
	private AnchorPane anchor;

	@FXML
	private ImageView btnSalir;

	@FXML
	private ImageView btnVender;

	@FXML
	private ImageView imgOb4;

	@FXML
	private ImageView imgOb5;

	@FXML
	private ImageView imgOb6;

	@FXML
	private ImageView imgOb7;

	@FXML
	private ImageView imgObjeto8;

	@FXML
	private ImageView imgPluma;

	@FXML
	private ImageView imgPokeball;

	@FXML
	private ImageView lblAnillo;

	@FXML
	private Label lblCantAnillo;

	@FXML
	private Label lblCantBaston;

	@FXML
	private Label lblCantChaleco;

	@FXML
	private Label lblCantEter;

	@FXML
	private Label lblCantPesa;

	@FXML
	private Label lblCantPilas;

	@FXML
	private Label lblCantPluma;

	@FXML
	private Label lblCantPokeball;

	@FXML
	private Label lblNombre6;

	@FXML
	private Label lblNombreOB2;

	@FXML
	private Label lblNombreOb1;

	@FXML
	private Label lblNombreOb3;

	@FXML
	private Label lblNombreOb4;

	@FXML
	private Label lblNombreOb5;

	@FXML
	private Label lblNombreOb7;

	@FXML
	private Label lblNombreOb8;

	@FXML
	private Spinner<Integer> spinnerAnillo;

	@FXML
	private Spinner<Integer> spinnerBaston;

	@FXML
	private Spinner<Integer> spinnerChaleco;

	@FXML
	private Spinner<Integer> spinnerEter;

	@FXML
	private Spinner<Integer> spinnerPesa;

	@FXML
	private Spinner<Integer> spinnerPilas;

	@FXML
	private Spinner<Integer> spinnerPluma;

	@FXML
	private Spinner<Integer> spinnerPokeball;

	public void setCombateController(CombateController combateController) {
		this.combateController = combateController;
	}

	public void init(Entrenador entrenador, Stage stage, MenuController menuController,
			LoginController loginController) {
		this.entrenador = entrenador;
		this.stage = stage;
		this.menuController = menuController;
		this.loginController = loginController;

		configurarSpinners();
		cargarDatosMochila();
	}

	private void configurarSpinners() {
		
		mapaSpinnerIDObjeto.put(spinnerPokeball, 8);
		mapaSpinnerIDObjeto.put(spinnerBaston, 4);
		mapaSpinnerIDObjeto.put(spinnerChaleco, 3);
		mapaSpinnerIDObjeto.put(spinnerPluma, 2);
		mapaSpinnerIDObjeto.put(spinnerAnillo, 7);
		mapaSpinnerIDObjeto.put(spinnerPesa, 1);
		mapaSpinnerIDObjeto.put(spinnerPilas, 5);
		mapaSpinnerIDObjeto.put(spinnerEter, 6);

	
		mapaSpinnerLabelCantidad.put(spinnerPokeball, lblCantPokeball);
		mapaSpinnerLabelCantidad.put(spinnerBaston, lblCantBaston);
		mapaSpinnerLabelCantidad.put(spinnerChaleco, lblCantChaleco);
		mapaSpinnerLabelCantidad.put(spinnerPluma, lblCantPluma);
		mapaSpinnerLabelCantidad.put(spinnerAnillo, lblCantAnillo);
		mapaSpinnerLabelCantidad.put(spinnerPesa, lblCantPesa);
		mapaSpinnerLabelCantidad.put(spinnerPilas, lblCantPilas);
		mapaSpinnerLabelCantidad.put(spinnerEter, lblCantEter);

		for (Spinner<Integer> spinner : mapaSpinnerIDObjeto.keySet()) {
			spinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 99, 0));
		}
	}

	private void cargarDatosMochila() {
		try (Connection con = ConexionBD.conectar()) {
			for (Map.Entry<Spinner<Integer>, Integer> entry : mapaSpinnerIDObjeto.entrySet()) {
				Spinner<Integer> spinner = entry.getKey();
				int idObjeto = entry.getValue();

				int cantidad = MochilaDAO.obtenerCantidadObjeto(con, entrenador.getIdentrenador(), idObjeto);
				mapaSpinnerLabelCantidad.get(spinner).setText(String.valueOf(cantidad));
			}
		} catch (SQLException e) {
			e.printStackTrace();
			mostrarMensaje("Error al cargar los datos de la mochila.");
		}
	}

	@FXML
	void vender(MouseEvent event) {
		int totalVendidos = 0;

		
		Map<Integer, String> nombresObjetos = Map.of(1, "Pesa", 2, "Pluma", 3, "Chaleco", 4, "Bastón", 5, "Pilas", 6,
				"Éter", 7, "Anillo", 8, "Pokéball");

		try (Connection con = ConexionBD.conectar()) {
			for (Map.Entry<Spinner<Integer>, Integer> entry : mapaSpinnerIDObjeto.entrySet()) {
				Spinner<Integer> spinner = entry.getKey();
				int idObjeto = entry.getValue();
				int cantidadAVender = spinner.getValue();
				Label labelCantidad = mapaSpinnerLabelCantidad.get(spinner);
				int cantidadActual = Integer.parseInt(labelCantidad.getText());

				if (cantidadAVender > 0) {
					if (cantidadAVender > cantidadActual) {
						mostrarMensaje("No puedes vender más de lo que tienes.");
						return;
					}

					MochilaDAO.restarObjeto(con, entrenador.getIdentrenador(), idObjeto, cantidadAVender);
					totalVendidos += cantidadAVender;

					// Mostrar por consola
					String nombreObjeto = nombresObjetos.getOrDefault(idObjeto, "Objeto desconocido");
					System.out.println("Entrenador: " + entrenador.getUsuario() + " ha vendido " + cantidadAVender
							+ " unidades de " + nombreObjeto);
				}
			}

			if (totalVendidos > 0) {
				int ganancia = totalVendidos * 500;
				int nuevoDinero = entrenador.getPokedolares() + ganancia;

				EntrenadorDAO.actualizarDinero(con, entrenador.getIdentrenador(), nuevoDinero);
				entrenador.setPokedolares(nuevoDinero);

				mostrarMensaje("Has vendido objetos por " + ganancia + " $.");
				cargarDatosMochila();
				resetearSpinners();
			} else {
				mostrarMensaje("No has seleccionado objetos para vender.");
			}

		} catch (SQLException e) {
			e.printStackTrace();
			mostrarMensaje("Error al realizar la venta.");
		}
	}

	private void resetearSpinners() {
		for (Spinner<Integer> spinner : mapaSpinnerIDObjeto.keySet()) {
			spinner.getValueFactory().setValue(0);
		}
	}

	@FXML
	void salirMenupoke(MouseEvent event) {
	    if (combateController != null) {
	        combateController.volverDeBolsa(); 
	    }
	    stage.close(); 
	}

	private void mostrarMensaje(String texto) {
		Alert alert = new Alert(Alert.AlertType.INFORMATION);
		alert.setHeaderText(null);
		alert.setContentText(texto);
		alert.showAndWait();
	}
	
	private void restarObjetoDeBD(int idObjeto) {
	    try (Connection con = ConexionBD.conectar()) {
	        MochilaDAO.restarObjeto(con, entrenador.getIdentrenador(), idObjeto, 1);
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	}

	private void usarObjeto(int idObjeto) {
	    Pokemon p = entrenador.getPrimerPokemon();

	    switch (idObjeto) {
	        case 1: // Pesa
	            p.setAtaque((int)(p.getAtaque() * 1.2));
	            p.setDefensa((int)(p.getDefensa() * 1.2));
	            p.setVelocidad((int)(p.getVelocidad() * 0.8));
	            mostrarMensaje("Usaste una Pesa. ATQ y DEF ↑, VELOCIDAD ↓.");
	            break;

	        case 2: // Pluma
	            p.setVelocidad((int)(p.getVelocidad() * 1.3));
	            p.setDefensa((int)(p.getDefensa() * 0.8));
	            p.setDefEspecial((int)(p.getDefEspecial() * 0.8));
	            mostrarMensaje("Usaste una Pluma. VELOCIDAD ↑, DEF y DEF.ESP ↓.");
	            break;

	        case 3: // Chaleco
	            p.setDefensa((int)(p.getDefensa() * 1.2));
	            p.setDefEspecial((int)(p.getDefEspecial() * 1.2));
	            p.setVelocidad((int)(p.getVelocidad() * 0.85));
	            p.setAtaque((int)(p.getAtaque() * 0.85));
	            mostrarMensaje("Usaste un Chaleco. DEF y DEF.ESP ↑, VELOCIDAD y ATQ ↓.");
	            break;

	        case 4: // Bastón
	            p.setVitalidad((int)(p.getVitalidad() * 1.2));
	            p.setVelocidad((int)(p.getVelocidad() * 0.85));
	            mostrarMensaje("Usaste un Bastón. VITALIDAD ↑, VELOCIDAD ↓.");
	            break;

	        case 5: // Pilas
	            p.setVitalidad((int)(p.getVitalidad() * 1.5)); 
	            p.setDefEspecial((int)(p.getDefEspecial() * 0.7));
	            mostrarMensaje("Usaste Pilas. CURA ↑, DEF.ESP ↓.");
	            break;

	        default:
	            mostrarMensaje("Objeto no válido.");
	            return;
	    }

	   
	    restarObjetoDeBD(idObjeto);
	    combateController.volverDeBolsa(); 
	    stage.close();
	}

}
