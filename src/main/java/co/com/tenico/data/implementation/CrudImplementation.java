package co.com.tenico.data.implementation;

import co.com.tenico.data.commons.CrudConstans;
import co.com.tenico.data.repository.CrudRepository;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.metamodel.SingularAttribute;
import java.util.List;
import java.util.Locale;
import java.util.Map;

@RequiredArgsConstructor
public  class CrudImplementation<T, ID> implements CrudRepository<T, ID> {

    @PersistenceContext
    @Getter(AccessLevel.PROTECTED)
    private EntityManager entityManager;

    private final Class<T> entityClass;

    @Override
    public T save(T entity) {
        entityManager.persist(entity);
        return entity;
    }

    @Override
    public T update(T entity) {
        return entityManager.merge(entity);
    }

    @Override
    public void delete(T entity) {
        entityManager.remove(entityManager.merge(entity));
    }

    @Override
    public T findById(ID id) {
        return entityManager.find(entityClass, id);
    }

    @Override
    public List<T> findAll() {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<T> cq = cb.createQuery(entityClass);
        cq.select(cq.from(entityClass));
        return entityManager.createQuery(cq).getResultList();
    }

    @Override
    public List<T> findAll(Map<String, String> parameters) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<T> cq = cb.createQuery(entityClass);
        Root<T> rt = cq.from(entityClass);
        cq.select(rt).distinct(true);

        String sortBy = parameters.get(CrudConstans.SORT);
        String pageSize = parameters.get(CrudConstans.PAGESIZE);
        String pageNo = parameters.get(CrudConstans.PAGENO);

        if(sortBy!=null) {
            if(sortBy.equals(CrudConstans.ASC)) {
                cq.orderBy(cb.asc(rt));
            }else if (sortBy.equals(CrudConstans.DESC)) {
                cq.orderBy(cb.desc(rt));
            }
        }

        int limit=10;
        int page=0;
        if(pageSize!=null) { limit = Integer.valueOf(pageSize); }
        if(pageNo!=null) { page = Integer.valueOf(pageNo) * limit; }

        return entityManager.createQuery(cq).setMaxResults(limit).setFirstResult(page).getResultList();
    }

    @Override
    public <X> List<T> findAllByValue(SingularAttribute<T, X> entity,
                                      SingularAttribute<X, String> stringEntity,
                                      String value) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<T> cq = cb.createQuery(entityClass);
        Root<T> rt = cq.from(entityClass);
        cq.select(rt).distinct(true);
        cq.where(cb.equal(rt.join(entity).get(stringEntity), value));
        TypedQuery<T> tq = entityManager.createQuery(cq);
        return tq.getResultList();
    }

    @Override
    public <X> List<T> findAllById(SingularAttribute<T, X> entity,
                                   SingularAttribute<X, ID> idEntity,
                                   ID id) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<T> cq = cb.createQuery(entityClass);
        Root<T> rt = cq.from(entityClass);
        cq.select(rt).distinct(true);
        cq.where(cb.equal(rt.join(entity).get(idEntity), id));
        TypedQuery<T> tq = entityManager.createQuery(cq);
        return tq.getResultList();
    }

    @Override
    public T findByValue(SingularAttribute<T, String> entity, String value) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<T> cq = cb.createQuery(entityClass);
        Root<T> rt = cq.from(entityClass);
        cq.where(cb.equal(rt.get(entity), value));
        cq.orderBy(cb.asc(rt.get(entity.getName())));
        return entityManager.createQuery(cq).getSingleResult();
    }

    @Override
    public Long count() {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> cq = cb.createQuery(Long.class);
        cq.select(cb.count(cq.from(entityClass)));
        return entityManager.createQuery(cq).getSingleResult();
    }

    protected String buildLikeValue(String value){
        return "%" + value +"%";
    }

    protected String buildLikeValueSearch(String value){
        return "%" + value.toLowerCase() + "%";
    }
}
