package ferranti.bikerbikus.queries;

import ferranti.bikerbikus.models.GaraExtended;
import ferranti.bikerbikus.models.Stagione;
import ferranti.bikerbikus.utils.Constants;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Alert.AlertType;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class StagioneQuery {

	private StagioneQuery() {
		throw new IllegalStateException("Utility class");
	}

	public static List<GaraExtended> execute(int idStagione) {
		String sql = "SELECT g.*, (SELECT COUNT(*) FROM PrenotazioneGara WHERE Gara = g.Id) Partecipanti, u.Nome AS NomeVincitore, u.Cognome AS CognomeVincitore FROM Gara g LEFT JOIN Utente u ON u.Id = (SELECT Utente FROM PrenotazioneGara WHERE Gara = g.Id AND Posizione = 1) WHERE Stagione = ? ORDER BY Data";
		List<GaraExtended> result = new ArrayList<>();
		try (Connection connection = DriverManager.getConnection(Constants.URL, Constants.USERNAME, Constants.PASSWORD);
				PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
			preparedStatement.setInt(1, idStagione);
			try (ResultSet resultSet = preparedStatement.executeQuery()) {
				while (resultSet.next()) {
					GaraExtended gara = new GaraExtended();
					gara.setId(resultSet.getInt(1));
					Stagione stagione = new Stagione();
					stagione.setId(resultSet.getInt(2));
					gara.setStagione(stagione);
					gara.setData(resultSet.getTimestamp(3).toLocalDateTime());
					gara.setPartecipanti(resultSet.getInt(4));
					gara.setNomeVincitore(resultSet.getString(5));
					gara.setCognomeVincitore(resultSet.getString(6));
					result.add(gara);
				}
			}
		} catch (SQLException e) {
			new Alert(AlertType.ERROR, e.getMessage(), ButtonType.OK).show();
		}
		return result;
	}
}
