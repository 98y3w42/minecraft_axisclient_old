package axis.management.managers;

import java.util.ArrayList;

import axis.management.ListManager;
import axis.util.Alt;

public final class AltManager extends ListManager {
   private Alt lastAlt;

   public Alt getLastAlt() {
      return this.lastAlt;
   }

   public void setLastAlt(Alt alt) {
      this.lastAlt = alt;
   }

   public void setup() {
      this.contents = new ArrayList();
   }
}
