package vazkii.botania.common.item.equipment.tool.elementium;

import net.minecraft.block.Block;
import net.minecraft.block.BlockFalling;
import net.minecraft.block.material.Material;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
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
	public boolean onBlockStartBreak(ItemStack stack, int x, int y, int z, EntityPlayer player) {
		World world = player.worldObj;
		Material mat = world.getBlock(x, y, z).getMaterial();
		if (!ToolCommons.isRightMaterial(mat, materialsShovel))
			return false;

		MovingObjectPosition block = ToolCommons.raytraceFromEntity(world, player, true, 10);
		if (block == null)
			return false;

		ForgeDirection.getOrientation(block.sideHit);
		int fortune = EnchantmentHelper.getFortuneModifier(player);
		boolean silk = EnchantmentHelper.getSilkTouchModifier(player);

		Block blk = world.getBlock(x, y, z);
		if(blk instanceof BlockFalling)
			ToolCommons.removeBlocksInIteration(player, stack, world, x, y, z, 0, -12, 0, 1, 12, 1, blk, materialsShovel, silk, fortune, false);

		return false;
	}

}
