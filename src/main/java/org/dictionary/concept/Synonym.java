package org.dictionary.concept;

import org.molgenis.gson.AutoGson;

import com.google.auto.value.AutoValue;

@AutoValue
@AutoGson(autoValueClass = AutoValue_Synonym.class)
public abstract class Synonym
{
	public enum Relevance
	{
		HIGH("relevant-3"),

		MEDIUM("relevant-2"),

		LOW("relevant-1");

		private String label;

		Relevance(String label)
		{
			this.label = label;
		}
	}

	public abstract String getName();

	public abstract String getRelevance();

	public static Synonym create(String name, String relevance)
	{
		return new AutoValue_Synonym(name, convertRelevanceToString(relevance).toString().toLowerCase());
	}

	private static Relevance convertRelevanceToString(String relevance)
	{
		for (Relevance relevanceEnum : Relevance.values())
		{
			if (relevanceEnum.label.equalsIgnoreCase(relevance))
			{
				return relevanceEnum;
			}
		}
		return Relevance.LOW;
	}
}
