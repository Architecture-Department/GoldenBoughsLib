package architecture.goldenboughs_lib.client

import architecture.goldenboughs_lib.util.LibUtil
import com.mojang.blaze3d.systems.RenderSystem
import com.mojang.blaze3d.vertex.BufferBuilder
import com.mojang.blaze3d.vertex.DefaultVertexFormat
import com.mojang.blaze3d.vertex.Tesselator
import com.mojang.blaze3d.vertex.VertexFormat
import net.minecraft.client.particle.ParticleRenderType
import net.minecraft.client.renderer.GameRenderer
import net.minecraft.client.renderer.texture.TextureAtlas
import net.minecraft.client.renderer.texture.TextureManager

object LibParticleRenderTypes {
	@JvmField
	val LOBOTOMY_CORPORATION_DAMAGE_ICON_PARTICLE: ParticleRenderType = object : ParticleRenderType {
		override fun begin(tesselator: Tesselator, textureManager: TextureManager): BufferBuilder {
			RenderSystem.disableDepthTest()
			RenderSystem.setShader { GameRenderer.getParticleShader() }
			RenderSystem.setShaderTexture(0, TextureAtlas.LOCATION_PARTICLES)
			return tesselator.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.PARTICLE)
		}

		override fun toString(): String {
			return LibUtil.modRlText("LOBOTOMY_CORPORATION_DAMAGE_ICON_PARTICLE")
		}
	}

	@JvmField
	val TEXT_PARTICLE: ParticleRenderType = object : ParticleRenderType {
		override fun begin(tesselator: Tesselator, textureManager: TextureManager): BufferBuilder {
			RenderSystem.enableBlend()
			RenderSystem.defaultBlendFunc()
			RenderSystem.depthMask(false)
			RenderSystem.disableCull()
			return tesselator.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.PARTICLE)
		}

		override fun toString(): String {
			return LibUtil.modRlText("TEXT_PARTICLE")
		}
	}

	@JvmField
	val TEXT_PARTICLE_THROUGH: ParticleRenderType = object : ParticleRenderType {
		override fun begin(tesselator: Tesselator, textureManager: TextureManager): BufferBuilder {
			RenderSystem.enableBlend()
			RenderSystem.defaultBlendFunc()
			RenderSystem.disableDepthTest()
			RenderSystem.depthMask(false)
			RenderSystem.disableCull()
			return tesselator.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.PARTICLE)
		}

		override fun toString(): String {
			return LibUtil.modRlText("TEXT_PARTICLE_THROUGH")
		}
	}

	@JvmField
	val MAGIC_CIRCLE_PARTICLE: ParticleRenderType = object : ParticleRenderType {
		override fun begin(tesselator: Tesselator, textureManager: TextureManager): BufferBuilder {
			RenderSystem.depthMask(true)
			RenderSystem.setShaderTexture(0, TextureAtlas.LOCATION_PARTICLES)
			RenderSystem.disableCull()
			RenderSystem.defaultBlendFunc()
			return tesselator.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.PARTICLE)
		}

		override fun toString(): String {
			return LibUtil.modRlText("MAGIC_CIRCLE_PARTICLE")
		}

		override fun isTranslucent(): Boolean {
			return false
		}
	}
}
