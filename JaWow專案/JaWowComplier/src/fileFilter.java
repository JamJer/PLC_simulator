import java.io.File;
import javax.swing.filechooser.*;

public class fileFilter extends FileFilter {
	String extension, description;

	public fileFilter(String extension, String description) {
		this.extension = extension;
		this.description = description;
	}

	public boolean accept(File file) {
		if (file.getName().endsWith(extension)) {
			return true;
		} else if (file.isDirectory()) {
			return true;
		}
		return false;
	}

	public String getDescription() {
		return this.description;
	}
}