package cn.lmx.flow.service.flow;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import cn.lmx.flow.bean.flow.TableFieldBean;
import cn.lmx.flow.dao.flow.TableFieldDao;
import cn.lmx.flow.entity.flow.TableFields;

@Repository("TableFieldService")
public class TableFieldService {
	@Resource(name="TableFieldDao")
	private TableFieldDao tableFieldDao;
	/**
	 * 根据数据表ID取得对应字段列表
	 * @param tableId
	 * @return
	 * @throws Exception
	 */
	@Transactional(rollbackFor=Exception.class)
	public List<TableFieldBean> getTableFieldList(String tableId) throws Exception {
		try {
			List<?> list = tableFieldDao.getTableFieldList(tableId);
			List<TableFieldBean> rstList = new ArrayList<TableFieldBean>();
			if (list == null || list.size() <= 0) {
				return rstList; 
			}
			for (int i = 0; i < list.size(); i++) {
				TableFields field = (TableFields)list.get(i);
				TableFieldBean bean = new TableFieldBean();
				BeanUtils.copyProperties(field, bean);
				rstList.add(bean);
			}
			return rstList;
		} catch (Exception e) {
			throw e;
		}
	}
}
