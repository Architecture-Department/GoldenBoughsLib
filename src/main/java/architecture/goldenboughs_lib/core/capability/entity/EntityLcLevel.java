package architecture.goldenboughs_lib.core.capability.entity;

import architecture.goldenboughs_lib.api.LcLevel;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public enum EntityLcLevel implements IEntityLcLevel {
	NULL() {
		@Override
		public @Nullable LcLevel getLcLevel() {
			return null;
		}
	},
	ZAYIN() {
		@Override
		public @NotNull LcLevel getLcLevel() {
			return LcLevel.ZAYIN;
		}
	},
	TETH() {
		@Override
		public @NotNull LcLevel getLcLevel() {
			return LcLevel.TETH;
		}
	},
	HE() {
		@Override
		public @NotNull LcLevel getLcLevel() {
			return LcLevel.HE;
		}
	},
	WAW() {
		@Override
		public @NotNull LcLevel getLcLevel() {
			return LcLevel.WAW;
		}
	},
	ALEPH() {
		@Override
		public @NotNull LcLevel getLcLevel() {
			return LcLevel.ALEPH;
		}
	}
}
