package com.gateway.parse.enumtype;

/**
 * 纬度类型
 * created by dyy
 */
@SuppressWarnings("all")
public enum LatitudeType {

    NORTH("0", "北纬"),
    SOUTH("1", "南纬");

    private String binary;
    private String desc;

    LatitudeType(String binary, String desc) {
        this.binary = binary;
        this.desc = desc;
    }

    public String getBinary() {
        return binary;
    }

    public void setBinary(String binary) {
        this.binary = binary;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public static LatitudeType valuesOfChar(char aChar) throws Exception {
        switch (aChar) {
            case (char)48:
                return NORTH;
            case (char)49:
                return SOUTH;
            default:
                throw new Exception("Unknown LatitudeType Char : " + aChar);
        }
    }

    public static LatitudeType valuesOfBinary(String binary) throws Exception {
        switch (binary) {
            case "0":
                return NORTH;
            case "1":
                return SOUTH;
            default:
                throw new Exception("Unknown LatitudeType binary : " + binary);
        }
    }
}
