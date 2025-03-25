package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class LoginController {

	public Stage stage;
	
    @FXML
    private Label ErrorNombre;

    @FXML
    private Label ErrorPass;

    @FXML
    private Button Loginbtn;
    @FXML
    private Button Registrarse;
    @FXML
    private PasswordField Password;

    @FXML
    private TextField TfNombre;

    @FXML
    void comprobarLogin(ActionEvent event) {

    }
   
    @FXML
    void comprobarRegistro(ActionEvent event) {
    	System.out.println("Te has registrado");
    }

	public void setStage(Stage primaryStage) {
		stage = primaryStage;		
	}
}
