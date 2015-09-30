package org.dictionary.controller;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import java.util.Collections;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/dictionary")
public class DictionaryController
{
	@Autowired
	public DictionaryController()
	{

	}

	@RequestMapping(method = RequestMethod.GET)
	public String defaultView(Model model)
	{
		return null;
	}

	@RequestMapping(value = "/word", method = RequestMethod.GET, produces = APPLICATION_JSON_VALUE)
	@ResponseBody
	public Map<String, Object> recovery(@RequestBody Map<String, Object> request)
	{
		return Collections.emptyMap();
	}
}