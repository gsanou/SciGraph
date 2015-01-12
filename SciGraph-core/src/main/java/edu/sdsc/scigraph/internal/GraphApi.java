/**
 * Copyright (C) 2014 The SciGraph authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package edu.sdsc.scigraph.internal;

import static com.google.common.collect.Iterables.isEmpty;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import javax.inject.Inject;

import org.neo4j.graphdb.Direction;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Path;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.RelationshipType;
import org.neo4j.graphdb.traversal.Evaluation;
import org.neo4j.graphdb.traversal.Evaluator;
import org.neo4j.graphdb.traversal.Evaluators;
import org.neo4j.graphdb.traversal.TraversalDescription;
import org.neo4j.tooling.GlobalGraphOperations;

import com.google.common.base.Optional;
import com.google.common.base.Predicate;
import com.tinkerpop.blueprints.impls.tg.TinkerGraph;

import edu.sdsc.scigraph.neo4j.DirectedRelationshipType;
import edu.sdsc.scigraph.owlapi.OwlLabels;
import edu.sdsc.scigraph.owlapi.OwlRelationships;

public class GraphApi {

  private final GraphDatabaseService graphDb;

  @Inject
  public GraphApi(GraphDatabaseService graphDb) {
    this.graphDb = graphDb;
  }

  /***
   * TODO: Add a boolean for equivalent classes
   * 
   * @param parent
   * @param type
   * @param direction
   * @return
   */
  public Collection<Node> getEntailment(Node parent, RelationshipType type, Direction direction) {
    Set<Node> entailment = new HashSet<>();
    for (Path path : graphDb.traversalDescription().depthFirst()
        .relationships(type, direction)
        .evaluator(Evaluators.fromDepth(0)).evaluator(Evaluators.all()).traverse(parent)) {
      entailment.add(path.endNode());
    }
    return entailment;
  }

  
  public boolean classIsInCategory(Node candidate, Node parentConcept) {
    return classIsInCategory(candidate, parentConcept, OwlRelationships.RDF_SUBCLASS_OF);
  }

  public boolean classIsInCategory(Node candidate, Node parent, RelationshipType... relationships) {
    TraversalDescription description = graphDb.traversalDescription().depthFirst()
        .evaluator(new Evaluator() {
          @Override
          public Evaluation evaluate(Path path) {
            if (path.endNode().hasLabel(OwlLabels.OWL_CLASS)) {
              return Evaluation.INCLUDE_AND_CONTINUE;
            } else {
              return Evaluation.EXCLUDE_AND_PRUNE;
            }
          }
        });
    for (RelationshipType type : relationships) {
      description.relationships(type, Direction.OUTGOING);
    }

    for (Path position : description.traverse(candidate)) {
      if (position.endNode().equals(parent)) {
        return true;
      }
    }
    return false;
  }

  /**
   * Get all the self loops in the Neo4j graph.
   * 
   * @return A set of self loop edges. An empty set will be returned if no self loops are found in
   *         in the graph.
   */
  public Set<Relationship> getSelfLoops() {
    Set<Relationship> result = new HashSet<Relationship>();

    for (Relationship n : GlobalGraphOperations.at(graphDb).getAllRelationships()) {
      if (n.getStartNode().equals(n.getEndNode())) {
        result.add(n);
      }
    }
    return result;
  }

  public TinkerGraph getNeighbors(Set<Node> nodes, int depth, Set<DirectedRelationshipType> types, final Optional<Predicate<Node>> includeNode) {
    TraversalDescription description = graphDb.traversalDescription().depthFirst().evaluator(Evaluators.toDepth(depth));
    for (DirectedRelationshipType type: types) {
      description = description.relationships(type.getType(), type.getDirection());
    }
    if (includeNode.isPresent()) {
      description = description.evaluator(new Evaluator() {
        @Override
        public Evaluation evaluate(Path path) {
          if (includeNode.get().apply(path.endNode())) {
            return Evaluation.INCLUDE_AND_CONTINUE;
          } else {
            return Evaluation.EXCLUDE_AND_PRUNE;
          }
        }
      });
    }
    TinkerGraph graph = new TinkerGraph();
    for (Path path: description.traverse(nodes)) {
      Relationship relationship = path.lastRelationship();
      if (null != relationship) {
        TinkerGraphUtil.addEdge(graph, relationship);
      }
    }
    if (isEmpty(graph.getEdges())) { 
      // If nothing was added to the graph add the root nodes
      for (Node node: nodes) {
        TinkerGraphUtil.addNode(graph, node);
      }
    }
    return graph;
  }

}
