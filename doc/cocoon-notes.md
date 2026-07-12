# Reworking the Cocoon of Caprice mob selection system in 1.21.1

The number of animals in Minecraft seemingly is increasing with about every other update. Considering the cocoon only has four categories and two hard-coded special cases for mob selection, selecting even for a specific subgroup of animals doesn't quite narrow it down anymore.

## Goals

The main change I would like to see is having more control over the categories of mobs you could expect to spawn from a cocoon. They should still be animals (or otherwise passive mobs), not random monsters (except shulkers, which do serve a reasonable purpose), but getting villagers in skyblock without having to cure a zombie villager should also stay an option.

- Animals should not be explicitly grouped into categories. Each mob should individually have defined criteria for improving (or reducing) the chance it is selected.
- It should be possible to somehow exclude certain mobs from being selected, or even boosted, based on the biome. Imagine if both snow golems and hoglins were cocoon options – you wouldn't want snow golems in the nether or hoglins outside the nether, as both would have rather low survival chances in those respective scenarios.
- Certain mobs should be possible everywhere, but need some sort of "unlocking".
- Certain mobs (a lot of them) should always be available as a default selection choice, but somehow it should be possible to guarantee them not spawning. Something like "herbivore foods suppress carnivore mobs" (and vice versa).
- When giving certain special food items, e.g. to select for a shulker, it needs to somehow reduce the chance (and eventually suppress) all other mobs. Ideally that effect should not need to be defined in every mob to suppress.

## Specific ideas

- Each mob is assigned a base weight, which includes a biome modifier and whether the cocoon is waterlogged.
- "Mob type" might include biome/climate-specific variants, such as snow foxes.
- "Modifier items" add to or subtract from the base weight of mob types in some way. These items should likely be defined in the form of item tags. 
- "Unlock items" could be a special type of modifier items that make the selection of a particular type of mob possible in the first place. For example, a single emerald could add villagers to the selection pool with a certain base weight, but isn't actually a modifier item that boosts their selection weight.
- A single gaia spirit "boosts" the effect of all modifier items given to a cocoon, probably both before and after the gaia spirit.

## Mobs

### Biome bias

| Mob            | Beneficial Biomes                           | Detrimental Biomes          | Unlock Biomes      | Lock Biomes |
|----------------|---------------------------------------------|-----------------------------|--------------------|-------------|
| Shulker        | The End                                     |                             | The End            |             |
| Villager       | biome matching the villager variant         |                             |                    |             |
| Allay          | anything "magical"                          |                             | anything "magical" |             |
| Pig            | biomes with natural pig spawns              |                             |                    |             |
| Cow            | biomes with natural cow spawns              |                             |                    |             |
| Chicken        | biomes with natural chicken spawns          |                             |                    |             |
| Rabbit         | biomes with matching the rabbit type spawns |                             |                    |             |
| Sheep          | biomes with natural sheep spawns            |                             |                    |             |
| Horse          | biomes with natural horse spawns            |                             |                    |             |
| Donkey         | biomes with natural donkey spawns           |                             |                    |             |
| Wolf           | biomes with natural wolf spawns             |                             |                    |             |
| Ocelot         | jungle variants                             |                             |                    |             |
| Cat            | biomes with village variants + swamps       |                             |                    |             |
| Parrot         | jungle variants                             |                             |                    |             |
| Llama          | biomes with natural llama spawns            |                             |                    |             |
| Red Fox        | cooler, but non-snowy forests               |                             |                    |             |
| Snow Fox       | snowy forests                               |                             |                    |             |
| Panda          | (bamboo?) jungle                            |                             |                    |             |
| Turtle         | beach, (non-frozen) oceans                  |                             |                    |             |
| Goat           | mountainous biomes                          |                             |                    |             |
| Camel          | deserts                                     |                             |                    |             |
| Armadillo      | savannas                                    |                             |                    |             |
| Cod            | temperate biomes                            | frozen biomes               |                    |             |
| Salmon         | cold/snowy/frozen biomes, rivers            | warm/hot biomes             |                    |             |
| Tropical Fish  | warm/hot biomes, lush caves                 | cold/frozen biomes          |                    |             |
| Pufferfish     | warm/hot biomes                             | cold/frozen biomes          |                    |             |
| Squid          | oceans, rivers                              |                             |                    |             |
| Tadpole (Frog) | swamp variants                              |                             |                    |             |
| Dolphin        | oceans                                      | frozen/snowy biomes, rivers |                    |             |
| Glow Squid     | underground biomes                          |                             |                    |             |
| Axolotl        | lush caves                                  | frozen/snowy biomes         |                    |             |
| Strider        |                                             |                             | The Nether         |             |
| Hoglin         | Crimson Forest                              |                             | The Nether         | non-nether  |

"Beneficial" biomes increase the base weight of the mob.
"Detrimental" biomes reduce the base weight of the mob, without preventing the mob from being picked.
"Unlock" biomes make the mob available without a potential unlock item.
"Lock" biomes prevent unlock items for that mob from working at all.

TODO: Should Hoglins be spawnable in other dimensions and automatically be zombification-safe?

### Terrestrial mobs

| Mob            | Major booster items              | Minor booster item              | Unlock item    | Major suppression items                    | Minor suppression items           |
|----------------|----------------------------------|---------------------------------|----------------|--------------------------------------------|-----------------------------------|
| Shulker        | chorus fruit                     |                                 | chorus fruit   | everything else                            |                                   |
| Villager       | bread                            | carrot, potato, beetroot        | emerald        | inedibles, harmful foods, seeds, flowers   | animal-based food                 |
| Allay          | amethyst shard                   |                                 | amethyst shard | everything else                            |                                   |
| Pig            | carrot, potato, beetroot         |                                 |                | inedibles, harmful food                    | meat?, fish, seeds, flowers, hay  |
| Cow            | wheat                            |                                 |                | inedibles, harmful food, animal-based food | seeds, vegetables                 |
| Chicken        | seeds                            |                                 |                | inedibles, harmful food, animal-based food | flowers, vegetables, hay          |
| Rabbit         | carrot, golden carrot, dandelion |                                 |                | inedibles, harmful food, animal-based food | other flowers, other vegetables   |
| Sheep          | wheat                            | short grass, fern               |                | inedibles, harmful food, animal-based food | seeds, vegetables                 |
| Horse          | golden apple                     | carrot, golden carrot, apple    |                | inedibles, harmful food, animal-based food | seeds, vegetables                 |
| Donkey         | golden apple                     | carrot, golden carrot, apple    |                | inedibles, harmful food, animal-based food | seeds, vegetables                 |
| Wolf           | bone                             | meats                           |                | inedibles, harmful food, plant-based foods |                                   |
| Ocelot         | raw cod, raw salmon              | raw chicken                     |                | inedibles, harmful food, plant-based foods |                                   |
| Cat            | raw cod, raw salmon              | raw chicken                     |                | inedibles, harmful food, plant-based foods |                                   |
| Parrot         | seeds                            |                                 |                | inedibles, harmful food, animal-based food | flowers, vegetables, hay          |
| Llama          | Hay bale                         | wheat                           |                | inedibles, harmful food, animal-based food | flowers, vegetables               |
| Red Fox        | berries                          | raw chicken, raw rabbit         |                | inedibles, harmful food, flowers, seeds    |                                   |
| Snow Fox       | berries                          | (harmless) raw fish, raw rabbit |                | inedible food, flowers, seeds              |                                   |
| Panda          | bamboo                           | cake                            |                | inedibles, harmful food, animal-based food | flowers, seeds, hay               |
| Turtle         | sea grass                        |                                 |                | inedibles, harmful food, animal-based food | plant-based food                  |
| Goat           | wheat                            |                                 |                | inedibles, harmful food, animal-based food | seeds, vegetables                 |
| Camel          | cactus                           |                                 |                | inedibles, harmful food, animal-based food | seeds, vegetables                 |
| Armadillo      | spider eye                       |                                 |                | inedibles, other harmful food              | plant-based food                  |
| Cod            |                                  |                                 |                | inedibles, harmful food, plant-based food  |                                   |
| Salmon         |                                  |                                 |                | inedibles, harmful food, plant-based food  |                                   |
| Tropical Fish  |                                  |                                 |                | inedibles, harmful food                    |                                   |
| Pufferfish     |                                  |                                 |                | inedibles, harmful food                    | plant-based food                  |
| Squid          |                                  |                                 |                | inedibles, harmful food, plant-based food  |                                   |
| Tadpole (Frog) | slimeball                        |                                 |                | inedibles, harmful food, plant-based food  |                                   |
| Dolphin        | fish                             |                                 |                | inedibles, harmful food, plant-based food  |                                   |
| Glow Squid     |                                  |                                 |                | inedibles, harmful food, plant-based food  |                                   |
| Axolotl        | bucket of tropical fish          | tropical fish                   |                | inedibles, harmful food, plant-based food  |                                   |
| Strider        | warped fungus                    |                                 |                | inedibles, harmful food, animal-based food | crimson fungus                    |
| Hoglin         | crimson fungus                   |                                 |                | inedibles, harmful food, warped fungus     | animal-based food, seeds, flowers |

"Harmful" is anything that can have a non-beneficial side effect. For example rotten flesh and raw chicken a harmful meats, and chorus fruit is a harmful vegetable.
Feeding container items leaves behind the container (e.g. bucket of tropical fish leaves behind a water bucket)

TODO: How to distinguish ocelots and cats? How to distinguish horses and donkeys? How to distinguish chicken and parrots?
