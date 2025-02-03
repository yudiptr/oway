package org.example;

import org.openqa.selenium.By;
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
import java.util.List;
import java.util.logging.Logger;

public class DeleteMailTest {
    private WebDriver driver;
    private WebDriverWait wait;
    private static final Logger logger = Logger.getLogger(DeleteMailTest.class.getName());

    @BeforeTest
    public void setUp() {
        logger.info("Setting up the WebDriver");
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--remote-allow-origins=*");
        driver = new ChromeDriver(options);
        wait = new WebDriverWait(driver, Duration.ofSeconds(60));
    }

    @Test
    public void testDeleteUnreadEmail() {
        loginToGmail("popoyudi333@gmail.com", "yudisabri123");
        deleteLatestUnreadEmail();
    }

    private void loginToGmail(String email, String password) {
        driver.get("https://mail.google.com/");
        logger.info("Navigated to Gmail login page");

        WebElement emailField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("identifierId")));
        Assert.assertNotNull(emailField, "Email field not found!");
        emailField.sendKeys(email);
        driver.findElement(By.id("identifierNext")).click();
        logger.info("Entered email and clicked next");

        WebElement passwordField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("Passwd")));
        Assert.assertNotNull(passwordField, "Password field not found!");
        passwordField.sendKeys(password);
        driver.findElement(By.id("passwordNext")).click();
        logger.info("Entered password and clicked next");

        // Verify successful login by checking the page title
        wait.until(ExpectedConditions.titleContains("Kotak Masuk"));
        String pageTitle = driver.getTitle();
        Assert.assertTrue(pageTitle.contains("Kotak Masuk"), "Login failed! Expected page title to contain 'Kotak Masuk', but got: " + pageTitle);
        logger.info("Successfully logged into Gmail");
    }

    private void deleteLatestUnreadEmail() {
        List<WebElement> unreadEmails = driver.findElements(By.cssSelector(".zA.zE"));
        Assert.assertFalse(unreadEmails.isEmpty(), "No unread emails found to delete.");

        WebElement lastUnreadEmail = unreadEmails.get(0);
        String emailTitle = lastUnreadEmail.findElement(By.cssSelector(".bog")).getText();
        logger.info("Last unread email title: " + emailTitle);

        WebElement emailCheckbox = lastUnreadEmail.findElement(By.cssSelector(".T-Jo"));
        Assert.assertNotNull(emailCheckbox, "Checkbox for selecting email not found!");
        emailCheckbox.click();
        logger.info("Selected the unread email");

        WebElement deleteButton = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector(".nX.T-I-ax7")));
        Assert.assertNotNull(deleteButton, "Delete button not found!");
        deleteButton.click();
        logger.info("Deleted the selected email");

        // Wait until the email disappears and verify it is deleted
        wait.until(ExpectedConditions.invisibilityOf(lastUnreadEmail));
        List<WebElement> remainingUnreadEmails = driver.findElements(By.cssSelector(".zA.zE"));
        Assert.assertFalse(remainingUnreadEmails.contains(lastUnreadEmail), "Email was not successfully deleted.");
        logger.info("Email successfully deleted");
    }

    @AfterTest
    public void tearDown() {
        if (driver != null) {
            driver.quit();
            logger.info("Closed the browser");
        }
    }
}
