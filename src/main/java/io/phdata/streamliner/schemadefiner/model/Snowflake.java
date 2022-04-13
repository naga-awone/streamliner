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

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@EqualsAndHashCode(callSuper = false)
@ToString
@Getter
@Setter
public class Snowflake extends Destination {
  public String type;
  public String snowSqlCommand;
  public String storagePath;
  public String storageIntegration;
  public String snsTopic;
  public String warehouse;
  public String taskSchedule = "5 minutes";
  public SnowflakeQAOptions quality;
  public SnowflakeDatabase stagingDatabase;
  public SnowflakeDatabase reportingDatabase;
  public String stageName;
  public IngestFileFormat fileFormat;
  public TableNameStrategy tableNameStrategy;
  public String errorIntegration;

  public Snowflake() {}
}
