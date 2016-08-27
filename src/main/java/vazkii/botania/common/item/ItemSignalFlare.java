/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 * 
 * File Created @ [Mar 3, 2014, 6:49:15 PM (GMT)]
 */
package vazkii.botania.common.item;

import net.minecraft.client.resources.I18n;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import vazkii.botania.client.core.handler.ModelHandler;
import vazkii.botania.common.achievement.ModAchievements;
import vazkii.botania.common.core.helper.ItemNBTHelper;
import vazkii.botania.common.entity.EntitySignalFlare;
import vazkii.botania.common.lib.LibItemNames;

import javax.annotation.Nonnull;
import java.util.List;

public class ItemSignalFlare extends ItemMod implements IColorable {

	private static final String TAG_COLOR = "color";

	public ItemSignalFlare() {
		super(LibItemNames.SIGNAL_FLARE);
		setMaxStackSize(1);
		setNoRepair();
		setMaxDamage(200);
	}

	@Nonnull
	@Override
	public ActionResult<ItemStack> onItemRightClick(@Nonnull ItemStack par1ItemStack, World world, EntityPlayer player, EnumHand hand) {
		if(par1ItemStack.getItemDamage() == 0) {
			if(world.isRemote)
				player.swingArm(hand);
			else {
				EntitySignalFlare flare = new EntitySignalFlare(world);
				flare.setPosition(player.posX, player.posY, player.posZ);
				flare.setColor(getColor(par1ItemStack));
				flare.setFiredAt((int) player.posY);
				world.playSound(null, player.posX, player.posY, player.posZ, SoundEvents.ENTITY_GENERIC_EXPLODE, SoundCategory.PLAYERS, 40F, (1.0F + (world.rand.nextFloat() - world.rand.nextFloat()) * 0.2F) * 0.7F);

				world.spawnEntityInWorld(flare);

				int stunned = 0;
				int range = 5;
				List<EntityLivingBase> entities = world.getEntitiesWithinAABB(EntityLivingBase.class, new AxisAlignedBB(player.posX - range, player.posY - range, player.posZ - range, player.posX + range, player.posY + range, player.posZ + range));
				for(EntityLivingBase entity : entities)
					if(entity != player && (!(entity instanceof EntityPlayer) || FMLCommonHandler.instance().getMinecraftServerInstance() == null || FMLCommonHandler.instance().getMinecraftServerInstance().isPVPEnabled())) {
						entity.addPotionEffect(new PotionEffect(MobEffects.SLOWNESS, 50, 5));
						stunned++;
					}

				if(stunned >= 100)
					player.addStat(ModAchievements.signalFlareStun, 1);
			}
			par1ItemStack.damageItem(200, player);
			return ActionResult.newResult(EnumActionResult.SUCCESS, par1ItemStack);
		}

		return ActionResult.newResult(EnumActionResult.PASS, par1ItemStack);
	}

	@Override
	public void onUpdate(ItemStack par1ItemStack, World world, Entity par3Entity, int par4, boolean par5) {
		if(par1ItemStack.isItemDamaged())
			par1ItemStack.setItemDamage(par1ItemStack.getItemDamage() - 1);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public int getColorFromItemStack(ItemStack par1ItemStack, int par2) {
		if(par2 == 0)
			return 0xFFFFFF;

		return EnumDyeColor.byMetadata(getColor(par1ItemStack)).getMapColor().colorValue;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void getSubItems(@Nonnull Item item, CreativeTabs tab, List<ItemStack> stacks) {
		for(int i = 0; i < 16; i++)
			stacks.add(forColor(i));
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void addInformation(ItemStack par1ItemStack, EntityPlayer player, List<String> stacks, boolean par4) {
		int storedColor = getColor(par1ItemStack);
		stacks.add(I18n.format("botaniamisc.flareColor", I18n.format("botania.color" + storedColor)));
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void registerModels() {
		ModelHandler.registerItemAllMeta(this, EnumDyeColor.values().length);
	}

	public static ItemStack forColor(int color) {
		ItemStack stack = new ItemStack(ModItems.signalFlare);
		ItemNBTHelper.setInt(stack, TAG_COLOR, color);

		return stack;
	}

	public static int getColor(ItemStack stack) {
		return ItemNBTHelper.getInt(stack, TAG_COLOR, 0xFFFFFF);
	}
}