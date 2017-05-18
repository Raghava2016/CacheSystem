package com.sideprojects.cachesystems.util;

public class ValueObj {
  Object obj;
  long expiry;

  public ValueObj(Object obj,long expiry)
  {
    this.obj = obj;
    this.expiry = expiry+System.currentTimeMillis();
  }
  
  public boolean isExpired()
  {
    return expiry <= System.currentTimeMillis();
  }
}
