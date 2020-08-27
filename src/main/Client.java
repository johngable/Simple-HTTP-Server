package main;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class Client {

	public static void main(String[] args) throws IOException {

		String hostName = "127.0.0.1"; // args[0];
		int portNumber = 8090; // Integer.parseInt(args[1]);

		try (Socket clientSocket = new Socket(hostName, portNumber);
				PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
				BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));) {
			String fromServer = "";
			String fromUser = "";
			
			while ((fromServer = in.readLine()) != null) {
				Scanner stdIn = new Scanner(System.in);
				
				while(in.ready()) {
					System.out.println(in.readLine());
				}
				
				//System.out.println("Server: " + fromServer);

				
				fromUser = stdIn.nextLine();
				
				if (fromUser != null && !fromUser.equals(" ") && !fromUser.contentEquals("")) {
					fromUser = "GET " + fromUser + " HTTP/1.1";
					System.out.println("Client: " + fromUser);
					out.println(fromUser);
					fromUser = "";
				}
			}

		}

		// System.out.println("You're connected");

	}
}
