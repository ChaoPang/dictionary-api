package org.dictionary.controller;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import java.util.List;
import java.util.Objects;

import org.dictionary.concept.WordSense;
import org.dictionary.crawl.impl.DictionaryWordCrawlingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/")
public class DictionaryController
{
	private final static String TEMPLATE_VIEW = "view-dictionary";
	private final DictionaryWordCrawlingService dictionaryWordCrawlingService;

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

	@RequestMapping(value = "/dictionary/{word}", method = RequestMethod.GET, produces = APPLICATION_JSON_VALUE)
	@ResponseBody
	public List<WordSense> recovery(@PathVariable("word") String wordToLookup)
	{
		List<WordSense> wordSenses = dictionaryWordCrawlingService.getWordSenses(wordToLookup);
		return wordSenses;
	}
}