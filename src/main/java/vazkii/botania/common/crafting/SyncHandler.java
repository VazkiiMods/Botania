package vazkii.botania.common.crafting;

import com.google.common.collect.ImmutableMap;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.resources.IResourceManager;
import net.minecraft.resources.IResourceManagerReloadListener;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ClientPlayerNetworkEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.network.PacketDistributor;
import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.api.recipe.RecipeBrew;
import vazkii.botania.api.recipe.RecipeElvenTrade;
import vazkii.botania.api.recipe.RecipeManaInfusion;
import vazkii.botania.api.recipe.RecipePetals;
import vazkii.botania.api.recipe.RecipePureDaisy;
import vazkii.botania.api.recipe.RecipeRuneAltar;
import vazkii.botania.api.recipe.RegisterRecipesEvent;
import vazkii.botania.common.lib.LibMisc;
import vazkii.botania.common.network.PacketHandler;
import vazkii.botania.common.network.PacketSyncRecipes;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class SyncHandler {
    public static class ReloadListener implements IResourceManagerReloadListener {
        @Override
        public void onResourceManagerReload(@Nullable IResourceManager manager) {
            Map<ResourceLocation, RecipePureDaisy> pureDaisy = new HashMap<>();
            Map<ResourceLocation, RecipeManaInfusion> manaInfusion = new HashMap<>();
            Map<ResourceLocation, RecipeBrew> brew = new HashMap<>();
            Map<ResourceLocation, RecipeElvenTrade> elvenTrade = new HashMap<>();
            Map<ResourceLocation, RecipePetals> apothecary = new HashMap<>();
            Map<ResourceLocation, RecipeRuneAltar> runeAltar = new HashMap<>();

            RegisterRecipesEvent evt = new RegisterRecipesEvent(
                    r -> pureDaisy.put(r.getId(), r),
                    r -> manaInfusion.put(r.getId(), r),
                    r -> brew.put(r.getId(), r),
                    r -> elvenTrade.put(r.getId(), r),
                    r -> apothecary.put(r.getId(), r),
                    r -> runeAltar.put(r.getId(), r)
            );
            MinecraftForge.EVENT_BUS.post(evt);

            BotaniaAPI.pureDaisyRecipes = ImmutableMap.copyOf(pureDaisy);
            BotaniaAPI.manaInfusionRecipes = ImmutableMap.copyOf(manaInfusion);
            BotaniaAPI.brewRecipes = ImmutableMap.copyOf(brew);
            BotaniaAPI.elvenTradeRecipes = ImmutableMap.copyOf(elvenTrade);
            BotaniaAPI.petalRecipes = ImmutableMap.copyOf(apothecary);
            BotaniaAPI.runeAltarRecipes = ImmutableMap.copyOf(runeAltar);
            PacketHandler.HANDLER.send(PacketDistributor.ALL.noArg(), syncPacket());
        }
    }

    private static PacketSyncRecipes syncPacket() {
        return new PacketSyncRecipes(BotaniaAPI.brewRecipes, BotaniaAPI.elvenTradeRecipes, BotaniaAPI.manaInfusionRecipes,
                BotaniaAPI.petalRecipes, BotaniaAPI.pureDaisyRecipes, BotaniaAPI.runeAltarRecipes);
    }

    @Mod.EventBusSubscriber(modid = LibMisc.MOD_ID, value = Dist.CLIENT)
    public static class ClientEvents {
        @SubscribeEvent
        public static void clientLogout(ClientPlayerNetworkEvent.LoggedOutEvent evt) {
            BotaniaAPI.brewRecipes = Collections.emptyMap();
            BotaniaAPI.elvenTradeRecipes = Collections.emptyMap();
            BotaniaAPI.manaInfusionRecipes = Collections.emptyMap();
            BotaniaAPI.petalRecipes = Collections.emptyMap();
            BotaniaAPI.pureDaisyRecipes = Collections.emptyMap();
            BotaniaAPI.runeAltarRecipes = Collections.emptyMap();
        }
    }

}
