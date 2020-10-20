package com.seo.controller;

import java.util.List;

import com.seo.model.LinkURL;
import com.seo.vo.AjaxResult;

/**
 * 友情链接
 * @author lmhc5
 *
 */
public class LinkURLController  extends BaseController {
	AjaxResult result =new AjaxResult();
	/**
	 * 获取所有模块信息
	 * 返回JSON格式
	 */
	public void findAllLinkurl() {
		List<LinkURL> list=LinkURL.dao.findAllLinkurl();
		renderJson(list);
	}
	
	/**
	 * 通过ID查询模块信息
	 * 返回JSON格式
	 */
	public void findLinkurlById() {
		String id=getPara("id");
		LinkURL linkurl=LinkURL.dao.findLinkurlById(id);
		renderJson(linkurl);
	}
	
	/**
	 * 新增/编辑
	 */
	public void edit() {
		LinkURL linkurl=new LinkURL();
		linkurl.set("id", getPara("id"));
		linkurl.set("website_id", getPara("website_id"));
		linkurl.set("station_name", getPara("station_name"));
		linkurl.set("weburl", getPara("weburl"));
		linkurl.set("remarks", getPara("remarks"));
		boolean info=LinkURL.dao.edit(linkurl);
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
		boolean info=LinkURL.dao.deleteBatch(ids);
		if (!info) {
        	result.addError("刪除失败");
        }
		renderJson(result);
	}
}
