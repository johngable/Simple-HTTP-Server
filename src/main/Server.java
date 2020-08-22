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
	private final static String noFileFoundMessage = "404 File not found\r\n";
	private final static String forbiddenMessage = "403 Access forbidden\r\n";
	private final static String welcomeMessage = "Welcome To The HTTP Server!";

	public static void main(String[] args) throws IOException {

		System.out.println("Debug : Server Running");

		int portNumber = Integer.parseInt(args[0]);

		while (true) {
			try (ServerSocket serverSocket = new ServerSocket(portNumber);
					Socket clientSocket = serverSocket.accept();
					PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
					BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));) {

				String inputLine;
				System.out.println("Debug : Client Connected");

				if ((inputLine = in.readLine()) != null) {

					String path = cleanPath(inputLine);

					if (!path.equals("/")) {

						Boolean directoryAccess = checkForbidden(path);

						if (directoryAccess == true) {

							try {
								File file = new File("." + path);

								if (!file.exists() && !file.isFile()) {
									out.write(noFileFoundMessage);
									out.flush();
								} else {
									Scanner sc = new Scanner(file);

									while (sc.hasNext()) {
										out.println(sc.nextLine());
									}
								}
							} catch (Exception io) {
								out.write(noFileFoundMessage);
								out.flush();
							}

						} else {
							out.print(forbiddenMessage);
							out.flush();
						}

					} else {
						out.println(welcomeMessage);
						out.close();
						in.close();
						clientSocket.close();
					}

				}

			}
		}
	}

	public static Boolean checkForbidden(String path) {
		if (path.contains("ForbiddenFiles")) {
			return false;
		} else {
			return true;
		}
	}

	public static String cleanPath(String request) {
		String[] requestParam = request.split(" ");
		String pathClean = requestParam[1];
		return pathClean;
	}

}
