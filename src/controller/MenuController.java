package controller;


import java.io.IOException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import model.Entrenador;
import javafx.stage.Stage;
import javafx.scene.input.MouseEvent;


public class MenuController {
	
	private Entrenador entrenador;
    private Stage stage;
    private LoginController loginController;

    @FXML
    private AnchorPane AnchorPane;

    @FXML
    private VBox Box;

    @FXML
    private ImageView imgBolsa;

    @FXML
    private ImageView imgCaptura;

    @FXML
    private ImageView imgCenPokemon;

    @FXML
    private ImageView imgCombate;

    @FXML
    private ImageView imgCrianza;

    @FXML
    private ImageView imgEquipo;

    @FXML
    private ImageView imgFondo;

    @FXML
    private ImageView imgSalir;
    
    @FXML
    private ImageView btnConf;

    @FXML
    private Label lblCantidad;

    @FXML
    private Label lblJugador;

    @FXML
    private Label lblNombre;

    @FXML
    private Label lblPesetas;
    
    
    public void init(Entrenador ent, Stage stage, LoginController loginController) {

        this.loginController = loginController;
        this.stage = stage;
        this.entrenador = ent;

 
        lblNombre.setText(ent.getUsuario());
        lblCantidad.setText(Integer.toString(ent.getPokedolares()));
        
        /*
        Box.prefWidthProperty().bind(stage.widthProperty());
        Box.prefHeightProperty().bind(stage.heightProperty());*/
    }
    
    @FXML
    void salirInicio(MouseEvent event) {
        if (loginController != null && stage != null) {
            loginController.show();
            stage.close();
            System.out.println("Has cerrado sesion correctamente");
        }
    }
    

    @FXML
    void abrirConf(MouseEvent event) {

    	try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/MenuConfiguracion.fxml"));
            Parent root = loader.load();

            Stage confStage = new Stage();
            confStage.setTitle("Configuración");
            confStage.setScene(new Scene(root));
            confStage.setResizable(false);
            confStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    
}
