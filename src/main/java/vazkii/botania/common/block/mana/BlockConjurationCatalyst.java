/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * File Created @ [Jun 20, 2014, 1:27:14 AM (GMT)]
 */
package vazkii.botania.common.block.mana;

import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import vazkii.botania.api.lexicon.LexiconEntry;
import vazkii.botania.client.core.handler.MiscellaneousIcons;
import vazkii.botania.common.lexicon.LexiconData;
import vazkii.botania.common.lib.LibBlockNames;

public class BlockConjurationCatalyst extends BlockAlchemyCatalyst {

	public BlockConjurationCatalyst() {
		super(LibBlockNames.CONJURATION_CATALYST);
	}

	@Override
	public LexiconEntry getEntry(World world, BlockPos pos, EntityPlayer player, ItemStack lexicon) {
		return LexiconData.conjurationCatalyst;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public TextureAtlasSprite getIcon(World world, BlockPos pos) {
		return MiscellaneousIcons.INSTANCE.conjurationCatalystOverlay;
	}
}
