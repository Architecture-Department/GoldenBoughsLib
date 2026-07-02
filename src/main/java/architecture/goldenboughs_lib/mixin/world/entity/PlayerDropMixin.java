package architecture.goldenboughs_lib.mixin.world.entity;

import architecture.goldenboughs_lib.event.PlayerDropItemEvent;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.common.NeoForge;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * 拦截玩家丢弃物品，触发 [PlayerDropItemEvent]。
 * 所有丢弃操作最终调用 [Player.drop(ItemStack, boolean, boolean)]。
 */
@Mixin(Player.class)
public class PlayerDropMixin {
    @Inject(at = @At("HEAD"), method = "drop(Lnet/minecraft/world/item/ItemStack;ZZ)", cancellable = true)
    private void gbl$onDropItem(ItemStack dropping, boolean dropAround, boolean includeThrowerName, CallbackInfoReturnable<ItemEntity> ci) {
        if (dropping.isEmpty()) return;

        PlayerDropItemEvent event = new PlayerDropItemEvent(
                (Player) (Object) this, dropping, dropAround
        );
        NeoForge.EVENT_BUS.post(event);

        if (event.isCanceled()) {
            ci.setReturnValue(null);
        }
    }
}
