package com.ecom.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.ecom.model.Orders;
import com.ecom.model.Product;
import com.ecom.model.Users;
import com.ecom.service.AdminUserService;
import com.ecom.service.OrderService;
import com.ecom.service.ProductService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private ProductService productService;

    @Autowired
    private AdminUserService adminUserService;

   //orders related

    @GetMapping("/orders/manage")
    public String showOrders(Model model) {
        List<Orders> orders = orderService.getAllOrders();
        model.addAttribute("orders", orders);
        return "/admin/manageOrder";
    }

    @PostMapping("/orders/updateStatus/{orderId}")
    public String updateOrderStatus(@PathVariable String orderId, @RequestParam("status") String status) {
    	System.out.println(orderId);
    	orderService.updateOrderStatus(orderId, status);
        return "redirect:/admin/orders/manage";
    }

    // Products related methods

    @GetMapping("/products/manage")
    public String showProducts(Model model) {
        List<Product> products = productService.getAllProducts();
        model.addAttribute("products", products);
        return "/admin/manageProduct";
    }

    
    @PostMapping("/products/delete/{productId}")
    public String deleteProduct(@PathVariable String productId) {
        productService.deleteProduct(productId);
        return "redirect:/admin/products/manage"; // Redirecting to the correct URL
    }

    @PostMapping("/products/updateStock/{productId}")
    public String updateStock(@PathVariable String productId, @RequestParam("quantity") int quantity) {
        Product product = productService.getProductById(productId);
        if (product != null) {
            product.setQuantity(quantity);
            productService.saveProduct(product);
        }
        return "redirect:/admin/products/manage"; // Redirect to product management page
    }

    @GetMapping("/products/add")
    public String showAddProductForm(Model model) {
    	 List<String> categories = new ArrayList<>();
         categories.add("FaceWash");
         categories.add("Shampoo");
         categories.add("Moisturizer");
         categories.add("FaceWash");
         categories.add("Shampoo");
         model.addAttribute("categories", categories);
         model.addAttribute("product", new Product());
        return "/admin/addProduct";
    }

    @PostMapping("/products/add")
    public String addProduct(@ModelAttribute("product") Product product, @RequestParam("image") MultipartFile file, RedirectAttributes redirectAttributes) {
        if (!file.isEmpty()) {
            try {
                String fileName = StringUtils.cleanPath(file.getOriginalFilename());
                String uploadDir = "src/main/resources/static/uploads"; 
                Path uploadPath = Paths.get(uploadDir);
                Path filePath = uploadPath.resolve(fileName); // Resolve the file path
                Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

                product.setImageUrl("/uploads/" + fileName);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        productService.saveProduct(product);
        redirectAttributes.addFlashAttribute("successMessage", "Product added successfully!");
        return "redirect:/admin/products/manage";
    }


    // Users related methods

    @GetMapping("/users/manage")
    public String showUsers(Model model) {
        List<Users> users = adminUserService.getAllUsers();
        model.addAttribute("users", users);
        return "/admin/manageUser";
    }

    @PostMapping("/users/delete")
    public String deleteUser(@RequestParam("userId") String userId) {
        adminUserService.deleteUser(userId);
        return "redirect:/admin/users/manage"; // Redirecting to the correct URL
    }
    
    //dashboard related
    
    @GetMapping("/dashboard")
    public String adminDashboard() {
        return "admin/dashboard";
    }
    
    @GetMapping("/logout")
    public String adminlogout(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        return "redirect:/";
    }

}
