package controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import model.Entrenador;

public class MenuController {
	
	private Entrenador entrenador;
	private Stage stage;
	private LoginController loginController;

	@FXML
    private ImageView ImgBolsa;

    @FXML
    private ImageView ImgCentroPokemon;

    @FXML
    private ImageView ImgCombate;

    @FXML
    private ImageView ImgEquipo;

    @FXML
    private ImageView ImgFondo;

    @FXML
    private ImageView ImgSalir;

    @FXML
    private Label LblJugador;

    @FXML
    private ImageView imgCaptura;

    @FXML
    private ImageView imgCrianza;

    @FXML
    private Label lblDinero;

    @FXML
    private Label lblPesetas;

    @FXML
    private Label lblUsuario;

	public void init(Entrenador ent, Stage stage, LoginController loginController) {
	
		this.loginController = loginController;
		this.stage = stage;
		this.entrenador = ent;
		
 
		lblUsuario.setText(ent.getUsuario());
		lblDinero.setText(Integer.toString(ent.getPokedolares()));
	}

    
   
    
}
