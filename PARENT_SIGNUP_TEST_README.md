# Parent Signup Test with CSV Data Integration

This document describes the enhanced Parent Signup Test that has been converted from JavaScript to Java TestNG format with CSV data integration.

## Overview

The `ParentSignupTest.java` file provides a comprehensive test suite for the USA Flag application's parent signup process. It reads user data from a CSV file and executes the complete signup flow for each user.

## Features

- **TestNG Framework**: Uses TestNG annotations for proper test lifecycle management
- **CSV Data Integration**: Reads user data from `user_data.csv` file
- **DataProvider**: Implements TestNG DataProvider for parameterized testing
- **Enhanced Selenium**: Uses WebDriverWait for robust element interactions
- **Comprehensive Flow**: Tests the complete parent signup process including email verification
- **Error Handling**: Robust error handling with detailed logging

## File Structure

```
src/test/java/com/usaflag/
├── ParentSignupTest.java          # Main optimized test class
├── ParentLoginTest.java           # Existing parent login test
└── AdminLoginTest.java            # Existing admin login test

user_data.csv                      # CSV file with test user data
```

## CSV Data Format

The `user_data.csv` file should contain the following columns:

```csv
First Name,Last Name,Email,Phone,Date of Birth,Password
James,Anderson,james.anderson@yopmail.com,(215) 443-8821,03/15/1985,12345678
Sarah,Mitchell,sarah.mitchell@yopmail.com,(267) 555-2347,08/22/1990,12345678
...
```

## Test Flow

The test executes a simple, optimized flow using the first user from the CSV file:

1. **Setup**: Initialize WebDriver with optimized Chrome options
2. **Data Loading**: Read first user data from `user_data.csv`
3. **Test Execution**: Open login page and click "Create An Account" (JavaScript click)
4. **Verification**: Verify single window maintained
5. **Cleanup**: Keep browser open for inspection

## Running the Tests

### Method 1: Using Maven

```bash
# Run the optimized parent signup test
mvn test -Dtest=ParentSignupTest#testParentSignupWithCSVData

# Run all tests including ParentSignupTest
mvn test -Dtest=ParentSignupTest
```

### Method 2: Using TestNG XML

```bash
# Run using testng.xml (includes all tests)
mvn test -DsuiteXmlFile=testng.xml
```

### Method 3: IDE Integration

1. Open the project in your IDE (IntelliJ IDEA, Eclipse, etc.)
2. Right-click on `ParentSignupTest.java`
3. Select "Run ParentSignupTest" or run the specific test method

## Configuration

### WebDriver Configuration

The test uses Chrome WebDriver with single window mode and the following configuration:
- **Browser**: Chrome
- **Window**: Maximized, Single Window Mode
- **Implicit Wait**: 10 seconds
- **Explicit Wait**: 15 seconds
- **No New Windows**: Prevents opening additional windows/tabs
- **JavaScript Click**: Uses JavaScript click to prevent new windows

### Test Parameters

You can modify the following constants in `ParentSignupTest.java`:

```java
private static final String LOGIN_URL = "https://staging-usaflag-playerportal.azurewebsites.net/login";
private static final String CSV_FILE_PATH = "user_data.csv";
private static final int IMPLICIT_WAIT = 10;
private static final int EXPLICIT_WAIT = 15;
```

## Dependencies

The following dependencies are required (already included in `pom.xml`):

- **Selenium WebDriver**: 4.15.0
- **TestNG**: 7.8.0
- **WebDriverManager**: 5.6.2
- **OpenCSV**: 5.8

## Test Data Management

### Adding New Users

To add new test users, simply add rows to the `user_data.csv` file:

```csv
NewUser,LastName,newuser@yopmail.com,(215) 123-4567,01/01/1990,12345678
```

### Using Specific User Data

The test will run for all users in the CSV file. If you want to test with specific users, you can:

1. Create a separate CSV file with only the desired users
2. Update the `CSV_FILE_PATH` constant
3. Or modify the CSV file temporarily

## Email Verification

The test includes automatic email verification using YOPmail:

1. Opens YOPmail in a new browser tab
2. Enters the email address from the signup form
3. Retrieves the verification code from the email
4. Returns to the verification page and enters the code

## Error Handling

The test includes comprehensive error handling:

- **Element Not Found**: Uses explicit waits with proper timeouts
- **CSV Reading Errors**: Falls back to default test data
- **Email Verification**: Uses fallback verification code if email parsing fails
- **Country Selection**: Multiple approaches for dropdown interaction

## Logging

The test provides detailed console output with emojis for easy identification:

- 🔧 Setup operations
- 🔍 Test steps
- ✅ Success messages
- ❌ Error messages
- ⚠️ Warning messages
- 📧 Email operations
- 🌐 Browser operations

## Browser Behavior

By default, the browser remains open after test completion for manual inspection. To close the browser automatically, uncomment the `driver.quit()` line in the `tearDown()` method.

## Troubleshooting

### Common Issues

1. **CSV File Not Found**: Ensure `user_data.csv` is in the project root directory
2. **Element Not Found**: Check if the website structure has changed
3. **Email Verification Fails**: Verify YOPmail is accessible and email format is correct
4. **Country Selection Issues**: The test includes multiple fallback approaches

### Debug Mode

To run in debug mode with more detailed logging:

1. Set breakpoints in your IDE
2. Run the test in debug mode
3. Inspect element states and page content

## Best Practices

1. **Data Management**: Keep test data in CSV files for easy maintenance
2. **Wait Strategies**: Use explicit waits instead of Thread.sleep()
3. **Error Handling**: Always include proper exception handling
4. **Test Isolation**: Each test method should be independent
5. **Cleanup**: Properly manage WebDriver instances

## Future Enhancements

Potential improvements for the test suite:

1. **Screenshot Capture**: Add screenshot capture on test failures
2. **Report Generation**: Integrate with ExtentReports for detailed reporting
3. **Parallel Execution**: Run tests in parallel for faster execution
4. **API Integration**: Add API calls for data setup/cleanup
5. **Cross-Browser Testing**: Support for multiple browsers
