package axis.module.modules.render;

import axis.Axis;
import axis.management.managers.ModuleManager.Category;
import axis.module.Module;

public class FullBright extends Module implements Runnable {

	public static boolean value = true;

	public FullBright() {
	      super("FullBright");
	      this.setCategory(Category.RENDER);
	}

	public void onEnabled()
    {
        super.onEnabled();

        if (mc.theWorld != null)
        {
        	this.value = true;
        	new Thread(new FullBright()).start();
        }
    }

	public void onDisabled()
    {
        super.onDisabled();

        if (mc.theWorld != null)
        {
        	this.value = false;
        	new Thread(new FullBright()).start();
        }
    }

	@Override
	public void run() {
		if (value) {
	    	for (;mc.gameSettings.gammaSetting < 10.0F;) {
	    		if (Axis.getAxis().getModuleManager().getModuleByName("FullBright").isEnabled()) {
	    			mc.gameSettings.gammaSetting += 10.0F;
	    		} else {
	    			break;
	    		}
	    		try {
					Thread.sleep(80);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
	    	}
		}
		if (!value) {
			for (;mc.gameSettings.gammaSetting > 0.5F;) {
	    		if (!Axis.getAxis().getModuleManager().getModuleByName("FullBright").isEnabled()) {
	    			mc.gameSettings.gammaSetting -= 1.0F;
	    		} else {
	    			break;
	    		}
	    		try {
					Thread.sleep(80);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
	    	}
		}
	}

}
