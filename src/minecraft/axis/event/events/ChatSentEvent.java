package axis.event.events;

import java.util.Iterator;

import axis.Axis;
import axis.command.Command;
import axis.event.Cancellable;
import axis.event.Event;
import axis.util.Logger;
import net.minecraft.client.Minecraft;

public final class ChatSentEvent extends Event implements Cancellable
{
    private boolean cancel;
    private String message;

    public ChatSentEvent(String message)
    {
        this.message = message;
    }

    public void checkForCommands()
    {
    	if (this.message.startsWith("."))
        {
            Iterator var2 = Axis.getCommandManager().getContents().iterator();

            while (var2.hasNext())
            {
                Command command = (Command)var2.next();

                if (this.message.split(" ")[0].equalsIgnoreCase("." + command.getCommand()))
                {
                    try
                    {
                        command.run(this.message);
                    }
                    catch (Exception var9)
                    {
                        Logger.logChat("Invalid arguments! " + command.getCommand() + " " + command.getArguments());
                    }

                    this.cancel = true;
                }
                else
                {
                    String[] var6;
                    int var5 = (var6 = command.getAliases()).length;

                    for (int var4 = 0; var4 < var5; ++var4)
                    {
                        String alias = var6[var4];

                        if (this.message.split(" ")[0].equalsIgnoreCase("." + alias))
                        {
                            try
                            {
                                command.run(this.message);
                            }
                            catch (Exception var8)
                            {
                                Logger.logChat("Invalid arguments! " + alias + " " + command.getArguments());
                            }

                            this.cancel = true;
                        }
                    }
                }
            }

            if (!this.cancel)
            {
                Logger.logChat("Command \"" + this.message + "\" was not found!");
                this.cancel = true;
            }
        }
    }

    public String getMessage()
    {
        return this.message;
    }

    public boolean isCancelled()
    {
        return this.cancel;
    }

    public void setCancelled(boolean cancel)
    {
        this.cancel = cancel;
    }

    public void setMessage(String message)
    {
        this.message = message;
    }
}
