package architecture.goldenboughs_lib.client.renderer;

import architecture.goldenboughs_lib.client.model.entity.EmptyEntityModel;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Mob;
import org.jetbrains.annotations.NotNull;

public class EmptyMobRenderer<T extends Mob> extends MobRenderer<T, EmptyEntityModel<T>> {
	private final ResourceLocation texture;

	public EmptyMobRenderer(EntityRendererProvider.Context context, float shadowRadius, ResourceLocation texture) {
		super(context, new EmptyEntityModel<>(), shadowRadius);
		this.texture = texture;
	}

	@Override
	public @NotNull ResourceLocation getTextureLocation(@NotNull T entity) {
		return texture;
	}
}
