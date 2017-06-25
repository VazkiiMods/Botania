package vazkii.botania.common.crafting;

/**
 * Migration recipes for things that will be removed in the future
 */
public final class ModMigrationRecipes {

	public static void init() {
		// Prismarine -> vanilla
		// todo 1.12 json GameRegistry.addShapelessRecipe(new ItemStack(Items.PRISMARINE_SHARD), new ItemStack(ModItems.manaResource, 1, 10));
	}

	private ModMigrationRecipes() {}

}
