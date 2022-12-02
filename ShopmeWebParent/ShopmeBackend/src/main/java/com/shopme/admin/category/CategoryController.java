package com.shopme.admin.category;

import ch.qos.logback.core.boolex.EvaluationException;
import com.shopme.common.entity.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.*;

@Controller
public class CategoryController {

    @Autowired
    CategoryService categoryService;

    @GetMapping(value = "/categories")
    public String getCategories(Model model){
        List<Category> categories = categoryService.getAllCategories();
        model.addAttribute("categories", categories);
        return "category/categories";
    }

    @GetMapping("/categories/new")
    public String newCategories(Model model){
//        List<Category> categories = categoryService.getAllCategories();
        Map<List<Category>, List<String>> map = categoryService.getHierarchicalMap();
        List<Category> categories = new ArrayList<>();
        List<String> stringCategories = new ArrayList<>();
        List<String> stringCategories2 = new ArrayList<>();

        for (List<Category> entry : map.keySet()){
            categories = entry;
            stringCategories = map.get(entry);
        }

        Category category = new Category();
        category.setEnabled(true);

        model.addAttribute("category", category);
        model.addAttribute("categories", categories);
        model.addAttribute("stringCategories", stringCategories);

        return "category/category_form";
    }

    @PostMapping(value = "/categories/save")
    public String saveCategory(Category category, RedirectAttributes redirectAttributes,
                               @RequestParam("image") MultipartFile multipartFile) throws IOException {



        return "redirect:/categories";
    }
}
