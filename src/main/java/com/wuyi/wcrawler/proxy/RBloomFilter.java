package com.wuyi.wcrawler.proxy;

import com.google.common.hash.BloomFilter;
import com.google.common.hash.Funnel;
import com.wuyi.wcrawler.bean.Proxy;
import org.springframework.stereotype.Component;

import java.io.Serializable;

@Component(value = "bloomFilter")
public class RBloomFilter implements Serializable {
    private BloomFilter bloomFilter;
    public RBloomFilter(Funnel funnel, int expectedInsertions, double falsePositiveProbability) {
        bloomFilter = BloomFilter.create(funnel, expectedInsertions, falsePositiveProbability);
    }

    public void put(Proxy proxy) {
        bloomFilter.put(proxy);
    }

    public boolean mightContain(Proxy proxy) {
        return bloomFilter.mightContain(proxy);
    }
}
