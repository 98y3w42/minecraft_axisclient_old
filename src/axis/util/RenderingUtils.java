package axis.util;

import java.awt.Color;

public class RenderingUtils {
	public static Color rainbow(long offset, float fade, float alpha) {
		float hue = (float) (System.nanoTime() + offset) / 1.0E10F % 1.0F;
		long color = Long.parseLong(Integer.toHexString(Integer.valueOf(Color.HSBtoRGB(hue, 1.0F, 1.0F)).intValue()),
				16);
		Color c = new Color((int) color);
		return new Color(c.getRed() / 255.0F * fade, c.getGreen() / 255.0F * fade, c.getBlue() / 255.0F * fade,
				alpha / 255.0F);
	}
}
