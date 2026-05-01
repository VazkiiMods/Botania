package vazkii.botania.api;

import net.fabricmc.fabric.api.lookup.v1.block.BlockApiLookup;
import net.fabricmc.fabric.api.lookup.v1.entity.EntityApiLookup;
import net.minecraft.util.Unit;

import vazkii.botania.api.block.MonocleHud;
import vazkii.botania.api.block.WandHUD;

public final class BotaniaFabricClientCapabilities {
	public static final BlockApiLookup<WandHUD, Unit> BLOCK_WAND_HUD = BlockApiLookup.get(WandHUD.ID, WandHUD.class, Unit.class);
	public static final EntityApiLookup<WandHUD, Unit> ENTITY_WAND_HUD = EntityApiLookup.get(WandHUD.ID, WandHUD.class, Unit.class);

	public static final BlockApiLookup<MonocleHud, Unit> BLOCK_MONOCLE_HUD = BlockApiLookup.get(MonocleHud.ID, MonocleHud.class, Unit.class);
	public static final EntityApiLookup<MonocleHud, Unit> ENTITY_MONOCLE_HUD = EntityApiLookup.get(MonocleHud.ID, MonocleHud.class, Unit.class);

	private BotaniaFabricClientCapabilities() {}
}
