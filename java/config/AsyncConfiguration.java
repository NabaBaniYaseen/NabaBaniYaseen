package config;

import java.util.concurrent.Executor;

import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

public class AsyncConfiguration {
	// this method used initialize ThreadPoolTaskExecutor
	public Executor taskExecutor() {
		ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
		executor.setCorePoolSize(ConfigurationReader.DBManagerCorePoolSize);
		executor.setMaxPoolSize(ConfigurationReader.DBManagerMaxPoolSize);
		executor.setQueueCapacity(ConfigurationReader.DBManagerQueueCapacity);
		executor.setThreadNamePrefix(ConfigurationReader.DBManagerThreadNamePrefix);
		executor.initialize();
		return executor;
	}

	/*
	 * I add main method to check if the values that comes from ConfigurationReader
	 * class come correctly
	 */
	public static void main(String[] args) {
		System.out.println(ConfigurationReader.DBManagerCorePoolSize);
		System.out.println(ConfigurationReader.DBManagerMaxPoolSize);
		System.out.println(ConfigurationReader.DBManagerQueueCapacity);
		System.out.println(ConfigurationReader.DBManagerThreadNamePrefix);
	}
}
