package kz.springboot.internetshop.controllers;

import kz.springboot.internetshop.entities.*;
import kz.springboot.internetshop.repositories.RoleRepository;
import kz.springboot.internetshop.repositories.SoldItemRepository;
import kz.springboot.internetshop.services.CommentService;
import kz.springboot.internetshop.services.ItemService;
import kz.springboot.internetshop.services.Repository;
import kz.springboot.internetshop.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

@Controller
public class AdminController {

    @Autowired
    private SoldItemRepository soldItemRepository;

    @Autowired
    private CommentService commentService;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private Repository repository;

    @Autowired
    private ItemService itemService;

    @Autowired
    private UserService userService;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @GetMapping(value = "/admin")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MODERATOR')")
    public String adminPanel(Model model){
        List<Country> countries = itemService.getAllCountries();
        model.addAttribute("countries", countries);
        return "adminPanel";
    }

    @GetMapping(value = "/brandspanel")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MODERATOR')")
    public String brandsPanel(Model model){
        List<Brand> brands = itemService.getAllBrands();
        model.addAttribute("brands", brands);

        List<Country> countries = itemService.getAllCountries();
        model.addAttribute("countries", countries);
        return "brandsPanel";
    }

    @GetMapping(value = "/productsPanel")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MODERATOR')")
    public String productsPanel(Model model){
        List<ShopItem> shopItems = itemService.getAllShopItems();
        model.addAttribute("shopItems", shopItems);

        List<Brand> brands = itemService.getAllBrands();
        model.addAttribute("brands", brands);
        return "productsPanel";
    }

    @GetMapping(value = "/categoriespanel")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MODERATOR')")
    public String CategoriesPanel(Model model){
        List<Categories> categories = itemService.listOfCategories();
        model.addAttribute("categories", categories);

        return "categoriesPanel";
    }

    @GetMapping(value = "/usersAdminPanel")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MODERATOR')")
    public String usersAdminPanel(Model model){
        List<Users> users = userService.listOfUsers();
        model.addAttribute("users", users);
        List<Role> roles = userService.listOfRoles();
        model.addAttribute("roles", roles);
        return "usersAdminPanel";
    }

    @GetMapping(value = "/rolesPanel")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MODERATOR')")
    public String rolesPanel(Model model){
        List<Role> roles = userService.listOfRoles();
        model.addAttribute("roles", roles);

        return "rolesPanel";
    }

    @GetMapping(value = "/categorydetails/{id}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MODERATOR')")
    public String CategoryDetails(Model model, @PathVariable(name = "id") Long id){
        Categories category = itemService.getCategory(id);
        model.addAttribute("category", category);
        return "categoriesDetails";
    }

    @GetMapping(value = "/countrydetails/{id}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MODERATOR')")
    public String CountryDetails(Model model, @PathVariable(name = "id") Long id){
//        Item item = repository.getItem(id);categorydetails
        Country country = itemService.getCountry(id);
        model.addAttribute("country", country);
        return "countryDetails";
    }

    @GetMapping(value = "/branddetails/{id}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MODERATOR')")
    public String BrandDetails(Model model, @PathVariable(name = "id") Long id){
        List<Country> countries = itemService.getAllCountries();
        model.addAttribute("countries", countries);

        Brand brand = itemService.getBrand(id);
        model.addAttribute("brand", brand);
        return "brandDetails";
    }

    @GetMapping(value = "/roledetails/{id}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MODERATOR')")
    public String roleDetails(Model model, @PathVariable(name = "id") Long id){
        Role role = userService.getRole(id);
        model.addAttribute(role);
        return "roleDetails";
    }

    @GetMapping(value = "/productdetails/{id}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MODERATOR')")
    public String ProductDetails(Model model, @PathVariable(name = "id") Long id){
        ShopItem item = itemService.getItem(id);
        model.addAttribute("item", item);

        List<Brand> brands = itemService.getAllBrands();
        model.addAttribute("brands", brands);

        List<Categories> categories = itemService.listOfCategories();
        model.addAttribute("categories", categories);

        List<Categories> itemCategories = item.getCategories();
        List<Categories> otherCategories = new ArrayList<>();

        for (Categories c : categories){
            otherCategories.add(c);
        }

        for(int i=0; i<itemCategories.size(); i++){
            for(int j=0; j<otherCategories.size(); j++){
                if(itemCategories.get(i) == otherCategories.get(j)){
                    otherCategories.remove(j);
                }
            }
        }

        List<Pictures> allPictures = itemService.getAllPictures();
        List<Pictures> pictures = new ArrayList<>();
        for(int i=0; i<allPictures.size(); i++){
            if(allPictures.get(i).getShopItem().getId() == id){
                pictures.add(allPictures.get(i));
            }
        }
        model.addAttribute("otherCategories", otherCategories);
        model.addAttribute("pictures", pictures);

        return "productDetails";
    }

    @GetMapping(value = "/userdetails/{id}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MODERATOR')")
    public String userDetails(Model model, @PathVariable(name = "id") Long id){
        Users user = userService.getUser(id);
        model.addAttribute("user", user);
        String password = passwordEncoder.encode(user.getPassword());
        return "userDetails";
    }

    @PostMapping(value = "/editDeleteCountry/{id}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MODERATOR')")
    public String editDelete(@PathVariable(name = "id") Long id,
                             @RequestParam(name = "name", defaultValue = "no name") String name,
                             @RequestParam(name = "code", defaultValue = "no code") String code,
                             @RequestParam(name = "btn_action") String action){
        if(action.equals("edit")){
            Country country = itemService.getCountry(id);
            if(country != null){
                country.setName(name);
                country.setCode(code);
                itemService.saveCountry(country);
            }
        }else if(action.equals("delete")){
            Country country = itemService.getCountry(id);
            if(country != null){
                itemService.deleteCountry(country);
            }
        }
        return "redirect:/admin";
    }

    @PostMapping(value = "/editDeleteBrand/{id}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MODERATOR')")
    public String editDeleteBrand(@PathVariable(name = "id") Long id,
                                  @RequestParam(name = "name", defaultValue = "no name") String name,
                                  @RequestParam(name = "country", defaultValue = "0") Long countryid,
                                  @RequestParam(name = "btn_action") String action){
        if(action.equals("edit")){
            Brand brand = itemService.getBrand(id);
            Country country = itemService.getCountry(countryid);
            if(brand != null){
                brand.setName(name);
                brand.setCountry(country);
                itemService.saveBrand(brand);
            }
        }else if(action.equals("delete")){
            Brand brand = itemService.getBrand(id);
            if(brand != null){
                itemService.deleteBrand(id);
            }
        }
        return "redirect:/brandspanel";
    }


    @PostMapping(value = "/addcountry")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MODERATOR')")
    public String addItem(Model model, @RequestParam (name = "name") String name,
                          @RequestParam (name = "code") String code){
        Country country = new Country();
        country.setName(name);
        country.setCode(code);
        itemService.addCountry(country);
        return "redirect:/admin";
    }

    @PostMapping(value = "/adduser")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    public String addUser(Model model,
                          @RequestParam (name = "full_name") String fullName,
                          @RequestParam (name = "email") String email,
                          @RequestParam (name = "password") String password,
                          @RequestParam (name = "re_password") String rePassword,
                          @RequestParam (name = "role") String role){
        if(password.equals(rePassword)){
            Users newUser = new Users();
            newUser.setFullname(fullName);
            newUser.setEmail(email);
            newUser.setPassword(password);
            ArrayList<Role> roles = new ArrayList<>();
            if(role.equals("ROLE_MODERATOR")){
                Role r1 = roleRepository.findByName("ROLE_USER");
                Role r2 = roleRepository.findByName("ROLE_MODERATOR");
                roles.add(r1);
                roles.add(r2);
                newUser.setRoles(roles);
                userService.saveUser(newUser);
                return "redirect:/usersAdminPanel?success";
            }else {
                if (userService.createUser(newUser) != null) {
                    return "redirect:/usersAdminPanel?success";
                }
            }

        }
        return "redirect:/usersAdminPanel?error";
    }

    @PostMapping(value = "/addbrand")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MODERATOR')")
    public String addBrand(Model model, @RequestParam (name = "name") String name,
                           @RequestParam (name = "country") Long countryid){
        Brand brand = new Brand();
        Country country = itemService.getCountry(countryid);
        brand.setName(name);
        brand.setCountry(country);
        itemService.addBrand(brand);
        return "redirect:/brandspanel";
    }

    @PostMapping(value = "/addrole")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    public String addRole(Model model,
                          @RequestParam (name = "name") String name,
                          @RequestParam (name = "description") String description){
       Role role = new Role();
       role.setName(name);
       role.setDescription(description);
       if(userService.createRole(role) != null){
           return "redirect:/rolesPanel";
       }
        return "redirect:/rolesPanel?error";
    }

    @PostMapping(value = "/additem")
    public String addItem(
            @RequestParam(name = "name") String name,
            @RequestParam(name = "description") String description,
            @RequestParam(name = "price", defaultValue = "0") int price,
            @RequestParam(name = "star", defaultValue = "0") int star,
            @RequestParam(name = "smallpicurl") String smallpicurl,
            @RequestParam(name = "largepicurl") String largepicurl,
            @RequestParam(name = "top_page") String top_page,
            @RequestParam(name = "brand", defaultValue = "0") Long id

    ){
        Brand brand = itemService.getBrand(id);
        if(brand != null) {
            boolean tp = true;
            if (top_page.equals("False")) {
                tp = false;
            }
            long millis = System.currentTimeMillis();
            java.sql.Date date = new java.sql.Date(millis);
            ShopItem shopItem = new ShopItem();
            shopItem.setName(name);
            shopItem.setDescription(description);
            shopItem.setPrice(price);
            shopItem.setStars(star);
            shopItem.setSmallPictureUrl(smallpicurl);
            shopItem.setLargePictureUrl(largepicurl);
            shopItem.setAddedDate(date);
            shopItem.setInTopPage(tp);
            shopItem.setBrand(brand );
            itemService.AddNewItem(shopItem);
        }
        return "redirect:/admin";
    }

    @PostMapping(value = "/addcategory")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MODERATOR')")
    public String addCategory(Model model, @RequestParam (name = "name") String name,
                           @RequestParam (name = "logoURL") String logoURL){
        Categories categories = new Categories();
        categories.setName(name);
        categories.setLogoURL(logoURL);
        itemService.addCategory(categories);
        return "redirect:/categoriespanel";
    }

    @PostMapping(value = "/assigncategory")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MODERATOR')")
    public String AssignCategory(@RequestParam(name = "item_id") Long itemID,
                                 @RequestParam(name = "category_id") Long categoryID){
        Categories category = itemService.getCategory(categoryID);
        if(category != null){
            ShopItem item = itemService.getItem(itemID);
            if(item != null){
                List<Categories> categories = item.getCategories();
                if(categories == null){
                    categories = new ArrayList<>();
                }
                categories.add(category);
                itemService.saveItem(item);

                return "redirect:/productdetails/" + item.getId();
            }
        }
        return "redirect:/admin";
    }

    @PostMapping(value = "/deleteitemcategory")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MODERATOR')")
    public String DeleteItemCategory(@RequestParam(name = "item_id") Long itemID,
                                 @RequestParam(name = "category_id") Long categoryID){
        Categories category = itemService.getCategory(categoryID);
        if(category != null){
            ShopItem item = itemService.getItem(itemID);
            if(item != null){
                List<Categories> categories = item.getCategories();
                for(int i=0; i< item.getCategories().size(); i++){
                    System.out.println(item.getCategories().get(i).getName());
                }

                for(int i=0; i< item.getCategories().size(); i++){
                    if( item.getCategories().get(i).getId().equals(category.getId())){
                        item.getCategories().remove(i);
                    }
                }
                itemService.saveItem(item);
                System.out.println("----------------");
                for(int i=0; i< item.getCategories().size(); i++){
                    System.out.println(item.getCategories().get(i).getName());
                }

                return "redirect:/productdetails/" + item.getId();
            }
        }
        return "redirect:/admin";
    }

    @PostMapping(value = "/editDeleteItem/{id}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    public String editDelete(@PathVariable(name = "id") Long id,
                             @RequestParam(name = "btn_action") String btn_action,
                             @RequestParam(name = "name", defaultValue = "no name") String name,
                             @RequestParam(name = "description", defaultValue = "no description") String description,
                             @RequestParam(name = "price", defaultValue = "0") int price,
                             @RequestParam(name = "star", defaultValue = "0") int star,
                             @RequestParam(name = "smallpicurl", defaultValue = "no small picture") String smallpicurl,
                             @RequestParam(name = "largepicurl", defaultValue = "no large picture") String largepicurl,
                             @RequestParam(name = "top_page", defaultValue = "false") String top_page,
                             @RequestParam(name = "brand") Long brandid){
        String redirect = "";
        if(btn_action.equals("delete")){
//            repository.deleteItem(id);editDeleteCategory
            ShopItem item = itemService.getItem(id);
            if(item != null){
                itemService.deleteItem(item);
            }
            redirect = "redirect:/productsPanel";
        }else if(btn_action.equals("edit")){
            boolean tp = true;
            if(top_page.equals("False")){
                tp = false;
            }
            ShopItem item = itemService.getItem(id);
            if(item != null){
                Brand brand = itemService.getBrand(brandid);
                if(brand!=null) {
                    long millis = System.currentTimeMillis();
                    java.sql.Date date = new java.sql.Date(millis);
                    item.setName(name);
                    item.setDescription(description);
                    item.setPrice(price);
                    item.setStars(star);
                    item.setSmallPictureUrl(smallpicurl);
                    item.setLargePictureUrl(largepicurl);
                    item.setAddedDate(date);
                    item.setInTopPage(tp);
                    item.setBrand(brand);
                    itemService.editItem(item);
                }
            }
//            repository.editItem(new Item(id, name, description, price, star, smallpicurl, largepicurl, null, tp));
            redirect = "redirect:/productsPanel";
        }
        return redirect;
    }

    @PostMapping(value = "/editDeleteCategory/{id}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MODERATOR')")
    public String EditDeleteCategory(@PathVariable(name = "id") Long id,
                             @RequestParam(name = "btn_action") String btn_action,
                             @RequestParam(name = "name", defaultValue = "no name") String name,
                             @RequestParam(name = "logoURL", defaultValue = "no description") String logoURL){
        String redirect = "";
        if(btn_action.equals("delete")){
            Categories categories = itemService.getCategory(id);
            if(categories != null){
                itemService.deleteCategory(categories);
            }
            redirect = "redirect:/categoriespanel";
        }else if(btn_action.equals("edit")){
            Categories categories = itemService.getCategory(id);
            if(categories != null){
                categories.setName(name);
                categories.setLogoURL(logoURL);
                itemService.saveCategory(categories);
            }
            redirect = "redirect:/categoriespanel";
        }
        return redirect;
    }

//    editDeleteUser
@PostMapping(value = "/editDeleteUser/{id}")
@PreAuthorize("hasAnyRole('ROLE_ADMIN')")
public String editDeleteUser(@PathVariable(name = "id") Long id,
                                 @RequestParam(name = "btn_action") String btn_action,
                                 @RequestParam(name = "user_full_name", defaultValue = "no name") String user_full_name,
                                 @RequestParam(name = "user_email", defaultValue = "no name") String user_email,
                                 @RequestParam(name = "user_password", defaultValue = "no name") String user_password){
    String redirect = "";
    if(btn_action.equals("delete")){
        Users user = userService.getUser(id);
        if(user != null){
            userService.deleteUser(user);
        }
        redirect = "redirect:/usersAdminPanel";
    }else if(btn_action.equals("edit")){
        Users user = userService.getUser(id);
        if(user != null){
            user.setFullname(user_full_name);
            user.setEmail(user_email);
            String encrypted_pass = passwordEncoder.encode(user_password);
            user.setPassword(encrypted_pass);
            userService.saveUser(user);
        }
        redirect = "redirect:/usersAdminPanel";
    }
    return redirect;
}

    @GetMapping(value = "/soldProductsPanel")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MODERATOR')")
    public String soldProductsPanel(HttpSession session, Model model){
        List<SoldItems> soldItems = itemService.getAllSoldItems();
        List<ShopItem> items = itemService.getAllShopItems();
        model.addAttribute("soldItems", soldItems);
        model.addAttribute("items", items);
        return "soldProductsPanel";
    }

    @GetMapping(value = "/commentspanel")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MODERATOR')")
    public String CommentsPanel(Model model){
        List<Comment> comments = commentService.getAllComments();
        List<Users> users = userService.listOfUsers();
        List<ShopItem> shopItems = itemService.getAllShopItems();
        model.addAttribute("comments", comments);
        model.addAttribute("users", users);
        model.addAttribute("shopItems", shopItems);
        return "commentsPanel";
    }

    @GetMapping(value = "/commentdetails/{id}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MODERATOR')")
    public String CommentsDetails(Model model,
                                  @PathVariable(name = "id") Long id){
        Comment comment = commentService.getComment(id);
        Users user = userService.getUser(comment.getAuthor().getId());
        ShopItem item = itemService.getItem(comment.getItem().getId());
        model.addAttribute("comment", comment);
        model.addAttribute("user", user);
        model.addAttribute("item", item);
        return "commentDetails";
    }

    @PostMapping(value = "/deleteuserscomment/{id}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MODERATOR')")
    public String deleteUsersComment(Model model,
                                  @PathVariable(name = "id") Long id){
        if(id != null) {
            Comment comment = commentService.getComment(id);
            commentService.deleteComment(comment);
            return "redirect:/commentspanel";
        }
        return "redirect:/403";
    }

    @PostMapping(value = "/editcomment/{id}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MODERATOR')")
    public String EditComment(Model model,
                              @PathVariable(name = "id") Long id,
                              @RequestParam(name = "comment") String comment){
        if(id != null) {
            Comment c = commentService.getComment(id);
            c.setComment(comment);
            commentService.addComment(c);
            return "redirect:/commentspanel";
        }
        return "redirect:/403";
    }
}
