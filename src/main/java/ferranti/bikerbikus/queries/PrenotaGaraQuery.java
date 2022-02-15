package ferranti.bikerbikus.queries;

import ferranti.bikerbikus.utils.Constants;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Alert.AlertType;

import java.sql.*;
import java.time.LocalDateTime;

public class PrenotaGaraQuery {
	public static boolean execute(int idUtente, int idGara) {
		try (Connection connection = DriverManager.getConnection(Constants.URL, Constants.USERNAME, Constants.PASSWORD);
				PreparedStatement preparedStatement = createStatement(connection, idUtente, idGara)) {
			preparedStatement.executeUpdate();
			return true;
		} catch (SQLException e) {
			new Alert(AlertType.ERROR, e.getMessage(), ButtonType.OK).show();
		}
		return false;
	}

	private static PreparedStatement createStatement(Connection connection, int idUtente, int idGara)
			throws SQLException {
		String sql = "INSERT INTO PrenotazioneGara VALUES(?,?,null,?);";
		PreparedStatement ps = connection.prepareStatement(sql);
		ps.setInt(1, idUtente);
		ps.setInt(2, idGara);
		ps.setTimestamp(3, Timestamp.valueOf(LocalDateTime.now()));
		return ps;
	}
}
