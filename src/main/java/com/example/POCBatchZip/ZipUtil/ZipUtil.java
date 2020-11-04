package com.example.POCBatchZip.ZipUtil;



import com.example.POCBatchZip.UpDownUtil.UpdownUtil;
import net.lingala.zip4j.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

@Component
public class ZipUtil {

    @Autowired
    private UpdownUtil updownUtil;

    public void createZip(Path Dir, List<String> listSubDir, Path dir, int limit) throws IOException {

        List<ZipFile> zipFiles = new ArrayList<>();

        if (listSubDir.size() > limit && limit > 0) {
            int index = 0, j = 0;
            // Creare lo ZIP
            String nomeDir = Dir.toString().substring(Dir.toString().lastIndexOf("\\") + 1);
            nomeDir += "_" + index + ".zip";
            for (String subDir : listSubDir) {
                j += 1;
                if (j >= limit) {
                    index++;
                    nomeDir = Dir.toString().substring(Dir.toString().lastIndexOf("\\") + 1);
                    nomeDir += "_" + index + ".zip";
                    j = 0;
                }
                ZipFile zipFile = new ZipFile(dir + "\\" + nomeDir);
                zipFile.addFolder(new File(subDir));
                updownUtil.upload(zipFile);
            }
        } else {
            // Creare lo ZIP
            String nomeDir = Dir.toString().substring(Dir.toString().lastIndexOf("\\") + 1);
            nomeDir += ".zip";
            for (String subDir : listSubDir) {
                ZipFile zipFile = new ZipFile(dir + "\\" + nomeDir);
                zipFile.addFolder(new File(subDir));
                updownUtil.upload(zipFile);
            }
        }
    }

    public List<ZipFile> createZips(Path Dir, List<String> listSubDir, Path dir, int limit) throws IOException {

        List<ZipFile> zipFiles = new ArrayList<>();

        if (listSubDir.size() > limit && limit > 0) {
            int index = 2, j = 0;
            // Creare lo ZIP
            String nomeDir = Dir.toString().substring(Dir.toString().lastIndexOf("\\") + 1);
            nomeDir += "_" + index + ".zip";
            for (String subDir : listSubDir) {
                j += 1;
                if (j >= limit) {
                    index++;
                    nomeDir = Dir.toString().substring(Dir.toString().lastIndexOf("\\") + 1);
                    nomeDir += "_" + index + ".zip";
                    j = 0;
                }
                ZipFile zipFile = new ZipFile(dir + "\\" + nomeDir);
                zipFile.addFolder(new File(subDir));
                zipFiles.add(zipFile);
            }
        } else {
            // Creare lo ZIP
            String nomeDir = Dir.toString().substring(Dir.toString().lastIndexOf("\\") + 1);
            nomeDir += ".zip";
            for (String subDir : listSubDir) {
                ZipFile zipFile = new ZipFile(dir + "\\" + nomeDir);
                zipFile.addFolder(new File(subDir));
                zipFiles.add(zipFile);
            }
        }
        return zipFiles;
    }
}
