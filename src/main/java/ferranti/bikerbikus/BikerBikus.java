package ferranti.bikerbikus;

import ferranti.bikerbikus.controllers.LoginController;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class BikerBikus extends Application {
    @Override
    public void start(Stage stage) {
        stage.setMaximized(true);
        stage.setTitle("BikerBikus");
        stage.setScene(new Scene(new Pane()));
        stage.show();
        new LoginController().showScene(stage);
    }

    public static void main(String[] args) {
        launch();
    }
}