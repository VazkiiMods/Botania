/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 * 
 * File Created @ [Feb 22, 2015, 2:01:01 PM (GMT)]
 */
package vazkii.botania.common.item.equipment.bauble;

import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.oredict.RecipeSorter;
import net.minecraftforge.oredict.RecipeSorter.Category;

import org.lwjgl.opengl.GL11;

import vazkii.botania.api.item.ICosmeticBauble;
import vazkii.botania.common.crafting.recipe.CosmeticAttachRecipe;
import vazkii.botania.common.crafting.recipe.CosmeticRemoveRecipe;
import vazkii.botania.common.lib.LibItemNames;
import baubles.api.BaubleType;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class ItemBaubleCosmetic extends ItemBauble implements ICosmeticBauble {

	private static final int SUBTYPES = 32;

	public ItemBaubleCosmetic() {
		super(LibItemNames.COSMETIC);
		setHasSubtypes(true);

		GameRegistry.addRecipe(new CosmeticAttachRecipe());
		GameRegistry.addRecipe(new CosmeticRemoveRecipe());
		RecipeSorter.register("botania:cosmeticAttach", CosmeticAttachRecipe.class, Category.SHAPELESS, "");
		RecipeSorter.register("botania:cosmeticRemove", CosmeticRemoveRecipe.class, Category.SHAPELESS, "");
	}

	@Override
	public void getSubItems(Item item, CreativeTabs tab, List list) {
		for(int i = 0; i < SUBTYPES; i++)
			list.add(new ItemStack(item, 1, i));
	}

	@Override
	public String getUnlocalizedName(ItemStack par1ItemStack) {
		return super.getUnlocalizedName(par1ItemStack) + par1ItemStack.getItemDamage();
	}

	@Override
	public void addHiddenTooltip(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, List par3List, boolean par4) {
		addStringToTooltip(StatCollector.translateToLocal("botaniamisc.cosmeticBauble"), par3List);
		super.addHiddenTooltip(par1ItemStack, par2EntityPlayer, par3List, par4);
	}

	@Override
	public BaubleType getBaubleType(ItemStack arg0) {
		return BaubleType.AMULET;
	}

	@Override
	public void onPlayerBaubleRender(ItemStack stack, RenderPlayerEvent event, RenderType type) {
		Minecraft.getMinecraft().renderEngine.bindTexture(TextureMap.locationBlocksTexture);
		if(type == RenderType.HEAD) {
			Helper.translateToHeadLevel(event.entityPlayer);
			switch(stack.getItemDamage()) {
			case 2:
				faceTranslate();
				scale(0.75F);
				GlStateManager.translate(0.04F, -0.5F, 0F);
				renderIcon(2);
				break;
			case 4:
				faceTranslate();
				scale(0.75F);
				GlStateManager.translate(0.04F, -0.5F, 0F);
				renderIcon(4);
				break;
			case 5:
				faceTranslate();
				scale(0.35F);
				GlStateManager.translate(0.3F, -0.5F, 0F);
				renderIcon(5);
				break;
			case 6:
				faceTranslate();
				scale(0.35F);
				GlStateManager.translate(0.9F, -0.5F, 0F);
				renderIcon(6);
				break;
			case 7:
				faceTranslate();
				scale(0.6F);
				GlStateManager.translate(0.2F, 0.3F, 0.6F);
				renderIcon(7);
				break;
			case 8:
				faceTranslate();
				GlStateManager.rotate(90F, 0F, 1F, 0F);
				scale(0.6F);
				GlStateManager.translate(-0.9F, 0F, 0.2F);
				renderIcon(8);
				break;
			case 9:
				faceTranslate();
				GlStateManager.rotate(90F, 0F, 1F, 0F);
				scale(0.6F);
				GlStateManager.translate(-0.9F, -0.2F, 0.2F);
				renderIcon(9);
				GlStateManager.translate(0F, 0F, 1F);
				renderIcon(9);
				break;
			case 10:
				faceTranslate();
				GlStateManager.rotate(90F, 0F, 1F, 0F);
				scale(0.4F);
				GlStateManager.translate(-0.5F, -0.1F, 0.3F);
				GlStateManager.rotate(120F, 0F, 1F, 0F);
				renderIcon(10);
				GlStateManager.rotate(-100F, 0F, 1F, 0F);
				renderIcon(10);
				break;
			case 11:
				faceTranslate();
				scale(0.6F);
				GlStateManager.translate(0.2F, -0.1F, 0.6F);
				renderIcon(11);
				break;
			case 15:
				faceTranslate();
				GlStateManager.translate(-0.1F, -0.55F, 0F);
				renderIcon(15);
				break;
			case 17:
				faceTranslate();
				scale(0.35F);
				GlStateManager.translate(0.3F, -0.6F, 0F);
				renderIcon(17);
				break;
			case 18:
				faceTranslate();
				scale(0.75F);
				GlStateManager.rotate(90F, 0F, 1F, 0F);
				GlStateManager.translate(-0.3F, 0.1F, 0.55F);
				renderIcon(18);
				break;
			case 19:
				faceTranslate();
				scale(0.6F);
				GlStateManager.translate(0.2F, -0.2F, 0.1F);
				renderIcon(19);
				break;
			case 20:
				faceTranslate();
				scale(0.25F);
				GlStateManager.translate(0.4F, 0.5F, -0.1F);
				renderIcon(20);
				GlStateManager.translate(1.4F, 0F, 0F);
				renderIcon(20);
				break;
			case 22:
				faceTranslate();
				scale(0.75F);
				GlStateManager.translate(0.04F, -0.4F, 0F);
				renderIcon(22);
				break;
			case 23:
				faceTranslate();
				scale(0.75F);
				GlStateManager.translate(0.04F, -0.4F, 0F);
				renderIcon(23);
				break;
			case 24:
				faceTranslate();
				scale(0.6F);
				GlStateManager.translate(0.5F, 0F, 0.1F);
				GlStateManager.rotate(60F, 0F, 0F, 1F);
				renderIcon(24);
				break;
			case 25:
				faceTranslate();
				scale(0.75F);
				GlStateManager.translate(0.04F, -0.5F, 0F);
				renderIcon(25);
				break;
			case 26:
				faceTranslate();
				GlStateManager.translate(-0.1F, -0.5F, 0F);
				GlStateManager.enableBlend();
				GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
				GlStateManager.color(1F, 1F, 1F, 0.7F);
				renderIcon(26);
				break;
			case 27:
				faceTranslate();
				scale(0.75F);
				GlStateManager.translate(0.04F, -0.5F, 0F);
				renderIcon(27);
				break;
			case 28:
				faceTranslate();
				scale(0.25F);
				GlStateManager.translate(1.55F, -0.2F, -0.1F);
				renderIcon(28);
				GlStateManager.rotate(180F, 0F, 1F, 0F);
				GlStateManager.translate(-0.1F, 0F, 0.1F);
				renderIcon(28);
				break;
			case 30:
				faceTranslate();
				scale(0.75F);
				GlStateManager.translate(0.04F, -0.4F, 0F);
				renderIcon(30);
				break;
			case 31:
				faceTranslate();
				scale(0.5F);
				GlStateManager.translate(0.3F, 0.7F, 0.5F);
				renderIcon(31);
				break;
			}
		} else {
			Helper.rotateIfSneaking(event.entityPlayer);
			switch(stack.getItemDamage()) {
			case 0:
				chestTranslate();
				scale(0.5F);
				GlStateManager.translate(0.5F, 0.7F, 0F);
				renderIcon(0);
				break;
			case 1:
				chestTranslate();
				scale(0.75F);
				GlStateManager.translate(0.15F, -0.1F, 0F);
				renderIcon(1);
				break;
			case 3:
				chestTranslate();
				scale(0.6F);
				GlStateManager.translate(0.35F, 0.3F, 0F);
				renderIcon(3);
				break;
			case 12:
				chestTranslate();
				scale(0.225F);
				GlStateManager.translate(1.2F, 1.9F, 0F);
				renderIcon(12);
				break;
			case 13:
				chestTranslate();
				GlStateManager.rotate(-90F, 0F, 1F, 0F);
				scale(0.5F);
				GlStateManager.translate(-1.3F, -0.4F, -1F);
				renderIcon(13);
				break;
			case 14:
				chestTranslate();
				scale(0.5F);
				GlStateManager.translate(2.3F, 1F, -0.05F);
				GlStateManager.rotate(180F, 0F, 1F, 0F);
				renderIcon(14);
				GlStateManager.rotate(180F, 0F, 1F, 0F);
				GlStateManager.color(0F, 0F, 0.3F, 1F);
				GlStateManager.translate(-2.6F, 0F, 0.05F);
				renderIcon(14);
				break;
			case 16:
				chestTranslate();
				scale(0.225F);
				GlStateManager.translate(2.3F, 1.9F, 0F);
				renderIcon(16);
				break;
			case 21:
				chestTranslate();
				scale(0.3F);
				GlStateManager.translate(1.2F, 0.5F, 0F);
				renderIcon(21);
				break;
			case 29:
				chestTranslate();
				scale(0.8F);
				GlStateManager.translate(0.2F, -0.2F, -0.35F);
				GlStateManager.rotate(10F, 0F, 0F, 1F);
				renderIcon(29);
				break;
			}
		}
	}

	public void faceTranslate() {
		GlStateManager.rotate(90F, 0F, 1F, 0F);
		GlStateManager.rotate(180F, 1F, 0F, 0F);
		GlStateManager.translate(-0.4F, 0.1F, -0.25F);
	}

	public void chestTranslate() {
		GlStateManager.rotate(180F, 1F, 0F, 0F);
		GlStateManager.translate(-0.5F, -0.7F, 0.15F);
	}

	public void scale(float f) {
		GlStateManager.scale(f, f, f);
	}

	public void renderIcon(int i) {
/*
		IIcon icon = icons[i];
		float f = icon.getMinU();
		float f1 = icon.getMaxU();
		float f2 = icon.getMinV();
		float f3 = icon.getMaxV();
		ItemRenderer.renderItemIn2D(Tessellator.getInstance(), f1, f2, f, f3, icon.getIconWidth(), icon.getIconHeight(), 1F / 16F);
*/
	}
}
