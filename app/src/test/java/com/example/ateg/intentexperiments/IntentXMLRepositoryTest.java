package com.example.ateg.intentexperiments;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.util.UUID;

import static org.junit.Assert.*;

/**
 * Created by ATeg on 11/1/2017.
 */
public class IntentXMLRepositoryTest {

    private IntentRepository intentRepository;
    private File file;

    @Before
    public void setup() {
        file = new File(UUID.randomUUID().toString());
        intentRepository = new IntentXMLRepository(file);
    }

    @After
    public void tearDown() {
        if (file.exists())
            file.delete();
    }

    @Test
    public void create() throws Exception {

        IntentWrapper

        intentRepository.create()
    }

    @Test
    public void create1() throws Exception {

    }

    @Test
    public void getAll() throws Exception {

    }

    @Test
    public void getDifferential() throws Exception {

    }

    @Test
    public void markAllArchived() throws Exception {

    }

    @Test
    public void deleteAll() throws Exception {

    }

}