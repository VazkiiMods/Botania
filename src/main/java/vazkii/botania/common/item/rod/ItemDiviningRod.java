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
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
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
				int x = MathHelper.floor_double(p.posX);
				int y = MathHelper.floor_double(p.posY);
				int z = MathHelper.floor_double(p.posZ);
				int range = IManaProficiencyArmor.Helper.hasProficiency(p) ? 20 : 15;
				long seedxor = world.rand.nextLong();
				doHighlight(world, x, y, z, range, seedxor);
				p.swingItem();
			} else world.playSoundAtEntity(p, "botania:divinationRod", 1F, 1F);
		}

		return stack;
	}

	public void doHighlight(World world, int x, int y, int z, int range, long seedxor) {
		Botania.proxy.setWispFXDepthTest(false);
		for(int i = -range; i < range + 1; i++)
			for(int j = -range; j < range + 1; j++)
				for(int k = -range; k < range + 1; k++) {
					int xp = x + i;
					int yp = y + j;
					int zp = z + k;
					Block block = world.getBlock(xp, yp, zp);
					int meta = world.getBlockMetadata(xp, yp, zp);
					ItemStack orestack = new ItemStack(block, 1, meta);
					for(int id : OreDictionary.getOreIDs(orestack)) {
						String s = OreDictionary.getOreName(id);
						if(s.matches("^ore[A-Z].+")) {
							Random rand = new Random(s.hashCode() ^ seedxor);
							Botania.proxy.wispFX(world, xp + world.rand.nextFloat(), yp + world.rand.nextFloat(), zp + world.rand.nextFloat(), rand.nextFloat(), rand.nextFloat(), rand.nextFloat(), 0.25F, 0F, 8);
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
		World world = te.getWorldObj();
		if(tile.getCurrentMana() >= COST && tile.getElapsedFunctionalTicks() % 200 == 0 && tile.isEnabled()) {
			doHighlight(world, te.xCoord, te.yCoord, te.zCoord, 18, te.xCoord ^ te.yCoord ^ te.zCoord);
			tile.recieveMana(-COST);
		}
	}

	@Override
	public ResourceLocation getOverlayResource(IAvatarTile tile, ItemStack stack) {
		return avatarOverlay;
	}
}