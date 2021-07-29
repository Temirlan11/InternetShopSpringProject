package kz.springboot.internetshop.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.sql.Date;

@Entity
@Table(name = "t_sold_items")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SoldItems {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "quantity")
    private int quantity;

    @Column(name = "soldDate")
    private Date soldDate;

    @ManyToOne(fetch = FetchType.EAGER)
    private ShopItem item;
}
