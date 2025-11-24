package com.shopme.admin.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Component
public class FileUploadUtil implements ApplicationContextAware {
	private static final Logger LOGGER = LoggerFactory.getLogger(FileUploadUtil.class);

	private static ApplicationContext applicationContext;
	private static FileUploadUtil instance;

	@Autowired
	private S3Service s3Service;

	@Override
	public void setApplicationContext(@NonNull ApplicationContext context) {
		applicationContext = context;
		instance = applicationContext.getBean(FileUploadUtil.class);
	}

	private static FileUploadUtil getInstance() {
		if (instance == null && applicationContext != null) {
			instance = applicationContext.getBean(FileUploadUtil.class);
		}
		return instance;
	}

	public static void saveFile(String uploadDir, String fileName,
			MultipartFile multipartFile) throws IOException {
		FileUploadUtil util = getInstance();
		if (util != null && util.s3Service != null && util.s3Service.isEnabled()) {
			// Use S3 storage
			String s3Key = uploadDir + "/" + fileName;
			util.s3Service.uploadFile(s3Key, multipartFile);
		} else {
			// Use local storage
			saveFileLocal(uploadDir, fileName, multipartFile);
		}
	}

	private static void saveFileLocal(String uploadDir, String fileName,
			MultipartFile multipartFile) throws IOException {
		Path uploadPath = Paths.get(uploadDir);

		if (!Files.exists(uploadPath)) {
			Files.createDirectories(uploadPath);
		}

		try (InputStream inputStream = multipartFile.getInputStream()) {
			Path filePath = uploadPath.resolve(fileName);
			Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
		} catch (IOException ex) {
			throw new IOException("Could not save file: " + fileName, ex);
		}
	}

	public static void cleanDir(String dir) {
		FileUploadUtil util = getInstance();
		if (util != null && util.s3Service != null && util.s3Service.isEnabled()) {
			// Use S3 storage - delete all files with the prefix
			util.s3Service.deleteDirectory(dir + "/");
		} else {
			// Use local storage
			cleanDirLocal(dir);
		}
	}

	private static void cleanDirLocal(String dir) {
		Path dirPath = Paths.get(dir);

		try {
			Files.list(dirPath).forEach(file -> {
				if (!Files.isDirectory(file)) {
					try {
						Files.delete(file);
					} catch (IOException ex) {
						LOGGER.error("Could not delete file: " + file);
					}
				}
			});
		} catch (IOException ex) {
			LOGGER.error("Could not list directory: " + dirPath);
		}
	}

	public static void removeDir(String dir) {
		FileUploadUtil util = getInstance();
		if (util != null && util.s3Service != null && util.s3Service.isEnabled()) {
			// Use S3 storage
			util.s3Service.deleteDirectory(dir + "/");
		} else {
			// Use local storage
			removeDirLocal(dir);
		}
	}

	private static void removeDirLocal(String dir) {
		cleanDirLocal(dir);

		try {
			Files.delete(Paths.get(dir));
		} catch (IOException e) {
			LOGGER.error("Could not remove directory: " + dir);
		}
	}
}
