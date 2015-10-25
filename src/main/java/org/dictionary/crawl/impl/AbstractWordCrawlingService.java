package org.dictionary.crawl.impl;

import java.io.IOException;
import java.util.Objects;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.dictionary.crawl.WordCrawlingService;
import org.slf4j.Logger;

public abstract class AbstractWordCrawlingService implements WordCrawlingService
{
	private final String pageBaseUrl;
	private final Logger Logger;

	public AbstractWordCrawlingService(String pageBaseUrl, Logger Logger)
	{
		this.pageBaseUrl = Objects.requireNonNull(pageBaseUrl);
		this.Logger = Objects.requireNonNull(Logger);
	}

	@Override
	public String getPage(String wordToLookup)
	{
		String url = pageBaseUrl + wordToLookup;
		String responseString = StringUtils.EMPTY;

		for (int i = 0; i < 50; i++)
		{
			if (StringUtils.isNotEmpty(responseString)) break;

			if (i > 0)
			{
				try
				{
					Thread.sleep(1000);
				}
				catch (InterruptedException e)
				{
					Logger.error(e.getMessage());
				}
			}

			try
			{
				HttpClient httpClient = HttpClientBuilder.create().build();
				HttpGet httpGet = new HttpGet(url);
				HttpResponse httpResponse = httpClient.execute(httpGet);
				responseString = EntityUtils.toString(httpResponse.getEntity(), "UTF-8");
			}
			catch (IOException e)
			{
				Logger.error("Failed to retrieve the response for request " + url + "\n" + e.getMessage());
			}
		}
		return responseString;
	}
}
