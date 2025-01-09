package commonGui;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import server.BraudeLibServer;
import ocsf.server.ConnectionToClient;

public class ServerLoginController {
    private static Connection conn = BraudeLibServer.getConnection();

    /**
     * Validates the username and password against the database.
     *
     * @param obj    The login object containing username and password (e.g., a User object).
     * @param client The client connection.
     * @throws Exception If an error occurs during database query or connection.
     */
    public static void loginFromDB(Object obj, ConnectionToClient client) throws Exception {
        // Check if the object is a ClientServerContact
        if (!(obj instanceof Shared_classes.ClientServerContact)) {
            client.sendToClient("Invalid data format");
            System.out.println("Invalid data format received.");
            return;
        }

        // Extract the User object from ClientServerContact
        Shared_classes.ClientServerContact contact = (Shared_classes.ClientServerContact) obj;
        Object data = contact.getObj1();

        // Ensure the extracted object is of type User
        if (!(data instanceof Shared_classes.User)) {
            client.sendToClient("Invalid data format");
            System.out.println("Invalid data format inside ClientServerContact.");
            return;
        }

        Shared_classes.User user = (Shared_classes.User) data;
        String username = user.getUsername().trim();
        String password = user.getPassword().trim();

        System.out.println("Username: " + username);
        System.out.println("Password: " + password);

        // SQL query to validate the username and password and retrieve user type
        String query = "SELECT type FROM db.user WHERE username = ? AND password = ?";

        try (PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, username);
            pstmt.setString(2, password);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    String userType = rs.getString("type");
                    System.out.println("User type: " + userType);
                    /////////////////// MAKE THE NAVIGATION
                    client.sendToClient("Login successful. User type: " + userType);
                } else {
                    client.sendToClient("Invalid username or password");
                }
            }
        } catch (SQLException e) {
            System.out.println("SQLException occurred: " + e.getMessage());
            e.printStackTrace();
            client.sendToClient("Database error occurred during login");
        }
    }
}
