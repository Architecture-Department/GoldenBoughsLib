package architecture.goldenboughs_lib.api.world.entity

interface ISpawnByEgg {
	/**
	 * 需要特化刷怪蛋生成时调用
	 */
	fun onSpawnByEgg()
}
