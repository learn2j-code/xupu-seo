package com.seo.model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.Record;
import com.seo.utils.StringUtils;

/**
 * 系统管理
 * @author lmhc5
 *
 */
public class System extends Model<System>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public final static System dao = new System();
	/**
	 * 查询所有
	 * 
	 * @return
	 */
	public List<System> findAllSystem() {
		List<System> list = dao.find("select s.*,s.website_abstract abstract ,w.website website_name from s_system s  LEFT JOIN s_website w on w.id=s.website_id where s.del_flg !=1");
		return list;
	}

	/**
	 * 通过ID查询
	 * 
	 * @param id
	 * @return
	 */
	public System findSystemById(String id) {
		//System system = dao.findById(id);
		System system =dao.findFirst("select s.*,s.website_abstract abstract from s_system s where s.id=?", id);
		return system;
	}

	/**
	 * 编辑保存接口
	 * @param id
	 * @param website_id
	 * @param keyword
	 * @param abstracts
	 * @param remarks
	 * @return
	 */
	public boolean edit(System system) {
		if (!StringUtils.isNotNullOrEmptyStr(system.getStr("id"))) {
			return system.save();
		} else {
			System qsystem= findSystemById(system.getStr("id"));
			if (qsystem != null) {
				return system.update();
			} else {
				return system.save();
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
			String sql = "select * from s_system where id in("+ids[0]+")";
			List<Record> list=Db.find(sql);
			if(!list.isEmpty()) {
				List<Record> listmodel=new ArrayList<Record>();
				for (Iterator<Record> iterator = list.iterator(); iterator.hasNext();) {
					Record record = (Record) iterator.next();
					record.set("del_flg", 1);
					listmodel.add(record);
				}

				int [] info=Db.batchUpdate("s_system", listmodel, 15);
				if(info.length>0) {
					return true;
				}
			}
		}
		return false;
	}
	
}
