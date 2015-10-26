package org.dictionary.crawl.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.dictionary.concept.WordSense;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DictionaryWordCrawlingService extends AbstractWordCrawlingService
{
	private static final String PAGE_BASE_URL = "http://dictionary.reference.com/browse/";
	private static final Logger LOGGER = LoggerFactory.getLogger(DictionaryWordCrawlingService.class);
	private static final String CLASS_ATTRIBUTE = "class";
	private static final String DEFINITION_LIST_HTML_CLASS = "def-list";
	private static final String WORD_TYPE_HTML_TAG = "header";

	public DictionaryWordCrawlingService()
	{
		super(PAGE_BASE_URL, LOGGER);
	}

	@Override
	public List<WordSense> getWordSenses(String wordToLookup)
	{
		if (StringUtils.isNotBlank(wordToLookup))
		{
			String htmlPage = getPage(wordToLookup);
			return extractWordSensesFromHtmlPage(htmlPage);
		}

		return Collections.emptyList();
	}

	List<WordSense> extractWordSensesFromHtmlPage(String htmlPage)
	{
		List<WordSense> wordSenses = new ArrayList<>();

		Document htmlDocument = Jsoup.parse(htmlPage);
		String wordName = extractWordName(htmlDocument);
		Elements listOfDefinitionElements = htmlDocument.getElementById("source-luna")
				.getElementsByAttributeValueContaining(CLASS_ATTRIBUTE, DEFINITION_LIST_HTML_CLASS);

		if (listOfDefinitionElements.size() > 0)
		{
			Elements sections = listOfDefinitionElements.get(0).select("section");

			for (Element wordElementsWithSameType : sections)
			{
				String wordType = extractWordType(wordElementsWithSameType);

				for (Element definitionElement : wordElementsWithSameType.select("div.def-set"))
				{
					String definition = extractDefinitionText(definitionElement);
					String example = extractDefinitionExample(definitionElement);
					wordSenses.add(WordSense.create(wordName, wordType, definition, example));
				}
			}
		}
		return wordSenses;
	}

	private String extractDefinitionExample(Element definitionElement)
	{
		Elements select = definitionElement.select(".def-content").first().select("div.def-inline-example");
		return select.size() > 0 ? select.text() : StringUtils.EMPTY;
	}

	private String extractDefinitionText(Element definitionElement)
	{
		Elements select = definitionElement.select(".def-content");
		return select.size() > 0 ? select.first().ownText() : StringUtils.EMPTY;
	}

	private String extractWordName(Document htmlDocument)
	{
		Elements headers = htmlDocument.getElementById("source-luna").select("header.main-header");
		return headers.size() > 0 ? headers.first().select("h1.head-entry").text() : StringUtils.EMPTY;
	}

	private String extractWordType(Element wordElementsWithSameType)
	{
		Elements select = wordElementsWithSameType.getElementsByTag(WORD_TYPE_HTML_TAG).select("span");
		return select.size() > 0 ? select.first().text() : StringUtils.EMPTY;
	}
}