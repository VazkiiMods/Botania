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
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.world.World;
import vazkii.botania.api.item.IAvatarTile;
import vazkii.botania.api.item.IAvatarWieldable;
import vazkii.botania.api.item.IManaProficiencyArmor;
import vazkii.botania.api.mana.IManaUsingItem;
import vazkii.botania.api.mana.ManaItemHandler;
import vazkii.botania.client.lib.LibResources;
import vazkii.botania.common.Botania;
import vazkii.botania.common.brew.ModPotions;
import vazkii.botania.common.core.handler.ModSounds;
import vazkii.botania.common.core.helper.ItemNBTHelper;
import vazkii.botania.common.item.ItemMod;
import vazkii.botania.common.lib.LibItemNames;

import javax.annotation.Nonnull;
import java.util.List;

public class ItemTornadoRod extends ItemMod implements IManaUsingItem, IAvatarWieldable {

	private static final ResourceLocation avatarOverlay = new ResourceLocation(LibResources.MODEL_AVATAR_TORNADO);

	private static final int FLY_TIME = 20;
	private static final int FALL_MULTIPLIER = 3;
	private static final int MAX_DAMAGE = FLY_TIME * FALL_MULTIPLIER;
	private static final int COST = 350;

	private static final String TAG_FLYING = "flying";

	public ItemTornadoRod() {
		super(LibItemNames.TORNADO_ROD);
		setMaxDamage(MAX_DAMAGE);
		setMaxStackSize(1);
		addPropertyOverride(new ResourceLocation("botania", "flying"), (stack, world, living) -> isFlying(stack) ? 1 : 0);
	}

	@Override
	public void onUpdate(ItemStack par1ItemStack, World world, Entity par3Entity, int par4, boolean holding) {
		if(par3Entity instanceof EntityPlayer) {
			EntityPlayer player = (EntityPlayer) par3Entity;
			boolean damaged = par1ItemStack.getItemDamage() > 0;

			if(damaged && !isFlying(par1ItemStack))
				par1ItemStack.setItemDamage(par1ItemStack.getItemDamage() - 1);

			int max = FALL_MULTIPLIER * FLY_TIME;
			if(par1ItemStack.getItemDamage() >= max) {
				setFlying(par1ItemStack, false);
				player.resetActiveHand();
			} else if(isFlying(par1ItemStack)) {
				if(holding) {
					player.fallDistance = 0F;
					player.motionY = IManaProficiencyArmor.Helper.hasProficiency(player, par1ItemStack) ? 1.6 : 1.25;

					player.world.playSound(null, player.posX, player.posY, player.posZ, ModSounds.airRod, SoundCategory.PLAYERS, 0.1F, 0.25F);
					for(int i = 0; i < 5; i++)
						Botania.proxy.wispFX(player.posX, player.posY, player.posZ, 0.25F, 0.25F, 0.25F, 0.35F + (float) Math.random() * 0.1F, 0.2F * (float) (Math.random() - 0.5), -0.01F * (float) Math.random(), 0.2F * (float) (Math.random() - 0.5));
				}

				par1ItemStack.setItemDamage(Math.min(max, par1ItemStack.getItemDamage() + FALL_MULTIPLIER));
				if(par1ItemStack.getItemDamage() == MAX_DAMAGE)
					setFlying(par1ItemStack, false);
			}

			if(damaged)
				player.fallDistance = 0;
		}
	}

	@Nonnull
	@Override
	public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, @Nonnull EnumHand hand) {
		ItemStack stack = player.getHeldItem(hand);
		int meta = stack.getItemDamage();
		if(meta != 0 || ManaItemHandler.requestManaExactForTool(stack, player, COST, false)) {
			player.setActiveHand(hand);
			if(meta == 0) {
				ManaItemHandler.requestManaExactForTool(stack, player, COST, true);
				setFlying(stack, true);
			}
			return ActionResult.newResult(EnumActionResult.SUCCESS, stack);
		}

		return ActionResult.newResult(EnumActionResult.PASS, stack);
	}

	@Nonnull
	@Override
	public EnumAction getItemUseAction(ItemStack par1ItemStack) {
		return EnumAction.BOW;
	}

	@Override
	public int getMaxItemUseDuration(ItemStack par1ItemStack) {
		return 720000;
	}

	public boolean isFlying(ItemStack stack) {
		return ItemNBTHelper.getBoolean(stack, TAG_FLYING, false);
	}

	public void setFlying(ItemStack stack, boolean flying) {
		ItemNBTHelper.setBoolean(stack, TAG_FLYING, flying);
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
			List<EntityPlayer> players = world.getEntitiesWithinAABB(EntityPlayer.class, new AxisAlignedBB(te.getPos().add(-0.5 - range, -0.5 - rangeY, -0.5 - range), te.getPos().add(0.5 + range, 0.5 + rangeY, 0.5 + range)));
			for(EntityPlayer p : players) {
				if(p.motionY > 0.3 && p.motionY < 2 && !p.isSneaking()) {
					p.motionY = 2.8;

					for(int i = 0; i < 20; i++)
						for(int j = 0; j < 5; j++)
							Botania.proxy.wispFX(p.posX, p.posY + i, p.posZ, 0.25F, 0.25F, 0.25F, 0.35F + (float) Math.random() * 0.1F, 0.2F * (float) (Math.random() - 0.5), -0.01F * (float) Math.random(), 0.2F * (float) (Math.random() - 0.5));

					if(!world.isRemote) {
						p.world.playSound(null, p.posX, p.posY, p.posZ, ModSounds.dash, SoundCategory.PLAYERS, 1F, 1F);
						p.addPotionEffect(new PotionEffect(ModPotions.featherfeet, 100, 0));
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
