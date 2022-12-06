package com.shopme.admin.category;

import com.shopme.admin.utils.FileUploadUtil;
import com.shopme.common.entity.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.*;

@Controller
public class CategoryController {

    @Autowired
    CategoryService categoryService;

    @GetMapping(value = "/categories")
    public String getCategories(@Param("sortDir") String sortDir, Model model){
        if (sortDir ==  null || sortDir.isEmpty()) {
            sortDir = "asc";
        }
        List<Category> categories = categoryService.getAllCategories(sortDir);

        String reverseSortDir = sortDir.equals("asc") ? "desc" : "asc";

        model.addAttribute("categories", categories);
        model.addAttribute("reverseSortDir", reverseSortDir);
        return "category/categories";
    }

    @GetMapping("/categories/new")
    public String newCategories(Model model){
//        List<Category> categories = categoryService.getAllCategories();
        Map<List<Category>, List<String>> map = categoryService.getHierarchicalMap();
        List<Category> categories = new ArrayList<>();
        List<String> stringCategories = new ArrayList<>();

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

    @PostMapping("/categories/save")
    public String saveCategory(Category category,
                               @RequestParam("fileImage") MultipartFile multipartFile,
                               RedirectAttributes ra) throws IOException {
        if (!multipartFile.isEmpty()) {
            String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
            category.setImage(fileName);

            Category savedCategory = categoryService.save(category);
            String uploadDir = "category-images/" + savedCategory.getId();

            FileUploadUtil.cleanDir(uploadDir);
            FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);
        } else {
            categoryService.save(category);
        }

        ra.addFlashAttribute("message", "The category has been saved successfully.");
        return "redirect:/categories";
    }

    @GetMapping("/categories/edit/{id}")
    public String editCategory(@PathVariable(name = "id") Integer id, Model model,
                               RedirectAttributes ra) {
        try {
            Category category = categoryService.get(id);
            Map<List<Category>, List<String>> map = categoryService.getHierarchicalMap();

            List<Category> categories = new ArrayList<>();
            List<String> stringCategories = new ArrayList<>();

            for (List<Category> entry : map.keySet()){
                categories = entry;
                stringCategories = map.get(entry);
            }

            model.addAttribute("category", category);
            model.addAttribute("categories", categories);
            model.addAttribute("stringCategories", stringCategories);
            model.addAttribute("pageTitle", "Edit Category (ID: " + id + ")");

            return "category/category_form";
        } catch (CategoryNotFoundException ex) {
            ra.addFlashAttribute("message", ex.getMessage());
            return "redirect:/categories";
        }
    }

    @GetMapping("/categories/{id}/enabled/{status}")
    public String updateCategoryEnabledStatus(@PathVariable("id") Integer id,
                                              @PathVariable("status") boolean enabled, RedirectAttributes redirectAttributes) {
        categoryService.updateCategoryEnabledStatus(id, enabled);
        String status = enabled ? "enabled" : "disabled";
        String message = "The category ID " + id + " has been " + status;
        redirectAttributes.addFlashAttribute("message", message);

        return "redirect:/categories";
    }

    @GetMapping("/categories/delete/{id}")
    public String deleteCategory(@PathVariable(name = "id") Integer id,
                                 Model model,
                                 RedirectAttributes redirectAttributes) {
        try {
            categoryService.delete(id);
            String categoryDir = "category-images/" + id;
            FileUploadUtil.removeDir(categoryDir);

            redirectAttributes.addFlashAttribute("message",
                    "The category ID " + id + " has been deleted successfully");
        } catch (CategoryNotFoundException ex) {
            redirectAttributes.addFlashAttribute("message", ex.getMessage());
        }

        return "redirect:/categories";
    }


}
