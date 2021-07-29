package kz.springboot.internetshop.db;

import java.util.ArrayList;

public class DBManager {
    private static ArrayList<Item> items = new ArrayList<>();
    private static Long id = 7L;

    static {
        long millis = System.currentTimeMillis();
        java.sql.Date date = new java.sql.Date(millis);
    }

    public static void addShopItem(Item shopItem){
        long millis = System.currentTimeMillis();
        java.sql.Date date = new java.sql.Date(millis);
        shopItem.setId(id);
        shopItem.setAddedDate(date);
        items.add(shopItem);
        id++;
    }

    public static Item getItem(Long id){
        for(Item i : items){
            if(i.getId() == id)
                return i;
        }
        return null;
    }

    public static ArrayList<Item> getAllShopItems(){
        return items;
    }

    public static void deleteItem(Long id){
        for(int i=0; i<items.size(); i++){
            if(items.get(i).getId() == id){
                items.remove(i);
                break;
            }
        }
    }

    public static void editItem(Item item){
        long millis = System.currentTimeMillis();
        java.sql.Date date = new java.sql.Date(millis);
        item.setAddedDate(date);
        for(Item i : items){
            if(i.getId() == item.getId()){
                i.setName(item.getName());
                i.setDescription(item.getDescription());
                i.setPrice(item.getPrice());
                i.setStars(item.getStars());
                i.setSmallPictureUrl(item.getSmallPictureUrl());
                i.setLargePictureUrl(item.getLargePictureUrl());
                i.setInTopPage(item.isInTopPage());
            }

        }
    }

}
