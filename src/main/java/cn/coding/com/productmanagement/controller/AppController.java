package cn.coding.com.productmanagement.controller;

import cn.coding.com.productmanagement.dto.CategoryDTO;
import cn.coding.com.productmanagement.dto.ProductDTO;
import cn.coding.com.productmanagement.service.CategoryService;
import cn.coding.com.productmanagement.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.NoHandlerFoundException;

import javax.validation.Valid;
import java.lang.reflect.Array;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
@RequestMapping("/products")
public class AppController {

    private ProductService productService;
    private CategoryService categoryService;
    private int currentPage = 1;
    private int pageSize = 5;
    private static final Logger LOGGER = Logger.getLogger(AppController.class.getName());

    @Autowired
    public AppController(ProductService productService, CategoryService categoryService) {
        this.productService = productService;
        this.categoryService = categoryService;
    }

    @GetMapping("")
    public String index(Model model, @RequestParam("page")Optional<Integer> page,
                        @RequestParam("size") Optional<Integer> size) {
        page.ifPresent(p -> currentPage = p);
        size.ifPresent(s -> pageSize = s);

        Pageable pageable = PageRequest.of(currentPage - 1,pageSize);

        Page<ProductDTO> productPage = productService.getAllProduct(pageable);
        model.addAttribute("productPage", productPage);

        int totalPages = productPage.getTotalPages();
        if (totalPages > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages).boxed().collect(Collectors.toList());
            model.addAttribute("pageNumbers", pageNumbers);
        }
        return "products";
    }

    @GetMapping(value = "/add")
    public String addProduct(@Valid Model model) {
        List<CategoryDTO> categoryList = categoryService.getAllCategory();
        model.addAttribute("product", new ProductDTO());
        model.addAttribute("categoryList", categoryList);
        return "addproduct";
    }

    @PostMapping(value = "/save")
    public String save(ProductDTO productDTO) {
        LOGGER.log(Level.INFO, "/ {0}", productDTO.getName());
        productService.saveProduct(productDTO);
        return "redirect:/products";
    }

    @GetMapping(value = "/edit/{id}")
    public String editProduct(@PathVariable("id") Integer id, Model model) {
        ProductDTO product = productService.getProductById(id);
        List<CategoryDTO> categoryList = categoryService.getAllCategory();
        model.addAttribute("product", product);
        model.addAttribute("categoryList", categoryList);
        return "editproduct";
    }

    @GetMapping(value = "/delete/{id}")
    public String deleteProduct(@PathVariable("id") Integer id) {
        productService.deleteProduct(id);
        return "redirect:/products";
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    public String handlerException() {
        return "/error/404";
    }
}
