package architecture.goldenboughs_lib.mixed;

import net.neoforged.neoforge.common.damagesource.DamageContainer;

public interface IDamageContainer {
	static IDamageContainer of(DamageContainer obj) {
		return (IDamageContainer) obj;
	}
}
