package org.dictionary.concept;

import java.util.Collections;
import java.util.List;

import javax.annotation.Nullable;

import org.apache.commons.lang3.StringUtils;
import org.molgenis.gson.AutoGson;

import com.google.auto.value.AutoValue;

@AutoValue
@AutoGson(autoValueClass = AutoValue_WordConcept.class)
public abstract class WordConcept
{
	public abstract String getName();

	@Nullable
	public abstract Pronunciation getPronunciation();

	public abstract List<WordSense> getWordSenses();

	@Nullable
	public abstract List<Synonym> getSynonyms();

	public static WordConcept create()
	{
		return new AutoValue_WordConcept(StringUtils.EMPTY, null, Collections.emptyList(), Collections.emptyList());
	}

	public static WordConcept create(String name, List<WordSense> wordSenses, List<Synonym> synonyms)
	{
		return new AutoValue_WordConcept(name, null, wordSenses, synonyms);
	}

	public static WordConcept create(String name, Pronunciation pronunciation, List<WordSense> wordSenses,
			List<Synonym> synonyms)
	{
		return new AutoValue_WordConcept(name, pronunciation, wordSenses, synonyms);
	}
}
