package com.fashioneto.ws.rest;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fashioneto.persistence.Comment;
import com.fashioneto.persistence.CommentParentTypeEnum;
import com.fashioneto.persistence.User;
import com.fashioneto.service.CommentService;
import com.fashioneto.service.UserService;
import com.fashioneto.utils.ContextUtils;
import com.fashioneto.utils.NoUserInContextException;
import com.fashioneto.ws.entities.ContentWrapper;
import com.fashioneto.ws.entities.LikesWrapper;
import com.fashioneto.ws.json.FashionetoJsonFactory;

/**
 * @author Felipe Tonon 13 Mar 2014
 **/
@Path("/comment")
@Component
public class CommentRestBean
{

	@Autowired
	protected UserService userService;
	@Autowired
	protected CommentService commentService;

	@DELETE
	@Path("{commentId}")
	public Response delete(@PathParam("commentId")
	int commentId)
	{
		Comment comment = commentService.getComment(commentId);

		if (comment != null)
		{
			try
			{
				User loggedUser = ContextUtils.getUserFromAuthenticationContext();
				if (loggedUser.getId() == comment.getUser().getId())
				{
					comment = commentService.deleteComment(commentId);
					return Response.status(Status.OK).entity(FashionetoJsonFactory.getJson(comment)).build();
				}
			}
			catch (NoUserInContextException e)
			{
				e.printStackTrace();
			}
			return Response.status(Status.UNAUTHORIZED).build();
		}
		return Response.status(Status.NOT_FOUND).build();
	}

	@POST
	@Path("{parentType}/{parentId}")
	@Consumes("application/json")
	public Response addComment(@PathParam("parentType")
	CommentParentTypeEnum parentType, @PathParam("parentId")
	int parentId, ContentWrapper content) throws NoUserInContextException
	{
		Comment comment = commentService.addComment(parentType, parentId, content.getContent());
		if (comment != null)
		{
			return Response.status(Status.OK).entity(FashionetoJsonFactory.getJson(comment)).build();
		}
		return Response.status(Status.NOT_FOUND).build();
	}

	@POST
	@Path("like/{commentId}")
	public Response likeComment(@PathParam("commentId")
	int commentId) throws Exception
	{
		//http://localhost:8080/Fashioneto/as/comment/like/1 
		if (commentId > 0)
		{
			User user = ContextUtils.getUserFromAuthenticationContext();

			int likesCount = commentService.addLike(user.getId(), commentId);

			LikesWrapper likesWrapper = new LikesWrapper(likesCount, true);
			return Response.status(Status.OK).entity(FashionetoJsonFactory.getJsonFromObject(likesWrapper)).build();
		}

		return Response.status(Status.NOT_FOUND).build();
	}

	@GET
	@Path("{parentType}/{parentId}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getMsg(@PathParam("parentType")
	CommentParentTypeEnum parentType, @PathParam("parentId")
	int parentId)
	{
		//http://localhost:8080/Fashioneto/as/comment/USER/1

		User user = userService.getUser(parentId);
		if (parentType.equals(CommentParentTypeEnum.USER) && user != null)
		{

			user.getReceivedComments();

			String jsonOutput = FashionetoJsonFactory.getJson(user.getReceivedCommentsCommentSet());

			return Response.status(Status.OK).entity(jsonOutput).build();
		}
		return Response.status(Status.NOT_FOUND).build();
	}
}
