package com.furniture.inventory.config;

import com.furniture.inventory.entity.InhousePart;
import com.furniture.inventory.entity.OutsourcedPart;
import com.furniture.inventory.entity.Product;
import com.furniture.inventory.repository.PartRepository;
import com.furniture.inventory.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.HashSet;

/**
 * Initializes sample data for the furniture inventory application.
 * Adds sample parts and products only if the database is empty.
 *
 * @author Hardik
 * @version 1.0
 */
@Component
public class SampleDataInitializer implements CommandLineRunner {

    private final PartRepository partRepository;
    private final ProductRepository productRepository;

    @Autowired
    public SampleDataInitializer(PartRepository partRepository, ProductRepository productRepository) {
        this.partRepository = partRepository;
        this.productRepository = productRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        // Only initialize if both parts and products are empty
        if (partRepository.count() == 0 && productRepository.count() == 0) {
            initializeSampleData();
        }
    }

    /**
     * Creates sample furniture parts and products for demonstration
     */
    private void initializeSampleData() {
        System.out.println("Initializing sample furniture inventory data...");

        // Create Inhouse Parts
        InhousePart woodenLeg = new InhousePart("Wooden Leg", 15.99, 50, 101);
        woodenLeg.setMinInv(10);
        woodenLeg.setMaxInv(100);

        InhousePart tableTop = new InhousePart("Oak Table Top", 89.99, 25, 102);
        tableTop.setMinInv(5);
        tableTop.setMaxInv(50);

        InhousePart chairBack = new InhousePart("Chair Back Rest", 25.50, 40, 103);
        chairBack.setMinInv(15);
        chairBack.setMaxInv(80);

        InhousePart drawerSlider = new InhousePart("Drawer Slider", 8.75, 100, 104);
        drawerSlider.setMinInv(20);
        drawerSlider.setMaxInv(150);

        InhousePart cabinetDoor = new InhousePart("Cabinet Door", 45.25, 30, 105);
        cabinetDoor.setMinInv(8);
        cabinetDoor.setMaxInv(60);

        // Create Outsourced Parts
        OutsourcedPart cushion = new OutsourcedPart("Premium Cushion", 32.99, 60, "Comfort Plus Inc.");
        cushion.setMinInv(25);
        cushion.setMaxInv(120);

        OutsourcedPart fabric = new OutsourcedPart("Upholstery Fabric", 18.50, 75, "Textile Masters");
        fabric.setMinInv(30);
        fabric.setMaxInv(150);

        OutsourcedPart hardware = new OutsourcedPart("Hardware Kit", 12.99, 90, "Fasteners Unlimited");
        hardware.setMinInv(40);
        hardware.setMaxInv(200);

        OutsourcedPart varnish = new OutsourcedPart("Wood Varnish", 24.75, 45, "Finish Perfect Co.");
        varnish.setMinInv(15);
        varnish.setMaxInv(80);

        OutsourcedPart screws = new OutsourcedPart("Assembly Screws", 5.99, 200, "Build-Rite Supplies");
        screws.setMinInv(50);
        screws.setMaxInv(300);

        // Save all parts
        partRepository.save(woodenLeg);
        partRepository.save(tableTop);
        partRepository.save(chairBack);
        partRepository.save(drawerSlider);
        partRepository.save(cabinetDoor);
        partRepository.save(cushion);
        partRepository.save(fabric);
        partRepository.save(hardware);
        partRepository.save(varnish);
        partRepository.save(screws);

        // Create Products with associated parts
        Product diningTable = new Product("Dining Table", 299.99, 10);
        diningTable.setParts(new HashSet<>());
        diningTable.getParts().add(woodenLeg);
        diningTable.getParts().add(tableTop);
        diningTable.getParts().add(hardware);
        diningTable.getParts().add(varnish);

        Product officeChair = new Product("Office Chair", 189.50, 15);
        officeChair.setParts(new HashSet<>());
        officeChair.getParts().add(woodenLeg);
        officeChair.getParts().add(chairBack);
        officeChair.getParts().add(cushion);
        officeChair.getParts().add(fabric);

        Product bookshelf = new Product("Bookshelf", 159.75, 8);
        bookshelf.setParts(new HashSet<>());
        bookshelf.getParts().add(woodenLeg);
        bookshelf.getParts().add(cabinetDoor);
        bookshelf.getParts().add(hardware);
        bookshelf.getParts().add(varnish);

        Product dresser = new Product("Dresser", 349.99, 5);
        dresser.setParts(new HashSet<>());
        dresser.getParts().add(woodenLeg);
        dresser.getParts().add(tableTop);
        dresser.getParts().add(drawerSlider);
        dresser.getParts().add(cabinetDoor);
        dresser.getParts().add(hardware);

        Product coffeeTable = new Product("Coffee Table", 129.99, 12);
        coffeeTable.setParts(new HashSet<>());
        coffeeTable.getParts().add(woodenLeg);
        coffeeTable.getParts().add(tableTop);
        coffeeTable.getParts().add(hardware);
        coffeeTable.getParts().add(varnish);

        // Save all products
        productRepository.save(diningTable);
        productRepository.save(officeChair);
        productRepository.save(bookshelf);
        productRepository.save(dresser);
        productRepository.save(coffeeTable);

        System.out.println("Sample data initialization completed!");
        System.out.println("Created: 10 parts (5 inhouse, 5 outsourced) and 5 products");
    }
}