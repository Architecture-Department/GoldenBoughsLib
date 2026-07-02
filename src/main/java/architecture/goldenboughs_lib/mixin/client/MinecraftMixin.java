package architecture.goldenboughs_lib.mixin.client;

import architecture.goldenboughs_lib.core.LibEventHooks;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Inventory;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

import javax.annotation.Nullable;

@Mixin(Minecraft.class)
public abstract class MinecraftMixin {
	@Shadow
	@Nullable
	public LocalPlayer player;

	@WrapOperation(method = "handleKeybinds", at = @At(value = "FIELD", target = "Lnet/minecraft/world/entity/player/Inventory;selected:I", opcode = Opcodes.PUTFIELD))
	private void goldenboughs_lib$handleKeybindsHotbarSlots(Inventory inventory, int newValue, Operation<Void> original) {
		if (player == null) {
			original.call(inventory, newValue);
			return;
		}

		var event = LibEventHooks.playerHotbarChange(
			player, inventory.selected, newValue,
			inventory.getSelected(), inventory.getItem(newValue)
		);

		if (event.isCanceled()) {
			return;
		}
		original.call(inventory, event.getToSlot());
	}

	@WrapOperation(method = "handleKeybinds", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/player/LocalPlayer;drop(Z)Z"))
	private boolean goldenboughs_lib$handleKeybindsDrop(LocalPlayer instance, boolean fullStack, Operation<Boolean> original) {
		if (player == null) {
			return original.call(instance, fullStack);
		}

		var event = LibEventHooks.playerDropItem(player, InteractionHand.MAIN_HAND);
		if (event.isCanceled()) {
			return false;
		}

		return original.call(instance, fullStack);
	}

	@WrapOperation(method = "pickBlock", at = @At(value = "FIELD", target = "Lnet/minecraft/world/entity/player/Inventory;selected:I", opcode = Opcodes.PUTFIELD))
	private void goldenboughs_lib$pickBlock(Inventory inventory, int newValue, Operation<Void> original) {
		if (player == null) {
			original.call(inventory, newValue);
			return;
		}

		var event = LibEventHooks.playerHotbarChange(
			player, inventory.selected, newValue,
			inventory.getSelected(), inventory.getItem(newValue)
		);

		if (event.isCanceled()) {
			return;
		}
		original.call(inventory, event.getToSlot());
	}
}
