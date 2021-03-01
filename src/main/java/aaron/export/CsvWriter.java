package aaron.export;

import aaron.model.Model;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class CsvWriter {

    public static void write(final Model model, final File file) throws IOException {
        CSVPrinter printer = CSVFormat.DEFAULT.print(file, StandardCharsets.UTF_8);
        model.getNodes().forEach((key, node) -> {

        });
        printer.printRecord();
    }
}
