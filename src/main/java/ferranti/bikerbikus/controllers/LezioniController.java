package ferranti.bikerbikus.controllers;

import ferranti.bikerbikus.data.UserData;
import ferranti.bikerbikus.models.TipoLezione;
import ferranti.bikerbikus.models.Utente;
import ferranti.bikerbikus.queries.LezioniQuery;
import ferranti.bikerbikus.queries.PrenotaLezioneQuery;
import ferranti.bikerbikus.utils.Utils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.*;
import java.time.format.TextStyle;
import java.util.Locale;

import ferranti.bikerbikus.models.Lezione;

public class LezioniController {

	private Stage stage;
	private Parent parent;
	private YearMonth currentYearMonth = YearMonth.now();
	private ObservableList<Lezione> lezioni = FXCollections.observableArrayList();

	@FXML
	HBox toolbar;
	@FXML
	Button btnBack;
	@FXML
	Label lblUserName;
	@FXML
	Label lblTipoUtente;
	@FXML
	Button btnAddLezione;
	@FXML
	Button btnProfile;
	@FXML
	Button btnPrevMonth;
	@FXML
	Button btnNextMonth;
	@FXML
	Label lblMese;
	@FXML
	Label lblAnno;
	@FXML
	TableView<Lezione> tableLezioni;
	@FXML
	TableColumn<Lezione, LocalDateTime> colGiorno;
	@FXML
	TableColumn<Lezione, LocalDateTime> colOrario;
	@FXML
	TableColumn<Lezione, TipoLezione> colTipo;
	@FXML
	TableColumn<Lezione, Boolean> colPrivata;
	@FXML
	TableColumn<Lezione, Utente> colMaestro;
	@FXML
	TableColumn<Lezione, Integer> colPrenotazione;

	public void showScene(Stage stage) {
		this.stage = stage;
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/views/lezioni-view.fxml"));
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
		if (UserData.getInstance().isMaestro() || UserData.getInstance().isMaestroAvanzato()) {
			btnAddLezione.setOnAction(event -> onActionAddLezione());
		} else {
			toolbar.getChildren().remove(btnAddLezione);
		}
		btnProfile.setOnAction(event -> onActionProfile());
		btnPrevMonth.setOnAction(event -> onActionPrevMonth());
		btnNextMonth.setOnAction(event -> onActionNextMonth());
		colGiorno.setCellFactory(param -> new TableCell<>() {
			@Override
			protected void updateItem(LocalDateTime item, boolean empty) {
				super.updateItem(item, empty);
				setText(item == null ? ""
						: Utils.uppercase(item.getDayOfWeek().getDisplayName(TextStyle.FULL, Locale.getDefault())) + " "
								+ item.getDayOfMonth());
			}
		});
		colGiorno.setCellValueFactory(new PropertyValueFactory<>("data"));
		colOrario.setCellFactory(param -> new TableCell<>() {
			@Override
			protected void updateItem(LocalDateTime item, boolean empty) {
				super.updateItem(item, empty);
				if (item != null)
					setText(Utils.formatTime(item.getHour(), item.getMinute()));
			}
		});
		colOrario.setCellValueFactory(new PropertyValueFactory<>("data"));
		colTipo.setCellFactory(param -> new TableCell<>() {
			@Override
			protected void updateItem(TipoLezione item, boolean empty) {
				super.updateItem(item, empty);
				if (item != null)
					setText(item.getNome());
			}
		});
		colTipo.setCellValueFactory(new PropertyValueFactory<>("tipo"));
		colPrivata.setCellFactory(param -> new TableCell<>() {
			@Override
			protected void updateItem(Boolean item, boolean empty) {
				super.updateItem(item, empty);
				if (item != null)
					setText(Boolean.TRUE.equals(item) ? "Si" : "No");
			}
		});
		colPrivata.setCellValueFactory(new PropertyValueFactory<>("privata"));
		colMaestro.setCellFactory(param -> new TableCell<>() {
			@Override
			protected void updateItem(Utente item, boolean empty) {
				super.updateItem(item, empty);
				if (item != null)
					setText(item.getNome() + " " + item.getCognome());
			}
		});
		colMaestro.setCellValueFactory(new PropertyValueFactory<>("maestro"));
		colPrenotazione.setCellFactory(param -> new TableCell<>() {
			@Override
			protected void updateItem(Integer item, boolean empty) {
				super.updateItem(item, empty);
				final Button btnPrenota = new Button("Prenota");
				btnPrenota.setPrefSize(150, 20);
				btnPrenota.setOnAction(event -> prenotaLezione(item));
				if (getTableRow() != null && getTableRow().getItem() != null) {
					btnPrenota.setDisable(getTableRow().getItem().getData().isBefore(LocalDateTime.now()));
				}
				setGraphic(item == null ? null : btnPrenota);
			}
		});
		colPrenotazione.setCellValueFactory(new PropertyValueFactory<>("id"));
		tableLezioni.setItems(lezioni);
		loadLezioni();
	}

	private void onActionBack() {
		new HomeController().showScene(stage);
	}

	private void onActionAddLezione() {
		new AggiungiLezioneController().showScene(stage);
	}

	private void onActionProfile() {
		new AreaPersonaleController().showScene(stage);
	}

	private void onActionPrevMonth() {
		currentYearMonth = currentYearMonth.minusMonths(1);
		loadLezioni();
	}

	private void onActionNextMonth() {
		currentYearMonth = currentYearMonth.plusMonths(1);
		loadLezioni();
	}

	private void loadLezioni() {
		lblMese.setText(
				Utils.uppercase(currentYearMonth.getMonth().getDisplayName(TextStyle.FULL, Locale.getDefault())));
		lblAnno.setText(Integer.toString(currentYearMonth.getYear()));
		lezioni.clear();
		lezioni.addAll(LezioniQuery.execute(currentYearMonth, UserData.getInstance().getUser().getId()));
	}

	private void prenotaLezione(int idLezione) {
		if (PrenotaLezioneQuery.execute(UserData.getInstance().getUser().getId(), idLezione)) {
			new Alert(AlertType.CONFIRMATION, "Prenotazione effettuata con successo!", ButtonType.OK).show();
			loadLezioni();
		}
	}
}
