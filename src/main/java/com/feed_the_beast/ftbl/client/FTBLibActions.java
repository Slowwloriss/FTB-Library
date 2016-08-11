package com.feed_the_beast.ftbl.client;

import com.feed_the_beast.ftbl.FTBLibFinals;
import com.feed_the_beast.ftbl.api.client.FTBLibClient;
import com.feed_the_beast.ftbl.api.config.ClientConfigRegistry;
import com.feed_the_beast.ftbl.api.gui.GuiIcons;
import com.feed_the_beast.ftbl.api.gui.GuiLM;
import com.feed_the_beast.ftbl.api.gui.GuiScreenRegistry;
import com.feed_the_beast.ftbl.api.gui.IMouseButton;
import com.feed_the_beast.ftbl.api.gui.guibuttons.SidebarButton;
import com.feed_the_beast.ftbl.api.gui.guibuttons.SidebarButtonRegistry;
import com.feed_the_beast.ftbl.api.notification.ClientNotifications;
import com.feed_the_beast.ftbl.gui.GuiEditConfig;
import com.feed_the_beast.ftbl.gui.GuiInfo;
import com.feed_the_beast.ftbl.gui.friends.InfoFriendsGUI;
import com.latmod.lib.TextureCoords;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;

public class FTBLibActions
{
    @SideOnly(Side.CLIENT)
    public static void init()
    {
        SidebarButtonRegistry.add(new ResourceLocation(FTBLibFinals.MOD_ID, "friends_gui"), new SidebarButton(995, TextureCoords.fromUV(new ResourceLocation(FTBLibFinals.MOD_ID, "textures/gui/friendsbutton.png")), null)
        {
            @Override
            public void onClicked(IMouseButton button)
            {
                new GuiInfo("friends_gui", new InfoFriendsGUI()).openGui();
            }

            @Override
            @Nullable
            public ITextComponent getDisplayNameOverride()
            {
                return new TextComponentString("FriendsGUI");
            }

            @Override
            public void postRender(Minecraft mc, double ax, double ay)
            {
                if(!ClientNotifications.Perm.map.isEmpty())
                {
                    String n = String.valueOf(ClientNotifications.Perm.map.size());
                    int nw = mc.fontRendererObj.getStringWidth(n);
                    int width = 16;
                    GlStateManager.color(1F, 0.13F, 0.13F, 0.66F);
                    GuiLM.drawBlankRect(ax + width - nw, ay - 4, nw + 1, 9);
                    GlStateManager.color(1F, 1F, 1F, 1F);
                    mc.fontRendererObj.drawString(n, (int) (ax + width - nw + 1), (int) (ay - 3), 0xFFFFFFFF);
                }
            }
        });

        SidebarButtonRegistry.add(new ResourceLocation(FTBLibFinals.MOD_ID, "settings"), new SidebarButton(990, GuiIcons.settings, null)
        {
            @Override
            @SideOnly(Side.CLIENT)
            public void onClicked(IMouseButton button)
            {
                new GuiEditConfig(null, ClientConfigRegistry.CONTAINER).openGui();
            }
        });

        SidebarButtonRegistry.add(new ResourceLocation(FTBLibFinals.MOD_ID, "my_server_settings"), new SidebarButton(985, GuiIcons.settings_red, true)
        {
            @Override
            @SideOnly(Side.CLIENT)
            public void onClicked(IMouseButton button)
            {
                FTBLibClient.execClientCommand("/ftb my_settings", false);
            }
        });

        SidebarButtonRegistry.add(new ResourceLocation(FTBLibFinals.MOD_ID, "heal"), new SidebarButton(200, GuiIcons.heart, true)
        {
            @Override
            @SideOnly(Side.CLIENT)
            public void onClicked(IMouseButton button)
            {
                FTBLibClient.execClientCommand("/ftb heal", false);
            }
        });

        SidebarButtonRegistry.add(new ResourceLocation(FTBLibFinals.MOD_ID, "toggle_gamemode"), new SidebarButton(195, GuiIcons.toggle_gamemode, true)
        {
            @Override
            @SideOnly(Side.CLIENT)
            public void onClicked(IMouseButton button)
            {
                int i = Minecraft.getMinecraft().thePlayer.capabilities.isCreativeMode ? 0 : 1;
                FTBLibClient.execClientCommand("/gamemode " + i, false);
            }
        });

        SidebarButtonRegistry.add(new ResourceLocation(FTBLibFinals.MOD_ID, "toggle_rain"), new SidebarButton(190, GuiIcons.toggle_rain, true)
        {
            @Override
            @SideOnly(Side.CLIENT)
            public void onClicked(IMouseButton button)
            {
                FTBLibClient.execClientCommand("/toggledownfall", false);
            }
        });

        SidebarButtonRegistry.add(new ResourceLocation(FTBLibFinals.MOD_ID, "set_day"), new SidebarButton(185, GuiIcons.toggle_day, true)
        {
            @Override
            @SideOnly(Side.CLIENT)
            public void onClicked(IMouseButton button)
            {
                FTBLibClient.execClientCommand("/time set 6000", false);
            }
        });

        SidebarButtonRegistry.add(new ResourceLocation(FTBLibFinals.MOD_ID, "set_night"), new SidebarButton(180, GuiIcons.toggle_night, true)
        {
            @Override
            @SideOnly(Side.CLIENT)
            public void onClicked(IMouseButton button)
            {
                FTBLibClient.execClientCommand("/time set 18000", false);
            }
        });

        GuiScreenRegistry.register(new ResourceLocation(FTBLibFinals.MOD_ID, "friends_gui"), () -> new GuiInfo("friends_gui", new InfoFriendsGUI()).getWrapper());
        GuiScreenRegistry.register(new ResourceLocation(FTBLibFinals.MOD_ID, "client_config"), () -> new GuiEditConfig(null, ClientConfigRegistry.CONTAINER).getWrapper());
    }
}