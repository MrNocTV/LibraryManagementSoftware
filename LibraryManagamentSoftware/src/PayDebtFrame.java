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

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;


public class PayDebtFrame extends JFrame{
	private MainFrame mainFrame;
	private JComboBox readerNameComboBox;
	private JTextField debtTextField;
	private JTextField payTextField;
	private JTextField leftTextField;
	private JButton processButton; 
	
	public PayDebtFrame(MainFrame frame) {
		super("Pay Debt");
		mainFrame = frame;
		setLayout(null);
		
		JLabel readerNameLabel = new JLabel("Reader Name");
		readerNameLabel.setSize(120, 20);
		readerNameLabel.setLocation(10,10);
		add(readerNameLabel);
		JLabel debtLabel = new JLabel("Debt");
		debtLabel.setSize(120, 20);
		debtLabel.setLocation(10, 35);
		add(debtLabel);
		JLabel payLabel = new JLabel("Pay");
		payLabel.setSize(120, 20);
		payLabel.setLocation(10, 60);
		add(payLabel);
		JLabel leftLabel = new JLabel("Left");
		leftLabel.setSize(120, 20);
		leftLabel.setLocation(10, 85);
		add(leftLabel);
		readerNameComboBox = new JComboBox<>();
		readerNameComboBox.setSize(150, 20);
		readerNameComboBox.setLocation(135, 10);
		readerNameComboBox.setMaximumRowCount(3);
		readerNameComboBox.addItem("Off");
		initReaderNameComboBox();
		readerNameComboBox.addItemListener(new ReaderNameSelectHandler());
		add(readerNameComboBox);
		debtTextField = new JTextField();
		debtTextField.setSize(150, 20);
		debtTextField.setLocation(135,35);
		debtTextField.setEditable(false);
		add(debtTextField);
		payTextField = new JTextField();
		payTextField.setSize(150, 20);
		payTextField.setLocation(135, 60);
		payTextField.addActionListener(new PayHandler());
		add(payTextField);
		leftTextField = new JTextField();
		leftTextField.setSize(150, 20);
		leftTextField.setLocation(135, 85);
		leftTextField.setEditable(false);
		add(leftTextField);
		processButton = new JButton("Process");
		processButton.setSize(100, 20);
		processButton.setLocation(160, 110);
		processButton.addActionListener(new ProcessHandler());
		add(processButton);
		
		if(mainFrame.language.equals("vietnamese")) {
			readerNameLabel.setText("Tên");
			debtLabel.setText("Nợ");
			payLabel.setText("Trả");
			leftLabel.setText("Còn");
			setTitle("Trả Nợ");
		}
		
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setSize(295,160);
		setVisible(true);
		setLocationRelativeTo(mainFrame);
		setResizable(false);
	}
	
	//handle when user click on process button
	private class ProcessHandler implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent event ){
			String newDebt = leftTextField.getText().trim();
			if(!newDebt.equals("")) {
				if(JOptionPane.showConfirmDialog(PayDebtFrame.this, "Are you sure?", "Prompt", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
					String name = (String)readerNameComboBox.getSelectedItem();
					try {
						PreparedStatement pStatement = mainFrame.connect.prepareStatement("UPDATE Reader SET Debt=? WHERE Name=?");
						pStatement.setString(1, newDebt);
						pStatement.setString(2, name);
						int resultCount = pStatement.executeUpdate();
						if(resultCount > 0) {
							JOptionPane.showMessageDialog(PayDebtFrame.this, "Process successfully.\n"
									+ "PayDebt window will close automatically");
							dispose();
						} else throw new SQLException();
					}catch(SQLException ex) {
						JOptionPane.showMessageDialog(PayDebtFrame.this, "SQL ERROR (ReaderNameSlelectHandler)");
					}
				}//end if choice
			} else {
				JOptionPane.showMessageDialog(PayDebtFrame.this, "Please enter a pay value first");
			}
		
		}
	}
	
	//handle when user enter a value in payTextField
	//and hit enter
	//pay value must be <= debt value
	private class PayHandler implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent event) {
			if(!payTextField.getText().trim().equals("")) {
				int debtValue = new Integer(debtTextField.getText());
				int payValue = 0;
				try {
					payValue = new Integer(payTextField.getText().trim());
					if(payValue > debtValue) 
						throw new Exception();
					leftTextField.setText(String.valueOf(debtValue-payValue));
				}catch(Exception ex) {
					JOptionPane.showMessageDialog(PayDebtFrame.this, "Some data is invalid\n"
							+ "Check out Q&A and try again");
				}
			}
			
		}
	}
	
	//handle when user select a reader name in readerNameComboBox
	private class ReaderNameSelectHandler implements ItemListener {
		@Override
		public void itemStateChanged(ItemEvent event) {
			String name = (String) readerNameComboBox.getSelectedItem();
			if(!name.equals("Off")) {
				//reset pay
				payTextField.setText("");
				//reset left
				leftTextField.setText("");
				payTextField.setEditable(true);
				try {
					Statement statement = mainFrame.connect.createStatement();
					ResultSet result = statement.executeQuery("SELECT * FROM Reader");
					while(result.next()) {
						if(result.getString("Name").equals(name)) {
							debtTextField.setText(result.getString("Debt"));
						}
					}
				}catch(SQLException ex) {
					JOptionPane.showMessageDialog(PayDebtFrame.this, "SQL ERROR (ReaderNameSlelectHandler)");
				}
			} else {
				debtTextField.setText("");
				payTextField.setEditable(false);
			} //end if
			 
		}
		
	}
	
	private void initReaderNameComboBox() {
		try {
			Statement statement = mainFrame.connect.createStatement();
			ResultSet result = statement.executeQuery("SELECT * FROM Reader");
			while(result.next()) {
				String eDate = result.getDate("ExpireDate").toString();
				if(isCardStillHealthy(eDate)) {
					readerNameComboBox.addItem(result.getString("Name"));
				}
			}
		}catch(SQLException ex) {
			JOptionPane.showMessageDialog(PayDebtFrame.this, "SQL ERROR (initReaderNameComboBox)");
		}catch(ParseException ex) {
			JOptionPane.showMessageDialog(PayDebtFrame.this, "Parse Error()initReaderNameComboBox");
		}
	}
	
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
}
