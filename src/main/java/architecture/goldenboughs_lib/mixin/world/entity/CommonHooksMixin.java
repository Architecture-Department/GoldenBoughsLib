package architecture.goldenboughs_lib.mixin.world.entity;

import architecture.goldenboughs_lib.event.PlayerDropItemEvent;
import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.common.CommonHooks;
import net.neoforged.neoforge.common.NeoForge;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(CommonHooks.class)
public abstract class CommonHooksMixin {
	@WrapMethod(method = "onPlayerTossEvent")
	private static ItemEntity goldenboughs_lib$onPlayerTossEvent(
		Player player,
		ItemStack item,
		boolean includeName,
		Operation<ItemEntity> original
	) {
		if (item.isEmpty()) {
			return original.call(player, item, includeName);
		}

		PlayerDropItemEvent event = NeoForge.EVENT_BUS.post(new PlayerDropItemEvent(player, item, includeName));
		if (event.isCanceled()) {
			return null;
		}

		return original.call(player, item, includeName);
	}
}
