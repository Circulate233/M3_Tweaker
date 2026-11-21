# ZenScript API

[English](zenscript-api.en-US.md) | [Java API（简体中文）](api.zh-CN.md) | [Java API (English)](api.en-US.md)

所有类都以 `mods.m3t` 为命名空间；饰品事件类位于 `mods.m3t.event`。这些扩展在模组初始化时注册，配方、商店、价格、掉落和套装改动会在 CraftTweaker 脚本重载后应用。

## 基本类型

- `IItemStack` 使用 MineTweaker/CraftTweaker 的物品栈字面量，例如 `<minecraft:iron_ingot>`。
- `IIngredient` 可接受物品栈或矿辞条目，例如 `<ore:ingotIron>`。
- `IItemStack[]` / `IIngredient[]` 使用 ZenScript 数组，例如 `[<minecraft:iron_ingot>, <minecraft:gold_ingot>]`。
- 属性效果使用两组平行数组：`typeID[i]` 对应 `data[i]`。ID 必须是 ManaMetal 的 `MagicItemType` 有效数字 ID，且 `data` 至少与 `typeID` 等长。

## 配方

表中省略的返回类型均为 `void`。除特别说明外，`removeRecipe` 按配方产物查找并移除。

| ZenScript 类 | 方法 |
| --- | --- |
| `mods.m3t.AncientEmpire` | `addRecipe(IItemStack output, IItemStack[] inputs, int needXP)`; `removeRecipe(IItemStack output)` |
| `mods.m3t.BlueSky` | `addRecipe(IItemStack output, IItemStack[] inputs, int needXP)`; `removeRecipe(IItemStack output)` |
| `mods.m3t.DarkItem` | `addRecipe(IItemStack output, IItemStack[] inputs, int needXP)`; `removeRecipe(IItemStack output)` |
| `mods.m3t.OriginCrystalCore` | `addRecipe(IItemStack output, IItemStack[] ingredients, int needXP)`; `removeRecipe(IItemStack output)` |
| `mods.m3t.Casting` | `addRecipe(IItemStack output, int money, IItemStack[] ingredients)`; `removeRecipe(IItemStack output)` |
| `mods.m3t.DarkFission` | `addRecipe(IItemStack input, IItemStack[] outputs)`; `removeRecipe(IItemStack input)` |
| `mods.m3t.DarkSteel` | `addRecipe(IItemStack input, IItemStack output)`; `addRecipe(IItemStack input, IItemStack output, float exp)`; `removeRecipe(IItemStack output)` |
| `mods.m3t.IronWroughtFurnace` | `addRecipe(IItemStack input, IItemStack output)`; `addRecipe(IItemStack input, IItemStack output, float exp)`; `removeRecipe(IItemStack output)` |
| `mods.m3t.EternalAnvil` | `addRecipe(IItemStack output, IItemStack[] ingredients)`; `addRecipe(IItemStack output, IItemStack[] ingredients, String nameKey)`; `removeRecipe(IItemStack output)` |
| `mods.m3t.ManaCraftTable` | `addRecipe(IItemStack output, IIngredient[] ingredients)`; `removeRecipe(IItemStack output)` |
| `mods.m3t.MetalCraftTable` | `addRecipe(IItemStack output, IIngredient[] ingredients)`; `removeRecipe(IItemStack output)` |
| `mods.m3t.ManaFurnace` | `addRecipe(IIngredient input, IItemStack output)`; `addRecipe(IIngredient input, IItemStack output, float exp)`; `removeRecipe(IItemStack output)` |
| `mods.m3t.ManaGravityWell` | `addRecipe(IItemStack output, IItemStack[] inputs)`; `removeRecipe(IItemStack output)` |
| `mods.m3t.ManaMetalInjection` | `addRecipe(IItemStack output, IItemStack mainInput, IItemStack[] inputs)`; `removeRecipe(IItemStack output)` |
| `mods.m3t.WandMake` | `addRecipe(IItemStack output, IItemStack[] inputs)`; `removeRecipe(IItemStack output)` |
| `mods.m3t.MirrorPool` | `addRecipe(IItemStack template)`; `removeRecipe(IItemStack template)` |
| `mods.m3t.MysteriousEnergyGenerator` | `addRecipe(IItemStack input, int energy)`; `removeRecipe(IItemStack input)` |
| `mods.m3t.PrimalForge` | `addRecipe(IItemStack input, int points)`; `removeRecipe(IItemStack input)` |

数组槽位约束：永恒铁砧需要 9 个材料；魔力/金属工作台使用 9 个槽位，可将空槽设为 `null`；魔力重力井需要 12 个输入槽；魔力金属注入器使用 8 个 `inputs` 槽，另有一个单独的 `mainInput`；法杖制作台需要 5 个输入槽。炉类配方不传 `exp` 时经验值为 `0`。永恒铁砧的 `nameKey` 可用于指定配方名称本地化键。

示例：

```zenscript
mods.m3t.ManaFurnace.addRecipe(<ore:ingotIron>, <minecraft:iron_ingot>, 0.5f);
mods.m3t.ManaCraftTable.addRecipe(<m3t:example>, [
    <ore:ingotIron>, null, <ore:ingotIron>,
    null, <minecraft:diamond>, null,
    <ore:ingotIron>, null, <ore:ingotIron>
]);
```

## 商店、价格与掉落

| ZenScript 类 | 方法和行为 |
| --- | --- |
| `mods.m3t.Money` | `setMoney(IItemStack item, int money)` 设置物品售价。脚本重载时售价表会被重建，只保留脚本中声明的条目；需要保留的价格也必须在脚本中重新声明。 |
| `mods.m3t.ProduceStore` | `addStoreItem(String type, IItemStack item, int money)`、`addStoreItems(String type, IItemStack[] items, int[] money)`、`removeStoreItem(String type, IItemStack item)`、`removeStoreItems(String type, IItemStack[] items)`。`type` 不区分大小写，可用 `MINING`, `FARMER`, `FISHING`, `BEEKEEPING`, `DRAGON`, `GEMCRAFT`, `CASTING`, `COOKING`, `TAILOR`, `BREWING`。批量物品和价格数组长度必须匹配。 |
| `mods.m3t.NPCStore` | `addItem(int typeID, IItemStack item, int money)`、`addItems(int typeID, IItemStack[] items, int[] money)`、`removeItem(int typeID, IItemStack item)`、`removeItems(int typeID, IItemStack[] items)`。`typeID` 是 ManaMetal NPC 商店类型枚举的序号。 |
| `mods.m3t.BossStore` | `addItem(IItemStack item, int money, boolean isLv2)`、`addItems(IItemStack[] items, int[] money, boolean isLv2)`、`removeItem(IItemStack item, boolean isLv2)`、`removeItems(IItemStack[] items, boolean isLv2)`。`isLv2=true` 只操作二级 Boss 商店；`false` 同时操作一级和二级商店。 |
| `mods.m3t.TrophyItem` | `addItem(IItemStack item, int weight, int min, int max, int level)`、`removaItem(IItemStack item, int level)`，编辑 1–3 级奖杯掉落池。要求 `weight >= 0`、`min >= 0`、`min <= max`。 |
| `mods.m3t.DungeonBox` | `addItem(IItemStack item, int weight, int min, int max, int ordinal)`、`addItem(IItemStack item, int weight, int min, int max, String name)`；对应删除方法在源码中拼作 `removaItem(IItemStack item, int ordinal)` 和 `removaItem(IItemStack item, String name)`。箱子名：`WOOD`, `WEAPON`, `QUARTZ`, `HEAVY`, `STURDY`, `PRECIOUST1`, `PRECIOUST2`, `PRECIOUST3`。 |

`removaItem` 是当前接口的实际方法名（历史拼写），脚本必须使用该名称。地牢箱推荐使用名称重载，避免序号含义随版本变化。

## 食物属性

`mods.m3t.AttributeItem`：

- `addFoodEffect(IItemStack item, int[] typeID, float[] data, short maxUse)`：为食物登记属性效果和最大使用次数。
- `addHotPotItemEffect(IItemStack item, int typeID, float data)`：为火锅材料登记单项效果。

## 饰品套装

套装创建与查询使用 `mods.m3t.BaublesSuit`：

- `createSuit(IItemStack item)` / `createSuit(String name)`：按饰品注册名定义套装。
- `createTagSuit(String name)`：按饰品 NBT 中的套装标记定义套装。
- `createScatteredSuit(String name)`：定义由多个指定物品/元数据组成的套装。
- `hasSuit(String name)`：检查套装是否已注册。
- `getSuitQuantity(String suitName, IPlayer player)`：读取玩家当前套装件数。

创建器方法：

- 普通套装：`addSuit(int quantity, String tooltip, int[] typeID, float[] data)`、`register()`。
- NBT 标记套装：另有 `setNameTag(String name)`。
- 分散套装：另有 `addItem(IItemStack item)`、`addItems(IItemStack[] items)`。

`quantity` 为触发该等级效果所需的件数。示例：

```zenscript
mods.m3t.BaublesSuit.createSuit("m3t:example_bauble")
    .addSuit(2, "2 件套效果", [10, 7], [5.0f, 10.0f])
    .register();
```

## 饰品事件

注册方法位于 `mods.m3t.Events`：

- `onBaubleWearEvent(function(event))`
- `onBaubleDisrobeEvent(function(event))`
- `onBaubleWearPostEvent(function(event))`
- `onBaubleDisrobePostEvent(function(event))`

事件对象提供 `event.player` 与 `event.item`。穿戴/卸下前事件还提供 `event.cancel()` 和 `event.isCancel()`；取消前置事件可阻止受支持装备流程。Post 事件在操作完成后调用且不可取消。事件只在逻辑服务端分发。

```zenscript
mods.m3t.Events.onBaubleWearEvent(function(event) {
    if (event.item == <m3t:forbidden_bauble>) {
        event.cancel();
    }
});
```

## 玩家、世界与工具

| 类/扩展 | API |
| --- | --- |
| `IPlayer` 扩展 | `player.getBauble(int slot)` 读取指定饰品槽；`player.world` 返回世界；`player.pos` 返回玩家坐标。 |
| `IDimension` 扩展 | `world.dim` 返回维度 ID；`world.remote` 表示当前世界是否为客户端世界。 |
| `mods.m3t.Util` | `getModItem(String modId)` 返回注册名以 `modId` 开头的物品栈；`I18n(String key)` 获取本地化文本；`createString(Object... values)` 直接拼接参数；`isServer()` 查询当前有效逻辑侧是否为服务端。 |
| `mods.m3t.Random` | `getRandom()` 或 `getRandom(long seed)` 创建随机对象；实例方法：`nextInt()`、`nextInt(int bound)`、`nextFloat()`、`nextFloat(float max)`、`nextDouble()`、`nextDouble(double max)`、`nextLong()`、`nextLong(long bound)`。不带种子的实例使用 ManaMetal 的共享随机源；整数、double 与 long 的有界重载要求正数边界。 |
