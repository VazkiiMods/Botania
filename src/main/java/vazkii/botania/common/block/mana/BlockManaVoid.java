/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 * 
 * File Created @ [Mar 5, 2014, 12:55:47 AM (GMT)]
 */
package vazkii.botania.common.block.mana;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import vazkii.botania.api.lexicon.ILexiconable;
import vazkii.botania.api.lexicon.LexiconEntry;
import vazkii.botania.api.mana.IPoolOverlayProvider;
import vazkii.botania.client.core.handler.MiscellaneousIcons;
import vazkii.botania.common.block.BlockModContainer;
import vazkii.botania.common.block.tile.mana.TileManaVoid;
import vazkii.botania.common.lexicon.LexiconData;
import vazkii.botania.common.lib.LibBlockNames;

public class BlockManaVoid extends BlockModContainer implements ILexiconable, IPoolOverlayProvider {

	public BlockManaVoid() {
		super(Material.rock);
		setHardness(2.0F);
		setResistance(2000F);
		setStepSound(Block.soundTypeStone);
		setUnlocalizedName(LibBlockNames.MANA_VOID);
	}

	@Override
	public TileEntity createNewTileEntity(World world, int id) {
		return new TileManaVoid();
	}

	@Override
	public LexiconEntry getEntry(World world, BlockPos pos, EntityPlayer player, ItemStack lexicon) {
		return LexiconData.manaVoid;
	}

	@Override
	public TextureAtlasSprite getIcon(World world, BlockPos pos) {
		return MiscellaneousIcons.INSTANCE.manaVoidOverlay;
	}

}
