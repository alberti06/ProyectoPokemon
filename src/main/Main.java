package main;

import java.io.File;

import javax.swing.JOptionPane;

import controller.LoginController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class Main extends Application {

	@Override
	public void start(Stage primaryStage) throws Exception {

		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("../view/Login.fxml"));
			Parent root = loader.load();
			Scene scene = new Scene(root);

			primaryStage.getIcons().add(new Image(new File("./img/imagenesExtra/logo.jpg").toURI().toString()));
			primaryStage.setTitle("Proyecto Pokemon los 3 mosqueteros");
			primaryStage.setScene(scene);

			LoginController controller = loader.getController();
			controller.setStage(primaryStage);

			primaryStage.show();

			System.out.println("Juego abierto correctamente");

		} catch (Exception e) {
			JOptionPane.showOptionDialog(null, "ERROR!! Juego abierto incorrectamente. Revisa la terminal", "ERROR",
					JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE, null, new Object[] { "CERRAR JUEGO" },
					"CERRAR JUEGO");
			try {
				if (util.AudioManager.getMediaPlayer() != null) {
					util.AudioManager.getMediaPlayer().stop();
				}
			} catch (Exception ex) {
				System.out.println("No se pudo detener la música.");
			}

			System.out.println("Juego abierto incorrectamente");
			e.printStackTrace();
		}

	}

	public static void main(String[] args) {
		launch(args);
	}
}
