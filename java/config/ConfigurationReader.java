package config;

import java.io.*;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;

public class ConfigurationReader {
	/* Multithreading parameteres */
	public final static int DBManagerMaxPoolSize = readDBManagerMaxPoolSize();
	public final static int DBManagerCorePoolSize = readDBManagerCorePoolSize();
	public final static int DBManagerQueueCapacity = readDBManagerQueueCapacity();
	public final static String DBManagerThreadNamePrefix = readDBManagerThreadNamePrefix();

	/*
	 * when I creat instance from ConfigurationReader the Multithreading parameteres
	 * will set to values in configuration file
	 */
	/* this method used to read configrartion from file */
	private static ArrayList<String> readConfiguration() {
		try {
			String fileName = "Configuration.txt";
			File file = getFileFromResource(fileName);
			BufferedReader br = new BufferedReader(new FileReader(file));
			ArrayList<String> array = new ArrayList<String>();
			String st;
			while ((st = br.readLine()) != null) {
				array.add(st);
			}
			return array;
		} catch (IOException | URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	// read DBManagerMaxPoolSize from configrution file
	private static int readDBManagerMaxPoolSize() {
		ArrayList<String> array = readConfiguration();
		for (int i = 0; i < array.size(); i++) {
			String[] arrayOfString = array.get(i).split(" ");
			for (int j = 0; j < arrayOfString.length - 1; j++) {
				String value = arrayOfString[2];
				String Key = arrayOfString[0];
				if (Key.contains("DBManagerMaxPoolSize")) {
					return Integer.parseInt(value);
				}
			}
		}
		return 0;
	}

	// read DBManagerCorePoolSize from configrution file
	private static int readDBManagerCorePoolSize() {
		ArrayList<String> array = readConfiguration();
		for (int i = 0; i < array.size(); i++) {
			String[] arrayOfString = array.get(i).split(" ");
			for (int j = 0; j < arrayOfString.length - 1; j++) {
				String value = arrayOfString[2];
				String Key = arrayOfString[0];
				if (Key.contains("DBManagerCorePoolSize")) {
					return Integer.parseInt(value);
				}
			}
		}
		return 0;
	}

	// read DBManagerQueueCapacity from configrution file
	private static int readDBManagerQueueCapacity() {
		ArrayList<String> array = readConfiguration();
		for (int i = 0; i < array.size(); i++) {
			String[] arrayOfString = array.get(i).split(" ");
			for (int j = 0; j < arrayOfString.length - 1; j++) {
				String value = arrayOfString[2];
				String Key = arrayOfString[0];
				if (Key.contains("DBManagerQueueCapacity")) {
					return Integer.parseInt(value);
				}
			}
		}
		return 0;
	}

	// read BManagerQueueCapacity from configrution file
	private static String readDBManagerThreadNamePrefix() {
		ArrayList<String> array = readConfiguration();
		for (int i = 0; i < array.size(); i++) {
			String[] arrayOfString = array.get(i).split(" ");
			for (int j = 0; j < arrayOfString.length - 1; j++) {
				String value = arrayOfString[2];
				String Key = arrayOfString[0];
				if (Key.contains("DBManagerThreadNamePrefix")) {
					return value;
				}
			}
		}
		return null;
	}

	/*
	 * The resource URL is not working in the JAR If we try to access a file that is
	 * inside a JAR, It throws NoSuchFileException (linux), InvalidPathException
	 * (Windows)
	 */
	private static File getFileFromResource(String fileName) throws URISyntaxException {

		ClassLoader classLoader = (ConfigurationReader.class).getClassLoader();
		URL resource = classLoader.getResource(fileName);
		if (resource == null) {
			throw new IllegalArgumentException("file not found! " + fileName);
		} else {

			// failed if files have whitespaces or special characters
			// return new File(resource.getFile());

			return new File(resource.toURI());
		}

	}

}