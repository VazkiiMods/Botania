/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * File Created @ [Jan 28, 2015, 2:59:06 PM (GMT)]
 */
package vazkii.botania.common.item;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import vazkii.botania.api.state.enums.CratePattern;
import vazkii.botania.common.block.tile.TileCraftCrate;
import vazkii.botania.common.lib.LibItemNames;

import javax.annotation.Nonnull;
import java.util.Locale;

public class ItemCraftPattern extends ItemMod {
	public final CratePattern pattern;

	public ItemCraftPattern(CratePattern pattern) {
		super(LibItemNames.CRAFT_PATTERN + "_" + pattern.name().toLowerCase(Locale.ROOT));
		this.pattern = pattern;
		setMaxStackSize(1);
	}

	@Nonnull
	@Override
	public EnumActionResult onItemUse(EntityPlayer p, World world, BlockPos pos, EnumHand hand, EnumFacing side, float xs, float ys, float zs) {
		TileEntity tile = world.getTileEntity(pos);
		if(tile != null && tile instanceof TileCraftCrate) {
			TileCraftCrate crate = (TileCraftCrate) tile;
			crate.pattern = this.pattern;
			world.notifyBlockUpdate(pos, world.getBlockState(pos), world.getBlockState(pos), 8);
			return EnumActionResult.SUCCESS;
		}
		return EnumActionResult.PASS;
	}
}
