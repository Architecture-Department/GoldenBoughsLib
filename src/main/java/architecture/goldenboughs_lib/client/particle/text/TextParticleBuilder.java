package architecture.goldenboughs_lib.client.particle.text;

import net.minecraft.network.chat.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TextParticleBuilder {
	protected List<Component> textComponent = new ArrayList<>();
	protected int fontColor = 0xffffff;
	protected int strokeColor = 0xafafafaf;
	/**
	 * 持续时间
	 */
	protected int particleLifeTime = 20 * 3;
	/**
	 * 文字大小
	 */
	protected float size = 0.02f;
	/**
	 * 文字对齐方式
	 */
	protected TextParticleAlignType alignType = TextParticleAlignType.CENTER;
	/**
	 * 是否发光
	 */
	protected boolean isShine;
	/**
	 * 阴影类型
	 */
	protected TextParticleStrokeType strokeType = TextParticleStrokeType.NONE;
	protected float xRot;
	protected float yRot;
	/**
	 * 是否面向玩家
	 */
	protected boolean isTargetingPlayers;
	/**
	 * 是否顶层渲染
	 */
	protected boolean isSeeThrough;

	protected TextParticleBuilder(
		List<Component> textComponent,
		int fontColor,
		int strokeColor,
		int particleLifeTime,
		float size,
		TextParticleAlignType alignType,
		boolean isShine,
		TextParticleStrokeType strokeType,
		float xRot,
		float yRot,
		boolean isTargetingPlayers,
		boolean isSeeThrough
	) {
		this.textComponent = textComponent;
		this.fontColor = fontColor;
		this.strokeColor = strokeColor;
		this.particleLifeTime = particleLifeTime;
		this.size = size;
		this.alignType = alignType;
		this.isShine = isShine;
		this.strokeType = strokeType;
		this.xRot = xRot;
		this.yRot = yRot;
		this.isTargetingPlayers = isTargetingPlayers;
		this.isSeeThrough = isSeeThrough;
	}

	public TextParticleBuilder() {
	}

	public TextParticleBuilder addTextComponent(Component... textComponent) {
		this.textComponent.addAll(Arrays.stream(textComponent).toList());
		return this;
	}

	public TextParticleBuilder setTextComponent(Component... textComponent) {
		this.textComponent = List.of(textComponent);
		return this;
	}

	/**
	 * 文字对齐方式
	 */
	public TextParticleBuilder align(TextParticleAlignType alignType) {
		this.alignType = alignType;
		return this;
	}

	/**
	 * 文字颜色
	 */
	public TextParticleBuilder fontColor(int fontColor) {
		this.fontColor = fontColor;
		return this;
	}

	/**
	 * 描边颜色
	 */
	public TextParticleBuilder strokeColor(int strokeColor) {
		this.strokeColor = strokeColor;
		return this;
	}

	/**
	 * 粒子持续时间
	 */
	public TextParticleBuilder particleLifeTime(int particleLifeTime) {
		this.particleLifeTime = particleLifeTime;
		return this;
	}

	/**
	 * 描边类型
	 */
	public TextParticleBuilder strokeType(TextParticleStrokeType strokeType) {
		this.strokeType = strokeType;
		return this;
	}

	/**
	 * 是否发光
	 */
	public TextParticleBuilder shine(boolean isShine) {
		this.isShine = isShine;
		return this;
	}

	/**
	 * 是否顶层渲染
	 */
	public TextParticleBuilder seeThrough(boolean isSeeThrough) {
		this.isSeeThrough = isSeeThrough;
		return this;
	}

	public TextParticleBuilder size(float size) {
		this.size = size;
		return this;
	}

	public TextParticleBuilder xRot(float xRot) {
		this.xRot = xRot;
		return this;
	}

	public TextParticleBuilder yRot(float yRot) {
		this.yRot = yRot;
		return this;
	}

	public TextParticleBuilder targetingPlayers(boolean targetingPlayers) {
		this.isTargetingPlayers = targetingPlayers;
		return this;
	}

	public TextParticleOptions buildOptions() {
		return new TextParticleOptions(
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
		);
	}
}
