package com.test.mqservice.cache;

import com.test.mqcore.bo.FileSliceInfo;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class FileSliceCacheTest {

    @Autowired
    FileSliceCache fileSliceCache;

    @Test
    public void putFileSliceInfo() {
        fileSliceCache.putFileSliceInfo("123", new FileSliceInfo());
        System.out.println(fileSliceCache.getFileSliceInfo("123"));
    }

    @Test
    public void getFileSliceInfo() {
        System.out.println(fileSliceCache.getFileSliceInfo("159278602041535086794416"));
    }
}