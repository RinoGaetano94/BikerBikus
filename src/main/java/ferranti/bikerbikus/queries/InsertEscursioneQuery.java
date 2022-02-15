package ferranti.bikerbikus.queries;

import ferranti.bikerbikus.models.Escursione;
import ferranti.bikerbikus.utils.Constants;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Alert.AlertType;

import java.sql.*;

public class InsertEscursioneQuery {
	public static boolean execute(Escursione escursione) {
		try (Connection connection = DriverManager.getConnection(Constants.URL, Constants.USERNAME, Constants.PASSWORD);
				PreparedStatement preparedStatement = createStatement(connection, escursione)) {
			preparedStatement.executeUpdate();
			return true;
		} catch (SQLException e) {
			new Alert(AlertType.ERROR, e.getMessage(), ButtonType.OK).show();
		}
		return false;
	}

	private static PreparedStatement createStatement(Connection connection, Escursione escursione) throws SQLException {
		String sql = "INSERT INTO Escursione(Id, Data, Luogo, Accompagnatore) VALUES ((SELECT MAX(Id) + 1 FROM Escursione e2),?,?,?);";
		PreparedStatement ps = connection.prepareStatement(sql);
		ps.setTimestamp(1, Timestamp.valueOf(escursione.getData()));
		ps.setString(2, escursione.getLuogo());
		ps.setInt(3, escursione.getAccompagnatore().getId());
		return ps;
	}
}
