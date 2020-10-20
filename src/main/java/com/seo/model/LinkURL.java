package com.seo.model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.Record;
import com.seo.utils.StringUtils;

/**
 * 友情连接
 * @author lmhc5
 *
 */
public class LinkURL extends Model<LinkURL> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public final static LinkURL dao = new LinkURL();
	
	/**
	 * 查询所有
	 * 
	 * @return
	 */
	public List<LinkURL> findAllLinkurl() {
		List<LinkURL> list = dao.find("select l.*,w.website website_name from s_linkurl l LEFT JOIN s_website w on w.id=l.website_id where l.del_flg !=1");
		return list;
	}

	/**
	 * 通过ID查询
	 * 
	 * @param id
	 * @return
	 */
	public LinkURL findLinkurlById(String id) {
		LinkURL linkurl = dao.findById(id);
		return linkurl;
	}

	/**
	 * 编辑保存接口
	 * 
	 * @param id
	 * @param module_name
	 * @param remarks
	 * @return
	 */
	public boolean edit(LinkURL linkurl) {
		if (!StringUtils.isNotNullOrEmptyStr(linkurl.getStr("id"))) {
			return linkurl.save();
		} else {
			LinkURL qlinkurl = findLinkurlById(linkurl.getStr("id"));
			if (qlinkurl != null) {
				return linkurl.update();
			} else {
				return linkurl.save();
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
			String sql = "select * from s_linkurl where id in("+ids[0]+")";
			List<Record> list=Db.find(sql);
			if(!list.isEmpty()) {
				List<Record> listmodel=new ArrayList<Record>();
				for (Iterator<Record> iterator = list.iterator(); iterator.hasNext();) {
					Record record = (Record) iterator.next();
					record.set("del_flg", 1);
					listmodel.add(record);
				}

				int [] info=Db.batchUpdate("s_linkurl", listmodel, 15);
				if(info.length>0) {
					return true;
				}
			}
		}
		return false;
	}
	
}
