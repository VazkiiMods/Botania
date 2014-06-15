package vazkii.botania.common.block.decor.slabs;

import java.util.Random;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;

public abstract class BlockLivingSlab extends BlockModSlab {

	Block source;
	int meta;
	
	public BlockLivingSlab(boolean full, Block source, int meta) {
		super(full, source.getMaterial(), source.getUnlocalizedName().replaceAll("tile.", "") + meta + "Slab" + (full ? "Full" : ""));
		setStepSound(source.stepSound);
		this.source = source;
		this.meta = meta;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public IIcon getIcon(int par1, int par2) {
		return source.getIcon(par1, meta);
	}

}
