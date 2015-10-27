package org.dictionary.concept;

import org.molgenis.gson.AutoGson;

import com.google.auto.value.AutoValue;

@AutoValue
@AutoGson(autoValueClass = AutoValue_Pronunciation.class)
public abstract class Pronunciation
{
	public abstract String getMp3Source();

	public abstract String getOggSource();

	public static Pronunciation create(String mp3Source, String oggSource)
	{
		return new AutoValue_Pronunciation(mp3Source, oggSource);
	}
}
