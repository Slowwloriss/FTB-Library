package com.feed_the_beast.ftbl.api.events.world;

import com.feed_the_beast.ftbl.api.IForgeWorld;

/**
 * Created by LatvianModder on 11.08.2016.
 */
public class ForgeWorldLoadedEvent extends ForgeWorldEvent
{
    public ForgeWorldLoadedEvent(IForgeWorld w)
    {
        super(w);
    }
}