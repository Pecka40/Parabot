package org.parabot.core;

import java.applet.Applet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.TimerTask;

import org.parabot.core.asm.ASMClassLoader;
import org.parabot.core.bot.loader.BotLoader;
import org.parabot.core.classpath.ClassPath;
import org.parabot.core.paint.PaintDebugger;
import org.parabot.core.parsers.HookParser;
import org.parabot.core.ui.BotUI;
import org.parabot.core.ui.components.GamePanel;
import org.parabot.environment.api.interfaces.Paintable;
import org.parabot.environment.input.Keyboard;
import org.parabot.environment.input.Mouse;
import org.parabot.environment.scripts.Script;
import org.parabot.environment.servers.ServerProvider;

/**
 * Game context
 * 
 * @author Clisprail
 * 
 */
public class Context {
	public static HashMap<ThreadGroup, Context> threadGroups = new HashMap<ThreadGroup, Context>();
	private static int id = 1;

	private ASMClassLoader classLoader = null;
	private ClassPath classPath = null;
	private ServerProvider serverProvider = null;
	private int tab = 0;
	private Applet gameApplet = null;
	private HookParser hookParser = null;
	private Script runningScript = null;

	private Object clientInstance = null;

	private static ArrayList<Paintable> paintables = new ArrayList<Paintable>();

	private PaintDebugger paintDebugger = new PaintDebugger();

	public boolean added = false;
	
	private Mouse mouse = null;
	private Keyboard keyboard = null;

	public Context(final ServerProvider serverProvider) {
		threadGroups.put(Thread.currentThread().getThreadGroup(), this);
		tab = id;
		this.serverProvider = serverProvider;
		id++;
		this.classPath = new ClassPath();
	}
	
	/**
	 * Resolves the context from threadgroup
	 * 
	 * @return context
	 */
	public static Context resolve() {
		return threadGroups.get(Thread.currentThread().getThreadGroup());
	}
	
	public static Context currentTab() {
		// TODO
		return threadGroups.values().iterator().next();
	}

	/**
	 * Sets the ServerProvider class loader
	 * 
	 * @param serverEnvironment
	 */
	public void setEnvironment(ASMClassLoader serverEnvironment) {
		classLoader = new BotLoader(classPath, serverEnvironment);
	}

	/**
	 * Sets the main client instance
	 */
	public void setClientInstance(Object object) {
		this.clientInstance = object;
	}
	
	/**
	 * Sets the hook parser
	 * @param hookParser
	 */
	public void setHookParser(final HookParser hookParser) {
		this.hookParser = hookParser;
	}
	
	/**
	 * Sets the mouse
	 * @param mouse
	 */
	public void setMouse(final Mouse mouse) {
		this.mouse = mouse;
	}
	
	/**
	 * Gets the mouse
	 * @return mouse
	 */
	public Mouse getMouse() {
		return mouse;
	}
	
	
	/**
	 * Sets the keyboard
	 * @param keyboard
	 */
	public void setKeyboard(final Keyboard keyboard) {
		this.keyboard = keyboard;
	}
	
	/**
	 * Gets the keyboard
	 * @return keyboard
	 */
	public Keyboard getKeyboard() {
		return keyboard;
	}

	/**
	 * ClassPath
	 * 
	 * @return classpath
	 */
	public ClassPath getClassPath() {
		return classPath;
	}

	/**
	 * Determines if applet has been set
	 * 
	 * @return <b>true</b> if set
	 */
	public boolean appletSet() {
		return gameApplet != null;
	}

	/**
	 * Gets game applet
	 * 
	 * @return applet
	 */
	public Applet getApplet() {
		return gameApplet;
	}

	/**
	 * Loads the game
	 */
	public void load() {
		serverProvider.parseJar();
		serverProvider.injectHooks();
		gameApplet = serverProvider.fetchApplet();
		if (getClient() == null) {
			setClientInstance(gameApplet);
		}
		serverProvider.addMenuItems(BotUI.getInstance().getJMenuBar());
		BotUI.getInstance().validate();
		final GamePanel panel = GamePanel.getInstance();
		panel.removeLoader();
		panel.setContext(this);
		gameApplet.setSize(765, 503);
		java.util.Timer t = new java.util.Timer();
		t.schedule(new TimerTask() {
			@Override
			public void run() {
				gameApplet.setBounds(0, 0, 765, 503);
			}
		}, 1000);
		serverProvider.initMouse();
		serverProvider.initKeyboard();
	}

	/**
	 * Gets the server prodiver belonging to this context
	 * 
	 * @return server provider
	 */
	public ServerProvider getServerProvider() {
		return serverProvider;
	}

	/**
	 * Gets class loader from this context
	 * 
	 * @return class loader
	 */
	public ASMClassLoader getASMClassLoader() {
		return classLoader;
	}

	/**
	 * Gets the id of this context
	 * 
	 * @return id context
	 */
	public static int getID() {
		return id;
	}

	/**
	 * Tab id of this context
	 * 
	 * @return tab id of this context
	 */
	public int getTab() {
		return tab;
	}

	/**
	 * Adds a paintable instance to the paintables
	 * 
	 * @param paintable
	 */
	public void addPaintable(Paintable paintable) {
		paintables.add(paintable);
	}

	/**
	 * Removes a paintable instance from the paintables
	 * 
	 * @param paintable
	 */
	public void removePaintable(Paintable paintable) {
		paintables.remove(paintable);
	}

	/**
	 * Gets the paintable instances
	 * 
	 * @return array of paintable instances
	 */
	public Paintable[] getPaintables() {
		return paintables.toArray(new Paintable[paintables.size()]);
	}

	/**
	 * The client debug painter
	 * 
	 * @return debug painter
	 */
	public PaintDebugger getPaintDebugger() {
		return paintDebugger;
	}

	/**
	 * Gets the main/client instance
	 * 
	 * @return instance of the the client
	 */
	public Object getClient() {
		return this.clientInstance;
	}
	
	/**
	 * Gets the hook parser, may be null if injection is not used or a custom hook parser is used for injecting
	 * @return hook parser
	 */
	public HookParser getHookParser() {
		return hookParser;
	}
	
	/**
	 * Sets the current running script, if a script stops it will call this method with a null argument
	 * @param script
	 */
	public void setRunningScript(final Script script) {
		this.runningScript = script;
	}
	
	/**
	 * Gets the current running script
	 * @return script
	 */
	public Script getRunningScript() {
		return this.runningScript;
	}

}
