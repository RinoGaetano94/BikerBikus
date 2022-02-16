package ferranti.bikerbikus.controllers;

import ferranti.bikerbikus.data.UserData;
import ferranti.bikerbikus.queries.CampionatiQuery;
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
import java.time.*;
import java.time.format.DateTimeFormatter;

import ferranti.bikerbikus.models.Campionato;
import ferranti.bikerbikus.models.Stagione;

public class CampionatiController {

	private Stage stage;
	private Parent parent;
	private ObservableList<Stagione> stagioni = FXCollections.observableArrayList();

	@FXML
	Button btnBack;
	@FXML
	Label lblUserName;
	@FXML
	Label lblTipoUtente;
	@FXML
	Button btnProfile;
	@FXML
	TableView<Stagione> tableStagioni;
	@FXML
	TableColumn<Stagione, Campionato> colCampionato;
	@FXML
	TableColumn<Stagione, String> colStagione;
	@FXML
	TableColumn<Stagione, LocalDate> colDataInizio;
	@FXML
	TableColumn<Stagione, LocalDate> colDataFine;
	@FXML
	TableColumn<Stagione, Stagione> colDettagli;

	public void showScene(Stage stage) {
		this.stage = stage;
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/views/campionati-view.fxml"));
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
		colCampionato.setCellFactory(param -> new TableCell<>() {
			@Override
			protected void updateItem(Campionato item, boolean empty) {
				super.updateItem(item, empty);
				setText(item == null ? "" : item.getNome());
			}
		});
		colCampionato.setCellValueFactory(
				cellData -> new SimpleObjectProperty<Campionato>(cellData.getValue().getCampionato()));
		colStagione.setCellValueFactory(new PropertyValueFactory<>("nome"));
		colDataInizio.setCellFactory(param -> new TableCell<>() {
			@Override
			protected void updateItem(LocalDate item, boolean empty) {
				super.updateItem(item, empty);
				setText(item == null ? "" : item.format(DateTimeFormatter.ofPattern(Constants.DEFAULT_DATE_PATTERN)));
			}
		});
		colDataInizio.setCellValueFactory(new PropertyValueFactory<>("dataInizio"));
		colDataFine.setCellFactory(param -> new TableCell<>() {
			@Override
			protected void updateItem(LocalDate item, boolean empty) {
				super.updateItem(item, empty);
				setText(item == null ? "" : item.format(DateTimeFormatter.ofPattern(Constants.DEFAULT_DATE_PATTERN)));
			}
		});
		colDataFine.setCellValueFactory(new PropertyValueFactory<>("dataFine"));
		colDettagli.setCellFactory(param -> new TableCell<>() {
			@Override
			protected void updateItem(Stagione item, boolean empty) {
				super.updateItem(item, empty);
				final Hyperlink hyperlink = new Hyperlink("Dettagli");
				hyperlink.setOnAction(event -> showStagione(item));
				setGraphic(item == null ? null : hyperlink);
			}
		});
		colDettagli.setCellValueFactory(cellData -> new SimpleObjectProperty<Stagione>(cellData.getValue()));
		tableStagioni.setItems(stagioni);
		loadStagioni();
	}

	private void onActionBack() {
		// TODO: Tornare indietro e non sempre alla home .-.
		new HomeController().showScene(stage);
	}

	private void onActionProfile() {
		new AreaPersonaleController().showScene(stage);
	}

	private void loadStagioni() {
		stagioni.clear();
		stagioni.addAll(CampionatiQuery.findAll());
	}

	private void showStagione(Stagione stagione) {
		new StagioneController(stagione).showScene(stage);
	}
}
