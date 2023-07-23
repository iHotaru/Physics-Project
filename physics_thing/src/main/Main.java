package main;

import javax.swing.JFrame;

public class Main {
	
	public static void main(String[] args) {
		
		//creates a window and applies some properties to it
		JFrame window = new JFrame();
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setResizable(false);
		window.setTitle("Physics Thing");
		
		WindowPanel windowPanel = new WindowPanel();
		window.add(windowPanel);
		
		window.pack();
		
		window.setLocation(520, 100);
		window.setVisible(true);
		
		//starts thread in WindowPanel (program loop)
		windowPanel.startWindowThread();
	}

}
