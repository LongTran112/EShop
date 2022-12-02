package com.shopme.admin.category;

import com.shopme.common.entity.Category;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

@DataJpaTest(showSql = false)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(value = false)
public class CategoryRepositoryTests {

    @Autowired
    private CategoryRepository repository;

    @Test
    public void testCreateRootCategory(){
        Category category = new Category("Electronics");
        Category savedCategory = repository.save(category);

        assertThat(savedCategory.getId()).isGreaterThan(0);
    }

    @Test
    public void testCreateSubCategory(){
//        Category parent = new Category(1);
//        Category subCategory = new Category("Computer Components", parent);
//        Category savedCategory = repository.save(subCategory);

//        Category parent2 = new Category(2);
//        Category cameras = new Category("Cameras", parent2);
//        Category smartphones = new Category("Smartphones", parent2);
        Category parent5 = new Category(5);
        Category subCategory = new Category("Memory", parent5);
        Category savedCategory = repository.save(subCategory);

        assertThat(savedCategory.getId()).isGreaterThan(0);

//        assertThat(savedCategory.getId()).isGreaterThan(0);
//        repository.saveAll(List.of(cameras, smartphones));
    }

    @Test
    public void testGetCategory(){
        Category category = repository.findById(1).get();
        System.out.println(category.getName());

        Set<Category> children = category.getChildren();

        for (Category subCategory : children){
            System.out.println(subCategory.getName());
        }

        assertThat(children.size()).isGreaterThan(0);
    }

    @Test
    public void testPrintHierarchicalCategories(){
        Iterable<Category> categories = repository.findAll();

        for (Category category : categories){
            if (category.getParent() == null){
                System.out.println(category.getName());

                Set<Category> children = category.getChildren();

                for (Category subCategory : children){
                    System.out.println("--" + subCategory.getName());
                    printChildren(subCategory, 1);
                }
            }
        }
    }

    private void printChildren(Category parent, int subLevel){
        int newSubLevel = subLevel + 1;
        Set<Category> children = parent.getChildren();


        for (Category subCategory : children){
            for (int i = 0; i < newSubLevel; i++) {
                System.out.print("--");
            }

            System.out.println(subCategory.getName());

            printChildren(subCategory, newSubLevel);
        }
    }

    @Test
    public void myTestGetHierarchicalCategories(){
        List<Category> categories = (List<Category>) repository.findAll();
        List<Category> hierarchicalCategories = new ArrayList<>();

        for (Category category: categories){
            //Chi lay nhung category nao khong co parent ID
            if (category.getParent() == null){
                hierarchicalCategories.add(category);
                for (Category childCategory: category.getChildren()){
                    hierarchicalCategories.add(childCategory);
                    getChildren(childCategory, hierarchicalCategories);
                }
            }
        }
        for (Category category: hierarchicalCategories) {
            System.out.println(category.getName());
        }
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

    @Test
    public void getStringHierarchicalCategory(){
        List<Category> categories = (List<Category>) repository.findAll();
        List<String> hierarchicalCategories = new ArrayList<>();


        for (Category category: categories){
            int subLevel = 0;
            //Chi lay nhung category nao khong co parent ID
            if (category.getParent() == null){
                addIndentCategoryToHierarchicalList(category, hierarchicalCategories, subLevel);
                for (Category childCategory: category.getChildren()){
                    subLevel = 0;
                    addIndentCategoryToHierarchicalList(childCategory, hierarchicalCategories, subLevel + 1);
                    getChildren(childCategory, hierarchicalCategories, subLevel+1);
                }
            }
        }
        for (String string :
                hierarchicalCategories) {
            System.out.println(string);
        }
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
            builder.append(">>");
        }
        builder.append(category.getName());
        hierarchical.add(builder.toString());

    }

    @Test
    public void myTestGetHierarchicalMap(){
        List<Category> categories = (List<Category>) repository.findAll();
        List<Category> hierarchicalCategories = new ArrayList<>();
        List<String> stringCategories = new ArrayList<>();
        Map<List<Category>, List<String>> map = new HashMap<>();


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


    }


}
