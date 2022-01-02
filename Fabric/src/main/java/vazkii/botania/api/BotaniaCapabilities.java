package vazkii.botania.api;

import net.fabricmc.fabric.api.lookup.v1.block.BlockApiLookup;
import net.fabricmc.fabric.api.lookup.v1.item.ItemApiLookup;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Unit;

import vazkii.botania.api.block.IHornHarvestable;
import vazkii.botania.api.block.IHourglassTrigger;
import vazkii.botania.api.block.IWandable;
import vazkii.botania.api.item.IAvatarWieldable;
import vazkii.botania.api.item.IBlockProvider;
import vazkii.botania.api.item.ICoordBoundItem;

public class BotaniaCapabilities {
	public static final ItemApiLookup<IAvatarWieldable, Unit> AVATAR_WIELDABLE = ItemApiLookup.get(new ResourceLocation(BotaniaAPI.MODID, "avatar_wieldable"), IAvatarWieldable.class, Unit.class);
	public static final ItemApiLookup<IBlockProvider, Unit> BLOCK_PROVIDER = ItemApiLookup.get(new ResourceLocation(BotaniaAPI.MODID, "block_provider"), IBlockProvider.class, Unit.class);
	public static final ItemApiLookup<ICoordBoundItem, Unit> COORD_BOUND_ITEM = ItemApiLookup.get(new ResourceLocation(BotaniaAPI.MODID, "coord_bound_item"),
			ICoordBoundItem.class, Unit.class);

	public static final BlockApiLookup<IHornHarvestable, Unit> HORN_HARVEST = BlockApiLookup.get(new ResourceLocation(BotaniaAPI.MODID, "horn_harvestable"), IHornHarvestable.class, Unit.class);
	public static final BlockApiLookup<IHourglassTrigger, Unit> HOURGLASS_TRIGGER = BlockApiLookup.get(new ResourceLocation(BotaniaAPI.MODID, "hourglass_trigger"), IHourglassTrigger.class, Unit.class);
	public static final BlockApiLookup<IWandable, Unit> WANDABLE = BlockApiLookup.get(new ResourceLocation(BotaniaAPI.MODID, "wandable"), IWandable.class, Unit.class);
}
