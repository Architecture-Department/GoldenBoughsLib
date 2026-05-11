package architecture.goldenboughs_lib.common.command

import architecture.goldenboughs_lib.datagen.i18n.DatagenI18n
import architecture.goldenboughs_lib.init.LibAttributes
import architecture.goldenboughs_lib.util.RationalityUtil.getMaxRationalityValue
import architecture.goldenboughs_lib.util.RationalityUtil.getNaturalRecoveryRate
import architecture.goldenboughs_lib.util.RationalityUtil.getRationalityRecoveryAmount
import architecture.goldenboughs_lib.util.RationalityUtil.getRationalityValue
import architecture.goldenboughs_lib.util.RationalityUtil.setBaseMaxValue
import architecture.goldenboughs_lib.util.RationalityUtil.setBaseNaturalRecoveryRate
import architecture.goldenboughs_lib.util.RationalityUtil.setBaseRationalityRecoveryAmount
import architecture.goldenboughs_lib.util.RationalityUtil.setRationalityValue
import com.mojang.brigadier.Command
import com.mojang.brigadier.CommandDispatcher
import com.mojang.brigadier.arguments.FloatArgumentType
import com.mojang.brigadier.builder.LiteralArgumentBuilder
import com.mojang.brigadier.context.CommandContext
import net.minecraft.commands.CommandSourceStack
import net.minecraft.commands.Commands
import net.minecraft.commands.arguments.EntityArgument
import net.minecraft.network.chat.Component
import net.minecraft.server.level.ServerPlayer

object RationalityCommands {

	const val SET_KEY: String = "set_rationality"
	const val GET_KEY: String = "get_rationality"
	const val RESET_KEY: String = "reset_rationality"
	const val SUGGESTIONS: String = "rationality.suggestions."

	@JvmField
	val FILL_SET_KEY: String = SUGGESTIONS + "set"

	@JvmField
	val FILL_GET_KEY: String = SUGGESTIONS + "get"

	@JvmField
	val FILL_VALUE_KEY: String = SUGGESTIONS + "value"

	@JvmField
	val ARG: FloatArgumentType = FloatArgumentType.floatArg()

	@JvmStatic
	fun processRationality(dispatcher: CommandDispatcher<CommandSourceStack>) {
		dispatcher.register(
			Commands.literal("rationality")
				.requires { source: CommandSourceStack -> source.hasPermission(2) }
				.then(
					Commands.argument("target", EntityArgument.player())
						.then(
							Commands.literal("set")
								.then(logic(ProcessType.VALUE, true))
								.then(logic(ProcessType.MAX_VALUE, true))
								.then(logic(ProcessType.NATURAL_RECOVERY_RATE, true))
								.then(logic(ProcessType.RATIONALITY_RECOVERY_AMOUNT, true))
						)
						.then(
							Commands.literal("get")
								.then(logic(ProcessType.VALUE, false))
								.then(logic(ProcessType.MAX_VALUE, false))
								.then(logic(ProcessType.NATURAL_RECOVERY_RATE, false))
								.then(logic(ProcessType.RATIONALITY_RECOVERY_AMOUNT, false))
						)
						.then(
							Commands.literal("reset")
								.executes { context: CommandContext<CommandSourceStack> ->
									val player = getTargetPlayer(context)
									player.setRationalityValue(0f, false)
									setBaseMaxValue(
										player,
										LibAttributes.MAX_RATIONALITY.value().defaultValue.toFloat()
									)
									setBaseNaturalRecoveryRate(
										player,
										LibAttributes.RATIONALITY_NATURAL_RECOVERY_WAIT_TIME.value().defaultValue
											.toFloat()
									)
									player.setBaseRationalityRecoveryAmount(
										LibAttributes.RATIONALITY_RECOVERY_AMOUNT.value().defaultValue.toFloat()
									)
									context.getSource().sendSuccess({
										Component.translatable(
											DatagenI18n.getFormattedKey(RESET_KEY), player.name
										)
									}, true)
									1
								}
						)
						.then(
							Commands.literal("reset")
								.then(reset(ProcessType.VALUE))
								.then(reset(ProcessType.MAX_VALUE))
								.then(reset(ProcessType.NATURAL_RECOVERY_RATE))
								.then(reset(ProcessType.RATIONALITY_RECOVERY_AMOUNT))
						)
				)
		)
	}

	@JvmStatic
	private fun reset(processType: ProcessType): LiteralArgumentBuilder<CommandSourceStack> {
		val name = processType.typeName
		return Commands.literal(name).executes { context: CommandContext<CommandSourceStack> ->
			val player = getTargetPlayer(context)
			var value = 0f
			when (processType) {
				ProcessType.VALUE -> player.setRationalityValue(0f, false)
				ProcessType.MAX_VALUE -> setBaseMaxValue(
					player,
					LibAttributes.MAX_RATIONALITY.value().defaultValue.toFloat().also { value = it })

				ProcessType.NATURAL_RECOVERY_RATE -> setBaseNaturalRecoveryRate(
					player,
					LibAttributes.RATIONALITY_NATURAL_RECOVERY_WAIT_TIME.value().defaultValue.toFloat()
						.also { value = it })

				ProcessType.RATIONALITY_RECOVERY_AMOUNT -> player.setBaseRationalityRecoveryAmount(
					LibAttributes.RATIONALITY_RECOVERY_AMOUNT.value().defaultValue.toFloat().also { value = it })
			}
			val finalValue = value
			context.getSource().sendSuccess({
				Component.translatable(
					DatagenI18n.getFormattedKey(RESET_KEY, name),
					player.name,
					finalValue
				)
			}, true)
			1
		}
	}

	@JvmStatic
	private fun logic(processType: ProcessType, isSet: Boolean): LiteralArgumentBuilder<CommandSourceStack> {
		val name = processType.typeName

		var literal = Commands.literal(name)
		literal = if (isSet) literal.then(
			Commands.argument<Float>("value", ARG).executes(logic(processType, true, name))
		) else literal.executes(
			logic(processType, false, name)
		)
		return literal
	}

	@JvmStatic
	private fun logic(processType: ProcessType, isSet: Boolean, name: String): Command<CommandSourceStack> {
		return Command { context: CommandContext<CommandSourceStack> ->
			val player = getTargetPlayer(context)
			var value = 0f
			if (isSet) {
				value = FloatArgumentType.getFloat(context, "value")
			}
			when (processType) {
				ProcessType.VALUE -> {
					if (isSet) {
						player.setRationalityValue(value, false)
					} else {
						value = player.getRationalityValue()
					}
				}

				ProcessType.MAX_VALUE -> {
					if (isSet) {
						setBaseMaxValue(player, value)
					} else {
						value = player.getMaxRationalityValue()
					}
				}

				ProcessType.NATURAL_RECOVERY_RATE -> {
					if (isSet) {
						setBaseNaturalRecoveryRate(player, value)
					} else {
						value = getNaturalRecoveryRate(player)
					}
				}

				ProcessType.RATIONALITY_RECOVERY_AMOUNT -> {
					if (isSet) {
						player.setBaseRationalityRecoveryAmount(value)
					} else {
						value = player.getRationalityRecoveryAmount()
					}
				}
			}
			val finalValue = value.toDouble()
			context.getSource().sendSuccess({
				Component.translatable(
					DatagenI18n.getFormattedKey(
						if (isSet) SET_KEY else GET_KEY,
						name
					), player.name, finalValue
				)
			}, true)
			1
		}
	}

	@Suppress("CheckedExceptionsKotlin")
	@JvmStatic
	private fun getTargetPlayer(context: CommandContext<CommandSourceStack>): ServerPlayer =
		EntityArgument.getPlayer(context, "target")

	enum class ProcessType(@JvmField val typeName: String) {
		VALUE("value"),
		MAX_VALUE("maxValue"),
		NATURAL_RECOVERY_RATE("natural_recovery_rate"),
		RATIONALITY_RECOVERY_AMOUNT("rationality_recovery_amount"),
	}

}
