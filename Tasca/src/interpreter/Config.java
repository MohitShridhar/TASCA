package interpreter;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
 
public class Config
{
   Properties configFile;
   private static Map<String, FolderName> folderNameRef = new HashMap<String, FolderName>();
   private static Map<FolderName, String> folderIdRef = new HashMap<FolderName, String>();
   
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
           
       folderNameRef.put(getProperty("folder1").trim(), FolderName.FOLDER1);
       folderNameRef.put(getProperty("folder2").trim(), FolderName.FOLDER2);
       folderNameRef.put(getProperty("folder3").trim(), FolderName.FOLDER3);
       folderNameRef.put(getProperty("folder4").trim(), FolderName.FOLDER4);
       folderNameRef.put(getProperty("folder5").trim(), FolderName.FOLDER5);
       folderNameRef.put(getProperty("default").trim(), FolderName.DEFAULT);
       
       folderIdRef.put(FolderName.FOLDER1, getProperty("folder1").trim());
       folderIdRef.put(FolderName.FOLDER2, getProperty("folder2").trim());
       folderIdRef.put(FolderName.FOLDER3, getProperty("folder3").trim());
       folderIdRef.put(FolderName.FOLDER4, getProperty("folder4").trim());
       folderIdRef.put(FolderName.FOLDER5, getProperty("folder5").trim());
       folderIdRef.put(FolderName.DEFAULT, getProperty("default").trim());
       
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
}