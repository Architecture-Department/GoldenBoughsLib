package architecture.goldenboughs_lib.util

import com.mojang.brigadier.context.CommandContext
import com.mojang.brigadier.context.ParsedArgument

object CommandContextUtil {
	fun <T> CommandContext<T>.getArguments(): Map<String, ParsedArgument<T, *>>? {
		try {
			val argumentsField = javaClass.getDeclaredField("arguments")
			argumentsField.isAccessible = true
			@Suppress("UNCHECKED_CAST")
			return argumentsField.get(this) as? Map<String, ParsedArgument<T, *>>
		} catch (exception: Exception) {
			LibUtil.LOGGER.error("CommandContext.getArguments() error: ${exception.message}")
		}
		return null
	}
}
