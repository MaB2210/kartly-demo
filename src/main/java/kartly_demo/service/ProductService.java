package kartly_demo.service;

import kartly_demo.entity.ProductEntity;
import kartly_demo.repository.ProductRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;

    public List<ProductEntity> getAllProducts(){
        return productRepository.findAll();
    }

    public ProductEntity createProduct(ProductEntity product){
        return productRepository.save(product);
    }
}
