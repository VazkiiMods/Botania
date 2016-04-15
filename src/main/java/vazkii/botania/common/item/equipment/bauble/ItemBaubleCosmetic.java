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

import baubles.api.BaubleType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.text.translation.I18n;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.RecipeSorter;
import net.minecraftforge.oredict.RecipeSorter.Category;
import org.lwjgl.opengl.GL11;
import vazkii.botania.api.item.ICosmeticBauble;
import vazkii.botania.common.crafting.recipe.CosmeticAttachRecipe;
import vazkii.botania.common.crafting.recipe.CosmeticRemoveRecipe;
import vazkii.botania.common.lib.LibItemNames;

import java.util.List;

public class ItemBaubleCosmetic extends ItemBauble implements ICosmeticBauble {

	private static final int SUBTYPES = 32;
	private final ItemStack renderStack;

	public ItemBaubleCosmetic() {
		super(LibItemNames.COSMETIC);
		setHasSubtypes(true);

		GameRegistry.addRecipe(new CosmeticAttachRecipe());
		GameRegistry.addRecipe(new CosmeticRemoveRecipe());
		RecipeSorter.register("botania:cosmeticAttach", CosmeticAttachRecipe.class, Category.SHAPELESS, "");
		RecipeSorter.register("botania:cosmeticRemove", CosmeticRemoveRecipe.class, Category.SHAPELESS, "");
		renderStack = new ItemStack(this);
	}

	@Override
	public void getSubItems(Item item, CreativeTabs tab, List<ItemStack> list) {
		for(int i = 0; i < SUBTYPES; i++)
			list.add(new ItemStack(item, 1, i));
	}

	@Override
	public String getUnlocalizedName(ItemStack par1ItemStack) {
		return super.getUnlocalizedName(par1ItemStack) + par1ItemStack.getItemDamage();
	}

	@Override
	public void addHiddenTooltip(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, List<String> par3List, boolean par4) {
		addStringToTooltip(I18n.translateToLocal("botaniamisc.cosmeticBauble"), par3List);
		super.addHiddenTooltip(par1ItemStack, par2EntityPlayer, par3List, par4);
	}

	@Override
	public BaubleType getBaubleType(ItemStack arg0) {
		return BaubleType.AMULET;
	}

	@Override
	public void onPlayerBaubleRender(ItemStack stack, EntityPlayer player, RenderType type, float partialTicks) {
		Minecraft.getMinecraft().renderEngine.bindTexture(TextureMap.locationBlocksTexture);
		if(type == RenderType.HEAD) {
			Helper.translateToHeadLevel(player);
			switch(stack.getItemDamage()) {
			case 2:
				faceTranslate();
				GlStateManager.translate(0F, 0F, 0.045F);
				renderIcon(2);
				break;
			case 4:
				faceTranslate();
				GlStateManager.translate(0F, 0F, 0.045F);
				renderIcon(4);
				break;
			case 5:
				faceTranslate();
				scale(0.35F);
				GlStateManager.translate(-0.3F, 0.2F, -0.5F);
				renderIcon(5);
				break;
			case 6:
				faceTranslate();
				scale(0.45F);
				GlStateManager.translate(0.2F, 0.2F, -0.4F);
				renderIcon(6);
				break;
			case 7:
				faceTranslate();
				scale(0.9F);
				GlStateManager.translate(0F, -0.4F, -0.42F);
				renderIcon(7);
				break;
			case 8:
				faceTranslate();
				GlStateManager.rotate(-90F, 0F, 0F, 1F);
				GlStateManager.translate(0F, 0.1F, -0.2F);
				renderIcon(8);
				break;
			case 9:
				faceTranslate();
				GlStateManager.rotate(-90F, 0F, 0F, 1F);
				GlStateManager.translate(0F, 0.1F, -0.1F);
				renderIcon(9);
				GlStateManager.translate(0F, -0.45F, 0F);
				renderIcon(9);
				break;
			case 10:
				faceTranslate();
				GlStateManager.rotate(-90F, 0F, 0F, 1F);
				scale(0.6F);
				GlStateManager.translate(0F, 0.25F, -0.4F);

				GlStateManager.pushMatrix();
				GlStateManager.translate(-0.2F, 0.15F, 0F);
				GlStateManager.rotate(-45F, 0F, 0F, 1F);
				renderIcon(10);
				GlStateManager.popMatrix();

				GlStateManager.translate(0.15F, 0.3F, 0F);
				GlStateManager.rotate(-135F, 0F, 0F, 1F);
				renderIcon(10);
				break;
			case 11:
				faceTranslate();
				scale(0.6F);
				GlStateManager.translate(0F, 0F, -0.55F);
				renderIcon(11);
				break;
			case 15:
				faceTranslate();
				GlStateManager.translate(-0.01F, 0F, 0.05F);
				renderIcon(15);
				break;
			case 17:
				faceTranslate();
				scale(0.35F);
				GlStateManager.translate(0.25F, 0.2F, -0.60F);
				renderIcon(17);
				break;
			case 18:
				faceTranslate();
				scale(0.75F);
				GlStateManager.rotate(-90F, 0F, 0F, 1F);
				GlStateManager.translate(-0.2F, -0.15F, -0.5F);
				renderIcon(18);
				break;
			case 19:
				faceTranslate();
				scale(0.6F);
				GlStateManager.translate(0F, 0F, -0.5F);
				renderIcon(19);
				break;
			case 20:
				faceTranslate();
				scale(0.25F);
				GlStateManager.translate(-0.7F, 0.3F, -1.5F);
				renderIcon(20);
				GlStateManager.translate(1.4F, 0F, 0F);
				renderIcon(20);
				break;
			case 22:
				faceTranslate();
				renderIcon(22);
				break;
			case 23:
				faceTranslate();
				renderIcon(23);
				break;
			case 24:
				faceTranslate();
				scale(0.8F);
				GlStateManager.translate(0F, -0.1F, -0.5F);
				renderIcon(24);
				break;
			case 25:
				faceTranslate();
				GlStateManager.translate(0F, 0F, 0.04F);
				renderIcon(25);
				break;
			case 26:
				faceTranslate();
				GlStateManager.enableBlend();
				GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
				GlStateManager.color(1F, 1F, 1F, 0.7F);
				renderIcon(26);
				break;
			case 27:
				faceTranslate();
				scale(0.90F);
				GlStateManager.translate(0F, 0F, 0.09F);
				renderIcon(27);
				break;
			case 28:
				faceTranslate();
				scale(0.25F);
				GlStateManager.translate(-0.4F, 0.4F, -1.1F);
				renderIcon(28);
				GlStateManager.rotate(180F, 0F, 0F, 1F);
				GlStateManager.translate(-0.775F, -0.4F, 0.04F);
				renderIcon(28);
				break;
			case 30:
				faceTranslate();
				renderIcon(30);
				break;
			case 31:
				faceTranslate();
				scale(0.8F);
				GlStateManager.translate(0F, -0.3F, -0.75F);
				renderIcon(31);
				break;
			}
		} else {
			Helper.rotateIfSneaking(player);
			switch(stack.getItemDamage()) {
			case 0:
				chestTranslate();
				renderIcon(0);
				break;
			case 1:
				chestTranslate();
				GlStateManager.translate(0F, 0F, 0.2F);
				renderIcon(1);
				break;
			case 3:
				chestTranslate();
				GlStateManager.translate(0F, 0F, 0.15F);
				renderIcon(3);
				break;
			case 12:
				chestTranslate();
				scale(0.225F);
				GlStateManager.translate(-0.725F, 0.45F, -0.6F);
				renderIcon(12);
				break;
			case 13:
				chestTranslate();
				GlStateManager.rotate(-90F, 0F, 0F, 1F);
				GlStateManager.translate(0.33F, -0.15F, 0.55F);
				renderIcon(13);
				break;
			case 14:
				chestTranslate();
				scale(0.9F);
				GlStateManager.translate(0.45F, 0.05F, -0.1F);
				renderIcon(14);
				GlStateManager.rotate(180F, 0F, 0F, 1F);
				GlStateManager.translate(0.9F, -0.25F, 0F);
				renderKamuiBlack();
				break;
			case 16:
				chestTranslate();
				scale(0.225F);
				GlStateManager.translate(0.725F, 0.45F, -0.6F);
				renderIcon(16);
				break;
			case 21:
				chestTranslate();
				scale(0.5F);
				GlStateManager.translate(0F, 0.15F, 0.32F);
				renderIcon(21);
				break;
			case 29:
				chestTranslate();
				GlStateManager.translate(0.05F, -0.25F, 0.25F);
				GlStateManager.rotate(8F, 0F, 1F, 0F);
				renderIcon(29);
				break;
			}
		}
	}

	public void faceTranslate() {
		GlStateManager.rotate(-90F, 0F, 1F, 0F);
		GlStateManager.rotate(-90F, 1F, 0F, 0F);
		GlStateManager.scale(1.25F, 1.25F, 1.25F);
		GlStateManager.translate(0F, 0.10F, 1.45F);
	}

	public void chestTranslate() {
		GlStateManager.translate(0F, 0.5F, 0F);
		GlStateManager.rotate(-90F, 1F, 0F, 0F);
	}

	public void scale(float f) {
		GlStateManager.scale(f, f, f);
	}

	@SuppressWarnings("deprecation")
	public void renderIcon(int i) {
		GlStateManager.pushMatrix();
		renderStack.setItemDamage(i);
		Minecraft.getMinecraft().getRenderItem().renderItem(renderStack, ItemCameraTransforms.TransformType.THIRD_PERSON_RIGHT_HAND); // todo 1.9
		GlStateManager.popMatrix();
	}

	public void renderKamuiBlack() {
		renderStack.setItemDamage(14);

		// Modified copy of RenderItem.renderItem(stack, transformtype)
		Minecraft.getMinecraft().renderEngine.bindTexture(TextureMap.locationBlocksTexture);
		Minecraft.getMinecraft().renderEngine.getTexture(TextureMap.locationBlocksTexture).setBlurMipmap(false, false);

		IBakedModel model = Minecraft.getMinecraft().getRenderItem().getItemModelMesher().getItemModel(renderStack);

		GlStateManager.enableRescaleNormal();
		GlStateManager.alphaFunc(516, 0.1F);
		GlStateManager.enableBlend();
		GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
		GlStateManager.pushMatrix();
		model = net.minecraftforge.client.ForgeHooksClient.handleCameraTransforms(model, ItemCameraTransforms.TransformType.THIRD_PERSON_RIGHT_HAND, false); // todo 1.9

		GlStateManager.translate(-0.5F, -0.1F, -0.4F);
		this.renderModel(model, renderStack, 0xFF00004C);

		GlStateManager.cullFace(GlStateManager.CullFace.BACK);
		GlStateManager.popMatrix();
		GlStateManager.disableRescaleNormal();
		GlStateManager.disableBlend();
		Minecraft.getMinecraft().renderEngine.bindTexture(TextureMap.locationBlocksTexture);
		Minecraft.getMinecraft().renderEngine.getTexture(TextureMap.locationBlocksTexture).restoreLastBlurMipmap();
		//
	}

	// Adapted from RenderItem.renderModel(model, stack), added extra color param
	private void renderModel(IBakedModel model, ItemStack stack, int color) {
		Tessellator tessellator = Tessellator.getInstance();
		VertexBuffer worldrenderer = tessellator.getBuffer();
		worldrenderer.begin(7, DefaultVertexFormats.ITEM);

		for (EnumFacing enumfacing : EnumFacing.values())
		{
			this.renderQuads(worldrenderer, model.getQuads(null, enumfacing, 0), color, stack);
		}

		this.renderQuads(worldrenderer, model.getQuads(null, null, 0), color, stack);
		tessellator.draw();
	}

	// Copy of RenderItem.renderQuads
	private void renderQuads(VertexBuffer renderer, List<BakedQuad> quads, int color, ItemStack stack)
	{
		boolean flag = color == -1 && stack != null;
		int i = 0;

		for (int j = quads.size(); i < j; ++i)
		{
			BakedQuad bakedquad = quads.get(i);
			int k = color;

			if (flag && bakedquad.hasTintIndex())
			{
				k = Minecraft.getMinecraft().getItemColors().getColorFromItemstack(stack, bakedquad.getTintIndex());

				if (EntityRenderer.anaglyphEnable)
				{
					k = TextureUtil.anaglyphColor(k);
				}

				k = k | -16777216;
			}

			net.minecraftforge.client.model.pipeline.LightUtil.renderQuadColor(renderer, bakedquad, k);
		}
	}

}
