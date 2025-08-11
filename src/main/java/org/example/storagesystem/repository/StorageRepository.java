package org.example.storagesystem.repository;

import org.example.storagesystem.entity.Storage;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface StorageRepository extends JpaRepository<Storage, Long> {
    @Query(value = """
    with recursive parent_chain as (
        select id, parent_storage_id
        from storages
        where id = :parentId
        union all
        select s.id, s.parent_storage_id
        from storages s
        inner join parent_chain pc on s.id = pc.parent_storage_id
    )
    select count(*) > 0
    from parent_chain
    where parent_storage_id = :storageId
    """, nativeQuery = true)
    boolean hasCycle(@Param("storageId") Long storageId,
                     @Param("parentId") Long parentId);

    @EntityGraph(attributePaths = {"children"})
    Optional<Storage> findWithChildrenById(Long id);
}
