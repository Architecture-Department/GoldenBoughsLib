package architecture.goldenboughs_lib.common.item;

import architecture.goldenboughs_lib.api.LcDamageType;
import architecture.goldenboughs_lib.api.world.item.IMeleeEgoWeaponItem;
import architecture.goldenboughs_lib.core.GoldenBoughsLib;
import architecture.goldenboughs_lib.core.capability.item.IItemLcDamageType;
import architecture.goldenboughs_lib.init.LibDataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.common.SimpleTier;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Set;

/// 混沌刀
public class ChaosKnifeItem extends SwordItem implements IMeleeEgoWeaponItem, IItemLcDamageType {
	public static final String KEY = GoldenBoughsLib.ID + ".item_tooltip.geo_describe.damage_type";
	private static final LcDamageType.Component DEFAULT_COMPONENT = new LcDamageType.Component(LcDamageType.PHYSICS, LcDamageType.values());

	public ChaosKnifeItem(Properties itemProperties, IMeleeEgoWeaponItem.Builder builder) {
		super(new SimpleTier(BlockTags.INCORRECT_FOR_IRON_TOOL, 0, builder.attackSpeed, builder.weaponDamage, 5, Ingredient::of),
			itemProperties.component(LibDataComponents.LC_DAMAGE_TYPE.get(),
				new LcDamageType.Component(LcDamageType.PHYSICS, LcDamageType.values())));
	}

	@Override
	@NotNull
	public InteractionResultHolder<ItemStack> use(@NotNull Level world, @NotNull Player playerEntity, @NotNull InteractionHand handUsed) {
		ItemStack itemStackInHand = playerEntity.getItemInHand(handUsed);
		itemStackInHand.update(LibDataComponents.LC_DAMAGE_TYPE, DEFAULT_COMPONENT,
			(damageType) -> {
				LcDamageType[] values = LcDamageType.values();
				int i = damageType.lcDamageType().getIndex() + 1;
				return new LcDamageType.Component(values[i >= values.length ? 0 : i], LcDamageType.values());
			});
		return InteractionResultHolder.success(itemStackInHand);
	}

	@Override
	public void appendHoverText(@NotNull ItemStack itemStack, @NotNull TooltipContext tooltipContext, @NotNull List<Component> tooltipComponents, @NotNull TooltipFlag tooltipFlag) {
		super.appendHoverText(itemStack, tooltipContext, tooltipComponents, tooltipFlag);
	}

	@Override
	public @Nullable LcDamageType getLcDamageType(ItemStack stack) {
		return getComponent(stack).lcDamageType();
	}

	private static LcDamageType.Component getComponent(ItemStack stack) {
		return stack.getOrDefault(LibDataComponents.LC_DAMAGE_TYPE, DEFAULT_COMPONENT);
	}

	@Override
	public @NotNull Set<LcDamageType> getCanCauseLcDamageTypes(ItemStack stack) {
		return getComponent(stack).canCauseLcDamageTypes();
	}
}
