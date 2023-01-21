import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Scanner;
import java.io.*;

public class ChatClient {

	public static void main(String[] args) {

		if (requestScan()) {

			String hostSelected = scanPorts();
			int connectionSelected = requestToConnect();

			if (connectionSelected == -1) {
				System.out.println("\nPermission to connect has not been given.");
				System.out.println("\nApplication closing...");
				System.exit(0);
			} else {
				System.out.println("\n---------------- CHAT STARTED ------------------\n");
				Thread task1 = new OutputWritingThread(hostSelected, connectionSelected);
				task1.start();
				connectToSocket(hostSelected, connectionSelected);
			}

		} else {
			System.out.println("\nPermission to scan ports has not been given.");
			System.out.println("\nApplication closing...");
			System.exit(0);
		}
	}

	public static boolean requestScan() {
		Scanner consoleInput = new Scanner(System.in);
		System.out.println("\n------------------- Starting CHAT CLIENT Application ------------------- ");
		System.out.println("\nDo you want to begin scanning ports (Y/N)");

		while (true) {

			String optionSelected = consoleInput.nextLine();

			if (optionSelected.equals("Y")) {
				return true;
			}
			if (optionSelected.equals("N")) {
				return false;
			}
		}
	}

	public static String scanPorts() {

		System.out.println("\nSelect a hostname :");
		Scanner consoleInput = new Scanner(System.in);
		String host = consoleInput.nextLine();
		System.out.println("\nScanning Ports on host : " + host + "\n");

		for (int currentPort = 1; currentPort < 1024; currentPort++) {
			try (Socket currentSocket = new Socket()) {
				currentSocket.connect(new InetSocketAddress(host, currentPort), 10);
				System.out.println("Available Port Found : " + currentPort);
			} catch (UnknownHostException ex) {
				System.err.println(ex);
				break;
			} catch (IOException ex) {
			}
		}
		System.out.println("\nScan Complete.");
		return host;

	}

	public static void connectToSocket(String host, int chosenPort) {

		while (true) {
			try (Socket chosenSocket = new Socket(host, chosenPort)) {
				chosenSocket.setSoTimeout(15000);


				// INPUT READER
				InputStream socketInput = chosenSocket.getInputStream();
				byte[] inputBytes = socketInput.readAllBytes();
				String inputString = new String(inputBytes, StandardCharsets.US_ASCII);

				if (!inputString.isEmpty()) {
					System.out.println("\nCHAT SERVER: " + inputString);
				}
				Thread.sleep(500);

			} catch (UnknownHostException e) {

			} catch (IOException e) {

			} catch (InterruptedException e) {

			}

		}

	}

	public static int requestToConnect() {
		Scanner consoleInput = new Scanner(System.in);
		System.out.println("\nDo you want to connect to an available port (Y/N)");

		while (true) {

			String optionSelected = consoleInput.nextLine();

			if (optionSelected.equals("Y")) {

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
			if (optionSelected.equals("N")) {
				return -1;
			}
		}
	}

	private static class OutputWritingThread extends Thread {

		private String hostSelected;
		private int connectionSelected;

		OutputWritingThread(String hostSelected, int connectionSelected) {
			this.hostSelected = hostSelected;
			this.connectionSelected = connectionSelected;
		}

		@Override
		public void run() {

			String host = this.hostSelected;
			int chosenPort = this.connectionSelected;

			while (true) {
				try (Socket chosenSocket = new Socket(host, chosenPort)) {
					chosenSocket.setSoTimeout(15000);

					Thread.sleep(500);

					// OUTPUT STREAM WRITER
					Scanner inputString = new Scanner(System.in);

					Writer output = new OutputStreamWriter(chosenSocket.getOutputStream());
					String message = inputString.nextLine();

					if (message.equals("exit")) {
						System.out.println("\nYou have ended the chat session.");
						System.out.println("\nApplication closing...\n");
						output.write(message);
						output.flush();
						System.exit(0);
					}

					output.write(message);
					output.flush();

				} catch (UnknownHostException e) {

				} catch (IOException e) {

				} catch (InterruptedException e) {

				}

			}

		}

	}

}