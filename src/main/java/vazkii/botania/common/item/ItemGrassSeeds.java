/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 * 
 * File Created @ [Apr 9, 2014, 5:11:34 PM (GMT)]
 */
package vazkii.botania.common.item;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import vazkii.botania.client.core.helper.IconHelper;
import vazkii.botania.common.Botania;
import vazkii.botania.common.block.ModBlocks;
import vazkii.botania.common.block.decor.IFloatingFlower.IslandType;
import vazkii.botania.common.lib.LibItemNames;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import cpw.mods.fml.common.gameevent.TickEvent.Phase;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemGrassSeeds extends ItemMod {

	private static Map<Integer, List<BlockSwapper>> blockSwappers = new HashMap();

	private static final IslandType[] ISLAND_TYPES = {
		IslandType.GRASS, IslandType.PODZOL, IslandType.MYCEL,
		IslandType.DRY, IslandType.GOLDEN, IslandType.VIVID, 
		IslandType.SCORCHED, IslandType.INFUSED, IslandType.MUTATED
	};

	private static final int SUBTYPES = 9;
	IIcon[] icons;

	public ItemGrassSeeds() {
		super();
		setUnlocalizedName(LibItemNames.GRASS_SEEDS);
		setHasSubtypes(true);
		FMLCommonHandler.instance().bus().register(this);
	}

	@Override
	public void getSubItems(Item par1, CreativeTabs par2, List par3) {
		for(int i = 0; i < SUBTYPES; i++)
			par3.add(new ItemStack(par1, 1, i));
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(IIconRegister par1IconRegister) {
		icons = new IIcon[SUBTYPES];
		for(int i = 0; i < SUBTYPES; i++)
			icons[i] = IconHelper.forItem(par1IconRegister, this, i);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public IIcon getIconFromDamage(int par1) {
		return icons[Math.min(icons.length - 1, par1)];
	}

	@Override
	public String getUnlocalizedName(ItemStack stack) {
		return super.getUnlocalizedName() + stack.getItemDamage();
	}

	@Override
	public boolean onItemUse(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, World par3World, int par4, int par5, int par6, int par7, float par8, float par9, float par10) {
		Block block = par3World.getBlock(par4, par5, par6);
		int bmeta = par3World.getBlockMetadata(par4, par5, par6);

		if((block == Blocks.dirt || (block == Blocks.grass && par1ItemStack.getItemDamage() != 0)) && bmeta == 0) {
			int meta = par1ItemStack.getItemDamage();

			BlockSwapper swapper = addBlockSwapper(par3World, par4, par5, par6, meta);
			par3World.setBlock(par4, par5, par6, swapper.blockToSet, swapper.metaToSet, 1 | 2);
			for(int i = 0; i < 50; i++) {
				double x = (Math.random() - 0.5) * 3;
				double y = Math.random() - 0.5 + 1;
				double z = (Math.random() - 0.5) * 3;

				float r = 0F;
				float g = 0.4F;
				float b = 0F;
				switch(meta) {
				case 1: {
					r = 0.5F;
					g = 0.37F;
					b = 0F;
					break;
				}
				case 2: {
					r = 0.27F;
					g = 0F;
					b = 0.33F;
					break;
				}
				case 3: {
					r = 0.4F;
					g = 0.5F;
					b = 0.05F;
					break;
				}
				case 4: {
					r = 0.75F;
					g = 0.7F;
					b = 0F;
					break;
				}
				case 5: {
					r = 0F;
					g = 0.5F;
					b = 0.1F;
					break;
				}
				case 6: {
					r = 0.75F;
					g = 0F;
					b = 0F;
					break;
				}
				case 7: {
					r = 0F;
					g = 0.55F;
					b = 0.55F;
					break;
				}
				case 8: {
					r = 0.4F;
					g = 0.1F;
					b = 0.4F;
					break;
				}
				}

				float velMul = 0.025F;

				Botania.proxy.wispFX(par3World, par4 + 0.5 + x, par5 + 0.5 + y, par6 + 0.5 + z, r, g, b, (float) Math.random() * 0.15F + 0.15F, (float) -x * velMul, (float) -y * velMul, (float) -z * velMul);
			}

			par1ItemStack.stackSize--;
		}

		return true;
	}

	@SubscribeEvent
	public void onTickEnd(TickEvent.WorldTickEvent event) {
		if(event.phase == Phase.END) {
			int dim = event.world.provider.dimensionId;
			if(blockSwappers.containsKey(dim)) {
				List<BlockSwapper> swappers = blockSwappers.get(dim);
				List<BlockSwapper> swappersSafe = new ArrayList(swappers);

				for(BlockSwapper s : swappersSafe)
					s.tick(swappers);
			}
		}
	}

	private static BlockSwapper addBlockSwapper(World world, int x, int y, int z, int meta) {
		BlockSwapper swapper = swapperFromMeta(world, x, y, z, meta);

		int dim = world.provider.dimensionId;
		if(!blockSwappers.containsKey(dim))
			blockSwappers.put(dim, new ArrayList());
		blockSwappers.get(dim).add(swapper);

		return swapper;
	}

	private static BlockSwapper swapperFromMeta(World world, int x, int y, int z, int meta) {
		switch(meta) {
		case 1 : return new BlockSwapper(world, new ChunkCoordinates(x, y, z),  Blocks.dirt, 2);
		case 2 : return new BlockSwapper(world, new ChunkCoordinates(x, y, z),  Blocks.mycelium, 0);
		case 3 : return new BlockSwapper(world, new ChunkCoordinates(x, y, z),  ModBlocks.altGrass, 0);
		case 4 : return new BlockSwapper(world, new ChunkCoordinates(x, y, z),  ModBlocks.altGrass, 1);
		case 5 : return new BlockSwapper(world, new ChunkCoordinates(x, y, z),  ModBlocks.altGrass, 2);
		case 6 : return new BlockSwapper(world, new ChunkCoordinates(x, y, z),  ModBlocks.altGrass, 3);
		case 7 : return new BlockSwapper(world, new ChunkCoordinates(x, y, z),  ModBlocks.altGrass, 4);
		case 8 : return new BlockSwapper(world, new ChunkCoordinates(x, y, z),  ModBlocks.altGrass, 5);
		default : return new BlockSwapper(world, new ChunkCoordinates(x, y, z),  Blocks.grass, 0);
		}
	}

	private static class BlockSwapper {

		final World world;
		final Random rand;
		final Block blockToSet;
		final int metaToSet;

		ChunkCoordinates startCoords;
		int ticksExisted = 0;

		BlockSwapper(World world, ChunkCoordinates coords, Block block, int meta) {
			this.world = world;
			blockToSet = block;
			metaToSet = meta;
			rand = new Random(coords.posX ^ coords.posY ^ coords.posZ);
			startCoords = coords;
		}

		void tick(List<BlockSwapper> list) {
			++ticksExisted;

			int range = 3;
			for(int i = -range; i < range + 1; i++)
				for(int j = -range; j < range + 1; j++) {
					int x = startCoords.posX + i;
					int y = startCoords.posY;
					int z = startCoords.posZ + j;
					Block block = world.getBlock(x, y, z);
					int meta = world.getBlockMetadata(x, y, z);

					if(block == blockToSet && meta == metaToSet) {
						if(ticksExisted % 20 == 0) {
							List<ChunkCoordinates> validCoords = new ArrayList();
							for(int k = -1; k < 2; k++)
								for(int l = -1; l < 2; l++) {
									int x1 = x + k;
									int z1 = z + l;
									Block block1 = world.getBlock(x1, y, z1);
									int meta1 = world.getBlockMetadata(x1, y, z1);
									if((block1 == Blocks.dirt || block1 == Blocks.grass) && meta1 == 0)
										validCoords.add(new ChunkCoordinates(x1, y, z1));
								}

							if(!validCoords.isEmpty() && !world.isRemote) {
								ChunkCoordinates coords = validCoords.get(rand.nextInt(validCoords.size()));
								world.setBlock(coords.posX, coords.posY, coords.posZ, blockToSet, metaToSet, 1 | 2);
							}
						}
					}
				}

			if(ticksExisted >= 80)
				list.remove(this);
		}
	}

	public static IslandType getIslandType(ItemStack stack) {
		return ISLAND_TYPES[Math.min(stack.getItemDamage(), ISLAND_TYPES.length - 1)];
	}

}
