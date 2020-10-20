package com.seo.interceptor;

import com.jfinal.aop.Interceptor;
import com.jfinal.aop.Invocation;

public class CorsInterceptor implements Interceptor{
   @Override
   public void intercept(Invocation inv){
	   inv.getController().getResponse().addHeader("Access-Control-Allow-Origin", "*");
       inv.invoke();
   }   
}