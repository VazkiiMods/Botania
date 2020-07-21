/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.item.equipment.bauble;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.ComparatorBlock;
import net.minecraft.block.RedstoneWireBlock;
import net.minecraft.block.RepeaterBlock;
import net.minecraft.block.enums.ComparatorMode;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Formatting;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import vazkii.botania.api.item.ICosmeticAttachable;
import vazkii.botania.api.item.ICosmeticBauble;
import vazkii.botania.common.core.handler.EquipmentHandler;
import vazkii.botania.common.lib.ModTags;

public class ItemMonocle extends ItemBauble implements ICosmeticBauble {

	public ItemMonocle(Settings props) {
		super(props);
	}

	@Override
	@Environment(EnvType.CLIENT)
	public void doRender(BipedEntityModel<?> bipedModel, ItemStack stack, LivingEntity player, MatrixStack ms, VertexConsumerProvider buffers, int light, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
		bipedModel.head.rotate(ms);
		ms.translate(0.15, -0.2, -0.25);
		ms.scale(0.3F, -0.3F, -0.3F);
		MinecraftClient.getInstance().getItemRenderer().renderItem(stack, ModelTransformation.Mode.NONE, light, OverlayTexture.DEFAULT_UV, ms, buffers);
	}

	@Environment(EnvType.CLIENT)
	public static void renderHUD(MatrixStack ms, PlayerEntity player) {
		MinecraftClient mc = MinecraftClient.getInstance();
		HitResult ray = mc.crosshairTarget;
		if (ray == null || ray.getType() != HitResult.Type.BLOCK) {
			return;
		}
		BlockPos pos = ((BlockHitResult) ray).getBlockPos();
		BlockState state = player.world.getBlockState(pos);
		Block block = state.getBlock();
		player.world.getBlockEntity(pos);

		ItemStack dispStack = ItemStack.EMPTY;
		String text = "";

		if (block == Blocks.REDSTONE_WIRE) {
			dispStack = new ItemStack(Items.REDSTONE);
			text = Formatting.RED + "" + state.get(RedstoneWireBlock.POWER);
		} else if (block == Blocks.REPEATER) {
			dispStack = new ItemStack(Blocks.REPEATER);
			text = "" + state.get(RepeaterBlock.DELAY);
		} else if (block == Blocks.COMPARATOR) {
			dispStack = new ItemStack(Blocks.COMPARATOR);
			text = state.get(ComparatorBlock.MODE) == ComparatorMode.SUBTRACT ? "-" : "+";
		}

		if (dispStack.isEmpty()) {
			return;
		}

		int x = mc.getWindow().getScaledWidth() / 2 + 15;
		int y = mc.getWindow().getScaledHeight() / 2 - 8;

		mc.getItemRenderer().renderInGuiWithOverrides(dispStack, x, y);

		mc.textRenderer.drawWithShadow(ms, text, x + 20, y + 4, 0xFFFFFF);
	}

	public static boolean hasMonocle(LivingEntity living) {
		return !EquipmentHandler.findOrEmpty(stack -> {
			if (!stack.isEmpty()) {
				Item item = stack.getItem();
				if (item.isIn(ModTags.Items.BURST_VIEWERS)) {
					return true;
				}
				if (item instanceof ICosmeticAttachable) {
					ICosmeticAttachable attach = (ICosmeticAttachable) item;
					ItemStack cosmetic = attach.getCosmeticItem(stack);
					return !cosmetic.isEmpty() && cosmetic.getItem().isIn(ModTags.Items.BURST_VIEWERS);
				}
			}
			return false;
		}, living).isEmpty();
	}

}
