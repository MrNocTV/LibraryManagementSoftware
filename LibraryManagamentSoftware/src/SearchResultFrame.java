import java.awt.Color;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;


public class SearchResultFrame extends JFrame{
	private MainFrame mainFrame;
	private ArrayList<String> resultList;
	private JTable table;
	private DefaultTableModel tableModel;
	
	public SearchResultFrame(MainFrame frame, ArrayList<String> results, Connection connection) {
		super("Search Result");
		resultList = results;
		mainFrame = frame;
		table = new JTable(new DefaultTableModel(new Object[]{"Order","Title","Author","Publisher","Year","Quantity","Borrowed"}, 0)) {
			@Override
			public boolean isCellEditable(int x, int y) {
				return false;
			}
		};
		table.setGridColor(Color.BLACK);
		table.setFillsViewportHeight(true);
		tableModel = (DefaultTableModel) table.getModel();
		try {
			Statement statement = connection.createStatement();
			ResultSet result = statement.executeQuery("SELECT * FROM Book");
			int order = 1;
			while(result.next()) {
				if(resultList.contains(result.getString("Title"))) {
					tableModel.addRow(new Object[]{
							String.valueOf(order),
							result.getString("Title"),
							result.getString("Author"),
							result.getString("Publisher"),
							String.valueOf(result.getInt("PublishYear")),
							String.valueOf(result.getInt("Quantity")),
							String.valueOf(result.getInt("Borrowed"))
					});
					++order;
				}
			}
		}catch(SQLException ex) {
			JOptionPane.showMessageDialog(mainFrame, "SQL ERROR (SearchResultFrame)");
		}
		JScrollPane scrollPane = new JScrollPane(table);
		scrollPane.setBorder(BorderFactory.createTitledBorder(null, ""));
		add(scrollPane);
		
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setSize(700,200);
		setVisible(true);
		setLocationRelativeTo(mainFrame);
	}
}
