package com.csci5408.distributeddatabase;

import com.csci5408.distributeddatabase.queryexecutor.*;
import org.springframework.boot.autoconfigure.*;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Scanner;

@SpringBootApplication
public class DistributedDatabaseApplication
{
	public static void main(String[] args) throws Exception {
		Scanner sc = new Scanner(System.in);
		boolean isLogged = true;
		BufferedReader reader = new BufferedReader(
				new InputStreamReader(System.in));

		if(isLogged){
			System.out.println("Please select one from below:::::::::::::");
			System.out.println("1.Write Queries"+"\n"+
					"2.Export"+"\n"+"3.Data Model"+"\n"+"4.Analytics");
			int operation = sc.nextInt();
			if(operation==1){
				System.out.print("Please enter the query to execute:::::::");
				String sql = reader.readLine();
				QueryExecutor queryExecutor = new QueryExecutor(sql);
				queryExecutor.executeQuery();
				}
			}
	}
}