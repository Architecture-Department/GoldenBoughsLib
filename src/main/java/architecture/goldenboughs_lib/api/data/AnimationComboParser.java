package architecture.goldenboughs_lib.api.data;

import architecture.goldenboughs_lib.core.GoldenBoughsLib;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Unique;
import yesman.epicfight.api.animation.AnimationManager;
import yesman.epicfight.api.animation.types.AttackAnimation;
import yesman.epicfight.api.animation.types.StaticAnimation;
import yesman.epicfight.world.capabilities.entitypatch.LivingEntityPatch;
import yesman.epicfight.world.capabilities.provider.ExtraEntryProvider;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 动画连招解析器
 * <p>负责解析武器配置中的动画和连招数据</p>
 */
public final class AnimationComboParser {

	private static final Logger LOGGER = GoldenBoughsLib.LOGGER;

	/**
	 * 解析连招动画列表
	 *
	 * @param resourceLocation   资源位置
	 * @param extraEntryProvider 额外条目提供者
	 * @param styleListTag       样式列表标签
	 * @return 动画提供者函数列表
	 */
	@Unique
	public static List<Function<LivingEntityPatch<?>, AnimationManager.AnimationAccessor<? extends AttackAnimation>>> parseComboAnimations(ResourceLocation resourceLocation, @SuppressWarnings("removal") ExtraEntryProvider extraEntryProvider, ListTag styleListTag) {
		return styleListTag.stream().map(styleTag -> parseComboAnimation(resourceLocation, extraEntryProvider, styleTag)).collect(Collectors.toList());
	}

	/**
	 * 解析单个连招动画（支持条件化配置）
	 *
	 * @param resourceLocation   资源位置
	 * @param extraEntryProvider 额外条目提供者
	 * @param styleTag           样式标签
	 * @return 条件化动画提供者函数
	 */
	@Unique
	@SuppressWarnings("unchecked")
	public static Function<LivingEntityPatch<?>, AnimationManager.AnimationAccessor<? extends AttackAnimation>> parseComboAnimation(ResourceLocation resourceLocation, @SuppressWarnings("removal") ExtraEntryProvider extraEntryProvider, Tag styleTag) {
		if (!(styleTag instanceof CompoundTag styleCompoundTag)) {
			var defaultAnim = AnimationComboParser.<AttackAnimation>getAnimationAccessor(resourceLocation, extraEntryProvider, styleTag.getAsString());
			return entitypatch -> defaultAnim;
		}
		return ConditionalProviderFactory.getProvider(AnimationComboParser.<AttackAnimation>getAnimationAccessor(resourceLocation, extraEntryProvider, styleCompoundTag.getString(ConditionalEntryParser.DEFAULT)), ConditionalEntryParser.parseCases(styleCompoundTag, valueString -> AnimationComboParser.getAnimationAccessor(resourceLocation, extraEntryProvider, valueString), (accessor, string) -> accessor != null));
	}

	/**
	 * 获取动画访问器
	 *
	 * @param resourceLocation   资源位置
	 * @param extraEntryProvider 额外条目提供者
	 * @param animationId        动画 ID
	 * @return 动画访问器，找不到时返回 null
	 */
	@Nullable
	@Unique
	public static <T extends StaticAnimation> AnimationManager.AnimationAccessor<? extends T> getAnimationAccessor(ResourceLocation resourceLocation, @SuppressWarnings("removal") @Nullable ExtraEntryProvider extraEntryProvider, String animationId) {
		AnimationManager.AnimationAccessor<? extends T> animation;
		if (extraEntryProvider == null) {
			animation = AnimationManager.byKey(animationId);
		} else {
			//noinspection removal
			animation = extraEntryProvider.getExtraOrBuiltInAnimation(animationId);
		}

		if (animation == null) {
			LOGGER.warn("Cannot find animation '{}' in {}", animationId, resourceLocation);
		}

		return animation;
	}
}
