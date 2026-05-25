package architecture.goldenboughs_lib.mixin.client.ui;

import architecture.goldenboughs_lib.util.GunWeaponUtil;
import com.llamalad7.mixinextras.expression.Definition;
import com.llamalad7.mixinextras.expression.Expression;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(Gui.class)
public abstract class GuiMixin {

	@Shadow
	@Final
	private Minecraft minecraft;

	@Definition(id = "minecraft", field = "Lnet/minecraft/client/gui/Gui;minecraft:Lnet/minecraft/client/Minecraft;")
	@Definition(id = "options", field = "Lnet/minecraft/client/Minecraft;options:Lnet/minecraft/client/Options;")
	@Definition(id = "attackIndicator", method = "Lnet/minecraft/client/Options;attackIndicator()Lnet/minecraft/client/OptionInstance;")
	@Definition(id = "get", method = "Lnet/minecraft/client/OptionInstance;get()Ljava/lang/Object;")
	@Definition(id = "CROSSHAIR", field = "Lnet/minecraft/client/AttackIndicatorStatus;CROSSHAIR:Lnet/minecraft/client/AttackIndicatorStatus;")
	@Expression("this.minecraft.options.attackIndicator().get() == CROSSHAIR")
	@ModifyExpressionValue(method = "renderCrosshair", at = @At("MIXINEXTRAS:EXPRESSION"))
	private boolean goldenboughs_lib$renderCrosshair(boolean original) {
		if (GunWeaponUtil.is(minecraft.player)) {
			return false;
		}
		return original;
	}

	@Definition(id = "minecraft", field = "Lnet/minecraft/client/gui/Gui;minecraft:Lnet/minecraft/client/Minecraft;")
	@Definition(id = "options", field = "Lnet/minecraft/client/Minecraft;options:Lnet/minecraft/client/Options;")
	@Definition(id = "attackIndicator", method = "Lnet/minecraft/client/Options;attackIndicator()Lnet/minecraft/client/OptionInstance;")
	@Definition(id = "get", method = "Lnet/minecraft/client/OptionInstance;get()Ljava/lang/Object;")
	@Definition(id = "HOTBAR", field = "Lnet/minecraft/client/AttackIndicatorStatus;HOTBAR:Lnet/minecraft/client/AttackIndicatorStatus;")
	@Expression("this.minecraft.options.attackIndicator().get() == HOTBAR")
	@ModifyExpressionValue(method = "renderItemHotbar", at = @At("MIXINEXTRAS:EXPRESSION"))
	private boolean goldenboughs_lib$renderItemHotbar(boolean original) {
		if (GunWeaponUtil.is(minecraft.player)) {
			return false;
		}
		return original;
	}
}
