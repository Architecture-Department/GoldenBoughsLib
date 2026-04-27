package architecture.goldenboughs_lib.util;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import org.joml.Vector3f;

public final class RendererUtil {
	public static void renderRotatedQuad(VertexConsumer buffer, PoseStack.Pose pose,
	                                     float centerX, float centerY, float centerZ, float quadSize, int lightColor) {
		renderRotatedQuad(buffer, pose, centerX, centerY, centerZ, quadSize, lightColor, 1, 1, 1, 1);
	}

	public static void renderRotatedQuad(VertexConsumer buffer, PoseStack.Pose pose, float x, float y, float z,
	                                     float quadSize, int lightColor, float rCol, float gCol, float bCol, float alpha) {
		renderVertex(buffer, pose, x, y, z, 0.5f, -0.5f, quadSize, 1, 1, rCol, gCol, bCol, alpha, lightColor);
		renderVertex(buffer, pose, x, y, z, 0.5f, 0.5f, quadSize, 1, 0, rCol, gCol, bCol, alpha, lightColor);
		renderVertex(buffer, pose, x, y, z, -0.5f, 0.5f, quadSize, 0, 0, rCol, gCol, bCol, alpha, lightColor);
		renderVertex(buffer, pose, x, y, z, -0.5f, -0.5f, quadSize, 0, 1, rCol, gCol, bCol, alpha, lightColor);
	}

	public static void renderVertex(VertexConsumer buffer, PoseStack.Pose pose, float x, float y, float z,
	                                float xOffset, float yOffset, float quadSize, float u, float v,
	                                float rCol, float gCol, float bCol, float alpha, int packedLight) {
		Vector3f vector3f = new Vector3f(xOffset, yOffset, 0.0F).normalize(quadSize).add(x, y, z);
		buffer.addVertex(pose, vector3f.x(), vector3f.y(), vector3f.z())
			.setUv(u, v)
			.setUv1(0, 0)
			.setUv2(0, 0)
			.setNormal(pose, 0, 0, 0)
			.setColor(rCol, gCol, bCol, alpha)
			.setLight(packedLight);
	}
}
