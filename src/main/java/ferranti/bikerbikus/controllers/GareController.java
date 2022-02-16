package ferranti.bikerbikus.controllers;

import ferranti.bikerbikus.data.UserData;
import ferranti.bikerbikus.queries.GareQuery;
import ferranti.bikerbikus.queries.PrenotaGaraQuery;
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

import ferranti.bikerbikus.models.Gara;
import ferranti.bikerbikus.models.Stagione;

public class GareController {

	private Stage stage;
	private Parent parent;
	private YearMonth currentYearMonth = YearMonth.now();
	private ObservableList<Gara> gare = FXCollections.observableArrayList();

	@FXML
	HBox toolbar;
	@FXML
	Button btnBack;
	@FXML
	Label lblUserName;
	@FXML
	Label lblTipoUtente;
	@FXML
	Button btnAddGara;
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
	TableView<Gara> tableGare;
	@FXML
	TableColumn<Gara, LocalDateTime> colGiorno;
	@FXML
	TableColumn<Gara, LocalDateTime> colOrario;
	@FXML
	TableColumn<Gara, Stagione> colStagione;
	@FXML
	TableColumn<Gara, Integer> colPrenotazione;

	public void showScene(Stage stage) {
		this.stage = stage;
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/views/gare-view.fxml"));
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
			btnAddGara.setOnAction(event -> onActionAddGara());
		} else {
			toolbar.getChildren().remove(btnAddGara);
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
		colStagione.setCellFactory(param -> new TableCell<>() {
			@Override
			protected void updateItem(Stagione item, boolean empty) {
				super.updateItem(item, empty);
				final Hyperlink hyperlink = new Hyperlink(item == null ? "" : item.toString());
				hyperlink.setOnAction(event -> showStagione(item));
				setGraphic(item == null ? null : hyperlink);
			}
		});
		colStagione.setCellValueFactory(new PropertyValueFactory<>("stagione"));
		colPrenotazione.setCellFactory(param -> new TableCell<>() {
			@Override
			protected void updateItem(Integer item, boolean empty) {
				super.updateItem(item, empty);
				final Button btnPrenota = new Button("Prenota");
				btnPrenota.setPrefSize(150, 20);
				btnPrenota.setOnAction(event -> prenotaGara(item));
				if (getTableRow() != null && getTableRow().getItem() != null) {
					btnPrenota.setDisable(getTableRow().getItem().getData().isBefore(LocalDateTime.now()));
				}
				setGraphic(item == null ? null : btnPrenota);
			}
		});
		colPrenotazione.setCellValueFactory(new PropertyValueFactory<>("id"));
		tableGare.setItems(gare);
		loadGare();
	}

	private void onActionBack() {
		new HomeController().showScene(stage);
	}

	private void onActionAddGara() {
		new AggiungiGaraController().showScene(stage);
	}

	private void onActionProfile() {
		new AreaPersonaleController().showScene(stage);
	}

	private void onActionPrevMonth() {
		currentYearMonth = currentYearMonth.minusMonths(1);
		loadGare();
	}

	private void onActionNextMonth() {
		currentYearMonth = currentYearMonth.plusMonths(1);
		loadGare();
	}

	private void loadGare() {
		lblMese.setText(
				Utils.uppercase(currentYearMonth.getMonth().getDisplayName(TextStyle.FULL, Locale.getDefault())));
		lblAnno.setText(Integer.toString(currentYearMonth.getYear()));
		gare.clear();
		gare.addAll(GareQuery.execute(currentYearMonth, UserData.getInstance().getUser().getId()));
	}

	private void prenotaGara(int idGara) {
		if (PrenotaGaraQuery.execute(UserData.getInstance().getUser().getId(), idGara)) {
			new Alert(AlertType.CONFIRMATION, "Prenotazione effettuata con successo!", ButtonType.OK).show();
			loadGare();
		}
	}

	private void showStagione(Stagione stagione) {
		new StagioneController(stagione).showScene(stage);
	}
}
