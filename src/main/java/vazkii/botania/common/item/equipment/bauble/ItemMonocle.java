/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * File Created @ [Jan 24, 2015, 3:03:18 PM (GMT)]
 */
package vazkii.botania.common.item.equipment.bauble;

import net.minecraft.block.Block;
import net.minecraft.block.BlockRedstoneComparator;
import net.minecraft.block.BlockRedstoneRepeater;
import net.minecraft.block.BlockRedstoneWire;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.state.properties.ComparatorMode;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import top.theillusivec4.curios.api.CuriosAPI;
import vazkii.botania.api.item.IBaubleRender;
import vazkii.botania.api.item.IBurstViewerBauble;
import vazkii.botania.api.item.ICosmeticAttachable;
import vazkii.botania.api.item.ICosmeticBauble;
import vazkii.botania.common.integration.curios.BaseCurio;
import vazkii.botania.common.item.ModItems;
import vazkii.botania.common.lib.LibItemNames;

public class ItemMonocle extends ItemBauble implements IBurstViewerBauble, ICosmeticBauble {

	public ItemMonocle(Properties props) {
		super(props);
	}

	public static class Curio extends BaseCurio {
		public Curio(ItemStack stack) {
			super(stack);
		}

		@Override
		public boolean hasRender(String identifier, EntityLivingBase living) {
			return true;
		}

		@Override
		public void doRender(String identifier, EntityLivingBase player, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
			boolean armor = !player.getItemStackFromSlot(EntityEquipmentSlot.HEAD).isEmpty();
			Minecraft.getInstance().textureManager.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);

			IBaubleRender.Helper.translateToHeadLevel(player);
			IBaubleRender.Helper.translateToFace();
			IBaubleRender.Helper.defaultTransforms();
			GlStateManager.rotatef(180F, 0F, 1F, 0F);
			GlStateManager.scalef(0.5F, 0.5F, 0.5F);
			GlStateManager.translatef(0.5F, -0.2F, armor ? 0.12F : 0F);
			Minecraft.getInstance().getItemRenderer().renderItem(stack, ItemCameraTransforms.TransformType.NONE);
		}
	}

	@OnlyIn(Dist.CLIENT)
	public static void renderHUD(EntityPlayer player) {
		Minecraft mc = Minecraft.getInstance();
		RayTraceResult pos = mc.objectMouseOver;
		if(pos == null || pos.getBlockPos() == null)
			return;
		IBlockState state = player.world.getBlockState(pos.getBlockPos());
		Block block = state.getBlock();
		player.world.getTileEntity(pos.getBlockPos());

		ItemStack dispStack = ItemStack.EMPTY;
		String text = "";

		if(block == Blocks.REDSTONE_WIRE) {
			dispStack = new ItemStack(Items.REDSTONE);
			text = TextFormatting.RED + "" + state.get(BlockRedstoneWire.POWER);
		} else if(block == Blocks.REPEATER) {
			dispStack = new ItemStack(Blocks.REPEATER);
			text = "" + state.get(BlockRedstoneRepeater.DELAY);
		} else if(block == Blocks.COMPARATOR) {
			dispStack = new ItemStack(Blocks.COMPARATOR);
			text = state.get(BlockRedstoneComparator.MODE) == ComparatorMode.SUBTRACT ? "-" : "+";
		}

		if(dispStack.isEmpty())
			return;

		int x = mc.mainWindow.getScaledWidth() / 2 + 15;
		int y = mc.mainWindow.getScaledHeight() / 2 - 8;

		net.minecraft.client.renderer.RenderHelper.enableGUIStandardItemLighting();
		mc.getItemRenderer().renderItemAndEffectIntoGUI(dispStack, x, y);
		net.minecraft.client.renderer.RenderHelper.disableStandardItemLighting();

		mc.fontRenderer.drawStringWithShadow(text, x + 20, y + 4, 0xFFFFFF);
	}

	public static boolean hasMonocle(EntityPlayer player) {
		CuriosAPI.FinderData result = CuriosAPI.getCurioEquipped(ModItems.monocle, player);
		if(result != null) {
			ItemStack stack = result.getStack();
			if(!stack.isEmpty()) {
				Item item = stack.getItem();
				if(item instanceof IBurstViewerBauble)
					return true;

				if(item instanceof ICosmeticAttachable) {
					ICosmeticAttachable attach = (ICosmeticAttachable) item;
					ItemStack cosmetic = attach.getCosmeticItem(stack);
					if(cosmetic != null && cosmetic.getItem() instanceof IBurstViewerBauble)
						return true;
				}
			}
		}

		return false;
	}

}
