package architecture.goldenboughs_lib.client.particle.text

import architecture.goldenboughs_lib.api.AllOpen
import architecture.goldenboughs_lib.client.LibParticleRenderTypes
import architecture.goldenboughs_lib.client.particle.text.TextParticleStrokeType.*
import com.mojang.blaze3d.systems.RenderSystem
import com.mojang.blaze3d.vertex.PoseStack
import com.mojang.blaze3d.vertex.VertexConsumer
import com.mojang.math.Axis
import net.minecraft.client.Camera
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.Font
import net.minecraft.client.multiplayer.ClientLevel
import net.minecraft.client.particle.Particle
import net.minecraft.client.particle.ParticleRenderType
import net.minecraft.client.renderer.LightTexture
import net.minecraft.client.renderer.MultiBufferSource
import net.minecraft.network.chat.Component
import net.minecraft.util.Mth
import org.joml.Matrix4f

// TODO 拆分成伤害，BOSS说话文本，普通文本
@AllOpen
class TextParticle(
	level: ClientLevel, x: Double, y: Double, z: Double, options: TextParticleOptions
) : Particle(level, x, y, z) {
	companion object {
		private val DIRECTIONS = arrayOf(
			floatArrayOf(-1f, 1f),
			floatArrayOf(0f, 1f),
			floatArrayOf(1f, 1f),
			floatArrayOf(-1f, 0f),
			floatArrayOf(1f, 0f),
			floatArrayOf(-1f, -1f),
			floatArrayOf(0f, -1f),
			floatArrayOf(1f, -1f)
		)
	}

	protected val minecraft: Minecraft = Minecraft.getInstance()
	protected val font: Font = this.minecraft.font
	protected val displayMode: Font.DisplayMode =
		if (options.isThrough) Font.DisplayMode.SEE_THROUGH
		else Font.DisplayMode.NORMAL
	protected val alignType: TextParticleAlignType = options.alignType
	protected val isShine: Boolean = options.isShine
	protected val strokeType: TextParticleStrokeType = options.strokeType
	protected val isTargetingPlayers: Boolean = options.isTargetingPlayers
	protected val isThrough: Boolean = options.isThrough
	protected var baseSize: Float = options.size
	protected var textComponent: MutableList<Component> = options.textComponent.toMutableList()
	protected var strokeColor: Int = options.strokeColor
	protected var strokeColorO: Int = options.strokeColor
	protected var fontColor: Int = options.fontColor
	protected var fontColorO: Int = options.fontColor
	protected var size: Float = options.size
	protected var sizeO: Float = options.size
	protected var xRot: Float = options.xRot
	protected var xRotO: Float = options.xRot
	protected var yRot: Float = options.yRot
	protected var yRotO: Float = options.yRot

	init {
		setLifetime(options.particleLifeTime)
		setSize(options.size, options.size)
	}

	override fun setSize(width: Float, height: Float) {
		super.setSize(width, height)
	}

	override fun tick() {
		xo = x
		yo = y
		zo = z
		xRotO = xRot
		yRotO = yRot
		sizeO = size
		fontColorO = fontColor
		strokeColorO = strokeColor
		tickAge()
		age++
	}

	protected fun tickAge() {
		if (age >= lifetime) {
			this.remove()
		}
	}

	override fun render(vertexConsumer: VertexConsumer, camera: Camera, partialTicks: Float) {
		val textListSize = textComponent.size
		if (textListSize == 0) {
			return
		}

		setupBlendRenderState()

		val camPos = camera.position
		val getLightColor = getLightColor(partialTicks)
		val size = getSize(partialTicks)
		val x = getX(partialTicks)
		val y = getY(partialTicks)
		val z = getZ(partialTicks)

		val font = this.font
		val fontHeight = font.lineHeight
		val splitter = font.splitter

		val poseStack = PoseStack()
		poseStack.pushPose()
		poseStack.translate(-camPos.x + x, -camPos.y + y, -camPos.z + z)
		rotate(camera, partialTicks, poseStack)
		poseStack.scale(size, size, size)
		poseStack.pushPose()
		val bufferSource = this.minecraft.renderBuffers().bufferSource()

		val v = -(fontHeight / 2f + 1)
		var textY = textListSize * v
		for (component in this.textComponent) {
			val fontWidth = splitter.stringWidth(component)
			val textX: Float = when (this.alignType) {
				TextParticleAlignType.LEFT -> 0f
				TextParticleAlignType.CENTER -> -fontWidth / 2f
				TextParticleAlignType.RIGHT -> fontWidth
			}
			renderText(component, font, textX, textY, poseStack, bufferSource, getLightColor, partialTicks)
			textY -= v
		}

		poseStack.popPose()
		poseStack.popPose()
		bufferSource.endBatch()

		resetRenderState()
	}

	/**
	 * 配置文本粒子的混合渲染状态
	 */
	protected fun setupBlendRenderState() {
		RenderSystem.enableBlend()
		RenderSystem.defaultBlendFunc()
		if (this.isThrough) {
			RenderSystem.disableDepthTest()
		}
		RenderSystem.disableCull()
		RenderSystem.setShaderColor(rCol, gCol, bCol, alpha)
	}

	/**
	 * 恢复渲染状态到默认设置
	 */
	protected fun resetRenderState() {
		RenderSystem.setShaderColor(1f, 1f, 1f, 1f)
		if (this.isThrough) {
			RenderSystem.enableDepthTest()
		}
		RenderSystem.enableCull()
	}

	protected fun rotate(camera: Camera, partialTicks: Float, poseStack: PoseStack) {
		if (this.isTargetingPlayers) {
			poseStack.mulPose(camera.rotation())
			poseStack.mulPose(Axis.XP.rotationDegrees(180f))
		} else {
			poseStack.mulPose(Axis.YP.rotationDegrees(getYRot(partialTicks)))
			poseStack.mulPose(Axis.XP.rotationDegrees(getXRot(partialTicks)))
		}
	}

	protected fun getXRot(partialTicks: Float): Float {
		return Mth.lerp(partialTicks, this.xRotO, this.xRot)
	}

	protected fun getYRot(partialTicks: Float): Float {
		return Mth.lerp(partialTicks, this.yRotO, this.yRot)
	}

	protected fun renderText(
		text: Component,
		font: Font,
		textX: Float,
		textY: Float,
		poseStack: PoseStack,
		bufferSource: MultiBufferSource,
		getLightColor: Int,
		partialTicks: Float
	) {
		poseStack.pushPose()
		if (this.strokeType != NONE) {
			renderStroke(text, font, textX, textY, poseStack, bufferSource, getLightColor, getStrokeColor(partialTicks))
		}
		renderText(
			text,
			font,
			textX,
			textY,
			poseStack.last().pose(),
			bufferSource,
			getLightColor,
			getFontColor(partialTicks)
		)
		poseStack.popPose()
	}

	protected fun renderStroke(
		text: Component,
		font: Font,
		textX: Float,
		textY: Float,
		poseStack: PoseStack,
		bufferSource: MultiBufferSource,
		getLightColor: Int,
		strokeColor: Int
	) {
		poseStack.pushPose()
		if (this.isThrough) {
			poseStack.translate(0.0, 0.0, 1.5)
		} else {
			poseStack.translate(0.0, 0.0, 0.03)
		}
		when (this.strokeType) {
			SHADOW -> {
				poseStack.translate(1f, 1f, 0f)
				renderText(text, font, textX, textY, poseStack.last().pose(), bufferSource, getLightColor, strokeColor)
			}

			STROKE -> {
				for (dir in DIRECTIONS) {
					poseStack.pushPose()
					poseStack.translate(dir[0], dir[1], 0f)
					renderText(
						text,
						font,
						textX,
						textY,
						poseStack.last().pose(),
						bufferSource,
						getLightColor,
						strokeColor
					)
					poseStack.popPose()
				}
			}

			NONE -> TODO()
		}
		poseStack.popPose()
	}

	protected fun renderText(
		text: Component,
		font: Font,
		textX: Float,
		textY: Float,
		matrix: Matrix4f,
		bufferSource: MultiBufferSource,
		getLightColor: Int,
		fontColor: Int
	) {
		font.drawInBatch(text, textX, textY, fontColor, false, matrix, bufferSource, displayMode, 0, getLightColor)
	}

	protected fun getStrokeColor(partialTicks: Float): Int {
		return Mth.floor(Mth.lerp(partialTicks, this.strokeColorO.toFloat(), this.strokeColor.toFloat()))
	}

	protected fun getFontColor(partialTicks: Float): Int {
		return Mth.floor(Mth.lerp(partialTicks, this.fontColorO.toFloat(), this.fontColor.toFloat()))
	}

	protected fun getX(partialTicks: Float): Double {
		return Mth.lerp(partialTicks.toDouble(), this.xo, this.x)
	}

	protected fun getY(partialTicks: Float): Double {
		return Mth.lerp(partialTicks.toDouble(), this.yo, this.y)
	}

	protected fun getZ(partialTicks: Float): Double {
		return Mth.lerp(partialTicks.toDouble(), this.zo, this.z)
	}

	protected fun getSize(partialTicks: Float): Float {
		return Mth.lerp(partialTicks, this.sizeO, this.size)
	}

	override fun getLightColor(partialTick: Float): Int {
		return if (this.isShine) LightTexture.FULL_BRIGHT else super.getLightColor(partialTick)
	}

	fun setAllSize(size: Float) {
		setSize(size, size)
		this.sizeO = this.size
		this.size = size
	}

	override fun getRenderType(): ParticleRenderType {
		if (this.isThrough) {
			return LibParticleRenderTypes.TEXT_PARTICLE_THROUGH
		}
		return LibParticleRenderTypes.TEXT_PARTICLE
	}
}
