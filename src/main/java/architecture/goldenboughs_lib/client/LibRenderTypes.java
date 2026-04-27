package architecture.goldenboughs_lib.client;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.Util;
import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;

import java.util.function.BiFunction;

public final class LibRenderTypes {
	public static final BiFunction<ResourceLocation, RenderStateShard.TransparencyStateShard, RenderType> MAGIC_BULLET_MAGIC_CIRCLE = Util.memoize((resourceLocation, transparencyStateShard) ->
		RenderType.create("GoldenBoughsLib:magic_bullet_magic_circle", DefaultVertexFormat.NEW_ENTITY, VertexFormat.Mode.QUADS, 1536, false, true,
			RenderType.CompositeState.builder()
				.setShaderState(RenderStateShard.RENDERTYPE_EYES_SHADER)
				.setTextureState(new RenderStateShard.TextureStateShard(resourceLocation, false, false))
				.setTransparencyState(transparencyStateShard)
				.setWriteMaskState(RenderStateShard.COLOR_WRITE)
				.setCullState(RenderStateShard.NO_CULL)
				.createCompositeState(true)));

	public static final BiFunction<ResourceLocation, Float[], RenderType> GLOWMASK = Util.memoize((glowmaskTexture, value) ->
		RenderType.create("GoldenBoughsLib:glowmask", DefaultVertexFormat.NEW_ENTITY, VertexFormat.Mode.QUADS, 1536, false, true,
			RenderType.CompositeState.builder()
				.setColorLogicState(new RenderStateShard.ColorLogicStateShard("GoldenBoughsLib:glowmask_color",
					() -> {
						RenderSystem.disableColorLogicOp();
						RenderSystem.setShaderColor(value[0], value[0], value[0], 1);
					},
					() -> {
						RenderSystem.setShaderColor(1, 1, 1, 1);
					}))
				.setShaderState(RenderType.RENDERTYPE_EYES_SHADER)
				.setTextureState(new RenderStateShard.TextureStateShard(glowmaskTexture, false, false))
				.setTransparencyState(RenderStateShard.ADDITIVE_TRANSPARENCY)
				.setWriteMaskState(RenderType.COLOR_WRITE)
				.createCompositeState(true)));

	/**
	 * 通过输入数值和纹理生成发光纹理渲染类型，修改数组可以修改发光程度
	 */
	public static RenderType glowmask(Float[] value, ResourceLocation glowmaskTexture) {
		return GLOWMASK.apply(glowmaskTexture, value);
	}

	public static RenderType magicBulletMagicCircle(ResourceLocation location) {
		return MAGIC_BULLET_MAGIC_CIRCLE.apply(location, RenderStateShard.ADDITIVE_TRANSPARENCY);
	}
}
