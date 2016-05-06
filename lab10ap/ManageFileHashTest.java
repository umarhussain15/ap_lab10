package lab10ap;

import junit.framework.TestCase;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import java.io.File;

/**
 * Created by Umar on 02-May-16.
 */
public class ManageFileHashTest extends TestCase {
    private static SessionFactory factory;
    public void testAddFileSha() throws Exception {
        try {
            factory = new Configuration().configure().buildSessionFactory();
            ManageFileHash ME = new ManageFileHash();
            File ff= new File("F:\\MPC\\bin\\AndroidManifest.xml");
            assertNotNull(ME.addFileSha(ff.getName(),ff.getPath(),ff.getParent()));
        } catch (Throwable ex) {
            System.err.println("Failed to create sessionFactory object." + ex);
            throw new ExceptionInInitializerError(ex);
        }
    }
}