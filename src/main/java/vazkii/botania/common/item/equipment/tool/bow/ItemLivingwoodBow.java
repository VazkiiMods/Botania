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
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.init.Enchantments;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.IItemPropertyGetter;
import net.minecraft.item.ItemArrow;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.event.entity.player.ArrowLooseEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import vazkii.botania.api.mana.IManaUsingItem;
import vazkii.botania.api.mana.ManaItemHandler;
import vazkii.botania.client.lib.LibResources;
import vazkii.botania.common.Botania;
import vazkii.botania.common.core.BotaniaCreativeTab;
import vazkii.botania.common.core.helper.PlayerHelper;
import vazkii.botania.common.item.ModItems;
import vazkii.botania.common.item.equipment.tool.ToolCommons;
import vazkii.botania.common.lib.LibItemNames;
import vazkii.botania.common.lib.LibMisc;

import javax.annotation.Nonnull;
import java.util.function.Predicate;

public class ItemLivingwoodBow extends ItemBow implements IManaUsingItem {

	private static final Predicate<ItemStack> AMMO_FUNC = s -> s != null && s.getItem() instanceof ItemArrow;
	public static final int MANA_PER_DAMAGE = 40;

	public ItemLivingwoodBow() {
		this(LibItemNames.LIVINGWOOD_BOW);
	}

	public ItemLivingwoodBow(String name) {
		setCreativeTab(BotaniaCreativeTab.INSTANCE);
		GameRegistry.register(this, new ResourceLocation(LibMisc.MOD_ID, name));
		setUnlocalizedName(name);
		setMaxDamage(500);
		addPropertyOverride(new ResourceLocation("minecraft:pull"), new IItemPropertyGetter() {
			@Override
			public float apply(@Nonnull ItemStack stack, World worldIn, EntityLivingBase entityIn) {
				if (entityIn == null)
				{
					return 0.0F;
				}
				else
				{
					ItemStack itemstack = entityIn.getActiveItemStack();
					return itemstack != null && itemstack.getItem() instanceof ItemLivingwoodBow ? (float)(stack.getMaxItemUseDuration() - entityIn.getItemInUseCount()) * chargeVelocityMultiplier() / 20.0F : 0.0F;
				}
			}
		});
	}

	@Nonnull
	@Override
	public String getUnlocalizedNameInefficiently(@Nonnull ItemStack par1ItemStack) {
		return super.getUnlocalizedNameInefficiently(par1ItemStack).replaceAll("item.", "item." + LibResources.PREFIX_MOD);
	}

	@Nonnull
	@Override
	public ActionResult<ItemStack> onItemRightClick(@Nonnull ItemStack stack, @Nonnull World world, EntityPlayer player, @Nonnull EnumHand hand) {
		// Copy from superclass with our own check
		boolean flag = canFire(stack, player);
		ActionResult<ItemStack> ret = ForgeEventFactory.onArrowNock(stack, world, player, hand, flag);
		if (ret != null) return ret;

		if (!player.capabilities.isCreativeMode && !flag)
		{
			return new ActionResult<>(EnumActionResult.FAIL, stack);
		}
		else
		{
			player.setActiveHand(hand);
			return new ActionResult<>(EnumActionResult.SUCCESS, stack);
		}
	}

	@Override
	public void onPlayerStoppedUsing(@Nonnull ItemStack stack, @Nonnull World world, EntityLivingBase shooter, int useTicks) {

		boolean isPlayer = shooter instanceof EntityPlayer;
		EntityPlayer player = isPlayer ? (EntityPlayer) shooter : null;

		// Begin copy modified ItemBow.onPlayerStoppedUsing
		boolean flag = !isPlayer || canFire(stack, player); // Botania - Custom canFire check
		ItemStack itemstack = this.getAmmo(shooter);

		int i = (int) ((getMaxItemUseDuration(stack) - useTicks) * chargeVelocityMultiplier()); // Botania - velocity multiplier
		if(isPlayer)
			i = net.minecraftforge.event.ForgeEventFactory.onArrowLoose(stack, world, player, i, itemstack != null || flag);
		if (i < 0) return;

		if (itemstack != null || flag)
		{
			if (itemstack == null)
			{
				itemstack = new ItemStack(Items.ARROW);
			}

			float f = getArrowVelocity(i);

			if ((double)f >= 0.1D)
			{
				boolean infinite = !isPlayer || player.capabilities.isCreativeMode || (itemstack.getItem() instanceof ItemArrow && ((ItemArrow) itemstack.getItem()).isInfinite(itemstack, stack, player));

				if (!world.isRemote)
				{
					ItemArrow itemarrow = (ItemArrow) (itemstack.getItem() instanceof ItemArrow ? itemstack.getItem() : Items.ARROW);
					EntityArrow entityarrow = itemarrow.createArrow(world, itemstack, shooter);
					entityarrow.setAim(shooter, shooter.rotationPitch, shooter.rotationYaw, 0.0F, f * 3.0F, 1.0F);

					if (f == 1.0F)
					{
						entityarrow.setIsCritical(true);
					}

					int j = EnchantmentHelper.getEnchantmentLevel(Enchantments.POWER, stack);

					if (j > 0)
					{
						entityarrow.setDamage(entityarrow.getDamage() + (double)j * 0.5D + 0.5D);
					}

					int k = EnchantmentHelper.getEnchantmentLevel(Enchantments.PUNCH, stack);

					if (k > 0)
					{
						entityarrow.setKnockbackStrength(k);
					}

					if (EnchantmentHelper.getEnchantmentLevel(Enchantments.FLAME, stack) > 0)
					{
						entityarrow.setFire(100);
					}

					// Botania - move bow damage into onFire (below)

					if (infinite)
					{
						entityarrow.pickupStatus = EntityArrow.PickupStatus.CREATIVE_ONLY;
					}

					world.spawnEntityInWorld(entityarrow);
					onFire(stack, shooter, infinite, entityarrow);
				}


				world.playSound(null, shooter.posX, shooter.posY, shooter.posZ, SoundEvents.ENTITY_ARROW_SHOOT, SoundCategory.NEUTRAL, 1.0F, 1.0F / (itemRand.nextFloat() * 0.4F + 1.2F) + f * 0.5F);

				// Botania - move ammo consumption into onFire (above)

				if(isPlayer)
					player.addStat(StatList.getObjectUseStats(this));
			}
		}
		// End modified ItemBow.onPlayerStoppedUsing
	}

	float chargeVelocityMultiplier() {
		return 1F;
	}

	boolean canFire(ItemStack stack, EntityPlayer player) {
		return player.capabilities.isCreativeMode || EnchantmentHelper.getEnchantmentLevel(Enchantments.INFINITY, stack) > 0 || PlayerHelper.hasAmmo(player, AMMO_FUNC);
	}

	void onFire(ItemStack bow, EntityLivingBase living, boolean infinity, EntityArrow arrow) {
		if(living instanceof EntityPlayerMP) {
			ToolCommons.damageItem(bow, 1, living, MANA_PER_DAMAGE);
			if(((EntityPlayerMP) living).interactionManager.getGameType().isSurvivalOrAdventure())
				PlayerHelper.consumeAmmo(((EntityPlayerMP) living), AMMO_FUNC);
		}
	}

	@Override
	public void onUpdate(ItemStack stack, World world, Entity player, int par4, boolean par5) {
		if(!world.isRemote && player instanceof EntityPlayer && stack.getItemDamage() > 0 && ManaItemHandler.requestManaExactForTool(stack, (EntityPlayer) player, MANA_PER_DAMAGE * 2, true))
			stack.setItemDamage(stack.getItemDamage() - 1);
	}

	@Override
	public boolean getIsRepairable(ItemStack par1ItemStack, ItemStack par2ItemStack) {
		return par2ItemStack.getItem() == ModItems.manaResource && par2ItemStack.getItemDamage() == 3 || super.getIsRepairable(par1ItemStack, par2ItemStack);
	}

	@Override
	public boolean usesMana(ItemStack stack) {
		return true;
	}

	protected ItemStack getAmmo(EntityLivingBase shooter) {
		if(shooter instanceof EntityPlayer)
			return PlayerHelper.getAmmo((EntityPlayer) shooter, AMMO_FUNC);
		else return new ItemStack(Items.ARROW);
	}

}
