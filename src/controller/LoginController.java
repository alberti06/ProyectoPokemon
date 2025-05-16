package controller;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

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
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;
import model.Entrenador;
import util.AudioManager;

public class LoginController {

	public Stage stage;

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

		    boolean camposValidos = true;

		    // Ya no usamos los Label de error
		    if (usuario.isEmpty()) {
		        int option = JOptionPane.showOptionDialog(
		            null,
		            "El nombre de usuario está vacío.",
		            "Error de Login",
		            JOptionPane.DEFAULT_OPTION,
		            JOptionPane.ERROR_MESSAGE,
		            null,
		            new Object[] { "Reintentar" },
		            "Reintentar"
		        );
		        if (option == JOptionPane.CLOSED_OPTION) {
		            return; // Si el usuario cierra la ventana, no hace nada.
		        }
		        camposValidos = false;
		    }

		    if (pass.isEmpty()) {
		        int option = JOptionPane.showOptionDialog(
		            null,
		            "La contraseña está vacía.",
		            "Error de Login",
		            JOptionPane.DEFAULT_OPTION,
		            JOptionPane.ERROR_MESSAGE,
		            null,
		            new Object[] { "Reintentar" },
		            "Reintentar"
		        );
		        if (option == JOptionPane.CLOSED_OPTION) {
		            return;
		        }
		        camposValidos = false;
		    }

		    if (!camposValidos) {
		        System.out.println("No se ha podido llevar a cabo el login");
		        return;
		    }

		    Connection conn = ConexionBD.conectar();

		    if (!EntrenadorDAO.existeEntrenador(conn, usuario)) {
		        int option = JOptionPane.showOptionDialog(
		            null,
		            "El nombre de usuario no está registrado en la base de datos.",
		            "Usuario no encontrado",
		            JOptionPane.DEFAULT_OPTION,
		            JOptionPane.ERROR_MESSAGE,
		            null,
		            new Object[] { "Reintentar" },
		            "Reintentar"
		        );
		        if (option == JOptionPane.CLOSED_OPTION) {
		            return;
		        }
		        System.out.println("Usuario no registrado");
		    } else {
		        Entrenador ent = EntrenadorDAO.login(conn, usuario, pass);

		        if (ent != null) {
		            System.out.println("Login correcto");
		            abrirPantallaMenu(ent);
		        } else {
		            int option = JOptionPane.showOptionDialog(
		                null,
		                "La contraseña es incorrecta.",
		                "Contraseña incorrecta",
		                JOptionPane.DEFAULT_OPTION,
		                JOptionPane.ERROR_MESSAGE,
		                null,
		                new Object[] { "Reintentar" },
		                "Reintentar"
		            );
		            if (option == JOptionPane.CLOSED_OPTION) {
		                return;
		            }
		            System.out.println("Contraseña incorrecta");
		        }
		    }
		}


	@FXML
	void comprobarRegistro(ActionEvent event) {
		String usuario = TfNombre.getText();
	    String pass = Password.getText();

	    boolean mover = false;
	    boolean camposValidos = true;

	    
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
	        Entrenador nuevo = new Entrenador(usuario, pass, 1000);
	        if (EntrenadorDAO.anyadirEntrenador(conn, nuevo)) {
	            System.out.println("Registro exitoso");

	            try {
	                // Insertar 5 Pokéballs por defecto
	                var stmt = conn.prepareStatement("""
	                    INSERT INTO MOCHILA (ID_ENTRENADOR, ID_OBJETO, NUMERO_OBJETOS)
	                    VALUES (?, 8, 5)
	                """);
	                stmt.setInt(1, EntrenadorDAO.obtenerIdEntrenador(conn, usuario));
	                stmt.executeUpdate();
	                System.out.println("Pokéballs iniciales otorgadas.");
	            } catch (Exception e) {
	                System.err.println("❌ Error al insertar Pokéballs iniciales: " + e.getMessage());
	            }

	            abrirPantallaElegirPokemon(EntrenadorDAO.obtenerIdEntrenador(conn, usuario));

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

			stage.getIcons().add(new Image(new File("./img/imagenesExtra/logo.jpg").toURI().toString()));
			stage.setTitle("Proyecto Pokemon los 3 mosqueteros");
			stage.setScene(scene);
			menuController.init(ent, stage, this);

			stage.show();

			this.stage.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void abrirPantallaElegirPokemon(int idEntrenador) {
	    try {
	        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/PantallaChoosePokemon.fxml"));
	        Parent root = loader.load();

	        PantallaChoosePokemonController controller = loader.getController();
	        controller.init(idEntrenador, this); // ✅ Método único

	        Stage stage = new Stage();
	        stage.setScene(new Scene(root));
	        stage.setTitle("Elige tu Pokémon inicial");
	        stage.getIcons().add(new Image(new File("./img/imagenesExtra/logo.jpg").toURI().toString()));
	        stage.show();

	        this.stage.close(); // cerrar login

	    } catch (IOException e) {
	        e.printStackTrace();
	    }
	}

	public void show() {
		stage.show();
		TfNombre.setText("");
		Password.setText("");
	}

	@FXML
	void activarDesactivarSonido(MouseEvent event) {
	    if (AudioManager.getMediaPlayer() != null) {
	        if (AudioManager.getMediaPlayer().getStatus().equals(MediaPlayer.Status.PLAYING)) {
	            AudioManager.pausar();
	            ImgSonido.setImage(new Image(new File("./img/imagenesExtra/sonidodes.png").toURI().toString()));
	        } else {
	            AudioManager.continuar();
	            ImgSonido.setImage(new Image(new File("./img/imagenesExtra/sonidoact.png").toURI().toString()));
	        }
	    }
	}

	@FXML
    public void initialize() {
        List<String> orden = new ArrayList<>();
        orden.add("./sonidos/Musica-‐-Hecho-con-Clipchamp.mp3");
        orden.add("./sonidos/Coldplay - Viva La Vida (Official Video).mp3");
        orden.add("./sonidos/Himno-del-Centenario-Real-Murcia-CF.wav");
        orden.add("./sonidos/La Roja Baila (Himno Oficial de la Selección Española) (Videoclip Oficial).mp3");
        orden.add("./sonidos/John Lennon - Imagine - 1971.mp3");
        orden.add("./sonidos/Queen - Too Much Love Will Kill You (Official Video).mp3");
        orden.add("./sonidos/Faro de Lisboa (feat. Bunbury).mp3");

        AudioManager.setPlaylist(orden);
        AudioManager.setCurrentIndex(0);
        AudioManager.reproducirActual();

        ImgSonido.setImage(new Image(new File("./img/imagenesExtra/sonidoact.png").toURI().toString()));
    }
	

	@FXML
	void salirJuego(MouseEvent event) {
		 if (stage != null) {
		        stage.close();
		        System.out.println("Se ha cerrado el juego");
		    }
    }
}