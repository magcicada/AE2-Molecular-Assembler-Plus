# AE2 Molecular Assembler Plus

AE2 Molecular Assembler Plus is a NeoForge addon for Minecraft 1.21.1 that extends Applied Energistics 2 auto-crafting with universal pattern encoding and dedicated assemblers for modded crafting workflows.

The mod is not limited to 9x9 recipes. It can support different recipe shapes, station layouts, tiers, catalysts, energy costs, and other requirements from supported mods.

## Features

- Extreme Pattern Encoding Terminal with a 9x9 crafting grid.
- Draconic Pattern Encoding Terminal for Draconic Evolution fusion crafting patterns.
- Dedicated blank and encoded pattern items for supported recipe families.
- Molecular assembler variants for supported recipe systems and tiered workflows.
- JEI recipe transfer support: the JEI `+` button can move supported recipes into the matching terminal.
- Optional recipe integration for supported crafting-table mods.
- Conditional crafting recipes that appear only when the matching optional mod is installed.

## Supported Mods

AE2 is the base mod. Everything else in this table is optional: AE2 Molecular Assembler Plus can load without it.

| Mod | Version | Pattern Terminal | JEI `+` Transfer | Conditional Recipes | Links |
| --- | --- | --- | --- | --- | --- |
| Applied Energistics 2 | 1.21.1, AE2 19.2.17+ | Core mod | Vanilla crafting recipes | Base AE2 recipes | [CurseForge](https://www.curseforge.com/minecraft/mc-mods/applied-energistics-2), [Modrinth](https://modrinth.com/mod/ae2) |
| Extended Crafting | 1.21.1 | Yes | Yes | Yes | [CurseForge](https://www.curseforge.com/minecraft/mc-mods/extended-crafting), [Modrinth](https://modrinth.com/mod/extended-crafting) |
| Extended Crafting: Expanded | 1.21.1 | Partial | Via Extended Crafting categories | Via Extended Crafting ids | [CurseForge](https://www.curseforge.com/minecraft/mc-mods/extended-crafting-expanded) |
| Re:Avaritia | 1.21.1 | Yes | Yes | Yes | [CurseForge](https://www.curseforge.com/minecraft/mc-mods/re-avaritia), [Modrinth](https://modrinth.com/mod/re-avaritia) |
| AvaritiaNeo | 1.21.1 | Yes | Yes | Yes | [CurseForge](https://www.curseforge.com/minecraft/mc-mods/avaritianeo) |
| Extended Terminal | 1.21.1 | No | No | No | [CurseForge](https://www.curseforge.com/minecraft/mc-mods/extended-terminal), [Modrinth](https://modrinth.com/mod/extended-terminal) |

## Other Mods

These integrations are for non-standard crafting systems that are not simple crafting grids.

| Mod | Supported Workflows | Pattern Terminal | Assembler Support | JEI `+` Transfer | Notes |
| --- | --- | --- | --- | --- | --- |
| Draconic Evolution | Fusion crafting | Draconic Pattern Encoding Terminal | Wyvern, Draconic, and Chaotic Molecular Assemblers | Yes | Encoded patterns store fusion tier and energy cost. Assemblers enforce tier limits and consume fusion energy. |

Notes:

- Extended Crafting: Expanded uses the same mod id as Extended Crafting, so support is handled through the Extended Crafting integration path.
- Re:Avaritia and AvaritiaNeo both use Avaritia-style ids. Recipes use common item ids so they can work with either fork when possible.
- Extended Terminal is listed because it targets similar terminal workflows, but AE2 Molecular Assembler Plus does not depend on it.

## JEI Integration

When JEI is installed, supported recipe categories show a `+` transfer button. Clicking it sends the recipe layout into the matching pattern terminal.

Supported JEI transfer targets include:

- Minecraft crafting recipes
- Extended Crafting table categories
- Re:Avaritia table categories
- AvaritiaNeo extreme crafting
- Draconic Evolution fusion crafting

## Conditional Recipes

Base AE2 recipes are always available for:

- `ccapplied:extreme_blank_pattern`
- `ccapplied:extreme_pattern_terminal`
- `ccapplied:extreme_molecular_assembler`

Extra recipes are loaded only when the matching mod is present:

- `extendedcrafting` recipes use Extended Crafting components and tables.
- `avaritia` recipes use Avaritia/Re:Avaritia/AvaritiaNeo items that share common ids.
- `draconicevolution` recipes add Draconic Fusion patterns, the Draconic Pattern Encoding Terminal, and tiered Draconic Molecular Assemblers.

Encoded crafting patterns are not craftable directly. They must be created through the matching Pattern Encoding Terminal so they contain recipe data.

## Roadmap

See [ROADMAP.md](ROADMAP.md).
