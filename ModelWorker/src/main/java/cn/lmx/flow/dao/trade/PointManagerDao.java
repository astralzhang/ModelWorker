package cn.lmx.flow.dao.trade;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Query;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;

import cn.lmx.flow.bean.officework.DocumentAcceptsBean;
import cn.lmx.flow.dao.flow.BaseDao;
import cn.lmx.flow.entity.trade.PointManager;
@Repository("PointManagerDao")
public class PointManagerDao extends BaseDao {
	/**
	 * 取得所有数据
	 * @return
	 * @throws Exception
	 */
	public List<?> listAll() throws Exception {
		Query q = getSession().createQuery("from PointManager");
		return q.list();
	}
	/**
	 * 根据ID取得数据
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public PointManager getById(String id) throws Exception {
		return (PointManager)getSession().get(PointManager.class, id);
	}
	/**
	 * 新增数据
	 * @param acceptLevel
	 * @throws Exception
	 */
	public void add(PointManager data) throws Exception {
		getSession().save(data);
	}
	/**
	 * 修改数据
	 * @param acceptLevel
	 * @throws Exception
	 */
	public void edit(PointManager data) throws Exception {
		getSession().saveOrUpdate(data);
	}
	/**
	 * 删除数据
	 * @param acceptLevel
	 * @throws Exception
	 */
	public void delete(PointManager data) throws Exception {
		getSession().delete(data);
	}
	public List<?> pointList(Map<String, Object> paraMap) {
		StringBuffer where = new StringBuffer();
		String infoType=paraMap.get("search_info").toString();
		String levelType=paraMap.get("search_level").toString();
		String magazinesType=paraMap.get("search_magazines").toString();
		String acceptType=paraMap.get("search_accept").toString();
		if (infoType != null && !"".equals(infoType)) {
			if (where.length() > 0) {
				where.append(" and ");
			}
			where.append(" t1.infoCode=:infoType ");
		}
		if (levelType != null && !"".equals(levelType)) {
			if (where.length() > 0) {
				where.append(" and ");
			}
			where.append(" t2.LevelCode=:levelType ");
		}
		if (magazinesType != null && !"".equals(magazinesType)) {
			if (where.length() > 0) {
				where.append(" and ");
			}
			where.append(" t2.MagazineCode=:magazinesType ");
		}
		if (acceptType != null && !"".equals(acceptType)) {
			if (where.length() > 0) {
				where.append(" and ");
			}
			where.append(" t2.AcceptType=:acceptType ");
		}

		/*StringBuffer sql = new StringBuffer("")
				.append(" select  t3.ID id,t1.infoCode infoType, t1.infoName infoTypeName, t2.LevelCode levelCode, t2.LevelName levelName, ")
				.append(" t2.AcceptType acceptType, t2.AcceptTypeName acceptTypeName, t2.MagazineCode magazineCode, t2.MagazineName magazineName, t3.Point point ")
				.append(" from  ( select  Code infoCode, Name infoName, '1' infoId from InfomationType ) t1 ")
				.append(" LEFT JOIN (SELECT t1.Code LevelCode, t1.Name LevelName, t2.Code MagazineCode, t2.Name MagazineName, ")
				.append(" t3.Code AcceptType, t3.Name AcceptTypeName, '1' checkId FROM AcceptLevel t1 LEFT JOIN Magazines t2 ON t1.Code = t2.LevelCode ")
				.append(" LEFT JOIN AcceptType t3 ON t2.Code = t3.MagazineCode ) t2 ON t1.infoId=t2.checkId ")
				.append(" LEFT JOIN PointManager t3 ON ifnull(t1.infoCode,'')=ifnull(t3.InfoType,'') and ifnull(t2.LevelCode,'')=ifnull(t3.LeaveType,'') ")
				.append(" and ifnull(t2.AcceptType,'')=ifnull(t3.AcceptType,'') and ifnull(t2.MagazineCode,'')=ifnull(t3.MagazineCode,'')");*/
		StringBuffer sql = new StringBuffer("")
				.append("SELECT\n")
				.append("	t3.ID id,\n")
				.append("	t1.infoCode infoType,\n")
				.append("	t1.infoName infoTypeName,\n")
				.append("	t2.LevelCode levelCode,\n")
				.append("	t2.LevelName levelName,\n")
				.append("	t2.AcceptType acceptType,\n")
				.append("	t2.AcceptTypeName acceptTypeName,\n")
				.append("	t2.MagazineCode magazineCode,\n")
				.append("	t2.MagazineName magazineName,\n")
				.append("	t3.Point point\n")
				.append("FROM\n")
				.append("	(SELECT\n")
				.append("			CODE infoCode,\n")
				.append("			NAME infoName,\n")
				.append("			'1' infoId\n")
				.append("		FROM\n")
				.append("			InfomationType\n")
				.append("	) t1\n")
				.append("	LEFT JOIN\n")
				.append("		(SELECT\n")
				.append("			t1. CODE LevelCode,\n")
				.append("			t1. NAME LevelName,\n")
				.append("			t2. CODE MagazineCode,\n")
				.append("			t2. NAME MagazineName,\n")
				.append("			t3. CODE AcceptType,\n")
				.append("			t3. NAME AcceptTypeName,\n")
				.append("			'1' checkId\n")
				.append("		FROM\n")
				.append("			AcceptLevel t1\n")
				.append("			LEFT JOIN\n")
				.append("				Magazines t2\n")
				.append("			ON\n")
				.append("				t1.CODE = t2.LevelCode\n")
				.append("			LEFT JOIN\n")
				.append("				AcceptType t3\n")
				.append("			ON\n")
				.append("				t2.CODE = t3.MagazineCode\n")
				.append("		) t2 \n")
				.append("	ON\n")
				.append("		t1.infoId = t2.checkId\n")
				.append("	LEFT JOIN\n")
				.append("		PointManager t3\n")
				.append("	ON\n")
				.append("		ifnull(t1.infoCode, '') = ifnull(t3.InfoType, '') AND\n")
				.append("		ifnull(t2.AcceptType, '') = ifnull(t3.AcceptType, '') AND\n")
				.append("		ifnull(t2.MagazineCode, '') = ifnull(t3.MagazineCode, '')\n");
				
		if (where.length() > 0) {
			sql.append("where\n")
				.append(where).append("\n");
		}
		sql.append("")
			.append("ORDER BY\n")
			.append("	t2.LevelCode,\n")
			.append("	t2.MagazineCode,\n")
			.append("	t2.AcceptType,\n")
			.append("	t1.infoCode\n");
		Query q = getSession().createSQLQuery(sql.toString());
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("infoType", infoType);
		map.put("levelType", levelType);
		map.put("magazinesType", magazinesType);
		map.put("acceptType", acceptType);
		q.setProperties(map);
		q.setResultTransformer(Transformers.aliasToBean(DocumentAcceptsBean.class));
		return q.list();
	}
}
