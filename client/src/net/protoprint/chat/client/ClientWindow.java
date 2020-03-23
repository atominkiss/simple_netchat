package net.protoprint.chat.client;

import net.protoprint.chat.network.TCPConnection;
import net.protoprint.chat.network.TCPConnectionListener;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class ClientWindow extends JFrame implements ActionListener, TCPConnectionListener {

	private static final String IP_ADDR = "127.0.0.1";
	private static final int PORT = 8189;
	private static final int WIDTH = 600;
	private static final int HEIGHT = 400;

	public static void main(String[] args) {
		//Свинг не может работать из главного потока. Только из потока ЕДТ. Для этого и нужна конструкция ниже.
		SwingUtilities.invokeLater(() -> new ClientWindow());
	}

	private final JTextArea log = new JTextArea();
	private final JTextField fieldNickname = new JTextField("your nickname");
	private final JTextField fieldInput = new JTextField();
	private TCPConnection connection;

	private ClientWindow() {
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		//Устанавливаем фиксированный начальный размер окна
		setSize(WIDTH, HEIGHT);
		//Задаем отображение окна по центру экрана
		setLocationRelativeTo(null);
		//отображение поверх всех окон
		setAlwaysOnTop(true);
		//запрещаем редактирование в окошке отображения сообщений
		log.setEditable(false);
		//Атовматический перенос строк
		log.setLineWrap(true);
		//компонуем отбражение окна чата на окне
		add(log, BorderLayout.CENTER);
		//перехватываем нажатие Enter для отправки сообщения
		fieldInput.addActionListener(this);
		//компонуем отбражение текстового поля для ввода сообщения на окне
		add(fieldInput, BorderLayout.SOUTH);
		//компонуем отбражение текстового поля никнейма на окне
		add(fieldNickname, BorderLayout.NORTH);
		//делаем окно видимым
		setVisible(true);
		try {
			connection = new TCPConnection(this, IP_ADDR, PORT);
		} catch (IOException e) {
			printMsg("Connection exception: " + e);
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		//получаем введенное сообщение
		String msg = fieldInput.getText();
		//если ошибочно нажали на интер и строчка пуста, значит ничего не отправляем
		if (msg.equals("")) return;
		//очищаем поле ввода ссобщения
		fieldInput.setText(null);
		//отправляем сообщение с ником
		connection.sendString(fieldNickname.getText() + ": " + msg);
	}

	@Override
	public void onConnectionReady(TCPConnection tcpConnection) {
		printMsg("Connection ready...");
	}

	@Override
	public void onReceiveString(TCPConnection tcpConnection, String value) {
		printMsg(value);
	}

	@Override
	public void onDisconnect(TCPConnection tcpConnection) {
		printMsg("Connection close.");
	}

	@Override
	public void onException(TCPConnection tcpConnection, Exception e) {
		printMsg("Connection exception: " + e);
	}

	//Метод логирования
	private synchronized void printMsg(String msg) {
		SwingUtilities.invokeLater(() -> {
			//вывод сообщений
			log.append(msg + "\n");
			//Автоскрол точно отработает так как всегда устанавливаем позицию каретки в самом конце поля
			log.setCaretPosition(log.getDocument().getLength());
		});
	}
}
