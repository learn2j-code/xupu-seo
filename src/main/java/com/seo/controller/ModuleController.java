package com.seo.controller;

import java.util.ArrayList;
import java.util.List;

import com.seo.model.Module;
import com.seo.model.Relation;
import com.seo.utils.StringUtils;
import com.seo.vo.AjaxResult;

/**
 * 模块控制器
 * 
 * @author lmhc5
 *
 */
public class ModuleController extends BaseController {
	AjaxResult result = new AjaxResult();

	/**
	 * 获取所有模块信息 返回JSON格式
	 */
	public void findAllModule() {
		List<Module> list = Module.dao.findAllModule();
		renderJson(list);
	}

	/**
	 * 通过获取所有模块信息 返回JSON格式
	 */
	public void findModuleByWebsiteId() {
		String website_id = getPara("website_id");
		List<Module> list = Module.dao.findModuleByWebsiteId(website_id);
		renderJson(list);
	}

	/**
	 * 通过ID查询模块信息 返回JSON格式
	 */
	public void findModuleById() {
		String id = getPara("id");
		Module module = Module.dao.findModuleById(id);
		renderJson(module);
	}

	/**
	 * 新增/编辑
	 */
	public void edit() {
		Module module = new Module();
		module.set("id", getPara("id"));// ID
		module.set("module_name", getPara("module_name"));// 模块名称
		module.set("remarks", getPara("remarks"));// 备注
		//module.put("relation", getParaValues("relation_ids"));
		String info = Module.dao.edit(module);
		if (StringUtils.isNotNullOrEmptyStr(info)) {
			List<Relation> list = new ArrayList<Relation>();
			//String[] relation_ids = getParaValues("relation_ids[]");
			String[] relation_ids = getPara("relation_ids").split(",");
			for (String string : relation_ids) {
				Relation relation = new Relation();
				relation.set("website_id", string.trim());
				relation.set("module_id", info);
				list.add(relation);
			}
			boolean re = Relation.dao.saveBatch(list, info);
			if (!re)
				result.addError("新增或更新失败");

		} else
			result.addError("新增或更新失败");

		renderJson(result);
	}

	/**
	 * 批量删除
	 */
	public void deleteBatch() {
		String[] ids = getParaValues("ids");
		boolean info = Module.dao.deleteBatch(ids);
		if (!info) {
			result.addError("刪除失败");
		}
		renderJson(result);
	}

}
