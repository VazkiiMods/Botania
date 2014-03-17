/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under a
 * Creative Commons Attribution-NonCommercial-ShareAlike 3.0 License
 * (http://creativecommons.org/licenses/by-nc-sa/3.0/deed.en_GB)
 * 
 * File Created @ [Jan 31, 2014, 3:02:58 PM (GMT)]
 */
package vazkii.botania.common.item;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Icon;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;
import vazkii.botania.api.internal.IManaBurst;
import vazkii.botania.api.mana.BurstProperties;
import vazkii.botania.api.mana.ILens;
import vazkii.botania.api.mana.IManaReceiver;
import vazkii.botania.client.core.helper.IconHelper;
import vazkii.botania.common.Botania;
import vazkii.botania.common.core.helper.ItemNBTHelper;
import vazkii.botania.common.core.helper.MathHelper;
import vazkii.botania.common.core.helper.Vector3;
import vazkii.botania.common.item.recipe.CompositeLensRecipe;
import vazkii.botania.common.lib.LibItemIDs;
import vazkii.botania.common.lib.LibItemNames;

public class ItemLens extends ItemMod implements ILens {

	private static final int NORMAL = 0, SPEED = 1, POWER = 2, TIME = 3, EFFICIENCY = 4,
			BOUNCE = 5, GRAVITY = 6, MINE = 7, DAMAGE = 8, PHANTOM = 9, 
			MAGNET = 10, EXPLOSIVE = 11;
	
	private static final String TAG_COLOR = "color";
	private static final String TAG_COMPOSITE_LENS = "compositeLens";

	public static Icon iconGlass;

	public static final int SUBTYPES = 12;
	Icon[] ringIcons;

	public ItemLens() {
		super(LibItemIDs.idLens);
		setUnlocalizedName(LibItemNames.LENS);
		setMaxStackSize(1);
		setHasSubtypes(true);

		GameRegistry.addRecipe(new CompositeLensRecipe());
	}

	@Override
	public void registerIcons(IconRegister par1IconRegister) {
		iconGlass = IconHelper.forName(par1IconRegister, "lensInside");

		ringIcons = new Icon[SUBTYPES];
		for(int i = 0; i < ringIcons.length; i++)
			ringIcons[i] = IconHelper.forNameRaw(par1IconRegister, LibItemNames.LENS_NAMES[i]);
	}

	@Override
	public void getSubItems(int par1, CreativeTabs par2CreativeTabs, List par3List) {
		for(int i = 0; i < SUBTYPES; i++)
			par3List.add(new ItemStack(par1, 1, i));
	}

	@Override
	public boolean requiresMultipleRenderPasses() {
		return true;
	}

	@Override
	public Icon getIconFromDamageForRenderPass(int par1, int par2) {
		return par2 == 0 ? ringIcons[Math.min(SUBTYPES - 1, par1)] : iconGlass;
	}

	@Override
	public Icon getIconFromDamage(int par1) {
		return getIconFromDamageForRenderPass(par1, 0);
	}

	@Override
	public int getColorFromItemStack(ItemStack par1ItemStack, int par2) {
		return par2 == 1 ? getLensColor(par1ItemStack) : 0xFFFFFF;
	}

	@Override
	public String getUnlocalizedName(ItemStack par1ItemStack) {
		return "item." + LibItemNames.LENS_NAMES[Math.min(SUBTYPES - 1, par1ItemStack.getItemDamage())];
	}

	@Override
	public void addInformation(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, List par3List, boolean par4) {
		int storedColor = getStoredColor(par1ItemStack);
		if(storedColor != -1)
			par3List.add(String.format(StatCollector.translateToLocal("botaniamisc.color"), StatCollector.translateToLocal("botania.color" + storedColor)));
	}
	

	public String getItemShortTermName(ItemStack stack) {
		return StatCollector.translateToLocal(stack.getUnlocalizedName() + ".short");
	}
	
	@Override
	public String getItemDisplayName(ItemStack stack) {
		ItemStack compositeLens = getCompositeLens(stack);
		if(compositeLens == null)
			return super.getItemDisplayName(stack);
		return String.format(StatCollector.translateToLocal("item.botania:compositeLens.name"), getItemShortTermName(stack), getItemShortTermName(compositeLens));
	}
	
	@Override
	public void apply(ItemStack stack, BurstProperties props) {
		int storedColor = getStoredColor(stack);
		if(storedColor != -1)
			props.color = getLensColor(stack);
		
		switch(stack.getItemDamage()) {
		case SPEED : {
			props.motionModifier *= 2F;
			props.maxMana *= 0.75F;
			props.ticksBeforeManaLoss /= 3F;
			props.manaLossPerTick *= 2F;
			break;
		}
		case POWER : {
			props.maxMana *= 2;
			props.motionModifier *= 0.85F;
			props.manaLossPerTick *= 2F;
			break;
		}
		case TIME : {
			props.ticksBeforeManaLoss *= 2.25F;
			props.motionModifier *= 0.8F;
			break;
		}
		case EFFICIENCY : {
			props.manaLossPerTick /= 5F;
			props.ticksBeforeManaLoss *= 1.1F;
			break;
		}
		case GRAVITY : {
			props.gravity = 0.0015F;
			props.ticksBeforeManaLoss *= 1.2F;
			break;
		}
		}
		
		ItemStack compositeLens = getCompositeLens(stack);
		if(compositeLens != null && compositeLens.getItem() instanceof ILens)
			((ILens) compositeLens.getItem()).apply(compositeLens, props);
	}

	@Override
	public boolean collideBurst(IManaBurst burst, MovingObjectPosition pos, boolean isManaBlock, boolean dead, ItemStack stack) {
		EntityThrowable entity = (EntityThrowable) burst;

		switch(stack.getItemDamage()) {
		case BOUNCE : {
			if(!isManaBlock && pos.entityHit == null) {
				ChunkCoordinates coords = burst.getBurstSourceChunkCoordinates();
				if(coords.posX != pos.blockX || coords.posY != pos.blockY || coords.posZ != pos.blockZ) {
					Vector3 currentMovementVec = new Vector3(entity.motionX, entity.motionY, entity.motionZ);
					ForgeDirection dir = ForgeDirection.getOrientation(pos.sideHit);
					Vector3 normalVector = new Vector3(dir.offsetX, dir.offsetY, dir.offsetZ).normalize();
					Vector3 movementVec = normalVector.multiply(-2 * currentMovementVec.dotProduct(normalVector)).add(currentMovementVec);

					burst.setMotion(movementVec.x, movementVec.y, movementVec.z);
					dead = false;
				}
			}
			break;
		}
		case MINE : {
			World world = entity.worldObj;
			int x = pos.blockX;
			int y = pos.blockY;
			int z = pos.blockZ;
			int id = world.getBlockId(x, y, z);

			int meta = world.getBlockMetadata(x, y, z);
			Block block = Block.blocksList[id];
			float hardness = block.getBlockHardness(world, x, y, z);
			int mana = burst.getMana();

			ChunkCoordinates coords = burst.getBurstSourceChunkCoordinates();
			if((coords.posX != x || coords.posY != y || coords.posZ != z) && !isManaBlock && block != null && hardness != -1 && hardness < 50F && (burst.isFake() || mana >= 24)) {
				List<ItemStack> items = new ArrayList();

				items.addAll(block.getBlockDropped(world, x, y, z, meta, 0));

				if(!burst.hasAlreadyCollidedAt(x, y, z)) {
					if(!burst.isFake() && !entity.worldObj.isRemote) {
						world.setBlockToAir(x, y, z);
						entity.worldObj.playAuxSFX(2001, x, y, z, id + (meta << 12));

						for(ItemStack stack_ : items)
							world.spawnEntityInWorld(new EntityItem(world, x + 0.5, y + 0.5, z + 0.5, stack_));
						burst.setMana(mana - 24);
					}
				}

				dead = false;
			}
			break;
		}
		case DAMAGE : {
			if(pos.entityHit != null && pos.entityHit instanceof EntityLivingBase) {
				EntityLivingBase living = (EntityLivingBase) pos.entityHit;
				if(living.hurtTime == 0) {
					int mana = burst.getMana();
					if(mana >= 16) {
						burst.setMana(mana - 16);
						if(!burst.isFake() && !entity.worldObj.isRemote)
							living.attackEntityFrom(DamageSource.magic, 2);
					}
				}
			}
		}
		case PHANTOM : {
			if(!isManaBlock) {
				dead = false;
				burst.setMinManaLoss(Math.max(0, burst.getMinManaLoss() - 4));
			}
		}
		case EXPLOSIVE : { 
			if(!burst.isFake()) {
				ChunkCoordinates coords = burst.getBurstSourceChunkCoordinates();
				if(!entity.worldObj.isRemote && pos.entityHit == null && !isManaBlock && (pos.blockX != coords.posX || pos.blockY != coords.posY || pos.blockZ != coords.posZ))
					entity.worldObj.createExplosion(entity, entity.posX, entity.posY, entity.posZ, (float) burst.getMana() / 50F, true); 
			} else dead = false;
		}
		}
		
		ItemStack compositeLens = getCompositeLens(stack);
		if(compositeLens != null && compositeLens.getItem() instanceof ILens)
			dead = ((ILens) compositeLens.getItem()).collideBurst(burst, pos, isManaBlock, dead, compositeLens);
		
		return dead;
	}

	@Override
	public void updateBurst(IManaBurst burst, ItemStack stack) {
		int storedColor = getStoredColor(stack);
		if(storedColor == 16)
			burst.setColor(getLensColor(stack));

		EntityThrowable entity = (EntityThrowable) burst;

		boolean magnetized = entity.getEntityData().hasKey("Botania:Magnetized");
		switch(stack.getItemDamage()) {
		case MAGNET : {
			int x = (int) entity.posX;
			int y = (int) entity.posY;
			int z = (int) entity.posZ;
			int range = 3;

			magnetize : {
				for(int i = -range; i < range; i++)
					for(int j = -range; j < range; j++)
						for(int k = -range; k < range; k++)
							if(entity.worldObj.getBlockTileEntity(i + x, j + y, k + z) instanceof IManaReceiver) {
								TileEntity tile = entity.worldObj.getBlockTileEntity(i + x, j + y, k + z);
								
								if(magnetized) {
									int magX = entity.getEntityData().getInteger("Botania:MagnetizedX");
									int magY = entity.getEntityData().getInteger("Botania:MagnetizedY");
									int magZ = entity.getEntityData().getInteger("Botania:MagnetizedZ");
									if(tile.xCoord != magX || tile.yCoord != magY || tile.zCoord != magZ)
										continue;
								}
								
								IManaReceiver receiver = (IManaReceiver) tile;

								ChunkCoordinates srcCoords = burst.getBurstSourceChunkCoordinates();

								if(MathHelper.pointDistanceSpace(tile.xCoord, tile.yCoord, tile.zCoord, srcCoords.posX, srcCoords.posY, srcCoords.posZ) > 3 && receiver.canRecieveManaFromBursts() && !receiver.isFull()) {
									Vector3 burstVec = Vector3.fromEntity(entity);
									Vector3 tileVec = Vector3.fromTileEntityCenter(tile).add(0, -0.1, 0);
									Vector3 motionVec = new Vector3(entity.motionX, entity.motionY, entity.motionZ);

									Vector3 normalMotionVec = motionVec.copy().normalize(); 
									Vector3 magnetVec = tileVec.sub(burstVec).normalize();
									Vector3 differenceVec = normalMotionVec.sub(magnetVec).multiply(motionVec.mag() * 0.1);

									Vector3 finalMotionVec = motionVec.sub(differenceVec);
									if(!magnetized) {
										finalMotionVec.multiply(0.75);
										entity.getEntityData().setBoolean("Botania:Magnetized", true);
										entity.getEntityData().setInteger("Botania:MagnetizedX", tile.xCoord);
										entity.getEntityData().setInteger("Botania:MagnetizedY", tile.yCoord);
										entity.getEntityData().setInteger("Botania:MagnetizedZ", tile.zCoord);
									}
									
									burst.setMotion(finalMotionVec.x, finalMotionVec.y, finalMotionVec.z);
									break magnetize;
								}
							}
			}
		}
		}
		
		ItemStack compositeLens = getCompositeLens(stack);
		if(compositeLens != null && compositeLens.getItem() instanceof ILens)
			((ILens) compositeLens.getItem()).updateBurst(burst, compositeLens);
	}

	@Override
	public int getLensColor(ItemStack stack) {
		int storedColor = getStoredColor(stack);

		if(storedColor == -1)
			return 0xFFFFFF;

		if(storedColor == 16)
			return Color.HSBtoRGB(Botania.proxy.getWorldElapsedTicks() * 2 % 360 / 360F, 1F, 1F);

		float[] color = EntitySheep.fleeceColorTable[storedColor];
		return new Color(color[0], color[1], color[2]).getRGB();
	}

	public static int getStoredColor(ItemStack stack) {
		return ItemNBTHelper.getInt(stack, TAG_COLOR, -1);
	}

	public static ItemStack setLensColor(ItemStack stack, int color) {
		ItemNBTHelper.setInt(stack, TAG_COLOR, color);
		return stack;
	}

	@Override
	public boolean doParticles(IManaBurst burst, ItemStack stack) {
		int storedColor = getStoredColor(stack);
		if(storedColor == 16 && !burst.isFake())
			return ((EntityThrowable) burst).ticksExisted > 5;

			return true;
	}

	// TODO
	@Override
	public boolean canCombineLenses(ItemStack sourceLens, ItemStack compositeLens) {
		if(sourceLens.getItemDamage() == compositeLens.getItemDamage())
			return false;
		
		return true;
	}
	
	@Override
	public ItemStack getCompositeLens(ItemStack stack) {
		NBTTagCompound cmp = ItemNBTHelper.getCompound(stack, TAG_COMPOSITE_LENS, false);
		ItemStack lens = ItemStack.loadItemStackFromNBT(cmp);
		return lens;
	}

	@Override
	public ItemStack setCompositeLens(ItemStack sourceLens, ItemStack compositeLens) {
		NBTTagCompound cmp = new NBTTagCompound();
		compositeLens.writeToNBT(cmp);
		ItemNBTHelper.setCompound(sourceLens, TAG_COMPOSITE_LENS, cmp);
		
		return sourceLens;
	}
}
