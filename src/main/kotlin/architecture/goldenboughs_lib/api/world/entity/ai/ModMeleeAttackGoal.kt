package architecture.goldenboughs_lib.api.world.entity.ai

import architecture.goldenboughs_lib.api.AllOpen
import net.minecraft.world.InteractionHand
import net.minecraft.world.entity.EntitySelector
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.Mob
import net.minecraft.world.entity.ai.goal.Goal
import net.minecraft.world.entity.player.Player
import net.minecraft.world.level.pathfinder.Path
import java.util.*
import kotlin.math.max

@AllOpen
class ModMeleeAttackGoal(
	protected val mob: Mob,
	private val speedModifier: Double,
	private val followingTargetEvenIfNotSeen: Boolean
) : Goal() {
	protected val attackInterval: Int
		get() = this.adjustedTickDelay(20)
	private val canPenalize = false
	private var path: Path? = null
	private var pathedTargetX = 0.0
	private var pathedTargetY = 0.0
	private var pathedTargetZ = 0.0
	private var ticksUntilNextPathRecalculation = 0
	protected final var ticksUntilNextAttack: Int = 0
		private set
	private var lastCanUseCheck: Long = 0
	private var failedPathFindingPenalty = 0

	init {
		this.flags = EnumSet.of(Flag.MOVE, Flag.LOOK)
	}

	override fun canUse(): Boolean {
		val i = this.mob.level().gameTime
		if (i - this.lastCanUseCheck < 20L) {
			return false
		}

		this.lastCanUseCheck = i
		val livingEntity = this.mob.target ?: return false

		if (!livingEntity.isAlive) {
			return false
		}

		if (canPenalize) {
			if (--this.ticksUntilNextPathRecalculation <= 0) {
				this.path = this.mob.getNavigation().createPath(livingEntity, 0)
				this.ticksUntilNextPathRecalculation = 4 + this.mob.getRandom().nextInt(7)
				return this.path != null
			} else {
				return true
			}
		}

		this.path = this.mob.getNavigation().createPath(livingEntity, 0)
		return this.path != null || this.mob.isWithinMeleeAttackRange(livingEntity)
	}

	override fun canContinueToUse(): Boolean {
		val livingEntity = this.mob.target ?: return false

		if (!livingEntity.isAlive) {
			return false
		}

		if (!this.followingTargetEvenIfNotSeen) {
			return !this.mob.getNavigation().isDone
		}

		return this.mob.isWithinRestriction(livingEntity.blockPosition()) && (livingEntity !is Player || !livingEntity.isSpectator && !livingEntity.isCreative)
	}

	override fun start() {
		this.mob.getNavigation().moveTo(this.path, this.speedModifier)
		this.mob.isAggressive = true
		this.ticksUntilNextPathRecalculation = 0
		this.ticksUntilNextAttack = 0
	}

	override fun stop() {
		val livingEntity = this.mob.target
		if (!EntitySelector.NO_CREATIVE_OR_SPECTATOR.test(livingEntity)) {
			this.mob.target = null
		}

		this.mob.isAggressive = false
		this.mob.getNavigation().stop()
	}

	override fun requiresUpdateEveryTick(): Boolean {
		return true
	}

	override fun tick() {
		val livingEntity = this.mob.target ?: return

		this.mob.getLookControl().setLookAt(livingEntity, 30.0f, 30.0f)
		this.ticksUntilNextPathRecalculation = max(this.ticksUntilNextPathRecalculation - 1, 0)
		if ((this.followingTargetEvenIfNotSeen || this.mob.sensing.hasLineOfSight(livingEntity))
			&& this.ticksUntilNextPathRecalculation <= 0 && (this.pathedTargetX == 0.0 && this.pathedTargetY == 0.0 && this.pathedTargetZ == 0.0 || livingEntity.distanceToSqr(
				this.pathedTargetX,
				this.pathedTargetY,
				this.pathedTargetZ
			) >= 1.0 || this.mob.getRandom().nextFloat() < 0.05f
				)
		) {
			this.pathedTargetX = livingEntity.x
			this.pathedTargetY = livingEntity.y
			this.pathedTargetZ = livingEntity.z
			this.ticksUntilNextPathRecalculation = 4 + this.mob.getRandom().nextInt(7)
			val d0 = this.mob.distanceToSqr(livingEntity)
			if (this.canPenalize) {
				this.ticksUntilNextPathRecalculation += failedPathFindingPenalty
				if (this.mob.getNavigation().getPath() != null) {
					val finalPathPoint = this.mob.getNavigation().getPath()!!.endNode
					if (finalPathPoint != null && livingEntity.distanceToSqr(
							finalPathPoint.x.toDouble(),
							finalPathPoint.y.toDouble(),
							finalPathPoint.z.toDouble()
						) < 1
					) failedPathFindingPenalty = 0
					else failedPathFindingPenalty += 10
				} else {
					failedPathFindingPenalty += 10
				}
			}
			if (d0 > 1024.0) {
				this.ticksUntilNextPathRecalculation += 10
			} else if (d0 > 256.0) {
				this.ticksUntilNextPathRecalculation += 5
			}

			if (!this.mob.getNavigation().moveTo(livingEntity, this.speedModifier)) {
				this.ticksUntilNextPathRecalculation += 15
			}

			this.ticksUntilNextPathRecalculation = this.adjustedTickDelay(this.ticksUntilNextPathRecalculation)
		}

		this.ticksUntilNextAttack = max(this.ticksUntilNextAttack - 1, 0)
		this.checkAndPerformAttack(livingEntity)
	}

	protected fun checkAndPerformAttack(target: LivingEntity) {
		if (!this.canPerformAttack(target)) {
			return
		}

		this.resetAttackCooldown()
		this.mob.swing(InteractionHand.MAIN_HAND)
		this.mob.doHurtTarget(target)
	}

	protected fun resetAttackCooldown() {
		this.ticksUntilNextAttack = this.adjustedTickDelay(20)
	}

	protected fun canPerformAttack(entity: LivingEntity): Boolean {
		return this.isTimeToAttack && this.mob.isWithinMeleeAttackRange(entity) && this.mob.sensing
			.hasLineOfSight(entity)
	}

	protected val isTimeToAttack: Boolean
		get() = this.ticksUntilNextAttack <= 0

	companion object {
		private const val COOLDOWN_BETWEEN_CAN_USE_CHECKS = 20L
	}
}
