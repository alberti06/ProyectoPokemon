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
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import model.Entrenador;

public class CapturaController {
	
	private MenuController menuController;
	private Stage stage;
	private Entrenador entrenador;
	private LoginController loginController;

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
    
    
    public void init(Entrenador entrenador, Stage stage, MenuController menuController,LoginController loginController) {

        this.menuController = menuController;
        this.stage = stage;
        this.entrenador = entrenador;
        this.loginController = loginController;
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

}
