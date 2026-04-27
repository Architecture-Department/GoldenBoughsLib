package architecture.goldenboughs_lib.mixin.client;

import architecture.goldenboughs_lib.mixed.client.IKeyMapping;
import net.minecraft.client.KeyMapping;
import net.neoforged.neoforge.client.extensions.IKeyMappingExtension;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(KeyMapping.class)
public abstract class KeyMappingMixin implements Comparable<KeyMapping>, IKeyMappingExtension, IKeyMapping {
	@Shadow
	private int clickCount;

	@Override
	public int goldenboughs_lib$getClickCount() {
		return clickCount;
	}
}
