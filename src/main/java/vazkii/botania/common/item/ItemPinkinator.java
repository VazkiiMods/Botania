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

import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.boss.EntityWither;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import vazkii.botania.common.advancements.UseItemSuccessTrigger;
import vazkii.botania.common.core.helper.PlayerHelper;
import vazkii.botania.common.entity.EntityPinkWither;
import vazkii.botania.common.lib.LibItemNames;
import vazkii.botania.common.lib.LibMisc;

import javax.annotation.Nonnull;
import java.util.List;

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
			if(!world.isRemote && !wither.isDead && !(wither instanceof EntityPinkWither)) {
				wither.setDead();
				EntityPinkWither pink = new EntityPinkWither(world);
				pink.setLocationAndAngles(wither.posX, wither.posY, wither.posZ, wither.rotationYaw, wither.rotationPitch);
				pink.onInitialSpawn(world.getDifficultyForLocation(new BlockPos(pink)), null);
				world.spawnEntity(pink);
				pink.spawnExplosionParticle();
				pink.playSound(SoundEvents.ENTITY_GENERIC_EXPLODE, 4F, (1F + (world.rand.nextFloat() - world.rand.nextFloat()) * 0.2F) * 0.7F);
				UseItemSuccessTrigger.INSTANCE.trigger((EntityPlayerMP) player, stack, (WorldServer) world, player.posX, player.posY, player.posZ);
				stack.shrink(1);
				return ActionResult.newResult(EnumActionResult.SUCCESS, stack);
			}

		return ActionResult.newResult(EnumActionResult.PASS, stack);
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void addInformation(ItemStack stack, World world, List<String> tooltip, ITooltipFlag flags) {
		tooltip.add(I18n.format("botaniamisc.pinkinatorDesc"));
	}

}
