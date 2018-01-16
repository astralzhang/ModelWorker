package cn.lmx.flow.utils;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class DataFilter {
	public static Object filter(Object data, Map<String, List<String>> dataPrivilegeMap) {
		if (data == null) {
			return data;
		}
		if (dataPrivilegeMap == null || dataPrivilegeMap.size() <= 0) {
			return data;
		}
		//当前对象
		if (data instanceof List) {
			//list对象
			filterList((List<?>)data, dataPrivilegeMap);
		} else if (data instanceof Map) {
			//Map对象
			filterMap((Map<?,?>)data, dataPrivilegeMap);
		} else {
			//Object对象
			if (filterObject(data, dataPrivilegeMap)) {
				List<Field> fieldList = getListMapField(data);
				if (fieldList != null && fieldList.size() > 0) {
					for (int i = 0; i < fieldList.size(); i++) {
						Field field = fieldList.get(i);
						if (!field.isAccessible()) {
							field.setAccessible(true);
						}
						try {
							Object object = field.get(data);
							if (object instanceof List) {
								filterList((List<?>)object, dataPrivilegeMap);
							} else if (object instanceof Map) {
								filterMap((Map<?,?>)object, dataPrivilegeMap);
							} else {
								System.out.println("数据错误！");
							}
						} catch (IllegalArgumentException | IllegalAccessException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
				return data;
			} else {
				try {
					return data.getClass().newInstance();
				} catch (InstantiationException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					return null;
				} catch (IllegalAccessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					return null;
				}
			}
		}		
		return data;
	}
	/**
	 * 过滤List数据
	 * @param list
	 * @param dataPrivilegeList
	 */
	private static void filterList(List<?> list, Map<String, List<String>> dataPrivilegeMap) {
		if (list == null || list.size() <= 0) {
			return;
		}
		if (dataPrivilegeMap == null || dataPrivilegeMap.size() <= 0) {
			return;
		}
		for (int i = 0; i < list.size(); i++) {
			Object object = list.get(i);
			if (object instanceof List) {
				//List对象
				List<?> tempList = (List<?>)object;
				filterList(tempList, dataPrivilegeMap);
			} else if (object instanceof Map) {
				//Map对象
				Map<?,?> tempMap = (Map<?, ?>)object;
				if (filterMap(tempMap, dataPrivilegeMap) == false) {
					list.remove(i);
					i--;
				}
			} else {
				//Object
				if (filterObject(object, dataPrivilegeMap) == false) {
					//filter没有通过
					list.remove(i);
					i--;
					continue;
				}
				List<Field> fieldList = getListMapField(object);
				if (fieldList != null) {
					for (int j = 0; j < fieldList.size(); j++) {
						Field field = fieldList.get(j);
						if (field.getType().equals(List.class) || field.getType().equals(ArrayList.class)) {
							if (!field.isAccessible()) {
								field.setAccessible(true);
							}
							try {
								List<?> tempList = (List<?>)field.get(object);
								filterList(tempList, dataPrivilegeMap);
							} catch (IllegalArgumentException e) {
								// TODO Auto-generated catch block
								//e.printStackTrace();
							} catch (IllegalAccessException e) {
								// TODO Auto-generated catch block
								//e.printStackTrace();
							}
						} else if (field.getType().equals(Map.class) || field.getType().equals(HashMap.class)) {
							//map对象
							if (!field.isAccessible()) {
								field.setAccessible(true);
							}
							try {
								Map<?,?> tempMap = (Map<?, ?>)field.get(object);
								if (filterMap(tempMap, dataPrivilegeMap) == false) {
									//Map对象不满足条件
									field.set(object, new HashMap<Object,Object>());
								}
							} catch (IllegalArgumentException e) {
								// TODO Auto-generated catch block
								//e.printStackTrace();
							} catch (IllegalAccessException e) {
								// TODO Auto-generated catch block
								//e.printStackTrace();
							}
						}
					}
				}
			}
			
		}
	}
	/**
	 * 过滤Map数据
	 * @param map
	 * @param dataPrivilegeList
	 */
	private static boolean filterMap(Map<?,?> map, Map<String, List<String>> dataPrivilegeMap) {
		if (map == null || map.size() <= 0) {
			return true;
		}
		if (dataPrivilegeMap == null || dataPrivilegeMap.size() <= 0) {
			return true;
		}
		Iterator<Entry<String, List<String>>> it = dataPrivilegeMap.entrySet().iterator();
		while (it.hasNext()) {
			Entry<String, List<String>> entry = it.next();
			Object obj = map.get(entry.getKey());
			if (obj == null) {
				continue;
			}
			if (obj instanceof List) {
				continue;
			}
			if (obj instanceof Map) {
				continue;
			}
			if (entry.getValue() == null || entry.getValue().size() <= 0) {
				//没有该数据对应的权限设定
				return false;
			}
			
			boolean result = false;
			List<String> privList = entry.getValue();
			for (int j = 0; j < privList.size(); j++) {
				String priv = privList.get(j);
				if (priv == null || "".equals(priv)) {
					continue;
				}
				if (priv.trim().equals(obj.toString().trim())) {
					result = true;
					break;
				}
			}
			if (result == false) {
				map = new HashMap();
				return false;
			}
		}
		Iterator it2 = map.entrySet().iterator();
		ArrayList<Object> removeList = new ArrayList<Object>();
		while (it2.hasNext()) {
			Entry<?,?> entry = (Entry<?, ?>)it2.next();
			Object objKey = entry.getKey();
			Object objValue = entry.getValue();
			if (objValue instanceof List) {
				filterList((List<?>)objValue, dataPrivilegeMap);
			} else if (objValue instanceof Map) {
				if (filterMap((Map<?,?>)objValue, dataPrivilegeMap) == false) {
					removeList.add(objKey);
				}
			} else {
				if (filterObject(objValue, dataPrivilegeMap) == false) {
					removeList.add(objKey);
				}
			}
		}
		if (removeList != null && removeList.size() > 0) {
			for (int i = 0; i < removeList.size(); i++) {
				map.remove(removeList.get(i));
			}
		}
		return true;
	}
	/**
	 * 过滤非List、Map数据
	 * @param data
	 * @param dataPrivilegeList
	 * @return
	 */
	private static boolean filterObject(Object data, Map<String, List<String>> dataPrivilegeMap) {
		if (data == null) {
			return true;
		}
		if (data instanceof List) {
			return true;
		}
		if (data instanceof Map) {
			return true;
		}
		if (dataPrivilegeMap == null || dataPrivilegeMap.size() <= 0) {
			//没有数据权限数据
			return false;
		}
		Iterator<Entry<String, List<String>>> it = dataPrivilegeMap.entrySet().iterator();
		while (it.hasNext()) {
			Entry<String, List<String>> entry = it.next();
			Field field = getFieldByName(data.getClass(), entry.getKey());
			if (field == null) {
				continue;
			}
			if (entry.getValue() == null || entry.getValue().size() <= 0) {
				//没有该数据的对应权限设定
				return false;
			}
			if (!field.isAccessible()) {
				field.setAccessible(true);
			}
			if (equalData(data, field, entry.getValue()) == false) {
				return false;
			}
		}
		return true;
	}
	/**
	 * 取得所有的List字段
	 * 
	 * @param data
	 * @return
	 */
	private static List<Field> getListMapField(Object data) {
		if (data == null) {
			return null;
		}
		List<Field> list = new ArrayList<Field>();
		getListMapField(data, list);
		return list;
	}
	/**
	 * 指定字段名称取得字段
	 * @param objClass
	 * @param fieldName
	 * @return
	 */
	private static Field getFieldByName(Class<?> objClass, String fieldName) {
		if (objClass == null || fieldName == null || "".equals(fieldName)) {
			return null;
		}
		while (!Object.class.equals(objClass)) {
			try {
				Field field = objClass.getDeclaredField(fieldName);
				if (field != null) {
					return field;
				}
			} catch (NoSuchFieldException e) {
				// TODO Auto-generated catch block
				//e.printStackTrace();
			} catch (SecurityException e) {
				// TODO Auto-generated catch block
				//e.printStackTrace();
			}
			objClass = objClass.getSuperclass();
		}
		return null;
	}
	/**
	 * 取得所有的List字段
	 * 
	 * @param data
	 * @param list
	 */
	private static void getListMapField(Object data, List<Field> list) {
		if (data == null) {
			return;
		}
		if (Object.class.equals(data.getClass())) {
			return;
		}
		Class<?> objClass = data.getClass();
		while (!objClass.equals(Object.class)) {
			Field[] fields = objClass.getDeclaredFields();
			for (int i = 0; i < fields.length; i++) {
				Field field = fields[i];
				if (field.getType().equals(List.class) || field.getType().equals(ArrayList.class)) {
					list.add(field);
				} else if (field.getType().equals(Map.class) || field.getType().equals(HashMap.class)) {
					list.add(field);
				}
			}
			objClass = objClass.getSuperclass();
		}
	}
	/**
	 * 比较数据
	 * @param data
	 * @param field
	 * @param compData
	 * @return
	 */
	private static boolean equalData(Object data, Field field, List<String> compData) {
		if (data == null || field == null || compData == null) {
			return true;
		}
		if (compData == null || compData.size() <= 0) {
			return false;
		}
		try {
			if (!field.isAccessible()) {
				field.setAccessible(true);
			}
			if (String.class.equals(field.getType())) {
				// 字符串类型
				String value = field.get(data) == null ? null : String.valueOf(field.get(data)).trim();
				if (value == null) {
					return true;
				}
				for (int i = 0; i < compData.size(); i++) {
					String tempValue = compData.get(i);
					if (tempValue == null) {
						continue;
					}
					if (value.equals(tempValue.trim())) {
						return true;
					}
				}
				return false;
			} else if (BigDecimal.class.equals(field.getType())) {
				//BigDecimal类型
				Object obj = field.get(data);
				if (obj == null) {
					return false;
				}
				BigDecimal value = new BigDecimal(String.valueOf(obj).trim());
				for (int i = 0; i < compData.size(); i++) {
					String tempValue = compData.get(i);
					if (tempValue == null) {
						continue;
					}
					if (value.compareTo(new BigDecimal(tempValue)) == 0) {
						return true;
					}
				}
				return false;
			} else if (Integer.class.equals(field.getType())) {
				//int
				int value = field.getInt(data);
				for (int i = 0; i < compData.size(); i++) {
					String tempValue = compData.get(i);
					if (tempValue == null) {
						continue;
					}
					if (value == Integer.parseInt(tempValue)) {
						return true;
					}
				}
			} else if (Long.class.equals(field.getType())) {
				//Long
				long value = field.getLong(data);
				for (int i = 0; i < compData.size(); i++) {
					String tempValue = compData.get(i);
					if (tempValue == null) {
						continue;
					}
					if (value == Long.parseLong(tempValue)) {
						return true;
					}
				}
				return false;
			} else if (Short.class.equals(field.getType())) {
				//Short
				short value = field.getShort(data);
				for (int i = 0; i < compData.size(); i++) {
					String tempValue = compData.get(i);
					if (tempValue == null) {
						continue;
					}
					if (value == Short.parseShort(tempValue)) {
						return true;
					}
				}
				return false;
			} else if (Double.class.equals(field.getType())) {
				//Double
				double value = field.getDouble(data);
				for (int i = 0; i < compData.size(); i++) {
					String tempValue = compData.get(i);
					if (tempValue == null) {
						continue;
					}
					if (Double.compare(value, Double.parseDouble(tempValue)) == 0) {
						return true;
					}
				}
				return false;
			} else if (Float.class.equals(field.getType())) {
				//Float
				float value = field.getFloat(data);
				for (int i = 0; i < compData.size(); i++) {
					String tempValue = compData.get(i);
					if (tempValue == null) {
						continue;
					}
					if (Float.compare(value, Float.parseFloat(tempValue)) == 0) {
						return true;
					}
				}
				return false;
			} else if (Byte.class.equals(field.getType())) {
				byte value = field.getByte(data);
				for (int i = 0; i < compData.size(); i++) {
					String tempData = compData.get(i);
					if (tempData == null || tempData.length() <= 0) {
						continue;
					}
					char tempChar = tempData.charAt(0);
					if (value == tempChar) {
						return true;
					}
				}
				return false;
			} else if (Character.class.equals(field.getType())) {
				char value = field.getChar(data);
				for (int i = 0; i < compData.size(); i++) {
					String tempData = compData.get(i);
					if (tempData == null || tempData.length() <= 0) {
						continue;
					}
					char tempChar = tempData.charAt(0);
					if (value == tempChar) {
						return true;
					}
				}
				return false;
			} else {
				Object value = field.get(data);
				if (value == null) {
					return false;
				}
				for (int i = 0; i < compData.size(); i++) {
					String tempValue = compData.get(i);
					if (tempValue == null) {
						continue;
					}
					if (value.toString().trim().equals(tempValue)) {
						return true;
					}
				}
				return false;
			}
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			return false;
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			return false;
		}
		return false;
	}
}
