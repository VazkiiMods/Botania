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
import net.minecraft.block.BlockDirt;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import vazkii.botania.api.state.BotaniaStateProps;
import vazkii.botania.api.state.enums.AltGrassVariant;
import vazkii.botania.client.core.helper.IconHelper;
import vazkii.botania.common.Botania;
import vazkii.botania.common.block.ModBlocks;
import vazkii.botania.common.block.decor.IFloatingFlower.IslandType;
import vazkii.botania.common.lib.LibItemNames;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.Phase;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemGrassSeeds extends ItemMod {

	private static Map<Integer, List<BlockSwapper>> blockSwappers = new HashMap();

	private static final IslandType[] ISLAND_TYPES = {
		IslandType.GRASS, IslandType.PODZOL, IslandType.MYCEL,
		IslandType.DRY, IslandType.GOLDEN, IslandType.VIVID,
		IslandType.SCORCHED, IslandType.INFUSED, IslandType.MUTATED
	};

	private static final int SUBTYPES = 9;

	public ItemGrassSeeds() {
		super();
		setUnlocalizedName(LibItemNames.GRASS_SEEDS);
		setHasSubtypes(true);
		MinecraftForge.EVENT_BUS.register(this);
	}

	@Override
	public void getSubItems(Item par1, CreativeTabs par2, List par3) {
		for(int i = 0; i < SUBTYPES; i++)
			par3.add(new ItemStack(par1, 1, i));
	}

	@Override
	public String getUnlocalizedName(ItemStack stack) {
		return super.getUnlocalizedName() + stack.getItemDamage();
	}

	@Override
	public boolean onItemUse(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, World par3World, BlockPos pos, EnumFacing side, float par8, float par9, float par10) {
		IBlockState state = par3World.getBlockState(pos);

		if((state.getBlock() == Blocks.dirt && state.getValue(BlockDirt.VARIANT) == BlockDirt.DirtType.DIRT) || (state.getBlock() == Blocks.grass && par1ItemStack.getItemDamage() != 0)) {
			int meta = par1ItemStack.getItemDamage();

			BlockSwapper swapper = addBlockSwapper(par3World, pos, meta);
			par3World.setBlockState(pos, swapper.stateToSet, 1 | 2);
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

				Botania.proxy.wispFX(par3World, pos.getX() + 0.5 + x, pos.getY() + 0.5 + y, pos.getZ() + 0.5 + z, r, g, b, (float) Math.random() * 0.15F + 0.15F, (float) -x * velMul, (float) -y * velMul, (float) -z * velMul);
			}

			par1ItemStack.stackSize--;
		}

		return true;
	}

	@SubscribeEvent
	public void onTickEnd(TickEvent.WorldTickEvent event) {
		if(event.phase == Phase.END) {
			int dim = event.world.provider.getDimensionId();
			if(blockSwappers.containsKey(dim)) {
				List<BlockSwapper> swappers = blockSwappers.get(dim);
				List<BlockSwapper> swappersSafe = new ArrayList(swappers);

				for(BlockSwapper s : swappersSafe)
					s.tick(swappers);
			}
		}
	}

	private static BlockSwapper addBlockSwapper(World world, BlockPos pos, int meta) {
		BlockSwapper swapper = swapperFromMeta(world, pos, meta);

		int dim = world.provider.getDimensionId();
		if(!blockSwappers.containsKey(dim))
			blockSwappers.put(dim, new ArrayList());
		blockSwappers.get(dim).add(swapper);

		return swapper;
	}

	private static BlockSwapper swapperFromMeta(World world, BlockPos pos, int meta) {
		switch(meta) {
		case 1 : return new BlockSwapper(world, pos,  Blocks.dirt.getDefaultState().withProperty(BlockDirt.VARIANT, BlockDirt.DirtType.PODZOL));
		case 2 : return new BlockSwapper(world, pos,  Blocks.mycelium.getDefaultState());
		case 3 : return new BlockSwapper(world, pos,  ModBlocks.altGrass.getDefaultState().withProperty(BotaniaStateProps.ALTGRASS_VARIANT, AltGrassVariant.DRY));
		case 4 : return new BlockSwapper(world, pos,  ModBlocks.altGrass.getDefaultState().withProperty(BotaniaStateProps.ALTGRASS_VARIANT, AltGrassVariant.GOLDEN));
		case 5 : return new BlockSwapper(world, pos,  ModBlocks.altGrass.getDefaultState().withProperty(BotaniaStateProps.ALTGRASS_VARIANT, AltGrassVariant.VIVID));
		case 6 : return new BlockSwapper(world, pos,  ModBlocks.altGrass.getDefaultState().withProperty(BotaniaStateProps.ALTGRASS_VARIANT, AltGrassVariant.SCORCHED));
		case 7 : return new BlockSwapper(world, pos,  ModBlocks.altGrass.getDefaultState().withProperty(BotaniaStateProps.ALTGRASS_VARIANT, AltGrassVariant.INFUSED));
		case 8 : return new BlockSwapper(world, pos,  ModBlocks.altGrass.getDefaultState().withProperty(BotaniaStateProps.ALTGRASS_VARIANT, AltGrassVariant.MUTATED));
		default : return new BlockSwapper(world, pos,  Blocks.grass.getDefaultState());
		}
	}

	private static class BlockSwapper {

		final World world;
		final Random rand;
		final IBlockState stateToSet;
		BlockPos startCoords;
		int ticksExisted = 0;

		BlockSwapper(World world, BlockPos coords, IBlockState state) {
			this.world = world;
			stateToSet = state;
			rand = new Random(coords.hashCode());
			startCoords = coords;
		}

		void tick(List<BlockSwapper> list) {
			++ticksExisted;

			int range = 3;
			for(int i = -range; i < range + 1; i++)
				for(int j = -range; j < range + 1; j++) {
					BlockPos pos = startCoords.add(i, 0, j);
					IBlockState state = world.getBlockState(pos);
					Block block = state.getBlock();

					if(state == stateToSet) {
						if(ticksExisted % 20 == 0) {
							List<BlockPos> validCoords = new ArrayList();
							for(int k = -1; k < 2; k++)
								for(int l = -1; l < 2; l++) {
									BlockPos pos1 = pos.add(k, 0, l);
									IBlockState state1 = world.getBlockState(pos1);
									if((state1.getBlock() == Blocks.dirt
											&& state1.getValue(BlockDirt.VARIANT) == BlockDirt.DirtType.DIRT)
											|| state1.getBlock() == Blocks.grass)
										validCoords.add(pos1);
								}

							if(!validCoords.isEmpty() && !world.isRemote) {
								BlockPos coords = validCoords.get(rand.nextInt(validCoords.size()));
								world.setBlockState(coords, stateToSet, 1 | 2);
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
