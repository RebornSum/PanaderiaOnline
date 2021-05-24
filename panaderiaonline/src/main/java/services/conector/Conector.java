package services.conector;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * 
 * @author Christian Pons Hernández
 *
 */

public class Conector {

	Properties prop = new Properties();
	
	public Conector() {
		try {
			prop.load(getClass().getClassLoader().getResourceAsStream("config.properties"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	
	/**
	 * recoge el driver MySQLConstants y crea la URL para poder generar la conexión.
	 * @return
	 */
	public Connection getMySQLConnection() {
		try {
			
			Class.forName(prop.getProperty(MySQLConstants.DRIVER));

			try {
			
				return DriverManager.getConnection(getURL());
		
			} catch (SQLException e) {
				e.printStackTrace();
			}

		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
			return null;
	}

	/**
	 * String URL sacados de los datos de MySQLConstants. 
	 * @return String con la url.
	 */
	private String getURL() {
		return new StringBuilder().append(prop.getProperty(MySQLConstants.URL_PREFIX))
		.append(prop.getProperty(MySQLConstants.URL_HOST)).append(":")
		.append(prop.getProperty(MySQLConstants.URL_PORT)).append("/")
		.append(prop.getProperty(MySQLConstants.URL_SCHEMA)).append("?user=")
		.append(prop.getProperty(MySQLConstants.USER)).append("&password=")
		.append(prop.getProperty(MySQLConstants.PASSWD)).append("&useSSL=")
		.append(prop.getProperty(MySQLConstants.URL_SSL)).toString();
		
		
		// --- jdbc:mysql://localhost:3306/world?user=yo&password=12345678&userSSL=false
		
		}
}
