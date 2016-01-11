/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 * 
 * File Created @ [May 15, 2015, 6:55:34 PM (GMT)]
 */
package vazkii.botania.common.item.equipment.tool.terrasteel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.minecraft.block.Block;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.api.item.ISequentialBreaker;
import vazkii.botania.common.item.ItemTemperanceStone;
import vazkii.botania.common.item.equipment.tool.ToolCommons;
import vazkii.botania.common.item.equipment.tool.manasteel.ItemManasteelAxe;
import vazkii.botania.common.item.relic.ItemLokiRing;
import vazkii.botania.common.lib.LibItemNames;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.Phase;

public class ItemTerraAxe extends ItemManasteelAxe implements ISequentialBreaker {

	private static final int MANA_PER_DAMAGE = 100;
	private static Map<Integer, List<BlockSwapper>> blockSwappers = new HashMap();

	public ItemTerraAxe() {
		super(BotaniaAPI.terrasteelToolMaterial, LibItemNames.TERRA_AXE);
		MinecraftForge.EVENT_BUS.register(this);
	}

	public boolean shouldBreak(EntityPlayer player) {
		return !player.isSneaking() && !ItemTemperanceStone.hasTemperanceActive(player);
	}

	@Override
	public boolean onBlockStartBreak(ItemStack stack, BlockPos pos, EntityPlayer player) {
		MovingObjectPosition raycast = ToolCommons.raytraceFromEntity(player.worldObj, player, true, 10);
		if(raycast != null) {
			breakOtherBlock(player, stack, pos, pos, raycast.sideHit);
			ItemLokiRing.breakOnAllCursors(player, this, stack, pos, raycast.sideHit);
		}

		return false;
	}

	@Override
	public int getManaPerDamage() {
		return MANA_PER_DAMAGE;
	}

	@Override
	public void breakOtherBlock(EntityPlayer player, ItemStack stack, BlockPos pos, BlockPos originPos, EnumFacing side) {
		if(shouldBreak(player)) {
			BlockPos coords = new BlockPos(pos);
			addBlockSwapper(player.worldObj, player, stack, coords, coords, 32, false, true, new ArrayList());
		}
	}

	@Override
	public boolean disposeOfTrashBlocks(ItemStack stack) {
		return false;
	}

	@SubscribeEvent
	public void onTickEnd(TickEvent.WorldTickEvent event) {
		if(event.phase == Phase.END) {
			int dim = event.world.provider.getDimensionId();
			if(blockSwappers.containsKey(dim)) {
				List<BlockSwapper> swappers = blockSwappers.get(dim);
				List<BlockSwapper> swappersSafe = new ArrayList(swappers);
				swappers.clear();

				for(BlockSwapper s : swappersSafe)
					if(s != null)
						s.tick();
			}
		}
	}

	private static BlockSwapper addBlockSwapper(World world, EntityPlayer player, ItemStack stack, BlockPos origCoords, BlockPos coords, int steps, boolean leaves, boolean force, List<String> posChecked) {
		BlockSwapper swapper = new BlockSwapper(world, player, stack, origCoords, coords, steps, leaves, force, posChecked);

		int dim = world.provider.getDimensionId();
		if(!blockSwappers.containsKey(dim))
			blockSwappers.put(dim, new ArrayList());
		blockSwappers.get(dim).add(swapper);

		return swapper;
	}

	private static class BlockSwapper {

		final World world;
		final EntityPlayer player;
		final ItemStack stack;
		final BlockPos origCoords;
		final int steps;
		final BlockPos coords;
		final boolean leaves;
		final boolean force;
		final List<String> posChecked;
		BlockSwapper(World world, EntityPlayer player, ItemStack stack, BlockPos origCoords, BlockPos coords, int steps, boolean leaves, boolean force, List<String> posChecked) {
			this.world = world;
			this.player = player;
			this.stack = stack;
			this.origCoords = origCoords;
			this.coords = coords;
			this.steps = steps;
			this.leaves = leaves;
			this.force = force;
			this.posChecked = posChecked;
		}

		void tick() {
			Block blockat = world.getBlockState(coords).getBlock();
			if(!force && blockat.isAir(world, coords))
				return;

			ToolCommons.removeBlockWithDrops(player, stack, world, coords, origCoords, null, ToolCommons.materialsAxe, EnchantmentHelper.getEnchantmentLevel(Enchantment.silkTouch.effectId, stack) > 0, EnchantmentHelper.getEnchantmentLevel(Enchantment.fortune.effectId, stack), 0F, false, !leaves);

			if(steps == 0)
				return;

			for(int i = 0; i < 3; i++)
				for(int j = 0; j < 3; j++)
					for(int k = 0; k < 3; k++) {
						BlockPos pos = coords.add(i - 1, j - 1, k - 1);
						String pstr = posStr(pos);
						if(posChecked.contains(pstr))
							continue;

						Block block = world.getBlockState(pos).getBlock();
						boolean log = block.isWood(world, pos);
						boolean leaf = block.isLeaves(world, pos);
						if(log || leaf) {
							int steps = this.steps - 1;
							steps = leaf ? leaves ? steps : 3 : steps;
							addBlockSwapper(world, player, stack, origCoords, pos, steps, leaf, false, posChecked);
							posChecked.add(pstr);
						}
					}
		}

		String posStr(BlockPos pos) {
			return pos.getX() + ":" + pos.getY() + ":" + pos.getZ();
		}
	}

}
