package org.alphatrack.screensociety.repositories;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.*;
import org.alphatrack.screensociety.dto.request.filters.PostFilterOptions;
import org.alphatrack.screensociety.models.Post;
import org.alphatrack.screensociety.repositories.contracts.PostRepositoryCustom;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class PostRepositoryImpl implements PostRepositoryCustom {

    private final EntityManager entityManager;

    @Autowired
    public PostRepositoryImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }


    @Override
    public List<Post> findAll(PostFilterOptions postFilterOptions) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Post> cq = cb.createQuery(Post.class);
        Root<Post> postRoot = cq.from(Post.class);

        List<Predicate> predicates = new ArrayList<>();

        postFilterOptions.getTitle().ifPresent(title ->
                predicates.add(cb.like(postRoot.get("title"), "%" + title + "%")));

        postFilterOptions.getAuthorUsername().ifPresent(authorUsername ->
                predicates.add(cb.like(postRoot.get("author").get("username"), authorUsername)));

        postFilterOptions.getCreatedAfter().ifPresent(afterDate ->
                predicates.add(cb.greaterThanOrEqualTo(postRoot.get("createdAt"), afterDate.atStartOfDay())));

        postFilterOptions.getCreatedBefore().ifPresent(beforeDate ->
                predicates.add(cb.lessThanOrEqualTo(postRoot.get("createdAt"), beforeDate.atTime(
                        23, 59, 59, 999999999))));

        postFilterOptions.getTagName().ifPresent(tagName -> {
            Join<Post, Object> tagsJoin = postRoot.join("tags");
            predicates.add(cb.equal(tagsJoin.get("name"), tagName));
        });

        cq.where(cb.and(predicates.toArray(new Predicate[0])));

        postFilterOptions.getSortBy().ifPresent(sortBy -> {
            String sortOrder = postFilterOptions.getSortOrder().orElse("asc");
            if (sortOrder.equalsIgnoreCase("desc")) {
                cq.orderBy(cb.desc(postRoot.get(sortBy)));
            } else {
                cq.orderBy(cb.asc(postRoot.get(sortBy)));
            }
        });

        return entityManager.createQuery(cq).getResultList();
    }

    @Override
    public List<Post> findAll(Long userId, PostFilterOptions postFilterOptions) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Post> cq = cb.createQuery(Post.class);
        Root<Post> postRoot = cq.from(Post.class);

        List<Predicate> predicates = new ArrayList<>();

        predicates.add(cb.equal(postRoot.get("author").get("id"), userId));

        applyFiltersAndSorting(cb, cq, postRoot, predicates, postFilterOptions);

        return entityManager.createQuery(cq).getResultList();
    }

    @Override
    public List<Post> findTop10MostCommented() {
        String jpql = "SELECT p FROM Post p ORDER BY SIZE(p.commentList) DESC";
        TypedQuery<Post> query = entityManager.createQuery(jpql, Post.class);
        query.setMaxResults(10);
        return query.getResultList();
    }

    @Override
    public List<Post> find10MostRecentPosts() {
        String jpql = "SELECT p FROM Post p ORDER BY p.createdAt DESC";
        TypedQuery<Post> query = entityManager.createQuery(jpql, Post.class);
        query.setMaxResults(10);
        return query.getResultList();
    }

    private void applyFiltersAndSorting(CriteriaBuilder cb, CriteriaQuery<Post> cq, Root<Post> postRoot,List<Predicate> predicates ,PostFilterOptions postFilterOptions){


        postFilterOptions.getTitle().ifPresent(title ->
                predicates.add(cb.like(postRoot.get("title"), "%" + title + "%")));

        postFilterOptions.getAuthorUsername().ifPresent(authorUsername ->
                predicates.add(cb.like(postRoot.get("author").get("username"), authorUsername)));

        postFilterOptions.getCreatedAfter().ifPresent(afterDate ->
                predicates.add(cb.greaterThanOrEqualTo(postRoot.get("createdAt"), afterDate.atStartOfDay())));

        postFilterOptions.getCreatedBefore().ifPresent(beforeDate ->
                predicates.add(cb.lessThanOrEqualTo(postRoot.get("createdAt"), beforeDate.atTime(
                        23, 59, 59, 999999999))));

        postFilterOptions.getTagName().ifPresent(tagName -> {
            Join<Post, Object> tagsJoin = postRoot.join("tags");
            predicates.add(cb.equal(tagsJoin.get("name"), tagName));
        });

        cq.where(cb.and(predicates.toArray(new Predicate[0])));

        postFilterOptions.getSortBy().ifPresent(sortBy -> {
            String sortOrder = postFilterOptions.getSortOrder().orElse("asc");
            if (sortOrder.equalsIgnoreCase("desc")) {
                cq.orderBy(cb.desc(postRoot.get(sortBy)));
            } else {
                cq.orderBy(cb.asc(postRoot.get(sortBy)));
            }
        });

    }
}
