/*
 * DeviceHiveNotificationProcessor.java 
 * --------------------------------------------------------------------------------------
 * Copyright (c) Reveal Technologies, LLC. All rights reserved. http://www.reveal-tech.com
 *
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package com.sitewhere.devicehive;

import org.apache.log4j.Logger;
import org.mule.api.callback.SourceCallback;

import com.devicehive.model.DeviceNotification;
import com.hazelcast.core.Message;
import com.hazelcast.core.MessageListener;

/**
 * Called by Hazelcast when commands are published to the DeviceHive global bus.
 * 
 * @author Derek
 */
public class DeviceHiveNotificationProcessor implements MessageListener<DeviceNotification> {

	/** Static logger instance */
	private static Logger LOGGER = Logger.getLogger(DeviceHiveNotificationProcessor.class);

	/** Mule callback */
	private SourceCallback mule;

	public DeviceHiveNotificationProcessor(SourceCallback mule) {
		this.mule = mule;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.hazelcast.core.MessageListener#onMessage(com.hazelcast.core.Message)
	 */
	@Override
	public void onMessage(Message<DeviceNotification> notification) {
		try {
			mule.process(notification);
		} catch (Exception e) {
			LOGGER.error("Unable to process DeviceHive notification message.", e);
		}
	}
}