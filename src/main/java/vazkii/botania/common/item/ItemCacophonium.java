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
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.monster.EntitySlime;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.Achievement;
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
import vazkii.botania.common.achievement.ICraftAchievement;
import vazkii.botania.common.achievement.ModAchievements;
import vazkii.botania.common.block.ModBlocks;
import vazkii.botania.common.block.tile.TileCacophonium;
import vazkii.botania.common.core.helper.ItemNBTHelper;
import vazkii.botania.common.lib.LibItemNames;
import vazkii.botania.common.lib.LibObfuscation;

import javax.annotation.Nonnull;
import java.util.List;

public class ItemCacophonium extends ItemMod implements ICraftAchievement {

	private static final String TAG_SOUND = "sound";
	private static final String TAG_SOUND_NAME = "soundName";
	private static final String TAG_HAS_SOUND = "hasSound";

	public ItemCacophonium() {
		super(LibItemNames.CACOPHONIUM);
		setMaxStackSize(1);
	}

	@Override
	public boolean itemInteractionForEntity(ItemStack stack, EntityPlayer player, EntityLivingBase entity, EnumHand hand) {
		if(entity instanceof EntityLiving) {
			EntityLiving living = (EntityLiving) entity;
			SoundEvent sound;
			try {
				if(living instanceof EntityCreeper)
					sound = SoundEvents.ENTITY_CREEPER_PRIMED;
				else if(living instanceof EntitySlime)
					sound = ((EntitySlime) living).isSmallSlime() ? SoundEvents.ENTITY_SMALL_SLIME_SQUISH : SoundEvents.ENTITY_SLIME_SQUISH;
				else sound = (SoundEvent) ReflectionHelper.findMethod(EntityLiving.class, living, LibObfuscation.GET_LIVING_SOUND).invoke(living);

				if(sound != null) {
					String s = EntityList.getEntityString(entity);
					if(s == null)
						s = "generic";

					ItemNBTHelper.setString(stack, TAG_SOUND, sound.getRegistryName().toString());
					ItemNBTHelper.setString(stack, TAG_SOUND_NAME, "entity." + s + ".name");
					ItemNBTHelper.setBoolean(stack, TAG_HAS_SOUND, true);
					player.setHeldItem(hand, stack);
					//player.inventory.setInventorySlotContents(player.inventory.currentItem, stack.copy());

					if(player.worldObj.isRemote)
						player.swingArm(hand);

					return true;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return false;
	}

	@Nonnull
	@Override
	public EnumActionResult onItemUse(ItemStack stack, EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing side, float xs, float ys, float zs) {
		boolean can = isDOIT(stack);
		if(!can) {
			String sound = ItemNBTHelper.getString(stack, TAG_SOUND, "");
			isDOIT(stack);
			if(sound != null && !sound.isEmpty())
				can = true;
		}

		if(can) {
			Block block = world.getBlockState(pos).getBlock();
			if(block == Blocks.NOTEBLOCK) {
				world.setBlockState(pos, ModBlocks.cacophonium.getDefaultState());
				((TileCacophonium) world.getTileEntity(pos)).stack = stack.copy();
				stack.stackSize--;
				return EnumActionResult.SUCCESS;
			}
		}

		return EnumActionResult.PASS;
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void addInformation(ItemStack stack, EntityPlayer player, List<String> list, boolean adv) {
		if(isDOIT(stack))
			list.add(I18n.format("botaniamisc.justDoIt"));
		else if(ItemNBTHelper.getBoolean(stack, TAG_HAS_SOUND, false))
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
	public ActionResult<ItemStack> onItemRightClick(@Nonnull ItemStack par1ItemStack, World world, EntityPlayer player, EnumHand hand) {
		if(ItemNBTHelper.getBoolean(par1ItemStack, TAG_HAS_SOUND, false) || isDOIT(par1ItemStack))
			player.setActiveHand(hand);
		return ActionResult.newResult(EnumActionResult.SUCCESS, par1ItemStack);
	}

	@Override
	public void onUsingTick(ItemStack stack, EntityLivingBase player, int count) {
		if(count % (isDOIT(stack) ? 20 : 6) == 0)
			playSound(player.worldObj, stack, player.posX, player.posY, player.posZ, SoundCategory.PLAYERS, 0.9F);
	}

	public static void playSound(World world, ItemStack stack, double x, double y, double z, SoundCategory category, float volume) {
		if(stack == null)
			return;

		String sound = ItemNBTHelper.getString(stack, TAG_SOUND, "");
		boolean doit = isDOIT(stack);
		if(doit)
			sound = "botania:doit";

		if(sound != null && !sound.isEmpty() && SoundEvent.REGISTRY.containsKey(new ResourceLocation(sound)))
			world.playSound(null, x, y, z, SoundEvent.REGISTRY.getObject(new ResourceLocation(sound)), category, volume, doit ? 1F : (world.rand.nextFloat() - world.rand.nextFloat()) * 0.2F + 1.0F);
	}

	private static boolean isDOIT(ItemStack stack) {
		return stack != null && stack.getDisplayName().equalsIgnoreCase("shia labeouf");
	}

	@Override
	public Achievement getAchievementOnCraft(ItemStack stack, EntityPlayer player, IInventory matrix) {
		return ModAchievements.cacophoniumCraft;
	}
}
