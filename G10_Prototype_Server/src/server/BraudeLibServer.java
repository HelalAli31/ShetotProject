package server;
import java.io.*;
import java.net.InetAddress;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;
import ocsf.server.ConnectionToClient;
import ocsf.server.AbstractServer;
import serverGui.ServerDisplayController;
import Shared_classes.Subscriber;
import Shared_classes.User;
import commonGui.ServerDisplayBooksController;
import commonGui.ServerLoginController;
import Shared_classes.ClientServerContact;
import Shared_classes.Command;
import serverGui.SubscriberController;
public class BraudeLibServer extends AbstractServer {

    // Default port for the server
    final public static int DEFAULT_PORT = 5555;
    private static final String DB_URL = "jdbc:mysql://localhost:3306/";  // URL without database name
    private BiConsumer<String, String> clientDetailsCallback;
    private Runnable clientDisconnectionCallback;
    // Database connection instance
    private static Connection conn = null;
    private static String dbUser;
    private static String dbPassword;
    private static String fullDBUrl;
    private SubscriberController SubscriberController;
    
    // Constructor
    public BraudeLibServer(int port) {
        super(port);
    }
    public BraudeLibServer(int port, String dbName, String dbUser, String dbPassword) {
        super(port);
        BraudeLibServer.dbUser = dbUser;
        BraudeLibServer.dbPassword = dbPassword;
        BraudeLibServer.fullDBUrl = DB_URL + dbName + "?serverTimezone=IST";
        try {
        	conn = getConnection();
            this.SubscriberController = new SubscriberController();
        } catch (Exception e) {
            System.out.println("Database connection failed: " + e.getMessage());
        }
    }
    //Implemented By Singletone way
    public static Connection getConnection() {
        if (conn == null) {
            try {
                // Update the connection string, username, and password to match your database configuration
            	String url = "jdbc:mysql://localhost:3306/db?serverTimezone=Asia/Jerusalem";
                String user = "root";
                String password = "Aa123456";
                
                conn = DriverManager.getConnection(url, user, password);
                System.out.println("Database connection established");
            } catch (SQLException e) {
                e.printStackTrace();
                System.out.println("Failed to establish database connection");
            }
        }
        return conn;
    }
    
    public void setClientDetailsCallback(BiConsumer<String, String> callback) {
        this.clientDetailsCallback = callback;
    }
    
    public void setClientDisconnectionCallback(Runnable callback) {
	    this.clientDisconnectionCallback = callback;
	}

    private void sendConnectivityDetailsToClient(ConnectionToClient client) {
	    try {
	      String clientInfo = "IP Address: " + client.getInetAddress().getHostAddress() +
                          ", Host Name: " + client.getInetAddress().getHostName();
	      client.sendToClient(clientInfo);
	    } catch (IOException e) {
	      System.out.println("Error sending connectivity details: " + e.getMessage());
	    }
	}
    @Override
	protected void clientDisconnected(ConnectionToClient client) {
    	ServerDisplayController.getInstance().updateClientDisconnectionDetails(client);
	    super.clientDisconnected(client);
	    if (clientDisconnectionCallback != null) {
	        clientDisconnectionCallback.run();
	    }
    }
    
    @Override
    protected void clientConnected(ConnectionToClient client) {
        super.clientConnected(client);
        String clientHostName = client.getInetAddress().getHostName(); // Get the client's host name
        String clientIpAddress = client.getInetAddress().getHostAddress();// Get the client's IP address
        if (clientDetailsCallback != null) 
            clientDetailsCallback.accept(clientHostName, clientIpAddress); // Invoke the callback with the client details
    }




    private static String readFromDB() throws Exception {
    	System.out.println("R");
        String query= "SELECT * FROM db.subscriber";
        String subscriber_name,subscriber_phone_number,subscriber_email;
        Integer subscriber_id,detailed_subscription_history;
        StringBuilder str = new StringBuilder();
        try (PreparedStatement pstmt = conn.prepareStatement(query)) {
        	System.out.println("B exec");
            ResultSet rs = pstmt.executeQuery();
	    	while(rs.next()) {
	    		subscriber_id = rs.getInt("subscriber_id");
	            subscriber_name = rs.getString("subscriber_name");
	            detailed_subscription_history = rs.getInt("detailed_subscription_history");
	            subscriber_phone_number = rs.getString("subscriber_phone_number");
	            subscriber_email = rs.getString("subscriber_email");
	            str.append("ID: ").append(subscriber_id).append(", Name: ").append(subscriber_name).append(", Subscription: ").append
	            (detailed_subscription_history).append(", Tel: ").append(subscriber_phone_number).append(", E-mail: ").append(subscriber_email).append("\n");                
	    	}
	    	System.out.println("READ:"+str.toString());
            return str.toString();
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
    }
    
    
    @Override
    protected void handleMessageFromClient(Object msg, ConnectionToClient client) {
        try {
            System.out.println("Received message: " + msg);
            System.out.println("Message class: " + msg.getClass().getName());

            // Ensure the incoming message is of the correct type
            if (msg instanceof ClientServerContact) {
                ClientServerContact csc = (ClientServerContact) msg;

                // Assuming csc.getCmd() returns a String
                String cmd = csc.getCmd().toString();
                System.out.println("Command: " + cmd);

                switch (cmd) {
                    case "Login":
                        // Logic for the Login command
                         ServerLoginController.loginFromDB(msg, client);
                        System.out.println("Handling Login command");
                        break;

                    case "DisplaySubscribers":
                        client.sendToClient(readFromDB());
                        break;

                    case "DisplayBooks":
                    	ServerDisplayBooksController.fetchBooksFromDB(client);
                        break;
                    case "AddSubscriber":
                        try {
                            // Log the incoming message
                            System.out.println("Processing AddSubscriber command: " + msg);

                            // Cast the message to ClientServerContact
                            ClientServerContact addSubMsg = (ClientServerContact) msg;

                            // Use reflection or knowledge of the internal structure
                            Object userObj = addSubMsg.getObj1(); // Assuming getObj1() holds User
                            Object subscriberObj = addSubMsg.getObj2(); // Assuming getObj2() holds Subscriber

                            // Validate and cast the objects
                            if (userObj instanceof User && subscriberObj instanceof Subscriber) {
                                User user = (User) userObj;
                                Subscriber subscriber = (Subscriber) subscriberObj;

                                // Call the method to add subscriber to DB
                                SubscriberController subscriberController = new SubscriberController();
                                subscriberController.addSubscriberToDB(user, subscriber, client);

                                System.out.println("AddSubscriber command handled successfully.");
                            } else {
                                client.sendToClient("Invalid data: Expected User and Subscriber objects.");
                                System.err.println("AddSubscriber: Objects are not of expected types.");
                            }
                        } catch (Exception e) {
                            System.err.println("Error handling AddSubscriber: " + e.getMessage());
                            e.printStackTrace();
                            client.sendToClient("Failed to process AddSubscriber request.");
                        }
                        break;

                    default:
                        System.out.println("Unknown command: " + cmd);
                        client.sendToClient("Unknown command: " + cmd);
                        break;
                }
            } else {
                System.out.println("Unrecognized message type: " + msg.getClass().getName());
                client.sendToClient("Unrecognized message type: " + msg.getClass().getName());
            }
        } catch (Exception e) {
            System.err.println("Exception occurred: " + e.getMessage());
            e.printStackTrace();

            try {
                client.sendToClient("An error occurred while processing your request.");
            } catch (Exception sendError) {
                System.err.println("Failed to send error message to client: " + sendError.getMessage());
            }
        }
    }


    public static void main(String[] args) {
        int port = DEFAULT_PORT;
        try {
            port = Integer.parseInt(args[0]);
        } catch (Exception ignored) {}

        BraudeLibServer server = new BraudeLibServer(port);
        try {
            server.listen();
        } catch (Exception e) {
            System.out.println("ERROR - Could not listen for clients!");
        }
    }
}