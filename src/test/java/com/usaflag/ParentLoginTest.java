package com.usaflag;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * Parent Login Test for USA Flag Application
 */
public class ParentLoginTest {
    
    private WebDriver driver;
    
    @BeforeMethod
    public void setUp() {
        WebDriverManager.chromedriver().setup();
        
        ChromeOptions options = new ChromeOptions();
        driver = new ChromeDriver(options);
        driver.manage().window().maximize();
        
        driver.get("https://staging-usaflag-playerportal.azurewebsites.net/login");
    }
    
    @Test
    public void testParentLogin() {
        // Find email field and enter email
        WebElement emailField = driver.findElement(By.id("email"));
        emailField.sendKeys("tyler.wright@yopmail.com");
        
        // Find password field and enter password
        WebElement passwordField = driver.findElement(By.id("password"));
        passwordField.sendKeys("12345678");
        
        // Find and click on the checkbox
        WebElement checkBox = driver.findElement(By.xpath("//div[@class='w-5 h-5 flex items-center justify-center rounded-sm border border-red bg-light-white']"));
        checkBox.click();
        
        // Click submit button
        WebElement submitButton = driver.findElement(By.xpath("//button[@type='submit']"));
        submitButton.click();
        
        // Wait for page to load after login
        try {
            Thread.sleep(3000); // Wait 3 seconds for page to load
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        boolean dashboardFound = false;
        boolean eventsFound = false;
        
        try {
            // Check for Dashboard menu item - looking for li element with text "dashboard"
            WebElement dashboardMenu = driver.findElement(By.xpath("//a[contains(@href, '/')]//li[contains(text(), 'dashboard')]"));
            if (dashboardMenu.isDisplayed()) {
                dashboardFound = true;
                System.out.println("✅ Dashboard menu found and visible");
            }
        } catch (Exception e) {
            System.out.println("❌ Dashboard menu not found");
        }
        
        try {
            // Check for Events menu item - looking for li element with text "events"
            WebElement eventsMenu = driver.findElement(By.xpath("//a[contains(@href, '/events')]//li[contains(text(), 'events')]"));
            if (eventsMenu.isDisplayed()) {
                eventsFound = true;
                System.out.println("✅ Events menu found and visible");
            }
        } catch (Exception e) {
            System.out.println("❌ Events menu not found");
        }
        
        // Assert test result - BOTH Dashboard and Events must be found for test to pass
        if (dashboardFound && eventsFound) {
            System.out.println("✅ Login successful! Both Dashboard and Events menus are visible on the dashboard page.");
            Assert.assertTrue(true, "Login successful - Both Dashboard and Events menus are visible");
        } else {
            System.out.println("❌ Login failed! Required menu items not found:");
            System.out.println("   - Dashboard found: " + dashboardFound);
            System.out.println("   - Events found: " + eventsFound);
            Assert.fail("Login failed - Required menu items (Dashboard and Events) not found in left sidebar");
        }
    }
    
    @AfterMethod
    public void tearDown() {
        if (driver != null) {
            //driver.quit();
        }
    }
}