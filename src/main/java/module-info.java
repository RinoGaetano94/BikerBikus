module ferranti.bikerbikus {
	requires transitive javafx.graphics;
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens ferranti.bikerbikus to javafx.fxml;
    opens ferranti.bikerbikus.models to javafx.base;
    opens ferranti.bikerbikus.controllers to javafx.fxml;
    exports ferranti.bikerbikus;
}