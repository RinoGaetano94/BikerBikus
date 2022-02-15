package ferranti.bikerbikus.queries;

import ferranti.bikerbikus.models.Lezione;
import ferranti.bikerbikus.utils.Constants;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Alert.AlertType;

import java.sql.*;

public class InsertLezioneQuery {
	public static boolean execute(Lezione lezione) {
		try (Connection connection = DriverManager.getConnection(Constants.URL, Constants.USERNAME, Constants.PASSWORD);
				PreparedStatement preparedStatement = createStatement(connection, lezione)) {
			preparedStatement.executeUpdate();
			return true;
		} catch (SQLException e) {
			new Alert(AlertType.ERROR, e.getMessage(), ButtonType.OK).show();
		}
		return false;
	}

	private static PreparedStatement createStatement(Connection connection, Lezione lezione) throws SQLException {
		String sql = "INSERT INTO Lezione(Id, Data, Maestro, TipoLezione, Privata) VALUES ((SELECT MAX(Id) + 1 FROM Lezione l2),?,?,?,?);";
		PreparedStatement ps = connection.prepareStatement(sql);
		ps.setTimestamp(1, Timestamp.valueOf(lezione.getData()));
		ps.setInt(2, lezione.getMaestro().getId());
		ps.setInt(3, lezione.getTipo().getId());
		ps.setBoolean(4, lezione.isPrivata());
		return ps;
	}
}
