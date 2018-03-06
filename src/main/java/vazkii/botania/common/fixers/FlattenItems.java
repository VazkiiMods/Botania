package vazkii.botania.common.fixers;

import net.minecraft.init.Items;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.datafix.IFixableData;
import vazkii.botania.api.item.IAncientWillContainer;
import vazkii.botania.api.state.enums.CratePattern;
import vazkii.botania.common.block.ModBlocks;
import vazkii.botania.common.item.ModItems;
import vazkii.botania.common.item.equipment.bauble.ItemBaubleCosmetic;
import vazkii.botania.common.lib.LibBlockNames;
import vazkii.botania.common.lib.LibItemNames;
import vazkii.botania.common.lib.LibMisc;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.function.IntFunction;

// Flattens meta variants by splitting out to new ID's
public class FlattenItems implements IFixableData {
	// old id to (meta -> new id)
	private static final Map<String, IntFunction<String>> LOOKUP = new HashMap<>();

	static {
		LOOKUP.put("botania:manaresource", i -> {
			if(i == 10) {
				// support ancient prismarine from 1.7
				return Items.PRISMARINE_SHARD.getRegistryName().toString();
			} else {
				return LibMisc.MOD_ID + ":" + LibItemNames.MANA_RESOURCE_NAMES[i % LibItemNames.MANA_RESOURCE_NAMES.length];
			}
		});
		LOOKUP.put("botania:vial", i -> {
			if(i == 1) {
				return ModItems.flask.getRegistryName().toString();
			} else {
				return ModItems.vial.getRegistryName().toString();
			}
		});
		LOOKUP.put("botania:quartz", i -> LibMisc.MOD_ID + ":" + LibItemNames.QUARTZ_NAMES[i % LibItemNames.QUARTZ_NAMES.length]);
		LOOKUP.put("botania:thornchakram", i -> {
			if(i == 1) {
				 return ModItems.flareChakram.getRegistryName().toString();
			} else {
				return ModItems.thornChakram.getRegistryName().toString();
			}
		});
		LOOKUP.put("botania:grasshorn", i -> {
			switch (i) {
				case 0:
				default:
				return ModItems.grassHorn.getRegistryName().toString();
				case 1: return ModItems.leavesHorn.getRegistryName().toString();
				case 2: return ModItems.snowHorn.getRegistryName().toString();
			}
		});
		LOOKUP.put("botania:lens", i -> LibMisc.MOD_ID + ":" + LibItemNames.LENS_NAMES[i % LibItemNames.LENS_NAMES.length]);
		LOOKUP.put("botania:dye", i -> {
			EnumDyeColor color = EnumDyeColor.byMetadata(i);
			return ModItems.dyes.get(color).getRegistryName().toString();
		});
		LOOKUP.put("botania:petal", i -> {
			EnumDyeColor color = EnumDyeColor.byMetadata(i);
			return ModItems.petals.get(color).getRegistryName().toString();
		});
		LOOKUP.put("botania:virus", i -> {
			if (i == 1) {
				return ModItems.nullVirus.getRegistryName().toString();
			} else {
				return ModItems.necroVirus.getRegistryName().toString();
			}
		});
		LOOKUP.put("botania:grassseeds", i -> {
			switch(i) {
				default:
				case 0: return ModItems.grassSeeds.getRegistryName().toString();
				case 1: return ModItems.podzolSeeds.getRegistryName().toString();
				case 2: return ModItems.mycelSeeds.getRegistryName().toString();
				case 3: return ModItems.drySeeds.getRegistryName().toString();
				case 4: return ModItems.goldenSeeds.getRegistryName().toString();
				case 5: return ModItems.vividSeeds.getRegistryName().toString();
				case 6: return ModItems.scorchedSeeds.getRegistryName().toString();
				case 7: return ModItems.infusedSeeds.getRegistryName().toString();
				case 8: return ModItems.mutatedSeeds.getRegistryName().toString();
			}
		});
		LOOKUP.put("botania:sparkupgrade", i -> {
			switch(i) {
				default:
				case 0: return ModItems.sparkUpgradeDispersive.getRegistryName().toString();
				case 1: return ModItems.sparkUpgradeDominant.getRegistryName().toString();
				case 2: return ModItems.sparkUpgradeRecessive.getRegistryName().toString();
				case 3: return ModItems.sparkUpgradeIsolated.getRegistryName().toString();
			}
		});
		LOOKUP.put("botania:ancientwill", i -> {
			IAncientWillContainer.AncientWillType[] vals = IAncientWillContainer.AncientWillType.values();
			return LibMisc.MOD_ID + ":" + LibItemNames.ANCIENT_WILL + "_" + vals[i & vals.length].name().toLowerCase(Locale.ROOT);
		});
		LOOKUP.put("botania:craftpattern", i -> {
			// +1 since there was no NONE meta variant
			CratePattern[] vals = CratePattern.values();
			return LibMisc.MOD_ID + ":" + LibItemNames.CRAFT_PATTERN + "_" + vals[(i + 1) % vals.length].name().toLowerCase(Locale.ROOT);
		});
		LOOKUP.put("botania:rune", i -> LibMisc.MOD_ID + ":" + LibItemNames.RUNE_NAMES[i % LibItemNames.RUNE_NAMES.length]);
		LOOKUP.put("botania:cosmetic", i -> {
			ItemBaubleCosmetic.Variant[] vals = ItemBaubleCosmetic.Variant.values();
			return LibMisc.MOD_ID + ":" + LibItemNames.COSMETIC + "_" + vals[i % vals.length].name().toLowerCase(Locale.ROOT);
		});
		LOOKUP.put("botania:corporeaspark", i -> {
			if(i == 1) {
				return ModItems.corporeaSparkMaster.getRegistryName().toString();
			} else {
				return ModItems.corporeaSpark.getRegistryName().toString();
			}
		});
		LOOKUP.put("botania:blacklotus", i -> {
			if(i == 1) {
				return ModItems.blackerLotus.getRegistryName().toString();
			} else {
				return ModItems.blackLotus.getRegistryName().toString();
			}
		});

		// ItemBlocks
		LOOKUP.put("botania:altgrass", i -> {
			switch (i) {
				default:
				case 0: return ModBlocks.dryGrass.getRegistryName().toString();
				case 1: return ModBlocks.goldenGrass.getRegistryName().toString();
				case 2: return ModBlocks.vividGrass.getRegistryName().toString();
				case 3: return ModBlocks.scorchedGrass.getRegistryName().toString();
				case 4: return ModBlocks.infusedGrass.getRegistryName().toString();
				case 5: return ModBlocks.mutatedGrass.getRegistryName().toString();
			}
		});
		LOOKUP.put("botania:storage", i -> {
			switch (i) {
				default:
				case 0: return ModBlocks.manasteelBlock.getRegistryName().toString();
				case 1: return ModBlocks.terrasteelBlock.getRegistryName().toString();
				case 2: return ModBlocks.elementiumBlock.getRegistryName().toString();
				case 3: return ModBlocks.manaDiamondBlock.getRegistryName().toString();
				case 4: return ModBlocks.dragonstoneBlock.getRegistryName().toString();
			}
		});
		LOOKUP.put("botania:custombrick", i -> {
			int new_variant = 0;
			// azulejos 1-12 in metas 4-15
			if (i >= 4) {
				new_variant = i - 3;
			} else {
				// azulejos 0, 13, 14, 15 in metas 0-3
				switch (i) {
					default:
					case 0: new_variant = 0; break;
					case 1: new_variant = 13; break;
					case 2: new_variant = 14; break;
					case 3: new_variant = 15; break;
				}
			}

			return LibMisc.MOD_ID + ":" + LibBlockNames.AZULEJO_PREFIX + new_variant;
		});
		LOOKUP.put("botania:pool", i -> {
			switch (i) {
				default:
				case 0: return ModBlocks.manaPool.getRegistryName().toString();
				case 1: return ModBlocks.creativePool.getRegistryName().toString();
				case 2: return ModBlocks.dilutedPool.getRegistryName().toString();
				case 3: return ModBlocks.fabulousPool.getRegistryName().toString();
			}
		});
	}

	@Override
	public int getFixVersion() {
		return 1;
	}

	@Nonnull
	@Override
	public NBTTagCompound fixTagCompound(@Nonnull NBTTagCompound compound) {
		String id = compound.getString("id");
		if(LOOKUP.containsKey(id)) {
			int meta = compound.getShort("Damage");
			compound.setString("id", LOOKUP.get(id).apply(meta));
			compound.removeTag("Damage");
		}
		return compound;
	}
}
