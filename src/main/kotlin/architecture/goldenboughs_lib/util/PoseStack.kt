package architecture.goldenboughs_lib.util

import com.google.common.collect.Queues
import com.mojang.math.MatrixUtil
import net.minecraft.Util
import net.neoforged.neoforge.client.extensions.IPoseStackExtension
import org.joml.Matrix3f
import org.joml.Matrix4f
import org.joml.Quaternionf
import org.joml.Vector3f
import java.util.*
import kotlin.math.abs
import kotlin.math.sign

data class PoseStack
@JvmOverloads
constructor(
	private val poseStack: Deque<Pose> =
		Util.make(Queues.newArrayDeque()) { poses ->
			val matrix4f = Matrix4f()
			val matrix3f = Matrix3f()
			poses.add(Pose(matrix4f, matrix3f))
		}
) : IPoseStackExtension {

	fun translate(x: Double, y: Double, z: Double) {
		this.translate(x.toFloat(), y.toFloat(), z.toFloat())
	}

	fun translate(x: Float, y: Float, z: Float) {
		val poseStackPose = this.poseStack.getLast()
		poseStackPose.pose.translate(x, y, z)
	}

	fun scale(x: Float, y: Float, z: Float) {
		val poseStackPose = this.poseStack.getLast()
		poseStackPose.pose.scale(x, y, z)
		if (abs(x) == abs(y) && abs(y) == abs(z)) {
			if (x < 0.0f || y < 0.0f || z < 0.0f) {
				poseStackPose.normal.scale(sign(x), sign(y), sign(z))
			}
		} else {
			poseStackPose.normal.scale(1.0f / x, 1.0f / y, 1.0f / z)
			poseStackPose.trustedNormals = false
		}
	}

	fun mulPose(quaternion: Quaternionf?) {
		val poseStackPose = this.poseStack.getLast()
		poseStackPose.pose.rotate(quaternion)
		poseStackPose.normal.rotate(quaternion)
	}

	fun rotateAround(quaternion: Quaternionf?, x: Float, y: Float, z: Float) {
		val poseStackPose = this.poseStack.getLast()
		poseStackPose.pose.rotateAround(quaternion, x, y, z)
		poseStackPose.normal.rotate(quaternion)
	}

	fun pushPose() {
		this.poseStack.addLast(Pose(this.poseStack.getLast()))
	}

	fun popPose() {
		this.poseStack.removeLast()
	}

	fun last(): Pose {
		return this.poseStack.getLast()
	}

	fun clear(): Boolean {
		return this.poseStack.size == 1
	}

	fun setIdentity() {
		val poseStackPose = this.poseStack.getLast()
		poseStackPose.pose.identity()
		poseStackPose.normal.identity()
		poseStackPose.trustedNormals = true
	}

	fun mulPose(pose: Matrix4f) {
		val poseStackPose = this.poseStack.getLast()
		poseStackPose.pose.mul(pose)
		if (MatrixUtil.isPureTranslation(pose)) return
		if (MatrixUtil.isOrthonormal(pose)) {
			poseStackPose.normal.mul(Matrix3f(pose))
		} else {
			poseStackPose.computeNormalMatrix()
		}
	}

	data class Pose(
		val pose: Matrix4f,
		val normal: Matrix3f,
		var trustedNormals: Boolean = true
	) {

		internal constructor(pose: Pose) :
			this(Matrix4f(pose.pose), Matrix3f(pose.normal), pose.trustedNormals)

		fun computeNormalMatrix() {
			this.normal.set(this.pose).invert().transpose()
			this.trustedNormals = false
		}

		fun transformNormal(vector: Vector3f, destination: Vector3f?): Vector3f? {
			return this.transformNormal(vector.x, vector.y, vector.z, destination)
		}

		fun transformNormal(x: Float, y: Float, z: Float, destination: Vector3f?): Vector3f? {
			val vector3f = this.normal.transform(x, y, z, destination)
			return if (this.trustedNormals) vector3f else vector3f.normalize()
		}

		fun copy(): Pose {
			return Pose(this)
		}
	}
}
