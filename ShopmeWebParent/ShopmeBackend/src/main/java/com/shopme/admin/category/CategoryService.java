package com.shopme.admin.category;

import com.shopme.common.entity.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class CategoryService {
    @Autowired
    CategoryRepository categoryRepository;

    public List<Category> getAllCategories(){
        List<Category> categories = (List<Category>) categoryRepository.findAll();
        return categories;
    }

    //Lay Map Category theo thu tu hierarchical va text value cua no cho front-end xem
    //Example:
    //Computer
    //--Computer Components
    //----Memory
    //--Desktops
    //--Laptops
    //Electronics
    //--Cameras
    //--Smartphones
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
                for (Category childCategory: category.getChildren()){
                    subLevel = 0;
                    hierarchicalCategories.add(childCategory);
                    addIndentCategoryToHierarchicalList(childCategory, stringCategories, subLevel + 1);
                    getChildren(childCategory, hierarchicalCategories);
                    getChildren(childCategory, stringCategories, subLevel+1);
                }
            }
        }

        map.put(hierarchicalCategories, stringCategories);
        for (Category category:
                hierarchicalCategories) {
            System.out.println(category.getName());
        }
        System.out.println("----------------");
        for (String string:
                stringCategories) {
            System.out.println(string);

        }
        return map;
    }

    public Category getChildren(Category category, List<Category> hierarchicalCategories){
        Set<Category> children = category.getChildren();
        for (Category childCategory :
                children) {
            hierarchicalCategories.add(childCategory);
            return getChildren(childCategory, hierarchicalCategories);
        }
        return null;
    }

    public Category getChildren(Category category, List<String> hierarchicalCategories,
                                int subLevel){

        Set<Category> children = category.getChildren();
        for (Category childCategory :
                children) {
            addIndentCategoryToHierarchicalList(childCategory, hierarchicalCategories, subLevel+1);
            return getChildren(childCategory, hierarchicalCategories, subLevel + 1);
        }
        return null;
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









}
