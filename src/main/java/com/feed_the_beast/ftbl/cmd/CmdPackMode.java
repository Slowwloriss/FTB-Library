package com.feed_the_beast.ftbl.cmd;

import com.feed_the_beast.ftbl.FTBLibLang;
import com.feed_the_beast.ftbl.api.events.ReloadType;
import com.feed_the_beast.ftbl.api_impl.FTBLibAPI_Impl;
import com.feed_the_beast.ftbl.lib.cmd.CommandLM;
import com.feed_the_beast.ftbl.lib.util.LMStringUtils;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.server.command.CommandTreeBase;

import javax.annotation.Nullable;
import java.util.List;

public class CmdPackMode extends CommandTreeBase
{
    public static class CmdSet extends CommandLM
    {
        public CmdSet(String s)
        {
            super(s);
        }

        @Override
        public String getCommandUsage(ICommandSender ics)
        {
            return '/' + commandName + " <modeID>";
        }

        @Override
        public List<String> getTabCompletionOptions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos pos)
        {
            if(args.length == 1)
            {
                return getListOfStringsMatchingLastWord(args, FTBLibAPI_Impl.INSTANCE.getPackModes().getModes());
            }

            return super.getTabCompletionOptions(server, sender, args, pos);
        }

        @Override
        public void execute(MinecraftServer server, ICommandSender ics, String[] args) throws CommandException
        {
            checkArgs(args, 1, "<modeID>");

            ITextComponent c;

            int i = FTBLibAPI_Impl.INSTANCE.getSharedData(Side.SERVER).setMode(args[0]);

            if(i == 1)
            {
                c = FTBLibLang.MODE_NOT_FOUND.textComponent();
                c.getStyle().setColor(TextFormatting.RED);
            }
            else if(i == 2)
            {
                c = FTBLibLang.MODE_ALREADY_SET.textComponent();
                c.getStyle().setColor(TextFormatting.RED);
            }
            else
            {
                c = FTBLibLang.MODE_LOADED.textComponent(args[0]);
                c.getStyle().setColor(TextFormatting.GREEN);
                FTBLibAPI_Impl.INSTANCE.reload(ics, ReloadType.SERVER_AND_CLIENT);
            }

            ics.addChatMessage(c);
        }
    }

    public static class CmdGet extends CommandLM
    {
        public CmdGet(String s)
        {
            super(s);
        }

        @Override
        public int getRequiredPermissionLevel()
        {
            return 0;
        }

        @Override
        public void execute(MinecraftServer server, ICommandSender ics, String[] args) throws CommandException
        {
            ITextComponent c = FTBLibLang.MODE_CURRENT.textComponent(FTBLibAPI_Impl.INSTANCE.getSharedData(Side.SERVER).getPackMode().getID());
            c.getStyle().setColor(TextFormatting.AQUA);
            ics.addChatMessage(c);
        }
    }

    public static class CmdList extends CommandLM
    {
        public CmdList(String s)
        {
            super(s);
        }

        @Override
        public int getRequiredPermissionLevel()
        {
            return 0;
        }

        @Override
        public void execute(MinecraftServer server, ICommandSender ics, String[] args) throws CommandException
        {
            ITextComponent c = FTBLibLang.MODE_LIST.textComponent(LMStringUtils.strip(FTBLibAPI_Impl.INSTANCE.getPackModes().getModes()));
            c.getStyle().setColor(TextFormatting.AQUA);
            ics.addChatMessage(c);
        }
    }

    public CmdPackMode()
    {
        addSubcommand(new CmdSet("set"));
        addSubcommand(new CmdGet("getPrivacyLevel"));
        addSubcommand(new CmdList("list"));
    }

    @Override
    public String getCommandName()
    {
        return "packmode";
    }

    @Override
    public int getRequiredPermissionLevel()
    {
        return 0;
    }

    @Override
    public String getCommandUsage(ICommandSender sender)
    {
        return "command.ftb.packmode.usage";
    }
}