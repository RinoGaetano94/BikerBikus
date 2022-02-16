package ferranti.bikerbikus.controllers;

import ferranti.bikerbikus.utils.Constants;
import ferranti.bikerbikus.utils.Utils;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.text.Text;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

import ferranti.bikerbikus.data.UserData;
import ferranti.bikerbikus.models.Escursione;
import ferranti.bikerbikus.models.Gara;
import ferranti.bikerbikus.models.Lezione;
import ferranti.bikerbikus.models.Stagione;
import ferranti.bikerbikus.models.TipoLezione;
import ferranti.bikerbikus.models.Utente;
import ferranti.bikerbikus.queries.AreaPersonaleQuery;

public class AreaPersonaleController {

	private Stage stage;
	private Parent parent;
	private ObservableList<Lezione> lezioni = FXCollections.observableArrayList();
	private ObservableList<Gara> gare = FXCollections.observableArrayList();
	private ObservableList<Escursione> escursioni = FXCollections.observableArrayList();

	@FXML
	Button btnBack;
	@FXML
	Label lblUserName;
	@FXML
	Label lblTipoUtente;
	@FXML
	Text txtNome;
	@FXML
	Text txtCognome;
	@FXML
	Text txtEmail;
	@FXML
	Text txtTipoUtente;
	@FXML
	TabPane tabPanePrenotazioni;
	@FXML
	TableView<Lezione> tableLezioni;
	@FXML
	TableColumn<Lezione, LocalDateTime> colGiornoLezione;
	@FXML
	TableColumn<Lezione, LocalDateTime> colOrarioLezione;
	@FXML
	TableColumn<Lezione, TipoLezione> colTipoLezione;
	@FXML
	TableColumn<Lezione, Boolean> colPrivataLezione;
	@FXML
	TableColumn<Lezione, Utente> colMaestroLezione;
	@FXML
	TableColumn<Lezione, Integer> colDisdiciLezione;
	@FXML
	TableView<Gara> tableGare;
	@FXML
	TableColumn<Gara, LocalDateTime> colGiornoGara;
	@FXML
	TableColumn<Gara, LocalDateTime> colOrarioGara;
	@FXML
	TableColumn<Gara, Stagione> colCampionatoGara;
	@FXML
	TableColumn<Gara, Stagione> colStagioneGara;
	@FXML
	TableView<Escursione> tableEscursioni;
	@FXML
	TableColumn<Escursione, LocalDateTime> colGiornoEscursione;
	@FXML
	TableColumn<Escursione, LocalDateTime> colOrarioEscursione;
	@FXML
	TableColumn<Escursione, String> colLuogoEscursione;
	@FXML
	TableColumn<Escursione, Utente> colAccompagnatoreEscursione;
	@FXML
	TableColumn<Escursione, Integer> colDisdiciEscursione;

	public void showScene(Stage stage) {
		this.stage = stage;
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/views/area-personale-view.fxml"));
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
		txtNome.setText(UserData.getInstance().getUser().getNome());
		txtCognome.setText(UserData.getInstance().getUser().getCognome());
		txtEmail.setText(UserData.getInstance().getUser().getEmail());
		txtTipoUtente.setText(UserData.getInstance().getUser().getTipoUtente().getNome());
		tabPanePrenotazioni.widthProperty().addListener((observable, oldValue, newValue) -> {
			tabPanePrenotazioni
					.setTabMinWidth(tabPanePrenotazioni.getWidth() / tabPanePrenotazioni.getTabs().size() - 20);
			tabPanePrenotazioni
					.setTabMaxWidth(tabPanePrenotazioni.getWidth() / tabPanePrenotazioni.getTabs().size() - 20);
		});
		tabPanePrenotazioni.getSelectionModel().selectedItemProperty()
				.addListener((ov, oldTab, newTab) -> loadPrenotazioni());
		colGiornoLezione.setCellFactory(param -> new TableCell<>() {
			@Override
			protected void updateItem(LocalDateTime item, boolean empty) {
				super.updateItem(item, empty);
				if (item != null)
					setText(item.format(DateTimeFormatter.ofPattern(Constants.DEFAULT_DATE_PATTERN)));
			}
		});
		colGiornoLezione.setCellValueFactory(new PropertyValueFactory<>("data"));
		colOrarioLezione.setCellFactory(param -> new TableCell<>() {
			@Override
			protected void updateItem(LocalDateTime item, boolean empty) {
				super.updateItem(item, empty);
				if (item != null)
					setText(Utils.formatTime(item.getHour(), item.getMinute()));
			}
		});
		colOrarioLezione.setCellValueFactory(new PropertyValueFactory<>("data"));
		colTipoLezione.setCellFactory(param -> new TableCell<>() {
			@Override
			protected void updateItem(TipoLezione item, boolean empty) {
				super.updateItem(item, empty);
				if (item != null)
					setText(item.getNome());
			}
		});
		colTipoLezione.setCellValueFactory(new PropertyValueFactory<>("tipo"));
		colPrivataLezione.setCellFactory(param -> new TableCell<>() {
			@Override
			protected void updateItem(Boolean item, boolean empty) {
				super.updateItem(item, empty);
				if (item != null)
					setText(Boolean.TRUE.equals(item) ? "Si" : "No");
			}
		});
		colPrivataLezione.setCellValueFactory(new PropertyValueFactory<>("privata"));
		colMaestroLezione.setCellFactory(param -> new TableCell<>() {
			@Override
			protected void updateItem(Utente item, boolean empty) {
				super.updateItem(item, empty);
				if (item != null)
					setText(item.getNome() + " " + item.getCognome());
			}
		});
		colMaestroLezione.setCellValueFactory(new PropertyValueFactory<>("maestro"));
		colDisdiciLezione.setCellFactory(param -> new TableCell<>() {
			@Override
			protected void updateItem(Integer item, boolean empty) {
				super.updateItem(item, empty);
				final Button btnDisdiciLezione = new Button("Disdici");
				btnDisdiciLezione.setPrefSize(150, 20);
				btnDisdiciLezione.setOnAction(event -> disdiciLezione(item));
				if (getTableRow() != null && getTableRow().getItem() != null) {
					btnDisdiciLezione.setDisable(getTableRow().getItem().getData().isBefore(LocalDateTime.now()));
				}
				if (item != null)
					setGraphic(btnDisdiciLezione);
			}
		});
		colDisdiciLezione.setCellValueFactory(new PropertyValueFactory<>("id"));
		tableLezioni.setItems(lezioni);
		colGiornoGara.setCellFactory(param -> new TableCell<>() {
			@Override
			protected void updateItem(LocalDateTime item, boolean empty) {
				super.updateItem(item, empty);
				if (item != null)
					setText(item.format(DateTimeFormatter.ofPattern(Constants.DEFAULT_DATE_PATTERN)));
			}
		});
		colGiornoGara.setCellValueFactory(new PropertyValueFactory<>("data"));
		colOrarioGara.setCellFactory(param -> new TableCell<>() {
			@Override
			protected void updateItem(LocalDateTime item, boolean empty) {
				super.updateItem(item, empty);
				if (item != null)
					setText(Utils.formatTime(item.getHour(), item.getMinute()));
			}
		});
		colOrarioGara.setCellValueFactory(new PropertyValueFactory<>("data"));
		colCampionatoGara.setCellFactory(param -> new TableCell<>() {
			@Override
			protected void updateItem(Stagione item, boolean empty) {
				super.updateItem(item, empty);
				if (item != null)
					setText(item.getCampionato().getNome());
			}
		});
		colCampionatoGara
				.setCellValueFactory(cellData -> new SimpleObjectProperty<Stagione>(cellData.getValue().getStagione()));

		colStagioneGara.setCellFactory(param -> new TableCell<>() {
			@Override
			protected void updateItem(Stagione item, boolean empty) {
				super.updateItem(item, empty);
				if (item != null)
					setText(item.getNome());
			}
		});
		colStagioneGara.setCellValueFactory(new PropertyValueFactory<>("stagione"));
		tableGare.setItems(gare);
		colGiornoEscursione.setCellFactory(param -> new TableCell<>() {
			@Override
			protected void updateItem(LocalDateTime item, boolean empty) {
				super.updateItem(item, empty);
				if (item != null)
					setText(item.format(DateTimeFormatter.ofPattern(Constants.DEFAULT_DATE_PATTERN)));
			}
		});
		colGiornoEscursione.setCellValueFactory(new PropertyValueFactory<>("data"));
		colOrarioEscursione.setCellFactory(param -> new TableCell<>() {
			@Override
			protected void updateItem(LocalDateTime item, boolean empty) {
				super.updateItem(item, empty);
				if (item != null)
					setText(Utils.formatTime(item.getHour(), item.getMinute()));
			}
		});
		colOrarioEscursione.setCellValueFactory(new PropertyValueFactory<>("data"));
		colLuogoEscursione.setCellValueFactory(new PropertyValueFactory<>("luogo"));
		colAccompagnatoreEscursione.setCellFactory(param -> new TableCell<>() {
			@Override
			protected void updateItem(Utente item, boolean empty) {
				super.updateItem(item, empty);
				if (item != null)
					setText(item.getNome() + " " + item.getCognome());
			}
		});
		colAccompagnatoreEscursione.setCellValueFactory(new PropertyValueFactory<>("accompagnatore"));
		colDisdiciEscursione.setCellFactory(param -> new TableCell<>() {
			@Override
			protected void updateItem(Integer item, boolean empty) {
				super.updateItem(item, empty);
				final Button btnDisdiciEscursione = new Button("Disdici");
				btnDisdiciEscursione.setPrefSize(150, 20);
				btnDisdiciEscursione.setOnAction(event -> disdiciEscursione(item));
				if (getTableRow() != null && getTableRow().getItem() != null) {
					btnDisdiciEscursione.setDisable(getTableRow().getItem().getData().isBefore(LocalDateTime.now()));
				}
				if (item != null)
					setGraphic(btnDisdiciEscursione);
			}
		});
		colDisdiciEscursione.setCellValueFactory(new PropertyValueFactory<>("id"));
		tableEscursioni.setItems(escursioni);
		loadPrenotazioni();
	}

	private void onActionBack() {
		new HomeController().showScene(stage);
	}

	private void loadPrenotazioni() {
		switch (tabPanePrenotazioni.getSelectionModel().getSelectedItem().getId()) {
		case "tabLezioni": {
			lezioni.clear();
			lezioni.addAll(AreaPersonaleQuery.findLezioni(UserData.getInstance().getUser().getId()));
			break;
		}
		case "tabGare": {
			gare.clear();
			gare.addAll(AreaPersonaleQuery.findGare(UserData.getInstance().getUser().getId()));
			break;
		}
		case "tabEscursioni": {
			escursioni.clear();
			escursioni.addAll(AreaPersonaleQuery.findEscursioni(UserData.getInstance().getUser().getId()));
			break;
		}
		default:
			throw new IllegalArgumentException(
					"Unexpected value: " + tabPanePrenotazioni.getSelectionModel().getSelectedItem().getId());
		}
	}

	private void disdiciLezione(int idLezione) {
		Optional<ButtonType> option = new Alert(AlertType.CONFIRMATION, "Confermi di voler annullare la prenotazione?",
				ButtonType.NO, ButtonType.YES).showAndWait();
		if (option.isPresent() && option.get() == ButtonType.YES) {
			AreaPersonaleQuery.disdiciLezione(UserData.getInstance().getUser().getId(), idLezione);
			loadPrenotazioni();
		}
	}

	private void disdiciEscursione(int idEscursione) {
		Optional<ButtonType> option = new Alert(AlertType.CONFIRMATION, "Confermi di voler annullare la prenotazione?",
				ButtonType.NO, ButtonType.YES).showAndWait();
		if (option.isPresent() && option.get() == ButtonType.YES) {
			AreaPersonaleQuery.disdiciEscursione(UserData.getInstance().getUser().getId(), idEscursione);
			loadPrenotazioni();
		}
	}
}
