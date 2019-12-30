package vazkii.botania.common.item.equipment.tool.terrasteel;

import java.util.Arrays;
import java.util.List;

import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.RayTraceContext;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;
import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.api.item.ISequentialBreaker;
import vazkii.botania.common.item.ItemTemperanceStone;
import vazkii.botania.common.item.equipment.tool.ToolCommons;
import vazkii.botania.common.item.equipment.tool.manasteel.ItemManasteelShovel;
import vazkii.botania.common.item.relic.ItemLokiRing;
import vazkii.botania.common.item.relic.ItemThorRing;

/**
 * This class was created by <ToMe25>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * File Created @ [Dec 30, 2019, 9:35 (GMT)]
 */
public class ItemTerraShovel extends ItemManasteelShovel implements ISequentialBreaker {

	/**
	 * The amount of mana required to restore 1 point of damage.
	 */
	private static final int MANA_PER_DAMAGE = 100;

	private static final List<Material> MATERIALS = Arrays.asList(Material.ORGANIC, Material.EARTH, Material.SAND,
			Material.SNOW, Material.SNOW_BLOCK, Material.CLAY);

	public ItemTerraShovel(Properties props) {
		super(BotaniaAPI.TERRASTEEL_ITEM_TIER, props);
	}

	@Override
	public boolean onBlockStartBreak(ItemStack stack, BlockPos pos, PlayerEntity player) {
		RayTraceResult raycast = ToolCommons.raytraceFromEntity(player.world, player, RayTraceContext.FluidMode.NONE, 10);
		if (raycast.getType() == RayTraceResult.Type.BLOCK) {
			Direction face = ((BlockRayTraceResult) raycast).getFace();
			breakOtherBlock(player, stack, pos, pos, face);
			ItemLokiRing.breakOnAllCursors(player, this, stack, pos, face);
		}

		return false;
	}

	@Override
	public int getManaPerDamage() {
		return MANA_PER_DAMAGE;
	}

	@Override
	public void breakOtherBlock(PlayerEntity player, ItemStack stack, BlockPos pos, BlockPos originPos, Direction side) {
		if(player.isSneaking())
			return;

		World world = player.world;
		Material mat = world.getBlockState(pos).getMaterial();
		if (!MATERIALS.contains(mat))
			return;

		if (world.isAirBlock(pos))
			return;

		boolean thor = !ItemThorRing.getThorRing(player).isEmpty();
		boolean doX = thor || side.getXOffset() == 0;
		boolean doY = thor || side.getYOffset() == 0;
		boolean doZ = thor || side.getZOffset() == 0;

		int range = 2 + (thor ? 1 : 0);
		if (ItemTemperanceStone.hasTemperanceActive(player))
			range = 1;

		int rangeY = Math.max(1, range);

		Vec3i beginDiff = new Vec3i(doX ? -range : 0, doY ? -1 : 0, doZ ? -range : 0);
		Vec3i endDiff = new Vec3i(doX ? range : 0, doY ? rangeY * 2 - 1 : 0, doZ ? range : 0);

		ToolCommons.removeBlocksInIteration(player, stack, world, pos, beginDiff, endDiff, state -> MATERIALS.contains(state.getMaterial()), false);
	}

	@Override
	public boolean disposeOfTrashBlocks(ItemStack stack) {
		return false;
	}

}
