/*package com.example.gsproject.config;

import com.example.gsproject.entity.Product;
import com.example.gsproject.repository.ProductRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ProductInitRunner implements CommandLineRunner {

    private final ProductRepository productRepository;

    public ProductInitRunner(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public void run(String... args) {
        if (productRepository.count() == 0) {
            productRepository.saveAll(List.of(
                    new Product("돼지양념칼집구이(800g/팩)", 7980, true, null, "pork_ribs.png"),
                    new Product("고흥 햇감자(1.5kg/박스)", 5900, true, null, "potato.png"),
                    new Product("광천 곱창김(5g*12봉)/2개/1세트", 7960, true, "1+1", "gim.png"),
                    new Product("5無 훈제오리 슬라이스(250g*2팩)", 7900, true, "1+1", "duck_slice.png"),
                    new Product("노르웨이 생연어 필렛(200g/팩)", 6980, true, "카드결제할인", "salmon.png"),
                    new Product("나이스 부채살 스테이크(200g)", 6980, true, null, "beef_steak.png"),
                    new Product("고당도 하미과 메론(대/1통)", 6900, true, null, "hamigua_melon.png"),
                    new Product("국산 생 블루베리(100g)/2개/1세트", 7920, true, null, "blueberry.png"),
                    new Product("수박(7~8kg/미만)", 17900, true, null, "watermelon.png"),
                    new Product("햇 초당옥수수(3입/망)", 4900, true, null, "corn.png"),
                    new Product("진짜 맛있는 고구마(1.2kg/봉)", 3980, true, null, "sweetpotato.png"),
                    new Product("제스프리 골드키위(9개/1세트)", 9900, true, "카드결제할인", "kiwi.png"),
                    new Product("머스크 메론(대/1통)", 7900, true, null, "muskmelon.png")
            ));
        }
    }

}

*/