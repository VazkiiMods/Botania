/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 * 
 * File Created @ [Jul 28, 2014, 6:18:05 PM (GMT)]
 */
package vazkii.botania.common.item.interaction.thaumcraft;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.RecipeSorter;
import net.minecraftforge.oredict.RecipeSorter.Category;
import thaumcraft.api.IGoggles;
import thaumcraft.api.nodes.IRevealer;
import vazkii.botania.client.lib.LibResources;
import vazkii.botania.common.core.handler.ConfigHandler;
import vazkii.botania.common.crafting.recipe.HelmRevealingRecipe;
import vazkii.botania.common.item.equipment.armor.manasteel.ItemManasteelHelm;
import vazkii.botania.common.lib.LibItemNames;
import cpw.mods.fml.common.Optional;
import cpw.mods.fml.common.registry.GameRegistry;

@Optional.InterfaceList({
	@Optional.Interface(modid = "Thaumcraft", iface = "thaumcraft.api.IGoggles", striprefs = true),
	@Optional.Interface(modid = "Thaumcraft", iface = "thaumcraft.api.nodes.IRevealer", striprefs = true)})
public class ItemManasteelHelmRevealing extends ItemManasteelHelm implements IGoggles, IRevealer {

	public ItemManasteelHelmRevealing() {
		super(LibItemNames.MANASTEEL_HELM_R);
		GameRegistry.addRecipe(new HelmRevealingRecipe()); //Manasteel is the base so it gets the recipe added in its constructor so that ModItems can call it
		RecipeSorter.register("botania:helmRevealing", HelmRevealingRecipe.class, Category.SHAPELESS, "");
	}

	@Override
	public boolean showNodes(ItemStack itemstack, EntityLivingBase player) {
		return true;
	}

	@Override
	public boolean showIngamePopups(ItemStack itemstack, EntityLivingBase player) {
		return true;
	}

	@Override
	public String getArmorTextureAfterInk(ItemStack stack, int slot) {
		return ConfigHandler.enableArmorModels ? LibResources.MODEL_MANASTEEL_NEW : LibResources.MODEL_MANASTEEL_2;
	}

}