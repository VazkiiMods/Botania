package vazkii.botania.common.crafting;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameRegistry;
import vazkii.botania.common.item.ModItems;

/**
 * Migration recipes for things that will be removed in the future
 */
public final class ModMigrationRecipes {

    public static void init() {
        // Prismarine -> vanilla
        GameRegistry.addShapelessRecipe(new ItemStack(Items.PRISMARINE_SHARD), new ItemStack(ModItems.manaResource, 1, 10));
    }

    private ModMigrationRecipes() {}

}
