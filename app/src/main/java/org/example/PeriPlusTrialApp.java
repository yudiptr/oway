/*
 * This source file was generated by the Gradle 'init' task
 */
package org.example;

import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

// For selenium version < 4, use this import
// import io.github.bonigarcia.wdm.WebDriverManager;

public class PeriPlusTrialApp {
    public static void main(String[] args){
        System.setProperty("webdriver.chrome.driver", "C:\\chromedriver-win64\\chromedriver.exe");


        ChromeOptions options = new ChromeOptions();
        options.addArguments("user-agent=Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.124 Safari/537.36");
        WebDriver driver = new ChromeDriver(options);

        driver.get("https://www.periplus.com/account/Login");
        
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        WebElement usernameField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("email")));
        usernameField.sendKeys("yudiputrasabri789@gmail.com"); 
        WebElement passwordField = driver.findElement(By.name("password"));
        passwordField.sendKeys("Yudisabri123*"); 

        WebElement submitButton = driver.findElement(By.id("button-login"));
        submitButton.click();

        WebElement searchBox = wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("filter_name"))); 
        searchBox.sendKeys("Naruto");

        WebElement searchButton = driver.findElement(By.xpath("//button[@type='submit']"));
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("arguments[0].click();", searchButton);

        WebElement firstProduct = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".row.row-category-grid .single-product")));

        WebElement productLinkElement = firstProduct.findElement(By.cssSelector(".product-img a"));
        String productLink = productLinkElement.getDomAttribute("href");
        System.out.println("Product Link: " + productLink);

        driver.get(productLink);

        WebElement addToCartButton = driver.findElement(By.cssSelector(".btn-add-to-cart"));
        js = (JavascriptExecutor) driver;

        wait.until(ExpectedConditions.elementToBeClickable(addToCartButton));

        js.executeScript("arguments[0].click();", addToCartButton);
        System.out.println("Product added to cart.");

        try {
            WebElement successModal = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.cssSelector("#Notification-Modal.show .modal-text")
            ));
        
            String modalText = successModal.getText();
            if ("Success add to cart".equals(modalText)) {
                System.out.println("Success modal displayed: Product successfully added to the cart.");
            } else {
                System.out.println("Modal displayed, but the message does not indicate success: " + modalText);
            }
        } catch (TimeoutException e) {
            System.out.println("Success modal did not appear.");
        }


        
    }
}
