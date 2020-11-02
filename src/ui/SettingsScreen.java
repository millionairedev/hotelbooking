package ui;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;

import org.jdesktop.swingx.JXDatePicker;

import util.HBUtil;
import db.DBUtil;
import domain.User;

/*
 * This class shows application settings to the Hotel Manager 
 * for him/her to be able to edit
 *  
 */

public class SettingsScreen {

	public JFrame frame;
	private JTextField textField;
	private DBUtil dbUtil;
	private String checkinDate = null;
	private String checkoutDate = null;
	private JXDatePicker picker1 = null;
	private JXDatePicker picker2 = null;
	private JTextField textField_2;
	private JTable roomTypesTable = new JTable();
	private JLabel revenueForSelectedDateLabel;
	private JLabel numberOfBookingsLabel;
	private JTextField textField_1;
	private JLabel currencyLabel;
	private JTextField passwordField;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					NewBookingScreen window = new NewBookingScreen();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 * 
	 */
	// Default Constructor
	public SettingsScreen() {
		initialize();
	}
	// Custom Constructor
	public SettingsScreen(DBUtil dbUtil, User user) {
		this.dbUtil = dbUtil;
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setTitle("Settings Screen/Admin Panel");
		frame.setBounds(100, 100, 450, 643);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.getContentPane().setLayout(null);

		JLabel lblCheckinDate = new JLabel("Add Room Type:");
		lblCheckinDate.setBounds(10, 11, 92, 15);
		frame.getContentPane().add(lblCheckinDate);

		JLabel lblRoomType = new JLabel("Existing Room types:");
		lblRoomType.setBounds(10, 82, 112, 26);
		frame.getContentPane().add(lblRoomType);

		JLabel lblCheckinDate_1 = new JLabel("Show analysis data for:");
		lblCheckinDate_1.setBounds(10, 259, 152, 26);
		frame.getContentPane().add(lblCheckinDate_1);

		textField = new JTextField();
		textField.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent arg0) {
			}

		});
		textField.setBounds(76, 44, 86, 20);
		frame.getContentPane().add(textField);
		textField.setColumns(10);

		final JPanel panel = new JPanel();
		panel.setBackground(Color.LIGHT_GRAY);
		panel.setBounds(54, 295, 151, 37);
		picker1 = getDatePicker();

		checkinDate = HBUtil.getSimpleDateFormat().format(picker1.getDate());
		picker1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				checkinDate = HBUtil.getSimpleDateFormat().format(picker1.getDate());
				showBookingDetails();
				
			}

		});
		panel.add(picker1);
		frame.getContentPane().add(panel);
		picker2 = getDatePicker();
		checkoutDate = HBUtil.getSimpleDateFormat().format(picker2.getDate());
		picker2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				checkoutDate = HBUtil.getSimpleDateFormat().format(
						picker2.getDate());
			}
		});

		JLabel lblType = new JLabel("Type:");
		lblType.setBounds(31, 47, 35, 14);
		frame.getContentPane().add(lblType);

		JLabel lblPrice_1 = new JLabel("Price:");
		lblPrice_1.setBounds(176, 47, 46, 14);
		frame.getContentPane().add(lblPrice_1);

		textField_2 = new JTextField();
		textField_2.setColumns(10);
		textField_2.setBounds(215, 44, 86, 20);
		frame.getContentPane().add(textField_2);

		JButton btnSave = new JButton("Save");
		
		// Adding new room type
		btnSave.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				String query = "INSERT INTO "
						+ "room_prices(RoomType,Price)VALUES('"
						+ textField.getText().trim() + "','"
						+ textField_2.getText().trim() + "')";
				int status = dbUtil.runQuery(query);
				if (status == 1) {
					HBUtil.displayMessage("New Room type added");
					populateRoomTypesTable();
				}
			}
		});
		btnSave.setBounds(313, 43, 89, 23);
		frame.getContentPane().add(btnSave);

		JSeparator separator = new JSeparator();
		separator.setForeground(Color.WHITE);
		separator.setBackground(Color.BLACK);
		separator.setBounds(10, 75, 414, 2);
		frame.getContentPane().add(separator);

		JSeparator separator_1 = new JSeparator();
		separator_1.setForeground(Color.WHITE);
		separator_1.setBackground(Color.BLACK);
		separator_1.setBounds(10, 31, 414, 2);
		frame.getContentPane().add(separator_1);

		JSeparator separator_2 = new JSeparator();
		separator_2.setForeground(Color.WHITE);
		separator_2.setBackground(Color.BLACK);
		separator_2.setBounds(10, 106, 414, 2);
		frame.getContentPane().add(separator_2);

		JSeparator separator_3 = new JSeparator();
		separator_3.setForeground(Color.WHITE);
		separator_3.setBackground(Color.BLACK);
		separator_3.setBounds(10, 246, 414, 2);
		frame.getContentPane().add(separator_3);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 114, 414, 88);

		roomTypesTable
				.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
		populateRoomTypesTable();
		scrollPane.setViewportView(roomTypesTable);
		frame.getContentPane().add(scrollPane);

		JButton btnDeleteRoomType = new JButton("Delete Room Type");
		btnDeleteRoomType.setBounds(73, 213, 130, 23);
		
		// Delete selected room type
		btnDeleteRoomType.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				// check for selected row first
				if (roomTypesTable.getSelectedRow() != -1) {
					int selectedRow = roomTypesTable.getSelectedRow();
					String selectedRowId = roomTypesTable.getValueAt(
							selectedRow, 0).toString();
					// remove selected row from the model
					((DefaultTableModel) roomTypesTable.getModel())
							.removeRow(selectedRow);
					// delete room type from database
					deleteRoomTypeFromDB(selectedRowId);
				} else {
					HBUtil.displayMessage("Please select a room type first");
				}
			}
		});

		JButton btnUpdateRoomType = new JButton("Update Room Type");
		btnUpdateRoomType.setBounds(215, 212, 142, 23);

		// Update room type
		btnUpdateRoomType.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				// check for selected row first
				if (roomTypesTable.getSelectedRow() != -1) {
					int selectedRow = roomTypesTable.getSelectedRow();
					String selectedRowId = roomTypesTable.getValueAt(
							selectedRow, 0).toString();
					String selectedRowType = roomTypesTable.getValueAt(
							selectedRow, 1).toString();
					String selectedRowPrice = roomTypesTable.getValueAt(
							selectedRow, 2).toString();
					updateRoomTypeInDB(selectedRowId, selectedRowType,
							selectedRowPrice);
				} else {
					HBUtil.displayMessage("Please select a room type first");
				}
			}
		});

		frame.getContentPane().add(btnDeleteRoomType);
		frame.getContentPane().add(btnUpdateRoomType);

		JLabel lblRevenueForSelected = new JLabel(
				"Revenue:");
		lblRevenueForSelected.setBounds(54, 343, 68, 14);
		frame.getContentPane().add(lblRevenueForSelected);

		revenueForSelectedDateLabel = new JLabel(
				getBookingTotalForSelectedDate(HBUtil.getSimpleDateFormat().format(picker1.getDate())));
		revenueForSelectedDateLabel.setBounds(200, 343, 46, 14);
		frame.getContentPane().add(revenueForSelectedDateLabel);
		
		currencyLabel = new JLabel(dbUtil.getCurrency());
		currencyLabel.setBounds(186, 343, 18, 14);
		frame.getContentPane().add(currencyLabel);
		
		JLabel lblNumberOfBookings = new JLabel("Number of bookings:");
		lblNumberOfBookings.setBounds(10, 371, 112, 14);
		frame.getContentPane().add(lblNumberOfBookings);
		
		numberOfBookingsLabel = 
				new JLabel(getNumOfBookingsForSelectedDate(HBUtil.getSimpleDateFormat().format(picker1.getDate())));
		numberOfBookingsLabel.setBounds(186, 371, 46, 14);
		frame.getContentPane().add(numberOfBookingsLabel);
		
		JButton btnOk = new JButton("OK");
		btnOk.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				frame.dispose();
			}
		});
		btnOk.setBounds(176, 570, 89, 23);
		frame.getContentPane().add(btnOk);
		
		JSeparator separator_4 = new JSeparator();
		separator_4.setForeground(Color.WHITE);
		separator_4.setBackground(Color.BLACK);
		separator_4.setBounds(10, 283, 414, 2);
		frame.getContentPane().add(separator_4);
		
		JSeparator separator_5 = new JSeparator();
		separator_5.setForeground(Color.WHITE);
		separator_5.setBackground(Color.BLACK);
		separator_5.setBounds(10, 396, 414, 2);
		frame.getContentPane().add(separator_5);
		
		JLabel lblChangeCurrency = new JLabel("Change currency:");
		lblChangeCurrency.setBounds(10, 421, 112, 14);
		frame.getContentPane().add(lblChangeCurrency);
		
		textField_1 = new JTextField(dbUtil.getCurrency());
		textField_1.setBounds(132, 418, 114, 22);
		frame.getContentPane().add(textField_1);
		textField_1.setColumns(10);
		
		JButton btnUpdate = new JButton("Update Currency");
		
		// Update application currency
		btnUpdate.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				String query = "UPDATE admin_settings SET currency = '"+textField_1.getText().trim()+ "'";
				int status = dbUtil.runQuery(query);
				if (status == 1) {
					HBUtil.displayMessage("Currency updated");
					currencyLabel.setText(dbUtil.getCurrency());
					currencyLabel.repaint();
					currencyLabel.revalidate();
				}
			}
		});
		btnUpdate.setBounds(255, 417, 142, 23);
		frame.getContentPane().add(btnUpdate);
		
		JSeparator separator_6 = new JSeparator();
		separator_6.setForeground(Color.WHITE);
		separator_6.setBackground(Color.BLACK);
		separator_6.setBounds(10, 466, 414, 2);
		frame.getContentPane().add(separator_6);
		
		JLabel lblChangePassword = new JLabel("Change password:");
		lblChangePassword.setBounds(10, 485, 112, 14);
		frame.getContentPane().add(lblChangePassword);
		
		passwordField = new JPasswordField();
		passwordField.setBounds(132, 479, 114, 20);
		frame.getContentPane().add(passwordField);
		passwordField.setColumns(10);
		
		JButton btnUpdatePassword = new JButton("Update Password");
		
		// Change password for manager
		btnUpdatePassword.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				String query = "UPDATE user SET password = '"+passwordField.getText().trim()+ "' where username = 'manager'";
				int status = dbUtil.runQuery(query);
				if (status == 1) {
					HBUtil.displayMessage("Password changed for Hotel Manager");
				}
			}
		});
		btnUpdatePassword.setBounds(255, 479, 142, 23);
		frame.getContentPane().add(btnUpdatePassword);
		
		JSeparator separator_7 = new JSeparator();
		separator_7.setForeground(Color.WHITE);
		separator_7.setBackground(Color.BLACK);
		separator_7.setBounds(10, 522, 414, 2);
		frame.getContentPane().add(separator_7);
		
		JButton btnExport = new JButton("Export");
		
		// Export room types data to text file
		btnExport.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				String roomsDetails = HBUtil.getTableData(roomTypesTable);
				HBUtil.writeContentToFile(roomsDetails);
				HBUtil.displayMessage("Data exported successfully");
			}
		});
		btnExport.setBounds(10, 535, 112, 23);
		frame.getContentPane().add(btnExport);
	}

	private void updateRoomTypeInDB(String selectedRowId, String roomType,
			String roomPrice) {
		String query = "update room_prices set RoomType = '" + roomType
				+ "', Price = '" + roomPrice + "' where id = " + selectedRowId;
		System.out.println("Query: " + query);
		System.out.println("Updating room type ...");
		int status = dbUtil.runQuery(query);
		if (status == 1) {
			HBUtil.displayMessage("Selected room type updated");
			populateRoomTypesTable();
		}

	}
	
	private void showBookingDetails() {
		revenueForSelectedDateLabel.setText(getBookingTotalForSelectedDate(HBUtil.getSimpleDateFormat().format(picker1.getDate())));
		revenueForSelectedDateLabel.repaint();
		revenueForSelectedDateLabel.revalidate();
		
		numberOfBookingsLabel.setText(getNumOfBookingsForSelectedDate(HBUtil.getSimpleDateFormat().format(picker1.getDate())));
		numberOfBookingsLabel.repaint();
		numberOfBookingsLabel.revalidate();
	}

	private void deleteRoomTypeFromDB(String selectedRowId) {
		String query = "delete from room_prices where id = " + selectedRowId;
		System.out.println("Deleting room type ...");
		int status = dbUtil.runQuery(query);
		if (status == 1) {
			HBUtil.displayMessage("Selected room type deleted");
		}
	}

	private void populateRoomTypesTable() {
		String query = "select * from room_prices";
		ResultSet resultSet = dbUtil.runRSQuery(query);
		try {
			roomTypesTable.setModel(HBUtil.buildTableModel(resultSet));
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static JXDatePicker getDatePicker() {
		JXDatePicker picker = new JXDatePicker();
		picker.setDate(Calendar.getInstance().getTime());
		return picker;
	}

	private String getBookingTotalForSelectedDate(String checkInDate) {
		String revenue = null;
		String query = "select sum(Price) from booking where CheckIn = '"+ checkInDate + "'";
		ResultSet resultSet = dbUtil.runRSQuery(query);
		try {
			while (resultSet.next()) {
				revenue = resultSet.getString(1);
				if (revenue == null){
					revenue = "0";
				}
				System.out.println("revenue: "+revenue);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return revenue;
	}
	
	private String getNumOfBookingsForSelectedDate(String checkInDate) {
		String numberOfBookings = null;
		String query = "select count(id) from booking where CheckIn = '"+ checkInDate + "'";
		System.out.println("Query: "+query);
		ResultSet resultSet = dbUtil.runRSQuery(query);
		try {
			while (resultSet.next()) {
				numberOfBookings = resultSet.getString(1);
				if (numberOfBookings == null){
					numberOfBookings = "0";
				}
				System.out.println("numberOfBookings: "+numberOfBookings);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return numberOfBookings;

	}
}
