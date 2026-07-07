package org.alphatrack.screensociety.repositories;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.alphatrack.screensociety.dto.request.filters.CommentFilterOptions;
import org.alphatrack.screensociety.models.Comment;
import org.alphatrack.screensociety.repositories.contracts.CommentRepository;
import org.alphatrack.screensociety.repositories.contracts.CommentRepositoryCustom;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
@Repository
public class CommentRepositoryImpl implements CommentRepositoryCustom {

    @PersistenceContext
    private final EntityManager entityManager;

    @Autowired
    public CommentRepositoryImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public List<Comment> findAll(CommentFilterOptions commentFilterOptions) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Comment> cq = cb.createQuery(Comment.class);
        Root<Comment> commentRoot = cq.from(Comment.class);

        List<Predicate> predicates = new ArrayList<>();

        commentFilterOptions.getContent().ifPresent(content ->
                predicates.add(cb.like(commentRoot.get("content"),"%" + content + "%")));

        commentFilterOptions.getAuthorUsername().ifPresent(authorUsername ->
                predicates.add(cb.like(commentRoot.get("authorUsername"), "%" + authorUsername + "%")));

        commentFilterOptions.getMinLength().ifPresent(minLength ->
                predicates.add(cb.equal(commentRoot.get("minLength"), minLength)));

        commentFilterOptions.getMaxLength().ifPresent(maxLength ->
                predicates.add(cb.equal(commentRoot.get("maxLength"), maxLength)));

        cq.where(cb.and(predicates.toArray(new Predicate[0])));

        commentFilterOptions.getSortBy().ifPresent(sortBy -> {
            String sortOrder = commentFilterOptions.getSortOrder().orElse("asc");
            if (sortOrder.equalsIgnoreCase("desc")) {
                cq.orderBy(cb.desc(commentRoot.get(sortBy)));
            } else {
                cq.orderBy(cb.asc(commentRoot.get(sortBy)));
            }
        });

        return entityManager.createQuery(cq).getResultList();
     }
}
