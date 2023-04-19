package com.xuan.dialog;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

public class DialogUtil {

	private void show(String message) throws Exception {
		JFrame f = new JFrame();
		f.setLocation(100, 100);
		f.setSize(1000, 600);
		//f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		JLabel jl = new JLabel(message, SwingConstants.CENTER);
		jl.setText(message);
		Font font = new Font(Font.SERIF, Font.BOLD, 64);
		jl.setFont(font);
		//jl.setForeground(Color.red);
		jl.setBounds(100, 100, 1000, 600);
		f.add(jl);
		f.setVisible(true);
		for (int i = 0; i < 3; i++) {
			Toolkit.getDefaultToolkit().beep();
			Thread.sleep(1500);
		}
		Thread.sleep(9 * 1000);
		f.dispose();
	}
  public static synchronized void showDialog(String message) throws Exception {
	    new DialogUtil().show(message);
  }
  public static void main(String[] args) throws Exception {
	  showDialog("DialogUtil main");
  }
}
