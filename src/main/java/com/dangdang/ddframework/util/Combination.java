package com.dangdang.ddframework.util;

import java.util.*;

/*
 * 组合类，实现笛卡尔积方法，对多个数组进行组合
 * 例如：[1,2,3]和[4,5,6]
 * 		生成组合为：[1,4]，[1,5]，[1,6]，[2,4]，[2,5]，[2,6]，[3,4]，[3,5]，[3,6]
 */

public class Combination {
	
	/*
	 * 笛卡尔积函数
	 * Args：
	 * 		list：需要进行组合的数组列表
	 *      count：递归用参数，默认应为0，list中需要组合数组的开始位置
	 *      result：结果
	 *      data：递归用参数，用来记录路径
	 */

	public static void Descartes(List<Object[]> list, int count,
			List<Object> result, List<Object> data) {

		List<Object>  temp = data;
		//temp.addAll(data);
		
		// 获取当前数组
		Object[] astr = list.get(count);
		// 循环当前数组
		for (Object item : astr) {
			if (count + 1 < list.size()) {

				// 递归连接其他列表
				temp.add(item);
				Descartes(list, count + 1, result, temp);
				temp.remove(item);
			} else {
				
				List<Object> tempResult = new ArrayList<Object>();
				tempResult.addAll(temp);
				tempResult.add(item);
				
				result.add(tempResult);
			}
		}
	}

}
