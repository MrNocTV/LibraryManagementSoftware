import java.awt.Color;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;


public class AddUserFrame extends JFrame{
	private MainFrame mainFrame;
	private JTextField userNameTextField;
	private JPasswordField newPasswordField;
	private JPasswordField confirmPasswordField;
	private JButton updateButton;
	private JTable currentUserTable;
	private DefaultTableModel currentUserTableModel;
	
	public AddUserFrame(MainFrame mainFrame) {
		super("Add User");
		setLayout(null);
		this.mainFrame = mainFrame;
		JLabel userNameLabel = new JLabel("Username");
		userNameLabel.setSize(120, 20);
		userNameLabel.setLocation(10, 10);
		add(userNameLabel);
		JLabel newPasswordLabel = new JLabel("Password");
		newPasswordLabel.setSize(120, 20);
		newPasswordLabel.setLocation(10, 35);
		add(newPasswordLabel);
		JLabel confirmPasswordLabel = new JLabel("Confirm Password");
		confirmPasswordLabel.setSize(120, 20);
		confirmPasswordLabel.setLocation(10, 60);
		add(confirmPasswordLabel);
		userNameTextField = new JTextField();
		userNameTextField.setSize(150, 20);
		userNameTextField.setLocation(135, 10);
		add(userNameTextField);
		newPasswordField = new JPasswordField();
		newPasswordField.setSize(150, 20);
		newPasswordField.setLocation(135, 35);
		add(newPasswordField);
		confirmPasswordField = new JPasswordField();
		confirmPasswordField.setSize(150, 20);
		confirmPasswordField.setLocation(135, 60);
		add(confirmPasswordField);
		updateButton = new JButton("Add");
		updateButton.setSize(100,20);
		updateButton.setLocation(160, 85);
		add(updateButton);
		
		currentUserTable = new JTable(new DefaultTableModel(new Object[]{"Order","Username"}, 0)){
			@Override
			public boolean isCellEditable(int x, int y) {
				return false;
			}
		};
		currentUserTable.setGridColor(Color.BLACK);
		currentUserTable.setFillsViewportHeight(true);
		currentUserTable.setBackground(Color.LIGHT_GRAY);
		currentUserTableModel = (DefaultTableModel) currentUserTable.getModel();
		JScrollPane scrollPane = new JScrollPane(currentUserTable);
		scrollPane.setSize(275, 100);
		scrollPane.setLocation(10, 115);
		scrollPane.setBackground(null);
		scrollPane.setBorder(BorderFactory.createTitledBorder(
				null, "Current Users", TitledBorder.LEFT, TitledBorder.ABOVE_TOP));
		scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		
		add(scrollPane);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setVisible(true);
		setSize(295,240);
		setLocationRelativeTo(mainFrame);
		setResizable(false);
	}
	
}
