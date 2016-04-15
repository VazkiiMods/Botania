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

import net.minecraft.entity.boss.EntityWither;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;
import vazkii.botania.common.achievement.ModAchievements;
import vazkii.botania.common.entity.EntityPinkWither;
import vazkii.botania.common.lib.LibItemNames;

import java.util.List;

public class ItemPinkinator extends ItemMod {

	public ItemPinkinator() {
		super(LibItemNames.PINKINATOR);
		setMaxStackSize(1);
		setFull3D();
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(ItemStack stack, World world, EntityPlayer player, EnumHand hand) {
		int range = 16;
		List<EntityWither> withers = world.getEntitiesWithinAABB(EntityWither.class, new AxisAlignedBB(player.posX - range, player.posY - range, player.posZ - range, player.posX + range, player.posY + range, player.posZ + range));
		for(EntityWither wither : withers)
			if(!wither.isDead && !(wither instanceof EntityPinkWither)) {
				if(!world.isRemote) {
					wither.setDead();
					EntityPinkWither pink = new EntityPinkWither(world);
					pink.setLocationAndAngles(wither.posX, wither.posY, wither.posZ, wither.rotationYaw, wither.rotationPitch);
					world.spawnEntityInWorld(pink);
					pink.playSound(SoundEvents.entity_generic_explode, 4F, (1F + (world.rand.nextFloat() - world.rand.nextFloat()) * 0.2F) * 0.7F);
				}
				player.addStat(ModAchievements.pinkinator, 1);

				world.spawnParticle(EnumParticleTypes.EXPLOSION_HUGE, wither.posX, wither.posY, wither.posZ, 1D, 0D, 0D);
				stack.stackSize--;
				return ActionResult.newResult(EnumActionResult.SUCCESS, stack);
			}

		return ActionResult.newResult(EnumActionResult.PASS, stack);
	}

	@Override
	public void addInformation(ItemStack p_77624_1_, EntityPlayer p_77624_2_, List<String> p_77624_3_, boolean p_77624_4_) {
		p_77624_3_.add(I18n.translateToLocal("botaniamisc.pinkinatorDesc"));
	}

}
