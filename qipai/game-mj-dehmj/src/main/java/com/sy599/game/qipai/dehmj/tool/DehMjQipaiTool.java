package com.sy599.game.qipai.dehmj.tool;

import org.apache.commons.lang3.StringUtils;

import com.sy599.game.qipai.dehmj.rule.DehMj;
import com.sy599.game.qipai.dehmj.rule.DehMjIndexArr;

import java.util.*;
import java.util.Map.Entry;

public class DehMjQipaiTool {
	/**
	 * 麻将转化为majiangIds
	 * 
	 * @param majiangs
	 * @return
	 */
	public static List<Integer> toRepeatMajiangVals(List<DehMj> majiangs) {
		List<Integer> majiangVals = new ArrayList<>();
		if (majiangs == null) {
			return majiangVals;
		}
		for (DehMj majiang : majiangs) {
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
	public static boolean isMajiangRepeat(List<DehMj> majiangs) {
		if (majiangs == null) {
			return false;
		}

		Map<Integer, Integer> map = new HashMap<>();
		for (DehMj mj : majiangs) {
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
	 * 检查是否全是字牌
	 * 
	 * @param majiangs
	 * @return
	 */
	public static boolean isAllZi(List<DehMj> majiangs) {
		for (DehMj mj : majiangs) {
			if(mj.getId() <109) {
				return false;
			}
		}
		
		return true;
	}
	
	
	/**
	 * 麻将转化为majiangIds
	 * 
	 * @param majiangs
	 * @return
	 */
	public static String toMajiangStrs(List<DehMj> majiangs) {
		StringBuffer sb = new StringBuffer();
		if (majiangs == null) {
			return sb.toString();
		}
		for (DehMj majiang : majiangs) {
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
	public static String implodeMajiang(List<DehMj> array, String delimiter) {
		StringBuilder sb = new StringBuilder("");
		for (DehMj i : array) {
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
	public static int getMajiangCount(List<DehMj> majiangs, int majiangVal) {
		int count = 0;
		for (DehMj majiang : majiangs) {
			if (majiang.getVal() == majiangVal) {
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
	public static List<DehMj> getMajiangList(List<DehMj> majiangs, int majiangVal) {
		List<DehMj> list = new ArrayList<>();
		for (DehMj majiang : majiangs) {
			if (majiang.getVal() == majiangVal) {
				list.add(majiang);
			}
		}
		return list;
	}
	public static Integer findMajiangIdByVal(List<Integer> copy, int val) {
		for (int majiangId : copy) {
			DehMj majiang = DehMj.getMajang(majiangId);
			if (majiang.getVal() == val) {
				return majiangId;
			}
		}
		return 0;
	}

	public static DehMj findMajiangByVal(List<DehMj> copy, int val) {
		for (DehMj majiang : copy) {
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
	public static Map<Integer, Integer> toMajiangValMap(List<DehMj> majiangs) {
		Map<Integer, Integer> majiangIds = new HashMap<>();
		if (majiangs == null) {
			return majiangIds;
		}
		for (DehMj majiang : majiangs) {

			if (majiangIds.containsKey(majiang.getVal())) {
				majiangIds.put(majiang.getVal(), majiangIds.get(majiang.getVal()) + 1);
			} else {
				majiangIds.put(majiang.getVal(), 1);
			}
		}
		return majiangIds;
	}
	
	public static List<DehMj> findMajiangByVals(List<DehMj> majiangs, List<Integer> vals) {
		List<DehMj> result = new ArrayList<>();
		for (int val : vals) {
			for (DehMj majiang : majiangs) {
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
	 * @param str
	 * @param delimiter
	 * @return String
	 */
	public static List<DehMj> explodeMajiang(String str, String delimiter) {
		List<DehMj> list = new ArrayList<>();
		if (StringUtils.isBlank(str) || str.equals("null") || str.equals("undefined"))
			return list;
		String strArray[] = str.split(delimiter);

		for (String val : strArray) {
			DehMj majiang = null;
			if (val.startsWith("mj")) {
				majiang = DehMj.valueOf(DehMj.class, val);
			} else {
				Integer intVal = (Integer.valueOf(val));
				if (intVal == 0) {
					continue;
				}
				majiang = DehMj.getMajang(intVal);
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
	public static List<Integer> toMajiangIds(List<DehMj> majiangs) {
		List<Integer> majiangIds = new ArrayList<>();
		if (majiangs == null) {
			return majiangIds;
		}
		for (DehMj majiang : majiangs) {
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
	public static List<Integer> toMajiangVals(List<DehMj> majiangs) {
		List<Integer> majiangIds = new ArrayList<>();
		if (majiangs == null) {
			return majiangIds;
		}
		for (DehMj majiang : majiangs) {
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
	public static List<DehMj> toMajiang(List<Integer> majiangIds) {
		if (majiangIds == null) {
			return new ArrayList<>();
		}
		List<DehMj> majiangs = new ArrayList<>();
		for (int majiangId : majiangIds) {
			if (majiangId == 0) {
				continue;
			}
			majiangs.add(DehMj.getMajang(majiangId));
		}
		return majiangs;
	}

	/**
	 * 得到最大相同数
	 * 
	 * @param card_index
	 * @param list
	 */
	public static void getMax(DehMjIndexArr card_index, List<DehMj> list) {
		Map<Integer, List<DehMj>> majiangMap = new HashMap<Integer, List<DehMj>>();
		for (int i = 0; i < list.size(); i++) {
			DehMj majiang = list.get(i);
			List<DehMj> count = null;
			if (majiangMap.containsKey(majiang.getVal())) {
				count = majiangMap.get(majiang.getVal());
			} else {
				count = new ArrayList<>();
				majiangMap.put(majiang.getVal(), count);
			}
			count.add(majiang);
		}
		for (int majiangVal : majiangMap.keySet()) {
			List<DehMj> majiangList = majiangMap.get(majiangVal);
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
			}
		}
	}

	/**
	 * 去掉麻将中指定的val
	 * 
	 * @param copy
	 * @return
	 */
	public static List<DehMj> dropMajiang(List<DehMj> copy, List<Integer> valList) {
		List<DehMj> hongzhong = new ArrayList<>();
		Iterator<DehMj> iterator = copy.iterator();
		while (iterator.hasNext()) {
			DehMj majiang = iterator.next();
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
	public static List<DehMj> dropMajiangId(List<Integer> copy, List<Integer> valList) {
		List<DehMj> hongzhong = new ArrayList<>();
		Iterator<Integer> iterator = copy.iterator();
		while (iterator.hasNext()) {
			Integer majiangId = iterator.next();
			DehMj majiang = DehMj.getMajang(majiangId);
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
	public static List<DehMj> dropVal(List<DehMj> copy, int val, int count) {
		List<DehMj> hongzhong = new ArrayList<>();
		Iterator<DehMj> iterator = copy.iterator();
		int i = 0;
		while (iterator.hasNext()) {
			DehMj majiang = iterator.next();
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
	public static List<DehMj> dropVal(List<DehMj> copy, int val) {
		List<DehMj> hongzhong = new ArrayList<>();
		Iterator<DehMj> iterator = copy.iterator();
		while (iterator.hasNext()) {
			DehMj majiang = iterator.next();
			if (majiang.getVal() == val) {
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
	public static void dropVal2(List<DehMj> copy, int val) {
		List<DehMj> hongzhong = new ArrayList<>();
		Iterator<DehMj> iterator = copy.iterator();
		while (iterator.hasNext()) {
			DehMj majiang = iterator.next();
			if (majiang.getVal() == val) {
				hongzhong.add(majiang);
				iterator.remove();
				break;
			}
		}
	}

	/**
	 * 得到某个值的麻将
	 * 
	 * @param copy
	 * @return
	 */
	public static List<DehMj> getVal(List<DehMj> copy, int val) {
		List<DehMj> hongzhong = new ArrayList<>();
		Iterator<DehMj> iterator = copy.iterator();
		while (iterator.hasNext()) {
			DehMj majiang = iterator.next();
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
	public static List<DehMj> getSameMajiang(List<DehMj> majiangs, DehMj majiang, int num) {
		List<DehMj> hongzhong = new ArrayList<>();
		int i = 0;
		for (DehMj maji : majiangs) {
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
	public static List<DehMj> dropMjId(List<DehMj> copy, int id) {
		List<DehMj> hongzhong = new ArrayList<>();
		Iterator<DehMj> iterator = copy.iterator();
		while (iterator.hasNext()) {
			DehMj majiang = iterator.next();
			if (majiang.getId() == id) {
				hongzhong.add(majiang);
				iterator.remove();
			}
		}
		return hongzhong;
	}

	/**
	 * 找出指定val的牌子，每个val找出count张
	 * @param majiangs
	 * @param vals
	 * @param count
	 * @return
	 */
	public static List<DehMj> findMajiangByValsAndCount(List<DehMj> majiangs, List<Integer> vals, int count) {
		List<DehMj> result = new ArrayList<>();
		Map<String, Integer> valCount = new HashMap<>();
		for (int val : vals) {
			for (DehMj majiang : majiangs) {
				if (majiang.getVal() == val) {
					if (valCount.get(val) == null) {
						valCount.put("" + val, 1);
						result.add(majiang);
					} else {
						if (valCount.get(val) < count) {
							valCount.put("" + val, valCount.get("" + val) + 1);
							result.add(majiang);
						}
					}

					break;
				}
			}
		}
		return result;
	}
	
}
