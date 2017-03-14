package axis.module.modules.combat.killaura;

import axis.Axis;
import axis.event.events.PacketSentEvent;
import axis.event.events.Render3DEvent;
import axis.event.events.UpdateEvent;
import axis.module.modules.combat.KillAura;
import axis.module.modules.combat.killaura.modes.Multi;
import axis.module.modules.combat.killaura.modes.Switch;
import net.minecraft.client.Minecraft;
import net.minecraft.network.play.client.C03PacketPlayer;

public abstract class AuraMode {
    private String name = "";
    protected final KillAura killAura;
    protected final Minecraft mc = Minecraft.getMinecraft();

    public AuraMode(String name, KillAura killAura) {
        this.name = name;
        this.killAura = killAura;
    }

    public String getName() {
        return name;
    }

	public abstract void onPacketSent(PacketSentEvent event);

    public abstract void onUpdate(UpdateEvent event);

    public abstract void onMotionPacket(C03PacketPlayer packet);

    public abstract void onRender(Render3DEvent event);

    public abstract void onDisabled();

	public static AuraMode getMode(String name, KillAura killaura) {
		if (name.indexOf("Multi") != -1) {
			return new Multi(killaura);
		} else if(name.indexOf("Switch") != -1){
			return new Switch(killaura);
		}
		return ((KillAura)Axis.getModuleManager().getModuleByName("KillAura")).currentMode.getDefaultValue();
	}
}
