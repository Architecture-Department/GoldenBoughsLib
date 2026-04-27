package architecture.goldenboughs_lib.mixed.client;

import architecture.goldenboughs_lib.api.NoMixinException;
import net.minecraft.client.KeyMapping;

public interface IKeyMapping {
	static IKeyMapping of(KeyMapping obj) {
		return obj;
	}

	default int goldenboughs_lib$getClickCount() {
		throw new NoMixinException();
	}
}
