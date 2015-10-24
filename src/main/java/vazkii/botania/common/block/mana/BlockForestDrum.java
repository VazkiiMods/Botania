/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 * 
 * File Created @ [May 16, 2014, 7:34:37 PM (GMT)]
 */
package vazkii.botania.common.block.mana;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.passive.EntityCow;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import net.minecraftforge.common.IShearable;
import vazkii.botania.api.internal.IManaBurst;
import vazkii.botania.api.lexicon.ILexiconable;
import vazkii.botania.api.lexicon.LexiconEntry;
import vazkii.botania.api.mana.IManaTrigger;
import vazkii.botania.client.core.helper.IconHelper;
import vazkii.botania.common.block.BlockMod;
import vazkii.botania.common.item.ItemGrassHorn;
import vazkii.botania.common.item.block.ItemBlockWithMetadataAndName;
import vazkii.botania.common.lexicon.LexiconData;
import vazkii.botania.common.lib.LibBlockNames;
import cpw.mods.fml.common.registry.GameRegistry;

public class BlockForestDrum extends BlockMod implements IManaTrigger, ILexiconable {

	IIcon iconBases, iconFaces;
	IIcon iconBasesA, iconFacesA;
	IIcon iconBasesB, iconFacesB;

	public BlockForestDrum() {
		super(Material.wood);
		float f = 1F / 16F;
		setBlockBounds(f * 3, 0F, f * 3, 1F - f * 3, 1F - f * 2, 1F - f * 3);

		setHardness(2.0F);
		setStepSound(soundTypeWood);
		setBlockName(LibBlockNames.FOREST_DRUM);
	}

	@Override
	protected boolean shouldRegisterInNameSet() {
		return false;
	}

	@Override
	public Block setBlockName(String par1Str) {
		GameRegistry.registerBlock(this, ItemBlockWithMetadataAndName.class, par1Str);
		return super.setBlockName(par1Str);
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
	public int damageDropped(int meta) {
		return meta;
	}

	@Override
	public void registerBlockIcons(IIconRegister par1IconRegister) {
		iconBases = IconHelper.forBlock(par1IconRegister, this, 0);
		iconFaces = IconHelper.forBlock(par1IconRegister, this, 1);
		iconBasesA = IconHelper.forBlock(par1IconRegister, this, 2);
		iconFacesA = IconHelper.forBlock(par1IconRegister, this, 3);
		iconBasesB = IconHelper.forBlock(par1IconRegister, this, 4);
		iconFacesB = IconHelper.forBlock(par1IconRegister, this, 5);
	}

	@Override
	public void getSubBlocks(Item item, CreativeTabs tab, List list) {
		for(int i = 0; i < 3; i++)
			list.add(new ItemStack(item, 1, i));
	}

	@Override
	public IIcon getIcon(int side, int meta) {
		boolean animal = meta == 1;
		boolean tree = meta == 2;
		return side < 2 ? animal ? iconBasesA : tree ? iconBasesB : iconBases : animal ? iconFacesA : tree ? iconFacesB : iconFaces;
	}

	@Override
	public void onBurstCollision(IManaBurst burst, World world, int x, int y, int z) {
		if(burst.isFake())
			return;

		if(world.getBlockMetadata(x, y, z) == 0)
			ItemGrassHorn.breakGrass(world, null, 0, x, y, z);
		else if(world.getBlockMetadata(x, y, z) == 2)
			ItemGrassHorn.breakGrass(world, null, 1, x, y, z);
		else if(!world.isRemote) {
			int range = 10;
			List<EntityLiving> entities = world.getEntitiesWithinAABB(EntityLiving.class, AxisAlignedBB.getBoundingBox(x - range, y - range, z - range, x + range + 1, y + range + 1, z + range + 1));
			List<EntityLiving> shearables = new ArrayList();
			ItemStack stack = new ItemStack(this, 1, 1);

			for(EntityLiving entity : entities) {
				if(entity instanceof IShearable && ((IShearable) entity).isShearable(stack, world, (int) entity.posX, (int) entity.posY, (int) entity.posZ)) {
					shearables.add(entity);
				} else if(entity instanceof EntityCow) {
					List<EntityItem> items = world.getEntitiesWithinAABB(EntityItem.class, AxisAlignedBB.getBoundingBox(entity.posX, entity.posY, entity.posZ, entity.posX + entity.width, entity.posY + entity.height, entity.posZ + entity.width));
					for(EntityItem item : items) {
						ItemStack itemstack = item.getEntityItem();
						if(itemstack != null && itemstack.getItem() == Items.bucket && !world.isRemote) {
							while(itemstack.stackSize > 0) {
								EntityItem ent = entity.entityDropItem(new ItemStack(Items.milk_bucket), 1.0F);
								ent.motionY += world.rand.nextFloat() * 0.05F;
								ent.motionX += (world.rand.nextFloat() - world.rand.nextFloat()) * 0.1F;
								ent.motionZ += (world.rand.nextFloat() - world.rand.nextFloat()) * 0.1F;
								itemstack.stackSize--;
							}
							item.setDead();
						}
					}
				}
			}

			Collections.shuffle(shearables);
			int sheared = 0;

			for(EntityLiving entity : shearables) {
				if(sheared > 4)
					break;

				List<ItemStack> stacks = ((IShearable) entity).onSheared(stack, world, (int) entity.posX, (int) entity.posY, (int) entity.posZ, 0);
				if(stacks != null)
					for(ItemStack wool : stacks) {
						EntityItem ent = entity.entityDropItem(wool, 1.0F);
						ent.motionY += world.rand.nextFloat() * 0.05F;
						ent.motionX += (world.rand.nextFloat() - world.rand.nextFloat()) * 0.1F;
						ent.motionZ += (world.rand.nextFloat() - world.rand.nextFloat()) * 0.1F;
					}
				++sheared;
			}
		}

		if(!world.isRemote)
			for(int i = 0; i < 10; i++)
				world.playSoundEffect(x, y, z, "note.bd", 1F, 1F);
		else world.spawnParticle("note", x + 0.5, y + 1.2, z + 0.5D, 1.0 / 24.0, 0, 0);

	}

	@Override
	public LexiconEntry getEntry(World world, int x, int y, int z, EntityPlayer player, ItemStack lexicon) {
		int meta = world.getBlockMetadata(x, y, z);

		switch(meta) {
		case 1:
			return LexiconData.gatherDrum;
		case 2:
			return LexiconData.canopyDrum;
		default:
			return LexiconData.forestDrum;
		}
	}

}
