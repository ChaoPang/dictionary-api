package org.dictionary.crawl.synonym.impl;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.swing.text.html.HTML.Tag;

import org.apache.commons.lang3.StringUtils;
import org.dictionary.concept.Synonym;
import org.dictionary.crawl.AbstractHtmlPageCrawlingService;
import org.dictionary.crawl.synonym.SynonymCrawlingService;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;

public class SynonymCrawlingServiceImpl extends AbstractHtmlPageCrawlingService<List<Synonym>>
		implements SynonymCrawlingService
{
	private final static Type TYPE = new TypeToken<Map<String, String>>()
	{
		private static final long serialVersionUID = 1L;
	}.getType();
	private final static String BASE_URL = "http://www.thesaurus.com/browse/";
	private final static Logger LOGGER = LoggerFactory.getLogger(SynonymCrawlingServiceImpl.class);
	private final static String SYNONYM_CONTAINER_ID = "synonyms-0";
	private final static String SYNONYM_RELEVANT_LIST_CLASS = "relevancy-list";
	private final static String SYNONYM_SPAN_CLASS_TEXT = "text";
	private final static String SYNONYM_DATA = "data-category";
	private final static String SYNONYM_DATA_NAME = "name";

	public SynonymCrawlingServiceImpl()
	{
		super(BASE_URL, LOGGER);
	}

	@Override
	public List<Synonym> getSynonyms(String word)
	{
		return extractConceptsFromHtmlPage(getPage(word));
	}

	public List<Synonym> extractConceptsFromHtmlPage(String htmlPage)
	{
		List<Synonym> synonyms = new ArrayList<>();

		Document htmlDocument = Jsoup.parse(htmlPage);

		Element synonymContainer = getElementById(htmlDocument, SYNONYM_CONTAINER_ID);

		Element synonymRelevantList = getElementByClass(synonymContainer, SYNONYM_RELEVANT_LIST_CLASS);

		for (Element unorderedListElement : getElementsByTagName(synonymRelevantList, Tag.UL.toString()))
		{
			for (Element listItem : getElementsByTagName(unorderedListElement, Tag.LI.toString()))
			{
				synonyms.add(Synonym.create(getSynonymName(listItem), getSynonymRelevance(listItem)));
			}
		}
		return synonyms;
	}

	String getSynonymRelevance(Element listItem)
	{
		Element hyperLinkElement = getElementsByTagName(listItem, Tag.A.toString()).first();
		String attrValue = hyperLinkElement.attr(SYNONYM_DATA);
		if (StringUtils.isNotBlank(attrValue))
		{
			Map<String, String> fromJson = new Gson().fromJson(attrValue, TYPE);
			return fromJson.get(SYNONYM_DATA_NAME);
		}

		return StringUtils.EMPTY;
	}

	String getSynonymName(Element listItem)
	{
		Element element = getElementByClass(listItem, SYNONYM_SPAN_CLASS_TEXT);
		if (element == null)
		{
			throw new RuntimeException("Could not find the synonym name in the element: " + listItem.toString());
		}
		return element.text();
	}

	private Elements getElementsByTagName(Element element, String tagName)
	{
		Elements elements = element.getElementsByTag(tagName);
		if (elements.size() == 0)
		{
			throw new RuntimeException(
					"Could not find the element by tag name " + tagName + " in the element: " + element.toString());
		}
		return elements;
	}

	private Element getElementById(Element element, String elementId)
	{
		Element elementById = element.getElementById(elementId);
		if (elementById == null)
		{
			throw new RuntimeException(
					"Could not find the element by Id " + elementById + " in the element: " + element.toString());
		}
		return elementById;
	}

	private Element getElementByClass(Element element, String className)
	{
		Elements elementsByClass = element.getElementsByClass(className);
		if (elementsByClass.size() == 0)
		{
			throw new RuntimeException("Could not find the class name in the element: " + element.toString());
		}
		return elementsByClass.first();
	}
}
