package nlp;
import java.util.Properties;
 
public class Config
{
   Properties configFile;
   public Config()
   {
    configFile = new java.util.Properties();
    try {
      configFile.load(this.getClass().getClassLoader().
      getResourceAsStream("config.cfg"));
    }catch(Exception eta){
        eta.printStackTrace();
    }
   }
 
   public String getProperty(String key)
   {
    String value = this.configFile.getProperty(key);
    return value;
   }
   
   public String[] getSynonyms(String commandType) {
       return getProperty(commandType).trim().toLowerCase().split(","); 
   }
}