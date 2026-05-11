package architecture.goldenboughs_lib.client.renderer

import architecture.goldenboughs_lib.api.AllOpe
import net.minecraft.client.model.HumanoidModel
import net.minecraft.client.model.geom.ModelPart
import net.minecraft.client.player.AbstractClientPlayer
import net.minecraft.client.renderer.MultiBufferSource
import net.minecraft.client.resources.PlayerSkin
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.EquipmentSlot
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import software.bernie.geckolib.animatable.GeoItem
import software.bernie.geckolib.cache.`object`.BakedGeoModel
import software.bernie.geckolib.cache.`object`.GeoBone
import software.bernie.geckolib.model.GeoModel
import software.bernie.geckolib.renderer.GeoArmorRenderer
import software.bernie.geckolib.util.RenderUtil

/**
 * 盔甲渲染
 */
@AllOpe
class RoughAndFineArmorRenderer<T>(model: GeoModel<T>) : GeoArmorRenderer<T>(model) where T : Item, T : GeoItem {
	/**
	 * 是否是细手臂渲染
	 */
	@JvmField
	final var isFine: Boolean = false

	@JvmField
	protected final var fineRightArm: GeoBone? = null

	@JvmField
	protected final var fineLeftArm: GeoBone? = null

	@JvmField
	protected final var armorPants: GeoBone? = null

	override fun grabRelevantBones(bakedModel: BakedGeoModel) {
		if (this.lastModel === bakedModel) {
			return
		}

		val model = geoModel
		this.lastModel = bakedModel
		this.head = getHeadBone(model)
		this.body = getBodyBone(model)
		this.rightArm = getRightArmBone(model)
		this.leftArm = getLeftArmBone(model)
		this.rightLeg = getRightLegBone(model)
		this.leftLeg = getLeftLegBone(model)
		this.rightBoot = getRightBootBone(model)
		this.leftBoot = getLeftBootBone(model)

		this.armorPants = getArmorPantsBone(model)

		// 细模型部分
		this.fineRightArm = getFineRightArmBone(model)
		this.fineLeftArm = getFineLeftArmBone(model)
	}

	fun getFineRightArmBone(model: GeoModel<T>): GeoBone? {
		return model.getBone("armorFineRightArm").orElse(null)
	}

	fun getFineLeftArmBone(model: GeoModel<T>): GeoBone? {
		return model.getBone("armorFineLeftArm").orElse(null)
	}

	fun getArmorPantsBone(model: GeoModel<T>): GeoBone? {
		return model.getBone("armorPants").orElse(null)
	}

	override fun applyBoneVisibilityByPart(
		currentSlot: EquipmentSlot,
		currentPart: ModelPart,
		model: HumanoidModel<*>
	) {
		setAllVisible(false)

		currentPart.visible = true
		var bone: GeoBone? = null

		when (currentPart) {
			model.hat, model.head -> {
				bone = this.head
			}

			model.body -> {
				bone = this.body
			}

			model.leftLeg -> {
				bone = if (currentSlot == EquipmentSlot.FEET) this.leftBoot else this.leftLeg
			}

			model.rightLeg -> {
				bone = if (currentSlot == EquipmentSlot.FEET) this.rightBoot else this.rightLeg
			}

			model.leftArm if isABoolean(this.fineRightArm) -> {
				bone = this.leftArm
			}

			model.rightArm if isABoolean(this.fineLeftArm) -> {
				bone = this.rightArm
			}

			model.rightArm if this.fineRightArm != null -> {
				bone = this.fineRightArm
			}

			model.leftArm if this.fineLeftArm != null -> {
				bone = this.fineLeftArm
			}
		}

		if (bone != null) {
			bone.setHidden(false)
			if ((currentPart == model.leftLeg || currentPart == model.rightLeg) &&
				currentSlot == EquipmentSlot.FEET &&
				armorPants != null
			) {
				armorPants!!.setHidden(false)
			}
		}
	}

	private fun isABoolean(slimArm: GeoBone?): Boolean {
		return slimArm == null || !this.isFine
	}

	override fun applyBaseTransformations(baseModel: HumanoidModel<*>) {
		super.applyBaseTransformations(baseModel)

		if (this.fineRightArm != null) {
			val rightArmPart = baseModel.rightArm

			RenderUtil.matchModelPartRot(rightArmPart, this.fineRightArm)
			this.fineRightArm!!.updatePosition(rightArmPart.x + 5, 2 - rightArmPart.y, rightArmPart.z)
		}

		if (this.fineLeftArm != null) {
			val leftArmPart = baseModel.leftArm

			RenderUtil.matchModelPartRot(leftArmPart, this.fineLeftArm)
			this.fineLeftArm!!.updatePosition(leftArmPart.x - 5f, 2f - leftArmPart.y, leftArmPart.z)
		}

		if (this.armorPants != null) {
			val bodyPart = baseModel.body

			RenderUtil.matchModelPartRot(bodyPart, this.armorPants)
			this.armorPants!!.updatePosition(bodyPart.x, -bodyPart.y, bodyPart.z)
		}
	}

	override fun prepForRender(
		entity: Entity,
		stack: ItemStack,
		slot: EquipmentSlot,
		baseModel: HumanoidModel<*>,
		bufferSource: MultiBufferSource,
		partialTick: Float,
		limbSwing: Float,
		limbSwingAmount: Float,
		netHeadYaw: Float,
		headPitch: Float
	) {
		super.prepForRender(
			entity,
			stack,
			slot,
			baseModel,
			bufferSource,
			partialTick,
			limbSwing,
			limbSwingAmount,
			netHeadYaw,
			headPitch
		)
		if (getCurrentEntity() is AbstractClientPlayer &&
			(this.fineRightArm != null || this.fineLeftArm != null)
		) {
			val player = entity as AbstractClientPlayer
			this.isFine = player.getSkin().model() == PlayerSkin.Model.SLIM
		}
	}

	override fun doPostRenderCleanup() {
		super.doPostRenderCleanup()
		this.isFine = false
	}

	override fun applyBoneVisibilityBySlot(currentSlot: EquipmentSlot) {
		setAllBonesVisible(false)
		val model: HumanoidModel<*> = this

		when (currentSlot) {
			EquipmentSlot.HEAD -> setBoneVisible(this.head, model.head.visible)
			EquipmentSlot.CHEST -> {
				setBoneVisible(this.body, model.body.visible)
				handApplyBoneVisibilityBySlot(model)
			}

			EquipmentSlot.LEGS -> {
				setBoneVisible(this.rightLeg, model.rightLeg.visible)
				setBoneVisible(this.leftLeg, model.leftLeg.visible)
				setBoneVisible(this.armorPants, model.leftLeg.visible || model.rightLeg.visible)
			}

			EquipmentSlot.FEET -> {
				setBoneVisible(this.rightBoot, model.rightLeg.visible)
				setBoneVisible(this.leftBoot, model.leftLeg.visible)
			}

			else -> {}
		}
	}

	override fun setAllBonesVisible(visible: Boolean) {
		super.setAllBonesVisible(visible)

		setBoneVisible(this.fineRightArm, visible)
		setBoneVisible(this.fineLeftArm, visible)
	}

	private fun handApplyBoneVisibilityBySlot(model: HumanoidModel<*>) {
		setBoneVisible(this.rightArm, model.rightArm.visible && !this.isFine)
		setBoneVisible(this.leftArm, model.leftArm.visible && !this.isFine)

		setBoneVisible(this.fineRightArm, model.rightArm.visible && this.isFine)
		setBoneVisible(this.fineLeftArm, model.leftArm.visible && this.isFine)
	}
}
