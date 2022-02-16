package ferranti.bikerbikus.queries;

import ferranti.bikerbikus.models.Lezione;
import ferranti.bikerbikus.utils.Constants;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Alert.AlertType;

import java.sql.*;

public class InsertLezioneQuery {

	private InsertLezioneQuery() {
		throw new IllegalStateException("Utility class");
	}

	public static boolean execute(Lezione lezione) {
		String sql = "INSERT INTO Lezione(Id, Data, Maestro, TipoLezione, Privata) VALUES ((SELECT MAX(Id) + 1 FROM Lezione l2),?,?,?,?);";
		try (Connection connection = DriverManager.getConnection(Constants.URL, Constants.USERNAME, Constants.PASSWORD);
				PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
			preparedStatement.setTimestamp(1, Timestamp.valueOf(lezione.getData()));
			preparedStatement.setInt(2, lezione.getMaestro().getId());
			preparedStatement.setInt(3, lezione.getTipo().getId());
			preparedStatement.setBoolean(4, lezione.isPrivata());
			preparedStatement.executeUpdate();
			return true;
		} catch (SQLException e) {
			new Alert(AlertType.ERROR, e.getMessage(), ButtonType.OK).show();
		}
		return false;
	}
}
