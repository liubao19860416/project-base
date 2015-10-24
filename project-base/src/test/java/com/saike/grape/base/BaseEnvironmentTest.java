package com.saike.grape.base;

import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class BaseEnvironmentTest {

    private final String env = "LOCAL";
    private final String projectName = "project-base";
    
    @Before
    public void setUp() throws Exception {
        //System.setProperty( "GRAPE_SYS_ENV", env );
        //System.setProperty( "GRAPE_PROJECT_NAME", projectName );
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testInitENV() {
        assertTrue( env.equals( BaseEnvironment.SYS_ENV ) );
    }

    @Test
    public void testInitProjectName() {
        assertTrue( projectName.equals( BaseEnvironment.PROJECT_NAME ) );
    }

}
