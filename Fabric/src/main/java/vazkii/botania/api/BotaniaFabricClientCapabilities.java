package vazkii.botania.api;

import net.fabricmc.fabric.api.lookup.v1.block.BlockApiLookup;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Unit;

import vazkii.botania.api.block.IWandHUD;

public class BotaniaFabricClientCapabilities {
	public static final BlockApiLookup<IWandHUD, Unit> WAND_HUD = BlockApiLookup.get(new ResourceLocation(BotaniaAPI.MODID, "wand_hud"), IWandHUD.class, Unit.class);
}
