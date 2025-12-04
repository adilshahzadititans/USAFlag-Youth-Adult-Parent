package com.usaflag;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Utility to generate adult_user_data.csv with 1000 unique rows.
 * DOBs are chosen so ages are in [18..25] or 30 as of year 2025.
 */
public class GenerateAdultCsv {

    private static final String OUTPUT = "adult_user_data.csv";

    public static void main(String[] args) {
        generate();
    }

    public static void generate() {
        PrintWriter writer = null;
        try {
            writer = new PrintWriter(new FileWriter(OUTPUT, false));
            writer.println("First Name,Last Name,Email,Phone,Date of Birth");

            // American/Canadian name pools (mix of common US/CA names)
            final String[] FIRST_NAMES = new String[]{
                "John","Michael","David","James","Robert","William","Matthew","Andrew","Justin","Kevin",
                "Anthony","Ryan","Tyler","Eric","Jason","Brandon","Nathan","Daniel","Jacob","Jonathan",
                "Emily","Sarah","Ashley","Jessica","Amanda","Jennifer","Lauren","Brittany","Nicole","Taylor",
                "Hannah","Rachel","Megan","Samantha","Alexis","Victoria","Alyssa","Kayla","Elizabeth","Madison"
            };
            final String[] LAST_NAMES = new String[]{
                "Smith","Johnson","Williams","Brown","Jones","Garcia","Miller","Davis","Rodriguez","Martinez",
                "Hernandez","Lopez","Gonzalez","Wilson","Anderson","Thomas","Taylor","Moore","Jackson","Martin",
                "Lee","Perez","Thompson","White","Harris","Sanchez","Clark","Ramirez","Lewis","Robinson",
                "Walker","Young","Allen","King","Wright","Scott","Torres","Nguyen","Hill","Flores"
            };

            // Ages cycle through [18,19,20,21,22,23,24,25,30]
            final int[] ages = new int[]{18,19,20,21,22,23,24,25,30};

            for (int i = 1; i <= 1000; i++) {
                int age = ages[(i - 1) % ages.length];
                int year = 2025 - age;

                // distribute months 1..12 and days 1..28 to keep valid
                int month = ((i - 1) % 12) + 1;
                int day = ((i - 1) % 28) + 1;

                String mm = String.format("%02d", month);
                String dd = String.format("%02d", day);
                String yyyy = String.format("%04d", year);

                String idx4 = String.format("%04d", i);

                String firstName = FIRST_NAMES[(i - 1) % FIRST_NAMES.length];
                String lastName = LAST_NAMES[(i - 1) % LAST_NAMES.length];

                // Email style: JohnSmith1001@yopmail.com (CamelCase local-part)
                String email = firstName + lastName + idx4 + "@yopmail.com";

                // Unique phone like (555) 200-1001, (555) 200-1002 ...
                String phone = String.format("(555) 200-%04d", 1000 + i);

                String dob = mm + "/" + dd + "/" + yyyy;

                writer.println(firstName + "," + lastName + "," + email + "," + phone + "," + dob);
            }

            System.out.println("Generated 1000 rows at: " + new java.io.File(OUTPUT).getAbsolutePath());
        } catch (IOException e) {
            System.err.println("Failed to generate CSV: " + e.getMessage());
        } finally {
            if (writer != null) {
                writer.flush();
                writer.close();
            }
        }
    }
}


