package controller;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;

import javax.swing.JOptionPane;

import dao.ConexionBD;
import dao.EntrenadorDAO;
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
	private ImageView imgSalir;

	@FXML
	private ImageView imgfondo;

	@FXML
	void comprobarLogin(ActionEvent event) {
		    String usuario = TfNombre.getText();
		    String pass = Password.getText();

		    boolean mover = false;
		    boolean camposValidos = true;

		    // Reset de errores visuales
		    ErrorPass.setLayoutX(225);
		    ErrorNombre.setLayoutX(225);
		    ErrorPass.setVisible(false);
		    ErrorNombre.setVisible(false);

		    if (usuario.isEmpty()) {
		        ErrorNombre.setText("Nombre vacío");
		        ErrorNombre.setVisible(true);
		        System.out.println("Nombre vacío");
		        mover = true;
		        camposValidos = false;
		    }

		    if (pass.isEmpty()) {
		        ErrorPass.setText("Contraseña vacía");
		        ErrorPass.setVisible(true);
		        System.out.println("Contraseña vacía");
		        camposValidos = false;

		        if (mover) {
		            double Y = ErrorPass.getLayoutY();
		            if (contadorError == 1) {
		                ErrorPass.setLayoutY(Y + 25);
		                contadorError--;
		            }
		        }
		    }

		    if (!camposValidos) {
		        System.out.println("No se ha podido llevar a cabo el login");
		        return;
		    }

		    // Si no están vacíos, intentamos login en BBDD
		    Connection conn = ConexionBD.conectar();
		    Entrenador ent = EntrenadorDAO.login(conn, usuario, pass);
		    
		    if (ent != null) {
		        System.out.println("Login correcto");
		        abrirPantallaMenu(ent);
		    } else {
		        ErrorPass.setText("Credenciales incorrectas");
		        ErrorPass.setVisible(true);
		        System.out.println("Credenciales incorrectas");
		    }
		}


	@FXML
	void comprobarRegistro(ActionEvent event) {
		String usuario = TfNombre.getText();
	    String pass = Password.getText();

	    boolean mover = false;
	    boolean camposValidos = true;

	    
	    ErrorPass.setLayoutX(225);
	    ErrorNombre.setLayoutX(225);
	    ErrorPass.setVisible(false);
	    ErrorNombre.setVisible(false);

	    if (usuario.isEmpty()) {
	        ErrorNombre.setText("Nombre vacío");
	        ErrorNombre.setVisible(true);
	        System.out.println("Nombre vacío");
	        mover = true;
	        camposValidos = false;
	    }

	    if (pass.isEmpty()) {
	        ErrorPass.setText("Contraseña vacía");
	        ErrorPass.setVisible(true);
	        System.out.println("Contraseña vacía");
	        camposValidos = false;

	        if (mover) {
	            double Y = ErrorPass.getLayoutY();
	            if (contadorError == 1) {
	                ErrorPass.setLayoutY(Y + 25);
	                contadorError--;
	            }
	        }
	    }

	    if (!camposValidos) {
	        System.out.println("No se ha podido llevar a cabo el registro");
	        return;
	    }

	   
	    Connection conn = ConexionBD.conectar();

	    if (EntrenadorDAO.existeEntrenador(conn, usuario)) {
	        ErrorNombre.setText("Nombre ya registrado");
	        ErrorNombre.setVisible(true);
	        System.out.println("Nombre ya registrado");
	    } else {
	        Entrenador nuevo = new Entrenador(usuario, pass, 0);
	        if (EntrenadorDAO.anyadirEntrenador(conn, nuevo)) {
	            System.out.println("Registro exitoso");
	            abrirPantallaMenu(nuevo);
	        } else {
	            System.out.println("Error al registrar en la base de datos");
	        }
	    }
	}

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
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void show() {
		stage.show();
		TfNombre.setText("");
		Password.setText("");
	}

	public void sonido() {
		if (!this.sonido) {
			mediaPlayer.play();
			ImgSonido.setImage(new Image(new File("./img/imagenesExtra/sonidoact.png").toURI().toString()));
			this.sonido = true;
		} else {
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

	@FXML
	void salirJuego(MouseEvent event) {
		// Falta implementar salida del juego si es necesario
	}
} 
