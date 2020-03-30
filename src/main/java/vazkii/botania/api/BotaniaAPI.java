/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.api;

import net.minecraft.block.Block;
import net.minecraft.entity.ai.attributes.IAttribute;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.DyeColor;
import net.minecraft.item.IArmorMaterial;
import net.minecraft.item.IItemTier;
import net.minecraft.item.Item;
import net.minecraft.item.Rarity;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.registry.DefaultedRegistry;
import net.minecraft.util.registry.MutableRegistry;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.registries.*;

import vazkii.botania.api.brew.Brew;
import vazkii.botania.api.internal.DummyMethodHandler;
import vazkii.botania.api.internal.IInternalMethodHandler;
import vazkii.botania.api.item.IExoflameHeatable;
import vazkii.botania.api.item.IFloatingFlower;
import vazkii.botania.api.recipe.RecipeBrew;
import vazkii.botania.api.recipe.RecipePetals;
import vazkii.botania.api.recipe.RecipeRuneAltar;
import vazkii.botania.common.core.handler.PixieHandler;

import javax.annotation.Nonnull;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Function;

public final class BotaniaAPI {
	public static int apiVersion() {
		return 94;
	}

	@CapabilityInject(IFloatingFlower.class) public static Capability<IFloatingFlower> FLOATING_FLOWER_CAP;

	@CapabilityInject(IExoflameHeatable.class) public static Capability<IExoflameHeatable> EXOFLAME_HEATABLE_CAP;

	// TODO 1.15 MOVE THIS
	public static IForgeRegistry<Brew> brewRegistry;

	/*
	* These maps are not meant to be mutated!
	*/
	public static Map<ResourceLocation, RecipePetals> petalRecipes = Collections.emptyMap();
	public static Map<ResourceLocation, RecipeRuneAltar> runeAltarRecipes = Collections.emptyMap();
	public static Map<ResourceLocation, RecipeBrew> brewRecipes = Collections.emptyMap();

	public static Map<ResourceLocation, Integer> oreWeights = Collections.emptyMap();
	public static Map<ResourceLocation, Integer> oreWeightsNether = Collections.emptyMap();

	public static Map<IRegistryDelegate<Block>, Function<DyeColor, Block>> paintableBlocks = Collections.emptyMap();

	private static final int[] MAX_DAMAGE_ARRAY = new int[] { 13, 15, 16, 11 };
	public static final IArmorMaterial MANASTEEL_ARMOR_MAT = new IArmorMaterial() {
		private final int[] damageReduction = { 2, 5, 6, 2 };

		@Override
		public int getDurability(EquipmentSlotType slotIn) {
			return 16 * MAX_DAMAGE_ARRAY[slotIn.getIndex()];
		}

		@Override
		public int getDamageReductionAmount(EquipmentSlotType slotIn) {
			return damageReduction[slotIn.getIndex()];
		}

		@Override
		public int getEnchantability() {
			return 18;
		}

		@Override
		public SoundEvent getSoundEvent() {
			return SoundEvents.ITEM_ARMOR_EQUIP_IRON;
		}

		@Override
		public Ingredient getRepairMaterial() {
			Item item = ForgeRegistries.ITEMS.getValue(new ResourceLocation("botania", "manasteel_ingot"));
			return Ingredient.fromItems(item);
		}

		@Override
		public String getName() {
			return "manasteel";
		}

		@Override
		public float getToughness() {
			return 0;
		}
	};
	public static final IItemTier MANASTEEL_ITEM_TIER = new IItemTier() {
		@Override
		public int getMaxUses() {
			return 300;
		}

		@Override
		public float getEfficiency() {
			return 6.2F;
		}

		@Override
		public float getAttackDamage() {
			return 2;
		}

		@Override
		public int getHarvestLevel() {
			return 3;
		}

		@Override
		public int getEnchantability() {
			return 20;
		}

		@Nonnull
		@Override
		public Ingredient getRepairMaterial() {
			Item item = ForgeRegistries.ITEMS.getValue(new ResourceLocation("botania", "manasteel_ingot"));
			return Ingredient.fromItems(item);
		}
	};

	public static final IArmorMaterial ELEMENTIUM_ARMOR_MAT = new IArmorMaterial() {
		private final int[] damageReduction = { 2, 5, 6, 2 };

		@Override
		public int getDurability(EquipmentSlotType slotIn) {
			return 18 * MAX_DAMAGE_ARRAY[slotIn.getIndex()];
		}

		@Override
		public int getDamageReductionAmount(EquipmentSlotType slotIn) {
			return damageReduction[slotIn.getIndex()];
		}

		@Override
		public int getEnchantability() {
			return 18;
		}

		@Override
		public SoundEvent getSoundEvent() {
			return SoundEvents.ITEM_ARMOR_EQUIP_IRON;
		}

		@Override
		public Ingredient getRepairMaterial() {
			Item item = ForgeRegistries.ITEMS.getValue(new ResourceLocation("botania", "elementium_ingot"));
			return Ingredient.fromItems(item);
		}

		@Override
		public String getName() {
			return "elementium";
		}

		@Override
		public float getToughness() {
			return 0;
		}
	};
	public static final IItemTier ELEMENTIUM_ITEM_TIER = new IItemTier() {
		@Override
		public int getMaxUses() {
			return 720;
		}

		@Override
		public float getEfficiency() {
			return 6.2F;
		}

		@Override
		public float getAttackDamage() {
			return 2;
		}

		@Override
		public int getHarvestLevel() {
			return 3;
		}

		@Override
		public int getEnchantability() {
			return 20;
		}

		@Nonnull
		@Override
		public Ingredient getRepairMaterial() {
			Item item = ForgeRegistries.ITEMS.getValue(new ResourceLocation("botania", "manasteel_ingot"));
			return Ingredient.fromItems(item);
		}
	};

	public static final IArmorMaterial TERRASTEEL_ARMOR_MAT = new IArmorMaterial() {
		private final int[] damageReduction = { 3, 6, 8, 3 };

		@Override
		public int getDurability(EquipmentSlotType slotIn) {
			return 34 * MAX_DAMAGE_ARRAY[slotIn.getIndex()];
		}

		@Override
		public int getDamageReductionAmount(EquipmentSlotType slotIn) {
			return damageReduction[slotIn.getIndex()];
		}

		@Override
		public int getEnchantability() {
			return 26;
		}

		@Override
		public SoundEvent getSoundEvent() {
			return SoundEvents.ITEM_ARMOR_EQUIP_DIAMOND;
		}

		@Override
		public Ingredient getRepairMaterial() {
			Item item = ForgeRegistries.ITEMS.getValue(new ResourceLocation("botania", "terrasteel_ingot"));
			return Ingredient.fromItems(item);
		}

		@Override
		public String getName() {
			return "terrasteel";
		}

		@Override
		public float getToughness() {
			return 3;
		}
	};
	public static final IItemTier TERRASTEEL_ITEM_TIER = new IItemTier() {
		@Override
		public int getMaxUses() {
			return 2300;
		}

		@Override
		public float getEfficiency() {
			return 9;
		}

		@Override
		public float getAttackDamage() {
			return 3;
		}

		@Override
		public int getHarvestLevel() {
			return 4;
		}

		@Override
		public int getEnchantability() {
			return 26;
		}

		@Nonnull
		@Override
		public Ingredient getRepairMaterial() {
			Item item = ForgeRegistries.ITEMS.getValue(new ResourceLocation("botania", "terrasteel_ingot"));
			return Ingredient.fromItems(item);
		}
	};
	public static final IArmorMaterial MANAWEAVE_ARMOR_MAT = new IArmorMaterial() {
		private final int[] damageReduction = { 1, 2, 2, 1 };

		@Override
		public int getDurability(EquipmentSlotType slotIn) {
			return 5 * MAX_DAMAGE_ARRAY[slotIn.getIndex()];
		}

		@Override
		public int getDamageReductionAmount(EquipmentSlotType slotIn) {
			return damageReduction[slotIn.getIndex()];
		}

		@Override
		public int getEnchantability() {
			return 18;
		}

		@Override
		public SoundEvent getSoundEvent() {
			return SoundEvents.ITEM_ARMOR_EQUIP_LEATHER;
		}

		@Override
		public Ingredient getRepairMaterial() {
			Item item = ForgeRegistries.ITEMS.getValue(new ResourceLocation("botania", "manaweave_cloth"));
			return Ingredient.fromItems(item);
		}

		@Override
		public String getName() {
			return "manaweave";
		}

		@Override
		public float getToughness() {
			return 0;
		}
	};

	public static final Rarity rarityRelic = Rarity.create("RELIC", TextFormatting.GOLD);

	// todo 1.15 move this too
	@ObjectHolder("botania:fallback")
	public static Brew fallbackBrew;

	/**
	 * The internal method handler in use.
	 * <b>DO NOT OVERWRITE THIS OR YOU'RE GOING TO FEEL MY WRATH WHEN I UPDATE THE API.</b>
	 * The fact I have to write that means some moron already tried, don't be that moron.
	 * 
	 * @see IInternalMethodHandler
	 */
	public static volatile IInternalMethodHandler internalHandler = new DummyMethodHandler();

	// todo 1.15 this is temporary, move when moving API to interface
	public static IAttribute getPixieSpawnChanceAttribute() {
		return PixieHandler.PIXIE_SPAWN_CHANCE;
	}
}
