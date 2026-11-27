package aaron.export;

import aaron.model.AAroNEdge;
import aaron.model.ImportConext;
import aaron.model.Model;
import aaron.model.Property;
import aaron.model.PropertyDefinition;
import aaron.model.PropertyType;
import aaron.model.WithProperties;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * Writes separate CSV files only when equal property names have incompatible property types.
 */
public class AAroNConflictAwareCsvWriter extends AAroNCsvWriter {

    private CsvFiles writtenFiles = new CsvFiles(List.of(), List.of());

    @Override
    public void write(Model model, File nodesFile, File edgesFile, boolean parenthesesFix) throws IOException {
        writeFiles(model, nodesFile, edgesFile, parenthesesFix);
    }

    public CsvFiles writeFiles(Model model, File nodesFile, File edgesFile, boolean parenthesesFix) throws IOException {
        List<File> nodeFiles = writeNodeFiles(model, nodesFile, parenthesesFix);
        List<File> edgeFiles = writeEdgeFiles(model, edgesFile, parenthesesFix);
        writtenFiles = new CsvFiles(nodeFiles, edgeFiles);
        return writtenFiles;
    }

    public CsvFiles getWrittenFiles() {
        return writtenFiles;
    }

    private List<File> writeNodeFiles(Model model, File requestedFile, boolean parenthesesFix) throws IOException {
        if (requestedFile == null) {
            return List.of();
        }

        HeaderPlan plan = createNodeHeaderPlan(model, parenthesesFix);
        List<File> files = createOutputFiles(requestedFile, plan.definitions().size());
        List<CSVHeader[]> headers = plan.definitions().stream().map(CsvHeaderDefinition::toArray).toList();
        try (WriterGroup writers = WriterGroup.open(files)) {
            writeHeaders(writers, headers);
            for (var entry : model.iterateNodeEntries()) {
                int definitionIndex = plan.definitionIndex(entry.getValue());
                String[] record = createNodeRecord(headers.get(definitionIndex), entry.getKey(), entry.getValue());
                writeRecord(writers.get(definitionIndex), record);
            }
        }
        return files;
    }

    private List<File> writeEdgeFiles(Model model, File requestedFile, boolean parenthesesFix) throws IOException {
        if (requestedFile == null) {
            return List.of();
        }

        HeaderPlan plan = createEdgeHeaderPlan(model, parenthesesFix);
        List<File> files = createOutputFiles(requestedFile, plan.definitions().size());
        List<CSVHeader[]> headers = plan.definitions().stream().map(CsvHeaderDefinition::toArray).toList();
        try (WriterGroup writers = WriterGroup.open(files)) {
            writeHeaders(writers, headers);
            for (AAroNEdge edge : model.iterateEdges()) {
                int definitionIndex = plan.definitionIndex(edge);
                String[] record = createEdgeRecord(model, headers.get(definitionIndex), edge);
                if (record != null) {
                    writeRecord(writers.get(definitionIndex), record);
                }
            }
        }
        return files;
    }

    List<CsvHeaderDefinition> createNodeHeaderDefinitions(Model model, boolean parenthesesFix) {
        return createNodeHeaderPlan(model, parenthesesFix).definitions();
    }

    private HeaderPlan createNodeHeaderPlan(Model model, boolean parenthesesFix) {
        List<CSVHeader> fixedHeaders = new ArrayList<>(2);
        String idSpace = getIdSpace(model);
        fixedHeaders.add(new CSVHeader(null, idSpace == null ? "ID" : "ID(" + idSpace + ")", parenthesesFix));
        fixedHeaders.add(new CSVHeader(null, "LABEL", parenthesesFix));
        return createHeaderPlan(model.iterateNodes(), fixedHeaders, parenthesesFix);
    }

    private HeaderPlan createEdgeHeaderPlan(Model model, boolean parenthesesFix) {
        List<CSVHeader> fixedHeaders = new ArrayList<>(3);
        String idSpace = getIdSpace(model);
        fixedHeaders.add(new CSVHeader(null, idSpace == null ? "START_ID" : "START_ID(" + idSpace + ")", parenthesesFix));
        fixedHeaders.add(new CSVHeader(null, "TYPE", parenthesesFix));
        fixedHeaders.add(new CSVHeader(null, idSpace == null ? "END_ID" : "END_ID(" + idSpace + ")", parenthesesFix));
        return createHeaderPlan(model.iterateEdges(), fixedHeaders, parenthesesFix);
    }

    private HeaderPlan createHeaderPlan(
            Iterable<? extends WithProperties> values,
            List<CSVHeader> fixedHeaders,
            boolean parenthesesFix) {
        List<CsvHeaderDefinition> definitions = new ArrayList<>();
        Map<PropertySignature, Integer> assignments = new HashMap<>();
        for (WithProperties value : values) {
            PropertySignature signature = PropertySignature.from(value);
            if (assignments.containsKey(signature)) {
                continue;
            }
            Map<String, PropertyType<?>> properties = signature.propertyTypes();
            int definitionIndex = findBestDefinitionIndex(definitions, properties);
            if (definitionIndex < 0) {
                CsvHeaderDefinition definition = new CsvHeaderDefinition(fixedHeaders, parenthesesFix);
                definitions.add(definition);
                definitionIndex = definitions.size() - 1;
            }
            definitions.get(definitionIndex).add(properties);
            assignments.put(signature, definitionIndex);
        }
        if (definitions.isEmpty()) {
            definitions.add(new CsvHeaderDefinition(fixedHeaders, parenthesesFix));
            assignments.put(PropertySignature.EMPTY, 0);
        }
        return new HeaderPlan(List.copyOf(definitions), Map.copyOf(assignments));
    }

    private int findBestDefinitionIndex(
            List<CsvHeaderDefinition> definitions,
            Map<String, PropertyType<?>> properties) {
        int bestIndex = -1;
        int bestMatchCount = -1;
        for (int i = 0; i < definitions.size(); i++) {
            CsvHeaderDefinition definition = definitions.get(i);
            if (definition.supports(properties)) {
                int matchCount = definition.matchCount(properties);
                if (matchCount > bestMatchCount) {
                    bestIndex = i;
                    bestMatchCount = matchCount;
                }
            }
        }
        return bestIndex;
    }

    private String getIdSpace(Model model) {
        ImportConext context = model.getContext();
        return context == null ? null : context.getFileHash();
    }

    private List<File> createOutputFiles(File requestedFile, int count) throws IOException {
        if (requestedFile.exists() && !requestedFile.canWrite()) {
            throw new IOException("Can not write CSV file: " + requestedFile);
        }
        if (count == 1) {
            return List.of(requestedFile);
        }

        String name = requestedFile.getName();
        int extensionStart = name.lastIndexOf('.');
        String baseName = extensionStart > 0 ? name.substring(0, extensionStart) : name;
        String extension = extensionStart > 0 ? name.substring(extensionStart) : "";
        File parent = requestedFile.getAbsoluteFile().getParentFile();
        List<File> files = new ArrayList<>(count);
        for (int i = 1; i <= count; i++) {
            File file = new File(parent, baseName + "_" + i + extension);
            if (file.exists() && !file.canWrite()) {
                throw new IOException("Can not write CSV file: " + file);
            }
            files.add(file);
        }
        return List.copyOf(files);
    }

    private void writeHeaders(WriterGroup writers, List<CSVHeader[]> headers) throws IOException {
        for (int i = 0; i < headers.size(); i++) {
            String[] values = Arrays.stream(headers.get(i)).map(CSVHeader::toString).toArray(String[]::new);
            writeRecord(writers.get(i), values);
        }
    }

    private void writeRecord(BufferedWriter writer, String[] record) throws IOException {
        writer.write(convertToCsvFormat(record));
        writer.newLine();
    }

    public record CsvFiles(List<File> nodeFiles, List<File> edgeFiles) {
        public CsvFiles {
            nodeFiles = List.copyOf(nodeFiles);
            edgeFiles = List.copyOf(edgeFiles);
        }
    }

    private record HeaderPlan(
            List<CsvHeaderDefinition> definitions,
            Map<PropertySignature, Integer> assignments) {

        int definitionIndex(WithProperties value) {
            PropertySignature signature = PropertySignature.from(value);
            Integer index = assignments.get(signature);
            if (index == null) {
                throw new IllegalStateException("No CSV header assigned for property definitions "
                        + signature.propertyTypes());
            }
            return index;
        }
    }

    private record PropertySignature(Map<String, PropertyType<?>> propertyTypes) {

        private static final PropertySignature EMPTY = new PropertySignature(Map.of());

        private PropertySignature {
            propertyTypes = Collections.unmodifiableMap(new TreeMap<>(propertyTypes));
        }

        static PropertySignature from(WithProperties value) {
            Map<String, PropertyType<?>> propertyTypes = new TreeMap<>();
            value.getProperties().forEach((name, property) ->
                    propertyTypes.put(name, ((Property<?>) property).getType()));
            return new PropertySignature(propertyTypes);
        }
    }

    static final class CsvHeaderDefinition {

        private final List<CSVHeader> fixedHeaders;
        private final Map<String, PropertyType<?>> propertyTypes = new TreeMap<>();
        private final boolean parenthesesFix;

        CsvHeaderDefinition(List<CSVHeader> fixedHeaders, boolean parenthesesFix) {
            this.fixedHeaders = List.copyOf(fixedHeaders);
            this.parenthesesFix = parenthesesFix;
        }

        boolean supports(Map<String, PropertyType<?>> properties) {
            return properties.entrySet().stream().allMatch(entry -> {
                PropertyType<?> definedType = propertyTypes.get(entry.getKey());
                return definedType == null || definedType.equals(entry.getValue());
            });
        }

        boolean supports(PropertyDefinition definition) {
            PropertyType<?> definedType = propertyTypes.get(definition.name());
            return definedType == null || definedType.equals(definition.type());
        }

        void add(Map<String, PropertyType<?>> properties) {
            if (!supports(properties)) {
                throw new IllegalArgumentException("Conflicting property definitions: " + properties);
            }
            properties.forEach(propertyTypes::putIfAbsent);
        }

        int matchCount(Map<String, PropertyType<?>> properties) {
            return (int) properties.entrySet().stream()
                    .filter(entry -> entry.getValue().equals(propertyTypes.get(entry.getKey())))
                    .count();
        }

        List<PropertyDefinition> getSupportedPropertyDefinitions() {
            return propertyTypes.entrySet().stream()
                    .map(entry -> new PropertyDefinition(entry.getKey(), entry.getValue()))
                    .toList();
        }

        CSVHeader[] toArray() {
            List<CSVHeader> headers = new ArrayList<>(fixedHeaders.size() + propertyTypes.size());
            headers.addAll(fixedHeaders);
            propertyTypes.forEach((name, type) -> headers.add(new CSVHeader(name, type.getCsvValue(), parenthesesFix)));
            return headers.toArray(CSVHeader[]::new);
        }
    }

    private static final class WriterGroup implements AutoCloseable {

        private final List<BufferedWriter> writers;

        private WriterGroup(List<BufferedWriter> writers) {
            this.writers = writers;
        }

        static WriterGroup open(List<File> files) throws IOException {
            List<BufferedWriter> writers = new ArrayList<>(files.size());
            try {
                for (File file : files) {
                    writers.add(new BufferedWriter(new FileWriter(file, StandardCharsets.UTF_8)));
                }
                return new WriterGroup(writers);
            } catch (IOException e) {
                closeAll(writers, e);
                throw e;
            }
        }

        BufferedWriter get(int index) {
            return writers.get(index);
        }

        @Override
        public void close() throws IOException {
            IOException failure = null;
            for (BufferedWriter writer : writers) {
                try {
                    writer.close();
                } catch (IOException e) {
                    if (failure == null) {
                        failure = e;
                    } else {
                        failure.addSuppressed(e);
                    }
                }
            }
            if (failure != null) {
                throw failure;
            }
        }

        private static void closeAll(List<BufferedWriter> writers, IOException failure) {
            for (BufferedWriter writer : writers) {
                try {
                    writer.close();
                } catch (IOException closeFailure) {
                    failure.addSuppressed(closeFailure);
                }
            }
        }
    }
}
