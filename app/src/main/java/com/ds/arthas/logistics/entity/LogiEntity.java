package com.ds.arthas.logistics.entity;

import android.support.v7.util.SortedList;

import java.util.List;

/**
 * Created by Administrator on 2016/7/18 0018.
 */
public class LogiEntity {


    /**
     * id : 187922
     * oid : 444405
     * status : 1
     * price : 0.00
     * paytype : 0
     * customername : caoyan_g
     * customerphone : 13522224914
     * customeraddress : 万达广场志晟信息三楼
     * remark :
     * goods : [{"id":"441361","gname":"转运竹","num":"1","price":"25.00"}]
     */

    private String id;
    private String oid;
    private String status;
    private String price;
    private int paytype;
    private String customername;
    private String customerphone;
    private String customeraddress;
    private String shopname;
    private String shopphone;
    private String shopaddress;
    private String remark;
    private String oaddtime;

    public String getOaddtime() {
        return oaddtime;
    }

    public void setOaddtime(String oaddtime) {
        this.oaddtime = oaddtime;
    }

    public String getShopname() {
        return shopname;
    }

    public void setShopname(String shopname) {
        this.shopname = shopname;
    }

    public String getShopphone() {
        return shopphone;
    }

    public void setShopphone(String shopphone) {
        this.shopphone = shopphone;
    }

    public String getShopaddress() {
        return shopaddress;
    }

    public void setShopaddress(String shopaddress) {
        this.shopaddress = shopaddress;
    }

    /**
     * id : 441361
     * gname : 转运竹
     * num : 1
     * price : 25.00
     */


    private List<GoodsBean> goods;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOid() {
        return oid;
    }

    public void setOid(String oid) {
        this.oid = oid;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public int getPaytype() {
        return paytype;
    }

    public void setPaytype(int paytype) {
        this.paytype = paytype;
    }

    public String getCustomername() {
        return customername;
    }

    public void setCustomername(String customername) {
        this.customername = customername;
    }

    public String getCustomerphone() {
        return customerphone;
    }

    public void setCustomerphone(String customerphone) {
        this.customerphone = customerphone;
    }

    public String getCustomeraddress() {
        return customeraddress;
    }

    public void setCustomeraddress(String customeraddress) {
        this.customeraddress = customeraddress;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public List<GoodsBean> getGoods() {
        return goods;
    }

    public void setGoods(List<GoodsBean> goods) {
        this.goods = goods;
    }

    public static class GoodsBean {
        private String id;
        private String gname;
        private String num;
        private String price;
        private String properties;

        public String getProperties() {
            return properties;
        }

        public void setProperties(String properties) {
            this.properties = properties;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getGname() {
            return gname;
        }

        public void setGname(String gname) {
            this.gname = gname;
        }

        public String getNum() {
            return num;
        }

        public void setNum(String num) {
            this.num = num;
        }

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }
    }

}
