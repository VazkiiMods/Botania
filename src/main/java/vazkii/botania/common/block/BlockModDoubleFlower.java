/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 * 
 * File Created @ [Mar 22, 2015, 7:46:55 PM (GMT)]
 */
package vazkii.botania.common.block;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.BlockDoublePlant;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.event.ForgeEventFactory;
import vazkii.botania.api.lexicon.ILexiconable;
import vazkii.botania.api.lexicon.LexiconEntry;
import vazkii.botania.client.core.helper.IconHelper;
import vazkii.botania.client.lib.LibRenderIDs;
import vazkii.botania.common.Botania;
import vazkii.botania.common.core.BotaniaCreativeTab;
import vazkii.botania.common.core.handler.ConfigHandler;
import vazkii.botania.common.item.block.ItemBlockWithMetadataAndName;
import vazkii.botania.common.lexicon.LexiconData;
import vazkii.botania.common.lib.LibBlockNames;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockModDoubleFlower extends BlockDoublePlant implements ILexiconable {

	private static final int COUNT = 8;

	IIcon[] doublePlantTopIcons, doublePlantBottomIcons;
	IIcon[] doublePlantTopIconsAlt, doublePlantBottomIconsAlt;

	final int offset;

	public BlockModDoubleFlower(boolean second) {
		offset = second ? 8 : 0;
		setBlockName(LibBlockNames.DOUBLE_FLOWER + (second ? 2 : 1));
		setHardness(0F);
		setStepSound(soundTypeGrass);
		setTickRandomly(false);
		setCreativeTab(BotaniaCreativeTab.INSTANCE);
	}

	@Override
	public Block setBlockName(String par1Str) {
		if(!par1Str.equals("doublePlant"))
			GameRegistry.registerBlock(this, ItemBlockWithMetadataAndName.class, par1Str);
		return super.setBlockName(par1Str);
	}

	@Override
	public Item getItemDropped(int p_149650_1_, Random p_149650_2_, int p_149650_3_) {
		return null;
	}

	@Override
	public int damageDropped(int p_149692_1_) {
		return p_149692_1_ & 7;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public IIcon func_149888_a(boolean top, int index) {
		return (ConfigHandler.altFlowerTextures ? top ? doublePlantTopIconsAlt : doublePlantBottomIconsAlt : top ? doublePlantTopIcons : doublePlantBottomIcons)[index & 7];
	}

	@Override
	public void func_149889_c(World p_149889_1_, int p_149889_2_, int p_149889_3_, int p_149889_4_, int p_149889_5_, int p_149889_6_) {
		p_149889_1_.setBlock(p_149889_2_, p_149889_3_, p_149889_4_, this, p_149889_5_, p_149889_6_);
		p_149889_1_.setBlock(p_149889_2_, p_149889_3_ + 1, p_149889_4_, this, p_149889_5_ | 8, p_149889_6_);
	}

	@Override
	public void onBlockPlacedBy(World p_149689_1_, int p_149689_2_, int p_149689_3_, int p_149689_4_, EntityLivingBase p_149689_5_, ItemStack p_149689_6_) {
		p_149689_1_.setBlock(p_149689_2_, p_149689_3_ + 1, p_149689_4_, this, p_149689_6_.getItemDamage() | 8, 2);
	}

	@Override
	public boolean func_149851_a(World world, int x, int y, int z, boolean fuckifiknow) {
		return false;
	}

	@Override
	public void harvestBlock(World p_149636_1_, EntityPlayer p_149636_2_, int p_149636_3_, int p_149636_4_, int p_149636_5_, int p_149636_6_) {
		if(p_149636_1_.isRemote || p_149636_2_.getCurrentEquippedItem() == null || p_149636_2_.getCurrentEquippedItem().getItem() != Items.shears || func_149887_c(p_149636_6_))
			harvestBlockCopy(p_149636_1_, p_149636_2_, p_149636_3_, p_149636_4_, p_149636_5_, p_149636_6_);
	}

	// This is how I get around encapsulation
	public void harvestBlockCopy(World p_149636_1_, EntityPlayer p_149636_2_, int p_149636_3_, int p_149636_4_, int p_149636_5_, int p_149636_6_) {
		p_149636_2_.addStat(StatList.mineBlockStatArray[getIdFromBlock(this)], 1);
		p_149636_2_.addExhaustion(0.025F);

		if(this.canSilkHarvest(p_149636_1_, p_149636_2_, p_149636_3_, p_149636_4_, p_149636_5_, p_149636_6_) && EnchantmentHelper.getSilkTouchModifier(p_149636_2_)) {
			ArrayList<ItemStack> items = new ArrayList<ItemStack>();
			ItemStack itemstack = createStackedBlock(p_149636_6_);

			if(itemstack != null)
				items.add(itemstack);

			ForgeEventFactory.fireBlockHarvesting(items, p_149636_1_, this, p_149636_3_, p_149636_4_, p_149636_5_, p_149636_6_, 0, 1.0f, true, p_149636_2_);
			for(ItemStack is : items)
				this.dropBlockAsItem(p_149636_1_, p_149636_3_, p_149636_4_, p_149636_5_, is);
		} else {
			harvesters.set(p_149636_2_);
			int i1 = EnchantmentHelper.getFortuneModifier(p_149636_2_);
			this.dropBlockAsItem(p_149636_1_, p_149636_3_, p_149636_4_, p_149636_5_, p_149636_6_, i1);
			harvesters.set(null);
		}
	}

	@Override
	public void onBlockHarvested(World p_149681_1_, int p_149681_2_, int p_149681_3_, int p_149681_4_, int p_149681_5_, EntityPlayer p_149681_6_) {
		if(func_149887_c(p_149681_5_)) {
			if(p_149681_1_.getBlock(p_149681_2_, p_149681_3_ - 1, p_149681_4_) == this) {
				if(!p_149681_6_.capabilities.isCreativeMode) {
					int i1 = p_149681_1_.getBlockMetadata(p_149681_2_, p_149681_3_ - 1, p_149681_4_);
					int j1 = func_149890_d(i1);

					if(j1 != 3 && j1 != 2);
					//p_149681_1_.func_147480_a(p_149681_2_, p_149681_3_ - 1, p_149681_4_, true);
					else {
						/*if (!p_149681_1_.isRemote && p_149681_6_.getCurrentEquippedItem() != null && p_149681_6_.getCurrentEquippedItem().getItem() == Items.shears)
                        {
                            this.func_149886_b(p_149681_1_, p_149681_2_, p_149681_3_, p_149681_4_, i1, p_149681_6_);
                        }*/

						p_149681_1_.setBlockToAir(p_149681_2_, p_149681_3_ - 1, p_149681_4_);
					}
				} else p_149681_1_.setBlockToAir(p_149681_2_, p_149681_3_ - 1, p_149681_4_);
			}
		} else if(p_149681_6_.capabilities.isCreativeMode && p_149681_1_.getBlock(p_149681_2_, p_149681_3_ + 1, p_149681_4_) == this)
			p_149681_1_.setBlock(p_149681_2_, p_149681_3_ + 1, p_149681_4_, Blocks.air, 0, 2);

		//super.onBlockHarvested(p_149681_1_, p_149681_2_, p_149681_3_, p_149681_4_, p_149681_5_, p_149681_6_);
	}

	@Override
	public boolean isShearable(ItemStack item, IBlockAccess world, int x, int y, int z) {
		return true;
	}

	@Override
	public ArrayList<ItemStack> onSheared(ItemStack item, IBlockAccess world, int x, int y, int z, int fortune) {
		ArrayList<ItemStack> ret = new ArrayList<ItemStack>();
		ret.add(new ItemStack(this, 1, world.getBlockMetadata(x, y, z) & 7));
		return ret;
	}

	@Override
	public ArrayList<ItemStack> getDrops(World world, int x, int y, int z, int meta, int fortune) {
		return new ArrayList();
	}

	@Override
	public IIcon getIcon(int p_149691_1_, int p_149691_2_) {
		boolean top = func_149887_c(p_149691_2_);
		return (ConfigHandler.altFlowerTextures ? top ? doublePlantTopIconsAlt : doublePlantBottomIconsAlt : top ? doublePlantTopIcons : doublePlantBottomIcons)[p_149691_2_ & 7];
	}

	@Override
	public IIcon getIcon(IBlockAccess world, int x, int y, int z, int side) {
		int meta = world.getBlockMetadata(x, y, z);
		boolean top = func_149887_c(meta);
		if(top)
			meta = world.getBlockMetadata(x, y - 1, z);

		return (ConfigHandler.altFlowerTextures ? top ? doublePlantBottomIconsAlt : doublePlantTopIconsAlt : top ? doublePlantBottomIcons : doublePlantTopIcons)[meta & 7];
	}

	@Override
	public void registerBlockIcons(IIconRegister register) {
		doublePlantTopIcons = new IIcon[COUNT];
		doublePlantBottomIcons = new IIcon[COUNT];
		doublePlantTopIconsAlt = new IIcon[COUNT];
		doublePlantBottomIconsAlt = new IIcon[COUNT];
		for(int i = 0; i < COUNT; i++) {
			int off = offset(i);
			doublePlantTopIcons[i] = IconHelper.forName(register, "flower" + off + "Tall0");
			doublePlantBottomIcons[i] = IconHelper.forName(register, "flower" + off + "Tall1");
			doublePlantTopIconsAlt[i] = IconHelper.forName(register, "flower" + off + "Tall0", BlockModFlower.ALT_DIR);
			doublePlantBottomIconsAlt[i] = IconHelper.forName(register, "flower" + off + "Tall1", BlockModFlower.ALT_DIR);
		}
	}

	@Override
	public int colorMultiplier(IBlockAccess blockAccess, int x, int y, int z) {
		return 16777215;
	}

	@Override
	public void getSubBlocks(Item p_149666_1_, CreativeTabs p_149666_2_, List p_149666_3_) {
		for(int i = 0; i < COUNT; ++i)
			p_149666_3_.add(new ItemStack(p_149666_1_, 1, i));
	}

	@Override
	public int getRenderType() {
		return LibRenderIDs.idDoubleFlower;
	}

	@Override
	public void randomDisplayTick(World par1World, int par2, int par3, int par4, Random par5Random) {
		int meta = par1World.getBlockMetadata(par2, par3, par4);
		float[] color = EntitySheep.fleeceColorTable[offset(meta & 7)];

		if(par5Random.nextDouble() < ConfigHandler.flowerParticleFrequency)
			Botania.proxy.sparkleFX(par1World, par2 + 0.3 + par5Random.nextFloat() * 0.5, par3 + 0.5 + par5Random.nextFloat() * 0.5, par4 + 0.3 + par5Random.nextFloat() * 0.5, color[0], color[1], color[2], par5Random.nextFloat(), 5);
	}

	@Override
	public LexiconEntry getEntry(World world, int x, int y, int z, EntityPlayer player, ItemStack lexicon) {
		return LexiconData.flowers;
	}

	int offset(int meta) {
		return meta + offset;
	}

}
