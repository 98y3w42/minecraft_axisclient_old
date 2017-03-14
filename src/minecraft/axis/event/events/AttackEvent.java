package axis.event.events;

import axis.event.Event;
import net.minecraft.entity.Entity;

public class AttackEvent extends Event {

	private final Entity entity;

	public AttackEvent(Entity targetEntity) {
		this.entity = targetEntity;
	}

	public Entity getEntity() {
		return entity;
	}

}
