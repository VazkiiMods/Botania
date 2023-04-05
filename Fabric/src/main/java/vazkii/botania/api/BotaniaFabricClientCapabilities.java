package vazkii.botania.api;

import net.fabricmc.fabric.api.lookup.v1.block.BlockApiLookup;
import net.fabricmc.fabric.api.lookup.v1.entity.EntityApiLookup;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Unit;

import vazkii.botania.api.block.WandHUD;

public final class BotaniaFabricClientCapabilities {
	public static final BlockApiLookup<WandHUD, Unit> WAND_HUD = BlockApiLookup.get(new ResourceLocation(BotaniaAPI.MODID, "wand_hud"), WandHUD.class, Unit.class);
	public static final EntityApiLookup<WandHUD, Unit> ENTITY_WAND_HUD = EntityApiLookup.get(new ResourceLocation(BotaniaAPI.MODID, "wand_hud"), WandHUD.class, Unit.class);

	private BotaniaFabricClientCapabilities() {}
}
