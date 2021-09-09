package com.dataBaseManager;

import org.json.JSONObject;
import org.json.XML;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jsonOperation.JsonOperation;


public class test {
	 public static void main(String[] args) throws JsonMappingException, JsonProcessingException {
		 String str = "{\"name\": \" \",\n"
				 + " \"label\": \" \",\n"
				 + " \"type\": \" \",\n"
				 + " \"value\": \" \"\n }";
//		 ObjectMapper mapper = new ObjectMapper();
//		 System.out.println(str);
//		 JsonNode node = mapper.readTree(str);
//		 String string = JsonOperation.convertJsonToString(node);
//		 System.out.println(string);
//		 String xml = XML.toString(node);
		 JSONObject jsonObject= new JSONObject(str);
		 String xml = XML.toString(jsonObject);
		 System.out.println(xml);


		 


	}
}
