package architecture.goldenboughs_lib.util

import com.google.common.collect.ImmutableList
import net.minecraft.world.effect.MobEffectInstance
import net.minecraft.world.food.FoodProperties
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.ItemLike
import java.util.*
import java.util.function.Supplier

/** 链式 builder 简化 {@link FoodProperties} 构造 */
class FoodPropertiesBuilder {
	/** 恢复饱食度 (鸡腿数) */
	private var nutrition = 0

	/** 饱和度 */
	private var saturation = 0f

	/** 满饱食度时是否仍可食用 */
	private var canAlwaysEat = false

	/** 食用耗时 (秒) */
	private var eatSeconds = 1.6f

	/** 食用后返还的容器物品, 如碗/瓶 */
	private var usingConvertsTo = Optional.empty<ItemStack>()

	/** 食用触发的药水效果列表 */
	private val effects = ImmutableList.builder<FoodProperties.PossibleEffect>()

	fun nutrition(nutrition: Int): FoodPropertiesBuilder {
		this.nutrition = nutrition
		return this
	}

	fun saturation(saturation: Float): FoodPropertiesBuilder {
		this.saturation = saturation
		return this
	}

	fun alwaysEdible(): FoodPropertiesBuilder {
		this.canAlwaysEat = true
		return this
	}

	fun eatSeconds(eatSeconds: Float): FoodPropertiesBuilder {
		this.eatSeconds = eatSeconds
		return this
	}

	fun eatSeconds(eatSecondsTick: Int): FoodPropertiesBuilder {
		this.eatSeconds = eatSecondsTick / 20f
		return this
	}

	/** 快捷食用 (0.8s), 对标原版干海带 */
	fun fast(): FoodPropertiesBuilder {
		this.eatSeconds = 0.8f
		return this
	}

	/** 添加固定药水效果 */
	fun effect(effect: MobEffectInstance, probability: Float): FoodPropertiesBuilder {
		this.effects.add(FoodProperties.PossibleEffect({ effect }, probability))
		return this
	}

	/** 添加延迟计算的药水效果 (如基于食用前状态的动态效果) */
	fun effect(effectIn: Supplier<MobEffectInstance>, probability: Float): FoodPropertiesBuilder {
		this.effects.add(FoodProperties.PossibleEffect(effectIn, probability))
		return this
	}

	/** 食用后返还空容器 (碗→空碗, 瓶→空瓶) */
	fun usingConvertsTo(item: ItemLike): FoodPropertiesBuilder {
		this.usingConvertsTo = Optional.of(ItemStack(item))
		return this
	}

	fun build(): FoodProperties {
		return FoodProperties(
			nutrition,
			saturation,
			canAlwaysEat,
			eatSeconds,
			usingConvertsTo,
			effects.build()
		)
	}
}
