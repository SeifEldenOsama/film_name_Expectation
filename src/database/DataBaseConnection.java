package database;

import java.sql.Connection;
import java.sql.DriverManager;

public class DataBaseConnection {
	public static Connection connect() {
	Connection con = null;
	try {
		con = DriverManager.getConnection("jdbc:mysql://localhost/films?user=root&password=Seif2354296");
		System.out.println("Success");
		return con;
	}catch(Exception E) {
		System.out.println(E.getMessage());
		return con;
	}
}
}