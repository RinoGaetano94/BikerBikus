package ferranti.bikerbikus.controllers;

import ferranti.bikerbikus.utils.Utils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import ferranti.bikerbikus.data.UserData;
import ferranti.bikerbikus.models.Gara;
import ferranti.bikerbikus.models.Stagione;
import ferranti.bikerbikus.queries.CampionatiQuery;
import ferranti.bikerbikus.queries.InsertGaraQuery;

public class AggiungiGaraController {

	private Stage stage;
	private Parent parent;
	private ObservableList<LocalTime> orari = FXCollections.observableArrayList();
	private ObservableList<Stagione> stagioni = FXCollections.observableArrayList();

	@FXML
	Button btnBack;
	@FXML
	Label lblUserName;
	@FXML
	Label lblTipoUtente;
	@FXML
	DatePicker dpGiorno;
	@FXML
	ComboBox<LocalTime> cmbOrario;
	@FXML
	ComboBox<Stagione> cmbStagione;
	@FXML
	Button btnConferma;

	public void showScene(Stage stage) {
		this.stage = stage;
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/views/aggiungi-gara-view.fxml"));
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
		loadOrari();
		cmbOrario.setItems(orari);
		loadStagioni();
		cmbStagione.setItems(stagioni);
		cmbStagione.setOnAction(event -> {
			Stagione stagioneSelezionata = cmbStagione.getSelectionModel().getSelectedItem();
			restrictDatePicker(dpGiorno, stagioneSelezionata.getDataInizio(), stagioneSelezionata.getDataFine());
			dpGiorno.setDisable(false);
		});
		btnConferma.setOnAction(event -> onActionConferma());
	}

	public void restrictDatePicker(DatePicker datePicker, LocalDate minDate, LocalDate maxDate) {
		final Callback<DatePicker, DateCell> dayCellFactory = new Callback<DatePicker, DateCell>() {
			@Override
			public DateCell call(final DatePicker datePicker) {
				return new DateCell() {
					@Override
					public void updateItem(LocalDate item, boolean empty) {
						super.updateItem(item, empty);
						if (item.isBefore(minDate) || item.isAfter(maxDate)) {
							setDisable(true);
							setStyle("-fx-background-color: #ffc0cb;");
						}
					}
				};
			}
		};
		datePicker.setDayCellFactory(dayCellFactory);
	}

	private void onActionBack() {
		new GareController().showScene(stage);
	}

	private void onActionConferma() {
		if (dpGiorno.getValue() == null || cmbOrario.getValue() == null || cmbStagione.getValue() == null) {
			new Alert(AlertType.WARNING, "Inserisci tutti i dati!").show();
		} else {
			Gara gara = new Gara();
			gara.setStagione(cmbStagione.getValue());
			gara.setData(LocalDateTime.of(dpGiorno.getValue(), cmbOrario.getValue()));
			if (InsertGaraQuery.execute(gara)) {
				new Alert(AlertType.CONFIRMATION, "Gara creata con successo!", ButtonType.OK).show();
				new GareController().showScene(stage);
			}

		}
	}

	private void loadOrari() {
		for (int h = 0; h < 24; h++) {
			for (int m = 0; m < 60; m += 15) {
				orari.add(LocalTime.of(h, m));
			}
		}
	}

	private void loadStagioni() {
		stagioni.clear();
		stagioni.addAll(CampionatiQuery.findCampionatiAperti());
	}
}
