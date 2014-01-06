/*
 * HazelcastTest.java 
 * --------------------------------------------------------------------------------------
 * Copyright (c) Reveal Technologies, LLC. All rights reserved. http://www.reveal-tech.com
 *
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package com.sitewhere.devicehive;

import org.junit.Before;

import com.hazelcast.client.HazelcastClient;
import com.hazelcast.client.config.ClientConfig;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.ITopic;
import com.hazelcast.core.Message;
import com.hazelcast.core.MessageListener;

/**
 * Test direct Hazelcast access to DeviceHive.
 * 
 * @author Derek
 */
public class HazelcastTest {

	/** Hazelcast instance for DeviceHive */
	private HazelcastInstance instance;

	/** Hazelcast client for DeviceHive */
	private HazelcastInstance client;

	@Before
	public void initialize() {
		// Config config = new Config();
		// config.getNetworkConfig().setPort(55701);
		//
		// Collection<Integer> outboundPorts = new ArrayList<Integer>();
		// outboundPorts.add(0);
		// config.getNetworkConfig().setOutboundPorts(outboundPorts);
		//
		// JoinConfig join = new JoinConfig();
		// TcpIpConfig tcpip = new TcpIpConfig();
		// tcpip.setEnabled(false);
		// join.setTcpIpConfig(tcpip);
		// MulticastConfig multicastConfig = new MulticastConfig();
		// multicastConfig.setEnabled(false);
		// join.setMulticastConfig(multicastConfig);
		// AwsConfig awsConfig = new AwsConfig();
		// awsConfig.setEnabled(false);
		// join.setAwsConfig(awsConfig);
		// config.getNetworkConfig().setJoin(join);
		//
		// InterfacesConfig interfaces = new InterfacesConfig();
		// interfaces.setEnabled(false);
		// config.getNetworkConfig().setInterfaces(interfaces);
		//
		// SerializationConfig serializationConfig = new SerializationConfig();
		// serializationConfig.setPortableVersion(0);
		// config.setSerializationConfig(serializationConfig);
		//
		// ServicesConfig servicesConfig = new ServicesConfig();
		// servicesConfig.setEnableDefaults(true);
		// config.setServicesConfig(servicesConfig);
		//
		// config.getGroupConfig().setName("DeviceHive");
		// config.getGroupConfig().setPassword("DeviceHive");
		// this.instance = Hazelcast.newHazelcastInstance(config);

		ClientConfig clientConfig = new ClientConfig();
		clientConfig.getGroupConfig().setName("DeviceHive");
		clientConfig.getGroupConfig().setPassword("DeviceHive");
		clientConfig.addAddress("localhost:54701");
		clientConfig.setSmartRouting(true);
		this.client = HazelcastClient.newHazelcastClient(clientConfig);
	}

	// @Test
	public void testPubSub() {
		ITopic<?> topic = client.getTopic("DEVICE_NOTIFICATION");
		topic.addMessageListener(new NotificationMessageListener());

		// ITopic<DeviceNotification> stopic = instance.getTopic("DEVICE_NOTIFICATION");
		// for (int i = 0; i < 10; i++) {
		// DeviceNotification notification = new DeviceNotification();
		// notification.setId((long) i);
		// notification.setNotification("Something happened.");
		// stopic.publish(notification);
		// }

		try {
			Thread.sleep(60 * 1000);
		} catch (InterruptedException e) {
		}
	}

	private static class NotificationMessageListener implements MessageListener {

		/*
		 * (non-Javadoc)
		 * 
		 * @see com.hazelcast.core.MessageListener#onMessage(com.hazelcast.core.Message)
		 */
		@Override
		public void onMessage(Message notification) {
			try {
				System.out.println("Received notification.");
			} catch (Exception e) {
				System.out.println("Unable to process device notification message.");
			}
		}
	}
}