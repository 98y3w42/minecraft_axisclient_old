package axis.util;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Random;

public class MathUtils {
	public static double round(double value, int places) {
		if (places < 0) {
			throw new IllegalArgumentException();
		}
		BigDecimal bd = new BigDecimal(value);
		bd = bd.setScale(places, RoundingMode.HALF_UP);
		return bd.doubleValue();
	}

	public static double roundToPlace(double value, int places)
	  {
	    if (places < 0) {
	      throw new IllegalArgumentException();
	    }
	    BigDecimal bd = new BigDecimal(value);
	    bd = bd.setScale(places, RoundingMode.HALF_UP);
	    return bd.doubleValue();
	  }

	  public static int customRandInt(int min, int max)
	  {
	    return new Random().nextInt(max - min + 1) + min;
	  }
}
