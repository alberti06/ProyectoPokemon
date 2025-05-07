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

public class BolsaController {

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
    

    public void init(Entrenador entrenador, Stage stage, MenuController menuController, LoginController loginController) {
        this.entrenador = entrenador;
        this.stage = stage;
        this.menuController = menuController;
        this.loginController = loginController;

        configurarSpinners();
        cargarDatosMochila();
    }

    private void configurarSpinners() {
        // Mapear cada spinner con su ID de objeto en la base de datos
        mapaSpinnerIDObjeto.put(spinnerPokeball, 8);
        mapaSpinnerIDObjeto.put(spinnerBaston, 4);
        mapaSpinnerIDObjeto.put(spinnerChaleco, 3);
        mapaSpinnerIDObjeto.put(spinnerPluma, 2);
        mapaSpinnerIDObjeto.put(spinnerAnillo, 7);
        mapaSpinnerIDObjeto.put(spinnerPesa, 1);
        mapaSpinnerIDObjeto.put(spinnerPilas, 5);
        mapaSpinnerIDObjeto.put(spinnerEter, 6);

        // Asociar cada spinner con su label correspondiente
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

        // Mapa de ID de objeto a su nombre (puedes modificar los nombres si deseas)
        Map<Integer, String> nombresObjetos = Map.of(
            1, "Pesa",
            2, "Pluma",
            3, "Chaleco",
            4, "Bastón",
            5, "Pilas",
            6, "Éter",
            7, "Anillo",
            8, "Pokéball"
        );

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
                    System.out.println("Entrenador: " + entrenador.getUsuario() + " ha vendido " + cantidadAVender + " unidades de " + nombreObjeto);
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
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../view/MenuPrincipal.fxml"));
            Parent root = loader.load();
            menuController = loader.getController();

            Scene scene = new Scene(root);
            Stage newStage = new Stage();
            newStage.getIcons().add(new Image(new File("./img/imagenesExtra/logo.jpg").toURI().toString()));
            newStage.setTitle("Proyecto Pokémon los 3 mosqueteros");
            newStage.setScene(scene);
            menuController.init(entrenador, newStage, loginController);

            newStage.show();
            this.stage.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void mostrarMensaje(String texto) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(null);
        alert.setContentText(texto);
        alert.showAndWait();
    }
}