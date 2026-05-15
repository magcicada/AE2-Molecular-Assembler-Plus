# Roadmap

AE2 Molecular Assembler Plus is intended to grow from 9x9 crafting support into a broader bridge between AE2 pattern automation and modded crafting stations.

## Planned Integrations

| Mod | Planned Support |
| --- | --- |
| Botania | Runic altar, mana-based workflows, and other station recipes where AE2 automation can map cleanly to the station inventory. |
| Blood Magic | Blood altar workflows and other Blood Magic crafting stations that need catalysts, tiers, or special inputs. |
| Draconic Evolution | Fusion crafting and other station recipes that need energy, catalysts, or multi-slot layouts. |
| Other crafting station mods | Additional large crafting tables and custom station recipes based on demand and technical fit. |

## Planned Systems

- A general crafting-station backend layer so integrations can be added without hard-coding every station into the terminal and assembler logic.
- Better JEI transfer coverage for non-grid recipes.
- Possible EMI and REI transfer support after the JEI path is stable.
- More assembler variants or adapters for recipes that need special inventories, fluids, energy, mana, blood, catalysts, or multi-block context.
- Safer optional-mod boundaries so each integration can load only when the matching mod is installed.
