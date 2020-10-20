package com.seo.model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.Record;
import com.seo.utils.StringUtils;

/**
 * 模板
 * 
 * @author lmhc5
 *
 */
public class Module extends Model<Module> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public final static Module dao = new Module();

	/**
	 * 查询所有模块
	 * 
	 * @return
	 */
	public List<Module> findAllModule() {
		List<Module> list = dao.find("select * from s_module where del_flg !=1");
		List<Module> mlist=new ArrayList<>();
		for (Module module : list) {
			int id=module.get("id");
			List<Relation> rlist=Relation.dao.findBymoduleName(id);
			module.put("website_names",rlist);
			mlist.add(module);
		}
		return mlist;
	}

	/**
	 * 通过站点查询所有模块
	 * 
	 * @return
	 */
	public List<Module> findModuleByWebsiteId(String website_id) {
		List<Module> list = dao.find("select * from s_module where del_flg !=1 and website_id=?",website_id);
		return list;
	}
	
	/**
	 * 通过ID查询模块
	 * 
	 * @param id
	 * @return
	 */
	public Module findModuleById(String id) {
		Module module = dao.findById(id);
		int mid=module.get("id");
		List<Relation> rlist=Relation.dao.findBymoduleId(mid);
		module.put("website_names",rlist);
		return module;
	}

	/**
	 * 编辑保存接口
	 * 
	 * @param id
	 * @param module_name
	 * @param remarks
	 * @return
	 */
	public String edit(Module module) {
		
		if (!StringUtils.isNotNullOrEmptyStr(module.getStr("id"))) {
			 module.save();
			 return module.getStr("id");
		} else {
			Module qmodule = findModuleById(module.getStr("id"));
			if (qmodule != null) {
				module.update();
				return module.getStr("id");
			} else {
				module.save();
				return module.getStr("id");
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
			String sql = "select * from s_module where id in("+ids[0]+")";
			List<Record> list=Db.find(sql);
			if(!list.isEmpty()) {
				List<Record> listmodel=new ArrayList<Record>();
				for (Iterator<Record> iterator = list.iterator(); iterator.hasNext();) {
					Record record = (Record) iterator.next();
					record.set("del_flg", 1);
					listmodel.add(record);
				}

				int [] info=Db.batchUpdate("s_module", listmodel, 15);
				if(info.length>0) {
					return true;
				}
			}
		}
		return false;
	}
	

}
