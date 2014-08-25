package edu.sdsc.scigraph.owlapi.cases;

import static com.google.common.collect.Iterables.getOnlyElement;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import org.junit.Test;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;

import edu.sdsc.scigraph.neo4j.GraphUtil;
import edu.sdsc.scigraph.neo4j.OwlLabels;

public class TestSubClassOf extends OwlTestCase {

  @Test
  public void testSubclass() {
    Node subclass = getNode("http://example.org/subclass");
    Node superclass = getNode("http://example.org/superclass");
    Relationship relationship = getOnlyElement(GraphUtil.getRelationships(subclass, superclass, OwlLabels.RDF_SUBCLASS_OF));
    assertThat("subclassOf relationship should start with the subclass.",
        relationship.getStartNode(), is(subclass));
    assertThat("subclassOf relationship should end with the subclass.",
        relationship.getEndNode(), is(superclass));
  }

}