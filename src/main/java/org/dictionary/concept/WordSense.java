package org.dictionary.concept;

import java.util.HashMap;
import java.util.Map;

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

		VERB_WITH_OBJECT("verb (used with object)"),

		VERB_WITHOUT_OBJECT("verb (used without object)"),

		ADVERB("adverb"),

		INDEFINITE_ARTICLE("indefinite article"),

		DEFINITE_ARTICLE("definite article"),

		ADJECTIVE("adjective"),

		IDIOMS("idioms"),

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

	private final static Map<String, WordType> MAP_WORD_TYPE;

	static
	{
		MAP_WORD_TYPE = new HashMap<String, WordType>();
		for (WordType internalWordType : WordType.values())
		{
			MAP_WORD_TYPE.put(internalWordType.toString().toLowerCase(), internalWordType);
		}
	}

	public abstract String getWordType();

	public abstract String getDefinition();

	@Nullable
	public abstract String getExample();

	public static WordSense create(String type, String definition, String example)
	{
		return new AutoValue_WordSense(toWordType(type.toLowerCase()).toString(), definition, example);
	}

	public static WordType toWordType(String wordType)
	{
		return MAP_WORD_TYPE.containsKey(wordType) ? MAP_WORD_TYPE.get(wordType) : WordType.UNDEFINED;
	}
}
