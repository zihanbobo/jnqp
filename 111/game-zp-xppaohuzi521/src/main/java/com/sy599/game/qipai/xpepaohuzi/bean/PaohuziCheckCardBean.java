package com.sy599.game.qipai.xpepaohuzi.bean;

import com.sy599.game.qipai.xpepaohuzi.constant.PaohzCard;
import com.sy599.game.qipai.xpepaohuzi.tool.PaohuziHuLack;
import com.sy599.game.qipai.xpepaohuzi.tool.PaohuziTool;
import com.sy599.game.util.StringUtil;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PaohuziCheckCardBean {
	private int seat;
	// 提
	private boolean ti;
	// 碰
	private boolean peng;
	// 吃
	private boolean chi;
	// 胡
	private boolean hu;
	// 栽/偎
	private boolean zai;
	// 臭偎
	private boolean chouZai;
	// 跑
	private boolean pao;
	//上一次被动操作
	private int autoAction;
	//上一次被动出牌, 碰吃等等操作组成的卡组
	private List<PaohzCard> autoDisList;
	//
	private PaohzCard disCard;
	//操作列表
	private List<Integer> actionList;
	//
	private boolean isMoPaiIng;
	//
	private PaohuziHuLack lack;
	//过掉胡
	private boolean isPassHu;

	public List<Integer> getActionList() {
		return actionList;
	}

	public void setActionList(List<Integer> actionList) {
		this.actionList = actionList;
	}

	public List<PaohzCard> getAutoDisList() {
		return autoDisList;
	}

	public void setAuto(int autoAction, List<PaohzCard> autoDisList) {
		if (this.autoAction == 0) {
			this.autoAction = autoAction;
			this.autoDisList = autoDisList;
		}

	}

	public boolean isTi() {
		return ti;
	}

	public void setTi(boolean ti) {
		this.ti = ti;
	}

	public boolean isPeng() {
		return peng;
	}

	public void setPeng(boolean peng) {
		this.peng = peng;
	}

	public boolean isChi() {
		return chi;
	}

	public void setChi(boolean chi) {
		this.chi = chi;
	}

	public boolean isHu() {
		return hu;
	}

	public void setHu(boolean hu) {
		this.hu = hu;
	}

	public boolean isZai() {
		return zai;
	}

	public void setZai(boolean zai) {
		this.zai = zai;
	}

	public boolean isPao() {
		return pao;
	}

	public void setPao(boolean pao) {
		this.pao = pao;
	}

	// public boolean isWei() {
	// return wei;
	// }
	//
	// public void setWei(boolean wei) {
	// this.wei = wei;
	// }

	public PaohzCard getDisCard() {
		return disCard;
	}

	public void setDisCard(PaohzCard disCard) {
		this.disCard = disCard;
	}

	public int getSeat() {
		return seat;
	}

	public void setSeat(int seat) {
		this.seat = seat;
	}

	/**
	 * 0胡，1碰，2栽，3提，4吃，5跑，6臭栽
	 * @return
	 */
	public List<Integer> buildActionList() {
		int[] arr = new int[7];
		// 0胡，1碰，2栽，3提，4吃，5跑，6臭栽
		if (hu) {
			arr[0] = 1;
		}
		if (peng) {
			arr[1] = 1;
		}
		if (zai) {
			arr[2] = 1;
		}
		if (ti) {
			arr[3] = 1;
		}
		if (chi) {
			arr[4] = 1;
		}
		if (pao) {
			arr[5] = 1;
		}
		if (chouZai) {
			arr[6] = 1;
		}
		List<Integer> list = new ArrayList<>();
		for (int val : arr) {
			list.add(val);
		}
		if (list.contains(1)) {
			actionList = list;
		} else {
			actionList = Collections.EMPTY_LIST;
		}
		return actionList;
	}

	public void setAutoAction(int autoAction) {
		this.autoAction = autoAction;
	}

	public int getAutoAction() {
		return autoAction;
	}

	public boolean isMoPaiIng() {
		return isMoPaiIng;
	}

	public void setMoPaiIng(boolean isMoPaiIng) {
		this.isMoPaiIng = isMoPaiIng;
	}

	public void initAutoDisData(String data) {
		if (!StringUtils.isBlank(data)) {
			int i = 0;
			String[] values = data.split(";");
			seat = StringUtil.getIntValue(values, i++);
			autoAction = StringUtil.getIntValue(values, i++);
			String autoDisStr = StringUtil.getValue(values, i++);
			if (!StringUtils.isBlank(autoDisStr)) {
				this.autoDisList = PaohuziTool.explodePhz(autoDisStr, ",");
			}
			this.isMoPaiIng = StringUtil.getIntValue(values, i++) == 1;
			this.isPassHu = StringUtil.getIntValue(values, i++) == 1;
		}
	}

	public String buildAutoDisStr() {
		StringBuilder sb = new StringBuilder();
		sb.append(seat).append(";");
		sb.append(autoAction).append(";");
		sb.append(PaohuziTool.implodePhz(autoDisList, ",")).append(";");
		sb.append(isMoPaiIng ? 1 : 0).append(";");
		sb.append(isPassHu ? 1 : 0).append(";");
		return sb.toString();
	}

	public PaohuziHuLack getLack() {
		return lack;
	}

	public void setLack(PaohuziHuLack lack) {
		this.lack = lack;
	}

	public boolean isChouZai() {
		return chouZai;
	}

	public void setChouZai(boolean chouZai) {
		this.chouZai = chouZai;
	}
	
	public boolean isPassHu() {
		return isPassHu;
	}

	public void setPassHu(boolean isPassHu) {
		this.isPassHu = isPassHu;
	}

    public static String actionListToString(List<Integer> actionList) {
        if (actionList == null || actionList.size() == 0) {
            return "";
        }
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < actionList.size(); i++) {
            if (actionList.get(i) == 1) {
                switch (i) {
                    case 0:
                        sb.append("hu").append(",");
                        break;
                    case 1:
                        sb.append("peng").append(",");
                        break;
                    case 2:
                        sb.append("zai").append(",");
                        break;
                    case 3:
                        sb.append("ti").append(",");
                        break;
                    case 4:
                        sb.append("chi").append(",");
                        break;
                    case 5:
                        sb.append("pao").append(",");
                        break;
                    case 6:
                        sb.append("chouZai").append(",");
                        break;
                    default:
                        sb.append("未知").append(i).append(",");
                }
            }

        }
        sb.append("]");
        return sb.toString();
    }

}
