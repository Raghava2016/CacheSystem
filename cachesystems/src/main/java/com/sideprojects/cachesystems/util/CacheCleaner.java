package com.sideprojects.cachesystems.util;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;

public class CacheCleaner extends Thread {
	ConcurrentHashMap<String,ValueObj> cache = null;
	TreeMap<Long,ArrayList<String>> expiryIndex = null;
	Map.Entry<Long,ArrayList<String>> indexElement = null;

	int cleanUpFrequency = 1;

	public CacheCleaner(ConcurrentHashMap<String,ValueObj> cache,TreeMap<Long,ArrayList<String>> expiryIndex,int cleanUpFrequency)
	{ 
       this.cache = cache;
       this.expiryIndex = expiryIndex;
       this.cleanUpFrequency = cleanUpFrequency;
	}

	public void run()
	{
	  Long currentTime = null;
      while(true)
      {
         currentTime = System.currentTimeMillis();
         indexElement = expiryIndex.floorEntry(currentTime);

         while(indexElement != null)
         {
         	Iterator<String> iterator = indexElement.getValue().iterator();

         	while(iterator.hasNext())
         	{
              String key = iterator.next();
              cache.remove(key);
              System.out.println("Removing "+key);
         	}
         	expiryIndex.remove(indexElement.getKey());
            indexElement = expiryIndex.floorEntry(currentTime);
         }

         try
         {
           synchronized(this)
           {
           	wait(cleanUpFrequency*30);
           }
         }
         catch(InterruptedException e)
         {
            e.printStackTrace();
         }
      }
	}
}
