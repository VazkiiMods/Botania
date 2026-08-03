# Future Version Notes
ToDo/wish list for future versions of Botania, and a list of vanilla features that may or may not deserve Botania interactions.

## Version-specific changes
Notable features in newer versions of Minecraft that might affect Botania in some way, or inspire changes.
Features in older versions are mentioned if there are issues or no real interactions with them in Botania.

### 1.13 ("Update Aquatic")
* Trident (does it need any interactions though?)
* Heart of the Sea
  * while technically obtainable in GoG via Loonium, Botania doesn't actually help with finding buried treasure locations at all
  * Ring of Chordata is locked behind this

### 1.14 ("Village & Pillage")
* Bamboo
  * bad obtainability in GoG:
    * via Loonium (jungle temple, shipwreck), i.e. quite late game and Botania doesn't help finding the structures
    * fishing in a jungle biome (which Botania doesn't help finding)
    * killing a panda (you monster!)
* Crossbow (does it need any interactions?)

### 1.15 ("Buzzy Bees")
* Bees and honey (comb)
  * (mandatory Beetunia mention)

### 1.16 ("Nether Update")
* Nylium
  * would currently not be obtainable in a void nether

### 1.17 ("Caves & Cliffs", part 1)
* Amethyst
  * currently unobtainable in GoG
  * (mandatory Amethystle mention)
* Moss (obtainable in GoG via Wandering Trader or Loonium in shipwrecks or trial chambers)
* Axolotl
  * would be nice if Botania helped with obtaining the blue variant, e.g. by somehow helping with bucketing fish

### 1.18 ("Caves & Cliffs", part 2)
* cave biomes (not sure if they need any interactions, specifically)

### 1.19 ("The Wild Update")
* Sculk block family
  * currently only obtainable in GoG via Loonium in Ancient City, which Botania doesn't really help finding
* Echo Shards
* Allays (currently unobtainable in GoG)
* Mud
  * vanilla source for clay, but the Clayconia doesn't interact with it at all yet
* Boats with Chest
* biomes can be "placed" via commands now (just a reminder that this is a thing mods could do as well, but do we really want to?)
* display/interaction entities (not that we need them, but maybe it's time to refresh the Red-Stringed Interceptor in some way?)

### 1.20 ("Trails & Tales")
* smithing templates/armor trim
* Sniffer (spawned by Cocoon of Caprice, but can we do anything fun with its digging behavior?)
* Camel (spawned via Cocoon of Caprice, not sure if it needs any specific interactions)
* Decorated Pot/pottery sherds
* establishes resonance feature of amethyst blocks via calibrated sculk sensor
* suspicious sand/gravel
* bamboo wood set (Botania doesn't help making it available, especially in GoG)

### 1.20.5 ("Armored Paws")
* Armadillos (spawned by Cocoon of Caprice, but the mob pool size is getting ridiculous)
* wolf armor (it's dyeable)
* wolf variants (Cocoon of Caprice happens to already respect the biome, but that's about it)
* item data components (DFU all the things!)

### 1.21 ("Tricky Trials")
* Trial Chambers
* Breeze/wind charges
* Bad Omen rework (Trial Omen/Raid Omen)
* new mob effects (Oozing, Weaving, Infested, Wind Charged)
* Mace
* a bunch of new paintings (I don't think Botania needs to participate here, but…)

----

### 1.21.2 ("Bundles of Bravery")
* vanilla-accessible bundles (including changed UI and usage pattern)
* baby variants for dolphins and (glow) squids
* updated redstone torch look (including comparators and repeaters – keep in mind for animated torch!)
* ender pearl chunk loading (but doesn't keep dimension ticking, so booray?)
* item rarity consolidation (we kind-of already did that in 1.21.1 Botania, but worth checking if everything still fits)
* Lots of data component changes, but here are a couple of noteworthy ones:
  * `minecraft:consumable` for things that can be consumed (maybe useful for the mana bottle?)
  * `minecraft:glider` makes the item work like elytra
  * `minecraft:equippable` allows specifying a screen overlay (equippable Fel Pumpkin?)
  * `minecraft:damage_resistant` replaces and generalizes `minecraft:fire_resistant`

### 1.21.4 ("The Garden Awakens")
* Pale Oak
* resin
* Creaking Heart/Creaking mob (would probably be indirectly obtainable in GoG via Loonium in Woodland Mansion automatically)
* Pale Moss, Hanging Moss (only obtainable in GoG via Wandering Trader)
* Eyeblossom flower (open and closes variants exist and convert into each other randomly when applicable, even in a flower pot)
* many crafted pickaxe blocks are no longer just destroyed when mined with an incorrect tool
* flowers can apply effects when used as bee food (open eyeblossom = poison, wither rose = wither)

### 1.21.5 ("Spring to Life")
* cold/warm animal variants, biome specific sheep color weights
* leaf litter, wildflowers (both work like pink petals – we discussed maybe changing buried mystical petals to work like this as well)
* bush, firefly bush
* cactus flower
* dry grass (small/tall)
* cartographer biome map trades (to villages, as well as swamp huts and jungle temples)
* Wandering Trader also buys stuff
* Test Blocks, Test Instance Block – good opportunity to revise and improve all kinds of in-game tests
  * Fabric's game test system was adapted to this
  * 
* random ticks everywhere (in entity-processing chunks), but spawn chunks became tiny
* ender pearls, and entities teleported by an end portal or end gateway create chunk loading tickets (but ender pearls still don't keep the dimension "active")

### 1.21.6 ("Chase the Skies")
* Non-grumpy Ghasts
  * maybe add to aquatic spawns for Cocoon of Caprice, but only via some sort of unlock (soul sand?)
  * people complain it's so slow, maybe we could do something about that
  * not vulnerable to Ender Air (no need to make them cry)
  * dried ghast block is available in GoG automatically via crafting or piglin bartering
* Non-grumpy leads (tie all the things together! not sure if that's something Botania can benefit from)
  * crafted without slime now
  * snippable, including via dispenser
* craftable saddle
* locator bar (not sure if Botania needs to interact with this)
* music toasts (probably need to give the Gaia Fight music a translation string)
* NBT serialization now uses `ValueInput` and `ValueOutput` instead of reading and writing compound tags

### 1.21.9 ("The Copper Age")
* copper golems/chests
* copper armor/tools (meh?)
* copper bars (maybe a way to summon a "fel breeze" for wind charges)
* shelves (we will need textures for livingwood/dreamwood versions of them)
* Mannequin entity (it has a shared parent class with player entities)
* spawn chunks are gone for good
* chunk loading ticket rework
  * ender pearls actually keep the dimension active now
* world spawn can be in any dimension (Nether start GoG variant?)
* charged creeper head drops are loot tables now (good to know, I guess?)
  * at this point pretty much all kinds of item drops have moved to loot tables, e.g. armadillo brushing or bee nest/hive harvesting, even pumpkin carving
* major changes to the way render logic is implemented
* NeoForge has transactional transfers now

### 1.21.11 ("Mounts of Mayhem")
* Nautilus, including armor and zombie variant
  * Breath of the Nautilus mob effect (it's just water breathing)
* Zombie horses are vanilla-obtainable, but will burn in sunlight unless covered in armor
* Spear (definitely needs Botania variants)
* Camel husk (no intentional baby variant)
* Parched
* held item "bobbing" on durability change was removed (relevant for consistency with mana-mending items)
* terrestrial mount animal no longer sink in water while ridden (except skeleton horses – yes, zombie horses swim)
* game rules use a registry now
* ResourceLocation is now Identifier (I guess Yarn won in the end)
* Minecraft and NeoForge switched to JSpecify annotations
  * migration may need to be done manually, if we decide to switch from JetBrains annotations
  * This only covers nullability!

### 26.1 ("Tiny Takeover")
* Java 25
* unobfuscated code, goodbye Parchment and good riddance Fabric Intermediary
* Golden Dandelion
  * age-locking for baby animals
  * new baby models
* all kinds of plant blocks have block tags for what they can be planted on
* block model faces can specify individual render types
  * transparent water in petal apothecary as part of the block model, not rendered by the BE
  * maybe this could even solve some of our platform block rendering issues
* data-driven villager trades
* trumpet instrument for note block
* craftable name tags
* world time stuff (not sure if Botania would want to interact with any of this)
  * game tests can happen at specific clock times
* configurable debug screen

### 26.2 ("Chaos Cubed")
* sulfur caves biome
  * sulfur/cinnabar blocks
  * an overworld biome with nearly no ores
  * sulfur spring features (similar to azalea trees above lush caves)
* potent sulfur and geysers
* Sulfur Cube (can be bucketed, multiplication by splitting and letting the small ones grow up)
  * block physics archetypes
  * entity reaction to redstone power
* beds, signs, and hanging signs use block model rendering (BE renderer only overlays the sign text)
  * new textures may be necessary for livingwood/dreamwood signs
* Vulkan rendering API (as option for now)

### 26.3 (as far as is known at this point)
* Dappled Forest biome
  * poplar trees and wood set (three different, but random leaves variants, from a single sapling type)
* shelf mushroom
  * vanilla mushrooms got a vanilla tag and tagged recipe usage
* straw bed
* cushions
* Abandoned Camp (in all kinds of biomes)
* Notable data component changes:
  * `minecraft:block_transformer` – for the various right-click actions of axes, shovels, and hoes that transform blocks, potentially with a loot item drop
    * not for e.g. water bottles, shears, or honey comb waxing
  * `minecraft:compostable` replaces data map
  * `minecraft:cooking_fuel` defines fuels for furnaces – and the Endoflame
  * `minecraft:mob_visibility` generalizes the visibility reduction previously hard-coded for mob heads
* Rendering changes (as in almost every version nowadays)
  * order-independent transparency (yay?)

----

## General wish list
* transactional mana transfers and corporea requests
  * integrated into Fabric/NeoForge transactional transfers APIs
* mana spreader behavior overhaul
  * distance should not be a (severely) limiting factor for transfer throughput
  * finer control over burst targeting/firing
  * maybe mana splitters are actually mana burst splitters?
* mana burst physics overhaul
  * simulation logic can cause all kinds of issues
  * being a projectile comes with downsides, but may be unavoidable
  * flare lens actually being useful and following the general trajectory other lenses can generate
  * burst between multiple tiny planets not being janky
* mana lens rework
  * somehow distinguish between effects that only make sense on a spreader (e.g. initial burst capacity, trigger effects) and effects that can be applied mid-flight (e.g. physics properties or impact behavior)
  * lens base material classification (manasteel, elementium, terrasteel)
  * maybe rethink the way composite lenses work
* armor trim support
  * Botania trim materials (probably easy)
  * trims on Botania armors (probably quite difficult)
* Crafty Crate and Orechid Ignem may actually still go away entirely
* .mcfunction support for non-block recipes (petal apothecary, runic altar, etc.) similar to what the Pure Daisy, Orechid and Marimophosis already support for block transformations
