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

	private TipiLezioneQuery() {
		throw new IllegalStateException("Utility class");
	}

	public static List<TipoLezione> execute() {
		String sql = "SELECT * FROM TipoLezione";
		List<TipoLezione> result = new ArrayList<>();
		try (Connection connection = DriverManager.getConnection(Constants.URL, Constants.USERNAME, Constants.PASSWORD);
				PreparedStatement preparedStatement = connection.prepareStatement(sql);
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
}
