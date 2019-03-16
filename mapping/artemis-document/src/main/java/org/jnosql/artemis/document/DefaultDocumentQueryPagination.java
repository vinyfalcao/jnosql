package org.jnosql.artemis.document;

import org.jnosql.artemis.Pagination;
import org.jnosql.diana.api.Sort;
import org.jnosql.diana.api.document.Document;
import org.jnosql.diana.api.document.DocumentCondition;
import org.jnosql.diana.api.document.DocumentQuery;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

final class DefaultDocumentQueryPagination implements DocumentQueryPagination {
    
    private final DocumentQuery query;

    private final Pagination pagination;

    DefaultDocumentQueryPagination(DocumentQuery query, Pagination pagination) {
        this.query = query;
        this.pagination = pagination;
    }

    @Override
    public long getLimit() {
        return pagination.getLimit();
    }

    @Override
    public long getSkip() {
        return pagination.getSkip();
    }

    @Override
    public String getDocumentCollection() {
        return query.getDocumentCollection();
    }

    @Override
    public Optional<DocumentCondition> getCondition() {
        return query.getCondition();
    }

    @Override
    public List<String> getDocuments() {
        return query.getDocuments();
    }

    @Override
    public List<Sort> getSorts() {
        return query.getSorts();
    }

    @Override
    public DocumentQueryPagination next() {
        return new DefaultDocumentQueryPagination(query, pagination.next());
    }

    @Override
    public Pagination getPagination() {
        return pagination.unmodifiable();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        DefaultDocumentQueryPagination that = (DefaultDocumentQueryPagination) o;
        return Objects.equals(query, that.query) &&
                Objects.equals(pagination, that.pagination);
    }

    @Override
    public int hashCode() {
        return Objects.hash(query, pagination);
    }

    @Override
    public String toString() {
        return "DefaultDocumentQueryPagination{" +
                "query=" + query +
                ", pagination=" + pagination +
                '}';
    }
}
