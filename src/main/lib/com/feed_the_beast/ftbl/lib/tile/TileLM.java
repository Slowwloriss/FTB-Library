package com.feed_the_beast.ftbl.lib.tile;

import com.feed_the_beast.ftbl.api.security.ISecure;
import com.feed_the_beast.ftbl.lib.Security;
import com.feed_the_beast.ftbl.lib.math.BlockDimPos;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.relauncher.Side;

import javax.annotation.Nullable;

public class TileLM extends TileEntity
{
    public final Security security = createSecurity();
    private boolean isDirty = true;
    private IBlockState currentState;

    protected Security createSecurity()
    {
        return new Security(ISecure.SAVE_OWNER);
    }

    @Override
    public final NBTTagCompound writeToNBT(NBTTagCompound tag)
    {
        super.writeToNBT(tag);
        writeTileData(tag);
        return tag;
    }

    @Override
    public final void readFromNBT(NBTTagCompound tag)
    {
        super.readFromNBT(tag);
        readTileData(tag);
    }

    @Override
    @Nullable
    public final SPacketUpdateTileEntity getUpdatePacket()
    {
        return new SPacketUpdateTileEntity(getPos(), 0, getUpdateTag());
    }

    @Override
    public final NBTTagCompound getUpdateTag()
    {
        NBTTagCompound tag = new NBTTagCompound();
        writeTileClientData(tag);
        return tag;
    }

    @Override
    public final void onDataPacket(NetworkManager m, SPacketUpdateTileEntity p)
    {
        handleUpdateTag(p.getNbtCompound());
    }

    @Override
    public final void handleUpdateTag(NBTTagCompound tag)
    {
        readTileClientData(tag);
        onUpdatePacket();
    }

    public void writeTileData(NBTTagCompound nbt)
    {
        nbt.setTag("Security", security.serializeNBT());
    }

    public void readTileData(NBTTagCompound nbt)
    {
        security.deserializeNBT(nbt.getTag("Security"));
    }

    public void writeTileClientData(NBTTagCompound nbt)
    {
        nbt.setTag("SCR", security.serializeNBT());
    }

    public void readTileClientData(NBTTagCompound nbt)
    {
        security.deserializeNBT(nbt.getTag("SCR"));
    }

    protected boolean rerenderBlock()
    {
        return false;
    }

    protected boolean updateComparator()
    {
        return false;
    }

    protected void onUpdatePacket()
    {
        if(rerenderBlock())
        {
            IBlockState state = getBlockState();
            worldObj.notifyBlockUpdate(pos, state, state, 7);
        }
    }

    @Override
    public void onLoad()
    {
        isDirty = true;
    }

    @Override
    public void onChunkUnload()
    {
    }

    @Override
    public void markDirty()
    {
        isDirty = true;
    }

    public final void checkIfDirty()
    {
        if(isDirty)
        {
            sendDirtyUpdate();
            isDirty = false;
        }
    }

    public void sendDirtyUpdate()
    {
        if(worldObj != null && !worldObj.isRemote)
        {
            updateContainingBlockInfo();
            worldObj.markChunkDirty(getPos(), this);

            if(getBlockType() != Blocks.AIR)
            {
                worldObj.updateComparatorOutputLevel(getPos(), getBlockType());
            }
        }
    }

    @Override
    public void updateContainingBlockInfo()
    {
        super.updateContainingBlockInfo();
        currentState = null;
    }

    public IBlockState getBlockState()
    {
        if(currentState == null)
        {
            currentState = worldObj.getBlockState(getPos());
        }

        return currentState;
    }

    public void onPlacedBy(EntityLivingBase el, ItemStack is, IBlockState state)
    {
        security.setOwner(el.getUniqueID());
        markDirty();
    }

    public boolean isExplosionResistant()
    {
        return !security.getPrivacyLevel().isPublic();
    }

    public final Side getSide()
    {
        return (worldObj != null && !worldObj.isRemote) ? Side.SERVER : Side.CLIENT;
    }

    public void notifyNeighbors()
    {
        worldObj.notifyBlockOfStateChange(getPos(), getBlockType());
    }

    public void onNeighborBlockChange(BlockPos pos)
    {
        if(worldObj != null)
        {
            //redstonePowered = worldObj.isBlockPowered(getPos());
            updateContainingBlockInfo();
        }
    }

    public void playSound(SoundEvent event, SoundCategory category, float volume, float pitch)
    {
        worldObj.playSound(null, pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D, event, category, volume, pitch);
    }

    public BlockDimPos getDimPos()
    {
        return new BlockDimPos(pos, hasWorldObj() ? worldObj.provider.getDimension() : 0);
    }
}