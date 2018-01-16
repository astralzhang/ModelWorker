package cn.lmx.flow.action.officework;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.binary.Base64;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import cn.lmx.flow.bean.module.UserBean;
import cn.lmx.flow.bean.officework.DocumentAcceptsBean;
import cn.lmx.flow.bean.officework.DocumentAttachmentBean;
import cn.lmx.flow.bean.officework.DocumentBean;
import cn.lmx.flow.entity.officework.DocumentAttachment;
import cn.lmx.flow.entity.officework.Documents;
import cn.lmx.flow.service.officework.TradeDocService;
import cn.lmx.flow.utils.UserInfo;

/**
 * 公文管理
 * @author yujx
 *
 */
@Controller
@RequestMapping("/TradeDoc")
public class TradeDocManager {
	//公文管理
	@Resource(name="TradeDocService")
	private TradeDocService tradeDocService;
	/**
	 * 取得公文信息一栏
	 * @return
	 */
	@RequestMapping(value="/list")
	public String list(Model model) {
		UserBean userBean = (UserBean)UserInfo.get(UserBean.class.getName());
		try {
			Map<String, Object> map = tradeDocService.listBasic(userBean.getUserNo());
			model.addAttribute("data", map);
			model.addAttribute("errType", "0");
		} catch (Exception e) {
			model.addAttribute("errType", "1");
			model.addAttribute("errMessage", e.getMessage());
		}
		return "officework/TradeDocList";
	}
	/**
	 * 查询公文信息
	 * @param map
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/query")
	public Map<String, Object> query(@RequestBody Map<String, Object> map) {
		Map<String, Object> result = new HashMap<String, Object>();
		try {
			UserBean userBean = (UserBean)UserInfo.get(UserBean.class.getName());
			Map<String, Object> dataMap = tradeDocService.listDocument(userBean.getUserNo(), map);
			result.put("errType", "0");
			result.put("data", dataMap.get("data"));
		} catch (Exception e) {
			result.put("errType", "1");
			result.put("errMessage", e.getMessage());
		}
		return result;
	}
	/**
	 * 新增画面
	 * @param model
	 * @return
	 */
	@RequestMapping(value="/add")
	public String add(Model model) {
		try {
			UserBean userBean = (UserBean)UserInfo.get(UserBean.class.getName());
			Map<String, Object> map = tradeDocService.add(userBean.getUserNo());
			model.addAttribute("InfomationTypeList", map.get("InfomationTypeList"));
		} catch (Exception e) {
			model.addAttribute("errType", "0");
			model.addAttribute("errMessage", e.getMessage());
		}
		return "officework/TradeDocEdit";
	}
	/**
	 * 附件上传
	 * @param request
	 * @param importFile
	 * @param id
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/upload")
	public Map<String, Object> upload(HttpServletRequest request, @RequestParam("uploadFile") MultipartFile uploadFile, @RequestParam("id") String id) {
		UserBean userBean = (UserBean)UserInfo.get(UserBean.class.getName());
		Map<String, Object> result = new HashMap<String, Object>();
		try {
			if (id == null || "".equals(id)) {
				id = UUID.randomUUID().toString();
			}
			List<DocumentAttachmentBean> list = new ArrayList<DocumentAttachmentBean>();
			if (request instanceof MultipartHttpServletRequest) {
				Map<String, MultipartFile> map = ((MultipartHttpServletRequest) request).getFileMap();
				Iterator<Entry<String, MultipartFile>> it = map.entrySet().iterator();
				while (it.hasNext()) {
					Entry<String, MultipartFile> entry = it.next();
					MultipartFile file = entry.getValue();
					String fileName = file.getOriginalFilename();
					int lastIndex = fileName.lastIndexOf(".");
					String extendName = "";
					if (lastIndex >= 0) {
						extendName = fileName.substring(lastIndex + 1);
					}
					DocumentAttachmentBean bean = new DocumentAttachmentBean();
					bean.setId(UUID.randomUUID().toString());
					bean.setParentId(id);
					bean.setFileName(fileName);
					bean.setFileType(extendName);
					bean.setFileContent(file.getBytes());
					list.add(bean);
				}
			}
			String path = request.getSession().getServletContext().getRealPath("/");
			String filePath = null;
			if (!path.endsWith(File.separator)) {
				filePath = new StringBuffer("")
							.append(path)
							.append(File.separator)
							.append("attachment")
							.append(File.separator).toString();
			} else {
				filePath = new StringBuffer("")
						.append(path)
						.append("attachment")
						.append(File.separator).toString();
			}
			//上传文件
			List<DocumentAttachmentBean> dataList = tradeDocService.upload(userBean.getUserNo(), filePath, list);
			result.put("data", dataList);
			result.put("id", id);
			result.put("errType", "0");
		} catch (Exception e) {
			result.put("errType", "1");
			result.put("errMessage", e.getMessage());
		}
		return result;
	}
	/**
	 * 附件删除
	 * @param id
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/deleteAtta")
	public Map<String, Object> deleteAttachment(HttpServletRequest request, String id) {
		UserBean userBean = (UserBean)UserInfo.get(UserBean.class.getName());
		Map<String, Object> result = new HashMap<String, Object>();
		try {
			if (id == null || "".equals(id)) {
				result.put("errType", "0");
				return result;
			}
			String path = request.getSession().getServletContext().getRealPath("/");
			String filePath = null;
			if (!path.endsWith(File.separator)) {
				filePath = new StringBuffer("")
							.append(path)
							.append(File.separator)
							.append("attachment")
							.append(File.separator).toString();
			} else {
				filePath = new StringBuffer("")
						.append(path)
						.append("attachment")
						.append(File.separator).toString();
			}
			tradeDocService.deleteAttachment(userBean.getUserNo(), filePath, id);
			result.put("errType", "0");
		} catch (Exception e) {
			result.put("errType", "1");
			result.put("errMessage", e.getMessage());
		}
		return result;
	}
	/**
	 * 附件下载
	 * @param no
	 * @param id
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/download")
	public Object viewFile(String id, HttpServletRequest request, HttpServletResponse response) {
		UserBean userBean = (UserBean)UserInfo.get(UserBean.class.getName());
		Map<String, Object> map = new HashMap<String, Object>();
		InputStream in = null;
		BufferedOutputStream out = null;
		try {
			DocumentAttachment atta = tradeDocService.download(userBean.getUserNo(), id);
			out = new BufferedOutputStream(response.getOutputStream());
			response.setContentType("application/x-download");
			String agent = (String)request.getHeader("USER-AGENT");
			String downloadFileName = String.valueOf(map.get("fileName"));
            if(agent != null && agent.toLowerCase().indexOf("firefox") > 0)
            {
            	downloadFileName = "=?UTF-8?B?" + (new String(Base64.encodeBase64(String.valueOf(atta.getFileName()).getBytes("UTF-8")))) + "?=";
            } else {
            	downloadFileName = URLEncoder.encode(String.valueOf(atta.getFileName()), "UTF-8");
            }
            response.setHeader("Content-Disposition", "attachment;fileName="+ downloadFileName);
            String path = request.getSession().getServletContext().getRealPath("/");
            String fileName = null;
            if (atta.getFileType() == null || "".equals(atta.getFileType())) {
            	fileName = new StringBuffer("")
            				.append(atta.getFileRealName()).toString();
            } else {
            	fileName = new StringBuffer("")
            				.append(atta.getFileRealName())
            				.append(".")
            				.append(atta.getFileType()).toString();
            }
			String filePath = null;
			if (!path.endsWith(File.separator)) {
				filePath = new StringBuffer("")
							.append(path)
							.append(File.separator)
							.append("attachment")
							.append(File.separator)
							.append(fileName).toString();
			} else {
				filePath = new StringBuffer("")
						.append(path)
						.append("attachment")
						.append(File.separator)
						.append(fileName).toString();
			}
			File file = new File(filePath);
			in = new BufferedInputStream(new FileInputStream(file));
			byte[] bData = new byte[1024];
			int byteReaded = 0;
			while ((byteReaded = in.read(bData)) > 0) {
				out.write(bData, 0, byteReaded);
			}
			out.flush();
			//out.write(atta.getFileContent());          
		} catch (Exception e) {
			map.put("errType", "1");
			map.put("errMessage", e.getMessage());
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (out != null) {
				try {
					out.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return map;
	}
	/**
	 * 新增画面关闭时调用
	 * @param id
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/close")
	public Map<String, Object> close(HttpServletRequest request, String id) {
		UserBean userBean = (UserBean)UserInfo.get(UserBean.class.getName());
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			if (id == null || "".equals(id)) {
				map.put("errType", "0");
				return map;
			}
			String path = request.getSession().getServletContext().getRealPath("/");
			String filePath = null;
			if (!path.endsWith(File.separator)) {
				filePath = new StringBuffer("")
							.append(path)
							.append(File.separator)
							.append("attachment")
							.append(File.separator).toString();
			} else {
				filePath = new StringBuffer("")
						.append(path)
						.append("attachment")
						.append(File.separator).toString();
			}
			tradeDocService.deleteInvalidAttachment(userBean.getUserNo(), filePath, id);
			map.put("errType", "0");
		} catch (Exception e) {
			map.put("errType", "1");
			map.put("errMessage", e.getMessage());
		}
		return map;
	}
	/**
	 * 保存公文信息
	 * @param bean
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/save")
	public Map<String, Object> save(@RequestBody DocumentBean bean) {
		UserBean userBean = (UserBean)UserInfo.get(UserBean.class.getName());
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			tradeDocService.save(userBean.getUserNo(), bean);
			map.put("errType", "0");
		} catch (Exception e) {
			map.put("errType", "1");
			map.put("errMessage", e.getMessage());
		}
		return map;
	}
	/**
	 * check公文是否可以修改
	 * @param id
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="check")
	public Map<String, Object> check(String id) {
		UserBean userBean = (UserBean)UserInfo.get(UserBean.class.getName());
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			tradeDocService.check(userBean.getUserNo(), id);
			map.put("errType", "0");
		} catch (Exception e) {
			map.put("errType", "1");
			map.put("errMessage", e.getMessage());
		}
		return map;
	}
	/**
	 * 修改画面
	 * @param id
	 * @param model
	 * @return
	 */
	@RequestMapping(value="edit")
	public String edit(String id, Model model) {
		UserBean userBean = (UserBean)UserInfo.get(UserBean.class.getName());
		try {
			Map<String, Object> map = tradeDocService.edit(userBean.getUserNo(), id);
			model.addAttribute("Document", map.get("Document"));
			model.addAttribute("InfomationTypeList", map.get("InfomationTypeList"));
			model.addAttribute("DocumentAttachment", map.get("DocumentAttachment"));
			model.addAttribute("errType", "0");
		} catch (Exception e) {
			model.addAttribute("errType", "1");
			model.addAttribute("errMessage", e.getMessage());
		}
		return "officework/TradeDocEdit";
	}
	
	/**
	 * 公文删除
	 * @param ids
	 * @param request
	 * @param model
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/deleteDoc")
	public Map<String,String> deleteDoc(@RequestParam String ids,HttpServletRequest request, Model model) {
		
		Map<String,String> result = new HashMap<String,String>();
		try{
			if(ids != null && !"".equals(ids.trim())){
				String[] arrID = ids.split(",");
				if (arrID == null || arrID.length <= 0) {
					return result;
				}
				//check是否存在已录用
				for (int i = 0; i < arrID.length; i++) {
					Documents doc = tradeDocService.checkUseNum(arrID[i]);
					if (doc== null){
						result.put("errType", "1");
						result.put("errMessage", "您选择删除的数据不存在！");
						return result;
					}else if("1".equals(doc.getAcceptStatus())) {
						result.put("errType", "1");
						result.put("errMessage", "您选择删除的数据已被采用，不能删除！");
						return result;
					}else if("1".equals(doc.getSubmitStatus())) {
						result.put("errType", "1");
						result.put("errMessage", "您选择删除的数据已上报，不能删除！");
						return result;
					}
				}
				for (int i = 0; i < arrID.length; i++) {
					tradeDocService.deleteDoc(arrID[i]);
				}
				result.put("errType", "0");
				result.put("errMessage", "删除成功");
			}
		}catch(Exception e){
			result.put("errType", "1");
			result.put("errMessage", e.getMessage());
		}
		return result;
	}
	
	/**
	 * 公文上报
	 * @param id
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/submit")
	public Map<String, Object> submit(String id) {
		Map<String, Object> map = new HashMap<String, Object>();
		UserBean userBean = (UserBean)UserInfo.get(UserBean.class.getName());
		try {
			tradeDocService.submit(userBean.getUserNo(), id);
			map.put("errType", "0");
		} catch (Exception e) {
			map.put("errType", "1");
			map.put("errMessage", e.getMessage());
		}
		return map;
	}
	/**
	 * 根据id,获取对应的采用信息
	 * @return
	 */
	@RequestMapping(value="/docUseView")
	public String docUseView(String id,HttpServletRequest request,Model model, HttpServletResponse response) {
		UserBean userBean = (UserBean)UserInfo.get(UserBean.class.getName());
		try {
			Map<String, Object> map = tradeDocService.docUseView(id,userBean.getUserNo());
			model.addAttribute("data", map);
			model.addAttribute("errType", "0");
		} catch (Exception e) {
			model.addAttribute("errType", "1");
			model.addAttribute("errMessage", e.getMessage());
		}
		return "officework/TradeDocUsed";
	}
	
	/**
	 * 保存采用信息
	 */
	@ResponseBody
	@RequestMapping(value = "/saveDocUser", method = RequestMethod.POST)
	public Map<String, Object> saveDocUser(HttpServletRequest request, @RequestBody DocumentAcceptsBean documentAcceptsBean) {
		Map<String, Object> result = new HashMap<String, Object>();
		UserBean userBean = (UserBean)UserInfo.get(UserBean.class.getName());
		try {
			tradeDocService.saveDocUser(documentAcceptsBean,userBean.getUserNo());
			result.put("errType", "0");
			result.put("errMessage", "保存成功");
		} catch (Exception e) {
			result.put("errType", "1");
			result.put("errMessage", e.getMessage());
		}
		return result;
	}
	/**
	 * 取得积分排名
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/pointList")
	public Map<String, Object> pointList(String tradeLevel) {
		Map<String, Object> result = new HashMap<String, Object>();
		UserBean userBean = (UserBean)UserInfo.get(UserBean.class.getName());
		try {
			result = tradeDocService.pointList(tradeLevel);
			result.put("errType", "0");
		} catch (Exception e) {
			result.put("errType", "1");
			result.put("errMessage", e.getMessage());
		}
		return result;
	}
	/**
	 * 驳回处理
	 * @param ids
	 * @param cause
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/return")
	public Map<String, Object> returnDoc(String ids, String cause) {
		Map<String, Object> result = new HashMap<String, Object>();
		UserBean userBean = (UserBean)UserInfo.get(UserBean.class.getName());
		try {
			tradeDocService.documentReturn(userBean.getUserNo(), ids, cause);
			result.put("errType", "0");
		} catch (Exception e) {
			result.put("errType", "1");
			result.put("errMessage", e.getMessage());
		}
		return result;
	}
}
