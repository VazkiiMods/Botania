package vazkii.botania.common.item.equipment.tool.elementium;

import net.minecraft.block.Block;
import net.minecraft.block.BlockFalling;
import net.minecraft.block.material.Material;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Enchantments;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;
import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.common.item.ModItems;
import vazkii.botania.common.item.equipment.tool.ToolCommons;
import vazkii.botania.common.item.equipment.tool.manasteel.ItemManasteelShovel;
import vazkii.botania.common.lib.LibItemNames;

import javax.annotation.Nonnull;
import java.util.Arrays;
import java.util.List;

public class ItemElementiumShovel extends ItemManasteelShovel {

	public static final List<Material> materialsShovel = Arrays.asList(Material.GRASS, Material.GROUND, Material.SAND, Material.SNOW, Material.CRAFTED_SNOW, Material.CLAY);

	public ItemElementiumShovel() {
		super(BotaniaAPI.elementiumToolMaterial, LibItemNames.ELEMENTIUM_SHOVEL);
	}

	@Override
	public boolean onBlockStartBreak(ItemStack stack, BlockPos pos, EntityPlayer player) {
		World world = player.world;
		Material mat = world.getBlockState(pos).getMaterial();
		if (!ToolCommons.materialsShovel.contains(mat))
			return false;

		RayTraceResult block = ToolCommons.raytraceFromEntity(world, player, true, 10);
		if (block == null)
			return false;

		Block blk = world.getBlockState(pos).getBlock();
		if(blk instanceof BlockFalling)
			ToolCommons.removeBlocksInIteration(player, stack, world, pos, new Vec3i(0, -12, 0), new Vec3i(1, 12, 1),
					state -> state.getBlock() == blk,
					false);

		return false;
	}

	@Override
	public boolean getIsRepairable(ItemStack toRepair, @Nonnull ItemStack repairBy) {
		return repairBy.getItem() == ModItems.manaResource && repairBy.getItemDamage() == 7 ? true : super.getIsRepairable(toRepair, repairBy);
	}

}
