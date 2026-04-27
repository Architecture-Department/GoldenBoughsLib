package architecture.goldenboughs_lib.events;

import architecture.goldenboughs_lib.core.GoldenBoughsLib;
import architecture.goldenboughs_lib.util.PiercingUtil;
import architecture.goldenboughs_lib.util.PiercingUtil.PierceData;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.Projectile;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.tick.EntityTickEvent;

import java.util.List;

/**
 * 弹射物事件监听器
 * 自动处理带有穿透标签的弹射物
 */
@EventBusSubscriber(modid = GoldenBoughsLib.ID)
public final class ProjectileEvents {

	/**
	 * 监听弹射物 tick 事件，自动应用穿透逻辑
	 */
	@SubscribeEvent(priority = EventPriority.HIGHEST)
	public static void onProjectileTick(EntityTickEvent.Pre event) {
		Entity entity = event.getEntity();

		// 只处理服务端的弹射物
		if (entity.level().isClientSide()) {
			return;
		}

		// 检查是否为弹射物
		if (!(entity instanceof Projectile projectile)) {
			return;
		}

		// 检查是否有穿透标签
		if (!PiercingUtil.hasPiercingTag(projectile)) {
			return;
		}

		// 获取穿透配置（恢复进度）
		PierceData pierceData = PiercingUtil.restorePiercingProgress(projectile);
		if (pierceData == null) {
			return;
		}

		// 执行穿透检测
		List<Entity> hitEntities = PiercingUtil.performPierceDetection(
			projectile,
			pierceData,
			0.5
		);

		// 处理命中的实体
		for (Entity hitEntity : hitEntities) {
			onPierceHit(projectile, hitEntity, pierceData);
		}

		// 更新穿透进度到 NBT
		PiercingUtil.updatePiercingProgress(projectile, pierceData);

		// 检查是否达到穿透上限
		if (!pierceData.canPierce()) {
			projectile.discard();
		}
	}

	/**
	 * 处理穿透命中
	 *
	 * @param projectile 弹射物
	 * @param hitEntity  被命中的实体
	 * @param pierceData 穿透数据
	 */
	private static void onPierceHit(Projectile projectile, Entity hitEntity, PierceData pierceData) {
		// 只对生物实体生效
		if (!(hitEntity instanceof LivingEntity livingTarget)) {
			return;
		}

		// 避免伤害射击者
		Entity owner = projectile.getOwner();
		if (owner != null && owner.equals(hitEntity)) {
			return;
		}

		// 计算当前伤害
		float damage = pierceData.getCurrentDamage();

		// 应用伤害（使用魔法伤害类型）
		DamageSource damageSource = livingTarget.damageSources().indirectMagic(projectile, owner);
		livingTarget.hurt(damageSource, damage);

		// 这里可以添加命中特效
		// spawnHitParticles(livingTarget);
	}
}
