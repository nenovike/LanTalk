package com.nenovike.lanTalk;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.MulticastSocket;

import com.nenovike.lanTalk.servers.MulticastListenServer;
import com.nenovike.lanTalk.servers.UnicastListenServer;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Main window class.
 * 
 * @author Kondzio
 * @version 1.0
 */
public class MainWindow extends Application {
	/**
	 * Current user's name.
	 */
	String userName;
	/**
	 * Main stage object.
	 */
	Stage mainWindowStage;
	/**
	 * Main window controller object.
	 */
	MainWindowController mainWindowController;
	/**
	 * Multicast address to send "Hello packets" through.
	 */
	InetSocketAddress helloAddress;
	/**
	 * Local user address.
	 */
	InetSocketAddress userAddress;
	/**
	 * Multicast socket to send "Hello" packets through.
	 */
	MulticastSocket multicastSocket;
	/**
	 * Unicast socket to send messages through.
	 */
	DatagramSocket datagramSocket;
	/**
	 * Socket server receiving messages.
	 */
	UnicastListenServer listenServer;
	/**
	 * Socket server receiving "Hello" packets.
	 */
	MulticastListenServer helloListenServer;
	/**
	 * {@link #listenServer} {@link Thread} object.
	 */
	Thread listenServerThread;
	/**
	 * {@link #helloListenServer} {@link Thread} object.
	 */
	Thread helloListenServerThread;
	/**
	 * Default port for unicast communication.
	 */
	private int defaultPort = 1234;
	/**
	 * Debug mode.
	 */
	boolean debug = true;

	@Override
	public void start(Stage mainWindowStage) throws IOException, InterruptedException {
		initStage(mainWindowStage);

		prepareSockets();
		startServers();

		mainWindowController.changeName();
	}

	@Override
	public void stop() throws InterruptedException {
		listenServer.terminate();
		helloListenServer.terminate();
		multicastSocket.close();
		datagramSocket.close();
		listenServerThread.join();
		helloListenServerThread.join();
	}

	/**
	 * Create all address and socket objects.
	 * 
	 * @throws IOException
	 */
	private void prepareSockets() throws IOException {
		userName = "LanTalk";
		helloAddress = new InetSocketAddress("233.0.0.0", 1111);
		int port = debug ? mainWindowController.getNewPort() : defaultPort;
		InetAddress address = mainWindowController.getNewAddress();
		userAddress = new InetSocketAddress(address, port);
		multicastSocket = new MulticastSocket(1111);
		multicastSocket.joinGroup(helloAddress.getAddress());
		datagramSocket = new DatagramSocket(userAddress);
	}

	/**
	 * Initialize main stage. Create and show scene.
	 * 
	 * @param stage
	 *            main window stage
	 * @throws IOException
	 */
	private void initStage(Stage stage) throws IOException {
		mainWindowStage = stage;
		mainWindowStage.setOnCloseRequest(e -> {
			if (mainWindowController.conversationWindowStage != null)
				mainWindowController.conversationWindowStage.close();
		});
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("MainWindow.fxml"));
		Parent mainWindowParent = fxmlLoader.load();
		mainWindowController = (MainWindowController) fxmlLoader.getController();
		mainWindowController.mainWindow = this;
		mainWindowStage.setScene(new Scene(mainWindowParent));
		mainWindowStage.show();
	}

	/**
	 * Start listen servers and their threads.
	 */
	private void startServers() {
		listenServer = new UnicastListenServer(this, datagramSocket);
		listenServerThread = new Thread(listenServer);
		listenServerThread.start();
		helloListenServer = new MulticastListenServer(this, multicastSocket);
		helloListenServerThread = new Thread(helloListenServer);
		helloListenServerThread.start();
	}

	/**
	 * Get user address.
	 * 
	 * @return user address
	 */
	public InetSocketAddress getUserAddress() {
		return userAddress;
	}

	/**
	 * Get user name.
	 * 
	 * @return user name
	 */
	public String getUserName() {
		return userName;
	}

	/**
	 * Add contact to {@link #mainWindowController}'s list.
	 * 
	 * @param message
	 */
	public void addContactFromMessage(String message) {
		mainWindowController.addAddressToContactList(message);
	}

	/**
	 * Get main window controller.
	 * 
	 * @return controller
	 */
	public MainWindowController getMainWindowController() {
		return mainWindowController;
	}

	public static void main(String[] args) {
		MainWindow.launch(args);
	}
}
