import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Scanner;

public class FinancialTrackerApp {

    //Shared scanner for user input across entire app
    // Avoids creating multiple Scanner objects
    static Scanner scanner = new Scanner(System.in);

    //In-memory list of transactions, loads once from file, then operate in list for efficiency
    static ArrayList<Transaction> transactions = new ArrayList<>();

    //Entry
    public static void main(String[] args) {

        //loading existing transactions from file into memory
        loadTransaction();
        // starts UI LOOP
        displayHomeScreen();
    }

    //Home Screen
    static void displayHomeScreen(){
        //while loop keeps program running until user exists
        while (true){
            System.out.println("Welcome to BECU Online Banking");
            System.out.println("\n---Home---");
            System.out.println("D) Add Deposit");
            System.out.println("P) Make Payment");
            System.out.println("L) Ledger");
            System.out.println("B) Balance");
            System.out.println("X) Exit");

            // initialize input to avoid case/spacing issues
            String choice = scanner.nextLine().toUpperCase().strip();

            switch (choice){
                case "D":
                    addDeposit();
                    break;
                case "P":
                    makePayment();
                    break;
                case "L":
                    displayLedger();
                    break;
                case "B":
                    displayBalance();
                    break;
                case "X":
                    System.out.println("Exiting application. Goodbye!");
                    return; // exists program completely
                default:
                    System.out.println("Invalid choice. Please select D, P, L, or X.");
            }
        }
    }

    static void displayBalance(){
        double balance = 0;
        for (Transaction t : transactions){
            balance += t.getAmount();
        }
        System.out.println("\nYour Current Balance: $" + String.format("%.2f", balance));
        System.out.println("--------------------------------------");
    }
    //add deposit
    static void addDeposit(){
        // collect user input
        System.out.print("Description: ");
        String desc = scanner.nextLine();

        System.out.print("Vendor: ");
        String vendor = scanner.nextLine();

        System.out.print("Amount: ");
        double amount = Double.parseDouble(scanner.nextLine());

        //deposits should always be positive
        saveTransaction(desc, vendor, Math.abs(amount));
        System.out.println("\nDeposit recorded successfully. Thank you! young buck");
        System.out.println("Press Enter to continue...");
        scanner.nextLine();
    }

    //make payment
    static void makePayment() {
        System.out.print("Description: ");
        String desc = scanner.nextLine();

        System.out.print("Vendor: ");
        String vendor = scanner.nextLine();

        System.out.print("Amount:");
        double amount = Double.parseDouble(scanner.nextLine());

        //Payments should always be negative
        saveTransaction(desc,vendor, -Math.abs(amount));
        System.out.println("\nPayment recorded successfully. Thank you! young buck");
        System.out.println("Press Enter to continue...");
        scanner.nextLine();
    }

    // Save transaction
    static void saveTransaction(String desc, String vendor, double amount) {

        //auto generate date and time
        String date = LocalDate.now().toString();
        String time = LocalTime.now().withNano(0).toString();

        //create object and store in memory
        Transaction t = new Transaction(date,time, desc, vendor, amount);
        transactions.add(t);

        // Append to CSV file for persistence(true = append mode)
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter("src/transactions.csv", true));
            bw.write(date + "|" + time + "|" + desc + "|" + vendor + "|" + amount);
            bw.newLine();
            bw.close();
        } catch (Exception e) {
            System.out.println("Error saving transaction: " + e.getMessage());
        }
    }
// Load transactions
    static void loadTransaction(){
        try{
            BufferedReader br = new BufferedReader(new FileReader("src/transactions.csv"));

            //skip header
            String line = br.readLine();
            // read file line by line
            while((line = br.readLine()) != null){

                //split by pipe
                String [] parts = line.split("\\|");

                //extract fields
                String date = parts[0];
                String time = parts[1];
                String desc = parts[2];
                String vendor = parts[3];
                double amount = Double.parseDouble(parts[4]);

                //Convert into object and store
                transactions.add(new Transaction(date, time, desc, vendor,amount));
            }
            br.close();
        }   catch (Exception e){
            System.out.println("Error loading transactions: " + e.getMessage());
        }
    }
//ledger screen
    static void displayLedger(){
        while (true){
            System.out.println("\n---Ledger---");
            System.out.println("A) All");
            System.out.println("D) Deposits");
            System.out.println("P) Payments");
            System.out.println("R) Reports");
            System.out.println("H) Home");

            String choice = scanner.nextLine().toUpperCase();

            switch (choice){
                case"A":
                    showAll();
                    break;
                case"D":
                    showDeposits();
                    break;
                case"P":
                    showPayments();
                    break;
                case"R":
                    displayReports();
                    break;
                case"H":
                    return; // back to home screen
                default:
                    System.out.println("Invalid choice. Please select A, D, P, R, or H.");
            }
        }
    }
        // display all
        static void showAll(){
        //loop backwards --> newest entries first
            for(int i = transactions.size() -1; i >= 0; i--){
                System.out.println(transactions.get(i));
            }
        }
        //deposits only
        static void showDeposits(){
            for(int i = transactions.size() -1; i >= 0; i--){
                if(transactions.get(i).getAmount() > 0){
                    System.out.println(transactions.get(i));
                }
            }
        }
        // payments only
        static void showPayments() {
            for (int i = transactions.size() - 1; i >= 0; i--) {
                if (transactions.get(i).getAmount() < 0) {
                    System.out.println(transactions.get(i));
                }
            }
        }

        // report menu
        static void displayReports(){
            while (true){
                System.out.println("\n---Reports---");
                System.out.println("1) Month to Date");
                System.out.println("5) Search by Vendor");
                System.out.println("0) Back");

                String choice = scanner.nextLine();

                switch(choice){
                    case "1":
                        monthToDate();
                        break;
                    case "5":
                        searchByVendor();
                        break;
                    case "0":
                    return;
                }
            }
        }
        //search by vendor
        static void searchByVendor() {
            System.out.print("Enter vendor name to search: ");
            String keyword = scanner.nextLine().toLowerCase();

            //loop through all transactions
            for (Transaction t : transactions) {
                // case-insensitive
                if (t.getVendor().toLowerCase().contains(keyword)) {
                    System.out.println(t);
                }
            }
        }
        // month to date report
        static void monthToDate() {
            LocalDate now = LocalDate.now();
            for (Transaction t : transactions) {
                LocalDate transactionDate = LocalDate.parse(t.getDate());
                // Same date + same year
                if (transactionDate.getMonth() == now.getMonth() &&
                    transactionDate.getYear() == now.getYear()) {
                    System.out.println(t);
                }
            }
        }
    }

