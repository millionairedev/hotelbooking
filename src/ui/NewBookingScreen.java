package ui;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Timestamp;
import java.util.Calendar;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.jdesktop.swingx.JXDatePicker;

import util.HBUtil;
import db.DBUtil;
import domain.User;

/*
 * This class allows users to add new bookings
 * 
 */

public class NewBookingScreen {

	public JFrame frame;
	private JTextField textField;
	private DBUtil dbUtil;
	private User user;
	private String checkinDate = null;
	private String checkoutDate = null;
	private JTextField textField_1;
	private JLabel priceValueLabel;
	private JLabel currencyLabel;
	private JLabel totalPriceValueLabel;
	private JXDatePicker picker1 = null;
	private JXDatePicker picker2 = null;

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
	 */
	// Default Constructor
	public NewBookingScreen() {
		initialize();
	}

	// Custom Constructor
	public NewBookingScreen(DBUtil dbUtil, User user) {
		this.dbUtil = dbUtil;
		this.user = user;
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setTitle("Add new booking");
		frame.setBounds(100, 100, 450, 352);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.getContentPane().setLayout(null);

		JLabel lblCheckinDate = new JLabel("Number of rooms:");
		lblCheckinDate.setBounds(10, 22, 112, 26);
		frame.getContentPane().add(lblCheckinDate);

		JLabel lblRoomType = new JLabel("Room type:");
		lblRoomType.setBounds(10, 59, 92, 26);
		frame.getContentPane().add(lblRoomType);

		JLabel lblCheckinDate_1 = new JLabel("Check-in date:");
		lblCheckinDate_1.setBounds(10, 110, 92, 26);
		frame.getContentPane().add(lblCheckinDate_1);

		JLabel lblCheckoutDate = new JLabel("Check-out date:");
		lblCheckoutDate.setBounds(10, 158, 92, 26);
		frame.getContentPane().add(lblCheckoutDate);

		textField = new JTextField();
		textField.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent arg0) {
				updateTotalPrice();
			}

		});
		textField.setBounds(161, 25, 151, 23);
		frame.getContentPane().add(textField);
		textField.setColumns(10);

		// Show existing room types
		final JComboBox<String> comboBox = new JComboBox<String>();
		comboBox.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent arg0) {
				if (arg0.getStateChange() == ItemEvent.SELECTED) {
					Object item = arg0.getItem();
					priceValueLabel.setText(dbUtil.getPriceForRoomType(item
							.toString()));
					priceValueLabel.repaint();
					priceValueLabel.revalidate();
					updateTotalPrice();
				}
			}
		});
		comboBox.setModel(new DefaultComboBoxModel<String>(dbUtil
				.getRoomTypes().split(",")));
		comboBox.setBounds(161, 62, 151, 20);
		frame.getContentPane().add(comboBox);

		final JPanel panel = new JPanel();
		panel.setBackground(Color.LIGHT_GRAY);
		panel.setBounds(161, 99, 151, 37);
		picker1 = getDatePicker();

		checkinDate = HBUtil.getSimpleDateFormat().format(picker1.getDate());
		
		// Event handler for date selection
		picker1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				checkinDate = HBUtil.getSimpleDateFormat().format(
						picker1.getDate());
				updateTotalPrice();
			}
		});
		panel.add(picker1);
		frame.getContentPane().add(panel);

		JPanel panel_1 = new JPanel();
		panel_1.setBackground(Color.LIGHT_GRAY);
		panel_1.setBounds(161, 147, 151, 37);
		picker2 = getDatePicker();
		checkoutDate = HBUtil.getSimpleDateFormat().format(picker2.getDate());
		
		// Event handler for date selection
		picker2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				checkoutDate = HBUtil.getSimpleDateFormat().format(
						picker2.getDate());
				updateTotalPrice();
			}
		});
		panel_1.add(picker2);
		frame.getContentPane().add(panel_1);

		JButton btnAddBooking = new JButton("Add booking");
		btnAddBooking.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				// Adding new booking for the logged in user
				String query = "INSERT INTO booking (Guest, Rooms, RoomType, CheckIn, CheckOut, BookedAt, "
						+ "customer_id, Price) values ('"
						+ textField_1.getText().trim()
						+ "','"
						+ textField.getText().trim()
						+ "','"
						+ comboBox.getSelectedItem().toString()
						+ "','"
						+ checkinDate
						+ "','"
						+ checkoutDate
						+ "','"
						+ new Timestamp(System.currentTimeMillis())
						+ "','"
						+ dbUtil.getIDForUser(user.getUsername())
						+ "','"
						+ getTotalBookingPrice() + "')";

				System.out.println("QUERY: " + query);
				int status = dbUtil.runQuery(query);
				if (status == 1) {
					HBUtil.displayMessage("Booking added successfully");
					frame.dispose();
				}
			}
		});
		btnAddBooking.setBounds(186, 279, 112, 23);
		frame.getContentPane().add(btnAddBooking);

		JLabel lblGuestName = new JLabel("Guest Name:");
		lblGuestName.setBounds(10, 210, 92, 26);
		frame.getContentPane().add(lblGuestName);

		textField_1 = new JTextField();
		textField_1.setBounds(161, 213, 151, 29);
		frame.getContentPane().add(textField_1);
		textField_1.setColumns(10);

		JLabel lblPrice = new JLabel("Price:");
		lblPrice.setBounds(338, 68, 42, 14);
		frame.getContentPane().add(lblPrice);

		priceValueLabel = new JLabel(dbUtil.getPriceForRoomType(comboBox
				.getSelectedItem().toString()));
		priceValueLabel.setBounds(397, 68, 27, 14);
		frame.getContentPane().add(priceValueLabel);

		currencyLabel = new JLabel(dbUtil.getCurrency());
		currencyLabel.setBounds(392, 68, 10, 14);
		frame.getContentPane().add(currencyLabel);

		JLabel lblTotalPrice = new JLabel("Total Price:");
		lblTotalPrice.setBounds(10, 255, 78, 20);
		frame.getContentPane().add(lblTotalPrice);

		totalPriceValueLabel = new JLabel(dbUtil.getCurrency() + "0");
		totalPriceValueLabel.setBounds(161, 253, 151, 20);
		frame.getContentPane().add(totalPriceValueLabel);
	}

	// DatePicker plugin class
	public static JXDatePicker getDatePicker() {
		JXDatePicker picker = new JXDatePicker();
		picker.setDate(Calendar.getInstance().getTime());
		return picker;
	}

	// This method gets total price for the current booking based on number of rooms and room unit price
	private int getTotalBookingPrice() {
		int totalPrice = 0;
		int numOfRooms = 0;
		String numOfRoomsTextVal = textField.getText().trim();
		try {
			numOfRooms = Integer.valueOf(numOfRoomsTextVal);
		} catch (NumberFormatException nfe) {
			if (numOfRoomsTextVal.isEmpty()) {
				System.out.println("Number of rooms value cannot be blank");
			}
			else {
				HBUtil.displayMessage("Invalid number, please re-enter");
			}
		}
		int selectedRoomPrice = Integer.valueOf(priceValueLabel.getText());
		int totalDaysForBooking = (int) ((picker2.getDate().getTime() - picker1
				.getDate().getTime()) / (1000 * 60 * 60 * 24));
		System.out.println("Booking for days: " + totalDaysForBooking);
		if (totalDaysForBooking == 0) {
			totalDaysForBooking = 1;
		}
		totalPrice = numOfRooms * selectedRoomPrice * (totalDaysForBooking);

		return totalPrice;
	}

	// This method updates total price on multiple events
	private void updateTotalPrice() {
		totalPriceValueLabel.setText(dbUtil.getCurrency()
				+ Integer.valueOf(getTotalBookingPrice()).toString());
		totalPriceValueLabel.repaint();
		totalPriceValueLabel.revalidate();
	}
}
