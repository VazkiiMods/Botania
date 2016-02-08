/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 * 
 * File Created @ [Sep 2, 2014, 5:58:39 PM (GMT)]
 */
package vazkii.botania.api.wiki;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;

import org.apache.commons.lang3.text.WordUtils;

public class SimpleWikiProvider implements IWikiProvider {

	final String name, urlBase, replacement;
	final boolean lowercase;

	public SimpleWikiProvider(String name, String urlBase) {
		this(name, urlBase, "%20");
	}

	public SimpleWikiProvider(String name, String urlBase, boolean lowercase) {
		this(name, urlBase, "%20", lowercase);
	}

	public SimpleWikiProvider(String name, String urlBase, String replacement) {
		this.name = name;
		this.urlBase = urlBase;
		this.replacement = replacement;
		lowercase = false;
	}

	public SimpleWikiProvider(String name, String urlBase, String replacement, boolean lowercase) {
		this.name = name;
		this.urlBase = urlBase;
		this.replacement = replacement;
		this.lowercase = lowercase;
	}

	@Override
	public String getBlockName(World world, MovingObjectPosition pos, EntityPlayer player) {
		BlockPos bPos = pos.getBlockPos();

		Block block = world.getBlockState(bPos).getBlock();
		if(block == null)
			return null;

		ItemStack stack = block.getPickBlock(pos, world, bPos, player);

		if(stack == null || stack.getItem() == null)
			stack = new ItemStack(block, 1, block.getDamageValue(world, bPos));

		if(stack.getItem() == null)
			return null;

		String name = stack.getDisplayName();
		if(name == null || name.isEmpty())
			return null;

		return name;
	}

	@Override
	public String getWikiURL(World world, MovingObjectPosition pos, EntityPlayer player) {
		String name = getBlockName(world, pos, player);
		if(name == null)
			return null;

		if(lowercase) {
			return String.format(urlBase, name.toLowerCase().replaceAll(" ", replacement));
		} else {
			return String.format(urlBase, WordUtils.capitalizeFully(name).replaceAll(" ", replacement));
		}
	}

	@Override
	public String getWikiName(World world, MovingObjectPosition pos, EntityPlayer player) {
		return name;
	}

}
