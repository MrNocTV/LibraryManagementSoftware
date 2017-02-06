import java.awt.Color;
import java.awt.Font;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.TreeMap;

import javax.swing.BorderFactory;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;


public class ReportCategoryFrame extends JFrame{
	private MainFrame mainFrame;
	private JPanel categoryPanel;
	private JComboBox categoryMonthComboBox;
	private JComboBox categoryYearComboBox;
	private JTable categoryTable;
	private DefaultTableModel categoryTableModel;
	private JTextField categoryTotalTextField;
	
	public ReportCategoryFrame(MainFrame frame) {
		super("Report");
		setLayout(null);
		mainFrame = frame;
		
		categoryPanel = new JPanel();
		categoryPanel.setLayout(null);
		categoryPanel.setSize(767, 203);
		categoryPanel.setLocation(5, 5);
		if(mainFrame.language.equals("english"))
		categoryPanel.setBorder(BorderFactory.createTitledBorder(
			null, "Books Borrowed By Genre", 
			TitledBorder.CENTER, TitledBorder.ABOVE_TOP));
		else
			categoryPanel.setBorder(BorderFactory.createTitledBorder(
					null, "Tình Hình Sách Mượn Theo Thể Loại", 
					TitledBorder.CENTER, TitledBorder.ABOVE_TOP));
		
		JPanel timePanel = new JPanel();
		timePanel.setSize(750, 35);
		timePanel.setLocation(8,22);
		timePanel.setBorder(BorderFactory.createLoweredBevelBorder());
		JLabel categoryMonthLabel = new JLabel("Month");
		timePanel.add(categoryMonthLabel);
		categoryMonthComboBox = new JComboBox<>();
		categoryMonthComboBox.setMaximumRowCount(3);
		for(int i = 1; i <= 12; ++i ){ 
			categoryMonthComboBox.addItem((String) (i < 10 ? "0"+i : ""+i) );
		}
		categoryMonthComboBox.addItemListener(new TimeSelectHandler());
		
		timePanel.add(categoryMonthComboBox);
		JLabel categoryYearLabel = new JLabel("Year");
		timePanel.add(categoryYearLabel);
		categoryYearComboBox = new JComboBox<>(new String[]{"2016","2017","2018","2019","2020","2021","2022","2023","2024","2025"});
		categoryYearComboBox.setMaximumRowCount(3);
		categoryYearComboBox.addItemListener(new TimeSelectHandler());
		timePanel.add(categoryYearComboBox);
		categoryPanel.add(timePanel);
		
		if(mainFrame.language.equals("english"))
			categoryTable = new JTable(new DefaultTableModel(new Object[]{"Order","Category","Borrowd Times", "Rate(%)"}, 0)){
				public boolean isCellEditable(int row, int column) {
					return false;
				}
			};
		else 
			categoryTable = new JTable(new DefaultTableModel(new Object[]{"STT","Thể Loại","Số Lượt Mượn", "Tỉ Lệ(%)"}, 0)){
			public boolean isCellEditable(int row, int column) {
				return false;
			}
			};
		categoryTable.setFillsViewportHeight(true);
		categoryTable.setGridColor(Color.BLACK);
		categoryTable.setBackground(Color.LIGHT_GRAY);
		for(int i = 0; i < categoryTable.getColumnCount(); ++i) {
			TableColumn column = categoryTable.getColumnModel().getColumn(i);
			if(i == 0) {
				column.setPreferredWidth(50);
			}else if(i == 1) {
				column.setPreferredWidth(400);
			} else if(i == 2) {
				column.setPreferredWidth(100);
			} else if(i == 3) {
				column.setPreferredWidth(100);
			} 
		}
		categoryTableModel = (DefaultTableModel) categoryTable.getModel();
		JScrollPane scrollPane = new JScrollPane(categoryTable);
		scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		scrollPane.setBorder(BorderFactory.createLoweredBevelBorder());
		scrollPane.setSize(750, 100);
		scrollPane.setLocation(8, 57);
		categoryPanel.add(scrollPane);
		
		JLabel categoryTotalLabel = new JLabel("Total:");
		categoryTotalLabel.setForeground(Color.RED);
		categoryTotalLabel.setFont(new Font("Serif", Font.BOLD, 15));
		categoryTotalLabel.setSize(100, 20);
		categoryTotalLabel.setLocation(400, 170);
		categoryTotalLabel.setHorizontalAlignment(SwingConstants.RIGHT);
		categoryPanel.add(categoryTotalLabel);
		categoryTotalTextField = new JTextField();
		categoryTotalTextField.setSize(100,20);
		categoryTotalTextField.setEditable(false);
		categoryTotalTextField.setLocation(510, 170);
		categoryPanel.add(categoryTotalTextField);
		
		if(mainFrame.language.equals("vietnamese")) {
			categoryMonthLabel.setText("Tháng");
			categoryYearLabel.setText("Năm");
			categoryTotalLabel.setText("Tổng Cộng");
			setTitle("Báo Cáo");
		}
		
		add(categoryPanel);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setSize(775, 240);
		setVisible(true);
		setLocationRelativeTo(mainFrame);
		setResizable(false);
	}
	
	//handle when user select a month (or a year)
	private class TimeSelectHandler implements ItemListener {
		@Override
		public void itemStateChanged(ItemEvent event) {
			String month = (String) categoryMonthComboBox.getSelectedItem();
			if(!month.equals("Off")) {
				String year = (String) categoryYearComboBox.getSelectedItem();
				
				//get categories
				System.out.println(year + " " + month);
				try {
					//reset table
					categoryTableModel.getDataVector().removeAllElements();
					categoryTable.repaint();
					//reset totalTextField
					categoryTotalTextField.setText("0");
					
					TreeMap<String, Integer> categoriesTree = new TreeMap<String, Integer>();
					Statement statement = mainFrame.connect.createStatement();
					ResultSet result = statement.executeQuery("SELECT * FROM Category");
					while(result.next()) {
						categoriesTree.put(result.getString("Category").trim(), 0);
					}
					statement.close(); result = null;
					statement = mainFrame.connect.createStatement();
					result = statement.executeQuery("SELECT * FROM BorrowedBooks");
					
					//go through borrowed books, 
					//increase value count of each category in tree
					while(result.next()) {
						DDate day = new DDate(result.getDate("BorrowedDate").toString());
						String m = "" + (day.month < 10 ? "0"+day.month : day.month);
						String y = String.valueOf(day.year);
						if(month.equals(m) && year.equals(y) ) {
							String category = result.getString("Category");
							setValueForKey(category, categoriesTree);
						}
					}
					int order = 1; 
					int total = 0;
					for(String s : categoriesTree.keySet()) {
						total += categoriesTree.get(s);
					}
					for(String s : categoriesTree.keySet()) {
						float rate = (categoriesTree.get(s) / (float)total ) * 100;
						int time = categoriesTree.get(s);
						categoryTableModel.addRow(new Object[]{String.valueOf(order), s,String.valueOf(time) , String.valueOf(rate)});
						++order;
					}
					categoryTotalTextField.setText(String.valueOf(total));
					
				}catch(SQLException ex) {
					JOptionPane.showMessageDialog(ReportCategoryFrame.this, "SQL ERROR (TimeSelectHandler)");
				}
				
			} else {
				JOptionPane.showMessageDialog(ReportCategoryFrame.this, "Please select a month");
			}
		}
		
		private void setValueForKey(String category, TreeMap<String, Integer> tree) {
			for(String s : tree.keySet()) {
				if(s.equals(category)) {
					int val = tree.get(s);
					tree.put(s, val+1);
				}
			}
		}
	}
	
}
