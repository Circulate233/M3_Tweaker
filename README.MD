# M3 Tweaker

1. [简体中文](#简介)
2. [English](#overview)

## 简介

M3 Tweaker 是 [魔法金属（ManaMetalMod）](https://www.curseforge.com/minecraft/mc-mods/manametal) 的 Minecraft 1.7.10 附属模组，为 CraftTweaker（MineTweaker3）添加 ZenScript 扩展。

通过脚本可以调整魔法金属的多类内容：
- 添加或移除多种机器与工作台的配方。
- 修改生产商店、NPC 商店、Boss 商店和地牢/战利品奖池。
- 为物品设置售价、食物与火锅属性效果。
- 定义饰品套装效果，并通过 `config/M3T/M3TBaubles.json` 注册自定义饰品及其元数据变体。
- 读取玩家饰品、注册饰品穿戴/卸下事件，并使用额外的随机数与物品工具方法。

运行需要 Minecraft Forge 1.7.10、ManaMetalMod 8.0.2 或更高版本、MineTweaker3 和 FastUtil 8.5.18。FastUtil 由 [GTNH External Lib](https://www.curseforge.com/minecraft/mc-mods/gtnh-external-lib) 提供。

### 文档

- [Java API（简体中文）](docs/api.zh-CN.md) | [English](docs/api.en-US.md)
- [ZenScript API（简体中文）](docs/zenscript-api.zh-CN.md) | [English](docs/zenscript-api.en-US.md)

---

## Overview

M3 Tweaker is a Minecraft 1.7.10 addon for [ManaMetalMod](https://www.curseforge.com/minecraft/mc-mods/manametal), adding ZenScript extensions for CraftTweaker (MineTweaker3).

Scripts can customize several parts of ManaMetal:
- Add or remove recipes for machines, crafting tables, and other workstations.
- Modify production, NPC, and boss shops, as well as dungeon and trophy loot pools.
- Set item prices and define attribute effects for foods and hot-pot ingredients.
- Define bauble set bonuses and register custom baubles with metadata variants through `config/M3T/M3TBaubles.json`.
- Read a player's baubles, subscribe to bauble wear/disrobe events, and use additional random and item utility methods.

Requires Minecraft Forge 1.7.10, ManaMetalMod 8.0.2 or later, MineTweaker3, and FastUtil 8.5.18. FastUtil is provided by [GTNH External Lib](https://www.curseforge.com/minecraft/mc-mods/gtnh-external-lib).

### Documentation

- [Java API (简体中文)](docs/api.zh-CN.md) | [English](docs/api.en-US.md)
- [ZenScript API (简体中文)](docs/zenscript-api.zh-CN.md) | [English](docs/zenscript-api.en-US.md)
