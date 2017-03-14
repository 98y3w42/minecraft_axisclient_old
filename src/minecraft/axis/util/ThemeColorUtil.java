package axis.util;

public class ThemeColorUtil {

	private int count =  0;
	private int wait = 0;
	private boolean f = true;
	private int oldcolor;

	public int getColor() {
		int color = 0;
		if (wait != 0) {
			wait--;
			return oldcolor;
		}

		wait = 2;

		if(count == 0) {
			color = 0xffffff;
			wait = 30;
			if(!f) {
				f = true;
			}
		} else if(count == 1) {
			color = 0xf2f2f2;
		} else if(count == 2) {
			color = 0xffffff;
		} else if(count == 3) {
			color = 0xe6e6e6;
		} else if(count == 4) {
			color = 0xd9d9d9;
		} else if(count == 5) {
			color = 0xcccccc;
		} else if(count == 6) {
			color = 0xbfbfbf;
		} else if(count == 7) {
			color = 0xb3b3b3;
		} else if(count == 8) {
			color = 0xa6a6a6;
		} else if(count == 9) {
			color = 0x999999;
		} else if(count == 10) {
			color = 0x8c8c8c;
		} else if(count == 11) {
			color = 0x808080;
		} else if(count == 12) {
			color = 0x737373;
		} else if(count == 13) {
			color = 0x666666;
		} else if(count == 14) {
			color = 0x595959;
		} else if(count == 15) {
			color = 0x4d4d4d;
		} else if(count == 16) {
			color = 0x404040;
		} else if(count == 17) {
			color = 0x333333;
		} else if(count == 18) {
			color = 0x262626;
		} else if(count == 19) {
			f = !f;
			wait = 10;
			color =  0x1a1a1a;
		}

		if (f && count  != 19) {
			count++;
		} else {
			count--;
		}

		oldcolor = color;
		return color;
	}

}
