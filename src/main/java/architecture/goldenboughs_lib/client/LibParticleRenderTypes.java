package architecture.goldenboughs_lib.client;

import architecture.goldenboughs_lib.core.GoldenBoughsLib;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureManager;

public final class LibParticleRenderTypes {
	public static final ParticleRenderType LOBOTOMY_CORPORATION_DAMAGE_ICON_PARTICLE = new ParticleRenderType() {
		@Override
		public BufferBuilder begin(Tesselator tesselator, TextureManager textureManager) {
			RenderSystem.disableDepthTest();
			RenderSystem.setShader(GameRenderer::getParticleShader);
			RenderSystem.setShaderTexture(0, TextureAtlas.LOCATION_PARTICLES);
			return tesselator.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.PARTICLE);
		}

		@Override
		public String toString() {
			return GoldenBoughsLib.modRlText("LOBOTOMY_CORPORATION_DAMAGE_ICON_PARTICLE");
		}
	};

	public static final ParticleRenderType TEXT_PARTICLE = new ParticleRenderType() {
		@Override
		public BufferBuilder begin(Tesselator tesselator, TextureManager textureManager) {
			RenderSystem.enableBlend();
			RenderSystem.defaultBlendFunc();
			RenderSystem.depthMask(false);
			RenderSystem.disableCull();
			return tesselator.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.PARTICLE);
		}

		@Override
		public String toString() {
			return GoldenBoughsLib.modRlText("TEXT_PARTICLE");
		}
	};

	public static final ParticleRenderType TEXT_PARTICLE_THROUGH = new ParticleRenderType() {
		@Override
		public BufferBuilder begin(Tesselator tesselator, TextureManager textureManager) {
			RenderSystem.enableBlend();
			RenderSystem.defaultBlendFunc();
			RenderSystem.disableDepthTest();
			RenderSystem.depthMask(false);
			RenderSystem.disableCull();
			return tesselator.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.PARTICLE);
		}

		@Override
		public String toString() {
			return GoldenBoughsLib.modRlText("TEXT_PARTICLE_THROUGH");
		}
	};

	public static final ParticleRenderType MAGIC_CIRCLE_PARTICLE = new ParticleRenderType() {
		@Override
		public BufferBuilder begin(Tesselator tesselator, TextureManager textureManager) {
			RenderSystem.depthMask(true);
			RenderSystem.setShaderTexture(0, TextureAtlas.LOCATION_PARTICLES);
			RenderSystem.disableCull();
			RenderSystem.defaultBlendFunc();
			return tesselator.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.PARTICLE);
		}

		@Override
		public String toString() {
			return GoldenBoughsLib.modRlText("MAGIC_CIRCLE_PARTICLE");
		}

		@Override
		public boolean isTranslucent() {
			return false;
		}
	};
}
