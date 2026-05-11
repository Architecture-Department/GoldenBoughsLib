package architecture.goldenboughs_lib.client.particle.text

import net.minecraft.network.chat.Component
import java.util.*

open class TextParticleBuilder {
	@JvmField
	protected var textComponent: MutableList<Component> = ArrayList()

	@JvmField
	protected var fontColor: Int = 0xffffff

	@JvmField
	protected var strokeColor: Int = -0x50505051

	/**
	 * 持续时间
	 */
	@JvmField
	protected var particleLifeTime: Int = 20 * 3

	/**
	 * 文字大小
	 */
	@JvmField
	protected var size: Float = 0.02f

	/**
	 * 文字对齐方式
	 */
	@JvmField
	protected var alignType: TextParticleAlignType = TextParticleAlignType.CENTER

	/**
	 * 是否发光
	 */
	@JvmField
	protected var isShine: Boolean = false

	/**
	 * 阴影类型
	 */
	@JvmField
	protected var strokeType: TextParticleStrokeType = TextParticleStrokeType.NONE

	@JvmField
	protected var xRot: Float = 0f

	@JvmField
	protected var yRot: Float = 0f

	/**
	 * 是否面向玩家
	 */
	@JvmField
	protected var isTargetingPlayers: Boolean = false

	/**
	 * 是否顶层渲染
	 */
	@JvmField
	protected var isSeeThrough: Boolean = false

	constructor(
		textComponent: MutableList<Component>,
		fontColor: Int,
		strokeColor: Int,
		particleLifeTime: Int,
		size: Float,
		alignType: TextParticleAlignType,
		isShine: Boolean,
		strokeType: TextParticleStrokeType,
		xRot: Float,
		yRot: Float,
		isTargetingPlayers: Boolean,
		isSeeThrough: Boolean
	) {
		this.textComponent = textComponent
		this.fontColor = fontColor
		this.strokeColor = strokeColor
		this.particleLifeTime = particleLifeTime
		this.size = size
		this.alignType = alignType
		this.isShine = isShine
		this.strokeType = strokeType
		this.xRot = xRot
		this.yRot = yRot
		this.isTargetingPlayers = isTargetingPlayers
		this.isSeeThrough = isSeeThrough
	}

	constructor()

	fun addTextComponent(vararg textComponent: Component): TextParticleBuilder {
		this.textComponent.addAll(Arrays.stream(textComponent).toList())
		return this
	}

	fun setTextComponent(vararg textComponent: Component): TextParticleBuilder {
		this.textComponent = mutableListOf(*textComponent)
		return this
	}

	/**
	 * 文字对齐方式
	 */
	fun align(alignType: TextParticleAlignType): TextParticleBuilder {
		this.alignType = alignType
		return this
	}

	/**
	 * 文字颜色
	 */
	fun fontColor(fontColor: Int): TextParticleBuilder {
		this.fontColor = fontColor
		return this
	}

	/**
	 * 描边颜色
	 */
	fun strokeColor(strokeColor: Int): TextParticleBuilder {
		this.strokeColor = strokeColor
		return this
	}

	/**
	 * 粒子持续时间
	 */
	fun particleLifeTime(particleLifeTime: Int): TextParticleBuilder {
		this.particleLifeTime = particleLifeTime
		return this
	}

	/**
	 * 描边类型
	 */
	fun strokeType(strokeType: TextParticleStrokeType): TextParticleBuilder {
		this.strokeType = strokeType
		return this
	}

	/**
	 * 是否发光
	 */
	fun shine(isShine: Boolean): TextParticleBuilder {
		this.isShine = isShine
		return this
	}

	/**
	 * 是否顶层渲染
	 */
	fun seeThrough(isSeeThrough: Boolean): TextParticleBuilder {
		this.isSeeThrough = isSeeThrough
		return this
	}

	fun size(size: Float): TextParticleBuilder {
		this.size = size
		return this
	}

	fun xRot(xRot: Float): TextParticleBuilder {
		this.xRot = xRot
		return this
	}

	fun yRot(yRot: Float): TextParticleBuilder {
		this.yRot = yRot
		return this
	}

	fun targetingPlayers(targetingPlayers: Boolean): TextParticleBuilder {
		this.isTargetingPlayers = targetingPlayers
		return this
	}

	fun buildOptions(): TextParticleOptions {
		return TextParticleOptions(
			textComponent,
			fontColor,
			strokeColor,
			particleLifeTime,
			size,
			alignType,
			isShine,
			strokeType,
			xRot,
			yRot,
			isTargetingPlayers,
			isSeeThrough
		)
	}
}
