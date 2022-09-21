package cn.coding.com.productmanagement.service;

import cn.coding.com.productmanagement.dto.ProductDTO;
import cn.coding.com.productmanagement.model.Category;
import cn.coding.com.productmanagement.model.Product;
import cn.coding.com.productmanagement.repository.CategoryRepository;
import cn.coding.com.productmanagement.repository.ProductRepository;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class ProductService {

    private ProductRepository productRepository;
    private CategoryRepository categoryRepository;
    private ModelMapper modelMapper;

    @Autowired
    public ProductService(ProductRepository productRepository, CategoryRepository categoryRepository, ModelMapper modelMapper) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
        this.modelMapper = modelMapper;
    }

    public Page<ProductDTO> getAllProduct(Pageable pageable) {
        return productRepository.findAll(pageable)
                .map(x-> modelMapper.map(x, ProductDTO.class));
    }

    public void saveProduct(ProductDTO productDTO) {
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.LOOSE);
        Product product = modelMapper.map(productDTO, Product.class);
        Category category = categoryRepository.findByCategoryName(product.getCategory().getCategoryName());
        product.setCategory(category);
        productRepository.save(product);
    }

    public void deleteProduct(Integer id) {
        productRepository.deleteById(id);
    }

    public ProductDTO getProductById(Integer id) {
        Product product = productRepository.getReferenceById(id);
        return modelMapper.map(product, ProductDTO.class);
    }
}
