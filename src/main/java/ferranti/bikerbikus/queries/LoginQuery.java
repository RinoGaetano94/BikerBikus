package ferranti.bikerbikus.queries;

import ferranti.bikerbikus.data.UserData;
import ferranti.bikerbikus.models.TipoUtente;
import ferranti.bikerbikus.models.Utente;
import ferranti.bikerbikus.utils.Constants;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Alert.AlertType;

import java.sql.*;

public class LoginQuery {

    public static boolean execute(String email, String password) {
        try (Connection connection = DriverManager.getConnection(Constants.URL, Constants.USERNAME, Constants.PASSWORD);
             PreparedStatement preparedStatement = createStatement(connection, email, password);
             ResultSet resultSet = preparedStatement.executeQuery()) {
            while (resultSet.next()) {
                Utente utente = new Utente();
                utente.setId(resultSet.getInt(1));
                utente.setEmail(resultSet.getString(2));
                utente.setPassword(resultSet.getString(3));
                utente.setNome(resultSet.getString(4));
                utente.setCognome(resultSet.getString(5));
                utente.setTipoUtente(new TipoUtente(resultSet.getInt(6), resultSet.getString(7)));
                UserData.getInstance().setUser(utente);
                return true;
            }
        } catch (SQLException e) {
        	new Alert(AlertType.ERROR, e.getMessage(), ButtonType.OK).show();
        }
        return false;
    }

    private static PreparedStatement createStatement(Connection connection, String email, String password) throws SQLException {
        String sql = "SELECT u.*, tu.Nome FROM Utente u LEFT JOIN TipoUtente tu ON tu.Id = u.TipoUtente WHERE email = ? AND password = ?;";
        PreparedStatement ps = connection.prepareStatement(sql);
        ps.setString(1, email);
        ps.setString(2, password);
        return ps;
    }
}
