package com.example.POCBatchZip.UpDownUtil;


import net.lingala.zip4j.ZipFile;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.logging.Level;

@Component
public class UpdownUtil {

    private static final java.util.logging.Logger log =
            java.util.logging.Logger.getLogger(UpdownUtil.class.getName());


    private static String server = "ftp.dlptest.com";
    private static String user = "dlpuser@dlptest.com";
    private static String pass = "eUj8GeW55SvYaswqUyDSm5v6N";
    private static String error = "ERROR :";

    // Upload sul server FTP

    public String upload(ZipFile zip) throws IOException {

        String uri = zip.getFile().toString();
        //Impostazioni server FTP di prova - durata permanenza archivio 30 min


        FTPClient ftpClient = new FTPClient();
        ftpClient.connect(server);
        ftpClient.login(user, pass);
        ftpClient.enterLocalPassiveMode();

        ftpClient.setFileType(FTP.BINARY_FILE_TYPE);

        // Upload usando InputStream
        File localFile = new File(uri);

        String remoteFile = uri.substring(uri.lastIndexOf("\\") + 1);
        try ( InputStream inputStream = new FileInputStream(localFile) ) {



            log.info("Inizio l'Upload dell'archivio");
            boolean done = ftpClient.storeFile(remoteFile, inputStream);
            if (done) {
                log.info("L'archivio è stato correttamente caricato.");
            }
            return "Upload Terminato";

        } catch (IOException ex) {
            log.log(Level.SEVERE, () ->error + ex.getMessage());
            ex.printStackTrace();
        } finally {
            try {
                if (ftpClient.isConnected()) {
                    ftpClient.logout();
                    ftpClient.disconnect();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        return "Upload Fallito";
    }

    public String upload(File zip) throws IOException {

        String uri = zip.toString();
        //Impostazioni server FTP di prova - durata permanenza archivio 30 min


        FTPClient ftpClient = new FTPClient();
        ftpClient.connect(server);
        ftpClient.login(user, pass);
        ftpClient.enterLocalPassiveMode();

        ftpClient.setFileType(FTP.BINARY_FILE_TYPE);

        // Upload usando InputStream
        File localFile = new File(uri);

        String remoteFile = uri.substring(uri.lastIndexOf("\\") + 1);
        try ( InputStream inputStream = new FileInputStream(localFile) ) {



            log.info("Inizio l'Upload dell'archivio");
            boolean done = ftpClient.storeFile(remoteFile, inputStream);
            if (done) {
                log.info("L'archivio è stato correttamente caricato.");
            }
            return "Upload Terminato";

        } catch (IOException ex) {
            log.log(Level.SEVERE, () ->error + ex.getMessage());
            ex.printStackTrace();
        } finally {
            try {
                if (ftpClient.isConnected()) {
                    ftpClient.logout();
                    ftpClient.disconnect();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        return "Upload Fallito";
    }


}