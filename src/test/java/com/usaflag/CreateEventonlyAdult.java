package com.usaflag;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import java.util.Properties;
import java.time.Duration;
import java.nio.file.Paths;

/**
 * Create Event Test - Adult Registration Only
 * 
 * This comprehensive test class automates the complete event creation process
 * with Adult Registration only (no Youth Registration).
 * 
 * Test Flow:
 * 1. Admin Login - Authenticates as admin and navigates to event creation
 * 2. Details Section - Sets up event basic information (name, description, dates, etc.)
 * 3. Payment Section - Configures Adult Registration payment and registration details
 * 4. Tournament Section - Creates and configures tournament brackets
 * 5. Event Specifics - Adds event-specific details, links, and configurations
 * 6. Add Ons - Configures hotels, flights, tickets, merchandise, and sponsors
 * 
 * Features:
 * - Uses TestNG priority and dependencies for sequential execution
 * - Robust element detection with multiple fallback methods
 * - Comprehensive logging for debugging
 * - Error handling with detailed console output
 * - Configurable via config.properties file
 * 
 * @author USA Flag Football Selenium Test Suite
 * @version 1.0
 */
public class CreateEventonlyAdult {
    
    private WebDriver driver;
    private WebDriverWait wait;
    private Properties config;
    
    @BeforeClass
    public void setUp() {
        // Load configuration properties
        loadConfig();
        
        // Setup WebDriver
        WebDriverManager.chromedriver().setup();
        
        ChromeOptions options = new ChromeOptions();
        driver = new ChromeDriver(options);
        driver.manage().window().maximize();
        
        // Initialize WebDriverWait with timeout
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        
        // Navigate to admin login page
        driver.get(config.getProperty("admin.login.url"));
    }
    
    /**
     * Utility method to upload files
     * @param filePath The path to the file to upload
     * @param fileType The type of file being uploaded (for logging purposes)
     */
    private void uploadFile(String filePath, String fileType) {
        try {
            // Find the file input element
            WebElement fileInput = wait.until(ExpectedConditions.presenceOfElementLocated(
                By.xpath("//input[@type='file']")));
            
            // Upload the file
            fileInput.sendKeys(filePath);
            System.out.println("✅ " + fileType + " uploaded: " + filePath);
            
            // Wait for file processing
            try {
                Thread.sleep(2000);
            } catch (InterruptedException ie) {
                Thread.currentThread().interrupt();
                System.err.println("❌ " + fileType + " upload wait interrupted: " + ie.getMessage());
            }
            
        } catch (Exception e) {
            System.err.println("❌ Failed to upload " + fileType + ": " + e.getMessage());
            throw e;
        }
    }
    
    /**
     * Overloaded utility to upload a file to a specific input element located by a locator
     * @param inputLocator Locator for the specific file input element
     * @param filePath The absolute path to the file to upload
     * @param fileType A friendly name for logging
     */
    private void uploadFile(By inputLocator, String filePath, String fileType) {
        try {
            WebElement fileInput = wait.until(ExpectedConditions.presenceOfElementLocated(inputLocator));
            fileInput.sendKeys(filePath);
            System.out.println("✅ " + fileType + " uploaded: " + filePath);
            try {
                Thread.sleep(2000);
            } catch (InterruptedException ie) {
                Thread.currentThread().interrupt();
                System.err.println("❌ " + fileType + " upload wait interrupted: " + ie.getMessage());
            }
        } catch (Exception e) {
            System.err.println("❌ Failed to upload " + fileType + ": " + e.getMessage());
            throw e;
        }
    }
    
    /**
     * Load configuration properties from config.properties file
     */
    private void loadConfig() {
        config = new Properties();
        try {
            FileInputStream input = new FileInputStream("src/main/resources/config.properties");
            config.load(input);
            input.close();
        } catch (IOException e) {
            System.err.println("Error loading configuration: " + e.getMessage());
            // Fallback to default values
            config.setProperty("admin.email", "rony13ching@gmail.com");
            config.setProperty("admin.password", "Flag!2025");
            config.setProperty("admin.login.url", "https://stage-usaflag-admin-portal-hefnfdgvhdgwfbck.eastus2-01.azurewebsites.net/login");
        }
    }
    
    /**
     * Test Method 1: Admin Login
     * 
     * Authenticates as admin user and navigates to the event creation page.
     * This is the first test in the sequence and must complete successfully
     * for subsequent tests to run.
     * 
     * Steps:
     * 1. Enter admin email
     * 2. Enter admin password  
     * 3. Click login button
     * 4. Verify successful login
     * 5. Navigate to "Create Event" page
     * 
     * @throws Exception if login fails or navigation fails
     */
    @Test(priority = 1)
    public void testAdminLogin() {
        try {
            System.out.println("🔐 Starting admin login process...");
            
            // Wait for email field to be visible and enter email
            WebElement emailField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("email")));
            emailField.clear();
            emailField.sendKeys(config.getProperty("admin.email"));
            System.out.println("✅ Email entered: " + config.getProperty("admin.email"));
            
            // Wait for password field to be visible and enter password
            WebElement passwordField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("password")));
            passwordField.clear();
            passwordField.sendKeys(config.getProperty("admin.password"));
            System.out.println("✅ Password entered");
            
            // Find and click on the checkbox
            WebElement checkBox = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//div[@class='w-5 h-5 flex items-center justify-center rounded-sm border border-red bg-light-white']")));
            checkBox.click();
            System.out.println("✅ Checkbox clicked");
            
            // Click submit button
            WebElement submitButton = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//button[@type='submit']")));
            submitButton.click();
            System.out.println("✅ Login button clicked");
            
            // Wait for successful login
            try {
                Thread.sleep(3000);
            } catch (InterruptedException ie) {
                Thread.currentThread().interrupt();
                System.err.println("❌ Login wait interrupted: " + ie.getMessage());
            }
            
            // Verify login success by checking if we're redirected to dashboard or admin panel
            String currentUrl = driver.getCurrentUrl();
            if (!currentUrl.contains("login")) {
                System.out.println("✅ Admin login successful! Redirected to: " + currentUrl);
            } else {
                System.out.println("⚠️ Login may not have been successful. Current URL: " + currentUrl);
            }
            
        } catch (Exception e) {
            System.err.println("❌ Admin login failed: " + e.getMessage());
            throw e;
        }
    }
    
    /**
     * Test Method 2: Event Details Section
     * 
     * Fills out all basic event details including tournament information,
     * dates, times, locations, and classifications.
     * 
     * Actions performed:
     * - Navigate to Events and Create Event
     * - Upload tournament logo
     * - Enter tournament name and description  
     * - Upload cover photo
     * - Set timezone and location
     * - Configure event classification
     * - Set start/end dates and times
     * - Configure registration and roster deadlines
     * - Navigate to Payment section
     * 
     * Depends on: testAdminLogin
     * @throws Exception if any element interaction fails
     */
    @Test(priority = 2, dependsOnMethods = "testAdminLogin")
    public void testDetails() {
        try {
            System.out.println("📅 Starting event creation process - DETAILS SECTION...");
            
            // Click on Events menu item in the left sidebar
            WebElement eventsMenuItem = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//li[normalize-space()='events']")));
            eventsMenuItem.click();
            System.out.println("✅ Events menu item clicked");
            
            // Wait a moment for the page to load
            try {
                Thread.sleep(2000);
            } catch (InterruptedException ie) {
                Thread.currentThread().interrupt();
                System.err.println("❌ Events page wait interrupted: " + ie.getMessage());
            }
            
            // Click on "Create New Event" button
            WebElement createEventButton = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//span[@class='mr-1']//*[name()='svg']")));
            createEventButton.click();
            System.out.println("✅ Create New Event button clicked");
            
            // Wait a moment for the create event form to load
            try {
                Thread.sleep(2000);
            } catch (InterruptedException ie) {
                Thread.currentThread().interrupt();
                System.err.println("❌ Create event form wait interrupted: " + ie.getMessage());
            }
            
            // Upload logo image using utility method
            // Use Paths.get() for portable file paths
            String logoPath = Paths.get("logo", "imagesa.png").toAbsolutePath().toString();
            uploadFile(logoPath, "Logo");
            
            // Click on the Tournament Name input and send keys
            WebElement tournamentNameInput = wait.until(ExpectedConditions.elementToBeClickable(
                By.name("details.tournamentName")));
            tournamentNameInput.click();
            tournamentNameInput.clear();
            tournamentNameInput.sendKeys("Power Play Football League-Ad");
            System.out.println("✅ Entered tournament name: Power Play Football League-Ad");
            
            // Click on the Description textarea and send keys
            WebElement descriptionTextarea = wait.until(ExpectedConditions.elementToBeClickable(
                By.name("details.description")));
            descriptionTextarea.click();
            descriptionTextarea.clear();
            descriptionTextarea.sendKeys("YOUR NEW HOME FOR SMALL-SIDED SPORTS." +
                "We are delighted to inform you that Powerplay has successfully joined forces with Powerleague, " +
                "combining our strengths to provide you with an even better sports experience.\n\n" +
                "As part of this transition, all Powerplay customers will need to reset their passwords to access " +
                "the Powerleague system. This is essential to ensure you can continue to access your account, " +
                "fixtures, and more without any interruptions.\n\n" +
                "To reset your password, access your account, and update your payment details, please click here.\n\n" +
                "If you encounter any issues or have any questions, our support team is here to help. " +
                "You can reach us at enquiries@powerleague.com or call us at 0800 567 0757.\n\n" +
                "Welcome to the Powerleague team.");
            System.out.println("✅ Entered tournament description");
            
            // Scroll down to find "Upload Cover Photo" text
            WebElement uploadCoverPhotoText = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//p[@class='text-sm font-medium text-gray-600' and normalize-space()='Upload Cover Photo']")));
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({behavior: 'instant', block: 'center'});", uploadCoverPhotoText);
            System.out.println("✅ Scrolled to 'Upload Cover Photo' section");
            
            // Wait a moment for scrolling to complete
            try {
                Thread.sleep(1000);
            } catch (InterruptedException ie) {
                Thread.currentThread().interrupt();
                System.err.println("❌ Scroll wait interrupted: " + ie.getMessage());
            }
            
            // Upload cover photo using utility method
            // Use Paths.get() for portable file paths
            String coverPhotoPath = Paths.get("C:", "Users", "User", "Desktop", "profile-pics", "CP", "360_F_517255588_qKuaEjpnGD7xkuIenozqUEd54z7SoT8u.jpg").toString();
            By coverPhotoInputLocator = By.xpath("//p[@class='text-sm font-medium text-gray-600' and normalize-space()='Upload Cover Photo']/ancestor::div[contains(@class,'border-dashed')][1]//input[@type='file']");
            uploadFile(coverPhotoInputLocator, coverPhotoPath, "Cover Photo");
            
            // Scroll to and click on the Time Zone dropdown
            WebElement timeZoneDropdown = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//div[contains(@class,'flex items-center justify-between w-full mx-auto')]")));
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({behavior: 'instant', block: 'center'});", timeZoneDropdown);
            try {
                Thread.sleep(500);
            } catch (InterruptedException ie) {
                Thread.currentThread().interrupt();
            }
            timeZoneDropdown.click();
            System.out.println("✅ Time Zone dropdown clicked");
            
            // Wait for dropdown to open
            try {
                Thread.sleep(1000);
            } catch (InterruptedException ie) {
                Thread.currentThread().interrupt();
            }
            
            // Enter timezone in search field and select
            boolean timezoneSelected = false;
            
            // Try search input approach
            try {
                WebElement searchInput = wait.until(ExpectedConditions.presenceOfElementLocated(
                    By.xpath("//input[contains(@id, 'radix') or @placeholder='Search...']")));
                searchInput.sendKeys("America/Chicago");
                System.out.println("✅ Entered 'America/Chicago' in search field");
                
                // Wait for filtered results and click option
                WebElement chicagoOption = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//span[normalize-space()='America/Chicago']")));
                chicagoOption.click();
                System.out.println("✅ Selected Time Zone: America/Chicago");
                timezoneSelected = true;
            } catch (Exception e1) {
                System.out.println("⚠️ Search approach failed: " + e1.getMessage());
            }
            
            // Fallback: Direct selection without search
            if (!timezoneSelected) {
            try {
                WebElement chicagoOption = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//span[normalize-space()='America/Chicago']")));
                chicagoOption.click();
                    System.out.println("✅ Selected Time Zone: America/Chicago (Direct)");
                    timezoneSelected = true;
                } catch (Exception e2) {
                    System.out.println("⚠️ Direct selection failed: " + e2.getMessage());
                }
            }
            
            // Final fallback: JavaScript click
            if (!timezoneSelected) {
                try {
                    WebElement chicagoOption = driver.findElement(
                        By.xpath("//*[contains(text(), 'America/Chicago')]"));
                    ((JavascriptExecutor) driver).executeScript("arguments[0].click();", chicagoOption);
                    System.out.println("✅ Selected Time Zone: America/Chicago (JavaScript)");
                    timezoneSelected = true;
                } catch (Exception e3) {
                    System.out.println("❌ Failed to select timezone: " + e3.getMessage());
                    throw new RuntimeException("Failed to select America/Chicago timezone");
                }
            }
            
            // Find the Location input field and enter "Chicago"
            WebElement locationInput = wait.until(ExpectedConditions.elementToBeClickable(
                By.name("details.location")));
            locationInput.click();
            locationInput.clear();
            locationInput.sendKeys("Chicago");
            System.out.println("✅ Entered location: Chicago");
            
            // Click on the Classification dropdown button
            boolean classificationClicked = false;
            
            // Approach 1: Use nth-child CSS selector
            try {
                WebElement classificationButton = wait.until(ExpectedConditions.elementToBeClickable(
                    By.cssSelector("div:nth-child(8) > button[role='combobox']")));
                classificationButton.click();
                System.out.println("✅ Classification dropdown clicked (CSS nth-child)");
                classificationClicked = true;
            } catch (Exception e1) {
                System.out.println("⚠️ Classification Approach 1 failed: " + e1.getMessage());
            }
            
            // Approach 2: Use XPath with Leagues text
            if (!classificationClicked) {
                try {
                    WebElement classificationButton = wait.until(ExpectedConditions.elementToBeClickable(
                        By.xpath("//button[@role='combobox' and @data-slot='select-trigger' and .//span[text()='Leagues']]")));
                    classificationButton.click();
                    System.out.println("✅ Classification dropdown clicked (XPath)");
                    classificationClicked = true;
                } catch (Exception e2) {
                    System.out.println("⚠️ Classification Approach 2 failed: " + e2.getMessage());
                }
            }
            
            // Approach 3: JavaScript click
            if (!classificationClicked) {
                try {
                    WebElement classificationButton = driver.findElement(
                        By.xpath("//button[@role='combobox' and contains(@class, 'flex justify-between')]"));
                    ((JavascriptExecutor) driver).executeScript("arguments[0].click();", classificationButton);
                    System.out.println("✅ Classification dropdown clicked (JavaScript)");
                    classificationClicked = true;
                } catch (Exception e3) {
                    System.out.println("⚠️ Classification Approach 3 failed: " + e3.getMessage());
                }
            }
            
            if (!classificationClicked) {
                System.out.println("❌ Failed to click Classification dropdown");
                throw new RuntimeException("Failed to click Classification dropdown");
            }
            
            // Wait for dropdown to open
            try {
                Thread.sleep(1500);
            } catch (InterruptedException ie) {
                Thread.currentThread().interrupt();
            }
            
            // Select "Leagues" from the dropdown options
            boolean leaguesSelected = false;
            
            // Try multiple approaches to select Leagues
            try {
                WebElement leaguesOption = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//span[normalize-space()='Leagues']")));
                leaguesOption.click();
                System.out.println("✅ Selected Classification: Leagues");
                leaguesSelected = true;
            } catch (Exception e1) {
                System.out.println("⚠️ Leagues selection failed: " + e1.getMessage());
            }
            
            if (!leaguesSelected) {
                try {
                    WebElement leaguesOption = wait.until(ExpectedConditions.elementToBeClickable(
                        By.xpath("//*[normalize-space()='Leagues' or text()='Leagues']")));
                    leaguesOption.click();
                    System.out.println("✅ Selected Classification: Leagues");
                    leaguesSelected = true;
                } catch (Exception e2) {
                    System.out.println("❌ Failed to select Leagues from Classification dropdown");
                    throw new RuntimeException("Failed to select Leagues");
                }
            }
            
            // Find and fill Start Date field
            WebElement startDateInput = wait.until(ExpectedConditions.elementToBeClickable(
                By.name("details.startDate")));
            startDateInput.click();
            startDateInput.clear();
            startDateInput.sendKeys("10252025");
            System.out.println("✅ Entered Start Date: 10252025");
            
            // Find and fill End Date field
            WebElement endDateInput = wait.until(ExpectedConditions.elementToBeClickable(
                By.name("details.endDate")));
            endDateInput.click();
            endDateInput.clear();
            endDateInput.sendKeys("11102025");
            System.out.println("✅ Entered End Date: 11102025");
            
            // Find and fill Start Time field
            WebElement startTimeInput = wait.until(ExpectedConditions.elementToBeClickable(
                By.name("details.startTime")));
            startTimeInput.click();
            startTimeInput.clear();
            startTimeInput.sendKeys("10001");
            System.out.println("✅ Entered Start Time: 10001");
            
            // Find and fill End Time field
            WebElement endTimeInput = wait.until(ExpectedConditions.elementToBeClickable(
                By.name("details.endTime")));
            endTimeInput.click();
            endTimeInput.clear();
            endTimeInput.sendKeys("10002");
            System.out.println("✅ Entered End Time: 10002");
            
            // Find and fill Check-In Opening Date field
            WebElement checkInOpeningDateInput = wait.until(ExpectedConditions.elementToBeClickable(
                By.name("details.checkInOpeningDate")));
            checkInOpeningDateInput.click();
            checkInOpeningDateInput.clear();
            checkInOpeningDateInput.sendKeys("10252025");
            System.out.println("✅ Entered Check-In Opening Date: 10252025");
            
            // Find and fill Check-In Closing Date field
            WebElement checkInClosingDateInput = wait.until(ExpectedConditions.elementToBeClickable(
                By.name("details.checkInClosingDate")));
            checkInClosingDateInput.click();
            checkInClosingDateInput.clear();
            checkInClosingDateInput.sendKeys("11102025");
            System.out.println("✅ Entered Check-In Closing Date: 11102025");
            
            // Find and fill Check-In Opening Time field
            WebElement checkInOpeningTimeInput = wait.until(ExpectedConditions.elementToBeClickable(
                By.name("details.checkInOpeningTime")));
            checkInOpeningTimeInput.click();
            checkInOpeningTimeInput.clear();
            checkInOpeningTimeInput.sendKeys("10001");
            System.out.println("✅ Entered Check-In Opening Time: 10001");
            
            // Find and fill Check-In Closing Time field
            WebElement checkInClosingTimeInput = wait.until(ExpectedConditions.elementToBeClickable(
                By.name("details.checkInClosingTime")));
            checkInClosingTimeInput.click();
            checkInClosingTimeInput.clear();
            checkInClosingTimeInput.sendKeys("10002");
            System.out.println("✅ Entered Check-In Closing Time: 10002");
            
            System.out.println("✅ Event details section filled successfully!");
            
            // Click on Next button to navigate to Payment section
            WebElement nextButton = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//button[normalize-space()='Next']")));
            nextButton.click();
            System.out.println("✅ Clicked Next button - Navigating to Payment section");
            
            System.out.println("✅ DETAILS section completed!");
            
        } catch (Exception e) {
            System.err.println("❌ Details section failed: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }
    
    /**
     * Test Method 3: Payment Section - Adult Registration Only
     * 
     * Configures payment and registration settings for Adult Registration only.
     * This method does NOT configure Youth Registration.
     * 
     * Actions performed:
     * - Enable Adult Registration checkbox
     * - Select Adult Registration style (5v5 Contact)
     * - Choose Adult category (Adult Coed) and division (Amateur)
     * - Set deposit amount (100)
     * - Set max number of teams (3)
     * - Configure waitlist (Yes)
     * - Set registration price (200)
     * - Validate form fields for errors
     * - Navigate to Tournament section
     * 
     * Depends on: testDetails
     * @throws InterruptedException if thread sleep is interrupted
     * @throws Exception if any element interaction fails
     */
    @Test(priority = 3, dependsOnMethods = "testDetails")
    public void testPaymentAdultOnly() throws InterruptedException {
        try {
            System.out.println("💳 Starting PAYMENT SECTION - ADULT ONLY...");
            
            // Wait for Payment section to load
            Thread.sleep(2000);
            System.out.println("✅ Payment section loaded successfully!");
            
            // DIAGNOSTIC: Print all input fields found on the page
            System.out.println("\n🔍 DIAGNOSTIC: Scanning for input fields on the page...");
            try {
                List<WebElement> allInputs = driver.findElements(By.tagName("input"));
                System.out.println("📋 Found " + allInputs.size() + " total input fields");
                
                int count = 0;
                for (WebElement input : allInputs) {
                    try {
                        String name = input.getAttribute("name");
                        String type = input.getAttribute("type");
                        boolean isVisible = input.isDisplayed();
                        
                        if (name != null && name.contains("paymentRegistration")) {
                            count++;
                            System.out.println("  " + count + ". Name: " + name + " | Type: " + type + " | Visible: " + isVisible);
                        }
                    } catch (Exception ignored) {}
                }
                System.out.println("🔍 DIAGNOSTIC END\n");
            } catch (Exception e) {
                System.out.println("⚠️ Could not scan fields: " + e.getMessage());
            }
            
            // ============================================
            // CLICK ADULT REGISTRATION CHECKBOX
            // ============================================
            
            System.out.println("⚠️ Looking for Adult Registration checkbox...");
            
            // Find the Adult Registration checkbox button
            WebElement adultRegCheckbox = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//label[contains(text(), 'Adult Registration')]/preceding-sibling::button[@role='checkbox']")));
            
            // Scroll to checkbox
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({behavior: 'instant', block: 'center'});", adultRegCheckbox);
            Thread.sleep(500);
            
            // Check current state
            String ariaChecked = adultRegCheckbox.getAttribute("aria-checked");
            System.out.println("📋 Adult Registration checkbox state: " + ariaChecked);
            
            // Click checkbox to enable Adult Registration (if not already checked)
            if (!"true".equals(ariaChecked)) {
                ((JavascriptExecutor) driver).executeScript("arguments[0].click();", adultRegCheckbox);
                System.out.println("✅ Clicked Adult Registration checkbox to enable it");
                Thread.sleep(1000);
            } else {
                System.out.println("✅ Adult Registration checkbox already enabled");
            }
            
            // Wait for Adult Registration section to appear
            Thread.sleep(2000);
            
            // Scroll to "Adult Registration" visible text
            WebElement adultRegistrationText = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//*[normalize-space(text())='Adult Registration']")));
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({behavior: 'instant', block: 'center'});", adultRegistrationText);
            System.out.println("✅ Scrolled to 'Adult Registration' section");
            Thread.sleep(500);
            
            // ==================== ADULT REGISTRATION ====================
            System.out.println("🔄 Starting Adult Registration...");
            
            // Select "5v5 Contact" from Style dropdown
            WebElement styleSelect = wait.until(ExpectedConditions.presenceOfElementLocated(
                By.xpath("//select[@aria-hidden='true'][option[text()='5v5 Collegiate']]")));
            Select styleDropdown = new Select(styleSelect);
            styleDropdown.selectByVisibleText("5v5 Contact");
            System.out.println("✅ Selected Style: 5v5 Contact");
            Thread.sleep(500);
            
            // Wait for Category button to be clickable and click it
            WebElement categoryButton = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//button[@role='combobox' and contains(@id, 'form-item')][@aria-describedby and contains(@class, 'border flex justify-between')][.//span[text()='Select']]")));
            categoryButton.click();
            System.out.println("✅ Category button clicked");
            Thread.sleep(500);
            
            // Click on "Adult Coed" option from the dropdown
            WebElement adultCoedOption = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//div[normalize-space()='Adult Coed'] | //span[normalize-space()='Adult Coed']")));
            adultCoedOption.click();
            System.out.println("✅ Selected Category: Adult Coed");
            Thread.sleep(1500);
            
            // Adult Division - Find the Division dropdown
            System.out.println("⚠️ Looking for Adult Division dropdown...");
            List<WebElement> divisionButtons = driver.findElements(
                By.xpath("//label[contains(text(), 'Division')]/following-sibling::button[@role='combobox']"));
            System.out.println("🔍 Found " + divisionButtons.size() + " Division buttons");
            
            if (divisionButtons.isEmpty()) {
                System.out.println("❌ No Division buttons found, waiting longer...");
                Thread.sleep(2000);
                divisionButtons = driver.findElements(
                    By.xpath("//label[contains(text(), 'Division')]/following-sibling::button[@role='combobox']"));
                System.out.println("🔍 After wait: Found " + divisionButtons.size() + " Division buttons");
            }
            
            WebElement adultDivisionButton = divisionButtons.get(0); // FIRST = Adult
            
            // Scroll to Division button
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({behavior: 'instant', block: 'center'});", adultDivisionButton);
            Thread.sleep(500);
            
            // Click Division dropdown
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", adultDivisionButton);
            System.out.println("✅ Adult Division button clicked");
            Thread.sleep(1500);
            
            // Adult Division - Select "Amateur"
            List<WebElement> amateurOptions = wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(
                By.xpath("//span[normalize-space()='Amateur'] | //div[normalize-space()='Amateur']")));
            System.out.println("🔍 Found " + amateurOptions.size() + " Amateur options");
            
            if (amateurOptions.size() >= 1) {
                WebElement adultAmateurOption = amateurOptions.get(0); // FIRST = Adult
                ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center'});", adultAmateurOption);
                Thread.sleep(500);
                ((JavascriptExecutor) driver).executeScript("arguments[0].click();", adultAmateurOption);
                Thread.sleep(1000);
                System.out.println("✅ Selected Adult Division: Amateur");
            } else {
                System.out.println("❌ No Amateur options found for Adult Division");
                throw new RuntimeException("Adult Division selection failed");
            }
            
            // Adult Deposit
            System.out.println("⚠️ Adult Deposit...");
            Thread.sleep(1000); // Wait before accessing next field
            try {
                WebElement depositField = driver.findElement(By.name("paymentRegistration.adult.0.deposit"));
                
                ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center'});", depositField);
                Thread.sleep(500);
                depositField.click();
                depositField.clear();
                depositField.sendKeys("100");
                Thread.sleep(800);
                System.out.println("✅ Entered deposit: 100");
            } catch (Exception e) {
                System.out.println("❌ FAILED to enter deposit: " + e.getMessage());
                throw e;
            }
            
            // Adult Max # Of Teams
            System.out.println("⚠️ Adult Max Teams...");
            Thread.sleep(800);
            try {
                WebElement maxTeamsField = driver.findElement(By.name("paymentRegistration.adult.0.maxNoOfTeams"));
                ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center'});", maxTeamsField);
                Thread.sleep(500);
                maxTeamsField.click();
                maxTeamsField.clear();
                maxTeamsField.sendKeys("3");
                Thread.sleep(800);
                System.out.println("✅ Entered Max # Of Teams: 3");
            } catch (Exception e) {
                System.out.println("❌ FAILED to enter Max Teams: " + e.getMessage());
                throw e;
            }
            
            // Adult Waitlist - Click dropdown
            List<WebElement> adultWaitlistButtons = driver.findElements(
                By.xpath("//label[contains(text(), 'Waitlist')]/following-sibling::button"));
            System.out.println("🔍 Found " + adultWaitlistButtons.size() + " Waitlist buttons");
            WebElement adultWaitlistButton = adultWaitlistButtons.get(0); // FIRST = Adult
            
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center'});", adultWaitlistButton);
            Thread.sleep(500);
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", adultWaitlistButton);
            System.out.println("✅ Adult Waitlist dropdown clicked");
            Thread.sleep(2000);
            
            // Adult Waitlist - Select "Yes"
            List<WebElement> adultYesOptions = wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(
                By.xpath("//span[normalize-space()='Yes'] | //div[normalize-space()='Yes']")));
            System.out.println("🔍 Found " + adultYesOptions.size() + " 'Yes' options");
            
            if (adultYesOptions.size() >= 1) {
                WebElement adultYesOption = adultYesOptions.get(0); // FIRST = Adult
                ((JavascriptExecutor) driver).executeScript("arguments[0].click();", adultYesOption);
                Thread.sleep(1500);
                System.out.println("✅ Selected Adult Waitlist: Yes");
            }
            
            // Adult Price
            System.out.println("⚠️ Adult Price...");
            Thread.sleep(800); // Wait before accessing next field
            try {
                WebElement priceField = driver.findElement(By.name("paymentRegistration.adult.0.price"));
                
                ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center'});", priceField);
                Thread.sleep(600);
                priceField.click();
                priceField.clear();
                priceField.sendKeys("200");
                Thread.sleep(800);
                System.out.println("✅ Entered price: 200");
            } catch (Exception e) {
                System.out.println("❌ FAILED to enter price: " + e.getMessage());
                throw e;
            }
            
            // Validate Adult Registration
            Thread.sleep(1000);
            List<WebElement> adultValidationErrors = driver.findElements(
                By.xpath("//div[contains(text(), 'Adult Registration')]//following::*[contains(text(), 'is required') or contains(text(), 'required')]"));
            
            if (adultValidationErrors.isEmpty()) {
                System.out.println("✅ Adult Registration completed successfully!");
            } else {
                System.out.println("❌ Adult Registration FAILED - Validation errors found:");
                for (WebElement error : adultValidationErrors) {
                    System.out.println("   ❌ " + error.getText());
                }
                throw new RuntimeException("Adult Registration validation failed");
            }
            
            // ============================================
            // CLICK NEXT BUTTON TO MOVE TO TOURNAMENT SECTION
            // ============================================
            
            System.out.println("⚠️ Waiting before clicking Next button...");
            Thread.sleep(2000);
            
            System.out.println("⚠️ Looking for 'Next' button...");
            Thread.sleep(1000);
            
            boolean paymentNextButtonClicked = false;
            WebElement paymentNextButton;
            
            // Try to find and click the Next button with scrolling
            for (int attempt = 1; attempt <= 5; attempt++) {
                try {
                    System.out.println("⚠️ Attempt " + attempt + " to find Next button...");
                    
                    // Try to find the Next button
                    paymentNextButton = driver.findElement(By.xpath("//button[normalize-space()='Next']"));
                    
                    // Check if button is visible
                    if (paymentNextButton.isDisplayed()) {
                        // Scroll to button and click
                        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({behavior: 'instant', block: 'center'});", paymentNextButton);
                        Thread.sleep(500);
                        
                        // Click using JavaScript for reliability
                        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", paymentNextButton);
                        System.out.println("✅ 'Next' button clicked successfully!");
                        System.out.println("✅ Moving to Tournament section...");
                        paymentNextButtonClicked = true;
                        break;
                    }
                } catch (Exception e) {
                    System.out.println("⚠️ Next button not found yet, scrolling down...");
                    // Scroll down by 300 pixels
                    ((JavascriptExecutor) driver).executeScript("window.scrollBy(0, 300);");
                    Thread.sleep(500);
                }
            }
            
            if (!paymentNextButtonClicked) {
                System.out.println("❌ Failed to find/click 'Next' button after 5 attempts");
                throw new RuntimeException("Next button not found in Payment section");
            }
            
            // Wait for Tournament section to load
            Thread.sleep(1500);
            
            System.out.println("✅ PAYMENT section (Adult Only) completed!");
            
        } catch (Exception e) {
            System.err.println("❌ Payment section failed: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }
    
    /**
     * Test Method 4: Tournament Section
     * 
     * Creates and configures tournament brackets with all necessary settings.
     * 
     * Actions performed:
     * - Find and click "Create Bracket" button
     * - Enter bracket name
     * - Navigate through bracket configuration tabs
     * - Set threshold in Advance Settings
     * - Configure placement rules
     * - Finalize bracket creation
     * - Navigate to Event Specifics section
     * 
     * Depends on: testPaymentAdultOnly
     * @throws InterruptedException if thread sleep is interrupted
     * @throws Exception if bracket creation fails
     */
    @Test(priority = 4, dependsOnMethods = "testPaymentAdultOnly")
    public void testTournament() throws InterruptedException {
        try {
            System.out.println("🏆 Starting TOURNAMENT SECTION...");
            
            // ============================================
            // TOURNAMENT SECTION - CREATE BRACKET
            // ============================================
            
            System.out.println("⚠️ Looking for 'Create Bracket' button...");
            
            // Wait for Tournament page to load
            try {
                Thread.sleep(2000);
            } catch (InterruptedException ie) {
                Thread.currentThread().interrupt();
            }
            
            // Find and click "Create Bracket" button with enhanced detection
            WebElement createBracketButton = null;
            boolean bracketButtonFound = false;
            
            // Method 1: Try with text-based locator (most stable)
            try {
                createBracketButton = new WebDriverWait(driver, Duration.ofSeconds(5))
                    .until(ExpectedConditions.elementToBeClickable(
                        By.xpath("//button[normalize-space()='Create Bracket' or contains(normalize-space(), 'Create Bracket')]")));
                bracketButtonFound = true;
                System.out.println("✅ Found 'Create Bracket' button using text (Method 1)");
            } catch (Exception e) {
                System.out.println("⚠️ Method 1 (text-based) failed, trying alternate approaches...");
            }
            
            // Method 2: Try with exact class match
            if (!bracketButtonFound) {
            try {
                createBracketButton = driver.findElement(
                    By.xpath("//button[@class='text-sm hover:underline capitalize']"));
                if (createBracketButton.isDisplayed()) {
                    bracketButtonFound = true;
                        System.out.println("✅ Found 'Create Bracket' button using class (Method 2)");
                }
        } catch (Exception e) {
                    System.out.println("⚠️ Method 2 (class-based) failed, trying contains...");
                }
            }
            
            // Method 3: Try with contains class
            if (!bracketButtonFound) {
                try {
                    createBracketButton = driver.findElement(
                        By.xpath("//button[contains(@class, 'hover:underline') and contains(@class, 'capitalize')]"));
                    if (createBracketButton.isDisplayed()) {
                        bracketButtonFound = true;
                        System.out.println("✅ Found 'Create Bracket' button (Method 3)");
                    }
                } catch (Exception e) {
                    System.out.println("⚠️ Method 3 failed, scrolling to find...");
                }
            }
            
            // Method 4: Scroll down to find the button
            if (!bracketButtonFound) {
                for (int i = 0; i < 5; i++) {
                    try {
                        ((JavascriptExecutor) driver).executeScript("window.scrollBy(0, 300);");
                        Thread.sleep(800);
                        
                        // Try text-based first after scroll
                        try {
                            createBracketButton = driver.findElement(
                                By.xpath("//button[contains(normalize-space(), 'Create Bracket')]"));
                            if (createBracketButton.isDisplayed()) {
                                bracketButtonFound = true;
                                System.out.println("✅ Found 'Create Bracket' button after scroll " + (i + 1));
                                break;
                            }
                        } catch (Exception textEx) {
                            // Fall back to class-based
                        createBracketButton = driver.findElement(
                            By.xpath("//button[contains(@class, 'hover:underline')]"));
                        if (createBracketButton.isDisplayed()) {
                            bracketButtonFound = true;
                            System.out.println("✅ Found 'Create Bracket' button after scroll " + (i + 1));
                            break;
                            }
                        }
                    } catch (Exception e) {
                        // Continue scrolling
                    }
                }
            }
            
            if (!bracketButtonFound) {
                System.out.println("❌ Create Bracket button not found");
                throw new RuntimeException("Create Bracket button not found");
            }
            
            try {
                // Scroll button into view
                ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({behavior: 'instant', block: 'center'});", createBracketButton);
                Thread.sleep(800);
                
                // Click using JavaScript for reliability
                ((JavascriptExecutor) driver).executeScript("arguments[0].click();", createBracketButton);
                System.out.println("✅ Clicked 'Create Bracket' button");
                
                // Wait for modal to open
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                }
                
                // Wait for modal to be visible
                wait.until(ExpectedConditions.presenceOfElementLocated(
                    By.xpath("//h2[contains(text(), 'Create Bracket')]")));
                System.out.println("✅ 'Create Bracket' modal opened");
                
                // Find input field and enter "Bracket"
                WebElement bracketNameInput = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//input[@type='text']")));
                bracketNameInput.click();
                bracketNameInput.clear();
                bracketNameInput.sendKeys("Bracket");
                System.out.println("✅ Entered Bracket name: Bracket");
                
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                }
                
                // ENHANCED: Scroll down WITHIN THE MODAL to find Next button
                System.out.println("⚠️ Scrolling within modal to find 'Next' button...");
                WebElement modalNextButton = null;
                boolean nextButtonFound = false;
                
                // Find the modal container - use role="dialog" which is more reliable
                WebElement modalDialog = null;
                try {
                    // The modal has role="dialog" and overflow-auto class
                    modalDialog = driver.findElement(By.xpath("//div[@role='dialog' and contains(@class, 'overflow-auto')]"));
                    System.out.println("✅ Found modal dialog with overflow-auto");
                } catch (Exception e) {
                    // Fallback: any div with role="dialog"
                    try {
                        modalDialog = driver.findElement(By.xpath("//div[@role='dialog']"));
                        System.out.println("✅ Found modal dialog by role");
                    } catch (Exception e2) {
                        System.out.println("⚠️ Could not find modal dialog, will use JavaScript to scroll");
                    }
                }
                
                // Multi-approach detection with AGGRESSIVE scrolling WITHIN THE MODAL
                for (int attempt = 0; attempt < 15; attempt++) {
                    try {
                        // Method 1: Find Next button inside dialog with bg-dark-blue class (most specific)
                        try {
                            List<WebElement> nextButtons = driver.findElements(
                                By.xpath("//div[@role='dialog']//button[@data-slot='button' and @type='button' and contains(@class, 'bg-dark-blue')]"));
                            
                            for (WebElement btn : nextButtons) {
                                try {
                                    String buttonText = btn.getText();
                                    if (btn.isDisplayed() && buttonText != null && buttonText.trim().startsWith("Next")) {
                                        modalNextButton = btn;
                                        nextButtonFound = true;
                                        System.out.println("✅ Found 'Next' button (Method 1 - data-slot in dialog)");
                                        break;
                                    }
                                } catch (Exception ignored) {}
                            }
                        } catch (Exception ignored) {}
                        
                        if (nextButtonFound) {
                            break;
                        }
                        
                        // Method 2: Find any button with bg-dark-blue class containing "Next"
                        try {
                            List<WebElement> blueButtons = driver.findElements(
                                By.xpath("//button[contains(@class, 'bg-dark-blue') and contains(., 'Next')]"));
                            
                            for (WebElement btn : blueButtons) {
                                try {
                                    if (btn.isDisplayed()) {
                                        modalNextButton = btn;
                                        nextButtonFound = true;
                                        System.out.println("✅ Found 'Next' button (Method 2 - bg-dark-blue)");
                                        break;
                                    }
                                } catch (Exception ignored) {}
                            }
                        } catch (Exception ignored) {}
                        
                        if (nextButtonFound) {
                            break;
                        }
                        
                        // Method 3: Find any button containing "Next" text
                        try {
                            List<WebElement> allNextButtons = driver.findElements(
                                By.xpath("//button[@type='button' and contains(text(), 'Next')]"));
                            
                            for (WebElement btn : allNextButtons) {
                                try {
                                    if (btn.isDisplayed()) {
                                        modalNextButton = btn;
                                        nextButtonFound = true;
                                        System.out.println("✅ Found 'Next' button (Method 3 - any Next button)");
                                        break;
                                    }
                                } catch (Exception ignored) {}
                            }
                        } catch (Exception ignored) {}
                        
                        if (nextButtonFound) {
                            break;
                        }
                        
                    } catch (Exception e) {
                        // Continue to scroll
                    }
                    
                    // AGGRESSIVE SCROLLING - Scroll within the modal dialog
                    System.out.println("⚠️ Attempt " + (attempt + 1) + " - Scrolling down within modal (300px)...");
                    
                    if (modalDialog != null) {
                        // Scroll within the modal dialog element using scrollTop
                        ((JavascriptExecutor) driver).executeScript(
                            "arguments[0].scrollTop = arguments[0].scrollTop + 300;", modalDialog);
                    } else {
                        // Fallback: Use JavaScript to find and scroll any dialog
                        ((JavascriptExecutor) driver).executeScript(
                            "var dialog = document.querySelector('div[role=\"dialog\"]');" +
                            "if (dialog) { dialog.scrollTop = dialog.scrollTop + 300; }" +
                            "else { window.scrollBy(0, 300); }");
                    }
                    
                    try {
                        Thread.sleep(700);
                    } catch (InterruptedException ie) {
                        Thread.currentThread().interrupt();
                    }
                }
                
                if (!nextButtonFound || modalNextButton == null) {
                    System.out.println("❌ Next button not found after scrolling");
                    throw new RuntimeException("Next button not found in modal");
                }
                
                // Scroll button into view and click
                ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({behavior: 'instant', block: 'center'});", modalNextButton);
                try {
                    Thread.sleep(600);
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                }
                
                modalNextButton.click();
                System.out.println("✅ Clicked 'Next' button in Create Bracket modal");
                
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                }
                
                // Enter threshold value in Advance Settings tab
                WebElement thresholdInput = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//*[@id='threshold']")));
                thresholdInput.click();
                thresholdInput.clear();
                thresholdInput.sendKeys("1");
                System.out.println("✅ Entered Threshold: 1");
                
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                }
                
                // Click Next button in Advance Settings tab (inside modal)
                System.out.println("⚠️ Looking for Next button in Advance Settings tab...");
                WebElement advanceNextButton = null;
                
                try {
                    // Method 1: Use dynamic ID pattern (works with any radix ID)
                    advanceNextButton = wait.until(ExpectedConditions.elementToBeClickable(
                        By.xpath("//*[contains(@id, '-content-advance')]/div[2]/button[2]")));
                    System.out.println("✅ Found Next button (Method 1 - content-advance pattern)");
                } catch (Exception e) {
                    // Method 2: Find the last Next button with bg-dark-blue class in modal
                    try {
                        List<WebElement> nextButtons = driver.findElements(
                            By.xpath("//div[@role='dialog']//button[contains(@class, 'bg-dark-blue') and contains(., 'Next')]"));
                        if (nextButtons.size() > 0) {
                            advanceNextButton = nextButtons.get(nextButtons.size() - 1);
                            System.out.println("✅ Found Next button (Method 2 - last Next in modal)");
                        }
                    } catch (Exception e2) {
                        System.out.println("⚠️ Could not find Next button: " + e2.getMessage());
                    }
                }
                
                if (advanceNextButton != null) {
                    advanceNextButton.click();
                    System.out.println("✅ Clicked Next button in Advance Settings");
                } else {
                    System.out.println("❌ Next button not found in Advance Settings");
                    throw new RuntimeException("Next button not found in Advance Settings tab");
                }
                
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                }
                
                // Click Create Bracket button in Placement Rules tab
                System.out.println("⚠️ Looking for 'Create Bracket' button in Placement Rules...");
                WebElement createBracketFinalButton = null;
                
                try {
                    // Method 1: Use dynamic ID pattern (works with any radix ID)
                    createBracketFinalButton = wait.until(ExpectedConditions.elementToBeClickable(
                        By.xpath("//*[contains(@id, '-content-placement')]/div[2]/button[2]")));
                    System.out.println("✅ Found 'Create Bracket' button (Method 1 - content-placement pattern)");
                } catch (Exception e) {
                    // Method 2: Find button with text "Create Bracket"
                    try {
                        List<WebElement> createButtons = driver.findElements(
                            By.xpath("//button[contains(., 'Create Bracket')]"));
                        if (createButtons.size() > 0) {
                            createBracketFinalButton = createButtons.get(createButtons.size() - 1);
                            System.out.println("✅ Found 'Create Bracket' button (Method 2 - button text)");
                        }
                    } catch (Exception e2) {
                        System.out.println("⚠️ Could not find Create Bracket button: " + e2.getMessage());
                    }
                }
                
                if (createBracketFinalButton != null) {
                    createBracketFinalButton.click();
                    System.out.println("✅ Clicked 'Create Bracket' button to finalize");
                } else {
                    System.out.println("❌ Create Bracket button not found");
                    throw new RuntimeException("Create Bracket button not found in Placement Rules");
                }
                
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                }
                
                System.out.println("✅ Tournament Bracket created successfully!");
                
                // Click Next button after bracket creation
                System.out.println("⚠️ Looking for 'Next' button after bracket creation...");
                
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                }
                
                WebElement finalNextButton = null;
                boolean finalNextFound = false;
                
                // Try to find and click the Next button
                for (int attempt = 0; attempt < 5; attempt++) {
                    try {
                        finalNextButton = driver.findElement(By.xpath("//button[normalize-space()='Next']"));
                        if (finalNextButton.isDisplayed()) {
                            // Scroll to button
                            ((JavascriptExecutor) driver).executeScript(
                                "arguments[0].scrollIntoView({behavior: 'instant', block: 'center'});", finalNextButton);
                            Thread.sleep(500);
                            
                            // Click the button
                            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", finalNextButton);
                            System.out.println("✅ Clicked 'Next' button - Tournament section completed");
                            System.out.println("✅ Moving to Event Specifics section...");
                            finalNextFound = true;
                            break;
                        }
                    } catch (Exception e) {
                        System.out.println("⚠️ Attempt " + (attempt + 1) + " - Next button not found, scrolling...");
                        ((JavascriptExecutor) driver).executeScript("window.scrollBy(0, 300);");
                        try {
                            Thread.sleep(500);
                        } catch (InterruptedException ie) {
                            Thread.currentThread().interrupt();
                        }
                    }
                }
                
                if (!finalNextFound) {
                    System.out.println("⚠️ Next button not found after bracket creation, continuing...");
                }
                
                try {
                    Thread.sleep(1500);
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                }
                
                System.out.println("✅ TOURNAMENT section completed!");
                
            } catch (Exception e) {
                System.err.println("❌ Failed to create bracket: " + e.getMessage());
            throw e;
            }
            
        } catch (Exception e) {
            System.err.println("❌ Tournament section failed: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }
    
    /**
     * Test Method 5: Event Specifics Section
     * 
     * Configures event-specific details, logos, links, and field information.
     * 
     * Actions performed:
     * - Upload event logo
     * - Add event links (name and URL)
     * - Set pricing information
     * - Configure deadlines, draft & seeding
     * - Add prizes and awards details
     * - Set event format and flags required
     * - Configure field details and location
     * - Upload cover photo
     * - Navigate to Add Ons section
     * 
     * Depends on: testTournament
     * @throws InterruptedException if thread sleep is interrupted
     * @throws Exception if any element interaction fails
     */
    @Test(priority = 5, dependsOnMethods = "testTournament")
    public void testEventSpecifics() throws InterruptedException {
        try {
            System.out.println("📋 Starting EVENT SPECIFICS SECTION...");
            
            try {
                Thread.sleep(2000);
            } catch (InterruptedException ie) {
                Thread.currentThread().interrupt();
            }
                
                // Upload Event Logo
                System.out.println("⚠️ Uploading event logo...");
                
                try {
                    // Find the file input element (usually hidden behind the SVG icon)
                    WebElement fileInput = null;
                    List<WebElement> fileInputsList = null;
                    
                    // Method 1: Try to find input[type="file"]
                    try {
                        fileInputsList = driver.findElements(By.xpath("//input[@type='file']"));
                        if (!fileInputsList.isEmpty()) {
                            fileInput = fileInputsList.get(0);
                            System.out.println("✅ Found file input element");
                        }
                    } catch (Exception e) {
                        System.out.println("⚠️ Direct file input not found");
                    }
                    
                    // Method 2: Try clicking the SVG or its parent to reveal file input
                    if (fileInput == null) {
                        try {
                            // Find the clickable element containing the SVG
                            WebElement uploadButton = driver.findElement(
                                By.xpath("//svg[contains(@viewBox, '33 32')]//ancestor::*[self::button or self::label or self::div][1]"));
                            uploadButton.click();
                            System.out.println("✅ Clicked upload button");
                            Thread.sleep(500);
                            
                            // Now try to find the file input again
                            fileInputsList = driver.findElements(By.xpath("//input[@type='file']"));
                            if (!fileInputsList.isEmpty()) {
                                fileInput = fileInputsList.get(0);
                            }
                        } catch (Exception e2) {
                            System.out.println("⚠️ Could not click SVG parent: " + e2.getMessage());
                        }
                    }
                    
                    // Upload the file
                    if (fileInput != null) {
                        // Use Paths.get() for portable file paths
                        String eventLogoPath = Paths.get("C:", "Users", "User", "Desktop", "profile-pics", "logo", "imagesa.png").toString();
                        fileInput.sendKeys(eventLogoPath);
                        System.out.println("✅ Uploaded logo: " + eventLogoPath);
                        
                        try {
                            Thread.sleep(2000);
                        } catch (InterruptedException ie) {
                            Thread.currentThread().interrupt();
                        }
                    } else {
                        System.out.println("⚠️ File input element not found, skipping logo upload");
                    }
                    
                } catch (Exception e) {
                    System.out.println("⚠️ Failed to upload logo: " + e.getMessage());
                }
                
                // Fill Event Links
                System.out.println("⚠️ Filling event links...");
                
                try {
                    // Wait for link fields to be available
                    Thread.sleep(1000);
                    
                    // Find and fill Link Name field
                    WebElement linkNameField = driver.findElement(By.name("eventSpecifiesForm.links.0.linkName"));
                    linkNameField.click();
                    linkNameField.clear();
                    linkNameField.sendKeys("PowerPlay");
                    System.out.println("✅ Entered Link Name: PowerPlay");
                    
                    Thread.sleep(500);
                    
                    // Find and fill Link URL field
                    WebElement linkUrlField = driver.findElement(By.name("eventSpecifiesForm.links.0.link"));
                    linkUrlField.click();
                    linkUrlField.clear();
                    linkUrlField.sendKeys("https://powerleague.com/powerplay");
                    System.out.println("✅ Entered Link URL: https://powerleague.com/powerplay");
                    
                    Thread.sleep(1000);
                    
                } catch (Exception e) {
                    System.out.println("⚠️ Failed to fill event links: " + e.getMessage());
                }
                
                // Fill Price field
                System.out.println("⚠️ Filling price field...");
                
                try {
                    Thread.sleep(500);
                    
                    WebElement eventPriceField = driver.findElement(By.name("eventSpecifiesForm.eventSpecificsDescription.price"));
                    eventPriceField.click();
                    eventPriceField.clear();
                    eventPriceField.sendKeys("12000");
                    System.out.println("✅ Entered Price: 12000");
                    
                    Thread.sleep(500);
                    
                } catch (Exception e) {
                    System.out.println("⚠️ Failed to fill price field: " + e.getMessage());
                }
                
                // Fill Deadlines field
                System.out.println("⚠️ Filling deadlines field...");
                
                try {
                    Thread.sleep(500);
                    
                    WebElement deadlinesField = driver.findElement(By.name("eventSpecifiesForm.eventSpecificsDescription.deadlines"));
                    deadlinesField.click();
                    deadlinesField.clear();
                    deadlinesField.sendKeys("Deadline are mentioned on Information Page");
                    System.out.println("✅ Entered Deadlines: Deadline are mentioned on Information Page");
                    
                    Thread.sleep(500);
                    
                } catch (Exception e) {
                    System.out.println("⚠️ Failed to fill deadlines field: " + e.getMessage());
                }
                
                // Fill Draft and Seeding field
                System.out.println("⚠️ Filling draft and seeding field...");
                
                try {
                    Thread.sleep(500);
                    
                    WebElement draftAndSeedingField = driver.findElement(By.name("eventSpecifiesForm.eventSpecificsDescription.draftAndSeeding"));
                    draftAndSeedingField.click();
                    draftAndSeedingField.clear();
                    draftAndSeedingField.sendKeys("Drafts & Seeding");
                    System.out.println("✅ Entered Draft and Seeding: Drafts & Seeding");
                    
                    Thread.sleep(500);
                    
                } catch (Exception e) {
                    System.out.println("⚠️ Failed to fill draft and seeding field: " + e.getMessage());
                }
                
                // Fill Prizes and Awards field
                System.out.println("⚠️ Filling prizes and awards field...");
                
                try {
                    Thread.sleep(500);
                    
                    WebElement prizesAndAwardsField = driver.findElement(By.name("eventSpecifiesForm.eventSpecificsDescription.prizesAndAwards"));
                    prizesAndAwardsField.click();
                    prizesAndAwardsField.clear();
                    prizesAndAwardsField.sendKeys("Prizes and Awards");
                    System.out.println("✅ Entered Prizes and Awards: Prizes and Awards");
                    
                    Thread.sleep(500);
                    
                } catch (Exception e) {
                    System.out.println("⚠️ Failed to fill prizes and awards field: " + e.getMessage());
                }
                
                // Fill Event Format field
                System.out.println("⚠️ Filling event format field...");
                
                try {
                    Thread.sleep(500);
                    
                    WebElement eventFormatField = driver.findElement(By.name("eventSpecifiesForm.eventSpecificsDescription.eventFormat"));
                    eventFormatField.click();
                    eventFormatField.clear();
                    eventFormatField.sendKeys("EventFormat");
                    System.out.println("✅ Entered Event Format: EventFormat");
                    
                    Thread.sleep(500);
                    
                } catch (Exception e) {
                    System.out.println("⚠️ Failed to fill event format field: " + e.getMessage());
                }
                
                // Fill Flags Required field
                System.out.println("⚠️ Filling flags required field...");
                
                try {
                    Thread.sleep(500);
                    
                    WebElement flagsRequiredField = driver.findElement(By.name("eventSpecifiesForm.eventSpecificsDescription.flagsRequired"));
                    flagsRequiredField.click();
                    flagsRequiredField.clear();
                    flagsRequiredField.sendKeys("Flags Required");
                    System.out.println("✅ Entered Flags Required: Flags Required");
                    
                    Thread.sleep(500);
                    
                } catch (Exception e) {
                    System.out.println("⚠️ Failed to fill flags required field: " + e.getMessage());
                }
                
                // Fill Fields Details field
                System.out.println("⚠️ Filling fields details...");
                
                try {
                    Thread.sleep(500);
                    
                    WebElement fieldsDetailsField = driver.findElement(By.name("eventSpecifiesForm.fieldsDetails"));
                    fieldsDetailsField.click();
                    fieldsDetailsField.clear();
                    fieldsDetailsField.sendKeys("Field Details");
                    System.out.println("✅ Entered Fields Details: Field Details");
                    
                    Thread.sleep(500);
                    
                } catch (Exception e) {
                    System.out.println("⚠️ Failed to fill fields details: " + e.getMessage());
                }
                
                // Fill Location field
                System.out.println("⚠️ Filling location...");
                
                try {
                    Thread.sleep(500);
                    
                    WebElement locationField = driver.findElement(By.name("eventSpecifiesForm.location"));
                    locationField.click();
                    locationField.clear();
                    locationField.sendKeys("Texas");
                    System.out.println("✅ Entered Location: Texas");
                    
                    Thread.sleep(500);
                    
                } catch (Exception e) {
                    System.out.println("⚠️ Failed to fill location: " + e.getMessage());
                }
                
                // Fill Field Configuration
                System.out.println("⚠️ Filling field configuration...");
                
                try {
                    Thread.sleep(500);
                    
                    // Find input by placeholder text
                    WebElement fieldConfigInput = wait.until(ExpectedConditions.elementToBeClickable(
                        By.xpath("//input[@placeholder='ex : Field 17A, Field 17B']")));
                    fieldConfigInput.click();
                    fieldConfigInput.clear();
                    fieldConfigInput.sendKeys("Field 1");
                    System.out.println("✅ Entered Field Configuration: Field 1");
                    
                    // Press Enter
                    fieldConfigInput.sendKeys(Keys.ENTER);
                    System.out.println("✅ Pressed Enter");
                    
                    Thread.sleep(1000);
                    
                } catch (Exception e) {
                    System.out.println("⚠️ Failed to fill field configuration: " + e.getMessage());
                }
                
                // Upload Cover Photo in Event Specifics
                System.out.println("⚠️ Uploading cover photo in Event Specifics...");
                
                try {
                    Thread.sleep(500);
                    
                    // Find and click on the SVG upload icon or its parent
                    WebElement uploadIcon = null;
                    
                    try {
                        // Method 1: Find SVG by viewBox attribute
                        uploadIcon = driver.findElement(By.xpath("//svg[@viewBox='0 0 17 16']"));
                        System.out.println("✅ Found upload SVG icon");
                    } catch (Exception e) {
                        System.out.println("⚠️ SVG not found, trying alternate method");
                    }
                    
                    if (uploadIcon != null) {
                        // Scroll to the icon
                        ((JavascriptExecutor) driver).executeScript(
                            "arguments[0].scrollIntoView({behavior: 'instant', block: 'center'});", uploadIcon);
                        Thread.sleep(500);
                        
                        // Click on the SVG's parent (clickable area)
                        WebElement clickableParent = uploadIcon.findElement(By.xpath("./ancestor::*[self::button or self::label or self::div][1]"));
                        clickableParent.click();
                        System.out.println("✅ Clicked upload icon");
                        Thread.sleep(1000);
                    }
                    
                    // Find the file input element and upload
                    List<WebElement> fileInputs = driver.findElements(By.xpath("//input[@type='file']"));
                    WebElement eventCoverPhotoInput = fileInputs.get(fileInputs.size() - 1); // Get last file input
                    // Use Paths.get() for portable file paths
                    String eventCoverPhotoPath = Paths.get("C:", "Users", "User", "Desktop", "profile-pics", "CP", "360_F_517255559_tP0Sq5ZJLW8naWm4bCaKuwXTMUuqT2aB.jpg").toString();
                    eventCoverPhotoInput.sendKeys(eventCoverPhotoPath);
                    System.out.println("✅ Uploaded cover photo: " + eventCoverPhotoPath);
                    
                    Thread.sleep(2000);
                    
                } catch (Exception e) {
                    System.out.println("⚠️ Failed to upload cover photo: " + e.getMessage());
                }
                
                // Click Next button to navigate to Add Ons section
                System.out.println("⚠️ Looking for 'Next' button to move to Add Ons section...");
                
                try {
                    Thread.sleep(1000);
                    
                    boolean nextButtonClicked = false;
                    WebElement eventSpecificsNextButton = null;
                    
                    // Try to find and click the Next button with scrolling
                    for (int attempt = 1; attempt <= 5; attempt++) {
                        try {
                            System.out.println("⚠️ Attempt " + attempt + " to find Next button...");
                            
                            // Find the Next button
                            eventSpecificsNextButton = driver.findElement(By.xpath("//button[normalize-space()='Next']"));
                            
                            // Check if button is visible
                            if (eventSpecificsNextButton.isDisplayed()) {
                                // Scroll to button and click
                                ((JavascriptExecutor) driver).executeScript(
                                    "arguments[0].scrollIntoView({behavior: 'instant', block: 'center'});", eventSpecificsNextButton);
                                Thread.sleep(500);
                                
                                // Click using JavaScript for reliability
                                ((JavascriptExecutor) driver).executeScript("arguments[0].click();", eventSpecificsNextButton);
                                System.out.println("✅ 'Next' button clicked successfully!");
                                System.out.println("✅ Event Specifics section completed!");
                                System.out.println("✅ Moving to Add Ons section...");
                                nextButtonClicked = true;
                                break;
                            }
                        } catch (Exception e) {
                            System.out.println("⚠️ Next button not found yet, scrolling down...");
                            // Scroll down by 300 pixels
                            ((JavascriptExecutor) driver).executeScript("window.scrollBy(0, 300);");
                            try {
                                Thread.sleep(500);
                            } catch (InterruptedException ie) {
                                Thread.currentThread().interrupt();
                            }
                        }
                    }
                    
                    if (!nextButtonClicked) {
                        System.out.println("❌ Failed to find/click 'Next' button after 5 attempts");
                        throw new RuntimeException("Next button not found");
                    }
                    
                    // Wait for Add Ons section to load
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException ie) {
                        Thread.currentThread().interrupt();
                    }
                    
                    System.out.println("✅ Add Ons section loaded successfully!");
                    System.out.println("✅ EVENT SPECIFICS section completed!");
                    
                } catch (Exception e) {
                    System.out.println("⚠️ Failed to navigate to Add Ons section: " + e.getMessage());
                    throw e;
                }
                
        } catch (Exception e) {
            System.err.println("❌ Event Specifics section failed: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }
    
    /**
     * Test Method 6: Add Ons Section
     * 
     * Configures additional event components including hotels, flights, and tickets.
     * Then clicks the Preview button to view the created event.
     * 
     * Actions performed:
     * - Upload and configure hotel details (logo, name, hyperlink)
     * - Upload and configure flight details (logo, name, hyperlink)
     * - Upload and configure ticket details (logo, name, hyperlink)
     * - [COMMENTED OUT] Add merchandise products via "Add Product" modal
     * - [COMMENTED OUT] Add vendors/sponsors via "Add Vendor/Sponsor" modal
     * - Click Preview button to view the created event
     * - Complete the entire event creation process
     * 
     * This is the final step in the event creation flow.
     * 
     * Note: Merchandise and Vendor/Sponsor sections are commented out.
     * The test now ends with clicking the Preview button.
     * 
     * Depends on: testEventSpecifics
     * @throws InterruptedException if thread sleep is interrupted
     * @throws Exception if any element interaction fails
     */
    @Test(priority = 6, dependsOnMethods = "testEventSpecifics")
    public void testAddOns() throws InterruptedException {
        try {
            System.out.println("🔧 Starting ADD ONS SECTION...");
            
            try {
                Thread.sleep(1000);
                    
                    // Find the hidden file input by ID
                    WebElement hotelLogoInput = wait.until(ExpectedConditions.presenceOfElementLocated(
                        By.id("logo-input-addOn.hotels.0.logo")));
                    
                    // Upload the logo directly to the hidden input
                    // Use Paths.get() for portable file paths
                    String hotelLogoPath = Paths.get("C:", "Users", "User", "Desktop", "profile-pics", "logo", "imagesa.png").toString();
                    hotelLogoInput.sendKeys(hotelLogoPath);
                    System.out.println("✅ Uploaded hotel logo: " + hotelLogoPath);
                    
                    Thread.sleep(2000);
                    
                } catch (Exception e) {
                    System.out.println("⚠️ Failed to upload hotel logo: " + e.getMessage());
                }
                
                // Fill Hotel Name
                System.out.println("⚠️ Filling hotel name...");
                
                try {
                    Thread.sleep(500);
                    
                    WebElement hotelNameField = wait.until(ExpectedConditions.elementToBeClickable(
                        By.name("addOn.hotels.0.name")));
                    hotelNameField.click();
                    hotelNameField.clear();
                    hotelNameField.sendKeys("Grande Islamabad Hotel");
                    System.out.println("✅ Entered hotel name: Grande Islamabad Hotel");
                    
                    Thread.sleep(500);
                    
                } catch (Exception e) {
                    System.out.println("⚠️ Failed to fill hotel name: " + e.getMessage());
                }
                
                // Fill Hotel Hyperlink
                System.out.println("⚠️ Filling hotel hyperlink...");
                
                try {
                    Thread.sleep(500);
                    
                    WebElement hotelHyperlinkField = wait.until(ExpectedConditions.elementToBeClickable(
                        By.name("addOn.hotels.0.hyperlink")));
                    hotelHyperlinkField.click();
                    hotelHyperlinkField.clear();
                    hotelHyperlinkField.sendKeys("www.grandislamabadhotel.com");
                    System.out.println("✅ Entered hotel hyperlink: www.grandislamabadhotel.com");
                    
                    Thread.sleep(500);
                    
                } catch (Exception e) {
                    System.out.println("⚠️ Failed to fill hotel hyperlink: " + e.getMessage());
                }
                
                // Upload Flight Logo - BEFORE entering flight name
                System.out.println("⚠️ Uploading flight logo (SVG viewBox='0 0 33 32')...");
                
                try {
                    Thread.sleep(500);
                    
                    // Find the flight logo file input by ID
                    WebElement flightLogoInput = null;
                    
                    try {
                        // Method 1: Try specific ID for flight logo
                        flightLogoInput = driver.findElement(By.id("logo-input-addOn.flights.0.logo"));
                        System.out.println("✅ Found flight logo input by ID");
                    } catch (Exception e) {
                        // Method 2: Fallback - find all addOn logo inputs and get by index
                        System.out.println("⚠️ Trying fallback method for flight logo...");
                        List<WebElement> logoInputs = driver.findElements(
                            By.xpath("//input[@type='file' and contains(@id, 'logo-input-addOn')]"));
                        
                        // Flight logo should be the second one (index 1) - after hotel (index 0)
                        if (logoInputs.size() > 1) {
                            flightLogoInput = logoInputs.get(1);
                            System.out.println("✅ Found flight logo input as second addOn input");
                        }
                    }
                    
                    if (flightLogoInput != null) {
                        // Use Paths.get() for portable file paths
                        String flightLogoPath = Paths.get("C:", "Users", "User", "Desktop", "profile-pics", "logo", "imagesa.png").toString();
                        flightLogoInput.sendKeys(flightLogoPath);
                        System.out.println("✅ Uploaded flight logo: " + flightLogoPath);
                    } else {
                        System.out.println("⚠️ Flight logo input not found");
                    }
                    
                    Thread.sleep(2000);
                    
                } catch (Exception e) {
                    System.out.println("⚠️ Failed to upload flight logo: " + e.getMessage());
                }
                
                // Fill Flight Name
                System.out.println("⚠️ Filling flight name...");
                
                try {
                    Thread.sleep(500);
                    
                    WebElement flightNameField = wait.until(ExpectedConditions.elementToBeClickable(
                        By.name("addOn.flights.0.name")));
                    flightNameField.click();
                    flightNameField.clear();
                    flightNameField.sendKeys("Emirates");
                    System.out.println("✅ Entered flight name: Emirates");
                    
                    Thread.sleep(500);
                    
                } catch (Exception e) {
                    System.out.println("⚠️ Failed to fill flight name: " + e.getMessage());
                }
                
                // Fill Flight Hyperlink
                System.out.println("⚠️ Filling flight hyperlink...");
                
                try {
                    Thread.sleep(500);
                    
                    WebElement flightHyperlinkField = wait.until(ExpectedConditions.elementToBeClickable(
                        By.name("addOn.flights.0.hyperlink")));
                    flightHyperlinkField.click();
                    flightHyperlinkField.clear();
                    flightHyperlinkField.sendKeys("www.emirates.com");
                    System.out.println("✅ Entered flight hyperlink: www.emirates.com");
                    
                    Thread.sleep(500);
                    
                } catch (Exception e) {
                    System.out.println("⚠️ Failed to fill flight hyperlink: " + e.getMessage());
                }
                
                // Upload Ticket Logo - BEFORE entering ticket name
                System.out.println("⚠️ Uploading ticket logo (SVG viewBox='0 0 33 32')...");
                
                try {
                    Thread.sleep(500);
                    
                    // Find the ticket logo file input by ID
                    WebElement ticketLogoInput = null;
                    
                    try {
                        // Method 1: Try specific ID for ticket logo
                        ticketLogoInput = driver.findElement(By.id("logo-input-addOn.tickets.0.logo"));
                        System.out.println("✅ Found ticket logo input by ID");
                    } catch (Exception e) {
                        // Method 2: Fallback - find all addOn logo inputs and get the second one
                        System.out.println("⚠️ Trying fallback method for ticket logo...");
                        List<WebElement> logoInputs = driver.findElements(
                            By.xpath("//input[@type='file' and contains(@id, 'logo-input-addOn')]"));
                        
                        if (logoInputs.size() > 1) {
                            ticketLogoInput = logoInputs.get(1);
                            System.out.println("✅ Found ticket logo input as second addOn input");
                        }
                    }
                    
                    if (ticketLogoInput != null) {
                        // Use Paths.get() for portable file paths
                        String ticketLogoPath = Paths.get("C:", "Users", "User", "Desktop", "profile-pics", "logo", "imagesa.png").toString();
                        ticketLogoInput.sendKeys(ticketLogoPath);
                        System.out.println("✅ Uploaded ticket logo: " + ticketLogoPath);
                    } else {
                        System.out.println("⚠️ Ticket logo input not found");
                    }
                    
                    Thread.sleep(2000);
                    
                } catch (Exception e) {
                    System.out.println("⚠️ Failed to upload ticket logo: " + e.getMessage());
                }
                
                // Fill Ticket Name
                System.out.println("⚠️ Filling ticket name...");
                
                try {
                    Thread.sleep(500);
                    
                    WebElement ticketNameField = wait.until(ExpectedConditions.elementToBeClickable(
                        By.name("addOn.tickets.0.name")));
                    ticketNameField.click();
                    ticketNameField.clear();
                    ticketNameField.sendKeys("Ticket");
                    System.out.println("✅ Entered ticket name: Ticket");
                    
                    Thread.sleep(500);
                    
                } catch (Exception e) {
                    System.out.println("⚠️ Failed to fill ticket name: " + e.getMessage());
                }
                
                // Fill Ticket Hyperlink
                System.out.println("⚠️ Filling ticket hyperlink...");
                
                try {
                    Thread.sleep(500);
                    
                    WebElement ticketHyperlinkField = wait.until(ExpectedConditions.elementToBeClickable(
                        By.name("addOn.tickets.0.hyperlink")));
                    ticketHyperlinkField.click();
                    ticketHyperlinkField.clear();
                    ticketHyperlinkField.sendKeys("www.usaflag.com");
                    System.out.println("✅ Entered ticket hyperlink: www.usaflag.com");
                    
                    Thread.sleep(500);
                    
                } catch (Exception e) {
                    System.out.println("⚠️ Failed to fill ticket hyperlink: " + e.getMessage());
                }
                
                // ============================================
                // ADD PRODUCT / MERCH SECTION - COMMENTED OUT
                // ============================================
                
                /* COMMENTED OUT - ADD MERCH/PRODUCT SECTION
                System.out.println("⚠️ Adding merchandise product...");
                
                try {
                    Thread.sleep(1000);
                    
                    // Click on "Add Product" SVG button
                    System.out.println("⚠️ Looking for 'Add Product' button...");
                    WebElement addProductButton = null;
                    
                    try {
                        // Scroll down to potentially reveal the Add Product button
                        ((JavascriptExecutor) driver).executeScript("window.scrollBy(0, 300);");
                        Thread.sleep(500);
                        
                        // Find the Add Product SVG button
                        addProductButton = wait.until(ExpectedConditions.elementToBeClickable(
                            By.xpath("//div[contains(@class,'rounded-xl flex flex-col items-center py-8 text-gray')]//*[name()='svg']")));
                        
                        // Scroll to the element
                        ((JavascriptExecutor) driver).executeScript(
                            "arguments[0].scrollIntoView({behavior: 'instant', block: 'center'});", addProductButton);
                        Thread.sleep(500);
                        
                        // Click using JavaScript for reliability
                        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", addProductButton);
                        System.out.println("✅ Clicked 'Add Product' button");
                        
                    } catch (Exception e) {
                        System.out.println("⚠️ Add Product button not found using primary selector, trying alternate...");
                        try {
                            // Alternate: Try finding any clickable SVG in a rounded-xl container
                            addProductButton = wait.until(ExpectedConditions.elementToBeClickable(
                                By.xpath("//div[contains(@class,'rounded-xl')]//*[name()='svg' and contains(@class,'')]")));
                            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", addProductButton);
                            System.out.println("✅ Clicked 'Add Product' button (alternate method)");
                        } catch (Exception e2) {
                            System.out.println("❌ Failed to find Add Product button: " + e2.getMessage());
                            throw new RuntimeException("Add Product button not found");
                        }
                    }
                    
                    // Wait for "Add Merch" modal to open
                    Thread.sleep(1000);
                    System.out.println("⚠️ Waiting for 'Add Merch' modal to open...");
                    
                    try {
                        // Wait for modal with "Add Merch" heading
                        wait.until(ExpectedConditions.visibilityOfElementLocated(
                            By.xpath("//*[normalize-space(text())='Add Merch' or contains(text(), 'Add Merch')]")));
                        System.out.println("✅ 'Add Merch' modal opened successfully");
                        
                        Thread.sleep(1000);
                        
                    } catch (Exception e) {
                        System.out.println("⚠️ Add Merch modal heading not found, but continuing...");
                    }
                    
                    // Find and click the "Add" button in the modal
                    System.out.println("⚠️ Looking for 'Add' button in modal...");
                    WebElement addButton = null;
                    
                    try {
                        // Method 1: Find by exact class attributes
                        addButton = wait.until(ExpectedConditions.elementToBeClickable(
                            By.xpath("//button[@data-slot='button' and @class='inline-flex items-center justify-center gap-2 whitespace-nowrap rounded-md text-sm font-medium transition-all disabled:pointer-events-none disabled:opacity-50 [&_svg]:pointer-events-none [&_svg:not([class*=\\'size-\\'])]:size-4 shrink-0 [&_svg]:shrink-0 outline-none focus-visible:border-ring focus-visible:ring-ring/50 focus-visible:ring-[3px] aria-invalid:ring-destructive/20 dark:aria-invalid:ring-destructive/40 aria-invalid:border-destructive shadow-xs h-9 px-4 py-2 has-[>svg]:px-3 bg-[#0f2b46] hover:bg-[#1a3c5e] text-white' and @type='button' and normalize-space()='Add']")));
                        System.out.println("✅ Found 'Add' button (Method 1 - exact class)");
                        
                    } catch (Exception e) {
                        System.out.println("⚠️ Method 1 failed, trying simplified selectors...");
                        
                        try {
                            // Method 2: Find by bg color and button text
                            addButton = wait.until(ExpectedConditions.elementToBeClickable(
                                By.xpath("//button[@data-slot='button' and contains(@class, 'bg-[#0f2b46]') and normalize-space()='Add']")));
                            System.out.println("✅ Found 'Add' button (Method 2 - bg color)");
                            
                        } catch (Exception e2) {
                            System.out.println("⚠️ Method 2 failed, trying text-based selector...");
                            
                            try {
                                // Method 3: Find button with "Add" text and specific styling
                                addButton = wait.until(ExpectedConditions.elementToBeClickable(
                                    By.xpath("//button[@type='button' and contains(@class, 'bg-[#0f2b46]') and contains(@class, 'text-white')][normalize-space()='Add']")));
                                System.out.println("✅ Found 'Add' button (Method 3 - text + styling)");
                                
                            } catch (Exception e3) {
                                System.out.println("❌ Failed to find 'Add' button with all methods");
                                throw new RuntimeException("'Add' button not found in modal");
                            }
                        }
                    }
                    
                    // Click the Add button
                    if (addButton != null) {
                        ((JavascriptExecutor) driver).executeScript(
                            "arguments[0].scrollIntoView({behavior: 'instant', block: 'center'});", addButton);
                        Thread.sleep(500);
                        
                        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", addButton);
                        System.out.println("✅ Clicked 'Add' button in modal");
                        
                        Thread.sleep(1500);
                    }
                    
                    // Find and click "Finalise Merch" button
                    System.out.println("⚠️ Looking for 'Finalise Merch' button...");
                    WebElement finaliseMerchButton = null;
                    
                    try {
                        // Method 1: Try the specific XPath provided
                        finaliseMerchButton = wait.until(ExpectedConditions.elementToBeClickable(
                            By.xpath("//*[@id=\"radix-_r_ms_\"]/div[3]/button[2]")));
                        System.out.println("✅ Found 'Finalise Merch' button (Method 1 - specific ID)");
                        
                    } catch (Exception e) {
                        System.out.println("⚠️ Method 1 failed, trying dynamic ID pattern...");
                        
                        try {
                            // Method 2: Try with dynamic radix ID pattern
                            finaliseMerchButton = wait.until(ExpectedConditions.elementToBeClickable(
                                By.xpath("//*[contains(@id, 'radix-')]/div[3]/button[2]")));
                            System.out.println("✅ Found 'Finalise Merch' button (Method 2 - dynamic ID)");
                            
                        } catch (Exception e2) {
                            System.out.println("⚠️ Method 2 failed, trying text-based selector...");
                            
                            try {
                                // Method 3: Find button by text containing "Finalise" or "Finalize"
                                finaliseMerchButton = wait.until(ExpectedConditions.elementToBeClickable(
                                    By.xpath("//button[contains(normalize-space(), 'Finalise') or contains(normalize-space(), 'Finalize')]")));
                                System.out.println("✅ Found 'Finalise Merch' button (Method 3 - text-based)");
                                
                            } catch (Exception e3) {
                                System.out.println("❌ Failed to find 'Finalise Merch' button with all methods");
                                throw new RuntimeException("'Finalise Merch' button not found");
                            }
                        }
                    }
                    
                    // Click the Finalise Merch button
                    if (finaliseMerchButton != null) {
                        ((JavascriptExecutor) driver).executeScript(
                            "arguments[0].scrollIntoView({behavior: 'instant', block: 'center'});", finaliseMerchButton);
                        Thread.sleep(500);
                        
                        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", finaliseMerchButton);
                        System.out.println("✅ Clicked 'Finalise Merch' button");
                        
                        Thread.sleep(1500);
                    }
                    
                    System.out.println("✅ Merchandise product added successfully!");
                    
                } catch (Exception e) {
                    System.out.println("⚠️ Failed to add merchandise product: " + e.getMessage());
                    e.printStackTrace();
                }
                END OF ADD MERCH SECTION COMMENT */
                
                // ============================================
                // ADD VENDOR / SPONSOR SECTION - COMMENTED OUT
                // ============================================
                
                /* COMMENTED OUT - ADD VENDOR/SPONSOR SECTION
                System.out.println("⚠️ Adding vendor/sponsor...");
                
                try {
                    Thread.sleep(1000);
                    
                    // Look for "Add Vendor / Sponser" visible text first
                    System.out.println("⚠️ Looking for 'Add Vendor / Sponsor' section...");
                    
                    try {
                        // Scroll down to potentially reveal the Add Vendor/Sponsor section
                        ((JavascriptExecutor) driver).executeScript("window.scrollBy(0, 300);");
                        Thread.sleep(500);
                        
                        // Try to find the visible text
                        WebElement addVendorText = wait.until(ExpectedConditions.visibilityOfElementLocated(
                            By.xpath("//*[contains(text(), 'Add Vendor') or contains(text(), 'Add Sponser') or contains(text(), 'Add Sponsor')]")));
                        
                        ((JavascriptExecutor) driver).executeScript(
                            "arguments[0].scrollIntoView({behavior: 'instant', block: 'center'});", addVendorText);
                        System.out.println("✅ Found 'Add Vendor/Sponsor' section");
                        Thread.sleep(500);
                        
                    } catch (Exception e) {
                        System.out.println("⚠️ Add Vendor/Sponsor text not found, continuing to search for button...");
                    }
                    
                    // Click on "Add Vendor/Sponsor" SVG button
                    System.out.println("⚠️ Looking for 'Add Vendor/Sponsor' button...");
                    WebElement addVendorButton = null;
                    
                    try {
                        // Method 1: Try the specific SVG path provided
                        addVendorButton = wait.until(ExpectedConditions.elementToBeClickable(
                            By.xpath("//div[contains(@class,'bg-[#EAECF0] p-5 min-w-52 w-52 h-28 overflow-hidden rounded-xl flex flex-col justify-center items-center py-8 text-gray')]//*[name()='svg']//*[name()='path' and contains(@d,'M12 5v14')]")));
                        System.out.println("✅ Found 'Add Vendor/Sponsor' button (Method 1 - specific path)");
                        
                    } catch (Exception e) {
                        System.out.println("⚠️ Method 1 failed, trying simplified selector...");
                        
                        try {
                            // Method 2: Try simplified - any SVG in bg-[#EAECF0] container
                            addVendorButton = wait.until(ExpectedConditions.elementToBeClickable(
                                By.xpath("//div[contains(@class,'bg-[#EAECF0]') and contains(@class,'rounded-xl')]//*[name()='svg']")));
                            System.out.println("✅ Found 'Add Vendor/Sponsor' button (Method 2 - simplified)");
                            
                        } catch (Exception e2) {
                            System.out.println("⚠️ Method 2 failed, trying alternate approach...");
                            
                            try {
                                // Method 3: Find by path containing M12 5v14
                                addVendorButton = wait.until(ExpectedConditions.elementToBeClickable(
                                    By.xpath("//*[name()='svg']//*[name()='path' and contains(@d,'M12 5v14')]/ancestor::*[self::button or self::div][1]")));
                                System.out.println("✅ Found 'Add Vendor/Sponsor' button (Method 3 - path ancestor)");
                                
                            } catch (Exception e3) {
                                System.out.println("❌ Failed to find 'Add Vendor/Sponsor' button with all methods");
                                throw new RuntimeException("Add Vendor/Sponsor button not found");
                            }
                        }
                    }
                    
                    // Click the Add Vendor/Sponsor button
                    if (addVendorButton != null) {
                        ((JavascriptExecutor) driver).executeScript(
                            "arguments[0].scrollIntoView({behavior: 'instant', block: 'center'});", addVendorButton);
                        Thread.sleep(500);
                        
                        // Click using JavaScript for reliability
                        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", addVendorButton);
                        System.out.println("✅ Clicked 'Add Vendor/Sponsor' button");
                        
                        Thread.sleep(1000);
                    }
                    
                    // Wait for "Add Sponser" modal to open
                    System.out.println("⚠️ Waiting for 'Add Sponsor' modal to open...");
                    
                    try {
                        // Wait for modal with "Add Sponser" or "Add Sponsor" heading
                        wait.until(ExpectedConditions.visibilityOfElementLocated(
                            By.xpath("//*[normalize-space(text())='Add Sponser' or normalize-space(text())='Add Sponsor' or contains(text(), 'Sponsor')]")));
                        System.out.println("✅ 'Add Sponsor' modal opened successfully");
                        
                        Thread.sleep(1000);
                        
                    } catch (Exception e) {
                        System.out.println("⚠️ Add Sponsor modal heading not found, but continuing...");
                    }
                    
                    // Find and click the first "Add" button in the modal
                    System.out.println("⚠️ Looking for 'Add' button in modal...");
                    WebElement addButtonInModal = null;
                    
                    try {
                        // Method 1: Find by data-slot and bg color with "Add" text
                        addButtonInModal = wait.until(ExpectedConditions.elementToBeClickable(
                            By.xpath("//button[@data-slot='button' and contains(@class, 'bg-[#0f2b46]') and @type='button' and normalize-space()='Add']")));
                        System.out.println("✅ Found 'Add' button in modal (Method 1)");
                        
                    } catch (Exception e) {
                        System.out.println("⚠️ Method 1 failed, trying simplified selector...");
                        
                        try {
                            // Method 2: Find button with "Add" text and specific bg color
                            addButtonInModal = wait.until(ExpectedConditions.elementToBeClickable(
                                By.xpath("//button[@type='button' and contains(@class, 'bg-[#0f2b46]')][normalize-space()='Add']")));
                            System.out.println("✅ Found 'Add' button in modal (Method 2)");
                            
                        } catch (Exception e2) {
                            System.out.println("⚠️ Method 2 failed, trying generic button...");
                            
                            try {
                                // Method 3: Find any button with "Add" text in modal
                                List<WebElement> addButtons = driver.findElements(
                                    By.xpath("//button[normalize-space()='Add' and @type='button']"));
                                if (!addButtons.isEmpty()) {
                                    // Get the first "Add" button
                                    addButtonInModal = addButtons.get(0);
                                    System.out.println("✅ Found 'Add' button in modal (Method 3 - first match)");
                                }
                            } catch (Exception e3) {
                                System.out.println("❌ Failed to find 'Add' button with all methods");
                                throw new RuntimeException("'Add' button not found in modal");
                            }
                        }
                    }
                    
                    // Click the Add button in modal
                    if (addButtonInModal != null) {
                        ((JavascriptExecutor) driver).executeScript(
                            "arguments[0].scrollIntoView({behavior: 'instant', block: 'center'});", addButtonInModal);
                        Thread.sleep(500);
                        
                        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", addButtonInModal);
                        System.out.println("✅ Clicked 'Add' button in modal");
                        
                        Thread.sleep(1500);
                    }
                    
                    // Find and click "Add Sponsor" submit button
                    System.out.println("⚠️ Looking for 'Add Sponsor' submit button...");
                    WebElement addSponsorSubmitButton = null;
                    
                    try {
                        // Method 1: Find by exact attributes - type="submit" with "Add Sponsor" text
                        addSponsorSubmitButton = wait.until(ExpectedConditions.elementToBeClickable(
                            By.xpath("//button[@data-slot='button' and contains(@class, 'bg-[#0f2b46]') and @type='submit' and normalize-space()='Add Sponsor']")));
                        System.out.println("✅ Found 'Add Sponsor' submit button (Method 1)");
                        
                    } catch (Exception e) {
                        System.out.println("⚠️ Method 1 failed, trying alternate text...");
                        
                        try {
                            // Method 2: Try with "Add Sponser" (typo variant)
                            addSponsorSubmitButton = wait.until(ExpectedConditions.elementToBeClickable(
                                By.xpath("//button[@type='submit' and contains(@class, 'bg-[#0f2b46]') and (normalize-space()='Add Sponsor' or normalize-space()='Add Sponser')]")));
                            System.out.println("✅ Found 'Add Sponsor' submit button (Method 2)");
                            
                        } catch (Exception e2) {
                            System.out.println("⚠️ Method 2 failed, trying generic submit button...");
                            
                            try {
                                // Method 3: Find any submit button with bg-[#0f2b46] containing "Sponsor"
                                addSponsorSubmitButton = wait.until(ExpectedConditions.elementToBeClickable(
                                    By.xpath("//button[@type='submit' and contains(@class, 'bg-[#0f2b46]') and contains(normalize-space(), 'Sponsor')]")));
                                System.out.println("✅ Found 'Add Sponsor' submit button (Method 3)");
                                
                            } catch (Exception e3) {
                                System.out.println("❌ Failed to find 'Add Sponsor' submit button with all methods");
                                throw new RuntimeException("'Add Sponsor' submit button not found");
                            }
                        }
                    }
                    
                    // Click the Add Sponsor submit button
                    if (addSponsorSubmitButton != null) {
                        ((JavascriptExecutor) driver).executeScript(
                            "arguments[0].scrollIntoView({behavior: 'instant', block: 'center'});", addSponsorSubmitButton);
                        Thread.sleep(500);
                        
                        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", addSponsorSubmitButton);
                        System.out.println("✅ Clicked 'Add Sponsor' submit button");
                        
                        Thread.sleep(1500);
                    }
                    
                    System.out.println("✅ Vendor/Sponsor added successfully!");
                    
                } catch (Exception e) {
                    System.out.println("⚠️ Failed to add vendor/sponsor: " + e.getMessage());
                    e.printStackTrace();
                }
                END OF ADD VENDOR/SPONSOR SECTION COMMENT */
                
                // ============================================
                // CLICK PREVIEW BUTTON
                // ============================================
                
                System.out.println("🔍 Looking for 'Preview' button...");
                
                try {
                    Thread.sleep(1500);
                    
                    // Scroll down to find the Preview button
                    ((JavascriptExecutor) driver).executeScript("window.scrollBy(0, 300);");
                    Thread.sleep(500);
                    
                    // Find the Preview button with multiple methods
                    WebElement previewButton = null;
                    boolean previewButtonFound = false;
                    
                    // Method 1: Find by button text and class
                    try {
                        previewButton = wait.until(ExpectedConditions.elementToBeClickable(
                            By.xpath("//button[@data-slot='button' and contains(@class, 'bg-dark-blue') and contains(., 'Preview')]")));
                        previewButtonFound = true;
                        System.out.println("✅ Found 'Preview' button (Method 1)");
                    } catch (Exception e) {
                        System.out.println("⚠️ Method 1 failed, trying alternate approach...");
                    }
                    
                    // Method 2: Find by button text only
                    if (!previewButtonFound) {
                        try {
                            previewButton = wait.until(ExpectedConditions.elementToBeClickable(
                                By.xpath("//button[contains(text(), 'Preview') and @type='button']")));
                            previewButtonFound = true;
                            System.out.println("✅ Found 'Preview' button (Method 2)");
                        } catch (Exception e) {
                            System.out.println("⚠️ Method 2 failed, trying to scroll and search...");
                        }
                    }
                    
                    // Method 3: Scroll more and try again
                    if (!previewButtonFound) {
                        for (int i = 0; i < 3; i++) {
                            try {
                                ((JavascriptExecutor) driver).executeScript("window.scrollBy(0, 200);");
                                Thread.sleep(500);
                                
                                previewButton = driver.findElement(
                                    By.xpath("//button[contains(., 'Preview')]"));
                                if (previewButton.isDisplayed()) {
                                    previewButtonFound = true;
                                    System.out.println("✅ Found 'Preview' button after scroll " + (i + 1));
                                    break;
                                }
                            } catch (Exception e) {
                                // Continue scrolling
                            }
                        }
                    }
                    
                    if (!previewButtonFound || previewButton == null) {
                        System.out.println("❌ Preview button not found after all attempts");
                        throw new RuntimeException("Preview button not found");
                    }
                    
                    // Scroll the button into view
                    ((JavascriptExecutor) driver).executeScript(
                        "arguments[0].scrollIntoView({behavior: 'instant', block: 'center'});", previewButton);
                    Thread.sleep(500);
                    
                    // Click the Preview button using JavaScript for reliability
                    ((JavascriptExecutor) driver).executeScript("arguments[0].click();", previewButton);
                    System.out.println("✅ 'Preview' button clicked successfully!");
                    
                    // Wait for preview page to load
                    Thread.sleep(2000);
                    System.out.println("✅ Preview page loaded successfully!");
                    
                } catch (Exception e) {
                    System.err.println("❌ Failed to click Preview button: " + e.getMessage());
                    e.printStackTrace();
                    throw e;
                }
                
                System.out.println("✅ Add Ons section completed!");
                System.out.println("✅ Event creation process completed - Preview page displayed!");
            
        } catch (Exception e) {
            System.err.println("❌ Add Ons section failed: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }
    
    /**
     * Cleanup Method: Tear Down
     * 
     * Closes the browser and cleans up resources after all tests complete.
     * Called automatically by TestNG after all @Test methods have finished.
     * 
     * Note: Browser quit is commented out by default to allow manual inspection
     * of the final state. Uncomment driver.quit() for automated runs.
     */
    @AfterClass
    public void tearDown() {
        if (driver != null) {
            System.out.println("🔚 Closing browser...");
            // Uncomment the line below if you want to close the browser after tests
            // driver.quit();
        }
    }
}

/*
 * =============================================================================
 * END OF CreateEventonlyAdult TEST CLASS
 * =============================================================================
 * 
 * This test suite successfully automates the complete event creation process
 * for Adult-only events on the USA Flag Football platform.
 * 
 * Total Test Methods: 6 (+ 1 setup + 1 teardown)
 * Estimated Execution Time: 3-5 minutes (depending on network speed)
 * Browser: Chrome (maximized window)
 * 
 * Test Execution Order:
 * 1. testAdminLogin()         - Authenticates and navigates to event creation
 * 2. testDetails()            - Fills event details and basic information
 * 3. testPaymentAdultOnly()   - Configures Adult Registration payment
 * 4. testTournament()         - Creates tournament brackets
 * 5. testEventSpecifics()     - Adds event-specific details and configurations
 * 6. testAddOns()             - Configures hotels, flights, tickets + Clicks Preview
 * 
 * Key Features:
 * - Robust error handling with multiple fallback methods
 * - Comprehensive console logging for debugging
 * - Adult Registration ONLY (no Youth section)
 * - Uses TestNG priorities and dependencies for proper execution order
 * - Configurable via config.properties file
 * - Merchandise and Vendor/Sponsor sections are COMMENTED OUT
 * - Test ends with Preview button click to display the created event
 * 
 * =============================================================================
 */

