package com.wuyi.wcrawler.proxy.util;

import com.google.common.hash.BloomFilter;
import com.google.common.hash.Funnel;

/**
 * Created by wuyi5 on 2017/9/4.
 */
public class ProxyFilterUtil {
    BloomFilter bloomFilter;
    public BloomFilter createBloomFilter(Funnel funnel, int expectedInsertions, double falsePositiveProbability) {
        bloomFilter = BloomFilter.create(funnel, expectedInsertions, falsePositiveProbability);
        return bloomFilter;
    }

}
