package com.exercie.exercies.dao;

import com.exercie.exercies.model.Product;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class ProductDaoImpl implements ProductDao{
    Logger logger = LoggerFactory.getLogger(ProductDaoImpl.class);
    private final NamedParameterJdbcTemplate jdbcTemplate;

    @Autowired
    public ProductDaoImpl(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }


    private static final String SQL_FIND_ALL = """
                SELECT id, name, description, price, stock, category_id, created_at, updated_at
                FROM products
            """;
    private static final String SQL_FIND_ALL_BY_CATEGORY_ID = """
                SELECT id, name, description, price, stock, category_id, created_at, updated_at
                FROM products
                WHERE category_id = :categoryId
            """;
    private static final String SQL_FIND_BY_ID = """
                SELECT id, name, description, price, stock, category_id, created_at, updated_at
                FROM products as p
                WHERE p = :id
            """;

    private static final String SQL_INSERT = """
            INSERT INTO products (name, description, price, stock, category_id) 
            VALUES (:name, :description, :price, :stock, :categoryId)
            """;



    public List<Product> findAll(){
        logger.info("{}", SQL_FIND_ALL);
        return jdbcTemplate.query(SQL_FIND_ALL, productRowMapper);
    }

    @Override
    public List<Product> findAllByCategoryId(Long id) {
        Map<String, Object> params = new HashMap<>();
        params.put("categoryId", id);
        return jdbcTemplate.query(SQL_FIND_ALL_BY_CATEGORY_ID, params, productRowMapper);
    }

    public Optional<Product> findById(Long id){
        try{
            Map<String, Object> params = new HashMap<>();
            params.put("id", id);
            Product product = jdbcTemplate.queryForObject(SQL_FIND_BY_ID, params, productRowMapper);
            return Optional.ofNullable(product);
        } catch (Exception e) {
            return Optional.empty();
        }
    }


    @Override
    public void saveDao(Product product) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("name", product.getName());
        params.addValue("description", product.getDescription());
        params.addValue("price", product.getPrice());
        params.addValue("stock", product.getStock());
        params.addValue("categoryId", product.getCategoryId());
        jdbcTemplate.update(SQL_INSERT, params, keyHolder, new String[]{"id"});
        product.setId(keyHolder.getKey().longValue());
    }

    @Override
    public void update(Product product) {

    }

    @Override
    public void delete(Product product) {

    }


    private final RowMapper<Product> productRowMapper = (rs, rowNum) -> {
        // SELECT id, name, description, price, stock, category_id, created_at, updated_at
        Product product = new Product();
        product.setId(rs.getLong("id"));
        product.setName(rs.getString("name"));
        product.setDescription(rs.getString("description"));
        product.setPrice(rs.getBigDecimal("price"));
        product.setStock(rs.getInt("stock"));
        product.setCategoryId(rs.getLong("category_id"));

        if (rs.getTimestamp("created_at") != null){
            product.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
        }
        if (rs.getTimestamp("updated_at") != null) {
            product.setUpdatedAt(rs.getTimestamp("updated_at").toLocalDateTime());
        }
        return product;
    };

}
