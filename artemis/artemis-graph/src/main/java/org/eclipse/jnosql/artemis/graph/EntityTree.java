/*
 *  Copyright (c) 2019 Otávio Santana and others
 *   All rights reserved. This program and the accompanying materials
 *   are made available under the terms of the Eclipse Public License v1.0
 *   and Apache License v2.0 which accompanies this distribution.
 *   The Eclipse Public License is available at http://www.eclipse.org/legal/epl-v10.html
 *   and the Apache License v2.0 is available at http://www.opensource.org/licenses/apache2.0.php.
 *
 *   You may elect to redistribute this code under either of these licenses.
 *
 *   Contributors:
 *
 *   Otavio Santana
 */
package org.eclipse.jnosql.artemis.graph;

import java.util.Map.Entry;
import java.util.Optional;
import java.util.stream.Stream;

public interface EntityTree {

    <T> Stream<T> getLeaf();

    <T> Stream<T> getParents();

    <K,V> Stream<Entry<K,V>> getParentsIds();

    <T> Optional<EntityTree> getParentId(T id);

    Stream<EntityTree> getLeafTrees();

    Stream<EntityTree> getTreesAtDepth(int depth);

    <T> Stream<T> getLeafsAtDepth(int depth);

    boolean isLeaf();
}
