package org.dictionary.crawl.synonym;

import java.util.List;

import org.dictionary.concept.Synonym;

public interface SynonymCrawlingService
{
	public abstract List<Synonym> getSynonyms(String word);
}
