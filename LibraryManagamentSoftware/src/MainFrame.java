import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.lang.model.element.QualifiedNameable;
import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.RowSorter;
import javax.swing.ScrollPaneConstants;
import javax.swing.SortOrder;
import javax.swing.SwingConstants;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;


public class MainFrame extends JFrame{
	protected Connection connect;
	private JMenuBar menuBar;
	private JMenu fileMenu;
	private JMenuItem exitMenuItem;
	private JMenuItem propertiesMenuItem;
	private JMenuItem reportDelayMenuItem;
	private JMenuItem reportCategoryMenuItem;
	private JMenu readerMenu;
	private JMenuItem registerMenuItem;
	private JMenuItem payBookMenuItem;
	private JMenuItem lendBookMenuItem;
	private JMenuItem payDebtMenuItem;
	private JMenuItem manageReaderMenuItem;
	private JMenu helpMenu;
	private JMenuItem qaMenuItem;
	private JMenuItem contactMenuItem;
	private JMenu languageMenu;
	private JMenuItem vietnameseMenuItem;
	private JMenuItem englishMenuItem;
	
	//login panel's stuff
	private JPanel loginPanel;
	protected JButton loginButton;
	protected JTextField usernameTextField;
	protected JPasswordField passwordTextField;
	private JLabel usernameLabel;
	private JLabel passwordLabel;
	private JLabel warningLabel;
	
	//panel contains books table
	private JPanel bookPanel;
	private JComboBox searchPublisherComboBox;
	private JComboBox searchYearComboBox; 
	private JComboBox searchCategoryComboBox;
	private JTextField searchNameTextField;
	private JButton searchButton;
	private JTable booksTable;
	protected DefaultTableModel booksTableModel;
	private JLabel searchPublisherLabel;
	private JLabel searchYearLabel;
	private JLabel searchCategoryLabel;
	private JLabel searchNameLabel;
	
	//panel contains books details info
	private JPanel bookDetailInfoPanel;
	private JLabel bookCoverLabel;
	private JButton bookCoverBrowseButton;
	private JLabel isbnLabel;
	private JTextField isbnTextField;
	private JLabel bookTitleLabel;
	private JTextField bookTitleTextField;
	private JLabel bookAuthorLabel;
	private JTextField bookAuthorTextField;
	private JLabel bookPublisherLabel;
	private JComboBox bookPublisherComboBox;
	private JLabel bookPublishYearLabel;
	private JTextField bookPublishYearTextField;
	private JLabel bookCategoryLabel;
	private JComboBox bookCategoryComboBox;
	private JLabel bookLanguageLabel;
	private JTextField bookLanguageTextField;
	private JLabel bookQuantityLabel;
	private JTextField bookQuantityTextField;
	private JLabel bookBorrowedLabel;
	private JTextField bookBorrowedTextField;
	private JLabel bookRatingLabel;
	private JSlider bookRatingSlider;
	private JLabel bookPageLabel;
	private JTextField bookPageTextField;
	private JLabel bookCostLabel;
	private JTextField bookCostTextField;
	private JLabel bookDescriptionLabel;
	private JTextArea bookDescriptionArea;
	private JLabel bookAddDateLabel;
	private JTextField bookAddDateTextField;
	private JLabel bookCodeLabel;
	private JTextField bookCodeTextField;
	
	private JButton addBookButton;
	private JButton updateBookButton;
	private JButton clearButton;
	
	public boolean isChanged = false;
	private String bookCoverImagePath;
	protected String language ="english";
	
	public MainFrame() throws Exception{
		super("Library Managament Sofware");
		setLayout(null);
		
		//connect to database
		connect = DriverManager.getConnection("jdbc:mysql://localhost:3306/Library?characterEncoding=utf8", "root","root");
		
		//initialize login panel
		initializeLoginPanel();
		//initialize book panel
		initializeBookPanel();
		//initialize book detail panel
		initializeBookDetailPanel();
		
		//create menu bar and add menu components to it
		//like file, view, help, ....
		menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		fileMenu = new JMenu("File");
		propertiesMenuItem = new JMenuItem("Properties");
		propertiesMenuItem.addActionListener(new PropertiesHandler());
		ReportHandler reportHandler = new ReportHandler();
		reportDelayMenuItem = new JMenuItem("Report Delay");
		reportDelayMenuItem.addActionListener(reportHandler);
		reportCategoryMenuItem = new JMenuItem("Report Cateogry");
		reportCategoryMenuItem.addActionListener(reportHandler);
		exitMenuItem = new JMenuItem("Exit");
		fileMenu.add(propertiesMenuItem);
		fileMenu.add(reportDelayMenuItem);
		fileMenu.add(reportCategoryMenuItem);
		fileMenu.addSeparator();
		fileMenu.add(exitMenuItem);
		menuBar.add(fileMenu);
		readerMenu = new JMenu("Reader");
		registerMenuItem = new JMenuItem("Register");
		payBookMenuItem = new JMenuItem("Pay Book");
		lendBookMenuItem = new JMenuItem("Lend Book");
		payDebtMenuItem = new JMenuItem("Pay Debt");
		manageReaderMenuItem = new JMenuItem("Manage Reader");
		ReaderHandler readerHandler = new ReaderHandler();
		registerMenuItem.addActionListener(readerHandler);
		payBookMenuItem.addActionListener(readerHandler);
		lendBookMenuItem.addActionListener(readerHandler);
		payDebtMenuItem.addActionListener(readerHandler);
		manageReaderMenuItem.addActionListener(readerHandler);
		readerMenu.add(registerMenuItem);
		readerMenu.add(payBookMenuItem);
		readerMenu.add(lendBookMenuItem);
		readerMenu.add(payDebtMenuItem);
		readerMenu.add(manageReaderMenuItem);
		menuBar.add(readerMenu);
		helpMenu = new JMenu("Help");
		qaMenuItem = new JMenuItem("Q&A");
		contactMenuItem = new JMenuItem("Contact Info");
		helpMenu.add(qaMenuItem);
		helpMenu.add(contactMenuItem);
		menuBar.add(helpMenu);
		//make things invisible
		//until login succeed
		languageMenu = new JMenu("Language");
		menuBar.add(languageMenu);
		vietnameseMenuItem = new JMenuItem("Vietnamese");
		vietnameseMenuItem.addActionListener(new VietnameseLanguageHandler());
		languageMenu.add(vietnameseMenuItem);
		englishMenuItem = new JMenuItem("English");
		languageMenu.add(englishMenuItem);
		englishMenuItem.addActionListener(new EnglishLanguageHandler());
		menuBar.setVisible(false);
	
		
		
		setSize(1000,700);
		setVisible(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		setResizable(false);
		
	}
	
	//handle when user choose vietnamese
	private class VietnameseLanguageHandler implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent event) {
			language = "vietnamese";
			
			readerMenu.setText("Độc Giả");
			registerMenuItem.setText("Đăng Ký");
			payBookMenuItem.setText("Trả Sách");
			lendBookMenuItem.setText("Cho Mượn Sách");
			payDebtMenuItem.setText("Trả Nợ");
			manageReaderMenuItem.setText("Quản Lý Độc Giả");
			
			fileMenu.setText("Tệp");
			propertiesMenuItem.setText("Thuộc Tính");
			reportDelayMenuItem.setText("Báo Cáo Sách Trả Trễ");
			reportCategoryMenuItem.setText("Báo Cáo Sách Theo Thể Loại");
			
			helpMenu.setText("Giúp Đỡ");
			qaMenuItem.setText("Hỏi&Đáp");
			contactMenuItem.setText("Liên Lạc");
			
			languageMenu.setText("Ngôn Ngữ");
			vietnameseMenuItem.setText("Tiếng Việt");
			englishMenuItem.setText("Tiếng Anh");
			
			searchPublisherLabel.setText("NXB");
			searchYearLabel.setText("Năm");
			searchCategoryLabel.setText("Thể Loại");
			searchNameLabel.setText("Tên Sách");
			searchButton.setText("Tìm");
			
			booksTable.getColumnModel().getColumn(0).setHeaderValue("Tên Sách");
			booksTable.getColumnModel().getColumn(1).setHeaderValue("Tác Giả");
			booksTable.getColumnModel().getColumn(2).setHeaderValue("Năm");
			booksTable.getColumnModel().getColumn(3).setHeaderValue("NXB");
			booksTable.getColumnModel().getColumn(4).setHeaderValue("Đánh Giá");
			booksTable.repaint();
			
			bookCoverLabel.setText("Bìa");
			bookTitleLabel.setText("Tên Sách");
			bookAuthorLabel.setText("Tác Giả");
			bookPublisherLabel.setText("NXB");
			bookPublishYearLabel.setText("Năm");
			bookCategoryLabel.setText("Thể Loại");
			bookLanguageLabel.setText("Ngôn Ngữ");
			bookQuantityLabel.setText("Số Lượng");
			bookBorrowedLabel.setText("Đã Mượn");
			bookAddDateLabel.setText("Ngày Nhập");
			bookCodeLabel.setText("Mã Sách");
			bookRatingLabel.setText("Xếp Hạng");
			bookPageLabel.setText("Số Trang");
			bookCostLabel.setText("Giá");
			bookDescriptionLabel.setText("Mô Tả");
			addBookButton.setText("Thêm");
			updateBookButton.setText("Thay Đổi");
			clearButton.setText("Dẹp");
			
			setTitle("Phần Mềm Quản Lý Thư Viện");
		}
	}
	
	//handle when user choose english
	private class EnglishLanguageHandler implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent event ) {
			language = "english";
			
			readerMenu.setText("Reader");
			registerMenuItem.setText("Register");
			payBookMenuItem.setText("Pay Book");
			lendBookMenuItem.setText("Lend Book");
			payDebtMenuItem.setText("Pay Debt");
			manageReaderMenuItem.setText("Manage Reader");
			
			fileMenu.setText("File");
			propertiesMenuItem.setText("Properties");
			reportDelayMenuItem.setText("Report Delay");
			reportCategoryMenuItem.setText("Report Category");
			
			helpMenu.setText("Help");
			qaMenuItem.setText("Q&A");
			contactMenuItem.setText("Contact Info");
			
			languageMenu.setText("Language");
			vietnameseMenuItem.setText("Vietnamese");
			englishMenuItem.setText("English");
			
			searchPublisherLabel.setText("Publisher");
			searchYearLabel.setText("Publish Year");
			searchCategoryLabel.setText("Book Category");
			searchNameLabel.setText("Book Name");
			searchButton.setText("Search");
			
			booksTable.getColumnModel().getColumn(0).setHeaderValue("Title");
			booksTable.getColumnModel().getColumn(1).setHeaderValue("Author");
			booksTable.getColumnModel().getColumn(2).setHeaderValue("Year");
			booksTable.getColumnModel().getColumn(3).setHeaderValue("Publisher");
			booksTable.getColumnModel().getColumn(4).setHeaderValue("Rating");
			booksTable.repaint();
			
			bookCoverLabel.setText("Cover");
			bookTitleLabel.setText("Title");
			bookAuthorLabel.setText("Author");
			bookPublisherLabel.setText("Publisher");
			bookPublishYearLabel.setText("Year");
			bookCategoryLabel.setText("Category");
			bookLanguageLabel.setText("Language");
			bookQuantityLabel.setText("Quantity");
			bookBorrowedLabel.setText("Borrowed");
			bookAddDateLabel.setText("Add Date");
			bookCodeLabel.setText("Code");
			bookRatingLabel.setText("Rating");
			bookPageLabel.setText("Pages");
			bookCostLabel.setText("Cost");
			bookDescriptionLabel.setText("Description");
			addBookButton.setText("Add");
			updateBookButton.setText("Update");
			clearButton.setText("Clear");
			
			setTitle("Library Management Software");
		}
	}
	
	//code for login handling
	//when click on login button
	//all things in here will run ..run run run run
	private class LoginHandler implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent event) {
			try {
				//get user input
				String usernameInput = usernameTextField.getText();
				String passwordInput = passwordTextField.getText();
	
				//create a statement and 
				//use it to execute sql query
				Statement query = connect.createStatement();
				ResultSet result = query.executeQuery("SELECT * FROM User");
				boolean accessSucceed = false;
				
				while(result.next()) {
					//get username and password of result
					//after running the query above
					String usernameInDataBase = result.getString("Username");
					String passwordInDataBase = result.getString("Password");
					System.out.println(usernameInDataBase + " " + passwordInDataBase);
					if(usernameInDataBase.equals(usernameInput) 
							&& passwordInDataBase.equals(passwordInput)) {
						//login succeed
						JOptionPane.showMessageDialog(MainFrame.this, "LOGIN SUCCEED", "WELCOME", JOptionPane.PLAIN_MESSAGE);
						//make window's components
						//visible again and make login panel disappear
						loginPanel.setVisible(false);
						loginPanel = null; //release object to save memory
						makeWindowComponentsAppear();
						accessSucceed = true;
						break;
					} 
				} 
				
				if(!accessSucceed)
					JOptionPane.showMessageDialog(MainFrame.this, "Wrong Username or Password. Try again", "Sorry", JOptionPane.ERROR_MESSAGE);
			
			}catch(SQLException ex) {
				JOptionPane.showMessageDialog(MainFrame.this, "Server Error", "Error", JOptionPane.ERROR_MESSAGE);
			}
			
		}
	}
	
	//handle when user click properties menu item
	private class PropertiesHandler implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			new PropertiesFrame(MainFrame.this);
			System.out.println(isChanged);
		}
	}
	
	//handle when user click on each report item 
	// (include 3 report items)
	private class ReportHandler implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent event) {
			if(event.getSource() == reportDelayMenuItem) {
				new ReportDelayFrame(MainFrame.this);
			} else if(event.getSource() == reportCategoryMenuItem) {
				new ReportCategoryFrame(MainFrame.this);
			}
		}
	}
	
	//handle when user click on each item in Reader menu
	private class ReaderHandler implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent event) {
			if(event.getSource() == registerMenuItem) {
				new RegisterFrame(MainFrame.this);
			} else if(event.getSource() == payBookMenuItem ) {
				new PayBookFrame(MainFrame.this);
			} else if(event.getSource() == lendBookMenuItem) {
				new LendBookFrame(MainFrame.this);
			} else if(event.getSource() == payDebtMenuItem) {
				new PayDebtFrame(MainFrame.this);
			} else if(event.getSource() == manageReaderMenuItem) {
				new ManageReaderFrame(MainFrame.this);
			}
		} 
	}
	
	
	//handle when user click on a book in book table
	//get info from server, this play in book detail panel below
	private class BookSelectHandler implements ListSelectionListener {
		@Override
		public void valueChanged(ListSelectionEvent event) {
			int row = booksTable.getSelectedRow();
			String bookName = (String)booksTable.getValueAt(row, 0);
			try {
				java.sql.PreparedStatement query = connect.prepareStatement("SELECT * FROM Book WHERE Title = ?");
				query.setString(1, bookName);
				ResultSet result = query.executeQuery();
				while(result.next()) {
					bookTitleTextField.setText(result.getString("Title").trim());
					bookTitleTextField.setCaretPosition(0);
					bookAuthorTextField.setText(result.getString("Author").trim());
					bookAuthorTextField.setCaretPosition(0);
					isbnTextField.setText(result.getString("ISBN").trim());
					bookPageTextField.setText(String.valueOf(result.getInt("Pages")).trim());
					bookPublishYearTextField.setText(String.valueOf(result.getInt("PublishYear")).trim());
					bookDescriptionArea.setText(result.getString("Description").trim());
					bookLanguageTextField.setText(result.getString("Language").trim());
					bookQuantityTextField.setText(String.valueOf(result.getInt("Quantity")).trim());
					bookBorrowedTextField.setText(String.valueOf(result.getInt("Borrowed")).trim());
					bookCodeTextField.setText(result.getString("Code").trim());
					bookCostTextField.setText(result.getString("Cost").trim());
					Date date = result.getDate("AddDate");
					bookAddDateTextField.setText(date.toString());
					System.out.println(bookAddDateTextField.getText());
					bookAddDateTextField.setCaretPosition(0);
					bookRatingSlider.setValue((int)Math.round(Float.parseFloat(result.getString("Rating").trim())));
					bookCoverImagePath = result.getString("Cover");
					//set book cover picture
					//read picture
					BufferedImage img = null; 
					try {
						img = ImageIO.read(new File(result.getString("Cover").trim()));
					}catch(IOException | NullPointerException ex) {
						JOptionPane.showMessageDialog(MainFrame.this, "Book Cover Image not found");
						System.out.println(ex.getMessage());
					}
					//resize picture
					if(img != null) {
						Image resizedImg = img.getScaledInstance(bookCoverLabel.getWidth(), bookCoverLabel.getHeight()-10, Image.SCALE_SMOOTH);
						//create image icon
						ImageIcon icon = new ImageIcon(resizedImg);
						bookCoverLabel.setIcon(icon);
					} else { 
						//load image broken if cannot load book cover image
						try  {
							setBrokenImage();
						}catch(IOException | NullPointerException ex) {
							JOptionPane.showMessageDialog(MainFrame.this, "Error : Cannot load broken image. "
									+ "Broken image should be at (Linux OS): /Users/mrnoctv/Documents/workspace/LibraryManagamentSoftware/ImageBroken.png"
									+ "\nWindows : C:\\Somewhere =]]");
						}
						
					}
					
					bookPublisherComboBox.setSelectedItem(result.getString("Publisher").trim());
					bookCategoryComboBox.setSelectedItem(result.getString("Category").trim());
					System.out.println(result.getString("Cover").trim());
				}
			}catch(SQLException ex) {
				JOptionPane.showMessageDialog(MainFrame.this, "SQL SERVER ERROR");
			}
		}
	}
	
	//handle when user click on browse button
	private class BrowseHandler implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent event) {
			JFileChooser fileChooser = new JFileChooser();
			fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
			int choice = fileChooser.showOpenDialog(MainFrame.this);
			if(choice == JFileChooser.APPROVE_OPTION) {
				File file = fileChooser.getSelectedFile();
				if(file.exists()) {
					//check if file is a png or jpeg image
					if(file.getAbsolutePath().endsWith("png") || file.getAbsolutePath().endsWith("jpeg") || file.getAbsolutePath().endsWith("jpg")) {
						try {
							BufferedImage img = ImageIO.read(file);
							Image resizeImage = img.getScaledInstance(bookCoverLabel.getWidth(), bookCoverLabel.getHeight()-15, Image.SCALE_SMOOTH);
							bookCoverLabel.setIcon(new ImageIcon(resizeImage));
							bookCoverImagePath = file.getAbsolutePath();
						}catch(IOException | NullPointerException ex) {
							JOptionPane.showMessageDialog(MainFrame.this, "Check file format again.");
						}
					} else {
						JOptionPane.showMessageDialog(MainFrame.this, "Check file format again.");
					}
				}
			} //end if choice
			
		}
	}
	
	//handle when user click on add button 
	//check info and then add book to database
	private class AddBookHandler implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent event ) {
			// check if book is already in database 
			//if it is, add more of it. 
			//if not, create new book 
			String bookTitle = bookTitleTextField.getText();
			if(!bookTitle.equals("")) {
				try {
					PreparedStatement statement = connect.prepareStatement("SELECT * FROM Book WHERE Title=?");
					statement.setString(1, bookTitle);
					ResultSet result = statement.executeQuery();
					//do not found book in database
					if(!result.next()) {
						int choice = JOptionPane.showConfirmDialog(MainFrame.this, "Do you want to add new book?");
						if(choice == JOptionPane.YES_OPTION) {
							//check user input data
							if(validateBookInfo()) {
								Statement s = connect.createStatement();
								s.execute("SET FOREIGN_KEY_CHECKS=0");
								statement = connect.prepareStatement("INSERT INTO Book VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
								statement.setString(1, bookTitleTextField.getText().trim());
								statement.setString(2, bookAuthorTextField.getText().trim());
								statement.setInt(3, new Integer(bookPublishYearTextField.getText()));
								statement.setString(4, (String)bookPublisherComboBox.getSelectedItem());
								statement.setString(5, bookDescriptionArea.getText().trim());
								statement.setInt(6, new Integer(bookPageTextField.getText()));
								statement.setString(7, bookCostTextField.getText().trim());
								statement.setString(8, String.valueOf(bookRatingSlider.getValue()));
								statement.setString(9, isbnTextField.getText().trim());
								statement.setString(10, (String)bookCategoryComboBox.getSelectedItem());
								statement.setString(11, bookAddDateTextField.getText());
								statement.setString(12, bookLanguageTextField.getText().trim());
								statement.setInt(13, new Integer(bookQuantityTextField.getText()));
								statement.setInt(14, new Integer(bookBorrowedTextField.getText()));
								statement.setString(15, bookCodeTextField.getText().trim());
								statement.setString(16, bookCoverImagePath);
								statement.executeUpdate();
								//update table
								updateBookTable();
							}
							
						}
						
					} else {
						//found book in database
						if(result.getString("Title").trim().equals(bookTitle.trim())) {
								//check user again if they want to add more this type of book
								int choice = JOptionPane.showConfirmDialog(MainFrame.this, "Are you sure to add more " + bookQuantityTextField.getText() + " " + bookTitle + " books");
								if(choice == JOptionPane.YES_OPTION) {
									if(validateBookInfo()) {
										int quantity = new Integer(bookQuantityTextField.getText());
										int currentQuantity = result.getInt("Quantity");
										String title = result.getString("Title");
										quantity += currentQuantity;
										statement = connect.prepareStatement("UPDATE Book SET Quantity=? WHERE Title=?");
										statement.setInt(1, quantity);
										statement.setString(2, title);
										statement.executeUpdate();
										JOptionPane.showMessageDialog(MainFrame.this, "Add more books successfully");
									}
								}
								
								
						}
					}
					
					
				}catch(SQLException ex) {
					JOptionPane.showMessageDialog(MainFrame.this, "SQL Error, please try again.");
					ex.printStackTrace();
				} catch(Exception ex) {
					JOptionPane.showMessageDialog(MainFrame.this, "Some data is not valid. Check Q&A and try again.");
				}
			} else {
				JOptionPane.showMessageDialog(MainFrame.this, "Book title is empty. \n"
						+ "You must make sure all the info here is valid. We are not responsible for stupid mistake you made");
			}
			
		}
		
		//call this method after updating or adding book
		private void updateBookTable() {
			String title = bookTitleTextField.getText();
			String author = bookAuthorTextField.getText();
			String year  = bookPublishYearTextField.getText();
			String publisher = (String)bookPublisherComboBox.getSelectedItem();
			String rating = String.valueOf(bookRatingSlider.getValue());
			booksTableModel.addRow(new Object[]{title,author,year,publisher,rating});
		}
		

	}
	
	private class ClearButtonHandler implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent event ) {
			isbnTextField.setText("");
			bookTitleTextField.setText("");
			bookAuthorTextField.setText("");
			bookPublishYearTextField.setText("");
			bookLanguageTextField.setText("");
			bookQuantityTextField.setText("");
			bookAddDateTextField.setText("yyyy-mm-dd");
			bookCodeTextField.setText("");
			bookRatingSlider.setValue(0);
			bookBorrowedTextField.setText("0");
			bookPageTextField.setText("");
			bookCostTextField.setText("xx.xx");
			bookDescriptionArea.setText("");
			try {
				setBrokenImage();
			}catch(IOException ex) {
				JOptionPane.showMessageDialog(MainFrame.this, "Some Error with book cover image");
			}
		}
	}
	
	private class UpdateButtonHandler implements ActionListener {
		@Override 
		public void actionPerformed(ActionEvent event) {
			try {
				if(!validateBookInfo())
					throw new Exception();
			
					
					Statement s = connect.createStatement();
					
					s.execute("SET FOREIGN_KEY_CHECKS=0");

					PreparedStatement	statement = connect.prepareStatement("UPDATE Book "
								+ "SET Author=?, PublishYear=?, Publisher=?, Description=?, "
								+ "Pages=?, Cost=?, Rating=?, ISBN=?, Category=?, "
								+ "AddDate=?, Language=?, Quantity=?, Code=?, Cover=? "
								+ "WHERE Title=?");
						statement.setString(1, bookAuthorTextField.getText().trim());
						statement.setInt(2, new Integer(bookPublishYearTextField.getText()));
						statement.setString(3, (String)bookPublisherComboBox.getSelectedItem());
						statement.setString(4, bookDescriptionArea.getText().trim());
						statement.setInt(5, new Integer(bookPageTextField.getText()));
						statement.setString(6, bookCostTextField.getText().trim());
						statement.setString(7, String.valueOf(bookRatingSlider.getValue()));
						statement.setString(8, isbnTextField.getText().trim());
						statement.setString(9, (String)bookCategoryComboBox.getSelectedItem());
						statement.setString(10, bookAddDateTextField.getText());
						statement.setString(11, bookLanguageTextField.getText().trim());
						statement.setInt(12, new Integer(bookQuantityTextField.getText()));
						statement.setString(13, bookCodeTextField.getText().trim());
						statement.setString(14, bookCoverImagePath);
						statement.setString(15, bookTitleTextField.getText());
						int result = statement.executeUpdate();
					
						if(result == 1) {
							updateBookTable(bookTitleTextField.getText(), bookAuthorTextField.getText(), bookPublishYearTextField.getText(), (String)bookPublisherComboBox.getSelectedItem(), String.valueOf(bookRatingSlider.getValue()));
							JOptionPane.showMessageDialog(MainFrame.this, "Update Successfully");
						}
						else 
							JOptionPane.showMessageDialog(MainFrame.this, "Cannot change book title.\n"
									+ "Check Q&A for more details.");
					
				
			}catch(Exception ex) {
				if(ex instanceof SQLException) {
					JOptionPane.showMessageDialog(MainFrame.this, "SQL Error (UpdateButtonHandler) ");
					ex.printStackTrace();
				}
			}
		}
		
		private void updateBookTable(String title, String author, String year, String publisher, String rating) {
			for(int i = 0; i < booksTable.getRowCount(); ++i) {
				String t = (String)booksTable.getValueAt(i, 0);
				if(t.equals(title)) {
					booksTable.setValueAt(author, i, 1);
					booksTable.setValueAt(year, i, 2);
					booksTable.setValueAt(publisher, i, 3);
					booksTable.setValueAt(rating, i, 4);
				}
			}
		} //end update
		
	}
	
	//handle when user click on search button
	private class SearchButtonHandler implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent event) {
			try {
				Statement statement = connect.createStatement();
				ResultSet result = statement.executeQuery("SELECT * FROM Book");
				ArrayList<String> results = new ArrayList<>();
				while(result.next()) {
					String title = result.getString("Title");
					String publisher = result.getString("Publisher");
					String category = result.getString("Category");
					String publishYear = String.valueOf(result.getInt("PublishYear"));
					String rating = result.getString("Rating");
					
					if(!isNameOff()) {
						if(title.contains(searchNameTextField.getText())) {
							results.add(title);
						}
					} else {
						String p = (String)searchPublisherComboBox.getSelectedItem(); //publisher of searchPublisherComboBox
						String y = String.valueOf(searchYearComboBox.getSelectedItem()); //year of searchYearComboBox
						String c = (String)searchCategoryComboBox.getSelectedItem(); //category of searchCategoryComboBox
						if(publisher.equals(p) || publishYear.equals(y) || category.equals(c)) {
							results.add(title);
						}
					}
					
					
					
				}
				new SearchResultFrame(MainFrame.this, results, connect);
			}catch(Exception ex) {
				if(ex instanceof SQLException)
					JOptionPane.showMessageDialog(MainFrame.this, "SQL ERROR (SearchButtonHandler)");
				else 
					JOptionPane.showMessageDialog(MainFrame.this, ex.getMessage());
			}
		}
		
		private boolean isCategoryOff() {
			return searchCategoryComboBox.getSelectedItem().equals("Off");
		}
		
		private boolean isPublishYearOff() {
			return searchYearComboBox.getSelectedItem().equals("Off");
		}
		
		private boolean isPublisherOff() {
			return searchPublisherComboBox.getSelectedItem().equals("Off");
		}
		
		private boolean isNameOff() {
			return searchNameTextField.getText().trim().equals("");
		}
	}
	
	//code to make window component
	//appear after user login succeed
	private void makeWindowComponentsAppear() {
		menuBar.setVisible(true);
		bookPanel.setVisible(true);
		bookDetailInfoPanel.setVisible(true);
	}
	
	//code to initialize components in login panel
	private void initializeLoginPanel() {
		//add login panel to main window
		//set login handling event to login button and passwordTextField
		loginPanel = new JPanel();
		loginPanel.setSize(300, 200);
		loginPanel.setLocation(350, 300);
		loginPanel.setVisible(true);
		add(loginPanel);
		loginPanel.setLayout(null);
		loginPanel.setBorder(BorderFactory.createTitledBorder(new LineBorder(Color.BLACK), "WELCOME", 
				TitledBorder.CENTER,TitledBorder.TOP, new Font("Serif", Font.BOLD, 25), new Color(26, 188, 156)));
		usernameLabel = new JLabel("Username: ", SwingConstants.RIGHT);
		usernameLabel.setSize(120, 30);
		usernameLabel.setLocation(10, 40);
		usernameLabel.setFont(new Font("Serif", Font.BOLD, 20));
		loginPanel.add(usernameLabel);
		usernameTextField = new JTextField();
		usernameTextField.setSize(120, 23);
		usernameTextField.setLocation(140, 43);
		loginPanel.add(usernameTextField);
		passwordLabel = new JLabel("Password: ", SwingConstants.RIGHT);
		passwordLabel.setSize(120, 30);
		passwordLabel.setLocation(10, 80);
		passwordLabel.setFont(new Font("Serif", Font.BOLD, 20));
		loginPanel.add(passwordLabel);
		passwordTextField = new JPasswordField();
		passwordTextField.setSize(120, 23);
		passwordTextField.setLocation(140, 83);
		loginPanel.add(passwordTextField);
		loginButton = new JButton("LOGIN");
		loginButton.setSize(100, 20);
		loginButton.setLocation(100, 120);
		loginPanel.add(loginButton);
		warningLabel = new JLabel("*You must login to continue!");
		warningLabel.setFont(new Font("Serif", Font.BOLD, 20));
		warningLabel.setForeground(new Color(231, 76, 60));
		warningLabel.setSize(260, 20);
		warningLabel.setLocation(20, 160);
		loginPanel.add(warningLabel);
		LoginHandler loginHandler = new LoginHandler();
		loginButton.addActionListener(loginHandler);
		passwordTextField.addActionListener(loginHandler);
		
	}
	//code to initialize components in book panel
	private void initializeBookPanel() {
		bookPanel = new JPanel();
		bookPanel.setLayout(null);
		bookPanel.setSize(990, 400);
		bookPanel.setLocation(3, 5);
		add(bookPanel);
		
		JPanel searchPanel = new JPanel();
		searchPanel.setLayout(null);
		searchPublisherComboBox = new JComboBox<>();
		searchPublisherComboBox.setMaximumRowCount(3);
		searchPublisherComboBox.setSize(190, 20);
		searchPublisherComboBox.setLocation(5,30);
		searchPublisherComboBox.addItem("Off");
		try {
			Statement statement = connect.createStatement();
			ResultSet result = statement.executeQuery("SELECT * FROM Publisher");
			while(result.next()) {
				searchPublisherComboBox.addItem(result.getString("Publisher"));
			}
		}catch(SQLException ex) {
			JOptionPane.showMessageDialog(MainFrame.this, "SQL ERROR (searchPublisherComboBox)");
		}
		searchPublisherLabel = new JLabel("Publisher:");
		searchPublisherLabel.setSize(190, 20);
		searchPublisherLabel.setLocation(8, 5);
		searchPanel.add(searchPublisherLabel);
		searchPanel.add(searchPublisherComboBox);
		searchYearComboBox = new JComboBox<>();
		searchYearComboBox.setMaximumRowCount(3);
		searchYearComboBox.setSize(190, 20);
		searchYearComboBox.setLocation(205,30);
		searchYearComboBox.addItem("Off");
		//from 1990 - 2020
		for(int i = 0; i <= 30; ++i) {
			searchYearComboBox.addItem(1990+i);
		}
		searchYearLabel = new JLabel("Publish Year:");
		searchYearLabel.setSize(190, 20);
		searchYearLabel.setLocation(208,5);
		searchPanel.add(searchYearLabel);
		searchPanel.add(searchYearComboBox);
		searchCategoryComboBox = new JComboBox<>();
		searchCategoryComboBox.setMaximumRowCount(3);
		searchCategoryComboBox.setSize(190, 20);
		searchCategoryComboBox.setLocation(405, 30);
		searchCategoryComboBox.addItem("Off");
		try {
			Statement statement = connect.createStatement();
			ResultSet result = statement.executeQuery("SELECT * FROM Category");
			while(result.next()) {
				searchCategoryComboBox.addItem(result.getString("Category"));
			}
		}catch(SQLException ex) {
			JOptionPane.showMessageDialog(MainFrame.this, "SQL ERROR (searchCategoryComboBox)");
		}
		
		searchCategoryLabel = new JLabel("Book Category:");
		searchCategoryLabel.setSize(190, 20);
		searchCategoryLabel.setLocation(408,5);
		searchPanel.add(searchCategoryLabel);
		searchPanel.add(searchCategoryComboBox);
		searchNameTextField = new JTextField();
		searchNameTextField.setSize(190, 20);
		searchNameTextField.setLocation(605, 30);
		searchNameLabel = new JLabel("Book Name:");
		searchNameLabel.setSize(190, 20);
		searchNameLabel.setLocation(608, 5);
		searchPanel.add(searchNameLabel);
		searchPanel.add(searchNameTextField);
		searchButton = new JButton("Search");
		searchButton.setSize(180, 20);
		searchButton.setLocation(805, 30);
		searchButton.addActionListener(new SearchButtonHandler());
		searchPanel.add(searchButton);
		searchPanel.setSize(990, 55);
		searchPanel.setLocation(0, 0);
		searchPanel.setBorder(BorderFactory.createLoweredBevelBorder());
		bookPanel.add(searchPanel);
		
		booksTable = new JTable(new DefaultTableModel(new Object[]{"Title","Author","Year","Publisher","Rating"}, 0)) {
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};
		booksTableModel = (DefaultTableModel) booksTable.getModel();
		booksTable.setFillsViewportHeight(true);
		booksTable.setGridColor(Color.BLACK);
		booksTable.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
			@Override
			 public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column)
		    {
		        final Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
		        c.setBackground(row % 2 == 0 ? Color.LIGHT_GRAY : Color.WHITE);
		        return c;
		    }
		});
		//add list selection listener for book table
		ListSelectionModel listModel = booksTable.getSelectionModel();
		listModel.addListSelectionListener(new BookSelectHandler());
		
		//load data from server to book table.
		try {
			Statement query = connect.createStatement();
			ResultSet result = query.executeQuery("SELECT * FROM Book");
			
			while(result.next()) {
				booksTableModel.addRow(new Object[]{result.getString("Title"), result.getString("Author"), result.getInt("PublishYear"), result.getString("Publisher"), result.getString("Rating")});
			}
			
		}catch(SQLException ex) {
			JOptionPane.showMessageDialog(MainFrame.this, "SQL Error");
			ex.printStackTrace();
		}
		
		JScrollPane scrollPane = new JScrollPane(booksTable);
		scrollPane.setSize(990, 340);
		scrollPane.setLocation(1, 58);
		scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		scrollPane.setBorder(BorderFactory.createLoweredBevelBorder());
		for(int i = 0; i < booksTable.getColumnCount(); ++i) {
			TableColumn column = booksTable.getColumnModel().getColumn(i);
			if(i == 0) {
				column.setPreferredWidth(250);
			}else if(i == 1) {
				column.setPreferredWidth(200);
			} else if(i == 2) {
				column.setPreferredWidth(40);
			} else if(i == 3) {
				column.setPreferredWidth(250);
			}
		}
		booksTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		booksTable.setSelectionForeground(Color.BLACK);
		bookPanel.add(scrollPane);
		bookPanel.setVisible(false);
	
	} 
	
	//code to initialize book detail panel
	private void initializeBookDetailPanel() {
		bookDetailInfoPanel = new JPanel();
		bookDetailInfoPanel.setLayout(null);
		bookDetailInfoPanel.setSize(990, 245);
		bookDetailInfoPanel.setLocation(3, 407);
		bookDetailInfoPanel.setBorder(BorderFactory.createLoweredBevelBorder());
		add(bookDetailInfoPanel);
		
		bookCoverLabel = new JLabel();
		bookCoverLabel.setBorder(BorderFactory.createTitledBorder(new LineBorder(Color.BLACK), "Cover", TitledBorder.LEFT, TitledBorder.ABOVE_TOP));
		bookCoverLabel.setSize(150, 197);
		bookCoverLabel.setLocation(5, 5);
		bookDetailInfoPanel.add(bookCoverLabel);
		bookCoverBrowseButton = new JButton("Browse");
		bookCoverBrowseButton.setSize(100, 20);
		bookCoverBrowseButton.setLocation(25, 210);
		bookCoverBrowseButton.addActionListener(new BrowseHandler());
		bookDetailInfoPanel.add(bookCoverBrowseButton);
		isbnLabel = new JLabel("ISBN");
		isbnLabel.setSize(100, 20);
		isbnLabel.setLocation(170, 5);
		bookDetailInfoPanel.add(isbnLabel);
		isbnTextField = new JTextField();
		isbnTextField.setSize(170, 20);
		isbnTextField.setLocation(170,30);
		bookDetailInfoPanel.add(isbnTextField);
		bookTitleLabel = new JLabel("Title");
		bookTitleLabel.setSize(200, 20);
		bookTitleLabel.setLocation(170, 55);
		bookDetailInfoPanel.add(bookTitleLabel);
		bookTitleTextField = new JTextField();
		bookTitleTextField.setSize(216, 20);
		bookTitleTextField.setLocation(170, 80);
		bookDetailInfoPanel.add(bookTitleTextField);
		bookAuthorLabel = new JLabel("Author");
		bookAuthorLabel.setSize(200, 20);
		bookAuthorLabel.setLocation(170,105);
		bookDetailInfoPanel.add(bookAuthorLabel);
		bookAuthorTextField = new JTextField();
		bookAuthorTextField.setSize(216,20);
		bookAuthorTextField.setLocation(170, 130);
		bookDetailInfoPanel.add(bookAuthorTextField);
		bookPublisherLabel = new JLabel("Publisher");
		bookPublisherLabel.setSize(160, 20);
		bookPublisherLabel.setLocation(170, 155);
		bookDetailInfoPanel.add(bookPublisherLabel);
		bookPublisherComboBox = new JComboBox<>();
		bookPublisherComboBox.setSize(150, 20);
		bookPublisherComboBox.setMaximumRowCount(3);
		bookPublisherComboBox.setLocation(170, 180);
		try {
			Statement statement = connect.createStatement();
			ResultSet result = statement.executeQuery("SELECT * FROM Publisher");
			while(result.next()) {
				bookPublisherComboBox.addItem(result.getString("Publisher").trim());
			}
		}catch(SQLException ex) {
			JOptionPane.showMessageDialog(MainFrame.this, "SQL Error, Could not read publisher data");
		}
		bookDetailInfoPanel.add(bookPublisherComboBox);
		bookPublishYearLabel = new JLabel("Year");
		bookPublishYearLabel.setSize(100,20);
		bookPublishYearLabel.setLocation(325, 155);
		bookDetailInfoPanel.add(bookPublishYearLabel);
		bookPublishYearTextField = new JTextField();
		bookPublishYearTextField.setSize(60, 20);
		bookPublishYearTextField.setLocation(325, 180);
		bookDetailInfoPanel.add(bookPublishYearTextField);
		bookCategoryLabel = new JLabel("Category");
		bookCategoryLabel.setSize(100, 20);
		bookCategoryLabel.setLocation(400, 5);
		bookDetailInfoPanel.add(bookCategoryLabel);
		bookCategoryComboBox = new JComboBox<>();
		bookCategoryComboBox.setSize(100, 20);
		bookCategoryComboBox.setLocation(397, 30);
		try {
			Statement statement = connect.createStatement();
			ResultSet result = statement.executeQuery("SELECT * FROM Category");
			while(result.next()) {
				bookCategoryComboBox.addItem(result.getString("Category").trim());
			}
		}catch(SQLException ex) {
			JOptionPane.showMessageDialog(MainFrame.this, "SQL Error: Could not read book categories data");
		}
		bookDetailInfoPanel.add(bookCategoryComboBox);
		bookLanguageLabel = new JLabel("Language");
		bookLanguageLabel.setSize(100,20);
		bookLanguageLabel.setLocation(400,55);
		bookDetailInfoPanel.add(bookLanguageLabel);
		bookLanguageTextField = new JTextField();
		bookLanguageTextField.setSize(100, 20);
		bookLanguageTextField.setLocation(397,80);
		bookDetailInfoPanel.add(bookLanguageTextField);
		bookQuantityLabel = new JLabel("Quantity");
		bookQuantityLabel.setSize(100,20);
		bookQuantityLabel.setLocation(400, 105);
		bookDetailInfoPanel.add(bookQuantityLabel);
		bookQuantityTextField = new JTextField();
		bookQuantityTextField.setSize(100,20);
		bookQuantityTextField.setLocation(397, 130);
		bookDetailInfoPanel.add(bookQuantityTextField);
		bookBorrowedLabel = new JLabel("Borrowed");
		bookBorrowedLabel.setSize(100,20);
		bookBorrowedLabel.setLocation(400,155);
		bookDetailInfoPanel.add(bookBorrowedLabel);
		bookBorrowedTextField = new JTextField();
		bookBorrowedTextField.setSize(100,20);
		bookBorrowedTextField.setLocation(397,180);
		bookBorrowedTextField.setEditable(false);
		bookDetailInfoPanel.add(bookBorrowedTextField);
		bookAddDateLabel = new JLabel("Add Date");
		bookAddDateLabel.setSize(90, 20);
		bookAddDateLabel.setLocation(505,5);
		bookDetailInfoPanel.add(bookAddDateLabel);
		bookAddDateTextField = new JTextField();
		bookAddDateTextField.setSize(90,20);
		bookAddDateTextField.setLocation(502,30);
		bookDetailInfoPanel.add(bookAddDateTextField);
		bookCodeLabel = new JLabel("Code");
		bookCodeLabel.setSize(90,20);
		bookCodeLabel.setLocation(600,5);
		bookDetailInfoPanel.add(bookCodeLabel);
		bookCodeTextField = new JTextField();
		bookCodeTextField.setSize(90, 20);
		bookCodeTextField.setLocation(598,30);
		bookDetailInfoPanel.add(bookCodeTextField);
		bookRatingLabel = new JLabel("Rating");
		bookRatingLabel.setSize(100,20);
		bookRatingLabel.setLocation(505, 55);
		bookDetailInfoPanel.add(bookRatingLabel);
		bookRatingSlider = new JSlider(SwingConstants.HORIZONTAL, 0, 5, 0);
		bookRatingSlider.setMajorTickSpacing(1);
		bookRatingSlider.setPaintTicks(true);
		bookRatingSlider.setSize(200, 20);
		bookRatingSlider.setLocation(500 ,80);
		bookRatingSlider.setValue((int)Math.round(4.7));
		bookDetailInfoPanel.add(bookRatingSlider);
		Icon icon = new ImageIcon(getClass().getResource("star.png"));
		JLabel star1 = new JLabel(icon);
		star1.setSize(32,32);
		star1.setLocation(531,105);
		bookDetailInfoPanel.add(star1);
		JLabel star2 = new JLabel(icon);
		star2.setSize(32,32);
		star2.setLocation(566,105);
		bookDetailInfoPanel.add(star2);
		JLabel star3 = new JLabel(icon);
		star3.setSize(32,32);
		star3.setLocation(601,105);
		bookDetailInfoPanel.add(star3);
		JLabel star4 = new JLabel(icon);
		star4.setSize(32,32);
		star4.setLocation(636,105);
		bookDetailInfoPanel.add(star4);
		JLabel star5 = new JLabel(icon);
		star5.setSize(32,32);
		star5.setLocation(671,105);
		bookDetailInfoPanel.add(star5);
		bookPageLabel = new JLabel("Pages");
		bookPageLabel.setSize(90,20);
		bookPageLabel.setLocation(505,155);
		bookDetailInfoPanel.add(bookPageLabel);
		bookPageTextField = new JTextField();
		bookPageTextField.setSize(90,20);
		bookPageTextField.setLocation(502,180);
		bookDetailInfoPanel.add(bookPageTextField);
		bookCostLabel = new JLabel("Cost");
		bookCostLabel.setSize(90,20);
		bookCostLabel.setLocation(602, 155);
		bookDetailInfoPanel.add(bookCostLabel);
		bookCostTextField = new JTextField();
		bookCostTextField.setSize(90, 20);
		bookCostTextField.setLocation(599,180);
		bookDetailInfoPanel.add(bookCostTextField);
		bookDescriptionArea = new JTextArea();
		
		JScrollPane scrollPane = new JScrollPane(bookDescriptionArea);
		scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		
		scrollPane.setSize(280,170);
		scrollPane.setLocation(700, 30);
		bookDescriptionArea.setLineWrap(true);
		bookDetailInfoPanel.add(scrollPane);
		bookDescriptionLabel = new JLabel("Description");
		bookDescriptionLabel.setSize(100,20);
		bookDescriptionLabel.setLocation(700, 5);
		bookDetailInfoPanel.add(bookDescriptionLabel);
		addBookButton = new JButton("Add");
		addBookButton.setSize(90, 20);
		addBookButton.setLocation(700,205);
		addBookButton.addActionListener(new AddBookHandler());
		bookDetailInfoPanel.add(addBookButton);
		updateBookButton = new JButton("Update");
		updateBookButton.setSize(90, 20);
		updateBookButton.setLocation(795, 205);
		updateBookButton.addActionListener(new UpdateButtonHandler());
		bookDetailInfoPanel.add(updateBookButton);
		clearButton = new JButton("Clear");
		clearButton.setSize(90,20);
		clearButton.setLocation(890, 205);
		clearButton.addActionListener(new ClearButtonHandler());
		bookDetailInfoPanel.add(clearButton);
		bookDetailInfoPanel.setVisible(false);
		bookDescriptionArea.setText("");
		
		
	}
	
	//set current image to broken image
	private void setBrokenImage() throws IOException{
		BufferedImage img = ImageIO.read(new File("/Users/mrnoctv/Documents/workspace/LibraryManagamentSoftware/ImageBroken.png"));
		Image resizedImg = img.getScaledInstance(bookCoverLabel.getWidth(), bookCoverLabel.getHeight()-15, Image.SCALE_SMOOTH);
		bookCoverLabel.setIcon(new ImageIcon(resizedImg));
	}
	
	private int[] getDate() {
		String[] temp = bookAddDateTextField.getText().split("-");
		int[] result = new int[3];
		for(int i = 0; i < temp.length; ++i)
			result[i] = new Integer(temp[i]);
			
		return result;
	}
	
	//validate book info before adding to database
	private boolean validateBookInfo() {
		try {
			int publishYear = Integer.parseInt(bookPublishYearTextField.getText());
			int quantity = Integer.parseInt(bookQuantityTextField.getText());
			int borrowed = Integer.parseInt(bookBorrowedTextField.getText());
			int pages = Integer.parseInt(bookPageTextField.getText());
			float cost = Float.parseFloat(bookCostTextField.getText());
			boolean date = bookAddDateTextField.getText().matches("\\d{4}-\\d{2}-\\d{2}");
			if(publishYear < 0 || quantity < borrowed 
					|| pages < 0 || cost < .0 || !date) {
				throw new Exception();
			}
		}catch(Exception ex) {
			JOptionPane.showMessageDialog(MainFrame.this, "Some data is not valid. Check Q&A and try again.");
			return false;
		}
		return true;
	}
	
	//execute program
	public static void main(String[] args) {
		try {
			new MainFrame();
		}catch(Exception ex) { 
			//catch exception might be thrown from MainFrame constructor
			ex.printStackTrace();
		}
	}
}
