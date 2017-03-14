package axis.event.events;

import java.util.Iterator;

import axis.Axis;
import axis.event.Event;
import axis.module.Module;

public class KeyboardEvent extends Event
{
    public int key;

    public KeyboardEvent(final int key) {
        this.key = key;
        onKeyBoard();
    }

    private void onKeyBoard() {
 	   Iterator modulelist = Axis.getModuleManager().getContents().iterator();
 	   while (modulelist.hasNext())
        {
 		   Module modules = (Module)modulelist.next();

            if (modules.getKeybind() != 0 && key == modules.getKeybind())
            {
         	   modules.toggle();
            }
        }
    }
}
