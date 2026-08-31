package kartly_demo.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import kartly_demo.entity.ProductEntity;
import kartly_demo.service.ProductService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/products")
@Tag(name = "Products", description = "Product catalog browsing and creation")
public class ProductController {
    private final ProductService productService;

    public ProductController(ProductService productService){
        this.productService = productService;
    }

    @Operation(summary = "Get all products in the catalog")
    @GetMapping
    public List<ProductEntity> getAllProducts(){
        return productService.getAllProducts();
    }

    @Operation(summary = "Add a new product to the catalog")
    @PostMapping
    public ProductEntity createProduct(@RequestBody ProductEntity product){
        return productService.createProduct(product);
    }

}