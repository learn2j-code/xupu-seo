/*
 * _MappingKit.java
 * 
 * @author gaojinfang0608@rayootech.com
 * 
 * @date 2016年1月30日 下午3:43:03
 */
package com.seo.common;

import com.jfinal.plugin.activerecord.ActiveRecordPlugin;
import com.seo.model.Clanhall;
import com.seo.model.Content;
import com.seo.model.Entry;
import com.seo.model.Images;
import com.seo.model.LinkURL;
import com.seo.model.Module;
import com.seo.model.Relation;
import com.seo.model.System;
import com.seo.model.Website;

/**
 * Model和表名做对应关系
 * @author lmhc5
 *
 */
public class _MappingKit {

  public static void mapping(ActiveRecordPlugin arp) {
      //arp.addMapping("user", "id", User.class);
	  arp.addMapping("s_module", "id", Module.class);
	  arp.addMapping("s_entry", "id", Entry.class);
	  arp.addMapping("s_content", "id", Content.class);
	  arp.addMapping("s_linkurl", "id", LinkURL.class);
	  arp.addMapping("s_system", "id", System.class);
	  arp.addMapping("s_website", "id", Website.class);
	  arp.addMapping("s_wm_relation", "id", Relation.class);
	  arp.addMapping("s_clan_hall", "id", Clanhall.class);
	  arp.addMapping("s_images", "id", Images.class);
  }
}