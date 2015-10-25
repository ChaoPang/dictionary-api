package org.dictionary.crawl;

import java.util.List;

import org.dictionary.concept.WordSense;

public interface WordCrawlingService
{
	public abstract String getPage(String wordToLookup);

	public abstract List<WordSense> getWordSenses(String wordToLookup);
}
