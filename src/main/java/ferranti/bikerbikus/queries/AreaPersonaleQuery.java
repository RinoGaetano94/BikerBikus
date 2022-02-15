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
	public static List<Lezione> findLezioni(int idUtente) {
        List<Lezione> result = new ArrayList<>();
        try (Connection connection = DriverManager.getConnection(Constants.URL, Constants.USERNAME, Constants.PASSWORD);
             PreparedStatement preparedStatement = createFindLezioniStatement(connection, idUtente);
             ResultSet resultSet = preparedStatement.executeQuery()) {
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
        } catch (SQLException e) {
        	new Alert(AlertType.ERROR, e.getMessage(), ButtonType.OK).show();
        }
        return result;
    }

    private static PreparedStatement createFindLezioniStatement(Connection connection, int idUtente) throws SQLException {
        String sql = "SELECT l.Id, l.Data, l.Privata, u.Nome AS NomeMaestro, u.Cognome AS CognomeMaestro, tl.Id AS TipoLezioneId, tl.Nome AS TipoLezioneNome FROM Lezione l LEFT JOIN Utente u ON u.Id = l.Maestro LEFT JOIN TipoLezione tl ON tl.Id = l.TipoLezione WHERE (?, l.Id) IN (SELECT pl.Utente, pl.Lezione FROM PrenotazioneLezione pl) ORDER BY l.Data DESC";
        PreparedStatement ps = connection.prepareStatement(sql);
        ps.setInt(1, idUtente);
        return ps;
    }
    
    public static boolean disdiciLezione(int idUtente, int idLezione) {
    	try (Connection connection = DriverManager.getConnection(Constants.URL, Constants.USERNAME, Constants.PASSWORD);
				PreparedStatement preparedStatement = createDisdiciLezioneStatement(connection, idUtente, idLezione)) {
			preparedStatement.executeUpdate();
			return true;
		} catch (SQLException e) {
			new Alert(AlertType.ERROR, e.getMessage(), ButtonType.OK).show();
		}
		return false;
    }

    private static PreparedStatement createDisdiciLezioneStatement(Connection connection, int idUtente, int idLezione) throws SQLException {
        String sql = "DELETE FROM PrenotazioneLezione WHERE Utente = ? AND Lezione = ?";
        PreparedStatement ps = connection.prepareStatement(sql);
        ps.setInt(1, idUtente);
        ps.setInt(2, idLezione);
        return ps;
    }
    
    public static List<Gara> findGare(int idUtente) {
    	List<Gara> result = new ArrayList<>();
        try (Connection connection = DriverManager.getConnection(Constants.URL, Constants.USERNAME, Constants.PASSWORD);
             PreparedStatement preparedStatement = createFindGareStatement(connection, idUtente);
             ResultSet resultSet = preparedStatement.executeQuery()) {
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
        } catch (SQLException e) {
        	new Alert(AlertType.ERROR, e.getMessage(), ButtonType.OK).show();
        }
        return result;
    }

    private static PreparedStatement createFindGareStatement(Connection connection, int idUtente) throws SQLException {
        String sql = "SELECT g.Id, g.Data, g.Stagione, c.Id AS Campionato, c.Nome AS NomeCampionato, s.Nome AS NomeStagione FROM Gara g LEFT JOIN Stagione s ON s.Id = g.Stagione LEFT JOIN Campionato c ON c.Id = s.Campionato WHERE (?, g.Id) IN (SELECT pg.Utente, pg.Gara FROM PrenotazioneGara pg) ORDER BY g.Data DESC";
        PreparedStatement ps = connection.prepareStatement(sql);
        ps.setInt(1, idUtente);
        return ps;
    }
    
    public static List<Escursione> findEscursioni(int idUtente) {
    	List<Escursione> result = new ArrayList<>();
        try (Connection connection = DriverManager.getConnection(Constants.URL, Constants.USERNAME, Constants.PASSWORD);
             PreparedStatement preparedStatement = createFindEscursioniStatement(connection, idUtente);
             ResultSet resultSet = preparedStatement.executeQuery()) {
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
        } catch (SQLException e) {
        	new Alert(AlertType.ERROR, e.getMessage(), ButtonType.OK).show();
        }
        return result;
    }

    private static PreparedStatement createFindEscursioniStatement(Connection connection, int idUtente) throws SQLException {
        String sql = "SELECT e.Id, e.Data, e.Luogo, u.Nome AS NomeAccompagnatore, u.Cognome AS CognomeAccompagnatore FROM Escursione e LEFT JOIN Utente u ON u.Id = e.Accompagnatore WHERE (?, e.Id) IN (SELECT pe.Utente, pe.Escursione FROM PrenotazioneEscursione pe) ORDER BY e.Data DESC";
        PreparedStatement ps = connection.prepareStatement(sql);
        ps.setInt(1, idUtente);
        return ps;
    }
    
    public static boolean disdiciEscursione(int idUtente, int idEscursione) {
    	try (Connection connection = DriverManager.getConnection(Constants.URL, Constants.USERNAME, Constants.PASSWORD);
				PreparedStatement preparedStatement = createDisdiciEscursioneStatement(connection, idUtente, idEscursione)) {
			preparedStatement.executeUpdate();
			return true;
		} catch (SQLException e) {
			new Alert(AlertType.ERROR, e.getMessage(), ButtonType.OK).show();
		}
		return false;
    }

    private static PreparedStatement createDisdiciEscursioneStatement(Connection connection, int idUtente, int idEscursione) throws SQLException {
        String sql = "DELETE FROM PrenotazioneEscursione WHERE Utente = ? AND Escursione = ?";
        PreparedStatement ps = connection.prepareStatement(sql);
        ps.setInt(1, idUtente);
        ps.setInt(2, idEscursione);
        return ps;
    }
}
