package me.melijn.jda.commands.fun;

import me.melijn.jda.blub.Category;
import me.melijn.jda.blub.Command;
import me.melijn.jda.blub.CommandEvent;
import net.dv8tion.jda.core.entities.User;

import static me.melijn.jda.Melijn.PREFIX;

public class HugCommand extends Command {


    public HugCommand() {
        this.commandName = "hug";
        this.description = "Shows a hugging person [anime]";
        this.usage = PREFIX + commandName + " [user]";
        this.category = Category.FUN;
        this.id = 96;
    }

    @Override
    protected void execute(CommandEvent event) {
        if (event.getGuild() == null || event.hasPerm(event.getMember(), commandName, 0)) {
            String[] args = event.getArgs().split("\\s+");
            if (args.length == 0 || args[0].isEmpty()) {
                event.getWebUtils().getImage("hug",
                        image -> event.getMessageHelper().sendFunText("**" + event.getAuthor().getName() + "** wants to hug someone", image.getUrl(), event)
                );
            } else if (args.length == 1) {
                User target = event.getHelpers().getUserByArgsN(event, args[0]);
                if (target == null) {
                    event.reply("Unknown user");
                } else {
                    event.getWebUtils().getImage("hug",
                            image -> event.getMessageHelper().sendFunText("**" + target.getName() + "** got hugged by **" + event.getAuthor().getName() + "**", image.getUrl(), event)
                    );
                }
            } else {
                event.sendUsage(this, event);
            }
        } else {
            event.reply("You need the permission `" + commandName + "` to execute this command.");
        }
    }
}
