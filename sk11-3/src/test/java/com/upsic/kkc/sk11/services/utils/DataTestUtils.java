package com.upsic.kkc.sk11.services.utils;


import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import com.upsic.kkc.sk11.data.AdderData;
import com.upsic.kkc.sk11.data.GouData;
import com.upsic.kkc.sk11.data.UdgData;
import com.upsic.kkc.sk11.enums.AdderType;
import com.upsic.kkc.sk11.enums.GouType;
import com.upsic.kkc.sk11.utils.ConverterTime;

public class DataTestUtils {

    public final static Random random = new Random();


    public static UdgData getUdgData(Consumer<UdgData> consumer) {
        Instant startDate = Instant.now();
        startDate = startDate.truncatedTo(ChronoUnit.MILLIS);
        int pointCount = 24 * 60;
        List<Float> points = new ArrayList<>();
        for (int i = 0; i < pointCount; i++) {
            Float point = random.nextFloat();
            points.add(point);
        }
        Instant endDate = startDate.plus(pointCount - 1, ChronoUnit.MINUTES); //Так как считаем с нулевой минуты, добавляем -1

        UdgData udgData = UdgData.builder()
                .dtStart(ConverterTime.toLocalDateTime(startDate))
                .dtEnd(ConverterTime.toLocalDateTime(endDate))
                .objGen(UUID.randomUUID())
                .values(points)
                .build();
        consumer.accept(udgData);
        return udgData;
    }

    public static List<GouData> getGouDataList() {
        return getGouDataList(2,2,2);
    }

    public static List<GouData> getGouDataList(int blockCount, int rgeCount, int gouCount) {
        List<GouData> result = new ArrayList<>();
        for (int i = 0; i < gouCount; i++) {
            GouData gou = getGouData(gouData -> gouData.setType(GouType.GOU));
            result.add(gou);
            for (int j = 0; j < rgeCount; j++) {
                GouData rge = getGouData(gouData -> {
                    gouData.setType(GouType.RGE);
                    gouData.setParentId(gou.getIdGou());
                });
                result.add(rge);
                for (int l = 0; l < blockCount; l++) {
                    GouData block = getGouData(gouData -> {
                        gouData.setType(GouType.NBLOCK);
                        gouData.setParentId(rge.getIdGou());
                    });
                    result.add(block);
                }
            }
        }
        return result;
    }

    public static List<AdderData> getAdderDataList(List<GouData> gouDataList) {
        return gouDataList.stream().map(gouData -> AdderData.builder()
                .idAdder(gouData.getIdGou())
                .parentId(gouData.getParentId())
                .uid(gouData.getUid())
                .type(gouData.getType() == GouType.RGE ? AdderType.RGE : AdderType.GEN)
                .build()
        ).collect(Collectors.toList());
    }

    public static GouData getGouData() {
        return getGouData(gouData -> {
        });
    }

    public static GouData getGouData(Consumer<GouData> consumer) {
        GouData gouData = new GouData();
        gouData.setUid(UUID.randomUUID());
        gouData.setIdGou(String.valueOf(random.nextInt()));
        gouData.setType(GouType.GOU);
        gouData.setParentId("");
        consumer.accept(gouData);
        return gouData;
    }

}
