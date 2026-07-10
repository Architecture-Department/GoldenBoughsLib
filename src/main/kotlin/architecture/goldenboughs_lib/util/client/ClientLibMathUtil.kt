package architecture.goldenboughs_lib.util.client

import architecture.goldenboughs_lib.util.toPos
import architecture.goldenboughs_lib.util.toRot
import com.mojang.blaze3d.vertex.PoseStack
import net.minecraft.core.Position
import org.joml.*

fun PoseStack.toPos(): Vector3d {
	return last().toPos()
}

fun PoseStack.Pose.toPos(): Vector3d {
	return pose().toPos()
}

fun PoseStack.Pose.toRot(): Vector3d {
	return pose().toRot()
}

fun PoseStack.toRot(): Vector3d {
	return last().toRot()
}

fun PoseStack.translate(vector3fc: Vector3fc) {
	translate(vector3fc.x(), vector3fc.y(), vector3fc.z())
}

fun PoseStack.translate(vector3fc: Vector3dc) {
	translate(vector3fc.x(), vector3fc.y(), vector3fc.z())
}

fun PoseStack.translate(vec3: Position) {
	translate(vec3.x(), vec3.y(), vec3.z())
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