package com.wuyi.wcrawler.common;

/**
 * @authort wuyi
 * @date 2017/11/3.
 */
public enum ProxySelectPolicy {
    SEQUENCE,
    RANDOM,
    SUCCESS_PROBABILITY_PRIORITY,
    SUCCESS_TIMES_PRIORITY,
    SUCCESS_TIME_CONSUME_LEAST_PRIORITY;
}
