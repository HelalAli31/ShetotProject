package server;

import javafx.application.Application;
import javafx.stage.Stage;
import java.io.IOException;
import serverGui.ServerDisplayController;

public class ServerUI extends Application {
	final public static int DEFAULT_PORT = 5555;
    private static BraudeLibServer server;

	public static void main( String args[] ) throws Exception
	   {   
		 launch(args);
	  } // end main
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		// TODO Auto-generated method stub	
		ServerDisplayController aFrame = new ServerDisplayController(); // create ServerFrame
		 
		aFrame.start(primaryStage);
	}
	
    public static void runServer(int port, BraudeLibServer serverInstance) {
        if (server != null && server.isListening()) {
            System.out.println("Server is already running");
            return;
        }

        server = serverInstance;

        try {
        	server.listen(); 
        	// Start listening for connections
           // System.out.println("Server listening for connections on port " + port);
        } catch (Exception ex) {
            System.out.println("ERROR - Could not listen for clients!");
        }
    }

    public static void stopServer() {
        if (server != null && server.isListening()) {
            try {
                server.close();
              //  System.out.println("Server has stopped listening for connections.");
            } catch (IOException e) {
                System.out.println("Error closing the server: " + e.getMessage());
            }
        }
    }
	

}