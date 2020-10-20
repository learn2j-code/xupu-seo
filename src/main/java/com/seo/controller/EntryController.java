package com.seo.controller;

import com.jfinal.plugin.activerecord.Page;
import com.seo.model.Content;
import com.seo.model.Entry;
import com.seo.utils.StringUtils;
import com.seo.vo.AjaxResult;

/**
 * 条目
 * 
 * @author lmhc5
 *
 */
public class EntryController extends BaseController {

	AjaxResult result = new AjaxResult();

	/**
	 * 获取所有模块信息 返回JSON格式
	 */
	public void findAllEntry() {
		Entry entry = new Entry();
		entry.set("review", getPara("review"));
		entry.set("module_id", getPara("module_id"));// 模块ID
		entry.set("website_id", getPara("website_id"));// 站点ID
		entry.set("title", getPara("title"));// 标题
		entry.set("artical_abstract", getPara("abstracts"));// 摘要
		entry.set("keyword", getPara("keyword"));// 关键词
		entry.set("issue", getPara("issue"));// 是否发布
		entry.put("module_name", getPara("module_name"));// 模块名称
		//entry.put("website_name", getPara("website_name"));// 站点名称
		entry.put("release_bigen", getPara("release_bigen"));// 开始时间
		entry.put("release_end", getPara("release_end"));// 结束时间
		
		int pageNumber=getParaToInt("pageNumber");
		int pageSize=getParaToInt("pageSize");
		
		Page<Entry> list = Entry.dao.findAllEntry(entry,pageNumber,pageSize);
		
		renderJson(list);
	}

	/**
	 * 展示界面
	 */
	public void findShowEntry() {
		Entry entry = new Entry();
		entry.set("module_id", getPara("module_id"));// 模块ID
		entry.set("website_id", getPara("website_id"));// 站点ID
		
		int pageNumber=getParaToInt("pageNumber");
		int pageSize=getParaToInt("pageSize");
		
		Page<Entry> list = Entry.dao.findShowEntry(entry,pageNumber,pageSize);
		
		renderJson(list);
	}
	/**
	 * 获取总条数
	 */
	public void findEntryCount() {
		Entry entry = new Entry();
		entry.set("review", getPara("review"));
		entry.set("module_id", getPara("module_id"));// 模块ID
		//entry.set("website_id", getPara("website_id"));// 站点ID
		entry.set("title", getPara("title"));// 标题
		entry.set("artical_abstract", getPara("abstracts"));// 摘要
		entry.set("keyword", getPara("keyword"));// 关键词
		entry.set("issue", getPara("issue"));// 是否发布
		entry.put("module_name", getPara("module_name"));// 模块名称
		//entry.put("website_name", getPara("website_name"));// 站点名称
		entry.put("release_bigen", getPara("release_bigen"));// 开始时间
		entry.put("release_end", getPara("release_end"));// 结束时间
		int count=Entry.dao.findEntryCount(entry);
		entry.put("count",count);
		renderJson(entry);
	}
	
	
	/**
	 * 通过ID查询模块信息 返回JSON格式
	 */
	public void findEntryById() {
		String id = getPara("id");
		Entry entry = Entry.dao.findEntryById(id);
		renderJson(entry);
	}

	public void saveDraft(){
		Integer review = 1;
		edit(review);
	}
	public void submitAudit(){
		Integer review = 3;
		edit(review);
	}
	public void saveAll(){
		Integer review = 4;
		edit(review);
	}
	public void sendBack(){
		Integer review = 2;
		edit(review);
	}
	/**
	 * 编辑、新增
	 */
	private void edit(Integer review) {
		Entry entry = new Entry();
		entry.set("review", review);
		entry.set("nickname", getPara("nickname"));
		entry.set("id", getPara("id"));// ID
		entry.set("module_id", getPara("module_id"));// 模块ID
		//entry.set("website_id", getPara("website_id"));// 站点ID
		//entry.put("website_name", getPara("website_name"));// 模块名称
		entry.set("title", getPara("title"));// 标题
		entry.set("author", getPara("author"));//作者
		entry.set("source", getPara("source"));//来源
		entry.set("artical_abstract", getPara("abstracts"));// 摘要
		entry.set("keyword", getPara("keyword"));// 关键词
		entry.set("issue", getPara("issue"));// 是否发布
		entry.set("release_date", getPara("release_date"));// 发布时间
		entry.set("thumbnail", getPara("thumbnail"));//缩略图
		entry.set("image_url", getPara("image_url"));//本地地址
		entry.set("image_ftpurl", getPara("image_ftpurl"));//FTP地址
		String id = Entry.dao.edit(entry);
		if (StringUtils.isNotNullOrEmptyStr(id) && StringUtils.isNotNullOrEmptyStr(getPara("content"))) {
			Content content = new Content();
			content.set("id", getPara("content_id"));
			content.set("entry_id", id);
			content.set("content", getPara("content"));
			content.set("remarks", getPara("remarks"));
			boolean info = Content.dao.edit(content);
			if (!info) {
				result.addError("新增或更新失败");
			}
		} else {
			result.addError("新增或更新失败");
		}
		renderJson(result);
	}

	/**
	 * 批量删除
	 */
	public void deleteBatch() {
		String [] ids=getParaValues("ids");
		boolean info=Entry.dao.deleteBatch(ids);
		if (!info) {
        	result.addError("刪除失败");
        }
		renderJson(result);
	}
	
	/**
	 * 批量发布
	 */
	public void release() {
		String [] ids=getParaValues("ids");
		boolean info=Entry.dao.release(ids);
		if (!info) {
        	result.addError("发布失败");
        }
		renderJson(result);
	}
	
	/**
	 * 批量取消发布
	 */
	public void noRelease() {
		String [] ids=getParaValues("ids");
		boolean info=Entry.dao.noRelease(ids);
		if (!info) {
        	result.addError("取消发布失败");
        }
		renderJson(result);
	}

}
