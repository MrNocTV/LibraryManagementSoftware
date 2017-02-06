import javax.swing.JFrame;


public class ContactInfoFrame extends JFrame {
	private MainFrame mainFrame;
	
	public ContactInfoFrame(MainFrame frame) {
		super("Contact Info");
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(400,300);
		setVisible(true);
		setResizable(false);
		setLocationRelativeTo(mainFrame);
	}
}
