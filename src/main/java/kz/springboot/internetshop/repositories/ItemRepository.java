package kz.springboot.internetshop.repositories;

import kz.springboot.internetshop.entities.Brand;
import kz.springboot.internetshop.entities.ShopItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional
public interface ItemRepository extends JpaRepository<ShopItem, Long> {

    List<ShopItem> findAllByNameContainingOrderByPriceAsc(String name);

    List<ShopItem> findAllByNameContainingOrderByPriceDesc(String name);

    List<ShopItem> findAllByNameContainingAndPriceBetweenOrderByPriceAsc(String name, int from, int to);
    List<ShopItem> findAllByNameContainingAndPriceBetweenOrderByPriceDesc(String name, int from, int to);
//
    List<ShopItem> findAllByInTopPageEquals(boolean bool);
//
//    List<ShopItem> findAllByPriceGreaterThanAndPriceLessThanOrderOrderByPriceAsc(int price);

    List<ShopItem> findAllByPriceGreaterThanOrderByPriceAsc(int price);

    ShopItem findByIdEquals(Long id);

    List<ShopItem> findAllByBrandEquals(Brand brand);
    List<ShopItem> findAllByBrandEqualsOrderByNameAsc(Brand brand);
    List<ShopItem> findAllByBrandEqualsOrderByNameDesc(Brand brand);
    List<ShopItem> findAllByBrandEqualsAndPriceBetweenOrderByPriceAsc(Brand brand, int from, int to);
    List<ShopItem> findAllByBrandEqualsAndPriceBetweenOrderByPriceDesc(Brand brand, int from, int to);


}
