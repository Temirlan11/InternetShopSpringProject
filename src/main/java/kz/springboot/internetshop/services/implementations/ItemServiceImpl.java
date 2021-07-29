package kz.springboot.internetshop.services.implementations;

import kz.springboot.internetshop.entities.*;
import kz.springboot.internetshop.repositories.*;
import kz.springboot.internetshop.services.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ItemServiceImpl implements ItemService {

    @Autowired
    private SoldItemRepository soldItemRepository;

    @Autowired
    private PictureRepository pictureRepository;

    @Autowired
    private CountryRepository countryRepository;

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private BrandRepository brandRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Override
    public void AddNewItem(ShopItem shopItem) {
        itemRepository.save(shopItem);
    }

    @Override
    public List<ShopItem> getAllShopItems() {
        return itemRepository.findAll();
    }

    @Override
    public List<ShopItem> listOfAllItems() {
        return itemRepository.findAllByPriceGreaterThanOrderByPriceAsc(0);
    }

    @Override
    public List<ShopItem> listOfTopPage(boolean bool) {
        return itemRepository.findAllByInTopPageEquals(bool);
    }

    @Override
    public ShopItem getItem(Long id) {
        return itemRepository.getOne(id);
    }

    @Override
    public ShopItem saveItem(ShopItem shopItem) {
        return itemRepository.save(shopItem);
    }

    @Override
    public void editItem(ShopItem shopItem) {
        itemRepository.save(shopItem);
    }

    @Override
    public void deleteItem(ShopItem shopItem) {
        itemRepository.delete(shopItem);
    }

    @Override
    public List<ShopItem> getItemsByNameAsc(String name) {
        return itemRepository.findAllByNameContainingOrderByPriceAsc(name);
    }

    @Override
    public List<ShopItem> getItemsByNameDesc(String name) {
        return itemRepository.findAllByNameContainingOrderByPriceDesc(name);
    }

    @Override
    public List<ShopItem> getItemsByPriceAsc(String name, int from, int to) {
        return itemRepository.findAllByNameContainingAndPriceBetweenOrderByPriceAsc(name, from, to);
    }

    @Override
    public List<ShopItem> getItemsByPriceDesc(String name, int from, int to) {
        return itemRepository.findAllByNameContainingAndPriceBetweenOrderByPriceDesc(name, from, to);
    }

    @Override
    public List<Country> getAllCountries() {
        return countryRepository.findAll();
    }

    @Override
    public Country addCountry(Country country) {
        return countryRepository.save(country);
    }

    @Override
    public Country saveCountry(Country country) {
        return countryRepository.save(country);
    }

    @Override
    public Country getCountry(Long id) {
        return countryRepository.getOne(id);
    }

    @Override
    public void deleteCountry(Country country) {
        countryRepository.delete(country);
    }

    @Override
    public List<Brand> getAllBrands() {
        return brandRepository.findAll();
    }

    @Override
    public Brand addBrand(Brand brand) {
        return brandRepository.save(brand);
    }

    @Override
    public Brand saveBrand(Brand brand) {
        return brandRepository.save(brand);
    }

    @Override
    public Brand getBrand(Long id) {
        return brandRepository.getOne(id);
    }

    @Override
    public void deleteBrand(Long id) {
        Brand brand = getBrand(id);
        brandRepository.delete(brand);
    }

    @Override
    public List<ShopItem> getItemsByBrandId(Long id) {
        Brand brand = getBrand(id);
        return itemRepository.findAllByBrandEquals(brand);
    }

    @Override
    public List<ShopItem> getItemsByBrandIdAsc(Long id) {
        Brand brand = getBrand(id);
        return itemRepository.findAllByBrandEqualsOrderByNameAsc(brand);
    }

    @Override
    public List<ShopItem> getItemsByBrandIdDesc(Long id) {
        Brand brand = getBrand(id);
        return itemRepository.findAllByBrandEqualsOrderByNameDesc(brand);
    }

    @Override
    public List<ShopItem> getItemsByPriceAndBrandAsc(Long id, int from, int to) {
        Brand brand = getBrand(id);
        return itemRepository.findAllByBrandEqualsAndPriceBetweenOrderByPriceAsc(brand, from, to);
    }

    @Override
    public List<ShopItem> getItemsByPriceAndBrandDesc(Long id, int from, int to) {
        Brand brand = getBrand(id);
        return itemRepository.findAllByBrandEqualsAndPriceBetweenOrderByPriceDesc(brand, from, to);
    }

    @Override
    public List<Categories> listOfCategories() {
        return categoryRepository.findAll();
    }

    @Override
    public Categories getCategory(Long id) {
        return categoryRepository.getOne(id);
    }

    @Override
    public Categories saveCategory(Categories category) {
        return categoryRepository.save(category);
    }

    @Override
    public Categories addCategory(Categories category) {
        return categoryRepository.save(category);
    }

    @Override
    public void deleteCategory(Categories category) {
        categoryRepository.delete(category);
    }

    @Override
    public Categories deleteItemsCategory(Categories category) {
        return null;
    }

    @Override
    public ShopItem deleteItemCategory(ShopItem shopItem) {
        return null;
    }

    @Override
    public Pictures getPicture(Long id) {
        return pictureRepository.getOne(id);
    }

    @Override
    public Pictures addPicture(Pictures picture) {
        return pictureRepository.save(picture);
    }

    @Override
    public Pictures savePicture(Pictures pictures) {
        return pictureRepository.save(pictures);
    }

    @Override
    public List<Pictures> getAllPictures() {
        return pictureRepository.findAll();
    }

    @Override
    public List<Pictures> getAllPicturesByID(Long id) {
        return null;
    }

    @Override
    public List<Pictures> getAllPicturesByItemID(Long id) {
        return pictureRepository.findAllByShopItemId(id);
    }

    @Override
    public void deletePictureByItemId(Pictures picture) {
        pictureRepository.deleteByShopItemId(picture.getShopItem().getId());
    }

    @Override
    public void deletePicture(Pictures picture) {
        pictureRepository.delete(picture);
    }

    @Override
    public SoldItems addSoldItems(SoldItems soldItems) {
        return soldItemRepository.save(soldItems);
    }

    @Override
    public List<SoldItems> getAllSoldItems() {
        return soldItemRepository.findAll();
    }
}
