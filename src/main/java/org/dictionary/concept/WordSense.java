package org.dictionary.concept;

import javax.annotation.Nullable;

import org.molgenis.gson.AutoGson;

import com.google.auto.value.AutoValue;

@AutoValue
@AutoGson(autoValueClass = AutoValue_WordSense.class)
public abstract class WordSense
{
	public enum WordType
	{
		NOUN("noun"),

		VERB("verb"),

		ADVERB("adverb"),

		INDEFINITE_ARTICLE("indefinite article"),

		DEFINITE_ARTICLE("definite article"),

		ADJECTIVE("adjective"),

		UNDEFINED("undefined");

		private String label;

		WordType(String label)
		{
			this.label = label;
		}

		@Override
		public String toString()
		{
			return label;
		}
	}

	public abstract String getName();

	public abstract WordType getWordType();

	public abstract String getDefinition();

	@Nullable
	public abstract String getExample();

	public static WordSense create(String name, String type, String definition, String example)
	{
		return new AutoValue_WordSense(name, toWordType(type), definition, example);
	}

	public static WordType toWordType(String wordType)
	{
		WordType valueOf;
		try
		{
			valueOf = WordType.valueOf(wordType.toUpperCase());
		}
		catch (Exception e)
		{
			valueOf = WordType.UNDEFINED;
		}
		return valueOf;
	}
}
