package lab10ap;

import org.hibernate.*;
import org.hibernate.cfg.Configuration;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;

/**
 * Created by Umar on 02-May-16.
 */
public class ManageFileHash {
    private static SessionFactory factory;

    public static void main(String[] args) {
        try {
            factory = new Configuration().configure().buildSessionFactory();
        } catch (Throwable ex) {
            System.err.println("Failed to create sessionFactory object." + ex);
            throw new ExceptionInInitializerError(ex);
        }
        ManageFileHash ME = new ManageFileHash();
        Scanner s = new Scanner(System.in);
        System.out.println("enter 1 for calculating check sum, 2 for checking integrity");
        int g = s.nextInt();
        if (g == 1) {
            System.out.println("Enter Folder Name to create checksum");

            String start = "F:\\MPC";
            System.out.println(start);
            final File folder = new File(start);
            if (!folder.isDirectory()) {
                System.out.print("Not a valid folder Exiting");
                System.exit(-1);
            }
            ME.listFilesForFolder(folder);
        } else if (g == 2) {

            String start = "F:\\MPC";

            final File folder = new File(start);
            if (!folder.isDirectory()) {
                System.out.print("Not a valid folder Exiting");
                System.exit(-1);
            }
            try {
                System.out.println("Integrity Report of : "+start);
                ME.validateIntegrity();
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void listFilesForFolder(final File folder) {
        for (final File fileEntry : folder.listFiles()) {
            if (fileEntry.isDirectory()) {
                listFilesForFolder(fileEntry);
            } else {
                addFileSha(fileEntry.getName(), fileEntry.getAbsolutePath(), fileEntry.getParent());
            }
        }
    }

    public void validateIntegrity() throws NoSuchAlgorithmException, IOException {
        Session session = factory.openSession();
        String hql = "FROM FileHash";
        Query query = session.createQuery(hql);
        List results = query.list();
        Iterator it = results.iterator();

        while (it.hasNext()) {
            FileHash std = (FileHash) it.next();
            String checksum = std.getShaHash();
            String filepath = std.getFilePath();

            File file = new File(filepath);
            MessageDigest sha1Digest = MessageDigest.getInstance("SHA-1");
            //Get the checksum
            String checksum2 = getFileChecksum(sha1Digest, file);

            if (!checksum.equals(checksum2)) {
                System.out.println("\tIntegrity Violation! File Name: " + filepath);
            }
        }
        System.out.println("Integrity Check Completed\nReport Ends");
    }

    public Integer addFileSha(String filename, String filepath, String filefolder) {
        Session session = factory.openSession();
        Transaction tx = null;
        Integer employeeID = null;

        try {
            File file = new File(filepath);

            //Use sha1 algorithm
            MessageDigest sha1Digest = MessageDigest.getInstance("SHA-1");

            //Get the checksum
            String checksum = getFileChecksum(sha1Digest, file);
            tx = session.beginTransaction();
            FileHash filesave = new FileHash(filename, filepath, filefolder, checksum);
            employeeID = (Integer) session.save(filesave);
            tx.commit();
        } catch (HibernateException e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            session.close();
        }
        return employeeID;
    }

    private static String getFileChecksum(MessageDigest digest, File file) throws IOException {
        //Get file input stream for reading the file content
        FileInputStream fis = new FileInputStream(file);

        //Create byte array to read data in chunks
        byte[] byteArray = new byte[1024];
        int bytesCount = 0;

        //Read file data and update in message digest
        while ((bytesCount = fis.read(byteArray)) != -1) {
            digest.update(byteArray, 0, bytesCount);
        }
        //close the stream; We don't need it now.
        fis.close();
        //Get the hash's bytes
        byte[] bytes = digest.digest();
        //This bytes[] has bytes in decimal format;
        //Convert it to hexadecimal format
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < bytes.length; i++) {
            sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
        }
        //return complete hash
        return sb.toString();
    }
}
