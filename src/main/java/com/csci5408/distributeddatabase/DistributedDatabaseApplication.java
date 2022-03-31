package com.csci5408.distributeddatabase;

import com.csci5408.distributeddatabase.globalmetadatahandler.GlobalMetadataHandler;
import org.springframework.boot.*;
import org.springframework.boot.autoconfigure.*;

@SpringBootApplication
public class DistributedDatabaseApplication
{
	public static void main(String[] args)
	{
		SpringApplication.run(DistributedDatabaseApplication.class, args);
	}
}