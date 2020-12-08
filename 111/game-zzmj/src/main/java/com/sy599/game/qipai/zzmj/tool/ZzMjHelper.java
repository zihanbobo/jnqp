package com.sy599.game.qipai.zzmj.tool;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.sy599.game.qipai.zzmj.rule.ZzMj;
import org.apache.commons.lang.StringUtils;

public class ZzMjHelper {

    /**
     * 删掉某个值
     *
     * @param copy
     * @return
     */
    public static List<ZzMj> dropVal(List<ZzMj> copy, int val, int count) {
        List<ZzMj> hongzhong = new ArrayList<>();
        Iterator<ZzMj> iterator = copy.iterator();
        int i = 0;
        while (iterator.hasNext()) {
            ZzMj majiang = iterator.next();
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
    public static List<ZzMj> dropVal(List<ZzMj> copy, int val) {
        List<ZzMj> hongzhong = new ArrayList<>();
        Iterator<ZzMj> iterator = copy.iterator();
        while (iterator.hasNext()) {
            ZzMj majiang = iterator.next();
            if (majiang.getVal() == val) {
                hongzhong.add(majiang);
                iterator.remove();
            }
        }
        return hongzhong;
    }

    /**
     * 麻将val的个数
     *
     * @param majiangs
     * @param majiangVal
     * @return
     */
    public static int getMajiangCount(List<ZzMj> majiangs, int majiangVal) {
        int count = 0;
        for (ZzMj majiang : majiangs) {
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
    public static List<ZzMj> getMajiangList(List<ZzMj> majiangs, int majiangVal) {
        List<ZzMj> list = new ArrayList<>();
        for (ZzMj majiang : majiangs) {
            if (majiang.getVal() == majiangVal) {
                list.add(majiang);
            }
        }
        return list;
    }

    /**
     * 检查麻将是否有重复
     *
     * @param majiangs
     * @return
     */
    public static boolean isMajiangRepeat(List<ZzMj> majiangs) {
        if (majiangs == null) {
            return false;
        }

        Map<Integer, Integer> map = new HashMap<>();
        for (ZzMj mj : majiangs) {
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
    public static List<Integer> toMajiangIds(List<ZzMj> majiangs) {
        List<Integer> majiangIds = new ArrayList<>();
        if (majiangs == null) {
            return majiangIds;
        }
        for (ZzMj majiang : majiangs) {
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
    public static String toMajiangStrs(List<ZzMj> majiangs) {
        StringBuffer sb = new StringBuffer();
        if (majiangs == null) {
            return sb.toString();
        }
        for (ZzMj majiang : majiangs) {
            sb.append(majiang.getId()).append(",");

        }
        if (sb.length() > 0) {
            sb.deleteCharAt(sb.length() - 1);
        }
        return sb.toString();
    }

    /**
     * 麻将转化为majiangIds
     *
     * @param majiangs
     * @return
     */
    public static List<Integer> toMajiangVals(List<ZzMj> majiangs) {
        List<Integer> majiangIds = new ArrayList<>();
        if (majiangs == null) {
            return majiangIds;
        }
        for (ZzMj majiang : majiangs) {
            majiangIds.add(majiang.getVal());
        }
        return majiangIds;
    }

    /**
     * 麻将转化为majiangIds
     *
     * @param majiangs
     * @return
     */
    public static List<Integer> toRepeatMajiangVals(List<ZzMj> majiangs) {
        List<Integer> majiangVals = new ArrayList<>();
        if (majiangs == null) {
            return majiangVals;
        }
        for (ZzMj majiang : majiangs) {
            if (!majiangVals.contains(majiang.getVal())) {
                majiangVals.add(majiang.getVal());

            }
        }
        return majiangVals;
    }

    /**
     * 麻将转化为Map<val,valNum>
     *
     * @param majiangs
     * @return
     */
    public static Map<Integer, Integer> toMajiangValMap(List<ZzMj> majiangs) {
        Map<Integer, Integer> majiangIds = new HashMap<>();
        if (majiangs == null) {
            return majiangIds;
        }
        for (ZzMj majiang : majiangs) {
            if (majiangIds.containsKey(majiang.getVal())) {
                majiangIds.put(majiang.getVal(), majiangIds.get(majiang.getVal()) + 1);
            } else {
                majiangIds.put(majiang.getVal(), 1);
            }
        }
        return majiangIds;
    }

    /**
     * 麻将Id转化为麻将
     *
     * @param majiangIds
     * @return
     */
    public static List<ZzMj> toMajiang(List<Integer> majiangIds) {
        if (majiangIds == null) {
            return new ArrayList<>();
        }
        List<ZzMj> majiangs = new ArrayList<>();
        for (int majiangId : majiangIds) {
            if (majiangId == 0) {
                continue;
            }
            majiangs.add(ZzMj.getMajang(majiangId));
        }
        return majiangs;
    }

    /**
     * 将array组合成用delimiter分隔的字符串
     *
     * @param array
     * @param delimiter
     * @return String
     */
    public static List<ZzMj> explodeMajiang(String str, String delimiter) {
        List<ZzMj> list = new ArrayList<>();
        if (StringUtils.isBlank(str) || str.equals("null") || str.equals("undefined"))
            return list;
        String strArray[] = str.split(delimiter);

        for (String val : strArray) {
            ZzMj majiang = null;
            if (val.startsWith("mj")) {
                majiang = ZzMj.valueOf(ZzMj.class, val);
            } else {
                Integer intVal = (Integer.valueOf(val));
                if (intVal == 0) {
                    continue;
                }
                majiang = ZzMj.getMajang(intVal);
            }
            list.add(majiang);
        }
        return list;
    }

    /**
     * 将array组合成用delimiter分隔的字符串
     *
     * @param array
     * @param delimiter
     * @return String
     */
    public static String implodeMajiang(List<ZzMj> array, String delimiter) {
        StringBuilder sb = new StringBuilder("");
        for (ZzMj i : array) {
            sb.append(i.getId());
            sb.append(delimiter);
        }
        if (sb.length() > 0) {
            sb.delete(sb.length() - 1, sb.length());
        }
        return sb.toString();
    }

    /**
     * val数量
     *
     * @param copy
     * @param val
     * @return
     */
    public static int getCountByVal(List<ZzMj> copy, int val) {
        int count = 0;
        for (ZzMj majiang : copy) {
            if (majiang.getVal() == val) {
                count++;
            }
        }
        return count;
    }

    public static Integer findMajiangIdByVal(List<Integer> copy, int val) {
        for (int majiangId : copy) {
            ZzMj majiang = ZzMj.getMajang(majiangId);
            if (majiang.getVal() == val) {
                return majiangId;
            }
        }
        return 0;
    }

    public static ZzMj findMajiangByVal(List<ZzMj> copy, int val) {
        for (ZzMj majiang : copy) {
            if (majiang.getVal() == val) {
                return majiang;
            }
        }
        return null;
    }

    public static List<ZzMj> find(List<Integer> copy, List<Integer> valList) {
        List<ZzMj> pai = new ArrayList<>();
        if (!valList.isEmpty()) {
            for (int zpId : valList) {
                Iterator<Integer> iterator = copy.iterator();
                while (iterator.hasNext()) {
                    int card = iterator.next();
                    ZzMj majiang = ZzMj.getMajang(card);
                    if (majiang.getVal() == zpId) {
                        pai.add(majiang);
                        iterator.remove();
                        break;
                    }
                }
            }

        }
        return pai;
    }

}
