package cn.lmx.flow.service.view;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.annotation.Resource;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.google.gson.Gson;

import cn.lmx.flow.bean.view.ReportDesignBean;
import cn.lmx.flow.dao.module.ModuleDao;
import cn.lmx.flow.dao.module.ModuleItemDao;
import cn.lmx.flow.dao.module.PermissionDao;
import cn.lmx.flow.dao.module.PrivilegeDao;
import cn.lmx.flow.dao.system.DataLogDao;
import cn.lmx.flow.dao.view.PublishReportDao;
import cn.lmx.flow.dao.view.ReportDesignDao;
import cn.lmx.flow.dao.view.ReportTemplateDao;
import cn.lmx.flow.entity.module.Module;
import cn.lmx.flow.entity.module.ModuleItems;
import cn.lmx.flow.entity.module.Permission;
import cn.lmx.flow.entity.module.Privilege;
import cn.lmx.flow.entity.system.DataLogs;
import cn.lmx.flow.entity.view.ReportDesign;
import cn.lmx.flow.entity.view.ReportPublish;
import cn.lmx.flow.entity.view.ReportTemplate;

@Repository("ReportDesignService")
public class ReportDesignService {
	//模块
	@Resource(name="ModuleDao")
	private ModuleDao moduleDao;
	//报表设计
	@Resource(name="ReportDesignDao")
	private ReportDesignDao reportDesignDao;
	//报表模板
	@Resource(name="ReportTemplateDao")
	private ReportTemplateDao reportTemplateDao;
	//发布
	@Resource(name="PublishReportDao")
	private PublishReportDao publishReportDao;
	//菜单
	@Resource(name="ModuleItemDao")
	private ModuleItemDao moduleItemDao;
	//权限管理
	@Resource(name="PrivilegeDao")
	private PrivilegeDao privilegeDao;
	//权限
	@Resource(name="PermissionDao")
	private PermissionDao permissionDao;
	//DataLog
	@Resource(name="DataLogDao")
	private DataLogDao dataLogDao;
	/**
	 * 取得报表一栏
	 * @param no
	 * @param name
	 * @return
	 * @throws Exception
	 */
	@Transactional(rollbackFor=Exception.class)
	public Map<String, Object> list(String no, String name) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			List<?> moduleList = moduleDao.list();
			List<?> reportList = reportDesignDao.list(no, name);
			map.put("module", moduleList);
			map.put("report", reportList);
			return map;
		} catch (Exception e) {
			if (e.getCause() != null) {
				throw new Exception(e.getCause().getMessage());
			}
			throw e;
		}
	}
	/**
	 * 取得模块一栏
	 * @return
	 * @throws Exception
	 */
	@Transactional(rollbackFor=Exception.class)
	public Map<String, Object> getModule() throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			List<?> moduleList = moduleDao.list();
			map.put("module", moduleList);
			return map;
		} catch (Exception e) {
			if (e.getCause() != null) {
				throw new Exception(e.getCause().getMessage());
			}
			throw e;
		}
	}
	/**
	 * 编辑报表
	 * @param id
	 * @return
	 * @throws Exception
	 */
	@Transactional(rollbackFor=Exception.class)
	public Map<String, Object> editReport(String id) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			ReportDesign design = reportDesignDao.getById(id);
			if (design != null) {
				map.put("report", design);
				if (design.getViewScript() == null || "".equals(design.getViewScript())) {
					design.setViewScript("{}");
				}
				List<?> moduleList = moduleDao.list();
				map.put("module", moduleList);
			} else {
				throw new Exception("报表设定不存在，请确认后重试！");
			}
			return map;
		} catch (Exception e) {
			if (e.getCause() != null) {
				throw new Exception(e.getCause().getMessage());
			}
			throw e;
		}
	}
	/**
	 * 保存报表
	 * @param systemUser
	 * @param bean
	 * @throws Exception
	 */
	@Transactional(rollbackFor=Exception.class)
	public void saveReport(String systemUser, ReportDesignBean bean) throws Exception {
		try {
			Calendar c = Calendar.getInstance();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");
			Gson gson = new Gson();
			if (bean.getId() == null || "".equals(bean.getId())) {
				//新增
				ReportDesign design = new ReportDesign();
				BeanUtils.copyProperties(bean, design);
				design.setId(UUID.randomUUID().toString());
				design.setVersion("00001");
				design.setStatus("0");
				design.setCreateUser(systemUser);
				design.setCreateTime(sdf.format(c.getTime()));
				reportDesignDao.add(design);
				DataLogs dataLog = new DataLogs();
				dataLog.setId(UUID.randomUUID().toString());
				dataLog.setType("Add");
				dataLog.setProcessor(systemUser);
				dataLog.setDataTable("ReportDesign");
				dataLog.setAfterData(gson.toJson(design));
				dataLog.setCreateUser(systemUser);
				dataLog.setCreateTime(sdf.format(c.getTime()));
				dataLogDao.add(dataLog);
			} else {
				//修改
				DataLogs dataLog = new DataLogs();
				dataLog.setId(UUID.randomUUID().toString());
				dataLog.setType("Update");
				dataLog.setProcessor(systemUser);
				dataLog.setDataTable("ReportDesign");
				ReportDesign design = reportDesignDao.getById(bean.getId());
				dataLog.setBeforeData(gson.toJson(design));
				String version = design.getVersion();
				version = new StringBuffer("")
								.append("0000")
								.append(Integer.parseInt(version) + 1).toString();
				version = version.substring(version.length() - 5);
				BeanUtils.copyProperties(bean, design);
				design.setVersion(version);
				design.setStatus("0");
				design.setUpdateUser(systemUser);
				design.setUpdateTime(sdf.format(c.getTime()));
				reportDesignDao.edit(design);
				dataLog.setAfterData(gson.toJson(design));
				dataLog.setCreateUser(systemUser);
				dataLog.setCreateTime(sdf.format(c.getTime()));
				dataLogDao.add(dataLog);
			}
		} catch (Exception e) {
			if (e.getCause() != null) {
				throw new Exception(e.getCause().getMessage());
			}
			throw e;
		}
	}
	/**
	 * 发布报表
	 * @param id
	 * @throws Exception
	 */
	@Transactional(rollbackFor=Exception.class)
	public void publishReport(String systemUser, String id) throws Exception {
		try {
			ReportDesign design = reportDesignDao.getById(id);
			if ("1".equals(design.getStatus())) {
				throw new Exception("已是最新发布版本，无需再次发布！");
			}
			Calendar c = Calendar.getInstance();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");
			ReportPublish report = publishReportDao.getById(id);
			String version = "00000";
			if (report == null) {
				//新增
				report = new ReportPublish();
				version = "00001";
				BeanUtils.copyProperties(design, report);
				report.setVersion(version);
				report.setCreateUser(systemUser);
				report.setCreateTime(sdf.format(c.getTime()));
				publishReportDao.add(report);
			} else {
				//修改
				version = report.getVersion();
				version = new StringBuffer("")
						.append("0000")
						.append(Integer.parseInt(version) + 1).toString();
				version = version.substring(version.length() - 5);
				BeanUtils.copyProperties(design, report);
				report.setVersion(version);
				report.setUpdateUser(systemUser);
				report.setUpdateTime(sdf.format(c.getTime()));
				publishReportDao.edit(report);
			}
			
			//生成菜单
			List<?> itemList = moduleItemDao.getModuleItem(report.getModuleNo());
			String showOrder = "00001";
			ModuleItems item = null;
			String menuNo = report.getNo() + "Report";
			if (itemList != null && itemList.size() > 0) {
				for (int i = 0; i < itemList.size(); i++) {
					ModuleItems tempItem = (ModuleItems)itemList.get(i);
					if (menuNo.equals(tempItem.getItemNo())) {
						item = tempItem;
						showOrder = item.getShowOrder();
						break;
					}
				}
			}
			//取得模块
			Module module = moduleDao.getModuleByNo(report.getModuleNo());
			if (item == null) {
				//新增菜单				
				item = new ModuleItems();
				item.setShowOrder(showOrder);
				item.setItemNo(menuNo);
				item.setItemName(report.getName());
				item.setDescription(report.getName());
				item.setMenuFlag("Y");
				item.setModuleNo(design.getModuleNo());
				item.setActionUrl("/report/list/" + menuNo);
				item.setCssStyle(module.getCssStyle());
				item.setCreateUser(systemUser);
				item.setCreateTime(sdf.format(c.getTime()));
				moduleItemDao.add(item);
			} else {
				//修改菜单
				item.setItemName(report.getName());
				item.setDescription(report.getName());
				item.setMenuFlag("Y");
				item.setActionUrl("/report/list/" + menuNo);
				item.setCssStyle(module.getCssStyle());
				item.setModuleNo(design.getModuleNo());
				item.setUpdateUser(systemUser);
				item.setUpdateTime(sdf.format(c.getTime()));
				moduleItemDao.edit(item);
			}
			//权限管理
			privilegeDao.deleteByNo(menuNo);
			Privilege privilege = new Privilege();
			privilege.setModuleNo(report.getModuleNo());
			privilege.setNo(menuNo);
			privilege.setName(report.getName());
			privilege.setActionUrl("/report/list/" + menuNo);
			privilege.setPrivType("M");
			privilege.setCreateUser(systemUser);
			privilege.setCreateTime(sdf.format(c.getTime()));
			privilegeDao.save(privilege);
			//权限
			permissionDao.deletePermission("system", menuNo);
			Permission permission = new Permission();
			permission.setId(UUID.randomUUID().toString());
			permission.setItemNo(menuNo);
			permission.setUserNo("system");
			permission.setUserType("G");
			permission.setCreateUser(systemUser);
			permission.setCreateTime(sdf.format(c.getTime()));
			permissionDao.add(permission);
			/*********************新增检索机能*************************/
			ModuleItems itemSearch = moduleItemDao.getItemByNo(menuNo + "Search");
			if (itemSearch == null) {
				itemSearch = new ModuleItems();
				itemSearch.setItemNo(menuNo + "Search");
				itemSearch.setItemName(report.getName() + "搜索");
				itemSearch.setDescription(report.getName() + "搜索");
				itemSearch.setMenuFlag("N");
				itemSearch.setModuleNo(design.getModuleNo());
				itemSearch.setActionUrl("/report/search/" + menuNo + "Search");
				itemSearch.setCreateUser(systemUser);
				itemSearch.setCreateTime(sdf.format(c.getTime()));
				moduleItemDao.add(itemSearch);
			} else {
				itemSearch.setItemName(report.getName() + "搜索");
				itemSearch.setDescription(report.getName() + "搜索");
				itemSearch.setMenuFlag("N");
				itemSearch.setModuleNo(design.getModuleNo());
				itemSearch.setActionUrl("/report/search/" + menuNo + "Search");
				itemSearch.setUpdateUser(systemUser);
				itemSearch.setUpdateTime(sdf.format(c.getTime()));
				moduleItemDao.edit(itemSearch);
			}
			//权限管理
			privilegeDao.deleteByNo(menuNo + "Search");
			privilege = new Privilege();
			privilege.setModuleNo(report.getModuleNo());
			privilege.setNo(menuNo + "Search");
			privilege.setName(report.getName() + "搜索");
			privilege.setActionUrl("/report/search/" + menuNo + "Search");
			privilege.setPrivType("B");
			privilege.setParentNo(menuNo);
			privilege.setCreateUser(systemUser);
			privilege.setCreateTime(sdf.format(c.getTime()));
			privilegeDao.save(privilege);
			//权限
			permissionDao.deletePermission("system", menuNo + "Search");
			permission = new Permission();
			permission.setId(UUID.randomUUID().toString());
			permission.setItemNo(menuNo + "Search");
			permission.setUserNo("system");
			permission.setUserType("G");
			permission.setCreateUser(systemUser);
			permission.setCreateTime(sdf.format(c.getTime()));
			permissionDao.add(permission);
			/**********************************下载机能*************************************/
			ModuleItems itemDownload = moduleItemDao.getItemByNo(menuNo + "Download");
			if (itemDownload == null) {
				itemDownload = new ModuleItems();
				itemDownload.setItemNo(menuNo + "Download");
				itemDownload.setItemName(report.getName() + "下载");
				itemDownload.setDescription(report.getName() + "下载");
				itemDownload.setMenuFlag("N");
				itemDownload.setModuleNo(design.getModuleNo());
				itemDownload.setActionUrl("/report/download/" + menuNo + "Download");
				itemDownload.setCreateUser(systemUser);
				itemDownload.setCreateTime(sdf.format(c.getTime()));
				moduleItemDao.add(itemDownload);
			} else {
				itemDownload.setItemName(report.getName() + "下载");
				itemDownload.setDescription(report.getName() + "下载");
				itemDownload.setMenuFlag("N");
				itemDownload.setModuleNo(design.getModuleNo());
				itemDownload.setActionUrl("/report/download/" + menuNo + "Download");
				itemDownload.setUpdateUser(systemUser);
				itemDownload.setUpdateTime(sdf.format(c.getTime()));
				moduleItemDao.edit(itemDownload);
			}
			//权限管理
			privilegeDao.deleteByNo(menuNo + "Download");
			privilege = new Privilege();
			privilege.setModuleNo(report.getModuleNo());
			privilege.setNo(menuNo + "Download");
			privilege.setName(report.getName() + "下载");
			privilege.setActionUrl("/report/download/" + menuNo + "Download");
			privilege.setPrivType("B");
			privilege.setParentNo(menuNo);
			privilege.setCreateUser(systemUser);
			privilege.setCreateTime(sdf.format(c.getTime()));
			privilegeDao.save(privilege);
			//权限
			permissionDao.deletePermission("system", menuNo + "Download");
			permission = new Permission();
			permission.setId(UUID.randomUUID().toString());
			permission.setItemNo(menuNo + "Download");
			permission.setUserNo("system");
			permission.setUserType("G");
			permission.setCreateUser(systemUser);
			permission.setCreateTime(sdf.format(c.getTime()));
			permissionDao.add(permission);
			//修改报表为发布状态
			design.setStatus("1");
			design.setUpdateUser(systemUser);
			design.setUpdateTime(sdf.format(c.getTime()));
			reportDesignDao.edit(design);
		} catch (Exception e) {
			if (e.getCause() != null) {
				throw new Exception(e.getCause().getMessage());
			}
			throw e;
		}
	}
	/**
	 * 取得报表模板
	 * @param reportNo
	 * @return
	 * @throws Exception
	 */
	@Transactional(rollbackFor=Exception.class)
	public List<?> getTemplate(String reportNo) throws Exception {
		try {
			List<?> list = reportTemplateDao.listByReportNo(reportNo);
			return list;
		} catch (Exception e) {
			if (e.getCause() != null) {
				throw new Exception(e.getCause().getMessage());
			}
			throw e;
		}
	}
	/**
	 * 上传报表模板
	 * @param systemUser
	 * @param path
	 * @param reportNo
	 * @param importFile
	 * @throws Exception
	 */
	@Transactional(rollbackFor=Exception.class)
	public void upload(String systemUser, String path, String reportNo, MultipartFile file) throws Exception {
		try {
			String fileName = file.getOriginalFilename();
			if (fileName == null || "".equals(fileName)) {
				throw new Exception("上传的文件不正确！");
			}
			if ((!fileName.endsWith(".xls")) && (!fileName.endsWith(".xlsx"))) {
				throw new Exception("请上传Excel文件！");
			}
			List<?> templateList = reportTemplateDao.listByReportNo(reportNo);
			if (templateList != null) {
				for (int i = 0; i < templateList.size(); i++) {
					ReportTemplate template = (ReportTemplate)templateList.get(i);
					if (fileName.equals(template.getFileName())) {
						throw new Exception("文件(" + fileName + ")已存在，请选择其他文件上传！");
					}
				}
			}
			ReportTemplate template = new ReportTemplate();
			template.setId(UUID.randomUUID().toString());
			template.setFileName(fileName);
			template.setReportNo(reportNo);
			template.setTemplate(file.getBytes());
			template.setCreateUser(systemUser);
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");
			Calendar c = Calendar.getInstance();
			template.setCreateTime(sdf.format(c.getTime()));
			reportTemplateDao.add(template);
			//写数据Log
			DataLogs log = new DataLogs();
			log.setId(UUID.randomUUID().toString());
			log.setType("ADD");
			log.setProcessor("报表模板文件上传");
			//template.setTemplate(null);
			Gson gson = new Gson();
			log.setAfterData(gson.toJson(template));
			log.setDataTable("ReportTemplate");
			log.setCreateUser(systemUser);
			log.setCreateTime(sdf.format(c.getTime()));
			dataLogDao.add(log);
		} catch (Exception e) {
			if (e.getCause() != null) {
				throw new Exception(e.getCause().getMessage());
			}
			throw e;
		}
	}
	/**
	 * 删除模板
	 * @param systemUser
	 * @param id
	 * @throws Exception
	 */
	@Transactional(rollbackFor=Exception.class)
	public void deleteTemplate(String systemUser, String id) throws Exception {
		try {
			ReportTemplate template = reportTemplateDao.getById(id);
			if (template == null) {
				throw new Exception("请要删除的文件不存在，请刷新画面后重试！");
			}
			reportTemplateDao.delete(template);
			//写Log
			DataLogs log = new DataLogs();
			log.setId(UUID.randomUUID().toString());
			log.setDataTable("ReportTemplate");
			Gson gson = new Gson();
			template.setTemplate(null);
			log.setBeforeData(gson.toJson(template));
			log.setType("Delete");
			log.setProcessor("报表模板文件删除");
			log.setCreateUser(systemUser);
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");
			Calendar c = Calendar.getInstance();
			log.setCreateTime(sdf.format(c.getTime()));
			dataLogDao.add(log);
		} catch (Exception e) {
			if (e.getCause() != null) {
				throw new Exception(e.getCause().getMessage());
			}
			throw e;
		}
	}
}
