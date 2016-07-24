/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 * 
 * File Created @ [Jan 30, 2014, 4:49:16 PM (GMT)]
 */
package vazkii.botania.common.item.material;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.Achievement;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import vazkii.botania.api.item.IPetalApothecary;
import vazkii.botania.api.recipe.IElvenItem;
import vazkii.botania.api.recipe.IFlowerComponent;
import vazkii.botania.client.core.handler.ModelHandler;
import vazkii.botania.common.Botania;
import vazkii.botania.common.achievement.IPickupAchievement;
import vazkii.botania.common.achievement.ModAchievements;
import vazkii.botania.common.entity.EntityDoppleganger;
import vazkii.botania.common.entity.EntityEnderAirBottle;
import vazkii.botania.common.item.IColorable;
import vazkii.botania.common.item.ItemMod;
import vazkii.botania.common.lib.LibItemNames;

import javax.annotation.Nonnull;
import java.awt.*;
import java.util.List;

public class ItemManaResource extends ItemMod implements IFlowerComponent, IElvenItem, IPickupAchievement, IColorable {

	final int types = 24;

	public ItemManaResource() {
		super(LibItemNames.MANA_RESOURCE);
		setHasSubtypes(true);
		MinecraftForge.EVENT_BUS.register(this);
	}

	@SubscribeEvent
	public void onPlayerInteract(PlayerInteractEvent.RightClickBlock event) {
		ItemStack stack = event.getItemStack();
		boolean correctStack = stack != null && stack.getItem() == Items.GLASS_BOTTLE;
		boolean ender = event.getWorld().provider.getDimension() == 1;

		if(correctStack && ender) {
			if (event.getWorld().isRemote) {
				event.getEntityPlayer().swingArm(event.getHand());
			} else {
				ItemStack stack1 = new ItemStack(this, 1, 15);
				event.getEntityPlayer().addStat(ModAchievements.enderAirMake, 1);

				if(!event.getEntityPlayer().inventory.addItemStackToInventory(stack1)) {
					event.getEntityPlayer().dropItem(stack1, true);
				} else {
					event.getEntityPlayer().openContainer.detectAndSendChanges();
				}

				stack.stackSize--;
				if(stack.stackSize == 0)
					event.getEntityPlayer().inventory.setInventorySlotContents(event.getEntityPlayer().inventory.currentItem, null);

				event.getWorld().playSound(null, event.getPos(), SoundEvents.ENTITY_ITEM_PICKUP, SoundCategory.PLAYERS, 0.5F, 1F);
				event.setCanceled(true);
			}
		}
	}

	@Nonnull
	@Override
	public EnumActionResult onItemUse(ItemStack par1ItemStack, EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing side, float par8, float par9, float par10) {
		if(par1ItemStack.getItemDamage() == 4 || par1ItemStack.getItemDamage() == 14)
			return world.isRemote || EntityDoppleganger.spawn(player, par1ItemStack, world, pos, par1ItemStack.getItemDamage() == 14) ? EnumActionResult.SUCCESS : EnumActionResult.FAIL;
		else if(par1ItemStack.getItemDamage() == 20 && net.minecraft.item.ItemDye.applyBonemeal(par1ItemStack, world, pos, player)) {
			if(!world.isRemote)
				world.playEvent(2005, pos, 0);

			return EnumActionResult.SUCCESS;
		}

		return super.onItemUse(par1ItemStack, player, world, pos, hand, side, par8, par9, par10);
	}

	@Nonnull
	@Override
	public ActionResult<ItemStack> onItemRightClick(@Nonnull ItemStack par1ItemStack, World world, EntityPlayer player, EnumHand hand) {
		if(par1ItemStack.getItemDamage() == 15) {
			if(!player.capabilities.isCreativeMode)
				--par1ItemStack.stackSize;

			world.playSound(null, player.posX, player.posY, player.posZ, SoundEvents.ENTITY_ARROW_SHOOT, SoundCategory.PLAYERS, 0.5F, 0.4F / (itemRand.nextFloat() * 0.4F + 0.8F));

			if(!world.isRemote) {
				EntityEnderAirBottle b = new EntityEnderAirBottle(world, player);
				b.setHeadingFromThrower(player, player.rotationPitch, player.rotationYaw, 0F, 1.5F, 1F);
				world.spawnEntityInWorld(b);
			}
			else player.swingArm(hand);
			return ActionResult.newResult(EnumActionResult.SUCCESS, par1ItemStack);
		}

		return ActionResult.newResult(EnumActionResult.PASS, par1ItemStack);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void getSubItems(@Nonnull Item item, CreativeTabs tab, List<ItemStack> stacks) {
		for(int i = 0; i < types; i++)
			if(Botania.gardenOfGlassLoaded || i != 20 && i != 21)
				stacks.add(new ItemStack(item, 1, i));
	}

	@Override
	@SideOnly(Side.CLIENT)
	public int getColorFromItemStack(ItemStack par1ItemStack, int par2) {
		if(par1ItemStack.getItemDamage() == 5 || par1ItemStack.getItemDamage() == 14)
			return Color.HSBtoRGB(Botania.proxy.getWorldElapsedTicks() * 2 % 360 / 360F, 0.25F, 1F);

		return 0xFFFFFF;
	}

	@Nonnull
	@Override
	public String getUnlocalizedName(ItemStack par1ItemStack) {
		return "item." + LibItemNames.MANA_RESOURCE_NAMES[Math.min(types - 1, par1ItemStack.getItemDamage())];
	}

	@Override
	public boolean canFit(ItemStack stack, IPetalApothecary apothecary) {
		int meta = stack.getItemDamage();
		return meta == 6 || meta == 8 || meta == 5 || meta == 23;
	}

	@Override
	public int getParticleColor(ItemStack stack) {
		return 0x9b0000;
	}

	@Override
	public boolean isElvenItem(ItemStack stack) {
		int meta = stack.getItemDamage();
		return meta == 7 || meta == 8 || meta == 9;
	}

	@Nonnull
	@Override
	public ItemStack getContainerItem(@Nonnull ItemStack itemStack) {
		return itemStack.getItemDamage() == 11 ? itemStack.copy() : null;
	}

	@Override
	public Achievement getAchievementOnPickup(ItemStack stack, EntityPlayer player, EntityItem item) {
		return stack.getItemDamage() == 4 ? ModAchievements.terrasteelPickup : null;
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void registerModels() {
		ModelHandler.registerItemMetas(this, LibItemNames.MANA_RESOURCE_NAMES.length, i -> LibItemNames.MANA_RESOURCE_NAMES[i]);
	}

}
