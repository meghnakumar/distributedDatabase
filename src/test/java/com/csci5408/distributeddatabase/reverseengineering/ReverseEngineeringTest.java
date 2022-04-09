package com.csci5408.distributeddatabase.reverseengineering;

import org.junit.Test;


public class ReverseEngineeringTest {

    @Test
    public void generateEntityRelationTest() throws Exception {
        ReverseEngineering reverseEngineering = new ReverseEngineering();
        reverseEngineering.reverseEngineering("demo");
    }
}