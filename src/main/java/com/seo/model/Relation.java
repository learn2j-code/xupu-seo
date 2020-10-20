package com.seo.model;

import java.util.List;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Model;

/**
 * 模块和站点的中间表
 * 
 * @author Lenovo
 *
 */
public class Relation extends Model<Relation> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public final static Relation dao = new Relation();
	/**
	 * 批量保存
	 * 
	 * @param ids
	 * @return
	 */
	public boolean saveBatch(List<Relation> list, String module_id) {
		if (!list.isEmpty()) {
			Db.update("delete from s_wm_relation where module_id=? ", module_id);
			int[] info = Db.batchSave(list, 15);
			if (info.length > 0) {
				return true;
			}
		}
		return false;
	}

	public List<Relation> findBymoduleId(int id){
		String sql="select website_id from s_wm_relation where module_id=?";
		List<Relation> list=dao.find(sql, id);
		return list;
	}
	public List<Relation> findBymoduleName(int id){
		String sql="select website from s_website where del_flg !=1 and id in(select website_id from s_wm_relation where module_id=?)";
		List<Relation> list=dao.find(sql, id);
		return list;
	}
}
