define(function(require){

	var

	$              = require("jquery"),
	Backbone       = require("backbone"),
	Handlebars     = require("handlebars"),

	Follow         = require("views/follow/follow"),
	template       = require("text!templates/people/person.hbr"),
	MasterBaseView = require( 'views/masterbaseview' );


	return MasterBaseView.extend({

		template: Handlebars.compile( template ),

		className: "people",

		init: function(){
			this.follow = new Follow({
				isFollowed: this.model.get( "isFollowed" ),
				id: this.model.get( "id" )
			});
		},

		postRender: function(){
			this.$el.find( '.followContainer' )
			.html( this.follow.render().el );
		}

	});

});