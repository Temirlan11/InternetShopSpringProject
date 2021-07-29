package kz.springboot.internetshop.controllers;

import kz.springboot.internetshop.db.DBManager;
import kz.springboot.internetshop.db.Item;
import kz.springboot.internetshop.entities.*;
import kz.springboot.internetshop.repositories.ItemRepository;
import kz.springboot.internetshop.services.CommentService;
import kz.springboot.internetshop.services.ItemService;
import kz.springboot.internetshop.services.Repository;
import kz.springboot.internetshop.services.UserService;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.IOUtils;
import org.dom4j.rule.Mode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpCookie;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.parameters.P;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Controller
public class HomeController {

    @Autowired
    private ItemService itemService;

    @Autowired
    private CommentService commentService;

    @Autowired
    private UserService userService;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Value("${file.avatar.viewPath}")
    private String viewPath;

    @Value("${file.avatar.uploadPath}")
    private String uploadPath;

    @Value("${file.avatar.defaultPicture}")
    private String defaultPicture;

    @GetMapping(value = "/")
    public String index(Model model, HttpSession session){
        model.addAttribute("currentUser", getUserData());
        List<ShopItem> shopItems = itemService.listOfAllItems();
        model.addAttribute("shopItems", shopItems);
        List<Brand> brands = itemService.getAllBrands();
        model.addAttribute("brands", brands);
        List<ShopItem> basket = (ArrayList<ShopItem>)session.getAttribute("basket");
        if(basket == null){
            basket = new ArrayList<ShopItem>();
        }
        model.addAttribute("basket", basket);

        List<Categories> categories = itemService.listOfCategories();
        model.addAttribute("categories", categories);

        return "index";
    }

    @GetMapping(value = "/tops")
    public String tops(Model model, HttpSession session){
        model.addAttribute("currentUser", getUserData());
        List<ShopItem> shopItems = itemService.listOfTopPage(true);
        model.addAttribute("shopItems", shopItems);
        List<Brand> brands = itemService.getAllBrands();
        model.addAttribute("brands", brands);
        List<ShopItem> basket = (ArrayList<ShopItem>)session.getAttribute("basket");
        if(basket == null){
            basket = new ArrayList<ShopItem>();
        }
        model.addAttribute("basket", basket);
        return "index";
    }

    @GetMapping(value = "/search")
    public String Searching(Model model,
                            @RequestParam(value = "name") String name, HttpSession session){
        model.addAttribute("currentUser", getUserData());
        List<ShopItem> shopItems = itemService.getItemsByNameAsc(name);
        model.addAttribute("name", name);
        model.addAttribute("shopItems", shopItems);
        List<Brand> brands = itemService.getAllBrands();
        model.addAttribute("brands", brands);
        List<ShopItem> basket = (ArrayList<ShopItem>)session.getAttribute("basket");
        if(basket == null){
            basket = new ArrayList<ShopItem>();
        }
        model.addAttribute("basket", basket);
        return "detailedSearch";
    }

    @GetMapping(value = "/detailedSearch")
    public String detailedSearching(Model model,
                                    @RequestParam(value = "name") String name,
                                    @RequestParam(value = "price_from", defaultValue = "0") int price_from,
                                    @RequestParam(value = "price_to", defaultValue = "0") int price_to,
                                    @RequestParam(value = "options") String options,
                                    @RequestParam(value = "brand") Long brandid, HttpSession session){
        model.addAttribute("currentUser", getUserData());
        List<Brand> brands = itemService.getAllBrands();
        model.addAttribute("brands", brands);
        if(options.equals("asc")){
            if((price_from == 0) && (price_to == 0) && name == null && brandid != null){
                List<ShopItem> shopItems = itemService.getItemsByBrandId(brandid);
                model.addAttribute("shopItems", shopItems);
                Brand brand = itemService.getBrand(brandid);
                model.addAttribute("brandbyid", brand);
            }else if((price_from == 0) && (price_to == 0) && name != null && brandid != null){
                List<ShopItem> shopItems = itemService.getItemsByNameAsc(name);
                Brand brand = itemService.getBrand(brandid);

                model.addAttribute("name", name);
                model.addAttribute("brandbyid", brand);
                model.addAttribute("shopItems", shopItems);
            } else if((price_from != 0) && (price_to != 0) && name == null && brandid != null){
                List<ShopItem> shopItems = itemService.getItemsByPriceAndBrandAsc(brandid, price_from, price_to);
                Brand brand = itemService.getBrand(brandid);

                model.addAttribute("brandbyid", brand);
                model.addAttribute("shopItems", shopItems);
            }else if((price_from != 0) && (price_to != 0) && name != null && brandid != null){
                List<ShopItem> shopItems = itemService.getItemsByPriceAndBrandAsc(brandid, price_from, price_to);
                Brand brand = itemService.getBrand(brandid);

                model.addAttribute("name", name);
                model.addAttribute("brandbyid", brand);
                model.addAttribute("shopItems", shopItems);
            }

        }else if(options.equals("desc")){
            if((price_from == 0) && (price_to == 0) && name == null && brandid != null){
                List<ShopItem> shopItems = itemService.getItemsByBrandId(brandid);
                model.addAttribute("shopItems", shopItems);
                Brand brand = itemService.getBrand(brandid);
                model.addAttribute("brandbyid", brand);
            }else if((price_from == 0) && (price_to == 0) && name != null && brandid != null){
                List<ShopItem> shopItems = itemService.getItemsByNameDesc(name);
                Brand brand = itemService.getBrand(brandid);

                model.addAttribute("name", name);
                model.addAttribute("brandbyid", brand);
                model.addAttribute("shopItems", shopItems);
            } else if((price_from != 0) && (price_to != 0) && name == null && brandid != null){
                List<ShopItem> shopItems = itemService.getItemsByPriceAndBrandDesc(brandid, price_from, price_to);
                Brand brand = itemService.getBrand(brandid);

                model.addAttribute("brandbyid", brand);
                model.addAttribute("shopItems", shopItems);
            }else if((price_from != 0) && (price_to != 0) && name != null && brandid != null){
                List<ShopItem> shopItems = itemService.getItemsByPriceAndBrandDesc(brandid, price_from, price_to);
                Brand brand = itemService.getBrand(brandid);

                model.addAttribute("name", name);
                model.addAttribute("brandbyid", brand);
                model.addAttribute("shopItems", shopItems);
            }
        }
        List<ShopItem> basket = (ArrayList<ShopItem>)session.getAttribute("basket");
        if(basket == null){
            basket = new ArrayList<ShopItem>();
        }
        model.addAttribute("basket", basket);
        return "detailedSearch";
    }

    @GetMapping(value = "/detailedSearch/{id}")
    public String detailedSearchingBrand(Model model, @PathVariable(name = "id") Long brandid, HttpSession session){
        model.addAttribute("currentUser", getUserData());
        List<Brand> brands = itemService.getAllBrands();
        model.addAttribute("brands", brands);
        if(brandid != null) {
            List<ShopItem> shopItems = itemService.getItemsByBrandId(brandid);
            model.addAttribute("shopItems", shopItems);
            Brand brand = itemService.getBrand(brandid);
            model.addAttribute("brandbyid", brand);
        }
        List<ShopItem> basket = (ArrayList<ShopItem>)session.getAttribute("basket");
        if(basket == null){
            basket = new ArrayList<ShopItem>();
        }
        model.addAttribute("basket", basket);
//        else if(options.equals("asc")){
//            if((price_from == 0) && (price_to == 0)){
//                List<ShopItem> shopItems = itemService.getItemsByNameAsc(name);
//                model.addAttribute("name", name);
//                model.addAttribute("shopItems", shopItems);
//            }else{
//                List<ShopItem> shopItems = itemService.getItemsByPriceAsc(name, price_from, price_to);
//                model.addAttribute("name", name);
//                model.addAttribute("shopItems", shopItems);
//            }
//        }else if(options.equals("desc")){
//            if((price_from == 0) && (price_to == 0)){
//                System.out.println("HELLO DESC");
//                List<ShopItem> shopItems = itemService.getItemsByNameDesc(name);
//                model.addAttribute("name", name);
//                model.addAttribute("shopItems", shopItems);
//            }else{
//                List<ShopItem> shopItems = itemService.getItemsByPriceDesc(name, price_from, price_to);
//                model.addAttribute("name", name);
//                model.addAttribute("shopItems", shopItems);
//            }
//        }
        return "detailedSearch";
    }

    @GetMapping(value = "/details/{id}")
    public String details(Model model, @PathVariable(name = "id") Long id, HttpSession session){
        model.addAttribute("currentUser", getUserData());
        ShopItem item = itemService.getItem(id);
        model.addAttribute("item", item);
        List<Brand> brands = itemService.getAllBrands();
        model.addAttribute("brands", brands);
        List<Pictures> pictures = itemService.getAllPicturesByItemID(id);
        model.addAttribute("pictures", pictures);
        List<ShopItem> basket = (ArrayList<ShopItem>)session.getAttribute("basket");
        if(basket == null){
            basket = new ArrayList<ShopItem>();
        }
        model.addAttribute("basket", basket);

        List<Comment> comments = commentService.getCommentByItemId(id);
        model.addAttribute("comments", comments);
        return "details";
    }

    @GetMapping(value = "/403")
    public String accesDenied(Model model, HttpSession session){
        model.addAttribute("currentUser", getUserData());
        List<ShopItem> basket = (ArrayList<ShopItem>)session.getAttribute("basket");
        if(basket == null){
            basket = new ArrayList<ShopItem>();
        }
        model.addAttribute("basket", basket);
        return "403";
    }

    @GetMapping(value = "/login")
    public String login(Model model, HttpSession session){
        model.addAttribute("currentUser", getUserData());
        List<ShopItem> basket = (ArrayList<ShopItem>)session.getAttribute("basket");
        if(basket == null){
            basket = new ArrayList<ShopItem>();
        }
        model.addAttribute("basket", basket);

        List<Categories> categories = itemService.listOfCategories();
        model.addAttribute("categories", categories);

        return "login";
    }

    @GetMapping(value = "/profile")
    @PreAuthorize("isAuthenticated()")
    public String profile(Model model, HttpSession session){
        model.addAttribute("currentUser", getUserData());
        List<ShopItem> basket = (ArrayList<ShopItem>)session.getAttribute("basket");
        if(basket == null){
            basket = new ArrayList<ShopItem>();
        }
        model.addAttribute("basket", basket);
        return "profile";
    }

    @GetMapping(value = "/register")
    public String register(Model model, HttpSession session){
        model.addAttribute("currentUser", getUserData());
        List<ShopItem> basket = (ArrayList<ShopItem>)session.getAttribute("basket");
        if(basket == null){
            basket = new ArrayList<ShopItem>();
        }
        model.addAttribute("basket", basket);
        return "register";
    }

    @PostMapping(value = "/register")
    public String toRegister(@RequestParam(name = "user_full_name") String fullName,
                             @RequestParam(name = "user_email") String email,
                             @RequestParam(name = "user_password") String password,
                             @RequestParam(name = "re_user_password") String rePassword,
                             Model model, HttpSession session){
//        model.addAttribute("currentUser", getUserData());
        if(password.equals(rePassword)){
            Users newUser = new Users();
            newUser.setFullname(fullName);
            newUser.setEmail(email);
            newUser.setPassword(password);
            if(userService.createUser(newUser)!=null){
                return "redirect:/register?success";
            }

        }
        List<ShopItem> basket = (ArrayList<ShopItem>)session.getAttribute("basket");
        if(basket == null){
            basket = new ArrayList<ShopItem>();
        }
        model.addAttribute("basket", basket);
        return "redirect:/register?error";
    }

    @PostMapping(value = "/updateProfile")
    @PreAuthorize("isAuthenticated()")
    public String updateProfile(@RequestParam(name = "fullname", defaultValue = "No name") String fullname,
                                @RequestParam(name = "old_password", defaultValue = "no pass") String old_password,
                                @RequestParam(name = "new_password", defaultValue = "no pass") String new_password,
                                @RequestParam(name = "re_new_password", defaultValue = "no pass") String re_new_password,
                                @RequestParam(name = "btn_action") String btn_action,
                                Model model, HttpSession session){
        model.addAttribute("currentUser", getUserData());
        List<ShopItem> basket = (ArrayList<ShopItem>)session.getAttribute("basket");
        if(basket == null){
            basket = new ArrayList<ShopItem>();
        }
        model.addAttribute("basket", basket);
        if(getUserData() != null){
            Users user = getUserData();
            if(btn_action.equals("update_profile")){
                if(!fullname.equals("No name")) {
                    user.setFullname(fullname);
                    userService.saveUser(user);
                    return "redirect:/profile";
                }
            }else if(btn_action.equals("update_password")){
                if(!old_password.equals("no pass") || !new_password.equals("no pass") || !re_new_password.equals("no pass")){
                    String encrypted_old_pass = passwordEncoder.encode(old_password);
                    if(new_password.equals(re_new_password)){
                        if(user.getPassword().equals(encrypted_old_pass)){
                            String encrypted_new_pass = passwordEncoder.encode(new_password);
                            user.setPassword(encrypted_new_pass);
                            userService.saveUser(user);
                            return "redirect:/profile";
                        }
                    }
                }
            }
        }
        return null;
    }

    @PostMapping(value = "/updatepassword")
    @PreAuthorize("isAuthenticated()")
    public String updatePassword(@RequestParam(name = "old_password", defaultValue = "no pass") String old_password,
                                @RequestParam(name = "new_password", defaultValue = "no pass") String new_password,
                                @RequestParam(name = "re_new_password", defaultValue = "no pass") String re_new_password,
                                @RequestParam(name = "btn_action") String btn_action,
                                 Model model, HttpSession session){
        model.addAttribute("currentUser", getUserData());
        System.out.println("[][][][][][][] " + old_password);
        List<ShopItem> basket = (ArrayList<ShopItem>)session.getAttribute("basket");
        if(basket == null){
            basket = new ArrayList<ShopItem>();
        }
        model.addAttribute("basket", basket);
        if(new_password.equals(re_new_password)){
            System.out.println("[]-[]-[]-[]-[]-[]-[]");
            Users user = getUserData();
            System.out.println("[]-[]-[]-[getuser]-[]-[]-[]");
            String encrypted_old_pass = userService.encodePassword(old_password);
            System.out.println("[1]" + encrypted_old_pass);
            System.out.println("[2]" + user.getPassword());
            if(user.getPassword().equals(encrypted_old_pass)){
                System.out.println("[3]" + encrypted_old_pass);
                System.out.println("[4]" + user.getPassword());
                String encrypted_new_pass = userService.encodePassword(new_password);
                user.setPassword(encrypted_new_pass);
                userService.saveUser(user);
                return "redirect:/profile";
            }
        }
        return null;
    }

    @PostMapping(value = "/uploadavatar")
    @PreAuthorize("isAuthenticated()")
    public String uploadAvatar(@RequestParam(name = "user_ava") MultipartFile file, HttpSession session, Model model){
        List<ShopItem> basket = (ArrayList<ShopItem>)session.getAttribute("basket");
        if(basket == null){
            basket = new ArrayList<ShopItem>();
        }
        model.addAttribute("basket", basket);
        if(file.getContentType().equals("image/jpeg") || file.getContentType().equals("image/png") || file.getContentType().equals("image/jpg")) {
            try {
                Users currentUser = getUserData();
                String picName = DigestUtils.sha1Hex("avatar_" + currentUser.getId() + "_!Picture");

                byte[] bytes = file.getBytes();
                Path path = Paths.get(uploadPath + picName + ".jpg");
                Files.write(path, bytes);

                currentUser.setPictureURL(picName);
                userService.saveUser(currentUser);

                return "redirect:/profile";
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return "redirect:/";
    }

    @GetMapping(value = "/viewphoto/{url}", produces = {MediaType.IMAGE_JPEG_VALUE})
    @PreAuthorize("isAuthenticated()")
    public @ResponseBody byte[] viewProfilePhoto(@PathVariable(name = "url") String url, HttpSession session, Model model) throws IOException{
        List<ShopItem> basket = (ArrayList<ShopItem>)session.getAttribute("basket");
        if(basket == null){
            basket = new ArrayList<ShopItem>();
        }
        model.addAttribute("basket", basket);
        String pictureURL = viewPath + defaultPicture;

        if(url!= null){
            pictureURL = viewPath + url + ".jpg";
        }

        InputStream in;

        try{
            ClassPathResource resource = new ClassPathResource(pictureURL);
            in = resource.getInputStream();
        }catch (Exception e){
            ClassPathResource resource = new ClassPathResource(viewPath + defaultPicture);
            in = resource.getInputStream();
            e.printStackTrace();
        }
        return IOUtils.toByteArray(in);
    }

    @PostMapping(value = "/uploadpicture")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MODERATOR')")
    public String uploadPicture(@RequestParam(name = "item_picture") MultipartFile file,
                                @RequestParam(name = "id") Long id,
                                Model model, HttpSession session){
        if(file.getContentType().equals("image/jpeg") || file.getContentType().equals("image/png") || file.getContentType().equals("image/jpg")) {
            List<ShopItem> basket = (ArrayList<ShopItem>)session.getAttribute("basket");
            if(basket == null){
                basket = new ArrayList<ShopItem>();
            }
            model.addAttribute("basket", basket);
            ShopItem shopItem = itemService.getItem(id);
            try {
                Pictures picture = new Pictures();
                long millis = System.currentTimeMillis();
                java.sql.Date date = new java.sql.Date(millis);
                picture.setAddedDate(date);
                picture.setShopItem(shopItem);

                Pictures newPic = itemService.addPicture(picture);

                String picName = DigestUtils.sha1Hex("item_" + newPic.getId() + "_!Picture");

                byte[] bytes = file.getBytes();
                Path path = Paths.get(uploadPath + picName + ".jpg");
                Files.write(path, bytes);

                newPic.setUrl(picName);
                itemService.addPicture(newPic);

                return "redirect:/productdetails/" + id;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return "redirect:/";
    }

    @GetMapping(value = "/viewpicture/{url}", produces = {MediaType.IMAGE_JPEG_VALUE})
    @PreAuthorize("isAuthenticated()")
    public @ResponseBody byte[] viewPicture(@PathVariable(name = "url") String url, Model model, HttpSession session) throws IOException{
        List<ShopItem> basket = (ArrayList<ShopItem>)session.getAttribute("basket");
        if(basket == null){
            basket = new ArrayList<ShopItem>();
        }
        model.addAttribute("basket", basket);
        String pictureURL = viewPath + defaultPicture;

        if(url!= null){
            pictureURL = viewPath + url + ".jpg";
        }

        InputStream in;

        try{
            ClassPathResource resource = new ClassPathResource(pictureURL);
            in = resource.getInputStream();
        }catch (Exception e){
            ClassPathResource resource = new ClassPathResource(viewPath + defaultPicture);
            in = resource.getInputStream();
            e.printStackTrace();
        }
        return IOUtils.toByteArray(in);
    }

    @PostMapping(value = "/deleteitemspicture")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MODERATOR')")
    public String DeleteItemCategory(@RequestParam(name = "pic_id") Long picID,
                                     @RequestParam(name = "item_id") Long itemID, Model model, HttpSession session){
        List<ShopItem> basket = (ArrayList<ShopItem>)session.getAttribute("basket");
        if(basket == null){
            basket = new ArrayList<ShopItem>();
        }
        model.addAttribute("basket", basket);
//        Pictures picture = itemService.getPicture(picID);
//        itemService.deletePictureByItemId(picture);
        Pictures picture = itemService.getPicture(picID);
        itemService.deletePicture(picture);
        return "redirect:/productdetails/" + itemID;
    }

    @GetMapping(value = "/basket")
    public String BasketPage(HttpSession session, Model model){
        model.addAttribute("currentUser", getUserData());
        ArrayList<ShopItem> basket1 = (ArrayList<ShopItem>)session.getAttribute("basket");
        if(basket1 == null){
            basket1 = new ArrayList<>();
        }

        HashMap<Long, Integer> newBasket = new HashMap<>();
        for(ShopItem item : basket1){
            if(newBasket.containsKey(item.getId())){
                int oldCount = newBasket.get(item.getId());
                newBasket.put(item.getId(), oldCount + 1);
            }else {
                newBasket.put(item.getId(), 1);
            }
        }

        HashMap<ShopItem, Integer> basket = new HashMap<>();
        for(Long id : newBasket.keySet()){
            ShopItem item = itemService.getItem(id);
            int count = newBasket.get(id).intValue();
            basket.put(item, count);
        }

        int total = 0;
        for(int i=0; i<basket1.size(); i++){
            total += basket1.get(i).getPrice();
        }

        model.addAttribute("basket", basket);
        model.addAttribute("total", total);
        return "basket";
    }

    @GetMapping(value="/addtobasket")
    public String AddItemToBasket(@RequestParam(name = "item_id") Long id,
                                  HttpSession session, Model model){
        ShopItem item = itemService.getItem(id);

        model.addAttribute("currentUser", getUserData());
        ArrayList<ShopItem> basket1 = (ArrayList<ShopItem>)session.getAttribute("basket");
        if(basket1 == null){
            basket1 = new ArrayList<>();
        }
        basket1.add(item);

        session.setAttribute("basket", basket1);
        return "redirect:/";
    }

    @GetMapping(value="/appendtobasket")
    public String AppendItemToBasket(@RequestParam(name = "item_id") Long id,
                                  HttpSession session, Model model){
        ShopItem item = itemService.getItem(id);

        model.addAttribute("currentUser", getUserData());
        ArrayList<ShopItem> basket1 = (ArrayList<ShopItem>)session.getAttribute("basket");
        if(basket1 == null){
            basket1 = new ArrayList<>();
        }
        basket1.add(item);

        session.setAttribute("basket", basket1);
        return "redirect:/basket";
    }

    @GetMapping(value="/deletefrombasket")
    public String DeleteItemFromBasket(@RequestParam(name = "item_id") Long id,
                                     HttpSession session, Model model){
        ShopItem item = itemService.getItem(id);

        model.addAttribute("currentUser", getUserData());
        ArrayList<ShopItem> basket1 = (ArrayList<ShopItem>)session.getAttribute("basket");
        for(int i=0; i<basket1.size(); i++){
            if(basket1.get(i).getId() == item.getId()){
                basket1.remove(i);
                break;
            }
        }
        basket1.remove(item);

        session.setAttribute("basket", basket1);
        return "redirect:/basket";
    }

    @GetMapping(value="/clearbasket")
    public String DeleteItemFromBasket(HttpSession session, Model model){
        session.removeAttribute("basket");
        return "redirect:/basket";
    }

    @PostMapping(value = "/makepayment")
    public String MakePayment(HttpSession session, Model model,
                              @RequestParam(name = "fullName") String fullName,
                              @RequestParam(name = "cardNumber") String cardNumber,
                              @RequestParam(name = "expiration") String expiration,
                              @RequestParam(name = "cvv") String cvv){
        String redirect = "basket";
        if(fullName != null && cardNumber != null && expiration != null && cvv != null){
            if(cardNumber.length()==16 && expiration.length() == 5 && cvv.length() ==3){
                try{
                    int m = Integer.parseInt(expiration.substring(0,2));
                    int y = Integer.parseInt(expiration.substring(3,5));
                    int c = Integer.parseInt(cvv);

                    ArrayList<ShopItem> basket1 = (ArrayList<ShopItem>)session.getAttribute("basket");
                    if(basket1 == null){
                        basket1 = new ArrayList<>();
                    }

                    HashMap<Long, Integer> newBasket = new HashMap<>();
                    for(ShopItem item : basket1){
                        if(newBasket.containsKey(item.getId())){
                            int oldCount = newBasket.get(item.getId());
                            newBasket.put(item.getId(), oldCount + 1);
                        }else {
                            newBasket.put(item.getId(), 1);
                        }
                    }

                    HashMap<ShopItem, Integer> basket = new HashMap<>();
                    for(Long id : newBasket.keySet()){
                        ShopItem item = itemService.getItem(id);
                        int count = newBasket.get(id).intValue();
                        basket.put(item, count);
                    }

                    for(ShopItem item : basket.keySet()){
                        long millis = System.currentTimeMillis();
                        java.sql.Date date = new java.sql.Date(millis);
                        SoldItems soldItems = new SoldItems();
                        soldItems.setItem(item);
                        soldItems.setSoldDate(date);
                        soldItems.setQuantity(basket.get(item).intValue());
                        itemService.addSoldItems(soldItems);
                    }

                    session.removeAttribute("basket");

                    return  "redirect:basket";
                }catch (Exception e){
                    return "redirect:basket";
                }
            }
            return "redirect:basket";
        }
        return "redirect:basket";
    }

    @PostMapping(value = "/addcomment/{id}")
    @PreAuthorize("isAuthenticated()")
    public String AddComment(@RequestParam(name = "comment") String comment,
                              @PathVariable(name="id") Long id, Model model){
//        ShopItem item = itemService.getItem(id);

        List<ShopItem> items = itemService.getAllShopItems();
        ShopItem item = new ShopItem();
        for(ShopItem i : items){
            if(i.getId() == id){
                item = i;
            }
        }
        model.addAttribute("currentUser", getUserData());
        long millis = System.currentTimeMillis();
        java.sql.Date date = new java.sql.Date(millis);

        Comment newComment = new Comment();
        newComment.setComment(comment);
        newComment.setAuthor(getUserData());
        newComment.setAddedDate(date);
        newComment.setItem(item);

        commentService.addComment(newComment);

        return "redirect:/details/" + id;
    }

    @PostMapping(value = "/deletecomment")
    public String DeleteComment(@RequestParam(name = "comment_id") Long comment_id,
                                @RequestParam(name = "item_id") Long item_id,
                                Model model){
        if(comment_id != null && item_id != null) {
            Comment comment = commentService.getComment(comment_id);
            commentService.deleteComment(comment);
            return "redirect:/details/" + item_id;
        }
        return "redirect:/403";
    }

    private Users getUserData(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(!(authentication instanceof AnonymousAuthenticationToken)){
            User secUser = (User) authentication.getPrincipal();
            Users user = userService.getUserByEmail(secUser.getUsername());
            return user;
        }
        return null;
    }
}
