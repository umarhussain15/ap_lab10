package lab10ap;

import javax.persistence.*;

/**
 * Created by Umar on 02-May-16.
 */
@Entity
@Table(name = "Files")
public class FileHash {

    @Id
    @GeneratedValue
    @Column(name = "id")
    private int id;
    @Column(name = "file_name")
    private String fileName;

    @Column(name = "file_path")
    private String filePath;

    @Column(name = "file_folder")
    private String fileFolder;

    @Column(name = "sha_hash")
    private String shaHash;

    public String getShaHash() {
        return shaHash;
    }

    public void setShaHash(String shaHash) {
        this.shaHash = shaHash;
    }

    public FileHash() {
    }

    public FileHash(String fileName, String filePath, String fileFolder, String shaHash) {
        this.fileName = fileName;
        this.filePath = filePath;
        this.fileFolder = fileFolder;
        this.shaHash = shaHash;
    }

    public int getId() {
        return id;
    }
    public void setId( int id ) {
        this.id = id;
    }
    public String getFileName() {
        return fileName;
    }
    public void setFileName( String file_name ) {
        this.fileName = file_name;
    }
    public String getFilePath() {
        return filePath;
    }
    public void setFilePath( String file_path ) {
        this.filePath = file_path;
    }
    public String getFileFolder() {
        return fileFolder;
    }
    public void setFileFolder( String file_folder ) {
        this.fileFolder = file_folder;
    }
}
