package com.shopme.admin.category;

import com.shopme.common.entity.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface CategoryRepository extends PagingAndSortingRepository<Category, Integer> {

    @Query("SELECT c FROM Category c WHERE c.parent.id is NULL")
    public List<Category> findRootCategories(Sort sort);

    @Query("SELECT c FROM Category c WHERE c.parent.id is NULL")
    public Page<Category> findRootCategories(Pageable pageable);

    @Query("SELECT c FROM Category c WHERE c.name LIKE %?1%")
    Page<Category> search(String keyword, Pageable pageable);

    Long countById(Integer id);

    Category findByName(String name);

    Category findByAlias(String alias);

    @Query("UPDATE Category c set c.enabled = ?2 where c.id =?1")
    @Modifying
    void updateEnabledStatus(Integer id, boolean enabled);
}
