/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 * 
 * File Created @ [Jul 7, 2015, 7:59:10 PM (GMT)]
 */
package vazkii.botania.common.block;

import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.monster.EntityBlaze;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import vazkii.botania.api.lexicon.ILexiconable;
import vazkii.botania.api.lexicon.LexiconEntry;
import vazkii.botania.client.core.helper.IconHelper;
import vazkii.botania.common.lexicon.LexiconData;
import vazkii.botania.common.lib.LibBlockNames;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockFelPumpkin extends BlockMod implements ILexiconable {

	private static final String TAG_FEL_SPAWNED = "Botania-FelSpawned";

	IIcon top, face;

	public BlockFelPumpkin() {
		super(Material.gourd);
		setBlockName(LibBlockNames.FEL_PUMPKIN);
		setHardness(1F);
		setStepSound(soundTypeWood);
		MinecraftForge.EVENT_BUS.register(this);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public IIcon getIcon(int p_149691_1_, int p_149691_2_) {
		return p_149691_1_ == 1 ? top : p_149691_1_ == 0 ? top : p_149691_2_ == 2 && p_149691_1_ == 2 ? face : p_149691_2_ == 3 && p_149691_1_ == 5 ? face : p_149691_2_ == 0 && p_149691_1_ == 3 ? face : p_149691_2_ == 1 && p_149691_1_ == 4 ? face : blockIcon;
	}

	@Override
	public void onBlockAdded(World p_149726_1_, int p_149726_2_, int p_149726_3_, int p_149726_4_) {
		super.onBlockAdded(p_149726_1_, p_149726_2_, p_149726_3_, p_149726_4_);

		if(!p_149726_1_.isRemote && p_149726_1_.getBlock(p_149726_2_, p_149726_3_ - 1, p_149726_4_) == Blocks.iron_bars && p_149726_1_.getBlock(p_149726_2_, p_149726_3_ - 2, p_149726_4_) == Blocks.iron_bars) {
			p_149726_1_.setBlock(p_149726_2_, p_149726_3_, p_149726_4_, getBlockById(0), 0, 2);
			p_149726_1_.setBlock(p_149726_2_, p_149726_3_ - 1, p_149726_4_, getBlockById(0), 0, 2);
			p_149726_1_.setBlock(p_149726_2_, p_149726_3_ - 2, p_149726_4_, getBlockById(0), 0, 2);
			EntityBlaze blaze = new EntityBlaze(p_149726_1_);
			blaze.setLocationAndAngles(p_149726_2_ + 0.5D, p_149726_3_ - 1.95D, p_149726_4_ + 0.5D, 0.0F, 0.0F);
			blaze.getEntityData().setBoolean(TAG_FEL_SPAWNED, true);
			p_149726_1_.spawnEntityInWorld(blaze);
			p_149726_1_.notifyBlockChange(p_149726_2_, p_149726_3_, p_149726_4_, getBlockById(0));
			p_149726_1_.notifyBlockChange(p_149726_2_, p_149726_3_ - 1, p_149726_4_, getBlockById(0));
			p_149726_1_.notifyBlockChange(p_149726_2_, p_149726_3_ - 2, p_149726_4_, getBlockById(0));
		}
	}

	@Override
	public void onBlockPlacedBy(World p_149689_1_, int p_149689_2_, int p_149689_3_, int p_149689_4_, EntityLivingBase p_149689_5_, ItemStack p_149689_6_) {
		int l = MathHelper.floor_double(p_149689_5_.rotationYaw * 4.0F / 360.0F + 2.5D) & 3;
		p_149689_1_.setBlockMetadataWithNotify(p_149689_2_, p_149689_3_, p_149689_4_, l, 2);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerBlockIcons(IIconRegister p_149651_1_)  {
		face = IconHelper.forBlock(p_149651_1_, this);
		top = Blocks.pumpkin.getIcon(0, 0);
		blockIcon = Blocks.pumpkin.getIcon(2, 0);
	}

	@SubscribeEvent
	public void onDrops(LivingDropsEvent event) {
		if(event.entity instanceof EntityBlaze && event.entity.getEntityData().getBoolean(TAG_FEL_SPAWNED))
			if(event.drops.isEmpty())
				event.drops.add(new EntityItem(event.entity.worldObj, event.entity.posX, event.entity.posY, event.entity.posZ, new ItemStack(Items.blaze_powder, 6)));
			else for(EntityItem item : event.drops) {
				ItemStack stack = item.getEntityItem();
				if(stack.getItem() == Items.blaze_rod)
					item.setEntityItemStack(new ItemStack(Items.blaze_powder, stack.stackSize * 10));
			}
	}

	@Override
	public LexiconEntry getEntry(World world, int x, int y, int z, EntityPlayer player, ItemStack lexicon) {
		return LexiconData.gardenOfGlass;
	}

}
