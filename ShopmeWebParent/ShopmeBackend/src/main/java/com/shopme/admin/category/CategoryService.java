package com.shopme.admin.category;

import com.shopme.common.entity.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;

@Service
@Transactional
public class CategoryService {
    @Autowired
    CategoryRepository categoryRepository;

    public List<Category> getAllCategories(String sortDir){

        Sort sort = Sort.by("name");

        if (sortDir.equals("asc")) {
            sort = sort.ascending();
        } else if (sortDir.equals("desc")) {
            sort = sort.descending();
        }

        List<Category> rootCategories = (List<Category>)
                categoryRepository.findRootCategories(sort);
        Map<List<Category>, List<String>> map = getHierarchicalMap(rootCategories);
        List<Category> categories = new ArrayList<>();

        for (List<Category> entry : map.keySet()){
            categories = entry;

        }
        return categories;
    }

    //Lay Map Category theo thu tu hierarchical va text value cua no cho front-end xem
    //Example:
    //Computer
    //>>Computer Components
    //>>>>Memory
    //>>Desktops
    //>>Laptops
    //Electronics
    //>>Cameras
    //>>Smartphones
    public Map<List<Category>, List<String>> getHierarchicalMap(List<Category> categories){
        List<Category> hierarchicalCategories = new ArrayList<>();
        List<String> stringCategories = new ArrayList<>();
        Map<List<Category>, List<String>> map = new LinkedHashMap<>();


        for (Category category: categories){
            int subLevel = 0;
            //Chi lay nhung category nao khong co parent ID
            if (category.getParent() == null){
                hierarchicalCategories.add(category);
                addIndentCategoryToHierarchicalList(category, stringCategories, subLevel);
                for (Category childCategory: sortSubCategories(category.getChildren())){
                    subLevel = 0;
                    hierarchicalCategories.add(childCategory);
                    addIndentCategoryToHierarchicalList(childCategory, stringCategories, subLevel + 1);
                    getChildren(childCategory, hierarchicalCategories);
                    getChildren(childCategory, stringCategories, subLevel+1);
                }
            }
        }

        map.put(hierarchicalCategories, stringCategories);
//        for (Category category:
//                hierarchicalCategories) {
//            System.out.println(category.getName());
//        }
//        System.out.println("----------------");
//        for (String string:
//                stringCategories) {
//            System.out.println(string);
//
//        }
        return map;
    }

    public Map<List<Category>, List<String>> getHierarchicalMap(){
        List<Category> categories = (List<Category>) categoryRepository.findAll();
        List<Category> hierarchicalCategories = new ArrayList<>();
        List<String> stringCategories = new ArrayList<>();
        Map<List<Category>, List<String>> map = new LinkedHashMap<>();


        for (Category category: categories){
            int subLevel = 0;
            //Chi lay nhung category nao khong co parent ID
            if (category.getParent() == null){
                hierarchicalCategories.add(category);
                addIndentCategoryToHierarchicalList(category, stringCategories, subLevel);
                for (Category childCategory: sortSubCategories(category.getChildren())){
                    subLevel = 0;
                    hierarchicalCategories.add(childCategory);
                    addIndentCategoryToHierarchicalList(childCategory, stringCategories, subLevel + 1);
                    getChildren(childCategory, hierarchicalCategories);
                    getChildren(childCategory, stringCategories, subLevel+1);
                }
            }
        }

        map.put(hierarchicalCategories, stringCategories);
        return map;
    }


    public Category save(Category category) {
        return categoryRepository.save(category);
    }

    public void getChildren(Category category, List<Category> hierarchicalCategories){
        Set<Category> children = sortSubCategories(category.getChildren());
        for (Category childCategory :
                children) {
            hierarchicalCategories.add(childCategory);
            getChildren(childCategory, hierarchicalCategories);
        }

    }

    public void getChildren(Category category, List<String> hierarchicalCategories,
                                int subLevel){

        Set<Category> children = sortSubCategories(category.getChildren());
        for (Category childCategory :
                children) {
            addIndentCategoryToHierarchicalList(childCategory, hierarchicalCategories, subLevel+1);
            getChildren(childCategory, hierarchicalCategories, subLevel + 1);
        }

    }

    public void addIndentCategoryToHierarchicalList(Category category, List<String> hierarchical,
                                                    int subLevel){
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < subLevel; i++) {
            builder.append("++");
        }
        builder.append(category.getName());
        hierarchical.add(builder.toString());

    }


    public Category get(int id) throws CategoryNotFoundException {
        try {
            return categoryRepository.findById(id).get();
        } catch (NoSuchElementException ex) {
            throw new CategoryNotFoundException("Could not find any category with ID " + id);
        }
    }

    public String checkUnique(Integer id, String name, String alias) {
        boolean isCreatingNew = (id == null || id == 0);

        Category categoryByName = categoryRepository.findByName(name);

        if (isCreatingNew) {
            if (categoryByName != null) {
                return "DuplicateName";
            } else {
                Category categoryByAlias = categoryRepository.findByAlias(alias);
                if (categoryByAlias != null) {
                    return "DuplicateAlias";
                }
            }
        } else {
            if (categoryByName != null && categoryByName.getId() != id) {
                return "DuplicateName";
            }

            Category categoryByAlias = categoryRepository.findByAlias(alias);
            if (categoryByAlias != null && categoryByAlias.getId() != id) {
                return "DuplicateAlias";
            }

        }
        return "OK";
    }

    private SortedSet<Category> sortSubCategories(Set<Category> children) {
        return sortSubCategories(children, "asc");
    }

    private SortedSet<Category> sortSubCategories(Set<Category> children, String sortDir) {
        SortedSet<Category> sortedChildren = new TreeSet<>(new Comparator<Category>() {
            @Override
            public int compare(Category cat1, Category cat2) {
                if (sortDir.equals("asc")) {
                    return cat1.getName().compareTo(cat2.getName());
                } else {
                    return cat2.getName().compareTo(cat1.getName());
                }
            }
        });

        sortedChildren.addAll(children);

        return sortedChildren;
    }

    public void updateCategoryEnabledStatus(Integer id, boolean enabled){
        categoryRepository.updateEnabledStatus(id, enabled);
    }

    public void delete(Integer id) throws CategoryNotFoundException {
        Long countById = categoryRepository.countById(id);
        if (countById == null || countById == 0) {
            throw new CategoryNotFoundException("Could not find any category with ID " + id);
        }

        categoryRepository.deleteById(id);
    }









}
