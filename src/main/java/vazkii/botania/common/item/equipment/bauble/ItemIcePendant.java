/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * File Created @ [Apr 26, 2014, 2:06:17 PM (GMT)]
 */
package vazkii.botania.common.item.equipment.bauble;

import com.mojang.blaze3d.platform.GlStateManager;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
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
import vazkii.botania.client.core.handler.MiscellaneousIcons;
import vazkii.botania.client.core.helper.IconHelper;

public class ItemIcePendant extends ItemBauble {

	public ItemIcePendant(Properties props) {
		super(props);
	}

	@Override
	public void onWornTick(ItemStack stack, LivingEntity entity) {
		if (!entity.world.isRemote && !entity.isSneaking()) {
			boolean lastOnGround = entity.onGround;
			entity.onGround = true;
			FrostWalkerEnchantment.freezeNearby(entity, entity.world, new BlockPos(entity), 8);
			entity.onGround = lastOnGround;

			int x = MathHelper.floor(entity.getX());
			int y = MathHelper.floor(entity.getY());
			int z = MathHelper.floor(entity.getZ());
			BlockState blockstate = Blocks.SNOW.getDefaultState();

			for(int l = 0; l < 4; ++l) {
				x = MathHelper.floor(entity.getX() + (double)((float)(l % 2 * 2 - 1) * 0.25F));
				z = MathHelper.floor(entity.getZ() + (double)((float)(l / 2 % 2 * 2 - 1) * 0.25F));
				BlockPos blockpos = new BlockPos(x, y, z);
				if (entity.world.isAirBlock(blockpos) && entity.world.getBiome(blockpos).func_225486_c(blockpos) < 0.9F && blockstate.isValidPosition(entity.world, blockpos)) {
					entity.world.setBlockState(blockpos, blockstate);
				}
			}
		}
		else if (entity.world.isRemote && !entity.isSneaking()) {
			if(entity.world.rand.nextFloat() >= 0.25F) {
				entity.world.addParticle(new BlockParticleData(ParticleTypes.FALLING_DUST, Blocks.SNOW_BLOCK.getDefaultState()), entity.getX() + entity.world.rand.nextFloat() * 0.6 - 0.3, entity.getY() + 1.1, entity.getZ()  + entity.world.rand.nextFloat() * 0.6 - 0.3, 0, -0.15, 0);
			}
		}
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void doRender(ItemStack stack, LivingEntity player, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
		Minecraft.getInstance().textureManager.bindTexture(AtlasTexture.LOCATION_BLOCKS_TEXTURE);
		boolean armor = !player.getItemStackFromSlot(EquipmentSlotType.CHEST).isEmpty();
		GlStateManager.rotatef(180F, 1F, 0F, 0F);
		GlStateManager.translatef(-0.36F, -0.3F, armor ? 0.2F : 0.15F);
		GlStateManager.rotatef(-45F, 0F, 0F, 1F);
		GlStateManager.scalef(0.5F, 0.5F, 0.5F);

		TextureAtlasSprite gemIcon = MiscellaneousIcons.INSTANCE.snowflakePendantGem;
		float f = gemIcon.getMinU();
		float f1 = gemIcon.getMaxU();
		float f2 = gemIcon.getMinV();
		float f3 = gemIcon.getMaxV();
		IconHelper.renderIconIn3D(Tessellator.getInstance(), f1, f2, f, f3, gemIcon.getWidth(), gemIcon.getHeight(), 1F / 32F);
	}

}
