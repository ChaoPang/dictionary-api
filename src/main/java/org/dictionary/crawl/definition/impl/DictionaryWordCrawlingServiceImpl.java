package org.dictionary.crawl.definition.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.dictionary.concept.Pronunciation;
import org.dictionary.concept.WordConcept;
import org.dictionary.concept.WordSense;
import org.dictionary.crawl.AbstractHtmlPageCrawlingService;
import org.dictionary.crawl.definition.WordCrawlingService;
import org.dictionary.crawl.synonym.SynonymCrawlingService;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

public class DictionaryWordCrawlingServiceImpl extends AbstractHtmlPageCrawlingService<WordConcept>
		implements WordCrawlingService
{
	private static final String PAGE_BASE_URL = "http://dictionary.reference.com/browse/";
	private static final Logger LOGGER = LoggerFactory.getLogger(DictionaryWordCrawlingServiceImpl.class);
	private static final String CLASS_ATTRIBUTE = "class";
	private static final String DEFINITION_LIST_HTML_CLASS = "def-list";
	private static final String WORD_TYPE_HTML_TAG = "header";
	private static final String NON_ALPHANUMERIC_CHARS = "[^a-zA-Z0-9]";
	private final SynonymCrawlingService synonymCrawlingService;

	@Autowired
	public DictionaryWordCrawlingServiceImpl(SynonymCrawlingService synonymCrawlingService)
	{
		super(PAGE_BASE_URL, LOGGER);
		this.synonymCrawlingService = Objects.requireNonNull(synonymCrawlingService);
	}

	@Override
	public WordConcept getWordSenses(String wordToLookup)
	{
		if (StringUtils.isNotBlank(wordToLookup))
		{
			String htmlPage = getPage(wordToLookup);
			return extractConceptsFromHtmlPage(htmlPage);
		}

		return WordConcept.create();
	}

	public WordConcept extractConceptsFromHtmlPage(String htmlPage)
	{
		List<WordSense> wordSenses = new ArrayList<>();

		Document htmlDocument = Jsoup.parse(htmlPage);
		String wordName = extractWordName(htmlDocument);
		Element mainElement = htmlDocument.getElementById("source-luna");
		Elements listOfDefinitionElements = mainElement.getElementsByAttributeValueContaining(CLASS_ATTRIBUTE,
				DEFINITION_LIST_HTML_CLASS);
		Pronunciation pronunciation = extractPronunciation(mainElement);

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
					wordSenses.add(WordSense.create(wordType, definition, example));
				}
			}
		}

		return WordConcept.create(wordName, pronunciation,
				wordSenses.stream().filter(this::wordSenseContainsDefinition).collect(Collectors.toList()),
				synonymCrawlingService.getSynonyms(wordName));
	}

	private Pronunciation extractPronunciation(Element mainElement)
	{
		Elements select = mainElement.select("div.audio-wrapper");
		Elements audio = select.select("audio");
		if (audio.size() > 0)
		{
			String oggSource = audio.first().getElementsByAttributeValue("type", "audio/ogg").first().attr("src");
			String mp3Source = audio.first().getElementsByAttributeValue("type", "audio/mpeg").first().attr("src");
			return Pronunciation.create(mp3Source, oggSource);
		}
		return null;
	}

	private boolean wordSenseContainsDefinition(WordSense wordSense)
	{
		return StringUtils
				.isNotBlank(wordSense.getDefinition().replaceAll(NON_ALPHANUMERIC_CHARS, StringUtils.EMPTY).trim());
	}

	private String extractDefinitionExample(Element definitionElement)
	{
		Elements select = definitionElement.select(".def-content").first().select("div.def-inline-example");
		return select.size() > 0 ? select.text() : StringUtils.EMPTY;
	}

	private String extractDefinitionText(Element definitionElement)
	{
		Elements select = definitionElement.select(".def-content");
		if (select.size() > 0)
		{
			String text = select.first().ownText().replaceAll(NON_ALPHANUMERIC_CHARS, StringUtils.EMPTY);
			return StringUtils.isBlank(text) ? select.first().text() : select.first().ownText();
		}
		return StringUtils.EMPTY;
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