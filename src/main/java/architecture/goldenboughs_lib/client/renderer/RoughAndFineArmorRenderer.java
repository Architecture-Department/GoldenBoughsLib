package architecture.goldenboughs_lib.client.renderer;

import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.resources.PlayerSkin;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoItem;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.GeoArmorRenderer;
import software.bernie.geckolib.util.RenderUtil;

/**
 * 盔甲渲染
 */
public class RoughAndFineArmorRenderer<T extends Item & GeoItem> extends GeoArmorRenderer<T> {
	/**
	 * 是否是细手臂渲染
	 */
	public boolean isFine;
	protected @Nullable GeoBone fineRightArm;
	protected @Nullable GeoBone fineLeftArm;
	protected @Nullable GeoBone armorPants;

	public RoughAndFineArmorRenderer(GeoModel<T> model) {
		super(model);
	}

	@Override
	protected void grabRelevantBones(BakedGeoModel bakedModel) {
		if (this.lastModel == bakedModel) {
			return;
		}

		GeoModel<T> model = getGeoModel();
		this.lastModel = bakedModel;
		this.head = getHeadBone(model);
		this.body = getBodyBone(model);
		this.rightArm = getRightArmBone(model);
		this.leftArm = getLeftArmBone(model);
		this.rightLeg = getRightLegBone(model);
		this.leftLeg = getLeftLegBone(model);
		this.rightBoot = getRightBootBone(model);
		this.leftBoot = getLeftBootBone(model);

		this.armorPants = getArmorPantsBone(model);

		// 细模型部分
		this.fineRightArm = getFineRightArmBone(model);
		this.fineLeftArm = getFineLeftArmBone(model);
	}

	@Nullable
	public GeoBone getFineRightArmBone(@NotNull GeoModel<T> model) {
		return model.getBone("armorFineRightArm").orElse(null);
	}

	@Nullable
	public GeoBone getFineLeftArmBone(@NotNull GeoModel<T> model) {
		return model.getBone("armorFineLeftArm").orElse(null);
	}

	@Nullable
	public GeoBone getArmorPantsBone(@NotNull GeoModel<T> model) {
		return model.getBone("armorPants").orElse(null);
	}

	@Override
	public void applyBoneVisibilityByPart(EquipmentSlot currentSlot, ModelPart currentPart, HumanoidModel<?> model) {
		setAllVisible(false);

		currentPart.visible = true;
		GeoBone bone = null;

		if (currentPart == model.hat || currentPart == model.head) {
			bone = this.head;
		} else if (currentPart == model.body) {
			bone = this.body;
		} else if (currentPart == model.leftLeg) {
			bone = currentSlot == EquipmentSlot.FEET ? this.leftBoot : this.leftLeg;
		} else if (currentPart == model.rightLeg) {
			bone = currentSlot == EquipmentSlot.FEET ? this.rightBoot : this.rightLeg;
		} else if (currentPart == model.leftArm && isABoolean(this.fineRightArm)) {
			bone = this.leftArm;
		} else if (currentPart == model.rightArm && isABoolean(this.fineLeftArm)) {
			bone = this.rightArm;
		} else if (currentPart == model.rightArm && this.fineRightArm != null) {
			bone = this.fineRightArm;
		} else if (currentPart == model.leftArm && this.fineLeftArm != null) {
			bone = this.fineLeftArm;
		}

		if (bone != null) {
			bone.setHidden(false);
			if ((currentPart == model.leftLeg || currentPart == model.rightLeg) && currentSlot == EquipmentSlot.FEET && armorPants != null) {
				armorPants.setHidden(false);
			}
		}
	}

	private boolean isABoolean(@Nullable GeoBone slimArm) {
		return slimArm == null || !this.isFine;
	}

	@Override
	protected void applyBaseTransformations(HumanoidModel<?> baseModel) {
		super.applyBaseTransformations(baseModel);

		if (this.fineRightArm != null) {
			ModelPart rightArmPart = baseModel.rightArm;

			RenderUtil.matchModelPartRot(rightArmPart, this.fineRightArm);
			this.fineRightArm.updatePosition(rightArmPart.x + 5, 2 - rightArmPart.y, rightArmPart.z);
		}

		if (this.fineLeftArm != null) {
			ModelPart leftArmPart = baseModel.leftArm;

			RenderUtil.matchModelPartRot(leftArmPart, this.fineLeftArm);
			this.fineLeftArm.updatePosition(leftArmPart.x - 5f, 2f - leftArmPart.y, leftArmPart.z);
		}

		if (this.armorPants != null) {
			ModelPart bodyPart = baseModel.body;

			RenderUtil.matchModelPartRot(bodyPart, this.armorPants);
			this.armorPants.updatePosition(bodyPart.x, -bodyPart.y, bodyPart.z);
		}
	}

	@Override
	public void prepForRender(Entity entity, ItemStack stack, EquipmentSlot slot, HumanoidModel<?> baseModel, MultiBufferSource bufferSource, float partialTick, float limbSwing, float limbSwingAmount, float netHeadYaw, float headPitch) {
		super.prepForRender(entity, stack, slot, baseModel, bufferSource, partialTick, limbSwing, limbSwingAmount, netHeadYaw, headPitch);
		if (getCurrentEntity() instanceof AbstractClientPlayer player &&
			(this.fineRightArm != null || this.fineLeftArm != null)) {
			this.isFine = player.getSkin().model() == PlayerSkin.Model.SLIM;
		}
	}

	@Override
	public void doPostRenderCleanup() {
		super.doPostRenderCleanup();
		this.isFine = false;
	}

	@Override
	protected void applyBoneVisibilityBySlot(EquipmentSlot currentSlot) {
		setAllBonesVisible(false);
		HumanoidModel<?> model = this;

		switch (currentSlot) {
			case HEAD -> setBoneVisible(this.head, model.head.visible);
			case CHEST -> {
				setBoneVisible(this.body, model.body.visible);
				HandApplyBoneVisibilityBySlot(model);
			}
			case LEGS -> {
				setBoneVisible(this.rightLeg, model.rightLeg.visible);
				setBoneVisible(this.leftLeg, model.leftLeg.visible);
				setBoneVisible(this.armorPants, model.leftLeg.visible || model.rightLeg.visible);
			}
			case FEET -> {
				setBoneVisible(this.rightBoot, model.rightLeg.visible);
				setBoneVisible(this.leftBoot, model.leftLeg.visible);
			}
		}
	}

	@Override
	protected void setAllBonesVisible(boolean visible) {
		super.setAllBonesVisible(visible);

		setBoneVisible(this.fineRightArm, visible);
		setBoneVisible(this.fineLeftArm, visible);
	}

	private void HandApplyBoneVisibilityBySlot(HumanoidModel<?> model) {
		setBoneVisible(this.rightArm, model.rightArm.visible && !this.isFine);
		setBoneVisible(this.leftArm, model.leftArm.visible && !this.isFine);

		setBoneVisible(this.fineRightArm, model.rightArm.visible && this.isFine);
		setBoneVisible(this.fineLeftArm, model.leftArm.visible && this.isFine);
	}
}
