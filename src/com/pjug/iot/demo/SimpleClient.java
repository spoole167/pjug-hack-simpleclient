package com.pjug.iot.demo;

import java.io.IOException;
import java.net.InetAddress;
import java.net.NetworkInterface;

import javax.xml.bind.DatatypeConverter;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.MqttTopic;

public class SimpleClient implements MqttCallback, Runnable {

	private static final String QUICKSTART = "tcp://quickstart.messaging.internetofthings.ibmcloud.com:1883";
	private static final String CLIENT_ID = buildID();
	private static final String TOPIC = "iot-2/evt/status/fmt/json";
	
	private MqttTopic topic = null;
	MqttClient mqttClient = null;

	public void run() {

		System.out.println("client started");
		MqttConnectOptions options = new MqttConnectOptions();

		options.setCleanSession(true);
		options.setKeepAliveInterval(30);

		try {

			mqttClient = new MqttClient(QUICKSTART, CLIENT_ID);
			mqttClient.setCallback(this);
			mqttClient.connect(options);

			System.out.println("Connected to quickstart broker");
			topic = mqttClient.getTopic(TOPIC);
			
		} catch (MqttException e) {
			e.printStackTrace();

		}
	}

	private static String buildID() {

		return "d:quickstart:pjug2015:" + getMacAddress();
	}

	@Override
	public void connectionLost(Throwable arg0) {
		System.out.println(arg0);

	}

	@Override
	public void deliveryComplete(IMqttDeliveryToken arg0) {
		
		System.out.println("sent " + arg0);

	}

	@Override
	public void messageArrived(String arg0, MqttMessage arg1) throws Exception {
		System.out.println("arrived"+arg0);
		System.out.println(arg1);
	}

	public void sendOn() {

		try {
			send("{ \"state\":\"1\"}");
		} catch (MqttException e) {

			e.printStackTrace();
		}

	}

	public void sendOff() {

		try {
		send("{ \"state\":\"0\"}");
		} catch(MqttException e) {
			e.printStackTrace();
		}
	}

	public void send(String msg) throws MqttException {
		MqttDeliveryToken token = null;

		MqttMessage message = new MqttMessage(msg.getBytes());
		message.setQos(0);
		message.setRetained(false);
		if(topic==null) {
			System.out.println("no topic");
			return;
		}
		token = topic.publish(message);
		token.waitForCompletion();

	}

	private static String getMacAddress() {
		String macHex = null;

		try {
			InetAddress ip = InetAddress.getLocalHost();
			System.out.println("local ip  address=" + ip.getHostAddress());
			NetworkInterface network = NetworkInterface.getByInetAddress(ip);
			byte[] mac = network.getHardwareAddress();
			macHex = DatatypeConverter.printHexBinary(mac);

		} catch (IOException ioe) {
			ioe.printStackTrace();
			macHex = "010203040506";
		}

		System.out.println("local mac address=" + macHex);
		return macHex;
	}

	public void close() {
		try {
			if(mqttClient.isConnected()) mqttClient.disconnect();
			mqttClient.close();
		} catch (MqttException e) {

			e.printStackTrace();
		}

	}
}
