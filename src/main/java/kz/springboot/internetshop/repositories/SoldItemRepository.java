package kz.springboot.internetshop.repositories;

import kz.springboot.internetshop.entities.SoldItems;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public interface SoldItemRepository extends JpaRepository<SoldItems, Long> {
}
