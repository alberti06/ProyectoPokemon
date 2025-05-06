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
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import model.Entrenador;

public class BolsaController {
	
	private MenuController menuController;
	private Stage stage;
	private Entrenador entrenador;
	private LoginController loginController;

    @FXML
    private AnchorPane anchor;

    @FXML
    private ImageView btnSalir;

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
    private Label lblCont0b7;

    @FXML
    private Label lblContador1;

    @FXML
    private Label lblContadorOB2;

    @FXML
    private Label lblContadorOB3;

    @FXML
    private Label lblContadorOb4;

    @FXML
    private Label lblContadorOb6;

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
    private Label lblOb5;

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
