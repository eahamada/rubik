package rubik.neo4j.domain;

import static org.neo4j.ogm.annotation.Relationship.OUTGOING;
import org.neo4j.ogm.annotation.GraphId;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Property;
import org.neo4j.ogm.annotation.Relationship;

@NodeEntity
public class RubikConfiguration {
  @GraphId
  private Long id;
  @Property
  private String configuration;
  
   public RubikConfiguration() {
  }
  
  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((configuration == null) ? 0 : configuration.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    RubikConfiguration other = (RubikConfiguration) obj;
    if (configuration == null) {
      if (other.configuration != null)
        return false;
    } else if (!configuration.equals(other.configuration))
      return false;
    return true;
  }

  public String getConfiguration()  {
    return configuration;
  }
  
  public void setConfiguration(String configuration) {
	this.configuration = configuration;
}

@Relationship(type="LCW", direction=OUTGOING)
  public RubikConfiguration lcw = null;
  @Relationship(type = "LCCW", direction = OUTGOING)
  public RubikConfiguration lccw = null;
  @Relationship(type = "rcw", direction = OUTGOING)
  public RubikConfiguration rcw= null;
  @Relationship(type = "RCCW", direction = OUTGOING)
  public RubikConfiguration rccw= null;
  @Relationship(type = "DCW", direction = OUTGOING)
  public RubikConfiguration dcw= null;
  @Relationship(type = "DCCW", direction = OUTGOING)
  public RubikConfiguration dccw= null;
  @Relationship(type = "UCW", direction = OUTGOING)
  public RubikConfiguration ucw= null;
  @Relationship(type = "UCCW", direction = OUTGOING)
  public RubikConfiguration uccw= null;
  @Relationship(type = "BCW", direction = OUTGOING)
  public RubikConfiguration bcw= null;
  @Relationship(type = "BCCW", direction = OUTGOING)
  public RubikConfiguration bccw= null;
  @Relationship(type = "FCW", direction = OUTGOING)
  public RubikConfiguration fcw= null;
  @Relationship(type = "FCCW", direction = OUTGOING)
  public RubikConfiguration fccw= null;
  
  private RubikConfiguration(Builder builder) {
      this.id = builder.id;
      this.configuration = builder.configuration;
  }

  public static class Builder {

    private Long id;
    private String configuration;

    public Builder withId(Long id) {
      this.id = id;
      return this;
    }

    public Builder withConfiguration(String configuration) {
      this.configuration = configuration;
      return this;
    }

    public RubikConfiguration build() {
      return new RubikConfiguration(this);
    }
  }

}
