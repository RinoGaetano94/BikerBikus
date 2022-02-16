package ferranti.bikerbikus.queries;

import ferranti.bikerbikus.models.Escursione;
import ferranti.bikerbikus.models.Utente;
import ferranti.bikerbikus.utils.Constants;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Alert.AlertType;

import java.sql.*;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;

public class EscursioniQuery {

	private EscursioniQuery() {
		throw new IllegalStateException("Utility class");
	}

	public static List<Escursione> execute(YearMonth yearMonth, int userId) {
		String sql = "SELECT e.Id, e.Data, e.Luogo, u.Nome AS NomeAccompagnatore, u.Cognome AS CognomeAccompagnatore FROM Escursione e LEFT JOIN Utente u ON u.Id = e.Accompagnatore WHERE MONTH(Data) = ? AND YEAR(Data) = ? AND (?, e.Id) NOT IN (SELECT pe.Utente, pe.Escursione FROM PrenotazioneEscursione pe) ORDER BY e.Data";
		List<Escursione> result = new ArrayList<>();
		try (Connection connection = DriverManager.getConnection(Constants.URL, Constants.USERNAME, Constants.PASSWORD);
				PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
			preparedStatement.setInt(1, yearMonth.getMonthValue());
			preparedStatement.setInt(2, yearMonth.getYear());
			preparedStatement.setInt(3, userId);
			try (ResultSet resultSet = preparedStatement.executeQuery()) {
				while (resultSet.next()) {
					Escursione escursione = new Escursione();
					escursione.setId(resultSet.getInt(1));
					escursione.setData(resultSet.getTimestamp(2).toLocalDateTime());
					escursione.setLuogo(resultSet.getString(3));
					Utente accompagnatore = new Utente();
					accompagnatore.setNome(resultSet.getString(4));
					accompagnatore.setCognome(resultSet.getString(5));
					escursione.setAccompagnatore(accompagnatore);
					result.add(escursione);
				}
			}
		} catch (SQLException e) {
			new Alert(AlertType.ERROR, e.getMessage(), ButtonType.OK).show();
		}
		return result;
	}
}
