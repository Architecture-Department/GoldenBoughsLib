package architecture.goldenboughs_lib.mixin.client;

import net.neoforged.neoforge.client.model.generators.ModelFile;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(ModelFile.class)
public interface ModelFileAccessorMixin {
	@Invoker("exists")
	boolean goldenboughs_lib$exists();
}
