package main;

import controller.LoginController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
    	FXMLLoader loader = new FXMLLoader(getClass().getResource("../view/Login.fxml"));
    	
    	
        Parent root = loader.load();
        Scene scene = new Scene(root);
       
        primaryStage.setTitle("Proyecto Pokemon los 3 mosqueteros");
        
        
        /*
        	ImageView myImageView = (ImageView) scene.lookup("#imgfondo");
        if(myImageView != null) {
            // Vincular las propiedades de tamaño del ImageView al tamaño del escenario
            myImageView.fitWidthProperty().bind(scene.widthProperty());
            myImageView.fitHeightProperty().bind(scene.heightProperty());s
        }
        */
        
        primaryStage.setScene(scene);
       
        LoginController controller = loader.getController();
        controller.setStage(primaryStage);
        
        primaryStage.show();
        
    }


    public static void main(String[] args) {
        launch(args);
    }
}
