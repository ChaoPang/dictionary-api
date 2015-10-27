package org.dictionary.controller;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import java.util.Objects;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.StringUtils;
import org.dictionary.concept.WordConcept;
import org.dictionary.crawl.impl.DictionaryWordCrawlingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

@Controller
@RequestMapping("/")
public class DictionaryController
{
	private final static String TEMPLATE_VIEW = "view-dictionary";
	private final DictionaryWordCrawlingService dictionaryWordCrawlingService;
	private final LoadingCache<String, WordConcept> cachedWordConcepts = CacheBuilder.newBuilder().maximumSize(1000)
			.expireAfterWrite(1, TimeUnit.DAYS).build(new CacheLoader<String, WordConcept>()
			{
				public WordConcept load(String word)
				{
					return dictionaryWordCrawlingService.getWordSenses(word);
				}
			});

	@Autowired
	public DictionaryController(DictionaryWordCrawlingService dictionaryWordCrawlingService)
	{
		this.dictionaryWordCrawlingService = Objects.requireNonNull(dictionaryWordCrawlingService);
	}

	@RequestMapping(method = RequestMethod.GET)
	public String defaultView(Model model)
	{
		return TEMPLATE_VIEW;
	}

	@CrossOrigin
	@RequestMapping(value = "/dictionary/{word}", method = RequestMethod.GET, produces = APPLICATION_JSON_VALUE)
	@ResponseBody
	public WordConcept getWordConcept(@PathVariable("word") String wordToLookup) throws ExecutionException
	{
		if (StringUtils.isNotBlank(wordToLookup))
		{
			return cachedWordConcepts.get(wordToLookup);
		}
		return WordConcept.create();
	}
}