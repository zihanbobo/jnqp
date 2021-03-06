package com.sy599.game.qipai.ldsphz.util;

import java.util.*;

public class LdsPhzLinkCard {
    private LdsPhzCardMsg current;
    private List<Integer> rest;
    private LdsPhzLinkCard pre;
    private List<LdsPhzCardMessage> cardMessageList = new ArrayList<>(8);
    private int huxi = 0;
    private Set<LdsPhzFanEnums> fans = new HashSet<>();
    private List<List<Integer>> yjhList;
    private List<List<Integer>> linkList;
    private String valStr;
    private int huCard;
    private int totalTun;

    public int getHuCard() {
        return huCard;
    }

    public void setHuCard(int huCard) {
        this.huCard = huCard;
    }

    public int getTotalTun() {
        return totalTun;
    }

    public void setTotalTun(int totalTun) {
        this.totalTun = totalTun;
    }

    public List<LdsPhzCardMessage> getCardMessageList() {
        return cardMessageList;
    }

    public List<List<Integer>> getYjhList() {
        return yjhList;
    }

    public void setYjhList(List<List<Integer>> yjhList) {
        this.yjhList = yjhList;
    }

    public Set<LdsPhzFanEnums> getFans() {
        return fans;
    }

    public void addFan(LdsPhzFanEnums fan) {
        fans.add(fan);
    }

    public int getHuxi() {
        return huxi;
    }

    public void setHuxi(int huxi) {
        this.huxi = huxi;
    }

    public LdsPhzCardMsg getCurrent() {
        return current;
    }

    public void setCurrent(LdsPhzCardMsg current) {
        this.current = current;
    }

    public List<Integer> getRest() {
        return rest;
    }

    public void setRest(List<Integer> rest) {
        this.rest = rest;
    }

    public LdsPhzLinkCard getPre() {
        return pre;
    }

    public void setPre(LdsPhzLinkCard pre) {
        this.pre = pre;
    }

    public int loadHuxi() {
        int total = 0;
        for (LdsPhzCardMessage cm : cardMessageList) {
            total += cm.loadHuxi();
        }
        return total;
    }

    public String loadValStr() {
        init();
        return valStr;
    }

    private void init() {
        if (valStr == null) {
            synchronized (this) {
                if (valStr == null) {

                    if (current != null || pre != null) {
                        List<List<Integer>> list = new ArrayList<>(8);
                        List<LdsPhzCardMsg> cmList = new ArrayList<>(8);
                        if (current != null) {
                            cmList.add(current);
                            if (current.getValStr().length() > 0) {
                                list.add(new ArrayList<>(current.getIds()));
                            }
                        }

                        LdsPhzLinkCard pre0 = pre;
                        while (pre0 != null) {
                            if (pre0.getCurrent() != null) {
                                cmList.add(pre0.getCurrent());
                                if (pre0.getCurrent().getValStr().length() > 0) {
                                    list.add(new ArrayList<>(pre0.getCurrent().getIds()));
                                }
                            }
                            pre0 = pre0.pre;
                        }

                        Collections.sort(list, new Comparator<List<Integer>>() {
                            @Override
                            public int compare(List<Integer> o1, List<Integer> o2) {
                                int id1 = o1.get(0);
                                int id2 = o2.get(0);
                                int val1 = LdsPhzCardUtils.loadCardVal(id1);
                                int val2 = LdsPhzCardUtils.loadCardVal(id2);
//                if (LdsPhzCardUtils.bigCard(id1)) {
//                    val1 -= 100;
//                }
//                if (LdsPhzCardUtils.bigCard(id2)) {
//                    val2 -= 100;
//                }
                                int val = val1 - val2;
                                return val != 0 ? val : (id1 - id2);
                            }
                        });

                        Collections.sort(cmList, new Comparator<LdsPhzCardMsg>() {
                            @Override
                            public int compare(LdsPhzCardMsg o1, LdsPhzCardMsg o2) {
                                int id1 = o1.getIds().get(0);
                                int id2 = o2.getIds().get(0);
                                int val1 = o1.getVals().get(0);
                                int val2 = o2.getVals().get(0);
//                if (LdsPhzCardUtils.bigCard(id1)) {
//                    val1 -= 100;
//                }
//                if (LdsPhzCardUtils.bigCard(id2)) {
//                    val2 -= 100;
//                }
                                int val = val1 - val2;
                                return val != 0 ? val : (id1 - id2);
                            }
                        });

                        StringBuilder stringBuilder = new StringBuilder(80);
                        for (LdsPhzCardMsg cm : cmList) {
                            stringBuilder.append(cm.getValStr()).append(";");
                        }

                        if (rest != null && rest.size() > 0) {
                            for (Integer integer : rest) {
                                stringBuilder.append(",").append(LdsPhzCardUtils.loadCardVal(integer));
                            }
                        }

                        valStr = stringBuilder.toString();

                        linkList = list;
                    } else if (cardMessageList != null) {
                        Collections.sort(cardMessageList, new Comparator<LdsPhzCardMessage>() {
                            @Override
                            public int compare(LdsPhzCardMessage o1, LdsPhzCardMessage o2) {
                                int size = o2.getCards().size() - o1.getCards().size();
                                if (size != 0) {
                                    return size;
                                }
                                int id1 = o1.getCards().get(0);
                                int id2 = o2.getCards().get(0);
                                int val1 = LdsPhzCardUtils.loadCardVal(id1);
                                int val2 = LdsPhzCardUtils.loadCardVal(id2);
//                if (LdsPhzCardUtils.bigCard(id1)) {
//                    val1 -= 100;
//                }
//                if (LdsPhzCardUtils.bigCard(id2)) {
//                    val2 -= 100;
//                }
                                int val = val2 - val1;
                                return val != 0 ? val : (id2 - id1);
                            }
                        });

                        StringBuilder stringBuilder = new StringBuilder(80);
                        linkList = new ArrayList<>(cardMessageList.size());
                        for (LdsPhzCardMessage yjh : cardMessageList) {
                            for (Integer id : yjh.getCards()) {
                                int val = LdsPhzCardUtils.loadCardVal(id);
                                stringBuilder.append(",").append(val);
                            }
                            stringBuilder.append(";");
                            linkList.add(yjh.getCards());
                        }

                        valStr = stringBuilder.append(huxi).append("_").append(LdsPhzHuPaiUtils.loadMaxFanCount(fans)).toString();

                    } else if (yjhList != null) {
                        Collections.sort(yjhList, new Comparator<List<Integer>>() {
                            @Override
                            public int compare(List<Integer> o1, List<Integer> o2) {
                                int id1 = o1.get(0);
                                int id2 = o2.get(0);
                                int val1 = LdsPhzCardUtils.loadCardVal(id1);
                                int val2 = LdsPhzCardUtils.loadCardVal(id2);
//                if (LdsPhzCardUtils.bigCard(id1)) {
//                    val1 -= 100;
//                }
//                if (LdsPhzCardUtils.bigCard(id2)) {
//                    val2 -= 100;
//                }
                                int val = val1 - val2;
                                return val != 0 ? val : (id1 - id2);
                            }
                        });

                        StringBuilder stringBuilder = new StringBuilder(80);
                        for (List<Integer> yjh : yjhList) {
                            for (Integer id : yjh) {
                                int val = LdsPhzCardUtils.loadCardVal(id);
                                stringBuilder.append(",").append(val);
                            }
                            stringBuilder.append(";");
                        }

                        valStr = stringBuilder.toString();

                        linkList = yjhList;
                    } else {
                        StringBuilder stringBuilder = new StringBuilder(80);

                        if (rest != null && rest.size() > 0) {
                            for (Integer integer : rest) {
                                stringBuilder.append(",").append(LdsPhzCardUtils.loadCardVal(integer));
                            }
                        }

                        valStr = stringBuilder.toString();
                        linkList = new ArrayList<>(8);
                    }
                }
            }
        }
    }

    public String appendValStr(){
        valStr = new StringBuilder(valStr).append(huxi).append("_").append(LdsPhzHuPaiUtils.loadMaxFanCount(fans)).toString();
        return valStr;
    }

    public List<List<Integer>> loadList(boolean containRest) {
        init();
        List<List<Integer>> list = new ArrayList<>(8);
        if (linkList != null) {
            for (List<Integer> temp : linkList) {
                list.add(new ArrayList<>(temp));
            }
        }

        if (containRest && rest != null && rest.size() > 0) {
            list.add(new ArrayList<>(rest));
        }

        return list;
    }

    @Override
    public String toString() {
        return loadList(true).toString().replace(" ", "");
    }

    @Override
    public int hashCode() {
        return loadValStr().hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        } else if (obj instanceof LdsPhzLinkCard)
            return loadValStr().equals(((LdsPhzLinkCard) obj).loadValStr());
        else
            return false;
    }

    public void setCardMessageList(List<LdsPhzCardMessage> cardMessageList) {
        this.cardMessageList = cardMessageList;
    }

}
