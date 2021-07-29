package kz.springboot.internetshop.services;

import kz.springboot.internetshop.db.Item;
import kz.springboot.internetshop.entities.*;

import java.util.List;

public interface ItemService {
    void AddNewItem(ShopItem shopItem);
    List<ShopItem> getAllShopItems();
    List<ShopItem> listOfAllItems();
    List<ShopItem> listOfTopPage(boolean bool);
    ShopItem getItem(Long id);
    ShopItem saveItem(ShopItem shopItem);
    void editItem(ShopItem shopItem);
    void deleteItem(ShopItem shopItem);
    List<ShopItem> getItemsByNameAsc(String name);
    List<ShopItem> getItemsByNameDesc(String name);
    List<ShopItem> getItemsByPriceAsc(String name, int from, int to);
    List<ShopItem> getItemsByPriceDesc(String name, int from, int to);

    List<Country> getAllCountries();
    Country addCountry(Country country);
    Country saveCountry(Country country);
    Country getCountry(Long id);
    void deleteCountry(Country country);

    List<Brand> getAllBrands();
    Brand addBrand(Brand brand);
    Brand saveBrand(Brand brand);
    Brand getBrand(Long id);
    void deleteBrand(Long id);

    List<ShopItem> getItemsByBrandId(Long id);
    List<ShopItem> getItemsByBrandIdAsc(Long id);
    List<ShopItem> getItemsByBrandIdDesc(Long id);
    List<ShopItem> getItemsByPriceAndBrandAsc(Long id, int from, int to);
    List<ShopItem> getItemsByPriceAndBrandDesc(Long id, int from, int to);

    List<Categories> listOfCategories();
    Categories getCategory(Long id);
    Categories saveCategory(Categories category);
    Categories addCategory(Categories category);
    void deleteCategory(Categories category);
    Categories deleteItemsCategory(Categories category);

    ShopItem deleteItemCategory(ShopItem shopItem);

    Pictures getPicture(Long id);
    Pictures addPicture(Pictures picture);
    Pictures savePicture(Pictures pictures);
    List<Pictures> getAllPictures();
    List<Pictures> getAllPicturesByID(Long id);
    List<Pictures> getAllPicturesByItemID(Long id);
    void deletePictureByItemId(Pictures picture);
    void deletePicture(Pictures picture);

    SoldItems addSoldItems(SoldItems soldItems);
    List<SoldItems> getAllSoldItems();
}
