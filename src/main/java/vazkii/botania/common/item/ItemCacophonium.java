/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * File Created @ [Jul 20, 2015, 9:53:57 PM (GMT)]
 */
package vazkii.botania.common.item;

import net.minecraft.block.Block;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.monster.EntitySlime;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.ReflectionHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import vazkii.botania.common.Botania;
import vazkii.botania.common.block.ModBlocks;
import vazkii.botania.common.block.tile.TileCacophonium;
import vazkii.botania.common.core.handler.ModSounds;
import vazkii.botania.common.core.helper.ItemNBTHelper;
import vazkii.botania.common.lib.LibItemNames;
import vazkii.botania.common.lib.LibObfuscation;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

public class ItemCacophonium extends ItemMod {

	private static final String TAG_SOUND = "sound";
	private static final String TAG_SOUND_NAME = "soundName";

	public ItemCacophonium() {
		super(LibItemNames.CACOPHONIUM);
		setMaxStackSize(1);
	}

	@Override
	public boolean itemInteractionForEntity(ItemStack stack, EntityPlayer player, EntityLivingBase entity, EnumHand hand) {
		if(entity instanceof EntityLiving) {
			EntityLiving living = (EntityLiving) entity;
			SoundEvent sound = null;

			if(living instanceof EntityCreeper)
				sound = SoundEvents.ENTITY_CREEPER_PRIMED;
			else if(living instanceof EntitySlime)
				sound = ((EntitySlime) living).isSmallSlime() ? SoundEvents.ENTITY_SMALL_SLIME_SQUISH : SoundEvents.ENTITY_SLIME_SQUISH;
			else {
				try {
					sound = (SoundEvent) ReflectionHelper.findMethod(EntityLiving.class, LibObfuscation.GET_LIVING_SOUND[0], LibObfuscation.GET_LIVING_SOUND[1]).invoke(living);
				} catch (InvocationTargetException | IllegalAccessException ignored) {
					Botania.LOGGER.debug("Couldn't get living sound");
				}
			}

			if(sound != null) {
				String s = EntityList.getEntityString(entity);
				if(s == null)
					s = "generic";

				ItemNBTHelper.setString(stack, TAG_SOUND, sound.getRegistryName().toString());
				ItemNBTHelper.setString(stack, TAG_SOUND_NAME, "entity." + s + ".name");
				player.setHeldItem(hand, stack);

				if(player.world.isRemote)
					player.swingArm(hand);

				return true;
			}
		}

		return false;
	}

	@Nonnull
	@Override
	public EnumActionResult onItemUse(EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing side, float xs, float ys, float zs) {
		ItemStack stack = player.getHeldItem(hand);
		if(getSound(stack) != null) {
			Block block = world.getBlockState(pos).getBlock();
			if(block == Blocks.NOTEBLOCK) {
				world.setBlockState(pos, ModBlocks.cacophonium.getDefaultState());
				((TileCacophonium) world.getTileEntity(pos)).stack = stack.copy();
				stack.shrink(1);
				return EnumActionResult.SUCCESS;
			}
		}

		return EnumActionResult.PASS;
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void addInformation(ItemStack stack, World world, List<String> list, ITooltipFlag flags) {
		if(isDOIT(stack))
			list.add(I18n.format("botaniamisc.justDoIt"));
		else if(getSound(stack) != null)
			list.add(I18n.format(ItemNBTHelper.getString(stack, TAG_SOUND_NAME, "")));
	}

	@Nonnull
	@Override
	public EnumAction getItemUseAction(ItemStack par1ItemStack) {
		return EnumAction.BLOCK;
	}

	@Override
	public int getMaxItemUseDuration(ItemStack par1ItemStack) {
		return 72000;
	}

	@Nonnull
	@Override
	public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, @Nonnull EnumHand hand) {
		ItemStack stack = player.getHeldItem(hand);
		if(getSound(stack) != null)
			player.setActiveHand(hand);
		return ActionResult.newResult(EnumActionResult.SUCCESS, stack);
	}

	@Override
	public void onUsingTick(ItemStack stack, EntityLivingBase player, int count) {
		if(count % (isDOIT(stack) ? 20 : 6) == 0)
			playSound(player.world, stack, player.posX, player.posY, player.posZ, SoundCategory.PLAYERS, 0.9F);
	}

	public static void playSound(World world, ItemStack stack, double x, double y, double z, SoundCategory category, float volume) {
		if(stack.isEmpty())
			return;

		SoundEvent sound = getSound(stack);

		if(sound != null)
			world.playSound(null, x, y, z, sound, category, volume, sound == ModSounds.doit ? 1F : (world.rand.nextFloat() - world.rand.nextFloat()) * 0.2F + 1.0F);
	}

	@Nullable
	private static SoundEvent getSound(ItemStack stack) {
		if(isDOIT(stack))
			return ModSounds.doit;
		else return SoundEvent.REGISTRY.getObject(new ResourceLocation(ItemNBTHelper.getString(stack, TAG_SOUND, "")));
	}

	private static boolean isDOIT(ItemStack stack) {
		return !stack.isEmpty() && stack.getDisplayName().equalsIgnoreCase("shia labeouf");
	}
}
