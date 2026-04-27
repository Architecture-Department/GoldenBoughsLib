package architecture.goldenboughs_lib.mixin.client;

import architecture.goldenboughs_lib.mixed.client.IModelBuilder;
import net.neoforged.neoforge.client.model.generators.ModelBuilder;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

import java.util.Map;

@Mixin(ModelBuilder.class)
public abstract class ModelBuilderMixin<T extends ModelBuilder<T>> implements IModelBuilder<T> {
	@Shadow
	@Final
	protected Map<String, String> textures;

	@Unique
	@Override
	public Map<String, String> goldenboughs_lib$getTexture() {
		return textures;
	}
}
