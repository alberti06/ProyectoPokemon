package controller;

import java.awt.Label;
import java.io.File;
import java.io.IOException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import model.Entrenador;

public class EquipoController {
	
	private MenuController menuController;
	private Stage stage;
	private Entrenador entrenador;
	private LoginController loginController;

	@FXML
    private ProgressBar barPokemon1;

    @FXML
    private ProgressBar barPokemon2;

    @FXML
    private ProgressBar barPokemon3;

    @FXML
    private ProgressBar barPokemon4;

    @FXML
    private ProgressBar barPokemon5;

    @FXML
    private ProgressBar barPokemon6;

    @FXML
    private ImageView btnSalir;

    @FXML
    private AnchorPane imgFondo;

    @FXML
    private ImageView imgPokemon1;

    @FXML
    private ImageView imgPokemon2;

    @FXML
    private ImageView imgPokemon3;

    @FXML
    private ImageView imgPokemon4;

    @FXML
    private ImageView imgPokemon5;

    @FXML
    private ImageView imgPokemon6;

    @FXML
    private Label lblNombre1;

    @FXML
    private Label lblNombre2;

    @FXML
    private Label lblNombre3;

    @FXML
    private Label lblNombre4;

    @FXML
    private Label lblNombre5;

    @FXML
    private Label lblNombre6;

    @FXML
    private Label lblPS2;

    @FXML
    private Label lblPS3;

    @FXML
    private Label lblPS4;

    @FXML
    private Label lblPS5;

    @FXML
    private Label lblPS6;

    @FXML
    private Label lblPs1;

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
    
    public void init(Entrenador entrenador, Stage stage, MenuController menuController, LoginController loginController) {

        this.menuController = menuController;
        this.stage = stage;
        this.entrenador = entrenador;
        this.loginController = loginController;
    }

}
