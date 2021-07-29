package kz.springboot.internetshop.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ManyToAny;

import javax.persistence.*;
import java.sql.Date;
import java.util.List;

@Entity
@Table(name = "t_items")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ShopItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name", length = 200)
    private String name;

    @Column(name = "description", length = 200)
    private String description;

    @Column(name = "price")
    private int price;

    @Column(name = "stars")
    private int stars;

    @Column(name = "smallPictureUrl", length = 200)
    private String smallPictureUrl;

    @Column(name = "largePictureUrl", length = 200)
    private String largePictureUrl;

    @Column(name = "addedDate")
    private Date addedDate;

    @Column(name = "inTopPage")
    private boolean inTopPage;

    @ManyToOne(fetch = FetchType.EAGER)
    private Brand brand;

    @ManyToMany(fetch = FetchType.EAGER)
    private List<Categories> categories;
}