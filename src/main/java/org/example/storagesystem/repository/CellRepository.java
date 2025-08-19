package org.example.storagesystem.repository;

import org.example.storagesystem.entity.Cell;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface CellRepository extends JpaRepository<Cell, Long> {
    @Query(value = """
    with recursive parent_chain as (
        select id, parent_cell_id
        from cells
        where id = :parentId
        union all
        select c.id, c.parent_cell_id
        from cells c
        inner join parent_chain pc on c.id = pc.parent_cell_id
    )
    select count(*) > 0
    from parent_chain
    where parent_cell_id = :cellId
    """, nativeQuery = true)
    boolean hasCycle(@Param("cellId") Long cellId,
                     @Param("parentId") Long parentId);

    @EntityGraph(attributePaths = {"children"})
    @Query("select s from Cell s where s.id = :id")
    Optional<Cell> findWithChildrenById(@Param("id") Long id);

    @EntityGraph(attributePaths = "children")
    @Query("SELECT c FROM Cell c")
    Page<Cell> findAllWithChildren(Pageable pageable);
}
