package ferranti.bikerbikus.controllers;

import ferranti.bikerbikus.data.UserData;
import ferranti.bikerbikus.utils.Constants;
import ferranti.bikerbikus.utils.Utils;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import ferranti.bikerbikus.models.GaraExtended;
import ferranti.bikerbikus.models.Stagione;
import ferranti.bikerbikus.queries.StagioneQuery;

public class StagioneController {

	private Stage stage;
	private Parent parent;
	private Stagione stagione;
	private ObservableList<GaraExtended> gare = FXCollections.observableArrayList();

	@FXML
	Button btnBack;
	@FXML
	Label lblUserName;
	@FXML
	Label lblTipoUtente;
	@FXML 
	Button btnProfile;
	@FXML
	Label lblNome;
	@FXML
	TableView<GaraExtended> tableGare;
	@FXML
	TableColumn<GaraExtended, LocalDateTime> colData;
	@FXML
	TableColumn<GaraExtended, Integer> colPartecipanti;
	@FXML
	TableColumn<GaraExtended, String> colNomeVincitore;
	@FXML
	TableColumn<GaraExtended, String> colCognomeVincitore;
	@FXML
	TableColumn<GaraExtended, GaraExtended> colDettagli;

	public StagioneController(Stagione stagione) {
		this.stagione = stagione;
	}

	public void showScene(Stage stage) {
		this.stage = stage;
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/views/stagione-view.fxml"));
		fxmlLoader.setController(this);
		try {
			parent = fxmlLoader.load();
		} catch (IOException e) {
			new Alert(AlertType.ERROR, e.getMessage(), ButtonType.OK).show();
		}
		stage.getScene().setRoot(parent);
		btnBack.setOnAction(event -> onActionBack());
		lblUserName.setText(Utils.uppercase(UserData.getInstance().getUser().getNome()) + " "
				+ Utils.uppercase(UserData.getInstance().getUser().getCognome()));
		lblTipoUtente.setText(UserData.getInstance().getUser().getTipoUtente().getNome());
		btnProfile.setOnAction(event -> onActionProfile());
		lblNome.setText("Campionato " + stagione.getCampionato().getNome() + " - Stagione " + stagione.getNome());
		colData.setCellFactory(param -> new TableCell<>() {
			@Override
			protected void updateItem(LocalDateTime item, boolean empty) {
				super.updateItem(item, empty);
				setText(item == null ? "" : item.format(DateTimeFormatter.ofPattern(Constants.DEFAULT_DATE_PATTERN)));
			}
		});
		colData.setCellValueFactory(new PropertyValueFactory<>("data"));
		colPartecipanti.setCellValueFactory(new PropertyValueFactory<>("partecipanti"));
		colNomeVincitore.setCellValueFactory(new PropertyValueFactory<>("nomeVincitore"));
		colCognomeVincitore.setCellValueFactory(new PropertyValueFactory<>("cognomeVincitore"));
		colDettagli.setCellFactory(param -> new TableCell<>() {
			@Override
			protected void updateItem(GaraExtended item, boolean empty) {
				super.updateItem(item, empty);
				final Hyperlink hyperlink = new Hyperlink("Dettagli");
				hyperlink.setOnAction(event -> showGara(item));
				setGraphic(item == null ? null : hyperlink);
			}
		});
		colDettagli.setCellValueFactory(cellData -> new SimpleObjectProperty<GaraExtended>(cellData.getValue()));
		tableGare.setItems(gare);
		loadStagione();
	}

	private void onActionBack() {
		new HomeController().showScene(stage);
	}

	private void onActionProfile() {
		new AreaPersonaleController().showScene(stage);		
	}
	
	private void loadStagione() {
		gare.addAll(StagioneQuery.execute(stagione.getId()));
	}

	private void showGara(GaraExtended gara) {
		new Alert(AlertType.INFORMATION, "Visualizzare dettaglio gara " + gara.getId(), ButtonType.OK).show();
	}
}
