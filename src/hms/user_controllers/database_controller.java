package hms.user_controllers;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class database_controller {

    // Database connection details
    private static final String DB_URL = "jdbc:mysql://localhost:3306/userdb";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "*Norsyahm1n*"; // Consider externalizing credentials

    public Connection getDatabaseConnection() throws SQLException {
        try {
            // Load the MySQL JDBC driver. Essential for establishing the connection.
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            // Handle case where the driver class isn't found in the classpath.
            System.err.println("MySQL JDBC Driver not found!");
            e.printStackTrace();
            // Re-throw as SQLException to indicate a connection-related failure.
            throw new SQLException("MySQL JDBC Driver not found!", e);
        }
        // Attempt to connect to the database.
        return DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
    }

    public void closeDatabaseConnection(Connection conn) {
        if (conn != null) {
            try {
                // Release database resources.
                conn.close();
            } catch (SQLException e) {
                // Log error during closing, but don't disrupt application flow.
                System.err.println("Error closing database connection: " + e.getMessage());
                e.printStackTrace(); // Print stack trace for debugging
            }
        }
    }

    public String getUserName(int userId) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        String userName = null;
        String query = "SELECT name FROM users WHERE id = ?";

        try {
            conn = getDatabaseConnection();
            pstmt = conn.prepareStatement(query);
            pstmt.setInt(1, userId);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                userName = rs.getString("name");
            }
        } catch (SQLException e) {
            System.err.println("Error fetching username for ID " + userId + ": " + e.getMessage());
            e.printStackTrace();
        } finally {
            // Use a helper method for cleaner resource closing
            closeResultSet(rs);
            closePreparedStatement(pstmt);
            closeDatabaseConnection(conn);
        }
        return userName;
    }

    public boolean saveUserRoomSelection(int userId, String roomType) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        // Consider using room_type_id if you have a separate room_types table
        String sql = "INSERT INTO user_rooms (user_id, room_type) VALUES (?, ?)";
        int rowsAffected = 0;

        try {
            conn = getDatabaseConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, userId);
            pstmt.setString(2, roomType);
            rowsAffected = pstmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error saving room selection for user " + userId + ": " + e.getMessage());
            e.printStackTrace();
            return false; // Indicate failure on exception
        } finally {
            closePreparedStatement(pstmt);
            closeDatabaseConnection(conn);
        }
        // Return true if one or more rows were inserted
        return rowsAffected > 0;
    }

    public boolean hasUserSelectedRoom(int userId) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        boolean hasSelected = false;
        String query = "SELECT COUNT(*) FROM user_rooms WHERE user_id = ?";

        try {
            conn = getDatabaseConnection();
            pstmt = conn.prepareStatement(query);
            pstmt.setInt(1, userId);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                // Check if the count is greater than 0
                hasSelected = rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            System.err.println("Error checking room selection for user " + userId + ": " + e.getMessage());
            e.printStackTrace();
            // Return false on error to prevent proceeding incorrectly
            return false;
        } finally {
            closeResultSet(rs);
            closePreparedStatement(pstmt);
            closeDatabaseConnection(conn);
        }
        return hasSelected;
    }

    public boolean saveServiceRequest(int userId, String serviceType) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        // Example SQL - Adjust table and column names (e.g., request_status,
        // request_time)
        String sql = "INSERT INTO service_requests (user_id, service_type, request_time, status) VALUES (?, ?, NOW(), 'Pending')";
        int rowsAffected = 0;

        try {
            conn = getDatabaseConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, userId);
            pstmt.setString(2, serviceType);
            rowsAffected = pstmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error saving service request for user " + userId + ": " + e.getMessage());
            e.printStackTrace();
            return false;
        } finally {
            closePreparedStatement(pstmt);
            closeDatabaseConnection(conn);
        }
        return rowsAffected > 0;
    }

    public boolean saveAmenityRequest(int userId, String amenityType) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        // Example SQL - Adjust table and column names as needed
        // You might want columns like request_time, status, quantity, etc.
        String sql = "INSERT INTO amenity_requests (user_id, amenity_type, request_time, status) VALUES (?, ?, NOW(), 'Pending')";
        int rowsAffected = 0;

        try {
            conn = getDatabaseConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, userId);
            pstmt.setString(2, amenityType);
            rowsAffected = pstmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error saving amenity request for user " + userId + ": " + e.getMessage());
            e.printStackTrace();
            return false;
        } finally {
            closePreparedStatement(pstmt); // Use helper method if available
            closeDatabaseConnection(conn);
        }
        return rowsAffected > 0;
    }

    private void closePreparedStatement(PreparedStatement pstmt) {
        if (pstmt != null) {
            try {
                pstmt.close();
            } catch (SQLException e) {
                System.err.println("Error closing PreparedStatement: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    private void closeResultSet(ResultSet rs) {
        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException e) {
                System.err.println("Error closing ResultSet: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }
}
