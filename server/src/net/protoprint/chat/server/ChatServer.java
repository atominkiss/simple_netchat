package net.protoprint.chat.server;

import net.protoprint.chat.network.TCPConnection;
import net.protoprint.chat.network.TCPConnectionListener;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;

public class ChatServer implements TCPConnectionListener {

	public static void main(String[] args) {
		new ChatServer();
	}

	private final ArrayList<TCPConnection> connections = new ArrayList<>();

	private ChatServer() {

		System.out.println("Server running...");

		try (ServerSocket serverSocket = new ServerSocket(8189)) {
			while (true) {
				try {
					new TCPConnection(this, serverSocket.accept());
				}catch (IOException e) {
					System.out.println("TCPconnection exception: " + e);
				}
			}
		}catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void onConnectionReady(TCPConnection tcpConnection) {

	}

	@Override
	public void onReceiveString(TCPConnection tcpConnection, String value) {

	}

	@Override
	public void onDisconnect(TCPConnection tcpConnection) {

	}

	@Override
	public void onException(TCPConnection tcpConnection, Exception e) {

	}
}
