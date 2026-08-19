package aaron.export;

import aaron.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class AAroNCsvWriter {

    private static final Logger LOG = LoggerFactory.getLogger(AAroNCsvWriter.class);

    private static final String COMMA = ",";
    private static final String DELIMITER = COMMA;
    private static final String ARRAY_DELIMITER = ";";
    private static final String DOUBLE_QUOTES = "\"";
    private static final String EMBEDDED_DOUBLE_QUOTES = "\"\"";
    private static final String NEW_LINE_UNIX = "\n";
    private static final String NEW_LINE_WINDOWS = "\r\n";
    private static final String EMPTY_STRING = "";

    private int nodesCounter = 0;
    private int edgesCounter = 0;
    private int propertiesCounter = 0;

    private int nodeIdCounter = 0;

    private Map<UniqueNodeIdentifier, Integer> uniqueIdToNodeId = new HashMap<>();

    public AAroNCsvWriter() {
    }

    public int getNodesCount() {
        return nodesCounter;
    }

    public int getEdgesCount() {
        return edgesCounter;
    }

    public int getPropertiesCount() {
        return propertiesCounter;
    }

    public String convertToCsvFormat(final String[] line) {
        return convertToCsvFormat(line, DELIMITER);
    }

    public String convertToCsvFormat(final String[] line, final String separator) {
        return convertToCsvFormat(line, separator, true);
    }

    // if quote = true, all fields are enclosed in double quotes
    public String convertToCsvFormat(
            final String[] line,
            final String separator,
            final boolean quote) {

        return Stream.of(line)                              // convert String[] to stream
                .map(l -> formatCsvField(l, quote))         // format CSV field
                .collect(Collectors.joining(separator));    // join with a separator

    }

    // put your extra login here
    private String formatCsvField(final String field, final boolean quote) {
        String result = field;
        if (result == null) {
            return EMPTY_STRING;
        }
        if (result.contains(COMMA)
                || result.contains(DOUBLE_QUOTES)
                || result.contains(NEW_LINE_UNIX)
                || result.contains(NEW_LINE_WINDOWS)) {
            // if field contains double quotes, replace it with two double quotes \"\"
            result = result.replace(DOUBLE_QUOTES, EMBEDDED_DOUBLE_QUOTES);

            // must wrap by or enclosed with double quotes
            result = DOUBLE_QUOTES + result + DOUBLE_QUOTES;
        } else {
            // should all fields enclosed in double quotes
            if (quote) {
                result = DOUBLE_QUOTES + result + DOUBLE_QUOTES;
            }
        }
        return result;
    }

    // a standard FileWriter, CSV is a normal text file
    private void writeToCsvFile(List<String[]> list, File file) throws IOException {
        List<String> collect = list.stream()
                .map(this::convertToCsvFormat)
                .toList();
        // CSV is a normal text file, need a writer
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(file, StandardCharsets.UTF_8))) {
            for (String line : collect) {
                bw.write(line);
                bw.newLine();
            }
            bw.flush();
        }
    }

    public void write(final Model model, final File nodesFile, final File edgesFile, final boolean parenthesesFix) throws IOException {
        Set<CSVHeader> nodeHeaders = new HashSet<>();
        CSVHeader[] nodeCSVHeader;
        Set<CSVHeader> edgeHeaders = new HashSet<>();
        CSVHeader[] edgeCSVHeader;
        if (nodesFile != null) {
            if (nodesFile.exists() && !nodesFile.canWrite()) {
                LOG.error("can not write to nodes file. Check file permission");
            } else {
                List<String[]> nodesData = new ArrayList<>();
                for (AAroNNode n : model.iterateNodes()) {
                    for (Map.Entry<String, Property> entry : n.getProperties().entrySet()) {
                        nodeHeaders.add(new CSVHeader(entry.getKey(), entry.getValue(), parenthesesFix));
                    }
                }
                nodeCSVHeader = createNodeCSVHeader(nodeHeaders, model, parenthesesFix);
                nodesData.add(Arrays.stream(nodeCSVHeader).map(CSVHeader::toString).toArray(String[]::new));
                model.iterateNodeEntries().forEach(entry -> {

                });
                model.iterateNodeEntries().forEach(entry -> {
                    String[] nodeRecord = createNodeRecord(model, nodeCSVHeader, entry.getKey(),  entry.getValue());
                    nodesData.add(nodeRecord);
                });
                writeToCsvFile(nodesData, nodesFile);
            }
        }
        if (edgesFile != null) {
            if (edgesFile.exists() && !edgesFile.canWrite()) {
                LOG.error("can not write to edges file. Check file permission");
            } else {
                List<String[]> edgesData = new ArrayList<>();
                for (AAroNEdge e : model.iterateEdges()) {
                    for (Map.Entry<String, Property> entry : e.getProperties().entrySet()) {
                        propertiesCounter++;
                        edgeHeaders.add(new CSVHeader(entry.getKey(), entry.getValue(), parenthesesFix));
                    }
                }
                edgeCSVHeader = createEdgeCSVHeader(edgeHeaders, model,parenthesesFix);
                edgesData.add(Arrays.stream(edgeCSVHeader).map(CSVHeader::toString).toArray(String[]::new));
                model.iterateEdges().forEach(edge -> {
                    String[] edgeRecord = createEdgeRecord(model, edgeCSVHeader, edge);
                    if (edgeRecord != null)
                        edgesData.add(edgeRecord);
                });
                writeToCsvFile(edgesData, edgesFile);
            }
        }
    }

    private CSVHeader[] createNodeCSVHeader(final Set<CSVHeader> headerSet, Model model, final boolean parenthesesFix) {
        String idSpace = null;
        ImportConext context = model.getContext();
        if (context != null) {
            idSpace = context.getFileHash();
        }
        List<CSVHeader> header = new ArrayList<>();
        if (idSpace != null) {
            header.add(new CSVHeader(null, "ID(" + idSpace + ")", parenthesesFix));
        } else {
            header.add(new CSVHeader(null, "ID", parenthesesFix));
        }
        header.add(new CSVHeader(null, "LABEL", parenthesesFix));
        addPropertiesForHeader(header, headerSet);
        return header.toArray(new CSVHeader[header.size()]);
    }

    private CSVHeader[] createEdgeCSVHeader(final Set<CSVHeader> headerSet, Model model, final boolean parenthesesFix) {
        String idSpace = null;
        ImportConext context = model.getContext();
        if (context != null) {
            idSpace = context.getFileHash();
        }
        List<CSVHeader> header = new ArrayList<>();
        if (idSpace != null) {
            header.add(new CSVHeader(null, "START_ID(" + idSpace + ")", parenthesesFix));
        } else {
            header.add(new CSVHeader(null, "START_ID", parenthesesFix));
        }
        header.add(new CSVHeader(null, "TYPE", parenthesesFix));
        if (idSpace != null) {
            header.add(new CSVHeader(null, "END_ID(" + idSpace + ")", parenthesesFix));
        } else {
            header.add(new CSVHeader(null, "END_ID", parenthesesFix));
        }
        addPropertiesForHeader(header, headerSet);
        return header.toArray(new CSVHeader[header.size()]);
    }

    private String[] createNodeRecord(final Model model, final CSVHeader[] headers, UniqueNodeIdentifier identifier, AAroNNode node) {
        List<String> csvRecord = new ArrayList<>();
        this.nodeIdCounter++;
        node.setId(this.nodeIdCounter);
        uniqueIdToNodeId.put(identifier, node.getId());
        csvRecord.add(Integer.toString(node.getId()));
        csvRecord.add(String.join(";", node.getLabels()));
        this.nodesCounter++;
        addPropertiesToNodeRecord(csvRecord, headers, node);
        return csvRecord.toArray(new String[0]);
    }

    private String[] createEdgeRecord(final Model model, final CSVHeader[] headers, final AAroNEdge edge) {
        List<String> csvRecord = new ArrayList<>();

        AAroNNode startNode = model.getNode(edge.getStart());
        UniqueNodeIdentifier uniqueStartNodeId = model.getUniqueNodeIdentifier(edge.getStart());
        if (uniqueStartNodeId != null) {
            startNode.setId(uniqueIdToNodeId.get(uniqueStartNodeId));
        }

        AAroNNode endNode = model.getNode(edge.getEnd());
        UniqueNodeIdentifier uniqueEndNodeId = model.getUniqueNodeIdentifier(edge.getEnd());
        if (uniqueEndNodeId != null) {
            endNode.setId(uniqueIdToNodeId.get(uniqueEndNodeId));
        }

        if(startNode != null && startNode.getId() != null && endNode != null && endNode.getId() != null) {
            csvRecord.add(String.valueOf(startNode.getId()));
            csvRecord.add(edge.getType());
            csvRecord.add(String.valueOf(endNode.getId()));
            this.edgesCounter++;
            addPropertiesToEdgeRecord(csvRecord, headers, edge);
            return csvRecord.toArray(new String[0]);
        }
        return null;
    }

    private void addPropertiesToNodeRecord(final List<String> csvRecord, final CSVHeader[] headers, final WithProperties withProperties) {
        addPropertiesToRecord(csvRecord, headers, withProperties, 2);
    }

    private void addPropertiesToEdgeRecord(final List<String> csvRecord, final CSVHeader[] headers, final WithProperties withProperties) {
        addPropertiesToRecord(csvRecord, headers, withProperties, 3);
    }

    private void addPropertiesToRecord(final List<String> csvRecord, final CSVHeader[] headers, final WithProperties withProperties, int skip) {
        Arrays.stream(headers).skip(skip).forEach(h -> {
            Property property = withProperties.getProperties().get(h.rawName);
            if (property != null) {
                Object value = property.getValue();
                if (value == null) {
                    csvRecord.add(null);
                } else {
                    if (value.getClass().isArray()) {
                        Object[] objects = (Object[]) value;
                        String list = Arrays.stream(objects).filter(Objects::nonNull).map(Object::toString).collect(Collectors.joining(ARRAY_DELIMITER));
                        this.propertiesCounter++;
                        csvRecord.add(list);
                    } else if (value instanceof LocalDateTime) {
                        LocalDateTime dateTime = (LocalDateTime) value;
                        String format = dateTime.format(DateTimeFormatter.ISO_DATE_TIME);
                        this.propertiesCounter++;
                        csvRecord.add(format);
                    } else {
                        this.propertiesCounter++;
                        csvRecord.add(value.toString());
                    }
                }
            } else {
                csvRecord.add(null);
            }
        });
    }

    private void addPropertiesForHeader(final List<CSVHeader> header, final Set<CSVHeader> headerSet) {
        Map<String, List<CSVHeader>> groupHeaderMap = new HashMap<>();
        if (headerSet != null) {
            headerSet.forEach(h -> {
                groupHeaderMap.getOrDefault(h.getName(), new ArrayList<>()).add(h);
                groupHeaderMap.put(h.getName(), new ArrayList<>());
                List<CSVHeader> headerList = groupHeaderMap.getOrDefault(h.getName(), new ArrayList<>());
                headerList.add(h);
                groupHeaderMap.put(h.getName(), headerList);
            });
            groupHeaderMap.forEach((key, headerList) -> {
                String definedType;
                boolean typeConflict = false;
                if (headerList.size() > 1) {
                    CSVHeader firstHeader = headerList.get(0);
                    definedType = firstHeader.getType();
                    for (int i = 1; i < headerList.size(); i++) {
                        String nextType = headerList.get(i).getType();
                        if (!definedType.equals(nextType)) {
                            typeConflict = true;
                        }
                    }
                    if (typeConflict) {
                        headerList.forEach(h -> h.setConflictingTypeDefinition(true));
                        header.add(headerList.get(0));
                    }
                } else {
                    header.add(headerList.get(0));
                }
            });
        }
    }

    private static class CSVHeader {

        private final String rawName;
        private final String name;
        private final String type;
        private boolean conflictingTypeDefinition = false;

        CSVHeader(final String name, final String type, final boolean parenthesesFix) {
            this.rawName = name;
            this.type = type;
            this.name = createName(name,parenthesesFix);
        }

        CSVHeader(final String name, final Property<?> property, final boolean parenthesesFix) {
            this.rawName = name;
            this.type = property.getType().getCsvValue();
            this.name = createName(name, parenthesesFix);
        }

        private static String createName(final String value, final boolean parenthesesFix) {
            if (value == null) {
                return null;
            } else {
                String header = value;
                header = header
                        .trim()
                        .replaceAll("\\s+", " ");
                if (parenthesesFix) {
                    return header
                            .replace('(', '[')
                            .replace(')', ']')
                            .replace('{', '[')
                            .replace('}', ']');
                } else {
                    return header;
                }
            }
        }

        public String getName() {
            return name;
        }

        public String getType() {
            return type;
        }

        public boolean isConflictingTypeDefinition() {
            return conflictingTypeDefinition;
        }

        public void setConflictingTypeDefinition(boolean conflictingTypeDefinition) {
            this.conflictingTypeDefinition = conflictingTypeDefinition;
        }

        @Override
        public String toString(){
            if (conflictingTypeDefinition) {
                return (name != null ? name : "") + ":string";
            }
            return (name != null ? name : "") + ":" + type;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            CSVHeader csvHeader = (CSVHeader) o;
            return Objects.equals(name, csvHeader.name) && Objects.equals(type, csvHeader.type);
        }

        @Override
        public int hashCode() {
            return Objects.hash(name, type);
        }
    }
}
