package aaron;

import picocli.CommandLine;
import java.util.concurrent.Callable;

@CommandLine.Command(name = "aaron", footer = "Copyright(c) 2025 Markus Schmitz", mixinStandardHelpOptions = true, subcommands = {
        CliConverter.class
})
public class AAroNCLI implements Callable<Integer> {

    @Override
    public Integer call() {
        System.out.println("Try to use the import command");
        return 0;
    }

    public static int execute(String[] args) {
        return  new CommandLine(new AAroNCLI()).execute(args);
    }

    public static void main(String[] args) {
        int exitCode = execute(args);
        System.exit(exitCode);
    }

}
