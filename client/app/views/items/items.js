define(function(require){

	var
	Backbone       = require("backbone"),
	Handlebars     = require("handlebars"),
	$              = require("jquery"),
	
	Collection     = require("collections/items/items"),
	Item           = require("views/item/item"),
	MasterBaseView = require( 'views/masterbaseview' );

	return MasterBaseView.extend({

		init: function(){
			this.collection = new Collection([
				{
					id: 1,
					userName: "user1",
					displayName: "Felipe Tonon",
					location: "Rio, BR",
					imgUrl: "img/pictures-thumbnail.jpg"
				},
				{
					id: 2,
					userName: "user2",
					displayName: "Caspar S-S",
					location: "London, UK",
					imgUrl: "img/pictures-thumbnail.jpg"
				},
				{
					id: 3,
					userName: "user3",
					displayName: "Ondrej Winter",
					location: "Prague, CZ",
					imgUrl: "img/pictures-thumbnail.jpg"
				},

				{
					id: 4,
					userName: "user4",
					displayName: "Adam Amran",
					location: "Prague, CZ",
					imgUrl: "img/pictures-thumbnail.jpg"
				}
			]);
		},

		render: function(){
			this.$el.empty();
			this.collection.each( this.renderPerson, this );
			return this;
		},

		renderPerson: function( person ){
			var personView = new Person( { model: person } );
			this.$el.append( personView.render().el );
		}

	});

});