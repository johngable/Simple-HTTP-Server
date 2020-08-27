package main;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

/**
 * This class is meant to be run as an HTTP server using a terminal. In order to
 * run, users must specify the port number (8090) for the server to be run on.
 * 
 * The accessible directories are hardcoded into "filePaths". This program will
 * process all user GET request, and either display the .txt to the user in raw
 * form, or will return an associated code (403, 404).
 * 
 * NOTE: NO multiuser functionality...
 * 
 * @author john
 *
 */
public class Server {

	private final String forbiddenDirectory = "ForbiddenFiles";
	private final static String noFileFoundMessage = "404 File Not Found\r\n";
	private final static String forbiddenMessage = "403 Access Forbidden\r\n";
	private final static String fileFoundMessage = "200 HTTP/1.1\r\n";
	private final static String welcomeMessage = "Welcome To The HTTP Server!\r";
	private final static String[] filePaths = { "/Files/File1/\r", "/Files/File2/\r",
			"/ForbiddenFiles/ForbiddenFile1/\r\n" };

	public static void main(String[] args) throws IOException {

		System.out.println("Debug : Server Running");

		int portNumber = Integer.parseInt(args[0]); //Port : 8090

		while (true) {
			try (ServerSocket serverSocket = new ServerSocket(portNumber);
					Socket clientSocket = serverSocket.accept();
					PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
					BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));) {

				String inputLine = "";
				System.out.println("Debug : Client Connected");

				// Display welcome message to client once connectivity established
				out.println(welcomeMessage);
				for (String s : filePaths) {
					out.println(s);
				}

				//Observe user input
				while ((inputLine = in.readLine()) != null) {
					String path;

					//Clean the entered user input/process GET header
					path = cleanPath(inputLine);

					//Ensure user is not trying to access forbidden directory
					Boolean directoryAccess = checkForbidden(path);

					// If user isn't trying to access forbidden directory
					if (directoryAccess) {
						// Try to make a file object given path
						try {
							File file = new File(path);
							System.out.println(path);

							// If file exist read it from directory
							if (file.exists() && file.isFile()) {

								Scanner sc = new Scanner(file);

								String fileContents = "";

								// Output the file to the user
								while (sc.hasNext()) {
									fileContents += sc.nextLine() + "\n";
								}
								out.println(fileFoundMessage + fileContents);
								sc.close();

							} // Path doesn't exist
							else {
								out.println(noFileFoundMessage);
								out.flush();
							}
						}
						// Exception in file creation
						catch (Exception io) {
							System.out.println("Exception");
						}

					}
					// If path is forbidden from user access
					else {
						out.println(forbiddenMessage);
						out.flush();
					}
					// out.close();
					// in.close();
				}
				// clientSocket.close();
			}
		}
	}

	// Checks for forbidden directory in the entered client string
	public static Boolean checkForbidden(String path) {
		if (path.contains("Forbidden")) {
			return false;
		} else {
			return true;
		}
	}

	// Processes the GET request to retrieve the path
	public static String cleanPath(String request) {
		System.out.println(request);
		String[] requestParam = request.split(" ");

		String pathClean = "." + requestParam[1];
		System.out.println(pathClean);
		return pathClean;
	}

}
