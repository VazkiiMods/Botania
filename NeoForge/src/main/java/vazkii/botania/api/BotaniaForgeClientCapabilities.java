package vazkii.botania.api;

import net.neoforged.neoforge.capabilities.BlockCapability;
import net.neoforged.neoforge.capabilities.EntityCapability;

import vazkii.botania.api.block.MonocleHud;
import vazkii.botania.api.block.WandHUD;

public final class BotaniaForgeClientCapabilities {
	public static final EntityCapability<WandHUD, Void> ENTITY_WAND_HUD = EntityCapability.createVoid(WandHUD.ID, WandHUD.class);
	public static final BlockCapability<WandHUD, Void> BLOCK_WAND_HUD = BlockCapability.createVoid(WandHUD.ID, WandHUD.class);

	public static final EntityCapability<MonocleHud, Void> ENTITY_MONOCLE_HUD = EntityCapability.createVoid(MonocleHud.ID, MonocleHud.class);
	public static final BlockCapability<MonocleHud, Void> BLOCK_MONOCLE_HUD = BlockCapability.createVoid(MonocleHud.ID, MonocleHud.class);

	private BotaniaForgeClientCapabilities() {}
}
