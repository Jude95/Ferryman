package com.jude.ferryman.internal.router;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * URL的实体封装类
 * URL: activity://  xxxxxxxxx  ?  a=1&b=2
 * scheme      authority      data
 * ---------------------
 * address
 * Created by zane on 2017/1/17.
 */

public class Url implements Parcelable {

    public static final String TAG_SCHEME_SUFFIX = "://";
    public static final String TAG_ADDRESS_SUFFIX = "?";
    public static final String TAG_PARAMS_DIVIDER = "&";
    public static final String TAG_PARAMS_OPERATOR = "=";


    private String address;
    private String data;
    private String scheme;
    private String authority;
    private Map<String, String> params;

    private Url(Builder builder) {
        scheme = builder.scheme;
        authority = builder.authority;
        params = builder.params;

        // 构造标准address
        this.address = "";
        if (!isEmpty(scheme)){
            this.address = getString(scheme) + TAG_SCHEME_SUFFIX;
        }
        this.address += getString(authority);

    }

    public String getAddress() {
        return address;
    }

    public String getData() {
        return data;
    }

    public String getScheme() {
        return scheme;
    }

    public String getAuthority() {
        return authority;
    }

    public Map<String, String> getParams() {
        return new HashMap<>(params);
    }

    public Builder newBuilder(){
        Builder builder = new Builder();
        builder.scheme = scheme;
        builder.authority = authority;
        builder.params = new HashMap<>(params);
        return builder;
    }

    public static Url parse(String urlStr) {
        Url.Builder urlBuilder = new Url.Builder();

        //寻找address的结尾地址
        int addressEndPos = 0;
        int paramsStartPos = 0;
        if (!urlStr.contains(TAG_ADDRESS_SUFFIX)) {
            if (urlStr.contains(TAG_PARAMS_OPERATOR)){
                addressEndPos = 0;
                paramsStartPos = 0;
            }else {
                addressEndPos = urlStr.length();
                paramsStartPos = addressEndPos;
            }
        }else {
            addressEndPos = urlStr.indexOf(TAG_ADDRESS_SUFFIX);
            paramsStartPos = addressEndPos+TAG_ADDRESS_SUFFIX.length();
        }

        //寻找scheme的结束地址 与 authority的起始地址
        int schemeEndPos = urlStr.indexOf(TAG_SCHEME_SUFFIX);
        int authorityStartPos = schemeEndPos + TAG_SCHEME_SUFFIX.length();
        if (!urlStr.contains(TAG_SCHEME_SUFFIX)) {
            schemeEndPos = 0;
            authorityStartPos = 0;
        }

        String scheme = urlStr.substring(0, schemeEndPos);
        String authority = urlStr.substring(authorityStartPos, addressEndPos);
        String params = urlStr.substring(paramsStartPos, urlStr.length());

        urlBuilder.setScheme(scheme);
        urlBuilder.setAuthority(authority);

        if (params != null && !params.isEmpty()) {
            String[] paramsEntries = params.split(TAG_PARAMS_DIVIDER);
            for (String entry : paramsEntries) {
                if (entry.contains(TAG_PARAMS_OPERATOR)) {
                    int operatorPos = entry.indexOf(TAG_PARAMS_OPERATOR);
                    urlBuilder.addParam(entry.substring(0,operatorPos), entry.substring(operatorPos+1,entry.length()));
                } else {
                    throw new IllegalArgumentException("params format error");
                }
            }
        }

        return urlBuilder.build();
    }


    //----------------------------------------------------------------------------------------------

    public static class Builder {

        private String scheme;
        private String authority;
        private Map<String, String> params;

        public Builder() {
            params = new HashMap<>();
        }

        public Builder addParam(String key, String value) {
            params.put(key, value);
            return this;
        }

        public Builder setScheme(String scheme) {
            this.scheme = scheme;
            return this;
        }

        public Builder setAuthority(String authority) {
            this.authority = authority;
            return this;
        }

        public Builder setAddress(String address) {
            //寻找scheme的结束地址 与 authority的起始地址
            int schemeEndPos = address.indexOf(TAG_SCHEME_SUFFIX);
            int authorityStartPos = schemeEndPos + TAG_SCHEME_SUFFIX.length();
            if (!address.contains(TAG_SCHEME_SUFFIX)) {
                schemeEndPos = 0;
                authorityStartPos = 0;
            }
            setScheme(address.substring(0,schemeEndPos));
            setAuthority(address.substring(authorityStartPos,address.length()));
            return this;
        }

        public Url build() {
            return new Url(this);
        }
    }

    //----------------------------------------------------------------------------------------------

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.address);
        dest.writeString(this.data);
        dest.writeString(this.scheme);
        dest.writeString(this.authority);
        dest.writeInt(this.params.size());
        for (Map.Entry<String, String> entry : this.params.entrySet()) {
            dest.writeString(entry.getKey());
            dest.writeString(entry.getValue());
        }
    }

    protected Url(Parcel in) {
        this.address = in.readString();
        this.data = in.readString();
        this.scheme = in.readString();
        this.authority = in.readString();
        int paramsSize = in.readInt();
        this.params = new HashMap<String, String>(paramsSize);
        for (int i = 0; i < paramsSize; i++) {
            String key = in.readString();
            String value = in.readString();
            this.params.put(key, value);
        }
    }

    public static final Creator<Url> CREATOR = new Creator<Url>() {
        @Override
        public Url createFromParcel(Parcel source) {
            return new Url(source);
        }

        @Override
        public Url[] newArray(int size) {
            return new Url[size];
        }
    };

    private static String getString(String value) {
        return value == null ? "" : value;
    }

    private static boolean isEmpty(String value){
        return value == null || value.isEmpty();
    }

    @Override
    public String toString() {
        String url = getString(address);

        if (!isEmpty(url) && !params.isEmpty()){
            url+=TAG_ADDRESS_SUFFIX;
        }

        Iterator<Map.Entry<String, String>> iterator = params.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, String> entry = iterator.next();
            url += getString(entry.getKey()) + TAG_PARAMS_OPERATOR + getString(entry.getValue());
            if (iterator.hasNext()) {
                url += TAG_PARAMS_DIVIDER;
            }
        }
        return url;
    }
}
