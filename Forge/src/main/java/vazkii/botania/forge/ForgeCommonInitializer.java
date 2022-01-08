package vazkii.botania.forge;

import net.minecraftforge.fml.common.Mod;

import vazkii.botania.common.lib.LibMisc;

@Mod(LibMisc.MOD_ID)
public class ForgeCommonInitializer {
	public ForgeCommonInitializer() {
		ForgeBotaniaConfig.setup();
	}
}
