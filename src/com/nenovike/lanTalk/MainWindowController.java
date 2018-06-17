package com.nenovike.lanTalk;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
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

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
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

public class MainWindowController implements Initializable {

	public MainWindow mainWindow = null;
	private HashMap<String, ContactButton> contactButtons = new HashMap<String, ContactButton>();
	private HashMap<String, String> conversationMap = new HashMap<String , String>();
	ConversationWindowController conversationWindowController = null;
	Stage conversationWindowStage;
	@FXML
	private MenuItem menuChangeName;
	@FXML
	private ScrollPane contactList;
	@FXML
	private Button button;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {

	}

	@FXML
	private void handleMenuAction(ActionEvent action) throws IOException {
		if (action.getSource() == menuChangeName) {
			changeName();
		}
	}

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

	public void resyncAddresses() throws IOException {
		contactButtons.clear();
		sendMulticastHelloPacket(mainWindow.defaultHelloAddress);
	}

	private void sendMulticastHelloPacket(InetSocketAddress address) throws IOException {

		String data = mainWindow.userName + mainWindow.userAddress.toString();
		byte[] buf = new MessagePacket('H', data).prepareToSend();

		mainWindow.multicastSocket.send(new DatagramPacket(buf, buf.length, address));
	}

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

	public void addAddressToContactList(String message){
		String address = message.substring(message.indexOf("/"));
		if (contactButtons.containsKey(address))
			contactButtons.replace(address, new ContactButton(message));
		else
			contactButtons.put(address, new ContactButton(message));
		renderContactButtons();
	}

	public void renderContactButtons(){
		VBox root = new VBox();
		root.setSpacing(10);
		root.setPadding(new Insets(10));
		for (String key : contactButtons.keySet()) {
			ContactButton button = contactButtons.get(key);
			button.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent event){
					try {
					openConversationWindow(button.getAddress().toString(), button.getName());
					}
					catch(Exception e) {
						System.out.println(e.getMessage());
					}
				}
			});
			root.getChildren().add(button);
		}
		contactList.setContent(root);
	}

	public void openConversationWindow(String address, String name) throws IOException{
		if (conversationWindowController == null) {
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("ConversationWindow.fxml"));
			Parent conversationWindowParent = fxmlLoader.load();
			conversationWindowController = (ConversationWindowController) fxmlLoader.getController();
			conversationWindowStage = new Stage();
			conversationWindowStage.setScene(new Scene(conversationWindowParent));
			conversationWindowStage.show();
		}
		conversationWindowStage.setTitle(name+address);
		conversationWindowController.setConversationText(conversationMap.get(address));
	}
}
