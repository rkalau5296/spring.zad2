package pl.spring.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Component
public class Basket {

    private List<Product> productList;

    @Value("${main.vat-value}")
    private double vatValue;
    @Value("${main.discount-value}")
    private double discountValue;

    public double calculateValueBasket() {

        double result = productList.stream()
                .map(Product::getPrice)
                .reduce(0.00, Double::sum);

        return Math.round(((result + (result * vatValue) - (result * discountValue))) * 100.00)/100.00;
    }

    public double generateRandomPrice(double min, double max) {
        double random = new Random().nextDouble();
        return Math.round((min + (random * (max - min)))*100.00)/100.00;
    }

    public void addProductToBasket() {

        productList = new ArrayList<>();
        productList.add(new Product("Milk", generateRandomPrice(50.00, 300.00)));
        productList.add(new Product("Bread", generateRandomPrice(50.00, 300.00)));
        productList.add(new Product("Ham", generateRandomPrice(50.00, 300.00)));
        productList.add(new Product("Cheese", generateRandomPrice(50.00, 300.00)));
        productList.add(new Product("Apple", generateRandomPrice(50.00, 300.00)));

       setProductList(productList);
    }
}
