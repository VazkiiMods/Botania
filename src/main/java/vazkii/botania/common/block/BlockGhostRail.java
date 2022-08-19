/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 * 
 * File Created @ [Jun 9, 2015, 12:48:18 AM (GMT)]
 */
package vazkii.botania.common.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockRailBase;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.minecart.MinecartUpdateEvent;
import vazkii.botania.api.lexicon.ILexiconable;
import vazkii.botania.api.lexicon.LexiconEntry;
import vazkii.botania.client.core.helper.IconHelper;
import vazkii.botania.common.core.BotaniaCreativeTab;
import vazkii.botania.common.item.block.ItemBlockMod;
import vazkii.botania.common.lexicon.LexiconData;
import vazkii.botania.common.lib.LibBlockNames;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockGhostRail extends BlockRailBase implements ILexiconable {

	private static final String TAG_FLOAT_TICKS = "Botania_FloatTicks";

	public BlockGhostRail() {
		super(true);
		setCreativeTab(BotaniaCreativeTab.INSTANCE);
		MinecraftForge.EVENT_BUS.register(this);
		setBlockName(LibBlockNames.GHOST_RAIL);
	}

	@Override
	public Block setBlockName(String par1Str) {
		GameRegistry.registerBlock(this, ItemBlockMod.class, par1Str);
		return super.setBlockName(par1Str);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerBlockIcons(IIconRegister par1IconRegister) {
		blockIcon = IconHelper.forBlock(par1IconRegister, this);
	}

	@SubscribeEvent
	public void onMinecartUpdate(MinecartUpdateEvent event) {
		int x = MathHelper.floor_double(event.entity.posX);
		int y = MathHelper.floor_double(event.entity.posY);
		int z = MathHelper.floor_double(event.entity.posZ);
		Block block = event.entity.worldObj.getBlock(x, y, z);
		boolean air = block.isAir(event.entity.worldObj, x, y, z);
		int floatTicks = event.entity.getEntityData().getInteger(TAG_FLOAT_TICKS);

		if(block == this)
			event.entity.getEntityData().setInteger(TAG_FLOAT_TICKS, 20);
		else if(block instanceof BlockRailBase || block == ModBlocks.dreamwood) {
			event.entity.getEntityData().setInteger(TAG_FLOAT_TICKS, 0);
			if(floatTicks > 0)
				event.entity.worldObj.playAuxSFX(2003, x, y, z, 0);
		}
		floatTicks = event.entity.getEntityData().getInteger(TAG_FLOAT_TICKS);

		if(floatTicks > 0) {
			Block blockBelow = event.entity.worldObj.getBlock(x, y - 1, z);
			boolean airBelow = blockBelow.isAir(event.entity.worldObj, x, y - 1, z);
			if(air && airBelow || !air && !airBelow)
				event.entity.noClip = true;
			event.entity.motionY = 0.2;
			event.entity.motionX *= 1.4;
			event.entity.motionZ *= 1.4;
			event.entity.getEntityData().setInteger(TAG_FLOAT_TICKS, floatTicks - 1);
			event.entity.worldObj.playAuxSFX(2000, x, y, z, 0);
		} else event.entity.noClip = false;
	}

	@Override
	public LexiconEntry getEntry(World world, int x, int y, int z, EntityPlayer player, ItemStack lexicon) {
		return LexiconData.ghostRail;
	}

}
