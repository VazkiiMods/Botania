/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.item.rod;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.potion.EffectInstance;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.vector.Vector3d;
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

import javax.annotation.Nonnull;

import java.util.List;

public class ItemTornadoRod extends Item implements IManaUsingItem, IAvatarWieldable {

	private static final ResourceLocation avatarOverlay = new ResourceLocation(LibResources.MODEL_AVATAR_TORNADO);

	private static final int FLY_TIME = 20;
	private static final int FALL_MULTIPLIER = 3;
	private static final int MAX_COUNTER = FLY_TIME * FALL_MULTIPLIER;
	private static final int COST = 350;

	private static final String TAG_FLYING = "flying";
	private static final String TAG_FLYCOUNTER = "flyCounter";
	private static final String TAG_COOLDOWNS = "boostCooldowns";

	public ItemTornadoRod(Properties props) {
		super(props);
	}

	@Override
	public void inventoryTick(ItemStack stack, World world, Entity ent, int slot, boolean active) {
		if (ent instanceof PlayerEntity) {
			PlayerEntity player = (PlayerEntity) ent;
			boolean damaged = getFlyCounter(stack) > 0;
			boolean held = player.getHeldItemMainhand() == stack || player.getHeldItemOffhand() == stack;

			if (damaged && !isFlying(stack)) {
				setFlyCounter(stack, getFlyCounter(stack) - 1);
			}

			if (getFlyCounter(stack) >= MAX_COUNTER) {
				setFlying(stack, false);
			} else if (isFlying(stack)) {
				if (held) {
					player.fallDistance = 0F;
					double my = IManaProficiencyArmor.hasProficiency(player, stack) ? 1.6 : 1.25;
					Vector3d oldMot = player.getMotion();
					if (player.isElytraFlying()) {
						Vector3d lookDir = player.getLookVec();
						player.setMotion(new Vector3d(lookDir.getX() * my, lookDir.getY() * my, lookDir.getZ() * my));
					} else {
						player.setMotion(new Vector3d(oldMot.getX(), my, oldMot.getZ()));
					}

					player.playSound(ModSounds.airRod, 0.1F, 0.25F);
					for (int i = 0; i < 5; i++) {
						WispParticleData data = WispParticleData.wisp(0.35F + (float) Math.random() * 0.1F, 0.25F, 0.25F, 0.25F);
						world.addParticle(data, player.getPosX(), player.getPosY(), player.getPosZ(),
								0.2F * (float) (Math.random() - 0.5),
								-0.01F * (float) Math.random(),
								0.2F * (float) (Math.random() - 0.5));
					}
				}

				setFlyCounter(stack, getFlyCounter(stack) + FALL_MULTIPLIER);
				if (getFlyCounter(stack) == MAX_COUNTER) {
					setFlying(stack, false);
				}
			}

			if (damaged) {
				player.fallDistance = 0;
			}
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
		if (fly == 0 && ManaItemHandler.instance().requestManaExactForTool(stack, player, COST, false)) {
			ManaItemHandler.instance().requestManaExactForTool(stack, player, COST, true);
			setFlying(stack, true);
			return ActionResult.resultSuccess(stack);
		}

		return ActionResult.resultPass(stack);
	}

	public static boolean isFlying(ItemStack stack) {
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
		TileEntity te = tile.tileEntity();
		World world = te.getWorld();
		if (tile.getCurrentMana() >= COST && tile.isEnabled()) {
			CompoundNBT cooldownTag = getAvatarCooldowns(te);
			if (!world.isRemote) {
				decAvatarCooldowns(cooldownTag);
			}

			int range = 5;
			int rangeY = 3;
			List<PlayerEntity> players = world.getEntitiesWithinAABB(PlayerEntity.class, new AxisAlignedBB(te.getPos().add(-0.5 - range, -0.5 - rangeY, -0.5 - range), te.getPos().add(0.5 + range, 0.5 + rangeY, 0.5 + range)));
			for (PlayerEntity p : players) {
				int cooldown = 0;
				if (cooldownTag.contains(p.getUniqueID().toString())) {
					cooldown = cooldownTag.getInt(p.getUniqueID().toString());
				}
				if (!p.isSneaking() && cooldown <= 0) {
					if (p.getMotion().length() > 0.2 && p.getMotion().length() < 5 && p.isElytraFlying()) {
						doAvatarElytraBoost(p, world);
						if (!world.isRemote) {
							doAvatarMiscEffects(p, tile);
							cooldownTag.putInt(p.getUniqueID().toString(), 20);
						}
					} else if (p.getMotion().getY() > 0.3 && p.getMotion().getY() < 2 && !p.isElytraFlying()) {
						doAvatarJump(p, world);
						if (!world.isRemote) {
							doAvatarMiscEffects(p, tile);
						}
					}
				}
			}
			if (!world.isRemote) {
				setAvatarCooldowns(te, cooldownTag);
			}
		}
	}

	private void doAvatarElytraBoost(PlayerEntity p, World world) {
		Vector3d lookDir = p.getLookVec();
		double mult = 1.25 * Math.pow(2.71828, -0.5 * p.getMotion().length());
		p.setMotion(p.getMotion().getX() + lookDir.getX() * mult,
				p.getMotion().getY() + lookDir.getY() * mult,
				p.getMotion().getZ() + lookDir.getZ() * mult);

		for (int i = 0; i < 20; i++) {
			for (int j = 0; j < 5; j++) {
				WispParticleData data = WispParticleData.wisp(0.35F + (float) Math.random() * 0.1F, 0.25F, 0.25F, 0.25F);
				world.addParticle(data, p.getPosX() + lookDir.getX() * i,
						p.getPosY() + lookDir.getY() * i,
						p.getPosZ() + lookDir.getZ() * i,
						0.2F * (float) (Math.random() - 0.5) * (Math.abs(lookDir.getY()) + Math.abs(lookDir.getZ())) + -0.01F * (float) Math.random() * lookDir.getX(),
						0.2F * (float) (Math.random() - 0.5) * (Math.abs(lookDir.getX()) + Math.abs(lookDir.getZ())) + -0.01F * (float) Math.random() * lookDir.getY(),
						0.2F * (float) (Math.random() - 0.5) * (Math.abs(lookDir.getY()) + Math.abs(lookDir.getX())) + -0.01F * (float) Math.random() * lookDir.getZ());
			}
		}
	}

	private void doAvatarJump(PlayerEntity p, World world) {
		p.setMotion(p.getMotion().getX(), 2.8, p.getMotion().getZ());

		for (int i = 0; i < 20; i++) {
			for (int j = 0; j < 5; j++) {
				WispParticleData data = WispParticleData.wisp(0.35F + (float) Math.random() * 0.1F, 0.25F, 0.25F, 0.25F);
				world.addParticle(data, p.getPosX(),
						p.getPosY() + i, p.getPosZ(),
						0.2F * (float) (Math.random() - 0.5),
						-0.01F * (float) Math.random(),
						0.2F * (float) (Math.random() - 0.5));
			}
		}
	}

	private void doAvatarMiscEffects(PlayerEntity p, IAvatarTile tile) {
		p.world.playSound(null, p.getPosX(), p.getPosY(), p.getPosZ(), ModSounds.dash, SoundCategory.PLAYERS, 1F, 1F);
		p.addPotionEffect(new EffectInstance(ModPotions.featherfeet, 100, 0));
		tile.receiveMana(-COST);
	}

	private CompoundNBT getAvatarCooldowns(TileEntity te) {
		if (te.getTileData().contains(TAG_COOLDOWNS)) {
			return ((CompoundNBT) te.getTileData().get(TAG_COOLDOWNS));
		} else {
			return new CompoundNBT();
		}
	}

	private void setAvatarCooldowns(TileEntity te, CompoundNBT cooldownTag) {
		te.getTileData().put(TAG_COOLDOWNS, cooldownTag);
	}

	private void decAvatarCooldowns(CompoundNBT cooldownTag) {
		for (String key : cooldownTag.keySet()) {
			int val = cooldownTag.getInt(key);
			if (val > 0) {
				cooldownTag.putInt(key, val - 1);
			} else {
				cooldownTag.remove(key);
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
