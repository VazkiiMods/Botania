package vazkii.botania.common;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegistryBuilder;
import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.api.subtile.SubTileType;
import vazkii.botania.common.lib.LibMisc;

public class BotaniaRegistries {
    public static final IForgeRegistry<SubTileType> SUBTILES = new RegistryBuilder<SubTileType>()
            .setName(new ResourceLocation(LibMisc.MOD_ID, "subtiles"))
            .setType(SubTileType.class)
            .setDefaultKey(BotaniaAPI.DUMMY_SUBTILE_NAME)
            .disableSaving()
            .create();

    // To force class load
    public static void init() {}
}
