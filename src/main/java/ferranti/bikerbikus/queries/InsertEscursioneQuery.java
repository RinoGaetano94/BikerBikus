package ferranti.bikerbikus.queries;

import ferranti.bikerbikus.models.Escursione;
import ferranti.bikerbikus.utils.Constants;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Alert.AlertType;

import java.sql.*;

public class InsertEscursioneQuery {

	private InsertEscursioneQuery() {
		throw new IllegalStateException("Utility class");
	}

	public static boolean execute(Escursione escursione) {
		String sql = "INSERT INTO Escursione(Id, Data, Luogo, Accompagnatore) VALUES ((SELECT MAX(Id) + 1 FROM Escursione e2),?,?,?);";
		try (Connection connection = DriverManager.getConnection(Constants.URL, Constants.USERNAME, Constants.PASSWORD);
				PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
			preparedStatement.setTimestamp(1, Timestamp.valueOf(escursione.getData()));
			preparedStatement.setString(2, escursione.getLuogo());
			preparedStatement.setInt(3, escursione.getAccompagnatore().getId());
			preparedStatement.executeUpdate();
			return true;
		} catch (SQLException e) {
			new Alert(AlertType.ERROR, e.getMessage(), ButtonType.OK).show();
		}
		return false;
	}
}
