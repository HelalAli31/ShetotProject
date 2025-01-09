package commonGui;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import server.BraudeLibServer;
import ocsf.server.ConnectionToClient;

public class ServerDisplayBooksController {
    private static Connection conn = BraudeLibServer.getConnection();

    /**
     * Fetches all books data from the database and sends it to the client.
     *
     * @param client The client connection.
     */
    public static void fetchBooksFromDB(ConnectionToClient client) {
        String query = "SELECT book_name, topic, description, copies, available_copies FROM db.Book";

        try (PreparedStatement pstmt = conn.prepareStatement(query);
             ResultSet rs = pstmt.executeQuery()) {

            StringBuilder booksData = new StringBuilder();
            booksData.append("Book List:\n");

            while (rs.next()) {
                String bookName = rs.getString("book_name");
                String topic = rs.getString("topic");
                String description = rs.getString("description");
                int copies = rs.getInt("copies");
                int availableCopies = rs.getInt("available_copies");

                booksData.append("Book Name: ").append(bookName)
                        .append(", Topic: ").append(topic)
                        .append(", Description: ").append(description)
                        .append(", Total Copies: ").append(copies)
                        .append(", Available Copies: ").append(availableCopies)
                        .append("\n");
            }

            if (booksData.length() > "Book List:\n".length()) {
                client.sendToClient(booksData.toString());
            } else {
                client.sendToClient("No books found in the database.");
            }
        } catch (SQLException e) {
            System.out.println("SQLException occurred while fetching books: " + e.getMessage());
            e.printStackTrace();
            try {
                client.sendToClient("Database error occurred while fetching books.");
            } catch (Exception sendError) {
                System.out.println("Error sending message to client: " + sendError.getMessage());
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}
