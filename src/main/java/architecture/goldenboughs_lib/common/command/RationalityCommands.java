package architecture.goldenboughs_lib.common.command;

import architecture.goldenboughs_lib.datagen.i18n.DatagenI18n;
import architecture.goldenboughs_lib.init.LibAttributes;
import architecture.goldenboughs_lib.util.RationalityUtil;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.FloatArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import org.jetbrains.annotations.NotNull;

public class RationalityCommands {
	public static final String SET_KEY = "set_rationality";
	public static final String GET_KEY = "get_rationality";
	public static final String RESET_KEY = "reset_rationality";
	public static final String SUGGESTIONS = "rationality.suggestions.";
	public static final String FILL_SET_KEY = SUGGESTIONS + "set";
	public static final String FILL_GET_KEY = SUGGESTIONS + "get";
	public static final String FILL_VALUE_KEY = SUGGESTIONS + "value";
	public static final FloatArgumentType ARG = FloatArgumentType.floatArg();

	public static void processRationality(@NotNull CommandDispatcher<CommandSourceStack> dispatcher) {
		dispatcher.register(Commands.literal("rationality")
			.requires(source -> source.hasPermission(2))
			.then(Commands.argument("target", EntityArgument.player())
				.then(Commands.literal("set")
					.then(logic(ProcessType.VALUE, true))
					.then(logic(ProcessType.MAX_VALUE, true))
					.then(logic(ProcessType.NATURAL_RECOVERY_RATE, true))
					.then(logic(ProcessType.RATIONALITY_RECOVERY_AMOUNT, true)))
				.then(Commands.literal("get")
					.then(logic(ProcessType.VALUE, false))
					.then(logic(ProcessType.MAX_VALUE, false))
					.then(logic(ProcessType.NATURAL_RECOVERY_RATE, false))
					.then(logic(ProcessType.RATIONALITY_RECOVERY_AMOUNT, false)))
				.then(Commands.literal("reset")
					.executes(context -> {
						ServerPlayer player = EntityArgument.getPlayer(context, "target");
						RationalityUtil.setValue(player, 0, false);
						RationalityUtil.setBaseMaxValue(player, (float) LibAttributes.MAX_RATIONALITY.value().getDefaultValue());
						RationalityUtil.setBaseNaturalRecoveryRate(player, (float) LibAttributes.RATIONALITY_NATURAL_RECOVERY_WAIT_TIME.value().getDefaultValue());
						RationalityUtil.setBaseRationalityRecoveryAmount(player, (float) LibAttributes.RATIONALITY_RECOVERY_AMOUNT.value().getDefaultValue());
						context.getSource().sendSuccess(() ->
							Component.translatable(DatagenI18n.getFormattedKey(RESET_KEY), player.getName()), true);
						return 1;
					}))
				.then(Commands.literal("reset")
					.then(reset(ProcessType.VALUE))
					.then(reset(ProcessType.MAX_VALUE))
					.then(reset(ProcessType.NATURAL_RECOVERY_RATE))
					.then(reset(ProcessType.RATIONALITY_RECOVERY_AMOUNT))
				)
			)
		);
	}

	private static LiteralArgumentBuilder<CommandSourceStack> reset(@NotNull ProcessType processType) {
		final String name = processType.getName();
		return Commands.literal(name).executes(context -> {
			ServerPlayer player = EntityArgument.getPlayer(context, "target");
			float value = 0;
			switch (processType) {
				case VALUE -> RationalityUtil.setValue(player, 0, false);
				case MAX_VALUE ->
					RationalityUtil.setBaseMaxValue(player, value = (float) LibAttributes.MAX_RATIONALITY.value().getDefaultValue());
				case NATURAL_RECOVERY_RATE ->
					RationalityUtil.setBaseNaturalRecoveryRate(player, value = (float) LibAttributes.RATIONALITY_NATURAL_RECOVERY_WAIT_TIME.value().getDefaultValue());
				case RATIONALITY_RECOVERY_AMOUNT ->
					RationalityUtil.setBaseRationalityRecoveryAmount(player, value = (float) LibAttributes.RATIONALITY_RECOVERY_AMOUNT.value().getDefaultValue());
			}
			final float finalValue = value;
			context.getSource().sendSuccess(() ->
				Component.translatable(DatagenI18n.getFormattedKey(RESET_KEY, name), player.getName(), finalValue), true);
			return 1;
		});
	}

	private static LiteralArgumentBuilder<CommandSourceStack> logic(@NotNull ProcessType processType, boolean isSet) {
		final String name = processType.getName();


		var literal = Commands.literal(name);
		literal = isSet ?
			literal.then(Commands.argument("value", ARG).executes(logic(processType, true, name))) :
			literal.executes(logic(processType, false, name));
		return literal;
	}

	private static @NotNull Command<CommandSourceStack> logic(final @NotNull ProcessType processType, final boolean isSet, final String name) {
		return context -> {
			ServerPlayer player = EntityArgument.getPlayer(context, "target");
			float value = 0;
			if (isSet) {
				value = FloatArgumentType.getFloat(context, "value");
			}
			switch (processType) {
				case VALUE -> {
					if (isSet) {
						RationalityUtil.setValue(player, value, false);
					} else {
						value = RationalityUtil.getValue(player);
					}
				}
				case MAX_VALUE -> {
					if (isSet) {
						RationalityUtil.setBaseMaxValue(player, value);
					} else {
						value = RationalityUtil.getMaxValue(player);
					}
				}
				case NATURAL_RECOVERY_RATE -> {
					if (isSet) {
						RationalityUtil.setBaseNaturalRecoveryRate(player, value);
					} else {
						value = RationalityUtil.getNaturalRecoveryRate(player);
					}
				}
				case RATIONALITY_RECOVERY_AMOUNT -> {
					if (isSet) {
						RationalityUtil.setBaseRationalityRecoveryAmount(player, value);
					} else {
						value = RationalityUtil.getRationalityRecoveryAmount(player);
					}
				}
			}
			final double finalValue = value;
			context.getSource().sendSuccess(() ->
				Component.translatable(DatagenI18n.getFormattedKey(isSet ? SET_KEY : GET_KEY, name), player.getName(), finalValue), true);
			return 1;
		};
	}

	public enum ProcessType {
		VALUE("value"),
		MAX_VALUE("maxValue"),
		NATURAL_RECOVERY_RATE("natural_recovery_rate"),
		RATIONALITY_RECOVERY_AMOUNT("rationality_recovery_amount"),
		;

		private final String name;

		ProcessType(String name) {
			this.name = name;
		}

		public String getName() {
			return name;
		}
	}
}
