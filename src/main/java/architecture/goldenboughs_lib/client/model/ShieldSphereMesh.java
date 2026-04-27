package architecture.goldenboughs_lib.client.model;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import org.joml.Matrix3f;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;

import java.util.ArrayList;
import java.util.List;

/**
 * 用于存储和渲染一个经纬球（UV Sphere）的网格数据。
 * 这个球体的半径为1，中心在 (0,0,0)。
 */
public class ShieldSphereMesh {
	// 球体的细分程度，数值越高越圆滑，但顶点数也越多
	private static final int STACKS = 16; // 经线数（垂直）
	private static final int SLICES = 16; // 纬线数（水平）
	private final List<MeshVertex> vertices = new ArrayList<>();
	private final List<Integer> indices = new ArrayList<>();

	public ShieldSphereMesh() {
		generateSphere();
	}

	private void generateSphere() {
		float phiStep = (float) (Math.PI / STACKS);
		float thetaStep = (float) (2 * Math.PI / SLICES);

		// 生成顶点和UV
		for (int i = 0; i <= STACKS; i++) {
			float phi = i * phiStep;
			for (int j = 0; j <= SLICES; j++) {
				float theta = j * thetaStep;

				// 球面坐标转笛卡尔坐标，半径为1
				float x = (float) (Math.sin(phi) * Math.cos(theta));
				float y = (float) Math.cos(phi);
				float z = (float) (Math.sin(phi) * Math.sin(theta));

				// 顶点位置
				Vector3f position = new Vector3f(x, y, z);
				// 法线方向与位置向量相同（因为半径为1）
				Vector3f normal = new Vector3f(x, y, z);
				// UV坐标：U是经度方向，V是纬度方向
				float u = (float) j / SLICES;
				float v = (float) i / STACKS;

				vertices.add(new MeshVertex(position, normal, u, v));
			}
		}

		// 生成索引，构成三角形
		for (int i = 0; i < STACKS; i++) {
			for (int j = 0; j < SLICES; j++) {
				int first = i * (SLICES + 1) + j;
				int second = first + SLICES + 1;

				// 两个三角形形成一个四边形格子
				// 三角形1 (左上, 右上, 左下)
				indices.add(first);
				indices.add(first + 1);
				indices.add(second);
				// 三角形2 (右上, 右下, 左下)
				indices.add(first + 1);
				indices.add(second + 1);
				indices.add(second);
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
	public void render(VertexConsumer consumer, PoseStack.Pose pose, float r, float g, float b, float a, int light) {
		Matrix4f matrix = pose.pose();
		Matrix3f normalMatrix = pose.normal();
		for (int index : indices) {
			MeshVertex vertex = vertices.get(index);
			// 顶点变换
			Vector4f worldPos = new Vector4f(vertex.position.x(), vertex.position.y(), vertex.position.z(), 1.0f);
			worldPos.mul(matrix);
			// 法线变换
			Vector3f worldNormal = new Vector3f(vertex.normal);
			worldNormal.mul(normalMatrix);
			// 提交顶点
			int blockLight = light & 0xFFFF;
			int skyLight = (light >> 16) & 0xFFFF;
			consumer.addVertex(worldPos.x(), worldPos.y(), worldPos.z())
				.setColor(r, g, b, a)
				.setUv(vertex.u, vertex.v)
				.setOverlay(OverlayTexture.NO_OVERLAY)
				.setUv2(blockLight, skyLight)
				.setNormal(worldNormal.x(), worldNormal.y(), worldNormal.z());
		}
	}

	// 简单的内部类存储顶点数据
	private record MeshVertex(Vector3f position, Vector3f normal, float u, float v) {
	}
}
