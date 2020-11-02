package ui;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Vector;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import db.DBUtil;
import domain.User;

public class CustomerScreen {

	public JFrame frame;
	private DBUtil dbUtil;
	private User user;
	private JPanel mainPanel;
	private JPanel buttonsPanel;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					CustomerScreen window = new CustomerScreen();
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
	public CustomerScreen(DBUtil dbUtil, User user) {
		this.dbUtil = dbUtil;
		this.user = user;
		initialize();
		showMyBookings(user);
	}
	
	private void showMyBookings(final User user){
		String query = "select Guest, Rooms,RoomType,CheckIn,CheckOut,BookedAt,Price from booking where customer_id = '"+user.getId()+"'";
		System.out.println("Query: "+query);
		JTable table = populateTableForGivenQuery(query);
		
		mainPanel = new JPanel(new GridLayout());
		mainPanel.setBackground(new Color(100));
		mainPanel.setSize(700,700);
		JScrollPane jScrollPane = new JScrollPane(table);
		mainPanel.add(jScrollPane);
		
		buttonsPanel = new JPanel();
		JButton addBookingBtn = new JButton("Add new booking");
		buttonsPanel.add(addBookingBtn);
		JButton refreshBtn = new JButton("Refresh");
		buttonsPanel.add(refreshBtn);
		JButton logoutBtn = new JButton("Logout");
		buttonsPanel.add(logoutBtn);
		
		addBookingBtn.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				NewBookingScreen newBookingScreen = new NewBookingScreen(dbUtil,user);
				newBookingScreen.frame.setVisible(true);
			}
		});
		
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
	public CustomerScreen() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setTitle("Bookings for customer "+user.getUsername());
		frame.setBounds(100, 100, 673, 500);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(new BoxLayout(frame.getContentPane(), BoxLayout.Y_AXIS));
	}
	
	private JTable populateTableForGivenQuery(String query){
		ResultSet rs = this.dbUtil.runRSQuery(query);
	    // It creates and displays the table
	    JTable table = null;
		try {
			table = new JTable(buildTableModel(rs));
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
//		table.getColumnModel().getColumn(0).setPreferredWidth(5);
//		table.getColumnModel().getColumn(1).setPreferredWidth(12);
//		table.getColumnModel().getColumn(2).setPreferredWidth(12);
//		table.getColumnModel().getColumn(3).setPreferredWidth(20);
		return table;
	}
	
	
	public static DefaultTableModel buildTableModel(ResultSet resultSet)
	        throws SQLException {

	    ResultSetMetaData metaData = resultSet.getMetaData();
	    Vector<String> columnNames = new Vector<String>();
	    int columnCount = metaData.getColumnCount();
	    for (int column = 1; column <= columnCount; column++) {
	        columnNames.add(metaData.getColumnName(column));
	    }

	    // data of the table
	    Vector<Vector<Object>> data = new Vector<Vector<Object>>();
	    while (resultSet.next()) {
	        Vector<Object> vector = new Vector<Object>();
	        for (int columnIndex = 1; columnIndex <= columnCount; columnIndex++) {
	            vector.add(resultSet.getObject(columnIndex));
	        }
	        data.add(vector);
	    }
	    return new DefaultTableModel(data, columnNames);
	}
}
