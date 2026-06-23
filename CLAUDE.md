# GoldenBoughsLib

Mod ID: `goldenboughs_lib`

核心库模块。所有其他模块的基础依赖，提供伤害系统、美德系统、理智系统、行为树、HUD、物品接口等框架。

## 包结构

- `core/` — `Lib.kt`(@Mod), `LibClient.kt`, `LibConfig.kt`, `LibEventHooks.kt`
- `init/` — 注册表声明（AttachmentTypes, Attributes, EntityTypes, Items, MobEffects, SoundEvents 等）
- `api/` — 公开 API
  - `capability/` — 实体/物品/方块能力接口
  - `network/` — 网络包工具
  - `payload/` — 网络包基类
  - `world/entity/` — 行为树系统（BTNode, 组合/条件/装饰/叶子节点）
  - `world/item/` — 物品接口（IEgoItem, IEgoWeaponItem 等）
- `common/` — 物品、实体、弹射物、状态效果、粒子类型、网络包
- `client/` — HUD层（生命/理智/护盾条）、模型（盔甲/饰品/实体/物品）、粒子、渲染器
- `events/` — 事件监听器 + 注册中心（LivingEntity, Player, Projectile 事件；Capability, Command, Payload 等注册）
- `event/` — 自定义事件（ChopFlavorLayerEvent, AddItemDataComponentTooltipEvent）
- `eventexecute/` — 事件执行器（EntityRender, Input 事件执行）
- `mixed/` — Mixin 接口注入目标（IEntityLib, IPlayerLib, IDamageContainer 等）
- `config/` — 配置文件
- `datagen/` — 数据生成（物品模型、粒子、标签、i18n）
- `linkage/jade/` — Jade 插件联动
- `util/` — 工具类（LcLevel, LcDamageType, Virtue, Rationality, GunWeapon, 实体/世界/物品工具）
- `module/` — 子模块
  - `lc_damage/` — LC 伤害系统
  - `virtue/` — 美德系统
  - `rationality/` — 理智系统
  - `shield/` — 护盾系统
  - `corpse/` — 尸体实体
- `mixin/java/` — Java Mixin 类（Entity, Player, LivingEntity, DamageSource, ItemRenderer, GeckoLib 等）

## 核心系统

| 系统           | 说明                                                |
|--------------|---------------------------------------------------|
| LcLevel      | ZAYIN/TETH/HE/WAW/ALEPH 风险等级                      |
| LcDamageType | 四色伤害：PHYSICS/SPIRIT/EROSION/THE_SOUL              |
| 行为树          | Sequence/Selector/Parallel 等完整 BT 实现 + Blackboard |
| 美德系统         | Fortitude/Prudence/Temperance/Justice 属性系统        |
| 理智系统         | RationalityEvent + 恢复机制                           |
| 护盾系统         | 四种伤害类型吸收护盾                                        |
| 物品接口         | IEgoItem/IEgoWeaponItem/IGunWeapon 等接口层次          |

## 依赖

- 无上游模块依赖（基础库）

由 ImaginaryCraft 模块 jarJar 聚合。
