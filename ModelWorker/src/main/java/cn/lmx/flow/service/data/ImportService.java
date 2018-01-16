package cn.lmx.flow.service.data;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import cn.lmx.flow.bean.data.ImportBean;
import cn.lmx.flow.bean.data.ImportGroupBean;
import cn.lmx.flow.dao.business.BusinessSQLDao;
import cn.lmx.flow.dao.flow.SQLDao;
import cn.lmx.flow.dao.system.DataLogDao;
import cn.lmx.flow.dao.view.ImportGroupDao;
import cn.lmx.flow.dao.view.ImportPublishDao;
import cn.lmx.flow.entity.view.ImportGroup;
import cn.lmx.flow.entity.view.ImportPublish;
import cn.lmx.flow.utils.FileImportUtils;

@Repository("ImportService")
public class ImportService {
	//导入组
	@Resource(name="ImportGroupDao")
	private ImportGroupDao importGroupDao;
	//导入
	@Resource(name="ImportPublishDao")
	private ImportPublishDao importPublishDao;
	//日志
	@Resource(name="DataLogDao")
	private DataLogDao dataLogDao;
	//SQL
	@Resource(name="SQLDao")
	private SQLDao sqlDao;
	//业务数据库SQL
	@Resource(name="BusinessSQLDao")
	private BusinessSQLDao businessSQLDao;
	/**
	 * 取得导入一栏
	 * @return
	 * @throws Exception
	 */
	@Transactional(rollbackFor=Exception.class)
	public List<ImportGroupBean> getList() throws Exception {
		try {
			List<ImportGroupBean> rstList = new ArrayList<ImportGroupBean>();
			//取得导入组
			List<?> importGroupList = importGroupDao.list("N");
			if (importGroupList == null || importGroupList.size() <= 0) {
				return rstList;
			}
			for (int i = 0; i < importGroupList.size(); i++) {
				ImportGroup group = (ImportGroup)importGroupList.get(i);
				if (group == null) {
					continue;
				}
				List<?> publishList = importPublishDao.listByGroup(group.getNo());
				if (publishList == null || publishList.size() <= 0) {
					continue;
				}
				ImportGroupBean groupBean = new ImportGroupBean();
				BeanUtils.copyProperties(group, groupBean);
				List<ImportBean> importList = new ArrayList<ImportBean>();
				for (int j = 0; j < publishList.size(); j++) {
					ImportPublish importPublish = (ImportPublish)publishList.get(j);
					ImportBean importBean = new ImportBean();
					BeanUtils.copyProperties(importPublish, importBean);
					importList.add(importBean);
				}
				groupBean.setImportList(importList);
				rstList.add(groupBean);
			}
			return rstList;
		} catch (Exception e) {
			if (e.getCause() != null) {
				throw new Exception(e.getCause().getMessage());
			}
			throw e;
		}
	}
	/**
	 * 数据导入
	 * @param file
	 * @param no
	 * @throws Exception
	 */
	@Transactional(rollbackFor=Exception.class)
	public void importData(String systemUser, String fileName, InputStream in, String no) throws Exception {
		try {
			FileImportUtils fileImportUtils = new FileImportUtils(importPublishDao, dataLogDao, sqlDao, businessSQLDao);
			fileImportUtils.importData(systemUser, fileName, in, no);
		} catch (Exception e) {
			if (e.getCause() != null) {
				throw new Exception(e.getCause().getMessage());
			}
			throw e;
		}
	}
}
