package com.seo.model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.Record;
import com.seo.utils.StringUtils;

/**
 * 站点
 * @author lmhc5
 *
 */
public class Website extends Model<Website>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public final static Website dao = new Website();
	/**
	 * 查询所有站点
	 * 
	 * @return
	 */
	public List<Website> findAllWebsite() {
		List<Website> list = dao.find("select * from s_website where del_flg !=1");
		return list;
	}
	
	public List<Website> findAllWebsiteByModelId(String id ) {
		List<Website> list = dao.find("select website from s_website where del_flg !=1 and id in(select website_id from s_wm_relation where module_id=?)",id);
		return list;
	}

	/**
	 * 通过ID查询站点
	 * 
	 * @param id
	 * @return
	 */
	public Website findWebsiteById(String id) {
		Website website = dao.findById(id);
		return website;
	}

	/**
	 * 编辑保存接口
	 * 
	 * @param id
	 * @param module_name
	 * @param remarks
	 * @return
	 */
	public boolean edit(Website website) {
		if (!StringUtils.isNotNullOrEmptyStr(website.getStr("id"))) {
			return website.save();
		} else {
			Website swebsite = findWebsiteById(website.getStr("id"));
			if (swebsite != null) {
				return website.update();
			} else {
				return website.save();
			}

		}
	}
	
	/**
	 * 通过ID批量删除模版数据
	 * @param ids
	 * @return
	 */
	public boolean deleteBatch(String [] ids) {
		if(ids.length>0) {
			String sql = "select * from s_website where id in("+ids[0]+")";
			List<Record> list=Db.find(sql);
			if(!list.isEmpty()) {
				List<Record> listmodel=new ArrayList<Record>();
				for (Iterator<Record> iterator = list.iterator(); iterator.hasNext();) {
					Record record = (Record) iterator.next();
					record.set("del_flg", 1);
					listmodel.add(record);
				}

				int [] info=Db.batchUpdate("s_website", listmodel, 15);
				if(info.length>0) {
					return true;
				}
			}
		}
		return false;
	}
	
}
