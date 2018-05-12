package com.pixelatedsource.jda.commands.management;

import com.pixelatedsource.jda.Helpers;
import com.pixelatedsource.jda.PixelSniper;
import com.pixelatedsource.jda.blub.Category;
import com.pixelatedsource.jda.blub.Command;
import com.pixelatedsource.jda.blub.CommandEvent;
import com.pixelatedsource.jda.blub.RoleType;
import net.dv8tion.jda.core.entities.Guild;

import java.util.HashMap;

import static com.pixelatedsource.jda.PixelSniper.PREFIX;

public class SetMuteRoleCommand extends Command {

    public SetMuteRoleCommand() {
        this.commandName = "setmuterole";
        this.description = "Set the role that will be added to the user when he/she gets muted";
        this.usage = PREFIX + commandName + " <@role | roleId>";
        this.aliases = new String[]{"smr"};
        this.category = Category.MANAGEMENT;
    }

    public static HashMap<String, String> muteRoles = PixelSniper.mySQL.getRoleMap(RoleType.MUTE);

    @Override
    protected void execute(CommandEvent event) {
        if (event.getGuild() != null) {
            if (Helpers.hasPerm(event.getMember(), commandName, 1)) {
                Guild guild = event.getGuild();
                String[] args = event.getArgs().split("\\s+");
                String role = muteRoles.getOrDefault(guild.getId(), null);
                if (args.length == 0 || args[0].equalsIgnoreCase("")) {
                    if (role != null && role.matches("\\d+") && guild.getRoleById(role) != null) event.reply("Current MuteRole: **@" + guild.getRoleById(role).getName() + "**");
                    else event.reply("Current MuteRole: **null**");
                } else {
                    String muteRoleId;
                    if (args[0].matches("\\d+") && guild.getRoleById(args[0]) != null) muteRoleId = guild.getRoleById(args[0]).getId();
                    else if (event.getMessage().getMentionedRoles().size() > 0) muteRoleId = event.getMessage().getMentionedRoles().get(0).getId();
                    else muteRoleId = "null";
                    new Thread(() -> PixelSniper.mySQL.setRole(guild, muteRoleId, RoleType.MUTE)).start();
                    if (muteRoles.containsKey(guild.getId())) muteRoles.replace(guild.getId(), muteRoleId);
                    else muteRoles.put(guild.getId(), muteRoleId);
                    String oldRoleName = role == null || role.equalsIgnoreCase("null") ? "null" : "@" + guild.getRoleById(role).getName();
                    String newRoleName = muteRoleId.equalsIgnoreCase("null") ? "null" : "@" + guild.getRoleById(muteRoleId).getName();
                    event.reply("JoinRole changed from **" + oldRoleName + "** to **" + newRoleName + "**");
                }
            } else {
                event.reply("You need the permission `" + commandName + "` to execute this command.");
            }
        } else {
            event.reply(Helpers.guildOnly);
        }
    }
}
