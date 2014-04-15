package com.fashioneto.ws.json;

import com.fashioneto.persistence.Comment;
import com.fashioneto.persistence.CommentSet;
import com.fashioneto.persistence.User;
import com.fashioneto.ws.entities.ResponseWrapper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;

/**
 * @author Felipe Tonon 6 Feb 2014
 **/
public class FashionetoJsonFactory
{

	private static final boolean PRETTY_PRINT = true;

	private static GsonBuilder getGsonBuilder()
	{
		GsonBuilder gBuilder = new GsonBuilder();
		gBuilder.registerTypeAdapter(Comment.class, new CommentJsonSerializer());
		gBuilder.registerTypeAdapter(User.class, new UserJsonSerializer());
		gBuilder.registerTypeAdapter(CommentSet.class, new CommentSetJsonSerializer());
		return gBuilder;
	}

	/**
	 * @TODO: REFACTOR THIS CLASS!!!!!!
	 */

	private static Gson getGson()
	{
		GsonBuilder gBuilder = getGsonBuilder();
		if (PRETTY_PRINT)
		{
			gBuilder.setPrettyPrinting();
		}
		return gBuilder.create();
	}

	public static String getJson(User user)
	{
		Gson gson = getGson();
		return gson.toJson(user);
	}

	public static JsonElement getJsonElement(User user)
	{
		Gson gson = getGson();
		return gson.toJsonTree(user);
	}

	public static String getJson(Comment comment)
	{
		Gson gson = getGson();
		return gson.toJson(comment);
	}

	public static String getJson(CommentSet comments)
	{
		Gson gson = getGson();
		return gson.toJson(comments);
	}

	public static String getJson(JsonElement jsonElement)
	{
		Gson gson = getGson();
		return gson.toJson(jsonElement);
	}

	public static String getJson(ResponseWrapper rw)
	{
		Gson gson = getGson();
		return gson.toJson(rw);
	}

	/**
	 * @param object
	 * @return
	 */
	public static String getJsonFromObject(Object object)
	{
		Gson gson = getGson();
		return gson.toJson(object);
	}
}
