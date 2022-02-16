package ferranti.bikerbikus.queries;

import ferranti.bikerbikus.models.Campionato;
import ferranti.bikerbikus.models.Gara;
import ferranti.bikerbikus.models.Stagione;
import ferranti.bikerbikus.utils.Constants;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Alert.AlertType;

import java.sql.*;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;

public class GareQuery {

	private GareQuery() {
		throw new IllegalStateException("Utility class");
	}

	public static List<Gara> execute(YearMonth yearMonth, int userId) {
		String sql = "SELECT g.Id, g.Data, g.Stagione, c.Id AS Campionato, c.Nome AS NomeCampionato, s.Nome AS NomeStagione FROM Gara g LEFT JOIN Stagione s ON s.Id = g.Stagione LEFT JOIN Campionato c ON c.Id = s.Campionato WHERE MONTH(Data) = ? AND YEAR(Data) = ? AND (?, g.Id) NOT IN (SELECT pg.Utente, pg.Gara FROM PrenotazioneGara pg) ORDER BY g.Data";
		List<Gara> result = new ArrayList<>();
		try (Connection connection = DriverManager.getConnection(Constants.URL, Constants.USERNAME, Constants.PASSWORD);
				PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
			preparedStatement.setInt(1, yearMonth.getMonthValue());
			preparedStatement.setInt(2, yearMonth.getYear());
			preparedStatement.setInt(3, userId);
			try (ResultSet resultSet = preparedStatement.executeQuery()) {
				while (resultSet.next()) {
					Gara gara = new Gara();
					gara.setId(resultSet.getInt(1));
					gara.setData(resultSet.getTimestamp(2).toLocalDateTime());
					Stagione stagione = new Stagione();
					stagione.setId(resultSet.getInt(3));
					Campionato campionato = new Campionato();
					campionato.setId(resultSet.getInt(4));
					campionato.setNome(resultSet.getString(5));
					stagione.setCampionato(campionato);
					stagione.setNome(resultSet.getString(6));
					gara.setStagione(stagione);
					result.add(gara);
				}
			}
		} catch (

		SQLException e) {
			new Alert(AlertType.ERROR, e.getMessage(), ButtonType.OK).show();
		}
		return result;
	}
}
