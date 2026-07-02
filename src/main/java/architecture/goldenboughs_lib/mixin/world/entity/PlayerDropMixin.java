package architecture.goldenboughs_lib.mixin.world.entity;

import architecture.goldenboughs_lib.event.PlayerDropItemEvent;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.common.NeoForge;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * 拦截玩家丢弃物品（Q 键、Ctrl+Q、背包拖出等），触发 [PlayerDropItemEvent]。
 * 所有丢弃操作最终都会调用 [Player.drop]，在此统一拦截。
 */
@Mixin(Player.class)
public class PlayerDropMixin {
    @Inject(at = @At("HEAD"), method = "drop", cancellable = true)
    private void gbl$onDropItem(boolean dropAll, CallbackInfoReturnable<Boolean> ci) {
        Player self = (Player) (Object) this;
        ItemStack dropping = self.getInventory().getSelected();
        if (dropping.isEmpty()) return;

        PlayerDropItemEvent event = new PlayerDropItemEvent(
                self, dropping, dropAll
        );
        NeoForge.EVENT_BUS.post(event);

        if (event.isCanceled()) {
            ci.setReturnValue(false);
        }
    }
}
