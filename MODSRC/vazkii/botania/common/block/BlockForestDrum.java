/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under a
 * Creative Commons Attribution-NonCommercial-ShareAlike 3.0 License
 * (http://creativecommons.org/licenses/by-nc-sa/3.0/deed.en_GB)
 * 
 * File Created @ [May 16, 2014, 7:34:37 PM (GMT)]
 */
package vazkii.botania.common.block;

import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import vazkii.botania.api.internal.IManaBurst;
import vazkii.botania.api.mana.IManaTrigger;
import vazkii.botania.client.core.helper.IconHelper;
import vazkii.botania.common.lib.LibBlockNames;

public class BlockForestDrum extends BlockMod implements IManaTrigger {

	IIcon iconBases, iconFaces;
	
	public BlockForestDrum() {
		super(Material.wood);
		float f = 1F / 16F;
		setBlockBounds(f * 3, 0F, f * 3, 1F - f * 3, 1F - f * 2, 1F - f * 3);
		
		setHardness(2.0F);
		setStepSound(soundTypeWood);
		setBlockName(LibBlockNames.FOREST_DRUM);
	}
	
	@Override
	public boolean renderAsNormalBlock() {
		return false;
	}

	@Override
	public boolean isOpaqueCube() {
		return false;
	}
	
	@Override
	public void registerBlockIcons(IIconRegister par1IconRegister) {
		iconBases = IconHelper.forBlock(par1IconRegister, this, 0);
		iconFaces = IconHelper.forBlock(par1IconRegister, this, 1);
	}
	
	@Override
	public IIcon getIcon(int side, int meta) {
		return side < 2 ? iconBases : iconFaces;
	}

	@Override
	public void onBurstCollision(IManaBurst burst, World world, int x, int y, int z) {
		System.out.println("collide! " + world.isRemote);
	}
	
}
