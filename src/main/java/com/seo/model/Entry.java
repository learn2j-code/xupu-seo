package com.seo.model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import com.seo.utils.StringUtils;

/**
 * 条目
 * @author lmhc5
 *
 */
public class Entry extends Model<Entry> {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public final static Entry dao = new Entry();
	
	/**
	 * 通过条件查询
	 * @param module_id  模块ID
	 * @param website_id 站点ID
	 * @param title 标题
	 * @param abstracts 摘要
	 * @param keyword 关键词
	 * @param module_name 模块名称
	 * @param website_name 站点名称
	 * @param issue 是否发布
	 * @param release_bigen 开始时间
	 * @param release_end 结束时间
	 * @return
	 */
	public Page<Entry> findAllEntry(Entry Entry,int pageNumber,int pageSize){
		String sql="select e.id,e.nickname,e.review,e.title,e.module_id,e.author,e.source,e.release_date,e.remarks,e.issue,e.keyword,e.artical_abstract  abstract,m.module_name " ; 
		String where=" from s_entry e,s_module m where e.module_id=m.id and IFNULL(e.del_flg,0)=0 ";
		//模块ID
		if(StringUtils.isNotNullOrEmptyStr(Entry.getStr("module_id"))) {
			where+=" and e.module_id in("+Entry.getStr("module_id")+")";
		}
		/*//站点ID
		if(StringUtils.isNotNullOrEmptyStr(Entry.getStr("website_id"))) {
			where+=" and e.website_id like '%"+Entry.getStr("website_id")+"%'";
		}*/
		//审核状态
		if(StringUtils.isNotNullOrEmptyStr(Entry.getStr("review"))) {
			where+=" and e.review = "+Integer.valueOf(Entry.getStr("review"));
		}
		//标题
		if(StringUtils.isNotNullOrEmptyStr(Entry.getStr("title"))) {
			where+=" and e.title like '%"+Entry.getStr("title")+"%'";
		}
		//摘要
		if(StringUtils.isNotNullOrEmptyStr(Entry.getStr("abstracts"))) {
			where+=" and e.artical_abstract like '%"+Entry.getStr("abstracts")+"%'";
		}
		//关键词
		if(StringUtils.isNotNullOrEmptyStr(Entry.getStr("keyword"))) {
			where+=" and e.keyword like '%"+Entry.getStr("keyword")+"%' ";
		}
		//模块名称
		if(StringUtils.isNotNullOrEmptyStr(Entry.getStr("module_name"))) {
			where+=" and m.module_name like '%"+Entry.getStr("module_name")+"%' ";
		}
		/*//站点名称
		if(StringUtils.isNotNullOrEmptyStr(Entry.getStr("website_name"))) {
			where+=" and e.website_name like '%"+Entry.getStr("website_name")+"%' ";
		}*/
		//发布
		if(StringUtils.isNotNullOrEmptyStr(Entry.getStr("issue"))) {
			where+=" and IFNULL(e.issue,1)="+Entry.getStr("issue");
		} 
//		else {
//			where+=" and IFNULL(e.issue,1)=1";
//		}
		//发布件时间段
		if(StringUtils.isNotNullOrEmptyStr(Entry.getStr("release_bigen")) && !StringUtils.isNotNullOrEmptyStr(Entry.getStr("release_end"))) {
			where+=" and e.release_date >= '"+Entry.getStr("release_bigen")+"'";
		}
		if(!StringUtils.isNotNullOrEmptyStr(Entry.getStr("release_bigen")) && StringUtils.isNotNullOrEmptyStr(Entry.getStr("release_end"))) {
			where+=" and e.release_date <= '"+Entry.getStr("release_end")+"'";
		}
		if(StringUtils.isNotNullOrEmptyStr(Entry.getStr("release_bigen")) && StringUtils.isNotNullOrEmptyStr(Entry.getStr("release_end"))) {
			where+=" and e.release_date BETWEEN '"+Entry.getStr("release_bigen")+"' AND '"+Entry.getStr("release_end")+"'";
		}
		
		
		where+=" order by e.update_time desc ";
		
		
		 Page<Entry> page = dao.paginate(pageNumber, pageSize, sql,where);
		 List<Entry> entrylist=new ArrayList<>();
		 for (Entry entry : page.getList()) {
			 int id=entry.get("module_id");
				List<Relation> rlist=Relation.dao.findBymoduleName(id);
				entry.put("website_names",rlist);
				entrylist.add(entry);
		}
		// int pageNumber, int pageSize, int totalPage, int totalRow
		 Page<Entry> entrypage=new Page<Entry>(entrylist,page.getPageNumber(),page.getPageSize(),page.getTotalPage(),page.getTotalRow());
		 return entrypage;
	}
	
	/**展示界面
	 * @param pageNumber
	 * @param pageSize
	 * @return
	 */
	public Page<Entry> findShowEntry(Entry Entry,int pageNumber,int pageSize){
		
		String sql="select e.*,e.artical_abstract  abstract " ; 
		String where=" from s_entry e where IFNULL(e.del_flg,0)=0 ";
		//模块ID
		if(StringUtils.isNotNullOrEmptyStr(Entry.getStr("module_id"))) {
			where+=" and e.module_id="+Entry.getStr("module_id");
		}
		
		where+=" and IFNULL(e.issue,1)=1 and and e.release_date <=SYSDATE() order by release_date desc ";
		
		Page<Entry> page = dao.paginate(pageNumber, pageSize, sql,where);
		
		return page;
	}
	
	/**
	 * 通过ID查询数据
	 * @param id
	 * @return
	 */
	public Entry findEntryById(String id) {
		String sql="select e.*,e.artical_abstract  abstract,m.module_name,c.content,c.id content_id from s_entry e LEFT JOIN s_module m on e.module_id=m.id LEFT JOIN s_content c on e.id =c.entry_id where e.id=?";
		Entry entry = dao.findFirst(sql, id);
		int mid=entry.get("module_id");
		List<Relation> rlist=Relation.dao.findBymoduleId(mid);
		entry.put("website_names",rlist);
		return entry;
	}
	
	
	public int findEntryCount(Entry Entry) {
		String sql="select count(1) from s_entry e,s_module m " ; 

		String where=" where e.module_id=m.id and IFNULL(e.del_flg,0)=0 ";
		//模块ID
		if(StringUtils.isNotNullOrEmptyStr(Entry.getStr("module_id"))) {
			where+=" and e.module_id in("+Entry.getStr("module_id")+")";
		}
		/*//站点ID
		if(StringUtils.isNotNullOrEmptyStr(Entry.getStr("website_id"))) {
			where+=" and e.website_id like '%"+Entry.getStr("website_id")+"%'";
		}*/
		//审核状态
		if(StringUtils.isNotNullOrEmptyStr(Entry.getStr("review"))) {
			where+=" and e.review = "+Integer.valueOf(Entry.getStr("review"));
		}
		//标题
		if(StringUtils.isNotNullOrEmptyStr(Entry.getStr("title"))) {
			where+=" and e.title like '%"+Entry.getStr("title")+"%'";
		}
		//摘要
		if(StringUtils.isNotNullOrEmptyStr(Entry.getStr("abstracts"))) {
			where+=" and ,e.artical_abstract like '%"+Entry.getStr("abstracts")+"%'";
		}
		//关键词
		if(StringUtils.isNotNullOrEmptyStr(Entry.getStr("keyword"))) {
			where+=" and e.keyword like '%"+Entry.getStr("keyword")+"%' ";
		}
		//模块名称
		if(StringUtils.isNotNullOrEmptyStr(Entry.getStr("module_name"))) {
			where+=" and m.module_name like '%"+Entry.getStr("module_name")+"%' ";
		}
		/*//站点名称
		if(StringUtils.isNotNullOrEmptyStr(Entry.getStr("website_name"))) {
			where+=" and e.website_name like '%"+Entry.getStr("website_name")+"%' ";
		}*/
		//发布
		if(StringUtils.isNotNullOrEmptyStr(Entry.getStr("issue"))) {
			where+=" and IFNULL(e.issue,1)="+Entry.getStr("issue");
		} 
//		else {
//			where+=" and IFNULL(e.issue,1)=1";
//		}
		//发布件时间段
		if(StringUtils.isNotNullOrEmptyStr(Entry.getStr("release_bigen")) && !StringUtils.isNotNullOrEmptyStr(Entry.getStr("release_end"))) {
			where+=" and e.release_date >= '"+Entry.getStr("release_bigen")+"'";
		}
		if(!StringUtils.isNotNullOrEmptyStr(Entry.getStr("release_bigen")) && StringUtils.isNotNullOrEmptyStr(Entry.getStr("release_end"))) {
			where+=" and e.release_date <= '"+Entry.getStr("release_end")+"'";
		}
		if(StringUtils.isNotNullOrEmptyStr(Entry.getStr("release_bigen")) && StringUtils.isNotNullOrEmptyStr(Entry.getStr("release_end"))) {
			where+=" and e.release_date BETWEEN '"+Entry.getStr("release_bigen")+"' AND '"+Entry.getStr("release_end")+"'";
		}
		
		int count = Db.queryInt(sql+where);
		return count;
	}
	
	
	
	/**
	 * 保存、编辑返回ID
	 * @param entry
	 * @return
	 */
	public String edit(Entry entry) {
		if (!StringUtils.isNotNullOrEmptyStr(entry.getStr("id"))) {
			 entry.save();
			 return entry.getStr("id");

		} else {
			Entry qentry = findEntryById(entry.getStr("id"));
			if (qentry != null) {
				entry.update();
				return entry.getStr("id");
			} else {
				entry.save();
				return entry.getStr("id");
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
			String sql = "select * from s_entry where id in("+ids[0]+")";
			List<Record> list=Db.find(sql);
			if(!list.isEmpty()) {
				List<Record> listmodel=new ArrayList<Record>();
				for (Iterator<Record> iterator = list.iterator(); iterator.hasNext();) {
					Record record = (Record) iterator.next();
					record.set("del_flg", 1);
					listmodel.add(record);
				}

				int [] info=Db.batchUpdate("s_entry", listmodel, 15);
				if(info.length>0) {
					return true;
				}
			}
		}
		return false;
	}
	
	
	/**
	 * 通过ID批量发布
	 * @param ids
	 * @return
	 */
	public boolean release(String [] ids) {
		if(ids.length>0) {
			String sql = "select * from s_entry where id in("+ids[0]+")";
			List<Record> list=Db.find(sql);
			if(!list.isEmpty()) {
				List<Record> listmodel=new ArrayList<Record>();
				for (Iterator<Record> iterator = list.iterator(); iterator.hasNext();) {
					Record record = (Record) iterator.next();
					record.set("issue", 1);
					listmodel.add(record);
				}

				int [] info=Db.batchUpdate("s_entry", listmodel, 15);
				if(info.length>0) {
					return true;
				}
			}
		}
		return false;
	}

	
	/**
	 * 通过ID批量取消发布
	 * @param ids
	 * @return
	 */
	public boolean noRelease(String [] ids) {
		if(ids.length>0) {
			String sql = "select * from s_entry where id in("+ids[0]+")";
			List<Record> list=Db.find(sql);
			if(!list.isEmpty()) {
				List<Record> listmodel=new ArrayList<Record>();
				for (Iterator<Record> iterator = list.iterator(); iterator.hasNext();) {
					Record record = (Record) iterator.next();
					record.set("issue", 0);
					listmodel.add(record);
				}
				int [] info=Db.batchUpdate("s_entry", listmodel, 15);
				if(info.length>0) {
					return true;
				}
			}
		}
		return false;
	}

	
}
