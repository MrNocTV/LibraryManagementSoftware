import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
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


public class LendBookFrame extends JFrame{
	private MainFrame mainFrame;
	private JPanel panel1;
	private JComboBox readerNameComboBox;
	private JTextField borrowedDateTextField;
	private JPanel panel2;
	private JComboBox bookTitleComboBox;
	private JButton addBookButton;
	private JPanel buttonPanel;
	private JButton clearButton;
	private JButton lendButton;
	private JTable bookTable;
	private DefaultTableModel bookTableModel;
	private int bookCount = 1;
	
	public LendBookFrame(MainFrame frame) {
		super("Lend Book");
		mainFrame = frame;
		setLayout(null);
		
		panel1 = new JPanel();
		panel1.setLayout(null);
		panel1.setSize(295, 57);
		panel1.setLocation(5,5);
		panel1.setBorder(BorderFactory.createTitledBorder(
				null, "",TitledBorder.LEFT, TitledBorder.ABOVE_TOP));;
		JLabel readerNameLabel = new JLabel("Name");
		readerNameLabel.setSize(120, 20);
		readerNameLabel.setLocation(10, 5);
		panel1.add(readerNameLabel);
		JLabel borrowedDateLabel = new JLabel("Borrowed Date");
		borrowedDateLabel.setSize(120, 20);
		borrowedDateLabel.setLocation(10, 30);
		panel1.add(borrowedDateLabel);
		readerNameComboBox = new JComboBox<>();
		readerNameComboBox.setSize(150, 20);
		readerNameComboBox.setLocation(135, 5);
		readerNameComboBox.setMaximumRowCount(3);
		readerNameComboBox.addItemListener(new ReaderSelectHandler());
		panel1.add(readerNameComboBox);
		borrowedDateTextField = new JTextField();
		borrowedDateTextField.setSize(150, 20);
		borrowedDateTextField.setLocation(135, 30);
		//get current day and set value for borrwedDateTextField
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		Date date = new Date();
		borrowedDateTextField.setText(dateFormat.format(date));
		panel1.add(borrowedDateTextField);
		
		panel2 = new JPanel();
		panel2.setLayout(null);
		panel2.setSize(305, 57);
		panel2.setLocation(305,5);
		panel2.setBorder(BorderFactory.createTitledBorder(
				null, "",TitledBorder.LEFT, TitledBorder.ABOVE_TOP));;
		JLabel bookNameLabel = new JLabel("Book Title ->");
		bookNameLabel.setSize(120, 20);
		bookNameLabel.setLocation(10, 5);
		panel2.add(bookNameLabel);
		bookTitleComboBox = new JComboBox<>();
		bookTitleComboBox.setSize(150, 20);
		bookTitleComboBox.setLocation(135, 5);
		bookTitleComboBox.setMaximumRowCount(5);
		panel2.add(bookTitleComboBox);
		addBookButton = new JButton("Add Book");
		addBookButton.setSize(140, 20);
		addBookButton.setLocation(140, 30);
		addBookButton.addActionListener(new AddBookButtonHandler());
		panel2.add(addBookButton);
		
		buttonPanel = new JPanel();
		buttonPanel.setLayout(null);
		buttonPanel.setSize(110, 57);
		buttonPanel.setLocation(615, 5);
		buttonPanel.setBorder(BorderFactory.createTitledBorder(
				null, "", TitledBorder.LEFT, TitledBorder.ABOVE_TOP));
		clearButton = new JButton("Clear");
		clearButton.setSize(90, 20);
		clearButton.setLocation(10,5);
		clearButton.addActionListener(new ClearButtonHandler());
		buttonPanel.add(clearButton);
		lendButton = new JButton("Lend");
		lendButton.setSize(90, 20);
		lendButton.setLocation(10, 30);
		lendButton.addActionListener(new LendBookButtonHandler());
		buttonPanel.add(lendButton);
		
		if(mainFrame.language.equals("english"))
			bookTable = new JTable(new DefaultTableModel(new Object[]{"Order","Book Title", "Category","Author"}, 0)) {
				@Override
				public boolean isCellEditable(int column, int row) {
					return false;
				}
		}; else {
			bookTable = new JTable(new DefaultTableModel(new Object[]{"STT","Tên Sách", "Thể Loại","Tác Giả"}, 0)) {
				@Override
				public boolean isCellEditable(int column, int row) {
					return false;
				}
			};
		}
		bookTable.setGridColor(Color.BLACK);
		bookTable.setFillsViewportHeight(true);
		bookTable.setBackground(Color.LIGHT_GRAY);
		bookTableModel = (DefaultTableModel) bookTable.getModel();
		JScrollPane scrollPane = new JScrollPane(bookTable);
		scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		scrollPane.setSize(720, 100);
		scrollPane.setLocation(5, 66);
		scrollPane.setBorder(BorderFactory.createTitledBorder(
				null, "", TitledBorder.LEFT, TitledBorder.ABOVE_TOP));
		
		
		if(mainFrame.language.equals("vietnamese")) {
			readerNameLabel.setText("");
			borrowedDateLabel.setText("");
			bookNameLabel.setText("");
			addBookButton.setText("");
			clearButton.setText("");
			lendButton.setText("");
			setTitle("Cho Mượn Sách");
		}
		
		initializeBookTitleComboBox();
		initReaderNameComboBox();
		add(panel1);
		add(panel2);
		add(buttonPanel);
		add(scrollPane);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setSize(730, 193);
		setVisible(true);
		setResizable(false);
		setLocationRelativeTo(mainFrame);
	}
	
	//handle when user click on lend button
	private class LendBookButtonHandler implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent event) {
			if(bookTable.getRowCount() == 0) {
				JOptionPane.showMessageDialog(LendBookFrame.this, "Please select book(s) first.");
 				return;
			}
			
			if(canReaderBorrowBooks((String)readerNameComboBox.getSelectedItem())) {
				int choice = JOptionPane.showConfirmDialog(LendBookFrame.this, "Are you sure?", "Prompt", JOptionPane.YES_NO_OPTION);
				if(choice == JOptionPane.YES_OPTION) {
					try {
						String borrowDate = borrowedDateTextField.getText().trim();
						String readerName = ((String) readerNameComboBox.getSelectedItem()).trim();
						for(int i = 0; i < bookTable.getRowCount(); ++i) {
							//get books in table
							String title = (String)bookTable.getValueAt(i, 1);
							String category = (String)bookTable.getValueAt(i, 2);
							String author = (String)bookTable.getValueAt(i, 3);
							PreparedStatement pStatement = mainFrame.connect.prepareStatement("INSERT INTO BorrowedBooks VALUES(?,?,?,?,?)");
							pStatement.setString(1, readerName);
							pStatement.setString(2, title);
							pStatement.setString(3, category);
							pStatement.setString(4, author);
							pStatement.setString(5, borrowDate);
							int resultCount = pStatement.executeUpdate();
							if(resultCount <= 0) //insert failed
								throw new SQLException();
							//add them to borrowedBooks table
							pStatement.close();
						}
						
						//update number of borrowed books in Book table
						for(int i = 0; i < bookTable.getRowCount(); ++i) {
							String title = (String)bookTable.getValueAt(i, 1);
							//get current number of borrowed books
							Statement statement = mainFrame.connect.createStatement();
							ResultSet result = statement.executeQuery("SELECT * FROM Book");
							while(result.next()) {
								if(result.getString("Title").trim().equals(title)) {
									int borrowedBooks = result.getInt("Borrowed");
									//update
									++borrowedBooks;
									PreparedStatement pStatement = mainFrame.connect.prepareStatement("UPDATE Book SET Borrowed=? WHERE Title=?");
									pStatement.setInt(1, borrowedBooks);
									pStatement.setString(2, title);
									int resultCount = pStatement.executeUpdate();
									if(resultCount <= 0)
										throw new SQLException();
									pStatement.close();
									break;
								}
							}
							result.close();
						}
						
						//lend book(s) success fully
						JOptionPane.showMessageDialog(LendBookFrame.this, "Lend book(s) successfully. LendBook Window will close automatically"); 
						dispose();
					}catch(SQLException ex) {
						JOptionPane.showMessageDialog(LendBookFrame.this, "SQL ERROR (LendBookButtonHandler)");
					}
				} //end if choice
			} //end if
			
		}
	}
	
	//handle when user click on clear button
	//clear book in book table
	//set bookCount = 1
	private class ClearButtonHandler implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent event) {
			bookCount = 1;
			bookTableModel.getDataVector().removeAllElements();
			bookTable.repaint();
		}
	}
	
	//handle when user click on add button
	//add book to table
	//cannot duplicate book
	private class AddBookButtonHandler implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent event ){ 
			String bookTitle = (String)bookTitleComboBox.getSelectedItem();
			
			//check duplicate 
			if(!checkDuplicate(bookTitle) && checkBookNumber()) {
				int choice = JOptionPane.showConfirmDialog(LendBookFrame.this, "Do you want to add this book?", "Prompt", JOptionPane.YES_NO_OPTION);
					if(choice == JOptionPane.YES_OPTION) {
						try {
							Statement statement = mainFrame.connect.createStatement();
							ResultSet result = statement.executeQuery("SELECT * FROM Book");
							while(result.next()) {
								//get info of book in database, put into table
								if(result.getString("Title").trim().equals(bookTitle)) {
									String title = result.getString("Title").trim();
									String category = result.getString("Category").trim();
									String author = result.getString("Author").trim();
									bookTableModel.addRow(new Object[]{String.valueOf(bookCount), title, category, author});
									++bookCount;
									break;
								}
							}
						}catch(SQLException ex) {
							JOptionPane.showMessageDialog(LendBookFrame.this, "SQL ERROR (AddBookButtonHandler)");
						}
					}
			} //end check duplicate
		}
		
		private boolean checkDuplicate(String bookTitle) {
			for(int i = 0 ; i < bookTable.getRowCount(); ++i) {
				String title = (String)bookTable.getValueAt(i, 1);
				if(title.equals(bookTitle)) {
					JOptionPane.showMessageDialog(LendBookFrame.this, "Duplicate Book.");
					return true;
				}
			}
			return false;
		}
		
		//number of books must be <= MaximumBorrowBook
		private boolean checkBookNumber() {
			//get maximum borrow book
			try {
				Statement statement = mainFrame.connect.createStatement();
				ResultSet result = statement.executeQuery("SELECT * FROM Properties");
				if(result.next()) {
					int maxBook = result.getInt("MaximumBorrowBook");
					if(bookCount > maxBook) {
						JOptionPane.showMessageDialog(LendBookFrame.this, "MaximumBorrowBook = " + maxBook);
						return false;
					}
				} else throw new SQLException();
			}catch(SQLException ex) {
				JOptionPane.showMessageDialog(LendBookFrame.this, "SQL ERROR (checkBookNumber)");
				System.out.println(ex.getMessage());
			}
			return true;
		}
	}
	
	//handle when user choose a reader name in reader name combo box
	//if this reader has already borrowed books. then stop user from lending books for this reader
	//and we also check reader debt too. 
	//we only consider readers have "healthy" card
	private class ReaderSelectHandler implements ItemListener {
		@Override
		public void itemStateChanged(ItemEvent event) {
			System.out.println((String)readerNameComboBox.getSelectedItem());
			if(event.getStateChange() == ItemEvent.SELECTED) {
				if(!canReaderBorrowBooks((String)readerNameComboBox.getSelectedItem())) {
					lendButton.setEnabled(false);
					addBookButton.setEnabled(false);
				} else {
					lendButton.setEnabled(true);
					addBookButton.setEnabled(true);
				}
			}
		}
	}
	
	private void initReaderNameComboBox() {
		try {
			Statement statement = mainFrame.connect.createStatement();
			ResultSet result = statement.executeQuery("SELECT * FROM Reader");
			while(result.next()) {
				if(isCardStillHealthy(result.getDate("ExpireDate").toString())) {
					readerNameComboBox.addItem(result.getString("Name"));
				}
			}
		}catch(SQLException ex) {
			JOptionPane.showMessageDialog(LendBookFrame.this, "SQL ERROR (initReaderNameComboBox)");
		}catch(ParseException ex) {
			JOptionPane.showMessageDialog(LendBookFrame.this, "Parse ERROR (initReaderNameComboBox)");
		}
	}

	
	private void initializeBookTitleComboBox() {
		try {
			Statement statement = mainFrame.connect.createStatement();
			ResultSet result = statement.executeQuery("SELECT * FROM Book");
			//only load book if they have quantity > borrowed
			while(result.next()) {
				int quantity = result.getInt("Quantity");
				int borrowed = result.getInt("Borrowed");
				if(quantity > borrowed)
					bookTitleComboBox.addItem(result.getString("Title"));
			}
		}catch(SQLException ex) {
			JOptionPane.showMessageDialog(LendBookFrame.this, "SQL ERROR (bookTitleComboBox)");
		}
	}
	
	//check if reader's card is healthy
	//if it's healthy, return true
	//otherwise, return false
	private boolean isCardStillHealthy(String eDate) throws ParseException{
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		java.util.Date date = new java.util.Date();
		DDate today = new DDate(dateFormat.format(date));
		DDate expireDate = new DDate(eDate);
		long compare = DDate.distanceBetweenDate(today, expireDate);
		System.out.println(compare);
		if(compare < 0) {
			return true;
		} 
		return false;
	}
	
	//check conditions to borrow book
	//debt below 10k (1)
	//and no borrowed books (2)
	private boolean canReaderBorrowBooks(String readerName) {
		try {
			Statement statement = mainFrame.connect.createStatement();
			ResultSet result = statement.executeQuery("SELECT * FROM BorrowedBooks");
			
			//check (1)
			while(result.next()) {
				if(result.getString("ReaderName").trim().equals(readerName)) {
					JOptionPane.showMessageDialog(LendBookFrame.this, readerName + " are not allowed to borrow book "
							+ "because he/she has borrowed book(s) before, or his/her debt is over 10.000");
					return false;
				}
			}
			
			//check (2);
			statement.close();
			result = null;
			statement = mainFrame.connect.createStatement();
			result = statement.executeQuery("SELECT * FROM Reader");
			while(result.next()) {
				if(result.getString("Name").trim().equals(readerName)) {
					float debt = new Float(result.getString("Debt").trim());
					if(debt > 10000.0) {
						JOptionPane.showMessageDialog(LendBookFrame.this, readerName + " are not allowed to borrow book "
								+ "because he/she has borrowed book(s) before, or his/her debt is over 10.000");
						return false;
					}
				}
			}
		}catch(SQLException ex) {
			JOptionPane.showMessageDialog(LendBookFrame.this, "SQL ERROR (canReaderBorrowBooks)");
		}
		return true;
	}
}
