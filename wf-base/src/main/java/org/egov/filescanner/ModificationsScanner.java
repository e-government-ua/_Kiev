package org.egov.filescanner;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ModificationsScanner {

	private static final String DATE_NAME_SEPARATOR = ";";
	private static final String DATE_FORMAT_FOR_MODIFIED_FILE = "yyyy-MM-dd_HH:mm:ss.SSS";
	private final static Logger log = LoggerFactory
			.getLogger(ModificationsScanner.class);
	private static String MODIFICAITONS_FILE_EXTENSITON = ".modify.lst";

	public static boolean hasModifiedfiles(String scanDirectoryPath) {
		try {
			Map<String, String> modifiedFiles = new HashMap<String, String>();

			File directoryToScan = new File(scanDirectoryPath);
			if (!directoryToScan.exists() || !directoryToScan.isDirectory()) {
				log.info("Path to check is not a directory");
				return false;
			}

			File fileWithModifications = getFileToSaveModifications(scanDirectoryPath);

			if (fileWithModifications.exists()) {
				long timeOfLastRun = fileWithModifications.lastModified();
				getModifiedFiles(modifiedFiles, directoryToScan, timeOfLastRun);
			} else {
				log.debug("Technical file from previous launch doesn't exist. Exiting.");
				fileWithModifications.createNewFile();
				return false;
			}
			
			if (!modifiedFiles.isEmpty()) {
				saveModifiedFilesToFile(fileWithModifications, modifiedFiles);
				modifiedFiles.clear();
				return true;
			} else {
				// overwriting file
				FileOutputStream fileStream = new FileOutputStream(fileWithModifications, false);
				fileStream.close();
			}
		} catch (IOException e) {
			log.error("Error while checking directory for modifications", e);
			e.printStackTrace();
		}

		return false;
	}

	private static File getFileToSaveModifications(String scanDirectoryPath)
			throws IOException {
		File directoryToSaveFile = new File(scanDirectoryPath + File.separator + ".." + File.separator + "..");
		log.debug("Folder to save file with modifications: "
				+ directoryToSaveFile.getCanonicalPath());

		String fileNameToSaveModification = StringUtils.substringAfter(
				scanDirectoryPath, directoryToSaveFile.getCanonicalPath()
						+ File.separator);
		fileNameToSaveModification = fileNameToSaveModification
				.replace(File.separator, ".");

		File fileWithModifications = new File(directoryToSaveFile,
				fileNameToSaveModification + MODIFICAITONS_FILE_EXTENSITON);
		log.debug("File for saving modified files: " + fileWithModifications.getCanonicalPath());
		return fileWithModifications;
	}

	private static void getModifiedFiles(Map<String, String> modifiedFiles,
			File directoryToScan, long lastRun) throws IOException {
		@SuppressWarnings("unchecked")
		Collection<File> files = FileUtils.listFiles(directoryToScan, null,
				true);
		SimpleDateFormat formatter = new SimpleDateFormat(
				DATE_FORMAT_FOR_MODIFIED_FILE);
		for (File currFile : files) {
			long currModificationDate = currFile.lastModified();
			if (currModificationDate > lastRun) {
				modifiedFiles.put(currFile.getCanonicalPath(),
						formatter.format(new Date(currModificationDate)));
			}
		}
	}

	private static void saveModifiedFilesToFile(File fileWithModifications,
			Map<String, String> modifiedFiles) {
		if (!modifiedFiles.isEmpty()) {
			PrintWriter printWriter = null;
			try {
				fileWithModifications.createNewFile();
				printWriter = new PrintWriter(fileWithModifications);

				for (Map.Entry<String, String> currElem : modifiedFiles
						.entrySet()) {
					printWriter.println(currElem.getValue() + DATE_NAME_SEPARATOR
							+ currElem.getKey());
				}
			} catch (FileNotFoundException e) {
				log.error("Unable to save file with modifications. ", e);
				e.printStackTrace();
			} catch (IOException e) {
				log.error("Unable to save file with modifications. ", e);
				e.printStackTrace();
			} 
			if (printWriter != null)
				printWriter.close();	
		}

	}

}
