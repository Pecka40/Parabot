package org.parabot.core.paint;

import java.awt.Color;
import java.awt.Graphics;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;

import org.parabot.core.Context;

/**
 * 
 * @author Clisprail
 *
 */
public class PaintDebugger {
	private final HashMap<String, AbstractDebugger> debuggers = new HashMap<String, AbstractDebugger>();
	private final Queue<String> stringDebug = new LinkedList<String>();
	
	public final void addDebugger(final String name, final AbstractDebugger debugger) {
		debuggers.put(name, debugger);
	}
	
	public void debug(Graphics g) {
		for(final AbstractDebugger d : debuggers.values()) {
			if(d.isEnabled()) {
				d.paint(g);
			}
		}
		g.setColor(Color.green);
		int y = 40;
		while(stringDebug.size() > 0) {
			g.drawString(stringDebug.poll(), 10, y);
			y += 15;
		}
	}
	
	public static final PaintDebugger getInstance() {
		return Context.resolve().getPaintDebugger();
	}
	
	public final void addLine(final String debugLine) {
		stringDebug.add(debugLine);
	}
	
	public final void toggle(final String name) {
		debuggers.get(name).toggle();
	}
	
	public final boolean isEnabled(final String name) {
		return debuggers.get(name).isEnabled();
	}

}
