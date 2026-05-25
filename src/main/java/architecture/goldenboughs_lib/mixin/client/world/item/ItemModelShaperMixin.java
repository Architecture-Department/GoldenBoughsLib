package architecture.goldenboughs_lib.mixin.client.world.item;

import architecture.goldenboughs_lib.mixed.client.IItemModelShaper;
import net.minecraft.client.renderer.ItemModelShaper;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(ItemModelShaper.class)
public abstract class ItemModelShaperMixin implements IItemModelShaper {
}
