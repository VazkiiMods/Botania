/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * File Created @ [Mar 29, 2015, 10:13:26 PM (GMT)]
 */
package vazkii.botania.common.item.relic;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import vazkii.botania.api.mana.IManaUsingItem;
import vazkii.botania.api.mana.ManaItemHandler;
import vazkii.botania.api.wand.ICoordBoundItem;
import vazkii.botania.common.Botania;
import vazkii.botania.common.core.helper.ItemNBTHelper;
import vazkii.botania.common.core.helper.MathHelper;
import vazkii.botania.common.lib.LibItemNames;
import vazkii.botania.common.lib.LibMisc;
import vazkii.botania.common.network.PacketBotaniaEffect;
import vazkii.botania.common.network.PacketHandler;

import javax.annotation.Nonnull;

public class ItemFlugelEye extends ItemRelic implements ICoordBoundItem, IManaUsingItem {

	public ItemFlugelEye() {
		super(LibItemNames.FLUGEL_EYE);
	}

	private static final String TAG_X = "x";
	private static final String TAG_Y = "y";
	private static final String TAG_Z = "z";
	private static final String TAG_DIMENSION = "dim";

	@Nonnull
	@Override
	public EnumActionResult onItemUse(EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
		if(player.isSneaking()) {
			if(world.isRemote) {
				player.swingArm(hand);
				for(int i = 0; i < 10; i++) {
					float x1 = (float) (pos.getX() + Math.random());
					float y1 = pos.getY() + 1;
					float z1 = (float) (pos.getZ() + Math.random());
					Botania.proxy.wispFX(x1, y1, z1, (float) Math.random(), (float) Math.random(), (float) Math.random(), (float) Math.random() * 0.5F, -0.05F + (float) Math.random() * 0.05F);
				}
			} else {
				ItemStack stack = player.getHeldItem(hand);
				ItemNBTHelper.setInt(stack, TAG_X, pos.getX());
				ItemNBTHelper.setInt(stack, TAG_Y, pos.getY());
				ItemNBTHelper.setInt(stack, TAG_Z, pos.getZ());
				ItemNBTHelper.setInt(stack, TAG_DIMENSION, world.provider.getDimension());
				world.playSound(null, player.posX, player.posY, player.posZ, SoundEvents.ENTITY_ENDERMEN_TELEPORT, SoundCategory.PLAYERS, 1F, 5F);
			}

			return EnumActionResult.SUCCESS;
		}

		return EnumActionResult.PASS;
	}

	@Override
	public void onUsingTick(ItemStack stack, EntityLivingBase living, int count) {
		float x = (float) (living.posX - Math.random() * living.width);
		float y = (float) (living.posY + Math.random());
		float z = (float) (living.posZ - Math.random() * living.width);
		Botania.proxy.wispFX(x, y, z, (float) Math.random(), (float) Math.random(), (float) Math.random(), (float) Math.random() * 0.7F, -0.05F - (float) Math.random() * 0.05F);
	}

	@Nonnull
	@Override
	public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, @Nonnull EnumHand hand) {
		player.setActiveHand(hand);
		return ActionResult.newResult(EnumActionResult.SUCCESS, player.getHeldItem(hand));
	}

	@Nonnull
	@Override
	public ItemStack onItemUseFinish(@Nonnull ItemStack stack, World world, EntityLivingBase living) {
		if(!(living instanceof EntityPlayer))
			return stack;

		EntityPlayer player = (EntityPlayer) living;
		int x = ItemNBTHelper.getInt(stack, TAG_X, 0);
		int y = ItemNBTHelper.getInt(stack, TAG_Y, -1);
		int z = ItemNBTHelper.getInt(stack, TAG_Z, 0);
		int dim = ItemNBTHelper.getInt(stack, TAG_DIMENSION, 0);

		int cost = (int) (MathHelper.pointDistanceSpace(x + 0.5, y + 0.5, z + 0.5, player.posX, player.posY, player.posZ) * 10);

		if(y > -1 && dim == world.provider.getDimension() && ManaItemHandler.requestManaExact(stack, player, cost, true)) {
			moveParticlesAndSound(player);
			if(player instanceof EntityPlayerMP)
				((EntityPlayerMP) player).connection.setPlayerLocation(x + 0.5, y + 1.6, z + 0.5, player.rotationYaw, player.rotationPitch);
			moveParticlesAndSound(player);
		}

		return stack;
	}

	private static void moveParticlesAndSound(Entity entity) {
		PacketHandler.sendToNearby(entity.world, entity, new PacketBotaniaEffect(PacketBotaniaEffect.EffectType.FLUGEL_EFFECT, entity.posX, entity.posY, entity.posZ, entity.getEntityId()));
		entity.world.playSound(null, entity.posX, entity.posY, entity.posZ, SoundEvents.ENTITY_ENDERMEN_TELEPORT, SoundCategory.PLAYERS, 1F, 1F);
	}

	@Override
	public int getMaxItemUseDuration(ItemStack par1ItemStack) {
		return 40;
	}

	@Nonnull
	@Override
	public EnumAction getItemUseAction(ItemStack par1ItemStack) {
		return EnumAction.BOW;
	}

	@Override
	public BlockPos getBinding(ItemStack stack) {
		int x = ItemNBTHelper.getInt(stack, TAG_X, 0);
		int y = ItemNBTHelper.getInt(stack, TAG_Y, -1);
		int z = ItemNBTHelper.getInt(stack, TAG_Z, 0);
		return y == -1 ? null : new BlockPos(x, y, z);
	}

	@Override
	public boolean usesMana(ItemStack stack) {
		return true;
	}

	@Override
	public ResourceLocation getAdvancement() {
		return new ResourceLocation(LibMisc.MOD_ID, "challenge/flugel_eye");
	}

}
