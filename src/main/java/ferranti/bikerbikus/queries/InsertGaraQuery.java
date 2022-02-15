package ferranti.bikerbikus.queries;

import ferranti.bikerbikus.models.Gara;
import ferranti.bikerbikus.utils.Constants;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Alert.AlertType;

import java.sql.*;

public class InsertGaraQuery {
	public static boolean execute(Gara gara) {
		try (Connection connection = DriverManager.getConnection(Constants.URL, Constants.USERNAME, Constants.PASSWORD);
				PreparedStatement preparedStatement = createStatement(connection, gara)) {
			preparedStatement.executeUpdate();
			return true;
		} catch (SQLException e) {
			new Alert(AlertType.ERROR, e.getMessage(), ButtonType.OK).show();
		}
		return false;
	}

	private static PreparedStatement createStatement(Connection connection, Gara gara) throws SQLException {
		String sql = "INSERT INTO Gara(Id, Stagione, Data) VALUES ((SELECT MAX(Id) + 1 FROM Gara g2),?,?);";
		PreparedStatement ps = connection.prepareStatement(sql);
		ps.setInt(1, gara.getStagione().getId());
		ps.setTimestamp(2, Timestamp.valueOf(gara.getData()));
		return ps;
	}
}
