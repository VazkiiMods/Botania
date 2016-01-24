/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 * 
 * File Created @ [Jan 26, 2014, 12:23:55 AM (GMT)]
 */
package vazkii.botania.common.block.tile.mana;

import java.awt.Color;
import java.util.List;

import com.google.common.base.Predicates;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ITickable;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.StatCollector;

import net.minecraft.world.World;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import org.lwjgl.opengl.GL11;

import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.api.internal.VanillaPacketDispatcher;
import vazkii.botania.api.item.IDyablePool;
import vazkii.botania.api.item.IManaDissolvable;
import vazkii.botania.api.mana.IKeyLocked;
import vazkii.botania.api.mana.IManaItem;
import vazkii.botania.api.mana.IManaPool;
import vazkii.botania.api.mana.IThrottledPacket;
import vazkii.botania.api.mana.ManaNetworkEvent;
import vazkii.botania.api.mana.spark.ISparkAttachable;
import vazkii.botania.api.mana.spark.ISparkEntity;
import vazkii.botania.api.recipe.RecipeManaInfusion;
import vazkii.botania.api.state.BotaniaStateProps;
import vazkii.botania.api.state.enums.PoolVariant;
import vazkii.botania.client.core.handler.HUDHandler;
import vazkii.botania.client.core.handler.LightningHandler;
import vazkii.botania.client.core.helper.RenderHelper;
import vazkii.botania.client.lib.LibResources;
import vazkii.botania.common.Botania;
import vazkii.botania.common.block.ModBlocks;
import vazkii.botania.common.block.tile.TileMod;
import vazkii.botania.common.core.handler.ConfigHandler;
import vazkii.botania.common.core.handler.ManaNetworkHandler;
import vazkii.botania.common.core.helper.Vector3;
import vazkii.botania.common.item.ItemManaTablet;
import vazkii.botania.common.item.ModItems;
import vazkii.botania.common.lib.LibMisc;
import vazkii.botania.common.lib.LibObfuscation;

public class TilePool extends TileMod implements IManaPool, IDyablePool, IKeyLocked, ISparkAttachable, IThrottledPacket {

	public static final int MAX_MANA = 1000000;
	public static final int MAX_MANA_DILLUTED = 10000;

	private static final String TAG_MANA = "mana";
	private static final String TAG_KNOWN_MANA = "knownMana";
	private static final String TAG_OUTPUTTING = "outputting";
	private static final String TAG_COLOR = "color";
	private static final String TAG_MANA_CAP = "manaCap";
	private static final String TAG_CAN_ACCEPT = "canAccept";
	private static final String TAG_CAN_SPARE = "canSpare";
	private static final String TAG_FRAGILE = "fragile";
	private static final String TAG_INPUT_KEY = "inputKey";
	private static final String TAG_OUTPUT_KEY = "outputKey";

	boolean outputting = false;
	public boolean alchemy = false;
	public boolean conjuration = false;
	boolean catalystsRegistered = false;

	public EnumDyeColor color = EnumDyeColor.WHITE;
	int mana;
	int knownMana = -1;

	public int manaCap = -1;
	int soundTicks = 0;
	boolean canAccept = true;
	boolean canSpare = true;
	public boolean fragile = false;
	public boolean isDoingTransfer = false;
	public int ticksDoingTransfer = 0;

	String inputKey = "";
	String outputKey = "";

	int ticks = 0;
	boolean sendPacket = false;

	@Override
	public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newState) {
		return true;
	}

	@Override
	public boolean isFull() {
		Block blockBelow = worldObj.getBlockState(pos.down()).getBlock();
		return blockBelow != ModBlocks.manaVoid && getCurrentMana() >= manaCap;
	}

	@Override
	public void recieveMana(int mana) {
		this.mana = Math.max(0, Math.min(getCurrentMana() + mana, manaCap));
		worldObj.updateComparatorOutputLevel(pos, worldObj.getBlockState(pos).getBlock());
		markDispatchable();
	}

	@Override
	public void invalidate() {
		super.invalidate();
		ManaNetworkEvent.removePool(this);
	}

	@Override
	public void onChunkUnload() {
		super.onChunkUnload();
		invalidate();
	}

	public boolean collideEntityItem(EntityItem item) {
		if(item.isDead)
			return false;

		boolean didChange = false;
		ItemStack stack = item.getEntityItem();
		if(stack == null)
			return false;

		if(stack.getItem() instanceof IManaDissolvable) {
			((IManaDissolvable) stack.getItem()).onDissolveTick(this, stack, item);
			if(stack.stackSize == 0)
				item.setDead();
		}

		int age = ObfuscationReflectionHelper.getPrivateValue(EntityItem.class, item, LibObfuscation.AGE);
		if(age > 100 && age < 130 || !catalystsRegistered)
			return false;

		for(RecipeManaInfusion recipe : BotaniaAPI.manaInfusionRecipes) {
			if(recipe.matches(stack) && (!recipe.isAlchemy() || alchemy) && (!recipe.isConjuration() || conjuration)) {
				int mana = recipe.getManaToConsume();
				if(getCurrentMana() >= mana) {
					recieveMana(-mana);

					if(!worldObj.isRemote) {
						stack.stackSize--;
						if(stack.stackSize == 0)
							item.setDead();

						ItemStack output = recipe.getOutput().copy();
						EntityItem outputItem = new EntityItem(worldObj, pos.getX() + 0.5, pos.getY() + 1.5, pos.getZ() + 0.5, output);
						ObfuscationReflectionHelper.setPrivateValue(EntityItem.class, outputItem, 105, LibObfuscation.AGE);
						worldObj.spawnEntityInWorld(outputItem);
					}

					craftingFanciness();
					didChange = true;
				}

				break;
			}
		}

		return didChange;
	}

	public void craftingFanciness() {
		if(soundTicks == 0) {
			worldObj.playSoundEffect(pos.getX(), pos.getY(), pos.getZ(), "botania:manaPoolCraft", 0.4F, 4F);
			soundTicks = 6;
		}

		for(int i = 0; i < 25; i++) {
			float red = (float) Math.random();
			float green = (float) Math.random();
			float blue = (float) Math.random();
			Botania.proxy.sparkleFX(worldObj, pos.getX() + 0.5 + Math.random() * 0.4 - 0.2, pos.getY() + 1, pos.getZ() + 0.5 + Math.random() * 0.4 - 0.2, red, green, blue, (float) Math.random(), 10);
		}
	}

	@Override
	public void updateEntity() {
		boolean wasDoingTransfer = isDoingTransfer;
		isDoingTransfer = false;
		if(manaCap == -1)
			manaCap = worldObj.getBlockState(getPos()).getValue(BotaniaStateProps.POOL_VARIANT) == PoolVariant.DILUTED ? MAX_MANA_DILLUTED : MAX_MANA;

		if(!ManaNetworkHandler.instance.isPoolIn(this) && !isInvalid())
			ManaNetworkEvent.addPool(this);

		if(soundTicks > 0)
			soundTicks--;

		if(worldObj.isRemote) {
			double particleChance = 1F - (double) getCurrentMana() / (double) manaCap * 0.1;
			Color color = new Color(0x00C6FF);
			if(Math.random() > particleChance)
				Botania.proxy.wispFX(worldObj, pos.getX() + 0.3 + Math.random() * 0.5, pos.getY() + 0.6 + Math.random() * 0.25, pos.getZ() + Math.random(), color.getRed() / 255F, color.getGreen() / 255F, color.getBlue() / 255F, (float) Math.random() / 3F, (float) -Math.random() / 25F, 2F);
		}

		if(sendPacket && ticks % 10 == 0) {
			VanillaPacketDispatcher.dispatchTEToNearbyPlayers(this);
			sendPacket = false;
		}

		alchemy = worldObj.getBlockState(pos.down()).getBlock() == ModBlocks.alchemyCatalyst;
		conjuration = worldObj.getBlockState(pos.down()).getBlock() == ModBlocks.conjurationCatalyst;
		catalystsRegistered = true;

		List<EntityItem> items = worldObj.getEntitiesWithinAABB(EntityItem.class, new AxisAlignedBB(pos, pos.add(1, 1, 1)));
		for(EntityItem item : items) {
			if(item.isDead)
				continue;

			ItemStack stack = item.getEntityItem();
			if(stack != null && stack.getItem() instanceof IManaItem) {
				IManaItem mana = (IManaItem) stack.getItem();
				if(outputting && mana.canReceiveManaFromPool(stack, this) || !outputting && mana.canExportManaToPool(stack, this)) {
					boolean didSomething = false;

					int bellowCount = 0;
					if(outputting)
						for(EnumFacing dir : LibMisc.CARDINAL_DIRECTIONS) {
							TileEntity tile = worldObj.getTileEntity(pos.offset(dir));
							if(tile != null && tile instanceof TileBellows && ((TileBellows) tile).getLinkedTile() == this)
								bellowCount++;
						}
					int transfRate = 1000 * (bellowCount + 1);

					if(outputting) {
						if(canSpare) {
							if(getCurrentMana() > 0 && mana.getMana(stack) < mana.getMaxMana(stack))
								didSomething = true;

							int manaVal = Math.min(transfRate, Math.min(getCurrentMana(), mana.getMaxMana(stack) - mana.getMana(stack)));
							if(!worldObj.isRemote)
								mana.addMana(stack, manaVal);
							recieveMana(-manaVal);
						}
					} else {
						if(canAccept) {
							if(mana.getMana(stack) > 0 && !isFull())
								didSomething = true;

							int manaVal = Math.min(transfRate, Math.min(manaCap - getCurrentMana(), mana.getMana(stack)));
							if(!worldObj.isRemote)
								mana.addMana(stack, -manaVal);
							recieveMana(manaVal);
						}
					}

					if(didSomething) {
						if(worldObj.isRemote && ConfigHandler.chargingAnimationEnabled && worldObj.rand.nextInt(20) == 0) {
							Vector3 itemVec = Vector3.fromTileEntity(this).add(0.5, 0.5 + Math.random() * 0.3, 0.5);
							Vector3 tileVec = Vector3.fromTileEntity(this).add(0.2 + Math.random() * 0.6, 0, 0.2 + Math.random() * 0.6);
							LightningHandler.spawnLightningBolt(worldObj, outputting ? tileVec : itemVec, outputting ? itemVec : tileVec, 80, worldObj.rand.nextLong(), 0x4400799c, 0x4400C6FF);
						}
						isDoingTransfer = outputting;
					}
				}
			}
		}

		if(isDoingTransfer)
			ticksDoingTransfer++;
		else {
			ticksDoingTransfer = 0;
			if(wasDoingTransfer)
				VanillaPacketDispatcher.dispatchTEToNearbyPlayers(this);
		}

		ticks++;
	}

	@Override
	public void writeCustomNBT(NBTTagCompound cmp) {
		cmp.setInteger(TAG_MANA, mana);
		cmp.setBoolean(TAG_OUTPUTTING, outputting);
		cmp.setInteger(TAG_COLOR, color.getMetadata());

		cmp.setInteger(TAG_MANA_CAP, manaCap);
		cmp.setBoolean(TAG_CAN_ACCEPT, canAccept);
		cmp.setBoolean(TAG_CAN_SPARE, canSpare);
		cmp.setBoolean(TAG_FRAGILE, fragile);

		cmp.setString(TAG_INPUT_KEY, inputKey);
		cmp.setString(TAG_OUTPUT_KEY, outputKey);
	}

	@Override
	public void readCustomNBT(NBTTagCompound cmp) {
		mana = cmp.getInteger(TAG_MANA);
		outputting = cmp.getBoolean(TAG_OUTPUTTING);
		color = EnumDyeColor.byMetadata(cmp.getInteger(TAG_COLOR));

		if(cmp.hasKey(TAG_MANA_CAP))
			manaCap = cmp.getInteger(TAG_MANA_CAP);
		if(cmp.hasKey(TAG_CAN_ACCEPT))
			canAccept = cmp.getBoolean(TAG_CAN_ACCEPT);
		if(cmp.hasKey(TAG_CAN_SPARE))
			canSpare = cmp.getBoolean(TAG_CAN_SPARE);
		fragile = cmp.getBoolean(TAG_FRAGILE);

		if(cmp.hasKey(TAG_INPUT_KEY))
			inputKey = cmp.getString(TAG_INPUT_KEY);
		if(cmp.hasKey(TAG_OUTPUT_KEY))
			inputKey = cmp.getString(TAG_OUTPUT_KEY);

		if(cmp.hasKey(TAG_KNOWN_MANA))
			knownMana = cmp.getInteger(TAG_KNOWN_MANA);
	}

	public void onWanded(EntityPlayer player, ItemStack wand) {
		if(player == null)
			return;

		if(player.isSneaking()) {
			outputting = !outputting;
			VanillaPacketDispatcher.dispatchTEToNearbyPlayers(worldObj, pos);
		}

		if(!worldObj.isRemote) {
			NBTTagCompound nbttagcompound = new NBTTagCompound();
			writeCustomNBT(nbttagcompound);
			nbttagcompound.setInteger(TAG_KNOWN_MANA, getCurrentMana());
			if(player instanceof EntityPlayerMP)
				((EntityPlayerMP) player).playerNetServerHandler.sendPacket(new S35PacketUpdateTileEntity(pos, -999, nbttagcompound));
		}

		worldObj.playSoundAtEntity(player, "botania:ding", 0.11F, 1F);
	}

	public void renderHUD(Minecraft mc, ScaledResolution res) {
		ItemStack pool = new ItemStack(ModBlocks.pool, 1, worldObj.getBlockState(getPos()).getValue(BotaniaStateProps.POOL_VARIANT).ordinal());
		String name = StatCollector.translateToLocal(pool.getUnlocalizedName().replaceAll("tile.", "tile." + LibResources.PREFIX_MOD) + ".name");
		int color = 0x4444FF;
		HUDHandler.drawSimpleManaHUD(color, knownMana, manaCap, name, res);

		int x = res.getScaledWidth() / 2 - 11;
		int y = res.getScaledHeight() / 2 + 30;

		int u = outputting ? 22 : 0;
		int v = 38;

		GlStateManager.enableBlend();
		GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

		mc.renderEngine.bindTexture(HUDHandler.manaBar);
		RenderHelper.drawTexturedModalRect(x, y, 0, u, v, 22, 15);
		GlStateManager.color(1F, 1F, 1F, 1F);

		ItemStack tablet = new ItemStack(ModItems.manaTablet);
		ItemManaTablet.setStackCreative(tablet);

		net.minecraft.client.renderer.RenderHelper.enableGUIStandardItemLighting();
		mc.getRenderItem().renderItemAndEffectIntoGUI(tablet, x - 20, y);
		mc.getRenderItem().renderItemAndEffectIntoGUI(pool, x + 26, y);
		net.minecraft.client.renderer.RenderHelper.disableStandardItemLighting();

		GlStateManager.disableLighting();
		GlStateManager.disableBlend();
	}

	@Override
	public boolean canRecieveManaFromBursts() {
		return true;
	}

	@Override
	public boolean isOutputtingPower() {
		return outputting;
	}

	@Override
	public int getCurrentMana() {
		return worldObj != null && worldObj.getBlockState(getPos()).getValue(BotaniaStateProps.POOL_VARIANT) == PoolVariant.CREATIVE ? MAX_MANA : mana;
	}

	@Override
	public String getInputKey() {
		return inputKey;
	}

	@Override
	public String getOutputKey() {
		return outputKey;
	}

	@Override
	public boolean canAttachSpark(ItemStack stack) {
		return true;
	}

	@Override
	public void attachSpark(ISparkEntity entity) {
		// NO-OP
	}

	@Override
	public ISparkEntity getAttachedSpark() {
		List sparks = worldObj.getEntitiesWithinAABB(Entity.class, new AxisAlignedBB(pos.up(), pos.up().add(1, 1, 1)), Predicates.instanceOf(ISparkEntity.class));
		if(sparks.size() == 1) {
			Entity e = (Entity) sparks.get(0);
			return (ISparkEntity) e;
		}

		return null;
	}

	@Override
	public boolean areIncomingTranfersDone() {
		return false;
	}

	@Override
	public int getAvailableSpaceForMana() {
		return Math.max(0, manaCap - getCurrentMana());
	}

	@Override
	public EnumDyeColor getColor() {
		return color;
	}

	@Override
	public void setColor(EnumDyeColor color) {
		this.color = color;
	}

	@Override
	public void markDispatchable() {
		sendPacket = true;
	}
}
