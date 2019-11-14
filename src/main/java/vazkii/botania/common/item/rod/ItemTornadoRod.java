/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * File Created @ [Jun 25, 2014, 4:36:13 PM (GMT)]
 */
package vazkii.botania.common.item.rod;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.UseAction;
import net.minecraft.potion.EffectInstance;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import vazkii.botania.api.item.IAvatarTile;
import vazkii.botania.api.item.IAvatarWieldable;
import vazkii.botania.api.item.IManaProficiencyArmor;
import vazkii.botania.api.mana.IManaUsingItem;
import vazkii.botania.api.mana.ManaItemHandler;
import vazkii.botania.client.fx.WispParticleData;
import vazkii.botania.client.lib.LibResources;
import vazkii.botania.common.brew.ModPotions;
import vazkii.botania.common.core.handler.ModSounds;
import vazkii.botania.common.core.helper.ItemNBTHelper;
import vazkii.botania.common.item.ItemMod;

import javax.annotation.Nonnull;
import java.util.List;

public class ItemTornadoRod extends ItemMod implements IManaUsingItem, IAvatarWieldable {

	private static final ResourceLocation avatarOverlay = new ResourceLocation(LibResources.MODEL_AVATAR_TORNADO);

	private static final int FLY_TIME = 20;
	private static final int FALL_MULTIPLIER = 3;
	private static final int MAX_COUNTER = FLY_TIME * FALL_MULTIPLIER;
	private static final int COST = 350;

	private static final String TAG_FLYING = "flying";
	private static final String TAG_FLYCOUNTER = "flyCounter";

	public ItemTornadoRod(Properties props) {
		super(props);
		addPropertyOverride(new ResourceLocation("botania", "flying"), (stack, world, living) -> isFlying(stack) ? 1 : 0);
	}

	@Override
	public void inventoryTick(ItemStack stack, World world, Entity ent, int slot, boolean active) {
		if(ent instanceof PlayerEntity) {
			PlayerEntity player = (PlayerEntity) ent;
			boolean damaged = getFlyCounter(stack) > 0;
			boolean held = player.getHeldItemMainhand() == stack || player.getHeldItemOffhand() == stack;

			if(damaged && !isFlying(stack))
				setFlyCounter(stack, getFlyCounter(stack) - 1);

			if(getFlyCounter(stack) >= MAX_COUNTER) {
				setFlying(stack, false);
			} else if(isFlying(stack)) {
				if(held) {
					player.fallDistance = 0F;
					double my =  IManaProficiencyArmor.Helper.hasProficiency(player, stack) ? 1.6 : 1.25;
					Vec3d oldMot = player.getMotion();
					player.setMotion(new Vec3d(oldMot.getX(), my, oldMot.getZ()));

					player.playSound(ModSounds.airRod, 0.1F, 0.25F);
					for(int i = 0; i < 5; i++) {
						WispParticleData data = WispParticleData.wisp(0.35F + (float) Math.random() * 0.1F, 0.25F, 0.25F, 0.25F);
						world.addParticle(data, player.posX, player.posY, player.posZ, 0.2F * (float) (Math.random() - 0.5), -0.01F * (float) Math.random(), 0.2F * (float) (Math.random() - 0.5));
					}
				}

				setFlyCounter(stack, getFlyCounter(stack) + FALL_MULTIPLIER);
				if(getFlyCounter(stack) == MAX_COUNTER)
					setFlying(stack, false);
			}

			if(damaged)
				player.fallDistance = 0;
		}
	}

	@Override
	public boolean showDurabilityBar(ItemStack stack) {
		return getFlyCounter(stack) > 0;
	}

	@Override
	public double getDurabilityForDisplay(ItemStack stack) {
		return getFlyCounter(stack) / (double) MAX_COUNTER;
	}

	@Nonnull
	@Override
	public ActionResult<ItemStack> onItemRightClick(World world, PlayerEntity player, @Nonnull Hand hand) {
		ItemStack stack = player.getHeldItem(hand);
		int fly = getFlyCounter(stack);
		if(fly == 0 && ManaItemHandler.requestManaExactForTool(stack, player, COST, false)) {
			ManaItemHandler.requestManaExactForTool(stack, player, COST, true);
			setFlying(stack, true);
			return ActionResult.newResult(ActionResultType.SUCCESS, stack);
		}

		return ActionResult.newResult(ActionResultType.PASS, stack);
	}

	private boolean isFlying(ItemStack stack) {
		return ItemNBTHelper.getBoolean(stack, TAG_FLYING, false);
	}

	private void setFlying(ItemStack stack, boolean flying) {
		ItemNBTHelper.setBoolean(stack, TAG_FLYING, flying);
	}

	private int getFlyCounter(ItemStack stack) {
		return stack.getOrCreateTag().getInt(TAG_FLYCOUNTER);
	}

	private void setFlyCounter(ItemStack stack, int counter) {
		stack.getOrCreateTag().putInt(TAG_FLYCOUNTER, counter);
	}

	@Override
	public boolean usesMana(ItemStack stack) {
		return true;
	}

	@Override
	public void onAvatarUpdate(IAvatarTile tile, ItemStack stack) {
		TileEntity te = (TileEntity) tile;
		World world = te.getWorld();
		if(tile.getCurrentMana() >= COST && tile.isEnabled()) {
			int range = 5;
			int rangeY = 3;
			List<PlayerEntity> players = world.getEntitiesWithinAABB(PlayerEntity.class, new AxisAlignedBB(te.getPos().add(-0.5 - range, -0.5 - rangeY, -0.5 - range), te.getPos().add(0.5 + range, 0.5 + rangeY, 0.5 + range)));
			for(PlayerEntity p : players) {
				if(p.getMotion().getY() > 0.3 && p.getMotion().getY() < 2 && !p.isSneaking()) {
					p.setMotion(p.getMotion().getX(), 2.8, p.getMotion().getZ());

					for(int i = 0; i < 20; i++)
						for(int j = 0; j < 5; j++) {
                            WispParticleData data = WispParticleData.wisp(0.35F + (float) Math.random() * 0.1F, 0.25F, 0.25F, 0.25F);
                            world.addParticle(data, p.posX, p.posY + i, p.posZ, 0.2F * (float) (Math.random() - 0.5), -0.01F * (float) Math.random(), 0.2F * (float) (Math.random() - 0.5));
                        }

					if(!world.isRemote) {
						p.world.playSound(null, p.posX, p.posY, p.posZ, ModSounds.dash, SoundCategory.PLAYERS, 1F, 1F);
						p.addPotionEffect(new EffectInstance(ModPotions.featherfeet, 100, 0));
						tile.recieveMana(-COST);
					}
				}
			}
		}
	}

	@Override
	public boolean shouldCauseReequipAnimation(ItemStack oldStack, @Nonnull ItemStack newStack, boolean slotChanged) {
		return newStack.getItem() != this || isFlying(oldStack) != isFlying(newStack);
	}

	@Override
	public ResourceLocation getOverlayResource(IAvatarTile tile, ItemStack stack) {
		return avatarOverlay;
	}

}
