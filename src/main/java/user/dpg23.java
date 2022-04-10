package user;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.Scanner;

public class dpg23 {
    public static void main(String[] args) throws IOException, NoSuchAlgorithmException {


        System.out.println("========================================");
        System.out.println("DPG23 Distributed database");
        Scanner sc;
        while (true) {
            System.out.println("Select appropriate option from below list");
            System.out.println("1. User Registration");
            System.out.println("2. User Login");
            System.out.println("3. Exit system");
            System.out.println("========================================");
            System.out.println("\nEnter your choice :");
            sc = new Scanner(System.in);

            final String uinput = sc.nextLine();
            switch (uinput) {
                case "1":
                    RegUser registration = new RegUser();
                    registration.registerUser();

                    break;
                case "2":
                    Login login = new Login();
                    login.loginuser(sc);
                    break;
               case "3":
                    break;
                default:
                    System.out.println("Enter valid input");
            }

        }



    }
}
