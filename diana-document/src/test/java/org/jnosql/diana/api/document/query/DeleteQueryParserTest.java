/*
 *
 *  Copyright (c) 2017 Otávio Santana and others
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
 *
 */
package org.jnosql.diana.api.document.query;

import org.jnosql.diana.api.TypeReference;
import org.jnosql.diana.api.Value;
import org.jnosql.diana.api.document.Document;
import org.jnosql.diana.api.document.DocumentCollectionManager;
import org.jnosql.diana.api.document.DocumentCollectionManagerAsync;
import org.jnosql.diana.api.document.DocumentCondition;
import org.jnosql.diana.api.document.DocumentDeleteQuery;
import org.jnosql.diana.api.document.DocumentEntity;
import org.jnosql.diana.api.document.DocumentObserverParser;
import org.jnosql.diana.api.document.DocumentPreparedStatement;
import org.jnosql.diana.api.document.DocumentPreparedStatementAsync;
import org.jnosql.query.QueryException;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.jnosql.diana.api.Condition.AND;
import static org.jnosql.diana.api.Condition.BETWEEN;
import static org.jnosql.diana.api.Condition.EQUALS;
import static org.jnosql.diana.api.Condition.GREATER_EQUALS_THAN;
import static org.jnosql.diana.api.Condition.GREATER_THAN;
import static org.jnosql.diana.api.Condition.IN;
import static org.jnosql.diana.api.Condition.LESSER_EQUALS_THAN;
import static org.jnosql.diana.api.Condition.LESSER_THAN;
import static org.jnosql.diana.api.Condition.LIKE;
import static org.jnosql.diana.api.Condition.NOT;
import static org.jnosql.diana.api.Condition.OR;
import static org.jnosql.diana.api.document.DocumentCondition.eq;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class DeleteQueryParserTest {

    private DeleteQueryParser parser = new DeleteQueryParser();

    private DocumentCollectionManager documentCollection = Mockito.mock(DocumentCollectionManager.class);
    private DocumentCollectionManagerAsync documentCollectionAsync = Mockito.mock(DocumentCollectionManagerAsync.class);

    private final DocumentObserverParser observer = new DocumentObserverParser() {
    };


    @ParameterizedTest(name = "Should parser the query {0}")
    @ValueSource(strings = {"delete from God"})
    public void shouldReturnParserQuery(String query) {
        ArgumentCaptor<DocumentDeleteQuery> captor = ArgumentCaptor.forClass(DocumentDeleteQuery.class);
        parser.query(query, documentCollection, observer);
        Mockito.verify(documentCollection).delete(captor.capture());
        DocumentDeleteQuery documentQuery = captor.getValue();

        assertTrue(documentQuery.getDocuments().isEmpty());
        assertEquals("God", documentQuery.getDocumentCollection());
        assertFalse(documentQuery.getCondition().isPresent());
    }

    @ParameterizedTest(name = "Should parser the query {0}")
    @ValueSource(strings = {"delete name, address from God"})
    public void shouldReturnParserQuery1(String query) {
        ArgumentCaptor<DocumentDeleteQuery> captor = ArgumentCaptor.forClass(DocumentDeleteQuery.class);
        parser.query(query, documentCollection, observer);
        Mockito.verify(documentCollection).delete(captor.capture());
        DocumentDeleteQuery documentQuery = captor.getValue();

        assertThat(documentQuery.getDocuments(), contains("name", "address"));
        assertEquals("God", documentQuery.getDocumentCollection());
        assertFalse(documentQuery.getCondition().isPresent());
    }

    @ParameterizedTest(name = "Should parser the query {0}")
    @ValueSource(strings = {"delete from God where stamina > 10.23"})
    public void shouldReturnParserQuery11(String query) {
        ArgumentCaptor<DocumentDeleteQuery> captor = ArgumentCaptor.forClass(DocumentDeleteQuery.class);
        parser.query(query, documentCollection, observer);
        Mockito.verify(documentCollection).delete(captor.capture());
        DocumentDeleteQuery documentQuery = captor.getValue();

        checkBaseQuery(documentQuery);
        assertTrue(documentQuery.getCondition().isPresent());
        DocumentCondition condition = documentQuery.getCondition().get();

        assertEquals(GREATER_THAN, condition.getCondition());
        assertEquals(Document.of("stamina", 10.23), condition.getDocument());
    }

    @ParameterizedTest(name = "Should parser the query {0}")
    @ValueSource(strings = {"delete from God where stamina >= -10.23"})
    public void shouldReturnParserQuery12(String query) {
        ArgumentCaptor<DocumentDeleteQuery> captor = ArgumentCaptor.forClass(DocumentDeleteQuery.class);
        parser.query(query, documentCollection, observer);
        Mockito.verify(documentCollection).delete(captor.capture());
        DocumentDeleteQuery documentQuery = captor.getValue();

        checkBaseQuery(documentQuery);
        assertTrue(documentQuery.getCondition().isPresent());
        DocumentCondition condition = documentQuery.getCondition().get();

        assertEquals(GREATER_EQUALS_THAN, condition.getCondition());
        assertEquals(Document.of("stamina", -10.23), condition.getDocument());
    }

    @ParameterizedTest(name = "Should parser the query {0}")
    @ValueSource(strings = {"delete  from God where stamina <= -10.23"})
    public void shouldReturnParserQuery13(String query) {
        ArgumentCaptor<DocumentDeleteQuery> captor = ArgumentCaptor.forClass(DocumentDeleteQuery.class);
        parser.query(query, documentCollection, observer);
        Mockito.verify(documentCollection).delete(captor.capture());
        DocumentDeleteQuery documentQuery = captor.getValue();

        checkBaseQuery(documentQuery);
        assertTrue(documentQuery.getCondition().isPresent());
        DocumentCondition condition = documentQuery.getCondition().get();

        assertEquals(LESSER_EQUALS_THAN, condition.getCondition());
        assertEquals(Document.of("stamina", -10.23), condition.getDocument());
    }

    @ParameterizedTest(name = "Should parser the query {0}")
    @ValueSource(strings = {"delete  from God where stamina < -10.23"})
    public void shouldReturnParserQuery14(String query) {
        ArgumentCaptor<DocumentDeleteQuery> captor = ArgumentCaptor.forClass(DocumentDeleteQuery.class);
        parser.query(query, documentCollection, observer);
        Mockito.verify(documentCollection).delete(captor.capture());
        DocumentDeleteQuery documentQuery = captor.getValue();

        checkBaseQuery(documentQuery);
        assertTrue(documentQuery.getCondition().isPresent());
        DocumentCondition condition = documentQuery.getCondition().get();

        assertEquals(LESSER_THAN, condition.getCondition());
        assertEquals(Document.of("stamina", -10.23), condition.getDocument());
    }

    @ParameterizedTest(name = "Should parser the query {0}")
    @ValueSource(strings = {"delete  from God where age between 10 and 30"})
    public void shouldReturnParserQuery15(String query) {
        ArgumentCaptor<DocumentDeleteQuery> captor = ArgumentCaptor.forClass(DocumentDeleteQuery.class);
        parser.query(query, documentCollection, observer);
        Mockito.verify(documentCollection).delete(captor.capture());
        DocumentDeleteQuery documentQuery = captor.getValue();

        checkBaseQuery(documentQuery);
        assertTrue(documentQuery.getCondition().isPresent());
        DocumentCondition condition = documentQuery.getCondition().get();

        assertEquals(BETWEEN, condition.getCondition());
        assertEquals(Document.of("age", Arrays.asList(10L, 30L)), condition.getDocument());
    }

    @ParameterizedTest(name = "Should parser the query {0}")
    @ValueSource(strings = {"delete  from God where name = \"diana\""})
    public void shouldReturnParserQuery16(String query) {
        ArgumentCaptor<DocumentDeleteQuery> captor = ArgumentCaptor.forClass(DocumentDeleteQuery.class);
        parser.query(query, documentCollection, observer);
        Mockito.verify(documentCollection).delete(captor.capture());
        DocumentDeleteQuery documentQuery = captor.getValue();

        checkBaseQuery(documentQuery);
        assertTrue(documentQuery.getCondition().isPresent());
        DocumentCondition condition = documentQuery.getCondition().get();

        assertEquals(EQUALS, condition.getCondition());
        assertEquals(Document.of("name", "diana"), condition.getDocument());
    }

    @ParameterizedTest(name = "Should parser the query {0}")
    @ValueSource(strings = {"delete  from God where siblings = {\"apollo\": \"Brother\", \"Zeus\": \"Father\"}"})
    public void shouldReturnParserQuery18(String query) {
        ArgumentCaptor<DocumentDeleteQuery> captor = ArgumentCaptor.forClass(DocumentDeleteQuery.class);
        parser.query(query, documentCollection, observer);
        Mockito.verify(documentCollection).delete(captor.capture());
        DocumentDeleteQuery documentQuery = captor.getValue();

        checkBaseQuery(documentQuery);
        assertTrue(documentQuery.getCondition().isPresent());
        DocumentCondition condition = documentQuery.getCondition().get();

        assertEquals(EQUALS, condition.getCondition());
        Document document = condition.getDocument();
        List<Document> documents = document.get(new TypeReference<List<Document>>() {
        });
        assertThat(documents, containsInAnyOrder(Document.of("apollo", "Brother"),
                Document.of("Zeus", "Father")));
        assertEquals("siblings", document.getName());
    }

    @ParameterizedTest(name = "Should parser the query {0}")
    @ValueSource(strings = {"delete  from God where age = convert(12, java.lang.Integer)"})
    public void shouldReturnParserQuery19(String query) {
        ArgumentCaptor<DocumentDeleteQuery> captor = ArgumentCaptor.forClass(DocumentDeleteQuery.class);
        parser.query(query, documentCollection, observer);
        Mockito.verify(documentCollection).delete(captor.capture());
        DocumentDeleteQuery documentQuery = captor.getValue();

        checkBaseQuery(documentQuery);
        assertTrue(documentQuery.getCondition().isPresent());
        DocumentCondition condition = documentQuery.getCondition().get();
        Document document = condition.getDocument();
        assertEquals(EQUALS, condition.getCondition());
        assertEquals("age", document.getName());
        assertEquals(Value.of(12), document.getValue());


    }

    @ParameterizedTest(name = "Should parser the query {0}")
    @ValueSource(strings = {"delete  from God where name in (\"Ada\", \"Apollo\")"})
    public void shouldReturnParserQuery20(String query) {
        ArgumentCaptor<DocumentDeleteQuery> captor = ArgumentCaptor.forClass(DocumentDeleteQuery.class);
        parser.query(query, documentCollection, observer);
        Mockito.verify(documentCollection).delete(captor.capture());
        DocumentDeleteQuery documentQuery = captor.getValue();

        checkBaseQuery(documentQuery);
        assertTrue(documentQuery.getCondition().isPresent());
        DocumentCondition condition = documentQuery.getCondition().get();
        Document document = condition.getDocument();
        assertEquals(IN, condition.getCondition());
        assertEquals("name", document.getName());
        List<String> values = document.get(new TypeReference<List<String>>() {
        });
        assertThat(values, containsInAnyOrder("Ada", "Apollo"));
    }

    @ParameterizedTest(name = "Should parser the query {0}")
    @ValueSource(strings = {"delete from God where name like \"Ada\""})
    public void shouldReturnParserQuery21(String query) {
        ArgumentCaptor<DocumentDeleteQuery> captor = ArgumentCaptor.forClass(DocumentDeleteQuery.class);
        parser.query(query, documentCollection, observer);
        Mockito.verify(documentCollection).delete(captor.capture());
        DocumentDeleteQuery documentQuery = captor.getValue();

        checkBaseQuery(documentQuery);
        assertTrue(documentQuery.getCondition().isPresent());
        DocumentCondition condition = documentQuery.getCondition().get();
        Document document = condition.getDocument();
        assertEquals(LIKE, condition.getCondition());
        assertEquals("name", document.getName());
        assertEquals("Ada", document.get());
    }

    @ParameterizedTest(name = "Should parser the query {0}")
    @ValueSource(strings = {"delete from God where name not like \"Ada\""})
    public void shouldReturnParserQuery22(String query) {
        ArgumentCaptor<DocumentDeleteQuery> captor = ArgumentCaptor.forClass(DocumentDeleteQuery.class);
        parser.query(query, documentCollection, observer);
        Mockito.verify(documentCollection).delete(captor.capture());
        DocumentDeleteQuery documentQuery = captor.getValue();

        checkBaseQuery(documentQuery);
        assertTrue(documentQuery.getCondition().isPresent());
        DocumentCondition condition = documentQuery.getCondition().get();
        Document document = condition.getDocument();
        assertEquals(NOT, condition.getCondition());
        List<DocumentCondition> conditions = document.get(new TypeReference<List<DocumentCondition>>() {
        });
        DocumentCondition documentCondition = conditions.get(0);
        assertEquals(LIKE, documentCondition.getCondition());
        assertEquals(Document.of("name", "Ada"), documentCondition.getDocument());
    }

    @ParameterizedTest(name = "Should parser the query {0}")
    @ValueSource(strings = {"delete  from God where name = \"Ada\" and age = 20"})
    public void shouldReturnParserQuery23(String query) {
        ArgumentCaptor<DocumentDeleteQuery> captor = ArgumentCaptor.forClass(DocumentDeleteQuery.class);
        parser.query(query, documentCollection, observer);
        Mockito.verify(documentCollection).delete(captor.capture());
        DocumentDeleteQuery documentQuery = captor.getValue();

        checkBaseQuery(documentQuery);
        assertTrue(documentQuery.getCondition().isPresent());
        DocumentCondition condition = documentQuery.getCondition().get();
        Document document = condition.getDocument();
        assertEquals(AND, condition.getCondition());
        List<DocumentCondition> conditions = document.get(new TypeReference<List<DocumentCondition>>() {
        });
        assertThat(conditions, contains(eq(Document.of("name", "Ada")),
                eq(Document.of("age", 20L))));
    }

    @ParameterizedTest(name = "Should parser the query {0}")
    @ValueSource(strings = {"delete  from God where name = \"Ada\" or age = 20"})
    public void shouldReturnParserQuery24(String query) {
        ArgumentCaptor<DocumentDeleteQuery> captor = ArgumentCaptor.forClass(DocumentDeleteQuery.class);
        parser.query(query, documentCollection, observer);
        Mockito.verify(documentCollection).delete(captor.capture());
        DocumentDeleteQuery documentQuery = captor.getValue();

        checkBaseQuery(documentQuery);
        assertTrue(documentQuery.getCondition().isPresent());
        DocumentCondition condition = documentQuery.getCondition().get();
        Document document = condition.getDocument();
        assertEquals(OR, condition.getCondition());
        List<DocumentCondition> conditions = document.get(new TypeReference<List<DocumentCondition>>() {
        });
        assertThat(conditions, contains(eq(Document.of("name", "Ada")),
                eq(Document.of("age", 20L))));
    }

    @ParameterizedTest(name = "Should parser the query {0}")
    @ValueSource(strings = {"delete  from God where name = \"Ada\" and age = 20 or" +
            " siblings = {\"apollo\": \"Brother\", \"Zeus\": \"Father\"}"})
    public void shouldReturnParserQuery25(String query) {

        ArgumentCaptor<DocumentDeleteQuery> captor = ArgumentCaptor.forClass(DocumentDeleteQuery.class);
        parser.query(query, documentCollection, observer);
        Mockito.verify(documentCollection).delete(captor.capture());
        DocumentDeleteQuery documentQuery = captor.getValue();

        checkBaseQuery(documentQuery);
        assertTrue(documentQuery.getCondition().isPresent());
        DocumentCondition condition = documentQuery.getCondition().get();
        Document document = condition.getDocument();
        assertEquals(AND, condition.getCondition());
        List<DocumentCondition> conditions = document.get(new TypeReference<List<DocumentCondition>>() {
        });
        assertEquals(EQUALS, conditions.get(0).getCondition());
        assertEquals(EQUALS, conditions.get(1).getCondition());
        assertEquals(OR, conditions.get(2).getCondition());

    }

    @ParameterizedTest(name = "Should parser the query {0}")
    @ValueSource(strings = {"delete  from God where name = \"Ada\" and age = 20 or" +
            " siblings = {\"apollo\": \"Brother\", \"Zeus\": \"Father\"} and birthday =" +
            " convert(\"2007-12-03\", java.time.LocalDate)"})
    public void shouldReturnParserQuery26(String query) {
        ArgumentCaptor<DocumentDeleteQuery> captor = ArgumentCaptor.forClass(DocumentDeleteQuery.class);
        parser.query(query, documentCollection, observer);
        Mockito.verify(documentCollection).delete(captor.capture());
        DocumentDeleteQuery documentQuery = captor.getValue();

        checkBaseQuery(documentQuery);
        assertTrue(documentQuery.getCondition().isPresent());
        DocumentCondition condition = documentQuery.getCondition().get();
        Document document = condition.getDocument();
        assertEquals(AND, condition.getCondition());
        List<DocumentCondition> conditions = document.get(new TypeReference<List<DocumentCondition>>() {
        });
        assertEquals(EQUALS, conditions.get(0).getCondition());
        assertEquals(EQUALS, conditions.get(1).getCondition());
        assertEquals(OR, conditions.get(2).getCondition());
        assertEquals(EQUALS, conditions.get(3).getCondition());
    }

    @ParameterizedTest(name = "Should parser the query {0}")
    @ValueSource(strings = {"delete  from God where age = @age"})
    public void shouldReturnErrorWhenNeedPrepareStatement(String query) {

        assertThrows(QueryException.class, () -> {
            parser.query(query, documentCollection, observer);
        });


    }

    @ParameterizedTest(name = "Should parser the query {0}")
    @ValueSource(strings = {"delete from God where age = @age"})
    public void shouldReturnErrorWhenIsQueryWithParam(String query) {

        assertThrows(QueryException.class, () -> {
            parser.query(query, documentCollection, observer);
        });

    }

    @ParameterizedTest(name = "Should parser the query {0}")
    @ValueSource(strings = {"delete from God where age = @age"})
    public void shouldReturnErrorWhenDontBindParameters(String query) {

        DocumentPreparedStatement prepare = parser.prepare(query, documentCollection, observer);
        assertThrows(QueryException.class, () -> {
            prepare.getResultList();
        });
    }

    @ParameterizedTest(name = "Should parser the query {0}")
    @ValueSource(strings = {"delete from God where age = @age"})
    public void shouldExecutePrepareStatment(String query) {
        ArgumentCaptor<DocumentDeleteQuery> captor = ArgumentCaptor.forClass(DocumentDeleteQuery.class);

        DocumentPreparedStatement prepare = parser.prepare(query, documentCollection, observer);
        prepare.bind("age", 12);
        prepare.getResultList();
        Mockito.verify(documentCollection).delete(captor.capture());
        DocumentDeleteQuery documentQuery = captor.getValue();
        DocumentCondition documentCondition = documentQuery.getCondition().get();
        Document document = documentCondition.getDocument();
        assertEquals(EQUALS, documentCondition.getCondition());
        assertEquals("age", document.getName());
        assertEquals(12, document.get());
    }

    @ParameterizedTest(name = "Should parser the query {0}")
    @ValueSource(strings = {"delete from God where age = @age"})
    public void shouldReturnErrorWhenIsQueryWithParamAsync(String query) {

        assertThrows(QueryException.class, () -> {
            parser.queryAsync(query, documentCollectionAsync, s->{}, observer);
        });

    }

    @ParameterizedTest(name = "Should parser the query {0}")
    @ValueSource(strings = {"delete from God where age = @age"})
    public void shouldReturnErrorWhenDontBindParametersAsync(String query) {

        DocumentPreparedStatementAsync prepare = parser.prepareAsync(query, documentCollectionAsync, observer);
        assertThrows(QueryException.class, () -> {
            prepare.getResultList(s ->{});
        });
    }

    @ParameterizedTest(name = "Should parser the query {0}")
    @ValueSource(strings = {"delete from God where age = @age"})
    public void shouldExecutePrepareStatmentAsync(String query) {
        ArgumentCaptor<DocumentDeleteQuery> captor = ArgumentCaptor.forClass(DocumentDeleteQuery.class);

        DocumentPreparedStatementAsync prepare = parser.prepareAsync(query, documentCollectionAsync, observer);
        prepare.bind("age", 12);
        Consumer<List<DocumentEntity>> callBack = s -> {
        };
        prepare.getResultList(callBack);
        Mockito.verify(documentCollectionAsync).delete(captor.capture(), Mockito.any(Consumer.class));
        DocumentDeleteQuery documentQuery = captor.getValue();
        DocumentCondition documentCondition = documentQuery.getCondition().get();
        Document document = documentCondition.getDocument();
        assertEquals(EQUALS, documentCondition.getCondition());
        assertEquals("age", document.getName());
        assertEquals(12, document.get());
    }

    @ParameterizedTest(name = "Should parser the query {0}")
    @ValueSource(strings = {"delete  from God where name = \"Ada\" or age = 20"})
    public void shouldReturnParserQueryAsync(String query) {
        ArgumentCaptor<DocumentDeleteQuery> captor = ArgumentCaptor.forClass(DocumentDeleteQuery.class);

        Consumer<List<DocumentEntity>> callBack = s -> {
        };
        parser.queryAsync(query, documentCollectionAsync, callBack, observer);
        Mockito.verify(documentCollectionAsync).delete(captor.capture(), Mockito.any(Consumer.class));
        DocumentDeleteQuery documentQuery = captor.getValue();

        checkBaseQuery(documentQuery);
        assertTrue(documentQuery.getCondition().isPresent());
        DocumentCondition condition = documentQuery.getCondition().get();
        Document document = condition.getDocument();
        assertEquals(OR, condition.getCondition());
        List<DocumentCondition> conditions = document.get(new TypeReference<List<DocumentCondition>>() {
        });
        assertThat(conditions, contains(eq(Document.of("name", "Ada")),
                eq(Document.of("age", 20L))));
    }
    private void checkBaseQuery(DocumentDeleteQuery documentQuery) {
        assertTrue(documentQuery.getDocuments().isEmpty());
        assertEquals("God", documentQuery.getDocumentCollection());
    }
}