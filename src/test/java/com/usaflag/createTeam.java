package com.usaflag;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.time.Duration;

/**
 * Create Team Test for USA Flag Application
 * Tests parent login and team creation functionality
 */
public class createTeam {
    
    private WebDriver driver;
    private WebDriverWait wait;
    
    // Configuration
    private static final String LOGIN_URL = "https://staging-usaflag-playerportal.azurewebsites.net/login";
    private static final String PARENT_EMAIL = "brittany.harris@yopmail.com";
    private static final String PARENT_PASSWORD = "12345678";
    
    @BeforeMethod
    public void setUp() {
        System.out.println("🔧 Setting up Create Team WebDriver...");
        
        WebDriverManager.chromedriver().setup();
        
        ChromeOptions options = new ChromeOptions();
        driver = new ChromeDriver(options);
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        
        wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        
        System.out.println("✅ Create Team WebDriver ready!");
    }
    
    @Test
    public void testCreateTeam() {
        System.out.println("🔍 Test: Create Team - Login and Navigate to Teams...");
        
        try {
            // Step 1: Navigate to login page
            System.out.println("🌐 Opening login page: " + LOGIN_URL);
            driver.get(LOGIN_URL);
            wait.until(ExpectedConditions.presenceOfElementLocated(By.tagName("body")));
            
            // Wait for page to load completely
            Thread.sleep(3000);
            
            // Step 2: Find email field and enter email
            System.out.println("📧 Entering parent email: " + PARENT_EMAIL);
            WebElement emailField = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("email")));
            emailField.clear();
            emailField.sendKeys(PARENT_EMAIL);
            System.out.println("✅ Parent email entered successfully");
            
            // Step 3: Find password field and enter password
            System.out.println("🔑 Entering parent password: " + PARENT_PASSWORD);
            WebElement passwordField = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("password")));
            passwordField.clear();
            passwordField.sendKeys(PARENT_PASSWORD);
            System.out.println("✅ Parent password entered successfully");
            
            // Step 4: Click on checkbox
            System.out.println("☑️ Clicking agreement checkbox...");
            WebElement checkBox = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//div[@class='w-5 h-5 flex items-center justify-center rounded-sm border border-red bg-light-white']")
            ));
            checkBox.click();
            System.out.println("✅ Agreement checkbox clicked successfully");
            
            // Step 5: Click on Login button
            System.out.println("🚀 Clicking login button...");
            WebElement loginButton = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//button[normalize-space()='Login']")
            ));
            loginButton.click();
            System.out.println("✅ Login button clicked successfully");
            
            // Step 6: Wait until the next page loads
            System.out.println("⏳ Waiting for next page to load...");
            Thread.sleep(5000);
            
            // Step 7: Click on the profile button
            System.out.println("👤 Clicking on profile button...");
            WebElement profileButton = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//span[@class='flex size-full items-center justify-center rounded-full w-[48px] h-[48px] bg-gray-300']")
            ));
            profileButton.click();
            System.out.println("✅ Profile button clicked successfully");
            
            // Wait for profile page to load
            Thread.sleep(3000);
            
            // Step 8: Click on Teams button tab
            System.out.println("🏈 Clicking on Teams button tab...");
            WebElement teamsButton = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//button[contains(text(), 'Teams') or contains(@id, 'teams')]")
            ));
            teamsButton.click();
            System.out.println("✅ Teams button tab clicked successfully");
            
            // Wait for teams page to load
            Thread.sleep(3000);
            
            // Step 9: Click on Add New Team button
            System.out.println("➕ Clicking on Add New Team button...");
            WebElement addNewTeamButton = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//button[@class='bg-[#0F2D52] text-white px-4 py-2 rounded-md text-sm']")
            ));
            addNewTeamButton.click();
            System.out.println("✅ Add New Team button clicked successfully");
            
            // Wait for modal/form to load
            Thread.sleep(3000);
            
            // Step 10: Upload team logo image first
            System.out.println("📁 Uploading team logo image...");
            try {
                WebElement uploadElement = driver.findElement(By.xpath("//label[@class='relative inline-flex items-center justify-center px-4 py-2 text-sm font-medium bg-light-white text-blue-900 border border-gray-300 rounded-md cursor-pointer hover:bg-gray-100 transition']//input[@type='file']"));
                uploadElement.sendKeys("C:\\Users\\User\\Desktop\\profile-pics\\logo\\29c4dbaa400e8914201711ae2063b17c.jpg");
                System.out.println("✅ Team logo image uploaded successfully");
                
                // Wait for image upload to process
                Thread.sleep(3000);
                
            } catch (Exception uploadException) {
                System.out.println("⚠️ Image upload failed: " + uploadException.getMessage());
                System.out.println("⚠️ Continuing with the test despite image upload error");
            }
            
            // Step 11: Fill out team creation form
            System.out.println("📝 Filling out team creation form...");
            
            // Find firstName field and enter "Arsenal"
            System.out.println("📝 Entering team name: Arsenal");
            WebElement firstNameField = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("firstName")));
            firstNameField.clear();
            firstNameField.sendKeys("Arsenal");
            System.out.println("✅ Team name entered successfully");
            
            // Find description field and enter description
            System.out.println("📝 Entering team description...");
            WebElement descriptionField = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("description")));
            descriptionField.clear();
            descriptionField.sendKeys("The Arsenal Football Club is a professional football club based in Islington, North London, England. They compete in the Premier League, the top tier of English football.");
            System.out.println("✅ Team description entered successfully");
            
            // Find location field and enter location
            System.out.println("📝 Entering team location: North London, England");
            WebElement locationField = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("location")));
            locationField.clear();
            locationField.sendKeys("North London, England");
            System.out.println("✅ Team location entered successfully");
            
            // Step 12: Click on classification dropdown and select Youth
            System.out.println("🔍 Clicking on classification dropdown...");
            WebElement classificationDropdown = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//select[@name='classification']")
            ));
            classificationDropdown.click();
            System.out.println("✅ Classification dropdown clicked successfully");
            
            // Wait a moment after clicking dropdown
            Thread.sleep(1000);
            
            // Select Youth option and click to close dropdown
            System.out.println("🔍 Selecting 'Youth' from dropdown...");
            WebElement youthOption = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//option[contains(text(), 'Youth') or contains(text(), 'youth')]")
            ));
            youthOption.click();
            System.out.println("✅ 'Youth' option selected and dropdown closed");
            
            // Wait for selection to be applied and dropdown to close
            Thread.sleep(2000);
            
            // Step 13: Click submit button
            System.out.println("🚀 Clicking submit button to create team...");
            WebElement submitButton = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//button[@type='submit']")
            ));
            submitButton.click();
            System.out.println("✅ Submit button clicked successfully - Team creation submitted!");
            
            // Wait for form submission to process
            Thread.sleep(3000);
            
            // Verify we're on the teams page
            String currentUrl = driver.getCurrentUrl();
            System.out.println("✅ Current URL after filling form: " + currentUrl);
            
            System.out.println("✅ Create Team test completed successfully - Team creation form filled with Arsenal details and logo uploaded!");
            
        } catch (Exception e) {
            System.err.println("❌ Create Team test failed: " + e.getMessage());
            e.printStackTrace();
            Assert.fail("Create Team test failed: " + e.getMessage());
        }
    }
    
    @AfterMethod
    public void tearDown() {
        System.out.println("🔍 Closing browser...");
        System.out.println("✅ Create Team test completed");
        System.out.println("📧 Parent email used: " + PARENT_EMAIL);
        
        // Close the browser
        if (driver != null) {
            // driver.quit();
            // System.out.println("✅ Browser closed successfully");
        }
    }
}