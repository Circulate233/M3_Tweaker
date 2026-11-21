# Java API

[简体中文](api.zh-CN.md) | [ZenScript API (简体中文)](zenscript-api.zh-CN.md) | [ZenScript API (English)](zenscript-api.en-US.md)

This document covers the Java integration points currently present in M3 Tweaker and the custom-bauble JSON configuration. The mod does not declare a separate stable `api` package; implementation classes other than the interfaces below may change between releases.

## Runtime Requirements

- Minecraft 1.7.10 / Forge 10.13.4.1614
- ManaMetalMod 8.0.2 or later
- MineTweaker3 (CraftTweaker for 1.7.10)
- FastUtil 8.5.18 (provided by [GTNH External Lib](https://www.curseforge.com/minecraft/mc-mods/gtnh-external-lib))

## Bauble Equipment Events

Events are in `com.circulation.m3t.event.BaubleEquipmentEvent`. They are posted on `MinecraftForge.EVENT_BUS` on the logical server only. Each event exposes `player` (`EntityPlayer`) and `item` (`ItemStack`) fields.

| Event type | Timing | Cancelable |
| --- | --- | --- |
| `BaubleEquipmentEvent.Wear` | During a bauble wear check | Yes; cancellation blocks wearing on supported paths |
| `BaubleEquipmentEvent.Disrobe` | During a bauble removal check | Yes; cancellation blocks removal or replacement on supported paths |
| `BaubleEquipmentEvent.WearPost` | After wearing completes | No |
| `BaubleEquipmentEvent.DisrobePost` | After removal completes | No |

Java listener example:

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

`Wear` and `Disrobe` extend Forge's cancelable event type. The post events are notifications and cannot change the completed operation. Events are emitted only when the player, world, and item stack are valid. The exact interception points depend on ManaMetal's equipment flow.

## Bauble Inventory Owner

`com.circulation.m3t.event.BaubleInventoryOwner` is an interface mixed into ManaMetal's `NbtBaubles` object:

```java
EntityPlayer owner = ((BaubleInventoryOwner) baubleInventory).m3t$getOwner();
```

`m3t$getOwner()` returns the player that owns the bauble inventory, or `null` when no player is bound. Cast only `NbtBaubles` instances for which M3 Tweaker's mixins have been applied.

## Custom Bauble Configuration

Configuration file: `config/M3T/M3TBaubles.json`. It is read during game startup, so restart the game after editing it. The top-level value must be a JSON array:

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

| Field | Meaning |
| --- | --- |
| `id` | Optional item registry name. When omitted, the ManaMetal bauble type name for `type` is used. Entries sharing an `id` become metadata variants of one item; array order determines metadata. |
| `name` | Display text or a localization key. |
| `tooltip` | Tooltip text or a localization key. |
| `icon` | Item ID (optionally with metadata, e.g. `minecraft:diamond:0`), or a texture resource name prefixed by `def:`, e.g. `def:m3t:amuck`. |
| `type` | Numeric ID from ManaMetal's `ManaItemType`. |
| `quality` | Bauble quality ordinal, supplied as a one-based value. |
| `level` | Required level; registration enforces a minimum of 1. |
| `money` | Bauble sell value. |
| `effects` | Attribute map: keys are numeric IDs from ManaMetal's `MagicItemType`; values are effect amounts. |

If the file does not exist on first launch, the mod writes a sample file. If JSON parsing fails or the root is not an array, the error is logged and baubles from that file are not registered for that run.

## Other Configuration

`config/M3T/M3T.cfg`:

| Option | Default | Description |
| --- | --- | --- |
| `randomTag` | `false` | Add a random NBT tag to each custom bauble. |
| `showAllSuitEffects` | `false` | Show all bauble set effects by default. |
