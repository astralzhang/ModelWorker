package cn.lmx.flow.utils;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import com.google.gson.Gson;

import cn.lmx.flow.bean.view.json.input.Excel;
import cn.lmx.flow.bean.view.json.input.ImportSet;
import cn.lmx.flow.bean.view.json.input.MappingField;
import cn.lmx.flow.bean.view.json.input.Parameter;
import cn.lmx.flow.bean.view.json.input.SpaceRow;
import cn.lmx.flow.bean.view.json.input.SqlProc;
import cn.lmx.flow.bean.view.json.input.Table;
import cn.lmx.flow.bean.view.json.input.Text;
import cn.lmx.flow.dao.business.BusinessSQLDao;
import cn.lmx.flow.dao.flow.SQLDao;
import cn.lmx.flow.dao.system.DataLogDao;
import cn.lmx.flow.dao.view.ImportPublishDao;
import cn.lmx.flow.entity.system.DataLogs;
import cn.lmx.flow.entity.view.ImportPublish;

public class FileImportUtils {
	//导入
	private ImportPublishDao importPublishDao;
	//日志
	private DataLogDao dataLogDao;
	//SQL
	private SQLDao sqlDao;
	//业务数据库SQL
	private BusinessSQLDao businessSQLDao;
	/**
	 * 构造函数
	 * @param importPublishDao
	 * @param dataLogDao
	 * @param sqlDao
	 * @param businessSQLDao
	 */
	public FileImportUtils(ImportPublishDao importPublishDao, DataLogDao dataLogDao, SQLDao sqlDao, BusinessSQLDao businessSQLDao) {
		this.importPublishDao = importPublishDao;
		this.dataLogDao = dataLogDao;
		this.sqlDao = sqlDao;
		this.businessSQLDao = businessSQLDao;
	}
	/**
	 * check空行
	 * @param sheet
	 * @param importSet
	 * @param currentRowNo
	 * @param arrData
	 * @throws Exception
	 */
	private void checkSpaceRow(Sheet sheet, ImportSet importSet, int currentRowNo, String[] arrData) throws Exception {
		SpaceRow spaceRow = importSet.getSpaceRow();
		//执行存储过程
		if (spaceRow.getProc() == null || "".equals(spaceRow.getProc())) {
			//没有指定执行的SQL或存储过程
			return;
		}
		String proc = URLDecoder.decode(spaceRow.getProc(), "UTF-8");
		List<Parameter> parameterList = spaceRow.getParameter();
		if (parameterList == null || parameterList.size() <= 0) {
			if ("R".equals(spaceRow.getTarget())) {
				businessSQLDao.update(proc, new HashMap<String, Object>());
			} else {
				sqlDao.update(proc, new HashMap<String, Object>());
			}
		} else {
			//存在参数
			Map<String, Object> map = new HashMap<String, Object>();
			for (int j = 0; j < parameterList.size(); j++) {
				Parameter parameter = parameterList.get(j);
				parseParameter(importSet, parameter, sheet, currentRowNo, map, arrData);
			}
			if (map != null) {
				Iterator<Entry<String, Object>> it = map.entrySet().iterator();
				while (it.hasNext()) {
					Entry<String, Object> entry = it.next();
					System.out.println(entry.getKey() + "=" + entry.getValue());
				}
			}
			if ("R".equals(spaceRow.getTarget())) {
				businessSQLDao.update(proc, map);
			} else {
				sqlDao.update(proc, map);
			}
		}
	}
	/**
	 * 执行存储过程
	 * @param list
	 * @param sheet
	 * @param runTime
	 * @throws Exception
	 */
	private void executeProc(Sheet sheet, String runTime, ImportSet importSet, int currentRowNo, String[] arrData) throws Exception {
		List<SqlProc> list = importSet.getProc();
		if (list == null || list.size() <= 0) {
			return;
		}
		if (runTime == null || "".equals(runTime)) {
			return;
		}
		for (int i = 0; i < list.size(); i++) {
			SqlProc sqlProc = list.get(i);
			if (runTime.equals(sqlProc.getRunTime())) {
				//执行存储过程
				if (sqlProc.getProc() == null || "".equals(sqlProc.getProc())) {
					//没有指定执行的SQL或存储过程
					continue;
				}
				String proc = URLDecoder.decode(sqlProc.getProc(), "UTF-8");
				List<Parameter> parameterList = sqlProc.getParameter();
				if (parameterList == null || parameterList.size() <= 0) {
					if ("R".equals(sqlProc.getTarget())) {
						businessSQLDao.update(proc, new HashMap<String, Object>());
					} else {
						sqlDao.update(proc, new HashMap<String, Object>());
					}
				} else {
					//存在参数
					Map<String, Object> map = new HashMap<String, Object>();
					for (int j = 0; j < parameterList.size(); j++) {
						Parameter parameter = parameterList.get(j);
						parseParameter(importSet, parameter, sheet, currentRowNo, map, arrData);
					}
					if (map != null) {
						Iterator<Entry<String, Object>> it = map.entrySet().iterator();
						while (it.hasNext()) {
							Entry<String, Object> entry = it.next();
							System.out.println(entry.getKey() + "=" + entry.getValue());
						}
					}
					if ("R".equals(sqlProc.getTarget())) {
						businessSQLDao.update(proc, map);
					} else {
						sqlDao.update(proc, map);
					}
				}
			}
		}
	}
	/**
	 * 参数解析
	 * @param importSet
	 * @param parameter
	 * @param sheet
	 * @param rowNo
	 * @param map
	 * @throws Exception
	 */
	private void parseParameter(ImportSet importSet, Parameter parameter, Sheet sheet, int rowNo, Map<String, Object> map, String[] arrData) throws Exception {
		try {
			if ("text".equals(importSet.getType())) {
				if (parameter.getSourceField() == null || "".equals(parameter.getSourceField())) {
					throw new Exception("没有指定列号！");
				}
				try {
					int iPos = Integer.parseInt(parameter.getSourceField()) - 1;
					if (iPos >= arrData.length) {
						return;
					}
					map.put(parameter.getTargetField(), arrData[iPos]);
				} catch (NumberFormatException e) {
					e.printStackTrace();
				}
				return;
			} else {
				if ("header".equals(parameter.getSource())) {
					//数据来源为标题
					String cellName = parameter.getSourceField();
					Object value = ExcelUtils.getCellDataByName(sheet, cellName);
					map.put(parameter.getTargetField(), value);
					return;
				}
				if ("title".equals(parameter.getSource())) {
					Excel excel = importSet.getExcel();
					if (excel.getTitleStartRowNo() <= 0 || excel.getTitleEndRowNo() <= 0) {
						throw new Exception("没有指定明细标题行！");
					}
					int iCol = ExcelUtils.getColNoByTitle(sheet, excel.getTitleStartRowNo(), excel.getTitleEndRowNo(), parameter.getSourceField());
					if (iCol < 0) {
						throw new Exception("没有对应明细标题(" + parameter.getSourceField() + "，请确认！");
					}
					Object value = ExcelUtils.getCellData(sheet, rowNo, iCol);
					map.put(parameter.getTargetField(), value);
					return;
				}
				if ("column".equals(parameter.getSource())) {
					if (parameter.getSourceField() == null || "".equals(parameter.getSourceField())) {
						throw new Exception("没有指定列号！");
					}
					String cellName = new StringBuffer("")
							.append(parameter.getSourceField())
							.append(rowNo).toString();
					Object value = ExcelUtils.getCellDataByName(sheet, cellName);
					map.put(parameter.getTargetField(), value);
					return;
				}
			}
			return;
		} catch (Exception e) {
			throw e;
		}
	}
	/**
	 * 创建新增SQL文
	 * @param table
	 * @return
	 * @throws Exception
	 */
	private String createAddSql(Table table) throws Exception {
		List<MappingField> mappingList = table.getMapping();
		if (mappingList == null || mappingList.size() <= 0) {
			throw new Exception("没有指定导入字段！");
		}
		StringBuffer sbField = new StringBuffer("");
		StringBuffer sbValue = new StringBuffer("");
		for (int i = 0; i < mappingList.size(); i++) {
			MappingField field = mappingList.get(i);
			if ("excel".equals(field.getSource())) {
				//来源Excel
				if (sbField.length() > 0) {
					sbField.append(",");
				}
				sbField.append(field.getTargetField());
				if (sbValue.length() > 0) {
					sbValue.append(",");
				}
				sbValue.append(":").append(field.getTargetField());
			} else if ("sqlFunc".equals(field.getSource())) {
				//数据库函数
				if (sbField.length() > 0) {
					sbField.append(",");
				}
				sbField.append(field.getTargetField());
				if (sbValue.length() > 0) {
					sbValue.append(",");
				}
				String proc = URLDecoder.decode(field.getFunc(), "UTF-8");
				sbValue.append(proc);
			} else if ("sqlProc".equals(field.getSource())) {
				//SQL文或存储过程
				if (sbField.length() > 0) {
					sbField.append(",");
				}
				sbField.append(field.getTargetField());
				if (sbValue.length() > 0) {
					sbValue.append(",");
				}
				sbValue.append(":").append(field.getTargetField());
			} else if ("script".equals(field.getSource())) {
				//脚本函数
				if (sbField.length() > 0) {
					sbField.append(",");
				}
				sbField.append(field.getTargetField());
				if (sbValue.length() > 0) {
					sbValue.append(",");
				}
				sbValue.append(":").append(field.getTargetField());
			} else {
				throw new Exception("数据字段的数据来源设定错误！");
			}
		}
		if (sbField.length() <= 0) {
			throw new Exception("没有指定导入的数据字段！");
		}
		String sql = new StringBuffer("")
				.append("insert into ")
				.append(table.getTable())
				.append("(")
				.append(sbField.toString())
				.append(",CreateUser,CreateTime) values(")
				.append(sbValue.toString())
				.append(",:systemUser,:currentTime)").toString();
		return sql;
	}
	/**
	 * 创建更新SQL文
	 * @param table
	 * @return
	 * @throws Exception
	 */
	private Map<String, String> createEditSql(Table table) throws Exception {
		List<MappingField> mappingList = table.getMapping();
		if (mappingList == null || mappingList.size() <= 0) {
			throw new Exception("没有指定导入字段！");
		}
		StringBuffer sbField = new StringBuffer("");
		StringBuffer sbCondition = new StringBuffer("");
		for (int i = 0; i < mappingList.size(); i++) {
			MappingField field = mappingList.get(i);
			if ("excel".equals(field.getSource())) {
				//来源Excel
				if ("Y".equals(field.getKeyField())) {
					//Key字段
					if (sbCondition.length() > 0) {
						sbCondition.append(" and ");
					}
					sbCondition.append(field.getTargetField())
							.append("=:").append(field.getTargetField());
					continue;
				}
				if ("Y".equals(field.getReplace())) {
					if (sbField.length() > 0) {
						sbField.append(",");
					}
					sbField.append(field.getTargetField())
						.append("=:")
						.append(field.getTargetField());
				}
			} else if ("sqlFunc".equals(field.getSource())) {
				//数据库函数
				String proc = URLDecoder.decode(field.getFunc(), "UTF-8");
				if ("Y".equals(field.getKeyField())) {
					//Key字段
					if (sbCondition.length() > 0) {
						sbCondition.append(" and ");
					}
					sbCondition.append(field.getTargetField()).append("=").append(proc);
					continue;
				}
				if ("Y".equals(field.getReplace())) {
					if (sbField.length() > 0) {
						sbField.append(",");
					}
					sbField.append(field.getTargetField()).append("=:").append(field.getTargetField());
				}
			} else if ("sqlProc".equals(field.getSource())) {
				//SQL文或存储过程
				if ("Y".equals(field.getKeyField())) {
					//Key字段
					if (sbCondition.length() > 0) {
						sbCondition.append(" and ");
					}
					sbCondition.append(field.getTargetField())
							.append("=:").append(field.getTargetField());
					continue;
				}
				if ("Y".equals(field.getReplace())) {
					if (sbField.length() > 0) {
						sbField.append(",");
					}
					sbField.append(field.getTargetField()).append("=:").append(field.getTargetField());
				}
			} else if ("script".equals(field.getSource())) {
				//脚本函数
				if ("Y".equals(field.getKeyField())) {
					//Key字段
					if (sbCondition.length() > 0) {
						sbCondition.append(" and ");
					}
					sbCondition.append(field.getTargetField())
							.append("=:").append(field.getTargetField());
					continue;
				}
				if ("Y".equals(field.getReplace())) {
					if (sbField.length() > 0) {
						sbField.append(",");
					}
					sbField.append(field.getTargetField()).append("=:").append(field.getTargetField());
				}
			} else {
				throw new Exception("数据字段的数据来源设定错误！");
			}
		}
		if (sbField.length() <= 0) {
			throw new Exception("没有指定导入的数据字段！");
		}
		if (sbCondition.length() <= 0) {
			throw new Exception("没有指定数据重复用Check字段！");
		}
		String sql = new StringBuffer("")
				.append("update ")
				.append(table.getTable())
				.append(" set ")
				.append(sbField.toString())
				.append(",UpdateUser=:systemUser,UpdateTime=:currentTime where ")
				.append(sbCondition.toString()).toString();
		String checkSql = new StringBuffer("")
				.append("select * from ")
				.append(table.getTable())
				.append(" where ")
				.append(sbCondition.toString()).toString();
		Map<String, String> sqlMap = new HashMap<String, String>();
		sqlMap.put("edit", sql);
		sqlMap.put("check", checkSql);
		return sqlMap;
	}
	/**
	 * 创建SQL参数
	 * @param table
	 * @return
	 * @throws Exception
	 */
	private Map<String, Object> createParameterMap(Sheet sheet, int iRow, Table table,ImportSet importSet, String[] arrData) throws Exception {
		List<MappingField> mappingList = table.getMapping();
		if (mappingList == null || mappingList.size() <= 0) {
			throw new Exception("没有指定导入字段！");
		}
		if ("text".equals(importSet.getType())) {
			boolean allNull = true;
			Map<String, Object> map = new HashMap<String, Object>();
			for (int i = 0; i < mappingList.size(); i++) {
				MappingField field = mappingList.get(i);
				if ("excel".equals(field.getSource())) {
					if ("column".equals(field.getMapping())) {
						//明细列号
						try {
							int col = Integer.parseInt(field.getSourceField()) - 1;
							if (col >= arrData.length) {
								continue;
							}
							if (arrData[col] != null && !"".equals(arrData[col])) {
								allNull = false;
							}
							map.put(field.getTargetField(), arrData[col]);
						} catch (NumberFormatException e) {
							e.printStackTrace();
							continue;
						}
					} else {
						throw new Exception("设定的字段映射方法不正确！");
					}
				} else if ("sqlFunc".equals(field.getSource())) {
					//数据库函数
					if (field.getParameter() == null || field.getParameter().size() <= 0) {
						continue;
					}
					List<Parameter> parameterList = field.getParameter();
					for (int j = 0; j < parameterList.size(); j++) {
						Parameter parameter = parameterList.get(j);
						parseParameter(importSet, parameter, sheet, iRow, map, arrData);
					}
				} else if ("sqlProc".equals(field.getSource())) {
					//SQL文或存储过程
					String sql = URLDecoder.decode(field.getFunc(), "UTF-8");
					Map<String, Object> paramMap = new HashMap<String, Object>();
					if (field.getParameter() != null && field.getParameter().size() > 0) {
						List<Parameter> parameterList = field.getParameter();
						for (int j = 0; j < parameterList.size(); j++) {
							Parameter parameter = parameterList.get(j);
							parseParameter(importSet, parameter, sheet, iRow, paramMap, arrData);
						}
					}
					List<?> dataList = null;
					if ("R".equals(field.getTarget())) {
						dataList = businessSQLDao.select(sql, paramMap);
					} else {
						dataList = sqlDao.select(sql, paramMap);
					}
					Map<String, Object> dataMap = null;
					if (dataList != null && dataList.size() > 0) {
						dataMap = (Map<String, Object>)dataList.get(0);
					} else {
						dataMap = new HashMap<String, Object>();
					}
					Object value = dataMap.get(field.getSourceField());
					map.put(field.getTargetField(), value);
				} else if ("script".equals(field.getSource())) {
					//脚本函数
					String func = URLDecoder.decode(field.getFunc(), "UTF-8");
					Map<String, Object> paramMap = new HashMap<String, Object>();
					if (field.getParameter() != null && field.getParameter().size() > 0) {
						List<Parameter> parameterList = field.getParameter();
						for (int j = 0; j < parameterList.size(); j++) {
							Parameter parameter = parameterList.get(j);
							parseParameter(importSet, parameter, sheet, iRow, paramMap, arrData);
						}
					}
					Object value = ExcelUtils.runScript(func, paramMap);
					map.put(field.getTargetField(), value);
				} else {
					throw new Exception("数据字段的数据来源设定错误！");
				}
			}
			if (allNull) {
				return null;
			}
			return map;
		} else {
			Excel excel = importSet.getExcel();
			boolean allNull = true;
			Map<String, Object> map = new HashMap<String, Object>();
			for (int i = 0; i < mappingList.size(); i++) {
				MappingField field = mappingList.get(i);
				if ("excel".equals(field.getSource())) {
					//来源Excel
					if ("header".equals(field.getMapping())) {
						//表头位置
						Object value = ExcelUtils.getCellDataByName(sheet, field.getSourceField());
						if (value != null && !"".equals(value)) {
							allNull = false;
						}
						map.put(field.getTargetField(), value);
					} else if ("title".equals(field.getMapping())) {
						//明细标题
						int colNo = ExcelUtils.getColNoByTitle(sheet, excel.getTitleStartRowNo(), excel.getTitleEndRowNo(), field.getSourceField());
						if (colNo <= 0) {
							throw new Exception("没有找到标题为（" + field.getSourceField() + "）的列！");
						}
						Object value = ExcelUtils.getCellData(sheet, iRow, colNo);
						if (value != null && !"".equals(value)) {
							allNull = false;
						}
						map.put(field.getTargetField(), value);
					} else if ("column".equals(field.getMapping())) {
						//明细列号
						String cellName = new StringBuffer("")
								.append(field.getSourceField()).append(iRow).toString();
						Object value = ExcelUtils.getCellDataByName(sheet, cellName);
						if (value != null && !"".equals(value)) {
							allNull = false;
						}
						map.put(field.getTargetField(), value);
					} else {
						throw new Exception("设定的字段映射方法不正确！");
					}
				} else if ("sqlFunc".equals(field.getSource())) {
					//数据库函数
					if (field.getParameter() == null || field.getParameter().size() <= 0) {
						continue;
					}
					List<Parameter> parameterList = field.getParameter();
					for (int j = 0; j < parameterList.size(); j++) {
						Parameter parameter = parameterList.get(j);
						parseParameter(importSet, parameter, sheet, iRow, map, arrData);
					}
				} else if ("sqlProc".equals(field.getSource())) {
					//SQL文或存储过程
					String sql = URLDecoder.decode(field.getFunc(), "UTF-8");
					Map<String, Object> paramMap = new HashMap<String, Object>();
					if (field.getParameter() != null && field.getParameter().size() > 0) {
						List<Parameter> parameterList = field.getParameter();
						for (int j = 0; j < parameterList.size(); j++) {
							Parameter parameter = parameterList.get(j);
							parseParameter(importSet, parameter, sheet, iRow, paramMap, arrData);
						}
					}
					List<?> dataList = null;
					if ("R".equals(field.getTarget())) {
						dataList = businessSQLDao.select(sql, paramMap);
					} else {
						dataList = sqlDao.select(sql, paramMap);
					}
					Map<String, Object> dataMap = null;
					if (dataList != null && dataList.size() > 0) {
						dataMap = (Map<String, Object>)dataList.get(0);
					} else {
						dataMap = new HashMap<String, Object>();
					}
					Object value = dataMap.get(field.getSourceField());
					map.put(field.getTargetField(), value);
				} else if ("script".equals(field.getSource())) {
					//脚本函数
					String func = URLDecoder.decode(field.getFunc(), "UTF-8");
					Map<String, Object> paramMap = new HashMap<String, Object>();
					if (field.getParameter() != null && field.getParameter().size() > 0) {
						List<Parameter> parameterList = field.getParameter();
						for (int j = 0; j < parameterList.size(); j++) {
							Parameter parameter = parameterList.get(j);
							parseParameter(importSet, parameter, sheet, iRow, paramMap, arrData);
						}
					}
					Object value = ExcelUtils.runScript(func, paramMap);
					map.put(field.getTargetField(), value);
				} else {
					throw new Exception("数据字段的数据来源设定错误！");
				}
			}
			if (allNull) {
				return null;
			}
			return map;
		}
	}
	/**
	 * 导入文件
	 * @param systemUser
	 * @param fileName
	 * @param in
	 * @param no
	 * @throws Exception
	 */
	public void importData(String systemUser, String fileName, InputStream in, String no) throws Exception {
		try {
			//取得导入设定信息
			ImportPublish importPublish = importPublishDao.getById(no);
			if (importPublish == null) {
				//没有导入设定信息
				throw new Exception("导入设定信息不存在，请和系统管理员联系！");
			}
			String script = importPublish.getScript();
			if (script == null || "".equals(script)) {
				throw new Exception("导入设定信息不正确！");
			}
			Gson gson = new Gson();
			ImportSet importSet = gson.fromJson(script, ImportSet.class);
			if (importSet == null) {
				throw new Exception("导入设定信息不正确！");
			}
			if (!"excel".equals(importSet.getType())) {
				//执行文本文件导入
				importTextData(systemUser, importSet, in, importPublish, fileName);
				return;
			}
			int iPos = fileName.lastIndexOf(".");
			if (iPos < 0) {
				throw new Exception("请选择Excel文件导入！");
			}
			String extendName = fileName.substring(iPos + 1);
			if (!"xls".equals(extendName)  && !"xlsx".equals(extendName)) {
				throw new Exception("请选择Excel文件导入！");
			}
			//读取Excel文件
			Workbook book = WorkbookFactory.create(in);
			Sheet sheet = book.getSheetAt(0);
			List<Table> tableList = importSet.getTable();
			if (tableList == null || tableList.size() <= 0) {
				throw new Exception("没有指定导入的数据表！");
			}
			//导入前存储过程处理
			executeProc(sheet, "BefImport", importSet, -1, null);
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");
			//导入处理
			for (int i = 0; i < tableList.size(); i++) {
				Table table = tableList.get(i);
				//生成Insert用SQL文
				String insertSql = createAddSql(table);
				Map<String, String> sqlMap = createEditSql(table);
				for (int j = importSet.getExcel().getDetailStartRowNo() - 1; j <= sheet.getLastRowNum(); j++) {
					if (importSet.getSpaceRow() != null) {
						try {
							checkSpaceRow(sheet, importSet, j + 1, null);
						} catch (Exception e) {
							if ("error".equals(importSet.getSpaceRow().getMethod())) {
								throw e;
							} else if ("skip".equals(importSet.getSpaceRow().getMethod())) {
								continue;
							}
						}
					}
					//创建SQL参数
					Map<String, Object> paramMap = createParameterMap(sheet, j + 1, table, importSet, null);
					if (paramMap != null && paramMap.size() > 0) {
						//执行每条数据执行前的存储过程
						executeProc(sheet, "BefData", importSet, j + 1, null);
						//取得当前系统时间
						Calendar c = Calendar.getInstance();
						paramMap.put("systemUser", systemUser);
						paramMap.put("currentTime", sdf.format(c.getTime()));
						//读取的Excel该行数据全部为空时，不处理
						if ("add".equals(importPublish.getModel())) {
							//新增模式
							//执行checkSQL
							List<?> dataList = null;
							if ("R".equals(table.getTarget())) {
								dataList = businessSQLDao.select(sqlMap.get("check"), paramMap);
							} else {
								dataList = sqlDao.select(sqlMap.get("check"), paramMap);
							}
							if (dataList == null || dataList.size() <= 0) {
								//执行SQL
								if ("R".equals(table.getTarget())) {
									businessSQLDao.update(insertSql, paramMap);
								} else {
									sqlDao.update(insertSql, paramMap);
								}
							} else {
								///写数据修改日志
								DataLogs dataLog = new DataLogs();
								dataLog.setId(UUID.randomUUID().toString());
								dataLog.setDataTable(table.getTable());
								dataLog.setType("Import");
								dataLog.setProcessor("新增模式，相同Key不处理！");
								dataLog.setBeforeData(gson.toJson(dataList.get(0)));
								dataLog.setAfterData(gson.toJson(paramMap));
								dataLog.setCreateUser(systemUser);
								dataLog.setCreateTime(sdf.format(c.getTime()));
								dataLogDao.add(dataLog);								
							}
						} else if ("replace".equals(importPublish.getModel())) {
							//覆盖模式
							if ("R".equals(table.getTarget())) {
								List<?> dataList = businessSQLDao.select(sqlMap.get("check"), paramMap);
								if (dataList == null || dataList.size() <= 0) {
									businessSQLDao.update(insertSql, paramMap);
								} else {
									businessSQLDao.update(sqlMap.get("edit"), paramMap);
									//写数据修改日志
									DataLogs dataLog = new DataLogs();
									dataLog.setId(UUID.randomUUID().toString());
									dataLog.setDataTable(table.getTable());
									dataLog.setType("Import");
									dataLog.setProcessor("修改");
									dataLog.setBeforeData(gson.toJson(dataList.get(0)));
									dataLog.setAfterData(gson.toJson(paramMap));
									dataLog.setCreateUser(systemUser);
									dataLog.setCreateTime(sdf.format(c.getTime()));
									dataLogDao.add(dataLog);
								}
							} else {
								List<?> dataList = sqlDao.select(sqlMap.get("check"), paramMap);
								if (dataList == null || dataList.size() <= 0) {
									sqlDao.update(insertSql, paramMap);
								} else {
									sqlDao.update(sqlMap.get("edit"), paramMap);
									//写数据修改日志
									DataLogs dataLog = new DataLogs();
									dataLog.setId(UUID.randomUUID().toString());
									dataLog.setDataTable(table.getTable());
									dataLog.setType("Import");
									dataLog.setProcessor("修改");
									dataLog.setBeforeData(gson.toJson(dataList.get(0)));
									dataLog.setAfterData(gson.toJson(paramMap));
									dataLog.setCreateUser(systemUser);
									dataLog.setCreateTime(sdf.format(c.getTime()));
									dataLogDao.add(dataLog);
								}
							}
						}
						//执行每条数据执行后的存储过程
						executeProc(sheet, "BakData", importSet, j + 1, null);
					}					
				}
			}
			//导入后存储过程处理
			executeProc(sheet, "BakImport", importSet, -1, null);
		} catch (Exception e) {
			if (e.getCause() != null) {
				throw new Exception(e.getCause().getMessage());
			}
			throw e;
		}
	}
	/**
	 * 导入文本文件
	 * @param systemUser
	 * @param importSet
	 * @param in
	 * @throws Exception
	 */
	private void importTextData(String systemUser, ImportSet importSet, InputStream in, ImportPublish importPublish, String fileName) throws Exception {
		try {
			Text text = importSet.getText();
			if (text == null) {
				throw new Exception("导入设定不正确！");
			}
			InputStreamReader inputStreamReader = new InputStreamReader(in, text.getEncode());
			BufferedReader reader = new BufferedReader(inputStreamReader);
			String lineText = null;
			String separator = "\t";
			if ("tab".equals(text.getSeparator())) {
				separator = "\t";
			} else if ("comma".equals(text.getSeparator())) {
				separator = ",";
			}
			List<Table> tableList = importSet.getTable();
			if (tableList == null || tableList.size() <= 0) {
				throw new Exception("没有指定导入的数据表！");
			}
			int rowNo = 0;
			Gson gson = new Gson();
			//导入前存储过程处理
			executeProc(null, "BefImport", importSet, -1, null);
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");
			while ((lineText = reader.readLine()) != null) {
				rowNo++;
				String[] arrData = lineText.split(separator);
				if (rowNo < text.getStartRow()) {
					continue;
				}
				if (importSet.getSpaceRow() != null) {
					try {
						checkSpaceRow(null, importSet, rowNo, arrData);
					} catch (Exception e) {
						if ("error".equals(importSet.getSpaceRow().getMethod())) {
							throw e;
						} else if ("skip".equals(importSet.getSpaceRow().getMethod())) {
							continue;
						}
					}
				}
				//导入处理
				for (int i = 0; i < tableList.size(); i++) {
					Table table = tableList.get(i);
					//生成Insert用SQL文
					String insertSql = createAddSql(table);
					Map<String, String> sqlMap = createEditSql(table);
					//创建SQL参数
					Map<String, Object> paramMap = createParameterMap(null, rowNo, table, importSet, arrData);
					if (paramMap != null && paramMap.size() > 0) {
						//执行每条数据执行前的存储过程
						executeProc(null, "BefData", importSet, rowNo, null);
						//取得当前系统时间
						Calendar c = Calendar.getInstance();
						paramMap.put("systemUser", systemUser);
						paramMap.put("currentTime", sdf.format(c.getTime()));
						paramMap.put("fileName", fileName);
						paramMap.put("rowNo", rowNo);
						//读取的Excel该行数据全部为空时，不处理
						if ("add".equals(importPublish.getModel())) {
							//新增模式
							//执行checkSQL
							List<?> dataList = null;
							if ("R".equals(table.getTarget())) {
								dataList = businessSQLDao.select(sqlMap.get("check"), paramMap);
							} else {
								dataList = sqlDao.select(sqlMap.get("check"), paramMap);
							}
							if (dataList == null || dataList.size() <= 0) {
								//执行SQL
								if ("R".equals(table.getTarget())) {
									businessSQLDao.update(insertSql, paramMap);
								} else {
									sqlDao.update(insertSql, paramMap);
								}
							} else {
								///写数据修改日志
								DataLogs dataLog = new DataLogs();
								dataLog.setId(UUID.randomUUID().toString());
								dataLog.setDataTable(table.getTable());
								dataLog.setType("Import");
								dataLog.setProcessor("新增模式，相同Key不处理！");
								dataLog.setBeforeData(gson.toJson(dataList.get(0)));
								dataLog.setAfterData(gson.toJson(paramMap));
								dataLog.setCreateUser(systemUser);
								dataLog.setCreateTime(sdf.format(c.getTime()));
								dataLogDao.add(dataLog);								
							}
						} else if ("replace".equals(importPublish.getModel())) {
							//覆盖模式
							if ("R".equals(table.getTarget())) {
								List<?> dataList = businessSQLDao.select(sqlMap.get("check"), paramMap);
								if (dataList == null || dataList.size() <= 0) {
									businessSQLDao.update(insertSql, paramMap);
								} else {
									businessSQLDao.update(sqlMap.get("edit"), paramMap);
									//写数据修改日志
									DataLogs dataLog = new DataLogs();
									dataLog.setId(UUID.randomUUID().toString());
									dataLog.setDataTable(table.getTable());
									dataLog.setType("Import");
									dataLog.setProcessor("修改");
									dataLog.setBeforeData(gson.toJson(dataList.get(0)));
									dataLog.setAfterData(gson.toJson(paramMap));
									dataLog.setCreateUser(systemUser);
									dataLog.setCreateTime(sdf.format(c.getTime()));
									dataLogDao.add(dataLog);
								}
							} else {
								List<?> dataList = sqlDao.select(sqlMap.get("check"), paramMap);
								if (dataList == null || dataList.size() <= 0) {
									sqlDao.update(insertSql, paramMap);
								} else {
									sqlDao.update(sqlMap.get("edit"), paramMap);
									//写数据修改日志
									DataLogs dataLog = new DataLogs();
									dataLog.setId(UUID.randomUUID().toString());
									dataLog.setDataTable(table.getTable());
									dataLog.setType("Import");
									dataLog.setProcessor("修改");
									dataLog.setBeforeData(gson.toJson(dataList.get(0)));
									dataLog.setAfterData(gson.toJson(paramMap));
									dataLog.setCreateUser(systemUser);
									dataLog.setCreateTime(sdf.format(c.getTime()));
									dataLogDao.add(dataLog);
								}
							}
						}
						//执行每条数据执行后的存储过程
						executeProc(null, "BakData", importSet, rowNo, null);
					}
				}
			}
			//导入后存储过程处理
			executeProc(null, "BakImport", importSet, -1, null);
		} catch (Exception e) {
			if (e.getCause() != null) {
				throw new Exception(e.getCause().getMessage());
			}
			throw e;
		}
	}
}
