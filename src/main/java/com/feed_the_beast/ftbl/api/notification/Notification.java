package com.feed_the_beast.ftbl.api.notification;

import com.feed_the_beast.ftbl.api.item.ItemStackSerializer;
import com.feed_the_beast.ftbl.util.JsonHelper;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import latmod.lib.LMColorUtils;
import latmod.lib.util.FinalIDObject;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IJsonSerializable;
import net.minecraft.util.text.ITextComponent;

public final class Notification extends FinalIDObject implements IJsonSerializable
{
    public ITextComponent title, desc;
    public int timer, color;
    public ItemStack item;
    public MouseAction mouse;

    public Notification(String s)
    {
        super(s);
    }

    public Notification(String s, ITextComponent t, int l)
    {
        super(s);
        title = t;
        timer = l;
    }

    public static Notification deserialize(JsonElement e)
    {
        if(e == null || !e.isJsonObject())
        {
            return null;
        }
        JsonObject o = e.getAsJsonObject();
        if(!o.has("id") || !o.has("title"))
        {
            return null;
        }
        Notification n = new Notification(o.get("id").getAsString());
        n.fromJson(o);
        return n;
    }

    private void setDefaults()
    {
        title = null;
        desc = null;
        timer = 3000;
        color = 0xFFA0A0A0;
        item = null;
        mouse = null;
    }

    public void setDesc(ITextComponent c)
    {
        desc = c;
    }

    public void setItem(ItemStack is)
    {
        item = is;
    }

    public void setColor(int c)
    {
        color = c;
    }

    public void setMouseAction(MouseAction e)
    {
        mouse = e;
    }

    public boolean isTemp()
    {
        return mouse == null;
    }

    @Override
    public JsonElement getSerializableElement()
    {
        JsonObject o = new JsonObject();
        o.add("id", new JsonPrimitive(getID()));
        o.add("title", JsonHelper.serializeICC(title));
        if(timer != 3000)
        {
            o.add("timer", new JsonPrimitive(timer));
        }
        if(desc != null)
        {
            o.add("desc", JsonHelper.serializeICC(desc));
        }
        if(item != null)
        {
            o.add("item", ItemStackSerializer.serialize(item));
        }
        if(color != 0xFFA0A0A0)
        {
            o.add("color", LMColorUtils.serialize(color));
        }
        if(mouse != null)
        {
            o.add("mouse", mouse.getSerializableElement());
        }
        return o;
    }

    @Override
    public void fromJson(JsonElement e)
    {
        if(e == null || !e.isJsonObject())
        {
            return;
        }
        setDefaults();
        JsonObject o = e.getAsJsonObject();
        title = JsonHelper.deserializeICC(o.get("title"));
        timer = o.has("timer") ? o.get("timer").getAsInt() : 3000;
        if(o.has("desc"))
        {
            setDesc(JsonHelper.deserializeICC(o.get("desc")));
        }
        if(o.has("color"))
        {
            setColor(LMColorUtils.deserialize(o.get("color")));
        }
        if(o.has("item"))
        {
            setItem(ItemStackSerializer.deserialize(o.get("item")));
        }
        if(o.has("mouse"))
        {
            MouseAction m = new MouseAction();
            m.fromJson(o.get("mouse"));
            setMouseAction(m);
        }
    }

    @Override
    public String toString()
    {
        return getSerializableElement().toString();
    }
}