package org.parabot.core;

import java.io.File;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.Random;

import javax.swing.JFileChooser;

import org.parabot.environment.OperatingSystem;

/**
 * 
 * @author Clisprail
 * @author Matt
 * 
 */
public class Directories {

	private static Map<String, File> cached = new HashMap<String, File>();

	static {

		switch (OperatingSystem.getOS()) {
			case WINDOWS:
				cached.put("Root", new JFileChooser().getFileSystemView().getDefaultDirectory());
				break;
			default:
				cached.put("Root", new File(System.getProperty("user.home")));
		}

		cached.put("Root", getDefaultDirectory());
		cached.put("Workspace", new File(cached.get("Root"), "/Parabot/"));
		cached.put("Sources", new File(cached.get("Root"), "/Parabot/scripts/sources/"));
		cached.put("Compiled", new File(cached.get("Root"), "/Parabot/scripts/compiled/"));
		cached.put("Resources", new File(cached.get("Root"), "/Parabot/scripts/resources/"));
		cached.put("Settings", new File(cached.get("Root"), "/Parabot/settings/"));
		cached.put("Servers", new File(cached.get("Root"), "/Parabot/servers/"));
	}

	/**
	 * Returns the root directory outside of the main Parabot folder.
	 * 
	 * @return
	 */
	public static File getDefaultDirectory() {
		return cached.get("Root");
	}

	/**
	 * Returns the Parabot folder.
	 * 
	 * @return
	 */
	public static File getWorkspace() {
		return cached.get("Workspace");
	}

	/**
	 * Returns the script sources folder.
	 * 
	 * @return
	 */
	public static File getScriptSourcesPath() {
		return cached.get("Sources");
	}

	/**
	 * Returns the compiled scripts folder.
	 * 
	 * @return
	 */
	public static File getScriptCompiledPath() {
		return cached.get("Compiled");
	}

	/**
	 * Returns the scripts resources folder.
	 * 
	 * @return
	 */
	public static File getResourcesPath() {
		return cached.get("Resources");
	}

	/**
	 * Returns the Parabot settings folder.
	 * 
	 * @return
	 */
	public static File getSettingsPath() {
		return cached.get("Settings");
	}

	/**
	 * Returns the Parabot servers folder.
	 * 
	 * @return
	 */
	public static File getServerPath() {
		return cached.get("Servers");
	}

	/**
	 * Validates all directories and makes them if necessary
	 */
	public static void validate() {
		final File defaultPath = getDefaultDirectory();
		if (defaultPath == null || !defaultPath.exists()) {
			throw new RuntimeException("Default path not found");
		}
		final Queue<File> files = new LinkedList<File>();
		files.add(getWorkspace());
		files.add(getServerPath());
		files.add(getSettingsPath());
		files.add(getScriptSourcesPath());
		files.add(getScriptCompiledPath());
		files.add(getResourcesPath());
		while (files.size() > 0) {
			final File file = files.poll();
			if (!file.exists()) {
				file.mkdirs();
			}
		}
	}

	private static File temp = null;

	public static File getTempDirectory() {
		if (temp != null) {
			return temp;
		}
		int randomNum = new Random().nextInt(999999999);
		temp = new File(getResourcesPath(), randomNum + "/");
		temp.mkdirs();
		temp.deleteOnExit();
		return temp;
	}

}
