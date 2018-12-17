package com.balance.service.shop;

import com.balance.client.RedisClient;
import com.balance.constance.RedisKeyConst;
import com.balance.entity.shop.SampleMachineLocation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.geo.GeoResult;
import org.springframework.data.geo.GeoResults;
import org.springframework.data.redis.connection.RedisGeoCommands;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SampleMachineService {

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

    public void updateSampleMachineLocation(String cityCode,List<SampleMachineLocation> sampleMachineLocations){
        String redisKey = RedisKeyConst.buildSampleMachineId(cityCode);
        sampleMachineLocations.forEach(sl -> {
            String member = sl.getSampleName()+":"+sl.getImgUrl()+":"+sl.getCoordinateX()+":"+sl.getCoordinateY();
            redisClient.leftPush(redisKey,member);
        });
    }
}
