package architecture.goldenboughs_lib.client.particle.text;

import architecture.imaginarycraft.client.ModParticleRenderTypes;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.StringSplitter;
import net.minecraft.client.gui.Font;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.joml.Matrix4f;

import java.util.List;

// TODO 拆分成伤害，BOSS说话文本，普通文本
public class TextParticle extends Particle {
	private static final float[][] DIRECTIONS = {{-1, 1}, {0, 1}, {1, 1}, {-1, 0}, {1, 0}, {-1, -1}, {0, -1}, {1, -1}};

	protected final Minecraft minecraft;
	protected final Font font;
	protected final Font.DisplayMode displayMode;
	protected final TextParticleAlignType alignType;
	protected final boolean isShine;
	protected final TextParticleStrokeType strokeType;
	protected final boolean isTargetingPlayers;
	protected final boolean isThrough;
	protected float baseSize;
	protected List<Component> textComponent;
	protected int strokeColor, strokeColorO;
	protected int fontColor, fontColorO;
	protected float size, sizeO;
	protected float xRot, xRotO;
	protected float yRot, yRotO;

	protected TextParticle(ClientLevel level, double x, double y, double z, TextParticleOptions options) {
		super(level, x, y, z);
		this.minecraft = Minecraft.getInstance();
		this.font = this.minecraft.font;
		this.displayMode = options.isThrough() ? Font.DisplayMode.SEE_THROUGH : Font.DisplayMode.NORMAL;
		setLifetime(options.particleLifeTime());
		setSize(options.size(), options.size());
		this.alignType = options.alignType();
		this.isShine = options.isShine();
		this.strokeType = options.strokeType();
		this.isTargetingPlayers = options.isTargetingPlayers();
		this.isThrough = options.isThrough();

		this.textComponent = options.textComponent();
		this.xRot = options.xRot();
		this.xRotO = options.xRot();
		this.yRot = options.yRot();
		this.yRotO = options.yRot();
		this.baseSize = options.size();
		this.size = options.size();
		this.sizeO = options.size();
		this.fontColor = options.fontColor();
		this.fontColorO = options.fontColor();
		this.strokeColor = options.strokeColor();
		this.strokeColorO = options.strokeColor();
	}

	@Override
	protected void setSize(float width, float height) {
		super.setSize(width, height);
	}

	@Override
	public void tick() {
		xo = x;
		yo = y;
		zo = z;
		xRotO = xRot;
		yRotO = yRot;
		sizeO = size;
		fontColorO = fontColor;
		strokeColorO = strokeColor;
		tickAge();
		age++;
	}

	protected void tickAge() {
		if (age >= lifetime) {
			this.remove();
		}
	}

	@Override
	public void render(@NotNull VertexConsumer vertexConsumer, Camera camera, float partialTicks) {
		int textListSize = textComponent.size();
		if (textListSize == 0) {
			return;
		}

		setupBlendRenderState();

		Vec3 camPos = camera.getPosition();
		int getLightColor = getLightColor(partialTicks);
		float size = getSize(partialTicks);
		double x = getX(partialTicks);
		double y = getY(partialTicks);
		double z = getZ(partialTicks);

		Font font = this.font;
		int fontHeight = font.lineHeight;
		StringSplitter splitter = font.getSplitter();

		PoseStack poseStack = new PoseStack();
		poseStack.pushPose();
		poseStack.translate(-camPos.x + x, -camPos.y + y, -camPos.z + z);
		rotate(camera, partialTicks, poseStack);
		poseStack.scale(size, size, size);
		poseStack.pushPose();
		MultiBufferSource.BufferSource bufferSource = this.minecraft.renderBuffers().bufferSource();

		float v = -(fontHeight / 2f + 1);
		float textY = textListSize * v;
		for (Component component : this.textComponent) {
			float fontWidth = splitter.stringWidth(component);
			float textX = switch (this.alignType) {
				case LEFT -> 0;
				case CENTER -> -fontWidth / 2f;
				case RIGHT -> fontWidth;
			};
			renderText(component, font, textX, textY, poseStack, bufferSource, getLightColor, partialTicks);
			textY -= v;
		}

		poseStack.popPose();
		poseStack.popPose();
		bufferSource.endBatch();

		resetRenderState();
	}

	/**
	 * 配置文本粒子的混合渲染状态
	 */
	protected void setupBlendRenderState() {
		RenderSystem.enableBlend();
		RenderSystem.defaultBlendFunc();
		if (this.isThrough) {
			RenderSystem.disableDepthTest();
		}
		RenderSystem.disableCull();
		RenderSystem.setShaderColor(rCol, gCol, bCol, alpha);
	}

	/**
	 * 恢复渲染状态到默认设置
	 */
	protected void resetRenderState() {
		RenderSystem.setShaderColor(1, 1, 1, 1);
		if (this.isThrough) {
			RenderSystem.enableDepthTest();
		}
		RenderSystem.enableCull();
	}

	protected void rotate(Camera camera, float partialTicks, PoseStack poseStack) {
		if (this.isTargetingPlayers) {
			poseStack.mulPose(camera.rotation());
			poseStack.mulPose(Axis.XP.rotationDegrees(180));
		} else {
			poseStack.mulPose(Axis.YP.rotationDegrees(getYRot(partialTicks)));
			poseStack.mulPose(Axis.XP.rotationDegrees(getXRot(partialTicks)));
		}
	}

	protected float getXRot(float partialTicks) {
		return Mth.lerp(partialTicks, this.xRotO, this.xRot);
	}

	protected float getYRot(float partialTicks) {
		return Mth.lerp(partialTicks, this.yRotO, this.yRot);
	}

	protected void renderText(Component text, Font font, float textX, float textY, PoseStack poseStack, MultiBufferSource bufferSource, int getLightColor, float partialTicks) {
		poseStack.pushPose();
		if (this.strokeType != TextParticleStrokeType.NONE) {
			renderStroke(text, font, textX, textY, poseStack, bufferSource, getLightColor, getStrokeColor(partialTicks));
		}
		renderText(text, font, textX, textY, poseStack.last().pose(), bufferSource, getLightColor, getFontColor(partialTicks));
		poseStack.popPose();
	}

	protected void renderStroke(Component text, Font font, float textX, float textY, PoseStack poseStack, MultiBufferSource bufferSource, int getLightColor, int strokeColor) {
		poseStack.pushPose();
		if (this.isThrough) {
			poseStack.translate(0, 0, 1.5);
		} else {
			poseStack.translate(0, 0, 0.03);
		}
		switch (this.strokeType) {
			case SHADOW -> {
				poseStack.translate(1, 1, 0);
				renderText(text, font, textX, textY, poseStack.last().pose(), bufferSource, getLightColor, strokeColor);
			}
			case STROKE -> {
				for (float[] dir : DIRECTIONS) {
					poseStack.pushPose();
					poseStack.translate(dir[0], dir[1], 0);
					renderText(text, font, textX, textY, poseStack.last().pose(), bufferSource, getLightColor, strokeColor);
					poseStack.popPose();
				}
			}
		}
		poseStack.popPose();
	}

	protected void renderText(Component text, Font font, float textX, float textY, Matrix4f matrix, MultiBufferSource bufferSource, int getLightColor, int fontColor) {
		font.drawInBatch(text, textX, textY, fontColor, false, matrix, bufferSource, displayMode, 0, getLightColor);
	}

	protected int getStrokeColor(float partialTicks) {
		return Mth.floor(Mth.lerp(partialTicks, this.strokeColorO, this.strokeColor));
	}

	protected int getFontColor(float partialTicks) {
		return Mth.floor(Mth.lerp(partialTicks, this.fontColorO, this.fontColor));
	}

	protected double getX(float partialTicks) {
		return Mth.lerp(partialTicks, this.xo, this.x);
	}

	protected double getY(float partialTicks) {
		return Mth.lerp(partialTicks, this.yo, this.y);
	}

	protected double getZ(float partialTicks) {
		return Mth.lerp(partialTicks, this.zo, this.z);
	}

	protected float getSize(float partialTicks) {
		return Mth.lerp(partialTicks, this.sizeO, this.size);
	}

	@Override
	protected int getLightColor(float partialTick) {
		return this.isShine ? LightTexture.FULL_BRIGHT : super.getLightColor(partialTick);
	}

	public void setSize(float size) {
		setSize(size, size);
		this.sizeO = this.size;
		this.size = size;
	}

	@Override
	public @NotNull ParticleRenderType getRenderType() {
		if (this.isThrough) {
			return ModParticleRenderTypes.TEXT_PARTICLE_THROUGH;
		}
		return ModParticleRenderTypes.TEXT_PARTICLE;
	}
}
