package architecture.goldenboughs_lib.mixin.client;

import architecture.goldenboughs_lib.event.PlayerHotbarChangeEvent;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.common.NeoForge;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

/**
 * 拦截数字键 1-9 切换热键栏，触发 [PlayerHotbarChangeEvent]。
 * 重定向 [Minecraft.handleKeybinds] 中 [KeyMapping.consumeClick] 的调用，
 * 在热键栏切换前发射可取消事件。
 */
@Mixin(Minecraft.class)
public class MinecraftKeybindsMixin {
    @Redirect(
        method = "handleKeybinds",
        at = @At(value = "INVOKE", target = "Lnet/minecraft/client/KeyMapping;consumeClick()Z")
    )
    private boolean gbl$redirectHotbarClick(KeyMapping mapping) {
        Minecraft mc = (Minecraft) (Object) this;
        if (mc.player == null) return mapping.consumeClick();

        // 检查是否是热键栏按键
        for (int i = 0; i < 9; i++) {
            if (mc.options.keyHotbarSlots[i] == mapping) {
                boolean result = mapping.consumeClick();
                if (!result) return false;

                int fromSlot = mc.player.getInventory().selected;
                ItemStack fromStack = mc.player.getInventory().getSelected();
                ItemStack toStack = mc.player.getInventory().getItem(i);

                PlayerHotbarChangeEvent event = new PlayerHotbarChangeEvent(
                        mc.player, fromSlot, i, fromStack, toStack
                );
                NeoForge.EVENT_BUS.post(event);

                // 已取消 → 返回 false，handleKeybinds 不会执行 selected = i
                if (event.isCanceled()) return false;
                return true;
            }
        }

        // 非热键栏按键原样处理
        return mapping.consumeClick();
    }
}
