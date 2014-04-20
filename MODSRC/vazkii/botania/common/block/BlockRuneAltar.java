/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under a
 * Creative Commons Attribution-NonCommercial-ShareAlike 3.0 License
 * (http://creativecommons.org/licenses/by-nc-sa/3.0/deed.en_GB)
 *
 * File Created @ [Feb 2, 2014, 2:10:14 PM (GMT)]
 */
package vazkii.botania.common.block;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import vazkii.botania.api.ILexiconable;
import vazkii.botania.api.IWandable;
import vazkii.botania.api.lexicon.LexiconEntry;
import vazkii.botania.client.core.helper.IconHelper;
import vazkii.botania.common.block.tile.TileRuneAltar;
import vazkii.botania.common.block.tile.TileSimpleInventory;
import vazkii.botania.common.lexicon.LexiconData;
import vazkii.botania.common.lib.LibBlockNames;

import java.util.Random;

public class BlockRuneAltar extends BlockModContainer implements IWandable, ILexiconable {

    Random random;
    IIcon[] icons;

    public BlockRuneAltar() {
        super(Material.rock);
        setBlockBounds(0F, 0F, 0F, 1F, 0.75F, 1F);
        setHardness(2.0F);
        setResistance(10.0F);
        setStepSound(soundTypeStone);
        setBlockName(LibBlockNames.RUNE_ALTAR);

        random = new Random();
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
    public void registerBlockIcons(IIconRegister par1IconRegister) {
        icons = new IIcon[3];
        for(int i = 0; i < icons.length; i++)
            icons[i] = IconHelper.forBlock(par1IconRegister, this, i);
    }

    @Override
    public boolean onBlockActivated(World par1World, int par2, int par3, int par4, EntityPlayer par5EntityPlayer, int par6, float par7, float par8, float par9) {
        ItemStack stack = par5EntityPlayer.getCurrentEquippedItem();
        if(stack != null)
            return ((TileRuneAltar) par1World.getTileEntity(par2, par3, par4)).addItem(par5EntityPlayer, stack);

        return false;
    }

    @Override
    public void breakBlock(World par1World, int par2, int par3, int par4, Block par5, int par6) {
        TileSimpleInventory inv = (TileSimpleInventory) par1World.getTileEntity(par2, par3, par4);

        if(inv != null) {
            for(int j1 = 0; j1 < inv.getSizeInventory(); ++j1) {
                ItemStack itemstack = inv.getStackInSlot(j1);

                if(itemstack != null) {
                    float f = random.nextFloat() * 0.8F + 0.1F;
                    float f1 = random.nextFloat() * 0.8F + 0.1F;
                    EntityItem entityitem;

                    for(float f2 = random.nextFloat() * 0.8F + 0.1F; itemstack.stackSize > 0; par1World.spawnEntityInWorld(entityitem)) {
                        int k1 = random.nextInt(21) + 10;

                        if(k1 > itemstack.stackSize) k1 = itemstack.stackSize;

                        itemstack.stackSize -= k1;
                        entityitem = new EntityItem(par1World, par2 + f, par3 + f1, par4 + f2, new ItemStack(itemstack.getItem(), k1, itemstack.getItemDamage()));
                        float f3 = 0.05F;
                        entityitem.motionX = (float) random.nextGaussian() * f3;
                        entityitem.motionY = (float) random.nextGaussian() * f3 + 0.2F;
                        entityitem.motionZ = (float) random.nextGaussian() * f3;

                        if(itemstack.hasTagCompound())
                            entityitem.getEntityItem().setTagCompound((NBTTagCompound) itemstack.getTagCompound().copy());
                    }
                }
            }

            par1World.func_147453_f(par2, par3, par4, par5);
        }

        super.breakBlock(par1World, par2, par3, par4, par5, par6);
    }

    @Override
    public IIcon getIcon(int par1, int par2) {
        return icons[Math.min(2, par1)];
    }

    @Override
    public TileEntity createNewTileEntity(World world, int meta) {
        return new TileRuneAltar();
    }

    @Override
    public boolean onUsedByWand(EntityPlayer player, ItemStack stack, World world, int x, int y, int z, int side) {
        ((TileRuneAltar) world.getTileEntity(x, y, z)).onWanded(player, stack);
        return true;
    }

    @Override
    public LexiconEntry getEntry(World world, int x, int y, int z, EntityPlayer player, ItemStack lexicon) {
        return LexiconData.runicAltar;
    }

}
