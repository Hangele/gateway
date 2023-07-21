package com.gateway.parse.entity;

import com.gateway.parse.base.IStatus;
import com.gateway.parse.constants.Constants;
import com.gateway.parse.enumtype.LatitudeType;
import com.gateway.parse.enumtype.LongitudeType;
import com.gateway.parse.utils.ByteUtil;
import com.gateway.parse.utils.LonLatUtil;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import java.math.BigDecimal;
import java.nio.ByteOrder;

/**
 * 位置数据
 */
@SuppressWarnings("all")
public class LocationData implements IStatus {

    /**
     * 定位状态 解析出是否有效定位 经纬度类型
     */
    private Short status;

    /**
     * 是否有效定位
     */
    private Boolean locationstate;

    /**
     * 经度类型
     */
    private LongitudeType eastwest;

    /**
     * 纬度类型
     */
    private LatitudeType southnorth;

    /**
     * 经度
     */
    private Double lon;

    /**
     * 纬度
     */
    private Double lat;

    /**
     * 加密经度
     */
    private Double enlon;

    /**
     * 加密纬度
     */
    private Double enlat;

    /**
     * 省市区
     */
    private String prov;
    private String city;
    private String district;
    private String provCode;
    private String cityCode;
    private String districtCode;

    public Short getStatus() {
        return status;
    }

    public void setStatus(Short status) {
        this.status = status;
    }

    public Boolean getLocationstate() {
        return locationstate;
    }

    public void setLocationstate(Boolean locationstate) {
        this.locationstate = locationstate;
    }

    public LongitudeType getEastwest() {
        return eastwest;
    }

    public LatitudeType getSouthnorth() {
        return southnorth;
    }

    public void setSouthnorth(LatitudeType southnorth) {
        this.southnorth = southnorth;
    }

    public void setEastwest(LongitudeType eastwest) {
        this.eastwest = eastwest;
    }

    public Double getLon() {
        return lon;
    }

    public void setLon(Double lon) {
        this.lon = lon;
    }

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public Double getEnlon() {
        return enlon;
    }

    public void setEnlon(Double enlon) {
        this.enlon = enlon;
    }

    public Double getEnlat() {
        return enlat;
    }

    public void setEnlat(Double enlat) {
        this.enlat = enlat;
    }

    public String getProv() {
        return prov;
    }

    public void setProv(String prov) {
        this.prov = prov;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getProvCode() {
        return provCode;
    }

    public void setProvCode(String provCode) {
        this.provCode = provCode;
    }

    public String getCityCode() {
        return cityCode;
    }

    public void setCityCode(String cityCode) {
        this.cityCode = cityCode;
    }

    public String getDistrictCode() {
        return districtCode;
    }

    public void setDistrictCode(String districtCode) {
        this.districtCode = districtCode;
    }

    @Override
    public LocationData decode(ByteBuf byteBuf) throws Exception {
        LocationData locationData = new LocationData();
        locationData.setStatus(byteBuf.readUnsignedByte());
        double lon = new BigDecimal(byteBuf.readUnsignedInt()).divide(new BigDecimal(1000000), 6, BigDecimal.ROUND_HALF_UP).doubleValue();
        locationData.setLon(lon);
        double lat = new BigDecimal(byteBuf.readUnsignedInt()).divide(new BigDecimal(1000000), 6, BigDecimal.ROUND_HALF_UP).doubleValue();
        locationData.setLat(lat);
        char[] chars = ByteUtil.to32BinaryString(locationData.getStatus()).toCharArray();
        locationData.setLocationstate(Constants.CHAR_48 == (chars[chars.length-1]));
        locationData.setSouthnorth(LatitudeType.valuesOfChar(chars[chars.length-2]));
        locationData.setEastwest(LongitudeType.valuesOfChar(chars[chars.length-3]));

        // 获取加密经纬度
        double[] enLonLat = LonLatUtil.transformFromWGSToGCJ(lat, lon);
        locationData.setEnlon(enLonLat[0]);
        locationData.setEnlat(enLonLat[1]);
        return locationData;
    }

    @Override
    public ByteBuf encode() {
        ByteBuf buffer = Unpooled.buffer();
        buffer.order(ByteOrder.BIG_ENDIAN);
        buffer.writeByte(status);
        buffer.writeDouble(lon);
        buffer.writeDouble(lat);
        return buffer;
    }

}
