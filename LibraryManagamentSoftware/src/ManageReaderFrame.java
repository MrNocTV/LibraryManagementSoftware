import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.ScrollPaneConstants;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;


public class ManageReaderFrame extends JFrame{
	private MainFrame mainFrame;
	private JPanel leftPanel;
	private JTextField readerNameTextField;
	private JTextField registeringDateTextField;
	private JTextField expireDateTextField;
	private JTextField readerTypeTextField;
	private JTextField dateOfBirthTextField;
	private JTextField addressTextField;
	private JPanel rightPanel;
	private JTextField emailTextField;
	private JTextField phoneTextField;
	private JTextField idCardNumberTextField;
	private JTextField statusTextField;
	private JPanel buttonPanel;
	private JButton updateButton;
	private JButton removeButton;
	private JTable readerTable;
	private DefaultTableModel readerTableModel;
	
	public ManageReaderFrame(MainFrame frame) {
		super("Manage Readers");
		mainFrame = frame;
		setLayout(null);
		
		leftPanel = new JPanel();
		leftPanel.setLayout(null);
		leftPanel.setSize(305, 170);
		leftPanel.setLocation(5, 5);
		leftPanel.setBorder(BorderFactory.createTitledBorder(
				null, "", TitledBorder.LEFT, TitledBorder.ABOVE_TOP));
		JLabel nameLabel = new JLabel("Reader Name");
		nameLabel.setSize(120, 20);
		nameLabel.setLocation(20, 10);
		leftPanel.add(nameLabel);
		JLabel registeringDateLabel = new JLabel("Registering Date");
		registeringDateLabel.setSize(120, 20);
		registeringDateLabel.setLocation(20, 35);
		leftPanel.add(registeringDateLabel);
		JLabel expireDateLabel = new JLabel("Expire Date");
		expireDateLabel.setSize(120, 20);
		expireDateLabel.setLocation(20, 60);
		leftPanel.add(expireDateLabel);
		JLabel readerTypeLabel = new JLabel("Reader Type");
		readerTypeLabel.setSize(120, 20);
		readerTypeLabel.setLocation(20, 85);
		leftPanel.add(readerTypeLabel);
		JLabel dateOfBirthLabel = new JLabel("Date Of Birth");
		dateOfBirthLabel.setSize(120, 20);
		dateOfBirthLabel.setLocation(20, 110);
		leftPanel.add(dateOfBirthLabel);
		JLabel addressLabel = new JLabel("Address");
		addressLabel.setSize(120, 20);
		addressLabel.setLocation(20, 135);
		leftPanel.add(addressLabel);
		readerNameTextField = new JTextField();
		readerNameTextField.setSize(150, 20);
		readerNameTextField.setLocation(140, 10);
		readerNameTextField.setEditable(false);
		leftPanel.add(readerNameTextField);
		registeringDateTextField = new JTextField();
		registeringDateTextField.setSize(150, 20);
		registeringDateTextField.setLocation(140, 35);
		registeringDateTextField.setEditable(false);
		leftPanel.add(registeringDateTextField);
		expireDateTextField = new JTextField();
		expireDateTextField.setSize(150, 20);
		expireDateTextField.setLocation(140, 60);
		expireDateTextField.setEditable(false);
		leftPanel.add(expireDateTextField);
		readerTypeTextField = new JTextField();
		readerTypeTextField.setSize(150, 20);
		readerTypeTextField.setLocation(140, 85);
		leftPanel.add(readerTypeTextField);
		dateOfBirthTextField = new JTextField();
		dateOfBirthTextField.setSize(150, 20);
		dateOfBirthTextField.setLocation(140, 110);
		dateOfBirthTextField.setEditable(false);
		leftPanel.add(dateOfBirthTextField);
		addressTextField = new JTextField();
		addressTextField.setSize(150, 20);
		addressTextField.setLocation(140, 135);
		leftPanel.add(addressTextField);
		
		rightPanel = new JPanel();
		rightPanel.setLayout(null);
		rightPanel.setSize(305, 115);
		rightPanel.setLocation(320, 5);
		rightPanel.setBorder(BorderFactory.createTitledBorder(
				null, "", TitledBorder.LEFT, TitledBorder.ABOVE_TOP));
		JLabel emailLabel = new JLabel("Email");
		emailLabel.setSize(120, 20);
		emailLabel.setLocation(20,10);
		rightPanel.add(emailLabel);
		JLabel phoneLabel = new JLabel("Phone");
		phoneLabel.setSize(120, 20);
		phoneLabel.setLocation(20, 35);
		rightPanel.add(phoneLabel);
		JLabel idCardNumberLabel = new JLabel("ID Card Number");
		idCardNumberLabel.setSize(120, 20);
		idCardNumberLabel.setLocation(20, 60);
		rightPanel.add(idCardNumberLabel);
		JLabel statusLabel = new JLabel("Card Status");
		statusLabel.setSize(120, 20);
		statusLabel.setLocation(20, 85);
		rightPanel.add(statusLabel);
		emailTextField = new JTextField();
		emailTextField.setSize(150, 20);
		emailTextField.setLocation(140, 10);
		rightPanel.add(emailTextField);
		phoneTextField = new JTextField();
		phoneTextField.setSize(150, 20);
		phoneTextField.setLocation(140, 35);
		rightPanel.add(phoneTextField);
		idCardNumberTextField = new JTextField();
		idCardNumberTextField.setSize(150, 20);
		idCardNumberTextField.setLocation(140, 60);
		rightPanel.add(idCardNumberTextField);
		statusTextField = new JTextField();
		statusTextField.setSize(150, 20);
		statusTextField.setLocation(140, 85);
		statusTextField.setEditable(false);
		rightPanel.add(statusTextField);
		
		buttonPanel = new JPanel();
		buttonPanel.setLayout(null);
		buttonPanel.setSize(305, 50);
		buttonPanel.setLocation(320, 125);
		buttonPanel.setBorder(BorderFactory.createTitledBorder(
				null, "", TitledBorder.LEFT, TitledBorder.ABOVE_TOP));
		updateButton = new JButton("Update");
		updateButton.setSize(95, 20);
		updateButton.setLocation(105, 12);
		updateButton.addActionListener(new UpdateButtonHandler());
		buttonPanel.add(updateButton);
		removeButton = new JButton("Remove");
		removeButton.setSize(95, 20);
		removeButton.setLocation(205, 12);
		removeButton.addActionListener(new RemoveButtonHandler());
		buttonPanel.add(removeButton);
		
		if(mainFrame.language.equals("english")) 
			readerTable = new JTable(new DefaultTableModel(new Object[]{"Order", "Name","Address", "Date Of Birth", "Email", "Phone"}, 0)) {
				@Override
				public boolean isCellEditable(int x, int y) {
					return false;
				}
			};
		else
			readerTable = new JTable(new DefaultTableModel(new Object[]{"STT", "Tên","Địa Chỉ", "Ngày Sinh", "Email", "SĐT"}, 0)) {
			@Override
			public boolean isCellEditable(int x, int y) {
				return false;
			}
		};
				
		readerTable.setGridColor(Color.BLACK);
		readerTable.setBackground(Color.LIGHT_GRAY);
		readerTable.setFillsViewportHeight(true);
		readerTableModel = (DefaultTableModel) readerTable.getModel();
		//add list selection listener for reader table
		ListSelectionModel listSelectionModel = readerTable.getSelectionModel();
		listSelectionModel.addListSelectionListener(new ReaderSelectedHandler());
		JScrollPane scrollPane = new JScrollPane(readerTable);
		scrollPane.setBorder(BorderFactory.createTitledBorder(null, "", TitledBorder.LEFT, TitledBorder.ABOVE_TOP));
		scrollPane.setSize(620, 150);
		scrollPane.setLocation(5, 180);
		scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		
		loadReaderDataIntoTable();
		
		if(mainFrame.language.equals("vietnamese")) {
			nameLabel.setText("Tên");
			registeringDateLabel.setText("Ngày Đăng Ký");
			expireDateLabel.setText("Ngày Hết Hạn");
			readerTypeLabel.setText("Loại Độc Giả");
			dateOfBirthLabel.setText("Ngày Sinh");
			addressLabel.setText("Địa Chỉ");
			phoneLabel.setText("SĐT");
			idCardNumberLabel.setText("CMND");
			statusLabel.setText("Tình Trạng Thẻ");
			updateButton.setText("Cập Nhật");
			removeButton.setText("Xoá");
			setTitle("Quản Lý Độc Giả");
		}
		add(leftPanel);
		add(rightPanel);
		add(buttonPanel);
		add(scrollPane);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setSize(630, 360);
		setVisible(true);
		setLocationRelativeTo(mainFrame);
		setResizable(false);
	}
	
	//remove reader
	//only remove if he or she does not own borrowed book
	private class RemoveButtonHandler implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent event) {
			String name = readerNameTextField.getText().trim();
			try {
				Statement statement = mainFrame.connect.createStatement();
				ResultSet result = statement.executeQuery("SELECT * FROM BorrowedBooks");
				boolean isReaderHasBorrowedBook = false;
				while(result.next()) {
					if(result.getString("ReaderName").trim().equals(name)) {
						isReaderHasBorrowedBook = true;
						break;
					}
				}
				if(isReaderHasBorrowedBook) {
					JOptionPane.showMessageDialog(ManageReaderFrame.this, "You cannot remove reader when"
							+ "they have borrowed books.");
				} else {
					PreparedStatement pStatement = mainFrame.connect.prepareStatement("DELETE FROM Reader WHERE Name=? ");
					pStatement.setString(1, name);
					//remove on server
					int count = pStatement.executeUpdate();
					if(count > 0) {
						//remove on table
						for(int i = 0; i < readerTable.getRowCount(); ++i) {
							String n = (String) readerTable.getValueAt(i, 1);
							if(n.equals(name)) {
								readerTableModel.removeRow(i);
								readerTable.repaint();
								break;
							}
						}
						JOptionPane.showMessageDialog(ManageReaderFrame.this, "Remove reader successfully");
					} else {
						JOptionPane.showMessageDialog(ManageReaderFrame.this, "Something happens.\n"
								+ "Check your input data and try again. If it does not work, ask for technical support");
					}
				}
			}catch(SQLException ex) {
				JOptionPane.showMessageDialog(ManageReaderFrame.this, "SQL ERROR (ManageReaderFrame)");
			}
		}
	}
	
	//handle when user click on upload
	//it's there responsible for updating data
	//so we don't ask to make sure if they want to update
	private class UpdateButtonHandler implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent event) {
			if(validateInput()) {
				String type = readerTypeTextField.getText().trim();
				String address = addressTextField.getText().trim();
				String email = emailTextField.getText().trim();
				String phone = phoneTextField.getText().trim();
				String idCardNumber = idCardNumberTextField.getText().trim();
				String name = readerNameTextField.getText();
				
				try {
					PreparedStatement pStatement = mainFrame.connect.prepareStatement("UPDATE Reader SET ReaderType=?, Address=?, Email=?, Phone=?, IDCardNumber=? "
							+ "WHERE Name=?");
					pStatement.setString(1, type);
					pStatement.setString(2, address);
					pStatement.setString(3, email);
					pStatement.setString(4, phone);
					pStatement.setString(5, idCardNumber);
					pStatement.setString(6, name);
					//update to database
					int count = pStatement.executeUpdate();
					if(count > 0) {
						//update table
						for(int i = 0; i < readerTable.getRowCount(); ++i) {
							if( ((String) readerTable.getValueAt(i, 1)).equals(name)) {
								readerTable.setValueAt(address, i, 2);
								readerTable.setValueAt(email, i, 4);
								readerTable.setValueAt(phone, i, 5);
								break;
							}
						}
						JOptionPane.showMessageDialog(ManageReaderFrame.this, "Update reader info successfully");
					} else {
						JOptionPane.showMessageDialog(ManageReaderFrame.this, "Something happens.\n"
								+ "Check your input data and try again. If it does not work, ask for technical support");
					}
				}catch(SQLException ex) {
					JOptionPane.showMessageDialog(ManageReaderFrame.this, "SQL ERROR (ManageReaderFrame)");
					System.out.println(ex.getMessage());
				}
			} else {
				JOptionPane.showMessageDialog(ManageReaderFrame.this, "Some data is in valid\n"
						+ "Check out Q&A and try again.");
			}
		}
		
		private boolean validateInput() {
			String type = readerTypeTextField.getText().trim();
			if(!type.equals("Normal") && !type.equals("Premium"))
				return false;
			if(addressTextField.getText().trim().equals("") 
			 || emailTextField.getText().trim().equals("") 
			 || phoneTextField.getText().trim().equals("")
			 || idCardNumberTextField.getText().trim().equals("")) 
				return false;
			return true;
		}
	}
	
	//handle when user click on a row in table
	//get information and then display it
	private class ReaderSelectedHandler implements ListSelectionListener {
		@Override
		public void valueChanged(ListSelectionEvent event) {
			int selectedRow = readerTable.getSelectedRow();
			//get reader name
			String name = (String)readerTable.getValueAt(selectedRow, 1);
			//search it in database 
			try {
				Statement statement = mainFrame.connect.createStatement();
				ResultSet result = statement.executeQuery("SELECT * FROM Reader");
				while(result.next()) {
					if(result.getString("Name").trim().equals(name)) {
						readerNameTextField.setText(name);
						registeringDateTextField.setText(result.getDate("RegisteringDate").toString());
						expireDateTextField.setText(result.getDate("ExpireDate").toString());
						readerTypeTextField.setText(result.getString("ReaderType"));
						dateOfBirthTextField.setText(result.getDate("DateOfBirth").toString());
						addressTextField.setText(result.getString("Address"));
						emailTextField.setText(result.getString("Email"));
						phoneTextField.setText(result.getString("Phone"));
						idCardNumberTextField.setText(result.getString("IDCardNumber"));
						isCardStillHealthy();
						break;
					}
				}
			}catch(Exception ex) {
				if(ex instanceof SQLException)
					JOptionPane.showMessageDialog(ManageReaderFrame.this, "");
				else {
					//do nothing
				}
			}
		}
	}
	
	//load readers data into Table
	private void loadReaderDataIntoTable() {
		try {
			Statement statement = mainFrame.connect.createStatement();
			ResultSet result = statement.executeQuery("SELECT * FROM Reader");
			int order = 1;
			while(result.next()) {
				String name = result.getString("Name").trim();
				String address = result.getString("Address").trim();
				String dateOfBirth = result.getString("DateOfBirth").trim();
				String email = result.getString("Email").trim();
				String phone = result.getString("Phone").trim();
				
				readerTableModel.addRow(new Object[]{String.valueOf(order), name, address, dateOfBirth, email, phone});
				++order;
			}
 			
		}catch(SQLException ex) {
			JOptionPane.showMessageDialog(ManageReaderFrame.this, "SQL ERROR (ManageReaderFrame)");
		}
	}
	
	//calculate card status value
	//if expire date is bigger than today, it's healthy
	//otherwise, it's  \'dead\'
	private boolean isCardStillHealthy() throws ParseException{
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		java.util.Date date = new java.util.Date();
		DDate today = new DDate(dateFormat.format(date));
		DDate expireDate = new DDate(expireDateTextField.getText());
		long compare = DDate.distanceBetweenDate(today, expireDate);
		System.out.println(compare);
		if(compare < 0) {
			statusTextField.setForeground(Color.GREEN);
			statusTextField.setText("Healthy");
		} else {
			statusTextField.setForeground(Color.RED);
			statusTextField.setText("Dead");
		}
		return true;
	}
	
}
