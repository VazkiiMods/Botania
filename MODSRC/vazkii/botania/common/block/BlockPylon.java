/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under a
 * Creative Commons Attribution-NonCommercial-ShareAlike 3.0 License
 * (http://creativecommons.org/licenses/by-nc-sa/3.0/deed.en_GB)
 *
 * File Created @ [Feb 18, 2014, 10:13:02 PM (GMT)]
 */
package vazkii.botania.common.block;

import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import vazkii.botania.api.ILexiconable;
import vazkii.botania.api.lexicon.LexiconEntry;
import vazkii.botania.client.lib.LibRenderIDs;
import vazkii.botania.common.block.tile.TilePylon;
import vazkii.botania.common.lexicon.LexiconData;
import vazkii.botania.common.lib.LibBlockNames;

public class BlockPylon extends BlockModContainer implements ILexiconable {

    public BlockPylon() {
        super(Material.iron);
        setHardness(5.5F);
        setStepSound(soundTypeMetal);
        setBlockName(LibBlockNames.PYLON);

        float f = 1F / 16F * 2F;
        setBlockBounds(f, 0F, f, 1F - f, 1F / 16F * 21F, 1F - f);
    }

    @Override
    public void registerBlockIcons(IIconRegister par1IconRegister) {
        // NO-OP
    }

    @Override
    public IIcon getIcon(int par1, int par2) {
        return Blocks.diamond_block.getIcon(par1, par2);
    }

    @Override
    public boolean isOpaqueCube() {
        return false;
    }

    @Override
    public boolean renderAsNormalBlock() {
        return false;
    }

    @Override
    public int getRenderType() {
        return LibRenderIDs.idPylon;
    }

    @Override
    public float getEnchantPowerBonus(World world, int x, int y, int z) {
        return 8;
    }

    @Override
    public TileEntity createNewTileEntity(World world, int meta) {
        return new TilePylon();
    }

    @Override
    public LexiconEntry getEntry(World world, int x, int y, int z, EntityPlayer player, ItemStack lexicon) {
        return LexiconData.pylon;
    }
}
