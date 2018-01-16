package cn.lmx.flow.dao.officework;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;

import cn.lmx.flow.bean.officework.DocumentAcceptsBean;
import cn.lmx.flow.dao.flow.BaseDao;
import cn.lmx.flow.entity.officework.DocumentAccepts;
/**
 * 公文采用状况
 * @author yujx
 *
 */
@Repository("DocumentAcceptsDao")
public class DocumentAcceptsDao extends BaseDao {
	/**
	 * 取得所有数据
	 * @return
	 * @throws Exception
	 */
	public List<?> listAll() throws Exception {
		Query q = getSession().createQuery("from DocumentAccepts");
		return q.list();
	}
	/**
	 * 根据ID取得数据
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public DocumentAccepts getById(String id) throws Exception {
		return (DocumentAccepts)getSession().get(DocumentAccepts.class, id);
	}
	/**
	 * 新增数据
	 * @param data
	 * @throws Exception
	 */
	public void add(DocumentAccepts data) throws Exception {
		getSession().save(data);
	}
	/**
	 * 修改数据
	 * @param data
	 * @throws Exception
	 */
	public void edit(DocumentAccepts data) throws Exception {
		getSession().saveOrUpdate(data);
	}
	/**
	 * 删除数据
	 * @param data
	 * @throws Exception
	 */
	public void delete(DocumentAccepts data) throws Exception {
		getSession().delete(data);
	}
	/**
	 * 根据公文ID取得采用状况
	 * @param docId
	 * @return
	 * @throws Exception
	 */
	public List<?> listByDocId(String docId) throws Exception {
		Query q = getSession().createQuery("from DocumentAccepts where parentId=:docId order by levelCode,magazineCode");
		q.setParameter("docId", docId);
		return q.list();
	}
	/**
	 * 根据公文ID取得采用信息
	 * @param id
	 * @return
	 */
	public List<?> getListByDocId(String id) {
		/*StringBuffer sql = new StringBuffer("")
				.append(" select  t1.Code LevelCode, t1.Name LevelName, t2.Code MagazineCode, t2.Name MagazineName, t3.Code AcceptType, t3.Name AcceptTypeName, t5.ParentId ParentId ")
				.append(" from  AcceptLevel t1 ")
				.append(" LEFT JOIN Magazines t2 ON t1.Code=t2.LevelCode ")
				.append(" LEFT JOIN AcceptType t3 on t2.Code=t3.MagazineCode ")
				.append(" LEFT JOIN PointManager t4 on t3.Code=t4.AcceptType and t3.MagazineCode=t4.MagazineCode ")
				.append(" LEFT JOIN DocumentAccepts t5 ON t2.LevelCode=t5.LevelCode and t2.Code=t5.MagazineCode and isnull(t3.Code, '')=isnull(t5.AcceptType, '') and t5.ParentId=:id  ");*/
		/*StringBuffer sql = new StringBuffer("")
				.append(" select  t1.Code LevelCode, t1.Name LevelName, t2.Code MagazineCode, t2.Name MagazineName, t3.Code AcceptType, t3.Name AcceptTypeName, t5.ParentId ParentId ")
				.append(" from  AcceptLevel t1 ")
				.append(" LEFT JOIN Magazines t2 ON t1.Code=t2.LevelCode ")
				.append(" LEFT JOIN AcceptType t3 on t2.Code=t3.MagazineCode ")
				.append(" LEFT JOIN PointManager t4 on t3.Code=t4.AcceptType and t3.MagazineCode=t4.MagazineCode ")
				.append(" LEFT JOIN DocumentAccepts t5 ON t2.LevelCode=t5.LevelCode and t2.Code=t5.MagazineCode and ifnull(t3.Code, '')=ifnull(t5.AcceptType, '') and t5.ParentId=:id  ");*/
		StringBuffer sql = new StringBuffer("")
				.append("SELECT\n")
				.append("	C.LevelCode as levelCode,\n")
				.append("	IFNULL(E.`Name`,'') levelName,\n")
				.append("	C.MagazineCode as magazineCode,\n")
				.append("	C.MagazineName as magazineName,\n")
				.append("	C.AcceptTypeCode acceptType,\n")
				.append("	C.AcceptTypeName acceptTypeName,\n")
				.append("	C.Periods as periods,\n")
				.append("	CASE WHEN IFNULL(D.MagazineCode,'') = '' then 'N' else 'Y' end status,\n")
				.append("	D.MagazineYear as magazineYear,\n")
				.append("	D.Period as period\n")
				.append("FROM\n")
				.append("	(SELECT\n")
				.append("		A.`Code` as MagazineCode,\n")
				.append("		A.`Name` As MagazineName,\n")
				.append("		A.LevelCode as LevelCode,\n")
				.append("		A.Periods,\n")
				.append("		B.`Code` as AcceptTypeCode,\n")
				.append("		B.`Name` as AcceptTypeName\n")
				.append("	FROM\n")
				.append("		magazines AS A\n")
				.append("		LEFT JOIN\n")
				.append("			accepttype as B\n")
				.append("		ON\n")
				.append("			A.`Code` = B.MagazineCode\n")
				.append("	) AS C\n")
				.append("	LEFT JOIN\n")
				.append("		(SELECT\n")
				.append("			DISTINCT MagazineCode,\n")
				.append("			AcceptType,\n")
				.append("			MagazineYear,\n")
				.append("			Period\n")
				.append("		FROM\n")
				.append("			documentaccepts\n")
				.append("		where\n")
				.append("			ParentId=:id\n")
				.append("		) AS D\n")
				.append("	ON\n")
				.append("		C.MagazineCode = D.MagazineCode and\n")
				.append("		IFNULL(C.AcceptTypeCode,'') = IFNULL(D.AcceptType,'')\n")
				.append("	LEFT JOIN\n")
				.append("		acceptlevel as E\n")
				.append("	ON\n")
				.append("		C.LevelCode = E.`Code`\n")
				.append("ORDER BY\n")
				.append("	C.LevelCode,\n")
				.append("	C.MagazineCode,\n")
				.append("	C.AcceptTypeCode\n");
		Query q = getSession().createSQLQuery(sql.toString());
		q.setParameter("id", id);
		q.setResultTransformer(Transformers.aliasToBean(DocumentAcceptsBean.class));
		return q.list();		
	}
	
	/**
	 * 根据公文ID删除公文采用信息
	 * @param id
	 */
	public void deleteById(String id) {
		Query q = getSession().createQuery("delete from DocumentAccepts where parentId=:parentId");
		q.setParameter("parentId", id);
		q.executeUpdate();
	}

}
