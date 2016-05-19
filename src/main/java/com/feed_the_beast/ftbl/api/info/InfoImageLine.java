package com.feed_the_beast.ftbl.api.info;

import com.feed_the_beast.ftbl.api.client.FTBLibClient;
import com.feed_the_beast.ftbl.gui.info.ButtonInfoImage;
import com.feed_the_beast.ftbl.gui.info.ButtonInfoTextLine;
import com.feed_the_beast.ftbl.gui.info.GuiInfo;
import com.feed_the_beast.ftbl.util.TextureCoords;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import latmod.lib.LMUtils;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.awt.image.BufferedImage;

/**
 * Created by LatvianModder on 23.03.2016.
 */
public class InfoImageLine extends InfoExtendedTextLine
{
    public String imageURL;
    private TextureCoords texture;
    private double displayW, displayH, displayS;

    public InfoImageLine(InfoPage c)
    {
        super(c, null);
    }

    @SideOnly(Side.CLIENT)
    public TextureCoords getImage()
    {
        if(texture == TextureCoords.nullTexture)
        {
            return null;
        }
        else if(texture != null)
        {
            return texture;
        }

        texture = TextureCoords.nullTexture;

        try
        {
            //File file = new File(FTBLib.folderModpack, "images/" + imageURL);
            //if(FTBLib.DEV_ENV) { FTBLib.dev_logger.info("Loading Guide image: " + file.getAbsolutePath()); }
            BufferedImage img = page.getResourceProvider().getConnection(imageURL).connect().asImage();
            ResourceLocation tex = FTBLibClient.mc.getTextureManager().getDynamicTextureLocation("ftbu_guide/" + imageURL, new DynamicTexture(img));
            texture = new TextureCoords(tex, 0D, 0D, img.getWidth(), img.getHeight(), img.getWidth(), img.getHeight());
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }

        return texture;
    }

    public InfoImageLine setImage(TextureCoords t)
    {
        texture = t;
        imageURL = null;
        text = null;
        return this;
    }

    @SideOnly(Side.CLIENT)
    public TextureCoords getDisplayImage()
    {
        TextureCoords img = getImage();
        if(img == null)
        {
            return null;
        }
        double w = (displayW > 0D) ? displayW : (displayS == 0D ? texture.width : (displayS > 0D ? texture.width * displayS : (texture.width / -displayS)));
        double h = (displayH > 0D) ? displayH : (displayS == 0D ? texture.height : (displayS > 0D ? texture.height * displayS : (texture.height / -displayS)));
        return new TextureCoords(texture.texture, 0D, 0D, w, h, w, h);
    }

    public InfoImageLine setImage(String img)
    {
        String imageURL0 = imageURL == null ? null : (imageURL + "");
        imageURL = img;
        if(!LMUtils.areObjectsEqual(imageURL0, imageURL, true))
        {
            texture = null;
        }
        if(imageURL != null)
        {
            text = null;
        }
        return this;
    }

    public InfoImageLine setSize(double w, double h)
    {
        displayW = w;
        displayH = h;
        return this;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public ButtonInfoTextLine createWidget(GuiInfo gui)
    {
        if(getImage() == null)
        {
            return null;
        }
        return new ButtonInfoImage(gui, this);
    }

    @Override
    public void fromJson(JsonElement e)
    {
        super.fromJson(e);

        displayW = displayH = displayS = 0D;

        JsonObject o = e.getAsJsonObject();

        setImage(o.has("image") ? o.get("image").getAsString() : null);

        if(o.has("scale"))
        {
            displayS = o.get("scale").getAsDouble();
        }
        else
        {
            if(o.has("width"))
            {
                displayW = o.get("width").getAsDouble();
            }

            if(o.has("height"))
            {
                displayH = o.get("height").getAsDouble();
            }
        }
    }

    @Override
    public JsonElement getSerializableElement()
    {
        JsonObject o = (JsonObject) super.getSerializableElement();

        if(imageURL != null && !imageURL.isEmpty())
        {
            o.add("image", new JsonPrimitive(imageURL));
        }

        return o;
    }
}