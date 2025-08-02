package com.airline.database;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.UUID;

public class DatabaseManager {
    private static final String DB_URL = "jdbc:postgresql://localhost:5432/flight_ticket";
    private static final String DB_USERNAME = "postgres";
    private static final String DB_PASSWORD = "Ini123@@@";
    
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
    }
    
    public static void initializeTables() {
        try (Connection conn = getConnection()) {
            String createUsersTable = """
                CREATE TABLE IF NOT EXISTS users (
                    id SERIAL PRIMARY KEY,
                    email VARCHAR(255) UNIQUE NOT NULL,
                    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                    last_login TIMESTAMP
                )
            """;
            
            String createSessionsTable = """
                CREATE TABLE IF NOT EXISTS user_sessions (
                    session_id VARCHAR(255) PRIMARY KEY,
                    user_id INT NOT NULL,
                    email VARCHAR(255) NOT NULL,
                    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                    expires_at TIMESTAMP NOT NULL,
                    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
                )
            """;
            
            Statement stmt = conn.createStatement();
            stmt.execute(createUsersTable);
            stmt.execute(createSessionsTable);
            
            System.out.println("Database tables created successfully");
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public static User getOrCreateUser(String email) {
        try (Connection conn = getConnection()) {
            String selectQuery = "SELECT * FROM users WHERE email = ?";
            PreparedStatement selectStmt = conn.prepareStatement(selectQuery);
            selectStmt.setString(1, email.toLowerCase());
            ResultSet rs = selectStmt.executeQuery();
            
            if (rs.next()) {
                String updateQuery = "UPDATE users SET last_login = CURRENT_TIMESTAMP WHERE id = ?";
                PreparedStatement updateStmt = conn.prepareStatement(updateQuery);
                updateStmt.setInt(1, rs.getInt("id"));
                updateStmt.executeUpdate();
                
                return new User(rs.getInt("id"), rs.getString("email"));
            } else {
                String insertQuery = "INSERT INTO users (email) VALUES (?) RETURNING id";
                PreparedStatement insertStmt = conn.prepareStatement(insertQuery);
                insertStmt.setString(1, email.toLowerCase());
                ResultSet generatedKeys = insertStmt.executeQuery();
                
                if (generatedKeys.next()) {
                    return new User(generatedKeys.getInt(1), email);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    public static UserSession createSession(User user) {
        try (Connection conn = getConnection()) {
            String sessionId = UUID.randomUUID().toString();
            LocalDateTime expiresAt = LocalDateTime.now().plusDays(30);
            
            String insertQuery = "INSERT INTO user_sessions (session_id, user_id, email, expires_at) VALUES (?, ?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(insertQuery);
            stmt.setString(1, sessionId);
            stmt.setInt(2, user.getId());
            stmt.setString(3, user.getEmail());
            stmt.setTimestamp(4, Timestamp.valueOf(expiresAt));
            
            if (stmt.executeUpdate() > 0) {
                return new UserSession(sessionId, user, expiresAt);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    public static void logoutSession(String sessionId) {
        try (Connection conn = getConnection()) {
            String query = "DELETE FROM user_sessions WHERE session_id = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, sessionId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
