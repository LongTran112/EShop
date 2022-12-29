package com.shopme.admin.brand;

import com.shopme.common.entity.Brand;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface BrandRepository extends PagingAndSortingRepository<Brand, Integer> {
	
	public Long countById(Integer id);
	
	public Brand findByName(String name);
	
	@Query("SELECT b FROM Brand b WHERE b.name LIKE %?1%")
	public Page<Brand> findAll(String keyword, Pageable pageable);

//	It is called projection - tell Spring JPA to return Brand objects that are populated
//	with only id and name. If not, it will populate all fields declared
//	in the Brand entity class.
	@Query("SELECT NEW Brand(b.id, b.name) from Brand b order by b.name ASC ")
	public List<Brand> findAll();
}
