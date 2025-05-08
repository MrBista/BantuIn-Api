package com.exercie.exercies.dao;

import com.exercie.exercies.model.Category;
import com.exercie.exercies.model.Product;
import com.exercie.exercies.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Repository
public class CategoryDaoImpl implements CategoryDao{
    private final NamedParameterJdbcTemplate jdbcTemplate;
    private final ProductDao productDao;
    private final CategoryRepository categoryRepository;


    @Autowired
    public CategoryDaoImpl(NamedParameterJdbcTemplate jdbcTemplate, ProductDao productDao, CategoryRepository categoryRepository) {
        this.jdbcTemplate = jdbcTemplate;
        this.productDao = productDao;
        this.categoryRepository = categoryRepository;
    }


    private final String SQL_SELECT_ALL = """
                SELECT id, name, description, created_at
                FROM categories
            """;



    @Override
    public Optional<Category> findById(Long id) {

        return Optional.empty();

    }

    @Override
    public List<Category> findAllCategory() {
        return jdbcTemplate.query(SQL_SELECT_ALL, categoryRowMapper);
    }

    @Override
    public Optional<Category> findByIdWithProducts(Long id) {
        try{
            Optional<Category> findCategory = categoryRepository.findById(id);

            if (findCategory.isEmpty()){
                throw new SQLException("Category not found");
            }


            Category category = findCategory.get();
            List<Product> allProducts = productDao.findAllByCategoryId(category.getId());
            category.setProducts(allProducts);


            return Optional.of(category);
        } catch (Exception e) {
            return Optional.empty();
        }


    }

    private final RowMapper<Category> categoryRowMapper = (rs, rowNum) -> {
        Category category = new Category();
        category.setId(rs.getLong("id"));
        category.setName(rs.getString("name"));
        category.setDescription(rs.getString("description"));
        category.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
        return category;
    };
}
