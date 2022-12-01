package contact.helper;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
public class FileUpload {
	private final File path = new ClassPathResource("static/uploaded_profile/").getFile();
	public final  Long maxSize = 1048576L; // 1Mb

	public FileUpload() throws IOException {

	}

	public String covertFileSize(Long size) {
		String st = "";
		if (size < 1024) {
			st += size + " byte";
		} else if (Math.ceil(size / 1024) < 1024) {
			st += (float) (size / 1024) + " kb";
		} else if (Math.ceil((size / 1024) / 1024) < 1024) {
			st += (float) (size / 1024) / 1024 + " mb";
		} else {
			st += (float) ((size / 1024) / 1024) / 1024 + " gb";
		}
		return st;
	}

	public void uploadFile(MultipartFile file, String folder,String fileName) throws IOException {
        //file writing 
		Files.copy(
			file.getInputStream(), 
			Paths.get(path.getAbsolutePath() + File.separator + folder + File.separator + fileName),
			StandardCopyOption.REPLACE_EXISTING
		);
		System.out.println(path);
	}
	
	public void uploadFile2(MultipartFile file, String folder, String fileName) throws IOException {
		//file writing 
		InputStream is = file.getInputStream();
		byte[] data = new byte[is.available()];
		is.read(data);
		FileOutputStream fos = new FileOutputStream(path.getAbsolutePath() + File.separator + folder + File.separator + fileName);
		fos.write(data);
		fos.flush();
		is.close();
		fos.close();
		System.out.println(path);
	}
	
	public boolean deleteFile(String oldFileName, String folder) {
		File file = new File(path + File.separator + folder, oldFileName);
		System.out.println("file is " + file.exists());
		if(!file.exists()) return true;
		return file.delete();
	}
}
