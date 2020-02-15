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

import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.Tags;
import vazkii.botania.api.item.IAvatarTile;
import vazkii.botania.api.item.IAvatarWieldable;
import vazkii.botania.api.item.IManaProficiencyArmor;
import vazkii.botania.api.mana.IManaUsingItem;
import vazkii.botania.api.mana.ManaItemHandler;
import vazkii.botania.client.fx.WispParticleData;
import vazkii.botania.client.lib.LibResources;
import vazkii.botania.common.core.handler.ModSounds;
import net.minecraft.item.Item;

import javax.annotation.Nonnull;
import java.util.Random;

public class ItemDiviningRod extends Item implements IManaUsingItem, IAvatarWieldable {

	private static final ResourceLocation avatarOverlay = new ResourceLocation(LibResources.MODEL_AVATAR_DIVINING);

	static final int COST = 3000;

	public ItemDiviningRod(Properties props) {
		super(props);
	}

	@Nonnull
	@Override
	public ActionResult<ItemStack> onItemRightClick(World world, PlayerEntity p, @Nonnull Hand hand) {
		ItemStack stack = p.getHeldItem(hand);
		if(ManaItemHandler.requestManaExactForTool(stack, p, COST, true)) {
			if(world.isRemote) {
				int range = IManaProficiencyArmor.hasProficiency(p, stack) ? 20 : 15;
				long seedxor = world.rand.nextLong();
				doHighlight(world, new BlockPos(p), range, seedxor);
				p.swingArm(hand);
			} else world.playSound(null, p.getX(), p.getY(), p.getZ(), ModSounds.divinationRod, SoundCategory.PLAYERS, 1F, 1F);
			return ActionResult.success(stack);
		}

		return ActionResult.pass(stack);
	}

	private void doHighlight(World world, BlockPos pos, int range, long seedxor) {
		for(BlockPos pos_ : BlockPos.getAllInBoxMutable(pos.add(-range, -range, -range),
				pos.add(range, range, range))) {
			BlockState state = world.getBlockState(pos_);

			if(Tags.Blocks.ORES.contains(state.getBlock())) {
				Random rand = new Random(state.hashCode() ^ seedxor);
				WispParticleData data = WispParticleData.wisp(0.25F, rand.nextFloat(), rand.nextFloat(), rand.nextFloat(), 8, false);
				world.addParticle(data, pos_.getX() + world.rand.nextFloat(),
						pos_.getY() + world.rand.nextFloat(),
						pos_.getZ() + world.rand.nextFloat(),
						0, 0, 0);
			}
		}
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