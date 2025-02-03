package org.example;

import org.openqa.selenium.*;
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

public class PeriPlusAddItemTest {

    private WebDriver driver;
    private WebDriverWait wait;
    private static final Logger logger = Logger.getLogger(PeriPlusAddItemTest.class.getName());

    @BeforeTest
    public void setUp() {
        logger.info("Setting up WebDriver");
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--remote-allow-origins=*");
        driver = new ChromeDriver(options);
        wait = new WebDriverWait(driver, Duration.ofSeconds(60));
    }

    @Test
    public void testAddToCart() {
        try {
            login("yudiputrasabri789@gmail.com", "Yudisabri123*");
            navigateToProductPage();
            addToCart();
            verifyProductInCart();
        } catch (Exception e) {
            logger.severe("Test failed: " + e.getMessage());
            Assert.fail("Test encountered an error: " + e.getMessage());
        }
    }

    private void login(String email, String password) {
        driver.get("https://www.periplus.com/account/Login");
        logger.info("Navigated to the Periplus login page");

        WebElement usernameField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("email")));
        WebElement passwordField = driver.findElement(By.name("password"));
        WebElement submitButton = driver.findElement(By.id("button-login"));

        usernameField.sendKeys(email);
        passwordField.sendKeys(password);
        submitButton.click();

        // Verify login success
        logger.info("Login successful");
        wait.until(ExpectedConditions.urlContains("/_index_/index"));
        logger.info("Login successful. Current URL: " + driver.getCurrentUrl());
        Assert.assertTrue(driver.getCurrentUrl().contains("/_index_/index"), "Login failed, URL does not contain 'account'");
    }

    private void navigateToProductPage() {
        driver.get("https://www.periplus.com/p/9798330457908/naruto?filter_name=naruto&filter_category_id=0");
        logger.info("Opened product page: Naruto");
    
        // Verify product title
        WebElement titleElement = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("h2[style*='border-bottom-style: dotted']")));
        String titleText = titleElement.getText().trim();
        Assert.assertEquals(titleText, "Naruto", "Product title does not match!");
        logger.info("Verified product title: " + titleText);
    
        // Verify publisher
        WebElement publisherElement = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".quickview-binding")));
        String publisherText = publisherElement.getText().trim();
        Assert.assertEquals(publisherText, "Paperback - -", "Publisher does not match!");
        logger.info("Verified publisher: " + publisherText);
    
        // Verify author
        WebElement authorElement = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".quickview-author a")));
        String authorText = authorElement.getText().trim();
        Assert.assertEquals(authorText, "Davis, John", "Author does not match!");
        logger.info("Verified author: " + authorText);
    }

    private void addToCart() {
        WebElement addToCartButton = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector(".btn-add-to-cart")));
        executeClick(addToCartButton);
        logger.info("Clicked 'Add to Cart' button");

        verifySuccessModal();
    }

    private void verifySuccessModal() {
        try {
            WebElement successModal = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("#Notification-Modal.show .modal-text")));
            String modalText = successModal.getText();
            Assert.assertEquals(modalText, "Success add to cart", "Unexpected modal message: " + modalText);
            logger.info("Product successfully added to cart");
        } catch (TimeoutException e) {
            Assert.fail("Success modal did not appear after adding to cart.");
        }
    }

    private void verifyProductInCart() {
        driver.get("https://www.periplus.com/checkout/cart");
        logger.info("Navigated to the cart page");

        WebElement cartProduct = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".product-name a")));
        Assert.assertNotNull(cartProduct, "The product was not found in the cart.");
        Assert.assertEquals(cartProduct.getText().trim(), "Naruto", "Cart product name does not match");

        logger.info("Verified product is present in the cart");
    }

    private void executeClick(WebElement element) {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("arguments[0].click();", element);
    }

    @AfterTest
    public void tearDown() {
        if (driver != null) {
            driver.quit();
            logger.info("Closed the browser");
        }
    }
}
