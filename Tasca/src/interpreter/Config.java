package interpreter;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
 

public class Config
{
   Properties configFile;
   private static Map<String, FolderName> folderNameRef = new HashMap<String, FolderName>();
   private static Map<FolderName, String> folderIdRef = new HashMap<FolderName, String>();
   private static Map<String, FolderName> folderIdHeader = new HashMap<String, FolderName>();
   private static Map<Integer, FolderName> intToFolderId = new HashMap<Integer, FolderName>();
   private static FolderName defaultFolder =  FolderName.FOLDER1;   
   
   public Config()
   {
       configFile = new java.util.Properties();
       try {
	   configFile.load(this.getClass().getClassLoader().
		   getResourceAsStream("Config.cfg"));
       }catch(Exception eta){
	   eta.printStackTrace();
	   System.out.println("Reading config file failed");
       }
           
       folderNameRef.put(getProperty("folder1").trim().toLowerCase(), FolderName.FOLDER1);
       folderNameRef.put(getProperty("folder2").trim().toLowerCase(), FolderName.FOLDER2);
       folderNameRef.put(getProperty("folder3").trim().toLowerCase(), FolderName.FOLDER3);
       folderNameRef.put(getProperty("folder4").trim().toLowerCase(), FolderName.FOLDER4);
       folderNameRef.put(getProperty("folder5").trim().toLowerCase(), FolderName.FOLDER5);
       folderNameRef.put("default", FolderName.DEFAULT);
       
       folderIdHeader.put("folder1", FolderName.FOLDER1);
       folderIdHeader.put("folder2", FolderName.FOLDER2);
       folderIdHeader.put("folder3", FolderName.FOLDER3);
       folderIdHeader.put("folder4", FolderName.FOLDER4);
       folderIdHeader.put("folder5", FolderName.FOLDER5);
       folderIdHeader.put("default", FolderName.DEFAULT);
       
       folderIdRef.put(FolderName.FOLDER1, getProperty("folder1").trim());
       folderIdRef.put(FolderName.FOLDER2, getProperty("folder2").trim());
       folderIdRef.put(FolderName.FOLDER3, getProperty("folder3").trim());
       folderIdRef.put(FolderName.FOLDER4, getProperty("folder4").trim());
       folderIdRef.put(FolderName.FOLDER5, getProperty("folder5").trim());
       folderIdRef.put(FolderName.DEFAULT, getProperty("default").trim());
       
       intToFolderId.put(0, FolderName.DEFAULT);
       intToFolderId.put(1, FolderName.FOLDER1);
       intToFolderId.put(2, FolderName.FOLDER2);
       intToFolderId.put(3, FolderName.FOLDER3);
       intToFolderId.put(4, FolderName.FOLDER4);
       intToFolderId.put(5, FolderName.FOLDER5);
       
       defaultFolder = folderIdHeader.get(folderIdRef.get(FolderName.DEFAULT));
       
   }

   public String getProperty(String key)
   {
    return this.configFile.getProperty(key);
   }
   
   public String[] getSynonyms(String commandType) {
       return getProperty(commandType).trim().toLowerCase().split(",");  // TODO: Remove spaces inbetween
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
   
   // Must be between 0 and 5 ASSERT
   public FolderName getFolderId (int folderInt) {
       return intToFolderId.get(folderInt);
   }
}