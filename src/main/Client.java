package main;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class Client {

	public static void main(String[] args) throws IOException {

		String hostName = args[0];
		int portNumber = Integer.parseInt(args[1]);

		try (Socket kkSocket = new Socket(hostName, portNumber);
				PrintWriter out = new PrintWriter(kkSocket.getOutputStream(), true);
				BufferedReader in = new BufferedReader(new InputStreamReader(kkSocket.getInputStream()));) {
			String fromServer = "";
			String fromUser = "";
			Scanner stdIn = new Scanner(System.in);

			while ((fromServer = in.readLine()) != null) {
				System.out.println(fromServer);
				fromUser = stdIn.nextLine();

				out.println(fromUser);

			}

		}
	}
}
