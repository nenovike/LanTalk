package com.nenovike.lanTalk;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.MulticastSocket;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Optional;
import java.util.ResourceBundle;

import com.nenovike.lanTalk.util.ContactButton;
import com.nenovike.lanTalk.util.MessagePacket;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 * MainWindow controller class.
 * 
 * @author Nenovike
 * @version 1.0
 * 
 */

public class MainWindowController implements Initializable {

	/**
	 * Reference to {@link MainWindow} object.
	 */
	public MainWindow mainWindow = null;
	/**
	 * {@link HashMap} of {@link ContactButton} to render on main window.
	 */
	private HashMap<String, ContactButton> contactButtons = new HashMap<String, ContactButton>();
	/**
	 * {@link HashMap} of conversation texts.
	 */
	private HashMap<String, String> conversationTexts = new HashMap<String, String>();
	/**
	 * {@link HashMap} of flags telling if user has any unread messages from a given
	 * address.
	 */
	private HashMap<String, Boolean> hasUnreadMessages = new HashMap<String, Boolean>();
	/**
	 * Reference to {@link ConversationWindowController} object.
	 */
	ConversationWindowController conversationWindowController = null;
	/**
	 * Reference to conversation window {@link Stage}.
	 */
	Stage conversationWindowStage;
	/**
	 * Default button style.
	 */
	private final String defBtnColor = new Button().getStyle();

	/**
	 * "Change name" menu item.
	 */
	@FXML
	private MenuItem menuChangeName;
	
	/**
	 * Contact list.
	 */
	@FXML
	private ScrollPane contactList;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		Timeline timeline = new Timeline(new KeyFrame(Duration.millis(15000), ae -> {
			try {
				resyncAddresses();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}));
		timeline.setCycleCount(Animation.INDEFINITE);
		timeline.play();
	}

	@FXML
	private void handleMenuAction(ActionEvent action) throws IOException {
		if (action.getSource() == menuChangeName) {
			changeName();
		}
	}

	/**
	 * "Change name" action. Shows a dialog until correct name is entered.
	 * Resynchronizes addresses.
	 * 
	 * @throws IOException
	 * @see #resyncAddresses()
	 */
	public void changeName() throws IOException {
		TextInputDialog nameDialog = new TextInputDialog("LanTalk");
		nameDialog.setTitle("LanTalk");
		nameDialog.setHeaderText("What is your name?");
		nameDialog.setContentText("Enter it here: ");
		Optional<String> result;
		do {
			result = nameDialog.showAndWait();
		} while (!result.isPresent() || result.get().length() > 10);
		mainWindow.userName = result.get();
		mainWindow.mainWindowStage.setTitle(mainWindow.userName + mainWindow.userAddress);
		resyncAddresses();
	}

	/**
	 * Removes all unused buttons (that do not have any unread messages) then sends
	 * "Hello" packet.
	 * 
	 * @throws IOException
	 * @see #sendMulticastHelloPacket(InetSocketAddress)
	 */
	public void resyncAddresses() throws IOException {
		for (String contact : contactButtons.keySet().toArray(new String[0]))
			if (!hasUnreadMessages.get(contact))
				contactButtons.remove(contact);
		if (contactButtons.isEmpty())
			contactList.setContent(null);
		sendMulticastHelloPacket(mainWindow.helloAddress);
	}

	/**
	 * Sends "Hello" packet through a {@link MulticastSocket}
	 * 
	 * @param address
	 *            address of client sending "Hello" packet
	 * @throws IOException
	 */
	public void sendMulticastHelloPacket(InetSocketAddress address) throws IOException {
		if (mainWindow.userName != null && mainWindow.userAddress != null) {
			String data = mainWindow.userName + mainWindow.userAddress.toString();
			byte[] buf = new MessagePacket('H', data).prepareToSend();

			mainWindow.multicastSocket.send(new DatagramPacket(buf, buf.length, address));
		}
	}

	/**
	 * Shows dialog until user enters port to set connections on.
	 * 
	 * @return chosen port
	 */
	public int getNewPort() {
		TextInputDialog nameDialog = new TextInputDialog("1234");
		nameDialog.setTitle("Choose port");
		nameDialog.setHeaderText("What is your port?");
		nameDialog.setContentText("Enter it here: ");
		Optional<String> result;
		do {
			result = nameDialog.showAndWait();
		} while (!result.isPresent());
		return Integer.parseInt(result.get());
	}

	/**
	 * Shows dialog for user to choose a preferred network interface.
	 * 
	 * @return Local address of selected interface
	 * @throws SocketException
	 */
	public InetAddress getNewAddress() throws SocketException {
		Enumeration<NetworkInterface> nets = NetworkInterface.getNetworkInterfaces();
		HashMap<String, InetAddress> netList = new HashMap<String, InetAddress>();
		for (NetworkInterface netint : Collections.list(nets)) {
			if (!netint.getInetAddresses().hasMoreElements())
				continue;
			Enumeration<InetAddress> inetAddresses = netint.getInetAddresses();
			for (InetAddress inetAddress : Collections.list(inetAddresses)) {
				if (inetAddress.isLoopbackAddress())
					continue;
				if (inetAddress.toString().contains(":"))
					continue;
				netList.put(netint.getDisplayName(), inetAddress);
			}
		}
		if (netList.size() == 1)
			return netList.get(netList.keySet().iterator().next());

		ChoiceDialog<String> dialog = new ChoiceDialog<String>("Choose", new ArrayList<String>(netList.keySet()));
		dialog.setTitle("Wybierz interfejs");
		dialog.setHeaderText("Wybierz interfejs sieciowy");
		InetAddress address = netList.get(dialog.showAndWait().get());
		return address;
	}

	/**
	 * Adds an address to {@link #contactButtons} if it doesn't exist there. Else
	 * update it's text. Then renders contact buttons.
	 * 
	 * @param message
	 *            message containing username and address.
	 * @see MessagePacket
	 * @see #renderContactButtons()
	 */
	public void addAddressToContactList(String message) {
		String address = message.substring(message.indexOf("/"));
		if (contactButtons.containsKey(address))
			contactButtons.get(address).setName(message.substring(0, message.indexOf("/")));
		else {
			contactButtons.put(address, new ContactButton(message));
			hasUnreadMessages.put(address, false);
		}

		renderContactButtons();
	}

	/**
	 * Renders contact buttons on main window stage.
	 */
	public void renderContactButtons() {
		VBox root = new VBox();
		root.setSpacing(10);
		root.setPadding(new Insets(10));
		for (String key : contactButtons.keySet()) {
			ContactButton button = contactButtons.get(key);
			button.setOnAction(e -> {
				try {
					openConversationWindow(button.getAddress().toString(), button.getName());
				} catch (Exception ex) {
					System.out.println(ex.getMessage());
				}
			});
			root.getChildren().add(button);
		}
		contactList.setContent(root);
	}

	/**
	 * Opens a new conversation window if it's not opened. Sets conversation text
	 * and chosen user's address and name.
	 * 
	 * @param address
	 *            address of chosen user
	 * @param name
	 *            name of chosen user
	 * @throws IOException
	 */
	public void openConversationWindow(String address, String name) throws IOException {
		if (conversationWindowController == null) {
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("ConversationWindow.fxml"));
			Parent conversationWindowParent = fxmlLoader.load();
			conversationWindowController = (ConversationWindowController) fxmlLoader.getController();
			conversationWindowStage = new Stage();
			conversationWindowStage.setScene(new Scene(conversationWindowParent));
			conversationWindowStage.show();
		}
		hasUnreadMessages.replace(address, false);
		contactButtons.get(address).setStyle(defBtnColor);
		conversationWindowStage.setTitle(name + address);
		conversationWindowController.setConversationText(conversationTexts.get(address));
		String host = address.substring(address.indexOf("/") + 1, address.indexOf(":"));
		int port = Integer.parseInt(address.substring(address.indexOf(":") + 1));
		conversationWindowController.address = new InetSocketAddress(host, port);
		conversationWindowController.mainController = this;
		conversationWindowStage.show();

	}

	/**
	 * Adds text to conversation text area.
	 * 
	 * @param address
	 *            address of user (conversation), key of {@link #conversationTexts}.
	 * @param text
	 *            text to write to {@link #conversationTexts}
	 */
	public void addSelfTextToConversation(String address, String text) {
		if (!conversationTexts.containsKey(address))
			conversationTexts.put(address, "");
		String oldText = conversationTexts.get(address);
		if (!oldText.isEmpty())
			oldText = oldText + "\n";
		oldText += text;
		conversationTexts.replace(address, oldText);
		if (conversationWindowController != null && conversationWindowController.address.toString().equals(address))
			conversationWindowController.setConversationText(oldText);
	}

	/**
	 * Adds text to conversation text area. Add prefix on the beginning of message.
	 * 
	 * @param address
	 *            address of user (conversation), key of {@link #conversationTexts}.
	 * @param text
	 *            text to write to {@link #conversationTexts}
	 */
	public void addOthersTextToConversation(String address, String text) {
		if (!conversationTexts.containsKey(address)) {
			conversationTexts.put(address, "");
		}
		String oldText = conversationTexts.get(address);
		if (!oldText.isEmpty())
			oldText = oldText + "\n";
		oldText += "RE: " + text.replaceAll("/n", "/n   ");
		conversationTexts.replace(address, oldText);
		if (conversationWindowController != null && conversationWindowController.address.toString().equals(address)
				&& conversationWindowStage.isShowing())
			conversationWindowController.setConversationText(oldText);
		else {
			hasUnreadMessages.replace(address, true);
			contactButtons.get(address).setStyle("-fx-background-color: #ff0000; ");
		}
	}

}
