# GoldenBoughsLib

核心库模块。所有其他模块的基础依赖。

## 项目结构

```
src/main/
├── java/architecture/goldenboughs_lib/
│   └── mixin/
│       ├── client/world/   — ItemRendererMixin, BlockEntityWithoutLevelRendererMixin
│       ├── client/          — FontMixin, GuiMixin, KeyMappingMixin, ModelBuilderMixin
│       ├── geckolib/        — AnimationControllerMixin, AnimationProcessorMixin
│       └── world/entity/    — EntityMixin, PlayerMixin, LivingEntityMixin, DamageSourceMixin
│
└── kotlin/architecture/goldenboughs_lib/
    ├── core/                — Lib.kt (@Mod), LibClient.kt, LibConstants.kt
    │   └── registry/        — 注册表：Capability/Payload/Command/EntityAttribute
    ├── api/                 — 公开 API
    │   ├── capability/      — 实体/物品/方块能力接口 (IEntityLcLevel, IItemLcDamageType 等)
    │   ├── virtue/          — 四美德系统 (Fortitude/Prudence/Temperance/Justice)
    │   ├── payload/         — 网络包基类 (ToClientPayload, ToServerPayload)
    │   └── world/entity/ai/behavior/ — 行为树系统 (BTNode, 组合/条件/装饰/叶子节点)
    ├── common/              — 物品/实体/属性/状态效果/网络包
    ├── client/              — 渲染器/HUD层/粒子/模型
    ├── events/              — NeoForge 事件订阅
    ├── event/               — 自定义事件 (RationalityEvent, ChopFlavorLayerEvent)
    ├── init/                — 注册表声明 (Items, EntityTypes, MobEffects, SoundEvents 等)
    ├── config/              — 配置文件 (LibConfig, LibClientConfig)
    ├── datagen/             — 数据生成 (物品模型/粒子/标签/i18n)
    ├── mixed/               — 接口注入目标 (IDamageContainer, IEntityLib, IPlayerLib 等)
    ├── linkage/jade/        — Jade 插件
    └── util/                — 工具类 (~20: LcLevelUtil, RationalityUtil, GunWeaponUtil 等)
```

## 核心系统

| 系统               | 包                               | 说明                                         |
|------------------|---------------------------------|--------------------------------------------|
| **LcLevel**      | `api/LcLevel.kt`                | ZAYIN/TETH/HE/WAW/ALEPH 风险等级               |
| **LcDamageType** | `api/LcDamageType.kt`           | 四色伤害：PHYSICS/SPIRIT/EROSION/THE_SOUL       |
| **行为树**          | `api/world/entity/ai/behavior/` | 完整 BT 实现：组合/条件/装饰/叶子节点 + Blackboard        |
| **美德系统**         | `api/virtue/`                   | Fortitude/Prudence/Temperance/Justice 属性系统 |
| **HUD层**         | `client/gui/hudlayers/`         | 生命/理智/护盾条 + 斩击特效 + 画面滤镜                    |
| **理智系统**         | `event/rationality/`            | RationalityEvent 自定义事件 + 恢复机制              |
| **护盾系统**         | `common/mobeffect/`             | 四种伤害类型吸收护盾                                 |
| **物品接口**         | `api/world/item/`               | IEgoItem/IEgoWeaponItem/IGunWeapon 等接口层次   |

## 关键文件

- `core/Lib.kt` — `@Mod("goldenboughs_lib")` 入口，注册所有 DeferredRegister
- `core/LibClient.kt` — 客户端入口
- `api/AllOpe.kt` — `@AllOpe` 注解（配合 allOpen 插件使类可继承）
- `api/LcLevel.kt` — 风险等级枚举
- `api/LcDamageType.kt` — 伤害类型枚举
- `util/PayloadUtil.kt` — 网络包工具
- `util/LcLevelUtil.kt` — 风险等级工具

## 依赖

- 无上游模块依赖（基础库）
- 构建依赖：MixinSquared (runtimeOnly), Jade, JEI (runtimeOnly)
