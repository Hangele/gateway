package com.gateway.parse.enumtype;

/**
 * 经度类型
 * created by dyy
 */
@SuppressWarnings("all")
public enum LongitudeType {

    EAST("0", "东经"),
    WEST("1", "西经");

    private String binary;
    private String desc;

    LongitudeType(String binary, String desc) {
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

    public static LongitudeType valuesOfChar(char aChar) throws Exception {
        switch (aChar) {
            case (char)48:
                return EAST;
            case (char)49:
                return WEST;
            default:
                throw new Exception("Unknown LatitudeType Char : " + aChar);
        }
    }

    public static LongitudeType valuesOfBinary(String binary) throws Exception {
        switch (binary) {
            case "0":
                return EAST;
            case "1":
                return WEST;
            default:
                throw new Exception("Unknown LatitudeType binary : " + binary);
        }
    }
}
