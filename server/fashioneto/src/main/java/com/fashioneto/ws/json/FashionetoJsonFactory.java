package com.fashioneto.ws.json;

import java.util.Collection;
import java.util.List;

import com.fashioneto.persistence.Comment;
import com.fashioneto.persistence.Image;
import com.fashioneto.persistence.User;
import com.fashioneto.ws.entities.DefaultSet;
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
		gBuilder.registerTypeAdapter(Image.class, new ImageJsonSerializer());
		gBuilder.registerTypeAdapter(DefaultSet.class, new DefaultSetJsonSerializer());
		return gBuilder;
	}

	private static Gson getGson()
	{
		GsonBuilder gBuilder = getGsonBuilder();
		if (PRETTY_PRINT)
		{
			gBuilder.setPrettyPrinting();
		}
		return gBuilder.create();
	}

	public static JsonElement getJsonElement(Collection<User> users)
	{
		GsonBuilder gBuilder = getGsonBuilder();
		
		//overrides the serializer for class User:
		gBuilder.registerTypeAdapter(User.class, new UserSimplifiedJsonSerializer());
		
		if (PRETTY_PRINT)
		{
			gBuilder.setPrettyPrinting();
		}
		Gson gson = gBuilder.create();
		return gson.toJsonTree(users);
	}	
	
	public static String getJson(Collection<User> users)
	{
		return getGson().toJson(getJsonElement(users));
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

	public static String getJson(DefaultSet<?> set)
	{
		Gson gson = getGson();
		return gson.toJson(set);
	}

	public static String getJson(JsonElement jsonElement)
	{
		Gson gson = getGson();
		return gson.toJson(jsonElement);
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
