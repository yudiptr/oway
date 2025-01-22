package org.example;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.time.Duration;
import java.util.logging.Logger;

// For selenium version < 4, use this import
// import io.github.bonigarcia.wdm.WebDriverManager;

public class PeriPlusAddItemTest {

    private WebDriver driver;
    private WebDriverWait wait;
    private static final Logger logger = Logger.getLogger(PeriPlusAddItemTest.class.getName());



    @BeforeTest
    public void setUp() {
        // For Chrome < 112
        // WebDriverManager.chromedriver().setup();

        // For Chrome > 112, path to chromedriver for specific version, download manual through chrome
        logger.info("Setting up the WebDriver");
        System.setProperty("webdriver.chrome.driver", "C:\\chromedriver-win64\\chromedriver.exe");
        ChromeOptions options = new ChromeOptions();

        options.addArguments("--remote-allow-origins=*");
      
        driver = new ChromeDriver(options);
        wait = new WebDriverWait(driver, Duration.ofSeconds(60));
    }

    @Test
    public void testAddToCart() {
        try {
            // Login to Periplus
            driver.get("https://www.periplus.com/account/Login");
            logger.info("Navigated to the Periplus login page");

            WebElement usernameField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("email")));
            usernameField.sendKeys("yudiputrasabri789@gmail.com");
            WebElement passwordField = driver.findElement(By.name("password"));
            passwordField.sendKeys("Yudisabri123*");
            WebElement submitButton = driver.findElement(By.id("button-login"));
            submitButton.click();
            logger.info("Logged in to Periplus");

            // Login Success, Find Naruto Product
            WebElement searchBox = wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("filter_name")));
            logger.info("Login successful. Current URL: " + driver.getCurrentUrl());

            searchBox.sendKeys("Naruto");
            WebElement searchButton = driver.findElement(By.xpath("//button[@type='submit']"));
            JavascriptExecutor js = (JavascriptExecutor) driver;
            js.executeScript("arguments[0].click();", searchButton);
            logger.info("Search Naruto Book");

            // Getting first naruto product, check to cart
            WebElement firstProduct = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".row.row-category-grid .single-product")));
            Assert.assertNotNull(firstProduct, "Product search failed: No products found.");

            WebElement productLinkElement = firstProduct.findElement(By.cssSelector(".product-img a"));
            String productLink = productLinkElement.getDomAttribute("href");
            driver.get(productLink);
            logger.info("Getting the first product with link : "+ productLink);


            logger.info("Adding the book to cart");
            WebElement addToCartButton = driver.findElement(By.cssSelector(".btn-add-to-cart"));
            js = (JavascriptExecutor) driver;
            wait.until(ExpectedConditions.elementToBeClickable(addToCartButton));
            js.executeScript("arguments[0].click();", addToCartButton);

            try {
                WebElement successModal = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("#Notification-Modal.show .modal-text")));
                String modalText = successModal.getText();
                Assert.assertEquals(modalText, "Success add to cart", "Unexpected modal message: " + modalText);
            } catch (TimeoutException e) {
                Assert.fail("Success modal did not appear after adding to cart.");
            }

            // Verify the product in the cart
            driver.get("https://www.periplus.com/checkout/cart");
            logger.info("Navigated to the cart page");

            WebElement cartProduct = wait
            .until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".product-name a")));
            Assert.assertNotNull(cartProduct, "The product was not found in the cart.");
            logger.info("The product is present in the cart: " + cartProduct.getText());
        } catch (Exception e) {
            logger.severe("Error during search and add product to cart: " + e.getMessage());
        }
    }

    @AfterTest
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}
