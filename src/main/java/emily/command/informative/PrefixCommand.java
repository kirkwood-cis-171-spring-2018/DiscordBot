/*
 * Copyright 2017 github.com/kaaz
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package emily.command.informative;

import emily.core.AbstractCommand;
import emily.guildsettings.GSetting;
import emily.handler.GuildSettings;
import emily.handler.Template;
import emily.main.BotConfig;
import emily.main.DiscordBot;
import emily.permission.SimpleRank;
import emily.util.DisUtil;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.User;

public class PrefixCommand extends AbstractCommand {
    public PrefixCommand() {
        super();
    }

    @Override
    public String getDescription() {
        return "Forgot what the prefix is? I got you covered";
    }

    @Override
    public String getCommand() {
        return "prefix";
    }

    @Override
    public String[] getUsage() {
        return new String[]{
                "prefix                           //shows the set prefix",
                "prefix <prefix>                  //sets the prefix to <prefix>",
        };
    }

    @Override
    public String[] getAliases() {
        return new String[]{};
    }

    @Override
    public String execute(DiscordBot bot, String[] args, MessageChannel channel, User author, Message inputMessage) {
        SimpleRank rank = bot.security.getSimpleRank(author, channel);
        if (args.length > 0 && rank.isAtLeast(SimpleRank.GUILD_ADMIN) && channel instanceof TextChannel) {
            TextChannel text = (TextChannel) channel;
            GuildSettings guildSettings = GuildSettings.get(text.getGuild());
            if (guildSettings.set(text.getGuild(), GSetting.COMMAND_PREFIX, args[0])) {
                return Template.get(channel, "command_prefix_saved", args[0]);
            }
            return Template.get(channel, "command_prefix_invalid",
                    args[0],
                    "```" + BotConfig.EOL + GSetting.COMMAND_PREFIX.getDescription() + BotConfig.EOL + "```");
        }
        return Template.get(channel, "command_prefix_is", DisUtil.getCommandPrefix(channel));
    }
}