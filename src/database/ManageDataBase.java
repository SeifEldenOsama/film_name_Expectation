package database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import FilmGame.filmdata;

public class ManageDataBase {
    
    public static filmdata getFilmDataById(int id) {
        try (Connection con = DataBaseConnection.connect();
             PreparedStatement p = con.prepareStatement("SELECT * FROM film_actors WHERE id = ?")) {
            
            p.setInt(1, id);
            try (ResultSet rs = p.executeQuery()) {
                if (rs.next()) {
                    return new filmdata(rs.getString("film"), rs.getString("actor"));
                } else {
                    return null;
                }
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        }   
    }
    
    public static void saveLevel(int level) {
        try (Connection con = DataBaseConnection.connect()) {
            String sql = "UPDATE game_state SET current_level = ?";
            try (PreparedStatement stmt = con.prepareStatement(sql)) {
                stmt.setInt(1, level);
                int rows = stmt.executeUpdate();
                
                if (rows == 0) {
                    try (PreparedStatement insert = con.prepareStatement(
                            "INSERT INTO game_state (current_level) VALUES (?)")) {
                        insert.setInt(1, level);
                        insert.executeUpdate();
                    }
                }
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
    
    public static int getLevel() {
        String sql = "SELECT current_level FROM game_state LIMIT 1";
        try (Connection con = DataBaseConnection.connect();
             PreparedStatement stmt = con.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            if (rs.next()) {
                return rs.getInt("current_level");
            } else {
                return 1;
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return 1;
        }
    }
    
    public static int levelsCount() {
    	String sql = "SELECT COUNT(film) AS count FROM film_actors";
    	try (Connection con = DataBaseConnection.connect(); PreparedStatement p = con.prepareStatement(sql)){
    		p.execute();
    		ResultSet R = p.executeQuery();
    		R.next();
    		return R.getInt("count");
    	}catch (Exception E) {
    		System.out.println(E.getMessage());
    	}
    	return 0;
    }
    
    public static int getCoins() {
        String sql = "SELECT value FROM coins";
        try (Connection con = DataBaseConnection.connect();
             PreparedStatement p = con.prepareStatement(sql);
             ResultSet rs = p.executeQuery()) {

            if (rs.next()) {
                return rs.getInt("value");
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
        return 0;
    }
    
    public static void addCoins(int amount) {
        String sql = "UPDATE coins SET value = value + ?";
        try (Connection con = DataBaseConnection.connect();
             PreparedStatement p = con.prepareStatement(sql)) {

            p.setInt(1, amount);
            p.executeUpdate();
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    
}
