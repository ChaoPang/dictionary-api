package org.dictionary.crawl.impl;

import java.io.IOException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.dictionary.concept.WordSense;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.google.common.io.Resources;

public class DictionaryWordCrawlingServiceTest
{
	DictionaryWordCrawlingService dictionaryWordCrawlingService = new DictionaryWordCrawlingService();

	@Test
	public void testExtractWordSenses() throws IOException
	{
		URL resource = Resources.getResource("example.html");

		String htmlPage = Resources.toString(resource, Charset.forName("UTF-8"));

		List<WordSense> extractWordSensesFromHtmlPage = dictionaryWordCrawlingService
				.extractWordSensesFromHtmlPage(htmlPage).getWordSenses();

		Assert.assertEquals(extractWordSensesFromHtmlPage.size(), 5);

		Assert.assertEquals(extractWordSensesFromHtmlPage.get(0), WordSense.create("noun",
				"the yearly recurrence of the date of a past event:", "the tenth anniversary of their marriage."));

		Assert.assertEquals(extractWordSensesFromHtmlPage.get(3),
				WordSense.create("adjective", "returning or recurring each year; annual.", StringUtils.EMPTY));
	}
}
