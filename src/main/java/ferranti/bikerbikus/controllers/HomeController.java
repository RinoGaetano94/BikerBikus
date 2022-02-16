package ferranti.bikerbikus.controllers;

import ferranti.bikerbikus.utils.Utils;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;

import java.io.IOException;

import ferranti.bikerbikus.data.UserData;

public class HomeController {

	private Stage stage;
	private Parent parent;

	@FXML
	Label lblUserName;
	@FXML
	Label lblTipoUtente;
	@FXML 
	Button btnProfile;
	@FXML
	Button btnLezioni;
	@FXML
	Button btnCampionati;
	@FXML
	Button btnGare;
	@FXML
	Button btnEscursioni;

	public void showScene(Stage stage) {
		this.stage = stage;
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/views/home-view.fxml"));
		fxmlLoader.setController(this);
		try {
			parent = fxmlLoader.load();
		} catch (IOException e) {
			new Alert(AlertType.ERROR, e.getMessage(), ButtonType.OK).show();
		}
		stage.getScene().setRoot(parent);
		lblUserName.setText(
				Utils.uppercase(UserData.getInstance().getUser().getNome()) + " " + Utils.uppercase(UserData.getInstance().getUser().getCognome()));
		lblTipoUtente.setText(UserData.getInstance().getUser().getTipoUtente().getNome());
		btnProfile.setOnAction(event -> onActionProfile());
		btnLezioni.setOnAction(event -> onActionLezioni());
		btnCampionati.setOnAction(event -> onActionCampionati());
		btnGare.setOnAction(event -> onActionGare());
		btnEscursioni.setOnAction(event -> onActionEscursioni());

	}

	private void onActionProfile() {
		new AreaPersonaleController().showScene(stage);		
	}
	
	private void onActionLezioni() {
		new LezioniController().showScene(stage);
	}
	
	private void onActionCampionati() {
		new CampionatiController().showScene(stage);
	}
	
	private void onActionGare() {
		new GareController().showScene(stage);
	}
	
	private void onActionEscursioni() {
		new EscursioniController().showScene(stage);
	}
}
