package com.app.http.model;

import java.util.List;

/**
 * Created by thkcheng on 2018/7/17.
 */

public class RecommendModel {


    /**
     * result : {"code":0,"message":"请求成功","bonus":0,"timestamp":"2018-07-17 10:04:24"}
     * data : {"cityTlinkList":[],"nationwideTlinkList":[],"heavyRecommend":{"linkid":632951,"url":"19058024","remark":"PT-支付测试-成都2","orders":1,"img_url":"/upload/2018/05/18/1526612290912_j9s2_m1.jpg","fconfigid":-1,"urlType":null,"isdisplaytime":1,"active_begindate":1513528800000,"active_enddate":1577375999000,"heavyRecommendForwordType":null,"isShow":1}}
     */

    private ResultBean result;
    private DataBean data;

    public ResultBean getResult() {
        return result;
    }

    public void setResult(ResultBean result) {
        this.result = result;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class ResultBean {
        /**
         * code : 0
         * message : 请求成功
         * bonus : 0
         * timestamp : 2018-07-17 10:04:24
         */

        private int code;
        private String message;
        private int bonus;
        private String timestamp;

        public int getCode() {
            return code;
        }

        public void setCode(int code) {
            this.code = code;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public int getBonus() {
            return bonus;
        }

        public void setBonus(int bonus) {
            this.bonus = bonus;
        }

        public String getTimestamp() {
            return timestamp;
        }

        public void setTimestamp(String timestamp) {
            this.timestamp = timestamp;
        }
    }

    public static class DataBean {
        /**
         * cityTlinkList : []
         * nationwideTlinkList : []
         * heavyRecommend : {"linkid":632951,"url":"19058024","remark":"PT-支付测试-成都2","orders":1,"img_url":"/upload/2018/05/18/1526612290912_j9s2_m1.jpg","fconfigid":-1,"urlType":null,"isdisplaytime":1,"active_begindate":1513528800000,"active_enddate":1577375999000,"heavyRecommendForwordType":null,"isShow":1}
         */

        private HeavyRecommendBean heavyRecommend;
        private List<?> cityTlinkList;
        private List<?> nationwideTlinkList;

        public HeavyRecommendBean getHeavyRecommend() {
            return heavyRecommend;
        }

        public void setHeavyRecommend(HeavyRecommendBean heavyRecommend) {
            this.heavyRecommend = heavyRecommend;
        }

        public List<?> getCityTlinkList() {
            return cityTlinkList;
        }

        public void setCityTlinkList(List<?> cityTlinkList) {
            this.cityTlinkList = cityTlinkList;
        }

        public List<?> getNationwideTlinkList() {
            return nationwideTlinkList;
        }

        public void setNationwideTlinkList(List<?> nationwideTlinkList) {
            this.nationwideTlinkList = nationwideTlinkList;
        }

        @Override
        public String toString() {
            return "DataBean{" +
                    "heavyRecommend=" + heavyRecommend +
                    ", cityTlinkList=" + cityTlinkList +
                    ", nationwideTlinkList=" + nationwideTlinkList +
                    '}';
        }

        public static class HeavyRecommendBean {
            /**
             * linkid : 632951
             * url : 19058024
             * remark : PT-支付测试-成都2
             * orders : 1
             * img_url : /upload/2018/05/18/1526612290912_j9s2_m1.jpg
             * fconfigid : -1
             * urlType : null
             * isdisplaytime : 1
             * active_begindate : 1513528800000
             * active_enddate : 1577375999000
             * heavyRecommendForwordType : null
             * isShow : 1
             */

            private int linkid;
            private String url;
            private String remark;
            private int orders;
            private String img_url;
            private int fconfigid;
            private Object urlType;
            private int isdisplaytime;
            private long active_begindate;
            private long active_enddate;
            private Object heavyRecommendForwordType;
            private int isShow;

            public int getLinkid() {
                return linkid;
            }

            public void setLinkid(int linkid) {
                this.linkid = linkid;
            }

            public String getUrl() {
                return url;
            }

            public void setUrl(String url) {
                this.url = url;
            }

            public String getRemark() {
                return remark;
            }

            public void setRemark(String remark) {
                this.remark = remark;
            }

            public int getOrders() {
                return orders;
            }

            public void setOrders(int orders) {
                this.orders = orders;
            }

            public String getImg_url() {
                return img_url;
            }

            public void setImg_url(String img_url) {
                this.img_url = img_url;
            }

            public int getFconfigid() {
                return fconfigid;
            }

            public void setFconfigid(int fconfigid) {
                this.fconfigid = fconfigid;
            }

            public Object getUrlType() {
                return urlType;
            }

            public void setUrlType(Object urlType) {
                this.urlType = urlType;
            }

            public int getIsdisplaytime() {
                return isdisplaytime;
            }

            public void setIsdisplaytime(int isdisplaytime) {
                this.isdisplaytime = isdisplaytime;
            }

            public long getActive_begindate() {
                return active_begindate;
            }

            public void setActive_begindate(long active_begindate) {
                this.active_begindate = active_begindate;
            }

            public long getActive_enddate() {
                return active_enddate;
            }

            public void setActive_enddate(long active_enddate) {
                this.active_enddate = active_enddate;
            }

            public Object getHeavyRecommendForwordType() {
                return heavyRecommendForwordType;
            }

            public void setHeavyRecommendForwordType(Object heavyRecommendForwordType) {
                this.heavyRecommendForwordType = heavyRecommendForwordType;
            }

            public int getIsShow() {
                return isShow;
            }

            public void setIsShow(int isShow) {
                this.isShow = isShow;
            }

            @Override
            public String toString() {
                return "HeavyRecommendBean{" +
                        "linkid=" + linkid +
                        ", url='" + url + '\'' +
                        ", remark='" + remark + '\'' +
                        ", orders=" + orders +
                        ", img_url='" + img_url + '\'' +
                        ", fconfigid=" + fconfigid +
                        ", urlType=" + urlType +
                        ", isdisplaytime=" + isdisplaytime +
                        ", active_begindate=" + active_begindate +
                        ", active_enddate=" + active_enddate +
                        ", heavyRecommendForwordType=" + heavyRecommendForwordType +
                        ", isShow=" + isShow +
                        '}';
            }
        }
    }
}
