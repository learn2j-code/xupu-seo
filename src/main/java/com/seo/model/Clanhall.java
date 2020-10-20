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
 * 宗祠
 * 
 * @author Lenovo
 *
 */
public class Clanhall extends Model<Clanhall> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public final static Clanhall dao = new Clanhall();
	
	
	public Page<Clanhall> findAllClanhall(Clanhall clanhall,int pageNumber,int pageSize){
		String sql="select *  " ; 
		String where=" from s_clan_hall where IFNULL(del_flg,0)=0 ";
		//查询条件
		if(StringUtils.isNotNullOrEmptyStr(clanhall.getStr("name"))) {
			where+=" and (name like '%"+clanhall.getStr("name")+"%' or name_of_a_hall like '%"+clanhall.getStr("name")+"%'  or address like '%"+clanhall.getStr("name")+"%' or fulladdress like '%"+clanhall.getStr("name")+"%')";
		}
		//审核状态
		if(StringUtils.isNotNullOrEmptyStr(clanhall.getStr("review"))) {
			where+=" and review = "+Integer.valueOf(clanhall.getStr("review"));
		}
		
		where+=" order by update_time desc ";
		 Page<Clanhall> page = dao.paginate(pageNumber, pageSize, sql,where);
		 return page;
	}

	
	/**
	 * 通过ID查询数据
	 * @param id
	 * @return
	 */
	public Clanhall findClanhallById(String id) {
		//IFNULL(del_flg,0)=0 and
		String sql="select * from s_clan_hall where id=?";
		Clanhall clanhall = dao.findFirst(sql, id);
		int cid=clanhall.get("id");
		List<Images> images=Images.dao.findByClanhallId(cid);
		clanhall.put("images",images);
		return clanhall;
	}
	
	
	
	/**
	 * 保存、编辑返回ID
	 * @param entry
	 * @return
	 */
	public boolean edit(Clanhall clanhall,List<Images> list) {
		if (!StringUtils.isNotNullOrEmptyStr(clanhall.getStr("id"))) {
			clanhall.remove("id");
			clanhall.save();
			String id=clanhall.getStr("id");
			Images.dao.saveBatch(getImages(list,id), id);
			return true;

		} else {
			Clanhall qclanhall = findClanhallById(clanhall.getStr("id"));
			if (qclanhall != null) {
				clanhall.update();
				String id= clanhall.getStr("id");
				Images.dao.saveBatch(getImages(list,id), id);
				return true;
			} else {
				clanhall.save();
				String id= clanhall.getStr("id");
				Images.dao.saveBatch(getImages(list,id), id);
				return true;
			}
		}
	}
	
	
	public String create(Clanhall clanhall) {
		clanhall.save();
		return clanhall.getStr("id");
	}
	
	//绑定关系
	private List<Images> getImages(List<Images> list,String id){
		List<Images> ilist=new ArrayList<>();
		for (Images images : list) {
			images.set("clan_id", id);
			ilist.add(images);
		}
		return ilist;
	}
	
	/**
	 * 通过ID批量删除模版数据
	 * @param ids
	 * @return
	 */
	public boolean deleteBatch(String [] ids) {
		if(ids.length>0) {
			String sql = "select * from s_clan_hall where id in("+ids[0]+")";
			List<Record> list=Db.find(sql);
			if(!list.isEmpty()) {
				List<Record> listmodel=new ArrayList<Record>();
				for (Iterator<Record> iterator = list.iterator(); iterator.hasNext();) {
					Record record = (Record) iterator.next();
					record.set("del_flg", 1);
					listmodel.add(record);
				}

				int [] info=Db.batchUpdate("s_clan_hall", listmodel, 15);
				if(info.length>0) {
					return true;
				}
			}
		}
		return false;
	}
	
	
	/**
	 * 通过ID批量推荐
	 * @param ids
	 * @return
	 */
	public boolean recommend(String [] ids) {
		if(ids.length>0) {
			String sql = "select * from s_clan_hall where id in("+ids[0]+")";
			List<Record> list=Db.find(sql);
			if(!list.isEmpty()) {
				List<Record> listmodel=new ArrayList<Record>();
				for (Iterator<Record> iterator = list.iterator(); iterator.hasNext();) {
					Record record = (Record) iterator.next();
					record.set("recommend", 1);
					listmodel.add(record);
				}

				int [] info=Db.batchUpdate("s_clan_hall", listmodel, 15);
				if(info.length>0) {
					return true;
				}
			}
		}
		return false;
	}

	
	/**
	 * 通过ID批量取消推荐
	 * @param ids
	 * @return
	 */
	public boolean noRecommend(String [] ids) {
		if(ids.length>0) {
			String sql = "select * from s_clan_hall where id in("+ids[0]+")";
			List<Record> list=Db.find(sql);
			if(!list.isEmpty()) {
				List<Record> listmodel=new ArrayList<Record>();
				for (Iterator<Record> iterator = list.iterator(); iterator.hasNext();) {
					Record record = (Record) iterator.next();
					record.set("recommend", 0);
					listmodel.add(record);
				}
				int [] info=Db.batchUpdate("s_clan_hall", listmodel, 15);
				if(info.length>0) {
					return true;
				}
			}
		}
		return false;
	}
	
	
}
