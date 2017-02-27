package com.open.face2facemanager.factory.enumeration;

/**
 * 项目名称：face2facemanager
 * 类描述：
 * 创建人：zhougl
 * 创建时间：2016/7/26 18:38
 * 修改人：zhougl
 * 修改时间：2016/7/26 18:38
 * 修改备注：
 */
public enum ModifyUserInfoEnum {
    MODIFY_NICKNAME(200),MODIFY_PHONE(100),MODIFY_CLAZZREMARKNAME(20),MODIFY_CLAZZNIKENAME(10);
    private int value = 0;
    ModifyUserInfoEnum(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
