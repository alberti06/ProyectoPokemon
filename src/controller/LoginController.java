package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class LoginController {
	int contadorError = 1;
	int contadorErrorLogin = 1;
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
		boolean mover = false;

		ErrorPass.setLayoutX(225);
		ErrorNombre.setLayoutX(225);
		if (TfNombre.getText().isEmpty()) {
			System.out.println("No se ha podido llevar a cabo el registro");
			ErrorNombre.setVisible(true);
			ErrorNombre.setText("Nombre vacío");
			mover = true;
		} else if (TfNombre.getText() == "") {
		} else {
			System.out.println("Te has registrado con éxito");
			ErrorNombre.setVisible(false);
		}
		if (Password.getText().isEmpty()) {
			System.out.println("No se ha podido llevar a cabo el registro");
			if (mover) {
				double Y = ErrorPass.getLayoutY();
				if (contadorError == 1) {
					ErrorPass.setLayoutY(Y + 25);
					contadorError--;
				}
			}
			ErrorPass.setVisible(true);
			ErrorPass.setText("Contraseña vacía");
		} else if (Password.getText() == "") {
		} else {
			System.out.println("Te has registrado con éxito");
			ErrorPass.setVisible(false);
		}

	}

	@FXML
	void comprobarRegistro(ActionEvent event) {
		boolean mover = false;

		ErrorPass.setLayoutX(225);
		ErrorNombre.setLayoutX(225);
		if (TfNombre.getText().isEmpty()) {
			System.out.println("no se ha podido llevar a cabo el registro");
			ErrorNombre.setVisible(true);
			ErrorNombre.setText("Nombre vacío");
			mover = true;
		} else {
			System.out.println("Te has registrado con éxito");
			ErrorNombre.setVisible(false);
		}
		if (Password.getText().isEmpty()) {
			System.out.println("no se ha podido llevar a cabo el registro");
			if (mover) {
				double Y = ErrorPass.getLayoutY();
				if (contadorError == 1) {
					ErrorPass.setLayoutY(Y + 25);
					contadorError--;
				}
			}
			ErrorPass.setVisible(true);
			ErrorPass.setText("Contraseña vacía");
		} else {
			System.out.println("Te has registrado con éxito");
			ErrorPass.setVisible(false);
		}

	}

	public void setStage(Stage primaryStage) {
		stage = primaryStage;
	}
}
