package com.personal.domain;

import com.personal.domain.owner.repository.OwnerRepository;
import com.personal.domain.store.repository.StoreRepository;
import com.personal.entity.product.Product;
import com.personal.entity.product.ProductType;
import com.personal.entity.store.Store;
import com.personal.entity.user.User;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Transactional;

import java.sql.PreparedStatement;
import java.util.*;
import java.util.stream.Collectors;

@SpringBootTest
class BulkInsertTest {
    private static final String[] STREET_NAMES = {"Main", "High", "Broad", "Elm", "Maple", "Oak", "Pine", "Cedar", "Birch", "Walnut"};
    private static final Random RANDOM = new Random();

    @Autowired
    private StoreRepository storeRepository;

    @Autowired
    private OwnerRepository ownerRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Nested
    class StoreInsertTest {

        @Test
        void insertStores() {
            User user = ownerRepository.findById(1L).orElse(new User());

            List<Store> stores = new ArrayList<>();

            for (int i = 0; i < 200000; i++) {
                String randomStreet = STREET_NAMES[RANDOM.nextInt(STREET_NAMES.length)];
                String randomName = "Store " + UUID.randomUUID().toString().substring(0, 8); // UUID로 랜덤 이름 생성
                String randomTel = "010-" + (RANDOM.nextInt(9000) + 1000) + "-" + (RANDOM.nextInt(9000) + 1000);
                String randomZip = String.valueOf(RANDOM.nextInt(90000) + 10000); // 70000부터 159999 사이의 랜덤 ZIP 코드
                String randomAddressDetail = "random " + RANDOM.nextInt(10000); // 0부터 999 사이의 랜덤 아파트 번호

                Store store = Store.builder()
                        .name(randomName)
                        .tel(randomTel)
                        .zip(randomZip)
                        .address(RANDOM.nextInt(1000) + " " + randomStreet + " Street") // 0부터 999 사이의 랜덤 거리 주소
                        .addressDetail(randomAddressDetail)
                        .description("Description for store " + i)
                        .user(user)
                        .build();

                stores.add(store);
            }

            String sql = "INSERT INTO store (name , tel , zip , address , address_detail , is_deleted , description , user_id) VALUES(? , ? , ? , ? , ? , ? , ? , ?)";

            jdbcTemplate.batchUpdate(sql,
                    stores,
                    stores.size(),
                    (PreparedStatement ps, Store store) -> {
                        ps.setString(1, store.getName());
                        ps.setString(2, store.getTel());
                        ps.setString(3, store.getZip());
                        ps.setString(4, store.getAddress());
                        ps.setString(5, store.getAddressDetail());
                        ps.setBoolean(6, store.isDeleted());
                        ps.setString(7, store.getDescription());
                        ps.setLong(8, store.getUser().getId());
                    });
        }
    }

    @Nested
    class ProductInsertTest {

        @Test
        void generateRandomProducts() {
            // 50개의 Store ID를 미리 정의
            List<Long> storeIds = List.of(
                    1L, 2L, 3L, 4L, 5L, 6L, 7L, 8L, 9L, 10L,
                    11L, 12L, 13L, 14L, 15L, 16L, 17L, 18L, 19L, 20L,
                    21L, 22L, 23L, 24L, 25L, 26L, 27L, 28L, 29L, 30L,
                    31L, 32L, 33L, 34L, 35L, 36L, 37L, 38L, 39L, 40L,
                    41L, 42L, 43L, 44L, 45L, 46L, 47L, 48L, 49L, 50L
            );

            // `storeRepository.findAllById`를 사용하여 한 번에 로드
            List<Store> stores = storeRepository.findAllById(storeIds);
            if (stores.isEmpty()) {
                throw new IllegalStateException("No stores found for the given IDs!");
            }

            // Store ID를 Key로, Store 객체를 Value로 하는 Map 생성
            Map<Long, Store> storeMap = stores.stream()
                    .collect(Collectors.toMap(Store::getId, store -> store));

            List<String> shoeNames = List.of(
                    "Air Max 90", "Yeezy Boost 350", "Stan Smith", "Superstar", "Chuck Taylor All Star",
                    "Old Skool", "Classic Slip-On", "Gel-Kayano", "Wave Rider", "Pegasus 39",
                    "Ultraboost", "Pureboost", "FuelCell Rebel", "ZoomX Vaporfly", "Metcon 8",
                    "GT-1000", "Trail Glove", "Fresh Foam 1080", "Ghost 15", "Clifton 9",
                    "Bondi 8", "Speedgoat 5", "Targhee III", "Moab 2", "Altra Lone Peak",
                    "Huarache", "Cortez", "Jordan 1", "LeBron 19", "Kyrie Infinity"
            );
            List<String> categories = List.of(
                    "Running Shoes", "Casual Sneakers", "Basketball Shoes", "Walking Shoes", "Training Shoes",
                    "Trail Shoes", "Lifestyle Shoes", "Performance Shoes", "Outdoor Shoes", "Athletic Shoes"
            );
            List<String> materials = List.of(
                    "Leather", "Synthetic Leather", "Mesh", "Knit Fabric", "Rubber", "Suede",
                    "Canvas", "Nylon", "Polyurethane", "EVA Foam"
            );

            Random random = new Random();
            List<Product> products = new ArrayList<>();

            for (int i = 0; i < 800000; i++) {
                long randomStoreId = storeIds.get(random.nextInt(storeIds.size()));
                Store store = storeMap.get(randomStoreId);

                String randomName = shoeNames.get(random.nextInt(shoeNames.size())) + random.nextInt(8);
                String randomCategory = categories.get(random.nextInt(categories.size())) + random.nextInt(5);

                Product product = Product.builder()
                        .store(store)
                        .type(ProductType.PRODUCT)
                        .name(randomName)
                        .category(randomCategory)
                        .material(materials.get(random.nextInt(materials.size())))
                        .basePrice(random.nextLong(10000, 100000))
                        .customPrice(random.nextLong(10000, 100000))
                        .description("Description for product " + i)
                        .build();

                products.add(product);

                if (products.size() == 10000) {
                    batchInsertProducts(products);
                    products.clear();
                }
            }

            if (!products.isEmpty()) {
                batchInsertProducts(products);
            }
        }

        @Test
        void insertMaterialProducts() {
            List<Long> storeIds = List.of(
                    1L, 2L, 3L, 4L, 5L, 6L, 7L, 8L, 9L, 10L,
                    11L, 12L, 13L, 14L, 15L, 16L, 17L, 18L, 19L, 20L,
                    21L, 22L, 23L, 24L, 25L, 26L, 27L, 28L, 29L, 30L,
                    31L, 32L, 33L, 34L, 35L, 36L, 37L, 38L, 39L, 40L,
                    41L, 42L, 43L, 44L, 45L, 46L, 47L, 48L, 49L, 50L
            );

            List<Store> stores = storeRepository.findAllById(storeIds);
            if (stores.isEmpty()) {
                throw new IllegalStateException("No stores found for the given IDs!");
            }

            Map<Long, Store> storeMap = stores.stream()
                    .collect(Collectors.toMap(Store::getId, store -> store));

            List<String> materials = List.of(
                    "Leather", "Synthetic Leather", "Mesh", "Knit Fabric", "Rubber", "Suede",
                    "Canvas", "Nylon", "Polyurethane", "EVA Foam"
            );

            Random random = new Random();

            List<Product> materialProducts = materials.stream()
                    .map(material -> Product.builder()
                            .store(storeMap.get(storeIds.get(random.nextInt(storeIds.size()))))
                            .type(ProductType.MATERIAL)
                            .name(material)
                            .category("Raw Material")
                            .material(material)
                            .basePrice(random.nextLong(1000, 5000))
                            .customPrice(random.nextLong(2000, 8000))
                            .description("Material: " + material)
                            .build())
                    .collect(Collectors.toList());

            batchInsertProducts(materialProducts);
        }
    }

    @Transactional
    void batchInsertProducts(List<Product> products) {
        String sql = "INSERT INTO product (type, name, category, material, spacing, base_price, custom_price, is_sold, is_deleted, description, store_id) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        jdbcTemplate.batchUpdate(sql,
                products,
                products.size(),
                (PreparedStatement ps, Product product) -> {
                    ps.setString(1, product.getType().name());
                    ps.setString(2, product.getName());
                    ps.setString(3, product.getCategory());
                    ps.setString(4, product.getMaterial());
                    ps.setLong(5, 10L);
                    ps.setLong(6, product.getBasePrice());
                    ps.setLong(7, product.getCustomPrice());
                    ps.setBoolean(8, true);
                    ps.setBoolean(9, false);
                    ps.setString(10, product.getDescription());
                    ps.setLong(11, product.getStore().getId());
                });
    }
}
