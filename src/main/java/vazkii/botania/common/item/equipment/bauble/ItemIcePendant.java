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
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.TexturedRenderLayers;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.enchantment.FrostWalkerEnchantment;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.BlockStateParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import vazkii.botania.client.core.handler.MiscellaneousIcons;

public class ItemIcePendant extends ItemBauble {

	public ItemIcePendant(Settings props) {
		super(props);
	}

	@Override
	public void onWornTick(ItemStack stack, LivingEntity entity) {
		if (!entity.world.isClient && !entity.isSneaking()) {
			boolean lastOnGround = entity.isOnGround();
			entity.setOnGround(true);
			FrostWalkerEnchantment.freezeWater(entity, entity.world, entity.getBlockPos(), 8);
			entity.setOnGround(lastOnGround);

			int x = MathHelper.floor(entity.getX());
			int y = MathHelper.floor(entity.getY());
			int z = MathHelper.floor(entity.getZ());
			BlockState blockstate = Blocks.SNOW.getDefaultState();

			for (int l = 0; l < 4; ++l) {
				x = MathHelper.floor(entity.getX() + (double) ((float) (l % 2 * 2 - 1) * 0.25F));
				z = MathHelper.floor(entity.getZ() + (double) ((float) (l / 2 % 2 * 2 - 1) * 0.25F));
				BlockPos blockpos = new BlockPos(x, y, z);
				if (entity.world.isAir(blockpos) && entity.world.getBiome(blockpos).getTemperature(blockpos) < 0.9F && blockstate.canPlaceAt(entity.world, blockpos)) {
					entity.world.setBlockState(blockpos, blockstate);
				}
			}
		} else if (entity.world.isClient && !entity.isSneaking()) {
			if (entity.world.random.nextFloat() >= 0.25F) {
				entity.world.addParticle(new BlockStateParticleEffect(ParticleTypes.FALLING_DUST, Blocks.SNOW_BLOCK.getDefaultState()), entity.getX() + entity.world.random.nextFloat() * 0.6 - 0.3, entity.getY() + 1.1, entity.getZ() + entity.world.random.nextFloat() * 0.6 - 0.3, 0, -0.15, 0);
			}
		}
	}

	@Override
	@Environment(EnvType.CLIENT)
	public void doRender(BipedEntityModel<?> bipedModel, ItemStack stack, LivingEntity player, MatrixStack ms, VertexConsumerProvider buffers, int light, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
		boolean armor = !player.getEquippedStack(EquipmentSlot.CHEST).isEmpty();
		bipedModel.torso.rotate(ms);
		ms.translate(-0.25, 0.5, armor ? 0.05 : 0.12);
		ms.scale(0.5F, -0.5F, -0.5F);

		BakedModel model = MiscellaneousIcons.INSTANCE.snowflakePendantGem;
		VertexConsumer buffer = buffers.getBuffer(TexturedRenderLayers.getEntityCutout());
		MinecraftClient.getInstance().getBlockRenderManager().getModelRenderer()
				.render(ms.peek(), buffer, null, model, 1, 1, 1, light, OverlayTexture.DEFAULT_UV);
	}

}
