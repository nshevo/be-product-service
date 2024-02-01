package com.nml.port.config;

import com.nml.core.domain.model.Category;
import com.nml.core.domain.model.Product;
import com.nml.core.domain.service.interfaces.ICategoryRepository;
import com.nml.core.domain.service.interfaces.IProductRepository;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Profile("!test")
@Component
public class MyCommandLineRunner implements CommandLineRunner {

    @Autowired
    private IProductRepository productRepository;

    @Autowired
    private ICategoryRepository categoryRepository;

    @Autowired
    EntityManager entityManager;

    private Category fruits, vegetables, meat, dairy, snacks, bakery;

    @Override
    public void run(String... args) {
        createCategories();
        createProducts();
    }

    private void createCategories() {
        meat = Category.builder().build();
        meat.setName("Meat");
        meat.setDescription("Meat products");
        meat.setDeleted(false);

        fruits = Category.builder().build();
        fruits.setName("Fruits");
        fruits.setDescription("Fruit products");
        fruits.setDeleted(false);

        vegetables = Category.builder().build();
        vegetables.setName("Vegetables");
        vegetables.setDescription("Vegetable products");
        vegetables.setDeleted(false);

        dairy = Category.builder().build();
        dairy.setName("Dairy");
        dairy.setDescription("Dairy products");
        dairy.setDeleted(false);

        snacks = Category.builder().build();
        snacks.setName("Snacks");
        snacks.setDescription("Snack products");
        snacks.setDeleted(false);

        bakery = Category.builder().build();
        bakery.setName("Bakery");
        bakery.setDescription("Bakery products");

        categoryRepository.save(meat);
        categoryRepository.save(fruits);
        categoryRepository.save(vegetables);
        categoryRepository.save(dairy);
        categoryRepository.save(snacks);
        categoryRepository.save(bakery);

    }

    private void createProducts() {
        Product metaMeat = Product.builder().build();
        metaMeat.setName("Meta Meat by Mark's");
        //metaMeat.addCategory(meat);
        metaMeat.setDescription("Perfect for cooking or eating raw, finest meta meat");
        metaMeat.setDetails("price per 1 kilo");
        metaMeat.setQuantity(50);
        metaMeat.setImagelink("imagelinkhastobehere");
        metaMeat.setPrice(99.999);
        metaMeat.setDeleted(false);

        Product explosiveOranges = Product.builder().build();
        explosiveOranges.setName("Explosive Oranges");
        //explosiveOranges.addCategory(fruits);
        explosiveOranges.setDescription("Experience the thrill of biting into our explosive oranges. Be prepared for a burst of flavor!");
        explosiveOranges.setDetails("Sold per piece");
        explosiveOranges.setQuantity(100);
        explosiveOranges.setImagelink("imagelinkhastobehere");
        explosiveOranges.setPrice(5.99);
        explosiveOranges.setDeleted(false);

        Product misshapenPotatoes = Product.builder().build();
        misshapenPotatoes.setName("Misshapen Potatoes");
        //misshapenPotatoes.addCategory(vegetables);
        misshapenPotatoes.setDescription("Embrace imperfection with our misshapen potatoes. Each potato is unique and brings character to your meals.");
        misshapenPotatoes.setDetails("Price per kilo");
        misshapenPotatoes.setQuantity(200);
        misshapenPotatoes.setImagelink("imagelinkhastobehere");
        misshapenPotatoes.setPrice(2.49);
        misshapenPotatoes.setDeleted(false);

        Product mooliciousBeef = Product.builder().build();
        mooliciousBeef.setName("Moo-licious Beef");
       // mooliciousBeef.addCategory(meat);
        mooliciousBeef.setDescription("Taste the magic of our Moo-licious Beef. It's so tender and juicy that the cows themselves recommend it!");
        mooliciousBeef.setDetails("Price per 100 grams");
        mooliciousBeef.setQuantity(30);
        mooliciousBeef.setImagelink("imagelinkhastobehere");
        mooliciousBeef.setPrice(12.99);
        mooliciousBeef.setDeleted(false);

        Product quirkyCheese = Product.builder().build();
        quirkyCheese.setName("Quirky Cheese");
        //quirkyCheese.addCategory(dairy);
        quirkyCheese.setDescription("Discover the quirkiest cheese in town. Each bite is an adventure as it changes flavors from mild to wild!");
        quirkyCheese.setDetails("Price per 200 grams");
        quirkyCheese.setQuantity(40);
        quirkyCheese.setImagelink("imagelinkhastobehere");
        quirkyCheese.setPrice(7.99);
        quirkyCheese.setDeleted(false);

        Product cloudLikeBaguettes = Product.builder().build();
        cloudLikeBaguettes.setName("Cloud-like Baguettes");
        //cloudLikeBaguettes.addCategory(bakery);
        cloudLikeBaguettes.setDescription("Indulge in our cloud-like baguettes. They are so light and fluffy; you'll feel like you're biting into a dream!");
        cloudLikeBaguettes.setDetails("Price per piece");
        cloudLikeBaguettes.setQuantity(60);
        cloudLikeBaguettes.setImagelink("imagelinkhastobehere");
        cloudLikeBaguettes.setPrice(3.49);
        cloudLikeBaguettes.setDeleted(false);

        Product crispyAirChips = Product.builder().build();
        crispyAirChips.setName("Crispy Air Chips");
        //crispyAirChips.addCategory(snacks);
        crispyAirChips.setDescription("Experience the lightness of our crispy air chips. They're so airy; you might think you're eating a cloud!");
        crispyAirChips.setDetails("Price per bag");
        crispyAirChips.setQuantity(80);
        crispyAirChips.setImagelink("imagelinkhastobehere");
        crispyAirChips.setPrice(4.99);
        crispyAirChips.setDeleted(false);

        Product avocadoNinja = Product.builder().build();
        avocadoNinja.setName("Avocado Ninja");
        //avocadoNinja.addCategory(fruits);
        avocadoNinja.setDescription("Unleash your inner ninja with our Avocado Ninja. Its green power will fuel you with energy and stealth!");
        avocadoNinja.setDetails("Price per piece");
        avocadoNinja.setQuantity(90);
        avocadoNinja.setImagelink("imagelinkhastobehere");
        avocadoNinja.setPrice(2.99);
        avocadoNinja.setDeleted(false);

        Product magicalZucchini = Product.builder().build();
        magicalZucchini.setName("Magical Zucchini");
        //magicalZucchini.addCategory(vegetables);
        magicalZucchini.setDescription("Enter the world of magic with our magical zucchini. Each bite may transport you to a different dimension!");
        magicalZucchini.setDetails("Price per kilo");
        magicalZucchini.setQuantity(150);
        magicalZucchini.setImagelink("imagelinkhastobehere");
        magicalZucchini.setPrice(3.99);
        magicalZucchini.setDeleted(false);

        Product sizzlingBaconSymphony = Product.builder().build();
        sizzlingBaconSymphony.setName("Sizzling Bacon Symphony");
        //sizzlingBaconSymphony.addCategory(meat);
        sizzlingBaconSymphony.setDescription("Immerse yourself in the sizzling sounds and savory notes of our Bacon Symphony. It's music to your taste buds!");
        sizzlingBaconSymphony.setDetails("Price per 100 grams");
        sizzlingBaconSymphony.setQuantity(50);
        sizzlingBaconSymphony.setImagelink("imagelinkhastobehere");
        sizzlingBaconSymphony.setPrice(9.99);
        sizzlingBaconSymphony.setDeleted(false);

        productRepository.save(metaMeat);
        productRepository.save(explosiveOranges);
        productRepository.save(misshapenPotatoes);
        productRepository.save(mooliciousBeef);
        productRepository.save(quirkyCheese);
        productRepository.save(cloudLikeBaguettes);
        productRepository.save(crispyAirChips);
        productRepository.save(avocadoNinja);
        productRepository.save(magicalZucchini);
        productRepository.save(sizzlingBaconSymphony);

    }

}
