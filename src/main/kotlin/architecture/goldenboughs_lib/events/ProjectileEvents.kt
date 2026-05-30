package architecture.goldenboughs_lib.events

import architecture.goldenboughs_lib.core.LibConstants
import architecture.goldenboughs_lib.util.PiercingUtil
import architecture.goldenboughs_lib.util.PiercingUtil.hasPiercingTag
import architecture.goldenboughs_lib.util.PiercingUtil.performPierceDetection
import architecture.goldenboughs_lib.util.PiercingUtil.restorePiercingProgress
import architecture.goldenboughs_lib.util.PiercingUtil.updatePiercingProgress
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.projectile.Projectile
import net.neoforged.bus.api.EventPriority
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.common.EventBusSubscriber
import net.neoforged.neoforge.event.tick.EntityTickEvent

/**
 * 弹射物事件监听器
 * 自动处理带有穿透标签的弹射物
 */
@EventBusSubscriber(modid = LibConstants.ID)
object ProjectileEvents {
	/**
	 * 监听弹射物 tick 事件，自动应用穿透逻辑
	 */
	@SubscribeEvent(priority = EventPriority.HIGHEST)
	fun onProjectileTick(event: EntityTickEvent.Pre) {
		val entity = event.entity

		// 只处理服务端的弹射物
		if (entity.level().isClientSide()) {
			return
		}

		// 检查是否为弹射物
		if (entity !is Projectile) {
			return
		}

		// 检查是否有穿透标签
		if (!hasPiercingTag(entity)) {
			return
		}

		// 获取穿透配置（恢复进度）
		val pierceData = restorePiercingProgress(entity) ?: return

		// 执行穿透检测
		val hitEntities: List<Entity> = performPierceDetection(
			entity,
			pierceData,
			0.5
		)

		// 处理命中的实体
		for (hitEntity in hitEntities) {
			onPierceHit(entity, hitEntity, pierceData)
		}

		// 更新穿透进度到 NBT
		updatePiercingProgress(entity, pierceData)

		// 检查是否达到穿透上限
		if (!pierceData.canPierce()) {
			entity.discard()
		}
	}

	/**
	 * 处理穿透命中
	 *
	 * @param projectile 弹射物
	 * @param hitEntity  被命中的实体
	 * @param pierceData 穿透数据
	 */
	private fun onPierceHit(projectile: Projectile, hitEntity: Entity, pierceData: PiercingUtil.PierceData) {
		// 只对生物实体生效
		if (hitEntity !is LivingEntity) {
			return
		}

		// 避免伤害射击者
		val owner = projectile.owner
		if (owner != null && owner == hitEntity) {
			return
		}

		// 计算当前伤害
		val damage = pierceData.currentDamage

		// 应用伤害（使用魔法伤害类型）
		val damageSource = hitEntity.damageSources().indirectMagic(projectile, owner)
		hitEntity.hurt(damageSource, damage)

		// 这里可以添加命中特效
		// spawnHitParticles(livingTarget);
	}
}
