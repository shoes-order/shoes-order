package com.personal.domain;

import com.personal.domain.store.repository.StoreRepository;
import com.personal.domain.user.repository.UserRepository;
import com.personal.entity.product.Product;
import com.personal.entity.product.ProductType;
import com.personal.entity.store.Store;
import com.personal.entity.user.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;

import java.sql.PreparedStatement;
import java.util.*;

@SpringBootTest
public class BulkInsertTest {
    @Autowired
    private StoreRepository storeRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private static final String[] STREET_NAMES = {"Main", "High", "Broad", "Elm", "Maple", "Oak", "Pine", "Cedar", "Birch", "Walnut"};
    private static final Random RANDOM = new Random();

    @Test
    public void test() {
        User user = userRepository.findById(3L).orElse(new User());

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
                (PreparedStatement ps , Store store) -> {
                    ps.setString(1 , store.getName());
                    ps.setString(2 , store.getTel());
                    ps.setString(3 , store.getZip());
                    ps.setString(4 , store.getAddress());
                    ps.setString(5 , store.getAddressDetail());
                    ps.setBoolean(6 , store.isDeleted());
                    ps.setString(7 , store.getDescription());
                    ps.setLong(8 , store.getUser().getId());
                });
    }

    @Test
    public void test2() {
        List<Product> products = new ArrayList<>();
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

        for (long i = 300001L; i <= 600000L; i++) {
            Store store = storeRepository.findById(i).orElse(new Store());
            for (long j = 0L; j < 2L; j++) {
                String randomName = shoeNames.get(RANDOM.nextInt(shoeNames.size()));
                String randomCategory = categories.get(RANDOM.nextInt(categories.size()));
                String randomMaterial = materials.get(RANDOM.nextInt(materials.size()));

                Product product = Product.builder()
                        .store(store)
                        .type(ProductType.PRODUCT)
                        .name(randomName + RANDOM.nextInt(8))
                        .category(randomCategory + RANDOM.nextInt(5)) // 예시 카테고리
                        .material(randomMaterial  +RANDOM.nextInt(3)) // 예시 재료
                        .basePrice(RANDOM.nextLong(10000, 100000)) // 예시 기본 가격
                        .customPrice(RANDOM.nextLong(10000, 100000)) // 예시 커스텀 가격
                        .description("Description for product " + i)
                        .build();

                products.add(product);
            }
        }

        String sql = "INSERT INTO product (type , name , category , material , spacing , base_price , custom_price , is_sold , is_deleted , description , store_id) VALUES(? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ?)";

        jdbcTemplate.batchUpdate(sql,
                products,
                products.size(),
                (PreparedStatement ps , Product product) -> {
                    ps.setString(1 , product.getType().name());
                    ps.setString(2 , product.getName());
                    ps.setString(3 , product.getCategory());
                    ps.setString(4 , product.getMaterial());
                    ps.setLong(5 , 10L);
                    ps.setLong(6 , product.getBasePrice());
                    ps.setLong(7 , product.getCustomPrice());
                    ps.setBoolean(8 , true);
                    ps.setBoolean(9 , false);
                    ps.setString(10 , product.getDescription());
                    ps.setLong(11 , product.getStore().getId());
                });
    }
}
