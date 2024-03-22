package dreamlink.cli;

import dreamlink.cli.command.BuildCommand;
import dreamlink.cli.command.EditCommand;
import dreamlink.cli.command.ExploreCommand;
import dreamlink.cli.command.ExploreLocalCommand;
import dreamlink.cli.command.ICommand;
import dreamlink.cli.command.PublishCommand;
import dreamlink.logger.Logger;
import net.sourceforge.argparse4j.ArgumentParsers;
import net.sourceforge.argparse4j.inf.ArgumentParserException;

public class Entry {

    private static ICommand[] commands = new ICommand[] {
        new EditCommand(),
        new ExploreCommand(),
        new ExploreLocalCommand(),
        new BuildCommand(),
        new PublishCommand()
    };

    public static void main(String args[]) {
        var parser = ArgumentParsers.newFor("DreamLink").build().defaultHelp(true);
        var subParsers = parser.addSubparsers().dest("command");
        for(var command : commands) {
            var subParser = subParsers.addParser(command.getCommandName());
            command.setupParser(subParser);
        }

        try {
            var parsedArgs = parser.parseArgs(args);
            for(var command : commands) {
                if(!command.getCommandName().equals(parsedArgs.getString("command"))) {
                    continue;
                }

                var msg = String.format("Running command: %s", command.getCommandName());
                Logger.instance.debug(msg);
                command.run(parsedArgs);
            }

        } catch (ArgumentParserException e) {
            parser.handleError(e);
            System.exit(1);
        }
    }
    
}
