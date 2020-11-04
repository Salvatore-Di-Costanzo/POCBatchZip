package com.example.POCBatchZip.UpDownUtil;


import net.lingala.zip4j.ZipFile;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.springframework.stereotype.Component;

import java.io.*;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

@Component
public class UpdownUtil {

    private static final java.util.logging.Logger log =
            java.util.logging.Logger.getLogger(UpdownUtil.class.getName());


    private static String server = "ftp.dlptest.com";
    private static String user = "dlpuser@dlptest.com";
    private static String pass = "eUj8GeW55SvYaswqUyDSm5v6N";

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
            log.log(Level.SEVERE, () ->"Error: " + ex.getMessage());
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


    public String uploadList(List<ZipFile> zips) throws IOException {

        boolean up = false;
        List <String> urls = new ArrayList<>();
        for (ZipFile zip : zips)
            urls.add(zip.getFile().toString());
        //String uri = zip.getFile().toString();
        //Impostazioni server FTP di prova - durata permanenza archivio 30 min


        FTPClient ftpClient = new FTPClient();
        ftpClient.connect(server);
        ftpClient.login(user, pass);
        ftpClient.enterLocalPassiveMode();

        ftpClient.setFileType(FTP.BINARY_FILE_TYPE);

        // Upload usando InputStream
        List<File> localFiles = new ArrayList<>();
        for(File localFile : localFiles) {

            String remoteFile = localFile.toString().substring(localFile.toString().lastIndexOf("\\") + 1);
            try (InputStream inputStream = new FileInputStream(localFile)) {

                log.info("Inizio l'Upload dell'archivio");
                boolean done = ftpClient.storeFile(remoteFile, inputStream);
                if (done) {
                    log.info("L'archivio è stato correttamente caricato.");
                }
                up = true;

            } catch (IOException ex) {
                log.log(Level.SEVERE, () -> "Error: " + ex.getMessage());
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

        }
        if (up)
            return "Upload Terminato";
        else
            return "Upload Fallito";
    }


    // Download dal server FTP


    public String download() throws IOException {

        String uri = "C:\\Users\\sdicostanzo\\Desktop\\download\\commessa.xml";

        //Impostazioni server FTP di prova - durata permanenza archivio 30 min


        FTPClient ftpClient = new FTPClient();
        ftpClient.connect(server);
        ftpClient.login(user, pass);
        ftpClient.enterLocalPassiveMode();
        ftpClient.setFileType(FTP.BINARY_FILE_TYPE);


        // APPROACH #1: using retrieveFile(String, OutputStream)
        String remoteFile = "/commessa.xml";
        File downloadFile = new File(uri);
        try ( OutputStream outputStream1 = new BufferedOutputStream(new FileOutputStream(downloadFile))) {



            boolean success = ftpClient.retrieveFile(remoteFile, outputStream1);


            if (success) {
                log.info("File scaricato correttamente");

                return downloadFile.toString();
            }
        } catch (IOException ex) {
            log.log(Level.SEVERE, () ->"Error: " + ex.getMessage());
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

        return "Download Fallito";
    }


    // Eliminazione dal Server FTP

    public String delete() {

        //Impostazioni server FTP di prova - durata permanenza archivio 30 min

        String nome = "/commessa.xml";

        FTPClient client = new FTPClient();
        try {

            client.connect(server);

            client.login(user, pass);

            // Eliminazione File

            boolean exist = client.deleteFile(nome);
            client.logout();

            // Norifichiamo all'utente il risultato
            if (exist)
                return "File '" + nome + "' eliminato...";
            else
                return "File '" + nome + "' non esiste...";
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                client.disconnect();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return "Eliminazione Fallita";
    }


}