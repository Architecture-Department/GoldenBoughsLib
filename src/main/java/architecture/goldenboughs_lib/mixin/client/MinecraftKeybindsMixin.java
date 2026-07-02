package architecture.goldenboughs_lib.mixin.client;

import architecture.goldenboughs_lib.event.PlayerHotbarChangeEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.common.NeoForge;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * 锁定热键栏槽位切换 —— 在 [Minecraft.handleKeybinds] 前后对比 selected 变化。
 * 若槽位发生变化（1-9 键），发射 [PlayerHotbarChangeEvent] 并在取消时还原。
 * 比拦截每个按键更简洁：任何改变 selected 的操作都会被捕获。
 */
@Mixin(Minecraft.class)
public class MinecraftKeybindsMixin {
    @Unique
    private int gbl$prevSelectedSlot = -1;

    @Inject(at = @At("HEAD"), method = "handleKeybinds")
    private void gbl$saveSelectedSlot(CallbackInfo ci) {
        Minecraft mc = (Minecraft) (Object) this;
        gbl$prevSelectedSlot = mc.player != null ? mc.player.getInventory().selected : -1;
    }

    @Inject(at = @At("RETURN"), method = "handleKeybinds")
    private void gbl$restoreSelectedSlot(CallbackInfo ci) {
        if (gbl$prevSelectedSlot < 0) return;
        Minecraft mc = (Minecraft) (Object) this;
        if (mc.player == null) return;

        int currentSlot = mc.player.getInventory().selected;
        if (currentSlot == gbl$prevSelectedSlot) return;

        // 槽位发生了变化，发射事件
        ItemStack fromStack = mc.player.getInventory().getItem(gbl$prevSelectedSlot);
        ItemStack toStack = mc.player.getInventory().getItem(currentSlot);

        PlayerHotbarChangeEvent event = new PlayerHotbarChangeEvent(
                mc.player, gbl$prevSelectedSlot, currentSlot, fromStack, toStack
        );
        NeoForge.EVENT_BUS.post(event);

        if (event.isCanceled()) {
            mc.player.getInventory().selected = gbl$prevSelectedSlot;
        }
    }
}
