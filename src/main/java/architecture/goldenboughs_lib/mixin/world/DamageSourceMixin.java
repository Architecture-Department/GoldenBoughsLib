package architecture.goldenboughs_lib.mixin.world;

import architecture.goldenboughs_lib.api.LcDamageType;
import architecture.goldenboughs_lib.api.LcLevel;
import architecture.goldenboughs_lib.mixed.IDamageSourceLib;
import architecture.goldenboughs_lib.util.LcDamageTypeUtil;
import architecture.goldenboughs_lib.util.LcLevelUtil;
import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import net.minecraft.core.Holder;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import javax.annotation.Nullable;

/**
 * @author 尽
 */
@Mixin(DamageSource.class)
public abstract class DamageSourceMixin implements IDamageSourceLib {
	@Unique
	@Nullable
	private LcDamageType goldenboughs_lib$lcDamageType;

	@Unique
	@Nullable
	private LcLevel goldenboughs_lib$lcDamageLevel;

	@Unique
	private ItemStack goldenboughs_lib$attackItemStack;

	@Inject(method = "<init>(Lnet/minecraft/core/Holder;Lnet/minecraft/world/entity/Entity;Lnet/minecraft/world/entity/Entity;Lnet/minecraft/world/phys/Vec3;)V", at = @At("RETURN"))
	private void goldenboughs_lib$DamageSource(
		Holder<DamageType> type,
		Entity directEntity,
		Entity causingEntity,
		Vec3 damageSourcePosition,
		CallbackInfo ci
	) {
		DamageSource damageSource = (DamageSource) (Object) this;
		ItemStack itemStack = LcDamageTypeUtil.getDamageItemStack(damageSource);
		this.goldenboughs_lib$attackItemStack = itemStack;

		@Nullable LcDamageType itemLcDamageType = null;
		@Nullable LcDamageType damageLcDamageType = LcDamageType.byDamageType(type);


		// 从物品获取信息
		if (itemStack != null) {
			if (itemStack.isEmpty()) {
				itemLcDamageType = LcDamageType.byDamageType(type);
				if (itemLcDamageType == null) {
					itemLcDamageType = LcDamageTypeUtil.getLcDamageType(itemStack);
				}
			} else {
				itemLcDamageType = LcDamageTypeUtil.getLcDamageType(itemStack);
			}
		}

		if (directEntity != null || causingEntity != null) {
			Entity entity = directEntity == null ? causingEntity : directEntity;
			var level = LcLevelUtil.getLevel(entity);
			if (level == LcLevel.ZAYIN || entity instanceof Player) {
				@Nullable LcLevel itemLcDamageLevel = null;
				if (itemStack != null) {
					if (itemLcDamageLevel == null) {
						itemLcDamageLevel = LcLevelUtil.getLevel(itemStack);
					}
				}

				level = itemLcDamageLevel;
			}

			this.goldenboughs_lib$lcDamageLevel = level;
		}

		this.goldenboughs_lib$lcDamageType = itemLcDamageType == null ? damageLcDamageType : itemLcDamageType;
	}

	@Override
	public void goldenboughs_lib$setWeaponItem(ItemStack itemStack) {
		goldenboughs_lib$attackItemStack = itemStack;
	}

	@Nullable
	@Override
	public LcDamageType goldenboughs_lib$getLcDamageType() {
		return goldenboughs_lib$lcDamageType;
	}

	@Override
	public void goldenboughs_lib$setLcDamageType(LcDamageType type) {
		this.goldenboughs_lib$lcDamageType = type;
	}

	@Nullable
	@Override
	public LcLevel goldenboughs_lib$getLcDamageLevel() {
		return goldenboughs_lib$lcDamageLevel;
	}

	@Override
	public void goldenboughs_lib$setDamageLevel(@Nullable LcLevel level) {
		this.goldenboughs_lib$lcDamageLevel = level;
	}

	@WrapMethod(method = "getWeaponItem")
	private ItemStack goldenboughs_lib$getWeaponItem(Operation<ItemStack> original) {
		return goldenboughs_lib$attackItemStack == null ? original.call() : goldenboughs_lib$attackItemStack;
	}
}
