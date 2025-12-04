# USA Flag Selenium Test Automation

Selenium WebDriver automation tests for USA Flag application signup flows (Youth, Adult, and Parent).

## Prerequisites

### Java Development Kit (JDK)
This project requires **JDK 17**. You have several options to install it:

#### Option 1: Download from Oracle (Recommended)
1. Visit [Oracle JDK 17 Downloads](https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html)
2. Download JDK 17 for your operating system
3. Install and set `JAVA_HOME` environment variable

#### Option 2: Use OpenJDK (Free Alternative)
1. Visit [Adoptium (Eclipse Temurin)](https://adoptium.net/temurin/releases/?version=17)
2. Download JDK 17 (LTS) for your OS
3. Install and set `JAVA_HOME` environment variable

#### Option 3: Use Package Manager
- **Windows (Chocolatey)**: `choco install openjdk17`
- **macOS (Homebrew)**: `brew install openjdk@17`
- **Linux (apt)**: `sudo apt install openjdk-17-jdk`

### Verify Installation
```bash
java -version
# Should show: openjdk version "17.x.x" or java version "17.x.x"
```

### Maven
This project uses Maven Wrapper (`mvnw`), so Maven installation is optional. If you prefer to install Maven:
- Download from [Apache Maven](https://maven.apache.org/download.cgi)
- Or use package manager: `choco install maven` (Windows) / `brew install maven` (macOS)

## Setup Instructions

1. **Clone the repository**
   ```bash
   git clone https://github.com/adilshahzadititans/USAFlag-Youth-Adult-Parent.git
   cd USAFlag-Youth-Adult-Parent
   ```

2. **Verify Java is installed**
   ```bash
   java -version
   ```

3. **Run tests using Maven Wrapper**
   ```bash
   # Windows
   .\mvnw.cmd test
   
   # Linux/macOS
   ./mvnw test
   ```

## Project Structure

```
├── src/
│   ├── main/
│   │   └── resources/
│   │       └── config.properties
│   └── test/
│       └── java/
│           └── com/
│               └── usaflag/
│                   ├── AdultSignupTest.java
│                   ├── YouthSignupTest.java
│                   ├── ParentSignupTest.java
│                   └── ...
├── adult_user_data.csv
├── youth_user_data.csv
├── user_data.csv
├── pom.xml
└── testng.xml
```

## Running Tests

### Run All Tests
```bash
.\mvnw.cmd test
```

### Run Specific Test Class
```bash
.\mvnw.cmd test -Dtest=AdultSignupTest
.\mvnw.cmd test -Dtest=YouthSignupTest
.\mvnw.cmd test -Dtest=ParentSignupTest
```

### Run Single Test Method
```bash
.\mvnw.cmd test -Dtest=AdultSignupTest#testOpenLoginAndClickCreateAccount
```

## Test Data

Test data is stored in CSV files:
- `adult_user_data.csv` - Adult signup test data
- `youth_user_data.csv` - Youth signup test data
- `user_data.csv` - Parent signup test data

## Configuration

Edit `src/main/resources/config.properties` to configure:
- Application URLs (staging/production)
- Browser settings
- Timeout values

## Dependencies

All dependencies are managed by Maven and defined in `pom.xml`:
- Selenium WebDriver 4.15.0
- TestNG 7.8.0
- WebDriverManager 5.6.2
- ExtentReports 5.1.1

## Notes

- **JDK directories are NOT included** in this repository due to GitHub file size limits (100MB)
- Install JDK 17 separately using one of the methods above
- The project uses Maven Wrapper, so no separate Maven installation needed
- ChromeDriver is automatically managed by WebDriverManager

## Troubleshooting

### Java Version Issues
If you get Java version errors:
1. Verify `JAVA_HOME` is set correctly
2. Check `java -version` shows JDK 17
3. Restart your terminal/IDE after installing JDK

### ChromeDriver Issues
WebDriverManager should automatically download the correct ChromeDriver version. If issues occur:
- Update Chrome browser to latest version
- Check internet connection for automatic driver download

## License

[Add your license information here]

