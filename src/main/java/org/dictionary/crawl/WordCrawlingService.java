package org.dictionary.crawl;

import org.dictionary.concept.WordConcept;

public interface WordCrawlingService
{
	public abstract String getPage(String wordToLookup);

	public abstract WordConcept getWordSenses(String wordToLookup);
}
