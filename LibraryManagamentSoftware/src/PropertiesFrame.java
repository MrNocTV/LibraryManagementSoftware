import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;


public class PropertiesFrame extends JFrame{
	private MainFrame mainFrame;
	private JPanel propertiesPanel;
	private JComboBox minimumReaderAgeComboBox;
	private JComboBox maximumReaderAgeComboBox;
	private JComboBox maximumBorrowTimeComboBox;
	private JComboBox maximumBorrowBookComboBox;
	private JTextField costPerDelayDayTextField;
	private JButton updatePropertiesButton;
	private JPanel categoryPanel;
	private JComboBox currentBookCategoryComboBox;
	private JTextField inputBookCategoryTextField;
	private JButton addBookCategoryButton;
	private JButton removeBookCategoryButton;
	private JPanel publisherPanel;
	private JComboBox currentPublisherComboBox;
	private JTextField inputPublisherTextField;
	private JButton addPublisherButton;
	private JButton removePublisherButton;
	
	public PropertiesFrame(MainFrame mainFrame) {
		super("Properties");
		setLayout(null);
		this.mainFrame = mainFrame;
		
		//initialize propertiesPanel components
		propertiesPanel = new JPanel();
		propertiesPanel.setLayout(null);
		propertiesPanel.setBorder(BorderFactory.createTitledBorder(
				null, "Properties", TitledBorder.LEFT, TitledBorder.ABOVE_TOP));
		propertiesPanel.setSize(300, 205);
		propertiesPanel.setLocation(3,0);
		JLabel minimumReaderAgeLabel = new JLabel("Minimum Age");
		minimumReaderAgeLabel.setSize(100,20);
		minimumReaderAgeLabel.setLocation(20,25);
		propertiesPanel.add(minimumReaderAgeLabel);
		JLabel maximumReaderAgeLabel = new JLabel("Maximum Age");
		maximumReaderAgeLabel.setSize(100,20);
		maximumReaderAgeLabel.setLocation(20,50);
		propertiesPanel.add(maximumReaderAgeLabel);
		JLabel maximumBorrowTimeLabel = new JLabel("Maximum Borrow Time");
		maximumBorrowTimeLabel.setSize(150,20);
		maximumBorrowTimeLabel.setLocation(20,75);
		propertiesPanel.add(maximumBorrowTimeLabel);
		JLabel maximumBorrowBookLabel = new JLabel("Maximum Borrow Book");
		maximumBorrowBookLabel.setSize(150,20);
		maximumBorrowBookLabel.setLocation(20, 100);
		propertiesPanel.add(maximumBorrowBookLabel);
		minimumReaderAgeComboBox = new JComboBox<>();
		minimumReaderAgeComboBox.setSize(100,20);
		minimumReaderAgeComboBox.setLocation(180,25);
		minimumReaderAgeComboBox.setMaximumRowCount(3);
		for(int i = 0; i <= 80; ++i) 
			minimumReaderAgeComboBox.addItem(String.valueOf(i));
		propertiesPanel.add(minimumReaderAgeComboBox);
		maximumReaderAgeComboBox = new JComboBox<>();
		maximumReaderAgeComboBox.setSize(100,20);
		maximumReaderAgeComboBox.setLocation(180,50);
		maximumReaderAgeComboBox.setMaximumRowCount(3);
		for(int i = 0; i <= 80; ++i) 
			maximumReaderAgeComboBox.addItem(String.valueOf(i));
		propertiesPanel.add(maximumReaderAgeComboBox);
		maximumBorrowTimeComboBox = new JComboBox<>();
		maximumBorrowTimeComboBox.setSize(100,20);
		maximumBorrowTimeComboBox.setLocation(180, 75);
		maximumBorrowTimeComboBox.setMaximumRowCount(3);
		for(int i = 0; i <= 10; ++i) 
			maximumBorrowTimeComboBox.addItem(String.valueOf(i));
		propertiesPanel.add(maximumBorrowTimeComboBox);
		maximumBorrowBookComboBox = new JComboBox<>();
		maximumBorrowBookComboBox.setSize(100,20);
		maximumBorrowBookComboBox.setLocation(180,100);
		maximumBorrowBookComboBox.setMaximumRowCount(3);
		for(int i = 0; i <= 10; ++i) 
			maximumBorrowBookComboBox.addItem(String.valueOf(i));
		propertiesPanel.add(maximumBorrowBookComboBox);
		JLabel costPerDelayDayLabel = new JLabel("Cost / Delay Day");
		costPerDelayDayLabel.setSize(150, 20);
		costPerDelayDayLabel.setLocation(20, 125);
		propertiesPanel.add(costPerDelayDayLabel);
	
		costPerDelayDayTextField = new JTextField();
		costPerDelayDayTextField.setSize(100, 20);
		costPerDelayDayTextField.setLocation(180,125);
		propertiesPanel.add(costPerDelayDayTextField);
		updatePropertiesButton = new JButton("Update");
		updatePropertiesButton.setSize(90,20);
		updatePropertiesButton.setLocation(185,175);
		updatePropertiesButton.addActionListener(new UpdateButtonHandler());
		propertiesPanel.add(updatePropertiesButton);
		
		//initialize category panel 
		categoryPanel = new JPanel();
		categoryPanel.setLayout(null);
		categoryPanel.setSize(300,138);
		categoryPanel.setLocation(3, 210);
		categoryPanel.setBorder(BorderFactory.createTitledBorder(
				null, "Book Category", TitledBorder.LEFT, TitledBorder.ABOVE_TOP));
		JLabel currentBookCategoryLabel = new JLabel("Current Book Category");
		currentBookCategoryLabel.setSize(190,20);
		currentBookCategoryLabel.setLocation(20, 25);
		categoryPanel.add(currentBookCategoryLabel);
		JLabel inputBookCategoryLabel = new JLabel(" + Input Book Category");
		inputBookCategoryLabel.setSize(150,20);
		inputBookCategoryLabel.setLocation(20,50);
		categoryPanel.add(inputBookCategoryLabel);
		currentBookCategoryComboBox = new JComboBox<>();
		currentBookCategoryComboBox.setSize(100,20);
		currentBookCategoryComboBox.setLocation(180,25);
		currentBookCategoryComboBox.setMaximumRowCount(3);
		categoryPanel.add(currentBookCategoryComboBox);
		inputBookCategoryTextField = new JTextField();
		inputBookCategoryTextField.setSize(100,20);
		inputBookCategoryTextField.setLocation(180, 50);
		categoryPanel.add(inputBookCategoryTextField);
		addBookCategoryButton = new JButton("Add");
		addBookCategoryButton.setSize(90, 20);
		addBookCategoryButton.setLocation(185,75);
		addBookCategoryButton.addActionListener(new AddCategoryButtonHandler());
		categoryPanel.add(addBookCategoryButton);
		removeBookCategoryButton = new JButton("Remove");
		removeBookCategoryButton.setSize(90, 20);
		removeBookCategoryButton.setLocation(185,100);
		removeBookCategoryButton.addActionListener(new RemoveCategoryButtonHandler());
		categoryPanel.add(removeBookCategoryButton);
		
		
		//initialize publisher panel
		publisherPanel = new JPanel();
		publisherPanel.setLayout(null);
		publisherPanel.setSize(300,138);
		publisherPanel.setLocation(3, 348);
		publisherPanel.setBorder(BorderFactory.createTitledBorder(
				null, "Publisher", TitledBorder.LEFT, TitledBorder.ABOVE_TOP));
		JLabel currentPublisherLabel = new JLabel("Current Publishers");
		currentPublisherLabel.setSize(120,20);
		currentPublisherLabel.setLocation(20, 25);
		publisherPanel.add(currentPublisherLabel);
		JLabel inputPublisherLabel = new JLabel(" + Input Publisher");
		inputPublisherLabel.setSize(120, 20);
		inputPublisherLabel.setLocation(20, 50);
		publisherPanel.add(inputPublisherLabel);
		currentPublisherComboBox = new JComboBox<>();
		currentPublisherComboBox.setSize(100,20);
		currentPublisherComboBox.setLocation(180,25);
		currentPublisherComboBox.setMaximumRowCount(3);
		publisherPanel.add(currentPublisherComboBox);
		inputPublisherTextField = new JTextField();
		inputPublisherTextField.setSize(100,20);
		inputPublisherTextField.setLocation(180,50);
		publisherPanel.add(inputPublisherTextField);
		addPublisherButton = new JButton("Add");
		addPublisherButton.setSize(90, 20);
		addPublisherButton.setLocation(185, 75);
		addPublisherButton.addActionListener(new AddPublisherButtonHandler());
		publisherPanel.add(addPublisherButton);
		removePublisherButton = new JButton("Remove");
		removePublisherButton.setSize(90,20);
		removePublisherButton.setLocation(185,100);
		removePublisherButton.addActionListener(new RemovePublisherButtonHandler());
		publisherPanel.add(removePublisherButton);
		
		if(mainFrame.language.equals("vietnamese")) {
			setTitle("");
			minimumReaderAgeLabel.setText("Tuổi Tối Thiểu");
			maximumReaderAgeLabel.setText("Tuổi Tối Đa");
			maximumBorrowTimeLabel.setText("TG Mượn Tối Đa");
			maximumBorrowBookLabel.setText("Sách Mượn Tối Đa");
			costPerDelayDayLabel.setText("Phí/Ngày Trễ");
			updatePropertiesButton.setText("Cập Nhật");
			currentBookCategoryLabel.setText("Loại Sách Hiện Tại");
			inputBookCategoryLabel.setText("+ Nhập Loại Sách");
			addBookCategoryButton.setText("Thêm");
			removeBookCategoryButton.setText("Xoá");
			currentPublisherLabel.setText("NXB Hiện Có");
			inputPublisherLabel.setText("+ Nhập NXB:");
			addPublisherButton.setText("Thêm");
			removePublisherButton.setText("Xoá");
		}
		
		initializeBookCategoryComboBox();
		initializeBookPublishersComboBox();
		initializeOthers();
		add(propertiesPanel);
		add(categoryPanel);
		add(publisherPanel);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setSize(306,515);
		setVisible(true);
		setLocationRelativeTo(mainFrame);
		setResizable(false);
		
	}
	
	//handle when user click on remove button in publisher section
	//we do the same like category
	//if publisher is in use, prevent user from removing it
	//otherwise, remove it immediately
	private class RemovePublisherButtonHandler implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent event) {
			String publisher = inputPublisherTextField.getText().trim();
			if(!checkPublisherEmpty(publisher) ) {
				if(  checkPublisherExist(publisher) ) {
					if(!checkPublisherIsInUse(publisher)) {
						int choice = JOptionPane.showConfirmDialog(PropertiesFrame.this, "Do you want to remove this publisher?", "Prompt", JOptionPane.YES_NO_OPTION);
						if(choice == JOptionPane.YES_OPTION) {
							//remove it on server
							try {
								PreparedStatement pStatement = mainFrame.connect.prepareStatement("DELETE FROM Publisher WHERE Publisher=?");
								pStatement.setString(1, publisher);
								int resultCount = pStatement.executeUpdate();
								if(resultCount > 0) {
									JOptionPane.showMessageDialog(PropertiesFrame.this, "Remove publisher successfully\n");
								} else throw new SQLException();
							}catch(SQLException ex ){
								JOptionPane.showMessageDialog(PropertiesFrame.this, "SQL ERROR (RemovePublisherButtonHandler)");
								System.out.println(ex.getMessage());
							}
							//remove in publisher combobox
							currentPublisherComboBox.removeItem((String)publisher);
						} //end "if choice"
					}
				} else {
					JOptionPane.showMessageDialog(PropertiesFrame.this, "Publisher does not exist. Removing failed.\n"
							+ "Check out Q&A and try again.");
				}
			} // end check if empty
		}
		
		//check if publisher is in use
		private boolean checkPublisherIsInUse(String publisher) {
			try {
				Statement statement = mainFrame.connect.createStatement();
				ResultSet result = statement.executeQuery("SELECT * FROM Book");
				boolean isInUse = false;
				while(result.next()) {
					if(result.getString("Publisher").trim().equals(publisher)) {
						isInUse = true;
						break;
					}
				}
				if(isInUse) {
					JOptionPane.showMessageDialog(PropertiesFrame.this, "There are some books have this publisher.\n"
							+ "You cannot remove it. Check Q&A for more details");
					return true;
				} 
			}catch(SQLException ex ){
				JOptionPane.showMessageDialog(PropertiesFrame.this, "SQL ERROR (RemovePublisherButtonHandler)");
			}
			return false;
		}
		
		//check if publisher exist
		private boolean checkPublisherExist(String publisher) {
			for(int i = 0; i < currentPublisherComboBox.getItemCount(); ++i) {
				if( ((String) currentPublisherComboBox.getItemAt(i)).equals(publisher) ) {
					return true;
				}
			}
			return false;
		}
	}
	
	//handle when user click on add button in publisher section
	//we do the same like category
	//check duplicate, if it does, cannot add, otherwise, add immediately
	private class AddPublisherButtonHandler implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent event) {
			String publisher = inputPublisherTextField.getText().trim();
			if(!checkPublisherEmpty(publisher) && !checkDuplicatePublisher(publisher)) {
				int choice = JOptionPane.showConfirmDialog(PropertiesFrame.this, "Do you want to add this publisher?", "Prompt", JOptionPane.YES_NO_OPTION);
				if(choice == JOptionPane.YES_OPTION) {
					//add to server
					try {
						PreparedStatement pStatement = mainFrame.connect.prepareStatement("INSERT INTO Publisher VALUES(?)");
						pStatement.setString(1, publisher);
						int resultCount = pStatement.executeUpdate();
						if(resultCount > 0 ) {
							JOptionPane.showMessageDialog(PropertiesFrame.this, "Adding publisher successfully");
						} else throw new SQLException();
					}catch(SQLException ex) {
						JOptionPane.showMessageDialog(PropertiesFrame.this, "SQL ERROR (AddPublisherButtonHandler)");
					}
					
					//add to publisher combo box
					currentPublisherComboBox.addItem((String)publisher);
				} //end "if choice"
			}
		}
		
		//check duplicate
		private boolean checkDuplicatePublisher(String publisher) {
			try {
				Statement statement = mainFrame.connect.createStatement();
				ResultSet result = statement.executeQuery("SELECT * FROM Publisher");
				boolean isDuplicated = false;	
				while(result.next()) {
					if(result.getString("Publisher").trim().equals(publisher)) {
						isDuplicated = true;
						break;
					}
				}
				if(isDuplicated) {
					JOptionPane.showMessageDialog(PropertiesFrame.this, "Duplicate publisher.\n"
							+ "Adding failed. Check out Q&A and then try again.");
					return true;
				} 
			}catch(SQLException ex) {
				JOptionPane.showMessageDialog(PropertiesFrame.this, "SQL ERROR (AddPublisherButtonHandler)");
			}
			return false;
		}
	}
	
	//handle when user click on remove button in category section
	//check if are there any books that have that category
	//if there are, prevent user from removing it,
	//if not, remove it immediately
	private class RemoveCategoryButtonHandler implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent event) {
			//check if there is a category like that
			String category = inputBookCategoryTextField.getText().trim();
			if(checkIfCategoryExist(category)) {
				//check in database if there is a book has that category
				if(!checkIfCategoryIsInUse(category)) {
					//it's not in use, we can remove it
					int choice = JOptionPane.showConfirmDialog(PropertiesFrame.this, "Do you want to remove this category?", "Prompt", JOptionPane.YES_NO_OPTION);
						if(choice == JOptionPane.YES_OPTION) {
							//remove it on server
							try {
								PreparedStatement pStatement = mainFrame.connect.prepareStatement("DELETE FROM Category WHERE Category=?");
								pStatement.setString(1, category);
								int resultCount = pStatement.executeUpdate();
								if(resultCount > 0) {
									JOptionPane.showMessageDialog(PropertiesFrame.this, "Remove category successfully!");
								} else throw new SQLException();
							}catch(SQLException ex) {
								JOptionPane.showMessageDialog(PropertiesFrame.this, "SQL ERROR (RemoveCategoryButtonHandler)");
								System.out.println(ex.getMessage());
							}
						
							//then remove it in combo box
							currentBookCategoryComboBox.removeItem(category);
							
						} //end "if choice"
				}
				
			} else {
				JOptionPane.showMessageDialog(PropertiesFrame.this, "That category does not exist. Removing failed");
			}
		}
		
		private boolean checkIfCategoryExist(String category) {
			for(int i = 0; i < currentBookCategoryComboBox.getItemCount(); ++i) {
				if(((String)currentBookCategoryComboBox.getItemAt(i)).equals(category)) {
					return true;
				}
			}
			return false;
		}
		
		private boolean checkIfCategoryIsInUse(String category) {
			try {
				Statement statement = mainFrame.connect.createStatement();
				ResultSet result = statement.executeQuery("SELECT * FROM Book");
				boolean isInUse = false;
				while(result.next()) {
					if(result.getString("Category").trim().equals(category)) {
						isInUse = true;
						break;
					}
				}
				
				if(isInUse) {
					JOptionPane.showMessageDialog(PropertiesFrame.this, "There are some books have this category.\n"
							+ "You cannot remove it. Check Q&A for more details");
					return true;
				}
			}catch(SQLException ex ) {
				JOptionPane.showMessageDialog(PropertiesFrame.this, "SQL ERROR ()");
			}
			return false;
		}
	}
	
	//handle when user click on add button in category section
	private class AddCategoryButtonHandler implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent event) {
			String newCategory = inputBookCategoryTextField.getText().trim();
			//check if category is empty
			if(!isCategoryEmpty(newCategory) && !checkDuplicate(newCategory)) {
				int choice = JOptionPane.showConfirmDialog(PropertiesFrame.this, "Do you want to add this category?", "Prompt", JOptionPane.YES_NO_OPTION);
				if(choice == JOptionPane.YES_OPTION) {
					//add to database
					try {
						PreparedStatement pStatement = mainFrame.connect.prepareStatement("INSERT INTO Category VALUES(?)");
						pStatement.setString(1, newCategory);
						int resultCount = pStatement.executeUpdate();
						if(resultCount > 0) 
							JOptionPane.showMessageDialog(PropertiesFrame.this, "Add new book category successfully");
						else throw new SQLException();
					}catch(SQLException ex) {
						JOptionPane.showMessageDialog(PropertiesFrame.this, "SQL ERROR (AddCategoryButtonHandler)");
						System.out.println(ex.getMessage());
					}
					
					//add to category combobox (for displaying purpose)
					currentBookCategoryComboBox.addItem((String)newCategory);
				}
			} 
		}
		
		//check if book category already exists
		//if duplicate, return true
		//otherwise, return false
		private boolean checkDuplicate(String category) {
			try {
				Statement pStatement = mainFrame.connect.createStatement();
				ResultSet result = pStatement.executeQuery("SELECT * FROM Category");
				boolean isDuplicate = false;
				while(result.next()) {
					if(result.getString("Category").trim().equals(category)) {
						isDuplicate = true;
						break;
					}
				}
				
				//notify user that they're adding duplicate category
				if(isDuplicate) {
					JOptionPane.showMessageDialog(PropertiesFrame.this, "Duplicate Category."
							+ "Adding failed.");
					return true;
				}
				else
					return false;
			}catch(SQLException ex) {
				JOptionPane.showMessageDialog(PropertiesFrame.this, "SQL ERROR (AddBookCategoryButtonHandler)");
				System.out.println(ex.getMessage());
				return true; //return true to stop user from adding category when something wrong happens
			}
		}
		
		//check if book category is empty
		private boolean isCategoryEmpty(String category) {
			if(category.equals("")){
				JOptionPane.showMessageDialog(PropertiesFrame.this, "Some data is invalid\n"
						+ "Check out Q&A and try again.");
				return true;
			}
			return false;
		}
	}
	
	//handle when user click on update button
	private class UpdateButtonHandler implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent event) {
			try {
				if(checkCostValues()) {
					int choice = JOptionPane.showConfirmDialog(PropertiesFrame.this, "Do you want to update properties?", "Prompt", JOptionPane.YES_NO_OPTION);
					if(choice == JOptionPane.YES_OPTION) {
						int minAge = new Integer((String)minimumReaderAgeComboBox.getSelectedItem());
						int maxAge = new Integer((String)maximumReaderAgeComboBox.getSelectedItem());
						int maxTime = new Integer((String)maximumBorrowTimeComboBox.getSelectedItem());
						int maxBook = new Integer((String)maximumBorrowBookComboBox.getSelectedItem());
						int costPerDay = new Integer(costPerDelayDayTextField.getText().trim());
			
						
						PreparedStatement pStatement = mainFrame.connect.prepareStatement("UPDATE Properties SET "
								+ "MinimumAge=?, MaximumAge=?, MaximumBorrowTime=?, MaximumBorrowBook=?, "
								+ "CostPerDelayDay=?");
						pStatement.setInt(1, minAge);
						pStatement.setInt(2, maxAge);
						pStatement.setInt(3, maxTime);
						pStatement.setInt(4, maxBook);
						pStatement.setInt(5, costPerDay);
						//execute updating
						int resultCount = pStatement.executeUpdate();
						//check if update is successful
						if(resultCount > 0) {
							JOptionPane.showMessageDialog(PropertiesFrame.this, "Update properties info successfully");
						} else throw new SQLException();
					}//end "if choice"
				}
			}catch(SQLException ex) {
				JOptionPane.showMessageDialog(PropertiesFrame.this, "SQL ERROR (UpdateButtonHandler)");
			}
		}
		
		//validate input data
		private boolean checkCostValues() {
			try {
				int costPerDay = new Integer(costPerDelayDayTextField.getText().trim());
				if(costPerDay <= 0)
					throw new Exception();
			}catch(Exception ex) {
				JOptionPane.showMessageDialog(PropertiesFrame.this, "Some data is invalid\n"
						+ "Check out Q&A and try again.");
				return false;
			}
			return true;
		}
	}
	
	private void initializeBookCategoryComboBox() {
		try {
			Statement statement = mainFrame.connect.createStatement();
			ResultSet result = statement.executeQuery("SELECT * FROM Category");
			while(result.next()) {
				currentBookCategoryComboBox.addItem(result.getString("Category"));
			}
		}catch(SQLException ex) {
			JOptionPane.showMessageDialog(PropertiesFrame.this, "SQL ERROR (bookCategoryComboBox)");
		}
	}
	
	private void initializeBookPublishersComboBox() {
		try {
			Statement statement = mainFrame.connect.createStatement();
			ResultSet result = statement.executeQuery("SELECT * FROM Publisher");
			while(result.next()) {
				currentPublisherComboBox.addItem(result.getString("Publisher"));
			}
		}catch(SQLException ex) {
			JOptionPane.showMessageDialog(PropertiesFrame.this, "SQL ERROR (publisherComboBox)");
		}
	}
	
	//initialize first value for 
	//min age, max age, max borrow time, max borrow book
	//cost per day, cost per month (use to calculate revenue )
	private void initializeOthers() {
		try {
			Statement statement = mainFrame.connect.createStatement();
			ResultSet result = statement.executeQuery("SELECT * FROM Properties");
			if(result.next()) {
				String minAge = String.valueOf(result.getInt("MinimumAge")).trim();
				String maxAge = String.valueOf(result.getInt("MaximumAge")).trim();
				String maxTime = String.valueOf(result.getInt("MaximumBorrowTime")).trim();
				String maxBook = String.valueOf(result.getInt("MaximumBorrowBook")).trim();
				String costDay = String.valueOf(result.getInt("CostPerDelayDay")).trim();
				String costMonth = String.valueOf(result.getInt("CostPerMonth")).trim();
				minimumReaderAgeComboBox.setSelectedItem(minAge);
				maximumReaderAgeComboBox.setSelectedItem(maxAge);
				maximumBorrowTimeComboBox.setSelectedItem(maxTime);
				maximumBorrowBookComboBox.setSelectedItem(maxBook);
				costPerDelayDayTextField.setText(costDay);
				
			} else throw new SQLException();
		}catch(SQLException ex) {
			JOptionPane.showMessageDialog(PropertiesFrame.this, "SQL ERROR (initializeOthers)");
		}
	}
	
		//check empty
			private boolean checkPublisherEmpty(String publisher) {
				 if(publisher.equals("") ) {
					 JOptionPane.showMessageDialog(PropertiesFrame.this, "Some data is invalid\n"
					 		+ "Check out Q&A and try again.");
					 return true;
				 }
				 return false;
			}
}
