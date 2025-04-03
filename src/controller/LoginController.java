package controller;

import java.io.File;
import java.io.IOException;

import javax.swing.JOptionPane;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;
import javafx.scene.input.MouseEvent;
import model.Entrenador;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;


public class LoginController {
	
	Entrenador entrenador = new Entrenador("Admin", "123456", 1000);
	
	int contadorError = 1;
	int contadorErrorLogin = 1;
	public Stage stage;
	public boolean sonido = false;
	public MediaPlayer mediaPlayer;


    @FXML
    private Label ErrorNombre;

    @FXML
    private Label ErrorPass;

    @FXML
    private ImageView ImgSonido;

    @FXML
    private Button Loginbtn;

    @FXML
    private PasswordField Password;

    @FXML
    private Button Registrarse;

    @FXML
    private TextField TfNombre;

    @FXML
    private ImageView imgfondo;

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
			abrirPantallaMenu(entrenador);
		}

	}

	@FXML
	void comprobarRegistro(ActionEvent event) {
		boolean mover = false;

		ErrorPass.setLayoutX(225);
		ErrorNombre.setLayoutX(225);
		if (TfNombre.getText().isEmpty()) {
			System.out.println("No se ha podido llevar a cabo el registro");
			ErrorNombre.setVisible(true);
			ErrorNombre.setText("Nombre vacío");
			mover = true;
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
		} else {
			System.out.println("Te has registrado con éxito");
			ErrorPass.setVisible(false);
		}
		
	}

	//HAY QUE PONER ESTO abrirPantallaMenu(entrenador); PERO NO SE DONDE VA
	
	public void setStage(Stage primaryStage) {
		stage = primaryStage;
	}
	
	@FXML
	private void abrirPantallaMenu(Entrenador ent) {
	
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("../view/MenuPrincipal.fxml"));
			Parent root = loader.load();
	   
	        MenuController menuController = loader.getController();
	       
	        Scene scene = new Scene(root);
	        Stage stage = new Stage();
	        
	        
	        stage.setTitle("Proyecto Pokemon los 3 mosqueteros");
	        stage.setScene(scene);
	        menuController.init(ent, stage, this);
	        
	        stage.show();
	        
	        this.stage.close();
	        
		}catch(IOException e) {
			e.printStackTrace();
		}
	} 
	
	public void show() {
		stage.show();
		TfNombre.setText("");
		Password.setText("");
		
		
	}
	
	public void sonido() {
		if(!this.sonido) {
			mediaPlayer.play();
			
			ImgSonido.setImage(new Image(new File("./img/imagenesExtra/sonidoact.png").toURI().toString()));
			this.sonido = true;
			
		}else {
				mediaPlayer.pause();
				this.sonido = false;
				ImgSonido.setImage(new Image(new File("./img/imagenesExtra/sonidodes.png").toURI().toString()));
				
			}
		
	}
	
	@FXML
    void activarDesactivarSonido(MouseEvent event) {
		sonido();

    }
	
	@FXML
	public void initialize() {
		String rutaSonido = "./sonidos/Musica-‐-Hecho-con-Clipchamp.mp3";
		Media sound = new Media(new File(rutaSonido).toURI().toString());
		mediaPlayer = new MediaPlayer(sound);
		mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);
		sonido();
	}
}
