package kz.springboot.internetshop.db;

import java.sql.Date;

public class Item {
    private Long id;
    private String name;
    private String description;
    private int price;
    private int stars;
    private String smallPictureUrl;
    private String largePictureUrl;
    private Date addedDate;
    private boolean inTopPage;

    public Item(){}

    public Item(Long id, String name, String description, int price, int stars, String smallPictureUrl, String largePictureUrl, Date addedDate, boolean inTopPage) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.stars = stars;
        this.smallPictureUrl = smallPictureUrl;
        this.largePictureUrl = largePictureUrl;
        this.addedDate = addedDate;
        this.inTopPage = inTopPage;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getStars() {
        return stars;
    }

    public void setStars(int stars) {
        this.stars = stars;
    }

    public String getSmallPictureUrl() {
        return smallPictureUrl;
    }

    public void setSmallPictureUrl(String smallPictureUrl) {
        this.smallPictureUrl = smallPictureUrl;
    }

    public String getLargePictureUrl() {
        return largePictureUrl;
    }

    public void setLargePictureUrl(String largePictureUrl) {
        this.largePictureUrl = largePictureUrl;
    }

    public Date getAddedDate() {
        return addedDate;
    }

    public void setAddedDate(Date addedDate) {
        this.addedDate = addedDate;
    }

    public boolean isInTopPage() {
        return inTopPage;
    }

    public void setInTopPage(boolean inTopPage) {
        this.inTopPage = inTopPage;
    }

    public int CompareTo(Item item){
        if(this.price > item.getPrice()){
            return 1;
        }else if(this.price < item.getPrice()){
            return -1;
        }
        return 0;
    }
}
