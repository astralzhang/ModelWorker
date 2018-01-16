package cn.lmx.flow.service.officework;

import java.io.File;
import java.io.FileOutputStream;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.annotation.Resource;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import cn.lmx.flow.bean.officework.AcceptLevelBean;
import cn.lmx.flow.bean.officework.DocumentAcceptsBean;
import cn.lmx.flow.bean.officework.DocumentAttachmentBean;
import cn.lmx.flow.bean.officework.DocumentBean;
import cn.lmx.flow.bean.officework.MagazineBean;
import cn.lmx.flow.dao.flow.SQLDao;
import cn.lmx.flow.dao.officework.DocumentAcceptsDao;
import cn.lmx.flow.dao.officework.DocumentAttachmentDao;
import cn.lmx.flow.dao.officework.DocumentReturnDao;
import cn.lmx.flow.dao.officework.DocumentsDao;
import cn.lmx.flow.dao.officework.SubmitHistoryDao;
import cn.lmx.flow.dao.trade.AcceptLevelDao;
import cn.lmx.flow.dao.trade.InfomationTypeDao;
import cn.lmx.flow.dao.trade.MagazinesDao;
import cn.lmx.flow.dao.trade.PointManagerDao;
import cn.lmx.flow.dao.trade.TradeUnionInfoDao;
import cn.lmx.flow.dao.trade.TradeUnionUserDao;
import cn.lmx.flow.dao.trade.TradeUnionUserRelateDao;
import cn.lmx.flow.entity.officework.DocumentAccepts;
import cn.lmx.flow.entity.officework.DocumentAttachment;
import cn.lmx.flow.entity.officework.DocumentReturn;
import cn.lmx.flow.entity.officework.Documents;
import cn.lmx.flow.entity.officework.SubmitHistory;
import cn.lmx.flow.entity.trade.AcceptLevel;
import cn.lmx.flow.entity.trade.Magazines;
import cn.lmx.flow.entity.trade.PointManager;
import cn.lmx.flow.entity.trade.TradeUnionInfo;
import cn.lmx.flow.entity.trade.TradeUnionUser;
import cn.lmx.flow.entity.trade.TradeUnionUserRelate;

/**
 * 公文管理
 * @author yujx
 *
 */
@Repository("TradeDocService")
public class TradeDocService {
	//工会信息
	@Resource(name="TradeUnionInfoDao")
	private TradeUnionInfoDao tradeUnionInfoDao;
	//工会人员
	@Resource(name="TradeUnionUserDao")
	private TradeUnionUserDao tradeUnionUserDao;
	//可操作工会信息
	@Resource(name="TradeUnionUserRelateDao")
	private TradeUnionUserRelateDao tradeUnionUserRelateDao;
	//信息分类
	@Resource(name="InfomationTypeDao")
	private InfomationTypeDao infomationTypeDao;
	//采用期刊
	@Resource(name="MagazinesDao")
	private MagazinesDao magazinesDao;
	//录用级别
	@Resource(name="AcceptLevelDao")
	private AcceptLevelDao acceptLevelDao;
	//公文信息
	@Resource(name="DocumentsDao")
	private DocumentsDao documentDao;
	//SQL
	@Resource(name="SQLDao")
	private SQLDao sqlDao;
	//公文附件
	@Resource(name="DocumentAttachmentDao")
	private DocumentAttachmentDao documentAttachmentDao;
	//杂志采用
	@Resource(name="DocumentAcceptsDao")
	private DocumentAcceptsDao documentAcceptDao;
	//积分
	@Resource(name="PointManagerDao")
	private PointManagerDao pointManagerDao;
	//提交履历
	@Resource(name="SubmitHistoryDao")
	private SubmitHistoryDao submitHistoryDao;
	//公文退回履历
	@Resource(name="DocumentReturnDao")
	private DocumentReturnDao documentReturnDao;
	/**
	 * 取得公文画面基本信息
	 * @param systemUser
	 * @return
	 * @throws Exception
	 */
	@Transactional(rollbackFor=Exception.class)
	public Map<String, Object> listBasic(String systemUser) throws Exception {
		try {
			Map<String, Object> map = new HashMap<String, Object>();
			//取得登录人员可操作的工会信息
			TradeUnionUser tradeUnionUser = tradeUnionUserDao.getById(systemUser);
			if (tradeUnionUser == null) {
				throw new Exception("您没有工会信息，请和管理员联系！");
			}
			if (tradeUnionUser.getTradeUnionCode() == null || "".equals(tradeUnionUser.getTradeUnionCode())) {
				throw new Exception("您没有工会信息，请和管理员联系！");
			}
			List<?> tradeUserRelateList = tradeUnionUserRelateDao.listByUserNo(systemUser);
			if (tradeUnionUser != null || (tradeUserRelateList != null && tradeUserRelateList.size() > 0)) {
				//取得所有工会
				List<?> tradeUnionInfoList = tradeUnionInfoDao.listAll();
				List<TradeUnionInfo> tradeInfoList = new ArrayList<TradeUnionInfo>(); 
				if (tradeUnionInfoList != null && tradeUnionInfoList.size() > 0) {
					for (int i = 0; i < tradeUnionInfoList.size(); i++) {
						TradeUnionInfo info = (TradeUnionInfo)tradeUnionInfoList.get(i);
						if (info.getCode() == null) {
							continue;
						}
						if (tradeUnionUser != null) {
							//所属工会
							if (info.getCode().equals(tradeUnionUser.getTradeUnionCode())) {
								tradeInfoList.add(info);
								continue;
							}
							//所属工会的子工会
							if (tradeUnionUser.getTradeUnionCode().equals(info.getParentCode())) {
								tradeInfoList.add(info);
								continue;
							}
						}
						if (tradeUserRelateList != null) {
							for (int j = 0; j < tradeUserRelateList.size(); j++) {
								TradeUnionUserRelate relate = (TradeUnionUserRelate)tradeUserRelateList.get(j);
								if (info.getCode().equals(relate.getTradeUnionCode())) {
									tradeInfoList.add(info);
									continue;
								}
							}
						}
					}
				}
				map.put("TradeInfoList", tradeInfoList);
			}
			//取得信息分类
			List<?> infomationTypeList = infomationTypeDao.listAll();
			map.put("InfomationTypeList", infomationTypeList);
			//采用期刊
			List<?> magazinesList = magazinesDao.listAll();
			map.put("magazineList", magazinesList);
			//录用级别
			List<?> acceptLevelList = acceptLevelDao.listAll();
			List<AcceptLevelBean> levelList = new ArrayList<AcceptLevelBean>();
			if (acceptLevelList != null) {
				for (int i = 0; i < acceptLevelList.size(); i++) {
					AcceptLevel acceptLevel = (AcceptLevel)acceptLevelList.get(i);
					AcceptLevelBean bean = new AcceptLevelBean();
					BeanUtils.copyProperties(acceptLevel, bean);
					List<MagazineBean> magazineList = new ArrayList<MagazineBean>();
					if (magazinesList != null) {
						for (int j = 0; j < magazinesList.size(); j++) {
							Magazines magazine = (Magazines)magazinesList.get(j);
							if (bean.getCode().equals(magazine.getLevelCode())) {
								MagazineBean magazineBean = new MagazineBean();
								BeanUtils.copyProperties(magazine, magazineBean);
								magazineList.add(magazineBean);
							}
						}
					}
					bean.setCount(magazineList.size());
					bean.setMagazineList(magazineList);
					levelList.add(bean);
				}
			}
			map.put("AcceptLevelList", levelList);
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
			Calendar c = Calendar.getInstance();
			String startSubmitDate = sdf.format(c.getTime()).substring(0, 6) + "01";
			map.put("StartSubmitDate", startSubmitDate);
			c.add(Calendar.MONTH, 1);
			c.setTime(sdf.parse(sdf.format(c.getTime()).substring(0, 6) + "01"));
			c.add(Calendar.DAY_OF_MONTH, -1);
			String endSubmitDate = sdf.format(c.getTime());
			map.put("EndSubmitDate", endSubmitDate);
			return map;
		} catch (Exception e) {
			if (e.getCause() != null) {
				throw new Exception(e.getCause().getMessage());
			}
			throw e;
		}
	}
	/**
	 * 取得公文列表信息
	 * @param map
	 * @return
	 * @throws Exception
	 */
	@Transactional(rollbackFor=Exception.class)
	public Map<String, Object> listDocument(String systemUser, Map<String, Object> map) throws Exception {
		try {
			Map<String, Object> result = new HashMap<String, Object>();
			//取得指定条件的公文信息
			//String sql = "exec LP_LIST_DOCUMENT :systemUser,:TradeCode,:InfomationType,:Magazine,:StartDate,:EndDate";
			map.put("systemUser", systemUser);
			String tradeCode = map.get("TradeCode") == null ? null : String.valueOf(map.get("TradeCode"));
			String infomationType = map.get("InfomationType") == null ? null : String.valueOf(map.get("InfomationType"));
			String magazineCode = map.get("Magazine") == null ? null : String.valueOf(map.get("Magazine"));
			String startDate = map.get("StartDate") == null ? null : String.valueOf(map.get("StartDate"));
			String endDate = map.get("EndDate") == null ? null : String.valueOf(map.get("EndDate"));
			List<?>dataList = documentDao.listDoc(systemUser, tradeCode, infomationType, magazineCode, startDate, endDate);
			//List<?> dataList = sqlDao.select(sql, map);
			if (dataList == null || dataList.size() <= 0) {
				return result;
			}
			//取得所有期刊
			List<?> magazinesList = magazinesDao.listAll();
			//取得所有级别
			List<?> acceptLevelList = acceptLevelDao.listAll();
			List<AcceptLevelBean> levelList = new ArrayList<AcceptLevelBean>();
			if (acceptLevelList != null) {
				for (int i = 0; i < acceptLevelList.size(); i++) {
					AcceptLevel acceptLevel = (AcceptLevel)acceptLevelList.get(i);
					AcceptLevelBean bean = new AcceptLevelBean();
					BeanUtils.copyProperties(acceptLevel, bean);
					List<MagazineBean> magazineList = new ArrayList<MagazineBean>();
					if (magazinesList != null) {
						for (int j = 0; j < magazinesList.size(); j++) {
							Magazines magazine = (Magazines)magazinesList.get(j);
							if (bean.getCode().equals(magazine.getLevelCode())) {
								MagazineBean magazineBean = new MagazineBean();
								BeanUtils.copyProperties(magazine, magazineBean);
								magazineList.add(magazineBean);
							}
						}
					}
					bean.setCount(magazineList.size());
					bean.setMagazineList(magazineList);
					levelList.add(bean);
				}
			}
			//取得积分信息
			List<?> pointList = pointManagerDao.listAll();
			if (pointList == null) {
				pointList = new ArrayList<Object>();
			}
			List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
			for (int i = 0; i < dataList.size(); i++) {
				Map<String, Object> dataMap = (Map<String, Object>)dataList.get(i);
				String docId = dataMap.get("id") == null ? null : String.valueOf(dataMap.get("id"));
				if (docId == null || "".equals(docId)) {
					dataList.remove(i);
					i--;
					continue;
				}
				if (resultList.size() > 0) {
					Map<String, Object> dataMap2 = resultList.get(resultList.size() - 1);
					if (!docId.equals(dataMap2.get("id"))) {
						resultList.add(dataMap);
					} else {
						dataMap = dataMap2;
					}
				} else {
					resultList.add(dataMap);
				}
				//取得采用情况
				List<?> acceptList = documentAcceptDao.listByDocId(docId);
				List<Map<String, Object>> docAcceptList = new ArrayList<Map<String, Object>>();
				if (acceptList == null || acceptList.size() <= 0) {
					//没有被采用
					for (int m = 0; m < levelList.size(); m++) {
						AcceptLevelBean levelBean = levelList.get(m);
						for (int n = 0; n < levelBean.getMagazineList().size(); n++) {
							Map<String, Object> tempMap = new HashMap<String, Object>();
							tempMap.put("status", "0");
							docAcceptList.add(tempMap);
						}
					}
				} else {
					for (int m = 0; m < levelList.size(); m++) {
						AcceptLevelBean levelBean = levelList.get(m);
						List<MagazineBean> magazineList = levelBean.getMagazineList();
						for (int n = 0; n < magazineList.size(); n++) {
							MagazineBean magazineBean = magazineList.get(n);
							boolean hasData = false;
							for (int j = 0; j < acceptList.size(); j++) {
								DocumentAccepts accept = (DocumentAccepts)acceptList.get(j);
								if (magazineBean.getCode().equals(accept.getMagazineCode())) {
									Map<String, Object> tempMap = new HashMap<String, Object>();
									tempMap.put("status", "1");
									tempMap.put("submitDate", accept.getAcceptDate());
									docAcceptList.add(tempMap);
									hasData = true;
									break;
								}
							}
							if (hasData == false) {
								//该杂志未采用
								Map<String, Object> tempMap = new HashMap<String, Object>();
								tempMap.put("status", "0");
								docAcceptList.add(tempMap);
							}
						}
					}
				}
				//加入杂志采用状况
				dataMap.put("magazine", docAcceptList);
				//累计积分
				BigDecimal point = new BigDecimal(0);
				for (int j = 0; j < acceptList.size(); j++) {
					DocumentAccepts accept = (DocumentAccepts)acceptList.get(j);
					for (int p = 0; p < pointList.size(); p++) {
						PointManager pointManager = (PointManager)pointList.get(p);
						//比较采用杂志
						if (accept.getMagazineCode() != null && accept.getMagazineCode().equals(pointManager.getMagazineCode())) {
							//比较采用类型
							String acceptType = accept.getAcceptType() == null ? "" : accept.getAcceptType();
							String pointAcceptType = pointManager.getAcceptType() == null ? "" : pointManager.getAcceptType();
							if (acceptType.equals(pointAcceptType)) {
								//比较信息类型
								String infoType = dataMap.get("infoType") == null ? "" : String.valueOf(dataMap.get("infoType"));
								String pointInfoType = pointManager.getInfoType() == null ? "" : pointManager.getInfoType();
								if (infoType.equals(pointInfoType)) {
									point = point.add(pointManager.getPoint());
									break;
								}												
							}
						}
					}
				}
				//加入积分状况
				dataMap.put("point", point);
			}
			//数据排序
			if (resultList != null && resultList.size() > 0) {
				for (int i = 0; i < resultList.size(); i++) {
					Map<String, Object> befMap = resultList.get(i);
					String befSubmitDate = befMap.get("submitDate") == null ? "" : String.valueOf(befMap.get("submitDate"));
					for (int j = i + 1; j < resultList.size(); j++) {
						Map<String, Object> bakMap = resultList.get(j);
						String bakSubmitDate = bakMap.get("submitDate") == null ? "" : String.valueOf(bakMap.get("submitDate"));
						if (bakSubmitDate.compareTo(befSubmitDate) > 0) {
							resultList.remove(j);
							resultList.add(i, bakMap);
						}
					}
				}
				//设定月份信息
				String sSubmitMonth = null;
				int iPos = 0;
				for (int i = 0; i < resultList.size(); i++) {
					Map<String, Object> tempMap = resultList.get(i);
					String submitDate = tempMap.get("submitDate") == null ? "" : String.valueOf(tempMap.get("submitDate"));
					if (submitDate.length() < 6) {
						submitDate = "000000";
					}
					if (sSubmitMonth == null) {
						if (submitDate.length() < 6) {
							sSubmitMonth = "000000";
						} else {
							sSubmitMonth = submitDate.substring(0, 6);
						}
					}
					if (!sSubmitMonth.equals(submitDate.substring(0, 6))) {
						iPos++;
					}
					tempMap.put("monthFlag", iPos % 2);
				}
			}
			result.put("data", resultList);			
			return result;
		} catch (Exception e) {
			if (e.getCause() != null) {
				throw new Exception(e.getCause().getMessage());
			}
			throw e;
		}
	}
	/**
	 * 新增画面
	 * @param systemUser
	 * @return
	 * @throws Exception
	 */
	@Transactional(rollbackFor=Exception.class)
	public Map<String, Object> add(String systemUser) throws Exception {
		try {
			//取得信息类型
			List<?> infoList = infomationTypeDao.listAll();
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("InfomationTypeList", infoList);
			return map;
		} catch (Exception e) {
			if (e.getCause() != null) {
				throw new Exception(e.getCause().getMessage());
			}
			throw e;
		}
	}
	/**
	 * 上传附件
	 * @param systemUser
	 * @param list
	 * @throws Excpetion
	 */
	@Transactional(rollbackFor=Exception.class)
	public List<DocumentAttachmentBean> upload(String systemUser, String filePath, List<DocumentAttachmentBean> list) throws Exception {
		try {
			if (list == null || list.size() <= 0) {
				return null;
			}
			if (filePath == null || "".equals(filePath)) {
				return null;
			}
			File objFilePath = new File(filePath);
			if (!objFilePath.exists()) {
				objFilePath.mkdirs();
			}
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");
			Calendar c = Calendar.getInstance();
			for (int i = 0; i < list.size(); i++) {
				DocumentAttachmentBean bean = list.get(i);
				DocumentAttachment atta = new DocumentAttachment();
				BeanUtils.copyProperties(bean, atta);
				atta.setFileRealName(UUID.randomUUID().toString());
				atta.setCreateUser(systemUser);
				atta.setCreateTime(sdf.format(c.getTime()));
				documentAttachmentDao.add(atta);
				//保存文件
				String fileName = null;
				if (atta.getFileType() != null && !"".equals(atta.getFileType())) {
					fileName = new StringBuffer("")
							.append(filePath)
							.append(atta.getFileRealName())
							.append(".")
							.append(atta.getFileType()).toString();
				} else {
					fileName = new StringBuffer("")
							.append(filePath)
							.append(atta.getFileRealName()).toString();
				}
				File file = new File(fileName);
				FileOutputStream out = new FileOutputStream(file);
				out.write(bean.getFileContent());
				out.flush();
				out.close();
			}
			List<?> dataList = documentAttachmentDao.listAll();
			List<DocumentAttachmentBean> resultList = new ArrayList<DocumentAttachmentBean>();
			SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
			for (int i = 0; i < dataList.size(); i++) {
				DocumentAttachment atta = (DocumentAttachment)dataList.get(i);
				DocumentAttachmentBean bean = new DocumentAttachmentBean();
				BeanUtils.copyProperties(atta, bean);
				c.setTime(sdf.parse(atta.getCreateTime()));
				bean.setUploadTime(sdf1.format(c.getTime()));
				resultList.add(bean);
			}
			return resultList;
		} catch (Exception e) {
			if (e.getCause() != null) {
				throw new Exception(e.getCause().getMessage());
			}
			throw e;
		}
	}
	/**
	 * 删除附件
	 * @param systemUser
	 * @param id
	 * @throws Exception
	 */
	@Transactional(rollbackFor=Exception.class)
	public void deleteAttachment(String systemUser, String filePath, String id) throws Exception {
		try {
			DocumentAttachment atta = documentAttachmentDao.getById(id);
			if (atta != null) {
				documentAttachmentDao.delete(atta);
				//删除实际文件
				String fileName = null;
				if (atta.getFileType() != null && !"".equals(atta.getFileType())) {
					fileName = new StringBuffer("")
							.append(filePath)
							.append(atta.getFileRealName())
							.append(".")
							.append(atta.getFileType()).toString();
				} else {
					fileName = new StringBuffer("")
							.append(filePath)
							.append(atta.getFileRealName()).toString();
				}
				File file = new File(fileName);
				if (file.exists()) {
					file.delete();
				}
			}
		} catch (Exception e) {
			if (e.getCause() != null) {
				throw new Exception(e.getCause().getMessage());
			}
			throw e;
		}
	}
	/**
	 * 下载附件
	 * @param systemUser
	 * @param id
	 * @throws Exception
	 */
	@Transactional(rollbackFor=Exception.class)
	public DocumentAttachment download(String systemUser, String id) throws Exception {
		try {
			return documentAttachmentDao.getById(id);
		} catch (Exception e) {
			if (e.getCause() != null) {
				throw new Exception(e.getCause().getMessage());
			}
			throw e;
		}
	}
	/**
	 * 删除无效的附件文件
	 * @param systemUser
	 * @param id
	 * @throws Exception
	 */
	@Transactional(rollbackFor=Exception.class)
	public void deleteInvalidAttachment(String systemUser, String filePath, String id) throws Exception {
		try {
			Documents doc = documentDao.getById(id);
			if (doc != null) {
				//存在公文数据无需处理
				return;
			}
			//取得公文附件
			List<?> list = documentAttachmentDao.getByDocId(id);
			if (list != null && list.size() > 0) {
				//删除公文附件
				for (int i = 0; i < list.size(); i++) {
					DocumentAttachment atta = (DocumentAttachment)list.get(i);
					documentAttachmentDao.delete(atta);
					//删除实际文件
					String fileName = null;
					if (atta.getFileType() != null && !"".equals(atta.getFileType())) {
						fileName = new StringBuffer("")
								.append(filePath)
								.append(atta.getFileRealName())
								.append(".")
								.append(atta.getFileType()).toString();
					} else {
						fileName = new StringBuffer("")
								.append(filePath)
								.append(atta.getFileRealName()).toString();
					}
					File file = new File(fileName);
					if (file.exists()) {
						file.delete();
					}
				}
			}
		} catch (Exception e) {
			if (e.getCause() != null) {
				throw new Exception(e.getCause().getMessage());
			}
			throw e;
		}
	}
	/**
	 * 保存公文信息
	 * @param systemUser
	 * @param bean
	 * @throws Exception
	 */
	@Transactional(rollbackFor=Exception.class)
	public void save(String systemUser, DocumentBean bean) throws Exception {
		try {
			if (bean == null) {
				return;
			}
			/*************************check字段信息***********************/
			if (bean.getTitle() == null || "".equals(bean.getTitle())) {
				throw new Exception("请输入标题！");
			}
			if (bean.getTitle().getBytes("UTF-8").length > 100) {
				throw new Exception("您输入的标题不能大于100字节！");
			}
			if (bean.getDescription() != null) {
				if (bean.getDescription().getBytes("UTF-8").length > 3000) {
					throw new Exception("您输入的正文不可以超过3000字节！");
				}
			}
			/************************保存公文数据***************************/
			if (bean.getId() == null || "".equals(bean.getId())) {
				bean.setId(UUID.randomUUID().toString());
			}
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");
			Calendar c = Calendar.getInstance();
			Documents document = documentDao.getById(bean.getId());
			if (document == null) {
				//新增
				document = new Documents();
				BeanUtils.copyProperties(bean, document);
				//取得登录人员的工会
				List<?> tradeUnionUserList = tradeUnionUserDao.listByUser(systemUser);
				if (tradeUnionUserList == null || tradeUnionUserList.size() <= 0) {
					throw new Exception("您不是工会人员，无法保存公文信息！");
				}
				TradeUnionUser tradeUnionUser = (TradeUnionUser)tradeUnionUserList.get(0);
				if (tradeUnionUser.getTradeUnionCode() == null || "".equals(tradeUnionUser.getTradeUnionCode())) {
					throw new Exception("您不是工会人员，无法保存公文信息！");
				}
				document.setTradeUnionCode(tradeUnionUser.getTradeUnionCode());
				//取的工会信息
				TradeUnionInfo tradeUnionInfo = tradeUnionInfoDao.getById(tradeUnionUser.getTradeUnionCode());
				if (tradeUnionInfo == null) {
					throw new Exception("您对应的工会编码（" + tradeUnionUser.getTradeUnionCode() + "）不存在！");
				}
				document.setTradeUnionName(tradeUnionInfo.getName());
				//上报状态
				document.setSubmitStatus("0");
				//录用状态
				document.setAcceptStatus("0");
				//创建人员
				document.setCreateUser(systemUser);
				//创建时间
				document.setCreateTime(sdf.format(c.getTime()));
				documentDao.add(document);
			} else {
				//修改
				BeanUtils.copyProperties(bean, document);
				document.setUpdateUser(systemUser);
				document.setUpdateTime(sdf.format(c.getTime()));
				documentDao.edit(document);
			}
		} catch (Exception e) {
			if (e.getCause() != null) {
				throw new Exception(e.getCause().getMessage());
			}
			throw e;
		}
	}
	/**
	 * check公文信息，是否可以修改
	 * @param systemUser
	 * @param id
	 * @throws Exception
	 */
	@Transactional(rollbackFor=Exception.class)
	public void check(String systemUser, String id) throws Exception {
		try {
			Documents doc = documentDao.getById(id);
			if (doc == null) {
				throw new Exception("您选择的信息不存在，请检索后重新选择！");
			}
			//取得当前用户的工会信息
			List<?> tradeUserList = tradeUnionUserDao.listByUser(systemUser);
			if (tradeUserList == null || tradeUserList.size() <= 0) {
				throw new Exception("您不是工会成员，无法修改信息！");
			}
			TradeUnionUser tradeUser = (TradeUnionUser)tradeUserList.get(0);
			if (tradeUser == null || tradeUser.getTradeUnionCode() == null || "".equals(tradeUser.getTradeUnionCode())) {
				throw new Exception("您不是工会成员，无法修改信息！");
			}
			if ("0".equals(doc.getSubmitStatus()) && tradeUser.getTradeUnionCode().equals(doc.getTradeUnionCode())) {
				//本工会的信息,没有上报，可以修改
				return;
			}
			//取得当前处理的工会
			List<?> historyList = submitHistoryDao.listByDocId(id);
			if (historyList == null || historyList.size() <= 0) {
				throw new Exception("上报信息不正确，请和系统管理员联系！");
			}
			SubmitHistory submitHistory = (SubmitHistory)historyList.get(0);
			if (tradeUser.getTradeUnionCode().equals(submitHistory.getRecvTradeCode())) {
				return;
			}
			throw new Exception("信息已上报，无法修改！");
		} catch (Exception e) {
			if (e.getCause() != null) {
				throw new Exception(e.getCause().getMessage());
			}
			throw e;
		}
	}
	/**
	 * 修改画面
	 * @param systemUser
	 * @param id
	 * @return
	 * @throws Exception
	 */
	@Transactional(rollbackFor=Exception.class)
	public Map<String, Object> edit(String systemUser, String id) throws Exception {
		try {
			Map<String, Object> map = new HashMap<String, Object>();
			//取得公文数据
			Documents doc = documentDao.getById(id);
			if (doc == null) {
				throw new Exception("公文信息不存在，请检索后重试！");
			}
			//取得信息类型
			List<?> infoList = infomationTypeDao.listAll();
			//取得公文附件信息
			List<?> attaList = documentAttachmentDao.getByDocId(id);
			List<DocumentAttachmentBean> attaBeanList = new ArrayList<DocumentAttachmentBean>();
			if (attaList != null) {
				SimpleDateFormat sdf1 = new SimpleDateFormat("yyyyMMddHHmmssSSS");
				SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
				for (int i = 0; i < attaList.size(); i++) {
					DocumentAttachment atta = (DocumentAttachment)attaList.get(i);
					DocumentAttachmentBean attaBean = new DocumentAttachmentBean();
					BeanUtils.copyProperties(atta, attaBean);
					attaBean.setFileContent(null);
					attaBean.setUploadTime(sdf2.format(sdf1.parse(atta.getCreateTime())));
					attaBeanList.add(attaBean);
				}
			}
			map.put("InfomationTypeList", infoList);
			map.put("Document", doc);
			map.put("DocumentAttachment", attaBeanList);
			return map;
		} catch (Exception e) {
			if (e.getCause() != null) {
				throw new Exception(e.getCause().getMessage());
			}
			throw e;
		}
	}
	/**
	 * 公文上报
	 * @param systemUser
	 * @param id
	 * @throws Exception
	 */
	@Transactional(rollbackFor=Exception.class)
	public void submit(String systemUser, String id) throws Exception {
		try {
			//取得公文信息
			Documents doc = documentDao.getById(id);
			if (doc == null) {
				throw new Exception("您选择的信息已不存在，请检索后重新上报！");
			}
			//取得当前处理人所属工会
			List<?> tradeUserList = tradeUnionUserDao.listByUser(systemUser);
			if (tradeUserList == null || tradeUserList.size() <= 0) {
				throw new Exception("您不是工会成员，无法上报信息！");
			}
			TradeUnionUser tradeUser = (TradeUnionUser)tradeUserList.get(0);
			if (tradeUser.getTradeUnionCode() == null || "".equals(tradeUser.getTradeUnionCode())) {
				throw new Exception("您不是工会成员，无法上报信息！");
			}
			if ("0".equals(doc.getSubmitStatus())) {
				//尚未上报
				if (!tradeUser.getTradeUnionCode().equals(doc.getTradeUnionCode())) {
					TradeUnionInfo tradeUnionInfo = tradeUnionInfoDao.getById(doc.getTradeUnionCode());
					if (tradeUnionInfo == null) {
						throw new Exception("工会编号（" + doc.getTradeUnionCode() + "）不存在！");
					}
					throw new Exception("当前信息所属工会为：" + tradeUnionInfo.getName() + ",您无法上报！");
				}
			} else {
				//已上报
				List<?> historyList = submitHistoryDao.listByDocId(id);
				if (historyList == null || historyList.size() <= 0) {
					throw new Exception("当前公文上报状态不正确，请联系系统管理员！");
				}
				SubmitHistory history = (SubmitHistory)historyList.get(0);
				if (!tradeUser.getTradeUnionCode().equals(history.getRecvTradeCode())) {
					TradeUnionInfo tradeUnionInfo = tradeUnionInfoDao.getById(history.getRecvTradeCode());
					if (tradeUnionInfo == null) {
						throw new Exception("工会编号（" + history.getRecvTradeCode() + "）不存在！");
					}
					throw new Exception("当前信息所属工会为：" + tradeUnionInfo.getName() + ",您无法上报！");
				}
			}
			//取得当前人员的上级工会
			TradeUnionInfo tradeUnionInfo = tradeUnionInfoDao.getById(tradeUser.getTradeUnionCode());
			if (tradeUnionInfo.getParentCode() == null || "".equals(tradeUnionInfo.getParentCode())) {
				throw new Exception("当前工会为" + tradeUnionInfo.getName() + ",没有设定上级工会信息！");
			}
			//信息上报
			SimpleDateFormat sdf1 = new SimpleDateFormat("yyyyMMddHHmmssSSS");
			SimpleDateFormat sdf2 = new SimpleDateFormat("yyyyMMdd");
			Calendar c = Calendar.getInstance();
			doc.setSubmitStatus("1");
			doc.setCurrentTradeUnionCode(tradeUnionInfo.getParentCode());
			doc.setUpdateUser(systemUser);
			doc.setUpdateTime(sdf1.format(c.getTime()));
			documentDao.edit(doc);
			//新增上报历史
			SubmitHistory history = new SubmitHistory();
			history.setId(UUID.randomUUID().toString());
			history.setParentId(id);
			history.setSubmitTradeCode(tradeUser.getTradeUnionCode());
			history.setRecvTradeCode(tradeUnionInfo.getParentCode());
			history.setSubmitDate(sdf2.format(c.getTime()));
			history.setStatus("Y");
			history.setCreateUser(systemUser);
			history.setCreateTime(sdf1.format(c.getTime()));
			submitHistoryDao.add(history);
		} catch (Exception e) {
			if (e.getCause() != null) {
				throw new Exception(e.getCause().getMessage());
			}
			throw e;
		}
	}
	/**
	 * 根据公文ID获取公文信息及申报信息
	 * @param id
	 * @param userNo
	 * @return
	 */
	@Transactional(rollbackFor=Exception.class)
	public Map<String, Object> docUseView(String id, String userNo) throws Exception {
		 Map<String, Object> map  = new HashMap<String, Object>();
		 if (id == null && "".equals(id)) {
			 throw new Exception("请选择公文信息！");
		 }
		 //根据ID获取文件信息
		 Documents documents = new Documents();
		 try {
			 TradeUnionUser tradeUser = tradeUnionUserDao.getById(userNo);
			 if (tradeUser == null) {
				 throw new Exception("用户的工会信息不存在！");
			 }
			 //取得工会信息
			 TradeUnionInfo tradeInfo = tradeUnionInfoDao.getById(tradeUser.getTradeUnionCode());
			 if (tradeInfo == null) {
				 throw new Exception("用户工会信息不存在！");
			 }
			documents = documentDao.getById(id);
			//取得信息分类
			List<?> infomationTypeList = infomationTypeDao.listAll();
			//取得公文附件信息
			List<?> attaList = documentAttachmentDao.getByDocId(id);
			List<DocumentAttachmentBean> attaBeanList = new ArrayList<DocumentAttachmentBean>();
			if (attaList != null) {
				SimpleDateFormat sdf1 = new SimpleDateFormat("yyyyMMddHHmmssSSS");
				SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
				for (int i = 0; i < attaList.size(); i++) {
					DocumentAttachment atta = (DocumentAttachment)attaList.get(i);
					DocumentAttachmentBean attaBean = new DocumentAttachmentBean();
					BeanUtils.copyProperties(atta, attaBean);
					attaBean.setFileContent(null);
					attaBean.setUploadTime(sdf2.format(sdf1.parse(atta.getCreateTime())));
					attaBeanList.add(attaBean);
				}
			}
//				//根据公文ID获取附件列表
//				List<?> list = documentAttachmentDao.getByDocId(id);
//				List<DocumentAttachmentBean> DocumentAttachmentList = new ArrayList<DocumentAttachmentBean>();
//				if (list != null) {
//					for (int i = 0; i < list.size(); i++) {
//						DocumentAttachment docBean = (DocumentAttachment)list.get(i);
//						DocumentAttachmentBean bean = new DocumentAttachmentBean();
//						BeanUtils.copyProperties(docBean, bean);
//						DocumentAttachmentList.add(bean);
//					}
//				}
			//公文采用列表
			List<?> list1 = documentAcceptDao.getListByDocId(id);
			/*List<DocumentAcceptsBean> DocumentAcceptsList = new ArrayList<DocumentAcceptsBean>();
			if (list1 != null) {
				for (int i = 0; i < list1.size(); i++) {
					DocumentAcceptsBean docBean = (DocumentAccepts)list1.get(i);
					DocumentAcceptsBean bean = new DocumentAcceptsBean();
					BeanUtils.copyProperties(docBean, bean);
					DocumentAcceptsList.add(bean);
				}
			}*/
			Calendar c = Calendar.getInstance();
			int year = c.get(Calendar.YEAR);				
			List<Integer> yearList = new ArrayList<Integer>();
			for (int i = -1; i <= 1; i++) {
				yearList.add(year + i);
			}
			//设定默认年
			if (list1 != null) {
				for (int i = 0; i < list1.size(); i++) {
					DocumentAcceptsBean bean = (DocumentAcceptsBean)list1.get(i);
					if (bean.getMagazineYear() == null || "".equals(bean.getMagazineYear())) {
						bean.setMagazineYear(String.valueOf(year));
					}
				}
			}
			//获取
			map.put("documents", documents);
			map.put("docAttachList", attaBeanList);
			map.put("infomationTypeList", infomationTypeList);
			map.put("docAcceptList", list1);
			map.put("yearList", yearList);
			map.put("tradeUnionInfo", tradeInfo);
			return map;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			if (e.getCause() != null) {
				throw new Exception(e.getCause().getMessage());
			}
			throw e;
		}

	}
	
	/**
	 * 保存采用
	 * @param documentAcceptsBean
	 */
	@Transactional(rollbackFor=Exception.class)
	public void saveDocUser(DocumentAcceptsBean documentAcceptsBean,String userName) throws Exception{
		try {
			String id = "";
			id = documentAcceptsBean.getParentId();
			if(id != null && !"".equals(id)){
				SimpleDateFormat sdf1 = new SimpleDateFormat("yyyyMMddHHmmssSSS");
				SimpleDateFormat sdf2 = new SimpleDateFormat("yyyyMMdd");
				Calendar c = Calendar.getInstance();
				List<DocumentAcceptsBean> documentAcceptsList = documentAcceptsBean.getDocumentAcceptsList();
				//取得公文信息
				Documents doc = documentDao.getById(id);
				if (doc == null) {
					throw new Exception("对应的公文不存在，请确认后重试！");
				}
				//取得现有的录用信息
				List<?> list = documentAcceptDao.listByDocId(id);
				//是否存在录用信息
				boolean hasAccept = false;
				if (list != null || list.size() > 0) {
					//check现有录用信息
					for (int i = 0; i < list.size(); i++) {
						DocumentAccepts accept = (DocumentAccepts)list.get(i);
						String levelCode = accept.getLevelCode() == null ? "" : accept.getLevelCode();
						String magazineCode = accept.getMagazineCode() == null ? "" : accept.getMagazineCode();
						String acceptType = accept.getAcceptType() == null ? "" : accept.getAcceptType();
						if (documentAcceptsList == null || documentAcceptsList.size() <= 0) {
							//删除录用信息
							documentAcceptDao.delete(accept);
							continue;
						}
						boolean hasData = false;
						for (int j = 0; j < documentAcceptsList.size(); j++) {
							DocumentAcceptsBean bean = documentAcceptsList.get(j);
							String newLevelCode = bean.getLevelCode() == null ? "" : bean.getLevelCode();
							String newMagazineCode = bean.getMagazineCode() == null ? "" : bean.getMagazineCode();
							String newAcceptType = bean.getAcceptType() == null ? "" : bean.getAcceptType();
							if (levelCode.equals(newLevelCode) && magazineCode.equals(newMagazineCode) && acceptType.equals(newAcceptType)) {
								//录用信息存在
								BigDecimal period = bean.getPeriod();
								if (period != null) {
									if (!bean.getPeriod().equals(accept.getPeriod())) {
										//更新录用期刊年
										accept.setMagazineYear(bean.getMagazineYear());
										//更新录用期次
										accept.setPeriod(bean.getPeriod());
										accept.setUpdateUser(userName);
										accept.setUpdateTime(sdf1.format(c.getTime()));
										documentAcceptDao.edit(accept);
									}
								}
								documentAcceptsList.remove(j);
								hasData = true;
								hasAccept = true;
								break;
							}
						}
						if (hasData == false) {
							//该录用信息已经不存在，删除
							documentAcceptDao.delete(accept);
						}
					}
				}
				//新增新的录用信息
				if (documentAcceptsList != null && documentAcceptsList.size() > 0) {
					for (int i = 0; i < documentAcceptsList.size(); i++) {
						DocumentAcceptsBean bean = documentAcceptsList.get(i);
						DocumentAccepts accept = new DocumentAccepts();
						BeanUtils.copyProperties(bean, accept);
						accept.setAcceptDate(sdf2.format(c.getTime()));
						accept.setId(UUID.randomUUID().toString());
						accept.setStatus("0");
						accept.setPeriod(bean.getPeriod());
						accept.setCreateUser(userName);
						accept.setCreateTime(sdf1.format(c.getTime()));
						documentAcceptDao.add(accept);
					}
					hasAccept = true;
				}
				if (hasAccept) {
					//存在录用信息
					doc.setAcceptStatus("1");
					doc.setUpdateUser(userName);
					doc.setUpdateTime(sdf1.format(c.getTime()));
				} else {
					//不存在录用信息
					doc.setAcceptStatus("0");
					doc.setUpdateUser(userName);
					doc.setUpdateTime(sdf1.format(c.getTime()));
				}
				documentDao.edit(doc);			
				/*
				List<DocumentAcceptsBean> documentAcceptsList = new ArrayList<DocumentAcceptsBean>();
				documentAcceptsList = documentAcceptsBean.getDocumentAcceptsList();
	
				//先删除旧数据
				documentAcceptDao.deleteById(id);
				Calendar c = Calendar.getInstance();
				SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");
				//TODO,积分
				int countNum=0;
				//存在采用信息时，保存，记录积分信息
				if(documentAcceptsList != null && documentAcceptsList.size()>0){
					for(int i=0;i<documentAcceptsList.size();i++){
						DocumentAcceptsBean bean = new DocumentAcceptsBean();
						bean = documentAcceptsList.get(i);
						DocumentAccepts  documentAccepts = new DocumentAccepts();
						documentAccepts.setId(UUID.randomUUID().toString());
						documentAccepts.setParentId(id);
						documentAccepts.setAcceptType(bean.getAcceptType());
						documentAccepts.setAcceptTypeName(bean.getAcceptTypeName());
						documentAccepts.setLevelCode(bean.getLevelCode());
						documentAccepts.setLevelName(bean.getLevelName());
						documentAccepts.setMagazineCode(bean.getMagazineCode());
						documentAccepts.setMagazineName(bean.getMagazineName());
						documentAccepts.setCreateTime(sdf.format(c.getTime()));
						documentAccepts.setCreateUser(userName);
						documentAcceptDao.add(documentAccepts);
					}
					//如果存在，则采用状态为1
					Documents documents = new Documents();
					documents = documentDao.getById(id);
					documents.setAcceptStatus("1");
	//				documents.set
					documentDao.edit(documents);
				}else{
					//如果不存在，则采用状态为0
					Documents documents = new Documents();
					documents = documentDao.getById(id);
					documents.setAcceptStatus("0");
					documentDao.edit(documents);
				}*/
			}
		} catch (Exception e) {
			if (e.getCause() != null) {
				throw new Exception(e.getCause().getMessage());
			}
			throw e;
		}
	}
	/**
	 * 删除check
	 * @param ids
	 * @return
	 * @throws Exception 
	 */
	@Transactional(rollbackFor=Exception.class)
	public Documents checkUseNum(String ids) throws Exception {
		return documentDao.getById(ids);
	}
	/**
	 * 删除公文信息
	 * @param id
	 * @throws Exception 
	 */
	@Transactional(rollbackFor=Exception.class)
	public void deleteDoc(String id) throws Exception {
		//删除公文信息
		Documents bean = new Documents();
		bean.setId(id);
		documentDao.delete(bean);
		//删除附件信息
		documentAttachmentDao.deleteByDcoId(id);
		
	}
	/**
	 * 取得指定工会级别的积分排名
	 * @param tradeLevel
	 * @return
	 * @throws Exception
	 */
	@Transactional(rollbackFor=Exception.class)
	public Map<String, Object> pointList(String tradeLevel) throws Exception {
		try {
			List<?> list = documentDao.point(tradeLevel);
			Map<String, Object> resultMap = new HashMap<String, Object>();
			resultMap.put("data", list);
			return resultMap;
		} catch (Exception e) {
			if (e.getCause() != null) {
				throw new Exception(e.getCause().getMessage());
			}
			throw e;
		}
	}
	/**
	 * 退回公文
	 * @param ids
	 * @throws Exception
	 */
	@Transactional(rollbackFor=Exception.class)
	public void documentReturn(String systemUser, String ids, String cause) throws Exception {
		try {
			if (ids == null || "".equals(ids)) {
				throw new Exception("请选择需要驳回的信息！");
			}
			String[] arrId = ids.split(",");
			if (arrId == null || arrId.length <= 0) {
				throw new Exception("请选择需要驳回的信息！");
			}
			//取得系统用户所在的工会
			List<?> tradeUserList = tradeUnionUserDao.listByUser(systemUser);
			if (tradeUserList == null || tradeUserList.size() <= 0) {
				throw new Exception("您不是工会成员，无法驳回信息！");
			}
			TradeUnionUser tradeUnionUser = (TradeUnionUser)tradeUserList.get(0);
			if (tradeUnionUser == null) {
				throw new Exception("您不是工会成员，无法驳回信息！");
			}
			SimpleDateFormat sdf1 = new SimpleDateFormat("yyyyMMddHHmmssSSS");
			SimpleDateFormat sdf2 = new SimpleDateFormat("yyyyMMdd");
			Calendar c = Calendar.getInstance();
			for (int i = 0; i < arrId.length; i++) {
				String id = arrId[i];
				if (id == null || "".equals(id)) {
					continue;
				}
				//取得公文
				Documents doc = documentDao.getById(id);
				if (doc == null) {
					throw new Exception("您选择的信息，已被他人删除，请确认后重试！");
				}
				String currentTrade = doc.getCurrentTradeUnionCode() == null ? "" : doc.getCurrentTradeUnionCode();
				if (!currentTrade.equals(tradeUnionUser.getTradeUnionCode())) {
					throw new Exception("您无法驳回公文（" + doc.getTitle() + "）");
				}
				if (currentTrade.equalsIgnoreCase(doc.getTradeUnionCode())) {
					throw new Exception("您无法驳回自己工会做成的公文，公文标题为：" + doc.getTitle());
				}
				//check公文采用信息
				List<?> acceptList = documentAcceptDao.listByDocId(id);
				if (acceptList != null && acceptList.size() > 0) {
					throw new Exception("公文（" + doc.getTitle() +  "）已被录用，无法驳回！");
				}
				//取得公文上报履历
				List<?> historyList = submitHistoryDao.listByDocId(id);
				if (historyList == null || historyList.size() < 0) {
					throw new Exception("公文（" + doc.getTitle() + "）没有上报，无法驳回！");
				}
				SubmitHistory history = null;
				for (int j = 0; j < historyList.size(); j++) {
					SubmitHistory history2 = (SubmitHistory)historyList.get(j);
					if ("Y".equals(history2.getStatus())) {
						history = history2;
						break;
					}
				}
				if (history == null) {
					throw new Exception("公文（" + doc.getTitle() + "）没有上报，无法驳回！");
				}
				//新增驳回公文
				DocumentReturn documentReturn = new DocumentReturn();
				documentReturn.setId(UUID.randomUUID().toString());
				documentReturn.setParentId(doc.getId());
				documentReturn.setSubmitId(history.getId());
				documentReturn.setReturnTradeCode(tradeUnionUser.getTradeUnionCode());
				documentReturn.setRecvTradeCode(history.getSubmitTradeCode());
				documentReturn.setReturnDate(sdf2.format(c.getTime()));
				documentReturn.setReturnCause(cause);
				documentReturn.setCreateUser(systemUser);
				documentReturn.setCreateTime(sdf1.format(c.getTime()));
				documentReturnDao.add(documentReturn);
				//修改当前处理工会
				doc.setCurrentTradeUnionCode(history.getSubmitTradeCode());
				if (doc.getCurrentTradeUnionCode().equals(doc.getTradeUnionCode())) {
					//做成工会和当前工会相同,修改上报状态为未上报
					doc.setSubmitStatus("0");
				}
				doc.setUpdateUser(systemUser);
				doc.setUpdateTime(sdf1.format(c.getTime()));
				documentDao.edit(doc);
				//修改上报履历为已取消
				history.setStatus("R");
				history.setUpdateUser(systemUser);
				history.setUpdateTime(sdf1.format(c.getTime()));
				submitHistoryDao.edit(history);
			}
		} catch (Exception e) {
			e.printStackTrace();
			if (e.getCause() != null) {
				throw new Exception(e.getCause().getMessage());
			}
			throw e;
		}
	}
}
