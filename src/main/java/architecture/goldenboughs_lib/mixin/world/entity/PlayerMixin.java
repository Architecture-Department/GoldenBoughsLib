package architecture.goldenboughs_lib.mixin.world.entity;

import architecture.goldenboughs_lib.event.PlayerDropItemEvent;
import architecture.goldenboughs_lib.mixed.IPlayerLib;
import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.common.NeoForge;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(Player.class)
public abstract class PlayerMixin implements IPlayerLib {

}
