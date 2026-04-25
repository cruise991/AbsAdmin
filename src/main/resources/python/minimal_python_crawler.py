import urllib.request
import urllib.error
import json
import time
import os
from datetime import datetime

# 目标URL
TARGET_URL = "https://push2.eastmoney.com/api/qt/clist/get?" \
             "&fid=f62&po=1&pz=500&pn=1&np=1&fltt=2&invt=2" \
             "&ut=8dec03ba335b81bf4ebdf7b29ec27d15" \
             "&fs=m%3A90+t%3A2" \
             "&fields=f12%2Cf14%2Cf2%2Cf3%2Cf62%2Cf184%2Cf66%2Cf69%2Cf72%2Cf75" \
             "%2Cf78%2Cf81%2Cf84%2Cf87%2Cf204%2Cf205%2Cf124%2Cf1%2Cf13"

def log(message):
    """简单日志函数"""
    timestamp = datetime.now().strftime("%Y-%m-%d %H:%M:%S")
    print(f"[{timestamp}] {message}")

def save_data(data, filename=None):
    """
    保存数据到JSON文件
    """
    if filename is None:
        # 生成带有时间戳的文件名
        timestamp = datetime.now().strftime("%Y%m%d_%H%M%S")
        filename = f"eastmoney_data_{timestamp}.json"
    
    try:
        with open(filename, 'w', encoding='utf-8') as f:
            json.dump(data, f, ensure_ascii=False, indent=2)
        log(f"数据已成功保存到文件: {filename}")
        log(f"保存路径: {os.path.abspath(filename)}")
        return True
    except Exception as e:
        log(f"保存文件失败: {str(e)}")
        return False

def parse_and_print_data(data):
    """
    解析并打印数据信息
    """
    log("=== 数据解析结果 ===")
    
    # 打印基本信息
    if 'rc' in data:
        log(f"返回码(rc): {data['rc']}")
    if 'lt' in data:
        log(f"延迟(lt): {data['lt']}")
    if 'total' in data.get('data', {}):
        log(f"总条数(total): {data['data']['total']}")
    
    # 打印数据内容
    if 'diff' in data.get('data', {}):
        stocks = data['data']['diff']
        log(f"获取到 {len(stocks)} 条股票数据")
        
        # 打印字段映射和前几条数据作为示例
        if stocks:
            log("\n字段映射说明:")
            field_mapping = {
                'f12': '股票代码',
                'f14': '股票名称',
                'f2': '最新价',
                'f3': '涨跌幅',
                'f62': '昨收',
                'f184': '换手率',
                'f66': '今开',
                'f69': '最高',
                'f72': '最低',
                'f75': '成交量(手)',
                'f78': '成交额(万)',
                'f81': '内盘',
                'f84': '外盘',
                'f87': '量比',
                'f204': '市场类型',
                'f205': '行业',
                'f124': '状态'
            }
            
            # 打印前5条数据示例
            log("\n前5条股票数据示例:")
            for i, stock in enumerate(stocks[:5]):
                log(f"\n股票 {i+1}:")
                for field, value in stock.items():
                    field_name = field_mapping.get(field, field)
                    log(f"  {field_name}: {value}")

    log("=== 数据解析完成 ===")

def fetch_eastmoney_data():
    """
    获取东方财富API数据，使用成功的请求头配置
    """
    # 使用最简单的请求头，这是测试成功的配置
    headers = {"User-Agent": "Mozilla/5.0"}
    
    log(f"使用成功的请求头: {headers}")
    
    try:
        # 创建请求
        request = urllib.request.Request(TARGET_URL, headers=headers)
        
        # 设置超时时间
        start_time = time.time()
        with urllib.request.urlopen(request, timeout=15) as response:
            end_time = time.time()
            
            log(f"请求成功! 响应状态码: {response.status}")
            log(f"请求耗时: {(end_time - start_time):.2f} 秒")
            
            # 获取响应头信息
            content_type = response.getheader('Content-Type', 'unknown')
            content_length = response.getheader('Content-Length', 'unknown')
            log(f"内容类型: {content_type}")
            log(f"内容长度: {content_length} 字节")
            
            # 读取并解码内容
            content = response.read().decode('utf-8')
            log(f"成功获取响应内容，长度: {len(content)} 字符")
            
            # 解析JSON
            data = json.loads(content)
            log("JSON解析成功!")
            log(f"数据结构: {list(data.keys())}")
            
            # 保存数据
            if save_data(data):
                # 解析并打印数据信息
                parse_and_print_data(data)
            
            return data
            
    except urllib.error.HTTPError as e:
        log(f"HTTP错误: {e.code} - {e.reason}")
        raise
    except urllib.error.URLError as e:
        log(f"URL错误: {str(e)}")
        raise
    except json.JSONDecodeError as e:
        log(f"JSON解析错误: {str(e)}")
        log("原始内容片段:")
        log(content[:500] if 'content' in locals() else "无内容")
        raise
    except Exception as e:
        log(f"未知错误: {str(e)}")
        raise

def main():
    """主函数"""
    log("=== 东方财富API爬虫（已验证可用版本） ===")
    log(f"目标URL: {TARGET_URL}")
    
    try:
        log("开始获取数据...")
        data = fetch_eastmoney_data()
        
        if data:
            log("\n🎉 爬虫执行成功！数据已获取并保存。")
            log("提示：如需获取更多数据，可以修改URL中的pz(每页数量)和pn(页码)参数。")
            
    except KeyboardInterrupt:
        log("爬虫被用户中断")
    except Exception as e:
        log(f"❌ 爬虫执行失败: {str(e)}")
        log("如果后续再次遇到访问限制，可以尝试:")
        log("1. 修改User-Agent")
        log("2. 增加请求间隔时间")
        log("3. 使用代理IP")
    finally:
        log("=== 爬虫执行结束 ===")

if __name__ == "__main__":
    main()