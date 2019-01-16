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

import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldProviderEnd;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.ItemHandlerHelper;
import vazkii.botania.api.item.IPetalApothecary;
import vazkii.botania.api.recipe.IElvenItem;
import vazkii.botania.api.recipe.IFlowerComponent;
import vazkii.botania.client.core.handler.ModelHandler;
import vazkii.botania.common.Botania;
import vazkii.botania.common.entity.EntityDoppleganger;
import vazkii.botania.common.entity.EntityEnderAirBottle;
import vazkii.botania.common.item.ItemMod;
import vazkii.botania.common.lib.LibItemNames;
import vazkii.botania.common.lib.LibMisc;

import javax.annotation.Nonnull;

public class ItemManaResource extends ItemMod implements IFlowerComponent, IElvenItem {

	final int types = 24;

	public ItemManaResource() {
		super(LibItemNames.MANA_RESOURCE);
		setHasSubtypes(true);
		MinecraftForge.EVENT_BUS.register(this);
	}

	@SubscribeEvent
	public void onPlayerInteract(PlayerInteractEvent.RightClickBlock event) {
		ItemStack stack = event.getItemStack();
		boolean correctStack = !stack.isEmpty() && stack.getItem() == Items.GLASS_BOTTLE;
		boolean ender = event.getWorld().provider instanceof WorldProviderEnd;

		if(correctStack && ender) {
			if (event.getWorld().isRemote) {
				event.getEntityPlayer().swingArm(event.getHand());
			} else {
				ItemStack stack1 = new ItemStack(this, 1, 15);

				ItemHandlerHelper.giveItemToPlayer(event.getEntityPlayer(), stack1);

				stack.shrink(1);

				event.getWorld().playSound(null, event.getPos(), SoundEvents.ENTITY_ITEM_PICKUP, SoundCategory.PLAYERS, 0.5F, 1F);
			}

			event.setCanceled(true);
			event.setCancellationResult(EnumActionResult.SUCCESS);
		}
	}

	@Nonnull
	@Override
	public EnumActionResult onItemUse(EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing side, float par8, float par9, float par10) {
		ItemStack stack = player.getHeldItem(hand);

		if(stack.getItemDamage() == 4 || stack.getItemDamage() == 14)
			return EntityDoppleganger.spawn(player, stack, world, pos, stack.getItemDamage() == 14) ? EnumActionResult.SUCCESS : EnumActionResult.FAIL;
		else if(stack.getItemDamage() == 20 && net.minecraft.item.ItemDye.applyBonemeal(stack, world, pos, player, hand)) {
			if(!world.isRemote)
				world.playEvent(2005, pos, 0);

			return EnumActionResult.SUCCESS;
		}

		return super.onItemUse(player, world, pos, hand, side, par8, par9, par10);
	}

	@Nonnull
	@Override
	public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, @Nonnull EnumHand hand) {
		ItemStack stack = player.getHeldItem(hand);
		if(stack.getItemDamage() == 15) {
			if(!player.capabilities.isCreativeMode)
				stack.shrink(1);

			world.playSound(null, player.posX, player.posY, player.posZ, SoundEvents.ENTITY_ARROW_SHOOT, SoundCategory.PLAYERS, 0.5F, 0.4F / (itemRand.nextFloat() * 0.4F + 0.8F));

			if(!world.isRemote) {
				EntityEnderAirBottle b = new EntityEnderAirBottle(world, player);
				b.shoot(player, player.rotationPitch, player.rotationYaw, 0F, 1.5F, 1F);
				world.spawnEntity(b);
			}
			else player.swingArm(hand);
			return ActionResult.newResult(EnumActionResult.SUCCESS, stack);
		}

		return ActionResult.newResult(EnumActionResult.PASS, stack);
	}

	@Override
	public void getSubItems(@Nonnull CreativeTabs tab, @Nonnull NonNullList<ItemStack> stacks) {
		if(isInCreativeTab(tab)) {
			for(int i = 0; i < types; i++) {
				if("UNUSED".equals(LibItemNames.MANA_RESOURCE_NAMES[i]))
					continue;
				if(Botania.gardenOfGlassLoaded || i != 20 && i != 21)
					stacks.add(new ItemStack(this, 1, i));
			}
		}
	}

	@Nonnull
	@Override
	public String getTranslationKey(ItemStack stack) {
		return "item." + LibItemNames.MANA_RESOURCE_NAMES[Math.min(types - 1, stack.getItemDamage())];
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
		return itemStack.getItemDamage() == 11 ? itemStack.copy() : ItemStack.EMPTY;
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void registerModels() {
		for (int i = 0; i < LibItemNames.MANA_RESOURCE_NAMES.length; i++) {
			if (!"UNUSED".equals(LibItemNames.MANA_RESOURCE_NAMES[i])) {
				ModelLoader.setCustomModelResourceLocation(
					this, i,
					new ModelResourceLocation(LibMisc.MOD_ID + ":" + LibItemNames.MANA_RESOURCE_NAMES[i], "inventory")
				);
			}
		}
	}

}
