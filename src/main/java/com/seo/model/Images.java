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
public class Images extends Model<Images> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public final static Images dao = new Images();
	/**
	 * 批量保存
	 * 
	 * @param ids
	 * @return
	 */
	public boolean saveBatch(List<Images> list, String id) {
		if (!list.isEmpty()) {
			Db.update("delete from s_images where clan_id=? ", id);
			int[] info = Db.batchSave(list, 15);
			if (info.length > 0) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 通过ID删除图片
	 * @param id
	 * @return
	 */
	public boolean deleteById(int id) {
		boolean info=Db.deleteById("s_images", id);
		return info;
	}
	
	public List<Images> findByClanhallId(int id){
		String sql="select * from s_images where clan_id=?";
		List<Images> list=dao.find(sql, id);
		return list;
	}

}
