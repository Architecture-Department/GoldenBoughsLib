package architecture.goldenboughs_lib.mixed.client

import architecture.goldenboughs_lib.api.NoMixinException
import com.mojang.blaze3d.vertex.VertexConsumer
import net.minecraft.client.gui.Font
import net.minecraft.network.chat.Component
import net.minecraft.util.FormattedCharSequence
import org.joml.Matrix4f

/**
 * 字体渲染扩展接口，提供额外的字体绘制功能
 */
interface IFont {
	/**
	 * 批量绘制文本（字符串版本）
	 *
	 * @param text              要绘制的文本
	 * @param x                 文本绘制的X坐标
	 * @param y                 文本绘制的Y坐标
	 * @param color             文本颜色
	 * @param dropShadow        是否绘制阴影
	 * @param matrix            变换矩阵
	 * @param vertexConsumer    顶点消费者
	 * @param backgroundColor   背景颜色
	 * @param packedLightCoords 打包的光照坐标
	 * @return 绘制后的X坐标
	 */
	fun `goldenboughs_lib$drawInBatch`(
		text: String,
		x: Float,
		y: Float,
		color: Int,
		dropShadow: Boolean,
		matrix: Matrix4f,
		vertexConsumer: VertexConsumer,
		backgroundColor: Int,
		packedLightCoords: Int
	): Int {
		throw NoMixinException()
	}

	/**
	 * 批量绘制文本（字符串版本，带双向控制）
	 *
	 * @param text              要绘制的文本
	 * @param x                 文本绘制的X坐标
	 * @param y                 文本绘制的Y坐标
	 * @param color             文本颜色
	 * @param dropShadow        是否绘制阴影
	 * @param matrix            变换矩阵
	 * @param vertexConsumer    顶点消费者
	 * @param backgroundColor   背景颜色
	 * @param packedLightCoords 打包的光照坐标
	 * @param bidirectional     是否启用双向文本处理
	 * @return 绘制后的X坐标
	 */
	fun `goldenboughs_lib$drawInBatch`(
		text: String,
		x: Float,
		y: Float,
		color: Int,
		dropShadow: Boolean,
		matrix: Matrix4f,
		vertexConsumer: VertexConsumer,
		backgroundColor: Int,
		packedLightCoords: Int,
		bidirectional: Boolean
	): Int {
		throw NoMixinException()
	}

	/**
	 * 批量绘制文本（组件版本）
	 *
	 * @param text              要绘制的文本组件
	 * @param x                 文本绘制的X坐标
	 * @param y                 文本绘制的Y坐标
	 * @param color             文本颜色
	 * @param dropShadow        是否绘制阴影
	 * @param matrix            变换矩阵
	 * @param vertexConsumer    顶点消费者
	 * @param backgroundColor   背景颜色
	 * @param packedLightCoords 打包的光照坐标
	 * @return 绘制后的X坐标
	 */
	fun `goldenboughs_lib$drawInBatch`(
		text: Component,
		x: Float,
		y: Float,
		color: Int,
		dropShadow: Boolean,
		matrix: Matrix4f,
		vertexConsumer: VertexConsumer,
		backgroundColor: Int,
		packedLightCoords: Int
	): Int {
		throw NoMixinException()
	}

	/**
	 * 批量绘制文本（格式化字符序列版本）
	 *
	 * @param text              要绘制的格式化字符序列
	 * @param x                 文本绘制的X坐标
	 * @param y                 文本绘制的Y坐标
	 * @param color             文本颜色
	 * @param dropShadow        是否绘制阴影
	 * @param matrix            变换矩阵
	 * @param vertexConsumer    顶点消费者
	 * @param backgroundColor   背景颜色
	 * @param packedLightCoords 打包的光照坐标
	 * @return 绘制后的X坐标
	 */
	fun `goldenboughs_lib$drawInBatch`(
		text: FormattedCharSequence,
		x: Float,
		y: Float,
		color: Int,
		dropShadow: Boolean,
		matrix: Matrix4f,
		vertexConsumer: VertexConsumer,
		backgroundColor: Int,
		packedLightCoords: Int
	): Int {
		throw NoMixinException()
	}

	/**
	 * 批量绘制8x轮廓文本
	 *
	 * @param text              要绘制的格式化字符序列
	 * @param x                 文本绘制的X坐标
	 * @param y                 文本绘制的Y坐标
	 * @param color             文本颜色
	 * @param backgroundColor   背景颜色
	 * @param matrix            变换矩阵
	 * @param vertexConsumer    顶点消费者
	 * @param packedLightCoords 打包的光照坐标
	 */
	fun `goldenboughs_lib$drawInBatch8xOutline`(
		text: FormattedCharSequence,
		x: Float,
		y: Float,
		color: Int,
		backgroundColor: Int,
		matrix: Matrix4f,
		vertexConsumer: VertexConsumer,
		packedLightCoords: Int
	) {
		throw NoMixinException()
	}

	/**
	 * 内部绘制文本（字符串版本，带双向控制）
	 *
	 * @param text              要绘制的文本
	 * @param x                 文本绘制的X坐标
	 * @param y                 文本绘制的Y坐标
	 * @param color             文本颜色
	 * @param dropShadow        是否绘制阴影
	 * @param matrix            变换矩阵
	 * @param vertexConsumer    顶点消费者
	 * @param backgroundColor   背景颜色
	 * @param packedLightCoords 打包的光照坐标
	 * @param bidirectional     是否启用双向文本处理
	 * @return 绘制后的X坐标
	 */
	fun `goldenboughs_lib$drawInternal`(
		text: String,
		x: Float,
		y: Float,
		color: Int,
		dropShadow: Boolean,
		matrix: Matrix4f,
		vertexConsumer: VertexConsumer,
		backgroundColor: Int,
		packedLightCoords: Int,
		bidirectional: Boolean
	): Int {
		throw NoMixinException()
	}

	/**
	 * 内部绘制文本（格式化字符序列版本）
	 *
	 * @param text              要绘制的格式化字符序列
	 * @param x                 文本绘制的X坐标
	 * @param y                 文本绘制的Y坐标
	 * @param color             文本颜色
	 * @param dropShadow        是否绘制阴影
	 * @param matrix            变换矩阵
	 * @param vertexConsumer    顶点消费者
	 * @param backgroundColor   背景颜色
	 * @param packedLightCoords 打包的光照坐标
	 * @return 绘制后的X坐标
	 */
	fun `goldenboughs_lib$drawInternal`(
		text: FormattedCharSequence,
		x: Float,
		y: Float,
		color: Int,
		dropShadow: Boolean,
		matrix: Matrix4f,
		vertexConsumer: VertexConsumer,
		backgroundColor: Int,
		packedLightCoords: Int
	): Int {
		throw NoMixinException()
	}

	/**
	 * 渲染文本（字符串版本）
	 *
	 * @param text              要渲染的文本
	 * @param x                 文本渲染的X坐标
	 * @param y                 文本渲染的Y坐标
	 * @param color             文本颜色
	 * @param dropShadow        是否绘制阴影
	 * @param matrix            变换矩阵
	 * @param vertexConsumer    顶点消费者
	 * @param backgroundColor   背景颜色
	 * @param packedLightCoords 打包的光照坐标
	 * @return 渲染后的X坐标
	 */
	fun `goldenboughs_lib$renderText`(
		text: String,
		x: Float,
		y: Float,
		color: Int,
		dropShadow: Boolean,
		matrix: Matrix4f,
		vertexConsumer: VertexConsumer,
		backgroundColor: Int,
		packedLightCoords: Int
	): Float {
		throw NoMixinException()
	}

	/**
	 * 渲染文本（格式化字符序列版本）
	 *
	 * @param text              要渲染的格式化字符序列
	 * @param x                 文本渲染的X坐标
	 * @param y                 文本渲染的Y坐标
	 * @param color             文本颜色
	 * @param dropShadow        是否绘制阴影
	 * @param matrix            变换矩阵
	 * @param vertexConsumer    顶点消费者
	 * @param backgroundColor   背景颜色
	 * @param packedLightCoords 打包的光照坐标
	 * @return 渲染后的X坐标
	 */
	fun `goldenboughs_lib$renderText`(
		text: FormattedCharSequence,
		x: Float,
		y: Float,
		color: Int,
		dropShadow: Boolean,
		matrix: Matrix4f,
		vertexConsumer: VertexConsumer,
		backgroundColor: Int,
		packedLightCoords: Int
	): Float {
		throw NoMixinException()
	}

	companion object {
		@JvmStatic
		fun of(obj: Font): IFont {
			return obj
		}
	}
}
