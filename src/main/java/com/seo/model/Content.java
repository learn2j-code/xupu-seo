package com.seo.model;

import com.jfinal.plugin.activerecord.Model;
import com.seo.utils.StringUtils;

/**
 * 条目
 * @author lmhc5
 *
 */
public class Content extends Model<Content> {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public final static Content dao = new Content();
	
	/**
	 * 通过ID查询数据
	 * @param id
	 * @return
	 */
	public Content findContentById(String id) {
		String sql="select * from s_content where id=?";
		Content entry = dao.findFirst(sql, id);
		return entry;
	}

	/**
	 * 保存、编辑返回ID
	 * @param entry
	 * @return
	 */
	public boolean edit(Content content) {
		if (!StringUtils.isNotNullOrEmptyStr(content.getStr("id"))) {
			return content.save();
		} else {
			Content qcontent = findContentById(content.getStr("id"));
			if (qcontent != null) {
				return	content.update();
			} else {
				return content.save();
			}
		}
	}
}
