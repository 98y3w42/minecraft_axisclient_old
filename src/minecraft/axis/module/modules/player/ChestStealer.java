package axis.module.modules.player;

import axis.event.events.PacketReceiveEvent;
import axis.event.events.TickEvent;
import axis.management.managers.ModuleManager.Category;
import axis.module.Module;
import net.minecraft.client.gui.inventory.GuiChest;
import net.minecraft.inventory.Container;
import net.minecraft.network.play.server.S30PacketWindowItems;

public class ChestStealer extends Module{

    private S30PacketWindowItems packet;
    private boolean shouldEmptyChest;
    private int delay = 0;
    private int currentSlot;
    private int[] whitelist = new int[] {54};

    public ChestStealer()
    {
        super("ChestStealer", 0x00FF00, Category.PLAYER);
        setDisplayName("Chest Stealer");
    }

    private int getNextSlot(Container container)
    {
        int i = 0;

        for (int slotAmount = container.inventorySlots.size() == 90 ? 54 : 27; i < slotAmount; ++i)
        {
            if (container.getInventory().get(i) != null)
            {
                return i;
            }
        }

        return -1;
    }

    public boolean isContainerEmpty(Container container)
    {
        boolean temp = true;
        int i = 0;

        for (int slotAmount = container.inventorySlots.size() == 90 ? 54 : 27; i < slotAmount; ++i)
        {
            if (container.getSlot(i).getHasStack())
            {
                temp = false;
            }
        }

        return temp;
    }

    public void onTick(TickEvent event) {
    	try
        {
            if (!mc.inGameHasFocus && this.packet != null && mc.thePlayer.openContainer.windowId == this.packet.func_148911_c() && mc.currentScreen instanceof GuiChest)
            {
                if (!this.isContainerEmpty(mc.thePlayer.openContainer))
                {
                    int rec = this.getNextSlot(mc.thePlayer.openContainer);

                    if (this.delay >= 2)
                    {
                        mc.playerController.windowClick(mc.thePlayer.openContainer.windowId, rec, 0, 1, mc.thePlayer);
                        this.delay = 0;
                    }

                    ++this.delay;
                }
                else
                {
                    mc.thePlayer.closeScreen();
                    this.packet = null;
                }
            }
        }
        catch (Exception var3)
        {
            var3.printStackTrace();
        }
    }

    public void onPacketReceive(PacketReceiveEvent event) {
    	if (event.getPacket() instanceof S30PacketWindowItems)
        {
            this.packet = (S30PacketWindowItems)event.getPacket();
        }
    }
}
