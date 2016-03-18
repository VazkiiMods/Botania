package vazkii.botania.common.item.equipment.tool.elementium;

import net.minecraft.block.Block;
import net.minecraft.block.BlockFalling;
import net.minecraft.block.material.Material;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.common.item.equipment.tool.ToolCommons;
import vazkii.botania.common.item.equipment.tool.manasteel.ItemManasteelShovel;
import vazkii.botania.common.lib.LibItemNames;

public class ItemElementiumShovel extends ItemManasteelShovel {

	public static Material[] materialsShovel = new Material[]{ Material.grass, Material.ground, Material.sand, Material.snow, Material.craftedSnow, Material.clay };

	public ItemElementiumShovel() {
		super(BotaniaAPI.elementiumToolMaterial, LibItemNames.ELEMENTIUM_SHOVEL);
	}

	@Override
	public boolean onBlockStartBreak(ItemStack stack, BlockPos pos, EntityPlayer player) {
		World world = player.worldObj;
		Material mat = world.getBlockState(pos).getBlock().getMaterial();
		if (!ToolCommons.isRightMaterial(mat, materialsShovel))
			return false;

		RayTraceResult block = ToolCommons.raytraceFromEntity(world, player, true, 10);
		if (block == null)
			return false;

		int fortune = EnchantmentHelper.getFortuneModifier(player);
		boolean silk = EnchantmentHelper.getSilkTouchModifier(player);

		Block blk = world.getBlockState(pos).getBlock();
		if(blk instanceof BlockFalling)
			ToolCommons.removeBlocksInIteration(player, stack, world, pos, new BlockPos(0, -12, 0), new BlockPos(1, 12, 1), blk, materialsShovel, silk, fortune, false);

		return false;
	}

}
