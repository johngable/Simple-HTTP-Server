package main;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class Server {

	private final String forbiddenDirectory = "ForbiddenFiles";
	private final static String noFileFoundMessage = "404 File not found";
	private final static String forbiddenMessage = "403 Access forbidden";
	private final static String fileFoundMessage = "202 File Found";
	private final static String welcomeMessage = "Welcome To The HTTP Server!";
	private final static String[] filePaths = { "/Files/File1/", "/Files/File2/", "/ForbiddenFiles/ForbiddenFile1/" };

	public static void main(String[] args) throws IOException {

		System.out.println("Debug : Server Running");

		int portNumber = Integer.parseInt(args[0]);

		while (true) {
			try (ServerSocket serverSocket = new ServerSocket(portNumber);
					Socket clientSocket = serverSocket.accept();
					PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
					BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));) {

				String inputLine = "";
				System.out.println("Debug : Client Connected");
				out.println(welcomeMessage);

				while ((inputLine = in.readLine()) != null) {
					System.out.println("Debug: Input= " + inputLine);
					String path = "";

					path = cleanPath(inputLine);

					Boolean directoryAccess = checkForbidden(path);

					// If user isn't trying to access forbidden directory
					if (directoryAccess) {
						System.out.println("User has access");
						// Try to make a file object given path
						try {
							File file = new File(path);
							System.out.println(path);

							// If file exist read it from directory
							if (file.exists() && file.isFile()) {

								System.out.println("File exists here it comes");
								Scanner sc = new Scanner(file);

								out.println(fileFoundMessage);
								String fileContents = "";
								// Output the file to the user
								while (sc.hasNext()) {
									fileContents = fileContents + sc.nextLine() +"\n";
								}
								out.println(fileContents);
								out.flush();
								sc.close();
							} else {
								System.out.println("File not here");
								out.println(noFileFoundMessage);
								out.flush();
							}
						}
						// Path doesn't exist
						catch (Exception io) {
							System.out.println("Exception");
						}

					}
					// If path is forbidden from user access
					else {
						System.out.println("User forbidden access");
						out.println(forbiddenMessage);
						out.flush();
					}
					//out.close();
					//in.close();
				}
				//clientSocket.close();
			}
		}
	}

	public static Boolean checkForbidden(String path) {
		if (path.contains("Forbidden")) {
			return false;
		} else {
			return true;
		}
	}

	public static String cleanPath(String request) {
		System.out.println(request);
		String[] requestParam = request.split(" ");

		String pathClean = "." + requestParam[1];
		System.out.println(pathClean);
		return pathClean;
	}

}
