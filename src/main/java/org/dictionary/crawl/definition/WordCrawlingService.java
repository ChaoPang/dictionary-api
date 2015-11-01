package org.dictionary.crawl.definition;

import org.dictionary.concept.WordConcept;

public interface WordCrawlingService
{
	public abstract WordConcept getWordSenses(String wordToLookup);
}
