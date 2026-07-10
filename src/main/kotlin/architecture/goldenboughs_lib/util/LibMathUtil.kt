package architecture.goldenboughs_lib.util

import net.minecraft.core.Position
import org.joml.Matrix4f
import org.joml.Matrix4fc
import org.joml.Quaternionf
import org.joml.Vector3d
import org.joml.Vector3dc
import org.joml.Vector3f
import org.joml.Vector3fc
import java.lang.Math
import kotlin.math.sin

/**
 * 计算基于正弦波的周期性波动值
 *
 * @param minValue        最小值
 * @param maxValue        最大值
 * @param durationSeconds 周期时长（秒）
 * @return 在 [minValue, maxValue] 范围内的周期性波动值
 */
fun calculateSineCycle(minValue: Float, maxValue: Float, durationSeconds: Float): Float {
	return calculateSineCycle(minValue, maxValue, durationSeconds, System.currentTimeMillis())
}

fun calculateSineCycle(minValue: Float, maxValue: Float, durationSeconds: Float, timeMillis: Long): Float {
	val normalizedTime = ((timeMillis % 10000) / 10000.0 * durationSeconds).toFloat()
	val sinValue = sin((normalizedTime * Math.PI.toFloat() * 2).toDouble()).toFloat()
	return mapSinToRange(minValue, maxValue, sinValue)
}

/**
 * 将正弦值映射到指定范围
 * 正弦值范围 [-1, 1] 映射到 [min, max]
 *
 * @param min      目标范围最小值
 * @param max      目标范围最大值
 * @param sinValue 正弦值（范围 -1 到 1）
 * @return 映射后的值
 */
fun mapSinToRange(min: Float, max: Float, sinValue: Float): Float {
	return min + (1 + sinValue) * (max - min) / 2
}

fun Float.toRadians(): Float {
	return Math.toRadians(toDouble()).toFloat()
}

fun Double.toRadians(): Double {
	return Math.toRadians(this)
}

fun Float.toDegrees(): Float {
	return Math.toDegrees(toDouble()).toFloat()
}

fun Double.toDegrees(): Double {
	return Math.toDegrees(this)
}

fun Matrix4fc.toPos(): Vector3d {
	return transformPosition(Vector3f()).toVector3d()
}

fun Matrix4fc.toRot(): Vector3d {
	val eulerAnglesRad = getUnnormalizedRotation(Quaternionf()).normalize().getEulerAnglesXYZ(Vector3f())
	return Vector3d(
		Math.toDegrees(eulerAnglesRad.x.toDouble()),
		Math.toDegrees(eulerAnglesRad.y.toDouble()),
		Math.toDegrees(eulerAnglesRad.z.toDouble())
	)
}

fun PoseStack.Pose.toPos(): Vector3d {
	return pose.toPos()
}

fun PoseStack.Pose.toRot(): Vector3d {
	return pose.toRot()
}

fun PoseStack.toPos(): Vector3d {
	return last().toPos()
}

fun PoseStack.toRot(): Vector3d {
	return last().toRot()
}

fun PoseStack.translate(vec3: Position) {
	translate(vec3.x(), vec3.y(), vec3.z())
}

fun PoseStack.translate(vector3fc: Vector3fc) {
	translate(vector3fc.x(), vector3fc.y(), vector3fc.z())
}

fun PoseStack.translate(vector3fc: Vector3dc) {
	translate(vector3fc.x(), vector3fc.y(), vector3fc.z())
}

fun PoseStack.scale(vector3fc: Vector3fc) {
	scale(vector3fc.x(), vector3fc.y(), vector3fc.z())
}

fun PoseStack.scale(vector3fc: Vector3dc) {
	scale(vector3fc.x().toFloat(), vector3fc.y().toFloat(), vector3fc.z().toFloat())
}

fun PoseStack.mulPose(matrix4fc: Matrix4fc) {
	mulPose(Matrix4f(matrix4fc))
}

/**
 * 从 4x4 变换矩阵提取三条归一化旋转轴（矩阵列）
 */
fun Matrix4fc.getObbAxes(): Array<Vector3f> = arrayOf(
	Vector3f(m00(), m10(), m20()).normalize(),
	Vector3f(m01(), m11(), m21()).normalize(),
	Vector3f(m02(), m12(), m22()).normalize(),
)

fun Vector3fc.copy(): Vector3f {
	return Vector3f(this)
}
