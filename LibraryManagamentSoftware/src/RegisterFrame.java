import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.nio.charset.Charset;
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


public class RegisterFrame extends JFrame {
	private MainFrame mainFrame;
	private JTextField nameTextField;
	private JTextField addressTextField;
	private JTextField emailTextField;
	private JTextField dateOfBirthTextField;
	private JTextField registeringDateTextField;
	private JTextField phoneTextField;
	private JTextField idCardNumberTextField;
	private JComboBox numberOfMonthsComboBox;
	private JTextField expireDateTextField;
	private JPanel buttonPanel;
	private JButton clearButton;
	private JButton addButton;
	
	private JPanel readerInfoPanel;
	private JPanel cardInfoPanel;
	private JPanel readerTypePanel;
	private JComboBox readerTypeComboBox;
	
	public RegisterFrame(MainFrame frame) {
		super("Register");
		System.setProperty("file.encoding","UTF-8");
		
		setLayout(null);
		mainFrame = frame;
		
		readerInfoPanel = new JPanel();
		readerInfoPanel.setLayout(null);
		readerInfoPanel.setSize(303, 180);
		readerInfoPanel.setLocation(5,5);
		if(mainFrame.language.equals("english"))
			readerInfoPanel.setBorder(BorderFactory.createTitledBorder(
					null, "Reader Info", TitledBorder.LEFT, TitledBorder.ABOVE_TOP));
		else
			readerInfoPanel.setBorder(BorderFactory.createTitledBorder(
					null, "Thông Tin Độc Giả", TitledBorder.LEFT, TitledBorder.ABOVE_TOP));
		JLabel nameLabel = new JLabel("Reader Name");
		nameLabel.setSize(120, 20);
		nameLabel.setLocation(20, 25);
		readerInfoPanel.add(nameLabel);
		JLabel dateOfBirthLabel = new JLabel("Date Of Birth");
		dateOfBirthLabel.setSize(120, 20);
		dateOfBirthLabel.setLocation(20, 50);
		readerInfoPanel.add(dateOfBirthLabel);
		JLabel addressLabel = new JLabel("Address");
		addressLabel.setSize(120, 20);
		addressLabel.setLocation(20, 75);
		readerInfoPanel.add(addressLabel);
		JLabel emailLabel = new JLabel("Email");
		emailLabel.setSize(120, 20);
		emailLabel.setLocation(20, 100);
		readerInfoPanel.add(emailLabel);
		JLabel phoneLabel = new JLabel("Phone");
		phoneLabel.setSize(120, 20);
		phoneLabel.setLocation(20, 125);
		readerInfoPanel.add(phoneLabel);
		JLabel idCardNumberLabel = new JLabel("ID Card Number");
		idCardNumberLabel.setSize(120, 20);
		idCardNumberLabel.setLocation(20, 150);
		readerInfoPanel.add(idCardNumberLabel);
		nameTextField = new JTextField();
		nameTextField.setSize(150, 20);
		nameTextField.setLocation(145, 25);
		readerInfoPanel.add(nameTextField);
		dateOfBirthTextField = new JTextField();
		dateOfBirthTextField.setSize(100, 20);
		dateOfBirthTextField.setLocation(195,50);
		dateOfBirthTextField.setText("yyyy-mm-dd");
		readerInfoPanel.add(dateOfBirthTextField);
		addressTextField = new JTextField();
		addressTextField.setSize(150, 20);
		addressTextField.setLocation(145, 75);
		readerInfoPanel.add(addressTextField);
		emailTextField = new JTextField();
		emailTextField.setSize(150, 20);
		emailTextField.setLocation(145, 100);
		readerInfoPanel.add(emailTextField);
		phoneTextField = new JTextField();
		phoneTextField.setSize(100, 20);
		phoneTextField.setLocation(195, 125);
		readerInfoPanel.add(phoneTextField);
		idCardNumberTextField = new JTextField();
		idCardNumberTextField.setSize(100, 20);
		idCardNumberTextField.setLocation(195, 150);
		readerInfoPanel.add(idCardNumberTextField);
		
		cardInfoPanel = new JPanel();
		cardInfoPanel.setLayout(null);
		cardInfoPanel.setSize(263, 103);
		cardInfoPanel.setLocation(310, 5);
		if(mainFrame.language.equals("english"))
			cardInfoPanel.setBorder(BorderFactory.createTitledBorder(
					null, "Card Info", TitledBorder.LEFT, TitledBorder.ABOVE_TOP));
		else {
			cardInfoPanel.setBorder(BorderFactory.createTitledBorder(
					null, "Thông Tin Thẻ", TitledBorder.LEFT, TitledBorder.ABOVE_TOP));
		}
		JLabel registeringDateLabel = new JLabel("Registering Date");
		registeringDateLabel.setSize(120, 20);
		registeringDateLabel.setLocation(20, 25);
		cardInfoPanel.add(registeringDateLabel);
		JLabel numberOfMonthsLabel = new JLabel("Number Of Months");
		numberOfMonthsLabel.setSize(120, 20);
		numberOfMonthsLabel.setLocation(20, 50);
		cardInfoPanel.add(numberOfMonthsLabel);
		JLabel expireDateLabel = new JLabel("Expire Date");
		expireDateLabel.setSize(120, 20);
		expireDateLabel.setLocation(20, 75);
		cardInfoPanel.add(expireDateLabel);
		registeringDateTextField = new JTextField();
		registeringDateTextField.setSize(100, 20);
		registeringDateTextField.setLocation(145, 25);
		registeringDateTextField.setText("yyyy-mm-dd");
		cardInfoPanel.add(registeringDateTextField);
		numberOfMonthsComboBox = new JComboBox<>(new String[]{"1","2","3","4","5","6","7"
				,"8","9","10","11","12"});
		numberOfMonthsComboBox.setMaximumRowCount(3);
		numberOfMonthsComboBox.setSize(100,20);
		numberOfMonthsComboBox.setLocation(145, 50);
		numberOfMonthsComboBox.addItemListener(new MonthSelectionHandler());
		cardInfoPanel.add(numberOfMonthsComboBox);
		expireDateTextField = new JTextField();
		expireDateTextField.setSize(100,20);
		expireDateTextField.setLocation(145, 75);
		expireDateTextField.setEditable(false);
		cardInfoPanel.add(expireDateTextField);
		
		buttonPanel = new JPanel();
		buttonPanel.setLayout(null);
		buttonPanel.setSize(120, 75);
		buttonPanel.setLocation(453, 110);
		
		buttonPanel.setBorder(BorderFactory.createTitledBorder(
				null, "", TitledBorder.LEFT, TitledBorder.ABOVE_TOP));
		clearButton = new JButton("Clear");
		clearButton.setSize(100, 20);
		clearButton.setLocation(9,22);
		clearButton.addActionListener(new ClearButtonHandler());
		buttonPanel.add(clearButton);
		addButton = new JButton("Add");
		addButton.setSize(100,20);
		addButton.setLocation(9,47);
		addButton.addActionListener(new AddButtonHandler());
		buttonPanel.add(addButton);
		
		readerTypePanel = new JPanel();

		readerTypePanel.setSize(120, 75);
		readerTypePanel.setLocation(310,110);
		if(mainFrame.language.equals("english"))
			readerTypePanel.setBorder(BorderFactory.createTitledBorder(
					null, "Reader Type", TitledBorder.LEFT, TitledBorder.ABOVE_TOP));
		else
			readerTypePanel.setBorder(BorderFactory.createTitledBorder(
					null, "Loại Độc Giả", TitledBorder.LEFT, TitledBorder.ABOVE_TOP));
		readerTypeComboBox = new JComboBox<>(new String[]{"Normal","Premium"});
		readerTypePanel.add(readerTypeComboBox);
		
		if(mainFrame.language.equals("vietnamese")) {
			nameLabel.setText("Tên Độc Giả");
			dateOfBirthLabel.setText("Ngày Sinh");
			addressLabel.setText("Địa Chỉ");
			phoneLabel.setText("SĐT");
			idCardNumberLabel.setText("CMND");
			registeringDateLabel.setText("Ngày Đăng Ký");
			numberOfMonthsLabel.setText("Số Tháng");
			expireDateLabel.setText("Ngày Hết Hạn");
			clearButton.setText("Dẹp");
			addButton.setText("Thêm");
			
		}
		
		add(readerInfoPanel);
		add(cardInfoPanel);
		add(buttonPanel);
		add(readerTypePanel);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setSize(578,220);
		setVisible(true);
		setLocationRelativeTo(mainFrame);
		setResizable(false);
	}
	
	//handle when user select item in numberOfMonthsComboBox;
	private class MonthSelectionHandler implements ItemListener {
		@Override
		public void itemStateChanged(ItemEvent event ) {
			calculateExpireDate();
		}
		
		private void calculateExpireDate() {
			if(validateRegisterDate()) {
				String[] splits = registeringDateTextField.getText().split("-");
				int month = new Integer(splits[1]);
				int year = new Integer(splits[0]);
				month += (numberOfMonthsComboBox.getSelectedIndex()+1);
				if(month > 12)  {
					month -= 12;
					++year;
				}
				splits[0] = String.valueOf(year);
				splits[1] = String.valueOf(month);
				expireDateTextField.setText(splits[0]+"-"+(month < 10 ? "0"+month : month)+"-"+splits[2]);
			}  
		}
	}
	
	//handle when user click on clearButton
	private class ClearButtonHandler implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent event) {
			nameTextField.setText("");
			addressTextField.setText("");
			emailTextField.setText("");
			dateOfBirthTextField.setText("yyyy-mm-dd");
			registeringDateTextField.setText("yyyy-mm-dd");
			phoneTextField.setText("");
			idCardNumberTextField.setText("");
			expireDateTextField.setText("");
		}
	}
	
	//handle add mechanism 
	private class AddButtonHandler implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent event) {
			try {
				validateInput();
			}catch(Exception ex) {
				if(ex instanceof SQLException) {
					JOptionPane.showMessageDialog(RegisterFrame.this, "SQL ERROR (RegisterFrame)");
				} 
			}
		} //end actionPerformed
			
	}
	
	private boolean validateRegisterDate() {
		return registeringDateTextField.getText().trim().matches("\\d{4}-\\d{2}-\\d{2}") ;
	}
	
	private boolean validateExpireDate() {
		return !expireDateTextField.getText().trim().equals("");
	}
	
	private boolean validateDateOfBirth() {
		return dateOfBirthTextField.getText().trim().matches("\\d{4}-\\d{2}-\\d{2}");
	}
	
	private boolean validateInput() throws Exception{
		String name = nameTextField.getText().trim(); //1
		String address = addressTextField.getText().trim(); //2
		String email = emailTextField.getText().trim(); //3
		boolean checkDateOfBirth = validateDateOfBirth();
		boolean checkRegisterDate = validateRegisterDate();
		String phone = phoneTextField.getText().trim(); //4
		String idCardNumber = idCardNumberTextField.getText().trim(); //5
		boolean checkExpireDate = validateExpireDate();
		
		//start checking
		if(name.equals("") || address.equals("") || email.equals("") || !checkDateOfBirth 
				|| !checkRegisterDate || phone.equals("") || idCardNumber.equals("") 
				|| !checkExpireDate) {
			//if data is invalid
			//we just say that it returns false
			//or we could add a JOptionPane to notify our little users
			JOptionPane.showMessageDialog(RegisterFrame.this, "Some data is invalid.\n"
					+ "Check Q&A and then try again ..... if you want. :v ");
			return false;
		}
		
		//done checking. now the hard part
		//check duplicate reader name (if has) 
		Statement statement = mainFrame.connect.createStatement();
		ResultSet result = statement.executeQuery("SELECT * FROM Reader");
		boolean isDuplicated = false;
		while(result.next()) {
			System.out.println(result.getString("Name"));
			if(result.getString("Name").trim().equals(name)) {
				
				isDuplicated = true;
				break;
			}
		}
		if(isDuplicated) {
			JOptionPane.showMessageDialog(RegisterFrame.this, "Duplicate reader name.\n"
					+ "Change and try again.");
		} else {
			String dateOfBirth = dateOfBirthTextField.getText().trim(); //6
			String registerDate = registeringDateTextField.getText().trim(); //7
			String expireDate = expireDateTextField.getText().trim(); //8
			String readerType = (String)readerTypeComboBox.getSelectedItem(); //9
			PreparedStatement pStatement = mainFrame.connect.prepareStatement("INSERT INTO Reader VALUES(?,?,?,?,?,?,?,?,?,?)");
			pStatement.setString(1, name);
			pStatement.setString(2, dateOfBirth);
			pStatement.setString(3, address);
			pStatement.setString(4, email);
			pStatement.setString(5, phone);
			pStatement.setString(6, idCardNumber);
			pStatement.setString(7, registerDate);
			pStatement.setString(8, expireDate);
			pStatement.setString(9, readerType);
			pStatement.setString(10, String.valueOf(0));
			
			int resultCount = pStatement.executeUpdate();
			if(resultCount > 0) {
				JOptionPane.showMessageDialog(RegisterFrame.this, "Add user successfully.");
			} else {
				throw new Exception();
			}
		}
		
		return true;
	}
	
}
