package architecture.goldenboughs_lib.util.client

import com.mojang.blaze3d.vertex.PoseStack
import com.mojang.blaze3d.vertex.VertexConsumer
import org.joml.Vector3f

object RendererUtil {
	@JvmStatic
	fun renderRotatedQuad(
		buffer: VertexConsumer, pose: PoseStack.Pose,
		centerX: Float, centerY: Float, centerZ: Float, quadSize: Float, lightColor: Int
	) {
		renderRotatedQuad(buffer, pose, centerX, centerY, centerZ, quadSize, lightColor, 1f, 1f, 1f, 1f)
	}

	@JvmStatic
	fun renderRotatedQuad(
		buffer: VertexConsumer, pose: PoseStack.Pose, x: Float, y: Float, z: Float,
		quadSize: Float, lightColor: Int, rCol: Float, gCol: Float, bCol: Float, alpha: Float
	) {
		renderVertex(buffer, pose, x, y, z, 0.5f, -0.5f, quadSize, 1f, 1f, rCol, gCol, bCol, alpha, lightColor)
		renderVertex(buffer, pose, x, y, z, 0.5f, 0.5f, quadSize, 1f, 0f, rCol, gCol, bCol, alpha, lightColor)
		renderVertex(buffer, pose, x, y, z, -0.5f, 0.5f, quadSize, 0f, 0f, rCol, gCol, bCol, alpha, lightColor)
		renderVertex(buffer, pose, x, y, z, -0.5f, -0.5f, quadSize, 0f, 1f, rCol, gCol, bCol, alpha, lightColor)
	}

	@JvmStatic
	fun renderVertex(
		buffer: VertexConsumer, pose: PoseStack.Pose, x: Float, y: Float, z: Float,
		xOffset: Float, yOffset: Float, quadSize: Float, u: Float, v: Float,
		rCol: Float, gCol: Float, bCol: Float, alpha: Float, packedLight: Int
	) {
		val vector3f = Vector3f(xOffset, yOffset, 0.0f).normalize(quadSize).add(x, y, z)
		buffer.addVertex(pose, vector3f.x(), vector3f.y(), vector3f.z())
			.setUv(u, v)
			.setUv1(0, 0)
			.setUv2(0, 0)
			.setNormal(pose, 0f, 0f, 0f)
			.setColor(rCol, gCol, bCol, alpha)
			.setLight(packedLight)
	}
}