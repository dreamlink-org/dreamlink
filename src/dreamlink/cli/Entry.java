package dreamlink.cli;

import dreamlink.cli.command.BuildCommand;
import dreamlink.cli.command.EditCommand;
import dreamlink.cli.command.ExploreCommand;
import dreamlink.cli.command.ExploreLocalCommand;
import dreamlink.cli.command.ICLICommand;
import dreamlink.cli.command.PublishCommand;
import dreamlink.logger.Logger;
import net.sourceforge.argparse4j.ArgumentParsers;
import net.sourceforge.argparse4j.inf.ArgumentParser;
import net.sourceforge.argparse4j.inf.ArgumentParserException;

public class Entry {

    private static ICLICommand[] commands = new ICLICommand[] {
        new EditCommand(),
        new ExploreCommand(),
        new ExploreLocalCommand(),
        new BuildCommand(),
        new PublishCommand()
    };

    private static String destination = "command";
    private ArgumentParser parser;

    public Entry() {
        this.parser = ArgumentParsers.newFor("DreamLink").build();
        var subParsers = this.parser.addSubparsers().dest(Entry.destination);
        for(var command : Entry.commands) {
            var subParser = subParsers.addParser(command.getCommandName());
            command.setupParser(subParser);
        }
    }


    public void runCommand(String args[]) {
        try {
            var parsedArgs = this.parser.parseArgs(args);
            for(var command : Entry.commands) {
                if(command.getCommandName().equals(parsedArgs.getString(Entry.destination))) {
                    command.run(parsedArgs);
                    break;
                }
            }
        } catch (ArgumentParserException e) {
            Logger.instance.error(String.format("Error parsing arguments: %s", e.getMessage()));
            System.exit(1);
        }
    }

    public static void main(String args[]) {
        new Entry().runCommand(args);
    }
    
}
