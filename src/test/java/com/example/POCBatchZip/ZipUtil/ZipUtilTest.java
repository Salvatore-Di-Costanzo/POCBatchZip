package com.example.POCBatchZip.ZipUtil;

import com.example.POCBatchZip.UpDownUtil.UpdownUtil;
import lombok.extern.slf4j.Slf4j;
import net.lingala.zip4j.ZipFile;
import org.junit.Assert;
import org.junit.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;
import org.springframework.test.annotation.Rollback;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

@Slf4j
class ZipUtilTest {

    @InjectMocks
    private ZipUtil zipUtilUnderTest;


    Path Dir = Paths.get("C:\\Users\\sdicostanzo\\Desktop\\DownFTP\\Class1");


    Path dir = Paths.get("C:\\Users\\sdicostanzo\\Desktop\\DownFTP");

    int limit = 2;

    @BeforeEach
    void setUp() {
        initMocks(this);
    }


    @Test
    @Rollback
    void testCreateZips() throws Exception {
        // Setup
        Stream<Path> walk1 = Files.walk(Dir,1);

        List <String> listSubDir = walk1.filter(p -> Files.isDirectory(p) && ! p.equals(dir) && ! p.equals(Dir))
            .map(x -> x.toString()).collect(Collectors.toList());



        // Run the test
        final List<ZipFile> result = zipUtilUnderTest.createZips(Dir, listSubDir, dir, limit);

        // Verify the results
        Assert.assertEquals(3,result.size());

    }



}
