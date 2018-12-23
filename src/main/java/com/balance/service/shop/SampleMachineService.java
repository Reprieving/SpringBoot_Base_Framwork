package com.balance.service.shop;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.balance.client.RedisClient;
import com.balance.constance.RedisKeyConst;
import com.balance.entity.shop.SampleMachineLocation;
import com.balance.entity.shop.SampleMachineSourceLocations;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.geo.GeoResult;
import org.springframework.data.geo.GeoResults;
import org.springframework.data.redis.connection.RedisGeoCommands;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class SampleMachineService {

    private static final Logger logger = LoggerFactory.getLogger(SampleMachineService.class);

    @Autowired
    private RedisClient redisClient;

    /**
     * 查询市内小样机坐标
     * @param cityCode
     * @param coordinateX
     * @param coordinateY
     * @return
     */
    public List<SampleMachineLocation> listSampleMachineLocation(String cityCode,Double coordinateX,Double coordinateY){
        List<SampleMachineLocation> machineLocation = new ArrayList<>();
        String redisKey = RedisKeyConst.buildSampleMachineId(cityCode);
        GeoResults<RedisGeoCommands.GeoLocation<Object>> radiusGeo = redisClient.radiusGeo(redisKey, coordinateX, coordinateY, 1000D, Sort.Direction.DESC, 100L);
        for (GeoResult<RedisGeoCommands.GeoLocation<Object>> geoResult : radiusGeo.getContent()) {
            Integer distance = Integer.parseInt(new java.text.DecimalFormat("0").format(geoResult.getDistance().getValue()));
            RedisGeoCommands.GeoLocation<Object> geoLocation = geoResult.getContent();

            //[sampleName],[imgUrl],[coordinateX],[coordinateY]
            String[] locationInfo = String.valueOf(geoLocation.getName()).split(":");
            SampleMachineLocation sl = new SampleMachineLocation(locationInfo[0],locationInfo[1],Double.valueOf(locationInfo[2]),Double.valueOf(locationInfo[3]),distance);
            machineLocation.add(sl);
        }
        return machineLocation;
    }


    /**
     * 更新市内小样机坐标
     * @param cityCode
     */
    public void updateSampleMachineLocation(String cityCode){
        cityCode = "440300";//先默认为深圳

        //扫码接口
        GetMethod get = null;
        String result = null;
        try {
            HttpClient client = new HttpClient();
            get = new GetMethod("http://pinkjewelry.cn/pinkjewelry/officialAccounts/queryAllLongitudeAndLatitude");
            client.executeMethod(get);
            result = get.getResponseBodyAsString();
        } catch (IOException e) {
            logger.error(e.getMessage());
        } finally {
            if(get!=null){
                get.releaseConnection();
            }
        }

        result = result.replace("null(","").replace(")","");
        SampleMachineSourceLocations sls = JSON.parseObject(result,SampleMachineSourceLocations.class);

        String redisKey = RedisKeyConst.buildSampleMachineId(cityCode);
        sls.getDatas().forEach(sl -> {
            //[sampleName],[imgUrl],[coordinateX],[coordinateY]
            String member = sl.getName()+":"+" "+":"+sl.getLongitude()+":"+sl.getLatitude();
            redisClient.cacheGeo(redisKey, sl.getLongitude(), sl.getLatitude(), member, -1L);
        });

    }

}
