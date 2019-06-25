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
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.ComparatorBlock;
import net.minecraft.block.RedstoneWireBlock;
import net.minecraft.block.RepeaterBlock;
import net.minecraft.client.Minecraft;
import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Items;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.state.properties.ComparatorMode;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import vazkii.botania.api.item.AccessoryRenderHelper;
import vazkii.botania.api.item.IBurstViewerBauble;
import vazkii.botania.api.item.ICosmeticAttachable;
import vazkii.botania.api.item.ICosmeticBauble;
import vazkii.botania.common.core.handler.EquipmentHandler;
import vazkii.botania.common.item.ModItems;

public class ItemMonocle extends ItemBauble implements IBurstViewerBauble, ICosmeticBauble {

	public ItemMonocle(Properties props) {
		super(props);
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void doRender(ItemStack stack, EntityLivingBase player, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
		boolean armor = !player.getItemStackFromSlot(EntityEquipmentSlot.HEAD).isEmpty();
		Minecraft.getInstance().textureManager.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);

		AccessoryRenderHelper.translateToHeadLevel(player);
		AccessoryRenderHelper.translateToFace();
		AccessoryRenderHelper.defaultTransforms();
		GlStateManager.rotatef(180F, 0F, 1F, 0F);
		GlStateManager.scalef(0.5F, 0.5F, 0.5F);
		GlStateManager.translatef(0.5F, -0.2F, armor ? 0.12F : 0F);
		Minecraft.getInstance().getItemRenderer().renderItem(stack, ItemCameraTransforms.TransformType.NONE);
	}

	@OnlyIn(Dist.CLIENT)
	public static void renderHUD(PlayerEntity player) {
		Minecraft mc = Minecraft.getInstance();
		RayTraceResult pos = mc.objectMouseOver;
		if(pos == null || pos.getBlockPos() == null)
			return;
		BlockState state = player.world.getBlockState(pos.getBlockPos());
		Block block = state.getBlock();
		player.world.getTileEntity(pos.getBlockPos());

		ItemStack dispStack = ItemStack.EMPTY;
		String text = "";

		if(block == Blocks.REDSTONE_WIRE) {
			dispStack = new ItemStack(Items.REDSTONE);
			text = TextFormatting.RED + "" + state.get(RedstoneWireBlock.POWER);
		} else if(block == Blocks.REPEATER) {
			dispStack = new ItemStack(Blocks.REPEATER);
			text = "" + state.get(RepeaterBlock.DELAY);
		} else if(block == Blocks.COMPARATOR) {
			dispStack = new ItemStack(Blocks.COMPARATOR);
			text = state.get(ComparatorBlock.MODE) == ComparatorMode.SUBTRACT ? "-" : "+";
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
		ItemStack stack = EquipmentHandler.findOrEmpty(ModItems.monocle, player);
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

		return false;
	}

}
