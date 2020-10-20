package com.seo.controller;

import java.util.List;

import com.seo.model.Website;
import com.seo.vo.AjaxResult;

/**
 * 站点
 * @author lmhc5
 *
 */
public class WebsiteController extends BaseController {
	AjaxResult result =new AjaxResult();
	/**
	 * 获取所有站点信息
	 * 返回JSON格式
	 */
	public void findAllWebsite() {
		//getResponse().addHeader("Access-Control-Allow-Origin", "*");
		List<Website> list=Website.dao.findAllWebsite();
		renderJson(list);
	}
	
	/**
	 * 通过ID查询站点信息
	 * 返回JSON格式
	 */
	public void findWebsiteById() {
		String id=getPara("id");
		Website website=Website.dao.findWebsiteById(id);
		renderJson(website);
	}
	
	/**
	 * 通过模块ID查询站点信息
	 * 返回JSON格式
	 */
	public void findWebsiteByModuleId() {
		String id=getPara("module_id");
		List<Website> website=Website.dao.findAllWebsiteByModelId(id);
		renderJson(website);
	}
	
	/**
	 * 新增/编辑
	 */
	public void edit() {
		Website website=new Website();
		website.set("id", getPara("id"));
		website.set("url", getPara("url"));
		website.set("keyword", getPara("keyword"));
		website.set("website_abstract", getPara("website_abstract"));
		website.set("website", getPara("website"));
		website.set("remarks", getPara("remarks"));
		boolean info=Website.dao.edit(website);
		if (!info) {
        	result.addError("新增或更新失败");
        }
		renderJson(result);
	}
	
	/**
	 * 批量删除
	 */
	public void deleteBatch() {
		String [] ids=getParaValues("ids");
		boolean info=Website.dao.deleteBatch(ids);
		if (!info) {
        	result.addError("刪除失败");
        }
		renderJson(result);
	}
}
