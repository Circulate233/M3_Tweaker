# Java API

[English](api.en-US.md) | [ZenScript API（简体中文）](zenscript-api.zh-CN.md) | [ZenScript API (English)](zenscript-api.en-US.md)

本文记录 M3 Tweaker 当前源码提供的 Java 集成点，以及自定义饰品 JSON 配置格式。模组没有单独声明稳定的 `api` 包；除下文接口外，其余实现类可能随版本变化。

## 版本与依赖

- Minecraft 1.7.10 / Forge 10.13.4.1614
- ManaMetalMod 8.0.2 或更高版本
- MineTweaker3（CraftTweaker 1.7.10）
- FastUtil 8.5.18（由 [GTNH External Lib](https://www.curseforge.com/minecraft/mc-mods/gtnh-external-lib) 提供）

## 饰品装备事件

事件位于 `com.circulation.m3t.event.BaubleEquipmentEvent`，由 `MinecraftForge.EVENT_BUS` 分发，且只在逻辑服务端触发。所有事件提供 `player`（`EntityPlayer`）与 `item`（`ItemStack`）字段。

| 事件类型 | 时机 | 可取消 |
| --- | --- | --- |
| `BaubleEquipmentEvent.Wear` | 饰品穿戴检查期间 | 是；取消会阻止受支持路径中的穿戴 |
| `BaubleEquipmentEvent.Disrobe` | 饰品卸下检查期间 | 是；取消会阻止受支持路径中的卸下/替换 |
| `BaubleEquipmentEvent.WearPost` | 穿戴完成后 | 否 |
| `BaubleEquipmentEvent.DisrobePost` | 卸下完成后 | 否 |

Java 监听示例：

```java
@SubscribeEvent
public void onBaubleWear(BaubleEquipmentEvent.Wear event) {
    if (shouldBlock(event.player, event.item)) {
        event.setCanceled(true);
    }
}

@SubscribeEvent
public void onBaubleWearPost(BaubleEquipmentEvent.WearPost event) {
    onEquipped(event.player, event.item);
}
```

`Wear` 与 `Disrobe` 继承 Forge 的可取消事件；`WearPost` 与 `DisrobePost` 是操作完成后的通知，不会再次改变操作结果。事件仅在玩家、世界和物品栈有效时发送。具体拦截点由 ManaMetal 的装备流程决定。

## 饰品库存所有者

`com.circulation.m3t.event.BaubleInventoryOwner` 是由 Mixin 注入到 ManaMetal `NbtBaubles` 对象的接口：

```java
EntityPlayer owner = ((BaubleInventoryOwner) baubleInventory).m3t$getOwner();
```

`m3t$getOwner()` 返回该饰品库存所属的玩家；对象没有绑定玩家时返回 `null`。只对已应用 M3 Tweaker Mixins 的 `NbtBaubles` 实例进行转换。

## 自定义饰品配置

配置文件：`config/M3T/M3TBaubles.json`。文件在游戏启动时读取；修改后重启游戏。顶层必须是 JSON 数组：

```json
[
  {
    "id": "example_bauble",
    "name": "item.example_bauble.name",
    "tooltip": "item.example_bauble.tooltip",
    "icon": "minecraft:diamond:0",
    "type": 10,
    "quality": 1,
    "level": 10,
    "money": 1000,
    "effects": {
      "10": 50.0,
      "7": 25.0
    }
  }
]
```

| 字段 | 含义 |
| --- | --- |
| `id` | 可选的物品注册名。省略时由 `type` 对应的 ManaMetal 饰品类型名生成。相同 `id` 的条目会成为同一物品的不同元数据变体，顺序即元数据值。 |
| `name` | 饰品名称文本或本地化键。 |
| `tooltip` | 饰品提示文本或本地化键。 |
| `icon` | 物品 ID（可带元数据，如 `minecraft:diamond:0`），或 `def:` 前缀加纹理资源名，如 `def:m3t:amuck`。 |
| `type` | ManaMetal `ManaItemType` 的数字 ID。 |
| `quality` | 饰品品质序号，按从 1 开始的值读取。 |
| `level` | 需求等级；注册时至少为 1。 |
| `money` | 饰品售价。 |
| `effects` | 属性效果映射：键为 ManaMetal `MagicItemType` 数字 ID，值为效果数值。 |

首次没有配置文件时，模组会创建一个带有示例条目的文件。若 JSON 解析失败或不是数组，相关错误会写入日志，且本次不会注册配置文件中的饰品。

## 其他配置

`config/M3T/M3T.cfg`：

| 选项 | 默认值 | 说明 |
| --- | --- | --- |
| `randomTag` | `false` | 是否为每件自定义饰品添加随机 NBT 标签。 |
| `showAllSuitEffects` | `false` | 是否默认显示全部饰品套装效果。 |
