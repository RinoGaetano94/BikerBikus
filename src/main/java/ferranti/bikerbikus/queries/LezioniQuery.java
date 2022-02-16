package ferranti.bikerbikus.queries;

import ferranti.bikerbikus.models.Lezione;
import ferranti.bikerbikus.models.TipoLezione;
import ferranti.bikerbikus.models.Utente;
import ferranti.bikerbikus.utils.Constants;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Alert.AlertType;

import java.sql.*;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;

public class LezioniQuery {

	private LezioniQuery() {
		throw new IllegalStateException("Utility class");
	}

	public static List<Lezione> execute(YearMonth yearMonth, int userId) {
		String sql = "SELECT l.Id, l.Data, l.Privata, u.Nome AS NomeMaestro, u.Cognome AS CognomeMaestro, tl.Id AS TipoLezioneId, tl.Nome AS TipoLezioneNome FROM Lezione l LEFT JOIN Utente u ON u.Id = l.Maestro LEFT JOIN TipoLezione tl ON tl.Id = l.TipoLezione WHERE MONTH(Data) = ? AND YEAR(Data) = ? AND (?, l.Id) NOT IN (SELECT pl.Utente, pl.Lezione FROM PrenotazioneLezione pl) ORDER BY l.Data";
		List<Lezione> result = new ArrayList<>();
		try (Connection connection = DriverManager.getConnection(Constants.URL, Constants.USERNAME, Constants.PASSWORD);
				PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
			preparedStatement.setInt(1, yearMonth.getMonthValue());
			preparedStatement.setInt(2, yearMonth.getYear());
			preparedStatement.setInt(3, userId);
			try (ResultSet resultSet = preparedStatement.executeQuery()) {
				while (resultSet.next()) {
					Lezione lezione = new Lezione();
					lezione.setId(resultSet.getInt(1));
					lezione.setData(resultSet.getTimestamp(2).toLocalDateTime());
					lezione.setPrivata(resultSet.getBoolean(3));
					Utente maestro = new Utente();
					maestro.setNome(resultSet.getString(4));
					maestro.setCognome(resultSet.getString(5));
					lezione.setMaestro(maestro);
					TipoLezione tipoLezione = new TipoLezione();
					tipoLezione.setId(resultSet.getInt(6));
					tipoLezione.setNome(resultSet.getString(7));
					lezione.setTipo(tipoLezione);
					result.add(lezione);
				}
			}
		} catch (SQLException e) {
			new Alert(AlertType.ERROR, e.getMessage(), ButtonType.OK).show();
		}
		return result;
	}
}
