package co.com.tenico.data.repository;

import java.util.List;
import java.util.Map;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.metamodel.SingularAttribute;

/**
 * @author inerjanuer
 * @param <T>
 * @param <ID>
 */
public interface CrudRepository<T, ID> extends Repository<T, ID> {

    /**
     *
     * @param entity
     * @return
     */
    T save(T entity);

    /**
     *
     * @param entity
     * @return
     */
    T update(T entity);

    /**
     *
     * @param entity
     */
    void delete(T entity);

    /**
     *
     * @param id
     * @return
     */
    T findById(ID id);

    /**
     *
     * @return
     */
    List<T> findAll();

    /**
     *
     * @param parameters
     * @return
     */
    List<T> findAll(Map<String, String> parameters);

    /**
     *
     * @param entity
     * @param stringEntity
     * @param value
     * @param <X>
     * @return
     */
    <X> List<T> findAllByValue(SingularAttribute<T, X> entity, SingularAttribute<X, String> stringEntity, String value);

    /**
     *
     * @param entity
     * @param idEntity
     * @param id
     * @param <X>
     * @return
     */
    <X> List<T> findAllById(SingularAttribute<T, X> entity, SingularAttribute<X, ID> idEntity, ID id);

    /**
     *
     * @param entity
     * @param value
     * @return
     */
    T findByValue(SingularAttribute<T, String> entity, String value);

    /**
     *
     * @return
     */
    Long count();


}
