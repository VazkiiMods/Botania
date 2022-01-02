/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.block;

public final class ModPatterns {
	/*
	public static LoomPattern FLOWER = new ModLoomPattern(true);
	public static LoomPattern LEXICON = new ModLoomPattern(true);
	public static LoomPattern LOGO = new ModLoomPattern(true);
	public static LoomPattern SAPLING = new ModLoomPattern(true);
	public static LoomPattern TINY_POTATO = new ModLoomPattern(true);
	public static LoomPattern SPARK_DISPERSIVE = new ModLoomPattern(true);
	public static LoomPattern SPARK_DOMINANT = new ModLoomPattern(true);
	public static LoomPattern SPARK_RECESSIVE = new ModLoomPattern(true);
	public static LoomPattern SPARK_ISOLATED = new ModLoomPattern(true);
	
	public static LoomPattern FISH = new ModLoomPattern(false);
	public static LoomPattern AXE = new ModLoomPattern(false);
	public static LoomPattern HOE = new ModLoomPattern(false);
	public static LoomPattern PICKAXE = new ModLoomPattern(false);
	public static LoomPattern SHOVEL = new ModLoomPattern(false);
	public static LoomPattern SWORD = new ModLoomPattern(false);
	*/

	public static void init() {
		/*
		register(LoomPatterns.REGISTRY, "flower", FLOWER);
		register(LoomPatterns.REGISTRY, "lexicon", LEXICON);
		register(LoomPatterns.REGISTRY, "logo", LOGO);
		register(LoomPatterns.REGISTRY, "sapling", SAPLING);
		register(LoomPatterns.REGISTRY, "tiny_potato", TINY_POTATO);
		//
		register(LoomPatterns.REGISTRY, "spark_dispersive", SPARK_DISPERSIVE);
		register(LoomPatterns.REGISTRY, "spark_dominant", SPARK_DOMINANT);
		register(LoomPatterns.REGISTRY, "spark_recessive", SPARK_RECESSIVE);
		register(LoomPatterns.REGISTRY, "spark_isolated", SPARK_ISOLATED);
		//
		register(LoomPatterns.REGISTRY, "fish", FISH);
		register(LoomPatterns.REGISTRY, "axe", AXE);
		register(LoomPatterns.REGISTRY, "hoe", HOE);
		register(LoomPatterns.REGISTRY, "pickaxe", PICKAXE);
		register(LoomPatterns.REGISTRY, "shovel", SHOVEL);
		register(LoomPatterns.REGISTRY, "sword", SWORD);
		*/
	}

	/*
	private static class ModLoomPattern extends LoomPattern {
		public ModLoomPattern(boolean special) {
			super(special);
		}
	
		@Override
		public ResourceLocation getSpriteId(String type) {
			return new ResourceLocation("entity/" + type + "/botania_" + LoomPatterns.REGISTRY.getKey(this).getPath());
		}
	
		@Override
		public void addPatternLine(List<Component> lines, DyeColor color) {
			String colorName = color.getName();
			if (colorName.equals("light_blue")) {
				colorName = "lightBlue";
			} else if (colorName.equals("light_gray")) {
				colorName = "silver";
			}
			lines.add(new TranslatableComponent("block.minecraft.banner.botania_" + LoomPatterns.REGISTRY.getKey(this).getPath() + "." + colorName).withStyle(ChatFormatting.GRAY));
		}
	}
	*/
}
