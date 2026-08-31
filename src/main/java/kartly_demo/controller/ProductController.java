package kartly_demo.controller;

import kartly_demo.entity.ProductEntity;
import kartly_demo.service.ProductService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductController {
    private final ProductService productService;

    public ProductController(ProductService productService){
        this.productService = productService;
    }

    @GetMapping
    public List<ProductEntity> getAllProducts(){
        return productService.getAllProducts();
    }

    @PostMapping
    public ProductEntity createProduct(@RequestBody ProductEntity product){
        return productService.createProduct(product);
    }

}