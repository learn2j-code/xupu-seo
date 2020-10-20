package com.seo.controller;

import java.util.List;

import com.seo.model.System;
import com.seo.vo.AjaxResult;

/**
 * 系统
 * @author lmhc5
 *
 */
public class SystemController extends BaseController {
	AjaxResult result =new AjaxResult();
	/**
	 * 获取所有系统信息
	 * 返回JSON格式
	 */
	public void findAllSystem() {
		List<com.seo.model.System> list=com.seo.model.System.dao.findAllSystem();
		renderJson(list);
	}
	
	/**
	 * 通过ID查询系统信息
	 * 返回JSON格式
	 */
	public void findSystemById() {
		String id=getPara("id");
		com.seo.model.System system=com.seo.model.System.dao.findSystemById(id);
		renderJson(system);
	}
	
	/**
	 * 新增/编辑
	 */
	public void edit() {
		com.seo.model.System system=new System();
		system.set("id", getPara("id"));
		system.set("website_id", getPara("website_id"));
		system.set("keyword", getPara("keyword"));
		system.set("website_abstract", getPara("abstracts"));
		system.set("remarks", getPara("remarks"));
		
		boolean info=com.seo.model.System.dao.edit(system);
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
		boolean info=com.seo.model.System.dao.deleteBatch(ids);
		if (!info) {
        	result.addError("刪除失败");
        }
		renderJson(result);
	}
}
