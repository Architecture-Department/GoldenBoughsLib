package architecture.goldenboughs_lib.event.rationality;

import architecture.goldenboughs_lib.init.LibAttributes;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.ICancellableEvent;
import net.neoforged.fml.event.IModBusEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;

public abstract class RationalityEvent extends PlayerEvent implements IModBusEvent {
	public RationalityEvent(Player player) {
		super(player);
	}

	public AttributeInstance getMax() {
		return getEntity().getAttribute(LibAttributes.MAX_RATIONALITY);
	}

	/**
	 * 获取自然恢复效率
	 */
	public AttributeInstance getNaturalRecoveryRate() {
		return getEntity().getAttribute(LibAttributes.RATIONALITY_NATURAL_RECOVERY_WAIT_TIME);
	}

	/**
	 * 获取自然恢复量
	 */
	public AttributeInstance getRecoveryAmount() {
		return getEntity().getAttribute(LibAttributes.RATIONALITY_RECOVERY_AMOUNT);
	}

	public abstract static class Modify extends RationalityEvent {
		private final float oldValue;

		public Modify(final Player player, float oldValue) {
			super(player);
			this.oldValue = oldValue;
		}

		public float getOldValue() {
			return oldValue;
		}

		public abstract float getNewValue();

		/**
		 * 可取消
		 */
		public static class Pre extends Modify implements ICancellableEvent {
			private float newValue;

			public Pre(final Player player, float oldValue, float newValue) {
				super(player, oldValue);
				this.newValue = newValue;
			}

			@Override
			public float getNewValue() {
				return newValue;
			}

			public void setNewValue(final float newValue) {
				this.newValue = newValue;
			}
		}

		/**
		 * 不可取消
		 */
		public static class Post extends Modify {
			private final float newValue;

			public Post(final Player player, float oldValue, float newValue) {
				super(player, oldValue);
				this.newValue = newValue;
			}

			@Override
			public float getNewValue() {
				return newValue;
			}
		}
	}
}
