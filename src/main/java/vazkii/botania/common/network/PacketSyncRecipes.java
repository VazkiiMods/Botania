package vazkii.botania.common.network;

import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.network.NetworkEvent;
import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.api.recipe.RecipeBrew;
import vazkii.botania.api.recipe.RecipeElvenTrade;
import vazkii.botania.api.recipe.RecipeManaInfusion;
import vazkii.botania.api.recipe.RecipePetals;
import vazkii.botania.api.recipe.RecipePureDaisy;
import vazkii.botania.api.recipe.RecipeRuneAltar;
import vazkii.botania.common.lexicon.LexiconData;

import java.util.Map;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class PacketSyncRecipes {
    private Map<ResourceLocation, RecipeBrew> brew;
    private Map<ResourceLocation, RecipeElvenTrade> elven;
    private Map<ResourceLocation, RecipeManaInfusion> manaInfusion;
    private Map<ResourceLocation, RecipePetals> petal;
    private Map<ResourceLocation, RecipePureDaisy> pureDaisy;
    private Map<ResourceLocation, RecipeRuneAltar> runeAltar;

    public PacketSyncRecipes(Map<ResourceLocation, RecipeBrew> brew, Map<ResourceLocation, RecipeElvenTrade> elven, Map<ResourceLocation, RecipeManaInfusion> manaInfusion, Map<ResourceLocation, RecipePetals> petal, Map<ResourceLocation, RecipePureDaisy> pureDaisy, Map<ResourceLocation, RecipeRuneAltar> runeAltar) {
        this.brew = brew;
        this.elven = elven;
        this.manaInfusion = manaInfusion;
        this.petal = petal;
        this.pureDaisy = pureDaisy;
        this.runeAltar = runeAltar;
    }

    public void encode(PacketBuffer buf) {
        buf.writeVarInt(brew.size());
        for (RecipeBrew recipe : brew.values()) {
            recipe.write(buf);
        }
        buf.writeVarInt(elven.size());
        for (RecipeElvenTrade recipe : elven.values()) {
            recipe.write(buf);
        }
        buf.writeVarInt(manaInfusion.size());
        for (RecipeManaInfusion recipe : manaInfusion.values()) {
            recipe.write(buf);
        }
        buf.writeVarInt(petal.size());
        for (RecipePetals recipe : petal.values()) {
            recipe.write(buf);
        }
        buf.writeVarInt(pureDaisy.size());
        for (RecipePureDaisy recipe : pureDaisy.values()) {
            recipe.write(buf);
        }
        buf.writeVarInt(runeAltar.size());
        for (RecipeRuneAltar recipe : runeAltar.values()) {
            recipe.write(buf);
        }
    }

    public static PacketSyncRecipes decode(PacketBuffer buf) {
        int brewCount = buf.readVarInt();
        Map<ResourceLocation, RecipeBrew> brew = Stream.generate(() -> RecipeBrew.read(buf))
                .limit(brewCount)
                .collect(Collectors.toMap(RecipeBrew::getId, r -> r));
        int elvenCount = buf.readVarInt();
        Map<ResourceLocation, RecipeElvenTrade> elven = Stream.generate(() -> RecipeElvenTrade.read(buf))
                .limit(elvenCount)
                .collect(Collectors.toMap(RecipeElvenTrade::getId, r -> r));
        int manaInfusionCount = buf.readVarInt();
        Map<ResourceLocation, RecipeManaInfusion> manaInfusion = Stream.generate(() -> RecipeManaInfusion.read(buf))
                .limit(manaInfusionCount)
                .collect(Collectors.toMap(RecipeManaInfusion::getId, r -> r));
        int petalCount = buf.readVarInt();
        Map<ResourceLocation, RecipePetals> petal = Stream.generate(() -> RecipePetals.read(buf))
                .limit(petalCount)
                .collect(Collectors.toMap(RecipePetals::getId, r -> r));
        int pureDaisyCount = buf.readVarInt();
        Map<ResourceLocation, RecipePureDaisy> pureDaisy = Stream.generate(() -> RecipePureDaisy.read(buf))
                .limit(pureDaisyCount)
                .collect(Collectors.toMap(RecipePureDaisy::getId, r -> r));
        int runeCount = buf.readVarInt();
        Map<ResourceLocation, RecipeRuneAltar> rune = Stream.generate(() -> RecipeRuneAltar.read(buf))
                .limit(runeCount)
                .collect(Collectors.toMap(RecipeRuneAltar::getId, r -> r));
        return new PacketSyncRecipes(brew, elven, manaInfusion, petal, pureDaisy, rune);
    }

    public void handle(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            BotaniaAPI.brewRecipes = brew;
            BotaniaAPI.elvenTradeRecipes = elven;
            BotaniaAPI.manaInfusionRecipes = manaInfusion;
            BotaniaAPI.petalRecipes = petal;
            BotaniaAPI.pureDaisyRecipes = pureDaisy;
            BotaniaAPI.runeAltarRecipes = runeAltar;
            LexiconData.reload();
        });
        ctx.get().setPacketHandled(true);
    }
}
