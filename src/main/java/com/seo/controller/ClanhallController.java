package com.seo.controller;

import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Page;
import com.seo.model.Clanhall;
import com.seo.model.Images;
import com.seo.utils.MapUtils;
import com.seo.utils.StringUtils;
import com.seo.vo.AjaxResult;
import com.seo.vo.ImagesVO;

public class ClanhallController extends Controller {
	AjaxResult result = new AjaxResult();

	/**
	 * 获取所有宗祠信息 返回JSON格式
	 */
	public void findAllClanhall() {
		Clanhall clanhall = new Clanhall();
		clanhall.set("name", getPara("name"));// 名称
		clanhall.set("review", getPara("review"));// 审核状态
		int pageNumber = getParaToInt("pageNumber");
		int pageSize = getParaToInt("pageSize");
		Page<Clanhall> list = Clanhall.dao.findAllClanhall(clanhall, pageNumber, pageSize);
		renderJson(list);
	}

	/**
	 * 获取所有宗祠信息 返回JSON格式
	 */
	public void findClanhallById() {
		String id = getPara("id");
		Clanhall clanhall = Clanhall.dao.findClanhallById(id);
		renderJson(clanhall);
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
	
	private void edit(Integer review) {
		Clanhall clanhall = new Clanhall();
		clanhall.set("nickname", getPara("nickname"));
		clanhall.set("review", review);
		clanhall.set("id", getPara("id"));// ID
		clanhall.set("name", getPara("name"));// 名称
		clanhall.set("name_of_a_hall", getPara("name_of_a_hall"));// 堂号
		clanhall.set("address", getPara("address"));// 地址
		clanhall.set("fulladdress", getPara("fulladdress"));// 详细地址
		clanhall.set("contacts", getPara("contacts"));// 联系人
		clanhall.set("tel", getPara("tel"));// 电话
		clanhall.set("synopsis", getPara("synopsis"));// 简介
		clanhall.set("details", getPara("details"));// 详情
		clanhall.set("recommend", getPara("recommend"));// 推荐
		clanhall.set("del_flg", 0);// 推荐
		clanhall.set("introduction", getPara("introduction"));// 简介
		clanhall.set("cover_image", getPara("cover_image"));// 封面图
		clanhall.set("cover_image_thumb", getPara("cover_image_thumb"));// 封面缩略图
		clanhall.set("protected_flag", getParaToInt("protected_flag",0));// 保护标识
		clanhall.set("surname", getPara("surname"));// 姓氏
		clanhall.set("def2", null);// 姓氏
		if (StringUtils.isNotNullOrEmptyStr(getPara("coordinate")))
			clanhall.set("coordinate", getPara("coordinate"));// 坐标
		else {
			String address = getPara("address") + getPara("fulladdress");
			if (StringUtils.isNotNullOrEmptyStr(address)) {
				String map = MapUtils.getLocation(address);
				JSONObject jsonObj = JSONObject.parseObject(map);
				String status = (String) jsonObj.get("status");
				if (status.equals("1")) {
					JSONArray geocodes = jsonObj.getJSONArray("geocodes");
					String location = geocodes.getJSONObject(0).getString("location");
					if (StringUtils.isNotNullOrEmptyStr(location))
						clanhall.set("coordinate", location);// 坐标

				} else {
					clanhall.set("coordinate", "根据地址获取坐标错误");// 坐标
				}
			}
		}

		List<ImagesVO> list = new ArrayList<>();
		if (StringUtils.isNotNullOrEmptyStr(getPara("images"))) {
			JSONArray json = JSONObject.parseArray(getPara("images"));
			list = json.toJavaList(ImagesVO.class);
		}
		boolean info = Clanhall.dao.edit(clanhall, convert(list));
		if (!info)
			result.addError("新增或更新失败");

		renderJson(result);
	}

	private List<Images> convert(List<ImagesVO> list) {
		List<Images> ilist = new ArrayList<>();
		for (ImagesVO images : list) {
			Images image = new Images();
			image.set("compositor", images.getCompositor());//
			image.set("image_url", images.getImage_url());//
			image.set("image_ftpurl", images.getImage_ftpurl());//
			ilist.add(image);
		}
		return ilist;
	}

	/**
	 * 批量删除
	 */
	public void deleteBatch() {
		String[] ids = getParaValues("ids");
		boolean info = Clanhall.dao.deleteBatch(ids);
		if (!info) {
			result.addError("刪除失败");
		}
		renderJson(result);
	}

	/**
	 * 批量发布
	 */
	public void release() {
		String[] ids = getParaValues("ids");
		boolean info = Clanhall.dao.recommend(ids);
		if (!info) {
			result.addError("发布失败");
		}
		renderJson(result);
	}

	/**
	 * 批量取消发布
	 */
	public void noRelease() {
		String[] ids = getParaValues("ids");
		boolean info = Clanhall.dao.noRecommend(ids);
		if (!info) {
			result.addError("取消发布失败");
		}
		renderJson(result);
	}

}
