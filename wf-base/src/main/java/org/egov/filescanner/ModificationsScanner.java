package org.egov.filescanner;

import org.apache.commons.io.FileUtils;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;

public class ModificationsScanner {

    private static final String DATE_NAME_SEPARATOR = ";";
    private static final String DATE_FORMAT_FOR_MODIFIED_FILE = "yyyy-MM-dd_HH:mm:ss.SSS";
    //private final static Logger log = LoggerFactory.getLogger(ModificationsScanner.class);
    private static String MODIFICAITONS_FILE_EXTENSITON = ".modify.lst";

    public static void main(String asArgument[]) throws Exception {
        if (hasModifiedfiles(asArgument[0])) {
            System.out.println("Modifications is present!");
        } else {
            //throw new Exception("Modifications is absant!");
            System.out.println("Modifications is absant!");
            //System.exit(0);
            Runtime.getRuntime().exit(0);
            System.exit(0);
            throw new RuntimeException("Modifications is absant!");
        }
    }

    public static boolean hasModifiedfiles(String sPathScan) {
        try {
            Map<String, String> mFileModified = new HashMap<String, String>();

            File oFilePathScan = new File(sPathScan);
            if (!oFilePathScan.exists() || !oFilePathScan.isDirectory()) {
                //log.info("Path to check is not a directory. Skip scaning...");
                System.out.println("Path to check is not a directory. Skip scaning... Default is modified!");
                return true;
            }

            File oFileHistory = getFileHistory(sPathScan);
            if (oFileHistory.exists()) {
                long nDateTimeLastRun = oFileHistory.lastModified();
                getFilesModified(mFileModified, oFilePathScan, nDateTimeLastRun);
            } else {
                //log.debug("Technical file from previous launch doesn't exist. All modified!");
                System.out.println("Technical file from previous launch doesn't exist. All modified!");
                oFileHistory.createNewFile();
                return true;
            }

            if (!mFileModified.isEmpty()) {
                saveFileHistory(oFileHistory, mFileModified);
                mFileModified.clear();
                return true;
            } else {
                // overwriting file
                FileOutputStream oFileOutputStreamWithModifications = new FileOutputStream(oFileHistory, false);
                oFileOutputStreamWithModifications.close();
                return false;
            }
        } catch (IOException oException) {
            //log.error("Error while checking directory for modifications!", oException);
            System.err.println("Error while checking directory for modifications!");
            oException.printStackTrace();
            return true;
        }

        //return false;
    }

    private static File getFileHistory(String sPathScan)
            throws IOException {
        System.out.println("sPathScan: " + sPathScan);
        File oFilePathHistory = new File(sPathScan + File.separator + ".." + File.separator + "..");
        //log.debug("Folder to save file with modifications: " + oFilePathHistory.getCanonicalPath());
        System.out.println("Folder to save file with modifications: " + oFilePathHistory.getCanonicalPath());

        //String sHistoryPathDiff = StringUtils.substringBefore(sPathScan, oFilePathHistory.getCanonicalPath() + File.separator);
        String sHistoryPathDiff = sPathScan.substring(oFilePathHistory.getCanonicalPath().length());
        System.out.println("sHistoryPathDiff=" + sHistoryPathDiff);
        String sHistoryFileName = sHistoryPathDiff.replace(File.separator, ".");
        System.out.println("sHistoryFileName=" + sHistoryFileName);

        File oFileHistory = new File(oFilePathHistory, sHistoryFileName + MODIFICAITONS_FILE_EXTENSITON);
        //log.debug("File for saving modified files: " + oFileHistory.getCanonicalPath());
        System.out.println("File for saving modified files: " + oFileHistory.getCanonicalPath());
        return oFileHistory;
    }

    private static void getFilesModified(Map<String, String> mFileModified, File oFilePathScan, long nDateTimeLastRun)
            throws IOException {
        @SuppressWarnings("unchecked")
        Collection<File> aFile = FileUtils.listFiles(oFilePathScan, null, true);
        SimpleDateFormat oSimpleDateFormat = new SimpleDateFormat(DATE_FORMAT_FOR_MODIFIED_FILE);
        for (File oFile : aFile) {
            long nDateTimeFileModify = oFile.lastModified();
            if (nDateTimeFileModify > nDateTimeLastRun) {
                mFileModified.put(oFile.getCanonicalPath(), oSimpleDateFormat.format(new Date(nDateTimeFileModify)));
            }
        }
    }

    private static void saveFileHistory(File oFileHistory, Map<String, String> mFileModified) {
        if (!mFileModified.isEmpty()) {
            PrintWriter oPrintWriter = null;
            try {
                oFileHistory.createNewFile();
                oPrintWriter = new PrintWriter(oFileHistory);
                for (Map.Entry<String, String> oFileParam : mFileModified.entrySet()) {
                    oPrintWriter.println(oFileParam.getValue() + DATE_NAME_SEPARATOR + oFileParam.getKey());
                }
            } catch (FileNotFoundException oException) {
                //log.error("Unable to save file with modifications. ", oException);
                System.err.println("Unable to save file with modifications. " + oException);
                oException.printStackTrace();
            } catch (IOException oException) {
                //log.error("Unable to save file with modifications. ", oException);
                System.err.println("Unable to save file with modifications. " + oException);
                oException.printStackTrace();
            }
            if (oPrintWriter != null)
                oPrintWriter.close();
        }

    }

}
