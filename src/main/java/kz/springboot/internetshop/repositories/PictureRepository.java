package kz.springboot.internetshop.repositories;

import kz.springboot.internetshop.entities.Pictures;
import kz.springboot.internetshop.entities.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional
public interface PictureRepository  extends JpaRepository<Pictures, Long> {
    void deleteByShopItemId(Long id);
    List<Pictures> findAllById(Long id);
    List<Pictures> findAllByShopItemId(Long id);
}
