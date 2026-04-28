package architecture.goldenboughs_lib.api.capability.item;

import architecture.goldenboughs_lib.api.LcLevel;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public enum ItemLcLevel implements IItemLcLevel {
	NULL() {
		@Override
		public @Nullable LcLevel getLcLevel(final ItemStack stack) {
			return null;
		}
	},
	ZAYIN() {
		@Override
		public @NotNull LcLevel getLcLevel(final ItemStack stack) {
			return LcLevel.ZAYIN;
		}
	},
	TETH() {
		@Override
		public @NotNull LcLevel getLcLevel(final ItemStack stack) {
			return LcLevel.TETH;
		}
	},
	HE() {
		@Override
		public @NotNull LcLevel getLcLevel(final ItemStack stack) {
			return LcLevel.HE;
		}
	},
	WAW() {
		@Override
		public @NotNull LcLevel getLcLevel(final ItemStack stack) {
			return LcLevel.WAW;
		}
	},
	ALEPH() {
		@Override
		public @NotNull LcLevel getLcLevel(final ItemStack stack) {
			return LcLevel.ALEPH;
		}
	}
}
