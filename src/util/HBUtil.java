package util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Vector;

import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

import db.DBUtil;


/*
 * Utility class to hold general common-use methods
 * across the application
 * 
 */
public class HBUtil {
	
	public static  SimpleDateFormat simpleDateFormat = new SimpleDateFormat("YYYY-MM-dd");
	
	public static SimpleDateFormat getSimpleDateFormat() {
		return simpleDateFormat;
	}

	public static void setSimpleDateFormat(SimpleDateFormat simpleDateFormat) {
		HBUtil.simpleDateFormat = simpleDateFormat;
	}

	public static void displayMessage(String message){
		JOptionPane.showMessageDialog(null, message);
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
	
	public static JTable populateTableForGivenQuery(String query, DBUtil dbUtil) {
		ResultSet rs = dbUtil.runRSQuery(query);
		JTable table = null;
		try {
			table = new JTable(HBUtil.buildTableModel(rs));
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return table;
	}
	
	public static void writeContentToFile(String content) {
		BufferedWriter writer = null;
		try {
			File file = new File("Backup.txt");
			writer = new BufferedWriter(new FileWriter(file));
			writer.write(content);
			System.out.println("Data written to file: "+file.getAbsolutePath());

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (writer != null)
					writer.close();
			} catch (IOException e) {
			}
		}
	}
	
	public static String getTableData(JTable roomTypesTable) {
		String tableData = ""
				+ "Room    Price\n";
		TableModel tableModel = roomTypesTable.getModel();
		for (int i = 0; i < tableModel.getRowCount(); i++) {
			tableData += "\n";
		  for (int j = 1; j < tableModel.getColumnCount(); j++) {
		    Object o = tableModel.getValueAt(i, j);
		    tableData += o.toString()+"    ";
		  }
		}
		return tableData;
	}
}
