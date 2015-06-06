import javax.swing.*;

public class CustomButton extends JButton {
	
	// Constructor method
	public CustomButton(String s) {
		
		// Disabling the default view
		setContentAreaFilled(false);
		setBorderPainted(false);
		
		// Loading the new icons and setting them
		ImageIcon up = new ImageIcon(s+"up.jpg");
		ImageIcon hover = new ImageIcon(s+"hover.jpg");
		ImageIcon down = new ImageIcon(s+"down.jpg");
		
		setIcon(up);
		setRolloverIcon(hover);
		setPressedIcon(down);
		
			
	}
	
}
