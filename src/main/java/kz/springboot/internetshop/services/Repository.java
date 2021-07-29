package kz.springboot.internetshop.services;

import kz.springboot.internetshop.db.Item;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.ArrayList;

public interface Repository {

    void AddNewItem(Item item);
    ArrayList<Item> listOfAllItems();
    ArrayList<Item> listOfTopPage();
    Item getItem(Long id);
    void editItem(Item item);
    void deleteItem(Long id);
    ArrayList<Item> searchByName(String name);
    ArrayList<Item> searchByNameAndPriceFromandTo(String name, int price_from, int price_to);
    ArrayList<Item> searchByNameAndPriceFromandToASC(String name, int price_from, int price_to);
}
