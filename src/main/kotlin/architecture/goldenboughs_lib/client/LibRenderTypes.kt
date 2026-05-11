@file:Suppress("INFERRED_INVISIBLE_RETURN_TYPE_WARNING")

package architecture.goldenboughs_lib.client

import com.mojang.blaze3d.systems.RenderSystem
import com.mojang.blaze3d.vertex.DefaultVertexFormat.NEW_ENTITY
import com.mojang.blaze3d.vertex.VertexFormat.Mode
import net.minecraft.Util
import net.minecraft.client.renderer.RenderStateShard.*
import net.minecraft.client.renderer.RenderType
import net.minecraft.client.renderer.RenderType.CompositeState
import net.minecraft.client.renderer.RenderType.create
import net.minecraft.resources.ResourceLocation
import java.util.function.BiFunction

object LibRenderTypes {
	@JvmField
	val MAGIC_BULLET_MAGIC_CIRCLE: BiFunction<ResourceLocation, TransparencyStateShard, RenderType> =
		Util.memoize { resourceLocation: ResourceLocation, transparencyStateShard: TransparencyStateShard ->
			create(
				"GoldenBoughsLib:magic_bullet_magic_circle",
				NEW_ENTITY,
				Mode.QUADS,
				1536,
				false,
				true,
				CompositeState.builder()
					.setShaderState(RENDERTYPE_EYES_SHADER)
					.setTextureState(TextureStateShard(resourceLocation, false, false))
					.setTransparencyState(transparencyStateShard)
					.setWriteMaskState(COLOR_WRITE)
					.setCullState(NO_CULL)
					.createCompositeState(true)
			)
		}

	@JvmField
	val GLOWMASK: BiFunction<ResourceLocation, Array<Float>, RenderType> =
		Util.memoize { glowmaskTexture: ResourceLocation, value: Array<Float> ->
			create(
				"GoldenBoughsLib:glowmask", NEW_ENTITY, Mode.QUADS, 1536, false, true,
				CompositeState.builder()
					.setColorLogicState(
						ColorLogicStateShard(
							"GoldenBoughsLib:glowmask_color",
							{
								RenderSystem.disableColorLogicOp()
								RenderSystem.setShaderColor(value[0], value[0], value[0], 1f)
							},
							{
								RenderSystem.setShaderColor(1f, 1f, 1f, 1f)
							})
					)
					.setShaderState(RENDERTYPE_EYES_SHADER)
					.setTextureState(TextureStateShard(glowmaskTexture, false, false))
					.setTransparencyState(ADDITIVE_TRANSPARENCY)
					.setWriteMaskState(COLOR_WRITE)
					.createCompositeState(true)
			)
		}

	/**
	 * 通过输入数值和纹理生成发光纹理渲染类型，修改数组可以修改发光程度
	 */
	fun glowmask(value: Array<Float>, glowmaskTexture: ResourceLocation): RenderType {
		return GLOWMASK.apply(glowmaskTexture, value)
	}

	@JvmStatic
	fun magicBulletMagicCircle(location: ResourceLocation): RenderType {
		return MAGIC_BULLET_MAGIC_CIRCLE.apply(location, ADDITIVE_TRANSPARENCY)
	}
}
