package controller;


import java.io.File;
import java.io.IOException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
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
    private ImageView btnConf;

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
    private ImageView imgTienda;

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
    
    //REVISAR
    public void show() {
        if (stage != null) {
            stage.show();
        }
    }

    @FXML
    void abrirConf(MouseEvent event) {
    	try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/MenuConfiguracion.fxml"));
            Parent root = loader.load();

            Stage confStage = new Stage();
            confStage.getIcons().add(new Image(new File("./img/imagenesExtra/logo.jpg").toURI().toString()));
            confStage.setTitle("Configuración");
            confStage.setScene(new Scene(root));
            confStage.setResizable(false);
            confStage.show();
            
            this.stage.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
   
    @FXML
    void abrirBolsa(MouseEvent event) {
    	try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/Bolsa Pokemon.fxml"));
            Parent root = loader.load();

            Stage confStage = new Stage();
            confStage.getIcons().add(new Image(new File("./img/imagenesExtra/logo.jpg").toURI().toString()));
            confStage.setTitle("Centro Pokemon");
            confStage.setScene(new Scene(root));
            confStage.setResizable(false);
            confStage.show();
            
            this.stage.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @FXML
    void abrirCaptura(MouseEvent event) {
    	try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/captura.fxml"));
            Parent root = loader.load();

            Stage confStage = new Stage();
            confStage.getIcons().add(new Image(new File("./img/imagenesExtra/logo.jpg").toURI().toString()));
            confStage.setTitle("Captura");
            confStage.setScene(new Scene(root));
            confStage.setResizable(false);
            confStage.show();
            this.stage.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @FXML
    void abrirCentropoke(MouseEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/CentroPokemon.fxml"));
            Parent root = loader.load();

            CentropokeController controller = loader.getController(); // ✅ Obtenemos el controller
            Stage confStage = new Stage();

            controller.init(entrenador, confStage, this, loginController); // ✅ Pasamos datos necesarios

            confStage.getIcons().add(new Image(new File("./img/imagenesExtra/logo.jpg").toURI().toString()));
            confStage.setTitle("Centro Pokemon");
            confStage.setScene(new Scene(root));
            confStage.setResizable(false);
            confStage.show();

            this.stage.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void abrirCombate(MouseEvent event) {

    }
    @FXML
    void abrirCrianza(MouseEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/Crianza.fxml"));
            Parent root = loader.load();

            CrianzaController crianzaController = loader.getController();
            
            Stage confStage = new Stage(); // <-- crea primero la nueva ventana
            crianzaController.init(entrenador, confStage, this, loginController); // <-- pasar confStage aquí

            confStage.getIcons().add(new Image(new File("./img/imagenesExtra/logo.jpg").toURI().toString()));
            confStage.setTitle("Crianza");
            confStage.setScene(new Scene(root));
            confStage.setResizable(false);
            confStage.show();

            this.stage.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @FXML
    void abrirEquipo(MouseEvent event) {
    	

    }

    @FXML
    void abrirPokedex(MouseEvent event) {
    	

    }
    
    @FXML
    void abrirTienda(MouseEvent event) {
    	
    	try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/Tienda.fxml"));
            Parent root = loader.load();

            Stage confStage = new Stage();
            confStage.getIcons().add(new Image(new File("./img/imagenesExtra/logo.jpg").toURI().toString()));
            confStage.setTitle("Tienda");
            confStage.setScene(new Scene(root));
            confStage.setResizable(false);
            confStage.show();
            
            this.stage.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    	

    }
    
    
}
