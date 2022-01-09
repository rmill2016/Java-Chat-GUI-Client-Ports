package chatclientgui;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JTextArea;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class ChatClientGUI {

	private JFrame frame;
	private JTextField tfServer;
	private JTextField tfPort;
	private JTextField tfDisplayName;
	private JTextField tfMsg;
	private JTextArea taMsg;
	
	private ChatClient client;
	private JButton btnBye;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ChatClientGUI window = new ChatClientGUI();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public ChatClientGUI() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 450, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		JLabel lblServer = new JLabel("Server");
		lblServer.setBounds(23, 25, 46, 14);
		frame.getContentPane().add(lblServer);
		
		JLabel lblPort = new JLabel("Port");
		lblPort.setBounds(23, 48, 46, 14);
		frame.getContentPane().add(lblPort);
		
		JLabel lblDisplayName = new JLabel("Display Name");
		lblDisplayName.setBounds(23, 73, 69, 14);
		frame.getContentPane().add(lblDisplayName);
		
		tfServer = new JTextField();
		tfServer.setBounds(99, 22, 200, 20);
		frame.getContentPane().add(tfServer);
		tfServer.setColumns(10);
		
		tfPort = new JTextField();
		tfPort.setBounds(99, 45, 200, 20);
		frame.getContentPane().add(tfPort);
		tfPort.setColumns(10);
		
		tfDisplayName = new JTextField();
		tfDisplayName.setBounds(99, 70, 200, 20);
		frame.getContentPane().add(tfDisplayName);
		tfDisplayName.setColumns(10);
		
		tfMsg = new JTextField();
		tfMsg.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				client.send();
				tfMsg.requestFocus();
			}
		});
		tfMsg.setBounds(16, 98, 357, 20);
		frame.getContentPane().add(tfMsg);
		tfMsg.setColumns(10);
		
		JButton btnJoin = new JButton("Join");
		btnJoin.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String server = tfServer.getText();
				int port = Integer.parseInt(tfPort.getText());
				String displayName = tfDisplayName.getText();
				
				// run chatting client
				taMsg.setText("");
				client =
						new ChatClient(server, port, displayName, taMsg, tfMsg);
				
				// can bye and chat
				btnJoin.setEnabled(false);
				btnBye.setEnabled(true);
				tfMsg.setEnabled(true);
			}
		});
		btnJoin.setBounds(312, 21, 89, 23);
		frame.getContentPane().add(btnJoin);
		
		btnBye = new JButton("Bye");
		btnBye.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//send .bye
				tfMsg.setText(".bye");
				client.send();
				
				// disable or enable buttons
				btnJoin.setEnabled(true);
				btnBye.setEnabled(false);
				tfMsg.setEditable(false);
			}
		});
		btnBye.setBounds(312, 48, 89, 23);
		frame.getContentPane().add(btnBye);
		
	    taMsg = new JTextArea();
		taMsg.setBounds(16, 124, 357, 126);
		frame.getContentPane().add(taMsg);
		
		btnJoin.setEnabled(true);
		btnBye.setEnabled(false);
		tfMsg.setEnabled(false);
	}
}
