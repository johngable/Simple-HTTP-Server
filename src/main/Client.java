package main;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

/**
 * This class is intended to allow users to connect to a running HTTP server
 * located at localhost on port 8090.
 * 
 * Once connected, users are displayed a list of possible directories, and can
 * input what they choose. The server will process their GET request, and the
 * return will be displayed to the user.
 * 
 * @author John Gable
 *
 */
public class Client {

	public static void main(String[] args) throws IOException {

		Scanner serverInformation = new Scanner(System.in);
		
		System.out.print("Server IP Address: ");
		String hostName = serverInformation.nextLine(); // Localhost : 127.0.0.1
		
		System.out.print("Server Port Number: ");
		int portNumber = serverInformation.nextInt(); // Port : 8090
		
		System.out.println("Please enter the path you would like to access.");

		try (Socket clientSocket = new Socket(hostName, portNumber);
				PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
				BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));) {
			String fromServer; // Socket information from the server to the client
			Scanner stdIn = new Scanner(System.in); // Client/User input from console

			while ((fromServer = in.readLine()) != null) {

				System.out.println("Server: " + fromServer);

				// While the server in stream contains string, display to user/client
				while (in.ready()) {
					System.out.println(in.readLine());
				}

				if (stdIn.hasNext()) {
					String fromUser = stdIn.nextLine();
					fromUser = "GET " + fromUser + " HTTP/1.1"; // Build full GET request
					System.out.println("Client: " + fromUser);
					out.println(fromUser);
				}
			}
			stdIn.close();
		}

	}
}
