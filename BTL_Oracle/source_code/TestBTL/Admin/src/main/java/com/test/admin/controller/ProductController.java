package com.test.admin.controller;

import com.test.library.dto.ProductDto;
import com.test.library.model.Category;
import com.test.library.model.Product;
import com.test.library.service.CategoryService;
import com.test.library.service.ProductService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    private final    CategoryService categoryService;
    @GetMapping("/products")
    public String products(Model model, Principal principal) {
        if(principal == null){
            return "redirect:/login";
        }
        List<ProductDto> productDtoList =productService.allProduct();
        model.addAttribute("title","Manage Products");
        model.addAttribute("products", productDtoList);
        model.addAttribute("size",productDtoList.size());
        return "products";
    }

    @GetMapping("/add-product")
    public String addProductForm(Model model,Principal principal){
        if(principal == null){
            return "redirect:/login";
        }
        model.addAttribute("title", "Add Product");
        List<Category> categories = categoryService.findAllByActivated();
        model.addAttribute("categories",categories);
        model.addAttribute("productDto",new ProductDto());
        return "add-product";
    }
    @PostMapping("/save-product")
    public String saveProduct(@ModelAttribute("productDto")ProductDto productDto,
                              @RequestParam("imageProduct") MultipartFile imageProduct,
                              RedirectAttributes attributes,Principal principal){
        try{
            if(principal == null){
                return "redirect:/login";
            }
            productService.save(imageProduct,productDto);
            attributes.addFlashAttribute("success","Add product success");
        }catch(Exception e){
            e.printStackTrace();
            attributes.addFlashAttribute("error","Failed to add");
        }
        return "redirect:/products/0";
    }
    @GetMapping("/update-product/{id}")
    public String updateProductForm(@PathVariable("id") Long id,Model model,Principal principal){
        if(principal == null){
            return "redirect:/products";
        }
        model.addAttribute("title","Update Product");
        List<Category> categories = categoryService.findAllByActivated();
        ProductDto productDto = productService.getById(id);
        model.addAttribute("categories",categories);
        model.addAttribute("productDto",productDto);
        return "update-product";

    }
    @PostMapping("/update-product/{id}")
    public String updateProduct(@ModelAttribute("productDto") ProductDto productDto,
                                @RequestParam("imageProduct") MultipartFile imageProduct,
                                RedirectAttributes attributes,Principal principal){
        try{
            if(principal == null){
                return "redirect:/login";
            }
            productService.update(imageProduct,productDto);
            attributes.addFlashAttribute("success","Update successfully");

        }catch (Exception e){
        e.printStackTrace();
            attributes.addFlashAttribute("error","Error server, please try again!");
        }
        return "redirect:/products/0";
    }
    @RequestMapping(value = "/enable-product",method={RequestMethod.PUT,RequestMethod.GET})
    public String enableProduct(Long id,RedirectAttributes attributes,Principal principal ){
        try{
            if(principal == null){
                return "redirect:/login";
            }
            productService.enableById(id);
            attributes.addFlashAttribute("success","Enable successfully !");
        }catch (Exception e){
            e.printStackTrace();
            attributes.addFlashAttribute("failed","Enable failed !");
        }
        return "redirect:/products/0";
    }
    @RequestMapping(value = "/delete-product", method = {RequestMethod.PUT, RequestMethod.GET})
    public String deletedProduct(Long id, RedirectAttributes redirectAttributes, Principal principal) {
        try {
            if (principal == null) {
                return "redirect:/login";
            }
            productService.deleteById(id);
            redirectAttributes.addFlashAttribute("success", "Deleted successfully!");
        } catch (Exception e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("error", "Deleted failed!");
        }
        return "redirect:/products/0";
    }

    @GetMapping("/products/{pageNo}")
    public String allProducts(@PathVariable("pageNo") int pageNo, Model model, Principal principal) {
        if (principal == null) {
            return "redirect:/login";
        }
        Page<ProductDto> products = productService.getAllProducts(pageNo);
        model.addAttribute("title", "Manage Products");
        model.addAttribute("size", products.getSize());
        model.addAttribute("products", products);
        model.addAttribute("currentPage", pageNo);
        model.addAttribute("totalPages", products.getTotalPages());
        return "products";
    }
    @GetMapping("/search-products/{pageNo}")
    public String searchProduct(@PathVariable("pageNo") int pageNo
            ,@RequestParam(value ="keyword") String keyword,Model model,Principal principal){
        if(principal == null){
            return "redirect:/login";
        }

        Page<ProductDto> products = productService.searchProducts(pageNo,keyword);
        model.addAttribute("title","Search Results");
        model.addAttribute("size",products.getSize());
        model.addAttribute("totalPages",products.getTotalPages());
        model.addAttribute("currentPage",pageNo);
        model.addAttribute("products",products);
        return "product-result";

    }
}
