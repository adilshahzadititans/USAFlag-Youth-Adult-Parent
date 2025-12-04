package com.usaflag;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * Simple test to verify Maven, Selenium, and TestNG are working
 */
public class SimpleTest {
    
    private WebDriver driver;
    
    @BeforeMethod
    public void setUp() {
        // Setup Chrome WebDriver
        WebDriverManager.chromedriver().setup();
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--disable-gpu");
        options.addArguments("--window-size=1920,1080");
        
        driver = new ChromeDriver(options);
    }
    
    @Test
    public void testGoogleSearch() {
        // Navigate to Google
        driver.get("https://www.google.com");
        
        // Verify page title
        String title = driver.getTitle();
        System.out.println("Page title: " + title);
        
        // Basic assertion
        assert title.contains("Google") : "Page title should contain 'Google'";
        
        System.out.println("✅ Test passed! Maven, Selenium, and TestNG are working correctly.");
    }
    
    @AfterMethod
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}
