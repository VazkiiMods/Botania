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

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import org.apache.commons.lang3.text.WordUtils;

public class SimpleWikiProvider implements IWikiProvider {

	private final String name, urlBase, replacement;
	private final boolean lowercase;

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
	public String getBlockName(World world, RayTraceResult pos, EntityPlayer player) {
		BlockPos bPos = pos.getBlockPos();
		IBlockState state = world.getBlockState(bPos);

		ItemStack stack = state.getBlock().getPickBlock(state, pos, world, bPos, player);

		if(stack.isEmpty())
			stack = new ItemStack(state.getBlock(), 1, state.getBlock().damageDropped(state));

		if(stack.isEmpty())
			return null;

		String name = stack.getDisplayName();
		if(name == null || name.isEmpty())
			return null;

		return name;
	}

	@Override
	public String getWikiURL(World world, RayTraceResult pos, EntityPlayer player) {
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
	public String getWikiName(World world, RayTraceResult pos, EntityPlayer player) {
		return name;
	}

}
