package cn.lmx.flow.action;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.codec.binary.Base64;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.lmx.flow.bean.module.GroupBean;
import cn.lmx.flow.bean.module.MenuBean;
import cn.lmx.flow.bean.module.ModuleItemBean;
import cn.lmx.flow.bean.module.UserBean;
import cn.lmx.flow.bean.module.UserPermissionBean;
import cn.lmx.flow.service.module.MenuService;
import cn.lmx.flow.service.module.PermissionService;
import cn.lmx.flow.service.module.UserService;
import cn.lmx.flow.utils.RandomNumUtil;
import cn.lmx.flow.utils.UserInfo;

@Controller
@RequestMapping("/user")
public class UserController {
	@Resource(name="UserService")
	private UserService userService;
	@Resource(name="PermissionService")
	private PermissionService permissionService;
	//菜单
	@Resource(name="MenuService")
	private MenuService menuService;
	/**
	 * 生成验证码图片
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value="/randPic", method=RequestMethod.GET)
	public String createStr(HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		try {
			RandomNumUtil rand = RandomNumUtil.Instance();
			ByteArrayInputStream in = rand.getImage();
			session.setAttribute("RandomPic", rand.getString());
			OutputStream out = response.getOutputStream();
			byte[] b = new byte[1024];
			int len = -1;
			while ((len = in.read(b)) != -1) {
				out.write(b, 0, len);
			}
			out.flush();
		} catch (Exception e) {
			
		}
		return "ok";
	}
	/**
	 * 登录画面
	 * @param model
	 * @return
	 */
	@RequestMapping(value="/login", method=RequestMethod.GET)
	public String doGetLogin(Model model) {
		return "login";
	}
	@RequestMapping(value="/login", method=RequestMethod.POST)
	public String doPostLogin(@RequestParam("accountId") String account, @RequestParam("password") String pwd, @RequestParam("randPic") String randPic,
			Model model, HttpSession session) {
		try {
			String randPicSession = String.valueOf(session.getAttribute("RandomPic"));
			if (!randPicSession.equals(randPic)) {
				//验证失败
				model.addAttribute("error", "验证码输入错误！");
				model.addAttribute("randPic", randPic);
				model.addAttribute("accountId", account);
				model.addAttribute("password", pwd);
				return "login";
			}
			UserBean userBean = userService.login(account, pwd);
			if (userBean == null) {
				//登录失败
				model.addAttribute("error", "登录失败！");
				model.addAttribute("accountId", account);
			} else {
				//登录成功
				//保存登录用户
				session.setAttribute(UserBean.class.getName(), userBean);
				UserInfo.put(UserBean.class.getName(), userBean);
				//取得用户权限数据
				List<UserPermissionBean> permissionList = permissionService.getPermission(userBean.getUserNo());
				UserInfo.put(UserPermissionBean.class.getName(), permissionList);
				session.setAttribute(UserPermissionBean.class.getName(), permissionList);
				//取得菜单数据
				List<MenuBean> menuList = menuService.getMenu();
				//去除没有权限的菜单
				if (permissionList == null || permissionList.size() <= 0) {
					menuList = new ArrayList<MenuBean>();
				} else {
					for (int i = 0; i < menuList.size(); i++) {
						MenuBean bean = menuList.get(i);
						List<ModuleItemBean> itemList = bean.getItemList();
						if (itemList == null) {
							menuList.remove(i);
							i--;
							continue;
						}
						for (int j = 0; j < itemList.size(); j++) {
							ModuleItemBean itemBean = itemList.get(j);
							boolean hasPermission = false;
							for (int m = 0; m < permissionList.size(); m++) {
								UserPermissionBean permissionBean = permissionList.get(m);
								if (itemBean.getItemNo().equals(permissionBean.getItemNo())) {
									hasPermission = true;
									break;
								}
							}
							if (hasPermission) {
								continue;
							} else {
								itemList.remove(j);
								j--;
							}
						}
						if (itemList.size() <= 0) {
							menuList.remove(i);
							i--;
						}
					}
				}
				session.setAttribute(MenuBean.class.getName(), menuList);
				//查询数据权限
				//session.setAttribute("DataPrivilege", userService.getUserDataPrivilege(account));
				//return "redirect:/user/index";
				return "redirect:/desktop/desktop";
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "login";
	}
	/**
	 * 初始画面
	 * @return
	 */
	@RequestMapping(value="index")
	public String index(Model model, HttpSession session) {
		model.addAttribute("user", UserInfo.get(UserBean.class.getName()));
		model.addAttribute("permission", UserInfo.get(UserPermissionBean.class.getName()));
		return "user/index";
	}
	/**
	 * 系统退出
	 * @param session
	 * @return
	 */
	@RequestMapping(value="logout")
	public String logout(HttpSession session) {
		UserInfo.remove(UserBean.class.getName());
		UserInfo.remove(UserPermissionBean.class.getName());
		session.removeAttribute(UserBean.class.getName());
		return "redirect:/user/login";
	}
	/**
	 * 用户一栏
	 * @param model
	 * @return
	 */
	@RequestMapping(value="/list")
	public String list(Model model, String userNo, String userName) {
		try {
			Map<String, Object> map = userService.list(userNo, userName);
			if (map == null) {
				throw new Exception("没有用户数据！");
			}
			model.addAttribute("errType", "0");
			model.addAttribute("data", map);
		} catch (Exception e) {
			model.addAttribute("errType", "1");
			model.addAttribute("errMessage", e.getMessage());
		}
		model.addAttribute("userNo", userNo);
		model.addAttribute("userName", userName);
		return "user/list";
	}
	/**
	 * 用户新增画面
	 * @param model
	 * @return
	 */
	@RequestMapping(value="/add")
	public String add(Model model) {
		return "user/edit";
	}
	/**
	 * 用户修改画面
	 * @param userNo
	 * @param model
	 * @return
	 */
	@RequestMapping(value="/edit")
	public String edit(String userNo, Model model) {
		try {
			UserBean userBean = userService.getUserByNo(userNo);
			model.addAttribute("user", userBean);
			model.addAttribute("errType", "0");
		} catch (Exception e) {
			model.addAttribute("errType", "1");
			model.addAttribute("errMessage", e.getMessage());
		}
		return "user/edit";
	}
	/**
	 * 用户保存
	 * @param userBean
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/save")
	public Map<String, Object> save(@RequestBody UserBean userBean) {
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			UserBean user = (UserBean)UserInfo.get(UserBean.class.getName());
			userService.saveUser(user.getUserNo(), userBean);
			map.put("errType", "0");
		} catch (Exception e) {
			map.put("errType", "1");
			map.put("errMessage", e.getMessage());
		}
		return map;
	}
	/**
	 * 重置密码
	 * @param userNo
	 * @param newPassword
	 * @param confirmPassword
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/resetPassword")
	public Map<String, Object> resetPassword(String userNo, String newPassword, String confirmPassword) {
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			UserBean user = (UserBean)UserInfo.get(UserBean.class.getName());
			userService.resetPassword(user.getUserNo(), userNo, newPassword, confirmPassword);
			map.put("errType", "0");
		} catch (Exception e) {
			map.put("errType", "1");
			map.put("errMessage", e.getMessage());
		}
		return map;
	}
	/**
	 * 修改用户密码画面
	 * @param model
	 * @return
	 */
	@RequestMapping(value="editPassword")
	public String editPassword(Model model) {
		return "user/edit_password";
	}
	/**
	 * 修改用户密码
	 * @param newPassword
	 * @param confirmPassword
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/saveNewPassword")
	public Map<String, Object> editPassword(String newPassword, String confirmPassword) {
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			UserBean user = (UserBean)UserInfo.get(UserBean.class.getName());
			userService.resetPassword(user.getUserNo(), user.getUserNo(), newPassword, confirmPassword);
			map.put("errType", "0");
		} catch (Exception e) {
			map.put("errType", "1");
			map.put("errMessage", e.getMessage());
		}
		return map;
	}
	/**
	 * 用户删除
	 * @param ids
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value="/delete")
	public Map<String, Object> delete(String ids) {
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			UserBean user = (UserBean)UserInfo.get(UserBean.class.getName());
			userService.delete(user.getUserNo(), ids);
			map.put("errType", "0");
		} catch (Exception e) {
			map.put("errType", "1");
			map.put("errMessage", e.getMessage());
		}
		return map;
	}
	/**
	 * 根据用户编码取得所属用户组
	 * @param userNo
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/getGroup")
	public Map<String, Object> getGroupByUserNo(String userNo) {
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			List<GroupBean> list = userService.getGroupByUserNo(userNo);
			map.put("data", list);
			map.put("errType", "0");
		} catch (Exception e) {
			map.put("errType", "1");
			map.put("errMessage", e.getMessage());
		}
		return map;
	}
	/**
	 * 加入用户组
	 * @param ids
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/addGroup")
	public Map<String, Object> addGroup(String userNo, String groups) {
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			UserBean userBean = (UserBean)UserInfo.get(UserBean.class.getName());
			userService.addGroup(userBean.getUserNo(), userNo, groups);
			map.put("errType", "0");
		} catch (Exception e) {
			map.put("errType", "1");
			map.put("errMessage", e.getMessage());
		}
		return map;
	}
	@RequestMapping(value="/help")
	public void help(HttpServletRequest request, HttpServletResponse response) {
		InputStream in = null;
		BufferedOutputStream out = null;
		try {
			String path = request.getSession().getServletContext().getRealPath("/");
			String fileName = null;
			if (path.endsWith(File.separator)) {
				fileName = new StringBuffer("")
							.append(path)
							.append("help/manual.pdf").toString();
			} else {
				fileName = new StringBuffer("")
							.append(path)
							.append(File.separator)
							.append("help/manual.pdf").toString();
			}
			response.setCharacterEncoding("utf-8");  
			response.setContentType("multipart/form-data");			
			String agent = (String)request.getHeader("USER-AGENT");
			String downloadFileName = null;
            if(agent != null && agent.toLowerCase().indexOf("firefox") > 0)
            {
            	downloadFileName = "=?UTF-8?B?" + (new String(Base64.encodeBase64("苏州智慧工会信息报送平台操作手册.pdf".getBytes("UTF-8")))) + "?=";
            } else {
            	downloadFileName = URLEncoder.encode("苏州智慧工会信息报送平台操作手册.pdf", "UTF-8");
            }
            response.setHeader("Content-Disposition", "attachment;fileName="+ downloadFileName);
			out = new BufferedOutputStream(response.getOutputStream());
			File file = new File(fileName);
			in = new BufferedInputStream(new FileInputStream(file));
			byte[] buf = new byte[1024];
			int len = -1;
			while ((len = in.read(buf)) != -1) {
				out.write(buf, 0, len);
			}
			out.flush();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException ex) {
					ex.printStackTrace();
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
	}
}
