package com.csci5408.distributeddatabase.distributedhelper;

import com.csci5408.distributeddatabase.globalmetadatahandler.GlobalMetadataConstants;
import com.csci5408.distributeddatabase.globalmetadatahandler.GlobalMetadataHandler;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Properties;

public class DistributedHelper
{
    private String currentInstanceIP;

    private String otherInstanceIP;

    public boolean isDatabasePresentInLocalInstance(String databaseName)
    {
        GlobalMetadataHandler globalMetadataHandler = new GlobalMetadataHandler();
        Properties prop = globalMetadataHandler.getGlobalMetadataProperties();
        String currentInstanceIP = prop.getProperty("current_instance");

        if(prop.containsKey(databaseName))
        {
            String databaseInstanceIp = prop.getProperty(databaseName);
            return currentInstanceIP.equals(databaseInstanceIp);
        }
        else
        {
            return false;
        }
    }

    public String updateGlobalMetadataPropInOtherInstance(String keyName, String keyValue)
    {
        String mapping = "updateGlobalMetaDataProp";
        MultiValueMap<String, String> parameterMap= new LinkedMultiValueMap<String, String>();
        parameterMap.add("propName", "propValue");
        return forwardRequestToOtherInstance(mapping, parameterMap).toString();
    }

    public String executeQueryInOtherInstance(String query)
    {
        String mapping = "redirectUpdateGlobalMetaDataProp";
        MultiValueMap<String, String> parameterMap= new LinkedMultiValueMap<String, String>();
        parameterMap.add("query", query);
        return forwardRequestToOtherInstance(mapping, parameterMap).toString();
    }

    private ResponseEntity<String> forwardRequestToOtherInstance(String mapping, MultiValueMap<String, String> parameterMap)
    {
        String url = "http://"+ GlobalMetadataConstants.INSTANCE_OTHER+"/"+mapping;
        System.err.println("routing query url "+url);

        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<MultiValueMap<String, String>>(parameterMap, headers);
        ResponseEntity<String> response = restTemplate.postForEntity( url, request , String.class );

        return response;
    }
}
