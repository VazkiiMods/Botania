/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 * 
 * File Created @ [Aug 25, 2014, 2:57:16 PM (GMT)]
 */
package vazkii.botania.common.item.rod;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.oredict.OreDictionary;
import vazkii.botania.api.item.IAvatarTile;
import vazkii.botania.api.item.IAvatarWieldable;
import vazkii.botania.api.item.IManaProficiencyArmor;
import vazkii.botania.api.mana.IManaUsingItem;
import vazkii.botania.api.mana.ManaItemHandler;
import vazkii.botania.client.lib.LibResources;
import vazkii.botania.common.Botania;
import vazkii.botania.common.item.ItemMod;
import vazkii.botania.common.lib.LibItemNames;

public class ItemDiviningRod extends ItemMod implements IManaUsingItem, IAvatarWieldable {

	private static final ResourceLocation avatarOverlay = new ResourceLocation(LibResources.MODEL_AVATAR_DIVINING);

	static final int COST = 3000;

	public ItemDiviningRod() {
		setMaxStackSize(1);
		setUnlocalizedName(LibItemNames.DIVINING_ROD);
	}

	@Override
	public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer p) {
		if(ManaItemHandler.requestManaExactForTool(stack, p, COST, true)) {
			if(world.isRemote) {
				int range = IManaProficiencyArmor.Helper.hasProficiency(p) ? 20 : 15;
				long seedxor = world.rand.nextLong();
				doHighlight(world, new BlockPos(p), range, seedxor);
				p.swingItem();
			} else world.playSoundAtEntity(p, "botania:divinationRod", 1F, 1F);
		}

		return stack;
	}

	public void doHighlight(World world, BlockPos pos, int range, long seedxor) {
		Botania.proxy.setWispFXDepthTest(false);
		for(int i = -range; i < range + 1; i++)
			for(int j = -range; j < range + 1; j++)
				for(int k = -range; k < range + 1; k++) {
					BlockPos pos_ = pos.add(i, j, k);
					IBlockState state = world.getBlockState(pos_);
					ItemStack orestack = new ItemStack(state.getBlock(), 1, state.getBlock().getMetaFromState(state));
					for(int id : OreDictionary.getOreIDs(orestack)) {
						String s = OreDictionary.getOreName(id);
						if(s.matches("^ore[A-Z].+")) {
							Random rand = new Random(s.hashCode() ^ seedxor);
							Botania.proxy.wispFX(world, pos_.getX() + world.rand.nextFloat(), pos_.getY() + world.rand.nextFloat(), pos_.getZ() + world.rand.nextFloat(), rand.nextFloat(), rand.nextFloat(), rand.nextFloat(), 0.25F, 0F, 8);
							break;
						}
					}
				}
		Botania.proxy.setWispFXDepthTest(true);
	}

	@Override
	public boolean usesMana(ItemStack stack) {
		return true;
	}

	@Override
	public void onAvatarUpdate(IAvatarTile tile, ItemStack stack) {
		TileEntity te = (TileEntity) tile;
		World world = te.getWorld();
		if(tile.getCurrentMana() >= COST && tile.getElapsedFunctionalTicks() % 200 == 0 && tile.isEnabled()) {
			doHighlight(world, te.getPos(), 18, te.getPos().hashCode());
			tile.recieveMana(-COST);
		}
	}

	@Override
	public ResourceLocation getOverlayResource(IAvatarTile tile, ItemStack stack) {
		return avatarOverlay;
	}
}