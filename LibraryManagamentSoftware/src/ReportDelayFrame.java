import java.awt.Color;
import java.awt.Font;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

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


public class ReportDelayFrame extends JFrame{
	private MainFrame mainFrame;
	private JPanel delayPanel;
	private JComboBox delayMonthComboBox;
	private JComboBox delayYearComboBox;
	private JTable delayTable;
	private DefaultTableModel delayTableModel;
	private JTextField totalDelayDaysTextField;
	
	public ReportDelayFrame(MainFrame frame) {
		super("Report");
		setLayout(null);
		mainFrame = frame;
		
		delayPanel = new JPanel();
		delayPanel.setLayout(null);
		delayPanel.setSize(767, 203);
		delayPanel.setLocation(5, 5);
		if(mainFrame.language.equals("english")) {
		delayPanel.setBorder(BorderFactory.createTitledBorder(
			null, "Delay Paying Books", 
			TitledBorder.CENTER, TitledBorder.ABOVE_TOP));
		} else {
			delayPanel.setBorder(BorderFactory.createTitledBorder(
					null, "Tình Hình Sách Trả Trễ", 
					TitledBorder.CENTER, TitledBorder.ABOVE_TOP));
		}
		JPanel timePanel = new JPanel();
		timePanel.setSize(750, 35);
		timePanel.setLocation(8,22);
		timePanel.setBorder(BorderFactory.createLoweredBevelBorder());
		JLabel delayMonthLabel = new JLabel("Month");
		timePanel.add(delayMonthLabel);
		delayMonthComboBox = new JComboBox<>();
		delayMonthComboBox.setMaximumRowCount(3);
		for(int i = 1; i <= 12; ++i) 
			delayMonthComboBox.addItem((String) (i < 10 ? "0"+i : ""+i) );
		delayMonthComboBox.addItemListener(new TimeSelectHandler());
		
		timePanel.add(delayMonthComboBox);
		JLabel delayYearLabel = new JLabel("Year");
		timePanel.add(delayYearLabel);
		delayYearComboBox = new JComboBox<>(new String[]{"2016","2017","2018","2019","2020","2021","2022","2023","2024","2025"});
		delayYearComboBox.setMaximumRowCount(3);
		delayYearComboBox.addItemListener(new TimeSelectHandler());
		timePanel.add(delayYearComboBox);
		delayPanel.add(timePanel);
		
		if(mainFrame.language.equals("english")) {
			delayTable = new JTable(new DefaultTableModel(new Object[]{"Order","Book Name","Borrowd Date", "Delay Days"}, 0)){
				public boolean isCellEditable(int row, int column) {
					return false;
				}
			};
		}else {
			delayTable = new JTable(new DefaultTableModel(new Object[]{"STT","Tên Sách","Ngày Mượn", "Số Ngày Trả Trễ"}, 0)){
			public boolean isCellEditable(int row, int column) {
				return false;
			}
			};
		}
		
		delayTable.setFillsViewportHeight(true);
		delayTable.setGridColor(Color.BLACK);
		delayTable.setBackground(Color.LIGHT_GRAY);
		for(int i = 0; i < delayTable.getColumnCount(); ++i) {
			TableColumn column = delayTable.getColumnModel().getColumn(i);
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
		delayTableModel = (DefaultTableModel) delayTable.getModel();
		JScrollPane scrollPane = new JScrollPane(delayTable);
		scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		scrollPane.setBorder(BorderFactory.createLoweredBevelBorder());
		scrollPane.setSize(750, 100);
		scrollPane.setLocation(8, 57);
		delayPanel.add(scrollPane);
		
		JLabel delayMoneyLabel = new JLabel("Total:");
		delayMoneyLabel.setForeground(Color.RED);
		delayMoneyLabel.setFont(new Font("Serif", Font.BOLD, 15));
		delayMoneyLabel.setSize(100, 20);
		delayMoneyLabel.setLocation(538, 170);
		delayMoneyLabel.setHorizontalAlignment(SwingConstants.RIGHT);
		delayPanel.add(delayMoneyLabel);
		totalDelayDaysTextField = new JTextField();
		totalDelayDaysTextField.setSize(100, 20);
		totalDelayDaysTextField.setLocation(640, 170);
		totalDelayDaysTextField.setEditable(false);
		delayPanel.add(totalDelayDaysTextField);
		
		if(mainFrame.language.equals("vietnamese")) {
			delayMoneyLabel.setText("Tổng Cộng");
			delayYearLabel.setText("Năm");
			delayMonthLabel.setText("Tháng");
			setTitle("Báo Cáo");
		}
		
		add(delayPanel);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setSize(775, 240);
		setVisible(true);
		setLocationRelativeTo(mainFrame);
		setResizable(false);
	}
	
	//
	private class TimeSelectHandler implements ItemListener {
		@Override
		public void itemStateChanged(ItemEvent event) {
			String month = (String) delayMonthComboBox.getSelectedItem();
			String year = (String) delayYearComboBox.getSelectedItem();
			
			
			//reset table 
			delayTableModel.getDataVector().removeAllElements();
			delayTable.repaint();
	
			//reset totalDelayTextField
			totalDelayDaysTextField.setText("");
			
			try {
				Statement statement = mainFrame.connect.createStatement();
				ResultSet result = statement.executeQuery("SELECT * FROM BorrowedBooks");
				int order = 1;
				while(result.next()) {
					String borrowedDate = result.getDate("BorrowedDate").toString();
					String delayDays = calculateDelayDays(borrowedDate);
					DDate day = new DDate(result.getDate("BorrowedDate").toString());
					String m = "" + (day.month < 10 ? "0"+day.month : day.month);
					String y = String.valueOf(day.year);
					if(month.equals(m) && year.equals(y) ) {
						if(!delayDays.equals("0")) {
							delayTableModel.addRow(new Object[]{String.valueOf(order),
									result.getString("BookTitle"), borrowedDate, delayDays});
							++order;
						}
					}
					
				}
				int total = 0;
				for(int i = 0; i < delayTable.getRowCount(); ++i) {
					total += new Integer((String)delayTable.getValueAt(i, 3));
				}
				totalDelayDaysTextField.setText(String.valueOf(total));
			}catch(SQLException ex) {
				JOptionPane.showMessageDialog(ReportDelayFrame.this, "SQL ERROR (ReportDelayFrame)");
			}
		}
		
		private String calculateDelayDays(String borrowedDate) {
			DDate bDay = new DDate(borrowedDate);
			DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			Date date = new Date();
			DDate today = new DDate(dateFormat.format(date));
			//get maximum borrow time
			long maximumBorrowTime = 0;
			try {
				Statement statement = mainFrame.connect.createStatement();
				ResultSet result = statement.executeQuery("SELECT * FROM Properties");
				if(result.next()) {
					maximumBorrowTime = (long)result.getInt("MaximumBorrowTime");
				} else throw new SQLException();
				long borrowDays = DDate.distanceBetweenDate(today, bDay);
				//if today <= bDay, mean reader pays book earlier or on time 
				if(borrowDays > maximumBorrowTime )
					return String.valueOf(borrowDays-maximumBorrowTime);
			}catch(SQLException ex ){ 
				JOptionPane.showMessageDialog(ReportDelayFrame.this, "SQL ERROR (calculateDelayDays)");
			}catch(ParseException ex) {
				JOptionPane.showMessageDialog(ReportDelayFrame.this, "Parse ERROR (calculateDelayDays)");
			}
			
			return "0";
		}
	}

}
