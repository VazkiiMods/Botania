/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * File Created @ [Mar 25, 2015, 6:03:28 PM (GMT)]
 */
package vazkii.botania.common.item;

import java.util.List;

import javax.annotation.Nonnull;

import net.minecraft.client.resources.I18n;
import net.minecraft.entity.boss.EntityWither;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import vazkii.botania.common.achievement.ModAchievements;
import vazkii.botania.common.entity.EntityPinkWither;
import vazkii.botania.common.lib.LibItemNames;

public class ItemPinkinator extends ItemMod {

	public ItemPinkinator() {
		super(LibItemNames.PINKINATOR);
		setMaxStackSize(1);
		setFull3D();
	}

	@Nonnull
	@Override
	public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, @Nonnull EnumHand hand) {
		ItemStack stack = player.getHeldItem(hand);
		int range = 16;
		List<EntityWither> withers = world.getEntitiesWithinAABB(EntityWither.class, new AxisAlignedBB(player.posX - range, player.posY - range, player.posZ - range, player.posX + range, player.posY + range, player.posZ + range));
		for(EntityWither wither : withers)
			if(!wither.isDead && !(wither instanceof EntityPinkWither)) {
				if(!world.isRemote) {
					wither.setDead();
					EntityPinkWither pink = new EntityPinkWither(world);
					pink.setLocationAndAngles(wither.posX, wither.posY, wither.posZ, wither.rotationYaw, wither.rotationPitch);
					pink.onInitialSpawn(world.getDifficultyForLocation(new BlockPos(pink)), null);
					world.spawnEntity(pink);
					pink.playSound(SoundEvents.ENTITY_GENERIC_EXPLODE, 4F, (1F + (world.rand.nextFloat() - world.rand.nextFloat()) * 0.2F) * 0.7F);
				}
				player.addStat(ModAchievements.pinkinator, 1);

				world.spawnParticle(EnumParticleTypes.EXPLOSION_HUGE, wither.posX, wither.posY, wither.posZ, 1D, 0D, 0D);
				stack.shrink(1);
				return ActionResult.newResult(EnumActionResult.SUCCESS, stack);
			}

		return ActionResult.newResult(EnumActionResult.PASS, stack);
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void addInformation(ItemStack stack, EntityPlayer player, List<String> tooltip, boolean advanced) {
		tooltip.add(I18n.format("botaniamisc.pinkinatorDesc"));
	}

}
