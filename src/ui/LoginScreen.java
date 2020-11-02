package ui;

import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Toolkit;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.UnsupportedLookAndFeelException;

import util.HBUtil;

import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;

import db.DBUtil;
import domain.User;

import javax.swing.JPanel;

public class LoginScreen extends JFrame {

	/**
	 * 
	 */
	public JFrame frame;
	private JTextField txtUser;
	private JPasswordField passwordField;
	private JLabel lblPleaseLoginBelow;
	private JButton btnLogin;
	private DBUtil dbUtil;
	private JLabel label;
	private JPanel panel;
	private JLabel lblNewLabel_1;
	private JTextField textField;
	private JLabel lblAddress;
	private JLabel lblAddress_1;
	private JLabel lblNewLabel_2;
	private JTextField textField_1;
	private JTextField textField_2;
	private JTextField textField_3;
	private JButton btnRegister;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {

				try {
					for (LookAndFeelInfo info : UIManager
							.getInstalledLookAndFeels()) {
						if ("Nimbus".equals(info.getName())) {
							UIManager.setLookAndFeel(info.getClassName());
							break;
						}
					}
				} catch (Exception e) {
					// If Nimbus is not available, you can set the GUI to
					// another look and feel.
					try {
						UIManager.setLookAndFeel(UIManager
								.getSystemLookAndFeelClassName());
					} catch (ClassNotFoundException | InstantiationException
							| IllegalAccessException
							| UnsupportedLookAndFeelException ex) {
						ex.printStackTrace();
					}
				}

				try {
					LoginScreen window = new LoginScreen();
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
	// Default Constructor
	public LoginScreen() {
		initialize();
		dbUtil = new DBUtil();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setTitle("Login Screen");
		frame.setBounds(100, 100, 452, 422);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(
				new FormLayout(new ColumnSpec[] {
				FormFactory.RELATED_GAP_COLSPEC,
				FormFactory.DEFAULT_COLSPEC,
				FormFactory.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("default:grow"),
				FormFactory.RELATED_GAP_COLSPEC,
				FormFactory.DEFAULT_COLSPEC,
				FormFactory.RELATED_GAP_COLSPEC,
				FormFactory.DEFAULT_COLSPEC,
				FormFactory.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("default:grow"),},
			new RowSpec[] {
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				RowSpec.decode("default:grow"),
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,}));

		label = new JLabel("");

		try {
			ImageIcon icon = new ImageIcon(ImageIO.read(ClassLoader
					.getSystemResource("booking.png")));
			label.setIcon(icon);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		frame.getContentPane().add(label, "4, 2, 7, 1");

		lblPleaseLoginBelow = new JLabel("Please login/register below:");
		frame.getContentPane().add(lblPleaseLoginBelow, "4, 6");

		JLabel lblNewLabel = new JLabel("Username");
		frame.getContentPane().add(lblNewLabel, "4, 8");

		txtUser = new JTextField();
		txtUser.setText("manager");
		frame.getContentPane().add(txtUser, "8, 8, fill, default");
		txtUser.setColumns(10);

		JLabel lblPassword = new JLabel("Password");
		frame.getContentPane().add(lblPassword, "4, 10");

		passwordField = new JPasswordField();
		frame.getContentPane().add(passwordField, "8, 10, fill, default");

		passwordField.setText("manager");
		
		btnLogin = new JButton("Login");
		btnLogin.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent arg0) {
				if (arg0.getKeyCode() == KeyEvent.VK_ENTER) {
					loginOrRegister();
				}
			}
		});
		btnLogin.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				loginOrRegister();
			}
		});
		frame.getContentPane().add(btnLogin, "8, 12");
		
		panel = new JPanel();
		frame.getContentPane().add(panel, "4, 14, 7, 10, fill, fill");
		panel.setLayout(null);
		
		lblNewLabel_1 = new JLabel("Name");
		lblNewLabel_1.setBounds(0, 29, 46, 14);
		panel.add(lblNewLabel_1);
		
		textField = new JTextField();
		textField.setBounds(233, 26, 100, 23);
		panel.add(textField);
		textField.setColumns(10);
		
		lblAddress = new JLabel("Address");
		lblAddress.setBounds(0, 60, 46, 14);
		panel.add(lblAddress);
		
		lblAddress_1 = new JLabel("Email");
		lblAddress_1.setBounds(0, 91, 46, 14);
		panel.add(lblAddress_1);
		
		lblNewLabel_2 = new JLabel("Telephone Number");
		lblNewLabel_2.setBounds(0, 122, 126, 14);
		panel.add(lblNewLabel_2);
		
		textField_1 = new JTextField();
		textField_1.setColumns(10);
		textField_1.setBounds(233, 57, 100, 23);
		panel.add(textField_1);
		
		textField_2 = new JTextField();
		textField_2.setColumns(10);
		textField_2.setBounds(233, 88, 100, 23);
		panel.add(textField_2);
		
		textField_3 = new JTextField();
		textField_3.setColumns(10);
		textField_3.setBounds(233, 118, 100, 23);
		panel.add(textField_3);
		
		btnRegister = new JButton("Register");
		btnRegister.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				registerUser(txtUser.getText().trim(), passwordField.getText().trim(), textField.getText().trim(),
						textField_1.getText().trim(), textField_2.getText().trim(), textField_3.getText().trim());
			}
		});
		btnRegister.setBounds(233, 154, 100, 23);
		panel.add(btnRegister);
		
		panel.setVisible(false);
	}

	// Login for existing user and Register for a new user
	private void loginOrRegister() {
		userExists(txtUser.getText().trim());
	}

	// Checking if user exists
	private void userExists(String username) {
		String query = "select username from user where username ='"+ username + "'";
		System.out.println("Query: "+query);
		if (dbUtil.recordExists(query)) {
			// Authenticating user
			query = "select username from hotel.user where username ='"
					+ username + "' and password = '"
					+ passwordField.getText().trim() + "'";

			if (dbUtil.recordExists(query)) {
				System.out
						.println("User account exists already, logging in ...");
				User user = new User();
				user.setUsername(username);
				user.setId(Integer.valueOf(dbUtil.getIDForUser(username)));
				frame.dispose();

				if (username.equalsIgnoreCase("manager")) {
					HotelManagerScreen hotelManagerScreen = new HotelManagerScreen(	dbUtil, user);
					hotelManagerScreen.frame.setVisible(true);
				} else {
					CustomerScreen customerScreen = new CustomerScreen(dbUtil,user);
					customerScreen.frame.setVisible(true);
				}
			} else {
				HBUtil.displayMessage("Incorrect password, please try again.");
			}
		} else {
			if (isNewAccountRequested(username) == JOptionPane.YES_OPTION) {
				panel.setVisible(true);
			}
		}
	}

	// Checking if user wants a new account added
	private int isNewAccountRequested(String username) {
		int dialogButton = JOptionPane.YES_NO_OPTION;
		int dialogResult = JOptionPane.showConfirmDialog(null, "User "
				+ username + " does not exist, would you like create "
				+ "a new account as this user?", "Confirmation", dialogButton);
		return dialogResult;
	}

	// Register new user here
	private void registerUser(String username, String password, String customerName, String address, String email, String phone) {
		String query = "insert into hotel.user(username,password,user_type,Name,Address,Email,Phone) "
				+ "values('" + username + "','" + password + "','Customer'"+",'" + customerName+ "','"+ address+ "','" + email+ "','" + phone+"')";
		int status = dbUtil.runQuery(query);
		if (status == 1) {
			HBUtil.displayMessage("New user " + username
					+ " added. Thank you for registering.");
			panel.setVisible(false);
		}
	}

}
