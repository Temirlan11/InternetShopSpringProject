package kz.springboot.internetshop.services.implementations;

import kz.springboot.internetshop.db.DBManager;
import kz.springboot.internetshop.db.Item;
import kz.springboot.internetshop.services.Repository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class RepositoryImpl implements Repository {
    @Override
    public void AddNewItem(Item item) {
        DBManager.addShopItem(item);
    }

    @Override
    public ArrayList<Item> listOfAllItems() {
        return DBManager.getAllShopItems();
    }

    @Override
    public ArrayList<Item> listOfTopPage() {
        ArrayList<Item> items = listOfAllItems();
        ArrayList<Item> tops = new ArrayList<>();
        for(int i=0; i<items.size(); i++){
            if(items.get(i).isInTopPage() == true){
                tops.add(items.get(i));
            }
        }
        return tops;
    }

    @Override
    public Item getItem(Long id) {
        Item item = DBManager.getItem(id);
        return item;
    }

    @Override
    public void editItem(Item item) {
        DBManager.editItem(item);
    }

    @Override
    public void deleteItem(Long id) {
        DBManager.deleteItem(id);
    }

    @Override
    public ArrayList<Item> searchByName(String name) {
        ArrayList<Item> items = listOfAllItems();
        ArrayList<Item> results = new ArrayList<>();
        for(Item i : items){
            if(i.getName().contains(name)){
                results.add(i);
            }
        }
        return results;
    }

    @Override
    public ArrayList<Item> searchByNameAndPriceFromandTo(String name, int price_from, int price_to) {
        ArrayList<Item> items = listOfAllItems();
        ArrayList<Item> results = new ArrayList<>();
        for(Item i : items){
            if(i.getName().contains(name)){
                if((i.getPrice() >= price_from) && (i.getPrice() <= price_to)){
                    results.add(i);
                }
            }
        }
        return results;
    }

    @Override
    public ArrayList<Item> searchByNameAndPriceFromandToASC(String name, int price_from, int price_to) {
        ArrayList<Item> items = listOfAllItems();
        ArrayList<Item> results = new ArrayList<>();
        for(Item i : items){
            if(i.getName().contains(name)){
                if((i.getPrice() >= price_from) && (i.getPrice() <= price_to)){
                    for(int j=0; j<results.size(); j++){
                        if(results.get(j).CompareTo(i) == 1){
                            Item itm = i;

                        }else if(results.get(j).CompareTo(i) == -1){

                        }
                    }
                    results.add(i);
                }
            }
        }
        return results;
    }


}
