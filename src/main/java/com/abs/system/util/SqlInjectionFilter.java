package com.abs.system.util;

public class SqlInjectionFilter {

	/**
	 * 完整的 SQL LIKE 参数转义 防止 SQL 注入和语法错误
	 */
	public static String escapeSqlLikeParam(String input) {
		if (input == null || input.isEmpty()) {
			return "";
		}

		StringBuilder escaped = new StringBuilder();

		for (char c : input.toCharArray()) {
			switch (c) {
			// 1. 转义单引号（最重要的）
			case '\'':
				escaped.append("''");
				break;

			// 2. 转义 MySQL 通配符（如果按字面意思匹配）
			case '%':
				escaped.append("\\%"); // LIKE 中的 % 需要转义
				break;

			case '_':
				escaped.append("\\_"); // LIKE 中的 _ 需要转义
				break;

			// 3. 转义其他可能破坏 SQL 的字符
			case '\\':
				escaped.append("\\\\"); // 转义反斜杠
				break;

			// 4. 移除或替换危险字符
			case ';':
			case '-': // 防止 SQL 注释
				escaped.append(' '); // 替换为空格
				break;

			case '\0': // NULL 字符
				escaped.append(' '); // 替换为空格
				break;

			// 5. 保留其他字符
			default:
				escaped.append(c);
			}
		}

		return escaped.toString();
	}

	/**
	 * 更严格的参数验证和清理
	 */
	public static String sanitizeSqlParam(String input) {
		if (input == null) {
			return "";
		}

		// 1. 去除首尾空格
		String cleaned = input.trim();

		// 2. 长度限制（防止过长的恶意输入）
		if (cleaned.length() > 100) {
			cleaned = cleaned.substring(0, 100);
		}

		// 3. 只允许特定字符（根据业务需求调整）
		// 例如：允许字母、数字、中文、部分符号
		String pattern = "^[a-zA-Z0-9\\u4e00-\\u9fa5\\-_@\\.\\s]+$";
		if (!cleaned.matches(pattern)) {
			// 如果不匹配，只保留允许的字符
			cleaned = cleaned.replaceAll("[^a-zA-Z0-9\\u4e00-\\u9fa5\\-_@\\.\\s]", "");
		}

		// 4. 转义特殊字符
		cleaned = escapeSqlLikeParam(cleaned);

		return cleaned;
	}
}