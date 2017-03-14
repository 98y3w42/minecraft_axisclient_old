package axis.util;

import java.awt.Color;

public enum ColorCode {
	BLACK("0", color(0, 0, 0, 255)), DARK_BLUE("1", color(0, 0, 170, 255)), DARK_GREEN("2", color(0, 170, 0,
			255)), DARK_AQUA("3", color(0, 170, 170, 255)), DARK_RED("4", color(170, 0, 0, 255)), DARK_PURPLE(
					"5",
					color(170, 0, 170, 255)), GOLD("6", color(255, 170, 0, 255)), GRAY("7", color(170, 170, 170,
							255)), DARK_GRAY("8", color(85, 85, 85, 255)), BLUE("9", color(85, 85, 255, 255)), GREEN(
									"a",
									color(85, 255, 85, 255)), AQUA("b", color(85, 255, 255, 255)), RED("c", color(255,
											85, 85, 255)), LIGHT_PURPLE("d", color(255, 85, 255, 255)), YELLOW("e",
													color(255, 255, 85, 255)), WHITE("f", color(255, 255, 255, 255));

	int code;
	String name;

	private ColorCode(String name, int code) {
		this.name = name;
		this.code = code;
	}

	public Color getColor() {
		float f = (this.code >> 16 & 255) / 255.0F;
		float f1 = (code >> 8 & 255) / 255.0F;
		float f2 = (code & 255) / 255.0F;
		return new Color(f, f1, f2, 255);
	}

	public int getCode() {
		return this.code;
	}

	public String getformat() {
		return this.name;
	}

	public static int color(int r, int g, int b, int a) {
		return new Color(r, g, b, a).getRGB();
	}
}
