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

// For selenium version < 4, use this import
// import io.github.bonigarcia.wdm.WebDriverManager;

public class DeleteMailTest {
    private WebDriver driver;
    private WebDriverWait wait;

    @BeforeTest
    public void setUp() {
        // For Chrome < 112
        // WebDriverManager.chromedriver().setup();

        // For Chrome > 112, path to chromedriver for specific version, download manual through chrome
        System.setProperty("webdriver.chrome.driver", "C:\\chromedriver-win64\\chromedriver.exe");
        ChromeOptions options = new ChromeOptions();

        options.addArguments("--remote-allow-origins=*");

        driver = new ChromeDriver(options);
        wait = new WebDriverWait(driver, Duration.ofSeconds(60));
    }

    @Test
    public void gmailLoginAndFetchUnreadEmail() {
        driver.get("https://mail.google.com/");

        // Login Gmail
        WebElement emailField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("identifierId")));
        emailField.sendKeys("popoyudi333@gmail.com");
        driver.findElement(By.id("identifierNext")).click();

        WebElement passwordField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("Passwd")));
        passwordField.sendKeys("yudisabri123");
        driver.findElement(By.id("passwordNext")).click();

        wait.until(ExpectedConditions.titleContains("Kotak Masuk"));

        List<WebElement> unreadEmails = driver.findElements(By.cssSelector(".zA.zE"));
        if (!unreadEmails.isEmpty()) {
            WebElement lastUnreadEmail = unreadEmails.get(0);
            String emailTitle = lastUnreadEmail.findElement(By.cssSelector(".bog")).getText();
            System.out.println("Last unread email title: " + emailTitle);
        } else {
            System.out.println("No unread emails found.");
        }
    }

    @AfterTest
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}
