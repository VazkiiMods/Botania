/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 * 
 * File Created @ [Feb 21, 2015, 4:58:45 PM (GMT)]
 */
package vazkii.botania.common.item.equipment.tool.bow;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.init.Enchantments;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArrow;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.event.entity.player.ArrowLooseEvent;
import vazkii.botania.api.mana.IManaUsingItem;
import vazkii.botania.api.mana.ManaItemHandler;
import vazkii.botania.client.lib.LibResources;
import vazkii.botania.common.core.BotaniaCreativeTab;
import vazkii.botania.common.core.helper.PlayerHelper;
import vazkii.botania.common.item.ModItems;
import vazkii.botania.common.item.equipment.tool.ToolCommons;
import vazkii.botania.common.lib.LibItemNames;
import net.minecraftforge.fml.common.registry.GameRegistry;

import java.util.function.Predicate;

public class ItemLivingwoodBow extends ItemBow implements IManaUsingItem {

	private static final Predicate<ItemStack> AMMO_FUNC = s -> s != null && s.getItem() instanceof ItemArrow;
	public static final int MANA_PER_DAMAGE = 40;

	public ItemLivingwoodBow() {
		this(LibItemNames.LIVINGWOOD_BOW);
	}

	public ItemLivingwoodBow(String name) {
		setCreativeTab(BotaniaCreativeTab.INSTANCE);
		setUnlocalizedName(name);
		setMaxDamage(500);
		setFull3D();
		addPropertyOverride(new ResourceLocation("minecraft:pull"), (stack, worldIn, entityIn) -> (stack.getMaxItemUseDuration() - entityIn.getItemInUseCount()) * chargeVelocityMultiplier() / 20.0F);
	}

	@Override
	public Item setUnlocalizedName(String par1Str) {
		GameRegistry.registerItem(this, par1Str);
		return super.setUnlocalizedName(par1Str);
	}

	@Override
	public String getUnlocalizedNameInefficiently(ItemStack par1ItemStack) {
		return super.getUnlocalizedNameInefficiently(par1ItemStack).replaceAll("item.", "item." + LibResources.PREFIX_MOD);
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(ItemStack stack, World world, EntityPlayer player, EnumHand hand) {
		// Copy from superclass with our own check
		boolean flag = canFire(stack, player);
		ActionResult<ItemStack> ret = ForgeEventFactory.onArrowNock(stack, world, player, hand, flag);
		if (ret != null) return ret;

		if (!player.capabilities.isCreativeMode)
		{
			return !flag ? new ActionResult<>(EnumActionResult.FAIL, stack) : new ActionResult<>(EnumActionResult.PASS, stack);
		}
		else
		{
			player.setActiveHand(hand);
			return new ActionResult<>(EnumActionResult.SUCCESS, stack);
		}
	}

	@Override
	public void onPlayerStoppedUsing(ItemStack stack, World world, EntityLivingBase shooter, int useTicks) {
		int j = (int) ((getMaxItemUseDuration(stack) - useTicks) * chargeVelocityMultiplier());

		if(shooter instanceof EntityPlayer) {
			ArrowLooseEvent event = new ArrowLooseEvent(((EntityPlayer) shooter), stack, world, j, true);
			if(MinecraftForge.EVENT_BUS.post(event))
				return;
			j = event.getCharge();
		}

		boolean flag = !(shooter instanceof EntityPlayer) || canFire(stack, ((EntityPlayer) shooter));
		boolean infinity = EnchantmentHelper.getEnchantmentLevel(Enchantments.infinity, stack) > 0;

		if(flag) {
			float f = j / 20.0F;
			f = (f * f + f * 2.0F) / 3.0F;

			if(f < 0.1D)
				return;

			if(f > 1.0F)
				f = 1.0F;

			ItemStack ammo = shooter instanceof EntityPlayer ? PlayerHelper.getAmmo(((EntityPlayer) shooter), AMMO_FUNC) : new ItemStack(Items.arrow);
			EntityArrow entityarrow = ((ItemArrow) (ammo == null ? Items.arrow : ammo.getItem())).createArrow(world, ammo, shooter);

			if(f == 1.0F)
				entityarrow.setIsCritical(true);

			int k = EnchantmentHelper.getEnchantmentLevel(Enchantments.power, stack);

			if(k > 0)
				entityarrow.setDamage(entityarrow.getDamage() + k * 0.5D + 0.5D);

			int l = EnchantmentHelper.getEnchantmentLevel(Enchantments.punch, stack);

			if(l > 0)
				entityarrow.setKnockbackStrength(l);

			if(EnchantmentHelper.getEnchantmentLevel(Enchantments.flame, stack) > 0)
				entityarrow.setFire(100);

			ToolCommons.damageItem(stack, 1, shooter, MANA_PER_DAMAGE);
			world.playSound(null, shooter.posX, shooter.posY, shooter.posZ, SoundEvents.entity_arrow_shoot, SoundCategory.PLAYERS, 1.0F, 1.0F / (itemRand.nextFloat() * 0.4F + 1.2F) + f * 0.5F);

			onFire(stack, shooter, infinity, entityarrow);

			if(!world.isRemote)
				world.spawnEntityInWorld(entityarrow);
		}
	}

	float chargeVelocityMultiplier() {
		return 1F;
	}

	boolean postsEvent() {
		return true;
	}

	boolean canFire(ItemStack stack, EntityPlayer player) {
		return player.capabilities.isCreativeMode || EnchantmentHelper.getEnchantmentLevel(Enchantments.infinity, stack) > 0 || PlayerHelper.hasAmmo(player, AMMO_FUNC);
	}

	void onFire(ItemStack bow, EntityLivingBase living, boolean infinity, EntityArrow arrow) {
		if(infinity)
			arrow.canBePickedUp = EntityArrow.PickupStatus.CREATIVE_ONLY;
		else if(living instanceof EntityPlayer)
			PlayerHelper.consumeAmmo(((EntityPlayer) living), AMMO_FUNC);
	}

	@Override
	public void onUpdate(ItemStack stack, World world, Entity player, int par4, boolean par5) {
		if(!world.isRemote && player instanceof EntityPlayer && stack.getItemDamage() > 0 && ManaItemHandler.requestManaExactForTool(stack, (EntityPlayer) player, MANA_PER_DAMAGE * 2, true))
			stack.setItemDamage(stack.getItemDamage() - 1);
	}

	@Override
	public boolean getIsRepairable(ItemStack par1ItemStack, ItemStack par2ItemStack) {
		return par2ItemStack.getItem() == ModItems.manaResource && par2ItemStack.getItemDamage() == 3 ? true : super.getIsRepairable(par1ItemStack, par2ItemStack);
	}

	@Override
	public boolean usesMana(ItemStack stack) {
		return true;
	}

}
