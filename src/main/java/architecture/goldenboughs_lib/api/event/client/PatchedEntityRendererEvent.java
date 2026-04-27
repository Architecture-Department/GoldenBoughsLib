package architecture.goldenboughs_lib.api.event.client;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.bus.api.Event;
import net.neoforged.bus.api.ICancellableEvent;
import yesman.epicfight.api.asset.AssetAccessor;
import yesman.epicfight.api.client.model.SkinnedMesh;
import yesman.epicfight.world.capabilities.entitypatch.LivingEntityPatch;

public abstract class PatchedEntityRendererEvent<E extends LivingEntity, T extends LivingEntityPatch<E>, R extends EntityRenderer<E>, AM extends SkinnedMesh> extends Event {
	private final AssetAccessor<? extends AM> skinnedMesh;
	protected E livingEntity;
	protected T livingEntityPatch;
	protected R entityRenderer;
	protected MultiBufferSource buffer;
	protected PoseStack poseStack;
	protected int packedLight;
	protected float partialTick;

	public PatchedEntityRendererEvent(E livingEntity, T livingEntityPatch, R entityRenderer, AssetAccessor<? extends AM> skinnedMesh, MultiBufferSource buffer, PoseStack poseStack, int packedLight, float partialTick) {
		this.livingEntity = livingEntity;
		this.livingEntityPatch = livingEntityPatch;
		this.entityRenderer = entityRenderer;
		this.skinnedMesh = skinnedMesh;
		this.buffer = buffer;
		this.poseStack = poseStack;
		this.packedLight = packedLight;
		this.partialTick = partialTick;
	}

	public E getLivingEntity() {
		return livingEntity;
	}

	public T getLivingEntityPatch() {
		return livingEntityPatch;
	}

	public R getEntityRenderer() {
		return entityRenderer;
	}

	public AssetAccessor<? extends AM> getSkinnedMesh() {
		return skinnedMesh;
	}

	public MultiBufferSource getBuffer() {
		return buffer;
	}

	public PoseStack getPoseStack() {
		return poseStack;
	}

	public int getPackedLight() {
		return packedLight;
	}

	public float getPartialTick() {
		return partialTick;
	}

	public static class Pre<E extends LivingEntity, T extends LivingEntityPatch<E>, R extends EntityRenderer<E>, AM extends SkinnedMesh> extends PatchedEntityRendererEvent<E, T, R, AM> implements ICancellableEvent {
		public Pre(E livingEntity, T livingEntityPatch, R entityRenderer, AssetAccessor<? extends AM> skinnedMesh, MultiBufferSource buffer, PoseStack poseStack, int packedLight, float partialTick) {
			super(livingEntity, livingEntityPatch, entityRenderer, skinnedMesh, buffer, poseStack, packedLight, partialTick);
		}

		public void setLivingEntity(E livingEntity) {
			this.livingEntity = livingEntity;
		}

		public void setLivingEntityPatch(T livingEntityPatch) {
			this.livingEntityPatch = livingEntityPatch;
		}

		public void setEntityRenderer(R entityRenderer) {
			this.entityRenderer = entityRenderer;
		}

		public void setBuffer(MultiBufferSource buffer) {
			this.buffer = buffer;
		}

		public void setPoseStack(PoseStack poseStack) {
			this.poseStack = poseStack;
		}

		public void setPackedLight(int packedLight) {
			this.packedLight = packedLight;
		}

		public void setPartialTick(float partialTick) {
			this.partialTick = partialTick;
		}
	}

	public static class Post<E extends LivingEntity, T extends LivingEntityPatch<E>, R extends EntityRenderer<E>, AM extends SkinnedMesh> extends PatchedEntityRendererEvent<E, T, R, AM> {
		public Post(E livingEntity, T livingEntityPatch, R entityRenderer, AssetAccessor<? extends AM> skinnedMesh, MultiBufferSource buffer, PoseStack poseStack, int packedLight, float partialTick) {
			super(livingEntity, livingEntityPatch, entityRenderer, skinnedMesh, buffer, poseStack, packedLight, partialTick);
		}
	}
}
