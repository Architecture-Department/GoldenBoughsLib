package architecture.goldenboughs_lib.mixin.client;

import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.client.model.generators.ModelProvider;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(ModelProvider.class)
public interface ModelProviderAccessor {
	@Invoker
	ResourceLocation callExtendWithFolder(ResourceLocation rl);
}
