package warlord;

import java.awt.Dimension;
import java.awt.Image;
import java.awt.Point;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;

public class ValidateFrame extends JFrame {

	JLabel label;
	JButton okButton;

	public ValidateFrame(){
		super("Validate String");
		label = new JLabel("Hello");
		this.add("North", label);
		okButton = new JButton("OK");
		this.add("South", okButton);
		this.setSize(new Dimension(300,300));
		this.setLocation(new Point(400,400));
	}

	public void setImage(Image image) {
		System.out.println("image null?"+(image == null));
		this.label.setIcon(new ImageIcon(image));
		this.setIconImage(image);
	}

	public String getValidateString() {
		return "????";
	}
}
