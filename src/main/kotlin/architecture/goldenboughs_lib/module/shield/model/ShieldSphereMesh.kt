package architecture.goldenboughs_lib.module.shield.model

import com.mojang.blaze3d.vertex.PoseStack
import com.mojang.blaze3d.vertex.VertexConsumer
import net.minecraft.client.renderer.texture.OverlayTexture
import org.joml.Vector3f
import org.joml.Vector4f
import kotlin.math.cos
import kotlin.math.sin

/**
 * 用于存储和渲染一个经纬球（UV Sphere）的网格数据。
 * 这个球体的半径为1，中心在 (0,0,0)。
 */
class ShieldSphereMesh {
	private val vertices: MutableList<MeshVertex> = ArrayList()
	private val indices: MutableList<Int> = ArrayList()

	init {
		generateSphere()
	}

	private fun generateSphere() {
		val phiStep = (Math.PI / STACKS).toFloat()
		val thetaStep = (2 * Math.PI / SLICES).toFloat()

		// 生成顶点和UV
		for (i in 0..STACKS) {
			val phi = i * phiStep
			for (j in 0..SLICES) {
				val theta = j * thetaStep

				// 球面坐标转笛卡尔坐标，半径为1
				val x = (sin(phi.toDouble()) * cos(theta.toDouble())).toFloat()
				val y = cos(phi.toDouble()).toFloat()
				val z = (sin(phi.toDouble()) * sin(theta.toDouble())).toFloat()

				// 顶点位置
				val position = Vector3f(x, y, z)
				// 法线方向与位置向量相同（因为半径为1）
				val normal = Vector3f(x, y, z)
				// UV坐标：U是经度方向，V是纬度方向
				val u: Float = j.toFloat() / SLICES
				val v: Float = i.toFloat() / STACKS

				vertices.add(MeshVertex(position, normal, u, v))
			}
		}

		// 生成索引，构成三角形
		for (i in 0..<STACKS) {
			for (j in 0..<SLICES) {
				val first: Int = i * (SLICES + 1) + j
				val second: Int = first + SLICES + 1

				// 两个三角形形成一个四边形格子
				// 三角形1 (左上, 右上, 左下)
				indices.add(first)
				indices.add(first + 1)
				indices.add(second)
				// 三角形2 (右上, 右下, 左下)
				indices.add(first + 1)
				indices.add(second + 1)
				indices.add(second)
			}
		}
	}

	/**
	 * 将球体网格渲染到给定的 VertexConsumer 中。
	 * 
	 * @param consumer VertexConsumer
	 * @param pose     变换矩阵（包含位置、旋转、缩放）
	 * @param light    光照值
	 */
	fun render(consumer: VertexConsumer, pose: PoseStack.Pose, r: Float, g: Float, b: Float, a: Float, light: Int) {
		val matrix = pose.pose()
		val normalMatrix = pose.normal()
		for (index in indices) {
			val vertex = vertices[index]
			// 顶点变换
			val worldPos = Vector4f(vertex.position.x(), vertex.position.y(), vertex.position.z(), 1.0f)
			worldPos.mul(matrix)
			// 法线变换
			val worldNormal = Vector3f(vertex.normal)
			worldNormal.mul(normalMatrix)
			// 提交顶点
			val blockLight = light and 0xFFFF
			val skyLight = (light shr 16) and 0xFFFF
			consumer.addVertex(worldPos.x(), worldPos.y(), worldPos.z())
				.setColor(r, g, b, a)
				.setUv(vertex.u, vertex.v)
				.setOverlay(OverlayTexture.NO_OVERLAY)
				.setUv2(blockLight, skyLight)
				.setNormal(worldNormal.x(), worldNormal.y(), worldNormal.z())
		}
	}

	// 简单的内部类存储顶点数据
	@JvmRecord
	private data class MeshVertex(
		val position: Vector3f,
		val normal: Vector3f,
		val u: Float,
		val v: Float
	)

	companion object {
		// 球体的细分程度，数值越高越圆滑，但顶点数也越多
		private const val STACKS = 16 // 经线数（垂直）
		private const val SLICES = 16 // 纬线数（水平）
	}
}
