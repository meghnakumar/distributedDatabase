package com.csci5408.distributeddatabase;

import com.csci5408.distributeddatabase.queryexecutor.*;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.*;
import user.Login;
import user.RegUser;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Scanner;

@SpringBootApplication
public class DistributedDatabaseApplication {
	public static void main(String[] args) throws Exception {

		SpringApplication.run(DistributedDatabaseApplication.class);

		Scanner sc = new Scanner(System.in);
		System.out.println("========================================");
		System.out.println("DPG23 Distributed database");
		boolean isLogged = true;
		BufferedReader reader = new BufferedReader(
				new InputStreamReader(System.in));
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
					isLogged = login.loginuser(sc);
					break;
				case "3":
					System.exit(0);
					break;
				default:
					System.out.println("Enter valid input");
			}


			while (isLogged) {
					System.out.println("Please select one from below:::::::::::::");
					System.out.println("1.Write Normal Queries" + "\n" +
							"2.Export" + "\n" + "3.Data Model" + "\n" + "4.Analytics" + "\n" + "5.Write Transaction Queries" + "\n" + "6.Exit");
					int operation = sc.nextInt();
					if (operation == 1) {
						System.out.print("Please enter the query to execute:::::::");
						String sql = reader.readLine();
						QueryExecutor queryExecutor = new QueryExecutor(sql);
						String queryResponse = queryExecutor.executeQuery();
						System.out.println("response for the query " + sql + " is " + queryResponse);
					}
					if (operation == 5) {
						try {
							System.out.print("Please enter the transaction query to execute:::::::" + "\n");
							String sql = reader.readLine();
							QueryExecutor queryExecutor = new QueryExecutor(sql);
							queryExecutor.executeTransaction(sql);
						} catch (Exception exception) {
							System.err.println("Cannot commit transaction: " + exception.getMessage());
						}
					}
					if (operation == 6) {
						isLogged = false;
					}
				}
			}
		}
	}


