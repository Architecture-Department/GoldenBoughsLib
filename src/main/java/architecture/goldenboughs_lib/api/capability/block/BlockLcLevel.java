package architecture.goldenboughs_lib.api.capability.block;

import architecture.goldenboughs_lib.api.LcLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public enum BlockLcLevel implements IBlockLcLevel {
	NULL() {
		@Override
		public @Nullable LcLevel getLcLevel(final Level level, final BlockPos pos) {
			return null;
		}
	},
	ZAYIN() {
		@Override
		public @NotNull LcLevel getLcLevel(final Level level, final BlockPos pos) {
			return LcLevel.ZAYIN;
		}
	},
	TETH() {
		@Override
		public @NotNull LcLevel getLcLevel(final Level level, final BlockPos pos) {
			return LcLevel.TETH;
		}
	},
	HE() {
		@Override
		public @NotNull LcLevel getLcLevel(final Level level, final BlockPos pos) {
			return LcLevel.HE;
		}
	},
	WAW() {
		@Override
		public @NotNull LcLevel getLcLevel(final Level level, final BlockPos pos) {
			return LcLevel.WAW;
		}
	},
	ALEPH() {
		@Override
		public @NotNull LcLevel getLcLevel(final Level level, final BlockPos pos) {
			return LcLevel.ALEPH;
		}
	}
}
