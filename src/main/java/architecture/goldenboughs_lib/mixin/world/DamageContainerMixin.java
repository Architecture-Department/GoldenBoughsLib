package architecture.goldenboughs_lib.mixin.world;

import architecture.goldenboughs_lib.mixed.IDamageContainer;
import net.minecraft.world.damagesource.DamageSource;
import net.neoforged.neoforge.common.damagesource.DamageContainer;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(DamageContainer.class)
public abstract class DamageContainerMixin implements IDamageContainer {

	@Shadow
	@Final
	private float originalDamage;

	@Shadow
	public abstract void setPostAttackInvulnerabilityTicks(int ticks);

	@Shadow
	public abstract void setNewDamage(final float damage);

	@Inject(method = "<init>", at = @At("RETURN"))
	private void goldenboughs_lib$DamageContainer(DamageSource source, float originalDamage, CallbackInfo ci) {
	}

	@Unique
	public DamageContainer goldenboughs_lib$this() {
		return (DamageContainer) (Object) this;
	}
}
