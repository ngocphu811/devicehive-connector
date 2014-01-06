/*
 * DeviceHiveTest.java 
 * --------------------------------------------------------------------------------------
 * Copyright (c) Reveal Technologies, LLC. All rights reserved. http://www.reveal-tech.com
 *
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package com.sitewhere.devicehive;

import java.net.URI;
import java.net.URISyntaxException;
import java.sql.Timestamp;
import java.util.Date;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

import com.devicehive.client.api.client.Client;
import com.devicehive.client.model.Device;
import com.devicehive.client.model.DeviceClass;
import com.devicehive.client.model.DeviceNotification;
import com.devicehive.client.model.Network;
import com.devicehive.client.model.Transport;

/**
 * Used to send data into DeviceHive to test connector.
 * 
 * @author Derek
 */
public class DeviceHiveTest {

	/** Device id for tests */
	private static String DEVICE_ID = "123-TEST-2384783";

	/** Network key for tests */
	private static String NETWORK_KEY = "SW_NETWORK_KEY";

	/** Device key for tests */
	private static String DEVICE_KEY = "SW_DEVICE_KEY";

	/** DeviceHive client */
	private Client client;

	@Before
	public void initialize() {
		try {
			this.client = new Client(new URI("http://localhost:8080/hive/rest"), Transport.REST_ONLY);
			client.authenticate("dhadmin", "dhadmin_#911");
		} catch (URISyntaxException e) {
			Assert.fail(e.getMessage());
		}
	}

	// @Test
	public void testCreateNetwork() {
		Network network = new Network();
		network.setId(0L);
		network.setName("SiteWhere");
		network.setKey(NETWORK_KEY);
		network.setDescription("SiteWhere Network");
		client.getNetworkController().insertNetwork(network);
	}

	// @Test
	public void testCreateDevice() {
		Device device = new Device();
		device.setId(DEVICE_ID);
		device.setName("SiteWhere Device");
		device.setKey(DEVICE_KEY);
		DeviceClass devclass = new DeviceClass();
		devclass.setName("SiteWhere Device Class");
		devclass.setOfflineTimeout(600000);
		devclass.setPermanent(true);
		devclass.setVersion("1");
		device.setDeviceClass(devclass);
		client.getDeviceController().registerDevice(DEVICE_ID, device);
	}

	@Test
	public void testCreateNotification() {
		DeviceNotification notification = new DeviceNotification();
		notification.setTimestamp(new Timestamp((new Date()).getTime()));
		notification.setNotification("You have been notified.");
		client.getNotificationsController().insertNotification(DEVICE_ID, notification);
	}
}