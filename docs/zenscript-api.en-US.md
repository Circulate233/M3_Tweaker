# ZenScript API

[简体中文](zenscript-api.zh-CN.md) | [Java API (简体中文)](api.zh-CN.md) | [Java API (English)](api.en-US.md)

All classes use the `mods.m3t` namespace; bauble event classes are under `mods.m3t.event`. The extensions are registered during mod initialization. Recipe, shop, price, loot, and set changes are applied when CraftTweaker reloads scripts.

## Core Types

- `IItemStack` uses MineTweaker/CraftTweaker item-stack literals, e.g. `<minecraft:iron_ingot>`.
- `IIngredient` accepts an item stack or ore-dictionary entry, e.g. `<ore:ingotIron>`.
- `IItemStack[]` / `IIngredient[]` use ZenScript arrays, e.g. `[<minecraft:iron_ingot>, <minecraft:gold_ingot>]`.
- Attribute effects use two parallel arrays: `typeID[i]` corresponds to `data[i]`. IDs must be valid numeric IDs from ManaMetal's `MagicItemType`, and `data` must contain at least as many entries as `typeID`.

## Recipes

Return types are omitted from the table; all listed methods return `void`. Unless noted otherwise, `removeRecipe` finds and removes a recipe by its output.

| ZenScript class | Methods |
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

Slot requirements: Eternal Anvil takes 9 ingredients; Mana and Metal Craft Tables use 9 slots and accept `null` for empty slots; Mana Gravity Well takes 12 input slots; Mana Metal Injection uses 8 `inputs` slots plus the separate `mainInput`; Wand Making takes 5 input slots. Furnace recipes default to `0` experience when `exp` is omitted. Eternal Anvil's optional `nameKey` sets the recipe localization key.

Examples:

```zenscript
mods.m3t.ManaFurnace.addRecipe(<ore:ingotIron>, <minecraft:iron_ingot>, 0.5f);
mods.m3t.ManaCraftTable.addRecipe(<m3t:example>, [
    <ore:ingotIron>, null, <ore:ingotIron>,
    null, <minecraft:diamond>, null,
    <ore:ingotIron>, null, <ore:ingotIron>
]);
```

## Shops, Prices, and Loot

| ZenScript class | Methods and behavior |
| --- | --- |
| `mods.m3t.Money` | `setMoney(IItemStack item, int money)` sets an item's sell value. On script reload, the value table is rebuilt and retains only entries declared by scripts; declare every price that should remain. |
| `mods.m3t.ProduceStore` | `addStoreItem(String type, IItemStack item, int money)`, `addStoreItems(String type, IItemStack[] items, int[] money)`, `removeStoreItem(String type, IItemStack item)`, `removeStoreItems(String type, IItemStack[] items)`. `type` is case-insensitive: `MINING`, `FARMER`, `FISHING`, `BEEKEEPING`, `DRAGON`, `GEMCRAFT`, `CASTING`, `COOKING`, `TAILOR`, or `BREWING`. Bulk item and price arrays must have matching lengths. |
| `mods.m3t.NPCStore` | `addItem(int typeID, IItemStack item, int money)`, `addItems(int typeID, IItemStack[] items, int[] money)`, `removeItem(int typeID, IItemStack item)`, `removeItems(int typeID, IItemStack[] items)`. `typeID` is the ordinal of ManaMetal's NPC store type enum. |
| `mods.m3t.BossStore` | `addItem(IItemStack item, int money, boolean isLv2)`, `addItems(IItemStack[] items, int[] money, boolean isLv2)`, `removeItem(IItemStack item, boolean isLv2)`, `removeItems(IItemStack[] items, boolean isLv2)`. `isLv2=true` affects only the level-2 boss store; `false` affects both level 1 and level 2. |
| `mods.m3t.TrophyItem` | `addItem(IItemStack item, int weight, int min, int max, int level)`, `removaItem(IItemStack item, int level)` edit trophy loot tiers 1-3. Requires `weight >= 0`, `min >= 0`, and `min <= max`. |
| `mods.m3t.DungeonBox` | `addItem(IItemStack item, int weight, int min, int max, int ordinal)` or `addItem(IItemStack item, int weight, int min, int max, String name)`; removal methods are spelled `removaItem(IItemStack item, int ordinal)` and `removaItem(IItemStack item, String name)` in source. Names: `WOOD`, `WEAPON`, `QUARTZ`, `HEAVY`, `STURDY`, `PRECIOUST1`, `PRECIOUST2`, `PRECIOUST3`. |

`removaItem` is the actual method name in the current API (a legacy spelling), so scripts must use it. Prefer the dungeon-box name overload to avoid ordinal changes between versions.

## Food Attributes

`mods.m3t.AttributeItem`:

- `addFoodEffect(IItemStack item, int[] typeID, float[] data, short maxUse)` registers food attribute effects and its maximum use count.
- `addHotPotItemEffect(IItemStack item, int typeID, float data)` registers one effect for a hot-pot ingredient.

## Bauble Sets

Create and query sets with `mods.m3t.BaublesSuit`:

- `createSuit(IItemStack item)` / `createSuit(String name)`: define a set keyed by a bauble registry name.
- `createTagSuit(String name)`: define a set keyed by a bauble NBT set marker.
- `createScatteredSuit(String name)`: define a set from specific items and metadata values.
- `hasSuit(String name)`: check whether a set is registered.
- `getSuitQuantity(String suitName, IPlayer player)`: read the player's current set count.

Builder methods:

- Standard set: `addSuit(int quantity, String tooltip, int[] typeID, float[] data)`, then `register()`.
- NBT-tag set: also provides `setNameTag(String name)`.
- Scattered set: also provides `addItem(IItemStack item)` and `addItems(IItemStack[] items)`.

`quantity` is the number of pieces required for that effect tier. Example:

```zenscript
mods.m3t.BaublesSuit.createSuit("m3t:example_bauble")
    .addSuit(2, "Two-piece bonus", [10, 7], [5.0f, 10.0f])
    .register();
```

## Bauble Events

Register callbacks through `mods.m3t.Events`:

- `onBaubleWearEvent(function(event))`
- `onBaubleDisrobeEvent(function(event))`
- `onBaubleWearPostEvent(function(event))`
- `onBaubleDisrobePostEvent(function(event))`

Events expose `event.player` and `event.item`. Pre-wear and pre-disrobe events also provide `event.cancel()` and `event.isCancel()`; canceling a pre-event can veto the supported equipment flow. Post events run after the operation and cannot be canceled. Events are dispatched on the logical server only.

```zenscript
mods.m3t.Events.onBaubleWearEvent(function(event) {
    if (event.item == <m3t:forbidden_bauble>) {
        event.cancel();
    }
});
```

## Player, World, and Utilities

| Class/extension | API |
| --- | --- |
| `IPlayer` expansion | `player.getBauble(int slot)` reads the specified bauble slot; `player.world` returns the world; `player.pos` returns the player's position. |
| `IDimension` expansion | `world.dim` returns the dimension ID; `world.remote` indicates whether the world is client-side. |
| `mods.m3t.Util` | `getModItem(String modId)` returns item stacks whose registry names start with `modId`; `I18n(String key)` resolves localized text; `createString(Object... values)` concatenates values directly; `isServer()` reports whether the effective logical side is the server. |
| `mods.m3t.Random` | Create a random object with `getRandom()` or `getRandom(long seed)`. Instance methods: `nextInt()`, `nextInt(int bound)`, `nextFloat()`, `nextFloat(float max)`, `nextDouble()`, `nextDouble(double max)`, `nextLong()`, and `nextLong(long bound)`. An unseeded instance uses ManaMetal's shared random source; bounded integer, double, and long variants require positive bounds. |
