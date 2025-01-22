package org.example;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
//import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.time.Duration;
import java.util.List;
import java.util.logging.Logger;
// For selenium version < 4, use this import
// import io.github.bonigarcia.wdm.WebDriverManager;

public class DeleteMailTest {
    private WebDriver driver;
    private WebDriverWait wait;
    private static final Logger logger = Logger.getLogger(DeleteMailTest.class.getName());

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
    public void gmailLoginAndFetchUnreadEmail() {
        try {

            // Login Gmail
            driver.get("https://mail.google.com/");
            logger.info("Navigated to Gmail login page");


            WebElement emailField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("identifierId")));
            emailField.sendKeys("popoyudi333@gmail.com");
            driver.findElement(By.id("identifierNext")).click();
            logger.info("Entered email and clicked next");

            WebElement passwordField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("Passwd")));
            passwordField.sendKeys("yudisabri123");
            driver.findElement(By.id("passwordNext")).click();
            logger.info("Entered password and clicked next");

            // Login Success, Navigate to Mail
            wait.until(ExpectedConditions.titleContains("Kotak Masuk"));
            logger.info("Successfully logged into Gmail");
            
            // Getting latest unread email
            List<WebElement> unreadEmails = driver.findElements(By.cssSelector(".zA.zE"));
            if (!unreadEmails.isEmpty()) {
                WebElement lastUnreadEmail = unreadEmails.get(0);
                String emailTitle = lastUnreadEmail.findElement(By.cssSelector(".bog")).getText();
                logger.info("Last unread email title: " + emailTitle);

                WebElement emailCheckbox = lastUnreadEmail.findElement(By.cssSelector(".T-Jo"));
                emailCheckbox.click();
                logger.info("Selected the unread email");

                WebElement deleteButton = wait
                        .until(ExpectedConditions.elementToBeClickable(By.cssSelector(".nX.T-I-ax7")));
                deleteButton.click();
                logger.info("Deleted the selected email");

                wait.until(ExpectedConditions.invisibilityOf(lastUnreadEmail));
                logger.info("Email successfully deleted");
            } else {
                logger.info("No unread emails found.");
            }
        } catch (Exception e) {
            logger.severe("Error during Gmail login, fetch, or delete: " + e.getMessage());
        }
    }

    @AfterTest
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}
