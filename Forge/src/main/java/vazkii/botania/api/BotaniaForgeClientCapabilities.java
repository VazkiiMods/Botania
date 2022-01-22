package vazkii.botania.api;

import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;

import vazkii.botania.api.block.IWandHUD;

public final class BotaniaForgeClientCapabilities {
	public static final Capability<IWandHUD> WAND_HUD = CapabilityManager.get(new CapabilityToken<>() {});

	private BotaniaForgeClientCapabilities() {}
}
