package com.usaflag;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Random;

/**
 * Utility class to generate CSV data for concurrent signup tests
 */
public class GenerateCSVData {
    
    private static final String[] FIRST_NAMES = {
        "Zephyr", "Atlas", "Phoenix", "Orion", "Juno", "River", "Sage", "Rowan", "Indigo", "Ember",
        "Kai", "Luna", "Aster", "Nova", "Echo", "Pax", "Quinn", "Reese", "Tatum", "Vesper",
        "Wren", "Xara", "Yara", "Zara", "Aiden", "Blake", "Cora", "Dane", "Eden", "Finley",
        "Gage", "Haven", "Iris", "Jade", "Lane", "Maya", "Ocean", "Parker", "Rain", "Sky",
        "Talon", "Uriel", "Vale", "Willow", "Xanthe", "Yarrow", "Aspen", "Briar", "Cedar",
        "Dorian", "Elara", "Felix", "Gemma", "Harrison", "Isla", "Jasper", "Kira", "Lucas",
        "Morgan", "Nolan", "Ophelia", "Preston", "Raven", "Sierra", "Tyler", "Uma", "Violet",
        "Weston", "Ximena", "Yael", "Zane", "Avery", "Bennett", "Cyrus", "Dahlia", "Ellis",
        "Fletcher", "Garnet", "Harlow", "Iker", "Jada", "Kellan", "Lorelai", "Miller", "Nadia",
        "Oakley", "Priscilla", "Quincy", "Raina", "Thiago", "Vincenzo", "Aileen", "Benson",
        "Callie", "Diego", "Estelle", "Flynn", "Gia", "Hendrix", "Indie", "Jovani", "Kaia",
        "Leandro", "Malia", "Nikolai", "Sabrina", "Tiana", "Lance", "Rylee", "Desmond",
        "Hugo", "Camila", "Ronan", "Amaya", "Cohen", "Daley", "Eliana", "Greta", "Harper"
    };
    
    private static final String[] LAST_NAMES = {
        "Abrams", "Barrett", "Chen", "Delaney", "Evans", "Foster", "Gibson", "Hayes", "Irwin", "Jacobs",
        "Knight", "Lawson", "Morrison", "Nash", "Owens", "Peterson", "Quinn", "Ramirez", "Stewart", "Tucker",
        "Valdez", "Walker", "Wood", "Yang", "Zane", "Archer", "Banks", "Cole", "Drake", "Ellis",
        "Fox", "Grant", "Ingram", "James", "King", "Lake", "May", "Neal", "Owen", "Page",
        "Quill", "Rose", "Star", "Taylor", "Vale", "West", "Xen", "York", "Zion", "Ace",
        "Blaze", "Cove", "Dawn", "Echo", "Flame", "Glade", "Harbor", "Island", "Jet", "Kestrel",
        "Lagoon", "Meadow", "Nile", "Oasis", "Peak", "Quartz", "Ridge", "Summit", "Thorn", "Valley",
        "Vine", "Wild", "Willow", "Xenon", "Yew", "Zinc", "Armstrong", "Blackwell", "Caldwell",
        "Delgado", "Espinoza", "Fitzgerald", "Gallagher", "Henderson", "Jefferson", "Kennedy", "Lancaster",
        "Madison", "Newman", "Osborne", "Preston", "Thornton", "Underwood", "Valentine", "Weston",
        "Young", "Anderson", "Bennett", "Carter", "Davis", "Foster", "Garcia", "Hall", "Johnson",
        "Kelly", "Lee", "Martinez", "Nelson", "Ortiz", "Perez", "Roberts", "Sanchez", "Thompson", "Turner",
        "White", "Williams", "Wilson", "Wright", "Brown", "Clark", "Coleman", "Cook", "Cooper", "Cox",
        "Cruz", "Daniels", "Dean", "Diaz", "Dixon", "Duncan", "Edwards", "Elliott", "Ferguson", "Fisher",
        "Flores", "Ford", "Franklin", "Garrett", "Gomez", "Gonzalez", "Graham", "Gray", "Green", "Griffin",
        "Hamilton", "Harrison", "Hart", "Hawkins", "Henry", "Hernandez", "Herrera", "Hicks", "Hill", "Hoffman",
        "Holmes", "Howard", "Hughes", "Hunt", "Jackson", "James", "Jenkins", "Jones", "Jordan", "Kelley",
        "Kennedy", "Kim", "King", "Knight", "Lane", "Lawson", "Lewis", "Long", "Lopez", "Marshall",
        "Martin", "Mason", "Matthews", "McDonald", "McLaughlin", "Medina", "Mendoza", "Meyer", "Mills", "Mitchell",
        "Moore", "Morales", "Moreno", "Morgan", "Morris", "Murphy", "Murray", "Myers", "Nguyen", "Oliver",
        "Olson", "Ortega", "Palmer", "Park", "Parker", "Patterson", "Payne", "Pearson", "Pena", "Perry",
        "Peters", "Phillips", "Pierce", "Porter", "Powell", "Price", "Ramos", "Reed", "Reid", "Reyes",
        "Reynolds", "Rice", "Richards", "Richardson", "Riley", "Robinson", "Rodriguez", "Rogers", "Ross", "Ruiz",
        "Russell", "Ryan", "Sanders", "Schmidt", "Scott", "Shaw", "Simmons", "Smith", "Snyder", "Soto",
        "Spencer", "Stephens", "Stevens", "Stone", "Sullivan", "Thomas", "Torres", "Vargas", "Vasquez", "Vega",
        "Wagner", "Wallace", "Walters", "Ward", "Warren", "Washington", "Watson", "Weaver", "Webb", "Welch",
        "Wells", "Wheeler", "Williamson", "Woods", "Yates", "Zimmerman"
    };
    
    private static final int[] MONTHS = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12};
    private static final int[] YEARS = {1975, 1976, 1977, 1978, 1979, 1980, 1981, 1982, 1983, 1984, 1985, 1986, 1987, 1988, 1989, 1990, 1991};
    
    public static void generateCSV(String filePath, int numberOfRows) {
        Random random = new Random();
        
        try (PrintWriter writer = new PrintWriter(new FileWriter(filePath))) {
            // Write header
            writer.println("First Name,Last Name,Email,Phone,Date of Birth");
            
            for (int i = 0; i < numberOfRows; i++) {
                // Generate unique combination
                int firstIdx = i % FIRST_NAMES.length;
                int lastIdx = (i / FIRST_NAMES.length) % LAST_NAMES.length;
                
                // Add suffix for uniqueness if needed
                String suffix = (i >= FIRST_NAMES.length * LAST_NAMES.length) 
                    ? String.valueOf(i / (FIRST_NAMES.length * LAST_NAMES.length)) 
                    : "";
                
                String firstName = FIRST_NAMES[firstIdx] + suffix;
                String lastName = LAST_NAMES[lastIdx] + suffix;
                
                // Generate email
                String email = firstName + lastName + ".p" + (2001 + i) + "@yopmail.com";
                
                // Generate complex phone number
                int areaCode = 401 + (i % 200);
                int phoneMid = 567 + ((i * 17) % 9000);
                int phoneEnd = 8901 + ((i * 23) % 1000);
                String phone = String.format("+1 (%d) %04d-%04d", areaCode, phoneMid, phoneEnd);
                
                // Generate date of birth
                int month = MONTHS[random.nextInt(MONTHS.length)];
                int day = random.nextInt(28) + 1;
                int year = YEARS[random.nextInt(YEARS.length)];
                String dob = String.format("%02d/%02d/%d", day, month, year);
                
                writer.println(firstName + "," + lastName + "," + email + "," + phone + "," + dob);
            }
            
            System.out.println("✅ Generated " + numberOfRows + " rows successfully in " + filePath);
            
        } catch (IOException e) {
            System.err.println("❌ Failed to generate CSV: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    public static void main(String[] args) {
        generateCSV("ParentConcurrentSignup.csv", 2000);
    }
}

