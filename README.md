# AAroN

AAroN is a plug-in for Neo4j® to analyze common UML®/ArchiMate® based architectures on the graph database Neo4j. The name can be interpreted as acronym and means "**A**nalyze **Ar**chitectures **o**n **N**eo4j". Currently this Plug-In supports the ArchiMate® Open Exchange XML Format and the EAP-/EAPX-File Format from the modelling tool Sparx Enterprise Architect©. AAroN was initially developed for analyzing Enterprise Architectures based on ArchiMate® and Sparx Enterprise Architect in combination with the NATO Architecture Framework (NAF). But the tool can be used to analyze any Architecture based on Sparx or ArchiMate.

Maybe some features are not supported yet. Especially around diagrams in ArchiMate models or if it is not that commonly used in classical enterprise architecture modelling. If you find anything missing please create an issue to request the feature.

## Installation
Simply place the Jar-File in the Neo4j plugins folder

## Usage
There are two procedures to import architectures. One for ArchiMate®, one for Sparx EAP/EAPX-Files

### ArchiMate
To import an ArchiMate® Open Exchange Format file use the following procedure:

```
CALL aaron.import.archiexchange("sample1.xml");
```

### Sparx EAP/EAPX-File
To import a Sparx EAP/EAPX-File use the following procedure:

```
CALL aaron.import.sparxeap("sample.eap");
```

Because of the very huge amount of TaggedValues in NAF Architectures TaggedValues a created as properties on nodes and relationships. You can change this behaviour with an optional config: 

This is the default behaviour:
<br>
```
CALL aaron.import.sparxeap("sample.eap", {taggedValues: 'AS_PROPERTIES'});
```

This one handles TaggedValues related to nodes as own nodes connected to the tagged element:
<br>
```
CALL aaron.import.sparxeap("sample.eap", {taggedValues: 'AS_NODES'});
```
<br>
Regarding the fact that Sparx Connectors will become relationships in our graph, it is not possible to create own nodes for TaggedValues related to connectors. They will always be created as properties on the relationship. 

This one does not create TaggedValues at all:
<br>
```
CALL aaron.import.sparxeap("sample.eap", {taggedValues: 'NONE'});
```

## Copyright and Trademark hints

**Neo4j®** and **Cypher®** are registered Trademarks of Neo4j, Inc. <br>
**ArchiMate®** and **The Open Group®** are registered trademark of The Open Group. <br>
**UML®** and **Unified Modeling Language®** are registered trademarks and **BPMN™**, and **Business Process Modeling Notation™** are trademarks of the Object Management Group. <br>
**Enterprise Architect** - Copyright © 1998 - 2021 Sparx Systems Pty. Ltd. All rights reserved.
