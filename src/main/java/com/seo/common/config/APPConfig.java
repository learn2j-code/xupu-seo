package com.seo.common.config;


import java.io.File;

import javax.servlet.http.HttpServletRequest;

import com.alibaba.druid.filter.stat.StatFilter;
import com.alibaba.druid.wall.WallFilter;
import com.jfinal.config.Constants;
import com.jfinal.config.Handlers;
import com.jfinal.config.Interceptors;
import com.jfinal.config.JFinalConfig;
import com.jfinal.config.Plugins;
import com.jfinal.config.Routes;
import com.jfinal.ext.handler.ContextPathHandler;
import com.jfinal.ext.interceptor.SessionInViewInterceptor;
import com.jfinal.kit.PathKit;
import com.jfinal.kit.PropKit;
import com.jfinal.log.Log;
import com.jfinal.plugin.activerecord.ActiveRecordPlugin;
import com.jfinal.plugin.druid.DruidPlugin;
import com.jfinal.plugin.druid.DruidStatViewHandler;
import com.jfinal.plugin.druid.IDruidStatViewAuth;
import com.jfinal.plugin.ehcache.EhCachePlugin;
import com.jfinal.render.ViewType;
import com.jfinal.template.Engine;
import com.jfinal.weixin.sdk.api.ApiConfigKit;
import com.seo.common._MappingKit;
import com.seo.controller.ClanhallController;
import com.seo.controller.EntryController;
import com.seo.controller.FileController;
import com.seo.controller.LinkURLController;
import com.seo.controller.ModuleController;
import com.seo.controller.SystemController;
import com.seo.controller.WebsiteController;
import com.seo.interceptor.CorsInterceptor;


/**
 * @author chenpeng
 */
public class APPConfig extends JFinalConfig {
	static Log log = Log.getLog(ModuleController.class);
	/**
	 * 如果生产环境配置文件存在，则优先加载该配置，否则加载开发环境配置文件
	 * 
	 * @param pro
	 *            生产环境配置文件
	 * @param dev
	 *            开发环境配置文件
	 */
	public void loadProp(String pro, String dev) {
		try {
			PropKit.use(pro);
		} catch (Exception e) {
			PropKit.use(dev);
		}
	}

	/**
	 * 配置常量
	 */
	public void configConstant(Constants me) {
		 //读取数据库配置文件
        loadProp("seo_pro.properties", "seo.properties");
		me.setDevMode(PropKit.getBoolean("devMode", false));
		me.setEncoding("utf-8");
		me.setViewType(ViewType.JSP);
		// 设置上传文件保存的路径
		me.setBaseUploadPath(PathKit.getWebRootPath() + File.separator + "myupload");
		// ApiConfigKit 设为开发模式可以在开发阶段输出请求交互的 xml 与 json 数据
		ApiConfigKit.setDevMode(me.getDevMode());

	}

	/**
	 * 配置路由
	 */
	public void configRoute(Routes me) {
		   // me.add("/admin", AdminController.class);
		   // me.add("/", HomeController.class);
	       // me.add("/product", ProductController.class);
	       // me.add("/user", UserController.class);
	       // me.add("/detail", DetailController.class);
	       // me.add("/message", MessageController.class);
		
			//上传文件
	        me.add("/file", FileController.class);
	        //模块
	        me.add("/module", ModuleController.class);
	        //记录
	        me.add("/entry", EntryController.class);
	        //友情链接
	        me.add("/linkurl", LinkURLController.class);
	        //系统管理
	        me.add("/system", SystemController.class);
	        //站点管理
	        me.add("/website", WebsiteController.class);
	        //宗祠管理
	        me.add("/clanhall", ClanhallController.class);
	        
	        
	        
	}

	/**
	 * 配置插件
	 */
	public void configPlugin(Plugins me) {
		// 配置ActiveRecord插件
		DruidPlugin druidPlugin = createDruidPlugin();
		me.add(druidPlugin);
		// 配置ActiveRecord插件
		ActiveRecordPlugin arp = new ActiveRecordPlugin(druidPlugin);
		 // 所有配置在 MappingKit 中搞定
	    _MappingKit.mapping(arp);
		arp.setShowSql(PropKit.getBoolean("devMode", false));
		me.add(arp);
		// ehcahce插件配置
		me.add(new EhCachePlugin());
	}

	public static DruidPlugin createDruidPlugin() {
		String jdbcUrl = PropKit.get("jdbcUrl");
		String user = PropKit.get("user");
		String password = PropKit.get("password");
		log.info(jdbcUrl + " " + user + " " + password);
		// 配置druid数据连接池插件
		DruidPlugin dp = new DruidPlugin(jdbcUrl, user, password);
		// 配置druid监控
		dp.addFilter(new StatFilter());
		WallFilter wall = new WallFilter();
		wall.setDbType("mysql");
		dp.addFilter(wall);
		return dp;
	}

	/**
	 * 配置全局拦截器
	 */
	public void configInterceptor(Interceptors me) {
		me.add(new SessionInViewInterceptor());
		me.add(new CorsInterceptor());
		
	}
	/**
	 * 配置处理器
	 */
	public void configHandler(Handlers me) {
		// Druid监控
		DruidStatViewHandler dvh = new DruidStatViewHandler("/druid", new IDruidStatViewAuth() {

			@Override
			public boolean isPermitted(HttpServletRequest request) {
				return true;
			}
		});
		me.add(dvh);
		me.add(new ContextPathHandler("ctx"));  
	}

	
	@Override
	public void configEngine(Engine me) {
		// TODO 自动生成的方法存根
		
	}
	 
}
