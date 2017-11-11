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

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.oredict.OreDictionary;
import vazkii.botania.api.item.IAvatarTile;
import vazkii.botania.api.item.IAvatarWieldable;
import vazkii.botania.api.item.IManaProficiencyArmor;
import vazkii.botania.api.mana.IManaUsingItem;
import vazkii.botania.api.mana.ManaItemHandler;
import vazkii.botania.client.lib.LibResources;
import vazkii.botania.common.Botania;
import vazkii.botania.common.core.handler.ModSounds;
import vazkii.botania.common.item.ItemMod;
import vazkii.botania.common.lib.LibItemNames;

import javax.annotation.Nonnull;
import java.util.Random;

public class ItemDiviningRod extends ItemMod implements IManaUsingItem, IAvatarWieldable {

	private static final ResourceLocation avatarOverlay = new ResourceLocation(LibResources.MODEL_AVATAR_DIVINING);

	static final int COST = 3000;

	public ItemDiviningRod() {
		super(LibItemNames.DIVINING_ROD);
		setMaxStackSize(1);
	}

	@Nonnull
	@Override
	public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer p, @Nonnull EnumHand hand) {
		ItemStack stack = p.getHeldItem(hand);
		if(ManaItemHandler.requestManaExactForTool(stack, p, COST, true)) {
			if(world.isRemote) {
				int range = IManaProficiencyArmor.Helper.hasProficiency(p, stack) ? 20 : 15;
				long seedxor = world.rand.nextLong();
				doHighlight(world, new BlockPos(p), range, seedxor);
				p.swingArm(hand);
			} else world.playSound(null, p.posX, p.posY, p.posZ, ModSounds.divinationRod, SoundCategory.PLAYERS, 1F, 1F);
			return ActionResult.newResult(EnumActionResult.SUCCESS, stack);
		}

		return ActionResult.newResult(EnumActionResult.PASS, stack);
	}

	public void doHighlight(World world, BlockPos pos, int range, long seedxor) {
		Botania.proxy.setWispFXDepthTest(false);
		for(BlockPos pos_ : BlockPos.getAllInBox(pos.add(-range, -range, -range), pos.add(range, range, range))) {
			IBlockState state = world.getBlockState(pos_);
			if(Item.getItemFromBlock(state.getBlock()) == Items.AIR)
				continue;
			ItemStack orestack = new ItemStack(state.getBlock(), 1, state.getBlock().getMetaFromState(state));
			for(int id : OreDictionary.getOreIDs(orestack)) {
				String s = OreDictionary.getOreName(id);
				if(s.matches("^ore[A-Z].+")) {
					Random rand = new Random(s.hashCode() ^ seedxor);
					Botania.proxy.wispFX(pos_.getX() + world.rand.nextFloat(), pos_.getY() + world.rand.nextFloat(), pos_.getZ() + world.rand.nextFloat(), rand.nextFloat(), rand.nextFloat(), rand.nextFloat(), 0.25F, 0F, 8);
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