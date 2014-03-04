package vazkii.botania.common.block.subtile.generating;

import cpw.mods.fml.common.network.PacketDispatcher;
import net.minecraft.block.Block;
import net.minecraft.nbt.NBTTagCompound;
import vazkii.botania.api.lexicon.LexiconEntry;
import vazkii.botania.common.Botania;
import vazkii.botania.common.block.subtile.SubTileGenerating;
import vazkii.botania.common.lexicon.LexiconData;

public class SubTileHydroangeas extends SubTileGenerating {

    private static final String TAG_BURN_TIME = "burnTime";
    int burnTime = 0;
    int currentTime = 0;

    @Override
    public void onUpdate(){
        super.onUpdate();

        int range = 1;

        boolean didSomething = false;

        if (burnTime == 0) {

            if (supertile.worldObj.getWorldTime() % 40L == 0) {

                if (mana < getMaxMana() && !supertile.worldObj.isRemote) {

                    for (int i = -range; i <= range; i++) {

                        for (int j = -range; j <= range; j++) {

                            if (supertile.worldObj.getBlockId(supertile.xCoord + i, supertile.yCoord, supertile.zCoord + j) == Block.waterStill.blockID && supertile.worldObj.getBlockMetadata(supertile.xCoord + i, supertile.yCoord, supertile.zCoord + j) == 0) {

                                supertile.worldObj.setBlockToAir(supertile.xCoord + i, supertile.yCoord, supertile.zCoord + j);
                                didSomething = true;
                                burnTime++;

                                break;

                            }
                        }
                    }

                }

                if (didSomething)
                    PacketDispatcher.sendPacketToAllInDimension(supertile.getDescriptionPacket(), supertile.worldObj.provider.dimensionId);
            }
        } else
        {
            if (supertile.worldObj.rand.nextInt(8) == 0)
                Botania.proxy.wispFX(supertile.worldObj, supertile.xCoord + 0.55 + Math.random() * 0.2 - 0.1, supertile.yCoord + 0.55 + Math.random() * 0.2 - 0.1, supertile.zCoord + 0.5, 0.7F, 0.05F, 0.05F, (float) Math.random() / 6, (float) -Math.random() / 60);
            burnTime--;
        }

    }


    @Override
    public int getMaxMana() {
        return 150;
    }

    @Override
    public int getValueForPassiveGeneration(){
        return 2;
    }

    @Override
    public int getColor(){
        if (burnTime > 0)
            return 0x532FE0;
        else
            return 0xA62997;

    }

    @Override
    public LexiconEntry getEntry()
    {
        return LexiconData.hydroangeas;
    }

    @Override
    public void writeToPacketNBT(NBTTagCompound cmp){
        super.writeToPacketNBT(cmp);

        cmp.setInteger(TAG_BURN_TIME, burnTime);
    }

    @Override
    public void readFromPacketNBT(NBTTagCompound cmp){
        super.readFromPacketNBT(cmp);

        burnTime = cmp.getInteger(TAG_BURN_TIME);
    }

    @Override
    public boolean canGeneratePassively(){
        return burnTime > 0;
    }

    @Override
    public int getDelayBetweenPassiveGeneration(){
        return 2;
    }


}
