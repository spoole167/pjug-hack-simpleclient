package com.pjug.iot.demo;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class SimpleUI implements ActionListener {

	public static final String ON="on";
	public static final String OFF="off";
	private SimpleClient client;
	public SimpleUI(SimpleClient client) {
		this.client=client;
	}




	public  void gui() {
		
		JFrame f=new JFrame("pjug hack");
		f.setSize(500, 250);
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		JPanel p=new JPanel();
		p.setLayout(new GridLayout(0,2));
		JButton jb=new JButton(ON);
		jb.setOpaque(true);
		jb.setBackground(Color.BLACK);
		jb.setForeground(Color.BLACK);
		jb.addActionListener(this);
		p.add(jb);
		jb=new JButton(OFF);
		jb.setOpaque(true);
		jb.setBackground(Color.BLACK);
		jb.setForeground(Color.BLACK);
		
		jb.addActionListener(this);
		p.add(jb);
		
		f.setContentPane(p);
		f.addWindowListener(new WindowAdapter() {
			public void windowClosing(java.awt.event.WindowEvent windowEvent) {
				client.close();
			}
		});
		f.setVisible(true);
	}
	
	
	

	@Override
	public void actionPerformed(ActionEvent e) {
		String command=e.getActionCommand();
		switch(command) {
		case ON:
			System.out.println("turn light on");
			client.sendOn();
			break;
		case OFF:
			System.out.println("turn light off");
			client.sendOff();
			break;
			
		}
		
		
	}




	public void show() throws Exception {
		
		gui();
		new Thread(client).start();
		
		
	}
}
