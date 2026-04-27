package architecture.goldenboughs_lib.core;

import architecture.goldenboughs_lib.event.rationality.RationalityEvent;
import net.minecraft.world.entity.player.Player;
import net.neoforged.fml.ModLoader;

import java.util.Map;

public final class LibEventHooks {
	private LibEventHooks() {
	}

	public static Map.Entry<Boolean, Float> sourceRationalityPre(Player player, float oldValue, float newValue) {
		var event = new RationalityEvent.Modify.Pre(player, oldValue, newValue);
		ModLoader.postEvent(event);
		var canceled = event.isCanceled();
		return Map.entry(canceled,
			canceled ? event.getOldValue() : event.getNewValue());
	}

	public static void sourceRationalityPost(Player player, float oldValue, float newValue) {
		ModLoader.postEvent(new RationalityEvent.Modify.Post(player, oldValue, newValue));
	}
}
