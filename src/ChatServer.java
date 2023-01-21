import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Scanner;

public class ChatServer {

	public static void main(String[] args) throws InterruptedException {
		int portSelected = selectPort();
		startChatServer(portSelected);

	}

	public static int selectPort() {

		Scanner consoleInput = new Scanner(System.in);
		System.out.println("\n------------------- Starting CHAT SERVER Application ------------------- ");

		while (true) {
			System.out.println("\nWhich port do you want to connect to :");
			String portSelected = consoleInput.nextLine();

			try {
				int portNumber = Integer.parseInt(portSelected);
				return portNumber;
			} catch (NumberFormatException e) {
			}
		}

	}

	public static void startChatServer(int portSelected) throws InterruptedException {

		try (ServerSocket server = new ServerSocket(portSelected)) {
			System.out.println("\nWaiting For Connection On Port : " + portSelected);
			int connectionEstablished = 0;

			while (true) {

				try (Socket connection = server.accept()) {

					connectionEstablished++;
					if (connectionEstablished == 1) {
						System.out.println("\nFirst Connection Made On Port : " + portSelected);
					}

					else {

						if (connectionEstablished == 2) {
							System.out.println("\n-------------------- CHAT STARTED ---------------------\n");

						}

						// CALL THREAD
						Thread task = new OutputWritingThread(connection);
						task.start();

						// INPUT STREAM READER
						InputStream socketInput = connection.getInputStream();
						byte[] inputBytes = socketInput.readAllBytes();
						String inputString = new String(inputBytes, StandardCharsets.US_ASCII);
						if (!inputString.isEmpty()) {
							System.out.println("\nCHAT CLIENT: " + inputString);
						}

					}

					Thread.sleep(500);

				} catch (IOException ex) {

				}

			}

		} catch (IOException ex) {
		}

	}

	private static class OutputWritingThread extends Thread {

		private Socket connection;

		OutputWritingThread(Socket connection) {
			this.connection = connection;
		}

		@Override
		public void run() {

			try {

				Scanner inputString = new Scanner(System.in);
				String message = inputString.nextLine();
				Writer out = new OutputStreamWriter(connection.getOutputStream());

				if (message.equals("exit")) {
					System.out.println("\nYou have ended the chat session.");
					System.out.println("\nApplication closing...\n");
					out.write(message);
					out.flush();
					System.exit(0);
				}

				out.write(message);
				out.flush();
				Thread.sleep(500);

			} catch (IOException | InterruptedException ex) {
			}

			finally {

				try {
					connection.close();
				} catch (IOException e) {
				}

			}

		}

	}

}