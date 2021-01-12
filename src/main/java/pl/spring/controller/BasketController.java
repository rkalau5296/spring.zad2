package pl.spring.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.spring.model.Basket;
import pl.spring.model.Product;

import java.util.List;

@RestController
public class BasketController {

    private final Basket basket;

    @Autowired
    public BasketController(Basket basket) {
        this.basket = basket;
        basket.addProductToBasket();
    }

    @GetMapping(value ="/getProductList")
    public List<Product> getProductList() {
        return basket.getProductList();
    }
    @GetMapping(value ="/calculateValueBasket")
    public Double calculateValueBasket() {
        return basket.calculateValueBasket();
    }

    @EventListener(ApplicationReadyEvent.class)
        public void getBasketValue() {

            for (Product product : basket.getProductList()) {
                System.out.println(product.getName() + " " + product.getPrice());
            }
            System.out.println("Total basket value: " + basket.calculateValueBasket());
        }
}
