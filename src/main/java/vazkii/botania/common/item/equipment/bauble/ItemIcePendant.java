/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.item.equipment.bauble;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Atlases;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.enchantment.FrostWalkerEnchantment;
import net.minecraft.entity.LivingEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.particles.BlockParticleData;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import vazkii.botania.client.core.handler.BaubleRenderHandler;
import vazkii.botania.client.core.handler.MiscellaneousIcons;

public class ItemIcePendant extends ItemBauble {

	public ItemIcePendant(Properties props) {
		super(props);
	}

	@Override
	public void onWornTick(ItemStack stack, LivingEntity entity) {
		if (!entity.world.isRemote && !entity.isSneaking()) {
			boolean lastOnGround = entity.func_233570_aj_();
			entity.func_230245_c_(true);
			FrostWalkerEnchantment.freezeNearby(entity, entity.world, entity.func_233580_cy_(), 8);
			entity.func_230245_c_(lastOnGround);

			int x = MathHelper.floor(entity.getPosX());
			int y = MathHelper.floor(entity.getPosY());
			int z = MathHelper.floor(entity.getPosZ());
			BlockState blockstate = Blocks.SNOW.getDefaultState();

			for (int l = 0; l < 4; ++l) {
				x = MathHelper.floor(entity.getPosX() + (double) ((float) (l % 2 * 2 - 1) * 0.25F));
				z = MathHelper.floor(entity.getPosZ() + (double) ((float) (l / 2 % 2 * 2 - 1) * 0.25F));
				BlockPos blockpos = new BlockPos(x, y, z);
				if (entity.world.isAirBlock(blockpos) && entity.world.getBiome(blockpos).getTemperature(blockpos) < 0.9F && blockstate.isValidPosition(entity.world, blockpos)) {
					entity.world.setBlockState(blockpos, blockstate);
				}
			}
		} else if (entity.world.isRemote && !entity.isSneaking()) {
			if (entity.world.rand.nextFloat() >= 0.25F) {
				entity.world.addParticle(new BlockParticleData(ParticleTypes.FALLING_DUST, Blocks.SNOW_BLOCK.getDefaultState()), entity.getPosX() + entity.world.rand.nextFloat() * 0.6 - 0.3, entity.getPosY() + 1.1, entity.getPosZ() + entity.world.rand.nextFloat() * 0.6 - 0.3, 0, -0.15, 0);
			}
		}
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void doRender(BaubleRenderHandler layer, ItemStack stack, LivingEntity player, MatrixStack ms, IRenderTypeBuffer buffers, int light, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
		boolean armor = !player.getItemStackFromSlot(EquipmentSlotType.CHEST).isEmpty();
		layer.getEntityModel().bipedBody.translateRotate(ms);
		ms.translate(-0.25, 0.5, armor ? 0.05 : 0.12);
		ms.scale(0.5F, -0.5F, -0.5F);

		IBakedModel model = MiscellaneousIcons.INSTANCE.snowflakePendantGem;
		IVertexBuilder buffer = buffers.getBuffer(Atlases.getCutoutBlockType());
		Minecraft.getInstance().getBlockRendererDispatcher().getBlockModelRenderer()
				.renderModelBrightnessColor(ms.getLast(), buffer, null, model, 1, 1, 1, light, OverlayTexture.NO_OVERLAY);
	}

}
