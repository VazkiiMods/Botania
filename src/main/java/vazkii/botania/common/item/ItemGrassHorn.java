/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 * 
 * File Created @ [Apr 11, 2014, 2:57:30 PM (GMT)]
 */
package vazkii.botania.common.item;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.BlockBush;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import vazkii.botania.api.item.IGrassHornExcempt;
import vazkii.botania.api.item.IHornHarvestable;
import vazkii.botania.api.item.IHornHarvestable.EnumHornType;
import vazkii.botania.api.subtile.ISpecialFlower;
import vazkii.botania.client.core.helper.IconHelper;
import vazkii.botania.common.core.handler.ConfigHandler;
import vazkii.botania.common.lib.LibItemNames;

public class ItemGrassHorn extends ItemMod {

	private static final int SUBTYPES = 3;
	IIcon[] icons;
	IIcon vuvuzelaIcon;

	public ItemGrassHorn() {
		super();
		setMaxStackSize(1);
		setUnlocalizedName(LibItemNames.GRASS_HORN);
		setHasSubtypes(true);
	}

	@Override
	public void getSubItems(Item item, CreativeTabs tab, List list) {
		for(int i = 0; i < SUBTYPES; i++)
			list.add(new ItemStack(item, 1, i));
	}

	@Override
	public void registerIcons(IIconRegister par1IconRegister) {
		icons = new IIcon[SUBTYPES];
		for(int i = 0; i < icons.length; i++)
			icons[i] = IconHelper.forItem(par1IconRegister, this, i);
		vuvuzelaIcon = IconHelper.forName(par1IconRegister, "vuvuzela");
	}

	@Override
	public IIcon getIconIndex(ItemStack par1ItemStack) {
		return par1ItemStack.getDisplayName().toLowerCase().contains("vuvuzela") ? vuvuzelaIcon : super.getIconIndex(par1ItemStack);
	}

	@Override
	public IIcon getIcon(ItemStack stack, int pass) {
		return getIconIndex(stack);
	}

	@Override
	public IIcon getIconFromDamage(int par1) {
		return icons[Math.min(icons.length - 1, par1)];
	}

	@Override
	public String getUnlocalizedName(ItemStack par1ItemStack) {
		return getUnlocalizedNameLazy(par1ItemStack) + par1ItemStack.getItemDamage();
	}

	String getUnlocalizedNameLazy(ItemStack par1ItemStack) {
		return super.getUnlocalizedName(par1ItemStack);
	}

	@Override
	public EnumAction getItemUseAction(ItemStack par1ItemStack) {
		return EnumAction.bow;
	}

	@Override
	public int getMaxItemUseDuration(ItemStack par1ItemStack) {
		return 72000;
	}

	@Override
	public ItemStack onItemRightClick(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer) {
		par3EntityPlayer.setItemInUse(par1ItemStack, getMaxItemUseDuration(par1ItemStack));
		return par1ItemStack;
	}

	@Override
	public void onUsingTick(ItemStack stack, EntityPlayer player, int time) {
		if(time != getMaxItemUseDuration(stack) && time % 5 == 0)
			breakGrass(player.worldObj, stack, stack.getItemDamage(), (int) player.posX, (int) player.posY, (int) player.posZ);

		if(!player.worldObj.isRemote)
			player.worldObj.playSoundAtEntity(player, "note.bassattack", 1F, 0.001F);
	}

	public static void breakGrass(World world, ItemStack stack, int stackDmg, int srcx, int srcy, int srcz) {
		EnumHornType type = EnumHornType.getTypeForMeta(stackDmg);
		Random rand = new Random(srcx ^ srcy ^ srcz);
		int range = 12 - stackDmg * 3;
		int rangeY = 3 + stackDmg * 4;
		List<ChunkCoordinates> coords = new ArrayList();

		for(int i = -range; i < range + 1; i++)
			for(int j = -range; j < range + 1; j++)
				for(int k = -rangeY; k < rangeY + 1; k++) {
					int x = srcx + i;
					int y = srcy + k;
					int z = srcz + j;

					Block block = world.getBlock(x, y, z);
					if(block instanceof IHornHarvestable ? ((IHornHarvestable) block).canHornHarvest(world, x, y, z, stack, type) : stackDmg == 0 && block instanceof BlockBush && !(block instanceof ISpecialFlower) && (!(block instanceof IGrassHornExcempt) || ((IGrassHornExcempt) block).canUproot(world, x, y, z)) || stackDmg == 1 && block.isLeaves(world, x, y, z) || stackDmg == 2 && block == Blocks.snow_layer)
						coords.add(new ChunkCoordinates(x, y, z));
				}

		Collections.shuffle(coords, rand);

		int count = Math.min(coords.size(), 32 + stackDmg * 16);
		for(int i = 0; i < count; i++) {
			ChunkCoordinates currCoords = coords.get(i);
			List<ItemStack> items = new ArrayList();
			Block block = world.getBlock(currCoords.posX, currCoords.posY, currCoords.posZ);
			int meta = world.getBlockMetadata(currCoords.posX, currCoords.posY, currCoords.posZ);
			items.addAll(block.getDrops(world, currCoords.posX, currCoords.posY, currCoords.posZ, meta, 0));

			if(block instanceof IHornHarvestable && ((IHornHarvestable) block).hasSpecialHornHarvest(world, currCoords.posX, currCoords.posY, currCoords.posZ, stack, type))
				((IHornHarvestable) block).harvestByHorn(world, currCoords.posX, currCoords.posY, currCoords.posZ, stack, type);
			else if(!world.isRemote) {
				world.setBlockToAir(currCoords.posX, currCoords.posY, currCoords.posZ);
				if(ConfigHandler.blockBreakParticles)
					world.playAuxSFX(2001, currCoords.posX, currCoords.posY, currCoords.posZ, Block.getIdFromBlock(block) + (meta << 12));

				for(ItemStack stack_ : items)
					world.spawnEntityInWorld(new EntityItem(world, currCoords.posX + 0.5, currCoords.posY + 0.5, currCoords.posZ + 0.5, stack_));
			}
		}
	}

	@Override
	public boolean isFull3D() {
		return true;
	}
}
