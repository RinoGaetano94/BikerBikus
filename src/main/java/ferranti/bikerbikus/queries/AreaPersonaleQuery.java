package ferranti.bikerbikus.queries;

import ferranti.bikerbikus.models.Campionato;
import ferranti.bikerbikus.models.Escursione;
import ferranti.bikerbikus.models.Gara;
import ferranti.bikerbikus.models.Lezione;
import ferranti.bikerbikus.models.Stagione;
import ferranti.bikerbikus.models.TipoLezione;
import ferranti.bikerbikus.models.Utente;
import ferranti.bikerbikus.utils.Constants;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Alert.AlertType;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AreaPersonaleQuery {

	private AreaPersonaleQuery() {
		throw new IllegalStateException("Utility class");
	}

	public static List<Lezione> findLezioni(int idUtente) {
		String sql = "SELECT l.Id, l.Data, l.Privata, u.Nome AS NomeMaestro, u.Cognome AS CognomeMaestro, tl.Id AS TipoLezioneId, tl.Nome AS TipoLezioneNome FROM Lezione l LEFT JOIN Utente u ON u.Id = l.Maestro LEFT JOIN TipoLezione tl ON tl.Id = l.TipoLezione WHERE (?, l.Id) IN (SELECT pl.Utente, pl.Lezione FROM PrenotazioneLezione pl) ORDER BY l.Data DESC";
		List<Lezione> result = new ArrayList<>();
		try (Connection connection = DriverManager.getConnection(Constants.URL, Constants.USERNAME, Constants.PASSWORD);
				PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
			preparedStatement.setInt(1, idUtente);
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

	public static boolean disdiciLezione(int idUtente, int idLezione) {
		String sql = "DELETE FROM PrenotazioneLezione WHERE Utente = ? AND Lezione = ?";
		try (Connection connection = DriverManager.getConnection(Constants.URL, Constants.USERNAME, Constants.PASSWORD);
				PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
			preparedStatement.setInt(1, idUtente);
			preparedStatement.setInt(2, idLezione);
			preparedStatement.executeUpdate();
			return true;
		} catch (SQLException e) {
			new Alert(AlertType.ERROR, e.getMessage(), ButtonType.OK).show();
		}
		return false;
	}

	public static List<Gara> findGare(int idUtente) {
		String sql = "SELECT g.Id, g.Data, g.Stagione, c.Id AS Campionato, c.Nome AS NomeCampionato, s.Nome AS NomeStagione FROM Gara g LEFT JOIN Stagione s ON s.Id = g.Stagione LEFT JOIN Campionato c ON c.Id = s.Campionato WHERE (?, g.Id) IN (SELECT pg.Utente, pg.Gara FROM PrenotazioneGara pg) ORDER BY g.Data DESC";
		List<Gara> result = new ArrayList<>();
		try (Connection connection = DriverManager.getConnection(Constants.URL, Constants.USERNAME, Constants.PASSWORD);
				PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
			preparedStatement.setInt(1, idUtente);
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
		} catch (SQLException e) {
			new Alert(AlertType.ERROR, e.getMessage(), ButtonType.OK).show();
		}
		return result;
	}

	public static List<Escursione> findEscursioni(int idUtente) {
		String sql = "SELECT e.Id, e.Data, e.Luogo, u.Nome AS NomeAccompagnatore, u.Cognome AS CognomeAccompagnatore FROM Escursione e LEFT JOIN Utente u ON u.Id = e.Accompagnatore WHERE (?, e.Id) IN (SELECT pe.Utente, pe.Escursione FROM PrenotazioneEscursione pe) ORDER BY e.Data DESC";
		List<Escursione> result = new ArrayList<>();
		try (Connection connection = DriverManager.getConnection(Constants.URL, Constants.USERNAME, Constants.PASSWORD);
				PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
			preparedStatement.setInt(1, idUtente);
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

	public static boolean disdiciEscursione(int idUtente, int idEscursione) {
		String sql = "DELETE FROM PrenotazioneEscursione WHERE Utente = ? AND Escursione = ?";
		try (Connection connection = DriverManager.getConnection(Constants.URL, Constants.USERNAME, Constants.PASSWORD);
				PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
			preparedStatement.setInt(1, idUtente);
			preparedStatement.setInt(2, idEscursione);
			preparedStatement.executeUpdate();
			return true;
		} catch (SQLException e) {
			new Alert(AlertType.ERROR, e.getMessage(), ButtonType.OK).show();
		}
		return false;
	}
}
