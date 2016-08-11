package com.feed_the_beast.ftbl.api.events.player;

import com.feed_the_beast.ftbl.api.IForgePlayer;

/**
 * Created by LatvianModder on 11.08.2016.
 */
public class ForgePlayerLoggedInEvent extends ForgePlayerEvent
{
    private final boolean first;

    public ForgePlayerLoggedInEvent(IForgePlayer p, boolean f)
    {
        super(p);
        first = f;
    }

    public boolean isFirstLogin()
    {
        return first;
    }
}