package com.pjug.iot.demo;

public class Main {

	public static void main(String[] args) throws Exception {
		
		SimpleClient client=new SimpleClient();
		SimpleUI ui=new SimpleUI(client);
		ui.show();
	
	}

}
