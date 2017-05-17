package com.sideprojects.cachesystems.util;

public class CacheCleaner extends Thread {
	ConcurrentHashMap<String,ValueObj> cache = null;
	TreeMap<Long,ArrayList<String>> expiryIndex = null;
	Map.Entry<Long,ArrayList<String>> indexElement = null;

	int cleanUpFrequency = 1;

	CacheCleaner(ConcurrentHashMap<String,ValueObj> cache,TreeMap<Long,ArrayList<String>> expiryIndex,int cleanUpFrequency)
	{ 
       this.cache = cache;
       this.expiryIndex = expiryIndex;
       this.cleanUpFrequency = cleanUpFrequency;
	}

	public void run()
	{
	  long currentTime = null;
      while(true)
      {
         currentTime = System.currentTimeMills();
         indexElement = cache.floorEntry(currentTime);

         while(indexElement != null)
         {
         	Iterator<String> iterator = indexElement.getValue().iterator();

         	while(iterator.hasNext())
         	{
              String key = iterator.next();
              cache.remove(key);
         	}
         	expiryIndex.remove(currentTime);
            indexElement = cache.floorEntry(currentTime);
         }

         try
         {
           synchronized(this)
           {
           	wait(cleanUpFrequency*30000);
           }
         }
         catch(InterruptedException e)
         {
            e.printStackTrace();
         }
      }
	}
}
