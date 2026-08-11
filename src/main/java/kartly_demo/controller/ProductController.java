package kartly_demo.controller;

import kartly_demo.entity.ProductEntity;
import kartly_demo.repository.ProductRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductController {
    private final ProductRepository productRepository;

    public ProductController(ProductRepository productRepository){
        this.productRepository = productRepository;
    }

    @GetMapping
    public List<ProductEntity> getAllProducts(){
        return productRepository.findAll();
    }

    @PostMapping
    public ProductEntity createProduct(@RequestBody ProductEntity product){
        return productRepository.save(product);
    }

}
