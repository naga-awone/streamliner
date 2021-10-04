// Licensed to the Apache Software Foundation (ASF) under one
// or more contributor license agreements.  See the NOTICE file
// distributed with this work for additional information
// regarding copyright ownership.  The ASF licenses this file
// to you under the Apache License, Version 2.0 (the
// "License"); you may not use this file except in compliance
// with the License.  You may obtain a copy of the License at
//
//   http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing,
// software distributed under the License is distributed on an
// "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
// KIND, either express or implied.  See the License for the
// specific language governing permissions and limitations
// under the License.
package io.phdata.streamliner.schemadefiner.model;

import io.phdata.streamliner.util.JavaHelper;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.apache.commons.lang3.StringUtils;

@EqualsAndHashCode(callSuper = false)
@ToString
@Getter
@Setter
public class HadoopTable extends TableDefinition {
  private String type;
  public String sourceName;
  public String destinationName;
  public String checkColumn;
  public String comment = "";
  public List<String> primaryKeys = new ArrayList<>();
  public Map<String, String> metadata;
  public Integer numberOfMappers = 1;
  public String splitByColumn;
  public Integer numberOfPartitions = 2;
  public List<ColumnDefinition> columns = new ArrayList<>();
  public String pkList;
  public String pkAsStringCommaSeparated;
  public List<ColumnDefinition> pkColumnDefs;
  public List<ColumnDefinition> nonPKColumnDefs;

  public HadoopTable() {}

  public HadoopTable(
      String sourceName,
      String destinationName,
      String checkColumn,
      String comment,
      List<String> primaryKeys,
      Map<String, String> metadata,
      Integer numberOfMappers,
      String splitByColumn,
      Integer numberOfPartitions,
      List<ColumnDefinition> columns) {
    this.sourceName = sourceName;
    this.destinationName = destinationName;
    this.checkColumn = checkColumn;
    this.comment = comment;
    this.primaryKeys = primaryKeys;
    this.metadata = metadata;
    this.numberOfMappers = numberOfMappers;
    this.splitByColumn = splitByColumn;
    this.numberOfPartitions = numberOfPartitions;
    this.columns = columns;
    pkList =
        StringUtils.join(
            primaryKeys.stream().map(pk -> String.format("`%s`", pk)).collect(Collectors.toList()),
            ",");
    pkAsStringCommaSeparated = StringUtils.join(primaryKeys, ",");
    pkColumnDefs =
        columns.stream()
            .filter(c -> primaryKeys.contains(c.getSourceName()))
            .collect(Collectors.toList());
    nonPKColumnDefs =
        columns.stream()
            .filter(c -> !primaryKeys.contains(c.getSourceName()))
            .collect(Collectors.toList());
  }

  // these setters are needed to set value to parent class. jackson is not setting the parent class
  // value
  public void setDestinationName(String destinationName) {
    super.destinationName = destinationName;
    this.destinationName = destinationName;
  }

  public void setSourceName(String sourceName) {
    super.setSourceName(sourceName);
    this.sourceName = sourceName;
  }

  public void setPrimaryKeys(List<String> primaryKeys) {
    super.setPrimaryKeys(primaryKeys);
    this.primaryKeys = primaryKeys;
  }

  public void setColumns(List<ColumnDefinition> columns) {
    super.setColumns(columns);
    this.columns = columns;
  }

  public List<ColumnDefinition> orderColumnsPKsFirst() {
    pkColumnDefs.addAll(nonPKColumnDefs);
    return pkColumnDefs;
  }

  public String columnDDL(
      scala.collection.immutable.Map<String, scala.collection.immutable.Map<String, String>>
          typeMapping,
      String targetMapping) {
    Map<String, Map<String, String>> javaTypeMap = JavaHelper.convertScalaMapToJavaMap(typeMapping);
    List<String> columnList =
        columns.stream()
            .map(
                column ->
                    String.format(
                        "`%s` %s COMMENT '%s'",
                        column.getDestinationName(),
                        column.mapDataTypeHadoop(javaTypeMap, targetMapping),
                        column.comment))
            .collect(Collectors.toList());
    return StringUtils.join(columnList, ",\n");
  }

  public String sourceColumns(String driverClass) {
    List<String> columnList =
        columns.stream()
            .map(
                column -> {
                  if (driverClass.toLowerCase().contains("oracle")
                      || driverClass.toLowerCase().contains("sqlserver")) {
                    return String.format(
                        "%s AS \"%s\"", column.getSourceName(), column.getDestinationName());
                  } else {
                    return String.format(
                        "`%s` AS %s", column.getSourceName(), column.getDestinationName());
                  }
                })
            .collect(Collectors.toList());
    return StringUtils.join(columnList, ",\n");
  }

  public String tableMetadata() {
    List<String> metadataList = new ArrayList<>();
    if (metadata != null) {
      metadata.forEach((k, v) -> metadataList.add(String.format("'%s' = '%s'", k, v)));
    }
    return StringUtils.join(metadataList, ",\n");
  }

  public String sqoopMapJavaColumns(
      scala.collection.immutable.Map<String, scala.collection.immutable.Map<String, String>>
          typeMapping) {
    Map<String, Map<String, String>> javaTypeMap = JavaHelper.convertScalaMapToJavaMap(typeMapping);
    List<String> map =
        columns.stream().map(c -> c.mapDataTypeJava(javaTypeMap)).collect(Collectors.toList());
    if (map.isEmpty()) {
      return "";
    } else {
      return StringUtils.join(map, ",\n");
    }
  }

  public List<String> columnList(
      scala.collection.immutable.Map<String, scala.collection.immutable.Map<String, String>>
          typeMapping) {
    Map<String, Map<String, String>> javaTypeMap = JavaHelper.convertScalaMapToJavaMap(typeMapping);
    return columns.stream()
        .map(column -> column.castColumn(javaTypeMap, "AVRO", "KUDU"))
        .collect(Collectors.toList());
  }
}
