package ferranti.bikerbikus.queries;

import ferranti.bikerbikus.utils.Constants;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Alert.AlertType;

import java.sql.*;
import java.time.LocalDateTime;

public class PrenotaEscursioneQuery {

	private PrenotaEscursioneQuery() {
		throw new IllegalStateException("Utility class");
	}

	public static boolean execute(int idUtente, int idEscursione) {
		String sql = "INSERT INTO PrenotazioneEscursione VALUES(?,?,?);";
		try (Connection connection = DriverManager.getConnection(Constants.URL, Constants.USERNAME, Constants.PASSWORD);
				PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
			preparedStatement.setInt(1, idUtente);
			preparedStatement.setInt(2, idEscursione);
			preparedStatement.setTimestamp(3, Timestamp.valueOf(LocalDateTime.now()));
			preparedStatement.executeUpdate();
			return true;
		} catch (SQLException e) {
			new Alert(AlertType.ERROR, e.getMessage(), ButtonType.OK).show();
		}
		return false;
	}
}
