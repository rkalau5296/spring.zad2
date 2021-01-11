package pl.spring.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.web.bind.annotation.RestController;
import pl.spring.model.Basket;
import pl.spring.model.Product;
import java.util.ArrayList;
import java.util.List;

@RestController
public class BasketController {

    private final Basket basket;
    private List<Product> productList;

    @Autowired
    public BasketController(Basket basket) {
        this.basket = basket;
        addProductToBasket(basket);
    }


    public void addProductToBasket(Basket basket) {

        productList = new ArrayList<>();
        productList.add(new Product("Milk", basket.generateRandomPrice(50.00, 300.00)));
        productList.add(new Product("Bread", basket.generateRandomPrice(50.00, 300.00)));
        productList.add(new Product("Ham", basket.generateRandomPrice(50.00, 300.00)));
        productList.add(new Product("Cheese", basket.generateRandomPrice(50.00, 300.00)));
        productList.add(new Product("Apple", basket.generateRandomPrice(50.00, 300.00)));

        basket.setProductList(productList);
    }

    @EventListener(ApplicationReadyEvent.class)
        public void getBasketValue() {

            for (Product product : productList) {
                System.out.println(product.getName() + " " + product.getPrice());
            }
            System.out.println("Total basket value: " + basket.calculateValueBasket());
        }
}
