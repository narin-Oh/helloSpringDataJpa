package kr.ac.hansung.cse.hellospringdatajpa.controller;

import kr.ac.hansung.cse.hellospringdatajpa.entity.Product;
import kr.ac.hansung.cse.hellospringdatajpa.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/products")
public class ProductController {

    @Autowired
    private ProductService service;

    @GetMapping({"", "/"})
    public String viewHomePage(Model model,
                               @RequestParam(value = "login", required = false) String login,
                               Principal principal) {
        if (principal != null) {
            String email = principal.getName();

            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String role = authentication.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority)
                    .findFirst().orElse("ROLE_USER");

            String message = "환영합니다. " + email + " (" + role + ")";
            model.addAttribute("loginMessage", message);
        }

        if ("success".equals(login)) {
            model.addAttribute("loginSuccess", true);
        }

        model.addAttribute("listProducts", service.listAll());
        return "index";
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/new")
    public String showNewProductPage(Model model) {

        Product product = new Product();
        model.addAttribute("product", product);

        return "new_product";
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/edit/{id}")
    public String showEditProductPage(@PathVariable(name = "id") Long id, Model model) {

        Product product = service.get(id);
        model.addAttribute("product", product);

        return "edit_product";
    }

    // @ModelAttribute는  Form data (예: name=Laptop&brand=Samsung&madeIn=Korea&price=1000.00)를 Product 객체
    // @RequestBody는 HTTP 요청 본문에 포함된
    //  JSON 데이터(예: {"name": "Laptop", "brand": "Samsung", "madeIn": "Korea", "price": 1000.00})를 Product 객체에 매핑
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/save")
    public String saveProduct(@ModelAttribute("product") Product product, Model model) {
        if (product.getName() == null || product.getName().trim().isEmpty()) {
            model.addAttribute("error", "상품 이름은 반드시 입력해야 합니다.");
            model.addAttribute("product", product);
            return "new_product";
        }

        if (product.getBrand() == null || product.getBrand().trim().isEmpty()) {
            model.addAttribute("error", "브랜드는 반드시 입력해야 합니다.");
            model.addAttribute("product", product);
            return "new_product";
        }

        if (product.getMadeIn() == null || product.getMadeIn().trim().isEmpty()) {
            model.addAttribute("error", "원산지는 반드시 입력해야 합니다.");
            model.addAttribute("product", product);
            return "new_product";
        }

        if (product.getPrice() <= 0.0) {
            model.addAttribute("error", "가격은 0 이상이어야 합니다.");
            model.addAttribute("product", product);
            return "new_product";
        }
        // 통과 시 저장
        service.save(product);
        return "redirect:/products";
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/delete/{id}")
    public String deleteProduct(@PathVariable(name = "id") Long id) {

        service.delete(id);
        return "redirect:/products";
    }

    @GetMapping("/products")
    public String listProducts(Model model, @RequestParam(value = "login", required = false) String login) {
        if ("success".equals(login)) {
            model.addAttribute("loginSuccess", true); // 알림 플래그
        }
        List<Product> listProducts = service.listAll();
        model.addAttribute("listProducts", listProducts);
        return "index";
    }
}
