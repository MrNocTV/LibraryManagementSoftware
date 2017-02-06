import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileLockInterruptionException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.BorderFactory;
import javax.swing.DefaultButtonModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;

import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;

public class PayBookFrame extends JFrame{
	private MainFrame mainFrame;
	private JComboBox readerNameComboBox;
	protected JTextField payingDateTextField;
	protected JTextField forfeitTextField;
	protected JTextField payingValueTextField;
	protected JTextField totalDebtTextField;
	private JButton processButton;
	private JPanel northPanel;
	private JTable payBooksTable;
	private DefaultTableModel payBooksTableModel;
	
	public PayBookFrame(MainFrame frame){
		super("Pay Book");
		setLayout(null);
		mainFrame = frame;
		northPanel = new JPanel();
		northPanel.setSize(585, 115);
		northPanel.setLocation(5,5);
		northPanel.setLayout(null);
		if(mainFrame.language.equals("english"))
			northPanel.setBorder(BorderFactory.createTitledBorder(
					null, "Paying Info", TitledBorder.LEFT,	TitledBorder.ABOVE_TOP));
		else 
			northPanel.setBorder(BorderFactory.createTitledBorder(
					null, "Thông Tin Trả Sách", TitledBorder.LEFT,	TitledBorder.ABOVE_TOP));
		JLabel readerNameLabel = new JLabel("Reader Name");
		readerNameLabel.setSize(120, 20);
		readerNameLabel.setLocation(20,25);
		northPanel.add(readerNameLabel);
		JLabel payingDateLabel = new JLabel("Paying Date");
		payingDateLabel.setSize(120, 20);
		payingDateLabel.setLocation(20, 50);
		northPanel.add(payingDateLabel);
		readerNameComboBox = new JComboBox<>(new String[]{"Off"});
		readerNameComboBox.setMaximumRowCount(5);
		readerNameComboBox.setSize(150, 20);
		readerNameComboBox.setLocation(145, 25);
		readerNameComboBox.addItemListener(new SelectReaderHandler());
		northPanel.add(readerNameComboBox);
		payingDateTextField = new JTextField();
		payingDateTextField.setSize(150, 20);
		payingDateTextField.setLocation(145, 50);
		payingDateTextField.setEditable(false);
		northPanel.add(payingDateTextField);
		processButton = new JButton("Process");
		processButton.setSize(145, 20);
		processButton.setLocation(148,75);
		processButton.addActionListener(new ProcessButtonHandler());
		northPanel.add(processButton);
		JLabel forfietLabel = new JLabel("Forfeit+oldDebt");
		forfietLabel.setSize(120, 20);
		forfietLabel.setLocation(310, 25);
		northPanel.add(forfietLabel);
		JLabel payingValueLabel = new JLabel("Pay");
		payingValueLabel.setSize(100,20);
		payingValueLabel.setLocation(310, 50);
		northPanel.add(payingValueLabel);
		JLabel totalDebtLabel = new JLabel("Total Debt");
		totalDebtLabel.setSize(100,20);
		totalDebtLabel.setLocation(310,75);
		northPanel.add(totalDebtLabel);
		forfeitTextField = new JTextField();
		forfeitTextField.setSize(150,20);
		forfeitTextField.setLocation(415, 25);
		forfeitTextField.setEditable(false);
		northPanel.add(forfeitTextField);
		payingValueTextField = new JTextField();
		payingValueTextField.setSize(150,20);
		payingValueTextField.setLocation(415, 50);
		payingValueTextField.addActionListener(new PayInputHandler());
		northPanel.add(payingValueTextField);
		totalDebtTextField = new JTextField();
		totalDebtTextField.setSize(150, 20);
		totalDebtTextField.setLocation(415, 75);
		totalDebtTextField.setEditable(false);
		northPanel.add(totalDebtTextField);
		if(mainFrame.language.equals("english"))
			payBooksTable = new JTable(new DefaultTableModel(
					new Object[]{"Order","Book Name","Borrowed Date", "Deylaying Days","Forfeit"}, 0)) {
				@Override
				public boolean isCellEditable(int row, int column) {
					return false;
				}
			};
		else 
			payBooksTable = new JTable(new DefaultTableModel(
					new Object[]{"STT","Tên Sách","Ngày Mượn", "Số Ngày Trễ","Tiền Phạt"}, 0)) {
				@Override
				public boolean isCellEditable(int row, int column) {
					return false;
				}
			};
		payBooksTable.setGridColor(Color.BLACK);
		payBooksTable.setFillsViewportHeight(true);
		payBooksTable.setBackground(Color.LIGHT_GRAY);
		for(int i = 0; i < payBooksTable.getColumnCount(); ++i) {
			
		}
		payBooksTableModel = (DefaultTableModel) payBooksTable.getModel();
		JScrollPane scrollPane = new JScrollPane(payBooksTable);
		scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		scrollPane.setBorder(BorderFactory.createTitledBorder(null,"",TitledBorder.LEFT, TitledBorder.ABOVE_TOP));
		scrollPane.setSize(581,115);
		scrollPane.setLocation(7,125);
		
		if(mainFrame.language.equals("vietnamese")) {
			readerNameLabel.setText("Tên");
			payingDateLabel.setText("Ngày Trả");
			processButton.setText("Tiến Hành");
			forfietLabel.setText("Phạt+Nợ Cũ");
			payingValueLabel.setText("Trả");
			totalDebtLabel.setText("Còn");
			setTitle("Trả Sách");
		}
		initReaderNameComboBox();
		initPayingDate();
		add(northPanel);
		add(scrollPane);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setSize(597,385);
		setVisible(true);
		setResizable(false);
		setLocationRelativeTo(mainFrame);
	}
		
	//handle when user click on process
	//just set user a new debt value
	//if book table has more than one row
	//then go through each row, update book info ( reduce borrowed value)
	private class ProcessButtonHandler implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent event) {
			if(!((String)readerNameComboBox.getSelectedItem()).equals("Off")) {
				if(!payingValueTextField.getText().trim().equals("") && !totalDebtTextField.getText().trim().equals("")) {
			
					int choice = JOptionPane.showConfirmDialog(PayBookFrame.this, "Do you want to do this?", "Prompt", JOptionPane.YES_NO_OPTION);
					if(choice == JOptionPane.YES_OPTION) {
						try {
							//set new debt value 
							String newDebt = totalDebtTextField.getText();
							String readerName = (String) readerNameComboBox.getSelectedItem();
							PreparedStatement pStatement = mainFrame.connect.prepareStatement("UPDATE Reader SET Debt=? WHERE Name=?");
							pStatement.setString(1, newDebt);
							pStatement.setString(2, readerName);
							int resultCount = pStatement.executeUpdate();
							pStatement.close();
							if(resultCount <= 0)
								throw new SQLException();
							//update borrowed book info (reduce borrowed value)
							for(int i = 0; i < payBooksTable.getRowCount(); ++i ){
								reduceBorrowedValue((String)payBooksTable.getValueAt(i, 1));
							}
							pStatement = mainFrame.connect.prepareStatement("DELETE FROM BorrowedBooks WHERE ReaderName=?");
							pStatement.setString(1, readerName);
							resultCount = pStatement.executeUpdate();
							if(resultCount <= 0)
								throw new SQLException();
							
							String saveString = String.format("Reader Name: %s%nForfeit (+oldDebt): %s%n"
									+ "Pay: %s%nTotal Debt: %s%nDate: %s%n", 
									(String)readerNameComboBox.getSelectedItem(), 
									forfeitTextField.getText(),
									payingValueTextField.getText(), totalDebtTextField.getText(),
									payingDateTextField.getText());
							
							//save to word
							 XWPFDocument document = new XWPFDocument();
						     XWPFParagraph paragraph = document.createParagraph();
						     XWPFRun run = paragraph.createRun();
						     run.setText(saveString);
						     run.setFontSize(16);
						     
						     JFileChooser fileChooser = new JFileChooser();
						     fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
						     int fileChoice = fileChooser.showSaveDialog(PayBookFrame.this);
						     if(fileChoice == JFileChooser.APPROVE_OPTION) {
						    	 FileOutputStream out = new FileOutputStream(fileChooser.getSelectedFile().getAbsolutePath()); 
						    	 document.write(out);
						     }
						     
						     //then everything is okey
						     JOptionPane.showMessageDialog(PayBookFrame.this, "Process successfully\n"
						     		+ "This window will close automatically");
						     dispose();
							
							
						}catch(SQLException ex) {
							JOptionPane.showMessageDialog(PayBookFrame.this, "SQL ERROR (ProcessButtonHandler)");
						}catch(FileNotFoundException ex) {
							JOptionPane.showMessageDialog(PayBookFrame.this, "FileNotFound Error.\n"
									+ "Check out Q&A for more details");
						}catch(IOException ex) {
							JOptionPane.showMessageDialog(PayBookFrame.this, "IO Error. \n"
									+ "Check out Q&A for more details");
						}
						
						
					} //end if choice
					
				} //end if equals("")
				else {
					JOptionPane.showMessageDialog(PayBookFrame.this, "Pay value / Total debt cannot empty");
				}
				
			}//end if equals("Off)
			else {
				JOptionPane.showMessageDialog(PayBookFrame.this, "Please choose a reader");
			}
				
		} //end actionPerformed
		
		private void reduceBorrowedValue(String bookTitle) throws SQLException {
			Statement statement = mainFrame.connect.createStatement();
			ResultSet result = statement.executeQuery("SELECT * FROM Book");
			int borrowed = 0;
			while(result.next()) {
				if(result.getString("Title").trim().equals(bookTitle)) {
					borrowed = result.getInt("Borrowed");
					break;
				}
			}
			statement.close();
			--borrowed;
			PreparedStatement pStatement = mainFrame.connect.prepareStatement("UPDATE BOOK SET Borrowed=? WHERE Title=?");
			pStatement.setInt(1, borrowed);
			pStatement.setString(2, bookTitle);
			int resultCount = pStatement.executeUpdate();
			pStatement.close();
			if(resultCount <= 0)
				throw new SQLException();
		}
	}
	
	
	//enter pay value handler
	//handle when user enter an amount of money in pay text field and hit enter
	//if it's empty, do nothing
	//if it's not a number or a negative number, notify user that they have to enter a positive integer
	//and it's value must be <= total debt
	private class PayInputHandler implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent event) {
			try {
				if(payingValueTextField.getText().trim().equals(""))
					throw new Exception();
				int payValue = new Integer(payingValueTextField.getText().trim());
				if(payValue < 0)
					throw new Exception();
				
				//set value of total debt
				int forfeit = new Integer(forfeitTextField.getText());
				//check if pay value < total debt
				if(payValue > forfeit)
					throw new Exception();
				forfeit -= payValue;
				totalDebtTextField.setText(String.valueOf(forfeit));
			}catch(Exception ex) {
				JOptionPane.showMessageDialog(PayBookFrame.this, "Pay value is not valid.\n"
						+ "Check out Q&A and try again");
			}
		}
	}
	
	//handle when user select a reader in readerComboBox
	//load borrowed books into table 
	//calculate forfeit
	private class SelectReaderHandler implements ItemListener {
		@Override
		public void itemStateChanged(ItemEvent event) {
			if(event.getStateChange() == ItemEvent.SELECTED) {
				String readerName = (String) readerNameComboBox.getSelectedItem();
				
				payingValueTextField.setText("");
				//clear books in pay table
				payBooksTableModel.getDataVector().removeAllElements();
				payBooksTable.repaint();
				
				loadBorrowedBooks(readerName);
				setForfeit(readerName);
			}
		}
		
		private void loadBorrowedBooks(String readerName) {
			try {
				Statement statement = mainFrame.connect.createStatement();
				ResultSet result = statement.executeQuery("SELECT * FROM BorrowedBooks");
				int bookCount = 1;
				while(result.next()) {
					if(result.getString("ReaderName").trim().equals(readerName)) {
						String delay = calculateDelayDays(result.getDate("BorrowedDate").toString());
						String forfeit = calculateForfeit(delay);
						payBooksTableModel.addRow(new Object[]{String.valueOf(bookCount), result.getString("BookTitle"),result.getDate("BorrowedDate"), delay, forfeit});
						++bookCount;
					}
					
				}
			}catch(SQLException ex) {
				JOptionPane.showMessageDialog(PayBookFrame.this, "SQL ERROR (loadBorrowedBooks)");
			}
		}
		
		private void setForfeit(String readerName) {
			//forfeit = forfeit of each book * number of book
			//if some books are lost, then user have to calculate forfeit manually 
			try { 
				int total = 0;
				//get oldDebt
				Statement statement = mainFrame.connect.createStatement();
				ResultSet result = statement.executeQuery("SELECT * FROM Reader");
				while(result.next()) {
					if(result.getString("Name").trim().equals(readerName)) {
						total = new Integer(result.getString("Debt"));
						break;
					}
				}
				for(int count = 0 ; count < payBooksTable.getRowCount(); ++count) {
					total += new Integer((String)payBooksTable.getValueAt(count, 4));
				}
				forfeitTextField.setText(String.valueOf(total));
			}catch(SQLException ex) {
				
			}
				
		} //end setForfeit
	}
	
	private void initReaderNameComboBox() {
		try {
			Statement statement = mainFrame.connect.createStatement();
			ResultSet result = statement.executeQuery("SELECT * FROM Reader");
			while(result.next()) {
				if(isHealthyCard(result.getDate("ExpireDate").toString())) {
					readerNameComboBox.addItem(result.getString("Name"));
				}
			}
		}catch(SQLException ex) {
			JOptionPane.showMessageDialog(PayBookFrame.this, "SQL ERROR (initReaderNameComboBox)");
		}
	}
	
	private void initPayingDate() {
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		Date toDay = new Date();
		payingDateTextField.setText(dateFormat.format(toDay));
	}
	
	//check if reader's card is healthy
	//if it's healthy, return true
	//otherwise, return false
	private boolean isHealthyCard(String expireDate) {
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		Date date = new Date();
		DDate today = new DDate(dateFormat.format(date));
		DDate eDate = new DDate(expireDate);
		int compare = DDate.compareDate(today, eDate);
		return compare < 0;
	}
	
	private String calculateForfeit(String delay){ 
		int delayDays = new Integer(delay);
		//get cost per delay day value on server 
		try {
			Statement statement = mainFrame.connect.createStatement();
			ResultSet result = statement.executeQuery("SELECT * FROM Properties");
			int costPerDelayDay = 0;
			if(result.next()) {
				costPerDelayDay = result.getInt("CostPerDelayDay");
			} else throw new SQLException();
			int cost = costPerDelayDay*delayDays;
			return String.valueOf(cost);
		}catch(SQLException ex) {
			JOptionPane.showMessageDialog(PayBookFrame.this, "SQL ERROR (calculateForfeit)");
		}
		return "0";
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
			JOptionPane.showMessageDialog(PayBookFrame.this, "SQL ERROR (calculateDelayDays)");
		}catch(ParseException ex) {
			JOptionPane.showMessageDialog(PayBookFrame.this, "Parse ERROR (calculateDelayDays)");
		}
		
		return "0";
	}
}
