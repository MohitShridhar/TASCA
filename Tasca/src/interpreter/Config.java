package interpreter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import controller.Controller;

import junit.framework.Assert;

//@author A0105912N
public class Config
{
    public final static Logger logger = Controller.getLogger();
    
    private static final String DELIMITER_PARAMETER = ",";
    private static final String FILENAME_USER_CONFIG_FILE = "Config.cfg";
    private static final String MESSAGE_GENERATING_CONFIG_FILE = "Config.cfg file not found. Generating Default Configurations";
    private static final String FILENAME_DEFAULT_CONFIG_FILE = "Default_Config.cfg";
    
    private static final String MESSAGE_SERIOUS_ERROR_DEFAULT_CONFIG_NOT_FOUND = "Serious error: Default config file not found";
    private static final String MESSAGE_DEFAULT_CONFIG_SAVE_FAILED = "Could save default config file. Settings can't be edited";
    
    private static final int INT_FOLDER5 = 5;
    private static final int INT_FOLDER4 = 4;
    private static final int INT_FOLDER3 = 3;
    private static final int INT_FOLDER2 = 2;
    private static final int INT_FOLDER1 = 1;
    private static final int INT_DEFAULT = 0;
    
    private static final String DEFAULT_ID_STRING = "default";
    private static final String FOLDER5_ID_STRING = "folder5";
    private static final String FOLDER4_ID_STRING = "folder4";
    private static final String FOLDER3_ID_STRING = "folder3";
    private static final String FOLDER2_ID_STRING = "folder2";
    private static final String FOLDER1_ID_STRING = "folder1";
    
    Properties configFile;
    
    private static Map<String, FolderName> folderNameRef = new HashMap<String, FolderName>();
    private static Map<FolderName, String> folderIdRef = new HashMap<FolderName, String>();
    private static Map<String, FolderName> folderIdHeader = new HashMap<String, FolderName>();
    private static Map<Integer, FolderName> intToFolderId = new HashMap<Integer, FolderName>();
    
    private static FolderName defaultFolder = FolderName.FOLDER1;   
    
    public Config()
    {
	clearAllMaps();
	loadConfigFile();

	buildFolderNameRef();
	buildFolderIdHeader();
	buildFolderIdRef();
	buildIntToFolderId();
    }

    public void buildIntToFolderId() {
	// To be used to decode logic's 'int' method of specifying folders:
	
	intToFolderId.put(INT_DEFAULT, FolderName.DEFAULT);
	intToFolderId.put(INT_FOLDER1, FolderName.FOLDER1);
	intToFolderId.put(INT_FOLDER2, FolderName.FOLDER2);
	intToFolderId.put(INT_FOLDER3, FolderName.FOLDER3);
	intToFolderId.put(INT_FOLDER4, FolderName.FOLDER4);
	intToFolderId.put(INT_FOLDER5, FolderName.FOLDER5);

	defaultFolder = folderIdHeader.get(folderIdRef.get(FolderName.DEFAULT));
    }

    public void buildFolderIdRef() {
	folderIdRef.put(FolderName.FOLDER1, getProperty(FOLDER1_ID_STRING).trim());
	folderIdRef.put(FolderName.FOLDER2, getProperty(FOLDER2_ID_STRING).trim());
	folderIdRef.put(FolderName.FOLDER3, getProperty(FOLDER3_ID_STRING).trim());
	folderIdRef.put(FolderName.FOLDER4, getProperty(FOLDER4_ID_STRING).trim());
	folderIdRef.put(FolderName.FOLDER5, getProperty(FOLDER5_ID_STRING).trim());
	folderIdRef.put(FolderName.DEFAULT, getProperty(DEFAULT_ID_STRING).trim());
    }

    public void buildFolderIdHeader() {
	folderIdHeader.put(FOLDER1_ID_STRING, FolderName.FOLDER1);
	folderIdHeader.put(FOLDER2_ID_STRING, FolderName.FOLDER2);
	folderIdHeader.put(FOLDER3_ID_STRING, FolderName.FOLDER3);
	folderIdHeader.put(FOLDER4_ID_STRING, FolderName.FOLDER4);
	folderIdHeader.put(FOLDER5_ID_STRING, FolderName.FOLDER5);
	folderIdHeader.put(DEFAULT_ID_STRING, FolderName.DEFAULT);
    }

    private void clearAllMaps() {
	folderNameRef.clear();
	folderIdRef.clear();
	folderIdHeader.clear();
	intToFolderId.clear();
    }

    public void buildFolderNameRef() {
	folderNameRef.put(getProperty(FOLDER1_ID_STRING).trim().toLowerCase(), FolderName.FOLDER1);
	folderNameRef.put(getProperty(FOLDER2_ID_STRING).trim().toLowerCase(), FolderName.FOLDER2);
	folderNameRef.put(getProperty(FOLDER3_ID_STRING).trim().toLowerCase(), FolderName.FOLDER3);
	folderNameRef.put(getProperty(FOLDER4_ID_STRING).trim().toLowerCase(), FolderName.FOLDER4);
	folderNameRef.put(getProperty(FOLDER5_ID_STRING).trim().toLowerCase(), FolderName.FOLDER5);
	folderNameRef.put(DEFAULT_ID_STRING, FolderName.DEFAULT);
    }

    public void loadConfigFile() {
	configFile = new java.util.Properties();
	try {
	    configFile.load(new FileInputStream(FILENAME_USER_CONFIG_FILE));

	}catch(Exception eta){
	    logger.log(Level.WARNING, MESSAGE_GENERATING_CONFIG_FILE);
	    loadDefaultConfig();
	}
    }

    public void loadDefaultConfig() {
	try {
	    configFile.load(Config.class.getResourceAsStream(FILENAME_DEFAULT_CONFIG_FILE));
	}catch(Exception severeException){
	    logger.log(Level.SEVERE, MESSAGE_SERIOUS_ERROR_DEFAULT_CONFIG_NOT_FOUND + severeException.getStackTrace());
	    Assert.fail(MESSAGE_SERIOUS_ERROR_DEFAULT_CONFIG_NOT_FOUND);
	}

	saveDefaultConfig();
    }

    public void saveDefaultConfig() {
	try {
	    configFile.store(new FileOutputStream(FILENAME_USER_CONFIG_FILE), null);
	} catch (FileNotFoundException serverException) {
	    logger.log(Level.SEVERE, MESSAGE_DEFAULT_CONFIG_SAVE_FAILED + serverException.getStackTrace());
	    Assert.fail(MESSAGE_DEFAULT_CONFIG_SAVE_FAILED);
	} catch (IOException serverException) {
	    logger.log(Level.SEVERE, MESSAGE_DEFAULT_CONFIG_SAVE_FAILED + serverException.getStackTrace());
	    Assert.fail(MESSAGE_DEFAULT_CONFIG_SAVE_FAILED);
	}
    }

    public String getProperty(String key)
    {
	return this.configFile.getProperty(key);
    }

    public String[] getSynonyms(String commandType) {
	return getProperty(commandType).trim().toLowerCase().split(DELIMITER_PARAMETER); 
    }

    public FolderName getFolderId(String folderName) {
	return folderNameRef.get(folderName);
    }

    public String getFolderName(FolderName folderId) {
	return folderIdRef.get(folderId);
    }

    public FolderName getDefaultFolder() {
	return defaultFolder;
    }

    public FolderName getFolderId (int folderInt) {
	assert (folderInt >= INT_DEFAULT && folderInt <= INT_FOLDER5);
	return intToFolderId.get(folderInt);
    }

    public Properties getConfigFile() {
	return configFile;
    }
}