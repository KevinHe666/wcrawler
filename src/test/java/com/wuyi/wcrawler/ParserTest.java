package com.wuyi.wcrawler;

import com.wuyi.wcrawler.proxy.parser.XicidailiParser;

import junit.framework.TestCase;

public class ParserTest extends TestCase{
	public void testParser() {
		XicidailiParser xicidailiParser = new XicidailiParser();
		xicidailiParser.parser();
	}
}
