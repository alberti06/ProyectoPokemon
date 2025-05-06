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
    
    public void init(Entrenador entrenador, Stage stage, MenuController menuController,LoginController loginController) {

        this.menuController = menuController;
        this.stage = stage;
        this.entrenador = entrenador;
        this.loginController = loginController;
    }


}
