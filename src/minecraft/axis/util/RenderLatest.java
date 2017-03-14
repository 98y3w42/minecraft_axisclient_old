package axis.util;

import java.util.HashMap;
import java.util.Iterator;

import org.lwjgl.opengl.GL11;

public class RenderLatest {

	private static HashMap renderlist = new HashMap();

	public static void set(final int n, final boolean b) {
		renderlist.put(n, GL11.glGetBoolean(n));
		if (b) {
			GL11.glEnable(n);
		} else {
			GL11.glDisable(n);
		}
	}

	public static void setGL(final int n) {
		Boolean localBoolean = (Boolean)renderlist.get(Integer.valueOf(n));
		if (localBoolean != null) {
			if (localBoolean) {
				GL11.glEnable(n);
			} else {
				GL11.glDisable(n);
			}
		}
	}

	public static void onEnable(int paramInt) {
		set(paramInt, true);
	}

	public static void onDisable(int paramInt) {
		set(paramInt, false);
	}

	public static void test() {
		Iterator localIterator = renderlist.keySet().iterator();
		while (localIterator.hasNext()) {
			int i = ((Integer)localIterator.next()).intValue();
			setGL(i);
		}
	}
}
