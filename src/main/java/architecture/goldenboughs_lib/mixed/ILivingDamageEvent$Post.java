package architecture.goldenboughs_lib.mixed;

import architecture.goldenboughs_lib.api.NoMixinException;
import net.neoforged.neoforge.common.damagesource.DamageContainer;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;

public interface ILivingDamageEvent$Post {
	static ILivingDamageEvent$Post of(LivingDamageEvent.Post obj) {
		return (ILivingDamageEvent$Post) obj;
	}

	default DamageContainer goldenboughs_lib$getDamageContainer() {
		throw new NoMixinException();
	}
}
