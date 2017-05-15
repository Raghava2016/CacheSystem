
import java.util.ArrayList;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;

public class CacheSystem
{
    private ConcurrentHashMap<String,Object> cache = null;
    private TreeMap<Long,ArrayList<String>> expiryIndex = null;

	CacheSystem()
	{
      cache = new ConcurrentHashMap<String,Object>();
      expiryIndex = new TreeMap<Long,ArrayList<String>>();
	}

	public void add(String key,Object object,long inputTTL)
	{
      long timeToLive = System.currentTimeInMills() + (inputTTL * 1000);
      cache.put(key,object);

      ArrayList<String> keys = expiryIndex.get(timeToLive);
      if(keys == null)
      {
      	keys = new ArrayList<String>();
      }
      keys.add(key);
      expiryIndex.put(timeToLive,keys);
	}
}