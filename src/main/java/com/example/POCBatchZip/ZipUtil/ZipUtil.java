package com.example.POCBatchZip.ZipUtil;



import net.lingala.zip4j.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.io.File;
import java.nio.file.Path;
import java.util.List;

@Component
public class ZipUtil {

    public void createZip (Path Dir, List<String> listSubDir,Path dir) throws ZipException {
        // Creare lo ZIP
        String nomeDir = Dir.toString().substring(Dir.toString().lastIndexOf("\\") + 1 );
        nomeDir += ".zip";
        for(String subDir : listSubDir )
            new ZipFile(dir+"\\"+nomeDir).addFolder(new File(subDir));
    }

}
