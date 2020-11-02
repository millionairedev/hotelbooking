package ui;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;

import util.HBUtil;
import db.DBUtil;
import domain.User;

/*
 * This class handles hotel manager screen from where the manager can 
 * view/delete bookings and also manage application settings 
 * 
 */

public class HotelManagerScreen {

	public JFrame frame;
	private DBUtil dbUtil;
	private JPanel mainPanel;
	private JPanel buttonsPanel;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					HotelManagerScreen window = new HotelManagerScreen();
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
	// Custom Constructor
	public HotelManagerScreen(DBUtil dbUtil, User user) {
		this.dbUtil = dbUtil;
		initialize();
		showMyBookings(user);
	}

	// Display all bookings
	private void showMyBookings(final User user) {
		String query = "select * from booking";
		System.out.println("Query: " + query);
		final JTable table = HBUtil.populateTableForGivenQuery(query, dbUtil);
		table.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);

		mainPanel = new JPanel(new GridLayout());
		mainPanel.setBackground(new Color(100));
		mainPanel.setSize(700, 700);
		JScrollPane jScrollPane = new JScrollPane(table);
		mainPanel.add(jScrollPane);

		buttonsPanel = new JPanel();
		JButton deleteBookingBtn = new JButton("Delete booking");
		buttonsPanel.add(deleteBookingBtn);
		JButton refreshBtn = new JButton("Refresh");
		buttonsPanel.add(refreshBtn);
		JButton settingsBtn = new JButton("Settings");
		buttonsPanel.add(settingsBtn);
		JButton logoutBtn = new JButton("Logout");
		buttonsPanel.add(logoutBtn);

		// Delete selected booking
		deleteBookingBtn.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				// check for selected row first
				if (table.getSelectedRow() != -1) {
					int selectedRow = table.getSelectedRow();
					String selectedRowId = table.getValueAt(selectedRow, 0)
							.toString();
					// remove selected row from the model
					((DefaultTableModel) table.getModel())
							.removeRow(selectedRow);
					// delete booking from database
					deleteBookingFromDB(selectedRowId);
				} else {
					HBUtil.displayMessage("Please select a booking first");
				}
			}
		});

		// Show settings screen
		settingsBtn.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				SettingsScreen settingsScreen = new SettingsScreen(dbUtil, user);
				settingsScreen.frame.setVisible(true);
			}
		});

		// Reload bookings table to show all currently existing bookings
		refreshBtn.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				frame.getContentPane().remove(mainPanel);
				frame.getContentPane().remove(buttonsPanel);
				showMyBookings(user);
				frame.getContentPane().add(buttonsPanel);
				frame.repaint();
				frame.revalidate();
			}
		});

		// Logout method
		logoutBtn.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				frame.dispose();
				LoginScreen loginScreen = new LoginScreen();
				loginScreen.frame.setVisible(true);
			}
		});

		frame.getContentPane().add(mainPanel);
		frame.getContentPane().add(buttonsPanel);
	}

	// Default Constructor
	public HotelManagerScreen() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setTitle("Hotel Manager/Admin Screen");
		frame.setBounds(100, 100, 734, 500);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(
				new BoxLayout(frame.getContentPane(), BoxLayout.Y_AXIS));
	}

	// Deleting booking from database
	private void deleteBookingFromDB(String selectedRowId) {
		String query = "delete from booking where id = '" + selectedRowId + "'";
		System.out.println("Deleting booking ...");
		int status = dbUtil.runQuery(query);
		if (status == 1) {
			HBUtil.displayMessage("Selected booking deleted");
		}
	}
}
