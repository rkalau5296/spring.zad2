package pl.spring.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
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

        if(vatValue == 0 || discountValue ==0)
        {
            return result;
        }
        return Math.round(result * vatValue * discountValue * 100.00)/100.00;
    }

    public double generateRandomPrice(double min, double max) {
        double random = new Random().nextDouble();
        return Math.round((min + (random * (max - min)))*100.00)/100.00;
    }
}
