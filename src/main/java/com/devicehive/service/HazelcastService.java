package com.devicehive.service;

import static javax.ejb.ConcurrencyManagementType.BEAN;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.ConcurrencyManagement;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hazelcast.client.HazelcastClient;
import com.hazelcast.client.config.ClientConfig;
import com.hazelcast.config.Config;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.ITopic;
import com.hazelcast.core.Message;
import com.hazelcast.core.MessageListener;

@Singleton
@ConcurrencyManagement(BEAN)
@Startup
public class HazelcastService {

	private static final Logger logger = LoggerFactory.getLogger(HazelcastService.class);

	private HazelcastInstance hazelcast;

	/** Test client */
	private HazelcastInstance client;

	@PostConstruct
	protected void postConstruct() {
		logger.debug("Initializing Hazelcast instance...");

		Config config = new Config();
		config.getNetworkConfig().setPort(54701);
		config.getGroupConfig().setName("DeviceHive");
		config.getGroupConfig().setPassword("DeviceHive");
		hazelcast = Hazelcast.newHazelcastInstance(config);

		logger.debug("New Hazelcast instance created: " + hazelcast);

		ClientConfig clientConfig = new ClientConfig();
		clientConfig.getGroupConfig().setName("DeviceHive");
		clientConfig.getGroupConfig().setPassword("DeviceHive");
		clientConfig.addAddress("localhost:54701");
		clientConfig.setSmartRouting(true);
		this.client = HazelcastClient.newHazelcastClient(clientConfig);

		ITopic<?> topic = client.getTopic("DEVICE_NOTIFICATION");
		topic.addMessageListener(new NotificationMessageListener());
	}

	@PreDestroy
	protected void preDestroy() {
		hazelcast.getLifecycleService().shutdown();
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public HazelcastInstance getHazelcast() {
		return hazelcast;
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
				logger.info("Received notification.");
			} catch (Exception e) {
				logger.error("Unable to process device notification message.");
			}
		}
	}
}
