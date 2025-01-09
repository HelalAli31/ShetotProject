package serverGui;
import javafx.scene.paint.Color;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import ocsf.server.ConnectionToClient;
import javafx.scene.Node;
import server.BraudeLibServer;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import Shared_classes.ClientServerContact;
import Shared_classes.Subscriber;
import Shared_classes.User;
public class SubscriberController {

    private static Connection conn = BraudeLibServer.getConnection();

    /**
     * Add a new user and subscriber to the database.
     */
    public void addSubscriberToDB(Object userObj, Object subscriberObj, ConnectionToClient client) throws Exception {
        // Check if the incoming objects are of type User and Subscriber
        if (userObj instanceof User && subscriberObj instanceof Subscriber) {
            User user = (User) userObj;
            Subscriber subscriber = (Subscriber) subscriberObj;

            try {
                // Begin transaction
                conn.setAutoCommit(false);

                // Insert into user table
                String userQuery = "INSERT INTO db.user (id, username, password, first_name, last_name, phone_number, email, type) " +
                                   "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
                try (PreparedStatement userStmt = conn.prepareStatement(userQuery)) {
                    userStmt.setInt(1, user.getId());
                    userStmt.setString(2, user.getUsername());
                    userStmt.setString(3, user.getPassword());
                    userStmt.setString(4, user.getFirstName());
                    userStmt.setString(5, user.getLastName());
                    userStmt.setString(6, user.getPhone());
                    userStmt.setString(7, user.getEmail());
                    userStmt.setString(8, user.getType().toString());
                    userStmt.executeUpdate();
                }

                // Insert into subscriber table
                String subscriberQuery = "INSERT INTO db.subscriber (id, status, detailed_subscription_history, late_returns) " +
                                         "VALUES (?, ?, ?, ?)";
                try (PreparedStatement subscriberStmt = conn.prepareStatement(subscriberQuery)) {
                    subscriberStmt.setInt(1, subscriber.getSubscriberId());
                    subscriberStmt.setString(2, subscriber.getStatus());
                    subscriberStmt.setInt(3, subscriber.getDetailedSubscriptionHistory());
                    subscriberStmt.setInt(4, subscriber.getLateReturns());
                    subscriberStmt.executeUpdate();
                }

                // Commit transaction
                conn.commit();
                client.sendToClient("Successfully Inserted");
            } catch (SQLException e) {
                conn.rollback(); // Rollback transaction on failure
                e.printStackTrace();
                client.sendToClient("Failed to insert subscriber: " + e.getMessage());
            } finally {
                conn.setAutoCommit(true); // Restore auto-commit mode
            }
        } else {
            client.sendToClient("Invalid data: Expected User and Subscriber objects.");
        }
    }


}