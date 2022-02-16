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
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import ferranti.bikerbikus.data.UserData;
import ferranti.bikerbikus.models.Lezione;
import ferranti.bikerbikus.models.TipoLezione;
import ferranti.bikerbikus.models.Utente;
import ferranti.bikerbikus.queries.InsertLezioneQuery;
import ferranti.bikerbikus.queries.TipiLezioneQuery;
import ferranti.bikerbikus.queries.UtentiQuery;

public class AggiungiLezioneController {

	private Stage stage;
	private Parent parent;
	private ObservableList<LocalTime> orari = FXCollections.observableArrayList();
	private ObservableList<TipoLezione> tipi = FXCollections.observableArrayList();
	private ObservableList<Utente> maestri = FXCollections.observableArrayList();

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
	ComboBox<TipoLezione> cmbTipo;
	@FXML
	CheckBox cbPrivata;
	@FXML
	ComboBox<Utente> cmbMaestro;
	@FXML
	Button btnConferma;

	public void showScene(Stage stage) {
		this.stage = stage;
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/views/aggiungi-lezione-view.fxml"));
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
		dpGiorno.setDayCellFactory(picker -> new DateCell() {
			@Override
			public void updateItem(LocalDate date, boolean empty) {
				super.updateItem(date, empty);
				LocalDate today = LocalDate.now();
				setDisable(empty || date.compareTo(today) < 0);
			}
		});
		loadOrari();
		cmbOrario.setItems(orari);
		loadTipi();
		cmbTipo.setItems(tipi);
		loadMaestri();
		cmbMaestro.setItems(maestri);
		btnConferma.setOnAction(event -> onActionConferma());
	}

	private void onActionBack() {
		new LezioniController().showScene(stage);
	}

	private void onActionConferma() {
		if (dpGiorno.getValue() == null || cmbOrario.getValue() == null || cmbTipo.getValue() == null
				|| cmbMaestro.getValue() == null) {
			new Alert(AlertType.WARNING, "Inserisci tutti i dati!").show();
		} else {
			Lezione lezione = new Lezione();
			lezione.setData(LocalDateTime.of(dpGiorno.getValue(), cmbOrario.getValue()));
			lezione.setTipo(cmbTipo.getValue());
			lezione.setPrivata(cbPrivata.isSelected());
			lezione.setMaestro(cmbMaestro.getValue());
			if (InsertLezioneQuery.execute(lezione)) {
				new Alert(AlertType.CONFIRMATION, "Lezione creata con successo!", ButtonType.OK).show();
				new LezioniController().showScene(stage);
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

	private void loadTipi() {
		tipi.clear();
		tipi.addAll(TipiLezioneQuery.execute());
	}

	private void loadMaestri() {
		maestri.clear();
		maestri.addAll(UtentiQuery.findMaestri());
	}
}
