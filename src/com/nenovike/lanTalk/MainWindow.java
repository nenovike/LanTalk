package com.nenovike.lanTalk;

import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextInputDialog;
import javafx.stage.Stage;

public class MainWindow extends Application {
	static String name = "LanTalk";
	static Stage primaryStage;

	static MainWindowController mainWindowController;

	@Override
	public void start(Stage primaryStage) throws Exception {
		this.primaryStage = primaryStage;
		FXMLLoader fxmlLoader = new FXMLLoader();
		Parent root = fxmlLoader.load(getClass().getResource("MainWindow.fxml"));
		mainWindowController = (MainWindowController) fxmlLoader.getController();
		Scene scene = new Scene(root);
		this.primaryStage.setScene(scene);
		this.primaryStage.show();
		ChangeName();
		byte[] buf = "Siema".getBytes(StandardCharsets.UTF_8);
		new Thread(new Server()).start();
		MulticastSocket ms = new MulticastSocket(1112);
		ms.joinGroup(InetAddress.getByName("233.0.0.0"));
		ms.send(new DatagramPacket(buf, buf.length, InetAddress.getByName("233.0.0.0"), 1111));

	}

	public static void ChangeName() throws Exception {
		TextInputDialog nameDialog = new TextInputDialog("LanTalk");
		nameDialog.setTitle("LanTalk");
		nameDialog.setHeaderText("What is your name?");
		nameDialog.setContentText("Enter it here: ");
		Optional<String> result;
		do {
			result = nameDialog.showAndWait();
		} while (result.isPresent() && result.get().length() > 10);
		if (result.isPresent())
			name = result.get();

		primaryStage.setTitle(name + "(" + InetAddress.getLocalHost().getHostAddress() + ")");
	}

	public static void main(String[] args) {
		MainWindow.launch(args);
	}

}
