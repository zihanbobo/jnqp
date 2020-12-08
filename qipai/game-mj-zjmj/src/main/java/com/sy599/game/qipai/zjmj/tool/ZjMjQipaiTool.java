package com.sy599.game.qipai.zjmj.tool;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.sy599.game.qipai.zjmj.rule.ZjMj;
import com.sy599.game.qipai.zjmj.rule.ZjMjIndexArr;

public class ZjMjQipaiTool {
	/**
	 * 麻将转化为majiangIds
	 * 
	 * @param majiangs
	 * @return
	 */
	public static List<Integer> toRepeatMajiangVals(List<ZjMj> majiangs) {
		List<Integer> majiangVals = new ArrayList<>();
		if (majiangs == null) {
			return majiangVals;
		}
		for (ZjMj majiang : majiangs) {
			if (!majiangVals.contains(majiang.getVal())) {
				majiangVals.add(majiang.getVal());

			}
		}
		return majiangVals;
	}
	/**
	 * 检查麻将是否有重复
	 * 
	 * @param majiangs
	 * @return
	 */
	public static boolean isMajiangRepeat(List<ZjMj> majiangs) {
		if (majiangs == null) {
			return false;
		}

		Map<Integer, Integer> map = new HashMap<>();
		for (ZjMj mj : majiangs) {
			int count = 0;
			if (map.containsKey(mj.getId())) {
				count = map.get(mj.getId());
			}
			map.put(mj.getId(), count + 1);
		}
		for (int count : map.values()) {
			if (count > 1) {
				return true;
			}
		}
		return false;
	}
	/**
	 * 麻将转化为majiangIds
	 * 
	 * @param majiangs
	 * @return
	 */
	public static String toMajiangStrs(List<ZjMj> majiangs) {
		StringBuffer sb = new StringBuffer();
		if (majiangs == null) {
			return sb.toString();
		}
		for (ZjMj majiang : majiangs) {
			sb.append(majiang.getId()).append(",");

		}
		if (sb.length() > 0) {
			sb.deleteCharAt(sb.length() - 1);
		}
		return sb.toString();
	}
	/**
	 * 将array组合成用delimiter分隔的字符串
	 * 
	 * @param array
	 * @param delimiter
	 * @return String
	 */
	public static String implodeMajiang(List<ZjMj> array, String delimiter) {
		StringBuilder sb = new StringBuilder("");
		for (ZjMj i : array) {
			sb.append(i.getId());
			sb.append(delimiter);
		}
		if (sb.length() > 0) {
			sb.delete(sb.length() - 1, sb.length());
		}
		return sb.toString();
	}
	/**
	 * 麻将val的个数
	 * 
	 * @param majiangs
	 * @param majiangVal
	 * @return
	 */
	public static int getMajiangCount(List<ZjMj> majiangs, int majiangVal) {
		int count = 0;
		for (ZjMj majiang : majiangs) {
			if (majiang.getVal() == majiangVal) {
				count++;
			}
		}
		return count;
	}
	/**
	 * 将的个数
	 * 
	 * @param majiangs
	 * @param majiangVal
	 * @return
	 */
	public static int getJiangCount(List<ZjMj> majiangs) {
		int count = 0;
		for (ZjMj majiang : majiangs) {
			if (majiang.isJiang()) {
				count++;
			}
		}
		return count;
	}

	/**
	 * 麻将val的List
	 * 
	 * @param majiangs
	 * @param majiangVal
	 * @return
	 */
	public static List<ZjMj> getMajiangList(List<ZjMj> majiangs, int majiangVal) {
		List<ZjMj> list = new ArrayList<>();
		for (ZjMj majiang : majiangs) {
			if (majiang.getVal() == majiangVal) {
				list.add(majiang);
			}
		}
		return list;
	}
	
	public static int findIndexByVal(List<ZjMj> copy, int val) {
		int index = -1;
		int count = 0;
		for (ZjMj majiang : copy) {
			if (majiang.getVal() == val) {
				index = count;
				break;
			}
			count++;
		}
		return index;
	}
	
	public static Integer findMajiangIdByVal(List<Integer> copy, int val) {
		for (int majiangId : copy) {
			ZjMj majiang = ZjMj.getMajang(majiangId);
			if (majiang.getVal() == val) {
				return majiangId;
			}
		}
		return 0;
	}

	public static ZjMj findMajiangByVal(List<ZjMj> copy, int val) {
		for (ZjMj majiang : copy) {
			if (majiang.getVal() == val) {
				return majiang;
			}
		}
		return null;
	}
	/**
	 * 麻将转化为Map<val,valNum>
	 * 
	 * @param majiangs
	 * @return
	 */
	public static Map<Integer, Integer> toMajiangValMap(List<ZjMj> majiangs) {
		Map<Integer, Integer> majiangIds = new HashMap<>();
		if (majiangs == null) {
			return majiangIds;
		}
		for (ZjMj majiang : majiangs) {

			if (majiangIds.containsKey(majiang.getVal())) {
				majiangIds.put(majiang.getVal(), majiangIds.get(majiang.getVal()) + 1);
			} else {
				majiangIds.put(majiang.getVal(), 1);
			}
		}
		return majiangIds;
	}
	
	public static List<ZjMj> findMajiangByVals(List<ZjMj> majiangs, List<Integer> vals) {
		List<ZjMj> result = new ArrayList<>();
		for (int val : vals) {
			for (ZjMj majiang : majiangs) {
				if (majiang.getVal() == val) {
					result.add(majiang);
					break;
				}
			}
		}
		return result;
	}

	/**
	 * 将array组合成用delimiter分隔的字符串
	 * 
	 * @param array
	 * @param delimiter
	 * @return String
	 */
	public static List<ZjMj> explodeMajiang(String str, String delimiter) {
		List<ZjMj> list = new ArrayList<>();
		if (StringUtils.isBlank(str) || str.equals("null") || str.equals("undefined"))
			return list;
		String strArray[] = str.split(delimiter);

		for (String val : strArray) {
			ZjMj majiang = null;
			if (val.startsWith("mj")) {
				majiang = ZjMj.valueOf(ZjMj.class, val);
			} else {
				Integer intVal = (Integer.valueOf(val));
				if (intVal == 0) {
					continue;
				}
				majiang = ZjMj.getMajang(intVal);
			}
			list.add(majiang);
		}
		return list;
	}

	/**
	 * 麻将转化为majiangIds
	 * 
	 * @param majiangs
	 * @return
	 */
	public static List<Integer> toMajiangIds(List<ZjMj> majiangs) {
		List<Integer> majiangIds = new ArrayList<>();
		if (majiangs == null) {
			return majiangIds;
		}
		for (ZjMj majiang : majiangs) {
			majiangIds.add(majiang.getId());
		}
		return majiangIds;
	}

	/**
	 * 麻将转化为majiangIds
	 * 
	 * @param majiangs
	 * @return
	 */
	public static List<Integer> toMajiangVals(List<ZjMj> majiangs) {
		List<Integer> majiangIds = new ArrayList<>();
		if (majiangs == null) {
			return majiangIds;
		}
		for (ZjMj majiang : majiangs) {
			majiangIds.add(majiang.getVal());
		}
		return majiangIds;
	}

	/**
	 * 麻将Id转化为麻将
	 * 
	 * @param majiangIds
	 * @return
	 */
	public static List<ZjMj> toMajiang(List<Integer> majiangIds) {
		if (majiangIds == null) {
			return new ArrayList<>();
		}
		List<ZjMj> majiangs = new ArrayList<>();
		for (int majiangId : majiangIds) {
			if (majiangId == 0) {
				continue;
			}
			majiangs.add(ZjMj.getMajang(majiangId));
		}
		return majiangs;
	}

	/**
	 * 得到最大相同数
	 * 
	 * @param card_index
	 * @param list
	 */
	public static void getMax(ZjMjIndexArr card_index, List<ZjMj> list) {
		Map<Integer, List<ZjMj>> majiangMap = new HashMap<Integer, List<ZjMj>>();
		for (int i = 0; i < list.size(); i++) {
			ZjMj majiang = list.get(i);
			List<ZjMj> count = null;
			if (majiangMap.containsKey(majiang.getVal())) {
				count = majiangMap.get(majiang.getVal());
			} else {
				count = new ArrayList<>();
				majiangMap.put(majiang.getVal(), count);
			}
			count.add(majiang);
		}
		for (int majiangVal : majiangMap.keySet()) {
			List<ZjMj> majiangList = majiangMap.get(majiangVal);
			switch (majiangList.size()) {
			case 1:
				card_index.addMajiangIndex(0, majiangList, majiangVal);
				break;
			case 2:
				card_index.addMajiangIndex(1, majiangList, majiangVal);
				break;
			case 3:
				card_index.addMajiangIndex(2, majiangList, majiangVal);
				break;
			case 4:
				card_index.addMajiangIndex(3, majiangList, majiangVal);
				break;
			case 5://听牌时加入一张导致5张牌
				card_index.addMajiangIndex(4, majiangList, majiangVal);
				break;	
			}
		}
	}

	/**
	 * 去掉麻将中指定的val
	 * 
	 * @param copy
	 * @return
	 */
	public static List<ZjMj> dropMajiang(List<ZjMj> copy, List<Integer> valList) {
		List<ZjMj> hongzhong = new ArrayList<>();
		Iterator<ZjMj> iterator = copy.iterator();
		while (iterator.hasNext()) {
			ZjMj majiang = iterator.next();
			if (valList.contains(majiang.getVal())) {
				hongzhong.add(majiang);
				iterator.remove();
			}
		}
		return hongzhong;
	}

	/**
	 * 去掉麻将中指定的val
	 * 
	 * @param copy
	 * @return
	 */
	public static List<ZjMj> dropMajiangId(List<Integer> copy, List<Integer> valList) {
		List<ZjMj> hongzhong = new ArrayList<>();
		Iterator<Integer> iterator = copy.iterator();
		while (iterator.hasNext()) {
			Integer majiangId = iterator.next();
			ZjMj majiang = ZjMj.getMajang(majiangId);
			if (valList.contains(majiang.getVal())) {
				hongzhong.add(majiang);
				iterator.remove();
			}
		}
		return hongzhong;
	}

	public static List<Integer> dropHongzhongVal(List<Integer> copy) {
		List<Integer> hongzhong = new ArrayList<>();
		Iterator<Integer> iterator = copy.iterator();
		while (iterator.hasNext()) {
			Integer majiang = iterator.next();
			if (majiang > 200) {
				hongzhong.add(majiang);
				iterator.remove();
			}
		}
		return hongzhong;
	}

	/**
	 * 删掉某个值
	 * 
	 * @param copy
	 * @return
	 */
	public static List<ZjMj> dropVal(List<ZjMj> copy, int val, int count) {
		List<ZjMj> hongzhong = new ArrayList<>();
		Iterator<ZjMj> iterator = copy.iterator();
		int i = 0;
		while (iterator.hasNext()) {
			ZjMj majiang = iterator.next();
			if (majiang.getVal() == val) {
				i++;
				hongzhong.add(majiang);
				iterator.remove();
				if (count == i) {
					break;
				}
			}
		}
		return hongzhong;
	}

	/**
	 * 删掉某个值
	 * 
	 * @param copy
	 * @return
	 */
	public static List<ZjMj> dropVal(List<ZjMj> copy, int val) {
		List<ZjMj> hongzhong = new ArrayList<>();
		Iterator<ZjMj> iterator = copy.iterator();
		while (iterator.hasNext()) {
			ZjMj majiang = iterator.next();
			if (majiang.getVal() == val) {
				hongzhong.add(majiang);
				iterator.remove();
			}
		}
		return hongzhong;
	}

	/**
	 * 得到某个值的麻将
	 * 
	 * @param copy
	 * @return
	 */
	public static List<ZjMj> getVal(List<ZjMj> copy, int val) {
		List<ZjMj> hongzhong = new ArrayList<>();
		Iterator<ZjMj> iterator = copy.iterator();
		while (iterator.hasNext()) {
			ZjMj majiang = iterator.next();
			if (majiang.getVal() == val) {
				hongzhong.add(majiang);
			}
		}
		return hongzhong;
	}

	/**
	 * 相同的麻将
	 * 
	 * @param majiangs
	 *            麻将牌
	 * @param majiang
	 *            麻将
	 * @param num
	 *            想要的数量
	 * @return
	 */
	public static List<ZjMj> getSameMajiang(List<ZjMj> majiangs, ZjMj majiang, int num) {
		List<ZjMj> hongzhong = new ArrayList<>();
		int i = 0;
		for (ZjMj maji : majiangs) {
			if (maji.getVal() == majiang.getVal()) {
				hongzhong.add(maji);
				i++;
			}
			if (i >= num) {
				break;
			}
		}
		return hongzhong;

	}

	/**
	 * 先去某个值
	 * 
	 * @param copy
	 * @return
	 */
	public static List<ZjMj> dropMjId(List<ZjMj> copy, int id) {
		List<ZjMj> hongzhong = new ArrayList<>();
		Iterator<ZjMj> iterator = copy.iterator();
		while (iterator.hasNext()) {
			ZjMj majiang = iterator.next();
			if (majiang.getId() == id) {
				hongzhong.add(majiang);
				iterator.remove();
			}
		}
		return hongzhong;
	}
}
