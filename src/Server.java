import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;


public class Server extends Application {
    String response = null;

    DataOutputStream outputToClient = null;
    DataInputStream inputFromCLient = null;

    @Override
    public void start(Stage primaryStage) {
        TextArea textArea = new TextArea();

        // Create a scene and place it in the primaryStage
        Scene scene = new Scene(new ScrollPane(textArea), 450, 200);
        // Set the stage title
        primaryStage.setTitle("Server");
        // Place the scene in the stage
        primaryStage.setScene(scene);
        // Display the stage
        primaryStage.show();

        new Thread(() -> {
            try {
                // Create a server socket
                ServerSocket serverSocket = new ServerSocket(8000);
                Platform.runLater(() -> textArea.appendText("Server started at " + new Date() + "\n"));

                // Listen for a connection request
                Socket socket = serverSocket.accept();

                // Create input and output streams
                inputFromCLient = new DataInputStream(socket.getInputStream());
                outputToClient = new DataOutputStream(socket.getOutputStream());

                while(true) {
                    Boolean isPrime = true;
                    // Receive number from client
                    int numberFromClient = inputFromCLient.readInt();

                    // Check if number is prime
                    for(int i = 2; i < numberFromClient; i++) {
                        if(numberFromClient % i == 0) {
                            isPrime = false;
                            break;
                        }
                    } // End of for loop
                    if(isPrime) {
                        response = numberFromClient + " is a prime number." + "\n";
                    } else {
                        response = numberFromClient + " is not a prime number." + "\n";
                    }

                    // Send response back to the client
                    outputToClient.writeUTF(response);

                    Platform.runLater(() -> {
                        textArea.appendText(response);
                    });
                } // end of while
            } catch (IOException e) {
                e.printStackTrace();
            } // End of try
        }).start();
    }

    public static void main(String[] args) {
        launch(args);
    } // Ends of main method
} // End of Server class
