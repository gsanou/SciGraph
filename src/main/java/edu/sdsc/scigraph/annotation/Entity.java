/**
 * Copyright (C) 2014 Christopher Condit (condit@sdsc.edu)
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
package edu.sdsc.scigraph.annotation;

import static com.google.common.collect.Iterables.getFirst;
import static java.lang.String.format;

import java.util.Collections;
import java.util.Objects;
import java.util.Set;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlValue;

import com.google.common.base.Joiner;
import com.google.common.collect.ImmutableSet;

import edu.sdsc.scigraph.frames.Concept;

// TODO: Can this be replaced by Concept?
@XmlRootElement
public class Entity {

  @XmlValue
  private final String term;

  @XmlAttribute
  private final String id;

  @XmlAttribute
  private Set<String> categories;

  public Entity(String term, String id) {
    this(term, id, Collections.<String> emptySet());
  }

  public Entity(Concept concept) {
    this(concept.getLabel(), concept.getUri(), concept.getCategories());
  }

  public Entity(String term, String id, Iterable<String> categories) {
    this.term = term;
    this.id = id;
    this.categories = ImmutableSet.copyOf(categories);
  }

  public String getTerm() {
    return term;
  }

  public String getId() {
    return id;
  }

  public Set<String> getCategories() {
    return categories;
  }

  protected static String escape(String value) {
    return (null == value) ? "" : value.replace(",", "\\,").replace("|", "\\|");
  }

  /***
   * @return a serialized version of the entity
   */
  public String serialize() {
    return Joiner.on(",")
        .join(escape(getTerm()), escape(getId()), escape(getFirst(categories, "")));
  }

  @Override
  public String toString() {
    return format("%s (%s)", term, id);
  }

  @Override
  public int hashCode() {
    return Objects.hash(term, id);
  }

  @Override
  public boolean equals(Object obj) {
    if (null == obj) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    final Entity other = (Entity) obj;
    return Objects.equals(term, other.term) && Objects.equals(id, other.id);
  }

}
