/*
 * DeviceHiveCommandProcessor.java 
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

import com.devicehive.model.DeviceCommand;
import com.hazelcast.core.Message;
import com.hazelcast.core.MessageListener;

public class DeviceHiveCommandProcessor implements MessageListener<DeviceCommand> {

	/** Static logger instance */
	private static Logger LOGGER = Logger.getLogger(DeviceHiveCommandProcessor.class);

	/** Mule callback */
	private SourceCallback mule;

	public DeviceHiveCommandProcessor(SourceCallback mule) {
		this.mule = mule;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.hazelcast.core.MessageListener#onMessage(com.hazelcast.core.Message)
	 */
	@Override
	public void onMessage(Message<DeviceCommand> command) {
		try {
			mule.process(command);
		} catch (Exception e) {
			LOGGER.error("Unable to process DeviceHive command message.", e);
		}
	}
}