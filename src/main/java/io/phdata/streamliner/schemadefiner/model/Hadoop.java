package io.phdata.streamliner.schemadefiner.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@EqualsAndHashCode(callSuper = false)
@ToString
@Getter
@Setter
public class Hadoop extends Destination {
  private String type;
  private String impalaShellCommand;
  private HadoopDatabase stagingDatabase;
  private HadoopDatabase reportingDatabase;

  public Hadoop() {}
}
