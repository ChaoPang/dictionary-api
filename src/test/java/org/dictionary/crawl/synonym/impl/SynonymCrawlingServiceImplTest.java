package org.dictionary.crawl.synonym.impl;

import java.io.IOException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.List;

import org.dictionary.concept.Synonym;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.google.common.io.Resources;

public class SynonymCrawlingServiceImplTest
{
	SynonymCrawlingServiceImpl synonymCrawlingService = new SynonymCrawlingServiceImpl();

	@Test
	public void extractConceptsFromHtmlPage() throws IOException
	{
		URL resource = Resources.getResource("synonym.html");

		String htmlPage = Resources.toString(resource, Charset.forName("UTF-8"));

		List<Synonym> extractWordSensesFromHtmlPage = synonymCrawlingService.extractConceptsFromHtmlPage(htmlPage);

		Assert.assertEquals(extractWordSensesFromHtmlPage.size(), 29);

		Assert.assertEquals(extractWordSensesFromHtmlPage.get(0), Synonym.create("provisional", "relevant-3"));

		Assert.assertEquals(extractWordSensesFromHtmlPage.get(3), Synonym.create("relative", "relevant-2"));
	}
}
