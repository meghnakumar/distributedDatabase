package com.csci5408.distributeddatabase;

import com.csci5408.distributeddatabase.globalmetadatahandler.GlobalMetadataHandler;
import org.springframework.boot.*;
import org.springframework.boot.autoconfigure.*;
import org.springframework.web.bind.annotation.*;

@SpringBootApplication
public class DistributedDatabaseApplication
{
	public static void main(String[] args)
	{
		SpringApplication.run(DistributedDatabaseApplication.class, args);

		GlobalMetadataHandler handler = new GlobalMetadataHandler();
		handler.initGlobalMetaData();
	}
}