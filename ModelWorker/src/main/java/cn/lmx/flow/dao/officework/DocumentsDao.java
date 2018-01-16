package cn.lmx.flow.dao.officework;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Query;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;

import cn.lmx.flow.bean.officework.PointQueueBean;
import cn.lmx.flow.dao.flow.BaseDao;
import cn.lmx.flow.entity.officework.Documents;
/**
 * 公文信息
 * @author yujx
 *
 */
@Repository("DocumentsDao")
public class DocumentsDao extends BaseDao {
	/**
	 * 取得所有数据
	 * @return
	 * @throws Exception
	 */
	public List<?> listAll() throws Exception {
		Query q = getSession().createQuery("from Documents");
		return q.list();
	}
	/**
	 * 根据ID取得数据
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public Documents getById(String id) throws Exception {
		return (Documents)getSession().get(Documents.class, id);
	}
	/**
	 * 新增数据
	 * @param data
	 * @throws Exception
	 */
	public void add(Documents data) throws Exception {
		getSession().save(data);
	}
	/**
	 * 修改数据
	 * @param data
	 * @throws Exception
	 */
	public void edit(Documents data) throws Exception {
		getSession().saveOrUpdate(data);
	}
	/**
	 * 删除数据
	 * @param data
	 * @throws Exception
	 */
	public void delete(Documents data) throws Exception {
		getSession().delete(data);
	}
	/**
	 * 根据工会级别取得积分排名
	 * @param level
	 * @return
	 * @throws Exception
	 */
	public List<?> point(String level) throws Exception {
		/*String sql = new StringBuffer("")
				.append("select\n")
				.append("	D.Code AS tradeUnionCode,\n")
				.append("	D.Name as tradeUnionName,\n")
				.append("	ISNULL(E.Point, 0) as point\n")
				.append("from\n")
				.append("	TradeUnionInfo as D\n")
				.append("	left join\n")
				.append("		(select\n")
				.append("			C.Code,\n")
				.append("			Sum(C.Point) Point\n")
				.append("		from\n")
				.append("			(select\n")
				.append("				B.Code,\n")
				.append("				dbo.LP_DOCUMENT_POINT(A.ParentId) Point\n")
				.append("			from\n")
				.append("				SubmitHistory AS A\n")
				.append("				left join\n")
				.append("					TradeUnionInfo as B\n")
				.append("				on\n")
				.append("					A.SubmitTradeCode = B.Code\n")
				.append("			where\n")
				.append("				B.TradeLevel = :tradeLevel\n")
				.append("			) C\n")
				.append("		group by\n")
				.append("			C.Code\n")
				.append("		) E\n")				
				.append("	on\n")
				.append("		D.Code = E.Code\n")
				.append("where\n")
				.append("	D.TradeLevel=:tradeLevel\n")
				.append("order by\n")
				.append("	E.Point desc\n").toString();*/
		String sql = new StringBuffer("")
				.append("SELECT\n")
				.append("	A.Code AS tradeUnionCode,\n")
				.append("	A.Name AS tradeUnionName,\n")
				.append("	C.Code as virtualUnionCode,\n")
				.append("	C.Name as virtualUnionName,\n")
				.append("	C.ShowVirtual showVirtual,\n")
				.append("	ifnull(A.Point,0)-ifnull(B.DeductPoint, 0) AS point\n")
				.append("FROM\n")
				.append("	(SELECT\n")
				.append("		A.`Code`,\n")
				.append("		A.`Name`,\n")
				.append("		A.VirtualParentCode,\n")
				.append("		A.ShowOrder,\n")
				.append("		sum(IFNULL(B.Point, 0)) Point\n")
				.append("	FROM\n")
				.append("		(SELECT\n")
				.append("			A.Code,\n")
				.append("			A.`Name`,\n")
				.append("			A.VirtualParentCode,\n")
				.append("			A.ShowOrder,\n")
				.append("			B.ParentId\n")
				.append("		FROM\n")
				.append("			tradeunioninfo AS A\n")
				.append("			LEFT JOIN\n")
				.append("				(SELECT\n")
				.append("					submithistory.ParentId,\n")
				.append("					submithistory.SubmitTradeCode\n")
				.append("				FROM\n")
				.append("					submithistory\n")
				.append("				WHERE\n")
				.append("					submithistory.Status='Y'\n")
				.append("				) AS B\n")
				.append("			ON\n")
				.append("				A.`Code` = B.SubmitTradeCode\n")
				.append("		WHERE\n")
				.append("			A.TradeLevel=:tradeLevel and\n")
				.append("			A.TradeType='0'\n")
				.append("		) AS A\n")
				.append("		LEFT JOIN\n")
				.append("			(SELECT\n")
				.append("				A.ParentId,\n")
				.append("				sum(ifnull(B.Point, 0)) Point\n")
				.append("			FROM\n")
				.append("				(SELECT\n")
				.append("					A.ParentId,\n")
				.append("					B.InfoType,\n")
				.append("					A.MagazineCode,\n")
				.append("					IFNULL(A.AcceptType, '') AcceptType\n")
				.append("				FROM\n")
				.append("					documentaccepts as A\n")
				.append("					left JOIN\n")
				.append("						documents as B\n")
				.append("					ON\n")
				.append("						A.ParentId = B.ID\n")
				.append("				) AS A\n")
				.append("				LEFT JOIN\n")
				.append("					pointmanager as B\n")
				.append("				ON\n")
				.append("					A.InfoType = B.InfoType AND\n")
				.append("					A.MagazineCode = B.MagazineCode AND\n")
				.append("					ifnull(A.AcceptType, '') = ifnull(B.AcceptType, '')\n")
				.append("				GROUP BY\n")
				.append("					A.ParentId\n")
				.append("			) AS B\n")
				.append("		ON\n")
				.append("			ifnull(A.ParentId, '') = ifnull(B.ParentId, '')\n")
				.append("	GROUP BY\n")
				.append("		A.Code,\n")
				.append("		A.`Name`,\n")
				.append("		A.VirtualParentCode\n")
				.append("	) AS A\n")
				.append("	left join\n")
				.append("		(SELECT\n")
				.append("			TradeCode,\n")
				.append("			SUM(DeductPoint) DeductPoint\n")
				.append("		FROM\n")
				.append("			deductinfo\n")
				.append("		WHERE\n")
				.append("			DeductYear=YEAR(now())\n")
				.append("		GROUP BY\n")
				.append("			TradeCode\n")
				.append("		) as B\n")
				.append("	on\n")
				.append("		A.Code = B.TradeCode\n")
				.append("	left join\n")
				.append("		tradeunioninfo as C\n")
				.append("	on\n")
				.append("		A.VirtualParentCode = C.Code\n")
				.append("WHERE\n")
				.append("	ifnull(A.Point,0)-ifnull(B.DeductPoint, 0) > 0\n")
				.append("ORDER BY\n")
				.append("	ifnull(A.Point,0)-ifnull(B.DeductPoint, 0) DESC,\n")
				.append("	A.ShowOrder\n").toString();
		Query q = getSession().createSQLQuery(sql);
		q.setParameter("tradeLevel", level);
		q.setResultTransformer(Transformers.aliasToBean(PointQueueBean.class));
		return q.list();
	}
	/**
	 * 取得文档列表一栏
	 * @param systemUser
	 * @param tradeCode
	 * @param infoType
	 * @param magazineCode
	 * @param startDate
	 * @param endDate
	 * @return
	 * @throws Exception
	 */
	public List<?> listDoc(String systemUser, String tradeCode, String infoType, String magazineCode, String startDate, String endDate) throws Exception {
		StringBuffer sql = new StringBuffer("")
						.append("SELECT\n")
						.append("	A.ID AS id,\n")
						.append("	A.Title AS title,\n")
						.append("	A.TradeUnionCode AS tradeUnionCode,\n")
						.append("	B.NAME AS tradeUnionName,\n")
						.append("	A.InfoType AS infoType,\n")
						.append("	D.Name AS infoTypeName,\n")
						.append("	A.SubmitDate AS submitDate,\n")
						.append("	A.Description as description,\n")
						.append("	A.SubmitStatus as submitStatus,\n")
						.append("	A.AcceptStatus as acceptStatus,\n")
						.append("	C.AcceptDate as acceptDate\n")
						.append("FROM\n")
						.append("	(SELECT\n")
						.append("		A.ID,\n")
						.append("		A.TITLE,\n")
						.append("		A.TradeUnionCode,\n")
						.append("		A.TradeUnionName,\n")
						.append("		A.InfoType,\n")
						.append("		left(A.CreateTime, 8) AS SubmitDate,\n")
						.append("		A.Description,\n")
						.append("		A.SubmitStatus,\n")
						.append("		A.AcceptStatus\n")
						.append("	FROM\n")
						.append("		documents AS A\n")
						.append("		LEFT JOIN\n")
						.append("			TradeUnionUser AS B\n")
						.append("		ON\n")
						.append("			A.TradeUnionCode = B.TradeUnionCode\n")
						.append("	WHERE\n")
						.append("		A.SubmitStatus = '0' and\n")
						.append("		B.Code = :systemUser\n")
						.append("	UNION ALL\n")
						.append("	SELECT\n")
						.append("		A.ID,\n")
						.append("		A.TITLE,\n")
						.append("		B.SubmitTradeCode AS TradeUnionCode,\n")
						.append("		A.TradeUnionName,\n")
						.append("		A.InfoType,\n")
						.append("		B.SubmitDate,\n")
						.append("		A.Description,\n")
						.append("		A.SubmitStatus,\n")
						.append("		A.AcceptStatus\n")
						.append("	FROM\n")
						.append("		documents AS A\n")
						.append("		LEFT JOIN\n")
						.append("			(SELECT\n")
						.append("				DISTINCT\n")
						.append("				submithistory.ParentId,\n")
						.append("				submithistory.SubmitTradeCode,\n")
						.append("				submithistory.SubmitDate\n")
						.append("			FROM\n")
						.append("				submithistory\n")
						.append("				LEFT JOIN\n")
						.append("					TradeUnionUser\n")
						.append("				ON\n")
						.append("					submithistory.RecvTradeCode = TradeUnionUser.TradeUnionCode\n")
						.append("			WHERE\n")
						.append("				submithistory.Status='Y' and\n")
						.append("				TradeUnionUser.Code=:systemUser\n")
						.append("			) AS B\n")
						.append("		ON\n")
						.append("			A.ID = B.ParentId\n")
						.append("	WHERE\n")
						.append("			A.SubmitStatus='1' and\n")
						.append("			ifnull(B.ParentId, '') <> ''\n")
						.append("	UNION ALL\n")
						.append("	SELECT\n")
						.append("		A.ID,\n")
						.append("		A.TITLE,\n")
						.append("		B.SubmitTradeCode AS TradeUnionCode,\n")
						.append("		A.TradeUnionName,\n")
						.append("		A.InfoType,\n")
						.append("		B.SubmitDate,\n")
						.append("		A.Description,\n")
						.append("		A.SubmitStatus,\n")
						.append("		A.AcceptStatus\n")
						.append("	FROM\n")
						.append("		documents AS A\n")
						.append("		LEFT JOIN\n")
						.append("			(SELECT\n")
						.append("				DISTINCT\n")
						.append("				submithistory.ParentId,\n")
						.append("				submithistory.SubmitTradeCode,\n")
						.append("				submithistory.SubmitDate\n")
						.append("			FROM\n")
						.append("				submithistory\n")
						.append("				LEFT JOIN\n")
						.append("					TradeUnionUser\n")
						.append("				ON\n")
						.append("					submithistory.SubmitTradeCode = TradeUnionUser.TradeUnionCode\n")
						.append("			WHERE\n")
						.append("				submithistory.Status='Y' and\n")
						.append("				TradeUnionUser.Code = :systemUser\n")
						.append("			) AS B\n")
						.append("		ON\n")
						.append("			A.ID = B.ParentId\n")
						.append("	WHERE\n")
						.append("			A.SubmitStatus='1' and\n")
						.append("			ifnull(B.ParentId, '') <> ''\n")
						.append("	) as A\n")
						.append("	left JOIN\n")
						.append("		tradeunioninfo as B\n")
						.append("	ON\n")
						.append("		A.TradeUnionCode = B.`Code`\n")
						.append("	LEFT JOIN\n")
						.append("		DocumentAccepts as C\n")
						.append("	ON\n")
						.append("		A.ID = C.ParentId\n")
						.append("	left JOIN\n")
						.append("		InfomationType as D\n")
						.append("	ON\n")
						.append("		A.InfoType = D.`Code`\n");
		StringBuffer sb = new StringBuffer("");
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("systemUser", systemUser);
		if (tradeCode != null && !"".equals(tradeCode)) {
			if (sb.length() > 0) {
				sb.append(" and\n");
			}
			sb.append("A.TradeUnionCode=:tradeCode");
			map.put("tradeCode", tradeCode);
		}
		if (infoType != null && !"".equals(infoType)) {
			if (sb.length() > 0) {
				sb.append(" and\n");
			}
			sb.append("A.InfoType=:infoType");
			map.put("infoType", infoType);
		}
		if (magazineCode != null && !"".equals(magazineCode)) {
			if (sb.length() > 0) {
				sb.append(" and\n");
			}
			sb.append("C.MagazineCode=:magazineCode");
			map.put("magazineCode", magazineCode);
		}
		if (startDate != null && !"".equals(startDate)) {
			if (sb.length() > 0) {
				sb.append(" and\n");
			}
			sb.append("A.SubmitDate>=:startDate");
			map.put("startDate", startDate);
		}
		if (endDate != null && !"".equals(endDate)) {
			if (sb.length() > 0) {
				sb.append(" and\n");
			}
			sb.append("A.SubmitDate<=:endDate");
			map.put("endDate", endDate);
		}
		if (sb.length() > 0) {
			sql.append(" WHERE\n").append(sb.toString());
		}
		Query q = getSession().createSQLQuery(sql.toString());
		q.setProperties(map);
		q.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
		return q.list();
	}
}
