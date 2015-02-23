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
package edu.sdsc.scigraph.owlapi;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.common.base.MoreObjects;
import com.google.common.base.Optional;

import edu.sdsc.scigraph.neo4j.OntologyConfiguration;

public class OwlLoadConfiguration {

  private OntologyConfiguration ontologyConfiguration;
  private List<OntologySetup> ontologies = new ArrayList<>();
  private Map<String, String> categories = new HashMap<>();
  private List<MappedProperty> mappedProperties = new ArrayList<>();
  private Set<String> indexedNodeProperties = new HashSet<>();
  private Set<String> exactNodeProperties = new HashSet<>();
  private Map<String, String> neo4jConfig = new HashMap<>();

  public OntologyConfiguration getOntologyConfiguration() {
    return ontologyConfiguration;
  }

  public void setOntologyConfiguration(OntologyConfiguration ontologyConfiguration) {
    this.ontologyConfiguration = ontologyConfiguration;
  }

  public List<OntologySetup> getOntologies() {
    return ontologies;
  }

  public Map<String, String> getCategories() {
    return categories;
  }

  public List<MappedProperty> getMappedProperties() {
    return mappedProperties;
  }

  public Set<String> getIndexedNodeProperties() {
    return indexedNodeProperties;
  }

  public Set<String> getExactNodeProperties() {
    return exactNodeProperties;
  }

  public Map<String, String> getNeo4jConfig() {
    return neo4jConfig;
  }

  public static class OntologySetup {

    String url;

    Optional<ReasonerConfiguration> reasonerConfiguration = Optional.absent();

    public String url() {
      return url;
    }

    public void setUrl(String url) {
      this.url = url;
    }

    public Optional<ReasonerConfiguration> getReasonerConfiguration() {
      return reasonerConfiguration;
    }

    public void setReasonerConfiguration(ReasonerConfiguration reasonerConfiguration) {
      this.reasonerConfiguration = Optional.of(reasonerConfiguration);
    }

    @Override
    public String toString() {
      return url;
    }

  }

  public static class ReasonerConfiguration {

    String factory;
    boolean addDirectInferredEdges = false;
    boolean removeUnsatisfiableClasses = false;
    boolean addInferredEquivalences = false;

    public String getFactory() {
      return factory;
    }

    public void setFactory(String factory) {
      this.factory = factory;
    }

    public boolean isAddDirectInferredEdges() {
      return addDirectInferredEdges;
    }

    public void setAddDirectInferredEdges(boolean addDirectInferredEdges) {
      this.addDirectInferredEdges = addDirectInferredEdges;
    }

    public boolean isRemoveUnsatisfiableClasses() {
      return removeUnsatisfiableClasses;
    }

    public void setRemoveUnsatisfiableClasses(boolean removeUnsatisfiableClasses) {
      this.removeUnsatisfiableClasses = removeUnsatisfiableClasses;
    }

    public boolean isAddInferredEquivalences() {
      return addInferredEquivalences;
    }

    public void setAddInferredEquivalences(boolean addInferredEquivalences) {
      this.addInferredEquivalences = addInferredEquivalences;
    }

  }

  public static class MappedProperty {
    String name;
    List<String> properties;

    public String getName() {
      return name;
    }

    public List<String> getProperties() {
      return properties;
    }

    @Override
    public String toString() {
      return MoreObjects.toStringHelper(this.getClass()).add("name", name)
          .add("properties", properties).toString();
    }

  }

}
