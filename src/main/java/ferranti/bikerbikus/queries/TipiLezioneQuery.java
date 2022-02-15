package ferranti.bikerbikus.queries;

import ferranti.bikerbikus.models.TipoLezione;
import ferranti.bikerbikus.utils.Constants;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Alert.AlertType;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TipiLezioneQuery {
    public static List<TipoLezione> execute() {
        List<TipoLezione> result = new ArrayList<>();
        try (Connection connection = DriverManager.getConnection(Constants.URL, Constants.USERNAME, Constants.PASSWORD);
             PreparedStatement preparedStatement = createStatement(connection);
             ResultSet resultSet = preparedStatement.executeQuery()) {
            while (resultSet.next()) {
            	TipoLezione tipoLezione = new TipoLezione();
            	tipoLezione.setId(resultSet.getInt(1));
            	tipoLezione.setNome(resultSet.getString(2));
            	result.add(tipoLezione);
            }
        } catch (SQLException e) {
        	new Alert(AlertType.ERROR, e.getMessage(), ButtonType.OK).show();
        }
        return result;
    }

    private static PreparedStatement createStatement(Connection connection) throws SQLException {
        String sql = "SELECT * FROM TipoLezione";
        PreparedStatement ps = connection.prepareStatement(sql);
        return ps;
    }
}
