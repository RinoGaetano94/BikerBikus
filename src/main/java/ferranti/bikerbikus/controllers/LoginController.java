package ferranti.bikerbikus.controllers;

import ferranti.bikerbikus.queries.LoginQuery;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class LoginController {

    private Stage stage;
    private Parent parent;

    @FXML
    TextField txtEmail;
    @FXML
    PasswordField txtPassword;
    @FXML
    Button btnLogin;
    @FXML
    Hyperlink linkRegister;

    public void showScene(Stage stage) {
        this.stage = stage;
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/views/login-view.fxml"));
        fxmlLoader.setController(this);
        try {
            parent = fxmlLoader.load();
        } catch (IOException e) {
        	new Alert(AlertType.ERROR, e.getMessage(), ButtonType.OK).show();
        }
        stage.getScene().setRoot(parent);
        btnLogin.setOnAction(event -> onActionLogin());
        linkRegister.setOnAction(event -> onActionRegister());
    }



    private void onActionLogin() {
        if(LoginQuery.execute(txtEmail.getText(), txtPassword.getText())) {
            new HomeController().showScene(stage);
        }
        else {
        	new Alert(AlertType.ERROR, "Credenziali errate", ButtonType.OK).show();
        }
    }

    private void onActionRegister() {
    	// TODO: Implementare
    	new Alert(AlertType.INFORMATION, "La registrazione non è al momento disponibile", ButtonType.OK).show();
    }
}
