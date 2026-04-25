package com.abs.system.util;

import java.util.HashMap;
import java.util.Map;

public class BaseBean {

	private final Map<String, Object> fieldMap;

	public BaseBean() {
		this.fieldMap = new HashMap<String, Object>();
		this.fieldMap.clear();
	}

	public BaseBean(Map<String, Object> fieldMap) {
		this.fieldMap = fieldMap;
	}

	public Map<String, Object> getFieldMap() {
		return this.fieldMap;
	}

	public Object get(String name) {
		if (this.fieldMap == null)
			return null;
		return this.fieldMap.get(name);
	}

	public String getString(String name) {
		return get(name) == null ? "" : get(name).toString();
	}

	public Integer getInteger(String name) {
		Object value = get(name);
		if (value == null) {
			return null;
		}
		if (value instanceof Integer) {
			return (Integer) value;
		}
		if (value instanceof String) {
			try {
				return Integer.parseInt((String) value);
			} catch (NumberFormatException e) {
				return null;
			}
		}
		if (value instanceof Number) {
			return ((Number) value).intValue();
		}
		return null;
	}

	public Integer getInteger(String name, Integer defaultValue) {
		Integer value = getInteger(name);
		return value == null ? defaultValue : value;
	}

	public int getInt(String name) {
		Integer value = getInteger(name);
		return value == null ? 0 : value;
	}

	public int getInt(String name, int defaultValue) {
		Integer value = getInteger(name);
		return value == null ? defaultValue : value;
	}

	public Long getLong(String name) {
		Object value = get(name);
		if (value == null) {
			return null;
		}
		if (value instanceof Long) {
			return (Long) value;
		}
		if (value instanceof String) {
			try {
				return Long.parseLong((String) value);
			} catch (NumberFormatException e) {
				return null;
			}
		}
		if (value instanceof Number) {
			return ((Number) value).longValue();
		}
		return null;
	}

	public Double getDouble(String name) {
		Object value = get(name);
		if (value == null) {
			return null;
		}
		if (value instanceof Double) {
			return (Double) value;
		}
		if (value instanceof String) {
			try {
				return Double.parseDouble((String) value);
			} catch (NumberFormatException e) {
				return null;
			}
		}
		if (value instanceof Number) {
			return ((Number) value).doubleValue();
		}
		return null;
	}

	public Boolean getBoolean(String name) {
		Object value = get(name);
		if (value == null) {
			return null;
		}
		if (value instanceof Boolean) {
			return (Boolean) value;
		}
		if (value instanceof String) {
			return Boolean.parseBoolean((String) value);
		}
		return null;
	}

	public Map<String, Object> getMapByKeys(String[] keys) {
		Map<String, Object> rtnMap = new HashMap<String, Object>();
		for (String key : keys) {
			rtnMap.put(key, get(key));
		}
		return rtnMap;
	}

	public void put(String name, Object value) {
		this.fieldMap.put(name, value);
	}

	public void setColumn(String name, Object value) {
		this.fieldMap.put(name, value);
	}

	public void remove(String key) {
		this.fieldMap.remove(key);
	}

	public Map<String, Object> getCloneMap() {
		Map<String, Object> map = new HashMap<String, Object>();
		if (this.fieldMap != null) {
			for (String key : this.fieldMap.keySet())
				map.put(key, this.fieldMap.get(key));
		}
		return map;
	}

	public void clear() {
		this.fieldMap.clear();
	}
}