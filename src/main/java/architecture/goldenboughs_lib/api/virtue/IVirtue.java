package architecture.goldenboughs_lib.api.virtue;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;

import java.util.Map;
import java.util.Set;

/**
 * 心核四德接口
 */
public interface IVirtue {
	/**
	 * 获取对应的心核四德类型
	 *
	 * @return 四德类型
	 */
	VirtueType getVirtue();

	/**
	 * 获取会影响的属性实例和属性修改器
	 *
	 * @return 属性实例和属性修改器
	 */
	Map<AttributeInstance, Set<AttributeModifier>> getAffectedAttributeAndModifiers();

	/**
	 * 获取关联属性Holder和评级值
	 *
	 * @return 关联属性Holder和评级值
	 */
	Map<AttributeInstance, Float> getCorrelationAttributesHolder();

	/**
	 * 获取点数属性实例
	 *
	 * @return 点数属性实例
	 */
	AttributeInstance getPointsAttributeInstance();

	/**
	 * 获取评级
	 *
	 * @return 评级
	 */
	VirtueRating getRating();

	/**
	 * 获取数据附件处理者
	 *
	 * @return 数据附件处理者
	 */
	Player getPlayer();

	/**
	 * 获取点数
	 *
	 * @return 点数
	 */
	int getPoints();

	/**
	 * 设置点数
	 *
	 * @param points 点数
	 */
	void setPoints(int points);

	/**
	 * 获取评级点数
	 *
	 * @return 评级点数
	 */
	int getRatingPoints();

	/**
	 * 获取振幅ID
	 *
	 * @return 振幅ID
	 */
	ResourceLocation getAmplitudeId();

	/**
	 * 设置点数
	 *
	 * @param points 点数
	 */
	void setPoints(int points, ResourceLocation modifierId);

	/**
	 * 同步数据
	 */
	void syncData();

	/**
	 * 更新触发
	 * <p>
	 * 在修改相关的时候触发
	 */
	void updateTrigger();

	/**
	 * 更新点数
	 *
	 * @param newPoints 新的点数
	 */
	void updatePoints(int newPoints);

	/**
	 * 在原来的基础上修改点数
	 *
	 * @param points 要修改的点数
	 */
	void modifyPoints(int points);

	/**
	 * 在原来的基础上修改点数
	 *
	 * @param points 要修改的点数
	 */
	void modifyPoints(int points, ResourceLocation modifierId);
}
