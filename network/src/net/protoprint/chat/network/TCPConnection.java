package net.protoprint.chat.network;

import java.io.*;
import java.net.Socket;
import java.nio.charset.Charset;

public class TCPConnection {

	// сокет связанный с TCP соединением
	private final Socket socket;
	//поток слушающий входящее соединение
	private final Thread rxThread;

	private final BufferedReader in;
	private final BufferedWriter out;

	public TCPConnection(Socket socket) throws IOException {
		this.socket = socket;
		in = new BufferedReader(new InputStreamReader(socket.getInputStream(), Charset.forName("UTF-8")));
		out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(), Charset.forName("UTF-8")));
		rxThread = new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					String msg = in.readLine();
				} catch (IOException e) {
					e.printStackTrace();
				} finally {

				}
			}
		});
		rxThread.start();
	}
}
