package db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/*
 * Database Utility class containing methods 
 * for database interaction
 * 
 */
public class DBUtil {

	private Connection connect = null;//
	private Statement statement = null;
	private ResultSet resultSet = null;

	// Database Configuration

	// DB HOST ADDRESS
	private String DB_HOST = "localhost";
	// DB NAME
	private String DB_NAME = "hotel";
	// DB USERNAME
	private String DB_USERNAME = "student";
	// DB PASSWORD
	private String DB_PASSWORD = "student";

	// Default Constructor
	public DBUtil() {
		System.out.println("Initializing DB connection");
		try {
			Class.forName("com.mysql.jdbc.Driver");
			connect = DriverManager.getConnection("jdbc:mysql://" + DB_HOST	+ ":3306/" + DB_NAME, DB_USERNAME, DB_PASSWORD);
			System.out.println("DB connected successfully");
		} catch (Exception e) {
			try {
				throw e;
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
	}

	public boolean recordExists(String query) {
		boolean exists = false;
		try {
			statement = connect.createStatement();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		try {
			resultSet = statement.executeQuery(query);
			while (resultSet.next()) {
				exists = true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return exists;
	}

	public int runQuery(String query) {
		int status = 0;
		try {
			statement = connect.createStatement();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		try {
			status = statement.executeUpdate(query);
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return status;
	}

	public ResultSet runRSQuery(String query) {
		ResultSet resultSet = null;
		try {
			statement = connect.createStatement();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		try {
			resultSet = statement.executeQuery(query);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return resultSet;
	}

	public String getIDForUser(String username) {
		String userId = null;
		ResultSet resultSet = runRSQuery("select id from user where username = '"
				+ username + "'");
		try {
			while (resultSet.next()) {
				userId = resultSet.getString(1);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return userId;
	}

	public String getRoomTypes() {
		String roomTypes = "";
		ResultSet resultSet = runRSQuery("select RoomType from room_prices");
		try {
			while (resultSet.next()) {
				roomTypes += resultSet.getString(1) + ",";
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return roomTypes;
	}

	public String getPriceForRoomType(String roomType) {
		String price = null;
		ResultSet resultSet = runRSQuery("select Price from room_prices where RoomType = '"
				+ roomType + "'");
		try {
			while (resultSet.next()) {
				price = resultSet.getString(1);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return price;
	}

	public String getCurrency() {
		String currency = null;
		ResultSet resultSet = runRSQuery("select currency from admin_settings");
		try {
			while (resultSet.next()) {
				currency = resultSet.getString(1);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return currency;
	}
}
