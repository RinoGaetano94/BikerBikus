package ferranti.bikerbikus.controllers;

import ferranti.bikerbikus.data.UserData;
import ferranti.bikerbikus.models.Utente;
import ferranti.bikerbikus.queries.EscursioniQuery;
import ferranti.bikerbikus.queries.PrenotaEscursioneQuery;
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

import ferranti.bikerbikus.models.Escursione;

public class EscursioniController {

	private Stage stage;
	private Parent parent;
	private YearMonth currentYearMonth = YearMonth.now();
	private ObservableList<Escursione> escursioni = FXCollections.observableArrayList();

	@FXML
	HBox toolbar;
	@FXML
	Button btnBack;
	@FXML
	Label lblUserName;
	@FXML
	Label lblTipoUtente;
	@FXML
	Button btnAddEscursione;
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
	TableView<Escursione> tableEscursioni;
	@FXML
	TableColumn<Escursione, LocalDateTime> colGiorno;
	@FXML
	TableColumn<Escursione, LocalDateTime> colOrario;
	@FXML
	TableColumn<Escursione, String> colLuogo;
	@FXML
	TableColumn<Escursione, Utente> colAccompagnatore;
	@FXML
	TableColumn<Escursione, Integer> colPrenotazione;

	public void showScene(Stage stage) {
		this.stage = stage;
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/views/escursioni-view.fxml"));
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
			btnAddEscursione.setOnAction(event -> onActionAddEscursione());
		} else {
			toolbar.getChildren().remove(btnAddEscursione);
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
				setText(item == null ? "" : Utils.formatTime(item.getHour(), item.getMinute()));
			}
		});
		colOrario.setCellValueFactory(new PropertyValueFactory<>("data"));
		colLuogo.setCellValueFactory(new PropertyValueFactory<>("luogo"));
		colAccompagnatore.setCellFactory(param -> new TableCell<>() {
			@Override
			protected void updateItem(Utente item, boolean empty) {
				super.updateItem(item, empty);
				setText(item == null ? "" : item.getNome() + " " + item.getCognome());
			}
		});
		colAccompagnatore.setCellValueFactory(new PropertyValueFactory<>("accompagnatore"));
		colPrenotazione.setCellFactory(param -> new TableCell<>() {
			@Override
			protected void updateItem(Integer item, boolean empty) {
				super.updateItem(item, empty);
				final Button btnPrenota = new Button("Prenota");
				btnPrenota.setPrefSize(150, 20);
				btnPrenota.setOnAction(event -> prenotaEscursione(item));
				if (getTableRow() != null && getTableRow().getItem() != null) {
					btnPrenota.setDisable(getTableRow().getItem().getData().isBefore(LocalDateTime.now()));
				}
				setGraphic(item == null ? null : btnPrenota);
			}
		});
		colPrenotazione.setCellValueFactory(new PropertyValueFactory<>("id"));
		tableEscursioni.setItems(escursioni);
		loadEscursioni();
	}

	private void onActionBack() {
		new HomeController().showScene(stage);
	}
	
	private void onActionAddEscursione() {
		new AggiungiEscursioneController().showScene(stage);
	}

	private void onActionProfile() {
		new AreaPersonaleController().showScene(stage);
	}

	private void onActionPrevMonth() {
		currentYearMonth = currentYearMonth.minusMonths(1);
		loadEscursioni();
	}

	private void onActionNextMonth() {
		currentYearMonth = currentYearMonth.plusMonths(1);
		loadEscursioni();
	}

	private void loadEscursioni() {
		lblMese.setText(
				Utils.uppercase(currentYearMonth.getMonth().getDisplayName(TextStyle.FULL, Locale.getDefault())));
		lblAnno.setText(Integer.toString(currentYearMonth.getYear()));
		escursioni.clear();
		escursioni.addAll(EscursioniQuery.execute(currentYearMonth, UserData.getInstance().getUser().getId()));
	}

	private void prenotaEscursione(int idEscursione) {
		if (PrenotaEscursioneQuery.execute(UserData.getInstance().getUser().getId(), idEscursione)) {
			new Alert(AlertType.CONFIRMATION, "Prenotazione effettuata con successo!", ButtonType.OK).show();
			loadEscursioni();
		}
	}
}
