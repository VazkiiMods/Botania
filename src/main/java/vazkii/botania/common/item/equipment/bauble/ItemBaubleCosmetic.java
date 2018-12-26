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
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;
import vazkii.botania.api.item.ICosmeticBauble;
import vazkii.botania.client.core.handler.ModelHandler;
import vazkii.botania.common.lib.LibItemNames;

import javax.annotation.Nonnull;
import java.util.List;

public class ItemBaubleCosmetic extends ItemBauble implements ICosmeticBauble {

	public enum Variants {
		BLACK_BOWTIE, BLACK_TIE, RED_GLASSES, PUFFY_SCARF,
		ENGINEER_GOGGLES, EYEPATCH, WICKED_EYEPATCH, RED_RIBBONS,
		PINK_FLOWER_BUD, POLKA_DOTTED_BOWS, BLUE_BUTTERFLY, CAT_EARS,
		WITCH_PIN, DEVIL_TAIL, KAMUI_EYE, GOOGLY_EYES,
		FOUR_LEAF_CLOVER, CLOCK_EYE, UNICORN_HORN, DEVIL_HORNS,
		HYPER_PLUS, BOTANIST_EMBLEM, ANCIENT_MASK, EERIE_MASK,
		ALIEN_ANTENNA, ANAGLYPH_GLASSES, ORANGE_SHADES, GROUCHO_GLASSES,
		THICK_EYEBROWS, LUSITANIC_SHIELD, TINY_POTATO_MASK, QUESTGIVER_MARK,
		THINKING_HAND
	}

	public static final int SUBTYPES = Variants.values().length;
	private ItemStack renderStack;

	public ItemBaubleCosmetic() {
		super(LibItemNames.COSMETIC);
		setHasSubtypes(true);
		renderStack = new ItemStack(this);
	}

	@Override
	public void getSubItems(@Nonnull CreativeTabs tab, @Nonnull NonNullList<ItemStack> list) {
		if(isInCreativeTab(tab)) {
			for(int i = 0; i < SUBTYPES; i++)
				list.add(new ItemStack(this, 1, i));
		}
	}

	@Nonnull
	@Override
	public String getTranslationKey(ItemStack stack) {
		return super.getTranslationKey(stack) + stack.getItemDamage();
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void addHiddenTooltip(ItemStack stack, World world, List<String> stacks, ITooltipFlag flags) {
		addStringToTooltip(I18n.format(stack.getItemDamage() == 32 ? "botaniamisc.cosmeticThinking" : "botaniamisc.cosmeticBauble"), stacks);
		super.addHiddenTooltip(stack, world, stacks, flags);
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void registerModels() {
		ModelHandler.registerItemAppendMeta(this, SUBTYPES, LibItemNames.COSMETIC);
	}

	@Override
	public BaubleType getBaubleType(ItemStack arg0) {
		return BaubleType.TRINKET;
	}

	@Override
	public void onPlayerBaubleRender(ItemStack stack, EntityPlayer player, RenderType type, float partialTicks) {
		Minecraft.getMinecraft().renderEngine.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);

		renderStack = stack;

		if (stack.getItemDamage() >= SUBTYPES || stack.getItemDamage() < 0) return;
		Variants variant = Variants.values()[stack.getItemDamage()];

		if (type == RenderType.HEAD) {
			Helper.translateToHeadLevel(player);
			Helper.translateToFace();
			Helper.defaultTransforms();
			switch (variant) {
			case RED_GLASSES:
				GlStateManager.scale(1.25, 1.25, 1.25);
				GlStateManager.translate(0F, -0.085F, 0.045F);
				renderItem();
				break;
			case ENGINEER_GOGGLES:
				GlStateManager.scale(1.25, 1.25, 1.25);
				GlStateManager.translate(0F, -0.085F, 0.045F);
				renderItem();
				break;
			case EYEPATCH:
				scale(0.55F);
				GlStateManager.translate(-0.45F, -0.25F, 0F);
				renderItem();
				break;
			case WICKED_EYEPATCH:
				scale(0.55F);
				GlStateManager.translate(0.45F, -0.25F, 0F);
				renderItem();
				break;
			case RED_RIBBONS:
				scale(0.9F);
				GlStateManager.translate(0F, 0.75F, 1F);
				renderItem();
				break;
			case PINK_FLOWER_BUD:
				GlStateManager.rotate(-90F, 0F, 1F, 0F);
				GlStateManager.translate(0.4F, 0.6F, 0.45F);
				renderItem();
				break;
			case POLKA_DOTTED_BOWS:
				GlStateManager.rotate(-90F, 0F, 1F, 0F);
				GlStateManager.translate(0.65F, 0.3F, 0.5F);
				renderItem();
				GlStateManager.translate(0F, 0F, -1F);
				renderItem();
				break;
			case BLUE_BUTTERFLY:
				GlStateManager.translate(-0.75F, 0.1F, 1F);
				GlStateManager.pushMatrix();
				GlStateManager.rotate(45F, 0F, 1F, 0F);
				renderItem();
				GlStateManager.popMatrix();

				GlStateManager.translate(0F, 0F, -0.75F);
				GlStateManager.rotate(-45F, 0F, 1F, 0F);
				renderItem();
				break;
			case CAT_EARS:
				GlStateManager.translate(0F, 0.25F, 0.25F);
				renderItem();
				break;
			case GOOGLY_EYES:
				GlStateManager.rotate(180F, 0F, 1F, 0F);
				GlStateManager.scale(1.5F, 1.5F, 1F);
				GlStateManager.translate(0F, -0.05F, 0F);
				renderItem();
				break;
			case CLOCK_EYE:
				scale(0.75F);
				GlStateManager.translate(-0.25F, -0.1F, 0F);
				GlStateManager.rotate(180F, 0F, 0F, 1F);
				renderItem();
				break;
			case UNICORN_HORN:
				scale(1.25F);
				GlStateManager.rotate(-90F, 0F, 1F, 0F);
				GlStateManager.translate(0F, 0.4F, 0F);
				renderItem();
				break;
			case DEVIL_HORNS:
				GlStateManager.translate(0F, 0.2F, 0.25F);
				renderItem();
				break;
			case HYPER_PLUS:
				scale(0.35F);
				GlStateManager.translate(-0.7F, 1F, -0.5F);
				renderItem();
				GlStateManager.translate(1.45F, 0F, 0F);
				renderItem();
				break;
			case ANCIENT_MASK:
				scale(1.25F);
				GlStateManager.translate(0F, 0.025F, 0.01F);
				renderItem();
				break;
			case EERIE_MASK:
				renderItem();
				break;
			case ALIEN_ANTENNA:
				scale(0.9F);
				GlStateManager.rotate(180F, 0F, 1F, 0F);
				GlStateManager.translate(0F, 0.75F, -1F);
				renderItem();
				break;
			case ANAGLYPH_GLASSES:
				scale(1.25F);
				GlStateManager.translate(0F, -0.025F, 0F);
				renderItem();
				break;
			case ORANGE_SHADES:
				scale(1.25f);
				GlStateManager.translate(0F, 0.04F, 0F);
				GlStateManager.enableBlend();
				GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
				GlStateManager.color(1F, 1F, 1F, 0.7F);
				renderItem();
				break;
			case GROUCHO_GLASSES:
				scale(1.5F);
				GlStateManager.translate(0F, -0.2125F, 0F);
				renderItem();
				break;
			case THICK_EYEBROWS:
				scale(0.5F);
				GlStateManager.translate(-0.4F, 0.05F, 0F);
				renderItem();
				GlStateManager.rotate(180F, 0F, 1F, 0F);
				GlStateManager.translate(-0.775F, 0F, 0F);
				renderItem();
				break;
			case TINY_POTATO_MASK:
				scale(1.25F);
				GlStateManager.translate(0F, 0.025F, 0F);
				renderItem();
				break;
			case QUESTGIVER_MARK:
				scale(0.8F);
				GlStateManager.translate(0F, 1F, 0.3F);
				renderItem();
				break;
			case THINKING_HAND:
				scale(0.9f);
				GlStateManager.translate(0.2F, -0.5F, 0F);
				GlStateManager.scale(-1, 1, 1);
				GlStateManager.rotate(15F, 0F, 0F, 1F);
				renderItem();
				break;
			default: break;
			}
		} else {
			Helper.rotateIfSneaking(player);
			Helper.translateToChest();
			Helper.defaultTransforms();
			switch (variant) {
			case BLACK_BOWTIE:
				GlStateManager.translate(0F, 0.15F, 0F);
				renderItem();
				break;
			case BLACK_TIE:
				GlStateManager.translate(0F, -0.15F, 0F);
				renderItem();
				break;
			case PUFFY_SCARF:
				GlStateManager.translate(0F, -0.15F, 0F);
				renderItem();
				break;
			case WITCH_PIN:
				scale(0.35F);
				GlStateManager.translate(-0.35F, 0.35F, 0.15F);
				renderItem();
				break;
			case DEVIL_TAIL:
				GlStateManager.rotate(90F, 0F, 1F, 0F);
				GlStateManager.translate(0.5F, -0.75F, 0F);
				renderItem();
				break;
			case KAMUI_EYE: // DON'T LOSE YOUR WAAAAAAAAY
				scale(0.9F);
				GlStateManager.translate(0.9F, 0.35F, 0F);
				renderItem();
				GlStateManager.translate(-1.3F, -0.5F, 0.5F);
				GlStateManager.rotate(180F, 0F, 0F, 1F);
				GlStateManager.rotate(180F, 1F, 0F, 0F);
				renderKamuiBlack();
				break;
			case FOUR_LEAF_CLOVER:
				scale(0.5F);
				GlStateManager.translate(0.35F, 0.3F, -0.075F);
				renderItem();
				break;
			case BOTANIST_EMBLEM:
				scale(0.5F);
				GlStateManager.translate(0F, -0.75F, 0F);
				renderItem();
				break;
			case LUSITANIC_SHIELD:
				GlStateManager.rotate(180F, 0F, 1F, 0F);
				GlStateManager.translate(0.035F, -0.2F, 0.55F);
				GlStateManager.rotate(8F, 0F, 0F, 1F);
				renderItem();
				break;
			default: break;
			}
		}
	}

	public void scale(float f) {
		GlStateManager.scale(f, f, f);
	}

	public void renderItem() {
		GlStateManager.pushMatrix();
		Minecraft.getMinecraft().getRenderItem().renderItem(renderStack, ItemCameraTransforms.TransformType.NONE);
		GlStateManager.popMatrix();
	}

	public void renderKamuiBlack() {

		// Modified copy of RenderItem.renderItem(stack, transformtype)
		Minecraft.getMinecraft().renderEngine.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
		Minecraft.getMinecraft().renderEngine.getTexture(TextureMap.LOCATION_BLOCKS_TEXTURE).setBlurMipmap(false, false);

		IBakedModel model = Minecraft.getMinecraft().getRenderItem().getItemModelMesher().getItemModel(renderStack);

		GlStateManager.enableRescaleNormal();
		GlStateManager.alphaFunc(516, 0.1F);
		GlStateManager.enableBlend();
		GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
		GlStateManager.pushMatrix();
		model = net.minecraftforge.client.ForgeHooksClient.handleCameraTransforms(model, ItemCameraTransforms.TransformType.NONE, false);

		renderModel(model, renderStack, 0xFF00004C);

		GlStateManager.cullFace(GlStateManager.CullFace.BACK);
		GlStateManager.popMatrix();
		GlStateManager.disableRescaleNormal();
		GlStateManager.disableBlend();
		Minecraft.getMinecraft().renderEngine.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
		Minecraft.getMinecraft().renderEngine.getTexture(TextureMap.LOCATION_BLOCKS_TEXTURE).restoreLastBlurMipmap();
	}

	// Adapted from RenderItem.renderModel(model, stack), added extra color param
	private void renderModel(IBakedModel model, ItemStack stack, int color) {
		Tessellator tessellator = Tessellator.getInstance();
		BufferBuilder worldrenderer = tessellator.getBuffer();
		worldrenderer.begin(7, DefaultVertexFormats.ITEM);

		for (EnumFacing enumfacing : EnumFacing.values())
		{
			renderQuads(worldrenderer, model.getQuads(null, enumfacing, 0), color, stack);
		}

		renderQuads(worldrenderer, model.getQuads(null, null, 0), color, stack);
		tessellator.draw();
	}

	// Copy of RenderItem.renderQuads
	private void renderQuads(BufferBuilder renderer, List<BakedQuad> quads, int color, ItemStack stack)
	{
		boolean flag = color == -1 && !stack.isEmpty();
		int i = 0;

		for (int j = quads.size(); i < j; ++i)
		{
			BakedQuad bakedquad = quads.get(i);
			int k = color;

			if (flag && bakedquad.hasTintIndex())
			{
				k = Minecraft.getMinecraft().getItemColors().colorMultiplier(stack, bakedquad.getTintIndex());

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
