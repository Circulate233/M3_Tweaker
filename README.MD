# M3 Tweaker

M3 Tweaker 为 Minecraft 1.7.10 的魔法金属提供 CraftTweaker 扩展。脚本可以修改配方、生产商店、NPC 商店、Boss 商店和奖池，也可以定义饰品套装、属性食物及火锅材料。自定义饰品通过 `config/M3T/M3TBaubles.json` 配置，同一物品 ID 的变体通过元数据区分。

## 饰品事件

Java 模组可在 `MinecraftForge.EVENT_BUS` 上通过 `@SubscribeEvent` 监听 `BaubleEquipmentEvent.Wear`、`Disrobe`、`WearPost` 和 `DisrobePost`。事件由逻辑服务端发布。装备前和卸下前可调用 `setCanceled(true)` 取消操作。后置事件在魔法金属处理对应的饰品效果后发布；部分调用路径会在事件发布后写入槽位。

Java 模组可在客户端订阅 `ItemTooltipEvent` 并向 `toolTip` 添加文字，为自定义饰品补充提示。

ZenScript 使用 `mods.m3t.Events.onBaubleWearEvent`、`onBaubleDisrobeEvent`、`onBaubleWearPostEvent` 和 `onBaubleDisrobePostEvent` 注册回调。`player`、`item`、`cancel()` 和 `isCancel()` 用法不变，脚本和 Forge 监听器读取同一取消状态。重新加载脚本会清除旧回调。

背包完整换装时，只要有槽位拒绝卸下，背包和饰品栏的物品都不会被替换。部分换装只跳过拒绝操作的槽位，其余槽位正常处理。

## 脚本重载

配方、固定商店和奖池按物品及元数据移除默认条目，比较时忽略堆叠数量和 NBT。保留的默认条目顺序不变，脚本新增条目按注册顺序追加。NPC 商店每次仍按魔法金属的规则生成商品，然后应用脚本改动。DungeonBox 和 TrophyItem 的移除方法仍名为 `removaItem`。

在 Windows 上运行 `gradlew.bat build` 可执行测试、格式检查并构建 JAR。
